# Distributed Banana shop
Distributed Systems Fall 2021 - Group 17 

## Install and run ZooKeeper

Download Apache Zookeeper and place it in a folder of your choosing. Next steps:

- create a folder for logs, for example in `zookeeper-path/logs`
- go to the `zookeeper-path/conf/` folder and rename `zoo.sample.cfg`. Open the file and change `dataDir=zookeeper-path/logs` and check that `clientPort=2181`. Save and close file.
- go to the `zookeeper-path/bin` folder and start up Zookeeper by running `./zkServer.sh start`

Now you have Zookeeper running.

## Build and run the app

The app consists of a cluster node (program for coordinator and worker nodes), storage and frontend. Run at least 3 cluster node instances to get two workers and one coordinator. Other than that, you only need to run one instance of storage and frontend.

### Coordinator and worker nodes

Open up a terminal, go to the project directory `cluster-node` and Run `mvn clean package`. Then, you can start up your first node with 

`java -jar target/distributed.shop-1.0-SNAPSHOT-jar-with-dependencies.jar 8081`.

Note, that we set port 8081 for the node.

The first node will be your coordinator. Start up at least 2 more nodes, for example by opening two more terminal windows. When working on your local device, remember to set different ports to each node.  

### Storage

Go to the project directory `storage` and and run `mvn clean package` again. Start up the storage in port 9001 for example by running command `java -jar target/storage-1.0-SNAPSHOT-jar-with-dependencies.jar 9001`.

### Frontend 

Go to directory `frontend` and run `mvn clean package`. Run the frontend in port 9000 with `java -jar target/front.end-1.0-SNAPSHOT-jar-with-dependencies.jar 9000`

Now you have a fully working distributed system running on your device. Open up your browser and go to `localhost:9000` and test the app. See what happens when you kill a worker or the coordinator.

## Meeting notes 13.11.2021
Present: Kari, Ossi

Tooling
- Javascript (nodes can be done with different languages)
- Docker
- Github

Topic: Online Store
- Check examples/background material from internet
- Nodes
  - Front
  - At least two as microservices: warehouse, order, etc..
  - Message queues as inter-node communication, e.g. RabbitMQ
- General
  - How to handle error situations?
  - Transactional / run-to-completion?
  - Microservices should not be blocking

Keep it simple

AP All: get familiar with background material   
AP All: consider architecture -- which nodes to use, inter-node communication, etc.   
AP Kari: setup Github

Next meeting Tue 16.11. 20:00 @ Zoom (same link)

## Meeting notes 16.11.2021
Present: Kari, Ilkka, Ossi

Topic: Online Store - we sell bananas
Nodes:
- Client - orders bananas
- Service handler - handles client orders
- Warehouse - contains bananas
- Shopping basket - shared (state) between nodes

Next steps:
- Design plan - google doc (Ilkka to create)
- Everyone to start filling


Next meeting Sunday 21.11.2021 14:00 @ Zoom

## Meeting notes 27.11.2021
Present: Lauri, Ilkka, Ossi

Topic: What everyone has done

Lauri:
- Master node initial implementation

Ossi:
- Bananashop api initial implementation

Ilkka:
- Tested out ZooKeeper

Next steps:
- Lauri will refactor his code and push to github on around Monday
- Ilkka will test out ZooKeeper with the Bananashop API
- Everyone can test out different things, let's meet next Wednesday


Next meeting Wednesday 1.12.2021 18:00 @ Zoom


