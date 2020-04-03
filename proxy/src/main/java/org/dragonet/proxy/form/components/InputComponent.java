/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.form.components;

import com.google.gson.JsonObject;
import lombok.Getter;

@Getter
public class InputComponent extends FormComponent {
    private String text;
    private String defaultValue;
    private String placeholder;

    public InputComponent(String text, String placeholder) {
        this(text, placeholder, null);
    }

    public InputComponent(String text, String placeholder, String defaultValue) {
        super("input");
        this.text = text;
        this.placeholder = placeholder;
        this.defaultValue = defaultValue;
    }

    @Override
    public JsonObject serialize() {
        JsonObject object = super.serialize();
        object.addProperty("text", text);
        if(defaultValue != null) {
            object.addProperty("default", defaultValue);
        }
        if(placeholder != null) {
            object.addProperty("placeholder", placeholder);
        }
        return object;
    }
}
