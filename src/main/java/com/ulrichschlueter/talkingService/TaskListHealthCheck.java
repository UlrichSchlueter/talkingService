package com.ulrichschlueter.talkingService;

import com.codahale.metrics.health.HealthCheck;

public class TaskListHealthCheck extends HealthCheck {

    private TaskListHealthResource healthResource;

    public TaskListHealthCheck(TaskListHealthResource healthResource) {
      this.healthResource=healthResource;
    }

    @Override
    protected Result check() throws Exception {
    	return healthResource.getHealthStatus();
    }


}