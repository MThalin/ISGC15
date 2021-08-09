import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class Log {
	public Logger logger;
	FileHandler fh;
	
	public Log(String filename)
	{
		try
		{
				File f = new File(filename);
				if(!f.exists())
				{
					f.createNewFile();
				}
		
				fh = new FileHandler(filename, true);
				logger = Logger.getLogger("test");
				fh.setLevel(Level.ALL); //Change level of logs in log file. INFO, WARNING, SEVERE etc...
				logger.addHandler(fh);
				
				SimpleFormatter formatter = new SimpleFormatter();
				fh.setFormatter(formatter);
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
		
	}
}