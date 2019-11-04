package GUI_Client;
import Communicate.FileClient;
import Communicate.*;
import Protocol.AttachTagsMsg;
import Protocol.DetachTagsMsg;

import Protocol.Tags;
import Server.AcceptFileBox;
import Server.AlertBox;
import data.FileData;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Class.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.*;



public class ChatUI implements Initializable {

    private static Integer TXTPORT = 50000;
    private static Integer SVRPORT = 44000;
    private static Integer FILEPORT = 60000;
    private static String current_peer = "";
    public static String SERVER = "";
    private static Socket socket;


    @FXML
    Button btnLogout;

    @FXML
    VBox scrlChat;

    @FXML
    VBox vbFriend;

    @FXML
    TextArea txtArea;

    @FXML
    Button btnSearch;

    @FXML
    TextField txtSearch;

    @FXML
    Button btnMessage;


    private User current;
    private ArrayList<User> peerLst = new ArrayList<User>();

    public void transferUserData(User user, String List, String server){

        SERVER = server;
        //Display the message
        current = new User(user.getUsrID(),user.getUsrIP(),user.getUsrPasswd(),user.getUsrPort(),user.getUsrStatus());
        peerLst = DetachTagsMsg.getSendIP(List);

        System.out.println("List :" + peerLst.size());
        int i;
        for(i=0;i<peerLst.size();i++) {
            System.out.println(peerLst.get(i).getUsrIP());
            if (peerLst.get(i).getUsrID().matches(current.getUsrID()))
                continue;
            createPeerTab(peerLst.get(i));
        }
        SendMsg.broadcastStatus(current,peerLst,1);
    }

    private static Socket socketClient;
    public String updatePeerTab(MouseEvent e) throws Exception {
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

    @FXML
    TabPane tabPane;

    private void genCommingMess(String u, String s) {

        for(Tab t : tabPane.getTabs()) {
            if(t.getText().matches(u)) {
                AnchorPane pane = (AnchorPane)t.getContent();
                ScrollPane scrollChat= (ScrollPane) pane.getChildren().get(0);
                scrollChat.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                VBox vb = (VBox)scrollChat.getContent();


                VBox tmpB = new VBox();
                Label usr = new Label(u);
                Label mess = new Label(s);
                mess.setWrapText(true);
                mess.setMinWidth(400);
                usr.getStylesheets().add(ChatUI.class.getResource("Usr.css").toExternalForm());
                mess.getStylesheets().add(ChatUI.class.getResource("IncomeMesg.css").toExternalForm());
                tmpB.getChildren().addAll(usr, mess);

                vb.getChildren().add(tmpB);
                scrollChat.setContent(vb);
                scrollChat.vvalueProperty().bind((ObservableValue<? extends Number>) vb.heightProperty());
                return;
            }
        }

        VBox vb = new VBox();
        vb.setStyle("-fx-max-width: 520px");
        vb.setStyle("-fx-spacing: 20px");
        ScrollPane scrollChat = new ScrollPane(vb);
        scrollChat.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollChat.getStylesheets().add(ChatUI.class.getResource("../GUI_CSS/ChatArea.css").toExternalForm());
        AnchorPane pane = new AnchorPane(scrollChat);
        pane.getStylesheets().add(ChatUI.class.getResource("../GUI_CSS/AnchorPane.css").toExternalForm());
        Tab tab = new Tab(u,pane);
        tabPane.getTabs().add(tab);

        VBox tmpB = new VBox();
        Label usr = new Label(u);
        Label mess = new Label(s);
        mess.setMinWidth(400);
        mess.setWrapText(true);
        usr.getStylesheets().add(ChatUI.class.getResource("Usr.css").toExternalForm());
        mess.getStylesheets().add(ChatUI.class.getResource("IncomeMesg.css").toExternalForm());
        tmpB.getChildren().addAll(usr, mess);

        vb.getChildren().add(tmpB);
        scrollChat.setContent(vb);
        scrollChat.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollChat.vvalueProperty().bind((ObservableValue<? extends Number>) vb.heightProperty());
    }

    private void genSendingMess(String s) {
        AnchorPane pane = (AnchorPane)tabPane.getSelectionModel().getSelectedItem().getContent();
        ScrollPane scrollChat= (ScrollPane) pane.getChildren().get(0);
        scrollChat.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        VBox vb = (VBox)scrollChat.getContent();

        Label mess = new Label(s);
        mess.setMinWidth(400);
        mess.setWrapText(true);
        mess.getStylesheets().add(ChatUI.class.getResource("../GUI_CSS/SendMesg.css").toExternalForm());
        vb.getChildren().add(mess);
        scrollChat.setContent(vb);
        scrollChat.vvalueProperty().bind((ObservableValue<? extends Number>) vb.heightProperty());
    }


    public void onbtnSendClick(MouseEvent e) throws Exception {
        Button button = (Button) e.getSource();
        String host = SendMsg.getIP(tabPane.getSelectionModel().getSelectedItem().getText(),peerLst);
        System.out.println("Host" + host);
        if(!(txtArea.getText().isBlank() || host==null)) {
            SendMsg.sendMessage(current,txtArea.getText(),host,TXTPORT);
        }
        genSendingMess(txtArea.getText());

        // clear the text area
        txtArea.setText("");
    }


    public void createPeerTab(User user){
        HBox tmp = new HBox();
        Button nameTag = new Button(user.getUsrID());
        nameTag.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                for(Tab t : tabPane.getTabs()){
                    if(t.getText().matches(user.getUsrID())){
                        tabPane.getSelectionModel().select(t);
                        return;
                    }
                }
                VBox vb = new VBox();
                vb.setStyle("-fx-max-width: 520px");
                vb.setStyle("-fx-spacing: 20px");
                ScrollPane scrollChat = new ScrollPane(vb);
                scrollChat.vvalueProperty().bind((ObservableValue<? extends Number>) vb.heightProperty());
                scrollChat.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                scrollChat.getStylesheets().add(ChatUI.class.getResource("../GUI_CSS/ChatArea.css").toExternalForm());
                AnchorPane pane = new AnchorPane(scrollChat);
                pane.getStylesheets().add(ChatUI.class.getResource("../GUI_CSS/AnchorPane.css").toExternalForm());
                Tab tab = new Tab(user.getUsrID(),pane);
                tabPane.getTabs().add(tab);
                tabPane.getSelectionModel().select(tab);

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

        // Send notice to server
        String message = "";
        try
        {
            int recvport = 44000;
            //InetAddress server_ip_addr = InetAddress.get;
            socketClient = new Socket(SERVER, recvport);
            // Encode message (user-defined protocol)
            message = AttachTagsMsg.processOfflineStatus(current.getUsrID());
            // Send message to the server
            ObjectOutputStream sender = new ObjectOutputStream(socketClient.getOutputStream());
            sender.writeObject(message); sender.flush();
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

        ///Broadcast status update to other peer and server
        SendMsg.broadcastStatus(current,peerLst,0);

        try
        {
            socket.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }


        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginUI.fxml"));/* Exception */
        Parent root = loader.load();
        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.show();
    }


    private static Socket Fsocket;
    public void onbtnSendFileclicked(MouseEvent e) throws Exception {
        Button button = (Button) e.getSource();
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        String path = selectedFile.getAbsolutePath();
        System.out.println(path);
        File myFile = new File(path);
//        byte[] myByteArray = new byte[(int) myFile.length()];

        try
        {
            String host = SendMsg.getIP(tabPane.getSelectionModel().getSelectedItem().getText(),peerLst);
            //String host = "192.168.43.200";
            Fsocket = new Socket(host,TXTPORT);
            // Encode message (user-defined protocol)
            String message = AttachTagsMsg.processFileMessage(tabPane.getSelectionModel().getSelectedItem().getText(),
                                                                path.substring(path.lastIndexOf("/") + 1));
            // Send message to the server
            ObjectOutputStream sender = new ObjectOutputStream(Fsocket.getOutputStream());
            sender.writeObject(message); sender.flush();
            System.out.println("314: " + message);
            // Get acknowledgment from the server
            ObjectInputStream listener = new ObjectInputStream(Fsocket.getInputStream());
            message = (String) listener.readObject();
            System.out.println("Accept or not?" + message);
            // Close socket
            Fsocket.close();

            if (message.matches(Tags.FILE_ACCEPT_TAG)) {
                SendMsg.sendMessage(current,myFile,host,FILEPORT);

            }
            else {
                AlertBox alertbox = new AlertBox();
                alertbox.display("Failed", tabPane.getSelectionModel().getSelectedItem().getText() + " doesn't accept your file.");
            }

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
                Fsocket.close();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    /* When successfully logged in, start sending request to MainServer
     * to retrieve user list and show that list in vbFriend.upd
     * Update the list and friend status every 1 minutes
     */



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

        /*
        try {
            int i;
            for(i = 0; i < peerLst.size(); i++) {
                System.out.println(peerLst.get(i).getUsrID());
                createPeerTab(peerLst.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
//================================================Start Listening for Chat Request===================================

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int port = TXTPORT;
                    ServerSocket serverSocket = new ServerSocket(port);
                    System.out.println("Server Started and listening to the port " + port);

                    //Listener is running always. This is done using this while(true) loop
                    while (true) {
                        //Reading the message from the peer
                        socket = serverSocket.accept();
                        ObjectInputStream receiver = new ObjectInputStream(socket.getInputStream());
                        String clientRequest = (String) receiver.readObject();
                        System.out.println(clientRequest);

                        // Analyse message
                        // Case 1: Text Message sent from peer
                        final String peerRequest = DetachTagsMsg.getTextMessage(clientRequest);
                        //System.out.println(peerRequest);
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

                        // Case 2: Request sending file from peer
                        final String fileRequest = DetachTagsMsg.checkFile(clientRequest);
                        System.out.println(fileRequest);
                        if (fileRequest != null) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("Inside case 2");
                                    //update application thread
                                    String tmp = fileRequest.replaceAll("\\ n", "\n");
                                    String[] id_fname = tmp.split("::", 2);
                                    AcceptFileBox afbox = new AcceptFileBox();
                                    afbox.display("Accept File", id_fname,socket,current,peerLst);
     ////////////////////////////////////////////////Hien cua so thong bao///////////////////////////////////////////////
                                }
                            });
                        }

                        // Case 3: Status update
                        String offID = DetachTagsMsg.getDiedAccount(clientRequest);
                        // Search for ID in peerlist

                        if(offID != null) {
                            for (User u : peerLst) {
                                if (u.getUsrID().matches(offID)) {
                                    u.setUsrStatus(0);
                                    u.setUsrIP("");
                                    break;
                                }
                            }
                            Platform.runLater(new Runnable() {

                                @Override
                                public void run() {
                                    clearPeerTab();
                                    for (User u : peerLst) {
                                        if(!u.getUsrID().matches(current.getUsrID()))
                                            createPeerTab(u);
                                    }
                                }
                            });
                        }

                        User onID = DetachTagsMsg.getOnlAccount(clientRequest);
                        // Search for ID in peerlist
                        if(onID != null) {
                            System.out.println("onID: " + onID.getUsrIP());
                            int i;
                            for (i = 0; i<peerLst.size(); i++) {
                                if (peerLst.get(i).getUsrID().matches(onID.getUsrID())) {
                                    peerLst.get(i).setUsrStatus(1);
                                    peerLst.get(i).setUsrIP(onID.getUsrIP());
                                    System.out.println("Listen to status update => update peerlst ?!");
                                    break;
                                }
                                else
                                    peerLst.add(onID);
                            }
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {

                                    clearPeerTab();
                                    for (User u : peerLst) {
                                        if(!u.getUsrID().matches(current.getUsrID()))
                                            createPeerTab(u);
                                    }
                                }
                            });
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



