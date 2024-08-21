package data;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import database.DatabaseConnectionException;
import database.DbAccess;
import database.EmptyTypeException;
import database.Example;
import database.TableData;
import database.TableSchema;


/**
 * Modella l'insieme di esempi di training, si occupa di caricare i dati (schema e esempi) di addestramento da una tabella della base di dati
 * @author Silvio Ferrara, Federico Vassallo, Nicola Cortese
 *
 */
public class Data implements Serializable{
	/**
	 * Lista che contiene il training set (esempi).
	 */
	private List<Example> data=new ArrayList<Example>();
	
	/**
	 * Cardinalità del training set
	 */
	private int numberOfExamples;
	
	/**
	 * Lista di oggetti di tipo Attribute per rappresentare gli attributi indipendenti (schema). 
	 */
	private List<Attribute> explanatorySet = new LinkedList<Attribute>();
	
	/**
	 * Oggetto per modellare l'attributo di classe (target attribute). L'attributo di classe è un attributo numerico.
	 */
	private ContinuousAttribute classAttribute;
	
	private DbAccess db;
	private TableData td;
	private TableSchema ts;
	

	/**
*Comportamento: E' il costruttore di classe. Esegue i seguenti compiti: <p>
*1. Avvalora lo schema di esempi explanatorySet <p>
*2. Avvalora classAttribute istanziando un oggetto di tipo ContinuousAttribute. <p>
*3. Avvalora il numero di esempi . <p>
*4. Popola la lista data con gli esempi di training ; <p>
*
	 * @param table Nome della tabella contenente i dati
	 * @throws TrainingDataException
	 * @throws DatabaseConnectionException 
	 */
	public Data(String table) throws  TrainingDataException, DatabaseConnectionException{

		try {
			db= new DbAccess();
			db.initConnection();
			td= new TableData(db);
			ts= new TableSchema(db, table);
			td.getTransazioni(table);
			data=td.getTransazioni(table);
			
			db.closeConnection();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			catch (NullPointerException e) {
				e.printStackTrace();
			}
			catch(DatabaseConnectionException e) {
				throw new TrainingDataException("connessione al database fallita");
			}
			catch(EmptyTypeException e) {
				throw new TrainingDataException("la tabella ha zero tuple");
			};

			if (data.isEmpty()) {
				 throw new TrainingDataException("la tabella non esiste");
			 }
			if (ts.getNumberOfAttributes()<2) {
				throw new TrainingDataException("la tabella ha meno di due colonne");
			}
			
			if( ! ts.getColumn(ts.getNumberOfAttributes() -1 ).isNumber()) {
				throw new TrainingDataException("l’attributo corrispondente all’ultima colonna della tabella non è numerico");
			}
			numberOfExamples=data.size();	
			for (int i = 0; i < ts.getNumberOfAttributes() - 1; i++) {

			    if (ts.getColumn(i).isNumber()) {
			        explanatorySet.add(new ContinuousAttribute(ts.getColumn(i).getColumnName(), i));
			    } else {
			        
			        explanatorySet.add(new DiscreteAttribute(ts.getColumn(i).getColumnName(), i,null));
			        
			    }
			}

		}

	/**
	 * Restituisce il valore del membro numberOfExamples
	 * @return Cardinalità dell'insieme di esempi
	 */
		public int getNumberOfExamples() {
			return this.numberOfExamples;
		}
		

		/**
		 * Restituisce la lunghezza dell'array explanatorySet[]
		 * @return Cardinalità dell'insieme degli attributi indipendenti
		 */
		public int getNumberOfExplanatoryAttributes() {
			return this.explanatorySet.size();
			
		}
		

		/**
		 * Restituisce il valore dell'attributo di classe per l'esempio exampleIndex
		 * @param exampleIndex : indice per uno specifico esempio
		 * @return valore dell'attributo di classe per l'esempio indicizzato in input
		 */
		public Double getClassValue(int exampleIndex) {;		
			return (Double) data.get(exampleIndex).get(this.getNumberOfExplanatoryAttributes());
		}
		
		/**
		 * Restituisce il valore dell' attributo indicizzato da attributeIndex per l'esempio exampleIndex
		 * @param exampleIndex : indice per uno specifico esempio
		 * @param attributeIndex : specifico esempio
		 * @return Object associato all'attributo indipendente per l'esempio indicizzato in input
		 */
		public Object getExplanatoryValue(int exampleIndex, int attributeIndex) {
			return this.data.get(exampleIndex).get(attributeIndex);
		}

		/**
		 * Restituisce l'attributo indicizzato da index in explanatorySet
		 * @param index : indice di explanatorySet per uno specifico attributo indipendente
		 * @returnoggetto Attribute indicizzato da index
		 */
		public Attribute getExplanatoryAttribute(int index) {
			return this.explanatorySet.get(index);
		}
	

		/**
		 * restituisce l'oggetto corrispondente all'attributo di classe
		 * @return Oggetto ContinuousAttribute associato al membro classAttribute;
		 */
		public ContinuousAttribute getClassAttribute() {
			return this.classAttribute;
		}

		/**
		 * legge i valori di tutti gli attributi per ogni esempio da data e li concatena in un oggetto String che restituisce come risultato finale in forma di sequenze di testi.
		 */
		public String toString(){
			String value="";
			for(int i=0;i<numberOfExamples;i++){
				for(int j=0;j<explanatorySet.size();j++)
					value+=data.get(i).get(j);
				
				value+=data.get(i).get(getNumberOfExplanatoryAttributes())+"\n";
			}
			return value;
		}

	/**
	 * Ordina il sottoinsieme di esempi compresi nell'intervallo [inf,sup] 
in data rispetto allo specifico attributo attribute. Usa l'Algoritmo quicksort  
L'array, in questo caso, è dato dai valori assunti dall'attributo passato in input. 
Vengono richiamati i metodi: private void quicksort(Attribute attribute, int inf, int sup); 
private int partition(DiscreteAttribute attribute, int inf, int sup)
e private void swap(int i,int j)
	 * @param attribute : Attributo i cui valori devono essere ordinati.
	 * @param beginExampleIndex : inizio intervallo
	 * @param endExampleIndex : fine intervallo
	 */
	public void sort(Attribute attribute, int beginExampleIndex, int endExampleIndex){
	
			quicksort(attribute, beginExampleIndex, endExampleIndex);
	}
	
	/**
	 * scambio esempio i con esempio j
	 * @param i indice elemento da scambiare
	 * @param j indice altro elemento da scambiare
	 */
	private void swap(int i,int j){
		Collections.swap(data, i, j);	
	}
	

	
	/**
	 * Partiziona il vettore rispetto all'elemento x e restiutisce il punto di separazione
	 * @param attribute tipo discreto
	 * @param inf estremo inferio
	 * @param sup estremo superiore
	 * @return 
	 */
	private  int partition(DiscreteAttribute attribute, int inf, int sup){
		int i,j;
	
		i=inf; 
		j=sup; 
		int	med=(inf+sup)/2;
		String x=(String)getExplanatoryValue(med, attribute.getIndex());
		swap(inf,med);
	
		while (true) 
		{
			
			while(i<=sup && ((String)getExplanatoryValue(i, attribute.getIndex())).compareTo(x)<=0){ 
				i++; 
				
			}
		
			while(((String)getExplanatoryValue(j, attribute.getIndex())).compareTo(x)>0) {
				j--;
			
			}
			
			if(i<j) { 
				swap(i,j);
			}
			else break;
		}
		swap(inf,j);
		return j;

	}
	
	/**
	 * Partiziona il vettore rispetto all'elemento x e restiutisce il punto di separazione
	 * @param attribute tipo continuio
	 * @param inf
	 * @param sup
	 * @return
	 */
	private  int partition(ContinuousAttribute attribute, int inf, int sup){
		int i,j;
	
		i=inf; 
		j=sup; 
		int	med=(inf+sup)/2;
		Double x=(Double)getExplanatoryValue(med, attribute.getIndex());
		swap(inf,med);
	
		while (true) 
		{
			
			while(i<=sup && ((Double)getExplanatoryValue(i, attribute.getIndex())).compareTo(x)<=0){ 
				i++; 
				
			}
		
			while(((Double)getExplanatoryValue(j, attribute.getIndex())).compareTo(x)>0) {
				j--;
			
			}
			
			if(i<j) { 
				swap(i,j);
			}
			else break;
		}
		swap(inf,j);
		return j;

	}

	
	/**
	 * Algoritmo quicksort per l'ordinamento di un array di interi A
	 * usando come relazione d'ordine totale "<="
	 * @param attribute
	 * @param inf estremo inferiore
	 * @param sup estremo superiore
	 */
	private void quicksort(Attribute attribute, int inf, int sup){
		
		if(sup>=inf){
			
			int pos;
			if(attribute instanceof DiscreteAttribute)
				pos=partition((DiscreteAttribute)attribute, inf, sup);
			else 
				pos=partition((ContinuousAttribute)attribute, inf, sup);
					
			if ((pos-inf) < (sup-pos+1)) {
				quicksort(attribute, inf, pos-1); 
				quicksort(attribute, pos+1,sup);
			}
			else
			{
				quicksort(attribute, pos+1, sup); 
				quicksort(attribute, inf, pos-1);
			}
					
		}
		
	}
	

	
}
