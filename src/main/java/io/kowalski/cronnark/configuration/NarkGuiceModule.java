package io.kowalski.cronnark.configuration;

import com.google.inject.Binder;
import com.google.inject.name.Names;
import com.hubspot.dropwizard.guicier.DropwizardAwareModule;
import com.zaxxer.hikari.HikariDataSource;

import io.kowalski.cronnark.resources.TaskResource;
import io.kowalski.cronnark.services.TrackedService;

public class NarkGuiceModule extends DropwizardAwareModule<CronNarkConfig> {

    public void configure(final Binder binder) {
        bindConstants(binder);
        bindServices(binder);
        bindResources(binder);
    }

    private void bindConstants(final Binder binder) {
        binder.bind(HikariDataSource.class).toInstance(getConfiguration().getHikari().buildDatasource());
        binder.bind(String.class).annotatedWith(Names.named("secretKey")).toInstance(getConfiguration().getSecretKey());
        binder.bind(String.class).annotatedWith(Names.named("issuer")).toInstance(getConfiguration().getIssuer());
    }

    private void bindServices(final Binder binder) {
        binder.bind(TrackedService.class);
    }

    private void bindResources(final Binder binder) {
        binder.bind(TaskResource.class);
    }

}
