package jw.instruments.spigot;

import jw.fluent.api.spigot.documentation.api.DocumentationDecorator;
import jw.fluent.api.spigot.documentation.api.models.Documentation;

public class PluginDocumentation extends DocumentationDecorator {
    @Override
    public void decorate(Documentation documentation) {

        addImage("https://raw.githubusercontent.com/jwdeveloper/JW_Instruments/master/resources/banner_plugin.jpg", documentation);

        var builder = createStringBuilder();
        builder.newLine()
                .text("If you are looking a plugin to enrich server experience this is the solution. ")
                .text("Plugin adds bunch of instruments each of them is playable with all possible chords you can imagine. ")
                .text("Therefore consider to use this plugin especially on RolePlay server")
                .newLine();

        addText(builder.build(), documentation);

        addText("Select chord from over 100", documentation);
        addText("https://raw.githubusercontent.com/jwdeveloper/JW_Instruments/master/resources/chords.gif", documentation);

        addText("Plugin showcase", documentation);
        addVideo("https://www.youtube.com/watch?v=F4iKXAMIioo&ab_channel=JW", documentation);
    }
}
