package chatroom.ChatClient.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import chatroom.ChatClient.client.ClientConectGUI;
import chatroom.ChatClient.client.ClientGUI;
///import chatroom.ChatClient.client.Listener;
import chatroom.ChatServer.server.ChatMessage;

public class Client implements Runnable {

    private boolean usingGUI;
    private ClientGUI gui;
    
    private boolean running;
    
    Thread thread;
    
    private Socket socket;
    private Listener listener;
    
    private String username;
    
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    public Client(boolean usingGUI, String ip, int port, String username) {
        running = true;
        this.usingGUI = usingGUI;
        this.username = username;
        
        if (usingGUI) {
            this.gui = new ClientGUI(this);
        }
        
        try {
            this.socket = new Socket(ip, port);

            this.thread = new Thread(this, "Chat Client");
            this.thread.start();
        } catch (Exception e) {
           printMessage("Could not connect to server! ");
           printMessage("Please log out and try again! ");
        }
    }
    
    @Override
    public void run() {
        printMessage("Connection accepted " + socket.getInetAddress() + ":" + socket.getPort());
        
        try {
            this.out = new ObjectOutputStream(this.socket.getOutputStream());
            this.in  = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            System.out.print("[ERROR] Failed to create Client I/O! " + e.getMessage());
        }         
        
        listener = new Listener();
        listener.start();
        
        try {
            out.writeObject(this.username);
        } catch (IOException e1) {
            printMessage("[ERROR] Failed to create Message! " + e1.getMessage());
        }
        
        try {
            while (running) {
              
            }
        } catch (Exception e) {
            printMessage("[ERROR] Failed to exit! " + e.getMessage());
        }
        stop();
    }
    

    public void logOut() {
        running = false;
        new ClientConectGUI(this, this.username);
    }
    
    public void stop() {
        try {
            running = false;
            in.close();
            out.close();
            socket.close();
            this.listener.join();
            this.thread.join();
            this.gui.dispose();
        } catch (Exception e) {
            printMessage("[ERROR] Failed to close Client I/O! " + e.getMessage());
        }
    }
    
    public void printMessage(String message) {
        if (usingGUI) {
            this.gui.printLog(message);
        } else {
            System.out.println(message);
        }
    }

    public void sendMessage(String message) throws IOException {
        ChatMessage Chatmessage = new ChatMessage(message);
        out.writeObject(Chatmessage);
    }
    
    class Listener extends Thread {
        public void run() {
            while (running) {
                try {
                    String message = (String)in.readObject();
                    printMessage(message);
                } catch (ClassNotFoundException | IOException e) {
                    printMessage("Lost Connection whith the Sever! ");
                    printMessage("Please log out and try again! ");
                    while (running) {
                        
                    }
                }
            }
            
            try {
                this.join();
            } catch (InterruptedException e) {
               printMessage("[ERROR] Failed to close Listner thread! " + e.getMessage());
            }
        }
    }

    public ClientGUI getGUI() {
        return this.gui;
    }
}
