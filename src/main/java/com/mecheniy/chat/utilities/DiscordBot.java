package com.mecheniy.chat.utilities;

import com.mecheniy.chat.utilities.commands.Info;
//import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class DiscordBot {
    private static ShardManager shardManager;
    //private static Dotenv config;

    public static void initialize() throws LoginException {
       // config = Dotenv.configure().ignoreIfMissing().load();
        String token = ("NzQ2NDgzNTgzMjQxNDIwOTQx.GGv-tQ.GEfPyFy2g6qdKBNwLH1cn_cTgqJEL2V1atNqz4");
        if (token == null || token.isEmpty()) {
            throw new LoginException("Token not found in .env file.");
        }

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                GatewayIntent.SCHEDULED_EVENTS,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("DxD"));
        builder.setMemberCachePolicy(MemberCachePolicy.VOICE);
        builder.enableCache(CacheFlag.VOICE_STATE);
        shardManager = builder.build();

        shardManager.addEventListener(new CommandManager(), new Info());
    }

    public static ShardManager getShardManager() {
        return shardManager;
    }

   /* public static Dotenv getConfig() {
        return config;
    }*/

    // Этот метод main не нужен для инициализации в моде и может быть удалён,
    // если вы не планируете запускать DiscordBot как отдельное приложение.
    public static void main(String[] args) {
        try {
            DiscordBot.initialize();
        } catch (LoginException e) {
            e.printStackTrace();
            System.out.println("ERROR: Provided bot token is invalid!");
        }
    }
}
