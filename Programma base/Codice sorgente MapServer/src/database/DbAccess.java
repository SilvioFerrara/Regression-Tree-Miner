
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import database.DatabaseConnectionException;

/**
 *
 * @author Silvio Ferrara, Federico Vassallo, Nicola Cortese
 */
public class DbAccess {
    
    private static final String DRIVER_CLASS_NAME =  "com.mysql.cj.jdbc.Driver"; //"org.gjt.mm.mysql.Driver"; //"sun.jdbc.odbc.JdbcOdbcDriver";
    
    private static final String DBMS = "jdbc:mysql";
//	contiene l’identificativo del server su cui risiede la base di dati (per esempio localhost)    
    private static String SERVER = "localhost";
//	contiene il nome della base di dati
    private static String DATABASE = "MapDB";
//	La porta su cui il database accetta le connessioni
    private static final String PORT = "3306";
//	contiene il nome dell’utente per l’accesso alla base di dati
    private static String USER_ID = "MapUser";
//	contiene la password di	autenticazione per l’utente identificato da USER_ID
    private static final String PASSWORD = "map";
//	gestisce la connessione correntemente aperta verso la base di dati dell’applicazione
    private static Connection conn;
    
    /**
     * impartisce al class loader l’ordine di caricare il driver mysql, 
     * inizializza la connessione riferita da conn. 
     * Il metodo solleva e propaga una eccezione di tipo DatabaseConnectionException 
     * in caso di fallimento nella connessione al database.
     * @throws ClassNotFoundException 
     *
     */
    public void initConnection() throws DatabaseConnectionException{
        //provvede a caricare il driver per stabilire la connessione con la base di dati e inizializza tale connessione
        //la stringa di connessione sarà: DBMS+"://" + SERVER + ":" + PORT + "/" + "DATABASE"

        try {
            //Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        	Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException: " + e);
        }

        try {
            conn = DriverManager.getConnection(DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=CET", USER_ID, PASSWORD);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("VendorError: " + e.getErrorCode());
            throw new DatabaseConnectionException();
        } 
}

    /**
     * Restituisce la connessione corrente conn
     *
     * @return conn, la connessione corrente
     */
    public static Connection getConnection() {
        return conn;
    }

    /**
     * chiude la connessione conn
     *
     */
    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println("Errore di chiusura connessione!");
            e.printStackTrace();
        }
    }
}
