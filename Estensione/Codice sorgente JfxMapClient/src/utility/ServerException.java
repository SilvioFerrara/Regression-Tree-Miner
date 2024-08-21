package utility;
/**
 * Eccezione sollevata dal sistema server e trasmessa al client dallo stream di connessione.
 *
 */
public class ServerException extends Exception{
	/**
	 * Stringa contenente il messaggio di errore
	 */
		private String str;
		/**
		 * Inizializza messaggio di errore
		 * @param result errore
		 */
		public ServerException(String result) {
			this.str=("L'output non ha prodotto risultati "+"\n"+"ERRORE : "+ result);
		}
		
		/**
		 * Stampa la stringa str 
		 */
		public String getMessage() { return str;}
		
		/**
		 * Inizializza messaggio di errore 
		 */
		public ServerException() {this.str=("L'output non ha prodotto risultati");}
}
