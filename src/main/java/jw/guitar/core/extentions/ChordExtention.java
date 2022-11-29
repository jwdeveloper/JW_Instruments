package jw.guitar.core.extentions;

import jw.fluent_plugin.api.FluentApiBuilder;
import jw.fluent_plugin.api.FluentApiExtention;
import jw.fluent_plugin.implementation.FluentApi;
import jw.fluent_plugin.implementation.modules.logger.FluentLogger;
import jw.guitar.core.data.chords.Chord;
import jw.guitar.core.builders.chord.ChordBuilder;
import jw.guitar.core.data.Consts;
import jw.guitar.core.services.ChordService;
import jw.fluent_api.utilites.files.json.JsonUtility;
import jw.fluent_api.utilites.java.JavaUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class ChordExtention implements FluentApiExtention {



    @Override
    public void onConfiguration(FluentApiBuilder builder) {

    }

    @Override
    public void onFluentApiEnable(FluentApi fluentAPI) {
        var chordsService =  FluentApi.injection().findInjection(ChordService.class);
        var chords = loadChords();
        FluentLogger.LOGGER.log("Chords",chords.size(), chordsService.toString());
        chordsService.add(chords);
    }

    @Override
    public void onFluentApiDisabled(FluentApi fluentAPI) {

    }


    private List<Chord> loadChords() {
        var file = FluentApi.plugin().getResource("chords_output.json");
        var chordsDto = JsonUtility.loadList(file, ChordDto.class);
        return chordsDto.stream().map(this::mapChord).toList();
    }

    private Chord mapChord(ChordDto chordDto) {
        var builder = ChordBuilder.create(chordDto.getKey(), chordDto.getSuffix(), chordDto.getFret());
        for(var i = 0; i< Consts.STRINGS; i++)
        {
            var finger = chordDto.getFrets().get(i);
            var stringId = Consts.STRINGS -1-i;
            if(chordDto.getKey().equals("E") && chordDto.getSuffix().equals("minor"))
            {
                FluentApi.logger().log("finger",finger,"StringID",stringId);
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



    @Data
    public class ChordDto
    {
        private Integer fret = 0;
        private String suffix = JavaUtils.EMPTY_STRING;
        private String key = JavaUtils.EMPTY_STRING;
        private ArrayList<Integer> frets = new ArrayList<>();
    }
}
