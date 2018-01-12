package org.dragonet.common.data.blocks;

import org.dragonet.common.maths.AxisAlignedBB;

public class BlockCarpet extends Block {
    public BlockCarpet(int meta) {
        super(meta);
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(
                this.x,
                this.y,
                this.z,
                this.x + 1,
                this.y + 0.0625,
                this.z + 1
        );
    }
}
