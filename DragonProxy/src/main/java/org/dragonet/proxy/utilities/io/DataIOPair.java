/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.utilities.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import lombok.Getter;

public class DataIOPair {

    private @Getter
    DataInputStream input;

    private @Getter
    DataOutputStream output;

    public DataIOPair(DataInputStream input, DataOutputStream output) {
        this.input = input;
        this.output = output;
    }
}
