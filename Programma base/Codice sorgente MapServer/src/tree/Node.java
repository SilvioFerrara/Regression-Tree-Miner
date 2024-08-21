package tree;
import java.io.Serializable;

import data.Data;
/**
 * Definire una classe astratta Node per modellare l'astrazione dell'entità nodo (fogliare o intermedio) dell'albero di decisione
 * @author Silvio Ferrara, Federico Vassallo, Nicola Cortese
 *
 */
public abstract class Node implements Serializable{
	/** 
	 * contatore dei nodi generati nell'albero
	 */
	private static int idNodeCount=0;
	
	/**
	 *  identificativo numerico del nodo
	 */
	private int idNode;
	
	/**
	 *  indice nell'array del training set del primo esempio coperto dal nodo corrente
	 */
	private int beginExampleIndex;
	
	/**
	 * indice nell'array del training set dell'ultimo 
	esempio coperto dal nodo corrente. beginExampleIndex e endExampleIndex individuano 
	un sotto-insieme di training.
	 */
	private int endExampleIndex;
	
	/**
	 * valore dello SSE calcolato, rispetto all'attributo di classe, nel sotto-insieme di training del nodo
	 */
	private double variance;
	
	/**
	 * Avvalora gli attributi primitivi di classe, inclusa la varianza che viene calcolata rispetto all'attributo da predire nel sotto-insieme di training coperto dal nodo
	 <p>Input: oggetto di classe Data contenente il training set completo, i due indici estremi che identificano il sotto-insieme di training coperto dal nodo corrente
	 * @param trainingSet
	 * @param beginExampleIndex
	 * @param endExampleIndex
	 */
	protected Node(Data trainingSet, int beginExampleIndex, int endExampleIndex){
		
		//idNode legato a idndCount, ogni volta che trovo un costruttore di node, idnode verra inizializzato a idnodeCount++
		
		this.idNode=this.idNodeCount++;
		this.beginExampleIndex=beginExampleIndex;
		this.endExampleIndex=endExampleIndex;
		this.variance=calcoloSSE(trainingSet);
	}
	
	/**
	 * Calcola SSE: Errore Quadratico Medio
	 * determinara quale attributo permette di costruire la migliore funzione di regressione
	 * @param trainingSet esempi di training da analizzare
	 * @return misura della variabilità contenuta nel trainingSet
	 */
	protected double calcoloSSE(Data trainingSet) {

		int numeroEsempi= this.endExampleIndex-this.beginExampleIndex +1;
		double somma0=sommatoria_alla_2a(trainingSet);
		double somma1=sommatoria(trainingSet);
		double somma2=Math.pow(somma1,2);
		double somma3=somma2/numeroEsempi;
		
		double sse=somma0-somma3;
		return sse;
	}
	
	/**
	 * calcola quadrato della somma per il calcolo SSE
	 * @param trainingSet
	 * @return quadrato della somma
	 */
	private double sommatoria_alla_2a(Data trainingSet) {
		double somma = 0;
		for(int c=this.beginExampleIndex; c<=this.endExampleIndex; c++) {
			somma +=Math.pow(trainingSet.getClassValue(c),2);
		}
		return somma;
	}
	
	/**
	 * calcola somma per il calcolo SSE
	 * @param trainingSet
	 * @return somma
	 */
	private double sommatoria(Data trainingSet) {
		
		double somma = 0;
		for(int c=this.beginExampleIndex; c<=this.endExampleIndex; c++) {
			somma +=trainingSet.getClassValue(c);
		}
		return somma;
	}
		
	/**
	 * Restituisce il valore del membro idNode
	 * @return identificativo numerico del nodo
	 */
	protected int getIdNode(){
		return this.idNode;
		}
	
	/**
	 * Restituisce il valore del membro beginExampleIndex 
	 * @return indice del primo esempio del sotto-insieme rispetto al training set complessivo
	 */
	protected int getBeginExampleIndex() {
		return this.beginExampleIndex;
	}
	/**
	 * Restituisce il valore del membro endExampleIndex
	 * @return indice del ultimo esempio del sotto-insieme rispetto al training set complessivo
	 */
	protected int getEndExampleIndex() {
		return this.endExampleIndex;
	}
	
	/**
	 * Restituisce il valore del membro variance
	 * @return valore dello SSE dell’attributo da predire rispetto al nodo corrente
	 */
	protected double getVariance() {
		return this.variance;
	}
	
	/**
	 * E' un metodo astratto la cui implementazione riguarda i nodi di tipo test (split node) dai quali si possono generare figli, uno per ogni split prodotto. Restituisce il numero di tali nodi figli.
	 * @return valore del numero di nodi sottostanti
	 */
	protected abstract int getNumberOfChildren();
	
	/**
	 *  Concatena in un oggetto String i valori di beginExampleIndex,endExampleIndex, variance e restituisce la stringa finale.
	 */
	public String toString() {
		
		return "Nodo : [Examples:" + beginExampleIndex + "-" + endExampleIndex + "]" + " variance:" + variance;
	
	}
	
}
