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
    private String serviceName;
    String serviceID;
    Timer timer = new Timer();
    Logger log = LoggerFactory.getLogger(TaskListResource.class);

    public ConsulHealthReporter(String consulURL, long delay, TaskListHealthResource healthResource, String serviceName, String serviceID) {
        this.delay = delay;
        this.healthResource = healthResource;
        this.serviceID = serviceID;
        this.serviceName = serviceName;

        consulConnector=new ConsulConnector(consulURL);

        ConsulResponse<List<ServiceHealth>> allServiceInstances = consulConnector.getConsul().healthClient().getAllServiceInstances(serviceName);
        int size = allServiceInstances.getResponse().size();
        this.serviceID = new Integer(size + 1).toString();

    }

    public void registerAndStartTimer() {
        timer.schedule(this, delay, delay);
        try {
            consulConnector.getAgentClient().register(8080, 30L, serviceName, serviceID);
            consulConnector.getAgentClient().pass(serviceID, "First"); // check in with Consul, serviceId required only.  client will prepend "service:" for service level checks.
            log.info("Register" + serviceName + serviceID);
        } catch (NotRegisteredException e) {
            timer.cancel();
            log.error("Consul Timer inactive " + serviceName + serviceID);
            e.printStackTrace();
        }
    }

    public void deregister() {
        consulConnector.getAgentClient().deregister(serviceID);
        timer.cancel();
        log.info("Deregister" + serviceName + serviceID);
    }


    // this method performs the task
    public void run() {
        log.debug("timer working" + healthResource.getHealthStatus());

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




