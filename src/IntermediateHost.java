/**
 * SYSC 3303 Assignment 2
 * Joseph Vretenar - 101234613
 */

import java.net.*;

/**
 * The type Intermediate host.
 */
public class IntermediateHost {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        try {
            DatagramSocket receiveSocket = new DatagramSocket(23);
            DatagramSocket sendSocket = new DatagramSocket();

            while (true) {
                byte[] requestData = new byte[1024];
                DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length);
                receiveSocket.receive(requestPacket);

                String request = new String(requestPacket.getData(), 0, requestPacket.getLength());
                System.out.println("Received request from client");

                InetAddress serverAddress = InetAddress.getLocalHost(); // Change this to the server's address
                int serverPort = 69; // Change this to the server's port

                DatagramPacket forwardPacket = new DatagramPacket(requestData, requestData.length, serverAddress, serverPort);
                System.out.println("Forwarding request to server");
                sendSocket.send(forwardPacket);

                byte[] responseData = new byte[1024];
                DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length);
                sendSocket.receive(responsePacket);

                String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
                System.out.println("Received response from server");

                InetAddress clientAddress = requestPacket.getAddress();
                int clientPort = requestPacket.getPort();

                DatagramPacket finalResponsePacket = new DatagramPacket(responseData, responseData.length, clientAddress, clientPort);
                System.out.println("Sending response to client");
                receiveSocket.send(finalResponsePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
