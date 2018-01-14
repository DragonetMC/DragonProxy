package org.dragonet.proxy.network.cache;

import org.dragonet.common.data.blocks.Block;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.common.maths.Position;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BlockCache {
    private final UpstreamSession upstream;
    private final Map<Integer, Block> blocks = new ConcurrentHashMap<Integer, Block>();

    public BlockCache(UpstreamSession upstream) {
        this.upstream = upstream;
    }

    public void saveBlock(Block block) {
        blocks.put(block.hashCode(), block);
    }

    public Block getBlock(Position position) {
        return blocks.get(position.hashCode());
    }

    public void removeBlock(Position position) {
        blocks.remove(position.hashCode());
    }

    public Collection<Block> getAllBlocks() {
        return blocks.values();
    }

    public void checkBlock(int id, int damage, Position position) {
        Block glitchyBlock = Block.get(id, damage, position);
        if (glitchyBlock != null) {
//            DragonProxy.getInstance().getLogger().debug("Added glitchy block : " + glitchyBlock);
            saveBlock(glitchyBlock);
        } else {
            if (getBlock(position) != null)
            {
                removeBlock(position);
            }
        }
    }
}
