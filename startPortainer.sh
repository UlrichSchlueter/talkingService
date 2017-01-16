#!/bin/sh

docker run -d -p 9000:9000 --privileged --name="portainer" -v /var/run/docker.sock:/var/run/docker.sock portainer/portainer
sleep 3

curl -X POST -H "Content-Type: application/json" -d '{"password":"uliuliuli"}' http://localhost:9000/api/users/admin/init


