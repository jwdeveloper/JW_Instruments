package jw.instruments.spigot;

import jw.fluent.api.spigot.documentation.api.DocumentationDecorator;
import jw.fluent.api.spigot.documentation.api.models.Documentation;
import jw.fluent.api.utilites.java.StringUtils;
import jw.instruments.core.data.Consts;

public class PluginDocumentation extends DocumentationDecorator {
    @Override
    public void decorate(Documentation documentation) {


        addText("Thank you for using JW Instruments don't forget to leave review ", documentation, "spigot-ignore", "github-ignore");
        addText("Those links might be helpful in case you need any help", documentation, "spigot-ignore", "github-ignore");
        addText(StringUtils.EMPTY_STRING, documentation, "spigot-ignore", "github-ignore");
        addText("  Spigot: " + Consts.SPIGOT_URL, documentation, "spigot-ignore", "github-ignore");
        addText("  Discord: " + Consts.DISCORD_URL, documentation, "spigot-ignore", "github-ignore");
        addText("  Github: " + Consts.GITHUB_URL, documentation, "spigot-ignore", "github-ignore");
        addText(StringUtils.EMPTY_STRING, documentation, "spigot-ignore", "github-ignore");
        addText("!Be aware this is only documentation, changes in this file has not impact on plugin", documentation, "spigot-ignore", "github-ignore");

        addImage("https://raw.githubusercontent.com/jwdeveloper/JW_Instruments/master/resources/plugin_baner.jpg", documentation);

        addText("<p align=\"center\">",documentation,"spigot-ignore", "plugin-ignore");
        addText("[CENTER]", documentation, "github-ignore", "plugin-ignore");
        addImageWithLink("https://raw.githubusercontent.com/jwdeveloper/SpigotFluentAPI/master/resources/social-media/discord.png", Consts.DISCORD_URL, documentation);
        addImageWithLink( "https://raw.githubusercontent.com/jwdeveloper/SpigotFluentAPI/master/resources/social-media/github.png", Consts.GITHUB_URL, documentation);
        addImageWithLink("https://raw.githubusercontent.com/jwdeveloper/SpigotFluentAPI/master/resources/social-media/spigot.png", Consts.SPIGOT_URL, documentation);
        addText("[/CENTER]", documentation, "github-ignore", "plugin-ignore");
        addText("</p>",documentation,"spigot-ignore", "plugin-ignore");

        var builder = createStringBuilder();
        builder.newLine()
                .text("If you are looking a plugin to enrich server experience this is the solution. ")
                .text("Plugin adds bunch of instruments each of them is playable with all possible chords you can imagine. ")
                .text("Therefore consider to use this plugin especially on RolePlay server")
                .newLine();

        addText("",documentation);
        addText("Get instrument by:",documentation);
        addText("/instrument get <instrument-name>",documentation,"bold");
        addText("",documentation);
        addText("",documentation);
        addText("Open instrument menu (instrument need to be held in left hand):",documentation);
        addText("/instrument",documentation,"bold");
        addText("",documentation);

        addText(builder.build(), documentation, "plugin-ignore");

        addText("Plugin tutorial: https://www.youtube.com/watch?v=5X_T7s8Z6YQ&t=1s&ab_channel=JW", documentation, "spigot-ignore");
        addVideo("https://www.youtube.com/watch?v=5X_T7s8Z6YQ&t=1s&ab_channel=JW", documentation);


        addImage("https://raw.githubusercontent.com/jwdeveloper/JW_Instruments/master/resources/guitars.gif", documentation);
        addImage("https://raw.githubusercontent.com/jwdeveloper/JW_Instruments/master/resources/chords.gif", documentation);
        addImage("https://raw.githubusercontent.com/jwdeveloper/JW_Instruments/master/resources/chordchange.gif", documentation);
    }
}
