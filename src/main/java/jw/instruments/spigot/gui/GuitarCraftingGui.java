package jw.instruments.spigot.gui;

import jw.fluent.api.player_context.api.PlayerContext;
import jw.instruments.core.services.InstrumentService;
import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent.api.desing_patterns.dependecy_injection.api.enums.LifeTime;
import jw.fluent.api.spigot.inventory_gui.implementation.crafting_ui.CraftingUI;
import jw.fluent.api.spigot.tasks.SimpleTaskTimer;
import org.bukkit.entity.Player;

@PlayerContext
@Injection(lifeTime = LifeTime.SINGLETON)
public class GuitarCraftingGui extends CraftingUI
{
    private final InstrumentService instrumentService;
    private SimpleTaskTimer changeReceptureTask;

    @Inject
    public GuitarCraftingGui(InstrumentService instrumentService)
    {
        super("Guitars");
        this.instrumentService = instrumentService;
    }

    @Override
    protected void onInitialize()
    {
        changeReceptureTask = new SimpleTaskTimer(40,(time, task) ->
        {
            var inst = instrumentService.getInstruments();
            var ins = inst.get(time%inst.size());
            var rec = ins.getRecipe();
            var buttons = mapRecipe(rec);
            for(var i =0;i<getSlots();i++)
            {
                var btn  = buttons.get(i);
                btn.setTitlePrimary(btn.getTitle());
                btn.setLocation(0,i);
                addButton(btn);
            }
            refreshButtons();
        });
    }

    @Override
    protected void onOpen(Player player)
    {
        changeReceptureTask.run();
    }

    @Override
    protected void onClose(Player player)
    {
        changeReceptureTask.stop();
    }
}
