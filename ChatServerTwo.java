package Networking;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mal
 */
public class ChatServerTwo {
    public static void main(String[] args) {
        ChatServerTwo s = new ChatServerTwo(5820);
    }
    private boolean running;
    private int port;
    private ServerSocket serversocket;
    private HashMap<Socket, DataOutputStream> map;
    
    public ChatServerTwo(int port) {
        this.port = port;
        this.map = new HashMap();
        try {
            
            this.serversocket = new ServerSocket(port);
            
        } catch (IOException ex) {
            System.out.println("could not setup server on port "+this.port+" \n "+ ex);
        }
        this.running = true;
        //infnite loop until running is set false
        while(running) {
            connectClients();
        }
        
    }
    private void connectClients() {
        Socket link = null;
        try {
            //put server in a waiting state
            System.out.println("waiting for clients");
            link = serversocket.accept();
            System.out.println("new Client connected");
            
            DataOutputStream outStream = new DataOutputStream(link.getOutputStream());
            map.put(link, outStream);
            System.out.println("client socket and stream added to hashmap");
            Thread clientThread = new Thread(new ClientHandlerTwo(this, link));
            System.out.println("Client thread startet");
            
        } catch (IOException ex) {
            System.out.println("Failed to connect client on port " +this.port+" "+ex);
        }
    }
    //echo klientens chat besked til alle connectede klienter. kaldes af ClientHandler(thread)
    public void sendToAll(String message) {
        //loop over hashmap med foreach entry
        //lånt fra http://stackoverflow.com/questions/4234985/how-to-for-each-the-hashmap
        for(Entry<Socket, DataOutputStream> entry : map.entrySet()) {
            Socket key = entry.getKey();
            DataOutputStream value = entry.getValue();
            try {
                value.writeUTF(message);
            } catch (IOException ex) {
                Logger.getLogger(ChatServerTwo.class.getName()).log(Level.SEVERE, null, ex);
            }
}
    }
    public void stopServer() {
        this.running = false;
    }
    public void removeConnection(Socket s) {
        this.map.remove(s);
    }
    
}
