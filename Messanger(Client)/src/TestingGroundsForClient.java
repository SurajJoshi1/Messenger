import javax.swing.JFrame;

public class TestingGroundsForClient {

	public static void main(String[] args) {
		
		Client client = new Client("localhost");
		
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		client.run();
				
	} // main
	
} // class
