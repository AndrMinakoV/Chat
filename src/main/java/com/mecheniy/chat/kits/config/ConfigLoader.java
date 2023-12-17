package com.mecheniy.chat.kits.config;

import com.mecheniy.chat.kits.Kit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ConfigLoader {
    private final Path configPath;
    private Map<String, Kit> kits = new HashMap<>();

    private static final Logger LOGGER = LogManager.getLogger();

    public ConfigLoader() {
        this.configPath = FMLPaths.CONFIGDIR.get().resolve("kitConfig/kits.yml");
    }

    public void loadKits() {
        LoaderOptions options = new LoaderOptions();
        options.setAllowDuplicateKeys(false);
        Yaml yaml = new Yaml(new Constructor(KitConfig.class, options));
        try (InputStream inputStream = Files.newInputStream(configPath)) {
            Map<String, Object> yamlData = yaml.load(inputStream);

            for (Map.Entry<String, Object> entry : yamlData.entrySet()){
                String kitName = entry.getKey();
                Map<String, Object> kitData = (Map<String, Object>) entry.getValue();
                Kit kit = convertToKit(kitData);
                kits.put(kitName, kit);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Kit convertToKit(Map<String, Object> kitData) {
        List<String> itemData = (List<String>) kitData.get("items");
        List<ItemStack> items = new ArrayList<>();
        for (String itemString : itemData) {
            String[] parts = itemString.split(" ");
            ResourceLocation id = new ResourceLocation(parts[0]);
            int count = Integer.parseInt(parts[1]);
            Item item = ForgeRegistries.ITEMS.getValue(id);
            if (item != null){
                ItemStack itemStack = new ItemStack(item, count);
                items.add(itemStack);
            } else {
                LOGGER.error("Not found id" + id);
            }
        }
        long cooldown = Long.parseLong(kitData.get("cooldown").toString());
        return new Kit(items, cooldown);
    }

    public Map<String, Kit> getKits() {
        return kits;
    }
}
