package org.dragonet.proxy.data;

import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class PlayerListInfo {
    private final PlayerListEntry entry;
    @Setter
    private String displayName;
}
