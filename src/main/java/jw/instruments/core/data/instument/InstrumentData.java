package jw.instruments.core.data.instument;

import lombok.Data;

import java.util.Map;

@Data
public class InstrumentData
{
    private int customModelId;

    private String name;

    private String type;

    private Map<Integer,String> crafting;

    private String[] chords;

    private int volume;
}
