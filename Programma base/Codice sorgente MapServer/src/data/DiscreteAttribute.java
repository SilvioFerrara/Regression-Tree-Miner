package data;
import java.io.Serializable;
//rivedere java dock
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
/**
 * Estende la classe Attribute e rappresenta un attributo discreto
 * @author Silvio Ferrara, Federico Vassallo, Nicola Cortese
 *
 */
public class DiscreteAttribute extends Attribute implements Iterable<String>, Serializable{
/**
 * Contenitore di oggetti String, uno per ciascun valore discreto che l'attributo può assumere.
 */
	private Set<String> values=new TreeSet<>(); // order by asc
	/**
	 * Invoca il costruttore della super-classe e avvalora il contenitore values di tipo TreeSet, con i valori discreti in input.
	 * @param name valori per nome simbolico dell'attributo
	 * @param index identificativo numerico dell'attributo 
	 * @param values valori discreti
	 */
	protected DiscreteAttribute(String name, int index, Set<String> values) {
		super(name,index);
		this.values=values;
	}
	
	/**
	 * Restituisce la cardinalità dell'array values[]
	 * @return numero di valori discreti dell'attributo
	 */
	public int getNumberOfDistinctValues(){
		return values.size();
	}

	@Override
	public Iterator<String> iterator() {
		// TODO Auto-generated method stub
		return values.iterator();
	}
}
