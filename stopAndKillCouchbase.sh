#!/bin/sh
docker rm -f  $(docker ps -q -f "name=db"  )
