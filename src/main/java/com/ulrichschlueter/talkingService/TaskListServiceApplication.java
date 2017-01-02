package com.ulrichschlueter.talkingService;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskListServiceApplication extends Application<TaskListServiceConfiguration>{

    final  static String SERVICENAME = "TalkingService";
    Logger log = LoggerFactory.getLogger(TaskListResource.class);

    public static void main(String[] args) throws Exception {
        new TaskListServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "talking-service";
    }

    @Override
    public void initialize(Bootstrap<TaskListServiceConfiguration> bootstrap) {
        // nothing to do yet
            //bootstrap.addBundle(new AssetsBundle("/assets/css", "/css", null, "css"));
            //bootstrap.addBundle(new AssetsBundle("/assets/js", "/js", null, "js"));
            //bootstrap.addBundle(new AssetsBundle("/assets/pages", "/", "index.html", "html"));
            }

    @Override
    public void run(TaskListServiceConfiguration configuration,
                    Environment environment) {

        // register resource now
        final TaskListResource resource = new TaskListResource(
                configuration.getMaxLength()
        );

        final TaskListShutdownResource shutdownResource = new TaskListShutdownResource(
        );

        final TaskListHealthResource healthResource = new TaskListHealthResource(
        );

        final ConsulTaskWorkerResource workerResource = new ConsulTaskWorkerResource(
                configuration.getConsulURL()
        );


        environment.jersey().register(resource);
        environment.jersey().register(shutdownResource);
        environment.jersey().register(healthResource);
        environment.jersey().register(workerResource);

        if (configuration.isConsulEnabled()) {
            final ConsulHealthReporter consulHealthReporter = new ConsulHealthReporter(configuration.getConsulURL(), 5000, healthResource, SERVICENAME, "1234");

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    consulHealthReporter.deregister();
                }
            });

            consulHealthReporter.registerAndStartTimer();
        }
        else
        {
            log.info("Consul Integration Disabled");
        }



        environment.healthChecks().register("fakeHealth", new TaskListHealthCheck(healthResource));
    }



}