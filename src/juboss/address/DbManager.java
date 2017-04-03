package juboss.address;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JOptionPane;

public class DbManager {
	
	private File dbDir = null;
	private String path = "";
	private Statement st = null;
	private Connection connection = null;
	private ResultSet rs = null;
	private PreparedStatement pst = null;
	
	
	//Inizializzazione connessione DB
	public DbManager() {
		
		//Get Path DB
		getPath();
		
		//Connessione al DB
		try {
			
			connection = DriverManager.getConnection("jdbc:ucanaccess://" + path);
			
			//Creazione Statement
			st = connection.createStatement();
		} catch (SQLException e) {
			errorMessage(e.getMessage());
		}
	}
	
	
	//Get Path del programma
	private void getPath() {
		
		//Estrazione path
		dbDir = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath());
		
		//Decode path e creazione string
	    path = dbDir.getAbsolutePath();
	    
		try {
			
			path = java.net.URLDecoder.decode(path, "UTF-8");
			
			path = path.replaceAll("bin", "");
		    path = path.replace("\\", "/");
		    path += "db/db.accdb;memory=false;COLUMNORDER=DISPLAY";
		} catch (UnsupportedEncodingException e) {
			errorMessage(e.getMessage());
		}
	}
	
	
	//Count elementi nel DB
	public int countVini() {
		
		//Variabile quantit� vini totali
		int countVini = 0;
		
		//Query per il count
		try {
			
			st.execute("SELECT COUNT(*) FROM Vini");
			rs = st.getResultSet();
			
			while (rs.next()) {
				
				countVini = rs.getInt(1);
			}
			
			return countVini;
		} catch (SQLException e) {
			errorMessage(e.getMessage());
		}
		
		return 0;
	}
	
	
	//Get Info Vini
	public Vector < Vector < Object >> getAllData() {
		
		//Vector di elementi info Vini
		Vector < Vector < Object >> dataVector = new Vector < Vector < Object >>();

		try {
			
			st.execute("SELECT * FROM Vini");
			
			rs = st.getResultSet();
			
			while (rs.next()) {

				Vector < Object > data = new Vector < Object >();
				
	            data.add(rs.getInt(1)); //ID
	            data.add(rs.getString(2)); //Denominazione
	            data.add(rs.getString(3)); //Produttore
	            data.add(rs.getString(4)); //Colore
	            data.add(rs.getString(5)); //TipoVino
	            data.add(rs.getString(6)); //Paese
	            data.add(rs.getString(7)); //Regione
	            data.add(rs.getString(8)); //Capacit�
	            data.add(rs.getString(9)); //Note
	            data.add(rs.getFloat(10)); //Prezzo
	            data.add(rs.getFloat(11)); //Ingrosso
	            data.add(rs.getFloat(12)); //Dettaglio
	            data.add(rs.getBoolean(13)); //Manuale True/False
	            dataVector.add(data);
			}
			
			return dataVector;
		} catch (SQLException e) {
			errorMessage(e.getMessage());
		}
		
		return null;
    }
	
	
	//Inserimento dati in DB con indice n+1
	public void insertData(String denominazione, String produttore, String colore, String tipovino, String paese, String regione, String capacita, String note, float prezzo, float ingrosso, float dettaglio, boolean manuale) {
		
		//Query per l'inserimento dei dati nel DB
		String query = "INSERT INTO Vini (ID, Denominazione, Produttore, Colore, TipoVino, Paese, Regione, Capacita, Note, Prezzo, Ingrosso, Dettaglio, Manuale) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		try {
			
			pst = connection.prepareStatement(query);
			
			pst.setInt(1, countVini()+1); //ID
			pst.setString(2, denominazione); //Denominazione
			pst.setString(3, produttore); //Produttore
			pst.setString(4, colore); //Colore
			pst.setString(5, tipovino); //TipoVino
			pst.setString(6, paese); //Paese
			pst.setString(7, regione); //Regione
			pst.setString(8, capacita); //Capacit�
			pst.setString(9, note); //Note
			pst.setFloat(10, prezzo); //Prezzo
			pst.setFloat(11, ingrosso); //Ingrosso
			pst.setFloat(12, dettaglio); //Dettaglio
			pst.setBoolean(13, manuale); //Manuale True/False
			pst.executeUpdate();
		} catch (SQLException e) {
			errorMessage(e.getMessage());
		}
	}
	
	
	//Modifica row DB tramite indice
	public void editData(int ind, String denominazione, String produttore, String colore, String tipovino, String paese, String regione, String capacita, String note, float prezzo, float ingrosso, float dettaglio, boolean manuale) {
		
		//Query per l'update dei dati nel DB
		String query = "UPDATE Vini SET Denominazione=?, Produttore=?, TipoVino=?, Paese=?, Regione=?, Capacita=?, Note=?, Prezzo=?, Ingrosso=?, Dettaglio=?, Manuale=?  WHERE ID=?";
		
		try {
			
			pst = connection.prepareStatement(query);
			
			pst.setInt(13, ind); //ID
			
			pst.setString(1, denominazione); //Denominazione
			pst.setString(2, produttore); //Produttore
			pst.setString(3, tipovino); //Colore
			pst.setString(4, tipovino); //TipoVino
			pst.setString(5, paese); //Paese
			pst.setString(6, regione); //Regione
			pst.setString(7, capacita); //Capacit�
			pst.setString(8, note); //Note
			pst.setFloat(9, prezzo); //Prezzo
			pst.setFloat(10, ingrosso); //Ingrosso
			pst.setFloat(11, dettaglio); //Dettaglio
			pst.setBoolean(12, manuale); //Manuale True/False
			pst.executeUpdate();
		} catch (SQLException e) {
			errorMessage(e.getMessage());
		}
	}
	
	
	//Eliminazione row DB tramite indice
	public void deleteData(int ind) {
				
		int count = countVini();
		
		try{
			
			if(count == ind) {
				
				st.executeUpdate("DELETE FROM Vini WHERE ID=" + ind);
			} else {
				
				st.execute("SELECT * FROM Vini WHERE ID=" + count);
				rs = st.getResultSet();
				
				rs.next();	
	            rs.getInt(1); //ID
	            String denominazione = rs.getString(2); //Denominazione
	            String produttore = rs.getString(3); //Produttore
	            String colore = rs.getString(4); //Colore
	            String tipovino = rs.getString(5); //TipoVino
	            String paese = rs.getString(6); //Paese
	            String regione = rs.getString(7); //Regione
	            String capacita = rs.getString(8); //Capacit�
	            String note = rs.getString(9); //Note
	            float prezzo = rs.getFloat(10); //Prezzo
	            float ingrosso = rs.getFloat(11); //Ingrosso
	            float dettaglio = rs.getFloat(12); //Dettaglio
	            boolean manuale = rs.getBoolean(13); //Manuale True/False
		        
				editData(ind, denominazione, produttore, colore, tipovino, paese, regione, capacita, note, prezzo, ingrosso, dettaglio, manuale);
				st.executeUpdate("DELETE FROM Vini WHERE ID=" + count);
			}
		} catch (SQLException e) {
			errorMessage(e.getMessage());
		}
	}
	
	
	//Get settings % Ingrosso/Dettaglio
	public int[] getSettings() {
		
		int[] setting = new int[2];
		
		try {
			
			st.execute("SELECT * FROM Settings");
			rs = st.getResultSet();
			
			rs.next();
			setting[0] = rs.getInt(1);
			setting[1] = rs.getInt(2);
			
			return setting;
		} catch (SQLException e) {
			errorMessage(e.getMessage());
		}
		
		return null;
	}
	
	
	//Set settings % Ingrosso/Dettaglio
	public void setSettings(int ingrosso, int dettaglio) {
		
		String query = "UPDATE Settings SET Ingrosso=?, Dettaglio=?";
		
		try {
			
			pst = connection.prepareStatement(query);
			
			pst.setInt(1, ingrosso); //Ingrosso
			pst.setInt(2, dettaglio); //Dettaglio
			pst.executeUpdate();
		} catch (SQLException e) {
			errorMessage(e.getMessage());
		}
	}
	
	
	//Messaggio di errore in MessageDialog e chiamata metodo per chiusura connessione db
	private void errorMessage(String error) {
		
		JOptionPane.showMessageDialog(null,"Connessione al database fallita!\n\n" + error, "Errore database", JOptionPane.ERROR_MESSAGE);
		closeDb();
	}
	
	
	//Chiusura connessione db
	public void closeDb() {
		
		try {
			st.close();
			connection.close();
			rs.close();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}