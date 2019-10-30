package GUI_Client;

import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JEditorPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Label;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JPanel;

import SendFile.Client;
import SendFile.Server;
import data.FileData;
import tags.Decode;
import tags.Encode;
import tags.Tags;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.border.EmptyBorder;


public class ChatGui {

    private static String URL_DIR = System.getProperty("user.dir");
    private static String TEMP = "/temp/";

    private ChatRoom chat;
    private Socket socketChat;
    private String nameUser = "", nameGuest = "", nameFile = "";
    private JFrame frameChatGui;
    private JTextField textName;
    private JPanel panelMessage;
    private JTextPane txtDisplayChat;
    private Label textState, lblReceive;
    private JButton btnDisConnect, btnSend, btnChoose, btnSave;
    public boolean isStop = false, isSendFile = false, isReceiveFile = false;
    private JProgressBar progressSendFile;
    private JTextField txtPath;
    private int portServer = 0;
    private JTextField txtMessage;
    private JScrollPane scrollPane;

    public ChatGui(String user, String guest, Socket socket, int port) {
        nameUser = user;
        nameGuest = guest;
        socketChat = socket;
        this.portServer = port;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ChatGui window = new ChatGui(nameUser, nameGuest, socketChat, portServer, 0);
                    window.frameChatGui.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public ChatGui( Socket socket, int port) {
        socketChat = socket;
        this.portServer = port;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ChatGui window = new ChatGui(nameUser, nameGuest, socketChat, portServer, 0);
                    window.frameChatGui.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ChatGui window = new ChatGui();
                    window.frameChatGui.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateChat_receive(String msg) {
        appendToPane(txtDisplayChat, "<div class='left' style='width: 40%; background-color: #f1f0f0;'>"+ msg +"</div>");
    }

    public void updateChat_send(String msg) {
        appendToPane(txtDisplayChat, "<table class='bang' style='color: white; clear:both; width: 100%;'>"
                + "<tr align='right'>"
                + "<td style='width: 59%; '></td>"
                + "<td style='width: 40%; background-color: #0084ff;'>" + msg
                +"</td> </tr>"
                + "</table>");
    }

    public void updateChat_notify(String msg) {
        appendToPane(txtDisplayChat, "<table class='bang' style='color: white; clear:both; width: 100%;'>"
                + "<tr align='right'>"
                + "<td style='width: 59%; '></td>"
                + "<td style='width: 40%; background-color: #f1c40f;'>" + msg
                +"</td> </tr>"
                + "</table>");
    }


    public void updateChat_send_Symbol(String msg) {
        appendToPane(txtDisplayChat, "<table style='width: 100%;'>"
                + "<tr align='right'>"
                + "<td style='width: 59%;'></td>"
                + "<td style='width: 40%;'>" + msg
                +"</td> </tr>"
                + "</table>");
    }

    public ChatGui() {
        initialize();
    }

    public ChatGui(String user, String guest, Socket socket, int port, int a)
            throws Exception {
        nameUser = user;
        nameGuest = guest;
        socketChat = socket;
        this.portServer = port;
        initialize();
        chat = new ChatRoom(socketChat, nameUser, nameGuest);
        chat.start();
    }

    private void initialize() {
        File fileTemp = new File(URL_DIR + "/temp");
        if (!fileTemp.exists()) {
            fileTemp.mkdirs();
        }
        frameChatGui = new JFrame();
        frameChatGui.setTitle("Send & Receive");
        frameChatGui.setResizable(false);
        frameChatGui.setBounds(200, 200, 673, 645);
        frameChatGui.getContentPane().setLayout(null);
        frameChatGui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

//        JLabel lblClientIP = new JLabel("");
//        lblClientIP.setFont(new Font("Segoe UI", Font.PLAIN, 13));
//        lblClientIP.setBounds(30, 6, 41, 40);
//        lblClientIP.setIcon(new javax.swing.ImageIcon(ChatGui.class.getResource("/image/user_chat.png")));
//        frameChatGui.getContentPane().add(lblClientIP);

        textName = new JTextField(nameUser);
        textName.setForeground(Color.RED);
        textName.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        textName.setEditable(false);
        textName.setBounds(70, 6, 148, 40);
        frameChatGui.getContentPane().add(textName);
        textName.setText(nameGuest);
        textName.setColumns(10);

        panelMessage = new JPanel();
        panelMessage.setBounds(6, 363, 649, 201);
        panelMessage.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Message"));
        frameChatGui.getContentPane().add(panelMessage);
        panelMessage.setLayout(null);

        txtMessage = new JTextField("");
        txtMessage.setBounds(10, 21, 479, 62);
        //panelMessage.add(txtMessage);
        txtMessage.setColumns(10);

        btnSend = new JButton("");
        btnSend.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSend.setBounds(551, 33, 65, 39);
        btnSend.setBorder(new EmptyBorder(0, 0, 0, 0));
        btnSend.setContentAreaFilled(false);
        panelMessage.add(btnSend);
        btnSend.setIcon(new javax.swing.ImageIcon(ChatGui.class.getResource("/image/send.png")));

        btnChoose = new JButton("");
        btnChoose.setBounds(551, 152, 50, 36);
        panelMessage.add(btnChoose);
        btnChoose.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnChoose.setIcon(new javax.swing.ImageIcon(ChatGui.class.getResource("/image/attachment.png")));
        btnChoose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System
                        .getProperty("user.home")));
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(frameChatGui);
                if (result == JFileChooser.APPROVE_OPTION) {
                    isSendFile = true;
                    String path_send = (fileChooser.getSelectedFile()
                            .getAbsolutePath()) ;

                    System.out.println(path_send);
                    nameFile = fileChooser.getSelectedFile().getName();
                    txtPath.setText(path_send);
                }
            }
        });
//        btnSave = new JButton("");
//        btnSave.setBounds(200, 152, 50, 36);
//        panelMessage.add(btnSave);
//        btnSave.setFont(new Font("Segoe UI", Font.PLAIN, 13));
//        btnSave.setIcon(new javax.swing.ImageIcon(ChatGui.class.getResource("/image/attachment.png")));
//        btnSave.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                JFileChooser chooser = new JFileChooser();
//                chooser.setCurrentDirectory(new java.io.File("."));
//                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//                //
//                // disable the "All files" option.
//                //
//                chooser.setAcceptAllFileFilterUsed(false);
//                //
//                if (chooser.showOpenDialog(frameChatGui) == JFileChooser.APPROVE_OPTION) {
//                    System.out.println("getCurrentDirectory(): "
//                            +  chooser.getCurrentDirectory());
//                    new Client(chooser.getCurrentDirectory().getAbsolutePath());
////                    System.out.println("getSelectedFile() : "
////                            +  chooser.getSelectedFile());
//                }
//                else {
//                    System.out.println("No Selection ");
//                }
//            }
//        });
        btnChoose.setBorder(BorderFactory.createEmptyBorder());
        btnChoose.setContentAreaFilled(false);

        txtPath = new JTextField("");
        txtPath.setBounds(76, 163, 433, 25);
        panelMessage.add(txtPath);
        txtPath.setEditable(false);
        txtPath.setColumns(10);


        Label label = new Label("Path");
        label.setBounds(10, 166, 39, 22);
        panelMessage.add(label);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));



        //action when press button Send
        btnSend.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (txtPath.getText() != null)
                    try {

                        //chat.sendMessage(Encode.sendFile(nameFile));

                        new Server(txtPath.getText());

                        System.out.println("sent file");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                if (isStop) {
                    updateChat_send(txtMessage.getText().toString());
                    txtMessage.setText(""); //reset text Send
                    return;
                }
//                String msg = txtMessage.getText();
//                if (msg.equals(""))
//                    return;
//                try {
//                    chat.sendMessage(Encode.sendMessage(msg));
//                    updateChat_send(msg);
//                    txtMessage.setText("");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });

        txtMessage.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent arg0) {

            }

            @Override
            public void keyReleased(KeyEvent arg0) {

            }

            @Override
            public void keyPressed(KeyEvent arg0) {
                if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
                    String msg = txtMessage.getText();
                    if (isStop) {
                        updateChat_send(txtMessage.getText().toString());
                        txtMessage.setText("");
                        return;
                    }
                    if (msg.equals("")) {
                        txtMessage.setText("");
                        txtMessage.setCaretPosition(0);
                        return;
                    }
                    try {
                        chat.sendMessage(Encode.sendMessage(msg));
                        updateChat_send(msg);
                        txtMessage.setText("");
                        txtMessage.setCaretPosition(0);
                    } catch (Exception e) {
                        txtMessage.setText("");
                        txtMessage.setCaretPosition(0);
                    }
                }
            }
        });

        btnDisConnect = new JButton("LEAVE CHAT");
        btnDisConnect.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnDisConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int result = Tags.show(frameChatGui, "Are you sure to close chat with account: "
                        + nameGuest, true);
                if (result == 0) {
                    try {
                        isStop = true;
                        frameChatGui.dispose();
                        chat.sendMessage(Tags.CHAT_CLOSE_TAG);
                        chat.stopChat();
                        System.gc();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnDisConnect.setBounds(540, 6, 113, 40);
        frameChatGui.getContentPane().add(btnDisConnect);

        progressSendFile = new JProgressBar(0, 100);
        progressSendFile.setBounds(170, 577, 388, 14);
        progressSendFile.setStringPainted(true);
        frameChatGui.getContentPane().add(progressSendFile);
        progressSendFile.setVisible(false);

        textState = new Label("");
        textState.setBounds(6, 570, 158, 22);
        textState.setVisible(false);
        frameChatGui.getContentPane().add(textState);

        lblReceive = new Label("Receiving ...");
        lblReceive.setBounds(564, 577, 83, 14);
        lblReceive.setVisible(false);
        frameChatGui.getContentPane().add(lblReceive);

        txtDisplayChat = new JTextPane();
        txtDisplayChat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDisplayChat.setEditable(false);
        txtDisplayChat.setContentType( "text/html" );
        txtDisplayChat.setMargin(new Insets(6, 6, 6, 6));
        txtDisplayChat.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        txtDisplayChat.setBounds(6, 59, 670, 291);
        appendToPane(txtDisplayChat, "<div class='clear' style='background-color:white'></div>"); //set default background

        frameChatGui.getContentPane().add(txtDisplayChat);

        scrollPane = new JScrollPane(txtDisplayChat);
        scrollPane.setBounds(6, 59, 649, 291);
        frameChatGui.getContentPane().add(scrollPane);
    }

    public class ChatRoom extends Thread {

        private Socket connect;
        private ObjectOutputStream outPeer;
        private ObjectInputStream inPeer;
        private boolean continueSendFile = true, finishReceive = false;
        private int sizeOfSend = 0, sizeOfData = 0, sizeFile = 0,
                sizeReceive = 0;
        private String nameFileReceive = "";
        private InputStream inFileSend;
        private FileData dataFile;

        public ChatRoom(Socket connection, String name, String guest)
                throws Exception {
            connect = new Socket();
            connect = connection;
            System.out.println();

            nameGuest = guest;
        }

        @Override
        public void run() {
            super.run();
            OutputStream out = null;

            System.out.println("ChatRoom Ran");
            System.out.println(isStop);
            System.out.println("while");

            while (!isStop) {
                System.out.println("start while and loop");

                try {
                    System.out.println("trying");
                    System.out.println(connect.getPort());
                    System.out.println(connect.getLocalPort());
                    System.out.println(connect.getInputStream().read());
                    System.out.println(connect.getOutputStream().toString());

                    inPeer = new ObjectInputStream(connect.getInputStream());
                    System.out.println(inPeer);

                    System.out.println("trying to read");

                    Object obj = inPeer.readObject();
                    System.out.println("read");

                    System.out.println("obj Inpeer");
                    System.out.println(obj);


                    if (obj instanceof String) {
                        String msgObj = obj.toString();
                        System.out.println("obj");
                        System.out.println(msgObj);


                        if (msgObj.equals(Tags.CHAT_CLOSE_TAG)) {
                            isStop = true;
                            System.out.println("isStop = true");

                            Tags.show(frameChatGui, nameGuest
                                    + " closed chat with you! This windows will also be closed.", false);
                            try {
                                isStop = true;
                                frameChatGui.dispose();
                                chat.sendMessage(Tags.CHAT_CLOSE_TAG);
                                chat.stopChat();
                                System.gc();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            connect.close();
                            break;
                        }
                        if (Decode.checkFile(msgObj)) {
                            isReceiveFile = true;
                            System.out.println("isReceiveFile");

                            nameFileReceive = msgObj.substring(10,
                                    msgObj.length() - 11);
                            int result = Tags.show(frameChatGui, nameGuest
                                    + " send file " + nameFileReceive
                                    + " for you", true);
                            if (result == 0) {
                                File fileReceive = new File(URL_DIR + TEMP
                                        + "/" + nameFileReceive);
                                System.out.println("create file");

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
                        } else if (Decode.checkFeedBack(msgObj)) {
                            btnChoose.setEnabled(false);

                            new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        sendMessage(Tags.FILE_DATA_BEGIN_TAG);
                                        updateChat_notify("You are sending file: " + nameFile);
                                        isSendFile = false;
                                        sendFile(txtPath.getText());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        } else if (msgObj.equals(Tags.FILE_REQ_NOACK_TAG)) {
                            Tags.show(frameChatGui, nameGuest
                                    + " don't want receive file", false);
                        } else if (msgObj.equals(Tags.FILE_DATA_BEGIN_TAG)) {
                            finishReceive = false;
                            lblReceive.setVisible(true);
                            out = new FileOutputStream(URL_DIR + TEMP
                                    + nameFileReceive);
                        } else if (msgObj.equals(Tags.FILE_DATA_CLOSE_TAG)) {
                            updateChat_receive("You receive file: " + nameFileReceive + " with size " + sizeReceive + " KB");
                            sizeReceive = 0;
                            out.flush();
                            out.close();
                            lblReceive.setVisible(false);
                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    showSaveFile();
                                }
                            }).start();
                            finishReceive = true;
//
                        } else {
                            String message = Decode.getMessage(msgObj);
                            updateChat_receive(message);
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
                    System.out.println("Catched");

                }
            }
            System.out.println(isStop);

        }


        private void getData(String path) throws Exception {
            File fileData = new File(path);
            if (fileData.exists()) {
                sizeOfSend = 0;
                dataFile = new FileData();
                sizeFile = (int) fileData.length();
                sizeOfData = sizeFile % 1024 == 0 ? (int) (fileData.length() / 1024)
                        : (int) (fileData.length() / 1024) + 1;
                inFileSend = new FileInputStream(fileData);
            }
        }

        public void sendFile(String path) throws Exception {
            getData(path);

            textState.setVisible(true);
            if (sizeOfData > Tags.MAX_MSG_SIZE/1024) {
                textState.setText("File is too large...");
                inFileSend.close();
//				isFileLarge = true;
//				sendMessage(Tags.FILE_DATA_CLOSE_TAG);
                txtPath.setText("");
                btnChoose.setEnabled(true);
                isSendFile = false;
                inFileSend.close();
                return;
            }

            progressSendFile.setVisible(true);
            progressSendFile.setValue(0);

            textState.setText("Sending ...");
            do {
                System.out.println("sizeOfSend : " + sizeOfSend);
                if (continueSendFile) {
                    continueSendFile = false;
//					updateChat_notify("If duoc thuc thi: " + String.valueOf(continueSendFile));
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
                                    textState.setVisible(false);
                                    isSendFile = false;
                                    txtPath.setText("");
                                    btnChoose.setEnabled(true);
                                    updateChat_notify("File sent complete");
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

        private void showSaveFile() {
            while (true) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System
                        .getProperty("user.home")));
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showSaveDialog(frameChatGui);
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
                            Tags.show(frameChatGui, "Your file receive has error!!!",
                                    false);
                        }
                        break;
                    } else {
                        int resultContinue = Tags.show(frameChatGui,
                                "File is exists. You want save file?", true);
                        if (resultContinue == 0)
                            continue;
                        else
                            break;
                    }
                }
            }
        }


        //void send Message
        public synchronized void sendMessage(Object obj) throws Exception {
            outPeer = new ObjectOutputStream(connect.getOutputStream());
            // only send text
            if (obj instanceof String) {
                String message = obj.toString();
                outPeer.writeObject(message);
                outPeer.flush();
                if (isReceiveFile)
                    isReceiveFile = false;
            }
            // send attach file
            else if (obj instanceof FileData) {
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

    public void copyFileReceive(InputStream inputStr, OutputStream outputStr,
                                String path) throws IOException {
        byte[] buffer = new byte[1024];
        int lenght;
        while ((lenght = inputStr.read(buffer)) > 0) {
            outputStr.write(buffer, 0, lenght);
        }
        inputStr.close();
        outputStr.close();
        File fileTemp = new File(path);
        if (fileTemp.exists()) {
            fileTemp.delete();
        }
    }

    // send html to pane
    private void appendToPane(JTextPane tp, String msg){
        HTMLDocument doc = (HTMLDocument)tp.getDocument();
        HTMLEditorKit editorKit = (HTMLEditorKit)tp.getEditorKit();
        try {

            editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
            tp.setCaretPosition(doc.getLength());

        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public class Server {

        private int port = 50000;
        private Socket socket = null;
        private ServerSocket serverSocket = null;

        public Server(String imagePath) {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("Server started on port " + serverSocket.getLocalPort() + "...");

                socket = serverSocket.accept();
                System.out.println("Client " + socket.getRemoteSocketAddress() + " connected to server...");

                // SEND FILE
                System.out.println("Sending file to client...");
//            String projectPath = System.getProperty("user.dir");
//            String imagePath = projectPath + "/gambarServer/gambar1.jpg";

                File myFile = new File(imagePath);
                byte[] myByteArray = new byte[(int) myFile.length()];

                FileInputStream fileInputStream = new FileInputStream(myFile);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                bufferedInputStream.read(myByteArray, 0, myByteArray.length);

                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(myByteArray, 0, myByteArray.length);
                outputStream.flush();
                System.out.println("Send file success...");

                fileInputStream.close();
                bufferedInputStream.close();
                outputStream.close();
                socket.close();

            } catch (IOException e) {
                System.out.println("Error : " + e);
            }
        }
    }


    public class Client {

        private String serverName = "localhost";
        private int serverPort = 50000;
        private Socket socket = null;

//        public Client() {
//            String path_save = null;
//            while (path_save != null) {
//                JFileChooser fileChooser = new JFileChooser();
//                fileChooser.setCurrentDirectory(new File(System
//                        .getProperty("user.home")));
//                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//                int result = fileChooser.showOpenDialog();
//                if (result == JFileChooser.APPROVE_OPTION) {
//                    boolean isSendFile = true;
//                    path_save = (fileChooser.getCurrentDirectory()
//                            .getAbsolutePath());
//
//                    System.out.println(path_save);
//                    //nameFile = fileChooser.getSelectedFile().getName();
//
//                }
//            }
//            new SendFile.Client(path_save);
//        }

        public Client(String imagePath) {
            try {
                socket = new Socket(serverName, serverPort);
                System.out.println("Connected to server " + socket.getRemoteSocketAddress());

                System.out.println("Getting file from server...");
                int FILE_SIZE = 6022386;
                byte[] myByteArray = new byte[FILE_SIZE];

//            String projectPath = System.getProperty("user.dir");
//            String imagePath = projectPath + "/README.md";

                InputStream inputStream = socket.getInputStream();
                int bytesRead = inputStream.read(myByteArray, 0, myByteArray.length);
                int current = bytesRead;
                do {
                    bytesRead = inputStream.read(myByteArray, current, (myByteArray.length - current));
                    if (bytesRead >= 0) {
                        current += bytesRead;
                    }
                } while (bytesRead > -1);

                FileOutputStream fileOutputStream = new FileOutputStream(imagePath);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                bufferedOutputStream.write(myByteArray, 0, current);
                bufferedOutputStream.flush();

                System.out.println("Get file success...");

                inputStream.close();
                fileOutputStream.close();
                bufferedOutputStream.close();
                socket.close();
            } catch (IOException e) {
                System.out.println("Error : " + e.getMessage());
            }
        }
    }
}