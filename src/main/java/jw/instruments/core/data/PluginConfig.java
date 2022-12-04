package jw.instruments.core.data;

import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent.plugin.implementation.config.config_sections.FluentConfigSection;
import jw.fluent.api.utilites.files.yml.api.annotations.YmlFile;
import jw.fluent.api.utilites.files.yml.api.annotations.YmlProperty;
import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;

@Injection
@YmlFile
@Data
public class PluginConfig implements FluentConfigSection {


    @YmlProperty(path = "song",name = "songs-limit",
    description = "Determine how much songs player can create \n"+
    "It's not applied for players with \n"+
    " - op \n"+
    " - "+PluginPermissions.SONGS_INSERT_NO_LIMIT+" \n")
    private Integer songsLimit = 5;

    @Override
    public void migrate(FileConfiguration oldVersion) {

    }
}
