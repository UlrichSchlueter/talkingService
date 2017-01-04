package com.ulrichschlueter.talkingService.strategy;

import com.orbitz.consul.model.health.ServiceHealth;

import java.util.List;

/**
 * Created by uli on 04.01.17.
 */
public abstract class Strategy
{
    public abstract long applyOfferHandling(String sender, long amountGiven, long currentAmount) ;
    public abstract NextPick applySelectionProcess(List<ServiceHealth> peerList, long currentAmount) ;

}
