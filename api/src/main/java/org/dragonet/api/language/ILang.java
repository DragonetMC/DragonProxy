/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.api.language;

/**
 *
 * @author Epic
 */
public interface ILang {

    public abstract String get(String key);

    public abstract String get(String key, Object... repl);
}
