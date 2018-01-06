package org.dragonet.proxy.utilities;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;

import java.util.Objects;

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
    
    public Vector3F toVector3F()
    {
        return new Vector3F(x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof BlockPosition)) {
            return false;
        }

        BlockPosition other = (BlockPosition) obj;

        return this.x == other.x && this.y == other.y && this.z == other.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
    
    @Override
    public String toString()
    {
        return x + "/" + y + "/" + z;
    }

}
