docker build --no-cache -f Dockerfiles/Dockerfile.Postgres -t springbootsample/postgres ../SQL

docker build --no-cache -f Dockerfiles/Dockerfile.Backend -t springbootsample/backend ../SpringBootBackend

docker build --no-cache -f Dockerfiles/Dockerfile.Gateway -t springbootsample/gateway ../SpringCloudGateway

docker build --no-cache -f Dockerfiles/Dockerfile.ElasticSearch -t springbootsample/elasticsearch ../

docker build --no-cache -f Dockerfiles/GrafanaLoki/Dockerfile.Loki -t springbootsample/loki .

docker build --no-cache -f Dockerfiles/GrafanaLoki/Dockerfile.Grafana -t springbootsample/grafana .

docker build --no-cache -f Dockerfiles/Kafka/Dockerfile.Broker -t springbootsample/broker .

docker build --no-cache -f Dockerfiles/Kafka/Dockerfile.ZooKeeper -t springbootsample/zookeeper .