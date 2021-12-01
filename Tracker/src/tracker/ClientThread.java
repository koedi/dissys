package tracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread extends Thread {

    private Socket socket;
    private int ID;
    private HashMap<String, String> nodeList;
    private long pingTime;
    private long passedTime;
    private long currentTime;

    ClientThread(Socket socket, int ID, HashMap nodeList) {
        this.socket = socket;
        this.ID = ID;
        this.nodeList = nodeList;
        this.pingTime = 10000;
        this.passedTime = 0;
        this.currentTime = System.currentTimeMillis();

    }

    public void setNewNodeList(HashMap<String, String> nodeList) {
        this.nodeList = nodeList;
    }
    
    public void closeSocket() throws IOException{
        socket.close();
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            String inLine;

            do {
                inLine = reader.readLine();
                System.out.println(inLine);
                if (inLine.equalsIgnoreCase("Hello tracker")) {
                    writer.println("Ok, your id is " + ID + "\n" + nodeList.toString());
                    inLine = "";
                }
                else if (!inLine.contains("PONG")){
                    closeSocket();
                }
                else {
                    Thread.sleep(100);
                }
                while (true) {
                    if (passedTime > pingTime) {
                        writer.println("PING " + this.ID);
                        currentTime = System.currentTimeMillis();
                        passedTime = 0;
                        Thread.sleep(10000);
                        break;
                    }
                    passedTime = System.currentTimeMillis() - currentTime;
                    Thread.sleep(100);
                }
            } while (!inLine.equals("bye"));

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
