package org.dragonet.plugin.bungeecord.compat.luckperms;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;

public class LuckPermsCompat {
  private static final LuckPermsApi api = LuckPerms.getApi();

  public static void addContextCalculator() {
    api.getContextManager().registerCalculator(new BedrockClientContextCalculator());
  }
}
