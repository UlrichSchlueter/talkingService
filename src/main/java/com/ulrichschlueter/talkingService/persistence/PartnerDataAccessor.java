package com.ulrichschlueter.talkingService.persistence;

import io.smartmachine.couchbase.GenericAccessor;
import io.smartmachine.couchbase.ViewQuery;

import java.util.List;

/**
 * Created by uli on 06.01.17.
 */
public interface PartnerDataAccessor extends GenericAccessor<PartnerData> {

    @ViewQuery("/^PARTNERDATA/.test(meta.id)")
    public List<PartnerData> findAll();

}
