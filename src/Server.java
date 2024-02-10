/**
 * SYSC 3303 Assignment 2
 * Joseph Vretenar - 101234613
 */

import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * The type Server.
 */
public class Server {
    static String file = "test.txt";
    static byte[] fileBytes = file.getBytes();
    static String mode = "netascii";

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(69);

            while (true) {
                byte[] requestData = new byte[1024];
                DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length);
                socket.receive(requestPacket);

                byte[] requested = requestPacket.getData();

                String request = new String(requestPacket.getData(), 0, requestPacket.getLength(), StandardCharsets.UTF_8);
                String opcode = Integer.toHexString(requested[0] & 0xFF) + " " + Integer.toHexString(requested[1] & 0xFF) + " ";
                String output = opcode + file + " 0 " + mode + " 0";
                System.out.println("Received request: " + output);

                InetAddress clientAddress = requestPacket.getAddress();
                int clientPort = requestPacket.getPort();

                String response;
                if (isValidRequest(requested)) {
                    if (requested[1] == 1) {
                        response = "0 3 0 1";
                    } else {
                        response = "0 4 0 0";
                    }
                } else {
                    response = "Invalid request";
                }

                byte[] responseData = response.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, clientAddress, clientPort);

                System.out.println("Sending response: " + response);
                socket.send(responsePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isValidRequest(byte[] request) {
        if (request[0] != 0 || (request[1] != 1 && request[1] != 2)) {
            return false;
        } else if (request[request.length - 1] != 0) {
            return false;
        } else if (request[2 + fileBytes.length] != 0) {
            return false;
        }else {
            return true;
        }
    }
}

