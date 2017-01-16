#!/bin/bash
docker run -d --privileged -p 5000:5000 --restart=always --name registry \
  -v /var/lib/registry:/var/lib/registry \
  registry:2

