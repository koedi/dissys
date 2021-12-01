
package tracker;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.awt.windows.ThemeReader;


public class Tracker {

    
    public static void main(String[] args) throws IOException {
        
        int userID = 0;
        ServerSocket serverSocket = new ServerSocket(6666);
        HashMap nodeList = new HashMap<String, String>();
        
        System.out.println("Waiting for clients on port: " + serverSocket.getLocalPort());
        
        //wating for clients
        while(true){
            Socket clientSocket = serverSocket.accept();
            userID++;
            System.out.println("New client at address: " + clientSocket.getRemoteSocketAddress());
            nodeList.put(userID, clientSocket.getRemoteSocketAddress());
            new ClientThread(clientSocket, userID, nodeList).start(); //new client thread
        }
    }
    
}
