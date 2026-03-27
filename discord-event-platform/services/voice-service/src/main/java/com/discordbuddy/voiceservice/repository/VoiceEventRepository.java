package com.discordbuddy.voiceservice.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.discordbuddy.voiceservice.model.VoiceEvent;

public interface VoiceEventRepository extends CosmosRepository<VoiceEvent, String> {
}
