package org.dragonet.proxy.data.scoreboard;

import com.nukkitx.protocol.bedrock.data.ScoreInfo;
import com.nukkitx.protocol.bedrock.packet.RemoveObjectivePacket;
import com.nukkitx.protocol.bedrock.packet.SetDisplayObjectivePacket;
import com.nukkitx.protocol.bedrock.packet.SetScorePacket;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.util.TextFormat;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Log4j2
public class Scoreboard {
    private ProxySession session;

    private final long id;
    private Map<String, ScoreObjective> objectives = new HashMap<>();
    private Map<String, ScoreTeam> teams = new HashMap<>();
    private Map<String, ScoreTeam> teamMemberships = new HashMap<>();

    public Scoreboard(ProxySession session) {
        this.session = session;
        id = ThreadLocalRandom.current().nextLong();
    }

    public ScoreObjective addObjective(String name) {
        return addObjective(name, name);
    }

    public ScoreObjective addObjective(String name, String displayName) {
        ScoreObjective objective = new ScoreObjective(name);
        objective.setDisplayName(displayName);
        objectives.put(name, objective);
        return objective;
    }

    public ScoreObjective getObjective(String name) {
        return objectives.get(name);
    }

    public ScoreTeam createTeam(String name) {
        ScoreTeam team = teams.get(name);

        if(team != null) {
            throw new IllegalArgumentException("A team with the name \'" + name + "\' already exists!");
        } else {
            team = new ScoreTeam();
            team.setName(name);
            teams.put(name, team);
            return team;
        }
    }

    public void removeTeam(ScoreTeam team) {
        teams.remove(team.getName());

        if(!team.getMembers().isEmpty()) {
            for(String playerName : team.getMembers()) {
                teamMemberships.remove(playerName);
            }
        }
    }


    public void addPlayerToTeam(String player, ScoreTeam team) {
        if(!teams.containsKey(team)) {
            log.warn("team doesnt exist");
            return;
        }
        if(getPlayersTeam(player) != null) {
            removePlayerFromTeams(player);
        }
        teamMemberships.put(player, team);
        team.getMembers().add(player);
    }

    public void removePlayerFromTeams(String player) {
        ScoreTeam team = getPlayersTeam(player);
        if(team != null) {
            removePlayerFromTeam(player, team);
        }
    }

    public void removePlayerFromTeam(String player, ScoreTeam team) {
        if (getPlayersTeam(player) != team) {
            throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team \'" + team.getName() + "\'.");
        } else {
            teamMemberships.remove(player);
            team.getMembers().remove(player);
        }
    }

    public ScoreTeam getPlayersTeam(String player) {
        return teamMemberships.get(player);
    }

    public void remove(ScoreObjective objective) {
        RemoveObjectivePacket packet = new RemoveObjectivePacket();
        packet.setObjectiveId(objective.getName());
        session.getBedrockSession().sendPacket(packet);
    }

    public void update(ScoreObjective objective) {
        remove(objective);

        SetDisplayObjectivePacket packet = new SetDisplayObjectivePacket();
        packet.setObjectiveId(objective.getName());
        packet.setDisplayName(objective.getDisplayName());
        packet.setDisplaySlot(objective.getDisplaySlot().name().toLowerCase());
        packet.setSortOrder(2);
        packet.setCriteria("dummy");

        session.getBedrockSession().sendPacket(packet);

        if(!objective.getScores().isEmpty()) {
            updateScores(objective, SetScorePacket.Action.SET);
        }
    }

    public void updateScores(ScoreObjective objective, SetScorePacket.Action action) {
        Collection<Score> collection = objective.getSortedScores();

        for(Score score : collection) {
            ScoreTeam team = getPlayersTeam(session.getUsername());
            if(team == null) {
                log.warn(TextFormat.GOLD + "updateScores(): team is null");
                continue;
            }
            log.info(TextFormat.AQUA + "team name: " + team.getName());
        }

        for(ScoreTeam team : teams.values()) {
            log.info(TextFormat.GREEN + "team name: " + team.getName() + "  prefix: " + team.getPrefix() + TextFormat.GREEN + "  color: " + team.getColor().name());
        }


        for(Map.Entry<String, Score> entry : objective.getScores().entrySet()) {
            Score score = entry.getValue();
            ScoreInfo scoreInfo = new ScoreInfo(id, objective.getName(), score.getValue(), score.getFakePlayer());

            SetScorePacket setScorePacket = new SetScorePacket();
            setScorePacket.setAction(action);
            setScorePacket.setInfos(Arrays.asList(scoreInfo));

            session.getBedrockSession().sendPacket(setScorePacket);

            if(action == SetScorePacket.Action.REMOVE) {
                objective.getScores().remove(score.getName());
            }
        }
    }
}
