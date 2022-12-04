package jw.instruments.spigot;

import jw.fluent.api.spigot.documentation.api.DocumentationDecorator;
import jw.fluent.api.spigot.documentation.api.models.Documentation;
import jw.fluent.api.spigot.messages.message.MessageBuilder;
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

        addImage("https://raw.githubusercontent.com/jwdeveloper/JW_Instruments/master/resources/banner_plugin.jpg", documentation);
        addText(banner(),documentation);

        var builder = createStringBuilder();
        builder.newLine()
                .text("If you are looking a plugin to enrich server experience this is the solution. ")
                .text("Plugin adds bunch of instruments each of them is playable with all possible chords you can imagine. ")
                .text("Therefore consider to use this plugin especially on RolePlay server")
                .newLine();

        addText(builder.build(), documentation, "plugin-ignore");

        addText("Plugin tutorial: https://www.youtube.com/watch?v=F4iKXAMIioo&ab_channel=JW", documentation, "spigot-ignore");
        addVideo("https://www.youtube.com/watch?v=F4iKXAMIioo&ab_channel=JW", documentation);


        addImage("https://raw.githubusercontent.com/jwdeveloper/JW_Instruments/master/resources/guitars.gif", documentation);
        addImage("https://raw.githubusercontent.com/jwdeveloper/JW_Instruments/master/resources/chords.gif", documentation);
        addImage("https://raw.githubusercontent.com/jwdeveloper/JW_Instruments/master/resources/chordchange.gif", documentation);
    }


    public String banner() {
        var builder = createStringBuilder();
        bannerElement(builder, "https://raw.githubusercontent.com/jwdeveloper/SpigotFluentAPI/master/resources/social/discord.png", Consts.DISCORD_URL);
        bannerElement(builder, "https://raw.githubusercontent.com/jwdeveloper/SpigotFluentAPI/master/resources/social/github.png", Consts.GITHUB_URL);
        bannerElement(builder, "https://raw.githubusercontent.com/jwdeveloper/SpigotFluentAPI/master/resources/social/spigot.png", Consts.SPIGOT_URL);
        return builder.build();
    }

    public void bannerElement(MessageBuilder builder, String image, String link) {
        builder.text(" [URL='" + link + "']");
        builder.text("[IMG]").text(image).text("[/IMG]");
        builder.text("[/URL]");
    }


}
