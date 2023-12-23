package com.mecheniy.chat.utilities;

import com.google.gson.Gson;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.JDA;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Discord {
    private static JDA jda;
    private static String globalChatId;
    private static String localChatId;

    public static void initialize() {
        try {
            // Чтение конфигурационного файла
            Gson gson = new Gson();
            String jsonString = new String(Files.readAllBytes(Paths.get("config/botConfig/bot.json")));
            BotConfig botConfig = gson.fromJson(jsonString, BotConfig.class);

            // Инициализация бота Discord
            jda = JDABuilder.createDefault(botConfig.getToken()).build().awaitReady(); // Блокирует до тех пор, пока JDA не будет готова
            globalChatId = botConfig.getGlobalChatId();
            localChatId = botConfig.getLocalChatId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendDiscordMessage(String chatId, String message) {
        TextChannel channel = jda.getTextChannelById(chatId);
        if (channel != null) {
            channel.sendMessage(message).queue();
        } else {
            System.err.println("The text channel with ID " + chatId + " was not found!");
        }
    }

    private static class BotConfig {
        private String token;
        private String globalChatId;
        private String localChatId;

        public String getToken() {
            return token;
        }

        public String getGlobalChatId() {
            return globalChatId;
        }

        public String getLocalChatId() {
            return localChatId;
        }
    }
}
