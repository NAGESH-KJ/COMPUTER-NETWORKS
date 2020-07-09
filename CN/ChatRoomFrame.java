import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.net.*;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChatRoomFrame extends JFrame implements Runnable
{

	private JPanel contentPane;
	private JButton btnSend,btnClear,btnExit;
	private TextField textmsg;
	private TextArea textArea;
	private List list;
	private String user;
	DatagramSocket dssend,dsrev;
	
	public void sendPocket(InetAddress inet,String msg)
	{
		byte[] b = msg.getBytes();
		DatagramPacket dp = new DatagramPacket(b,b.length,inet,5001);
		try 
		{
			dssend.send(dp);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void sendLoginPocket()
	{
		try 
		{
			byte[] ipaddr = new byte[] {(byte) 255,(byte) 255,(byte) 255,(byte) 255};
			InetAddress inet = InetAddress.getByAddress(ipaddr);
			String msg = "LGN:"+user+":";
			sendPocket(inet, msg);
		}
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public void initComponent()
	{
		list = new List();
		list.setBounds(10, 10, 147, 450);
		contentPane.add(list);
		
		textArea = new TextArea();
		textArea.setBounds(163, 10, 414, 287);
		textArea.setEditable(false);;
		contentPane.add(textArea);
		
		Label lblmsg = new Label("Message  :");
		lblmsg.setBounds(163, 326, 68, 21);
		contentPane.add(lblmsg);
		
		textmsg = new TextField();
		textmsg.setBounds(237, 326, 340, 21);
		contentPane.add(textmsg);
		
		btnSend = new JButton("Send");
		
		btnSend.setBounds(183, 388, 114, 25);
		contentPane.add(btnSend);
		
		btnClear = new JButton("Clear");
		
		btnClear.setBounds(309, 388, 114, 25);
		contentPane.add(btnClear);
		
		btnExit = new JButton("Exit");
		
		btnExit.setBounds(435, 388, 114, 25);
		contentPane.add(btnExit);
	}
	
	public ChatRoomFrame(String user) 
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		initComponent();
		this.user = user;
		try 
		{
			dssend = new DatagramSocket(5000);
			dsrev = new DatagramSocket(5001);
			dssend.setBroadcast(true);
			//dsrev.setBroadcast(true);
			Thread t = new Thread(this);
			t.start();
			sendLoginPocket();
		} 
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try 
				{
					byte[] ipaddr = new byte[] {(byte) 255,(byte) 255,(byte) 255,(byte) 255};
					InetAddress inet = InetAddress.getByAddress(ipaddr);
					String msg = "MSG:"+user+":"+textmsg.getText().trim()+":";
					textmsg.setText("");
					sendPocket(inet, msg);
				} 
				catch (UnknownHostException e) 
				{
					e.printStackTrace();
				}
			}
		});
		
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try 
				{
					byte[] ipaddr = new byte[] {(byte) 255,(byte) 255,(byte) 255,(byte) 255};
					InetAddress inet = InetAddress.getByAddress(ipaddr);
					String msg = "LGT:"+user+":";
					sendPocket(inet,msg);
				}
				catch (UnknownHostException e) 
				{
					e.printStackTrace();
				}
				System.exit(0);
			}
		});
		
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText("");
			}
		});
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		DatagramPacket dp = null;
		byte[] b;
		String str,cmd,sender;
		int i;
		InetAddress inet;
		while(true)
		{
			b = new byte[256];
			dp = new DatagramPacket(b,256);
			try 
			{
				dsrev.receive(dp);
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
			str = new String(b);
			str = str.trim();
			cmd = str.substring(0,3);
			i = str.indexOf(':',4);
			sender = str.substring(4,i);
			if(cmd.equals("LGN"))
			{
				list.addItem(sender);
				textArea.append(sender+" : Logged in\n");
				inet = dp.getAddress();
				str = "ACK:"+user+":";
				sendPocket(inet,str);
				
			}
			else if(cmd.equals("ACK"))
			{
				if(!sender.equals(user))
					list.addItem(sender);
			}
			else if(cmd.equals("MSG"))
			{
				str = str.substring(4);
				int m = str.lastIndexOf(':');
				str = str.substring(0,m);
				textArea.append(str+"\n");
				
			}
			else if(cmd.equals("LGT"))
			{
				for(i=0;i<list.getItemCount();i++)
				{
					if(list.getItem(i).equals(sender))
					{
						textArea.append(sender+" : Logged out\n");
						list.remove(i);
						break;
					}
				}
			}
		}
	}
}
