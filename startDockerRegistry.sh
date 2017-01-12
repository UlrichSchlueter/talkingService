#!/bin/bash
docker run -d -p 5000:5000 --restart=always --name registry \
  -v /var/lib/registry:/var/lib/registry \
  registry:2

su -c "setenforce 0"
