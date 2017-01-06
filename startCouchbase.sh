#!/bin/bash
docker run -d --name db -p 8091-8094:8091-8094 -p 11210-11215:11210-11215 couchbase
sleep 5
curl -v -X POST http://localhost:8091/settings/web -d port=8091 -d username=Administrator -d password=password
curl -v -X POST -u Administrator:password \
  -d name=newbucket -d ramQuotaMB=200 -d authType=sasl \
  -d replicaNumber=1 \
  -d saslPassword=test \
  http://localhost:8091/pools/default/buckets

