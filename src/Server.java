import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(69);

        while (true) {
            // Receive request
            byte[] requestData = new byte[1024];
            DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length);
            socket.receive(requestPacket);

            // Print received request packet as hexadecimal
            System.out.print("Received request (hex): ");
            for (int i = 0; i < requestPacket.getLength(); i++) {
                System.out.print(String.format("%02X ", requestData[i]));
            }
            System.out.println();

            // Parse and validate request
            String request = new String(requestPacket.getData(), 0, requestPacket.getLength(), StandardCharsets.UTF_8);
            if (!isValidRequest(request)) {
                System.err.println("Invalid request received: " + request);
                continue; // Skip further processing for invalid request
            }

            // Formulate response
            byte[] responseData;
            if (request.startsWith("\0\1")) {
                // For read requests, respond with opcode 3 followed by data
                String filename = request.substring(2, request.indexOf('\0', 2));
                String data = "Content of " + filename; // Example data, replace with actual file content
                responseData = new byte[4 + data.length()];
                responseData[0] = 0;
                responseData[1] = 3; // Opcode for data
                responseData[2] = 0; // Block number
                responseData[3] = 1; // Block number (continued)
                byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
                System.arraycopy(dataBytes, 0, responseData, 4, dataBytes.length);
            } else {
                // For write requests, respond with opcode 4 (acknowledgment)
                responseData = new byte[]{0, 4, 0, 0};
            }

            // Print response information
            System.out.print("Sending response (hex): ");
            for (int i = 0; i < responseData.length; i++) {
                System.out.print(String.format("%02X ", responseData[i]));
            }
            System.out.println();

            // Send response to intermediate host
            DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, requestPacket.getAddress(), requestPacket.getPort());
            socket.send(responsePacket);
        }
    }

    private static boolean isValidRequest(String request) {
        // Validate request format
        return request.matches("^\\x00[\\x01\\x02].+\\x00(netascii|octet)\\x00$");
    }
}
