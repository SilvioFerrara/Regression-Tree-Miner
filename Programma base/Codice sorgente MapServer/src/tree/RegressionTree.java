package tree;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.TreeSet;
import data.Attribute;
import data.ContinuousAttribute;
import data.Data;
import data.DiscreteAttribute;
import server.UnknownValueException;

/**
 * la classe RegressionTree modella l'entità l'intero albero di decisione come insieme di sotto-alberi
 * @author Silvio Ferrara, Federico Vassallo, Nicola Cortese
 *
 */
public class RegressionTree implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  radice del sotto-albero corrente
	 */
	private Node root; 
	
	/**
	 *  array di sotto-alberi originanti nel nodo root:vi è un elemento nell’array per ogni figlio del nodo
	 */
	private RegressionTree childTree[];
			
/**
Comportamento: istanzia un sotto-albero dell'intero albero
 */
	public RegressionTree() {
		
	}

	/**
Comportamento: istanzia un sotto-albero dell'intero albero 
e avvia l'induzione dell'albero dagli esempi di training in input
	 * @param trainingSet training set complessivo
	 */
	public RegressionTree(Data trainingSet){
			learnTree(trainingSet,0,trainingSet.getNumberOfExamples()-1,trainingSet.getNumberOfExamples()*10/100);
	}
		/**
		 * Comportamento: genera un sotto-albero con il sotto-insieme di input istanziando un nodo fogliare (isLeaf()) o un nodo di split. In tal caso determina il miglior nodo rispetto al sotto-insieme di input (determineBestSplitNode()), ed a tale nodo esso associa un sotto-albero avente radice il nodo medesimo (root) e avente un numero di rami pari il numero dei figli determinati dallo split (childTree[]). Ricorsivamente ogni oggetto DecisionTree in childTree[] sarà re-invocato il metodo learnTree() per l'apprendimento su un insieme ridotto del sotto-insieme attuale (begin... end). Nella condizione in cui il nodo di split non origina figli, il nodo diventa fogliare.
		 * @param trainingSet training set complessivo
		 * @param begin indici estremi del sotto-insieme di training
		 * @param end indici estremi del sotto-insieme di training
		 * @param numberOfExamplesPerLeaf numero max che una foglia deve contenere
		 */
		public void learnTree(Data trainingSet,int begin, int end,int numberOfExamplesPerLeaf){
			if( isLeaf(trainingSet, begin, end, numberOfExamplesPerLeaf)){
				//determina la classe che compare più frequentemente nella partizione corrente
				root=new LeafNode(trainingSet,begin,end);
			}
			else //split node
			{
				root=determineBestSplitNode(trainingSet, begin, end);
			
				if(root.getNumberOfChildren()>1){
					childTree=new RegressionTree[root.getNumberOfChildren()];
					for(int i=0;i<root.getNumberOfChildren();i++){
						childTree[i]=new RegressionTree();
						childTree[i].learnTree(trainingSet, ((SplitNode)root).getSplitInfo(i).beginIndex, ((SplitNode)root).getSplitInfo(i).endIndex, numberOfExamplesPerLeaf);
					}
				}
				else
					root=new LeafNode(trainingSet,begin,end);
				
			}
		}
			
		/**
		 * verifica se il sotto-insieme corrente può essere coperto da un nodo 
foglia controllando che il numero di esempi del training set compresi tra begin ed 
end sia minore uguale di numberOfExamplesPerLeaf.

N.B. isLeaf() è chiamato da learnTree() che è chiamato dal costruttore di RegresioinTree 
dove numberOfExamplesPerLeaf è fissato al 10% della dimensione del training set
		 * @param trainingSet training set complessivo
		 * @param begin indici estremi del sotto-insieme di training
		 * @param end indici estremi del sotto-insieme di training
		 * @param numberOfExamplesPerLeaf numero minimo che una foglia deve contenere
		 * @return esito sulle condizioni per i nodi fogliari
		 */
		public boolean isLeaf(Data trainingSet,int begin, int end,int numberOfExamplesPerLeaf) {
			int size = end - begin;
			if (size > numberOfExamplesPerLeaf)
				return false;
			else
				return true;
		}
	

		/**
		 * Per ciascun attributo indipendente istanzia il DiscreteNode associato 
		e seleziona il nodo di split con minore varianza tra i DiscreteNode istanziati. 
		Ordina la porzione di trainingSet corrente (tra begin ed end) rispetto 
		all’attributo indipendente del nodo di split selezionato. Restituisce il nodo selezionato.
		 * @param trainingSet training set complessivo
		 * @param begin indici estremi del sotto-insieme di training
		 * @param end indici estremi del sotto-insieme di training
		 * @return nodo di split migliore per il sotto-insieme di training
		 */
		public SplitNode determineBestSplitNode(Data trainingSet,int begin,int end) {
			TreeSet<SplitNode> ts = new TreeSet<SplitNode>();
			SplitNode best = null;
			for (int i = 0; i < trainingSet.getNumberOfExplanatoryAttributes(); i++) {
				SplitNode current = null;
				Attribute a = trainingSet.getExplanatoryAttribute(i);
				if (a instanceof DiscreteAttribute)
					current = new DiscreteNode(trainingSet, begin, end, (DiscreteAttribute) a);
				else if (a instanceof ContinuousAttribute)
					current = new ContinuousNode(trainingSet, begin, end, (ContinuousAttribute) a);
				
				best = current;
				
				ts.add(current);
			}
			trainingSet.sort(ts.last().getAttribute(), begin, end);
			return (SplitNode) ts.last();
		}
		
		
		/**
		 * @return oggetto String con le informazioni dell'intero albero (compresa una intestazione
		 */
		public String printTree(){
			String s =new String();
			s+=("********* TREE **********\n");
			s+=(toString());
			s+=("*************************\n");
			return s;
		}
		
		/**
		 * Input: //
Output: oggetto String con le informazioni dell'intero albero
Comportamento: Concatena in una String tutte le informazioni di root-childTree[] correnti invocando i relativi metodo toString(): nel caso il root corrente è di split vengono concatenate anche le informazioni dei rami. Fare uso di per riconoscere se root è SplitNode o LeafNode.
		 */
		public String toString(){
			String tree=root.toString()+"\n";
			
			if( root instanceof LeafNode){
			
			}
			else //split node
			{
				for(int i=0;i<childTree.length;i++)
					tree +=childTree[i];
			}
			return tree;
		}
	
		
		/**
		 * * Comportamento: Scandisce ciascun ramo dell'albero completo dalla radice alla 
		foglia concatenando le informazioni dei nodi di split fino al nodo foglia. 
		In particolare per ogni sotto-albero (oggetto DecisionTree) in childTree[] 
		concatena le informazioni del nodo root: se è di split discende 
		ricorsivamente l'albero per ottenere le informazioni del nodo 
		sottostante (necessario per ricostruire le condizioni in AND) 
		di ogni ramo-regola, se è di foglia (leaf) termina l'attraversamento 
		visualizzando la regola.
		 * @return
		 */
		public String printRules() {
			String stringa = new String();
			String rules = new String();
			
			stringa+=("****** RULES ******\n");
			
			if (root instanceof LeafNode){
			
			stringa+=("==> Class= " + ((LeafNode) root).getPredictedClassValue() + "\n");
			
			} else if (root instanceof DiscreteNode){
			for (int i = 0; i < ((SplitNode) root).mapSplit.size(); i++) {
			rules += ((SplitNode) root).getAttribute().getName() + "="+ ((SplitNode) root).mapSplit.get(i).getSplitValue() + " ";

			stringa+=childTree[i].printRules(rules); // entra nella funzione e poi riprende nel for
			
			rules = "";
			stringa+=rules;//!
			}
			} else if (root instanceof ContinuousNode) {
			for (int i = 0; i < ((SplitNode) root).mapSplit.size(); i++) {
			rules += ((SplitNode) root).getAttribute().getName()+ ((SplitNode) root).mapSplit.get(i).getComparator()+ ((SplitNode) root).mapSplit.get(i).getSplitValue() +" ";
			 
			stringa+=childTree[i].printRules(rules);// entra nella funzione e poi riprende nel for
			
			rules = "";
			stringa+=rules;
			}
			}
			stringa+=("******************\n");
			return stringa;
			}
		
		//private void printRules(String current) { //MODIFICATO IN "String printRules"
		/**
		 * Supporta il metodo public void printRules(). Concatena alle informazioni in current del precedente nodo quelle del nodo root del corrente sotto-albero (oggetto DecisionTree): se il nodo corrente è di split il metodo viene invocato ricorsivamente con current e le informazioni del nodo corrente, se è di fogliare (leaf) visualizza tutte le informazioni concatenate
		 * @param current Informazioni del nodo di split del sotto-albero al livello superiore
		 * @return
		 */
		private String printRules(String current) {
			String stringa = new String(); 
			String x;
			 int i;
				if (root instanceof LeafNode) {
				
				stringa+=(current + "==>Class= " + ((LeafNode) root).getPredictedClassValue() + ""+ "\n");
				
				} else if (root instanceof DiscreteNode) {
				current += "AND " + ((SplitNode) root).getAttribute().getName();
				
				for (i = 0; i < ((SplitNode) root).mapSplit.size(); i++) {
				x = " ";
				x = current + "=" + ((SplitNode) root).mapSplit.get(i).getSplitValue() + " ";
				
				stringa+=childTree[i].printRules(x);
				}
				}else if (root instanceof ContinuousNode) {
				current += "AND " + ((SplitNode) root).getAttribute().getName();
				for (i = 0; i < ((SplitNode) root).mapSplit.size(); i++) {
				x = "";

				x = current +((SplitNode) root).mapSplit.get(i).getComparator()+ ((SplitNode) root).mapSplit.get(i).getSplitValue()+ " ";

				stringa+=childTree[i].printRules(x);
				}
				}
			return stringa;
	    }
		
		
		/**
	     * Visualizza le informazioni di ciascuno split dell'albero (SplitNode.formulateQuery()) e per il corrispondente attributo acquisisce il valore dell'esempio da predire da tastiera. Se il nodo root corrente è leaf termina l'acquisizione e visualizza la predizione per l’attributo classe, altrimenti invoca ricorsivamente sul figlio di root in childTree[] individuato dal valore acquisito da tastiera.
	     *
	     * @return Oggetto String contennente il valore di classe (YES/NO)
	     * dell'esempio inserito
		 * @throws UnknownValueException 
		 * @throws IOException 
		 * @throws ClassNotFoundException 
	     */
		
		 public Double predictClass(ObjectInputStream in,ObjectOutputStream out)throws UnknownValueException, ClassNotFoundException, IOException{
		    	if(root instanceof LeafNode)
		    		return ((LeafNode) root).getPredictedClassValue();
		    	else
		    	{
		    		int risp;
		    		out.writeObject("QUERY");

		    		out.writeObject(((SplitNode)root).formulateQuery());
		    		
		    		risp=(int) in.readObject();
		    		
		    		if(risp==-1 || risp>=root.getNumberOfChildren())
		    			throw new UnknownValueException("The answer should be an integer between 0 and " +(root.getNumberOfChildren()-1)+"!");
		    		else
		    		return childTree[risp].predictClass(in,out);
		    }
		   }
		 

	    	   /**
	    * Serializza l'albero in un file.
	    * @param nomeFile : Nome del file in cui salvare l'albero
	    * @throws FileNotFoundException
	    * @throws IOException
	    */
		public void salva(String nomeFile) throws FileNotFoundException, IOException {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nomeFile));
			out.writeObject(this);

			out.close();
		}
		/**
		 * Carica un albero di regressione salvato in un file.
		 * @param nomeFile :Nome del file in cui è salvato l'albero
		 * @return L'albero contenuto nel file.
		 * @throws FileNotFoundException
		 * @throws ClassNotFoundException
		 * @throws IOException
		 */
		public static RegressionTree carica(String nomeFile)
				throws FileNotFoundException, ClassNotFoundException, IOException {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(nomeFile));
			RegressionTree rTree = (RegressionTree) in.readObject();

			in.close();
			return rTree;
		}
/**
 * 
 * @return radice del sotto-albero corrente
 */
		public Node getRoot() {
			return root;
		}
/**
 * array di sotto-alberi originanti nel nodo root:vi è un elemento nell’array per ogni figlio del nodo
 * @param index
 * @return
 */
		public RegressionTree getChildTree(int index) {
			return childTree[index];
		}
}
		
