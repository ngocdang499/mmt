package Protocol;

import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageListener extends Thread{

    ServerSocket server;
    int port = 44000;
    WritableGUI gui;
    public MessageListener (WritableGUI gui,int port) {
        this.port = port;
        this.gui = gui;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            Logger.getLogger(MessageListener.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    public MessageListener () {
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            Logger.getLogger(MessageListener.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    @Override
    public void run() {
        Socket clientSocket;

            try {
                while ((clientSocket = server.accept()) != null) {
                    InputStream is = clientSocket.getInputStream();
                    // Convert is to string
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = br.readLine();
                    if(line != null) {
                        gui.write(line);
                    }
                }
            } catch (IOException e) {
                Logger.getLogger(MessageListener.class.getName()).log(Level.SEVERE,null,e);
            }


    }
}
