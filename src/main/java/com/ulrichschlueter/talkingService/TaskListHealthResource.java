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
@Path("/health/{newstate}")
@Produces(MediaType.APPLICATION_JSON)
public class TaskListHealthResource {
	

	private Result healthStatus=Result.healthy();
    //SLF4J is provided with dropwizard
    Logger log = LoggerFactory.getLogger(TaskListResource.class);

    public TaskListHealthResource() {
        
    }

    @GET
    @Timed
	public Result setHealth(@PathParam("newstate") String newstate) {
    	
    	if (newstate.equals("SICK"))
    		healthStatus=Result.unhealthy("EXTERN");
    	else
    		healthStatus=Result.healthy();
    	
        return healthStatus;
        
    }

    public boolean isHealthy() {
        return healthStatus==Result.healthy();
    }

    
    Result getHealthStatus() {
		return healthStatus;
	}
}