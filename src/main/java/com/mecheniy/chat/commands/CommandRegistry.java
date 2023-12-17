package com.mecheniy.chat.commands;

import com.mecheniy.chat.Chat;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;




@Mod.EventBusSubscriber(modid = Chat.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommandRegistry {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("kit")
                .then(Commands.literal("penis")
                        .executes(context -> kitPenis(context.getSource()))));
    }

    private static int kitPenis(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        ItemStack itemStack = new ItemStack(Items.STONE, 64); // 64 камня

        if (player.getInventory().add(itemStack)) {
            source.sendSuccess(Component.literal("§aВы получили 64 камня!"), true);
        } else {
            source.sendFailure(Component.literal("§cНедостаточно места в инвентаре для камней."));
        }

        return 1;
    }
}
