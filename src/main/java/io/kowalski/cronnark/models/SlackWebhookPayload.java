package io.kowalski.cronnark.models;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SlackWebhookPayload implements Serializable {

    private static final long serialVersionUID = 1128365378127292466L;

    private String text;

}
