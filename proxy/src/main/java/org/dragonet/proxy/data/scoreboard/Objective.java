package org.dragonet.proxy.data.scoreboard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Objective {
    private final String name;
    private String displayName;

    private DisplaySlot displaySlot;

    public Objective(String name) {
        this(name, name);
    }

    public Objective(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }
}
