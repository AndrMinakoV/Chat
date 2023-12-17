package com.mecheniy.chat.kits.config;

import net.minecraft.world.item.ItemStack;
import java.util.List;

public class KitConfig {
    private List<String> items; // Предполагаем, что в YAML будут строки
    private long cooldown;

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }
}
