package org.dragonet.proxy.data.scoreboard;

import com.github.steveice10.mc.protocol.data.game.scoreboard.ScoreboardPosition;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.RequiredArgsConstructor;
import org.dragonet.proxy.network.session.ProxySession;

@RequiredArgsConstructor
public class Scoreboard {
    private final ProxySession session;

    private Object2ObjectMap<String, Objective> objectives = new Object2ObjectOpenHashMap<>();
    private Object2ObjectMap<String, Team> teams = new Object2ObjectOpenHashMap<>();

    public Objective registerObjective(String name) {
        return registerObjective(name, name);
    }

    public Objective registerObjective(String name, String displayName) {
        Objective objective = new Objective(name, displayName);
        objectives.put(name, objective);
        return objective;
    }

    public void removeObjective(String name) {
        objectives.remove(name);
    }

    public Objective getObjective(String name) {
        return objectives.get(name);
    }

    public Team registerTeam(String name) {
        Team team = new Team(name);
        teams.put(name, team);
        return team;
    }

    public void removeTeam(String name) {
        teams.remove(name);
    }

    public Team getTeam(String name) {
        return teams.get(name);
    }
}
