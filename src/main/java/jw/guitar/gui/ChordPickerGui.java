package jw.guitar.gui;

import jw.guitar.chords.Chord;
import jw.guitar.gameobjects.instuments.Instrument;
import jw.guitar.rhythms.Rhythm;
import jw.guitar.rhythms.events.PlayingStyleEvent;
import jw.guitar.services.ChordService;
import jw.guitar.services.InstrumentService;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.api.enums.LifeTime;
import jw.spigot_fluent_api.desing_patterns.observer.ObserverBag;
import jw.spigot_fluent_api.fluent_gui.button.button_observer.ButtonObserverUI;
import jw.spigot_fluent_api.fluent_gui.events.ButtonUIEvent;
import jw.spigot_fluent_api.fluent_gui.implementation.list_ui.ListUI;
import jw.spigot_fluent_api.fluent_message.FluentMessage;
import org.bukkit.Material;

import java.util.List;

@Injection(lifeTime = LifeTime.TRANSIENT)
public class ChordPickerGui extends ListUI<Chord> {
    private final ChordService chordService;
    private final InstrumentService guitarService;

    private ObserverBag<Integer> currentSound;
    private ObserverBag<Integer> currentRythm;
    private List<Rhythm> rhythms;
    private ButtonUIEvent rightClick;

    private static String[] instruction;
    static
    {
       instruction = FluentMessage
               .message()
               .field("Left click ", "Select chord").newLine()
               .field("Right click","Play sound").newLine()
               .field("Shift click", "Draw chord").newLine()
               .toArray();
    }


    @Inject
    public ChordPickerGui(ChordService chordService,
                          InstrumentService guitarService,
                          List<Rhythm> rythms) {
        super("Chords", 6);
        this.chordService = chordService;
        this.guitarService = guitarService;
        this.rhythms = rythms;
        this.rightClick = (a, b) -> {
        };

        currentSound = new ObserverBag<>(0);
        currentRythm = new ObserverBag<>(0);
    }

    public void onContentClickDynamic(ButtonUIEvent event) {
        rightClick = event;
    }

    @Override
    protected void onInitialize() {
        setContentButtons(chordService.get(), (chord, button) ->
        {
            button.setTitlePrimary(chord.fullName());
            button.setCustomMaterial(chord.getItemStack().getType(), chord.getCustomId());
            button.setDataContext(chord);
            button.setDescription(instruction);
            button.setOnRightClick((player, b) ->
            {
                var chord_ = button.<Chord>getDataContext();
                var sound = guitarService.getNames().get(currentSound.get());
                var rythm = rhythms.get(currentRythm.get());
                rythm.play(new PlayingStyleEvent(player, chord_, true, sound));
            });
            button.setOnShiftClick((player, b) ->
            {
                b.addDescription(chordService.getDisplay(chord));
                refreshButton(b);
            });
        });
        onContentClick((player, button) ->
        {
            rightClick.execute(player, button);
        });

        ButtonObserverUI.factory()
                .listSelectorObserver(currentSound.getObserver(), guitarService.getInstruments(), Instrument::getName,
                        onSelectEvent ->
                        {
                            onSelectEvent.buttonUI().setCustomMaterial(
                                    onSelectEvent.data().getCustomModel().getMaterial(),
                                    onSelectEvent.data().getCustomModel().getCustomModelId());
                        })
                .setMaterial(Material.GLASS)
                .setLocation(0, 3)
                .setTitlePrimary("Select sound")
                .buildAndAdd(this);

        ButtonObserverUI.factory()
                .listSelectorObserver(currentRythm.getObserver(), rhythms.stream().toList(), Rhythm::getName)
                .setMaterial(Material.MUSIC_DISC_CAT)
                .setLocation(0, 4)
                .setTitle("Select rythm")
                .buildAndAdd(this);

        addSearchStrategy("By name ", event -> event.data().fullName().contains(event.searchKey()));
        addSearchStrategy("By note key", event -> event.data().key().contains(event.searchKey()));
        addSearchStrategy("By suffix", event -> event.data().suffix().contains(event.searchKey()));

    }
}
