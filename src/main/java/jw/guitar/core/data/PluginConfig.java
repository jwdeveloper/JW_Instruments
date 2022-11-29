package jw.guitar.core.data;

import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent_plugin.implementation.config.config_sections.FluentConfigSection;
import jw.fluent_api.utilites.files.yml.api.annotations.YmlFile;
import jw.fluent_api.utilites.files.yml.api.annotations.YmlProperty;
import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

@Injection
@YmlFile
@Data
public class PluginConfig implements FluentConfigSection {

    @YmlProperty(path = "guitar",name = "enable-crafting")
    private boolean crating = true;


    @YmlProperty(path = "song",name = "songs-limit",
    description = "Determine how much songs player can create \n"+
    "It's not applied for players with \n"+
    " - op \n"+
    " - guitar.songs.insert.no-limit \n")
    private Integer songsLimit = 5;

    @YmlProperty(path = "guitar",name = "skins",description = "")
    private List<CustomSkin> customSkins;

    @Override
    public void migrate(FileConfiguration oldVersion) {

    }
}
