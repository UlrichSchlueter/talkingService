package com.ulrichschlueter.talkingService;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class TaskListServiceConfiguration extends Configuration {

    private int maxLength;
    private String consulURL;
    private boolean consulEnabled;

    @JsonProperty
    public int getMaxLength() {
        return maxLength;
    }



    @JsonProperty
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    @JsonProperty
    public String getConsulURL() {
        return consulURL;
    }

    @JsonProperty
    public void setConsulURL(String consulURL) {
        this.consulURL = consulURL;
    }


    @JsonProperty
    public boolean isConsulEnabled() {
        return consulEnabled;
    }

    @JsonProperty
    public void setConsulEnabled(boolean consulEnabled) {
        this.consulEnabled = consulEnabled;
    }
}