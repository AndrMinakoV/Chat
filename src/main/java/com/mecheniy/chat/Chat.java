package com.mecheniy.chat;


import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.mecheniy.chat.utilities.MessageFunctions;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@Mod("chat")
public class Chat {
    public static final String MODID = "chat";
        @Mod.EventBusSubscriber(modid = Chat.MODID)
        public static class ForgeBeautifulChatEvent {
            public static final double rangeTalk = 100;
            @SubscribeEvent
            public static void onServerChat(ServerChatEvent.Submitted event) {
                LocalTime timeNow = LocalTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                String formattedTime = timeNow.format(formatter);
                ServerPlayer serverPlayer = event.getPlayer();
                MinecraftServer server = serverPlayer.getServer();
                String playerName = serverPlayer.getName().getString();
                String rawMessage = event.getMessage().getString();
                event.setCanceled(true);
                if (rawMessage.startsWith("!")) {
                    Component formattedMessage = Component.literal("§8[" + formattedTime + "] [§6G§8] §c" + playerName + "§8: ").append(Component.literal(rawMessage.substring(1)));
                    MessageFunctions.broadcastMessageGlobal(server, formattedMessage);
                } else {
                    Component formattedMessage = Component.literal("§8[" + formattedTime + "] [§aL§8] §c" + playerName + "§8: ").append(Component.literal(rawMessage));
                    MessageFunctions.broadcastMessageLocal(serverPlayer, formattedMessage);
                    //1
                }
            }
        }
    }

