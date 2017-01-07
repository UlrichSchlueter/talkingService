package com.ulrichschlueter.talkingService.persistence;

import com.couchbase.client.java.document.json.JsonObject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * Created by uli on 06.01.17.
 */
public class PartnerData {

    @Length(min = 12, max=12)
    @NotEmpty
    private String serial;

    private String from;
    private String to;
    private long amountGiven;



    @JsonCreator
    public PartnerData(@JsonProperty("serial") String serial) {
        this.serial = serial;
    }

    public PartnerData() {

    }


    @JsonProperty
    public String getFrom() {
        return from;
    }

    @JsonProperty
    public void setFrom(String from) {
        this.from = from;
    }

    @JsonProperty
    public String getTo() {
        return to;
    }

    @JsonProperty
    public void setTo(String to) {
        this.to = to;
    }

    @JsonProperty
    public long getAmountGiven() {
        return amountGiven;
    }

    @JsonProperty
    public void setAmountGiven(long amountGiven) {
        this.amountGiven = amountGiven;
    }

    @JsonProperty
    public String getSerial() {
        return serial;
    }

    @JsonProperty
    public void setSerial(String serial) {
        this.serial= serial;
    }




}
