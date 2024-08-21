package database;
/**
 * eccezione propagata dal Database nel caso sia fallita la connessione
 */
public class DatabaseConnectionException extends Exception {
	/**
	 * Stringa contenente il messaggio di errore
	 */
	private String str;
	/**
	 * Crea un messaggio di errore per connessione al DB fallita
	 */
	public DatabaseConnectionException() {
		this.str=("Connection to DB failed!");
	}
	/**
	 * Restituisce la stringa str 
	 */
	public String message(){return str;}
	
	public DatabaseConnectionException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
		
	}


}
