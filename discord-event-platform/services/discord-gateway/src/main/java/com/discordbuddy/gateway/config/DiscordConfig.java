package com.discordbuddy.gateway.config;

import com.discordbuddy.gateway.listener.DiscordEventListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscordConfig {

  @Value("${discord.bot.token}")
  private String botToken;

  @Bean
  public JDA jda(DiscordEventListener listener) throws InterruptedException {
    return JDABuilder.createDefault(botToken)
        .enableIntents(
            GatewayIntent.GUILD_VOICE_STATES,
            GatewayIntent.GUILD_MEMBERS)
        .addEventListeners(listener)
        .build()
        .awaitReady();
  }
}
