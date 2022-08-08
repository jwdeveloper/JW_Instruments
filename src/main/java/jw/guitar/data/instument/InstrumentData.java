package jw.guitar.data.instument;

import jw.spigot_fluent_api.fluent_gameobjects.api.models.CustomModel;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class InstrumentData
{
    private Map<Integer, String> chords = new HashMap<>();

    private String sound;

    private CustomModel customModel;
}
