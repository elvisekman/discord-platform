package com.discordbuddy.voiceservice.consumer;

import com.discordbuddy.voiceservice.model.VoiceEvent;
import com.discordbuddy.voiceservice.service.VoiceEventStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class VoiceEventConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(VoiceEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final VoiceEventStorageService storageService;

    public VoiceEventConsumer(ObjectMapper objectMapper, VoiceEventStorageService storageService) {
        this.objectMapper = objectMapper;
        this.storageService = storageService;
    }

    @KafkaListener(
            topics = "${discord.kafka.topics.voice:discord.voice}",
            groupId = "${spring.kafka.consumer.group-id:voice-service}"
    )
    public void consume(ConsumerRecord<String, String> record) {
        try {
            VoiceEvent event = objectMapper.readValue(record.value(), VoiceEvent.class);
            logger.info("Received {} eventId={} user={} guild={} joined={} left={}",
                    event.getEventType(),
                    event.getEventId(),
                    event.getUserId(),
                    event.getGuildId(),
                    event.getChannelJoinedId(),
                    event.getChannelLeftId());
            storageService.save(event);
        } catch (Exception e) {
            logger.error("Failed to process voice event from partition={} offset={}: {}",
                    record.partition(), record.offset(), record.value(), e);
        }
    }
}
