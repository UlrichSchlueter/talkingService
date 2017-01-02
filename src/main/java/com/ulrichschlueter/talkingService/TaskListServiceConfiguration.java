package com.ulrichschlueter.talkingService;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.bundles.assets.AssetsBundleConfiguration;
import io.dropwizard.bundles.assets.AssetsConfiguration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class TaskListServiceConfiguration extends Configuration implements AssetsBundleConfiguration {

    private int maxLength;
    private String consulURL;
    private boolean consulEnabled;

    @Valid
    @NotNull
    @JsonProperty
    private final AssetsConfiguration assets = AssetsConfiguration.builder().build();

    @Override
    public AssetsConfiguration getAssetsConfiguration() {
        return assets;
    }

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