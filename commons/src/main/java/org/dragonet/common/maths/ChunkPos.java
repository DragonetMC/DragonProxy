package org.dragonet.common.maths;

/**
 *
 * @author https://github.com/nullbear/minecraft/blob/master/net/minecraft/util/math/ChunkPos.java
 */
public class ChunkPos {

    /**
     * The X position of this Chunk Coordinate Pair
     */
    public final int chunkXPos;

    /**
     * The Z position of this Chunk Coordinate Pair
     */
    public final int chunkZPos;

    public ChunkPos(int x, int z) {
        this.chunkXPos = x;
        this.chunkZPos = z;
    }

    public ChunkPos(BlockPosition pos) {
        this.chunkXPos = pos.x >> 4;
        this.chunkZPos = pos.z >> 4;
    }

    /**
     * converts a chunk coordinate pair to an integer (suitable for hashing)
     */
    public static long chunkXZ2Int(int x, int z) {
        return (long) x & 4294967295L | ((long) z & 4294967295L) << 32;
    }

    public int hashCode() {
        int i = 1664525 * this.chunkXPos + 1013904223;
        int j = 1664525 * (this.chunkZPos ^ -559038737) + 1013904223;
        return i ^ j;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_)
            return true;
        else if (!(p_equals_1_ instanceof ChunkPos))
            return false;
        else {
            ChunkPos chunkpos = (ChunkPos) p_equals_1_;
            return this.chunkXPos == chunkpos.chunkXPos && this.chunkZPos == chunkpos.chunkZPos;
        }
    }

    public double getDistanceSq(Position position) {
        double d0 = (double) (this.chunkXPos * 16 + 8);
        double d1 = (double) (this.chunkZPos * 16 + 8);
        double d2 = d0 - position.x;
        double d3 = d1 - position.x;
        return d2 * d2 + d3 * d3;
    }

    public int getCenterXPos() {
        return (this.chunkXPos << 4) + 8;
    }

    public int getCenterZPosition() {
        return (this.chunkZPos << 4) + 8;
    }

    /**
     * Get the first world X coordinate that belongs to this Chunk
     */
    public int getXStart() {
        return this.chunkXPos << 4;
    }

    /**
     * Get the first world Z coordinate that belongs to this Chunk
     */
    public int getZStart() {
        return this.chunkZPos << 4;
    }

    /**
     * Get the last world X coordinate that belongs to this Chunk
     */
    public int getXEnd() {
        return (this.chunkXPos << 4) + 15;
    }

    /**
     * Get the last world Z coordinate that belongs to this Chunk
     */
    public int getZEnd() {
        return (this.chunkZPos << 4) + 15;
    }

    /**
     * Get the World coordinates of the Block with the given Chunk coordinates
     * relative to this chunk
     */
    public BlockPosition getBlock(int x, int y, int z) {
        return new BlockPosition((this.chunkXPos << 4) + x, y, (this.chunkZPos << 4) + z);
    }

    /**
     * Get the coordinates of the Block in the center of this chunk with the
     * given Y coordinate
     */
    public BlockPosition getCenterBlock(int y) {
        return new BlockPosition(this.getCenterXPos(), y, this.getCenterZPosition());
    }

    public String toString() {
        return "[" + this.chunkXPos + ", " + this.chunkZPos + "]";
    }
}
