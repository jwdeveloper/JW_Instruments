package jw.instruments.core.services;

import jw.fluent.api.spigot.events.FluentEvent;
import jw.fluent.api.utilites.seralizer.ItemStackSerializerProfile;
import jw.fluent.api.utilites.seralizer.ItemStackSerlializer;
import jw.fluent.plugin.implementation.FluentApi;
import jw.instruments.core.data.instrument.InstrumentData;
import jw.instruments.core.data.instrument.InstrumentItemStack;
import jw.instruments.core.instuments.Instrument;
import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Injection;
import lombok.SneakyThrows;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Injection
public class InstrumentItemStackFactory implements ItemStackSerializerProfile<InstrumentData>
{
    private ChordService chordService;
    private final String pluginVersion;
    private final ItemStackSerlializer stackSerlializer;
    private Map<ItemStack, InstrumentItemStack> registerdItems;

    @Inject
    public InstrumentItemStackFactory( ChordService chordService)
    {
        this.chordService = chordService;
        pluginVersion = FluentApi.plugin().getDescription().getVersion();
        stackSerlializer = new ItemStackSerlializer(this);
        registerdItems = new HashMap<>();

        FluentEvent.onEvent(PluginDisableEvent.class,this::onDisable);
    }

    @SneakyThrows
    public Optional<InstrumentItemStack> create(Instrument instrument)
    {
        var itemstack = instrument.getCustomModel().getItemStack();
        var data = new InstrumentData();
        data.setName(instrument.getName());
        data.setCustomModelId(instrument.getCustomModel().getCustomModelId());

        var chords = data.getChords();
        var names =  chordService.getDefaultChords().stream().map(c -> c.fullName()).toList();
        for(var i=0;i<chords.length;i++)
        {
            chords[i] = names.get(i);
        }

        stackSerlializer.serialize(itemstack, data);


        var instrumentItemStack = new InstrumentItemStack(itemstack, data);
        registerdItems.put(itemstack,instrumentItemStack);
        return Optional.of(instrumentItemStack);
    }

    @SneakyThrows
    public Optional<InstrumentItemStack> create(ItemStack itemStack)
    {
        if(!Instrument.isInstrument(itemStack))
        {
            return Optional.empty();
        }
        if(registerdItems.containsKey(itemStack))
        {
            return Optional.of(registerdItems.get(itemStack));
        }

        var deserialize = stackSerlializer.deserialize(itemStack, InstrumentData.class);
        var instumentItemstack =  new InstrumentItemStack(itemStack,deserialize);
        registerdItems.put(itemStack, instumentItemstack);
        return Optional.of(instumentItemstack);
    }


    @Override
    public void serialize(Map<String, Object> map, InstrumentData instance)
    {
        map.put("version",instance.getVersion());
        map.put("name",instance.getName());
        map.put("volume",instance.getVolume());
        map.put("chords",instance.getChords());
    }

    @Override
    public InstrumentData deserialize(Map<String, Object> map)
    {
        var instance = new InstrumentData();
        instance.setVersion((String)map.get("version"));
        instance.setName((String)map.get("name"));
        instance.setVolume((Integer) map.get("volume"));
        instance.setChords((String[]) map.get("chords"));
        return instance;
    }

    private void onDisable(PluginDisableEvent event)
    {
        if(!event.getPlugin().equals(FluentApi.plugin()))
        {
            return;
        }
        for(var registeredItem : registerdItems.values())
        {
            try
            {
                stackSerlializer.serialize(registeredItem.getItemStack(),registeredItem.getInstrumentData());
            }catch (Exception e)
            {
                FluentApi.logger().error("Unable to save itemstack data",e);
            }
        }
    }
}
