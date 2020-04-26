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
package org.dragonet.proxy.network.session.cache;

import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.setting.Difficulty;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.data.GameRuleData;
import com.nukkitx.protocol.bedrock.data.LevelEventType;
import com.nukkitx.protocol.bedrock.packet.GameRulesChangedPacket;
import com.nukkitx.protocol.bedrock.packet.LevelEventPacket;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.dragonet.proxy.data.scoreboard.Scoreboard;
import org.dragonet.proxy.data.stats.StatInfo;
import org.dragonet.proxy.network.session.ProxySession;

@Data
public class WorldCache implements Cache {
    private final Object2IntMap<StatInfo> statistics = new Object2IntOpenHashMap<>();
    private double rainLevel = 0.0;

    private Difficulty difficulty = Difficulty.EASY;

    @Setter(value = AccessLevel.NONE)
    private boolean timeStopped = false;

    private boolean firstTimePacket = true;

    @Setter(value = AccessLevel.NONE)
    private boolean showCoordinates = true;

    private Scoreboard scoreboard;

    public WorldCache(ProxySession session) {
        scoreboard = new Scoreboard(session);
    }

    /**
     * Starts or stops the daylight cycle in the current world.
     */
    public void setTimeStopped(ProxySession session, boolean value) {
        timeStopped = value;

        GameRulesChangedPacket gameRulesChangedPacket = new GameRulesChangedPacket();
        gameRulesChangedPacket.getGameRules().add(new GameRuleData<>("dodaylightcycle", !value));
        session.sendPacket(gameRulesChangedPacket);
    }

    public void setShowCoordinates(ProxySession session, boolean value) {
        showCoordinates = value;

        GameRulesChangedPacket gameRulesChangedPacket = new GameRulesChangedPacket();
        gameRulesChangedPacket.getGameRules().add(new GameRuleData<>("showcoordinates", value));
        session.sendPacket(gameRulesChangedPacket);
    }

    /**
     * Starts raining in the current world, with the specified strength.
     */
    public void startRain(ProxySession session, int strength) {
        LevelEventPacket levelEventPacket = new LevelEventPacket();
        levelEventPacket.setType(LevelEventType.START_RAIN);
        levelEventPacket.setPosition(Vector3f.ZERO);
        levelEventPacket.setData(strength);
        session.sendPacket(levelEventPacket);
    }

    /**
     * Stops the rain in the current world.
     */
    public void stopRain(ProxySession session) {
        LevelEventPacket levelEventPacket = new LevelEventPacket();
        levelEventPacket.setType(LevelEventType.STOP_RAIN);
        levelEventPacket.setPosition(Vector3f.ZERO);
        levelEventPacket.setData(0);
        session.sendPacket(levelEventPacket);
    }

    @Override
    public void purge() {

    }
}
