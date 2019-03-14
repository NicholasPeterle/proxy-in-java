package sockets_example;
import java.net.*;
	
public class NameToAddress {
	public static void main(String[] args) {

		for (String hostname : args) {
			try { 
				InetAddress a = InetAddress.getByName(hostname);
				
				System.out.println(hostname + ": " + a.getHostAddress()); 	
			} catch (UnknownHostException e) {
				System.out.println("No address found for " + hostname); 
			}
		}
	}
}
