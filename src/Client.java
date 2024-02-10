/**
 * SYSC 3303 Assignment 2
 * Joseph Vretenar - 101234613
 */

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * The type Client.
 */
public class Client {
    static String file = "test.txt";
    static String mode = "netascii";
    static byte[] zeroByte = {0};
    static byte[] oneByte = {1};
    static byte[] twoByte = {2};
    static byte[] filenameBytes = file.getBytes(StandardCharsets.UTF_8);
    static byte[] modeBytes = mode.getBytes(StandardCharsets.UTF_8);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress host = InetAddress.getLocalHost();
            int port = 23;
            byte[] requested;

            for (int i = 1; i <= 11; i++) {
                if (i % 2 == 0) {
                    requested = createRequest(2);
                } else {
                    requested = createRequest(1);
                }
                DatagramPacket requestPacket = new DatagramPacket(requested, requested.length, host, port);
                String opcode = Integer.toHexString(requested[0] & 0xFF) + " " + Integer.toHexString(requested[1] & 0xFF) + " ";
                String output = opcode + file + " 0 " + mode + " 0";
                if (requested[1] == 1) {
                    System.out.println("Sending read request " + i + ": " + output);
                } else {
                    System.out.println("Sending write request " + i + ": " + output);
                }
                socket.send(requestPacket);

                byte[] responseData = new byte[1024];
                DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length);
                socket.receive(responsePacket);

                String response = new String(responsePacket.getData(), 0, 7, StandardCharsets.UTF_8);
                System.out.println("Received response " + i + ": " + response);
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] createRequest(int opcode) throws IOException {
        byte[] output = new byte[filenameBytes.length + modeBytes.length + 4];
        ByteBuffer buffer = ByteBuffer.wrap(output);
        buffer.put(zeroByte);
        if (opcode == 1) {
            buffer.put(oneByte);
        } else if (opcode == 2) {
            buffer.put(twoByte);
        } else {
            throw new IOException("Invalid opcode");
        }
        buffer.put(filenameBytes);
        buffer.put(zeroByte);
        buffer.put(modeBytes);
        buffer.put(zeroByte);
        output = buffer.array();
        return output;
    }
}
