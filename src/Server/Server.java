package Server;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Class.*;
import Protocol.*;

public class Server {
    /* Server save all of registered user data in User_Database file
     * UserID UserIP UserPwd UserPort UserStatus
     */

    private ArrayList<User> usr_lst = null;
    private ServerSocket server;
    private Socket connection;
    private ObjectOutputStream sender;
    private ObjectInputStream receiver;
    public boolean isStop = false;
    public boolean isOffline = false;

    public final static int SERVER_PORT = 44000;

    public Server(String ip, int port) throws Exception{
        server = new ServerSocket(port, 40, InetAddress.getLocalHost());
        usr_lst = new ArrayList<User>();
        (new WaitforRequest()).start();
    }

    //Waiting for connection request
    private boolean listen() throws Exception{ // return true if it's account verification or account register
        System.out.println("Connecting to client at port" + SERVER_PORT);
        connection = server.accept();
        receiver = new ObjectInputStream(connection.getInputStream());
        String clientRequest = (String) receiver.readObject();

        System.out.println("Start classify client request.");
        // Classify request from client
        // CASE 1 : Client request account verify
        User acc_info = DetachTagsMsg.getAccountInformation(clientRequest);
        System.out.println(acc_info.getUsrID());
        if (acc_info != null) {
            System.out.println("Case1");
            User acc = UserDB.searchID(acc_info.getUsrID());
            System.out.println("Test: " +acc.getUsrID());
            if (acc.getUsrID().matches("")) {
                UserDB.addRecord(acc_info);
                return true;
            }
            if (acc.getUsrPasswd().matches(acc_info.getUsrPasswd())) { // Account exist and pwd is corrected, send user list + IP
                UserDB.status_update(acc_info.getUsrID(), 1); // Update User status to online
                usr_lst = UserDB.retrieveUsrLst();
            }
            else {
                return false;
                // if acc null => add account to db and log in :)
            }
        }


        // CASE 2 : Client send an offline status
        String offID = DetachTagsMsg.getDiedAccount(clientRequest);
        if (offID != null) {
            UserDB.status_update(offID,0);
            isOffline = true;
            return false;
        }
        return true;
    }

    public class WaitforRequest extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                while (!isStop) {
                    String serverReply = "";
                    if(listen()) {
                        serverReply = AttachTagsMsg.processSendChatIP(usr_lst);
                    }
                    else {
                        if (isOffline) {
                            // Broadcast to client ???
                        }
                        else
                            serverReply = Tags.ACC_DENY_TAG;
                    }

                    //Send message to client
                    sender = new ObjectOutputStream(connection.getOutputStream());
                    sender.writeObject(serverReply);
                    sender.flush();
                    sender.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws Exception {
        Server MainServer = new Server(InetAddress.getLocalHost().getHostAddress(),SERVER_PORT);
    }
}
