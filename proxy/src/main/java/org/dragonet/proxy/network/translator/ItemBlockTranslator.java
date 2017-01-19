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
package org.dragonet.proxy.network.translator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.spacehq.mc.protocol.data.game.entity.metadata.ItemStack;

import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.Tag;
import org.spacehq.mc.protocol.data.message.Message;

public class ItemBlockTranslator {
    
    public final static int UNSUPPORTED_BLOCK_ID = 165;

    public final static String DRAGONET_COMPOUND = "DragonetNBT";
    
    public final static Map<Integer, Integer> PC_TO_PE_OVERRIDE = new HashMap<>();
    public final static Map<Integer, Integer> PE_TO_PC_OVERRIDE = new HashMap<>();
    public final static Map<Integer, String> NAME_OVERRIDES = new HashMap<>();
    
    static {
        swap(125, 157); //Double Slab <-> Activator Rail
        onewayOverride(126, 158); //Slab <-> NULL
        onewayOverride(95, 241); 
        onewayOverride(176, 63, "Banner"); //Sign         =\_
        onewayOverride(177, 68, "Banner"); //Wall sign    =/ We send banner as sign [Banner]
        onewayOverride(36, 248);
        onewayOverride(84, 248);
        onewayOverride(137, 248);
        onewayOverride(166, 248);
        onewayOverride(176, 248);
        onewayOverride(177, 248);
        onewayOverride(188, 85, "Spruce Fence"); // Spruce Fence -> Fence
        onewayOverride(189, 85, "Birch Fence"); // Birch Fence -> Fence
        onewayOverride(190, 85, "Jungle Fence"); // Jungle Fence -> Fence
        onewayOverride(191, 85, "Dark Oak Fence"); // Dark Oak Fence -> Fence
        onewayOverride(192, 85, "Acacia Fence"); // Acacia Fence -> Fence
    }

    private static void swap(int pcId, int peId) {
        PC_TO_PE_OVERRIDE.put(pcId, peId);
        PE_TO_PC_OVERRIDE.put(peId, pcId);
    }
    
    private static void onewayOverride(int fromPc, int toPe, String nameOverride){
        onewayOverride(fromPc, toPe);
        if(nameOverride != null){
            NAME_OVERRIDES.put(fromPc, nameOverride);
        }
    }

    private static void onewayOverride(int fromPc, int toPe) {
        PC_TO_PE_OVERRIDE.put(fromPc, toPe);
    }

    // Query handler
    public static int translateToPE(int pcItemBlockId) {
        if (!PC_TO_PE_OVERRIDE.containsKey(pcItemBlockId)) {
            return pcItemBlockId;
        }
        int ret = PC_TO_PE_OVERRIDE.get(pcItemBlockId);
        if (pcItemBlockId >= 255 && ret == UNSUPPORTED_BLOCK_ID) {
            ret = 0;   //Unsupported item becomes air
        }
        return ret;
    }
    
    public static int translateToPC(int peItemBlockId){
        if (!PE_TO_PC_OVERRIDE.containsKey(peItemBlockId)) {
            return peItemBlockId;
        }
        int ret = PE_TO_PC_OVERRIDE.get(peItemBlockId);
        return ret;
    }
    
    public static CompoundTag newTileTag(String id, int x, int y, int z){
        CompoundTag t = new CompoundTag();
        t.putString("id", id);
        t.putInt("x", x);
        t.putInt("y", y);
        t.putInt("z", z);
        return t;
    }
    
    public static CompoundTag translateNBT(int id, org.spacehq.opennbt.tag.builtin.CompoundTag pcTag){
        CompoundTag peTag = new CompoundTag();
        if(pcTag != null && pcTag.contains("display")) {
	  Object o = pcTag.get("display").getValue();
	  if (o instanceof org.spacehq.opennbt.tag.builtin.CompoundTag) {
		  org.spacehq.opennbt.tag.builtin.CompoundTag t = (org.spacehq.opennbt.tag.builtin.CompoundTag) o;
		  if (t.contains("Name")){
		    peTag.putCompound("display", new CompoundTag().putString("Name", ((org.spacehq.opennbt.tag.builtin.CompoundTag)pcTag.get("display").getValue()).get("Name").getValue().toString()));
		  }
	  } else {
		if (o instanceof LinkedHashMap) {
			LinkedHashMap map = (LinkedHashMap) o;
			Set<String> t = map.keySet();
			  if (t.contains("Name")){
			    org.spacehq.opennbt.tag.builtin.StringTag tag = (org.spacehq.opennbt.tag.builtin.StringTag ) map.get("Name");
			    peTag.putCompound("display", new CompoundTag().putString("Name", tag.getValue().toString()));
			  }
		}
	  }
        }else{
            if(NAME_OVERRIDES.containsKey(id)){
                peTag.putCompound("display", new CompoundTag().putString("Name", NAME_OVERRIDES.get(id)));
            }
        }
        return peTag;
    }
    
    public static Item translateToPE(ItemStack item){
        if(item == null || item.getId() == 0) return null;

        Item inv = new Item(translateToPE(item.getId()), item.getData(), (item.getAmount() & 0xFF));
        CompoundTag d = new CompoundTag();
        d.putShort("id", item.getId());
        d.putShort("amount", item.getAmount());
        d.putShort("data", item.getData());
        inv.setCompoundTag(translateNBT(item.getId(), item.getNBT()));
        
        //inv.putCompound(DRAGONET_COMPOUND, d);
        d.setName(DRAGONET_COMPOUND);
        inv.setNamedTag(d);
        
        return inv;
    }
    
    public static ItemStack translateToPC(Item slot){
        ItemStack item = null;
        Tag tag = null;
        if((tag = slot.getNamedTagEntry(DRAGONET_COMPOUND)) != null){
        	if(tag instanceof CompoundTag){
			    item = new ItemStack(
			            ((CompoundTag) tag).getShort("id"), 
			            ((CompoundTag) tag).getShort("amount"), 
			            ((CompoundTag) tag).getShort("data"));
			}
        }else{
            item = new ItemStack(translateToPC((int)slot.getId()), (int)(slot.count & 0xFF), (int)slot.getDamage());
        }
        return item;
    }
}
