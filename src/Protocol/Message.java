package Protocol;

import java.io.Serializable;

public class Message implements Serializable{

    private static final long serialVersionUID = 1L;

    public byte[] mess;

    public Message(int size) {
        mess = new byte[size];
    }

    public Message() {
        mess = new byte[Tags.MAX_MSG_SIZE];
    }

}
