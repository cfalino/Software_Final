package chatroom.ChatServer.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import chatroom.ChatServer.server.ClientHandler;
import chatroom.ChatServer.server.Server;
import chatroom.ChatServer.server.ServerGUI;

public class Server implements Runnable {

    private boolean usingGUI;
    private ServerGUI gui;
    
    Thread thread;
    
    private ServerSocket socket;
    private int port = 3333;
    
    private ArrayList<ClientHandler> clients;
    
    public Server(boolean usingGUI) {
        this.usingGUI = usingGUI;
        
        if (usingGUI) {
            this.gui = new ServerGUI(this);
        }
        
        clients = new ArrayList<ClientHandler>();
        
        try {
            this.socket = new ServerSocket(port);
            printMessage("[SERVER] Server started at: " + new Date() +  " on port: " + port);
            this.thread = new Thread(this, "Chat Server");
            this.thread.start();
        } catch (IOException e) {
            System.out.print("[ERROR] Server Failed to start! " + e.getMessage());
            System.exit(0);
        }
    }
    
    
    @Override
    public void run() {
        while (true) {
            try {
                Socket client = socket.accept();
                
                InetAddress inetAddress = socket.getInetAddress();
                this.printMessage("[SERVER] Connection made form: " + inetAddress.getHostAddress() + " at: " + new Date());

                ClientHandler handle = new ClientHandler(client, this);
                this.clients.add(handle);
                handle.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public synchronized String checkUsername(String username, ClientHandler client) {
        int amount = 0;
        
        String newUsername = username;
        String tmpusername =username.toUpperCase();
        
        
        for (int i = 0; i < clients.size(); i++) {
            if (!clients.get(i).equals(client)) {
                String clintUser = clients.get(i).getUsername().toUpperCase();
                
                if (clintUser.equals(tmpusername)) {
                    amount++;
                }
            }
        }
        if (amount > 0) {
            return newUsername + "(" + amount +")";
        }

        return newUsername;
    }
    
    public void stop() {
        try {
            for (int i = clients.size(); i > 0; i --) {
                clients.get(i).end();
                clients.get(i).join();
            }
            
            this.socket.close();
        } catch (Exception e) {
            printMessage("Cound not shutdown Threads! " + e.getMessage());
        }
    }
    
    public synchronized void BroadcastMessage(String message, ClientHandler sender) {
            for (int i = 0; i < clients.size(); i++) {
               if (clients.get(i) != sender) {
                   clients.get(i).sendMessage(message);
               }
            }
    }
    
    public synchronized void BroadcastMessage(String message) {
        for (int i = 0; i < clients.size(); i++) {
               clients.get(i).sendMessage(message);
        }
}
    
    public void printMessage(String message) {
        if (usingGUI) {
            this.gui.printLog(message);
        } else {
            System.out.println(message);
        }
    }
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        new Server(true);
    }
}
