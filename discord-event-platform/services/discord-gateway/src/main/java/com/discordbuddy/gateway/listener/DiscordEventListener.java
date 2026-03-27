package com.discordbuddy.gateway.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class DiscordEventListener extends ListenerAdapter {

  private static final Logger logger = LoggerFactory.getLogger(DiscordEventListener.class);

  private static final String VOICE_EVENT_TYPE = "VOICE_STATE_UPDATE";

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;
  private final String voiceTopic;

  public DiscordEventListener(
      KafkaTemplate<String, String> kafkaTemplate,
      ObjectMapper objectMapper,
      @Value("${discord.kafka.topics.voice:discord.voice}") String voiceTopic) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
    this.voiceTopic = voiceTopic;
  }

  @Override
  public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
    String userId = event.getMember().getId();
    String guildId = event.getGuild().getId();
    try {
      Map<String, Object> payload = createVoicePayload(event, guildId, userId);
      publishVoiceEvent(guildId, payload);
      logVoiceEvent(event, userId, guildId);
    } catch (Exception e) {
      logger.error("Failed to publish {} user={} guild={} topic={}", VOICE_EVENT_TYPE, userId, guildId, voiceTopic, e);
    }
  }

  private Map<String, Object> createVoicePayload(GuildVoiceUpdateEvent event, String guildId, String userId) {
    Map<String, Object> payload = basePayload(VOICE_EVENT_TYPE, guildId, userId);
    payload.put("channelJoinedId", channelId(event.getChannelJoined()));
    payload.put("channelLeftId", channelId(event.getChannelLeft()));
    addVoiceStateData(payload, event);
    return payload;
  }

  private String channelId(AudioChannel channel) {
    return channel != null ? channel.getId() : null;
  }

  private String channelName(AudioChannel channel) {
    return channel != null ? channel.getName() : "null";
  }

  private void addVoiceStateData(Map<String, Object> payload, GuildVoiceUpdateEvent event) {
    var voiceState = event.getMember().getVoiceState();
    if (voiceState == null) {
      return;
    }

    payload.put("selfMuted", voiceState.isSelfMuted());
    payload.put("selfDeafened", voiceState.isSelfDeafened());
    payload.put("muted", voiceState.isMuted());
    payload.put("deafened", voiceState.isDeafened());
    payload.put("streaming", voiceState.isStream());
  }

  private void publishVoiceEvent(String guildId, Map<String, Object> payload) throws Exception {
    publish(voiceTopic, guildId, payload);
  }

  private void logVoiceEvent(GuildVoiceUpdateEvent event, String userId, String guildId) {
    logger.info("{} user={} guild={} topic={} joined={} left={}",
        VOICE_EVENT_TYPE,
        userId,
        guildId,
        voiceTopic,
        channelName(event.getChannelJoined()),
        channelName(event.getChannelLeft()));
  }

  private Map<String, Object> basePayload(String eventType, String guildId, String userId) {
    Map<String, Object> payload = new LinkedHashMap<>();
    payload.put("eventId", UUID.randomUUID().toString());
    payload.put("eventType", eventType);
    payload.put("version", 1);
    payload.put("guildId", guildId);
    payload.put("userId", userId);
    payload.put("timestamp", Instant.now().toString());
    return payload;
  }

  private void publish(String topic, String key, Map<String, Object> payload) throws Exception {
    String json = objectMapper.writeValueAsString(payload);
    kafkaTemplate.send(topic, key, json);
  }
}
