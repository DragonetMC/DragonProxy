package org.dragonet.plugin.bukkit.events;

import com.google.gson.JsonArray;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.dragonet.plugin.bukkit.BedrockPlayer;

public class ModalFormResponseEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final BedrockPlayer bedrockPlayer;

    private final JsonArray values;

    public ModalFormResponseEvent(BedrockPlayer player, JsonArray values) {
        this.bedrockPlayer = player;
        this.values = values;
    }

    public BedrockPlayer getBedrockPlayer() {
        return bedrockPlayer;
    }

    public JsonArray getValues() {
        return values;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
