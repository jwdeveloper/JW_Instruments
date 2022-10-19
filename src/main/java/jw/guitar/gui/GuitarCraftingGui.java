package jw.guitar.gui;

import jw.guitar.services.InstrumentService;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent_api.desing_patterns.dependecy_injection.api.enums.LifeTime;
import jw.fluent_api.minecraft.gui.implementation.crafting_ui.CraftingUI;
import jw.fluent_api.minecraft.tasks.FluentTaskTimer;
import org.bukkit.entity.Player;

@Injection(lifeTime = LifeTime.TRANSIENT)
public class GuitarCraftingGui extends CraftingUI
{
    private final InstrumentService instrumentService;
    private FluentTaskTimer changeReceptureTask;

    @Inject
    public GuitarCraftingGui(InstrumentService instrumentService)
    {
        super("Guitars");
        this.instrumentService = instrumentService;
    }

    @Override
    protected void onInitialize()
    {
        changeReceptureTask = new FluentTaskTimer(40,(time, task) ->
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
