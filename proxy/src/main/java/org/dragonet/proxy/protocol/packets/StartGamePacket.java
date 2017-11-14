package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.BlockPosition;
import org.dragonet.proxy.utilities.GameRule;
import org.dragonet.proxy.utilities.Vector3F;

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
    public int difficulty;
    public BlockPosition spawnPosition;
    public boolean achievementsDisabled;
    public int time;
    public boolean eduMode;
    public float rainLevel;
    public float lightningLevel;
    public boolean multiplayerGame;
    public boolean lanBroadcast;
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

        //Level settings
        putVarInt(seed);
        putVarInt(dimension);
        putVarInt(generator);
        putVarInt(worldGamemode);
        putVarInt(difficulty);
        putBlockPosition(spawnPosition);
        putBoolean(achievementsDisabled);
        putVarInt(time);
        putBoolean(eduMode);
        putLFloat(rainLevel);
        putLFloat(lightningLevel);
        putBoolean(multiplayerGame);
        putBoolean(lanBroadcast);
        putBoolean(xboxLiveBroadcast);
        putBoolean(true);
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

        //Level settings
        seed = getVarInt();
        dimension = getVarInt();
        generator = getVarInt();
        worldGamemode = getVarInt();
        difficulty = getVarInt();
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
