package com.ulrichschlueter.talkingService;

import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.health.ServiceHealth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uli on 02.01.17.
 */
public class ConsulConnector {
    private Consul consul = null;
    private AgentClient agentClient = null;
    private String serviceName;
    private String serviceID;

    Logger log = LoggerFactory.getLogger(ConsulConnector.class);

    public void setLog(Logger log) {
        this.log = log;
    }

    public ConsulConnector(String consulURL, String serviceName) {
        if (consulURL == null || consulURL.equals("")) {
            log.info("Using default consul configuration");
            consul = Consul.builder().build(); // connect to Consul on localhost
        } else {
            log.info("Using custom consul configuration:" + consulURL);
            consul = Consul.builder().withUrl(consulURL).build(); // connect to Consul to given URL
        }

        this.serviceName=serviceName;

        long size = getPeerServiceInstanceCount();
        this.serviceID = new Long(size + 2L).toString();
        this.agentClient = consul.agentClient();
        while (agentClient.isRegistered(this.serviceID))
        {
            this.serviceID=this.serviceID+1;
        }
    }


    public Consul getConsul() {
        return consul;
    }

    public AgentClient getAgentClient() {
        return agentClient;
    }



    public String getServiceName() {
        return serviceName;
    }

    public String getServiceID() {
        return serviceID;
    }

    public String getFullServiceName()
    {
        return serviceName+":"+serviceID;
    }

    public List<ServiceHealth> getAllHealthyServicesByName(String findServiceName  ) {
        List<ServiceHealth> peers = new ArrayList<ServiceHealth>();

        for (ServiceHealth peer : consul.healthClient().getHealthyServiceInstances(findServiceName).getResponse()) {
            log.info("healthy "+peer.getService().toString());
            peers.add(peer);
        }

        return peers;
    }

    public List<ServiceHealth> removeSelf(List<ServiceHealth> list)
    {
        List<ServiceHealth> filteredList=new ArrayList<ServiceHealth>();

        //FilterList
        for (ServiceHealth peer : list)
        {
            if ( peer.getService().getService().equals(serviceName)
                    &&  ! peer.getService().getId().equals(serviceID))
            {
                filteredList.add(peer);
                log.debug("keeping peer " + peer + " "+ serviceName + " "+ serviceID);
            }
            else
            {
                log.debug("dropping peer" + peer);
            }
        }
        return filteredList;
    }

    public List<ServiceHealth> getAllPeerServices( ) {
        return getAllServicesByName(serviceName);
    }

    public long getServiceInstanceCountByName(String findServiceName) {
        return getAllServicesByName(findServiceName).size();
    }

    public long getPeerServiceInstanceCount() {
        return getAllServicesByName(this.serviceName).size();
    }


    public List<ServiceHealth> getAllServicesByName(String findServiceName) {
        List<ServiceHealth> peers = new ArrayList<ServiceHealth>();

        for (ServiceHealth peer : consul.healthClient().getAllServiceInstances(findServiceName).getResponse()) {
            log.info("All"+
                    (peer.getService().toString()));
            peers.add(peer);
        }

        return peers;
    }

    public List<ServiceHealth> getAllHealthyPeerServices() {
        return getAllHealthyServicesByName(serviceName);
    }



}
