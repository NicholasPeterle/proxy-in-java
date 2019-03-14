package sockets_example;
import java.io.*;
import java.net.*;
import java.util.*;

class WebServer {
	public static void main(String argv[]) throws Exception {
		String requestMessageLine;
		String fileName;

		ServerSocket listenSocket = new ServerSocket(6789);
		Socket connectionSocket = listenSocket.accept();

		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

		DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

		requestMessageLine = inFromClient.readLine();

		StringTokenizer tokenizedLine = new StringTokenizer(requestMessageLine);

		if (tokenizedLine.nextToken().equals("GET")) {
			fileName = tokenizedLine.nextToken();
			if (fileName.startsWith("/") == true)
				fileName = fileName.substring(1);
			try {
				File file = new File(fileName);
				int numOfBytes = (int)file.length();

				if (file.isDirectory()) {
					String[] names = file.list();
					outToClient.writeBytes("HTTP/1.0 200 Document Follows\r\n");
					outToClient.writeBytes("Content-Type: text/html\r\n\r\n");
					outToClient.writeBytes(
"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\r\n" +
"<head>\r\n" +
"<title>Linux/kernel/ - Linux Cross Reference - Free Electrons</title>\r\n" +
"</head>\r\n" +
"<body>\r\n");

					
					for (int i = 0; i < names.length; i++) {
						String line = String.format("<td><a href=\"%s\">%s</a></td>\n", names[i], names[i]);
						outToClient.writeBytes(line);
					}
					outToClient.writeBytes("</body>\r\n");
					return;
				}
				
				FileInputStream inFile = new FileInputStream(fileName);
				byte[] fileInBytes = new byte[numOfBytes];

				inFile.read(fileInBytes);
				
				outToClient.writeBytes("HTTP/1.0 200 Document Follows\r\n");

				if (fileName.endsWith(".jpg"))
					outToClient.writeBytes("Content-Type: image/jpeg\r\n");
			
				if (fileName.endsWith(".gif"))
					outToClient.writeBytes("Content-Type: image/gif\r\n");

				if (fileName.endsWith(".txt"))
					outToClient.writeBytes("Content-Type: text/plain\r\n");

				outToClient.writeBytes("Content-Length: " + numOfBytes + "\r\n");
				
				outToClient.writeBytes("\r\n");
				outToClient.write(fileInBytes, 0, numOfBytes);

				connectionSocket.close();
			}
			catch (IOException e) {
				outToClient.writeBytes("HTTP/1.1 404 File not found\r\n");
			}
			
			
		}
		else
			System.out.println("Bad Request Message");
		
	}
}
