package org.dragonet.proxy.network.translator.misc.inventory.action;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import com.nukkitx.protocol.bedrock.data.InventoryActionData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;

@Getter
@RequiredArgsConstructor
@Log4j2
public class DropItemAction implements IInventoryAction {
    private final InventoryActionData actionData;

    @Override
    public boolean isValid(ProxySession session) {
        return actionData.getFromItem() != null;
    }

    @Override
    public boolean execute(ProxySession session) {
        CachedWindow inventory = session.getWindowCache().getPlayerInventory();

        return inventory.setItem(actionData.getSlot(), actionData.getFromItem());
    }

    @Override
    public void onSuccess(ProxySession session) {
        CachedWindow inventory = session.getWindowCache().getPlayerInventory();
        inventory.sendSlot(session, actionData.getSlot());

        PlayerAction actionType = PlayerAction.DROP_ITEM;
        session.sendRemotePacket(new ClientPlayerActionPacket(actionType, new Position(0, 0, 0), BlockFace.UP));
    }

    @Override
    public void onFail(ProxySession session) {
        CachedWindow inventory = session.getWindowCache().getPlayerInventory();

        inventory.setItem(actionData.getSlot(), actionData.getToItem());
        inventory.sendSlot(session, actionData.getSlot());
    }
}
