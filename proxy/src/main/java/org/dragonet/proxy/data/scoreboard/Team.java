package org.dragonet.proxy.data.scoreboard;

import com.github.steveice10.mc.protocol.data.game.scoreboard.NameTagVisibility;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class Team {
    private final String name;
    private List<String> players = new LinkedList<>();

    private String displayName;
    private String prefix;
    private String suffix;

    private NameTagVisibility nameTagVisibility;
    private boolean friendlyFire;
    private boolean seeFriendlyInvisibles;

    /**
     * Add players or entities to this team.
     *
     * @param names the player name or UUID if it is an entity
     */
    public void addPlayers(String[] names) {
        for (String name : names) {
            if(players.contains(name)) {
                players.add(name);
            }
        }
    }

    /**
     * Remove players or entities from this team.
     *
     * @param names the player name or UUID if it is an entity
     */
    public void removePlayers(String[] names) {
        for (String name : names) {
            players.remove(name);
        }
    }
}
