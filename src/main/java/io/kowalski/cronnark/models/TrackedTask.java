package io.kowalski.cronnark.models;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackedTask implements Serializable {

    private static final long serialVersionUID = 3408305421435979498L;

    private UUID id;
    private String name;
    private Interval interval;
    private String endpoint;

}
