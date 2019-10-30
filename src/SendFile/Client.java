package SendFile;
//get file
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;
import GUI_Client.ChatGui;
import GUI_Client.ChatUI;
public class Client extends GUI_Client.ChatGui {

    private String serverName = "localhost";
    private int serverPort = 50000;
    private Socket socket = null;

    public Client() {
        String path_save = null;
        while (path_save != null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System
                    .getProperty("user.home")));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = fileChooser.showOpenDialog();
            if (result == JFileChooser.APPROVE_OPTION) {
                boolean isSendFile = true;
                path_save = (fileChooser.getCurrentDirectory()
                        .getAbsolutePath());

                System.out.println(path_save);
                //nameFile = fileChooser.getSelectedFile().getName();

            }
        }
        new Client(path_save);
    }

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

    public static void main(String[] args) throws IOException {
//        String path_save = null;
//        while (!path_save) {
//            JFileChooser fileChooser = new JFileChooser();
//            fileChooser.setCurrentDirectory(new File(System
//                    .getProperty("user.home")));
//            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//            Component frameChatGui;
//            int result = fileChooser.showOpenDialog(frameChatGui);
//            if (result == JFileChooser.APPROVE_OPTION) {
//                boolean isSendFile = true;
//                path_save = (fileChooser.getCurrentDirectory()
//                        .getAbsolutePath());
//
//                System.out.println(path_save);
//                //nameFile = fileChooser.getSelectedFile().getName();
//
//            }
//        }
//        Client client = new Client(path_save);
        Client();

    }
}