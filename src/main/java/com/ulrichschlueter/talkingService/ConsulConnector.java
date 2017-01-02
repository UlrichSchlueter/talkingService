package com.ulrichschlueter.talkingService;

import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by uli on 02.01.17.
 */
public class ConsulConnector {
    private Consul consul = null;
    private AgentClient agentClient=null;

    Logger log = LoggerFactory.getLogger(ConsulConnector.class);

    public ConsulConnector(String consulURL) {
        if (consulURL == null || consulURL.equals("")) {
            log.info("Using default consul configuration");
            consul = Consul.builder().build(); // connect to Consul on localhost
        } else {
            log.info("Using custom consul configuration:" +consulURL);
            consul = Consul.builder().withUrl(consulURL).build(); // connect to Consul to given URL
        }

        this.agentClient = consul.agentClient();
    }


    public Consul getConsul() {
        return consul;
    }

    public AgentClient getAgentClient() {
        return agentClient;
    }

}
