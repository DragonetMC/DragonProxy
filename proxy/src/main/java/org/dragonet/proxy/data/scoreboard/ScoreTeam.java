package org.dragonet.proxy.data.scoreboard;

import com.github.steveice10.mc.protocol.data.game.scoreboard.TeamColor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class ScoreTeam {
    private String name;
    private String prefix = "";
    private String suffix = "";
    private TeamColor color;
    private Set<String> members = new HashSet<>();
}
