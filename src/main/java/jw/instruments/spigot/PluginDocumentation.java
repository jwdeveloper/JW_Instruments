package jw.instruments.spigot;

import jw.fluent.api.spigot.documentation.api.DocumentationDecorator;
import jw.fluent.api.spigot.documentation.api.models.Documentation;

public class PluginDocumentation extends DocumentationDecorator
{
    @Override
    public void decorate(Documentation documentation) {

        addImage("https://raw.githubusercontent.com/jwdeveloper/JW_Instruments/master/resources/plugin_banner.jpg",documentation);
        addText("Tutorial",documentation);
        addVideo("https://www.youtube.com/watch?v=F4iKXAMIioo&ab_channel=JW",documentation);
    }
}
