package chatroom.ChatServer.server;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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

import net32.net.paulb.ChatServer.server.Server;

public class ServerGUI extends JFrame implements Runnable, WindowListener, ActionListener {
    
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextArea txtrSda;
    
    private Button sendMesssageButton;
    
    private JMenuItem menuItemQuit;
    private JMenuItem menuItemClear;
    
    private chatroom.ChatServer.server.Server server;

    
    public ServerGUI(chatroom.ChatServer.server.Server server2) {
        this.server = server2;
        
        createWindow();
        EventQueue.invokeLater(this);
    }
    
    
    public void run() {
        this.setTitle("Chat Server");
        this.setResizable(false);
        this.setVisible(true);

    }
    
    public void printLog(String string) {
        this.txtrSda.setText(this.txtrSda.getText() + System.lineSeparator() + string);
    }
    
    
    private void createWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);
        
        JMenuItem mntmRestart = new JMenuItem("Restart");
        mnFile.add(mntmRestart);
        
        menuItemQuit = new JMenuItem("Quit");
        mnFile.add(menuItemQuit);
        menuItemQuit.addActionListener(this);
        
        JMenu mnEdit = new JMenu("Edit");
        menuBar.add(mnEdit);
        
        menuItemClear = new JMenuItem("Clear Log");
        mnEdit.add(menuItemClear);
        menuItemClear.addActionListener(this);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        JLabel lblNewLabel = new JLabel("Server");
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
        sendMesssageButton.setBackground(UIManager.getColor("textHighlight"));
        panel.add(sendMesssageButton);
        sendMesssageButton.addActionListener(this);
        
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
            this.server.BroadcastMessage("[SERVER] " + this.textField.getText());
            this.server.printMessage("[SERVER] " + this.textField.getText());
            this.textField.setText("");
        }
        
        if (e.getSource() == menuItemQuit) {
            System.exit(0);
        }
        
        if (e.getSource() == menuItemClear) {
            this.txtrSda.setText("");
        }
    }
    
    @Override
    public void windowClosed(WindowEvent e) {
        this.server.stop();
        this.dispose();
        System.exit(0);
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
