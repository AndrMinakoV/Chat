package com.mecheniy.chat.commands;

import com.mecheniy.chat.Chat;
import com.mecheniy.chat.utilities.MessageFunctions;
import com.mecheniy.chat.utilities.PermissionUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.neoxygen.neologger.chat.MessageLogger;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Chat.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommandRegistry {
    private static final String path = "/home/andrey/mecheniy/server/MagicRPG1192_logger_public_logs/logs/chat";
    public static void register(CommandDispatcher<CommandSourceStack> source){
        LiteralCommandNode<CommandSourceStack> literalCommandNode = source.register(Commands.literal("m")
                .then(Commands.argument("player", EntityArgument.player())
                        .suggests(PLAYER_SUGGESTIONS_PROVIDER)
                        .then(Commands.argument("message", MessageArgument.message())
                                .executes(context -> {
                                    return sendPrivateMessage(context.getSource(), EntityArgument.getPlayer(context, "player"), MessageArgument.getMessage(context, "message"));
                                }))));

    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event){
        register(event.getDispatcher());
    }

    private static CompletableFuture<Suggestions> suggestPlayerName(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder){
        String input = builder.getRemaining().toLowerCase();
        if (input.isEmpty()){
            context.getSource().getServer().getPlayerList().getPlayers().forEach(serverPlayer ->
                    builder.suggest(serverPlayer.getName().getString()));
        } else {
            context.getSource().getServer().getPlayerList().getPlayers().forEach(serverPlayer -> {
                String playerName = serverPlayer.getName().getString();
                if (playerName.toLowerCase().startsWith(input)){
                    builder.suggest(playerName);
                }
            });
        }
        return builder.buildFuture();
    }

    private static final SuggestionProvider<CommandSourceStack> PLAYER_SUGGESTIONS_PROVIDER
            = CommandRegistry::suggestPlayerName;

    private static int sendPrivateMessage(CommandSourceStack source, ServerPlayer target, Component message) {
        ServerPlayer serverPlayer = (ServerPlayer) source.getEntity();
        if (target.getName().getString().equals(serverPlayer.getName().getString())) {
            MessageFunctions.broadcastMcSkillMessage(serverPlayer, "§8Вы не можете написать сами себе.");
            return 0;
        }
        User senderUser = LuckPermsProvider.get().getUserManager().getUser(serverPlayer.getUUID());
        User targetUser = LuckPermsProvider.get().getUserManager().getUser(target.getUUID());
        String prefixSender = "", prefixTarget = "";
        if (senderUser != null) {
            CachedMetaData metaData = senderUser.getCachedData().getMetaData();
            prefixSender = metaData.getPrefix();
            if (prefixSender == null) {
                prefixSender = "§8[§7Игрок§8]";
            }
        }
        if (targetUser != null) {
            CachedMetaData metaData = targetUser.getCachedData().getMetaData();
            prefixTarget = metaData.getPrefix();
            if (prefixTarget == null) {
                prefixTarget = "§8[§7Игрок§8]";
            }
        }
        LocalTime timeNow = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = timeNow.format(timeFormatter);
        String finalMessage = message.getString();
        String timeInMessage = "§8[" + "§7" + formattedTime + "§8] ";
        if(PermissionUtils.hasPermission(senderUser, "chat.pmcolor")){
            finalMessage = finalMessage.replaceAll("&([0-9a-fr)])", "§$1");
        }
        serverPlayer.sendSystemMessage(Component.literal(timeInMessage + "§7Вы §6-> " + prefixSender + "§7 " + serverPlayer.getName().getString() + "§8: §r" + finalMessage));
        target.sendSystemMessage(Component.literal(timeInMessage + prefixTarget + "§7 " + serverPlayer.getName().getString() + "§6 -> §7Вы§8: §r" + finalMessage));

        return 1;
    }
}
