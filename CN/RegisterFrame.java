import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class RegisterFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textUser,textPassword;
	private JButton btnSubmit,btnCancel;
	private JFrame thisFrame;
	private String serverip = "127.0.0.1";
	private int port = 1234;
	
	
	public void createloginFrame()
	{
		LoginFrame l = new LoginFrame();
		l.setVisible(true);
		thisFrame.dispose();
	}
	
	public RegisterFrame() 
	{
		this.thisFrame = this;
		initComponent();
		btnCancel.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				createloginFrame();
			}
		});
		
		btnSubmit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = insertUserintoDatabase();
				if(i == 1)
				{
					System.out.println("Insert Successfull...");
					createloginFrame();
				}
				else
					System.out.println("Error in Inserting the data...");
			}
		});
		
	}
	
	private int insertUserintoDatabase()
	{
		Socket socket;
		DataInputStream din = null;
		DataOutputStream dout = null;
		try 
        {
            socket = new Socket(serverip, port);
            System.out.println("Connected to server for insert...");
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
            dout.writeUTF("2");
            String userpass = textUser.getText()+":"+textPassword.getText();
            dout.writeUTF(userpass);
            din.close();
            dout.close();
            socket.close();
            return 1;
        } 
        catch (IOException ex) 
        {
        	return -2;
        }
	}
	
	public void initComponent()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100,600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setLayout(null);
		
		JLabel lblWelcomeToChatting = new JLabel("WELCOME TO CHATTING APP");
		lblWelcomeToChatting.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		lblWelcomeToChatting.setBounds(120, 40, 354, 40);
		contentPane.add(lblWelcomeToChatting);
		
		JLabel lblUsername = new JLabel("USERNAME");
		lblUsername.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		lblUsername.setBounds(120, 112, 85, 25);
		contentPane.add(lblUsername);
		
		textUser = new JTextField();
		textUser.setBounds(120, 144, 220, 35);
		contentPane.add(textUser);
		textUser.setColumns(10);
		
		JLabel lblPassword = new JLabel("PASSWORD");
		lblPassword.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		lblPassword.setBounds(120, 188, 85, 25);
		contentPane.add(lblPassword);
		
		textPassword = new JTextField();
		textPassword.setBounds(120, 223, 220, 35);
		contentPane.add(textPassword);
		textPassword.setColumns(10);
		
		btnCancel = new JButton("CANCEL");
		btnCancel.setBounds(120, 284, 85, 21);
		contentPane.add(btnCancel);
		
		btnSubmit = new JButton("SUBMIT");
		btnSubmit.setBounds(255, 284, 85, 21);
		contentPane.add(btnSubmit);
	}
}
