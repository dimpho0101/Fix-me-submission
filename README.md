*FIX-ME*

This project is a simulation of the financial markets. Using a simplified version of FIX messages that are exchanged over the TCP protocol.


*Router*
The router is the central component of the application. All other components connect to it in order to send messages to other components. 
The router perform no business logic, it just sends off messages to the destination component(s).
The router must accept incoming connections from multiple brokers and markets. We call the router a market connetivity provider,
because it allows brokers to send messages (in FIX format) to markets, without depending on specific implementation of the market.


The router will listen on 2 ports:

- Port 5000 for messages from Broker components. When a Broker establishes the connection the Router assigns it a unique 6 digit ID and communicates the ID to the Broker.

- Port 5001 for messages from Market components. When a Market establishes the connection the Router assigns it a unique 6 digit ID and communicates the ID to the Market.


Once the Router receives a message it will perform 3 steps:

- Validate the message based on the checkshum.
- Identify the destination in the routing table.
- Forward the message.


*Broker:*

The Broker will send two types of messages:

Buy. - An order where the broker wants to buy an instrument
Sell. - An order where the broker want to sell an instrument
and will receive from the market messages of the following types:

Exeuted - when the order was accepted by the market and the action succeeded
Rejected - when the order could not be met


*Market:*

A market has a list of tools which can be traded. When orders from brokers are received, the market attempts to execute them.
 If the execution is successful, the broker updates the internal instrument list and sends an excuted message to the broker.
 If the order can not be fulfilled, a rejected message is sent to the market.
 
 *Prerequisites:*
 Java (Core Programming Language - JDK 8 >)
 Maven (Dependency Management)
 
 *Set Up*
 After cloning the repo, cd into the repo and run the following commands in your terminal:
 
 
 First shell:
 $ mvn clean package
 $ java -jar router/target/Router-1.0-SNAPSHOT.jar
 
 Second shell:
 $ java -jar broker/target/Broker-1.0-SNAPSHOT.jar
 
 Third shell:
 $ java -jar market/target/Market-1.0-SNAPSHOT.jar
