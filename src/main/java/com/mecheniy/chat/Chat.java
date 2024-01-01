package com.mecheniy.chat;

import com.mecheniy.chat.utilities.DiscordBot;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.mecheniy.chat.utilities.MessageFunctions;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import com.mecheniy.chat.utilities.DiscordBot;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import java.util.logging.Level;

@Mod("chat")
public class Chat {

    public static final String MODID = "chat";
    private static final Logger LOGGER = Logger.getLogger(MODID);

    public Chat() {
        try {
            DiscordBot.initialize();
            LOGGER.info("Discord Bot initialized successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize Discord Bot: ", e);
        }
    }

    @Mod.EventBusSubscriber(modid = Chat.MODID)
    public static class ForgeBeautifulChatEvent {
        public static final double rangeTalk = 100;

        @SubscribeEvent
        public static void onServerChat(ServerChatEvent.Submitted event) {
            ServerPlayer serverPlayer = event.getPlayer();

             /*User user = LuckPermsProvider.get().getUserManager().getUser(serverPlayer.getUUID());
            String prefix = "";
            if (user != null) {
                CachedMetaData metaData = user.getCachedData().getMetaData();
                prefix = metaData.getPrefix();
                if (prefix == null) {
                    prefix = "";
                }
            } */

            LocalTime timeNow = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = timeNow.format(formatter);

            MinecraftServer server = serverPlayer.getServer();
            String playerName = serverPlayer.getName().getString();
            String rawMessage = event.getMessage().getString();
            event.setCanceled(true);
            String chatTag;
            Component formattedMessage;
            if (rawMessage.startsWith("!")) {
                chatTag = "G";
                // Убираем первый символ для глобального чата
                String messageWithoutFirstChar = rawMessage.substring(1);
                formattedMessage = Component.literal("§8[" + "§7" + formattedTime + "§8] " + "[§6" + chatTag + "§8] "  +  /* prefix  + */ " §7" + playerName + "§8: ").append(Component.literal(messageWithoutFirstChar));
                MessageFunctions.broadcastMessageGlobal(server, formattedMessage);
                // Логирование сообщения без первого символа
                System.out.println("[" + formattedTime + "] [" + chatTag + "] " + playerName + ": " + messageWithoutFirstChar);
            } else {
                chatTag = "L";
                formattedMessage = Component.literal("§8[" + "§7" + formattedTime + "§8] " + "[§a" + chatTag + "§8] "  +  /* prefix  + */ " §7" + playerName + "§8: ").append(Component.literal(rawMessage));
                MessageFunctions.broadcastMessageLocal(serverPlayer, formattedMessage);
                // Логирование локального сообщения
                System.out.println("[" + formattedTime + "] [" + chatTag + "] " + playerName + ": " + rawMessage);
            }
    }
}}
