package com.mecheniy.chat.utilities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

public class MessageFunctions {
    public static void broadcastMessage(MinecraftServer server, Component message) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.sendSystemMessage(message);

        }
    }

    public static double compareCoordinateDistance(BlockPos playerPos1, BlockPos playerPos2){
        double x = (double) Math.abs(playerPos1.getX() - playerPos2.getX());
        double y = (double) Math.abs(playerPos1.getY() - playerPos2.getY());
        double z = (double) Math.abs(playerPos1.getZ() - playerPos2.getZ());
        return x + y + z;
    }


}
