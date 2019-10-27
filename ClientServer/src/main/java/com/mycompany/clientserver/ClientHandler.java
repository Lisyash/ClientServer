package com.mycompany.clientserver;

/**
 *
 * @author Lassie
 */
import java.io.*;
import java.net.*;

class ClientHandler implements Runnable {

    private String name;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private Socket socket;

    public ClientHandler(Socket socket, String name, DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.socket = socket;
    }

    ClientHandler(Socket socket, String username, BufferedReader dis, DataOutputStream dos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        String clientMessage;

        while (true) {
            try {
                clientMessage = dis.readUTF();
                // Logging messages to the server
                System.out.println("<CLIENT MESSAGE>" + this.name + "> " + clientMessage);

                // User can request to see the list of clients
                if (clientMessage.equalsIgnoreCase("clientlist")) {
                    System.out.println("<SYSTEM REQUEST>" + this.name + "> " + clientMessage);
                    for (ClientHandler client : Server.clients) {
                        this.dos.writeUTF("-" + " " + client.name);
                    }
                } // Otherwise the message will be just printed
                else for (ClientHandler client : Server.clients) {
                        client.dos.writeUTF(this.name + " > " + clientMessage);
                    }

                // A way for users to exit the chat-room
                if (clientMessage.equals("exit")) {
                    try {
                        System.out.println("<SYSTEM REQUEST>" + this.name + "> " + clientMessage);
                        this.socket.close();
                      	System.exit(0);
                    } catch (Exception e) {
                        System.out.println("[<SYSTEM> Communication protocol failed. One connection terminated.\n" + e + "]");
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("[<SYSTEM> Communication protocol failed. One connection terminated.\n" + e + "]");
                break;
            }
        }

        try {
            this.dos.close();
            this.dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}