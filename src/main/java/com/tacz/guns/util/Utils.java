package com.tacz.guns.util;

import com.tacz.guns.config.common.WallConfig;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class Utils {
    public static void debug(Object ... objects){
        StringBuilder s = new StringBuilder("DEBUG: ");
        for(Object obj : objects){
            if(obj == null)
                s.append("EMPTY ");
            else
                s.append(obj.toString()).append(" ");
        }
        System.out.println(s);
//        IndustrialRenewal.LOGGER.info(String.valueOf(s));
    }

    public static boolean consumeIfAvailable(Player player, ItemStack itemStack){
        int playerHas = countMaterialInInventory(player.getInventory(), itemStack.getItem());
        if(playerHas >= itemStack.getCount()){
            removeItemFromInventory(player.getInventory(), itemStack.getItem(), itemStack.getCount());
            return true;
        }
        return false;
    }

    public static int countMaterialInInventory(Inventory inventory, Item item){
        return inventory.items.stream().filter(stack -> stack.getItem() == item).mapToInt(ItemStack::getCount).sum();
    }

    public static void removeItemFromInventory(Inventory inventory, Item item, int count) {
        for (ItemStack stack : inventory.items) {
            if (stack.getItem() == item) {
                int toRemove = Math.min(stack.getCount(), count);
                stack.shrink(toRemove);
                count -= toRemove;
                if (count <= 0) {
                    break;
                }
            }
        }
    }

    public static float getStrength(Tiers.TIER tier){
        return switch (tier){
            case THATCH -> WallConfig.THATCH_RESISTANCE.get().floatValue();
            case WOOD -> WallConfig.WOODEN_RESISTANCE.get().floatValue();
            case STONE -> WallConfig.STONE_RESISTANCE.get().floatValue();
            case METAL -> WallConfig.METAL_RESISTANCE.get().floatValue();
            case ARMOR -> WallConfig.ARMORED_RESISTANCE.get().floatValue();
        };
    }

    public static float getStrengthNoConfig(Tiers.TIER tier){
        return switch (tier){
            case THATCH -> 3 * 2;
            case WOOD -> 5 * 2;
            case STONE -> 7.5f * 2;
            case METAL -> 10 * 2;
            case ARMOR -> 12.5f * 2;
        };
    }

    public static BlockBehaviour.Properties getBaseProperty(Tiers.TIER tier){
        return switch (tier){
            case THATCH, WOOD -> BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS);
            case STONE -> BlockBehaviour.Properties.copy(Blocks.COBBLESTONE);
            case METAL, ARMOR -> BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK);
        };
    }
}
