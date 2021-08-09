import java.util.ArrayList;

public class Controller {
	
	private CollectLeads cl;
	private LeadsFileHandler lfh;
	private ConnectToDoli con;
	private ValidateLeads vl;
	private SendEmail sm;
	private ArrayList<Lead> leadlist, validatedLeadsList, leadsToSave, leadsFromFile, customersPreviousWeek;
	private ArrayList<String> settingslist;
	private ArrayList<String> leadsCSV;
	private Log myLog;
	private String url,key,emailDC,emailWS,dbUser,dbPass,dbDriverURL, leadsfile, DCmailUser, DCmailPass, logPath;

	
	public Controller()
	{	
		long startTime = System.nanoTime();
		
		lfh = new LeadsFileHandler();
		loadSettings();
		
		myLog = new Log(logPath);
		cl = new CollectLeads(myLog);
		vl = new ValidateLeads(myLog);
		con = new ConnectToDoli(myLog);
		sm = new SendEmail(DCmailUser, DCmailPass);
		
		
		
		prepareLeadList();
		//triggerValidation();
		prepareDoli();
		//leadsFromPreviousWeek();
		//importToDoli();
		//removeNotClientsFromDoli();
		//leadsFromPreviousWeek();
		//importPreviousWeeksLeads();

		
		long endTime = System.nanoTime();
		float timeElapsed = endTime - startTime;
		myLog.logger.info("Successfully executed in " + timeElapsed / 1000000000 + " seconds.");
	}
	
	public void loadSettings()
	{
		settingslist = lfh.getRegisterContent();
		url = settingslist.get(0);
		key = settingslist.get(1);
		emailDC = settingslist.get(2);
		emailWS = settingslist.get(3);
		dbUser = settingslist.get(4);
		dbPass = settingslist.get(5);
		dbDriverURL = settingslist.get(6);
		leadsfile = settingslist.get(7);
		DCmailUser = settingslist.get(8);
		DCmailPass = settingslist.get(9);
		logPath = settingslist.get(10);
	}
	
	//This function will collect and save the response from webscraper through the webscraperURL file in root-folder.
	//If the request and response is completed correctly, a leads.xml file will be created and saved.
	//the leads file
	//Erik & Christoffer, 03-05
	public void prepareLeadList()
	{	
		cl.getResponseFromURL(url, key);
		if(cl.checkResponse())
		{
			String leadsString = cl.getResponse();
			lfh.writeLeadsToXMLFile(leadsString, leadsfile);
			leadlist = cl.getLeadListFromXMLFile();
		}
		else
		{	
			//Send mail to jour people from Webscraper
			String logContent = lfh.readFileFromPath(logPath);
			sm.Send(DCmailUser, emailDC, logContent);
		}
				
	}
	
	public void triggerValidation()
	{
		customersPreviousWeek = con.previousWeeksCustomers();
		validatedLeadsList =  vl.validator(leadlist, customersPreviousWeek);
	}
	
	public void prepareDoli()
	{
		con.setDBsettings(dbUser, dbPass, dbDriverURL);
	}
	
	public void importToDoli()
	{
		con.importLead(leadlist);
	}
	
	public void leadsFromPreviousWeek() {
		leadsToSave = con.previousWeeksLeads();
		lfh.savePreviousWeeksLeads(leadsToSave);
	}
	
	public void customersFromPreviousWeek() {
		con.previousWeeksCustomers();
	}
	
	public void removeNotClientsFromDoli()
	{
		con.removeLead();
	}
	
	public void notifyer(String s, int i)
	{
		String logContent;
		
		switch(i)
		{
		
		//Notifys what went wrong and sends e-mail to Dynamic Commercials.
		case 1:
		
			myLog.logger.info("Problem detected: " + s + ". Sending report by e-mail.");
			logContent = lfh.readFileFromPath(logPath);
			sm.Send(DCmailUser, emailDC, logContent);
			
		//Notifys what went wrong and sends e-mail to both Dynamic Commercials and Web Scraper.
		case 2:
			
			myLog.logger.info("Problem detected: " + s + ". Sending report by e-mail.");
			logContent = lfh.readFileFromPath(logPath);
			sm.Send(DCmailUser, emailDC, logContent);
			sm.Send(DCmailUser, emailWS, logContent);
		
		//Notifys what went wrong, sends e-mail to both Dynamic Commercials and Web Scraper, and shuts down the integration.
		case 3:
			
			myLog.logger.severe("Critical problem detected: " + s + ". Sending report by e-mail and shutting down.");
			logContent = lfh.readFileFromPath(logPath);
			sm.Send(DCmailUser, emailDC, logContent);
			sm.Send(DCmailUser, emailWS, logContent);
			System.exit(0);
		}
	}
		
	public void importPreviousWeeksLeads() {
		leadsFromFile = lfh.getPreviousWeek();
		con.importLead(leadsFromFile);
	}
			
	public static void main(String argv[])   
	{  
		new Controller();
		
	}
}
