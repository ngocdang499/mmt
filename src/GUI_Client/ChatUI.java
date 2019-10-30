package GUI_Client;
import Protocol.AttachTagsMsg;
import Protocol.DetachTagsMsg;

import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import Class.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ChatUI implements Initializable {

    @FXML
    VBox scrlChat;

    @FXML
    VBox vbFriend;

    @FXML
    TextArea txtArea;
    @FXML
    Label lbChatID;

    User current;
    ArrayList<User> peerLst = new ArrayList<User>();
    private static Socket socketClient;
    public String updatePeer(MouseEvent e) throws Exception {

        Button button = (Button) e.getSource();
        //String msg = AttachTagsMsg.processRequestChatIP("khuong");
        // Send request ID verification to server
        String host = "";

        return host;
    }

    public void transferClientData(User user, String List){
        //Display the message
        current = new User(user.getUsrID(),user.getUsrIP(),user.getUsrPasswd(),user.getUsrPort(),user.getUsrStatus());
        peerLst = DetachTagsMsg.getSendIP(List);
        System.out.println("List :" + List);
        int i;
        for(i=0;i<peerLst.size();i++) {
            createPeerTab(peerLst.get(i));
        }
    }

    private void genCommingMess(String u, String s) {
        VBox tmpB = new VBox();
        Label usr = new Label(u);
        Label mess = new Label(s);
        usr.getStylesheets().add(ChatUI.class.getResource("Usr.css").toExternalForm());
        mess.getStylesheets().add(ChatUI.class.getResource("IncomeMesg.css").toExternalForm());
        tmpB.getChildren().addAll(usr, mess);
        scrlChat.getChildren().add(tmpB);
    }

    private void genSendingMess(String s) {
        Label mess = new Label(s);
        mess.getStylesheets().add(ChatUI.class.getResource("../GUI_CSS/SendMesg.css").toExternalForm());
        scrlChat.getChildren().add(mess);
    }


    public void write(String s) {
        // Display received message
        String[] id_mess = s.split(":", 2);
        genCommingMess(id_mess[0], id_mess[1]);
    }

    public void onbtnSendClick(MouseEvent e) throws Exception {
        Button button = (Button) e.getSource();
        //String msg = AttachTagsMsg.processRequestChatIP("khuong");
        //System.out.println(msg);
        String host = "";
        // Send request ID verification to server
        try
        {
            int recvport = 44000;
            //InetAddress server_ip_addr = InetAddress.get;
            socketClient = new Socket("192.168.137.1", recvport);
            // Encode message (user-defined protocol)
            String message = AttachTagsMsg.processRequestChatIP("khuong");
            // Send message to the server
            ObjectOutputStream sender = new ObjectOutputStream(socketClient.getOutputStream());
            sender.writeObject(message); sender.flush();
            // Get acknowledgment from the server
            ObjectInputStream listener = new ObjectInputStream(socketClient.getInputStream());
            message = (String) listener.readObject();
            System.out.println(message);
            host = message;
            // Close socket
            socketClient.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            //Closing the socket
            try
            {
                socketClient.close();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }


        System.out.println(host);
        if (txtArea.getText() != null) {
            // Display message
            String msg = txtArea.getText();
            genSendingMess(msg);
            // Send mess to Other peer
            //========================================================================================
            try
            {
                int recvport = 50000;

                socket = new Socket(host,recvport);

                String tmp = txtArea.getText();
                if(tmp.contains("\n")) {
                    tmp.replaceAll("\n","(\\ n)");
                }

                String sendTxt = AttachTagsMsg.processTextMessage(lbChatID.getText(),tmp);
                //Send the message to the server
                //PrintStream o_stream = new PrintStream(socket.getOutputStream());
                ObjectOutputStream sender = new ObjectOutputStream(socket.getOutputStream());
                sender.writeObject(sendTxt);
                sender.flush();
                sender.close();
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
            finally
            {
                //Closing the socket
                try
                {
                    socket.close();
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }

        // clear the text area
        txtArea.setText("");
    }

    public void createPeerTab(User user){
        HBox tmp = new HBox();
        Button nameTag = new Button(user.getUsrID());
        Circle c1 = new Circle(5);
        nameTag.getStylesheets().add(ChatUI.class.getResource("../GUI_CSS/NameTag.css").toExternalForm());
        tmp.getChildren().addAll(nameTag,c1);
        if (user.getUsrStatus() == 1) {
            c1.setFill(Paint.valueOf("#50e09e"));
           // HBox.setMargin(c1,new Insets(25,20,25,10));
            vbFriend.getChildren().add(0, tmp);
        }
        else {
            c1.setFill(Paint.valueOf("#bcbcbc"));
            vbFriend.getChildren().add( tmp);
        }
    }

    public void clearPeerTab(){
        vbFriend.getChildren().clear();
    }

    public void onbtnSendFileclicked(MouseEvent e) throws Exception {
        Button button = (Button) e.getSource();
        //String msg = AttachTagsMsg.processRequestChatIP("khuong");
        //System.out.println(msg);
        String host = "";
        // Send request ID verification to server
        try
        {
            int recvport = 44000;
            //InetAddress server_ip_addr = InetAddress.get;
            socketClient = new Socket("192.168.137.1", recvport);
            // Encode message (user-defined protocol)
            String message = AttachTagsMsg.processRequestChatIP("khuong");
            // Send message to the server
            ObjectOutputStream sender = new ObjectOutputStream(socketClient.getOutputStream());
            sender.writeObject(message); sender.flush();
            // Get acknowledgment from the server
            ObjectInputStream listener = new ObjectInputStream(socketClient.getInputStream());
            message = (String) listener.readObject();
            System.out.println(message);
            host = message;
            // Close socket
            socketClient.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            //Closing the socket
            try
            {
                socketClient.close();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }


        System.out.println(host);

        ChatGui c = new ChatGui(new Socket(host,50000),50000);


        //        //Open windows for choosing file
//        Stage stage = new Stage();
//        final FileChooser fileChooser = new FileChooser();
//        File file = fileChooser.showOpenDialog(stage);
//        if (file != null) {
//            openFile(file);
//        }
    }

    private Desktop desktop = Desktop.getDesktop();

    void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                    FileChooserSample.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }



    /* When successfully logged in, start sending request to MainServer
     * to retrieve user list and show that list in vbFriend.
     * Update the list and friend status every 1 minutes
     */


    private static Socket socket;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            int i;
            for(i = 0; i < peerLst.size(); i++) {
                createPeerTab(peerLst.get(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//===================================================Start Listening for Chat Request====================================================
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int port = 50000;
                    ServerSocket serverSocket = new ServerSocket(port);
                    System.out.println("Server Started and listening to the port " + port);

                    //Listener is running always. This is done using this while(true) loop
                    while (true) {
                        //Reading the message from the peer
                        socket = serverSocket.accept();
                        ObjectInputStream receiver = new ObjectInputStream(socket.getInputStream());
                        String clientRequest = (String) receiver.readObject();

                        // Analyse message
                        String peerRequest = DetachTagsMsg.getTextMessage(clientRequest);
                        if (peerRequest != null) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //update application thread
                                    String tmp = peerRequest.replaceAll("(\\ n)", "\n");
                                    String[] id_mess = tmp.split("::", 2);
                                    genCommingMess(id_mess[0], id_mess[1]);
                                }
                            });
                        }

                        String offID = DetachTagsMsg.getDiedAccount(clientRequest);
                        // Search for ID in peerlist
                        if(offID != null) {
                            for (User u : peerLst) {
                                if (u.getUsrID().matches(offID))
                                    u.setUsrStatus(0);
                            }
                            clearPeerTab();
                            for (User u : peerLst) {
                                createPeerTab(u);
                            }
                        }

                        User onID = DetachTagsMsg.getOnlAccount(clientRequest);
                        // Search for ID in peerlist
                        if(offID != null) {
                            for (User u : peerLst) {
                                if (u.getUsrID().matches(onID.getUsrID()))
                                    u.setUsrStatus(1);
                                else
                                    peerLst.add(onID);
                            }
                            clearPeerTab();
                            for (User u : peerLst) {
                                createPeerTab(u);
                            }
                        }



                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    try
                    {

                    }
                    catch(Exception e)
                    {

                    }
                }
            }
        }).start();


    }
}




