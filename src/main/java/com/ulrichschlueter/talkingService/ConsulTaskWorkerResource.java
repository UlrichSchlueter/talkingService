package com.ulrichschlueter.talkingService;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Random;


/**
 * Created by Uli on 03/01/2016.
 */
@Path("/work")
@Produces(MediaType.APPLICATION_JSON)
public class ConsulTaskWorkerResource {
    private final ConsulConnector consulConnector;
    Logger log = LoggerFactory.getLogger(ConsulTaskWorkerResource.class);

    public ConsulTaskWorkerResource(String consulURL) {
        this.log = log;
        consulConnector=new ConsulConnector(consulURL);
    }

    @GET
    @Timed
    public Long work(@QueryParam("contains") Optional<String> contains) {

        long res=0;
        Random rn = new Random();
        for (int i = 0; i < 10000000; i++) {
            res = rn.nextLong();
        }
        consulConnector.getConsul().keyValueClient().putValue("Test",new Long(res).toString());
        return res;
    }
}
