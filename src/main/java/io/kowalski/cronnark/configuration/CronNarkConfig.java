package io.kowalski.cronnark.configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import lombok.Getter;

@Getter
public class CronNarkConfig extends Configuration {

    @Valid
    @NotNull
    @JsonProperty("hikari")
    private HikariBuilder hikari;

    @Valid
    @NotNull
    @JsonProperty("jwtIssuer")
    private String issuer;

    @Valid
    @NotNull
    @JsonProperty("secretKey")
    private String secretKey;

}
