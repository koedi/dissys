# dissys
Distributed Systems Fall 2021 - Group 17 

# Simple ZooKeeper election algorithm

## Install and run ZooKeeper

Download Apache Zookeeper and place it in a folder of your choosing. Next steps:

- create a folder for logs, for example in `zookeeper-path/logs`
- go to the `zookeeper-path/conf/` folder and rename `zoo.sample.cfg`. Open the file and change `dataDir=zookeeper-path/logs` and check that `clientPort=2181`. Save and close file.
- go to the `zookeeper-path/bin` folder and start up Zookeeper by running `./zkServer.sh start`

Now you have Zookeeper running.

## Build and run the Zookeeper election program

Go to the project directory and run `mvn clean package`. Then, you can run the program by `java -jar target/ddharkka1-1.0-SNAPSHOT-jar-with-dependencies.jar`.

Test it by opening 4 terminals and run the program in each. Kill the leader and you will see the next smallest znode to be the new leader. Kill any znode another one is watching and you will see the watcher change it's target znode.


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


