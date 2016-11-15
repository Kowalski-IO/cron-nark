package io.kowalski.cronnark.models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackedTask implements Serializable {

    private static final long serialVersionUID = 3408305421435979498L;

    private UUID id;
    private String name;
    private Interval interval;

    @JsonProperty(access = Access.WRITE_ONLY)
    private String webhook;

    @JsonIgnore
    private Timestamp lastCheckIn;

    @JsonProperty
    public LocalDateTime getLastCheckInLDT() {
        return lastCheckIn != null ? LocalDateTime.ofInstant(lastCheckIn.toInstant(), ZoneId.systemDefault()) : null;
    }

}
