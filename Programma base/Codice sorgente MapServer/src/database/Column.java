package database;



/**
 * Classe che modella la struttura di una colonna del database relazionale
 * @author Silvio Ferrara, Federico Vassallo, Nicola Cortese
 *
 */
public class Column {
	

		private String name;
		private String type;
		
		protected Column (String name, String type){
			this.name=name;
			this.type=type;
		}
		
		public String getColumnName() {
			return name;
		}
		
		public boolean isNumber() {
			return type.equals("number");
		}
		
		public String toString() {
			return name+":"+type;
		}
		
}

