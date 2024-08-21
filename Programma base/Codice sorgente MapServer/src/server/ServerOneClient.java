package server;

import java.net.*;
import data.Data;
import data.TrainingDataException;
import tree.RegressionTree;


import java.io.*;

/**
 * la classe ServerOneClient estende la classe Thread.
 */
class ServerOneClient extends Thread {
private Socket socket;
/**
 * stream con richieste del client
 */
public static ObjectInputStream in ; 

/**
 * stream con risposta del server
 */
public static ObjectOutputStream out;


/**
 * Costruttore di classe. Inizializza gli attributi socket, in e out. Avvia il thread tramite il metodo start().</p>
 * @param s socket
 * @throws IOException
 */
public ServerOneClient (Socket s) throws IOException {
	socket = s;
	
	in = new ObjectInputStream(socket.getInputStream());
	out = new ObjectOutputStream(socket.getOutputStream());
	// se una qualsiasi delle chiamate precedenti solleva una
	// eccezione, il processo chiamante è responsabile della 
	// chiusura del socket. Altrimenti lo chiuderà il thread 
	start(); // Chiama run()
}

/**
Riscrive il metodo run della superclasse Thread al fine di gestire le richieste del client. 
*/
public void run()  {

	try {
	String table="";
	Data trainingSet=null;			
	RegressionTree tree=null;		
	
	
	int menuAnswer = 0;
	while(menuAnswer!=-1) {
		menuAnswer=(int) in.readObject();
		
			switch(menuAnswer)
			{
			
			case 0://Learn Regression Tree from data
			System.out.println("caso 0");
				table =(String)in.readObject();
				
				try {
					if (table!=null)	{
						trainingSet= new Data(table);
					}
					
				}catch(TrainingDataException e)  {
					out.writeObject(e.getMessage());
					e.printStackTrace();
					break;
				
				}catch(Exception e)  {
					out.writeObject(e.getMessage());
					e.printStackTrace();
					break;
				}
				out.writeObject((String)"OK");
				System.out.println("fine caso 0");
			break; 
			
			case 1://Save Regression Tree on archive
				System.out.println("caso 1");
				tree=new RegressionTree(trainingSet);
				try {
					tree.salva(table+".dmp");
				} catch (IOException e) {
					
					out.writeObject(e.toString());
					e.printStackTrace();
					break;
				}
				out.writeObject((String)"OK");
				System.out.println("fine caso 1");
			break; 
			
			
			case 2://Load Regression Tree from archive
				System.out.println("caso 2");
				table =(String)in.readObject();
				
				try {
					tree=RegressionTree.carica(table+".dmp");
				} catch (ClassNotFoundException | IOException e) {
					out.writeObject(e.getMessage());
					e.printStackTrace();
					break;
				}
				out.writeObject((String)"OK");
				System.out.println("fine caso 2");
			break;	
			
			case 3:
	
				Double anserw;
				try {
					anserw= tree.predictClass(in,out);//leafNode
					out.writeObject("OK");
					out.writeObject(anserw);
	
				} catch (UnknownValueException e) {
					out.writeObject(e.getMessage());
					e.printStackTrace();
					break; 
				}
			break;	
			
			case 4:
				out.writeObject(tree.printRules());
				out.writeObject(tree.printTree());
				
			}//end switch
		}//end while
	}//end try
	catch(IOException | ClassNotFoundException e) {
		//System.err.println("IO Exception");
		e.printStackTrace();
	}finally 
		{
		try {
			System.out.println("closing ServerOneClient...");
			socket.close();
			}catch(IOException e) {
			e.printStackTrace();
			System.err.println("Socket not closed");	
			}
		}
	}//end run
}