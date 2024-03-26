package com.mecheniy.chat;

import com.mecheniy.chat.utilities.ChatConfig;
import com.mecheniy.chat.utilities.MessageFunctions;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.neoxygen.neologger.NeoLogger;
import com.neoxygen.neologger.chat.MessageLogger;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import com.mecheniy.chat.utilities.MessageFunctions;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("chat")
public class Chat {
    public static final String MODID = "chat";
    private static final Logger LOGGER = Logger.getLogger(MODID);

    public Chat(){
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ChatConfig.spec);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Здесь считываем конфигурацию
        String chatName = ChatConfig.chatName.get();
        // Применяем настройки, считанные из конфига
        System.out.println("Название чата из конфига: " + chatName);
        // Здесь можете добавить код, который должен выполниться с учетом настроек конфига перед запуском сервера
    }
    @Mod.EventBusSubscriber(modid = Chat.MODID)
    public static class ForgeBeautifulChatEvent {

        public static final double rangeTalk = 100;

        @SubscribeEvent
        public static void onServerChat(ServerChatEvent.Submitted event) {
            ServerPlayer serverPlayer = event.getPlayer();
            User user = LuckPermsProvider.get().getUserManager().getUser(serverPlayer.getUUID());
            String prefix = "";
            if (user != null) {
                CachedMetaData metaData = user.getCachedData().getMetaData();
                prefix = metaData.getPrefix();
                if (prefix == null) {
                    prefix = "";
                }
            }


            LocalTime timeNow = LocalTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = timeNow.format(timeFormatter);
            LocalDate dateNow = LocalDate.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = dateNow.format(dateFormatter);
            MinecraftServer server = serverPlayer.getServer();
            String playerName = serverPlayer.getName().getString();
            String rawMessage = event.getMessage().getString();
            event.setCanceled(true);
            String chatTag;
            Component formattedMessage;

           if (rawMessage.startsWith("!")){
                chatTag = "G";
                String messageWithoutFirstChar = rawMessage.substring(1);
                formattedMessage = Component.literal("§8[" + "§7" + formattedTime + "§8] " + "[§6" + chatTag + "§8] "  + prefix + " §7" + playerName + "§8: ").append(Component.literal(messageWithoutFirstChar.replaceAll("&([0-9a-fk-or])", "§$1")));
                MessageFunctions.broadcastMessageGlobal(server, formattedMessage);
                MessageLogger.logMessageToFile(("[" + formattedTime + " | " +  formattedDate + "] " +" [" + chatTag + "] " + prefix + " " + playerName + ": " + messageWithoutFirstChar).replaceAll("[&§]([0-9a-fk-or])", ""));
                System.out.println(("§8[" + "§7" + formattedTime + "§8] " + "[§6" + chatTag + "§8] "  + prefix + " §7" + playerName + "§8: " + messageWithoutFirstChar).replaceAll("[&§]([0-9a-fk-or])", ""));

            }
            else {
                chatTag = "L";
                formattedMessage = Component.literal("§8[" + "§7" + formattedTime + "§8] " + "[§a" + chatTag + "§8] " + prefix + " §7" + playerName + "§8: ").append(Component.literal(rawMessage.replaceAll("&([0-9a-fk-or])", "§$1")));
                MessageFunctions.broadcastMessageLocal(serverPlayer, formattedMessage);
                MessageLogger.logMessageToFile(("[" + formattedTime + " | " + formattedDate + "] " +" [" +chatTag + "] " + prefix + " " + playerName + ": " + rawMessage).replaceAll("[&§]([0-9a-fk-or])", ""));
                System.out.println(("§8[" + "§7" + formattedTime + "§8] " + "[§6" + chatTag + "§8] "  + prefix + " §7" + playerName + "§8: " + rawMessage).replaceAll("[&§]([0-9a-fk-or])", ""));
            }
        }
     }
}
