package com.discordbuddy.voiceservice.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;

@JsonIgnoreProperties(ignoreUnknown = true)
@Container(containerName = "voice-events")
public class VoiceEvent {

    @Id
    private String eventId;
    private String eventType;
    private int version;

    @PartitionKey
    private String guildId;
    private String userId;
    private String timestamp;
    private String channelJoinedId;
    private String channelLeftId;
    private boolean selfMuted;
    private boolean selfDeafened;
    private boolean muted;
    private boolean deafened;
    private boolean streaming;

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }

    public String getGuildId() { return guildId; }
    public void setGuildId(String guildId) { this.guildId = guildId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getChannelJoinedId() { return channelJoinedId; }
    public void setChannelJoinedId(String channelJoinedId) { this.channelJoinedId = channelJoinedId; }

    public String getChannelLeftId() { return channelLeftId; }
    public void setChannelLeftId(String channelLeftId) { this.channelLeftId = channelLeftId; }

    public boolean isSelfMuted() { return selfMuted; }
    public void setSelfMuted(boolean selfMuted) { this.selfMuted = selfMuted; }

    public boolean isSelfDeafened() { return selfDeafened; }
    public void setSelfDeafened(boolean selfDeafened) { this.selfDeafened = selfDeafened; }

    public boolean isMuted() { return muted; }
    public void setMuted(boolean muted) { this.muted = muted; }

    public boolean isDeafened() { return deafened; }
    public void setDeafened(boolean deafened) { this.deafened = deafened; }

    public boolean isStreaming() { return streaming; }
    public void setStreaming(boolean streaming) { this.streaming = streaming; }
}
