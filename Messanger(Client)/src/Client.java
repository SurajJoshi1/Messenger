import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{

	// GUI fields 
	
	private JTextField USER_ENTER_TEXT; 
	private JTextArea CHAT_WINDOW; 
	private ObjectOutputStream output; 
	private ObjectInputStream input; 

	// Server fields 
		
	private String str = ""; 
	private String IP; 
	private Socket socket_Connection;	
	
	
	// setting up the client...
	
	public Client(String s) {
		
		super("Messager(Client)");
			
		IP = s;	
		
		USER_ENTER_TEXT = new JTextField();
		CHAT_WINDOW = new JTextArea();
		
		USER_ENTER_TEXT.addActionListener( 
				
				new ActionListener() {
					
					public void actionPerformed(ActionEvent eventA) {
						
						messageSend(eventA.getActionCommand());
						USER_ENTER_TEXT.setText("");
						
					} // actionPerfomed
					
				}); // new ActionListener 
	
		add(USER_ENTER_TEXT, BorderLayout.NORTH);
		add(new JScrollPane(CHAT_WINDOW));
		setSize(300, 150);
		setVisible(true);
		
	} // Client(String s)
	
	
	// starting the client
	
	public void run() {
		
		try {
			
			connectingToServer();
			setup();
			chatting();
			
		} catch(EOFException e) {
			
			messageShow("Connection has ended!");
			
		} catch(IOException e) {
			
			e.printStackTrace();
			
		} finally {
			
			Close();
			
		} // finally
			
	} // run
	
	
	// connecting the server 
	
	private void connectingToServer() throws IOException {
		
		messageShow("Trying to connect \n");
		
		socket_Connection = new Socket(InetAddress.getByName(IP), 1234);
		
		messageShow("Connected to Server: " + socket_Connection.getInetAddress().getHostAddress());
		
		
	} // connectingToServer
	
	
	// sending and recieving the messages
	
	private void setup() throws IOException {
		
		output = new ObjectOutputStream(socket_Connection.getOutputStream());
		output.flush();
		
		input = new ObjectInputStream(socket_Connection.getInputStream());
		
		messageShow("\n Can message now! \n");
	
	} // setup
	
	
	// chatting with the server
	
	private void chatting() throws IOException {

		do {
			
			try {
				
				str = (String) input.readObject();
				messageShow("\n" + str);
				
			} catch (ClassNotFoundException e) {
				
				messageShow("\n error! message not understood");
				
			} // catch
			
		} while(!str.equals("SERVER - END"));
		
	} // chatting
	
	
	// closing the chat
	
	private void Close() {
		
		messageShow("\nclosing chat...\n");
		
		try {
			
			output.close();
			input.close();
			socket_Connection.close();
			
			
		} catch(IOException e) {
			
			e.printStackTrace();
			
		} // catch
		
	} // Close
	
	
	// sending a message to the Server
	
	private void messageSend(String mes) {
			
		try {
				
			output.writeObject("Client - " + mes);
			output.flush();
			messageShow("\n Client - " + mes);
				
		} catch(IOException e) {
				
			CHAT_WINDOW.append("\n Error! I can cannot send the message");
				
		} // catch
			
	} // messageShow
		
	
	// updating the chat window - showing the message
		
	private void messageShow(final String mes) {
			
		SwingUtilities.invokeLater(
				
				new Runnable() {
						
					public void run() {
							
						CHAT_WINDOW.append(mes);
						
					} // run
						
				} // Runnable
					
			); // SwingUtilities.invokeLater
			
	} // messageShow	
		
} // class
