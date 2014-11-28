import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class Test {
	public static int flag=1;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 /*---------------------------------JDBC start-----------------------------------------------*/
		String url="jdbc:oracle:thin:System@//Apoorv-PC:1521/xe";
		String user="SYSTEM";
		String pass="apoorvm";
		int delay = 0; // delay for 0 sec.
		int period = 10000; // repeat every minute.
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() 
		{	
			public void run() 
		    {
				Date dateobj1= new Date() ;
				SimpleDateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
				String sys_date = df1.format(dateobj1);
				System.out.println("System date"+sys_date);
				try
				{
						Connection myConn = DriverManager.getConnection(url,user,pass);
						Statement myStmt = myConn.createStatement();
						ResultSet myRs = myStmt.executeQuery("select * from (select * from IP ORDER BY  JobID) where rownum=1");
						while(myRs.next())
						{
							String job_date=myRs.getString("DateOfJob");
							int ID=myRs.getInt("JobID");
							System.out.println("job date:"+job_date);
							if(sys_date.equalsIgnoreCase(job_date) && flag==1)
							{
								flag=0;
								System.out.println("Match found");
								Statement myStmt1 = myConn.createStatement();
								myStmt1.executeUpdate("delete from IP where JobID="+ID+"");
								
							}
				
							else 
							{
								System.out.println("Match not found");
							}
						}
						
						myConn.commit();
						myConn.close();
				}
				
				catch(Exception x)
				{
					System.out.println("Unable to load the driver class!");
				}
		    }
		 }, delay, period);
	}

}
