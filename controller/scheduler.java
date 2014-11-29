
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;
import org.json.*;

import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class scheduler {

	// 0 = No flow sent, 1 = Flow is being sent, 2 = Flow sent
	public static int flow_status = 0;
	
	static void sendFlowAndGetResponse(String flow) throws UnsupportedEncodingException, IOException {

		String controller_ip = "127.0.0.1:8080";

		System.out.println("Flow to be pushed:\n" + flow);

		// Connect to the controller
		URL url = new URL("http://" + controller_ip + "/wm/staticflowentrypusher/json");
		HttpURLConnection connection;

		connection = (HttpURLConnection) url.openConnection();

		connection.setDoOutput(true);

		// Push flow to the controller
		OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

		writer.write(flow);
		writer.flush();

		// Get response from the controller
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
		writer.close();
		reader.close();
	}

	public static void main(String[] args) throws UnsupportedEncodingException, IOException {

		Connection myConn;
		Statement myStmt;
		ResultSet myRs;
		final String db_url  = "jdbc:mysql://127.0.0.1/ip_project";
		final String db_user = "root";
		final String db_pass = "root";
		final String db_tablename = "jobs";

/* Trying out JSON */
		String myString;

		URL url = new URL("http://127.0.0.1:8080/wm/device/");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) 
		{
			for (String line; (line = reader.readLine()) != null;) 
			{
				String device_ip;

				System.out.println(line + "\n");

				JSONArray json_a = new JSONArray(line);
				
				for (int i = 0; i < json_a.length(); i++)
				{
					System.out.println(json_a.length());
					JSONObject json = json_a.getJSONObject(i);
					device_ip = json.get("ipv4").toString();

					JSONArray json_a_attachmentPoints = new JSONArray(json.get("attachmentPoint").toString());

					for (int j = 0; j < json_a_attachmentPoints.length(); j++)
					{
						json = json_a_attachmentPoints.getJSONObject(j);
						System.out.println("dpid " + json.get("switchDPID") + " port " + json.get("port"));
					}
				}
			}
		}

/*
		// Create the flow
		String switch_dpid = "00:00:00:00:00:00:00:01";
		String flow_name   = "flow-mod-1";
		String flow_priority = "32766";
		String flow_outport  = "1";
		String flow_q        = "1";
		String flow_actions  = "enqueue=" + flow_outport + ":" + flow_q;

		JSONObject json_req = new JSONObject();
		json_req.put("switch", switch_dpid);
		json_req.put("name", flow_name);
		json_req.put("cookie", "" + 0);
		json_req.put("priority", "" + flow_priority);
		json_req.put("ingress-port", "" + 2);
		json_req.put("active", "true");
		json_req.put("actions", flow_actions);
		
		//flow = "{\"switch\":\"" + switch_dpid + "\",\"name\":\"" + flow_name + "\",\"cookie\":\"0\",\"priority\":\"32768\",\"ingress-port\":\"2\",\"active\":\"true\",\"actions\":\"output=5\"}";

		sendFlowAndGetResponse(json_req.toString());
		
		
/* Trying out JSON */
/*
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
			myConn = DriverManager.getConnection(db_url, db_user, db_pass);

			// Get all the jobs from the database
			myStmt = myConn.createStatement();
			myRs   = myStmt.executeQuery("select * from " + db_tablename);
			
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
						myConn = DriverManager.getConnection(db_url, db_user, db_pass);
						myStmt = myConn.createStatement();

						// Query to get the top row in the sorted list
						myRs = myStmt.executeQuery("SELECT JobID, DATE_FORMAT(DateOfJob, '%m/%d/%Y') as DateOfJob, TIME_FORMAT(StartTime, '%H:%i') as StartTime, TIME_FORMAT(StopTime, '%H:%i') as StopTime, DstAdd, Port, Protocol FROM (SELECT * FROM " + db_tablename +" ORDER BY DateOfJob, StartTime ASC) as j2 LIMIT 1");
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
								//myRs = myStmt.executeQuery("DELETE FROM " + db_tablename + " WHERE JobID = " + job_id);
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
*/
	}

}
