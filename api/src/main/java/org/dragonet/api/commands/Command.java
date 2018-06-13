package org.dragonet.api.commands;

import org.dragonet.api.ProxyServer;

public abstract class Command {

    private final String name;
    protected String description = "";

    // constructor
    public Command(String name) {
        this(name, "");
    }

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public abstract void execute(ProxyServer proxy, String[] args);

}
