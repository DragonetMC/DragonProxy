package org.dragonet.proxy.commands;

import org.dragonet.proxy.DragonProxy;

public abstract class Command {
	//vars
    private final String name;
    protected String description = "";
    private CommandRegister commandMap = null;
	
	//constructor
	public Command(String name) {
        this(name, "");
    }
	public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }
	
	//public
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	
	public abstract void execute(DragonProxy proxy, String[] args);
	
	//private
	
}
