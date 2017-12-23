package org.dragonet.proxy.utilities;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;

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

    public BlockPosition(Position pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
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
