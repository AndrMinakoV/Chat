package com.mecheniy.chat;

import ca.weblite.objc.Client;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.mecheniy.chat.utilities.MessageFunctions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("chat")
public class Chat {
    public static final String MODID = "chat";
        @Mod.EventBusSubscriber(modid = Chat.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
        public static class CommandRegistry {
            @SubscribeEvent
            public static void onRegisterCommands(RegisterCommandsEvent event) {
                // Регистрируем новую команду
                event.getDispatcher().register(Commands.literal("kit")
                        .then(Commands.literal("penis")
                                .executes(context -> kitPenis(context.getSource()))));
            }

            private static int kitPenis(CommandSourceStack source) {
                if (source.getEntity() instanceof ServerPlayer) {
                    ServerPlayer player = (ServerPlayer) source.getEntity();
                    // Создаем новый предмет (камень)
                    ItemStack itemStack = new ItemStack(Items.STONE, 64); // 64 камня

                    // Проверяем, есть ли место в инвентаре
                    if (player.getInventory().add(itemStack)) {
                        // Уведомляем игрока о получении предмета
                        source.sendSuccess(Component.literal("§aВы получили 64 камня!"), true);
                    } else {
                        // Если нет места, сообщаем об этом
                        source.sendFailure(Component.literal("§cНедостаточно места в инвентаре для камней."));
                    }
                } else {
                    source.sendFailure(Component.literal("§cЭту команду может использовать только игрок."));
                }
                return 1; // Возвращаем успешный результат выполнения команды
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
}
