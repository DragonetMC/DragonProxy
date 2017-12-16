package org.dragonet.proxy.utilities;

/**
 * Created on 2017/10/21.
 */
public class BlockPosition {

    public int x;
    public int y;
    public int z;

    public BlockPosition() {

    }

    public BlockPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BlockPosition)) {
            return false;
        }

        BlockPosition other = (BlockPosition) obj;

        return this.x == other.x && this.y == other.y && this.z == other.z;
    }
}
