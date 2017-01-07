package com.ulrichschlueter.talkingService.Couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.cluster.ClusterManager;

import java.util.logging.Logger;

/**
 * Created by uli on 07.01.17.
 */

public class ConnectionFactory {

    private static final Logger LOG = Logger.getLogger(ConnectionFactory.class.getName());
    private static Bucket bucket;
    private static Cluster cluster;



    public static Bucket getBucketConnection() {

        if (bucket == null) try {

            createConnection();

        } catch (Exception ex) {

            LOG.severe(ex.toString());
        }

        return bucket;
    }

    /**
     * To get the cluster manager
     *
     * @return
     */
    public static Cluster getClusterConnection() {

        if (cluster == null) try {

            createConnection();

        } catch (Exception ex) {

            LOG.severe(ex.toString());
        }

        return cluster;
    }


    public static Bucket createConnection() {



        Cluster cluster = CouchbaseCluster.create(CouchbaseConfig.couchbasehosts);

        bucket = cluster.openBucket(CouchbaseConfig.bucketName,CouchbaseConfig.bucketPassword);


        return bucket;
    }
}



