package chatroom.ChatServer.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import chatroom.ChatServer.server.ChatMessage;
import chatroom.ChatServer.server.Server;

public class ClientHandler extends Thread {

    
    private Socket socket;
    private Server server;
    
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    private String username = "";
    
    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        
        try {
            this.out = new ObjectOutputStream(this.socket.getOutputStream());
            this.in  = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            this.server.printMessage("[ERROR] Failed to create Client I/O! " + e.getMessage());
            
            try {
                this.join();
            } catch (InterruptedException e1) {
                this.server.printMessage("[ERROR] Failed to stop a client thread! "  + e1.getMessage());
            }
        }
    }
    

    public void run() {
        this.server.printMessage("[SERVER] New Thread for client made Sucesfuly ");
      
        // get the username
        try {
            this.username = (String)in.readObject();
            sendMessage(username + " Welcome to the chat room.");
        } catch (ClassNotFoundException | IOException e2) {
            e2.printStackTrace();
        }
        
        this.username = this.server.checkUsername(this.username, this);
        
        this.server.printMessage("[SERVER] " + this.username + " has connected successfully!");
        this.server.BroadcastMessage("[SERVER] " + this.username + " has Connected!", this);
        // get messages
        while (true) {
            try {
                readMessage();
            } catch (ClassNotFoundException | IOException e1) {
                this.server.printMessage("[SERVER] "+ username +" Disconnected! ");
                this.server.BroadcastMessage("[SERVER] "+ username +" Disconnected! ", this);
                end();
                this.server.printMessage("[ERROR] Failed to read input! " + e1.getMessage());
            }
            
            if (socket.isClosed()) {
                try {
                    this.server.printMessage("[SERVER] "+ username +" Disconnected! ");
                    this.server.BroadcastMessage("[SERVER] "+ username +" Disconnected! ", this);
                    this.join();
                } catch (InterruptedException e) {
                    this.server.printMessage("[ERROR] Failed to stop a client thread! "  + e.getMessage());
                }
            }
        }
    }
    
    public void end() {
        try {
            this.in.close();
            this.out.close();
            this.socket.close();
            this.join();
        } catch (Exception e) {
            this.server.printMessage("[ERROR] Could not stop thread! "+ e.getMessage());
        }
    }
    
    public boolean socketIsOpen() {
        return this.socket.isClosed();
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void readMessage() throws IOException, ClassNotFoundException {
       ChatMessage chatMessage = (ChatMessage)in.readObject();
       this.server.printMessage("["+username+"] " + chatMessage.getMessage());
       this.server.BroadcastMessage("["+username+"] " + chatMessage.getMessage(), this);
       sendMessage("[You] " + chatMessage.getMessage());
    }
    
    public void sendMessage(String message) {
        try {
            out.writeObject(message);
        } catch (Exception e) {
            this.server.printMessage("[ERROR] Failed to create Message! " + e.getMessage());
        }
    }
}
