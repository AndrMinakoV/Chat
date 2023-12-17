package com.mecheniy.chat.kits;

import net.minecraft.world.item.ItemStack;
import java.util.List;

public class Kit {
    private List<ItemStack> items;
    private long cooldown;

    public Kit(List<ItemStack> items, long cooldown) {
        this.items = items;
        this.cooldown = cooldown;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public long getCooldown() {
        return cooldown;
    }
}
