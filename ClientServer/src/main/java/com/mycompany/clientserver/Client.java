package com.mycompany.clientserver;

/**
 *
 * @author Lassie
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // Trying to establish connection with the server
            Socket socket = new Socket("localhost", 5000);

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            // Output data fow
            Thread sendMessage = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        // Message is typed
                        String message = sc.nextLine();
                        try {
                            // Message is sent to the server
                            dos.writeUTF(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            });

            // Input data flow
            Thread readMessage = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            // Message is received from the server
                            String message = dis.readUTF();
                            // Message is printed in the client console
                            System.out.println(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            });

            sendMessage.start();
            readMessage.start();
            // Connection failed
        } catch (Exception e) {
            System.out.println("[<SYSTEM> Communication protocol failed. One connection terminated.\n" + e + "]");
        }
    }
}