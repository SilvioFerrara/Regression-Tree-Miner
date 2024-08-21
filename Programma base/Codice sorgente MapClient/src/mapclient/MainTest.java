package mapclient;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import utility.Keyboard;
//String ip="0.0.0.0";//test
//int port=new Integer(8080);

public class MainTest {

	/**
	 * Il sistema client deve collegarsi al server tramite l'indirizzo e la porta su cui il server è in ascolto.
Indirizzo del server e porta sono acquisiti come parametri tramite args. 
Una volta instaurata la connessione l'utente può scegliere se avviare un nuovo processo di scoperta di albero di regressione o recuperare un albero precedentemente serializzati in un qualche file.
	 * @param args [0]:indizizzo del server;	[1]: porta
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException{
		
		InetAddress addr;
		try {
			addr = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			System.out.println(e.toString());
			return;
		}
		Socket socket=null;
		ObjectOutputStream out=null;
		ObjectInputStream in=null;
		try {
			socket = new Socket(addr, new Integer(args[1]).intValue());
			System.out.println(socket);		
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());	; // stream con richieste del client
			
		}  catch (IOException e) {
			System.out.println(e.toString());
			return;
		}

		/*
		 * Legenda:
		 * -1: Exit
		 * 0: Learn Regression Tree from data
		 * 1: Save Regression Tree on archive
		 * 2: Load Regression Tree from archive
		 * 3: Predict
		 */
		String answer="";
		
		int decision=0;
		do{
		
			System.out.println("Learn Regression Tree from data [1]");
			System.out.println("Load Regression Tree from archive [2]");
			decision=Keyboard.readInt();
		}while(!(decision==1) && !(decision ==2));
		
		String tableName="";
		System.out.println("File name:");
		tableName=Keyboard.readString();
		try{
		
		if(decision==1)
		{
			System.out.println("Starting data acquisition phase!");
			
			
			out.writeObject(0);						
			out.writeObject(tableName);				
			answer=in.readObject().toString();
			if(!answer.equals("OK")){
				System.out.println(answer);
				return;
			}
				
			System.out.println("Starting learning phase!");
			out.writeObject(1);

		}
		else
		{
			out.writeObject(2);
			out.writeObject(tableName);
			
		}
		
		answer=in.readObject().toString();//ok OR err
		if(!answer.equals("OK")){
			System.out.println(answer);
			return;
		}
			
		
		
		// .........
		out.writeObject(4);
		System.out.println(in.readObject());//tree.printRules()
		System.out.println(in.readObject());//tree.printTree()
		
		char risp='y';
		
		do{
			out.writeObject(3);
			System.out.println("Starting prediction phase!");
			answer=in.readObject().toString();//anserw: ok OR query OR err OR valore double finale (ma non esce alla prima)
			
			
			while(answer.equals("QUERY")){
				// Formualting query, reading answer
				answer=in.readObject().toString();//possibbili test con scelta da 0 a n
				System.out.println(answer);
				int path=Keyboard.readInt();//risp da 0 a n
				out.writeObject(path);
				answer=in.readObject().toString();
			}// END WHILE
		
			if(answer.equals("OK"))
			{ // Reading prediction
				answer=in.readObject().toString();
				System.out.println("Predicted class:"+answer);
				
			}
			else { //Printing error message 
				System.out.println(answer);
				}
			
			do {	
				System.out.println("Would you repeat ? (y/n)");
				
					risp=Keyboard.readChar();
					
						if (!(Character.toUpperCase(risp)=='Y') && !(Character.toUpperCase(risp)=='N') ){
							System.out.println("Risposta errata !");
						}
				
				}while(!(Character.toUpperCase(risp)=='Y') && !(Character.toUpperCase(risp)=='N') );
					
			
			}while (Character.toUpperCase(risp)=='Y');
	
		}
		catch(IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
		 finally {
			 out.writeObject(-1);
			 socket.close(); 
		 }
		System.out.println("closing...");
	}//endmain
	
}
