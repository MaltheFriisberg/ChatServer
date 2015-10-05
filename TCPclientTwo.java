package Networking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mal
 */
public class TCPclientTwo {
    public String username;
    private ChatGUI cg;
    private Socket link;
    private DataInputStream input;
    private DataOutputStream output;
    public static void main(String[] args) {
        TCPclientTwo c = new TCPclientTwo("Hans");
    }
    public TCPclientTwo(String username) {
        this.username = username;
        this.cg = new ChatGUI();
        cg.addChatListener(new chatListener());
        try {
            this.link = new Socket(InetAddress.getLocalHost(), 5820);
            System.out.println("Connected to server");
            this.input = new DataInputStream(this.link.getInputStream());
            this.output = new DataOutputStream(this.link.getOutputStream());
            
            this.output.writeUTF("*** "+username+ " joined the chat");
            
            while(true) { //read the messages returned by server
                String msg = input.readUTF();
                //add msg to gui
                cg.AddMessage(msg);
            }
            
            
        } catch (IOException ex) {
            System.out.println("Could not connect to server " + ex);
        }
        
    }
    class chatListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == cg.getSendBtn()) {
                try {
                    //take the msg from the gui and send it to the server
                    output.writeUTF(username + ": "+cg.getChatMsg());
                    
                } catch (IOException ex) {
                    Logger.getLogger(TCPclientTwo.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                cg.clearMsgField();
                
            }
        }
    
    }
}
