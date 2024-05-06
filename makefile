# Example Makefile for docker builds
# See http://blog.dixo.net/2015/07/building-docker-containers-with-make-on-coreos/

#-----------------------------------------------------------------------------
# configuration - see also 'make help' for list of targets
#-----------------------------------------------------------------------------

# name of container
DOCKER_IMAGE_NAME=next-car-backend

#-----------------------------------------------------------------------------
# default target
#-----------------------------------------------------------------------------

all   : ## Build the container - this is the default action
all: build

#-----------------------------------------------------------------------------
# build project
#-----------------------------------------------------------------------------

build  : ## maven build all projects
build:
	@mvn clean package

#-----------------------------------------------------------------------------
# build docker image
#-----------------------------------------------------------------------------

image    : ## build the application docker images
image: build
	@docker-compose down
	@docker-compose build

#-----------------------------------------------------------------------------
# build and test container
#-----------------------------------------------------------------------------

docker : ## build the application and run it in docker containers
docker: image run

run    : ## run the application in docker containers
run:
	@docker-compose up

clean    : ## run the application in docker containers
clean:
	@docker-compose down

#-----------------------------------------------------------------------------
# repository control
#-----------------------------------------------------------------------------

push  : ## Push container to remote repository
push: build
	docker push $(DOCKER_IMAGE_NAME)

pull  : ## Pull container from remote repository - might speed up rebuilds
pull:
	docker pull $(DOCKER_IMAGE_NAME)

#-----------------------------------------------------------------------------
# supporting targets
#-----------------------------------------------------------------------------

help  : ## Show this help.
	@fgrep -h "##" $(MAKEFILE_LIST) | fgrep -v fgrep | sed -e 's/\\$$//' | sed -e 's/##//'

.PHONY : all build build-image docker run push pull help
