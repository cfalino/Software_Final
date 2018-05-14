package chatroom.ChatClient.client;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import chatroom.ChatClient.client.Client;
import chatroom.ChatClient.client.ClientConectGUI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ClientConectGUI extends JFrame implements Runnable, ActionListener{

    private static final long serialVersionUID = 1L;
    
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;

    public ClientConectGUI() {
        createWindow();
        EventQueue.invokeLater(this);
    }
    
    public ClientConectGUI(Client client, String username) {
        client.getGUI().dispose();
        createWindow();
        this.textField.setText(username);
        EventQueue.invokeLater(this);
    }
    
    @Override
    public void run() {
        this.setTitle("Chat Client");
        this.setResizable(false);
        this.setVisible(true);
    }


    public void createWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 206, 324);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblConnectToA = new JLabel("Connect to a Room");
        lblConnectToA.setBounds(51, 11, 122, 14);
        contentPane.add(lblConnectToA);
        
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBounds(51, 54, 71, 14);
        contentPane.add(lblUsername);
        
        textField = new JTextField();
        textField.setBounds(51, 71, 86, 20);
        contentPane.add(textField);
        textField.setColumns(10);
        textField .setDocument(new PlainDocument(){

            private static final long serialVersionUID = 1L;

            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if(getLength() + str.length() <= 8) {
                    super.insertString(offs, str, a);
                }
            }
        });
        
        textField_1 = new JTextField();
        textField_1.setBounds(53, 140, 86, 20);
        contentPane.add(textField_1);
        textField_1.setColumns(10);
        
        JLabel lblIpAddress = new JLabel("Ip Address");
        lblIpAddress.setBounds(51, 119, 94, 14);
        contentPane.add(lblIpAddress);
        
        textField_2 = new JTextField();
        textField_2.setBounds(51, 196, 86, 20);
        contentPane.add(textField_2);
        textField_2.setColumns(10);
        
        JLabel lblPort = new JLabel("Port");
        lblPort.setBounds(51, 171, 46, 14);
        contentPane.add(lblPort);
        
        JButton btnConnect = new JButton("Connect");
        btnConnect.addActionListener(this);
        btnConnect.setBounds(51, 252, 89, 23);
        contentPane.add(btnConnect);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String userName =  this.textField.getText().trim();
        
        if (!userName.isEmpty()) {
            new Client(true, this.textField_1.getText(), Integer.parseInt(this.textField_2.getText()),userName);
            this.dispose();
        } else {
            this.textField.setText("");
        }
    }

    public static void main(String[] args) {
        new ClientConectGUI();
    }
}
