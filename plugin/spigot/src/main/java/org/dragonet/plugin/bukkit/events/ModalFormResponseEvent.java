package org.dragonet.plugin.bukkit.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.dragonet.plugin.bukkit.BedrockPlayer;
import org.json.JSONArray;

public class ModalFormResponseEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final BedrockPlayer bedrockPlayer;

    private final JSONArray values;

    public ModalFormResponseEvent(BedrockPlayer player, JSONArray values) {
        this.bedrockPlayer = player;
        this.values = values;
    }

    public BedrockPlayer getBedrockPlayer() {
        return bedrockPlayer;
    }

    public JSONArray getValues() {
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
