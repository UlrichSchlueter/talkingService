package com.ulrichschlueter.talkingService.strategy;

import com.orbitz.consul.model.health.ServiceHealth;

/**
 * Created by uli on 04.01.17.
 */
public class NextPick {
    public ServiceHealth peer;
    public long nextOffer;
}
