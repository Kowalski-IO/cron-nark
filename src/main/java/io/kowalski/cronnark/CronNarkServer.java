package io.kowalski.cronnark;

import com.hubspot.dropwizard.guicier.GuiceBundle;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.kowalski.cronnark.configuration.CronNarkConfig;
import io.kowalski.cronnark.configuration.NarkGuiceModule;

public class CronNarkServer extends Application<CronNarkConfig> {

    public CronNarkServer() {

    }

    public static void main(final String[] args) {
        try {
            new CronNarkServer().run(args);
        } catch (final Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public final void initialize(final Bootstrap<CronNarkConfig> bootstrap) {
        final GuiceBundle<CronNarkConfig> guiceBundle = GuiceBundle.defaultBuilder(CronNarkConfig.class)
                .modules(new NarkGuiceModule()).build();
        bootstrap.addBundle(guiceBundle);
    }

    @Override
    public void run(final CronNarkConfig arg0, final Environment arg1) throws Exception {

    }

}
