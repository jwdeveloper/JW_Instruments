package jw.guitar;

import jw.guitar.chords.Chord;
import jw.guitar.chords.ChordBuilder;
import jw.guitar.services.ChordService;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.FluentInjection;
import jw.spigot_fluent_api.fluent_plugin.FluentPlugin;
import jw.spigot_fluent_api.fluent_plugin.starup_actions.pipeline.PluginPipeline;
import jw.spigot_fluent_api.fluent_plugin.starup_actions.pipeline.data.PipelineOptions;
import jw.spigot_fluent_api.utilites.files.json.JsonUtility;
import jw.spigot_fluent_api.utilites.java.JavaUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class ChordSetup implements PluginPipeline {


    @Override
    public void pluginEnable(PipelineOptions options) throws Exception {

        var chordsService = FluentInjection.getInjection(ChordService.class);
        var chords = loadChrods();
        chordsService.add(chords);
    }


    public List<Chord> loadChrods() {
        var file = FluentPlugin.getPlugin().getResource("chords_output.json");
        var chordsdto = JsonUtility.loadList(file, ChordDto.class);
        return chordsdto.stream().map(this::mapChord).toList();
    }

    public Chord mapChord(ChordDto chordDto) {
        var builder = ChordBuilder.create(chordDto.getKey(), chordDto.getSuffix());
        for(var i =0;i<6;i++)
        {
            var finger = chordDto.getFrets().get(i);
            if(finger == 0)
            {
                builder.addNote(finger, 5-i,0.4f);
            }
            else
            {
                builder.addNote(finger, 5-i);
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
