package io.kowalski.cronnark.models;

import java.io.Serializable;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackedTask implements Serializable {

    private static final long serialVersionUID = 3408305421435979498L;

    private UUID id;
    private String name;
    private ChronoUnit interval;
    private String endpoint;

}
