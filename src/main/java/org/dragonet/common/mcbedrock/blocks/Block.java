package org.dragonet.common.mcbedrock.blocks;

import org.dragonet.common.mcbedrock.maths.AxisAlignedBB;
import org.dragonet.proxy.utilities.Position;

import java.lang.reflect.Constructor;

public class Block extends Position {
    public static final int SLABS = 44;
    public static final int WOOD_STAIRS = 53;
    public static final int COBBLESTONE_STAIRS = 67;
    public static final int BRICK_STAIRS = 108;
    public static final int STONE_BRICK_STAIRS = 109;
    public static final int NETHER_BRICKS_STAIRS = 114;
    public static final int SANDSTONE_STAIRS = 128;
    public static final int SPRUCE_WOOD_STAIRS = 134;
    public static final int BIRCH_WOODEN_STAIRS = 135;
    public static final int JUNGLE_WOOD_STAIRS = 136;
    public static final int QUARTZ_STAIRS = 156;
    public static final int WOOD_SLABS = 158;
    public static final int ACACIA_WOOD_STAIRS = 163;
    public static final int DARK_OAK_WOOD_STAIRS = 164;
    public static final int RED_SANDSTONE_STAIRS = 180;
    public static final int RED_SANDSTONE_SLAB = 182;
    public static final int PURPUR_STAIRS = 203;
    public static final int PURPUR_SLAB = 205;

    public static Class[] list = null;

    public static void init() {
        if (list == null) {
            list = new Class[256];
            list[WOOD_STAIRS] = BlockStairs.class;
            list[COBBLESTONE_STAIRS] = BlockStairs.class;
            list[BRICK_STAIRS] = BlockStairs.class;
            list[STONE_BRICK_STAIRS] = BlockStairs.class;
            list[NETHER_BRICKS_STAIRS] = BlockStairs.class;
            list[SANDSTONE_STAIRS] = BlockStairs.class;
            list[SPRUCE_WOOD_STAIRS] = BlockStairs.class;
            list[BIRCH_WOODEN_STAIRS] = BlockStairs.class;
            list[JUNGLE_WOOD_STAIRS] = BlockStairs.class;
            list[QUARTZ_STAIRS] = BlockStairs.class;
            list[ACACIA_WOOD_STAIRS] = BlockStairs.class;
            list[DARK_OAK_WOOD_STAIRS] = BlockStairs.class;
            list[RED_SANDSTONE_STAIRS] = BlockStairs.class;
            list[PURPUR_STAIRS] = BlockStairs.class;
            list[SLABS] = BlockSlab.class;
            list[WOOD_SLABS] = BlockSlab.class;
            list[RED_SANDSTONE_SLAB] = BlockSlab.class;
            list[PURPUR_SLAB] = BlockSlab.class;
        }
    }

    private AxisAlignedBB boundingBox = null;
    private AxisAlignedBB collisionBoundingBox = null;
    protected int meta = 0;

    protected Block(Integer meta) {
        this.meta = (meta != null ? meta : 0);
    }

    public static Block get(int id, Integer meta, Position pos) {
        Block block;
        try {
            Class c = list[id];
            if (c != null) {
                Constructor constructor = c.getDeclaredConstructor(int.class);
                constructor.setAccessible(true);
                block = (Block) constructor.newInstance(meta);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        if (pos != null) {
            block.x = pos.x;
            block.y = pos.y;
            block.z = pos.z;
        }

        return block;
    }

    public final int getDamage() {
        return this.meta;
    }

    public final void setDamage(Integer meta) {
        this.meta = (meta == null ? 0 : meta & 0x0f);
    }

    public boolean collidesWithBB(AxisAlignedBB bb) {
        return collidesWithBB(bb, false);
    }

    public AxisAlignedBB getBoundingBox() {
        if (this.boundingBox == null) {
            this.boundingBox = this.recalculateBoundingBox();
        }
        return this.boundingBox;
    }

    public AxisAlignedBB getCollisionBoundingBox() {
        if (this.collisionBoundingBox == null) {
            this.collisionBoundingBox = this.recalculateCollisionBoundingBox();
        }
        return this.collisionBoundingBox;
    }

    public boolean collidesWithBB(AxisAlignedBB bb, boolean collisionBB) {
        AxisAlignedBB bb1 = collisionBB ? this.getCollisionBoundingBox() : this.getBoundingBox();
        return bb1 != null && bb.clone().intersectsWith(bb1);
    }

    protected AxisAlignedBB recalculateCollisionBoundingBox() {
        return getBoundingBox();
    }

    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(
            this.x,
            this.y,
            this.z,
            this.x + 1,
            this.y + 1,
            this.z + 1
        );
    }


}
