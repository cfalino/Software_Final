package chatroom.ChatClient.client;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import net32.net.paulb.ChatClient.client.Client;

public class ClientGUI extends JFrame implements Runnable, WindowListener, ActionListener {
    
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextArea txtrSda;
    
    private Button sendMesssageButton;
    
    private JMenuItem menuItemQuit;
    private JMenuItem menuItemClear;
    private JMenuItem mntmRestart; 
    
    private Client client;
    
    public ClientGUI(Client client) {
        createWindow();
        this.client = client;
        EventQueue.invokeLater(this);
    }
    
    @Override
    public void run() {
        this.setTitle("Chat Client");
        this.setVisible(true);
    }
    
    public void printLog(String string) {
        this.txtrSda.setText(this.txtrSda.getText() + System.lineSeparator() + string);
    }
    
    
    private void createWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);
        
        mntmRestart = new JMenuItem("Logout");
        mntmRestart.addActionListener(this);
        mnFile.add(mntmRestart);
        
        menuItemQuit = new JMenuItem("Quit");
        menuItemQuit.addActionListener(this);
        mnFile.add(menuItemQuit);
        
        JMenu mnEdit = new JMenu("Edit");
        menuBar.add(mnEdit);
        
        menuItemClear = new JMenuItem("Clear Log");
        mnEdit.add(menuItemClear);
        menuItemClear.addActionListener(this);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        JLabel lblNewLabel = new JLabel("Sever");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblNewLabel, BorderLayout.NORTH);
        
        Panel panel = new Panel();
        contentPane.add(panel, BorderLayout.SOUTH);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        
        textField = new JTextField();
        textField.setFont(new Font("Consolas", Font.PLAIN, 12));
        panel.add(textField);
        textField.setColumns(45);
        
        sendMesssageButton = new Button("Submit");
        sendMesssageButton.addActionListener(this);
        sendMesssageButton.setBackground(UIManager.getColor("textHighlight"));
        panel.add(sendMesssageButton);
        
        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);
        
        txtrSda = new JTextArea();
        txtrSda.setTabSize(4);
        txtrSda.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtrSda.setEditable(false);
        scrollPane.setViewportView(txtrSda);
        
        DefaultCaret caret = (DefaultCaret)txtrSda.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        addWindowListener(this);
    }

    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendMesssageButton) {
            try {
                this.client.sendMessage(this.textField.getText());
            } catch (IOException e1) {
                printLog("Failed to send Message! " + e1.getMessage());
            }
            
            this.textField.setText("");
        }
        
        if (e.getSource() == menuItemQuit) {
            System.exit(0);
        }
        
        if (e.getSource() == menuItemClear) {
            this.txtrSda.setText("");
        }
        
        if (e.getSource() == mntmRestart) {
            this.client.logOut();
        }
    }



    @Override
    public void windowClosed(WindowEvent e) {
    }
    
    @Override
    public void windowClosing(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e){}
    @Override
    public void windowDeiconified(WindowEvent e){}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowOpened(WindowEvent e){}
}
