package database;

import database.DbAccess;
import database.Column;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
/**
 * modellano lo schema di una tabella nel database relazionale
 * @author Silvio Ferrara, Federico Vassallo, Nicola Cortese
 *
 */
public class TableSchema implements Iterable<Column>{

    /**
     * Lista delle colonne della tabella
     */
    protected List<Column> tableSchema = new ArrayList<Column>();

    /**
     * Crea lo schema della tabella tableName
     *
     * @param db
     * @param tableName
     * @throws SQLException 
     */
    public TableSchema(DbAccess db, String tableName) throws SQLException {
        HashMap<String, String> mapSQL_JAVATypes = new HashMap<String, String>();

        mapSQL_JAVATypes.put("CHAR", "string");
        mapSQL_JAVATypes.put("VARCHAR", "string");
        mapSQL_JAVATypes.put("LONGVARCHAR", "string");
        mapSQL_JAVATypes.put("BIT", "string");
        mapSQL_JAVATypes.put("SHORT", "number");
        mapSQL_JAVATypes.put("INT", "number");
        mapSQL_JAVATypes.put("LONG", "number");
        mapSQL_JAVATypes.put("FLOAT", "number");
        mapSQL_JAVATypes.put("DOUBLE", "number");



        Connection con = DbAccess.getConnection();
        DatabaseMetaData meta = con.getMetaData();
        ResultSet res = meta.getColumns(null, null, tableName, null);

        while (res.next()) {

            if (mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME"))) {
                tableSchema.add(new Column(
                        res.getString("COLUMN_NAME"),
                        mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))));
            }



        }
        res.close();



    }
    
    /**
     * Restituisce il numero dei campi della tabella
     * 
     * @return size
     */
    public int getNumberOfAttributes() {
        return tableSchema.size();
    }
    
    
    /**
     * Restituisce la colonna in posizione index della tabella
     * 
     * @param index indice della colonna
     * @return iterator
     */
    public Column getColumn(int index) {
        return tableSchema.get(index);
    }
    
    @Override
	public Iterator<Column> iterator() {
		// TODO Auto-generated method stub
		return tableSchema.iterator();
	}
}
