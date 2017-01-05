package com.ulrichschlueter.talkingService;

import com.orbitz.consul.NotRegisteredException;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by uli on 02.01.17.
 */
public class ConsulHealthReporter extends TimerTask {
    private final ConsulConnector consulConnector;
    private long delay = 15000; // milliseconds

    private TaskListHealthResource healthResource;
    //private String serviceName;
    private int applicationPort=0;
    //String serviceID;
    Timer timer = new Timer();
    Logger log = LoggerFactory.getLogger(ConsulHealthReporter.class);


    public ConsulHealthReporter(ConsulConnector consulConnector, long delay, TaskListHealthResource healthResource) {
        this.delay = delay;
        this.healthResource = healthResource;
        this.consulConnector=consulConnector;
    }

    public void registerAndStartTimer(int applicationPort) {
        timer.schedule(this, delay, delay);
        this.applicationPort=applicationPort;
        String serviceName=consulConnector.getServiceName();
        String serviceID=consulConnector.getServiceID();

        Registration registration = ImmutableRegistration.builder()
                .check(Registration.RegCheck.ttl(30L))
                .port(applicationPort)
                .name(serviceName)
                .id(serviceID)
                .address(getIp())
                .build();
        try {
            consulConnector.getAgentClient().register(registration);
            consulConnector.getAgentClient().pass(serviceID, "First"); // check in with Consul, serviceId required only.  client will prepend "service:" for service level checks.
            log.info("Register" + serviceName + serviceID);
        } catch (NotRegisteredException e) {
            timer.cancel();
            log.error("Consul Timer inactive " + serviceName + serviceID);
            e.printStackTrace();
        }
    }

    public void deregister() {
        String serviceName = consulConnector.getServiceName();
        String serviceID = consulConnector.getServiceID();
        consulConnector.getAgentClient().deregister(serviceID);
        timer.cancel();
        log.info("Deregister" + serviceName + serviceID);
    }


    // this method performs the task
    public void run() {
        log.debug("timer working" + healthResource.getHealthStatus());
        String serviceName = consulConnector.getServiceName();
        String serviceID = consulConnector.getServiceID();
        try {
            if (healthResource.isHealthy()) {
                consulConnector.getAgentClient().pass(serviceID, healthResource.getHealthStatus().toString());
            } else {
                consulConnector.getAgentClient().fail(serviceID, healthResource.getHealthStatus().toString());
            }
        } catch (NotRegisteredException e) {
            e.printStackTrace();
        }
    }

    private String getIp() {
        String ip = "127.0.0.1";
        try {
            Process p = Runtime.getRuntime().exec("hostname -i");

            p.waitFor();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = reader.readLine();
            if (line != null) {
                ip = line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ip;
    }
}




