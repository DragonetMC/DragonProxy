package org.dragonet.common.mcbedrock.blocks;

import org.dragonet.common.mcbedrock.maths.AxisAlignedBB;

public class BlockSlab extends Block {
    public BlockSlab(int meta) {
        super(meta);
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        if ((this.meta & 0x08) > 0) {
            return new AxisAlignedBB(
                this.x,
                this.y + 0.5,
                this.z,
                this.x + 1,
                this.y + 1,
                this.z + 1
            );
        } else {
            return new AxisAlignedBB(
                this.x,
                this.y,
                this.z,
                this.x + 1,
                this.y + 0.5,
                this.z + 1
            );
        }
    }
}
