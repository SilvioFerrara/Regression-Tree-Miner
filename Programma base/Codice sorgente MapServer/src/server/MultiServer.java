package server;


import java.io.*; 
import java.net.*;

/**
 *  Server che usa il multithreading 
 */
public class MultiServer {
/**
 * La porta su cui il Server accetta le richieste dal client
 */
static final int PORT = 8080;

/**
 * <p>istanzia un oggetto di tipo MultiServer</p>
 * <p>Istanzia un oggetto istanza della classe ServerSocket che pone in attesa di richiesta di connessioni da parte del client. Ad ogni nuova richiesta connessione si istanzia ServerOneClient.</p>
 * @param args 
 * @throws IOException
 */
	public static void main(String[] args) throws IOException {
		
	ServerSocket s = new ServerSocket(PORT);
	System.out.println("Server Started");
	try {
			while(true) {
			// Rimane in ascolto finchè non si verifica una connessione:
			Socket socket = s.accept();
				
				try {
					System.out.println(
							"Connessione accettata: "+ socket);	
					
				new ServerOneClient(socket);
				
					} catch(IOException e) {
				// Se fallisce chiude il socket,
				// altrimenti il thread la chiuderà:
				System.out.println("ERRORE "+ e +"Server Closed");
				socket.close();
				}
			} 
		} finally {
		System.out.println("Server Closed");
		s.close();
		}
	}
}




