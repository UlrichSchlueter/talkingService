package com.ulrichschlueter.talkingService;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.NotRegisteredException;
import com.orbitz.consul.model.ConsulResponse;
import com.orbitz.consul.model.health.ServiceHealth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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

        try {
            consulConnector.getAgentClient().register(applicationPort, 30L, serviceName, serviceID);
            consulConnector.getAgentClient().pass(serviceID, "First"); // check in with Consul, serviceId required only.  client will prepend "service:" for service level checks.
            log.info("Register" + serviceName + serviceID);
        } catch (NotRegisteredException e) {
            timer.cancel();
            log.error("Consul Timer inactive " + serviceName + serviceID);
            e.printStackTrace();
        }
    }

    public void deregister() {
        String serviceName=consulConnector.getServiceName();
        String serviceID=consulConnector.getServiceID();
        consulConnector.getAgentClient().deregister(serviceID);
        timer.cancel();
        log.info("Deregister" + serviceName + serviceID);
    }


    // this method performs the task
    public void run() {
        log.debug("timer working" + healthResource.getHealthStatus());
        String serviceName=consulConnector.getServiceName();
        String serviceID=consulConnector.getServiceID();
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
}




