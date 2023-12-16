package com.mecheniy.chat.utilities;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

public class MessageFunctions {
    public static void broadcastMessage(MinecraftServer server, Component message) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.sendSystemMessage(message);

        }
    }


}
