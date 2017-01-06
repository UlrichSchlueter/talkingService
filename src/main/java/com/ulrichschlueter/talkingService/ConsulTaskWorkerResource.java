package com.ulrichschlueter.talkingService;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.orbitz.consul.KeyValueClient;
import com.orbitz.consul.model.health.ServiceHealth;
import com.orbitz.consul.model.kv.Value;
import com.ulrichschlueter.talkingService.persistence.PartnerData;
import com.ulrichschlueter.talkingService.persistence.PartnerDataAccessor;
import com.ulrichschlueter.talkingService.strategy.BaseStrategy;
import com.ulrichschlueter.talkingService.strategy.NextPick;
import com.ulrichschlueter.talkingService.strategy.RandomStrategy;
import com.ulrichschlueter.talkingService.strategy.Strategy;
import io.smartmachine.couchbase.Accessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.*;


/**
 * Created by Uli on 03/01/2016.
 */
@Path("/work/{sender}")
@Produces(MediaType.APPLICATION_JSON)
public class ConsulTaskWorkerResource extends TimerTask{
    private final ConsulConnector consulConnector;
    Logger log = LoggerFactory.getLogger(ConsulTaskWorkerResource.class);
    Timer timer = new Timer();
    Random rnd = new Random();
    Client jerseyClient=null;
    private long amount=100;
    private Strategy strategy=new RandomStrategy();
    private int maxStrategy=2;

    @Accessor
    private PartnerDataAccessor accessor;

    public ConsulTaskWorkerResource(ConsulConnector consulConnector, Client jerseyClient) {
        this.consulConnector =consulConnector;
        this.jerseyClient=jerseyClient;
        timer.schedule(this, 500, 5000);
        //strategy=rnd.nextInt(maxStrategy);
    }

    @GET
    @Timed
    public Long work(@PathParam("sender") String sender, @QueryParam("drop") Optional<Long> dropAmount) {

        long amountReceived= dropAmount.or(0L);
        long amountToBeReturned=strategy.applyOfferHandling(sender,amountReceived,amount);

        log.info(strategy +" > + "+ amountReceived +" - "+ amountToBeReturned+"> "+amount +" "+sender);
        amount=amount+amountReceived-amountToBeReturned;
        log.info(">> : "+amount);

        addLong(consulConnector.getFullServiceName() +"/from/"+sender+"/amount",amountReceived);
        addLong(consulConnector.getFullServiceName() +"/to/"+sender+"/amount",amountToBeReturned);
        addLong(consulConnector.getFullServiceName() +"/from/"+sender+"/calls",1);

        PartnerData p = new PartnerData("1");
        p.setFrom(sender);
        p.setTo(consulConnector.getFullServiceName());

        accessor.create("1",p);

        return amountToBeReturned;
    }

     private void addLong(String key, long addThis)
     {


         KeyValueClient kvClient = consulConnector.getConsul().keyValueClient();
         Optional<String> val =kvClient.getValueAsString(key);
         long currentValue=0;
         if (val.isPresent())
         {
             try {
                 String What=val.get();
                 currentValue = Long.parseLong(What);
             }
             catch (Exception e)
             {
                 e.printStackTrace();
             }
         }
         long newvalue=currentValue+addThis;
         kvClient.putValue(key,String.valueOf(newvalue));
     }

    @Override
    public void run() {

        List<ServiceHealth> filteredList=consulConnector.removeSelf(consulConnector.getAllHealthyPeerServices());

        log.info("Filtered " + filteredList.size() );
        if (filteredList.size()>0) {
            NextPick nextPick=strategy.applySelectionProcess(filteredList,amount);

            log.info("would talk to" + nextPick.peer);
            Long amountReceived = getAnswerToOffer(nextPick.peer, nextPick.nextOffer);
            amount=amount-nextPick.nextOffer+amountReceived;
            log.info(">> : "+amount);

        }
        else
        {
            log.info("no peer to talk to");
        }
    }

    private Long getAnswerToOffer(ServiceHealth peer, long amountToOffer) {
        peer.getService().getPort();
        //WebTarget target=jerseyClient.target("localhost:"++"/api/work");
        String sender=consulConnector.getFullServiceName();
        Long amountReceived = jerseyClient.target("http://"+peer.getService().getAddress()+":"+  peer.getService().getPort()+"/api/work/"+sender)
                .queryParam("drop", String.valueOf(amountToOffer))
                .request(MediaType.APPLICATION_JSON)
                .get(Long.class);
        log.info("Initiator:"+ amount+" - "+ amountToOffer +" +"+ amountReceived+" to " + peer.getService().getId());
        return amountReceived;
    }
}
