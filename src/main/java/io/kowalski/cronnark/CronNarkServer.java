package io.kowalski.cronnark;

import java.util.Map;

import com.hubspot.dropwizard.guicier.GuiceBundle;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
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
        bootstrap.addBundle(new ViewBundle<CronNarkConfig>() {
            @Override
            public Map<String, Map<String, String>> getViewConfiguration(final CronNarkConfig config) {
                return config.getViewRendererConfiguration();
            }
        });
        bootstrap.addBundle(new AssetsBundle("/assets", "/assets"));
    }

    @Override
    public void run(final CronNarkConfig arg0, final Environment arg1) throws Exception {

    }

}
