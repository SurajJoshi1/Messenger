import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;

public class Server extends JFrame{

	// GUI fields 
	
	private JTextField USER_ENTER_TEXT; 
	private JTextArea CHAT_WINDOW; 
	private ObjectOutputStream output; 
	private ObjectInputStream input; 

	// Server fields 
	
	private ServerSocket ss; 
	private Socket socket_Connection; 
	
	
	// Setting up the Server's GUI....
	
	public Server() {
		
		super("Messager");
		
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
				
	} // Server


	// Running the Server's GUI
	
	public void run() {
		
		try {
			
			ss = new ServerSocket(1234, 100);
			
			while(true) {
				
				try {
					
					connectionWait();
					streamStart();
					chatting();
					
				} catch(EOFException e) {
					
					messageShow("Connection has ended!");
					
				} finally {
					
					Close();
					
				} // finally
				
			} // while
			
		} catch(IOException e) {
			
			e.printStackTrace();
			
		} // catch
		
	} // run
	

	//waiting for the connection.... if true... display info
	
	private void connectionWait() throws IOException {
		
		messageShow("waiting for other user...\n");
		
		socket_Connection = ss.accept();
		
		messageShow("Connected to " + socket_Connection.getInetAddress().getHostName());
		
		
	} // connectionWait
	
	
	// sending and recieving the messages
	
	private void streamStart() throws IOException {
		
		output = new ObjectOutputStream(socket_Connection.getOutputStream());
		
		output.flush();
		
		input = new ObjectInputStream(socket_Connection.getInputStream());
		
		messageShow("\n now both are fully connected \n");
		
	} // streamStart
	
	
	// chatting...
	
	private void chatting() throws IOException {
		
		String str = "connected to chat";
		
		messageSend(str);
		
		do {
			
			try {
				
				str = (String) input.readObject();
				messageShow("\n" + str);
				
			} catch (ClassNotFoundException e) {
				
				messageShow("\n error! message not understood");
				
			} // catch
			
		} while (!str.equals("CLINET - END"));
		
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
	
	
	// sending a message to the client
	
	private void messageSend(String str) {
		
		try {
			
			output.writeObject("SERVER - " + str);
			output.flush();
			messageShow("\n SERVER - " + str);
			
		} catch(IOException e) {
			
			CHAT_WINDOW.append("\n Error! I can cannot send the message");
			
		} // catch
		
	} // messageShow
	
	
	// updating the chat window - showing the message
	
	private void messageShow(final String str) {
		
		SwingUtilities.invokeLater(
			
				new Runnable() {
					
					public void run() {
						
						CHAT_WINDOW.append(str);
						
					} // run
					
				} // Runnable
				
			); // SwingUtilities.invokeLater
		
	} // messageShow
		
} // class
