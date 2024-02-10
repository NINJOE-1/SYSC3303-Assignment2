# SYSC 3303 Assignment 2
Joseph Vretenar 101234613
## Introduction to UDP
The goal of this assignment is to build a very basic three part system consisting of a client, an
intermediate host, and a server. The client sends requests to the intermediate host, which sends them
on to the server. The server sends responses to the intermediate host, which sends them on to the
client. From the client's point of view, the intermediate host appears to be the server. From the server's
point of view, the intermediate host appears to be the client. In this assignment, the intermediate host
will not change the packets, it will just send them on. The intermediate host could be updated to
change packets and thus become an error simulator for the system.

## Files
### [Client.java](src/main/java/Client.java)
This is the client file, when run it will send a datagram packet to the intermediate host. The packet will 
contain bytes that represent a read or write request, the file name, and the mode. A sample packet would look like this:
```0 1 test.txt 0 octet 0```
This packet is a read request for the file test.txt in octet mode. The 0's are used to separate the different parts of the packet.
The client will then wait for a response from the intermediate host. If the intermediate host sends a response, the client will print it out.

### [IntermediateHost.java](src/main/java/IntermediateHost.java)
This is the intermediate host file, when run it will listen for a packet from the client. When it receives a packet, it will send it to the server. It will then wait for a response from the server. When it receives a response, it will send it to the client.

### [Server.java](src/main/java/Server.java)
This is the server file, when run it will listen for a packet from the intermediate host. When it receives a packet, it will send a response to the intermediate host. The response will contain bytes that represent the validation of the received request. The server will then wait for another packet from the intermediate host.

## Setup Instructions
1. Make sure java is installed on your system
2. Compile the Java files using an IDE or through command lines
3. Run the files in the following order:
    1. Server
    2. IntermediateHost
    3. Client

## Expected Output
The Client file should produce this output:
```
Sending read request 1: 0 1 test.txt 0 netascii 0 
Received response 1: 0 3 0 1
```

The IntermediateHost file should produce this output:
```
Received request from client
Forwarding request to server
Received response from server
Sending response to client
```

The Server file should produce this output:
```
Received request: 0 1 test.txt 0 netascii 0
Sending response: 0 3 0 1
```

## UML Diagrams
The UML class diagram can be found [here](UMLDiagrams/classDiagram.png)

The UML sequence diagram can be found [here](UMLDiagrams/sequenceDiagram.png)