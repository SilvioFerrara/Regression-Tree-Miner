package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import database.DbAccess;
import database.Column;

/**
 * Classe per gestire i dati contenuti in una tabella. Al suo interno è
 * innestata la classe TupleData utilizzata per gestire una tupla della tabella
 *
 * @author Silvio Ferrara, Federico Vassallo, Nicola Cortese
 */
public class TableData {
	/**
	 * la classe DbAccess realizza l'accesso alla base di dati.
	 */
	private DbAccess db;

    public TableData(DbAccess db) {
    	this.db=db;
    }

    /**
     * Ricava lo schema della tabella con nome table. Esegue una interrogazione per estrarre le tuple distinte da tale tabella. 
     * Per ogni tupla del resultset, si crea un oggetto, istanza della classe Example, il cui riferimento va incluso nella lista 
     * da restituire. In particolare, per la tupla corrente nel resultset, si estraggono i valori dei singoli campi 
     * (usando getFloat() o getString()), e li si aggiungono all’oggetto istanza della classe Example che si sta costruendo.
     * Il metodo può propagare un eccezione di tipo SQLException (in presenza di errori nella esecuzione della query) 
     * o EmptySetException (se il resultset è vuoto)
     *
     * @param table babella nel database.
     * @return Lista di transizioni memorizzate nella tabella.
     * @throws SQLException
     * @throws EmptySetException
     */
    public List<Example> getTransazioni(String table) throws SQLException, EmptyTypeException {
        
    	LinkedList<Example> transSet = new LinkedList<Example>();
    	Statement statement;
    	TableSchema tSchema = new TableSchema(db,table);
        

        String query="select ";
        
        for(int i=0;i<tSchema.getNumberOfAttributes();i++){
			Column c=tSchema.getColumn(i);
			if(i>0)
				query+=",";
			query += c.getColumnName();
		}
		if(tSchema.getNumberOfAttributes()==0)
			throw new SQLException();
		query += (" FROM "+table);
		
		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
        
		boolean empty=true;
        while (rs.next()) {
        	empty=false;
            Example currentTuple = new Example();
            for (int i = 1; i <= tSchema.getNumberOfAttributes(); i++) {
                if (tSchema.getColumn(i - 1).isNumber()) {
                    //currentTuple.add(rs.getFloat(i));
                	currentTuple.add(rs.getDouble(i)); 
                    //MODIFICA FLOAT IN DOUBLE!
                } else {
                    currentTuple.add(rs.getString(i));
                }
            }
            transSet.add(currentTuple);
        }
        rs.close();
        statement.close();
        if(empty) throw new EmptyTypeException();

        return transSet;
    }

    /**
     * Formula ed esegue una interrogazione SQL per estrarre i valori distinti 
     * ordinati di column e popolare un insieme da restituire
     *
     * @param table Tabella da interrogare
     * @param column colonna da interrogare
     * @return Set dei valori di un attributo.
     * @throws SQLException
     */
    public Set<Object> getDisctinctColumnValues(String table, Column column) throws SQLException {
        Set<Object> valueSet = new HashSet<Object>();
        QUERY_TYPE modality = null;
        Statement s = DbAccess.getConnection().createStatement();
        System.out.println("SELECT " + modality + " " + column.getColumnName() + " FROM " + table + " ORDER BY " + column.getColumnName() + ";");
        ResultSet transQuery = s.executeQuery("SELECT " + modality + " " + column.getColumnName() + " FROM " + table + " ORDER BY " + column.getColumnName() + ";");
        while (transQuery.next()) {
            valueSet.add(transQuery.getObject(1));
        }
        transQuery.close();
        s.close();
        return valueSet;
    }
    
    /**
     * classe enumerativa per l'ordinamento secondo un certo criterio
     * @author Silvio
     *
     */
    public enum QUERY_TYPE
    {
    	MIN, MAX;
    }
}
