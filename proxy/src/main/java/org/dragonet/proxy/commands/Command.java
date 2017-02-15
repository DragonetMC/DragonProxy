package org.dragonet.proxy.commands;

import org.dragonet.proxy.DragonProxy;

import lombok.Getter;

public abstract class Command {

    @Getter
    private final String name;

    private CommandRegister commandMap = null;

    @Getter
    protected String description = "";

    public Command(String name) {
        this(name, "");
    }

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract void execute(DragonProxy proxy, String[] args);
}
