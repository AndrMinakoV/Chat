package com.mecheniy.chat;

import ca.weblite.objc.Client;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.mecheniy.chat.utilities.MessageFunctions;

import java.util.Iterator;
import java.util.List;

@Mod("chat")
public class Chat {
    public static final String MODID = "chat";
    public Chat() {
    }
    @Mod.EventBusSubscriber(modid = Chat.MODID)
    public static class ForgeBeautifulChatEvent {

        public static final double rangeTalk = 100;
        @SubscribeEvent
        public static void onServerChat(ServerChatEvent.Submitted event) {
                ServerPlayer serverPlayer = event.getPlayer();
                MinecraftServer server = serverPlayer.getServer();
                String playerName = serverPlayer.getName().getString();
                String rawMessage = event.getMessage().getString();


                event.setCanceled(true);
                if (rawMessage.startsWith("!")){

                    Component formattedMessage = Component.literal("§8[§6G§8] [§c" + playerName + "§8] ").append(Component.literal(rawMessage.substring(1)));
                    MessageFunctions.broadcastMessageGlobal(server, formattedMessage);
                } else {
                    Component formattedMessage = Component.literal("§8[§aL§8] [§c" + playerName + "§8] ").append(Component.literal(rawMessage));
                    MessageFunctions.broadcastMessageLocal(serverPlayer, formattedMessage);


                }




            }


    }

}
