
import java.util.ArrayList;

public class ValidateLeads {

	private Log mylog;
	private Boolean match;
	
	public ValidateLeads(Log myLogIn) {
		
		mylog = myLogIn;
	}
	
	//Marcus T: Tar emot en leadlista, skriver den validerade datan till en ny leadlista, returnerar den valida listan.
	//          Om 'name', 'contact' eller 'tele' �r fel, s� f�rsvinner hela "leaden". Om n�got annat �r fel s� f�rsvinner endast det f�ltet.
	public ArrayList<Lead> validator(ArrayList<Lead> tempList , ArrayList<Lead> customerList) {
		
		ArrayList<Lead> validList = new ArrayList<Lead>();
		
		try {
			
			for (int i = 0; i < tempList.size(); i++) {
				
				match = false;
				
				String leadName = tempList.get(i).getName();
				String leadAddress = tempList.get(i).getAdress();
				String leadZip = tempList.get(i).getZip();
				String leadCity = tempList.get(i).getCity();
				String leadContact = tempList.get(i).getContact();
				String leadTele = tempList.get(i).getTele();
				String leadSize = tempList.get(i).getSize();
				String leadCurrent = tempList.get(i).getCurrentProvider();
				String leadEmail = tempList.get(i).getEmail();
				
				Boolean name = false;
				Boolean address = false;
				Boolean zip = false;
				Boolean city = false;
				Boolean contact = false;
				Boolean tele = false;
				Boolean size = false;
				Boolean current = false;
				Boolean email = false;		
				
				if (leadName.length()> 40) { leadName = leadName.substring(0,40); }
				if (leadAddress.length()> 40) { leadAddress = leadAddress.substring(0,40); }
				if (leadCity.length()> 40) { leadCity = leadCity.substring(0,40); }
				if (leadContact.length()> 40) { leadContact = leadContact.substring(0,40); }
				if (leadTele.length()> 40) { leadTele = leadTele.substring(0,40); }
				if (leadSize.length()> 40) { leadSize = leadSize.substring(0,40); }
				if (leadCurrent.length()> 40) { leadCurrent = leadCurrent.substring(0,40); }
				if (leadEmail.length()> 40) { leadEmail = leadEmail.substring(0,40); }
				
				if (!leadName.equals("null") && digitCheck(leadName) == false && letterCheck(leadName) == true) { name = true; }
				if (!leadAddress.equals("null")) { address = true; }
				if (!leadZip.equals("null") && leadZip.length()== 5 && letterCheck(leadZip) == false) { zip = true; }
				if (!leadCity.equals("null") && digitCheck(leadCity) == false) { city = true; }
				if (!leadContact.equals("null") && digitCheck(leadContact) == false && letterCheck(leadContact) == true) { contact = true; }
				if (!leadTele.equals("null") && letterCheck(leadTele) == false && digitCheck(leadTele) == true) { tele = true; }
				if (!leadSize.equals("null") && letterCheck(leadSize) == false) { size = true; }
				if (!leadCurrent.equals("null")) { current = true; }
				if (!leadEmail.equals("null") && leadEmail.contains("@")) { email = true; }
				
				for (int j = 0; j < validList.size(); j++) {
					
					if (validList.get(j).getTele().equals(leadTele)) { match = true; continue; }
				}
				
				for (int j = 0; j < customerList.size(); j++) {
					
					if (customerList.get(j).getTele().equals(leadTele)) { match = true; continue; }
				}
				
				if (name == false || contact == false || tele == false || match == true) { continue; }
				
				else {
					
					if (address == false) { leadAddress = ""; }
					if (zip == false) { leadZip = ""; }
					if (city == false) { leadCity = ""; }
					if (size == false) { leadSize = ""; } 
					if (current == false) { leadCurrent = ""; }
					if (email == false) { leadEmail = ""; }
					
					Lead temp = new Lead(leadName, leadAddress, leadZip, leadCity, leadContact, leadTele, leadSize, leadCurrent, leadEmail);
					validList.add(temp);
				}
			}
			
			mylog.logger.info("Validation completed, number of valid leads: " + validList.size() + ". Number of invalid leads: " + (tempList.size() - validList.size()) + ".");
		}
			
		catch (Exception e) {
			e.printStackTrace();
			mylog.logger.severe("Validation failed: " + e.getMessage());
		}
		
		return validList;
	}
	
	//Marcus T: Letar bokst�ver, returnerar 'true' om en bokstav hittas, annars 'false'.
	public Boolean letterCheck(String x) {
		
		for (int i = 0; i < x.length(); i++) {
			
			char c = x.charAt(i);
			if (Character.isLetter(c)) { return true; }
		}
		
		return false;
	}
	
	//Marcus T: Letar siffror, returnerar 'true' om en siffra hittas, annars 'false'.
	public Boolean digitCheck(String x) {
		
		for (int i = 0; i < x.length(); i++) {
			
			char c = x.charAt(i);
			if (Character.isDigit(c)) { return true; }
		}
		
		return false;
	}
	

}