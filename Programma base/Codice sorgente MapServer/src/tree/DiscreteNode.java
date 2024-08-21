package tree;
import java.io.Serializable;
import java.util.ArrayList;

import data.Attribute;
import data.Data;
import data.DiscreteAttribute;
import tree.SplitNode.SplitInfo;
/**
 * La classe DiscreteNode, estende la superclasse SplitNode, per modellare l'entità nodo di split relativo ad un attributo indipendente discreto.
 * @author Silvio Ferrara, Federico Vassallo, Nicola Cortese
 *
 */
public class DiscreteNode extends SplitNode implements Serializable{
	
	/**
	 * Istanzia un oggetto che rappresenta un nodo corrispondente ad un attributo discreto, invocando il costruttore della superclasse con il parametro attribute
	 * @param trainingSet training set complessivo
	 * @param beginExampleIndex indice estremo superiore del sotto-insieme di training
	 * @param endExampleIndex indice estremo inferiore del sotto-insieme di training
	 * @param attribute attributo indipendente sul quale si definisce lo split
	 */
	public DiscreteNode(Data trainingSet,int beginExampelIndex, int endExampleIndex, DiscreteAttribute attribute) {
		super(trainingSet, beginExampelIndex, endExampleIndex, attribute);
	}

	/**
	 * 
Comportamento (Implementazione da class abstract): istanzia oggetti SpliInfo 
(definita come inner class in Splitnode) con ciascuno dei valori discreti dell’attributo 
relativamente al sotto-insieme di training corrente 
(ossia la porzione di trainingSet compresa tra beginExampelIndex e endExampelIndex), 
quindi popola l'array c mapSplit[] con tali oggetti.
	 */
	protected void setSplitInfo(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute) {
		Object currentSplitValue = trainingSet.getExplanatoryValue(beginExampleIndex, attribute.getIndex());
		int beginSplit = beginExampleIndex;
		int child = 0;
		for (int i = beginExampleIndex + 1; i <= endExampleIndex; i++) {
			if (currentSplitValue.equals(trainingSet.getExplanatoryValue(i, attribute.getIndex())) == false) {
				mapSplit.add(new SplitInfo(currentSplitValue, beginSplit, i - 1, child));
				currentSplitValue = trainingSet.getExplanatoryValue(i, attribute.getIndex());
				beginSplit = i;
				child++;
			}
	}
		mapSplit.add(new SplitInfo(currentSplitValue, beginSplit, endExampleIndex, child));
}

	
	/**
	 * Input: valore discreto dell'attributo che si vuole testare rispetto a tutti gli split
Output: numero del ramo di split
Comportamento (Implementazione da class abstract) :effettua il confronto del valore 
in input rispetto al valore contenuto nell’attributo splitValue di ciascuno degli oggetti 
SplitInfo collezionati in mapSplit[] e restituisce l'identificativo dello split 
(indice della posizione nell’array mapSplit) con cui il test è positivo

	 */
	protected int testCondition(Object value) {
		int i;
		for (i = 0; i < mapSplit.size(); i++) {
			if (mapSplit.get(i).splitValue.equals(value))
				break;
		}
		return mapSplit.get(i).numberChild;
	}

	/**
	 *Comportamento:invoca il metodo della superclasse specializzandolo per discreti 
	 */
	public String toString() {
		return "DISCRETE " + super.toString();
	}

} 
 