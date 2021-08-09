import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LeadsFileHandler {
	
	private ArrayList<String> settings;
	private String savePath;
	
	//Function for saving a string to a choosen path
	public void writeLeadsToXMLFile(String input, String filePath)
	{	
		try
		{
			FileWriter myWriter = new FileWriter(filePath);
		    myWriter.write(input);
		    myWriter.close();
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
		
	}
	
	public String readFileFromPath(String filePath)
	{	
		String content = "";
		try
		{
			FileReader fr = new FileReader(filePath);
			
			int i;    
	        while((i=fr.read())!=-1) {
	         content += (char)i;
	        }
			fr.close();
		
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
			
		}
		return  content;
	}
	
	public ArrayList<String> getRegisterContent()
	{
		try {
			settings = new ArrayList<String>();
			
			File leads = new File("register.xml");
		    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		    Document doc = dBuilder.parse(leads);
		    doc.getDocumentElement().normalize();
		
		    //System.out.println(doc.getDocumentElement().getNodeName());
		    NodeList nodes = doc.getElementsByTagName("props");
		
		    for (int i = 0; i < nodes.getLength(); i++) {
		      Node node = nodes.item(i);
		      if (node.getNodeType() == Node.ELEMENT_NODE) {
		        Element element = (Element) node;
		        
		        settings.add(getTagValue("url", element));
		        settings.add(getTagValue("key", element));
		        settings.add(getTagValue("jouremailDC", element));
		        settings.add(getTagValue("jouremailWS", element));
		        settings.add(getTagValue("dbUser", element));
		        settings.add(getTagValue("dbPass", element));
		        settings.add(getTagValue("dbDriverURL", element));
		        settings.add(getTagValue("leadsfile", element));
		        settings.add(getTagValue("DCmailUser", element));
		        settings.add(getTagValue("DCmailPass", element));
		        settings.add(getTagValue("logFilePath", element));
		        
		        
		      }
		    }
		    
		    //mylog.logger.info("XML file with leads collected in List. Total number of leads: " + leadlist.size());
		    return settings;
		}
		catch(Exception e)
		{	
			//mylog.logger.severe("Failed to read XML file and create a lead list");
			return settings;
		}	 
	}
	
	private String getTagValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
	}
	
	public void savePreviousWeeksLeads(ArrayList<Lead> previousWeeksLeads) {
        try {
    		FileOutputStream fos = new FileOutputStream("PreviousWeeksLeads.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(previousWeeksLeads);
            oos.close();
        }
        
        catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
    public ArrayList<Lead> getPreviousWeek() {
        FileInputStream fs;
		try {
			fs = new FileInputStream("PreviousWeeksLeads.txt");
	        ObjectInputStream ois = new ObjectInputStream(fs);
	        ArrayList<Lead> leadsFromFile = (ArrayList<Lead>) ois.readObject();
	        ois.close();
	        return leadsFromFile;
		} 
		
		
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
}
