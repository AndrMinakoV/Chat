package com.mecheniy.chat.utilities;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();

        // Добавьте команду "info"
        commandData.add(Commands.slash("info", "Bot information + commands"));

        event.getJDA().updateCommands().addCommands(commandData).queue();
    }
}

