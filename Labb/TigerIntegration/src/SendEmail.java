
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import javax.mail.Message.RecipientType;

public class SendEmail {
	
	
		private String username = "";
		private String password = "";

        //private String fromEmail = "TeamTiger1337@gmail.com";
        //private String toEmail = "TeamTiger1337@gmail.com";
	
        public SendEmail (String usIn, String passIn) {
        	username = usIn;
        	password = passIn;
        }
        
        public void Send(String fromEmail, String toEmail, String logContent)
        {	 try {
        	 	Properties properties = new Properties();
		        properties.put("mail.smtp.auth", "true");
		        properties.put("mail.smtp.starttls.enable", "true");
		        properties.put("mail.smtp.host", "smtp.gmail.com");
		        properties.put("mail.smtp.port", "587");
		        
		        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
		            protected PasswordAuthentication getPasswordAuthentication() {
		                return new PasswordAuthentication(username,password);
		            }
		        });
		       
		        MimeMessage msg = new MimeMessage(session);
		       
		            msg.setFrom(new InternetAddress(fromEmail));
		            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
		            msg.setSubject("Fel har uppståt vid hämtning av leadslista");
		            msg.setText("Detta är ett automatiskt email om att leadslistan inte hämtades rätt \n Logfilsinnehåll: \n " + logContent);
		
		
		            Transport.send(msg);
		            System.out.println("Message Sent to: " + toEmail);
		         
		        } catch (Exception e) {
		            System.out.println(e.getMessage());
		            e.printStackTrace();
		           
		        } 
        }
}
