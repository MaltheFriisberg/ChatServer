package Networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mal
 */
public class ClientHandlerTwo extends Thread {
    //reference til serveren der spawnede tråden. Bruges til at echo client messages videre til alle de andre klients
    ChatServerTwo server; 
    Socket client;
    DataOutputStream output;
    DataInputStream input;
    
    public ClientHandlerTwo(ChatServerTwo server, Socket link) {
        this.server = server;
        this.client = link;
        try {
            
            this.input = new DataInputStream(client.getInputStream());
            output = new DataOutputStream(client.getOutputStream());
            
        } catch (IOException ex) {
            System.out.println("could not setup I/O streams" + ex);
        }
        start();
        
    }
    @Override
    public void run() {
        try {
            // SÅ længe tråden kører(run) infinite loop
            while (true) {
                //lyt hele tiden til clientens DataInputStream
                String message = input.readUTF();
                
                System.out.println( "Sending "+message );
                //echo beskeden videre til øvrige clients
                server.sendToAll( message );
            }
    } catch( EOFException ie ) {
        
    } catch( IOException ie ) {
        
        ie.printStackTrace();
    } finally {
        server.removeConnection( this.client );
    }
}
    
    public void sendMsg(String Message) {
        
    }
    
}