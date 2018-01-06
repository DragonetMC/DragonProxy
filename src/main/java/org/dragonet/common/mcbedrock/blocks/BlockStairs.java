package org.dragonet.common.mcbedrock.blocks;

import org.dragonet.common.mcbedrock.maths.AxisAlignedBB;

public class BlockStairs extends Block {

    public BlockStairs(int meta) {
        super(meta);
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        if ((this.getDamage() & 0x04) > 0) {
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

    @Override
    public boolean collidesWithBB(AxisAlignedBB bb) {
        int damage = this.getDamage();
        int side = damage & 0x03;
        double f = 0;
        double f1 = 0.5;
        double f2 = 0.5;
        double f3 = 1;
        if ((damage & 0x04) > 0) {
            f = 0.5;
            f1 = 1;
            f2 = 0;
            f3 = 0.5;
        }

        //The base
        if (bb.intersectsWith(new AxisAlignedBB(
            this.x,
            this.y + f,
            this.z,
            this.x + 1,
            this.y + f1,
            this.z + 1
        ))) {
            return true;
        }


        if (side == 0) {
            if (bb.intersectsWith(new AxisAlignedBB(
                this.x + 0.5,
                this.y + f2,
                this.z,
                this.x + 1,
                this.y + f3,
                this.z + 1
            ))) {
                return true;
            }
        } else if (side == 1) {
            if (bb.intersectsWith(new AxisAlignedBB(
                this.x,
                this.y + f2,
                this.z,
                this.x + 0.5,
                this.y + f3,
                this.z + 1
            ))) {
                return true;
            }
        } else if (side == 2) {
            if (bb.intersectsWith(new AxisAlignedBB(
                this.x,
                this.y + f2,
                this.z + 0.5,
                this.x + 1,
                this.y + f3,
                this.z + 1
            ))) {
                return true;
            }
        } else if (side == 3) {
            if (bb.intersectsWith(new AxisAlignedBB(
                this.x,
                this.y + f2,
                this.z,
                this.x + 1,
                this.y + f3,
                this.z + 0.5
            ))) {
                return true;
            }
        }

        return false;
    }
}
