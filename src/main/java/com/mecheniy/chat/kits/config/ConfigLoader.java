package com.mecheniy.chat.kits.config;

import com.mecheniy.chat.kits.Kit;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.LoaderOptions;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfigLoader {
    private final Path configPath;
    private Map<String, Kit> kits = new HashMap<>();

    public ConfigLoader() {
        this.configPath = FMLPaths.CONFIGDIR.get().resolve("kitConfig/kits.yml");
    }

    public void loadKits() {
        LoaderOptions options = new LoaderOptions();
        options.setAllowDuplicateKeys(false);
        Yaml yaml = new Yaml(new Constructor(KitConfig.class, options));
        try (InputStream inputStream = Files.newInputStream(configPath)) {
            // Ваша логика загрузки и обработки YAML
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Kit convertToKit(KitConfig kitConfig) {
        // Тут должна быть ваша логика преобразования из KitConfig в Kit.
        // Например, вы можете разобрать строку из KitConfig для создания ItemStack.
        // Возвращаем пустой Kit для примера.
        return new Kit(new ArrayList<>(), kitConfig.getCooldown());
    }

    public Map<String, Kit> getKits() {
        return kits;
    }
}
