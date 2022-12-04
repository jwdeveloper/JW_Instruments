package jw.instruments.core.instuments;

import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.instruments.core.data.Consts;
import jw.fluent.api.spigot.gameobjects.api.models.CustomModel;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.persistence.PersistentDataType;


@Injection
public class Classical implements Instrument {


    @Override
    public ShapedRecipe getRecipe() {
        var recipe = new ShapedRecipe(getNamespaceKey(), getCustomModel().getItemStack());
        recipe.shape(
                "XX2",
                "32X",
                "33X");
        recipe.setIngredient('X', Material.AIR);
       // recipe.setIngredient('1', Material.WOODEN_SHOVEL);
        recipe.setIngredient('2', Material.STICK);
        recipe.setIngredient('3', Material.OAK_PLANKS);

        return recipe;
    }

    @Override
    public CustomModel getCustomModel() {
        var model = new CustomModel(Consts.MODEL_MATERIAL, 100);
        model.setName(getName());
        model.addProperty(PersistentDataType.STRING, NAMESPACE, getName());
        return model;
    }
}
