package com.ulrichschlueter.talkingService;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.bundles.assets.AssetsBundleConfiguration;
import io.dropwizard.bundles.assets.AssetsConfiguration;
import io.dropwizard.client.JerseyClientConfiguration;



import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class TaskListServiceConfiguration extends Configuration implements AssetsBundleConfiguration {

    private int maxLength;
    private String consulURL;
    private boolean consulEnabled;
    private String couchbasebucket;
    private String couchbasebucketpassword;
    private String couchbasehosts;

    @JsonProperty
    public String getCouchbasebucket() {
        return couchbasebucket;
    }

    @JsonProperty
    public void setCouchbasebucket(String couchbasebucket) {
        this.couchbasebucket = couchbasebucket;
    }

    @JsonProperty
    public String getCouchbasebucketpassword() {
        return couchbasebucketpassword;
    }

    @JsonProperty
    public void setCouchbasebucketpassword(String couchbasebucketpassword) {
        this.couchbasebucketpassword = couchbasebucketpassword;
    }

    @JsonProperty
    public String getCouchbasehosts() {
        return couchbasehosts;
    }

    @JsonProperty
    public void setCouchbasehosts(String couchbasehosts) {
        this.couchbasehosts = couchbasehosts;
    }

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

    @Valid
    @NotNull
    private JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();

    @JsonProperty("jerseyClient")
    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return jerseyClient;
    }

    public void setJerseyClientConfiguration(JerseyClientConfiguration jcc) {
        this.jerseyClient = jcc;
    }



}