import java.io.*; 
import java.net.*;
import java.util.Scanner;
import java.util.*;

public class Proxy{
	public static void main(String[] args) throws Exception {
		int porta = 0, tamanhoCache = 0;
		for (int i = 0; i < args.length; i++) {
			porta = Integer.parseInt(args[0]);
			tamanhoCache = Integer.parseInt(args[1]);
			
			//System.out.println("Porta em que o Proxy está escutando: "+ porta);
			//System.out.println("O tamanho da cache é: " + tamanhoCache);
		}
		//criacao da hash com o tamanho passado para a cache
		Hashtable<String, String> cache = new Hashtable<String, String>(tamanhoCache); 

		System.out.println("Porta em que o Proxy esta escutando: "+ porta);
		System.out.println("Quantidade de elementos na cache: " + cache.size());

		//construtor onde passamos a porta que desejamos usar para escutar as conexões
		ServerSocket urlAbsoluta = new ServerSocket(porta);

		//***receber requisicao do navegador***

		//getInetAddress(): resolve o nome do host
		InetAddress a = urlAbsoluta.getInetAddress();
		//getHostAddress() Retorna o endereço de IP, no qual, o socket está conectado
		System.out.println("Address socket is connect: " + a.getHostAddress());

		//***************while(connectionSocket.close())//enquanto a conexao for TRUE
				
		String clientSentence;

		String capitalizedSentence;
		while(true) {
			Socket connectionSocket = urlAbsoluta.accept();

			InetAddress b = connectionSocket.getInetAddress();
			
			System.out.println("Address: " + b.getHostAddress());
			
			//lê do cliente através do getInputStream()
			BufferedReader inFromClient  = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			// o getOutputStream() envia dados para o outro lado da comunicação, neste caso para o cliente 
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

			String urlToCall="";
			int count = 0;
			while (true) {
				clientSentence = inFromClient.readLine();
				//System.out.println("Recebido do browser: "+ clientSentence);

				//processamento da string recebida do browser(inFromClient)
				if (count ==0) {
					String[] inFromBrowser = clientSentence.split(" ");
					urlToCall = inFromBrowser[1];
					System.out.println("Requisição feita para: "+ urlToCall);
				}
				count++;
				if (urlToCall == null)
					break;
				
				else if (urlToCall.equals("exit"))
					break;
				
				//************procesamento da cache**********

				else{
					//verifica host está na cache?
					if (cache.containsKey(urlToCall)==true){
						//retornar para browser
						//InetAddress ipHost = InetAddress.getByName(urlToCall);
						//System.out.println(urlToCall + ": " + ipHost);
						cache.get(urlToCall);
						System.out.println("Esta na cache");
					}
					//o host nao esta na cache:
					else{
						BufferedReader rd = null;
						String recebidoServidor ="";
						try {
							//Abri requisição com servidor
							URL url = new URL(urlToCall);
							URLConnection conectionWithServer = url.openConnection();
							conectionWithServer.setDoInput(true);
							conectionWithServer.setDoInput(false);

							//Obtendo resposta do servidor
							InputStream is = null;
							HttpURLConnection httpConnection = (HttpURLConnection)conectionWithServer;
							if (conectionWithServer.getContentLength()>0) {
								try{
									is = conectionWithServer.getInputStream();
									rd = new BufferedReader(new InputStreamReader(is));
									recebidoServidor = rd.readLine();

								} catch (IOException ioe) {
			                        System.out.println("********* IO EXCEPTION **********: " + ioe);
									}
							}
							//armazenando na cache
							cache.put(urlToCall, recebidoServidor);
							System.out.println("TAMANHO DA CACHE: " + cache.size());

						} catch (UnknownHostException e) {
							System.out.println("No address found for " + urlToCall); 
						}
					}
					capitalizedSentence = urlToCall.toUpperCase() + '\n';
					outToClient.writeBytes(capitalizedSentence);
					break;
				}
			}
			connectionSocket.close();
		}

	}	
}