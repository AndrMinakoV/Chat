package com.mecheniy.chat;


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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                String formattedTime = timeNow.format(formatter);

                MinecraftServer server = serverPlayer.getServer();
                String playerName = serverPlayer.getName().getString();
                String rawMessage = event.getMessage().getString();
                event.setCanceled(true);
                if (rawMessage.startsWith("!")) {
                    Component formattedMessage = Component.literal("§8[" + "§7" +formattedTime + "§8] " + "[§6G§8] "  +  prefix  + " §7" + playerName + "§8: ").append(Component.literal(rawMessage.substring(1)));
                    MessageFunctions.broadcastMessageGlobal(server, formattedMessage);
                } else {
                    Component formattedMessage = Component.literal("§8[" + "§7" +formattedTime + "§8] " + "[§aL§8] "  +  prefix  + " §7" + playerName + "§8: ").append(Component.literal(rawMessage));
                    MessageFunctions.broadcastMessageLocal(serverPlayer, formattedMessage);

                }
            }
        }
    }

