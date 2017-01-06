package com.ulrichschlueter.talkingService;

import io.dropwizard.Application;

import io.dropwizard.bundles.assets.AssetsBundleConfiguration;
import io.dropwizard.bundles.assets.ConfiguredAssetsBundle;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.smartmachine.couchbase.CouchbaseBundle;
import io.smartmachine.couchbase.CouchbaseClientFactory;
import jersey.repackaged.com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;

import static jersey.repackaged.com.google.common.collect.ImmutableMap.builder;

public class TaskListServiceApplication extends Application<TaskListServiceConfiguration> {

    final static String SERVICENAME = "TalkingService";
    Logger log = LoggerFactory.getLogger(TaskListResource.class);

    public static void main(String[] args) throws Exception {
        new TaskListServiceApplication().run(args);
    }

    private final CouchbaseBundle<TaskListServiceConfiguration> couchbaseBundle = new CouchbaseBundle<TaskListServiceConfiguration>() {

        @Override
        public CouchbaseClientFactory getCouchbaseClientFactory(TaskListServiceConfiguration configuration) {
            return configuration.getCouchbaseClientFactory();
        }
    };

    @Override
    public String getName() {
        return "talking-service";
    }

    @Override
    public void initialize(Bootstrap<TaskListServiceConfiguration> bootstrap) {
        // nothing to do yet
        //bootstrap.addBundle(new ConfiguredAssetsBundle("/assets/", "/anything/"));
        bootstrap.addBundle(new ConfiguredAssetsBundle()); //take the definitions from the config yaml file (assets/mappings)

        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor())
        );

        bootstrap.addBundle(couchbaseBundle);
    }


    @Override
    public void run(TaskListServiceConfiguration configuration,
                    Environment environment) {

        // register resource now

        final Client client = new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration())
                .build(getName());


        final TaskListResource resource = new TaskListResource(
                configuration.getMaxLength()
        );

        final TaskListShutdownResource shutdownResource = new TaskListShutdownResource(
        );

        final TaskListHealthResource healthResource = new TaskListHealthResource(
        );

        final ConsulConnector consulConnector = new ConsulConnector(configuration.getConsulURL(), SERVICENAME);

        final ConsulTaskWorkerResource workerResource = new ConsulTaskWorkerResource(consulConnector, client);




        environment.jersey().register(resource);
        environment.jersey().register(shutdownResource);
        environment.jersey().register(healthResource);
        environment.jersey().register(workerResource);
        environment.healthChecks().register("fakeHealth", new TaskListHealthCheck(healthResource));


        if (configuration.isConsulEnabled()) {
            final ConsulHealthReporter consulHealthReporter = new ConsulHealthReporter(consulConnector
                    , 5000, healthResource);

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    consulHealthReporter.deregister();
                }
            });
            RuntimeInfo runtimeInfo = new RuntimeInfo(consulHealthReporter);
            environment.lifecycle().addServerLifecycleListener(runtimeInfo);

        } else {
            log.info("Consul Integration Disabled");
        }


    }


}