package org.dragonet.common.mcbedrock.protocol.packets;

import com.github.steveice10.mc.protocol.data.game.setting.Difficulty;
import org.dragonet.common.mcbedrock.protocol.PEPacket;
import org.dragonet.common.mcbedrock.protocol.ProtocolInfo;
import org.dragonet.common.mcbedrock.utilities.BlockPosition;
import org.dragonet.common.mcbedrock.utilities.GameRule;
import org.dragonet.common.mcbedrock.utilities.Vector3F;

import java.util.Map;

/**
 * Created on 2017/10/21.
 */
public class StartGamePacket extends PEPacket {

    public long eid;
    public long rtid;
    public int gamemode;
    public Vector3F position;
    public float pitch;
    public float yaw;

    // world settings
    public int seed;
    public int dimension;
    public int generator;
    public int worldGamemode;
    public Difficulty difficulty;
    public BlockPosition spawnPosition;
    public boolean achievementsDisabled;
    public int time;
    public boolean eduMode;
    public float rainLevel;
    public float lightningLevel;
    public boolean multiplayerGame = true;
    public boolean lanBroadcast = true;
    public boolean xboxLiveBroadcast;
    public boolean commandsEnabled;
    public boolean texturePacksRequired;
    public Map<String, GameRule> gameRules;
    public boolean bonusChestEnabled;
    public boolean startWithMapEnabled;
    public boolean trustPlayersEnabled;
    public int defaultPlayerPermission;
    public int xboxLiveBroadcastMode;

    public String levelId;
    public String worldName;
    public String premiumWorldTemplateId;
    public boolean unknownBool;
    public long currentTick;

    public int enchantmentSeed;

    public StartGamePacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.START_GAME_PACKET;
    }

    @Override
    public void encodePayload() {
        putVarLong(eid);
        putUnsignedVarLong(rtid);
        putVarInt(gamemode);

        putVector3F(position);

        putLFloat(pitch);
        putLFloat(yaw);

        // Level settings
        putVarInt(seed);
        putVarInt(dimension);
        putVarInt(generator);
        putVarInt(worldGamemode);
        putVarInt(difficulty.ordinal());
        putBlockPosition(spawnPosition);
        putBoolean(achievementsDisabled);
        putVarInt(time);
        putBoolean(eduMode);
        putLFloat(rainLevel);
        putLFloat(lightningLevel);
        putBoolean(multiplayerGame);
        putBoolean(lanBroadcast);
        putBoolean(xboxLiveBroadcast);
        putBoolean(commandsEnabled);
        putBoolean(texturePacksRequired);
        putGameRules(gameRules);
        putBoolean(bonusChestEnabled);
        putBoolean(startWithMapEnabled);
        putBoolean(trustPlayersEnabled);
        putVarInt(defaultPlayerPermission);
        putVarInt(xboxLiveBroadcastMode);

        putString(levelId);
        putString(worldName);
        putString(premiumWorldTemplateId);
        putBoolean(unknownBool);
        putLLong(currentTick);

        putVarInt(enchantmentSeed);
    }

    @Override
    public void decodePayload() {
        eid = getVarLong();
        rtid = getUnsignedVarLong();
        gamemode = getVarInt();

        position = getVector3F();

        pitch = getLFloat();
        yaw = getLFloat();

        // Level settings
        seed = getVarInt();
        dimension = getVarInt();
        generator = getVarInt();
        worldGamemode = getVarInt();
        difficulty = Difficulty.values()[getVarInt()];
        spawnPosition = getBlockPosition();
        achievementsDisabled = getBoolean();
        time = getVarInt();
        eduMode = getBoolean();
        rainLevel = getLFloat();
        lightningLevel = getLFloat();
        multiplayerGame = getBoolean();
        lanBroadcast = getBoolean();
        xboxLiveBroadcast = getBoolean();
        commandsEnabled = getBoolean();
        texturePacksRequired = getBoolean();
        gameRules = getGameRules();
        bonusChestEnabled = getBoolean();
        startWithMapEnabled = getBoolean();
        trustPlayersEnabled = getBoolean();
        defaultPlayerPermission = getVarInt();
        xboxLiveBroadcastMode = getVarInt();

        levelId = getString();
        worldName = getString();
        premiumWorldTemplateId = getString();
        unknownBool = getBoolean();
        currentTick = getLLong();

        enchantmentSeed = getVarInt();
    }
}
