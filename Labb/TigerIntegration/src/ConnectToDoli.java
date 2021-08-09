
/*
 * 19/4-21 Marcus Simonsson
 */
import java.sql.*;
import java.util.ArrayList;


public class ConnectToDoli {
	
	private String userName;
	private String passWord;
	private String dbDriverURL;
	private ArrayList<Lead> customersList = new ArrayList<Lead>();
	private ArrayList<Lead> previousWeeksLeads = new ArrayList<Lead>();
	Log mylog;

	public ConnectToDoli (Log myLogIn) {
		
		mylog = myLogIn;
	}
	
	
	public void setDBsettings(String user, String pass, String driver)
	{
		userName = user;
		passWord = pass;
		dbDriverURL = driver;
	}

	/*
	 * Method for adding leads from the list.
	 */
	public void importLead(ArrayList<Lead> leadList) {
		try {

			Class.forName("com.mysql.cj.jdbc.Driver"); 
			java.sql.Connection con = DriverManager.getConnection(dbDriverURL,userName,passWord);

			for(int i = 0; i < leadList.size(); i++) {
				Statement stmtInsert = con.createStatement();	
				
				stmtInsert.executeUpdate("INSERT INTO llx_societe (nom, name_alias, address, zip, town, phone, email, note_private, siren, client, status) values "
						+ "('"+ leadList.get(i).getName()+"','"+ leadList.get(i).getContact()+"','"+ leadList.get(i).getAdress()+"','"+ leadList.get(i).getZip()
						+"','"+ leadList.get(i).getCity()+"','"+ leadList.get(i).getTele()+"','"+ leadList.get(i).getEmail()+"','"+ leadList.get(i).getCurrentProvider()
						+"','"+ leadList.get(i).getSize()+"','"+ 2 +"','1')");
			}
			con.close();
			mylog.logger.info("Import to dolibarr completed." + leadList.size() + " leads added.");
		}
		
		catch (Exception e){
			e.printStackTrace();
			mylog.logger.severe("Import to dolibarr failed: "  + e.getMessage());
		}
	}
	
	/*
	 * Method for removing leads.
	 */
	public void removeLead() {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");  
			java.sql.Connection con = DriverManager.getConnection(dbDriverURL,userName,passWord);
			Statement stmtDelete = con.createStatement();
			stmtDelete.executeUpdate("DELETE FROM `llx_societe` where `client` = '2' and `fk_stcomm` = '0' or `fk_stcomm` = '-1'");
			con.close();
			mylog.logger.info("Leads with status not client removed");
		}
		
		catch (Exception e) {
			e.printStackTrace();
			mylog.logger.severe("Remove leads failed: " + e.getMessage());
		}
	}
	
	/*
     * Method for saving previous weeks leads into a new list in case of something going wrong with the new list from webscraper.
     */
    public ArrayList<Lead> previousWeeksLeads() {
    	previousWeeksLeads.clear();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection con = DriverManager.getConnection(dbDriverURL,userName,passWord);
            Statement stmtSave = con.createStatement();
            String query = "SELECT * FROM llx_societe WHERE client = '2'";
            ResultSet rs = stmtSave.executeQuery(query);
            
            while(rs.next()) {
                String nom = rs.getString("nom");
                String name_alias = rs.getString("name_alias");
                String address = rs.getString("address");
                String zip = rs.getString("zip");
                String town = rs.getString("town");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String note_private = rs.getString("note_private");
                String siren = rs.getString("siren");
                String client = rs.getString("client");
                String status = rs.getString("status");
                String line = String.format("%s,%s,,%s,,,,,%s,%s,%s,,,%s,,,%s,,,,,%s,,,,,,,,,%s,,%s,0.", nom, name_alias, status, address, zip, town, phone, email, siren, note_private, client);
                Lead l = new Lead(nom, address, zip, town, name_alias, phone, siren, note_private, email);            
                previousWeeksLeads.add(l);
            }
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }
        return previousWeeksLeads;
    }
 
	/*
	 * Method for collecting previous weeks customers..
	 */
    public ArrayList<Lead> previousWeeksCustomers() {
    	customersList.clear();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection con = DriverManager.getConnection(dbDriverURL,userName,passWord);
            Statement stmtSave = con.createStatement();
            String query = "SELECT * FROM llx_societe WHERE client = '1'";
            ResultSet rs = stmtSave.executeQuery(query);
            
            while(rs.next()) {
                String nom = rs.getString("nom");
                String name_alias = rs.getString("name_alias");
                String address = rs.getString("address");
                String zip = rs.getString("zip");
                String town = rs.getString("town");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String note_private = rs.getString("note_private");
                String siren = rs.getString("siren");
                String client = rs.getString("client");
                String status = rs.getString("status");
                String line = String.format("%s,%s,,%s,,,,,%s,%s,%s,,,%s,,,%s,,,,,%s,,,,,,,,,%s,,%s,0.", nom, name_alias, status, address, zip, town, phone, email, siren, note_private, client);
                Lead l = new Lead(nom, address, zip, town, name_alias, phone, siren, note_private, email);            
                customersList.add(l);
            }
            con.close();
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }
        return customersList;
    }
}
