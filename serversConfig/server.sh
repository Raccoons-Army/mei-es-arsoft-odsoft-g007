#!/bin/bash

server_name="dev-env"

# get the context from the first argument -> prod, dev, test
context=$1

# validate and handle context [dev, prod, test]
case $context in
    "dev")
        echo "Setting up for development environment"
        server_name="dev-env"
        ;;
    "prod")
        echo "Setting up for production environment"
        server_name="prod-env"
        ;;
    "test")
        echo "Setting up for test environment"
        server_name="test-env"
        ;;
    *)
        echo "Invalid context. Please provide a valid context [dev, prod, test]"
        exit 1
        ;;
esac

# update the system
apt update && apt upgrade -y

# install ssh
apt install openssh-server -y

# install cronjob
apt-get update && apt-get install -y cron

# install docker ###
echo "Installing Docker in the LXD container..."
apt install apt-transport-https ca-certificates curl software-properties-common -y
curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -
add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable"
apt update
apt install docker-ce -y
systemctl enable docker
systemctl start docker

# activate docker swarm
docker swarm init

# create our network
docker network create --driver overlay --scope swarm --attachable lms_network
