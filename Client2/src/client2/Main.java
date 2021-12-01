/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client2;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author retsi
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {

        try (Socket socket = new Socket("localhost", 6666)) {

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            String text = "";
            Parser parser = new Parser();

            writer.println("Hello tracker");

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = "";
            
            //listening to messages
            do {

                line = reader.readLine();

                System.out.println(line);
                
                if(line.contains("PING")){
                    
                    writer.println("PONG " + parser.nthWordFromMsg(line, 2));
                }
                
                Thread.sleep(100);

            } while (!text.equals("bye"));

            socket.close();
        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
