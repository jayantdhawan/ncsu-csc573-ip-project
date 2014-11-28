import java.awt.EventQueue;

import javax.swing.JFrame;
//import javax.swing.BoxLayout;

//import java.awt.GridLayout;

import javax.swing.JLabel;

//import java.awt.BorderLayout;

import javax.swing.SwingConstants;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

//import net.miginfocom.swing.MigLayout;

//import java.awt.GridBagLayout;
//import java.awt.GridBagConstraints;
//import java.awt.Insets;
import java.awt.Color;
import java.awt.Font;

//import javax.swing.JTextPane;
import javax.swing.JTextField;
//import javax.swing.JList;
//import javax.swing.ListSelectionModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class GUIforIP {

	private JFrame frmPacketPrioritizationScheduler;
	private JTextField db_date;
	private JTextField db_startTime;
	private JTextField db_stopTime;
	private JTextField db_dstAdd;
	private JTextField db_dstPort;
	private JTextField db_protocol;
	private JTextField db_id;
	String url="jdbc:oracle:thin:System@//Apoorv-PC:1521/xe";
	String user="SYSTEM";
	String pass="apoorvm";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIforIP window = new GUIforIP();
					window.frmPacketPrioritizationScheduler.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUIforIP() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmPacketPrioritizationScheduler = new JFrame();
		frmPacketPrioritizationScheduler.setTitle("Packet Prioritization Scheduler");
		frmPacketPrioritizationScheduler.setBounds(100, 100, 521, 338);
		frmPacketPrioritizationScheduler.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblNewLabel_1 = new JLabel("SCHEDULE NEW JOB:");
		lblNewLabel_1.setFont(new Font("Calibri", Font.BOLD, 16));
		lblNewLabel_1.setBackground(Color.WHITE);
		lblNewLabel_1.setVerticalAlignment(SwingConstants.TOP);
		
		JLabel lblNewLabel_3 = new JLabel("DATE");
		lblNewLabel_3.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel lblNewLabel_2 = new JLabel("DISPLAY PENDING JOBS");
		lblNewLabel_2.setFont(new Font("Calibri", Font.BOLD, 16));
		lblNewLabel_2.setVerticalAlignment(SwingConstants.TOP);
		frmPacketPrioritizationScheduler.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{lblNewLabel_2}));
		
		JLabel lblNewLabel = new JLabel("DELETE A JOB:");
		lblNewLabel.setFont(new Font("Calibri", Font.BOLD, 16));
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		
		JLabel lblNewLabel_4 = new JLabel("START TIME");
		
		JLabel lblEndTime = new JLabel("STOP TIME");
		
		JButton btnAddSchedule = new JButton("ADD SCHEDULE");
		btnAddSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					String s_date=db_date.getText();
					String s_startTime=db_startTime.getText();
					String s_stopTime=db_stopTime.getText();
					String s_dstAdd=db_dstAdd.getText();
					String s_dstPort=db_dstPort.getText();
					String s_protocol=db_protocol.getText();
					
			
						Connection myConn = DriverManager.getConnection(url,user,pass);
						Statement myStmt = myConn.createStatement();
						String query = "insert into ip values(ip_seq.nextval,'"+s_date+"','"+s_startTime+"','"+s_stopTime+"','"+s_dstAdd+"','"+s_dstPort+"','"+s_protocol+"')";
						myStmt.executeUpdate(query);
						System.out.println("\nValues Inserted..");
						myConn.commit();
						myConn.close();
						
				}
				catch(Exception x)
				{
					x.printStackTrace();
				}
				
			}
		});
		
		JButton btnDisplay = new JButton("DISPLAY");
		btnDisplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{			
						Connection myConn = DriverManager.getConnection(url,user,pass);
						Statement myStmt = myConn.createStatement();
						ResultSet myRs = myStmt.executeQuery("select * from IP");
						System.out.println("\nDisplaying entries....\n\n");
						while(myRs.next())
						{
							int ID=myRs.getInt("JobID");
							String s_date = myRs.getString("DateOfJob");
							String s_startTime=myRs.getString("StartTime");
							String s_stopTime=myRs.getString("StopTime");
							String s_dstAdd=myRs.getString("DstAdd");
							String s_dstPort=myRs.getString("Port");
							String s_protocol=myRs.getString("Protocol");
							System.out.print("\nID:"+ID);
							System.out.print("  Date:");
							System.out.print(s_date);
							System.out.print("  Start Time:");
							System.out.print(s_startTime);
							System.out.print("  Stop Time:");
							System.out.print(s_stopTime);
							System.out.print("  Dest Add:");
							System.out.print(s_dstAdd);
							System.out.print("  Port:");
							System.out.print(s_dstPort);
							System.out.print("  Protocol:");
							System.out.print(s_protocol);
							
						}
						myConn.commit();
						myConn.close();
						
				}
				catch(Exception x)
				{
					x.printStackTrace();
				}
			}
		});
		
		JLabel lblNewLabel_5 = new JLabel("JOB ID:");
		
		JButton btnDelete = new JButton("DELETE");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{		
						int jobID=Integer.parseInt(db_id.getText());
						Connection myConn = DriverManager.getConnection(url,user,pass);
						Statement myStmt = myConn.createStatement();
						ResultSet myRs = myStmt.executeQuery("select jobid from IP");
						while(myRs.next())
						{
							int ID=myRs.getInt("JobID");
							if(jobID==ID)
							{
								Statement myStmt1 = myConn.createStatement();
								myStmt1.executeUpdate("delete from IP where JobID="+ID+"");
								System.out.println("\nDeleted...\n");
							}
							
						}
						myConn.commit();
						myConn.close();
						
				}
				catch(Exception x)
				{
					x.printStackTrace();
				}
			}
		});
		
		JLabel lblDestinationAddress = new JLabel("DESTINATION ADDRESS");
		
		db_date = new JTextField();
		
		db_date.setColumns(10);
		
		db_startTime = new JTextField();
		db_startTime.setColumns(10);
		
		db_stopTime = new JTextField();
		db_stopTime.setHorizontalAlignment(SwingConstants.LEFT);
		db_stopTime.setColumns(10);
		
		db_dstAdd = new JTextField();
		db_dstAdd.setColumns(10);
		
		JLabel lblNewLabel_6 = new JLabel("DESTINATION PORT");
		
		db_dstPort = new JTextField();
		db_dstPort.setColumns(10);
		
		JLabel lblNewLabel_7 = new JLabel("PROTOCOL");
		
		db_protocol = new JTextField();
		db_protocol.setColumns(10);
		
		db_id = new JTextField();
		db_id.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(frmPacketPrioritizationScheduler.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(19)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
									.addGap(176))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(12)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(btnAddSchedule)
										.addGroup(groupLayout.createSequentialGroup()
											.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addComponent(lblEndTime)
												.addComponent(lblDestinationAddress)
												.addComponent(lblNewLabel_3, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
												.addComponent(lblNewLabel_4)
												.addComponent(lblNewLabel_6)
												.addComponent(lblNewLabel_7))
											.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
											.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
													.addComponent(db_startTime)
													.addComponent(db_date)
													.addComponent(db_dstAdd)
													.addComponent(db_stopTime))
												.addComponent(db_dstPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(db_protocol, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
											.addGap(111))))
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
									.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
										.addComponent(lblNewLabel_2, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(btnDisplay))
									.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
										.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 247, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnDelete))))
							.addPreferredGap(ComponentPlacement.RELATED))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(28)
							.addComponent(lblNewLabel_5)
							.addGap(88)
							.addComponent(db_id, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(110)))
					.addGap(156))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_3)
						.addComponent(db_date, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_4)
						.addComponent(db_startTime, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEndTime)
						.addComponent(db_stopTime, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDestinationAddress)
						.addComponent(db_dstAdd, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_6)
						.addComponent(db_dstPort, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_7)
						.addComponent(db_protocol, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnAddSchedule)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(2)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel_2)
								.addComponent(btnDisplay))
							.addGap(18)
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel_5)
								.addComponent(db_id, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE))
							.addGap(18))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
							.addGap(21))))
		);
		frmPacketPrioritizationScheduler.getContentPane().setLayout(groupLayout);
	}
}
