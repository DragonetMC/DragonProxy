/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.common.data.entity.meta.type;

import org.dragonet.common.data.entity.meta.EntityMetaData;
import org.dragonet.common.data.entity.meta.IEntityMetaDataObject;
import org.dragonet.common.data.inventory.Slot;
import org.dragonet.common.utilities.BinaryStream;

public class SlotMeta implements IEntityMetaDataObject {

    public Slot slot;

    public SlotMeta(Slot slot) {
        this.slot = slot;
    }

    public int type() {
        return EntityMetaData.Constants.DATA_TYPE_SLOT;
    }

    public void encode(BinaryStream out) {
        out.putSlot(slot);
    }

    @Override
    public String toString() {
        return "SlotMeta{" + slot.toString() + "}";
    }

}
