package jw.guitar.core.data;

public class PluginPermissions {
    public final static String BASE = "guitar";

    private final static String COMMANDS = BASE + ".commands";


    public final static String GET_CMD = COMMANDS + ".get";

    public final static String GIVE_CMD = COMMANDS + ".give";

    public final static String SONGS_CMD = COMMANDS + ".songs";


    private final static String GUI = BASE + ".gui";

    public final static String SONGS_GUI = GUI + ".songs";

    public final static String SONGS_INSERT = SONGS_GUI + ".insert";

    public final static String SONGS_EDIT = SONGS_GUI + ".edit";

    public final static String SONGS_DELETE = SONGS_GUI + ".delete";

    public final static String SONGS_COPY = SONGS_GUI + ".copy";


    public final static String CRAFTING = BASE + ".crafting";
}
