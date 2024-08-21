package data;

import java.io.Serializable;

/**
 * Estende la classe Attribute e rappresenta un attributo continuo 
 * @author Silvio Ferrara, Federico Vassallo, Nicola Cortese
 *
 */
public class ContinuousAttribute extends Attribute implements Serializable
{

	/**
	 * rappresenta un attributo continuo
	 * @param name valori per nome simbolico dell'attributo
	 * @param index identificativo numerico dell'attributo
	 */
	protected ContinuousAttribute(String name, int index) {
		super(name, index);
		// TODO Auto-generated constructor stub
	 }
 
}
