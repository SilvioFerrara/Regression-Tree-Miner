package jfx;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.ServerException;
import java.util.Optional;

import com.google.protobuf.TextFormat;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PopupControl;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import utility.Keyboard;




 
public class Interface extends Application { 
	
	/**
	 * stream con richieste del client
	 */
	private static ObjectOutputStream out;
	
	/**
	 * stream con risposta del server
	 */
	private static ObjectInputStream in ; 
	
	/**
	 * Socket
	 */
	private static Socket socket= null;
	
	/**
	 * La main class in una applicazione JavaFX eredita dalla classe
        javafx.application.Application .
         <p>Imposta la connessione al server e avvia l'interfaccia</p>
	 * @param args <p>[0] ip</p> <p>[1] port</p>
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		//String ip="0.0.0.0";//test
		//String ip=args[0];
		
		int port=new Integer(8080);
		//int port=new Integer(args[1]).intValue();
		
		InetAddress addr;
		try {
			addr = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			//System.out.println(e.toString());
			return;
		} //ip
			//System.out.println("addr = " + addr);
			
			//Socket socket=null;
			//ObjectOutputStream out=null;
			//ObjectInputStream in=null;
			try {
				socket = new Socket(addr, new Integer(args[1]).intValue());
				//System.out.println(socket);		
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());	; // stream con richieste del client
				
			}  catch (IOException e) {
				//System.out.println(e.toString());
				return;
			} // stream con richieste del client
	
		
		launch(args);
        
    }
	
	//String answer=""; 
	

	
	/**
	 * Carica la tabella dal Database
	 * @param tabName nome della tabella nel database
	 * @throws NullPointerException
	 * @throws SocketException
	 * @throws ServerException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void learningFromDb(String name) throws NullPointerException, SocketException,ServerException,IOException,ClassNotFoundException{
		out.writeObject(0);
		
		out.writeObject(name);
		//System.out.println("Starting data acquisition phase!");
		
		String result = (String)in.readObject();
		//System.out.println(result);
		
		if(!result.equals("OK"))
		throw new ServerException(result);
		
	}
	
	/**
	 * Salva i risutati su file
	 * @throws SocketException
	 * @throws ServerException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void saveOnFile() throws SocketException,ServerException,IOException,ClassNotFoundException{
		out.writeObject(1);

		//System.out.println("Starting learning phase!");
		String result = (String)in.readObject();
		//System.out.println(result);
		
		if(!result.equals("OK") ) {
			throw new ServerException(result);
			}
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText("Save on File");
		//alert.setContentText();

		alert.showAndWait();
	}
	
	/**
	 * Acquisisce i dati contenuti nel file
	 * @throws SocketException
	 * @throws ServerException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void learningFromFile(String name) 
			throws SocketException,ServerException,IOException,ClassNotFoundException{
		out.writeObject(2);
		
		out.writeObject(name);
		//System.out.println("Starting data acquisition phase!");
		
		String result = (String)in.readObject();
		//System.out.println(result);
		if(!result.equals("OK")) 
			 throw new ServerException(result);
	}
	
	public String regressionTree() throws IOException, ClassNotFoundException {
		out.writeObject(4);
		//System.out.println("Regression Tree:");
		String result="";
		result += (String)(in.readObject());//tree.printRules()
		result +=(String)(in.readObject());//tree.printTree()
		return result;
	}
	
	private static boolean isNumeric(String str) { 
		  try {  
		    Double.parseDouble(str);  
		    return true;
		  } catch(NumberFormatException e){  
		    return false;  
		  }  
		}
	
	public void prediction() throws ClassNotFoundException, IOException {
		out.writeObject(3);
		String answer="";
		//System.out.println("Starting prediction phase!");
		answer=in.readObject().toString();//anserw: ok OR query OR err OR valore double finale (ma non esce alla prima)
		while(answer.equals("QUERY")){
			// Formualting query, reading answer
			answer=in.readObject().toString();//possibbili test con scelta da 0 a n
			//System.out.println(answer);
			
			//int path=Keyboard.readInt();//risp da 0 a n//ELIMINATO
				
				TextInputDialog dialog = new TextInputDialog("");
				dialog.setHeaderText("Prediction phase!");
				dialog.setTitle("Select number");
				dialog.setContentText(answer);
	
				Optional<String> result = dialog.showAndWait();
				
					if(!isNumeric(result.get())) {
						throw new ServerException("Result is not numeric");
					}
					
				Integer num = Integer.valueOf(result.get());
				int path=num;

			out.writeObject(path);
			answer=in.readObject().toString();
		}// END WHILE
		
		if(answer.equals("OK"))
		{ // Reading prediction
			answer=in.readObject().toString();
			//System.out.println("Predicted class:"+answer);
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("Predicted class:");
			alert.setContentText(answer);

			alert.showAndWait();
			
			
		}
		else { //Printing error message 
			//System.out.println(answer);
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Error");
			alert.setContentText(answer);

			alert.showAndWait();
			
			}
	}
	
	/**
	 * Il metodo start() è il punto d’ingresso dell' applicazione JavaFX.
	 */
	 public void start(Stage primaryStage) {
		   
		 /*In JavaFX, le applicazioni definiscono un container per l’intefaccia
		 utente mediane l’uso di un stage e una scene.
		 • JavaFX Stage è il contenitore principale
		 • JavaFX Scene è quello che mostra il contenuto dell’interfaccia	
		 • In JavaFX, il contenuto della scena è rappresentato come una
			gerarchia di nodi.  */
		 
		 primaryStage.setTitle("Regression Tree Miner");
		 //StackPane root = new StackPane();
		
		 primaryStage.setOnCloseRequest(evt -> {
			String s = "Sei sicuro di voler uscire dall'applicazione?";
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION, s, ButtonType.YES, ButtonType.NO);
			ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
			if (ButtonType.NO.equals(result)) {
			evt.consume();
			}
			else {
				try {
					out.writeObject(-1);
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
			}
		});
		 

		 //primaryStage.setScene(new Scene(root, 1500, 800));
		 //primaryStage.show();
	     
		 BorderPane borderPane = new BorderPane();
         //borderPane.setTop(new Label("Top"));
//         borderPane.setLeft(new Label("Left"));
//         borderPane.setCenter(new Label("Center"));
//         borderPane.setRight(new Label("Right"));
//         borderPane.setBottom(new Label("Bottom"));
         Scene scene = new Scene(borderPane);
         primaryStage.setScene(scene);
         primaryStage.show();
         
         
        //Parte superiore
         //Elementi
         Label label1 = new Label("File name");
	     TextField textF1 = new TextField();
	     Button btn1 = new Button();
	     btn1.setText("SEARCH");
	     Button btn2 = new Button();
	     btn2.setText("REFRESH");

	        ObservableList<String> comboItems = FXCollections.observableArrayList(
	                "Database",
	                "File"
	                );
	            ComboBox comboBox = new ComboBox(comboItems);

	     //Box Orizzontale
	     HBox hBox = new HBox();
         hBox.setSpacing(20);
         hBox.getChildren().addAll(label1,textF1,comboBox,btn1,btn2);

         borderPane.setTop(hBox);
    
         
        //Sinistra
         TextArea textA1 = new TextArea();
//	        text.setPrefWidth(50);
//	        text.setPrefHeight(100);
	        textA1.setEditable(false);
	        
         borderPane.setLeft(textA1);
	        
	    //Destra
	        HBox hBox2 = new HBox();
	        hBox2.setSpacing(20);
	        TextField textF2 = new TextField();
	        Button btn3 = new Button();
	        btn3.setText("OK");
	        hBox2.getChildren().addAll(textF2,btn3);

//	        borderPane.setRight(hBox2);
	        
	      //Centro
	        TextArea textA2 = new TextArea();
//	        text.setPrefWidth(50);
//	        text.setPrefHeight(100);
//	        textA2.setEditable(false);
	    
//         borderPane.setCenter(textA2);
	     
	        //Evento Cerca
	        EventHandler<ActionEvent> ev = new EventHandler<ActionEvent>() {
	        	public void handle(ActionEvent event) {
	        		String tableName = null;
	        		
	        		try {
	        			EventHandler<ActionEvent> canc ;
	        			tableName=textF1.getText();
	        			if (comboBox.getValue()=="Database"){
	        				learningFromDb(tableName);
							saveOnFile();
	                	}
	        			else if (comboBox.getValue()=="File") {
	                		learningFromFile(tableName);
	                	}
	                	else
	                		throw new ServerException("Errore: selezionare fonte");
	                	textA1.setText(regressionTree());// STAMPA REGRESSION TREE
	                	prediction();
	        			
					}
	                catch(IOException|ClassNotFoundException e){
							Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
							alert.show();
							return;
							
					}
	       
	        	}
	        };
	        btn1.setOnAction(ev);
	        
	        //Evento Canc
	        EventHandler<ActionEvent> canc = new EventHandler<ActionEvent>() {
	        	public void handle(ActionEvent event) {
	        		textF1.clear();
	        		textF2.clear();
	        		textA1.clear();
	        		textA2.clear();
	        	}
	        };
	        btn2.setOnAction(canc);
	        
	 }
	
}
