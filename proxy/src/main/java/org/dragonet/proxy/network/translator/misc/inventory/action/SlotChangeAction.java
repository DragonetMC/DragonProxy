package org.dragonet.proxy.network.translator.misc.inventory.action;

import com.github.steveice10.mc.protocol.data.game.window.ClickItemParam;
import com.github.steveice10.mc.protocol.data.game.window.WindowAction;
import com.github.steveice10.mc.protocol.data.game.window.WindowActionParam;
import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import com.nukkitx.protocol.bedrock.data.InventoryActionData;
import com.nukkitx.protocol.bedrock.data.ItemData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;
import org.dragonet.proxy.network.translator.misc.ItemTranslator;

@Getter
@RequiredArgsConstructor
public class SlotChangeAction implements IInventoryAction {
    private final CachedWindow inventory;
    private final InventoryActionData actionData;
    private final WindowAction windowAction;
    private final WindowActionParam windowActionParam;

    @Override
    public boolean isValid(ProxySession session) {
        return inventory.getItem(actionData.getSlot()) != null && inventory.getItem(actionData.getSlot()).getId() == actionData.getFromItem().getId();
    }

    @Override
    public boolean execute(ProxySession session) {
        return inventory.setItem(actionData.getSlot(), actionData.getToItem());
    }

    @Override
    public void onSuccess(ProxySession session) {
        inventory.sendSlot(session, actionData.getSlot());

        session.sendRemotePacket(new ClientWindowActionPacket(inventory.getWindowId(), session.getWindowCache().getTransactionIdCounter().getAndIncrement(),
            actionData.getSlot(), ItemTranslator.translateToJava(actionData.getToItem()), windowAction, windowActionParam));
    }

    @Override
    public void onFail(ProxySession session) {
        inventory.setItem(actionData.getSlot(), actionData.getFromItem());
        inventory.sendSlot(session, actionData.getSlot());
    }
}
