package chatroom.ChatServer.server;

import java.io.*;

public class ChatMessage implements Serializable {
    
    private static final long serialVersionUID = 688752890184785135L;
    
    private String message;
    
    public ChatMessage(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
}
