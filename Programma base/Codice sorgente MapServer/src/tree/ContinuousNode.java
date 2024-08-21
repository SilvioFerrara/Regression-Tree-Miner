package tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import data.Attribute;
import data.ContinuousAttribute;
import data.Data;


/**
 * La classe ContinuousNode, estende la superclasse SplitNode, per modellare l'entità nodo di split relativo ad un attributo indipendente continuo.
 * @author Silvio Ferrara, Federico Vassallo, Nicola Cortese
 *
 */
public class ContinuousNode extends SplitNode implements Serializable{


	
/**
 * Istanzia un oggetto che rappresenta un nodo corrispondente ad un attributo continuo, invocando il costruttore della superclasse con il parametro attribute
 * @param trainingSet training set complessivo
 * @param beginExampleIndex indice estremo superiore del sotto-insieme di training
 * @param endExampleIndex indice estremo inferiore del sotto-insieme di training
 * @param attribute attributo indipendente sul quale si definisce lo split
 */
	 public ContinuousNode(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute) {
		super(trainingSet, beginExampleIndex, endExampleIndex, attribute);
		// TODO Auto-generated constructor stub
	}

	 /**
	  * Istanzia oggetti SpliInfo 
(definita come inner class in Splitnode) con ciascuno dei valori continui dell’attributo 
relativamente al sotto-insieme di training corrente 
(ossia la porzione di trainingSet compresa tra beginExampelIndex e endExampelIndex), 
quindi popola l'array c mapSplit[] con tali oggetti.
	  */
	 protected void setSplitInfo(Data trainingSet,int beginExampleIndex, int endExampleIndex, Attribute attribute){
			//Update mapSplit defined in SplitNode -- contiene gli indici del partizionamento
			Double currentSplitValue= (Double)trainingSet.getExplanatoryValue(beginExampleIndex,attribute.getIndex());
			double bestInfoVariance=0;
			List <SplitInfo> bestMapSplit=null;
			
			for(int i=beginExampleIndex+1;i<=endExampleIndex;i++){
				Double value=(Double)trainingSet.getExplanatoryValue(i,attribute.getIndex());
				if(value.doubleValue()!=currentSplitValue.doubleValue()){
				//	System.out.print(currentSplitValue +" var ");
					double localVariance=new LeafNode(trainingSet, beginExampleIndex,i-1).getVariance();
					double candidateSplitVariance=localVariance;
					localVariance=new LeafNode(trainingSet, i,endExampleIndex).getVariance();
					candidateSplitVariance+=localVariance;
					//System.out.println(candidateSplitVariance);
					if(bestMapSplit==null){
						bestMapSplit=new ArrayList<SplitInfo>();
						bestMapSplit.add(new SplitInfo(currentSplitValue, beginExampleIndex, i-1,0,"<="));
						bestMapSplit.add(new SplitInfo(currentSplitValue, i, endExampleIndex,1,">"));
						bestInfoVariance=candidateSplitVariance;
					}
					else{		
												
						if(candidateSplitVariance<bestInfoVariance){
							bestInfoVariance=candidateSplitVariance;
							bestMapSplit.set(0, new SplitInfo(currentSplitValue, beginExampleIndex, i-1,0,"<="));
							bestMapSplit.set(1, new SplitInfo(currentSplitValue, i, endExampleIndex,1,">"));
						}
					}
					currentSplitValue=value;
				}
			}
			mapSplit=bestMapSplit;
			//rimuovo split inutili (che includono tutti gli esempi nella stessa partizione)
			
			if((mapSplit.get(1).beginIndex==mapSplit.get(1).getEndIndex())){
				mapSplit.remove(1);
				
			}
			
	 }

	@Override
	protected int testCondition(Object value) {
	    if (((Float) (value)).floatValue() <= ((Float) mapSplit.get(0).splitValue).floatValue()) {
            return 0;
        } else {
            return 1;
        }
	}
	 

	 
    public String toString() {
        return "CONTINUOUS " + super.toString();
    }

}
