import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CollectLeads {
	
	private Log mylog;
	private ArrayList<Lead> leadlist = new ArrayList<Lead>();
	private Boolean webReq = null;
	private String response = "";
	
	public CollectLeads(Log myLogIn)
	{
		mylog = myLogIn;
	}
	
	//https://www.baeldung.com/java-http-request
	//This function will trigger the request from WebScraper URL and return the response in a string
	//28-4, Christoffer Wiklander
	public void getResponseFromURL(String requestURL, String key)
	{	
		
		try {
			URL url = new URL(requestURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			
			con.setRequestProperty("Authorization", key);
			
			BufferedReader in = new BufferedReader(
					  new InputStreamReader(con.getInputStream()));
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
					    response = inputLine;
					}
			in.close();
			
			con.disconnect();
			
			webReq = true;
			mylog.logger.info("Webscraper request completed and collected");
		}
		catch(Exception e) {
			webReq = false;
			mylog.logger.severe("Webscraper request failed: " + e.getMessage());
		}
	}
	
	public String getResponse()
	{
		return response;
	}
	public Boolean checkResponse()
	{
		return webReq;
	}
	
	//This function will read the saved leadsfile and return a list of leads objects
	//Erik & Christoffer, 03-05
	public ArrayList<Lead> getLeadListFromXMLFile()
	{
		try {
			File leads = new File("leads.xml");
		    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		    Document doc = dBuilder.parse(leads);
		    doc.getDocumentElement().normalize();
		
		    NodeList nodes = doc.getElementsByTagName("lead");
		
		    for (int i = 0; i < nodes.getLength(); i++) {
		      Node node = nodes.item(i);
		      if (node.getNodeType() == Node.ELEMENT_NODE) {
		        Element element = (Element) node;
		        Lead l = new Lead(getTagValue("name", element),getTagValue("address", element),getTagValue("zip", element),getTagValue("city", element),getTagValue("contact", element),getTagValue("tele", element),getTagValue("size", element),element.getElementsByTagName("current_provider").item(0).getChildNodes().item(0).getNodeValue(),getTagValue("email", element));
		        leadlist.add(l);
		      }
		    }
		    
		    mylog.logger.info("XML file with leads collected in List. Total number of leads: " + leadlist.size());
		    return leadlist;
		}
		catch(Exception e)
		{	
			mylog.logger.severe("Failed to read XML file and create a lead list");
			return leadlist;
		}
		 
	}
	
	//This function will collect values from inside the xml tags and return its values
	private String getTagValue(String tag, Element element) {
        NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
        if(nodes.item(0) == null) {
            return "";
        }
        else {
            Node node = (Node) nodes.item(0);
            return node.getNodeValue();
        }
    }
}
