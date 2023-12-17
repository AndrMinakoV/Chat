package com.mecheniy.chat.commands;

import com.mecheniy.chat.Chat;
import com.mecheniy.chat.kits.Kit;
import com.mecheniy.chat.kits.config.ConfigLoader;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Chat.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommandRegistry {

    private static final Logger LOGGER = LogManager.getLogger();
    private static Map<String, Kit> kits;
    private static Map<UUID, Long> lastKitTimes = new HashMap<>();

    private static void initConfigLoader() {
        if (kits == null) {
            try {
                ConfigLoader configLoader = new ConfigLoader();
                configLoader.loadKits();
                kits = configLoader.getKits();
            } catch (Exception e) {
                LOGGER.error("Failed to load kits configuration: ", e);
                kits = new HashMap<>(); // Initialize with an empty map to prevent further errors
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("kit")
                .then(Commands.argument("kitName", StringArgumentType.string())
                        .executes(context -> kitCommand(context.getSource(), StringArgumentType.getString(context, "kitName")))));
    }

    private static int kitCommand(CommandSourceStack source, String kitName) {
        initConfigLoader(); // Ensure configuration is loaded
        ServerPlayer player;
        try {
            player = source.getPlayerOrException();
        } catch (CommandSyntaxException e) {
            source.sendFailure(Component.literal("Только игрок может использовать эту команду."));
            return 0;
        }

        Kit kit = kits.get(kitName);
        if (kit == null) {
            source.sendFailure(Component.literal("Кит \"" + kitName + "\" не найден."));
            return 0;
        }

        // Проверяем, прошло ли достаточно времени с последнего получения кита
        long lastTime = lastKitTimes.getOrDefault(player.getUUID(), 0L);
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime < kit.getCooldown()) {
            source.sendFailure(Component.literal("Вы ещё не можете получить этот кит."));
            return 0;
        }

        // Выдаём предметы из кита
        for (ItemStack item : kit.getItems()) {
            if (!player.getInventory().add(item)) {
                source.sendFailure(Component.literal("Недостаточно места в инвентаре."));
                return 0;
            }
        }

        lastKitTimes.put(player.getUUID(), currentTime); // Обновляем время получения кита
        source.sendSuccess(Component.literal("Вы получили кит \"" + kitName + "\"."), true);
        return 1;
    }
}
