
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;


public class ConvertToCsv {
	
	String myCSV;
	final String xmlSchema;
	String myXML;
	
	
	public ConvertToCsv() {

		//Marcus T: Schemat vi fick av kund. 
		xmlSchema =
				
			"<xs:complexType name=\"lead\">" +
				"<xs:sequence>" +
					" <xs:element name=\"name\" nillable=\"true\" type=\"xs:string\"/>" +
					"<xs:element name=\"address\" nillable=\"true\" type=\"xs:string\"/>" +
					"<xs:element name=\"zip\" nillable=\"true\" type=\"xs:string\"/>" +
					"<xs:element name=\"city\" nillable=\"true\" type=\"xs:string\"/>" +
					"<xs:element name=\"contact\" nillable=\"true\" type=\"xs:string\"/>" +
					"<xs:element name=\"tele\" nillable=\"true\" type=\"xs:string\"/>" +
					"<xs:element name=\"size\" nillable=\"true\" type=\"xs:string\"/>" +
					"<xs:element name=\"current_provider\" nillable=\"true\" type=\"xs:string\"/>" +
					"<xs:element name=\"email\" nillable=\"true\" type=\"xs:string\"/>" +
            	"</sequence>" +
            "</lead>";
		
		//Marcus T: Tillfällig teststräng. 
		myXML =
				
			"<document> " +
				"<lead> " +
					"<name>Sven Svensson</name> " +
					"<address>adress1</address> " +
					"<zip>12345</zip> " +
					"<city>Karlstad</city> " +
					"<contact>?</contact> " +
					"<tele>0701</tele> " +
					"<size>?</size> " +
					"<current_provider>BadCompany</current_provider> " +
					"<email>Sven@email.com</email> " +
				"</lead> " +
				"<lead> " +
					"<name>Jan Jansson</name> " +
					"<address>adress2</address> " +
					"<zip>23456</zip> " +
					"<city>Stockholm</city> " +
					"<contact>?</contact> " +
					"<tele>0702</tele> " +
					"<size>?</size> " +
					"<current_provider>BaddestCompany</current_provider> " +
					"<email>Jan@email.com</email> " +
				"</lead> " +
			"</document> ";
	}
	
	//Marcus T: Lösningsförslag för att konvertera till CSV ifall det behövs.
	public String converter() {
		
		myCSV = "";
		
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(myXML)));
			Element rootElement = document.getDocumentElement();
			NodeList list = rootElement.getElementsByTagName("lead");
		
        	if (list != null && list.getLength() > 0) 
        	{
        		for (int i = 0; i < list.getLength(); i++)
        		{
        			if (myCSV != "") { myCSV += "\n"; }
        			myCSV += getString("name", rootElement, i) + ",";
        			myCSV += getString("address", rootElement, i) + ",";
        			myCSV += getString("zip", rootElement, i) + ",";
        			myCSV += getString("city", rootElement, i) + ",";
        			myCSV += getString("contact", rootElement, i) + ",";
        			myCSV += getString("tele", rootElement, i) + ",";
        			myCSV += getString("size", rootElement, i) + ",";
        			myCSV += getString("current_provider", rootElement, i) + ",";
        			myCSV += getString("email", rootElement, i);
        		}           
        	}

		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		//System.out.println(myCSV);
		return myCSV;
	}
	
	//Marcus T: Anropas i metod ovan (converter).
	protected static String getString(String tagName, Element element, int i) {
        NodeList list = element.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0) {
            NodeList subList = list.item(i).getChildNodes();

            if (subList != null && subList.getLength() > 0) {
                return subList.item(0).getNodeValue();
            }
        }

        return null;
    }

}
