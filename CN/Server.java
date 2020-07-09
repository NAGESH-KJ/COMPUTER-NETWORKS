
import java.io.*;
import java.net.*;
import java.sql.*;

public class Server
{
    private ServerSocket server;
    public Server(int portno) {
        try {
            server = new ServerSocket(portno);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void acceptRequest()
    {
    	System.out.println("Server is running...");
        Socket soc = null;
        while(true)
        {
            try 
            {
                soc = server.accept();
                Validate t = new Validate(soc);
                t.start();
            } 
            catch (IOException ex) 
            {
               ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server(1234);
        server.acceptRequest();
    }
    
    
}

class Validate extends Thread
{
    private Socket soc;
    private DataInputStream din;
    private DataOutputStream dout;
    private Connection c = null;
    Statement stmt;
    Validate(Socket soc) 
    {
        try 
        {
            this.soc = soc;
            din = new DataInputStream(soc.getInputStream());
            dout = new DataOutputStream(soc.getOutputStream());
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:user.db");
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS USERS (USERNAME STRING NOT NULL,PASSWORD STRING NOT NULL)"; 
            stmt.executeUpdate(sql);
            //stmt.close();
        } 
        catch (IOException | ClassNotFoundException | SQLException ex) 
        {
            ex.printStackTrace();
        } 
    }
    
    public void run()
    {
    	String requestedfor;
        try 
        {
        	requestedfor = din.readUTF();
        	if(requestedfor.equals("1"))
        	{
	            String[] userpass = din.readUTF().split(":");
	            ResultSet rs = stmt.executeQuery("SELECT * FROM USERS WHERE USERNAME='"+userpass[0]+"'");
	            //rs.next();
	            
	            if(rs.next() && rs.getString(2).equals(userpass[1]))
	            	dout.writeUTF("1");
	            else
	            	dout.writeUTF("0");
        	}
        	else if(requestedfor.equals("2"))
        	{
        		String[] userpass = din.readUTF().split(":");
        		String sql = "INSERT INTO USERS(USERNAME,PASSWORD) VALUES(?,?)";
        		PreparedStatement pstmt = c.prepareStatement(sql);  
                pstmt.setString(1, userpass[0]);  
                pstmt.setString(2, userpass[1]);  
                pstmt.executeUpdate();
        	}
            din.close();
            dout.close();
            soc.close();
            stmt.close();
            c.close();
        }
        catch (IOException | SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
}
    