package jw.instruments.core.data.instrument;

import jw.fluent.api.spigot.inventory_gui.InventoryUI;
import jw.fluent.api.utilites.java.StringUtils;
import lombok.Data;

@Data
public class InstrumentData
{
    private Integer customModelId;

    private String name= StringUtils.EMPTY_STRING;

    private String type= StringUtils.EMPTY_STRING;

    private String[] chords = new String[InventoryUI.INVENTORY_WIDTH];

    private Integer volume = 100;

    private String  version = StringUtils.EMPTY_STRING;


}
