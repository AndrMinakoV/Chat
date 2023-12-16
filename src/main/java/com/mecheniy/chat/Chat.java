package com.mecheniy.chat;

import ca.weblite.objc.Client;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.mecheniy.chat.utilities.MessageFunctions;

@Mod("chat")
public class Chat {
    public static final String MODID = "chat";
    public Chat() {
    }
    @Mod.EventBusSubscriber(modid = Chat.MODID)
    public static class ForgeBeautifulChatEvent {
        @SubscribeEvent
        public static void onServerChat(ServerChatEvent event) {
            ServerPlayer serverPlayer = event.getPlayer();
            String rawMessage = event.getMessage().getString();
            Component formattedMessage = Component.literal("§8[§6G§8] [§c" + serverPlayer.getName().getString() + "§8] ").append(Component.literal(rawMessage));
           event.setCanceled(true);
            MessageFunctions.broadcastMessage(serverPlayer.getServer(), formattedMessage);
        }
    }

}
