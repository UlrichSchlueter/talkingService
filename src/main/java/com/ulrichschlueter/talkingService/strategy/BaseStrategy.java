package com.ulrichschlueter.talkingService.strategy;

import com.orbitz.consul.model.health.ServiceHealth;

import java.util.List;
import java.util.Random;

/**
 * Created by uli on 04.01.17.
 */
public class BaseStrategy extends Strategy{

    Random rnd=new Random();


    public long applyOfferHandling(String sender, long amountGiven, long currentAmount)
    {
         return amountGiven/2;
    }

    public NextPick applySelectionProcess(List<ServiceHealth> peerList, long currentAmount)
    {
        NextPick nextPick=new NextPick();

        int pick = rnd.nextInt(peerList.size() );
        nextPick.peer = peerList.get(pick);
        nextPick.nextOffer=currentAmount/10;
        return nextPick;
    }

    @Override
    public String toString() {
        return "BaseStrategy";
    }
}
