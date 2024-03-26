package com.mecheniy.chat.utilities;

import com.mecheniy.chat.Chat;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber
public class ChatConfigReloadListener {
    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event){
        if (event.getConfig().getModId().equals(Chat.MODID)) {
            System.out.println("Конфигурация была изменена");
            ChatConfig.applyConfig();
        }
    }


}
