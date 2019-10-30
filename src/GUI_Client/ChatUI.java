package GUI_Client;
import Protocol.AttachTagsMsg;
import Protocol.DetachTagsMsg;

import Protocol.Tags;
import data.FileData;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import Class.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.*;
import javafx.stage.Window;
import tags.Decode;


public class ChatUI implements Initializable {

    @FXML
    Button btnLogout;

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
        // Send request ID verification to server
        String message = "";
        try
        {
            int recvport = 44000;
            //InetAddress server_ip_addr = InetAddress.get;
            socketClient = new Socket("192.168.137.1", recvport);
            // Encode message (user-defined protocol)
            message = Tags.UPDATE_PEER_LST_TAG;
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

        clearPeerTab();
        peerLst = DetachTagsMsg.getSendIP(message);
        System.out.println("List :" + peerLst.size());
        int i;
        for(i=0;i<peerLst.size();i++) {
            createPeerTab(peerLst.get(i));
        }

        System.out.println(host);


        return host;
    }

    public static String SERVER = "";

    public void transferClientData(User user, String List, String server){
        SERVER = server;
        //Display the message
        current = new User(user.getUsrID(),user.getUsrIP(),user.getUsrPasswd(),user.getUsrPort(),user.getUsrStatus());
        peerLst = DetachTagsMsg.getSendIP(List);
        System.out.println("List :" + peerLst.size());
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


    public void sendTxtMessage(String msg1, String PeerName) {
        String host = "";
        // Send request ID verification to server
        try
        {
            int recvport = 44000;
            //InetAddress server_ip_addr = InetAddress.get;
            socketClient = new Socket("192.168.137.1", recvport);
            // Encode message (user-defined protocol)
            String message = AttachTagsMsg.processRequestChatIP(PeerName);
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
        {
            // Display message
            String msg = msg1;
            genSendingMess(msg);
            // Send mess to Other peer
            //========================================================================================
            try
            {
                int recvport = 50000;

                socket = new Socket(host,recvport);

                String tmp = msg1;
                if(tmp.contains("\n")) {
                    tmp = tmp.replaceAll("\n","(\\ n)");
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
    }


    public void onbtnSendClick(MouseEvent e) throws Exception {
        Button button = (Button) e.getSource();
        //String msg = AttachTagsMsg.processRequestChatIP("khuong");
        //System.out.println(msg);
        if(!(txtArea.getText().isBlank() || lbChatID.getText().isBlank())) {
            sendTxtMessage(txtArea.getText(), lbChatID.getText());
        }


        // clear the text area
        txtArea.setText("");

    }

    public void createPeerTab(User user){
        HBox tmp = new HBox();
        Button nameTag = new Button(user.getUsrID());
        nameTag.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                lbChatID.setText(nameTag.getText());
            }
        });

        Circle c1 = new Circle(5);
        nameTag.getStylesheets().add(ChatUI.class.getResource("../GUI_CSS/NameTag.css").toExternalForm());
        tmp.getChildren().addAll(nameTag,c1);
        if (user.getUsrStatus() == 1) {
            c1.setFill(Paint.valueOf("#50e09e"));
            HBox.setMargin(c1,new Insets(25,25,20,20));
            vbFriend.getChildren().add(0, tmp);
        }
        else {
            c1.setFill(Paint.valueOf("#bcbcbc"));
            HBox.setMargin(c1,new Insets(25,25,20,20));
            vbFriend.getChildren().add( tmp);
        }
    }

    public void clearPeerTab(){
        vbFriend.getChildren().clear();
    }

    public void onbtnLogoutClicked(MouseEvent e) throws  Exception {
        Button button = (Button) e.getSource();

        String host = "";
        // Send request ID verification to server
        String message = "";
        try
        {
            int recvport = 44000;
            //InetAddress server_ip_addr = InetAddress.get;
            socketClient = new Socket("192.168.137.1", recvport);
            // Encode message (user-defined protocol)
            message = AttachTagsMsg.processOfflineStatus(current.getUsrID());
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

        Stage stage = (Stage) button.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginUI.fxml"));/* Exception */
        Parent root = loader.load();
        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.show();
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


        ChatGui c = new ChatGui("khuong",new Socket(host,50000),50000);
//        FileChooser fileChooser = new FileChooser();
//        File selectedFile = fileChooser.showOpenDialog(new Stage());
//        System.out.println(selectedFile.getAbsolutePath());

    }

    /* When successfully logged in, start sending request to MainServer
     * to retrieve user list and show that list in vbFriend.upd
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
                        if (peerRequest.matches( "NhanFileKhong")){
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation Dialog");
                            alert.setHeaderText("Look, a Confirmation Dialog");
                            alert.setContentText("Are you ok with this?");

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK){
                                DirectoryChooser dc = new DirectoryChooser();
                                Stage window = new Stage();
                                window.initModality(Modality.APPLICATION_MODAL);
                                window.setTitle("A File was sent to you!");
                                window.setWidth(300);
                                window.setHeight(200);
                                dc.showDialog(window);
                                File f = dc.getInitialDirectory();
                                String s = f.getAbsolutePath();
                                new ChatGui.Client(s);
                            } else {
                                socket.close();
                                // ... user chose CANCEL or closed the dialog

                            }

                        }
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
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    }

    /////////////////////////////////////////////////////////////////////////////////////

    public void copyFileReceive(
            InputStream inputStr,
            OutputStream outputStr,
            String path)
            throws IOException {

        byte[] buffer = new byte[1024];
        int lenght;
        while ((lenght = inputStr.read(buffer)) > 0)
            outputStr.write(buffer, 0, lenght);

        inputStr.close();
        outputStr.close();
        File fileTemp = new File(path);
        if (fileTemp.exists()) fileTemp.delete();

    }



}




