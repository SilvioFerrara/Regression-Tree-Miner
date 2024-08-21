package tree;
import java.io.Serializable;

import data.Data;
/**
 * Estendendo la superclasse Node definire la classe LeafNode per modellare l'entità nodo fogliare.
 * @author Silvio Ferrara, Federico Vassallo, Nicola Cortese
 *
 */
public class LeafNode extends Node implements Serializable{
	/**
	 *  valore dell'attributo di classe espresso nella foglia corrente
	 */
	private double predictedClassValue; 
	
	/**
Input: training set complessivo, 
indici estremi del sotto-insieme di training, coperto nella 
foglia
Output:
Comportamento: istanzia un oggetto invocando il costruttore 
della superclasse e avvalora l'attributo predictedClassValue 
(come media dei valori dell’attributo di classe che ricadono 
nella partizione---ossia la porzione di trainingSet compresa 
tra beginExampelIndex e endExampelIndex )
	 */
	protected LeafNode(Data trainingSet, int beginExampleIndex, int endExampleIndex) {
		super(trainingSet, beginExampleIndex, endExampleIndex);
		double media=0;
		double somma=0;

		for(int i=beginExampleIndex;i<=endExampleIndex;i++) {
			somma= somma+ trainingSet.getClassValue(i);
		}
		media=somma/(endExampleIndex-beginExampleIndex +1);

		
		predictedClassValue=media;
	
	}
	
	/**
	 * Comportamento: restituisce il valore del membro predictedClassValue
	 */
	protected double getPredictedClassValue() {
		return this.predictedClassValue;
	}
	
	/**
	 * Comportamento: restituisce il numero di split originanti dal nodo foglia, ovvero 0.
	 */
	protected int getNumberOfChildren() {
	return 0;
	}
	

	/**
	 * Comportamento:invoca il metodo della superclasse e assegnando anche il valore di classe della foglia. 
	 */
	public String toString() {
		return "LEAF class=" + predictedClassValue + " " + super.toString();
	}

}


