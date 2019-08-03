package org.dragonet.proxy.stats;

import com.github.steveice10.mc.protocol.data.game.statistic.GenericStatistic;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class Statistics {
    private static Map<GenericStatistic, StatInfo> statMap = new HashMap<>();

    static {
        statMap.put(GenericStatistic.LEAVE_GAME, new StatInfo("Games Quit", StatMeasurement.NONE));
        statMap.put(GenericStatistic.PLAY_ONE_MINUTE, new StatInfo("Minutes Played", StatMeasurement.TIME));
        statMap.put(GenericStatistic.TIME_SINCE_DEATH, new StatInfo("Since Last Death", StatMeasurement.TIME));
        statMap.put(GenericStatistic.TIME_SINCE_REST, new StatInfo("Since Last Rest", StatMeasurement.TIME));
        statMap.put(GenericStatistic.SNEAK_TIME, new StatInfo("Sneak Time", StatMeasurement.TIME));
        statMap.put(GenericStatistic.WALK_ONE_CM, new StatInfo("Distance Walked", StatMeasurement.DISTANCE));
        statMap.put(GenericStatistic.CROUCH_ONE_CM, new StatInfo("Distance Crouched", StatMeasurement.DISTANCE));
        statMap.put(GenericStatistic.SPRINT_ONE_CM, new StatInfo("Distance Sprinted", StatMeasurement.DISTANCE));
        statMap.put(GenericStatistic.WALK_ON_WATER_ONE_CM, new StatInfo("Distance Walked on Water", StatMeasurement.DISTANCE));
        statMap.put(GenericStatistic.FALL_ONE_CM, new StatInfo("Distance Fallen", StatMeasurement.DISTANCE));
        statMap.put(GenericStatistic.CLIMB_ONE_CM, new StatInfo("Distance Climbed", StatMeasurement.DISTANCE));
        statMap.put(GenericStatistic.FLY_ONE_CM, new StatInfo("Distance Flown", StatMeasurement.DISTANCE));
        statMap.put(GenericStatistic.WALK_UNDER_WATER_ONE_CM, new StatInfo("Distance Walked under Water", StatMeasurement.DISTANCE));
        statMap.put(GenericStatistic.MINECART_ONE_CM, new StatInfo("Distance by Minecart", StatMeasurement.DISTANCE));
        statMap.put(GenericStatistic.BOAT_ONE_CM, new StatInfo("Distance by Boat", StatMeasurement.DISTANCE));
        statMap.put(GenericStatistic.PIG_ONE_CM, new StatInfo("Distance by Pig", StatMeasurement.DISTANCE));
        statMap.put(GenericStatistic.HORSE_ONE_CM, new StatInfo("Distance by Horse", StatMeasurement.DISTANCE));
        statMap.put(GenericStatistic.AVIATE_ONE_CM, new StatInfo("Distance by Elytra", StatMeasurement.DISTANCE));
        statMap.put(GenericStatistic.SWIM_ONE_CM, new StatInfo("Distance Swum", StatMeasurement.DISTANCE));
        statMap.put(GenericStatistic.JUMP, new StatInfo("Jumps", StatMeasurement.NONE));
        statMap.put(GenericStatistic.DROP, new StatInfo("Items Dropped", StatMeasurement.NONE));
        statMap.put(GenericStatistic.DAMAGE_DEALT, new StatInfo("Damage Dealt", StatMeasurement.DIVIDE_BY_TEN));
        statMap.put(GenericStatistic.DAMAGE_DEALT_ABSORBED, new StatInfo("Damage Dealt (Absorbed)", StatMeasurement.DIVIDE_BY_TEN));
        statMap.put(GenericStatistic.DAMAGE_DEALT_RESISTED, new StatInfo("Damage Dealt (Resisted)", StatMeasurement.DIVIDE_BY_TEN));
        statMap.put(GenericStatistic.DAMAGE_TAKEN, new StatInfo("Damage Taken", StatMeasurement.DIVIDE_BY_TEN));
        statMap.put(GenericStatistic.DAMAGE_BLOCKED_BY_SHIELD, new StatInfo("Damage Blocked by Shield", StatMeasurement.DIVIDE_BY_TEN));
        statMap.put(GenericStatistic.DAMAGE_ABSORBED, new StatInfo("Damage Absorbed", StatMeasurement.DIVIDE_BY_TEN));
        statMap.put(GenericStatistic.DAMAGE_RESISTED, new StatInfo("Damage Resisted", StatMeasurement.DIVIDE_BY_TEN));
        statMap.put(GenericStatistic.DEATHS, new StatInfo("Number of Deaths", StatMeasurement.NONE));
        statMap.put(GenericStatistic.MOB_KILLS, new StatInfo("Mob Kills", StatMeasurement.NONE));
        statMap.put(GenericStatistic.ANIMALS_BRED, new StatInfo("Animals Bred", StatMeasurement.NONE));
        statMap.put(GenericStatistic.FISH_CAUGHT, new StatInfo("Fish Caught", StatMeasurement.NONE));
        statMap.put(GenericStatistic.TALKED_TO_VILLAGER, new StatInfo("Talked to Villagers", StatMeasurement.NONE));
        statMap.put(GenericStatistic.TRADED_WITH_VILLAGER, new StatInfo("Traded with Villagers", StatMeasurement.NONE));
        statMap.put(GenericStatistic.EAT_CAKE_SLICE, new StatInfo("Cake Slices Eaten", StatMeasurement.NONE));
        statMap.put(GenericStatistic.FILL_CAULDRON, new StatInfo("Cauldrons Filled", StatMeasurement.NONE));
        statMap.put(GenericStatistic.USE_CAULDRON, new StatInfo("Water Taken from Cauldron", StatMeasurement.NONE));
        statMap.put(GenericStatistic.CLEAN_ARMOR, new StatInfo("Armor Pieces Cleaned", StatMeasurement.NONE));
        statMap.put(GenericStatistic.CLEAN_BANNER, new StatInfo("Banners Cleaned", StatMeasurement.NONE));
        statMap.put(GenericStatistic.CLEAN_SHULKER_BOX, new StatInfo("Shulker Boxes Cleaned", StatMeasurement.NONE));
        statMap.put(GenericStatistic.INTERACT_WITH_BREWINGSTAND, new StatInfo("Interactions with Brewing Stand", StatMeasurement.NONE));
        statMap.put(GenericStatistic.INTERACT_WITH_BEACON, new StatInfo("Interactions with Beacon", StatMeasurement.NONE));
        statMap.put(GenericStatistic.INSPECT_DROPPER, new StatInfo("Droppers Searched", StatMeasurement.NONE));
        statMap.put(GenericStatistic.INSPECT_HOPPER, new StatInfo("Hoppers Searched", StatMeasurement.NONE));
        statMap.put(GenericStatistic.INSPECT_DISPENSER, new StatInfo("Dispensers Searched", StatMeasurement.NONE));
        statMap.put(GenericStatistic.PLAY_NOTEBLOCK, new StatInfo("Note Blocks Played", StatMeasurement.NONE));
        statMap.put(GenericStatistic.TUNE_NOTEBLOCK, new StatInfo("Note Blocks Tuned", StatMeasurement.NONE));
        statMap.put(GenericStatistic.POT_FLOWER, new StatInfo("Plants  Potted", StatMeasurement.NONE));
        statMap.put(GenericStatistic.TRIGGER_TRAPPED_CHEST, new StatInfo("Trapped Chests Triggered", StatMeasurement.NONE));
        statMap.put(GenericStatistic.OPEN_ENDERCHEST, new StatInfo("Ender Chests Opened", StatMeasurement.NONE));
        statMap.put(GenericStatistic.ENCHANT_ITEM, new StatInfo("Items Enchanted", StatMeasurement.NONE));
        statMap.put(GenericStatistic.PLAY_RECORD, new StatInfo("Music Discs Played", StatMeasurement.NONE));
        statMap.put(GenericStatistic.INTERACT_WITH_FURNACE, new StatInfo("Interactions with Furnace", StatMeasurement.NONE));
        statMap.put(GenericStatistic.INTERACT_WITH_CRAFTING_TABLE, new StatInfo("Interactions with Crafting Table", StatMeasurement.NONE));
        statMap.put(GenericStatistic.OPEN_CHEST, new StatInfo("Chests Opened", StatMeasurement.NONE));
        statMap.put(GenericStatistic.SLEEP_IN_BED, new StatInfo("Items Dropped", StatMeasurement.NONE));
        statMap.put(GenericStatistic.OPEN_SHULKER_BOX, new StatInfo("Shulker Boxes Opened", StatMeasurement.NONE));
        statMap.put(GenericStatistic.OPEN_BARREL, new StatInfo("Barrels Opened", StatMeasurement.NONE));
        statMap.put(GenericStatistic.INTERACT_WITH_BLAST_FURNACE, new StatInfo("Interactions with Blast Furnace", StatMeasurement.NONE));
        statMap.put(GenericStatistic.INTERACT_WITH_SMOKER, new StatInfo("Interactions with Smoker", StatMeasurement.NONE));
        statMap.put(GenericStatistic.INTERACT_WITH_LECTERN, new StatInfo("Interactions with Lectern", StatMeasurement.NONE));
        statMap.put(GenericStatistic.INTERACT_WITH_CAMPFIRE, new StatInfo("Interactions with Campfire", StatMeasurement.NONE));
        statMap.put(GenericStatistic.INTERACT_WITH_CARTOGRAPHY_TABLE, new StatInfo("Interactions with Cartography Table", StatMeasurement.NONE));
        statMap.put(GenericStatistic.INTERACT_WITH_LOOM, new StatInfo("Interactions with Loom", StatMeasurement.NONE));
        statMap.put(GenericStatistic.INTERACT_WITH_STONECUTTER, new StatInfo("Interactions with Stonecutter", StatMeasurement.NONE));
        statMap.put(GenericStatistic.BELL_RING, new StatInfo("Bells Rung", StatMeasurement.NONE));
        statMap.put(GenericStatistic.RAID_TRIGGER, new StatInfo("Raids Triggered", StatMeasurement.NONE));
        statMap.put(GenericStatistic.RAID_WIN, new StatInfo("Raids Won", StatMeasurement.NONE));
    }

    public static StatInfo getStatisticInfo(GenericStatistic statistic) {
        if(statMap.containsKey(statistic)) {
            return statMap.get(statistic);
        }
        log.warn("Statistic " + statistic.name() + " is not in the map!");
        return null;
    }
}
