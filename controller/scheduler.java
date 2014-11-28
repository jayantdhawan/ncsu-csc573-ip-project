import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class scheduler {

	// 0 = No flow sent, 1 = Flow is being sent, 2 = Flow sent
	public static int flow_status = 0;

	public static void main(String[] args) {

		Connection myConn;
		Statement myStmt;
		ResultSet myRs;
		final String url  = "jdbc:mysql://127.0.0.1/ip_project";
		final String user = "root";
		final String pass = "root";
		final String tablename = "jobs";

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
			myRs   = myStmt.executeQuery("select * from " + tablename);
			
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
		int period = 7000; // repeat every 'period' miliseconds

		Timer timer = new Timer();

		timer.scheduleAtFixedRate(new TimerTask() 
		{
			public void run() 
		    {
				if (flow_status == 1)
				{
					return;
				}

				Connection myConn;
				Statement myStmt;
				ResultSet myRs;
								
				SimpleDateFormat df1;
				String sys_date;
				String sys_time;
				Date dateobj1 = new Date();

				df1        = new SimpleDateFormat("MM/dd/yyyy");
				sys_date   = df1.format(dateobj1);
				
				df1        = new SimpleDateFormat("HH:mm");
				sys_time   = df1.format(dateobj1);

				System.out.println("\nSystem date "+ sys_date + " time " + sys_time);

				try
				{
						myConn = DriverManager.getConnection(url, user, pass);
						myStmt = myConn.createStatement();

						// Query to get the top row in the sorted list
						myRs = myStmt.executeQuery("SELECT JobID, DATE_FORMAT(DateOfJob, '%m/%d/%Y') as DateOfJob, TIME_FORMAT(StartTime, '%H:%i') as StartTime, TIME_FORMAT(StopTime, '%H:%i') as StopTime, DstAdd, Port, Protocol FROM (SELECT * FROM " + tablename +" ORDER BY DateOfJob, StartTime ASC) as j2 LIMIT 1");
						while (myRs.next())
						{
							String job_id = myRs.getString("JobID");
							String job_date = myRs.getString("DateOfJob");
							String job_start_time = myRs.getString("StartTime");
							String job_stop_time = myRs.getString("StopTime");

							int ID = myRs.getInt("JobID");

							if (sys_date.equalsIgnoreCase(job_date) && sys_time.equalsIgnoreCase(job_start_time) && (flow_status == 0))
							{
								flow_status = 1;
								System.out.println("Match found. Preparing flow entries.");

								System.out.println("Job " +myRs.getString("JobID")+": "
								+ myRs.getString("DateOfJob") + " " + myRs.getString("StartTime") + " "
								+ myRs.getString("StopTime") + " " + myRs.getString("DstAdd") + " "
								+ myRs.getString("Port") + " " + myRs.getString("Protocol"));

								flow_status = 2;
							}
							else if (sys_date.equalsIgnoreCase(job_date) && sys_time.equalsIgnoreCase(job_stop_time) && (flow_status == 2))
							{
								System.out.println("Deleting flow entry.");
								//myRs = myStmt.executeQuery("DELETE FROM " + tablename + " WHERE JobID = " + job_id);
								//myRs.next();?
								flow_status = 0;
								
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
