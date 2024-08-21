package tree;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import data.Attribute;
import data.Data;

/**Classe astratta SplitNode per modellare l'astrazione dell'entità nodo di split (continuo o discreto) estendendo la superclasse Node
 * @author Silvio Ferrara, Federico Vassallo, Nicola Cortese
 *
 */
public abstract class SplitNode extends Node implements Comparable<SplitNode>, Serializable{
	// Classe che colleziona informazioni descrittive dello split
	/**
	 * Classe che aggrega tutte le informazioni riguardanti un nodo di split 
	 * @author Silvio
	 */
	protected class SplitInfo implements Serializable{
		/**
		 * valore di tipo Object (di un attributo indipendente) che definisce uno split
		 */
		Object splitValue;
		int beginIndex;
		int endIndex;
		/**
		 * numero di split (nodi figli) originanti dal nodo corrente
		 */
		int numberChild;
		/**
		 * operatore matematico che definisce il test nel nodo corrente (“=” per valori discreti)
		 */
		String comparator="=";
		/**
		 * Costruttore che avvalora gli attributi di classe per split a valori discreti
		 * @param splitValue
		 * @param beginIndex
		 * @param endIndex
		 * @param numberChild
		 */
		SplitInfo(Object splitValue,int beginIndex,int endIndex,int numberChild){
			this.splitValue=splitValue;
			this.beginIndex=beginIndex;
			this.endIndex=endIndex;
			this.numberChild=numberChild;
		}
		/**
		 * Costruttore che avvalora gli attributi di classe per generici split (da usare per valori continui)
		 * @param splitValue
		 * @param beginIndex
		 * @param endIndex
		 * @param numberChild
		 * @param comparator
		 */
		SplitInfo(Object splitValue,int beginIndex,int endIndex,int numberChild, String comparator){
			this.splitValue=splitValue;
			this.beginIndex=beginIndex;
			this.endIndex=endIndex;
			this.numberChild=numberChild;
			this.comparator=comparator;
		}
		int getBeginindex(){
			return beginIndex;			
		}
		int getEndIndex(){
			return endIndex;
		}
		/**
		 * 
		 * @return restituisce il valore dello split
		 */
		Object getSplitValue(){
			return splitValue;
		}
		/**
		 * concatena in un oggetto String i valori di beginExampleIndex,endExampleIndex, child, splitValue, comparator e restituisce la stringa finale.
		 */
		public String toString(){
			return "child " + numberChild +" split value"+comparator+splitValue + "[Examples:"+beginIndex+"-"+endIndex+"]";
		}
		String getComparator(){
			return comparator;
		}
	
		
	}
	/**
	 * oggetto Attribute che modella l'attributo indipendente sul quale lo split è generato
	 */
	private Attribute attribute;	
	
	/**
	 * array per memorizzare gli split candidati in una struttura dati di dimensione pari ai possibili valori di test protected SplitInfo mapSplit[];//MOD VISIBILITA
	 */
	protected List<SplitInfo> mapSplit = new ArrayList<SplitInfo>();
	
	/**
	 * attributo che contiene il valore di varianza a seguuito del partizionamento indotto dallo split corrente
	 */
	protected double splitVariance;
	
	/**
	 * metodo abstract per generare le informazioni necessarie per ciascuno degli split candidati (in mapSplit[])
	 * @param trainingSet training set complessivo
	 * @param beginExampelIndex indici estremi del sotto-insieme di training
	 * @param endExampleIndex indici estremi del sotto-insieme di training
	 * @param attribute attributo indipendente sul quale si definisce lo split
	 */
	protected abstract void setSplitInfo(Data trainingSet,int beginExampelIndex, int endExampleIndex, Attribute attribute);


	/**
	 * valore dell'attributo che si vuole testare rispetto a tutti gli split
	 * @param value : metodo abstract per modellare la condizione di test (ad ogni valore di test c'è un ramo dallo split)
	 * @return
	 */
	protected abstract int testCondition (Object value);
	
	/**
* Invoca il costruttore della superclasse,
ordina i valori dell'attributo di input per gli esempi beginExampleIndex-endExampleIndex 
e sfrutta questo ordinamento per determinare i possibili split e popolare l'array mapSplit[], 
computa lo SSE (splitVariance) per l'attributo usato nello split sulla base 
del partizionamento indotto dallo split (lo stesso è la somma degli SSE calcolati 
su ciascuno SplitInfo colelzioanto in mapSplit)
	 * @param trainingSet training set complessivo
	 * @param beginExampleIndex indici estremi del sotto-insieme di training
	 * @param endExampleIndex indici estremi del sotto-insieme di training
	 * @param attribute attributo indipendente sul quale si definisce lo split
	 */
	protected SplitNode(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute){
			super(trainingSet, beginExampleIndex,endExampleIndex);
			this.attribute=attribute;
			trainingSet.sort(attribute, beginExampleIndex, endExampleIndex); // order by attribute
			setSplitInfo(trainingSet, beginExampleIndex, endExampleIndex, attribute);
						
			//compute variance
			splitVariance=0;
			for(int i=0;i<mapSplit.size();i++){
					double localVariance=new LeafNode(trainingSet, mapSplit.get(i).getBeginindex(), mapSplit.get(i).getEndIndex()).getVariance();
					splitVariance+=(localVariance);
			}
	}
	/**
	 * @return restituisce l'oggetto per l'attributo usato per lo split
	 */
	protected Attribute getAttribute(){
		return attribute;
	}
	/**
	 * Comportamento: restituisce l'information gain per lo split corrente
	 */
	protected double getVariance(){
		return splitVariance;
	}
	
	/**
	 * Comportamento: restituisce il numero dei rami originanti nel nodo corrente;
	 */
	protected int getNumberOfChildren(){
		return mapSplit.size();
	}
	
	/**
	 * Comportamento: restituisce le informazioni per il ramo in mapSplit[] indicizzato da child.
	 * @param child
	 * @return
	 */
	protected SplitInfo getSplitInfo(int child){
		return mapSplit.get(child);
	}

	/**
	 * Comportamento: concatena le informazioni di ciascuno test (attributo, operatore e valore) 
	 * in una String finale. Necessario per la predizione di nuovi esempi
	 */
	protected String formulateQuery(){
		String query = "";
		for(int i=0;i<mapSplit.size();i++)
			query+= (i + ":" + attribute + mapSplit.get(i).getComparator() +mapSplit.get(i).getSplitValue())+"\n";
		return query;
	}

	/**
	 * concatena le informazioni di ciascuno test
	 * (attributo, esempi coperti, varianza di Split) in una String finale.
	 */
	public String toString(){
		String v= "SPLIT : attribute=" +attribute +" "+ super.toString()+  " Split Variance: " + getVariance()+ "\n" ;
		
		for (int i = 0; i < mapSplit.size(); i++) {
            v += "\t" + mapSplit.get(i) + "\n";
		}
		
		return v;
	}

/**
 * Confrontare i valori di splitVariance dei due nodi e restituire l'esito
Classe RegressionTree
@param o : Nodo di split da confrontare con il corrente nodo DiscreteNode
 @return Esito del confronto (0: uguali, -1 gain minore, 1 gain maggiore)
 */
	@Override
	public int compareTo(SplitNode o) {
		if (o.getVariance() == this.splitVariance)
			return 0;
		else if (o.getVariance() < this.splitVariance)
			return -1;
		else
			return 1;
	}
}
