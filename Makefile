CONSUL_HOST=localhost
DC=docker-compose
DCP=docker-compose --file docker-compose-platform.yml

services-restart: services-stop services-start

restart-all: services-stop platform-restart services-start

services-stop:
	$(DC) stop;
	$(DC) rm -f;

services-start:
	$(DC) up -d;

# ###################################################################

platform-restart: platform-stop platform-start

platform-stop:
	$(DCP) stop;
	$(DCP) rm -f;

platform-start: prepare consul-start
	$(DCP) up -d;

consul-stop:
	$(DCP) stop consul2 consul1;
	$(DCP) rm -f consul1 consul2;

consul-start:
	$(DCP) up -d consul1 consul2;
	# give consul some time to startup;
	@echo ------------------------------------------------------------------------------------------------------
	@echo Using CONSUL_HOST="$(CONSUL_HOST)" as Consul host. To overwrite start make with -e option, e.g. make platform-restart -e CONSUL_HOST=dockerhost
	@echo ------------------------------------------------------------------------------------------------------
	@sleep 5;
	deploy/consul/run-git2consul.sh $(CONSUL_HOST);

docker-clean:
	# Delete all containers
	docker rm $$(docker ps -a -q)
	# Delete all images
	docker rmi $$(docker images -q)

prepare:
	mkdir -p \
		./data/whisper \
		./data/grafana \
		./data/elasticsearch \
		./log/graphite \
		./log/graphite/webapp \
		./log/elasticsearch
	chmod 777 \
		./data/whisper \
		./data/grafana \
		./data/elasticsearch \
		./log/graphite \
		./log/graphite/webapp \
		./log/elasticsearch

