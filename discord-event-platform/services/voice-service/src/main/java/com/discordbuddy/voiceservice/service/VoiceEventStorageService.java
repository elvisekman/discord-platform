package com.discordbuddy.voiceservice.service;

import com.discordbuddy.voiceservice.model.VoiceEvent;
import com.discordbuddy.voiceservice.repository.VoiceEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class VoiceEventStorageService {

    private static final Logger logger = LoggerFactory.getLogger(VoiceEventStorageService.class);

    private final VoiceEventRepository repository;

    public VoiceEventStorageService(VoiceEventRepository repository) {
        this.repository = repository;
    }

    public void save(VoiceEvent event) {
        repository.save(event);
        logger.info("Persisted VoiceEvent eventId={} to Cosmos DB", event.getEventId());
    }
}
