package com.mecheniy.chat.utilities;

import com.google.gson.Gson;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.common.util.Snowflake;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DiscordBot {
    private static GatewayDiscordClient client;
    private static String globalChatId;
    private static String localChatId;

    public static void initialize() {
        try {
            // Чтение конфигурационного файла
            Gson gson = new Gson();
            String jsonString = new String(Files.readAllBytes(Paths.get("config/botConfig/bot.json")));
            BotConfig botConfig = gson.fromJson(jsonString, BotConfig.class);

            // Инициализация бота Discord
            client = DiscordClientBuilder.create(botConfig.getToken()).build()
                    .login().block(); // Блокирует до тех пор, пока клиент не будет готов
            globalChatId = botConfig.getGlobalChatId();
            localChatId = botConfig.getLocalChatId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendDiscordMessage(String chatId, String message) {
        client.getChannelById(Snowflake.of(chatId))
                .ofType(MessageChannel.class)
                .flatMap(channel -> channel.createMessage(message))
                .subscribe(null, error -> System.err.println("Error sending message: " + error));
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
