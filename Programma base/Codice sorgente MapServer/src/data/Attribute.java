package data;

import java.io.Serializable;

/**
 * La classe modella un generico attributo discreto o continuo.
 * @author Silvio Ferrara, Federico Vassallo, Nicola Cortese
 *
 */
public abstract class Attribute implements Serializable{
	/**
	 * nome simbolico dell'attributo
	 */
	private String name;
	/**
	 * identificativo numerico dell'attributo
	 */
	private int index;
	
	
	/**
	 * costruttore di classe. Inizializza i valori dei membri name, index
	 * @param name Nome simbolico dell'attributo
	 * @param index identificativo numerico dell'attributo
	 */
	protected Attribute(String name, int index)
		{
		this.name = name;
		this.index = index;
		}

	/**
	 * Restituisce il valore nel membro name
	 * @return Nome simbolico dell'attributo (di tipo String)
	 */
	public String getName() {return this.name;}
	

	/**
	 * Restituisce il valore nel membro index;
	 * @return identificativo numerico dell'attributo
	 */
	public int getIndex() {return this.index;}
	
	public String toString() {return name;}

} 
 