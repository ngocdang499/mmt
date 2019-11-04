package Protocol;

import javax.swing.*;

public class Tags {
    public static int MAX_MSG_SIZE = 1024;


    public static String SERVER_ONLINE = "ONLINE";
    public static String SERVER_OFFLINE = "OFFLINE";

    public static String ACC_VERIF_OPEN_TAG = "<ACC_VERIF>";
    public static String ACC_VERIF_CLOSE_TAG = "</ACC_VERIF>";
    public static String ACC_REGIS_OPEN_TAG = "<ACC_REGIS>";
    public static String ACC_REGIS_CLOSE_TAG = "</ACC_REGIS>";
    public  static String ACC_DENY_TAG = "<ACC_DENY>";
    public  static String REG_SUCCESS_TAG = "<REG_SUCCESS>";
    public static String REG_FAIL_TAG = "<REG_FAIL>";
    public static String UPDATE_PEER_LST_TAG = "<PEER_LST_UPDATE>";

    public static String USR_ID_OPEN_TAG = "<USR_ID>";
    public static String USR_ID_CLOSE_TAG = "</USR_ID>";
    public static String USR_PWD_OPEN_TAG = "<USR_PWD>";
    public static String USR_PWD_CLOSE_TAG = "</USR_PWD>";
    public static String USR_IP_OPEN_TAG = "<USR_IP>";
    public static String USR_IP_CLOSE_TAG = "</USR_IP>";
    public static String USR_PORT_OPEN_TAG = "<USR_PORT>";
    public static String USR_PORT_CLOSE_TAG = "</USR_PORT>";
    public static String USR_STAT_OPEN_TAG = "<USR_STAT>";
    public static String USR_STAT_CLOSE_TAG = "</USR_STAT>";
    public static String SESSION_ON_OPEN_TAG = "<SESSION_ON>";
    public static String SESSION_ON_CLOSE_TAG = "</SESSION_ON>";
    public static String SESSION_OFF_OPEN_TAG = "<SESSION_OFF>";
    public static String SESSION_OFF_CLOSE_TAG = "</SESSION_OFF>";
    public static String CHAT_REQ_OPEN_TAG = "<CHAT_REQ>";
    public static String CHAT_REQ_CLOSE_TAG = "</CHAT_REQ>";
    public static String CHAT_IP_OPEN_TAG = "<CHAT_IP>";
    public static String CHAT_IP_CLOSE_TAG = "</CHAT_IP>";
    public static String CHAT_MSG_OPEN_TAG = "<CHAT>";
    public static String CHAT_MSG_CLOSE_TAG = "</CHAT>";
    public static String TEXT_MSG_OPEN_TAG = "<TXT_MSG>";
    public static String TEXT_MSG_CLOSE_TAG = "</TXT_MSG>";
    public static  String FILE_MSG_OPEN_TAG = "<FILE_MSG>";
    public static  String FILE_MSG_CLOSE_TAG = "</FILE_MSG>";
    public static String CHAT_ACCEPT_TAG = "<CHAT_ACPT>";
    public static String CHAT_DENY_TAG = "CHAT_DENY>";
    public static String FILE_ACCEPT_TAG = "FILE_ACCEPT";
    public static String FILE_REFUSE_TAG = "FILE_REFUSE";






    public static String CHAT_END_TAG = "<CHAT_END>";

    public static String FILE_REQ_OPEN_TAG = "<FILE_REQ>";
    public static String FILE_REQ_CLOSE_TAG = "</FILE_REQ>";
    public static String FILE_REQ_NOACK_TAG = "<FILE_REQ_NOACK>";
    public static String FILE_REQ_ACK_OPEN_TAG = "<FILE_REQ_ACK>";
    public static String FILE_REQ_ACK_CLOSE_TAG = "</FILE_REQ_ACK>";
    public static String FILE_DATA_BEGIN_TAG = "<FILE_DATA_BEGIN>";
    public static String FILE_DATA_END_TAG = "<FILE_DATA_END>";
    public static String FILE_DATA_OPEN_TAG = "<FILE_DATA>";
    public static String FILE_DATA_CLOSE_TAG = "</FILE_DATA>";

    public static int show(JFrame frame, String msg, boolean type) {
        if (type)
            return JOptionPane.showConfirmDialog(frame, msg, null, JOptionPane.YES_NO_OPTION);
        JOptionPane.showMessageDialog(frame, msg);
        return 0;
    }
}

