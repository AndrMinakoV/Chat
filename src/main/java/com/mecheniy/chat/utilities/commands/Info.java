package com.mecheniy.chat.utilities.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Info extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("info")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Information");
            embed.setDescription("Choose an option:");
            embed.setColor(5446170);
            embed.setAuthor("Rias");
            embed.setThumbnail("https://cdn.discordapp.com/attachments/963878656726409281/1090601321834496092/artworks-a97bKtSqgVNU2sM9-0enDuQ-t500x500.jpg");
            embed.setFooter("©Project Rias • 2023");

            event.replyEmbeds(embed.build())
                    .addActionRow(
                            StringSelectMenu.create("informationMenu")
                                    .setPlaceholder("Choose an option:")
                                    .addOptions(SelectOption.of("Commands", "commands")
                                            .withDescription("Commands list"))
                                    .addOptions(SelectOption.of("Bot information", "bInfo")
                                            .withDescription("Bot information"))
                                    .addOptions(SelectOption.of("Support", "support")
                                            .withDescription("Support"))
                                    .build()) // Вызовите метод build() здесь, чтобы получить StringSelectMenu

                    .queue();
        }
    }
}
