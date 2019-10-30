package GUI_Client;
import Protocol.AttachTagsMsg;
import Protocol.DetachTagsMsg;

import data.FileData;
import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.geometry.Insets;
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
import tags.Decode;
import tags.Tags;

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


        //ChatGui c = new ChatGui(new Socket(host,50000),50000);
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        System.out.println(selectedFile.getAbsolutePath());

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

    /*
    private static String URL_DIR = System.getProperty("user.dir");
    private static String TEMP = "/temp/";
    public class ChatRoom extends Thread {


        private Socket connect;
        private ObjectOutputStream outPeer;
        private ObjectInputStream inPeer;
        private boolean continueSendFile = true, finishReceive = false;
        private int sizeOfSend = 0, sizeOfData = 0, sizeFile = 0, sizeReceive = 0;
        private String nameFileReceive = "";
        private InputStream inFileSend;
        private FileData dataFile;
        private String username = "", guest_name = "", file_name = "";
        public boolean isStop = false, isSendFile = false, isReceiveFile = false;

        public ChatRoom(Socket connection, String name, String guest)
                throws Exception {
            connect = new Socket();
            connect = connection;
            guest_name = guest;
        }

        @Override
        public void run() {
            super.run();
            OutputStream out = null;
            while (!isStop) {
                try {
                    // Get data from another peer
                    inPeer = new ObjectInputStream(connect.getInputStream());
                    Object obj = inPeer.readObject();

                    if (obj instanceof String) {
                        String msgObj = obj.toString();

                        if (msgObj.equals(Tags.CHAT_CLOSE_TAG)) {
                            isStop = true;
                            System.out.println("CLose Tag");

                            connect.close();
                            break;
                        }

                        if (Decode.checkFile(msgObj)) {
                            isReceiveFile = true;
                            nameFileReceive = msgObj.substring(10,
                                    msgObj.length() - 11);

                            System.out.println("checFile");

                            if (result == 0) {
                                File fileReceive = new File(URL_DIR + TEMP
                                        + "/" + nameFileReceive);
                                if (!fileReceive.exists()) {
                                    fileReceive.createNewFile();
                                }
                                String msg = Tags.FILE_REQ_ACK_OPEN_TAG
                                        + Integer.toBinaryString(portServer)
                                        + Tags.FILE_REQ_ACK_CLOSE_TAG;
                                sendMessage(msg);
                            } else {
                                sendMessage(Tags.FILE_REQ_NOACK_TAG);
                            }
                        }

                        if (Decode.checkFeedBack(msgObj)) {
                            new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        sendMessage(Tags.FILE_DATA_BEGIN_TAG);
                                        updateChat("you are sending file:	" + file_name);
                                        isSendFile = false;
                                        sendFile(textPath.getText());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        } else if (msgObj.equals(Tags.FILE_REQ_NOACK_TAG)) {
                            JOptionPane.showMessageDialog(
                                    fmChat, guest_name + " wantn't receive file");
                        } else if (msgObj.equals(Tags.FILE_DATA_BEGIN_TAG)) {
                            finishReceive = false;
                            lbReceiving.setVisible(true);
                            out = new FileOutputStream(URL_DIR + TEMP
                                    + nameFileReceive);
                        } else if (msgObj.equals(Tags.FILE_DATA_CLOSE_TAG)) {
                            updateChat("You receive file:	" + nameFileReceive + " with size" + sizeReceive + " KB");
                            sizeReceive = 0;
                            out.flush();
                            out.close();
                            lbReceiving.setVisible(false);
                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    showSaveFile();
                                }
                            }).start();
                            finishReceive = true;
                        } else {
                            String message = Decode.getTextMessage(msgObj);
                            updateChat("[" + guest_name + "] : " + message);
                        }
                    } else if (obj instanceof FileData) {
                        FileData data = (FileData) obj;
                        ++sizeReceive;
                        out.write(data.data);
                    }
                } catch (Exception e) {
                    File fileTemp = new File(URL_DIR + TEMP + nameFileReceive);
                    if (fileTemp.exists() && !finishReceive) {
                        fileTemp.delete();
                    }
                }
            }
        }

        private void getData(String path) throws Exception {
            File fileData = new File(path);
            if (fileData.exists()) {
                sizeOfSend = 0;
                dataFile = new FileData();
                sizeFile = (int) fileData.length();
                sizeOfData = sizeFile % 1024 == 0 ? (int) (fileData.length() / 1024)
                        : (int) (fileData.length() / 1024) + 1;
                lbSending.setVisible(true);
                progressSendFile.setVisible(true);
                progressSendFile.setValue(0);
                inFileSend = new FileInputStream(fileData);
            }
        }

        public void sendFile(String path) throws Exception {
            getData(path);
            lbSending.setText("Sending ...");
            do {
                if (continueSendFile) {
                    continueSendFile = false;
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                inFileSend.read(dataFile.data);
                                sendMessage(dataFile);
                                sizeOfSend++;
                                if (sizeOfSend == sizeOfData - 1) {
                                    int size = sizeFile - sizeOfSend * 1024;
                                    dataFile = new FileData(size);
                                }
                                progressSendFile
                                        .setValue((int) (sizeOfSend * 100 / sizeOfData));
                                if (sizeOfSend >= sizeOfData) {
                                    inFileSend.close();
                                    isSendFile = true;
                                    sendMessage(Tags.FILE_DATA_CLOSE_TAG);
                                    progressSendFile.setVisible(false);
                                    lbSending.setVisible(false);
                                    isSendFile = false;
                                    textPath.setText("");
                                    btnChoose.setEnabled(true);
                                    btnUpload.setEnabled(true);
                                    btnDelete.setEnabled(true);
                                    updateChat("!!!YOU ARE SEND FILE COMPLETE!!!");
                                    inFileSend.close();
                                }
                                continueSendFile = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            } while (sizeOfSend < sizeOfData);
        }
*/
    /*
        private void showSaveFile() {
            while (true) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System
                        .getProperty("user.home")));
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showSaveDialog(fmChat);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = new File(fileChooser.getSelectedFile()
                            .getAbsolutePath() + "/" + nameFileReceive );
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                            Thread.sleep(1000);
                            InputStream input = new FileInputStream(URL_DIR
                                    + TEMP + nameFileReceive);
                            OutputStream output = new FileOutputStream(
                                    file.getAbsolutePath());
                            copyFileReceive(input, output, URL_DIR + TEMP
                                    + nameFileReceive);
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(fmChat, "Your file receive has error!!!");
                        }
                        break;
                    } else {
                        int resultContinue = JOptionPane.showConfirmDialog(
                                fmChat, "File is exists. You want save file?", null,
                                JOptionPane.YES_NO_OPTION
                        );
                        if (resultContinue == 0)
                            continue;
                        else
                            break;
                    }
                }
            }
        }

        public synchronized void sendMessage(Object obj) throws Exception {
            outPeer = new ObjectOutputStream(connect.getOutputStream());
            if (obj instanceof String) {
                String message = obj.toString();
                outPeer.writeObject(message);
                outPeer.flush();
                if (isReceiveFile)
                    isReceiveFile = false;
            } else if (obj instanceof FileData) {
                outPeer.writeObject(obj);
                outPeer.flush();
            }
        }

        public void stopChat() {
            try {
                connect.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}*/

}




