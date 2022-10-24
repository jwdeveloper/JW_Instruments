package jw.guitar.plugin_setup;

import jw.fluent_plugin.api.PluginAction;
import jw.fluent_plugin.implementation.FluentAPI;
import jw.guitar.chords.Chord;
import jw.guitar.builders.chord.ChordBuilder;
import jw.guitar.data.Consts;
import jw.guitar.services.ChordService;
import jw.fluent_api.logger.OldLogger;
import jw.fluent_plugin.implementation.FluentPlugin;
import jw.fluent_plugin.api.options.PipelineOptions;
import jw.fluent_api.utilites.files.json.JsonUtility;
import jw.fluent_api.utilites.java.JavaUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class ChordSetup implements PluginAction {


    @Override
    public void pluginEnable(PipelineOptions options) throws Exception {

        var chordsService =  FluentAPI.injection().findInjection(ChordService.class);
        var chords = loadChords();
        chordsService.add(chords);
    }


    public List<Chord> loadChords() {
        var file = FluentPlugin.getPlugin().getResource("chords_output.json");
        var chordsDto = JsonUtility.loadList(file, ChordDto.class);
        return chordsDto.stream().map(this::mapChord).toList();
    }

    public Chord mapChord(ChordDto chordDto) {
        var builder = ChordBuilder.create(chordDto.getKey(), chordDto.getSuffix(), chordDto.getFret());
        for(var i = 0; i< Consts.STRINGS; i++)
        {
            var finger = chordDto.getFrets().get(i);
            var stringId = Consts.STRINGS -1-i;
            if(chordDto.getKey().equals("E") && chordDto.getSuffix().equals("minor"))
            {
                OldLogger.log("finger",finger,"StringID",stringId);
            }
            if(finger == 0)
            {
                builder.addNote(finger, stringId,0.4f);
            }
            else
            {
                builder.addNote(finger, stringId);
            }

        }
        return builder.build();
    }


    @Override
    public void pluginDisable(FluentPlugin fluentPlugin) throws Exception {

    }

    @Data
    public class ChordDto
    {
        private Integer fret = 0;
        private String suffix = JavaUtils.EMPTY_STRING;
        private String key = JavaUtils.EMPTY_STRING;
        private ArrayList<Integer> frets = new ArrayList<>();
    }
}
