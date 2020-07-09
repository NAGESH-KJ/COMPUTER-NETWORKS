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
import java.awt.event.ActionEvent;
import java.net.*;
import java.io.*;
import java.awt.Color;

public class LoginFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtUserid,txtPassword;
	private LoginFrame frame;
	private JButton btnLogin,btnExit,btnRegister;
	private JLabel showInvalid;
	private String serverip = "127.0.0.1";
	private int port = 1234;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public LoginFrame() 
	{
		getContentPane().setLayout(null);
		initComponent();
		frame = this;
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String uname = txtUserid.getText();
				String pwd = txtPassword.getText();
				int valid = checkUser(uname,pwd);
				if(valid == 1)
				{
					//System.out.println("Valid User...");
					ChatRoomFrame f = new ChatRoomFrame(txtUserid.getText());
					f.setVisible(true);
					frame.dispose();
				}
				else if(valid == -2)
					showInvalid.setText("Server is Down...");
				else
					showInvalid.setText("Invalid User...");
			}
		});
		
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		btnRegister.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				RegisterFrame r = new RegisterFrame();
				r.setVisible(true);
				frame.dispose();
			}
		});
	}
	
	
	private int checkUser(String uname,String pwd)
	{
		Socket socket;
		DataInputStream din = null;
		try 
        {
            socket = new Socket(serverip, port);
            System.out.println("Connected to server for validating...");
            din = new DataInputStream(socket.getInputStream());
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            dout.writeUTF("1");
            String userpass = uname+":"+pwd;
            dout.writeUTF(userpass);
            String res = din.readUTF();
            din.close();
            dout.close();
            socket.close();
            if(res.equals("1"))
            	return 1;
            else
            	return 0;
            
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
		
		JLabel lblChatingApp = new JLabel("CHATTING APP");
		lblChatingApp.setFont(new Font("Segoe UI Black", Font.BOLD, 21));
		lblChatingApp.setBounds(187, 38, 179, 24);
		contentPane.add(lblChatingApp);
		
		JLabel lblUsername = new JLabel("Username   :");
		lblUsername.setFont(new Font("Dialog", Font.BOLD, 15));
		lblUsername.setBounds(132, 122, 103, 24);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel(" Password  :");
		lblPassword.setFont(new Font("Dialog", Font.BOLD, 15));
		lblPassword.setBounds(132, 165, 100, 13);
		contentPane.add(lblPassword);
		
		txtUserid = new JTextField();
		txtUserid.setToolTipText("Enter the Username");
		txtUserid.setBounds(240, 125, 165, 20);
		contentPane.add(txtUserid);
		txtUserid.setColumns(10);
		
		txtPassword = new JTextField();
		txtPassword.setBounds(240, 164, 165, 20);
		txtUserid.setToolTipText("Enter the Password");
		contentPane.add(txtPassword);
		txtPassword.setColumns(10);
		
		btnLogin = new JButton("Login");
		btnLogin.setBounds(151, 226, 115, 25);
		contentPane.add(btnLogin);
		
		btnExit = new JButton("Exit");
		
		btnExit.setBounds(277, 226, 115, 25);
		contentPane.add(btnExit);
		
		showInvalid = new JLabel("");
		showInvalid.setForeground(Color.RED);
		showInvalid.setBounds(209, 72, 133, 40);
		contentPane.add(showInvalid);
		
		JLabel lblDontHaveAn = new JLabel("Don't have an account?");
		lblDontHaveAn.setBounds(209, 273, 133, 22);
		contentPane.add(lblDontHaveAn);
		
		btnRegister = new JButton("Register");
		btnRegister.setBounds(209, 305, 115, 25);
		contentPane.add(btnRegister);
	
	}
}
