package jw.instruments.core.data.instrument;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

public class InstrumentItemStack
{
    @Getter
    private final ItemStack itemStack;
    @Getter
    private final InstrumentData instrumentData;

    public InstrumentItemStack(ItemStack itemStack, InstrumentData instrumentData)
    {
        this.itemStack = itemStack;
        this.instrumentData =instrumentData;
    }



}
