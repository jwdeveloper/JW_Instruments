package jw.guitar.gameobjects.test;

import jw.fluent_api.minecraft.gameobjects.api.GameObject;
import jw.fluent_api.minecraft.gameobjects.api.ModelRenderer;
import jw.fluent_api.minecraft.tasks.FluentTasks;
import jw.fluent_api.utilites.math.collistions.HitBox;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NoteGameObject extends GameObject
{
    @Getter
    @Setter
    private HitBox hitBox;
    private ModelRenderer modelRenderer;

    @Override
    public void onCreated() {
        modelRenderer = addGameComponent(new ModelRenderer());

    }

    public void setHitBox(HitBox hitBox) {
        this.hitBox = hitBox;
        modelRenderer.setLocation(hitBox.getMax());
    }

    @Override
    public void onPlayerInteraction(Player player, Location location) {
        showNote();
     //   showHitbox();
    }


    @Override
    public void onDestroy() {
        if(hitBox!=null)
            hitBox.hide();;
    }

    public void showHitbox()
    {
        FluentTasks.taskTimer(20,(iteration, task) ->
        {
           // hitBox.show();
        }).onStop(fluentTaskTimer ->
        {
            //FluentLogger.log("stop hitbox");
          //  hitBox.hide();
        }).stopAfterIterations(1).run();
    }

    public void show()
    {
        //showHitbox();
        showNote();
    }

    public void showNote()
    {
        FluentTasks.taskTimer(10,(iteration, task) ->
        {
            modelRenderer.setCustomModel(new ItemStack(Material.DIAMOND));
        }).stopAfterIterations(2).onStop(fluentTaskTimer ->
        {
            //FluentLogger.log("stop note");
            modelRenderer.setCustomModel(null);
        }).run();
    }



}
