import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Client {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress host = InetAddress.getLocalHost();

        for (int i = 1; i <= 11; i++) {
            String request;
            if (i % 2 == 0) {
                request = "write request";
            } else {
                request = "read request";
            }
            byte[] requestData = getRequestData(request, i);

            DatagramPacket packet = new DatagramPacket(requestData, requestData.length, host, 23);

            // Print request information
            System.out.println("Sending request: " + request);
            System.out.print("As bytes: ");
            for (byte b : requestData) {
                System.out.print(String.format("%02X ", b));
            }
            System.out.println();

            // Send the packet
            socket.send(packet);

            // Receive response
            byte[] responseData = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length);
            socket.receive(responsePacket);

            // Extracting the relevant part of the response and printing it
            byte[] responseDataTrimmed = Arrays.copyOfRange(responsePacket.getData(), 0, responsePacket.getLength());
            System.out.println("Received response: " + new String(responseDataTrimmed, StandardCharsets.UTF_8));
        }

        socket.close();
    }


    private static byte[] getRequestData(String request, int i) {
        // Constructing request data according to the specified format
        String filename = "test.txt"; // Example filename
        String mode = "octet"; // Example mode
        String requestData;
        if (request.equals("read request")) {
            requestData = "\0\1" + filename + "\0" + mode + "\0";
        } else {
            requestData = "\0\2" + filename + "\0" + mode + "\0";
        }
        return requestData.getBytes();
    }
}
