package org.dragonet.proxy.data.scoreboard;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Comparator;

@AllArgsConstructor
@Data
public class Score {
    private String name;
    private String fakePlayer;
    private int value;

    public static final Comparator<Score> SCORE_COMPARATOR = new Comparator<Score>()
    {
        public int compare(Score p_compare_1_, Score p_compare_2_)
        {
            return p_compare_1_.getValue() > p_compare_2_.getValue() ? 1 : (p_compare_1_.getValue() < p_compare_2_.getValue() ? -1 : p_compare_2_.getName().compareToIgnoreCase(p_compare_1_.getName()));
        }
    };
}
