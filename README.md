# dissys
Distributed Systems Fall 2021 - Group 17 

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
