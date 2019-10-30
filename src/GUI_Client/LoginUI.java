package GUI_Client;

import Protocol.AttachTagsMsg;
import Protocol.Tags;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import  Class.User;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import Server.*;


public class LoginUI {
    @FXML
    Button btnSignIn;

    @FXML
    TextField txtID;

    @FXML
    TextField txtPwd;

    @FXML
    TextField txtIP;

    @FXML
    TextField txtPort;

    private static Socket socketClient;

    public void onbtnLogInClick(MouseEvent e) throws Exception {

        Button button = (Button) e.getSource();
        String msg = AttachTagsMsg.processAccVerification(txtID.getText(),txtPwd.getText(),txtIP.getText(),txtPort.getText());
        System.out.println(msg);
         // Send request ID verification to server
        try
        {
            int recvport = 44000;
            //InetAddress server_ip_addr = InetAddress.get;
            socketClient = new Socket(txtIP.getText(), Integer.parseInt(txtPort.getText()));
            // Encode message (user-defined protocol)
            String message = AttachTagsMsg.processAccVerification(txtID.getText(),txtPwd.getText(), txtIP.getText(), txtPort.getText());
            // Send message to the server
            ObjectOutputStream sender = new ObjectOutputStream(socketClient.getOutputStream());
            sender.writeObject(message); sender.flush();
            // Get acknowledgment from the server
            ObjectInputStream listener = new ObjectInputStream(socketClient.getInputStream());
            message = (String) listener.readObject();
            System.out.println(message);
            // Close socket
            socketClient.close();

            if (!message.matches(Tags.ACC_DENY_TAG)) {
                User current_User = new User(txtID.getText(),txtIP.getText(),
                        txtPwd.getText(),Integer.parseInt(txtPort.getText()),1);
                Stage stage = (Stage) button.getScene().getWindow();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatUI.fxml"));/* Exception */
                Parent root = loader.load();

                //Get controller of scene2
                ChatUI inputController = loader.getController();
                //Pass whatever data you want. You can have multiple method calls here
                inputController.transferClientData(current_User, message,txtIP.getText());

                Scene scene = new Scene(root, 900, 600);
                stage.setScene(scene);
                stage.show();
            }
            else {
                AlertBox alertbox = new AlertBox();
                alertbox.display("Login Error", "Invalid username or password!");
                txtID.setText("");
                txtIP.setText("");
                txtPort.setText("");
                txtPwd.setText("");
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
                socketClient.close();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }



    }

    public void onbtnSignUpClick(MouseEvent e) throws Exception {

        Button button = (Button) e.getSource();
        String msg = AttachTagsMsg.processAccRegistration(txtID.getText(),txtPwd.getText());
        System.out.println(msg);
        // Send request ID verification to server
        try
        {
            int recvport = 44000;
            //InetAddress server_ip_addr = InetAddress.get;
            socketClient = new Socket("192.168.137.1", recvport);
            // Encode message (user-defined protocol)
            String message = AttachTagsMsg.processAccRegistration(txtID.getText(),txtPwd.getText());
            // Send message to the server
            ObjectOutputStream sender = new ObjectOutputStream(socketClient.getOutputStream());
            sender.writeObject(message); sender.flush();
            // Get acknowledgment from the server
            ObjectInputStream listener = new ObjectInputStream(socketClient.getInputStream());
            message = (String) listener.readObject();
            System.out.println(message);
            // Close socket
            socketClient.close();

            if (message.matches(Tags.REG_SUCCESS_TAG)) {
                AlertBox alertbox = new AlertBox();
                alertbox.display("Notice", "Your account has been created. Please log in.");
                txtID.setText("");
                txtIP.setText("");
                txtPort.setText("");
                txtPwd.setText("");
            }
            else {
                AlertBox alertbox = new AlertBox();
                alertbox.display("Login Error", "Error Occurred. Please try again.");
                txtID.setText("");
                txtIP.setText("");
                txtPort.setText("");
                txtPwd.setText("");
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
                socketClient.close();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }



    }

}

