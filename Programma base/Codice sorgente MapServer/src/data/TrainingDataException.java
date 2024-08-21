package data;

/**
 * Definisce una eccezione TrainingDataException per gestire il caso di acquisizione errata 
 * del Training set (file inesistente, schema mancante, training set vuoto o training set 
 * privo di variabile target numerica). 
 * @author Silvio Ferrara, Federico Vassallo, Nicola Cortese
 *
 */
public class TrainingDataException extends Exception{
	/**
	 * Stringa contente il messaggio di errore
	 */
	private String str;
	/**
	 * Inizializza messaggio di errore
	 */
	public TrainingDataException() {}
	/**
	 * Inizializza messaggio di errore
	 */
	public TrainingDataException(String s) {
		this.str=(s);
	}
	/**
	 * Stampa la stringa str 
	 */
	public String getMessage(){return str;}
}
