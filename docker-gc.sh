#!/bin/bash


#  https://github.com/spotify/docker-gc
# GRACE_PERIOD_SECONDS=86400

docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -e FORCE_IMAGE_REMOVAL=1 -v /etc:/etc spotify/docker-gc

