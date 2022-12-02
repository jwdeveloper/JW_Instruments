package jw.instruments.core.data;

import jw.fluent_api.utilites.files.yml.api.annotations.YmlProperty;
import jw.fluent_api.utilites.java.StringUtils;
import lombok.Data;
import org.bukkit.Material;


@Data
public class CustomSkin
{
    @YmlProperty
    private String name = StringUtils.EMPTY_STRING;

    @YmlProperty
    private String parent = StringUtils.EMPTY_STRING;

    @YmlProperty(name = "custom-model-id")
    private Integer customModelId;

    @YmlProperty(name = "crafting-material")
    private String craftingMaterial = StringUtils.EMPTY_STRING;


    public Material getMaterial()
    {
        var value = craftingMaterial;
        value = value.toUpperCase();
        value = value.replace(" ","_");
        return Material.getMaterial(value);
    }
}
