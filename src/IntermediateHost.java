import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class IntermediateHost {
    public static void main(String[] args) throws IOException {
        DatagramSocket receiveSocket = new DatagramSocket(23);
        DatagramSocket sendSocket = new DatagramSocket();

        while (true) {
            // Receive request from the client
            byte[] requestData = new byte[1024];
            DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length);
            receiveSocket.receive(requestPacket);

            // Print request information
            String request = new String(requestPacket.getData(), 0, requestPacket.getLength(), StandardCharsets.UTF_8);
            System.out.println("Received request from client: " + request);

            // Send request to server
            DatagramPacket sendPacketToServer = new DatagramPacket(requestPacket.getData(), requestPacket.getLength(), InetAddress.getLocalHost(), 69);
            sendSocket.send(sendPacketToServer);
            System.out.println("Sent request to server: " + request);

            // Receive response from server
            byte[] responseData = new byte[1024];
            DatagramPacket responsePacketFromServer = new DatagramPacket(responseData, responseData.length);
            sendSocket.receive(responsePacketFromServer);

            // Print response information
            String response = new String(responsePacketFromServer.getData(), 0, responsePacketFromServer.getLength(), StandardCharsets.UTF_8);
            System.out.println("Received response from server: " + response);

            // Send response back to the client
            DatagramPacket responsePacketToClient = new DatagramPacket(responsePacketFromServer.getData(), responsePacketFromServer.getLength(), requestPacket.getAddress(), requestPacket.getPort());
            receiveSocket.send(responsePacketToClient);
            System.out.println("Sent response to client: " + response);
        }
    }
}
