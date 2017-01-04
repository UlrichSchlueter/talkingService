package com.ulrichschlueter.talkingService;

import com.codahale.metrics.annotation.Timed;
import com.orbitz.consul.model.health.ServiceHealth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukas on 03.01.17
 */
@Path("/consul")
@Produces(MediaType.APPLICATION_JSON)
public class ConsulDiscoveryResource {
    private final ConsulConnector consulConnector;

    public ConsulDiscoveryResource(ConsulConnector consulConnector) {
        this.consulConnector=consulConnector;
    }

    @GET
    @Timed
    @Path("list")
    public List<String> list() {
        List<String> returnThis = new ArrayList<String>();

        for (ServiceHealth service : consulConnector.getAllServicesByName(TaskListServiceApplication.SERVICENAME))
        {
            returnThis.add(service.toString());
        }

        return returnThis;
    }
}
