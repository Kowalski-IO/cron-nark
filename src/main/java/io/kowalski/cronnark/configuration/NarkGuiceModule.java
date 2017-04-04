package io.kowalski.cronnark.configuration;

import com.google.inject.Binder;
import com.google.inject.name.Names;
import com.hubspot.dropwizard.guicier.DropwizardAwareModule;
import com.zaxxer.hikari.HikariDataSource;

import io.kowalski.cronnark.filter.JWTFilter;
import io.kowalski.cronnark.resources.StatusPageResource;
import io.kowalski.cronnark.resources.TrackedTaskResource;
import io.kowalski.cronnark.services.SlackService;
import io.kowalski.cronnark.services.SnitchService;
import io.kowalski.cronnark.services.TrackedTaskService;

public class NarkGuiceModule extends DropwizardAwareModule<CronNarkConfig> {

    @Override
    public void configure(final Binder binder) {
        bindConstants(binder);
        bindServices(binder);
        bindFilter(binder);
        bindResources(binder);
    }

    private void bindConstants(final Binder binder) {
        binder.bind(HikariDataSource.class).toInstance(getConfiguration().getHikari().buildDatasource());
        binder.bind(String.class).annotatedWith(Names.named("secretKey")).toInstance(getConfiguration().getSecretKey());
        binder.bind(String.class).annotatedWith(Names.named("issuer")).toInstance(getConfiguration().getIssuer());
    }

    private void bindServices(final Binder binder) {
        binder.bind(SlackService.class).asEagerSingleton();
        binder.bind(SnitchService.class).asEagerSingleton();
        binder.bind(TrackedTaskService.class);
    }

    private void bindFilter(final Binder binder) {
        binder.bind(JWTFilter.class);
    }

    private void bindResources(final Binder binder) {
        binder.bind(StatusPageResource.class);
        binder.bind(TrackedTaskResource.class);
    }

}
