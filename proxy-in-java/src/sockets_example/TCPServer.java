package sockets_example;
import java.io.*; 
import java.net.*;

class TCPServer { 
	public static void main(String argv[]) throws Exception { 
		String clientSentence; 
		String capitalizedSentence;
		
		ServerSocket welcomeSocket = new ServerSocket(6789);
		InetAddress a = welcomeSocket.getInetAddress();

		System.out.println("Address: " + a.getHostAddress());
		
		while(true) {
			Socket connectionSocket = welcomeSocket.accept();

			InetAddress b = connectionSocket.getInetAddress();
			
			System.out.println("Address: " + b.getHostAddress());
			
			
			BufferedReader inFromClient  = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

			while (true) {
				clientSentence = inFromClient.readLine();

				if (clientSentence == null)
					break;
				
				if (clientSentence.equals("exit"))
					break;
				
				System.out.println(clientSentence);
			
				capitalizedSentence = clientSentence.toUpperCase() + '\n';
			    
				outToClient.writeBytes(capitalizedSentence);
			}

			connectionSocket.close();
		}
	}
}
