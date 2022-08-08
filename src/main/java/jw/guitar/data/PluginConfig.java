package jw.guitar.data;

import jw.spigot_fluent_api.desing_patterns.dependecy_injection.annotations.Injection;
import jw.spigot_fluent_api.fluent_plugin.config.config_sections.FluentConfigSection;
import jw.spigot_fluent_api.utilites.files.yml.api.annotations.YmlFile;
import jw.spigot_fluent_api.utilites.files.yml.api.annotations.YmlProperty;
import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;

@Injection
@YmlFile
@Data
public class PluginConfig implements FluentConfigSection {


    @YmlProperty(path = "guitar",name = "enable-crafting")
    private boolean crating = true;

    @Override
    public void migrate(FileConfiguration oldVersion) {

    }
}
