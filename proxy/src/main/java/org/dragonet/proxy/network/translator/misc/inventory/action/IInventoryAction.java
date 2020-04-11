package org.dragonet.proxy.network.translator.misc.inventory.action;

import org.dragonet.proxy.network.session.ProxySession;

public interface IInventoryAction {

    boolean isValid(ProxySession session);

    boolean execute(ProxySession session);

    void onSuccess(ProxySession session);

    void onFail(ProxySession session);
}
