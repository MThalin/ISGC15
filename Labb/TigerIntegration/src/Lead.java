import java.io.Serializable;

//26-4, Christoffer Wiklander
//This class is used to create 1 Lead that will be used in a list in the CollectLeads class
public class Lead implements Serializable {
	private String name;
	private String adress;
	private String zip;
	private String city;
	private String contact;
	private String tele;
	private String size;
	private String current_provider;
	private String email;
	
	//Constructor will set the attributes for the created Lead object
	public Lead(String nameIn, String adressIn, String zipIn, String cityIn, String contactIn, String teleIn, String sizeIn, String currIn, String emailIn)
	{
		name = nameIn;
		adress = adressIn;
		zip = zipIn;
		city = cityIn;
		contact = contactIn;
		tele = teleIn;
		size = sizeIn;
		current_provider = currIn;
		email = emailIn;	
	}
	
	//These methods will return specific values of the Lead object
	public String getName() {return name;}
	public String getAdress() {return adress;}
	public String getZip() {return zip;}
	public String getCity() {return city;}
	public String getContact() {return contact;}
	public String getTele() {return tele;}
	public String getSize() {return size;}
	public String getCurrentProvider() {return current_provider;}
	public String getEmail() {return email;}
}