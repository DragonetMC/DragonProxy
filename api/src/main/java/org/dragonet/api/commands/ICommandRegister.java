/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.api.commands;

import java.util.Map;

/**
 *
 * @author Epic
 */
public interface ICommandRegister {
    
    public void registerDefaults();

    public void registerCommand(String command, Command console);

    public Map<String, Command> getCommands();

    public void callCommand(String cmd);
}
