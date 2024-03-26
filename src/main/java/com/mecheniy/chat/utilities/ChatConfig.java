package com.mecheniy.chat.utilities;


import net.minecraftforge.common.ForgeConfigSpec;

import java.awt.geom.GeneralPath;

public class ChatConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec.ConfigValue<String> chatName;

    public static final ForgeConfigSpec spec;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("General Settings").push("general");

        chatName = builder.comment("Отображение названия сервера в чате").define("chatName", "Server");

        builder.pop();

        spec = builder.build();
    }

    public static String curChatName = ChatConfig.chatName.get();

    public static void applyConfig(){
        curChatName = ChatConfig.chatName.get();
        System.out.println("Конфигурация чата была обновлена");
    }


}
