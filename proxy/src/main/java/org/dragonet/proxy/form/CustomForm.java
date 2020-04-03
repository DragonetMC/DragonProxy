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
package org.dragonet.proxy.form;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.dragonet.proxy.form.components.FormComponent;

import java.util.ArrayList;
import java.util.List;

public class CustomForm extends Form {
    @Getter
    private final List<FormComponent> components = new ArrayList<>();

    public CustomForm(String title) {
        super("custom_form", title);
    }

    // TODO: @Builder (but its not working for me for some reason)
    public CustomForm addComponent(FormComponent component) {
        components.add(component);
        return this;
    }

    @Override
    public JsonObject serialize() {
        JsonObject object = super.serialize();
        JsonArray content = new JsonArray();
        components.forEach((x) -> {
            content.add(x.serialize());
        });
        object.add("content", content);
        return object;
    }
}
