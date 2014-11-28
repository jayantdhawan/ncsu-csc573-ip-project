import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class scheduler {

	public static int flag=1;

	public static void main(String[] args) {

		Connection myConn;
		Statement myStmt;
		ResultSet myRs;
		final String url  = "jdbc:mysql://127.0.0.1/ip_project";
		final String user = "root";
		final String pass = "root";

		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			System.out.println("MySQL JDBC Driver not found");
			return;
		}

		try
		{
			myConn = DriverManager.getConnection(url, user, pass);

			// Get all the jobs from the database
			myStmt = myConn.createStatement();
			myRs   = myStmt.executeQuery("select * from jobs2");
			
			System.out.println("Jobs available:");

			while (myRs.next())
			{
				// Print all jobs' details
				System.out.println("Job " +myRs.getString("JobID")+": "
				+ myRs.getString("DateOfJob") + " " + myRs.getString("StartTime") + " "
				+ myRs.getString("StopTime") + " " + myRs.getString("DstAdd") + " "
				+ myRs.getString("Port") + " " + myRs.getString("Protocol")
				);
			}
		}
		catch (Exception x)
		{
			System.out.println("Error (MySQL): " + x);
		}
		
		System.out.println("\nMonitoring system date/time...\n");


		// Initialize the scheduler task
		int delay = 0; 		// initial delay for 'delay' miliseconds
		int period = 5000; // repeat every 'period' miliseconds

		Timer timer = new Timer();

		timer.scheduleAtFixedRate(new TimerTask() 
		{
			public void run() 
		    {
				Date dateobj1        = new Date();
				SimpleDateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
				String sys_date      = df1.format(dateobj1);

				Connection myConn;
				Statement myStmt;
				ResultSet myRs;

				System.out.println("System date "+ sys_date + " time " + sys_date);

				try
				{
						myConn = DriverManager.getConnection(url, user, pass);
						myStmt = myConn.createStatement();

						// Query to get the top row in the sorted list
						myRs = myStmt.executeQuery("SELECT JobID, DATE_FORMAT(DateOfJob, '%m/%d/%Y') as DateOfJob, TIME_FORMAT(StartTime, '%H:%i') as StartTime, TIME_FORMAT(StopTime, '%H:%i') as StopTime, DstAdd, Port, Protocol FROM (SELECT * FROM jobs2 ORDER BY DateOfJob, StartTime ASC) as j2 LIMIT 1");
						while(myRs.next())
						{
							String job_date = myRs.getString("DateOfJob");
							int ID = myRs.getInt("JobID");

							if(sys_date.equalsIgnoreCase(job_date) && flag==1)
							{
								flag=0;
								System.out.println("Match found. Preparing flow entries.");

								System.out.println("Job " +myRs.getString("JobID")+": "
								+ myRs.getString("DateOfJob") + " " + myRs.getString("StartTime") + " "
								+ myRs.getString("StopTime") + " " + myRs.getString("DstAdd") + " "
								+ myRs.getString("Port") + " " + myRs.getString("Protocol"));

								//Statement myStmt1 = myConn.createStatement();
								//myStmt1.executeUpdate("delete from IP where JobID="+ID+"");								
							}
							else 
							{
								System.out.println("Match not found");
							}
						}				
		//				myConn.commit();
						myConn.close();
				}
				
				catch(Exception x)
				{
					System.out.println("Error (MySQL): " + x);
				}
		    }
		 }, delay, period);
	}

}
