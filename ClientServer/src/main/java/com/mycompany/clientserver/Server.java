package com.mycompany.clientserver;

/**
 *
 * @author Lassie
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

public class Server {

    public static int SERVER_PORT = 5000;
    public static String SERVER_HOST = "localhost";

    static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("<SERVER> Starting Services...");

        try {
            ServerSocket server = new ServerSocket(SERVER_PORT);

            while (true) {
                System.out.println("<SERVER> Ready to receive connection request - Waiting...");
                Socket socket = server.accept();

                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                System.out.println("<SERVER> Connection request detected. New thread created. Waiting for username input...");
                dos.writeUTF("SERVER> Please input username:");
                String username = dis.readUTF();
                System.out.println("<CLIENT>"+ username +"> Username received. Registering user...");
                dos.writeUTF("SERVER> Joined the chat-room as " + username + ". Welcome! Type your message or 'exit' to end the process");

                ClientHandler clientHandler = new ClientHandler(socket, username, dis, dos);
                Thread thread = new Thread(clientHandler);

                System.out.println("<SERVER>"+ username +"> Connection completed successfully! Thread started. User " + username + " is now connected.");
                clients.add(clientHandler);
              	boolean done = false;
                while(!done)
                {
                    try{
                      	String line = dis.readUTF();
                      	if(line.equals("exit") || line.equals("Exit")){	
                          done=true;
                          dos.writeUTF("SERVER> Leave request accepted. Connection terminated. Goodbye!");
                          socket.close();
                          System.exit(0);
                          }
                        else{
                        	System.out.println(username+": "+line);
                      		dos.writeUTF("<SERVER>Message received, type another message or 'exit' to end the connection");
                        	}
                    }
                    		
                    catch(IOException ioe){
                        done=true;
                  	}
                }
                thread.start();
            }

        } catch (IOException e) {
            System.out.println("[<SYSTEM> Communication protocol failed. One connection terminated.\n" + e + "]");
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
        }

    }

}
