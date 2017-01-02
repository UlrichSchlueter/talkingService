package com.ulrichschlueter.talkingService;

import com.codahale.metrics.annotation.Timed;
import com.codahale.metrics.health.HealthCheck.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import javax.ws.rs.core.MediaType;

/**
 * Created by Ulrich Schlueter
 */

@Path("/shutdown/{shutdownmode}")
@Produces(MediaType.APPLICATION_JSON)
public class TaskListShutdownResource {

	// SLF4J is provided with dropwizard
	Logger log = LoggerFactory.getLogger(TaskListResource.class);

	public TaskListShutdownResource() {       
    }

	@GET
    @Timed
	public int shutdown(@PathParam("shutdownmode") String shutdownmode) {
   
    	if (shutdownmode.equalsIgnoreCase("Suicide"))
    	{
    		log.error("Suicide.");
    		Runtime.getRuntime().halt(0);
    	}
    	else
    	{
    		log.error("Shutting Down.");
    		System.exit(99);
    	}
    	
        
        return 0;
    }
}