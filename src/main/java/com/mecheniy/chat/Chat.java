package com.mecheniy.chat;

import com.mecheniy.chat.utilities.MessageFunctions;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import com.mecheniy.chat.utilities.MessageFunctions;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.minecraft.commands.SharedSuggestionProvider;

@Mod("chat")
public class Chat {
    public static final String MODID = "chat";
    private static final Logger LOGGER = Logger.getLogger(MODID);

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
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = timeNow.format(timeFormatter);
            LocalDate dateNow = LocalDate.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = dateNow.format(dateFormatter);
            MinecraftServer server = serverPlayer.getServer();
            String playerName = serverPlayer.getName().getString();
            String rawMessage = event.getMessage().getString();
            event.setCanceled(true);
            String chatTag;
            Component formattedMessage;

           if (rawMessage.startsWith("!")){
                chatTag = "G";
                String messageWithoutFirstChar = rawMessage.substring(1);
                formattedMessage = Component.literal("§8[" + "§7" + formattedTime + "§8] " + "[§6" + chatTag + "§8] "  + prefix + " §7" + playerName + "§8: ").append(Component.literal(messageWithoutFirstChar));                MessageFunctions.broadcastMessageGlobal(server, formattedMessage);
                logMessageToFile("[" + formattedTime + " | " +  formattedDate + "] " +" [" + chatTag + "] " + prefix + " " + playerName + ": " + messageWithoutFirstChar.replaceAll("§.", ""));
                System.out.println("§8[" + "§7" + formattedTime + "§8] " + "[§6" + chatTag + "§8] "  + prefix + " §7" + playerName + "§8: " + messageWithoutFirstChar);

            }
            else {
                chatTag = "L";
                formattedMessage = Component.literal("§8[" + "§7" + formattedTime + "§8] " + "[§a" + chatTag + "§8] " + prefix + " §7" + playerName + "§8: ").append(Component.literal(rawMessage));
                MessageFunctions.broadcastMessageLocal(serverPlayer, formattedMessage);
                logMessageToFile("[" + formattedTime + " | " + formattedDate + "] " +" [" +chatTag + "] " + prefix + " " + playerName + ": " + rawMessage.replaceAll("§.", ""));
                System.out.println("§8[" + "§7" + formattedTime + "§8] " + "[§6" + chatTag + "§8] "  + prefix + " §7" + playerName + "§8: ");
            }
        }

        private static void logMessageToFile(String message) {
            LocalDate dateNow = LocalDate.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = dateNow.format(dateFormatter);
            String logDirectoryPath = "/home/mecheniy/server/MagicRPG1192_logger_public_logs/logs"; // Указываем путь к директории логов
            String fileName = formattedDate + ".txt";

            File logDirectory = new File(logDirectoryPath);
            if (!logDirectory.exists()) {
                logDirectory.mkdirs(); // Создаем директорию, если она не существует
            }

            File logFile = new File(logDirectory, fileName);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                // Логируем путь к файлу для устранения неполадок
                LOGGER.info("Writing to log file: " + logFile.getAbsolutePath());

                if (!logFile.exists()) {
                    boolean fileCreated = logFile.createNewFile();
                    LOGGER.info("Log file created: " + fileCreated + " Path: " + logFile.getAbsolutePath());
                }
                writer.write(message);
                writer.newLine();
            } catch (IOException e) {
                LOGGER.severe("Could not write to log file: " + e.getMessage());
            }
        }
        @Mod.EventBusSubscriber
        public static class CommandLogger {


            @SubscribeEvent
            public static void onCommand(CommandEvent event) {
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate dateNow = LocalDate.now();
                LocalTime timeNow = LocalTime.now();
                String formattedTime = timeNow.format(timeFormatter);
                String formattedDate = dateNow.format(dateFormatter);
                CommandSourceStack source = event.getParseResults().getContext().getSource();
                try {
                    ServerPlayer player = source.getPlayerOrException();
                    String playerName = player.getName().getString(); // Теперь мы получаем имя игрока таким образом
                    String command = event.getParseResults().getReader().getString();
                    logMessageToFile("[" + formattedTime + " | " + formattedDate + "] " + playerName + " использовал команду: " + command);
                } catch (CommandSyntaxException e) {
                    logMessageToFile("A non-player source executed command: ");
                }
            }





            @Mod.EventBusSubscriber(modid = Chat.MODID)
            public class PlayerActivityLogger {


                @SubscribeEvent
                public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate dateNow = LocalDate.now();
                    LocalTime timeNow = LocalTime.now();
                    String formattedTime = timeNow.format(timeFormatter);
                    String formattedDate = dateNow.format(dateFormatter);
                    ServerPlayer player = (ServerPlayer) event.getEntity();
                    String playerName = player.getGameProfile().getName();
                    logMessageToFile("[" + formattedTime + " | " + formattedDate + "] " + playerName + " зашел.");
                }

                @SubscribeEvent
                public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate dateNow = LocalDate.now();
                    LocalTime timeNow = LocalTime.now();
                    String formattedTime = timeNow.format(timeFormatter);
                    String formattedDate = dateNow.format(dateFormatter);
                    ServerPlayer player = (ServerPlayer) event.getEntity();
                    String playerName = player.getGameProfile().getName();
                    logMessageToFile("[" + formattedTime + " | " + formattedDate + "] " + playerName + " вышел.");
                }

            }}




        }
}
