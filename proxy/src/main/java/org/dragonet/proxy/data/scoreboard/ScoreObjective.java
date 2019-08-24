package org.dragonet.proxy.data.scoreboard;

import lombok.Data;

import java.util.*;

@Data
public class ScoreObjective {
    private final String name;
    private String displayName;
    private DisplaySlot displaySlot;

    private Map<String, Score> scores = new HashMap<>();

    public ScoreObjective(String name) {
        this.name = name;
        this.displayName = name;
    }

    public Score createScore(String name, String fakePlayer, int value) {
        Score score = new Score(name, fakePlayer, value);
        scores.put(name, score);
        return score;
    }

    public Score getOrCreateScore(String name) {
        if(scores.containsKey(name)) {
            return scores.get(name);
        }
        return createScore(name, name,0);
    }

    public Score getScore(String name) {
       return scores.get(name);
    }

    public Collection<Score> getSortedScores() {
        List<Score> list = new ArrayList<>();

        for (Score score : scores.values()) {
            if (score != null) {
                list.add(score);
            }
        }

        Collections.sort(list, Score.SCORE_COMPARATOR);
        return list;
    }

    public enum DisplaySlot {
        SIDEBAR, LIST, BELOWNAME
    }
}
