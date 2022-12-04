package jw.instruments.spigot.gui;

import jw.fluent.api.player_context.api.PlayerContext;
import jw.fluent.api.spigot.messages.FluentMessage;
import jw.fluent.plugin.implementation.modules.translator.FluentTranslator;
import jw.instruments.core.data.chords.Chord;
import jw.instruments.core.instuments.Instrument;
import jw.instruments.core.rhythms.Rhythm;
import jw.instruments.core.rhythms.events.PlayingStyleEvent;
import jw.instruments.core.services.ChordService;
import jw.instruments.core.services.InstrumentService;
import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent.api.desing_patterns.dependecy_injection.api.enums.LifeTime;
import jw.fluent.api.desing_patterns.observer.implementation.ObserverBag;
import jw.fluent.api.spigot.inventory_gui.button.button_observer.ButtonObserverUI;
import jw.fluent.api.spigot.inventory_gui.events.ButtonUIEvent;
import jw.fluent.api.spigot.inventory_gui.implementation.list_ui.ListUI;
import org.bukkit.Material;

import java.util.List;

@PlayerContext
@Injection(lifeTime = LifeTime.TRANSIENT)
public class ChordPickerGui extends ListUI<Chord> {
    private final ChordService chordService;
    private final InstrumentService guitarService;
    private ObserverBag<Integer> currentSound;
    private ObserverBag<Integer> currentRythm;
    private List<Rhythm> rhythms;
    private ButtonUIEvent rightClick;
    private FluentTranslator lang;

    @Inject
    public ChordPickerGui(ChordService chordService,
                          InstrumentService guitarService,
                          List<Rhythm> rhythms,
                          FluentTranslator translator) {
        super("Chords", 6);
        this.chordService = chordService;
        this.guitarService = guitarService;
        this.rhythms = rhythms;
        this.lang = translator;
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
        setContentButtons(chordService.find(), (chord, button) ->
        {
            button.setTitlePrimary(chord.fullName());
            button.setCustomMaterial(chord.getItemStack().getType(), chord.getCustomId());
            button.setDataContext(chord);
            button.setDescription(FluentMessage
                    .message()
                    .field(lang.get("gui.base.left-click"),lang.get("gui.chords.chord.left-click")).newLine()
                    .field(lang.get("gui.base.right-click"),lang.get("gui.chords.chord.right-click")).newLine()
                    .field(lang.get("gui.base.shift-click"),lang.get("gui.chords.chord.shift-click")).newLine()
                    .toArray());
            button.setOnRightClick((player, b) ->
            {
                var chord_ = button.<Chord>getDataContext();
                var sound = guitarService.getNames().get(currentSound.get());
                var rhythm = rhythms.get(currentRythm.get());
                rhythm.play(new PlayingStyleEvent(player, chord_, true, sound,1));
            });
            button.setOnShiftClick((player, b) ->
            {
                b.addDescription(chordService.getDisplay(chord));
                refreshButton(b);
            });
        });
        setListTitlePrimary(lang.get("gui.chords.title"));
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
                .setTitlePrimary(lang.get("gui.chords.select-instrument.title"))
                .buildAndAdd(this);

        ButtonObserverUI.factory()
                .listSelectorObserver(currentRythm.getObserver(), rhythms.stream().toList(), Rhythm::getName)
                .setMaterial(Material.MUSIC_DISC_CAT)
                .setLocation(0, 4)
                .setTitlePrimary(lang.get("gui.chords.select-rhythm.title"))
                .buildAndAdd(this);

        addSearchStrategy(lang.get("gui.chords.search.by-name"), event -> event.data().fullName().contains(event.searchKey()));
        addSearchStrategy(lang.get("gui.chords.search.by-key"), event -> event.data().key().contains(event.searchKey()));
        addSearchStrategy(lang.get("gui.chords.search.by-suffix"), event -> event.data().suffix().contains(event.searchKey()));
    }
}
