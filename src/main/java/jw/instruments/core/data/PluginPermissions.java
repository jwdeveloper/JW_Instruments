package jw.instruments.core.data;

import jw.fluent_api.spigot.permissions.api.annotations.PermissionGroup;
import jw.fluent_api.spigot.permissions.api.annotations.PermissionProperty;
import jw.fluent_api.spigot.permissions.api.annotations.PermissionTitle;

public class PluginPermissions {

    public final static String PLUGIN = "instrument";
    private final static String COMMAND = PLUGIN + ".commands";
    private final static String GUI = PLUGIN + ".gui";


    @PermissionGroup(group = "plugin")
    @PermissionProperty(description = "Allows player to play the instrument")
    public final static String PLAY = PLUGIN + ".play";

    @PermissionGroup(group = "plugin")
    @PermissionProperty(description = "Unlimited amount of songs player can create it also includes [song export]")
    public final static String SONGS_INSERT_NO_LIMIT = PLUGIN + ".song.no-limit";




    @PermissionGroup(group = "commands")
    @PermissionProperty(description = "/instrument (opens instrument gui)")
    public final static String INSTRUMENT_CMD = COMMAND + ".instrument";

    @PermissionGroup(group = "commands")
    @PermissionProperty(description = "/instrument get (pick your instrument)")
    public final static String GET_CMD = COMMAND + ".get";

    @PermissionGroup(group = "commands")
    @PermissionProperty(description = "/instrument songs (opens songs gui)")
    public final static String SONGS_CMD = COMMAND + ".songs";





    @PermissionTitle(title = "gui [Instrument]")
    @PermissionGroup(group = "gui")
    @PermissionProperty(description = "Instrument gui")
    public final static String INSTRUMENT_GUI = GUI + ".instrument";

    @PermissionGroup(group = "gui")
    @PermissionProperty(description = "Change volume button")
    public final static String INSTRUMENT_GUI_VOLUME = INSTRUMENT_GUI + ".volume";

    @PermissionGroup(group = "gui")
    @PermissionProperty(description = "Change rhythm on shift press ON/OFF button")
    public final static String INSTRUMENT_GUI_RHYTHM_CHANGE = INSTRUMENT_GUI + ".rhythm.change";

    @PermissionGroup(group = "gui")
    @PermissionProperty(description = "Display chords above inventory bar ON/OFF button")
    public final static String INSTRUMENT_GUI_DISPLAY_CHORDS = INSTRUMENT_GUI + ".chords.display";

    @PermissionGroup(group = "gui")
    @PermissionProperty(description = "Importing chords from a song")
    public final static String INSTRUMENT_GUI_IMPORT_SONG = INSTRUMENT_GUI + ".song.import";

    @PermissionGroup(group = "gui")
    @PermissionProperty(description = "Exporting chords to new song")
    public final static String INSTRUMENT_GUI_EXPORT_SONG = INSTRUMENT_GUI + ".song.export";

    @PermissionGroup(group = "gui")
    @PermissionProperty(description = "Enable Editing chords in instrument GUI")
    public final static String INSTRUMENT_GUI_CHORDS = INSTRUMENT_GUI + ".chords";

    @PermissionTitle(title = "gui [Songs]")
    @PermissionGroup(group = "gui")
    @PermissionProperty(description = "Songs GUI")
    public final static String SONGS_GUI = GUI + ".songs";

    @PermissionGroup(group = "gui")
    @PermissionProperty(description = "Enable to insert new song")
    public final static String SONGS_INSERT = SONGS_GUI + ".insert";

    @PermissionGroup(group = "gui")
    @PermissionProperty(description = "Enable to edit song")
    public final static String SONGS_EDIT = SONGS_GUI + ".edit";

    @PermissionGroup(group = "gui")
    @PermissionProperty(description = "Enable to delete song")
    public final static String SONGS_DELETE = SONGS_GUI + ".delete";
}
