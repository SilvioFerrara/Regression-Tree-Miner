package server;
/**
 * Eccezione sollevata dal metodo predictClass() in caso di acquisizione di
 * valore mancante o fuori range di un attributo di un nuovo esempio da
 * classificare.
 */
public class UnknownValueException extends Exception {
	private String str;
    public UnknownValueException() {
    }
    public UnknownValueException(String s) {
    	this.str=(s);
    }
    public String getMessage(){return str;}
}