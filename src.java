package eman01;

import java.io.FileOutputStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.*;

import java.awt.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.text.DateFormatter;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.event.*;


public class eman01 {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection connection = null;
		JOptionPane.showMessageDialog(null, "Welcome to Employee Management System Database");
		DriverRegistration();
		try {
			connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost", "project", "cse301");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		if (connection != null) {
			JOptionPane.showMessageDialog(null, "Database Connection Successful");
		} else {
			JOptionPane.showMessageDialog(null, "Database Connection Failed!");
			return;
		}
		
		Login(connection);
	}
	
	public static void DriverRegistration(){
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			return;
		}
		JOptionPane.showMessageDialog(null, "Oracle JDBC Driver Registered!");
	}
	
	public static void Login(Connection conn){         //Starting of Login function
	
		JFrame frame=new JFrame("Login Panel");
		JPanel panel=new JPanel();
		JLabel level=new JLabel("Username");
		JLabel level1=new JLabel("Password");
		JTextField text=new JTextField(20);
		JPasswordField password = new JPasswordField(20);
		JButton button=new JButton("Login");
		JButton button1=new JButton("Exit");
		panel.add(level);
		panel.add(text);
		panel.add(level1);
		panel.add(password);
		panel.add(button);
		panel.add(button1);
		frame.add(panel);
		frame.setSize(300, 250);
		frame.setVisible(true);
		button1.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		try {
					conn.close();
					JOptionPane.showMessageDialog(null, "Database Connection Closed. Good Bye!");
					System.exit(0);
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
	    		}
	    	});
		button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				Statement st;
				ResultSet rs;
				try{                                     //Checking admin and employee
				String user = text.getText().trim();
				String pass = password.getText().trim();
				text.setText("");
				password.setText("");
				String admin="admin";
				String apas="admin";
				if(user.equals(admin) && pass.equals(apas)){
					JOptionPane.showMessageDialog(null, "admin found");
					frame.setVisible(false);
					Mainmenu(conn);
				}
				
				else if(user.equals("") || pass.equals(""))
				{
					JOptionPane.showMessageDialog(null, "No input found");
				}
				
				else{
				String sql = "select E_id,E_pass from employee where E_id='"+user+"' and E_pass='"+pass+"'";
				st = conn.createStatement();
				rs = st.executeQuery(sql);
			    String a="", b="";
				while(rs.next())
				{
					a = rs.getString("E_id");
					b = rs.getString("E_pass");
				}
				if(a.equals(user) && b.equals(pass)){
					JOptionPane.showMessageDialog(null, "User found");
					String sql1="select * from employee where E_id='"+user+"' and E_pass='"+pass+"'";
					int o=0;
					ExecuteSQL(conn,sql1,o);
					frame.setVisible(false);
				}
				
				else{
					JOptionPane.showMessageDialog(null, "No user found");
				}
				
				}	
				
				}
				catch(SQLException e1){
					e1.printStackTrace();
				}
				
			}
		});
		
	}   //End of Login function
	
	
	public static void ExecuteSQL(Connection conn, String s, int o){    //Starting of ExecuteSQL function
		Vector<String> cnames = new Vector<String>();
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		try {
			Statement stmt = conn.createStatement() ;
			ResultSet rs = stmt.executeQuery(s);
			ResultSetMetaData rsmd = rs.getMetaData();
			Integer columnsNumber = rsmd.getColumnCount();
			for(int i=1; i<=columnsNumber; i++){
				cnames.addElement(rsmd.getColumnName(i));
			}
			while(rs.next()){
				Vector<String> row = new Vector<String>();
				for(int i=1; i<=columnsNumber; i++){
					row.addElement(rs.getString(i));
				}
				data.addElement(row);
			}
			if(o==1){                                     //Calling AdminDisplay
				AdminDisplayTable(data,cnames,conn,s);
			}
			else if(o==0){                               //Calling UserDisplay
				UserDisplayTable(data,cnames,conn,s); 
			}
			else if(o==2){                              //Just ExecuteSQL
				;
			}
			
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		
	}     //End of ExecuteSQL function
	
	
	
	public static void UserDisplayTable (Vector data, Vector cnames, Connection conn,String s){  //Starting of AdminDisplayTable function
		JFrame frame=new JFrame("User Information");
	    frame.setLayout(new FlowLayout());
		JPanel panel = new JPanel(new FlowLayout());
		JTable table = new JTable(data, cnames);
		JButton button=new JButton("Logout");
		JButton button1=new JButton("Print");
		JScrollPane pane =new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		frame.add(panel);
		panel.setLayout(new BorderLayout());
		frame.add(button);
		frame.add(button1);
		frame.setSize(500,550);
        panel.add(pane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	    button.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		Login(conn);                           //Going back to the login function 
	    		frame.setVisible(false);
	    		}
	    	});
	    
	    button1.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		try{                             //Calling Print function
	    			Print(conn,s);
	    		} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		frame.setVisible(false);
	    		JOptionPane.showMessageDialog(null, "Document Printed");
	    		}
	    	});
	}             //End of UserDisplayTable function
	
	public static void AdminDisplayTable (Vector data, Vector cnames, Connection conn,String s){  //Starting of AdminDisplayTable function
		JFrame frame=new JFrame("User Information");
	    frame.setLayout(new FlowLayout());
		JPanel panel = new JPanel(new FlowLayout());
		JTable table = new JTable(data, cnames);
		JButton button=new JButton("< Go Back");
		JButton button1=new JButton("Print");
		JScrollPane pane =new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		frame.add(panel);
		panel.setLayout(new BorderLayout());
		frame.add(button);
		frame.add(button1);
		frame.setSize(500,550);
        panel.add(pane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	    button.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {      //Going back to Mainmenu function
	    		Mainmenu(conn);
	    		frame.setVisible(false);
	    		}
	    	});
	    button1.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		try{                                         //Calling Print function
	    			Print(conn,s);
	    		} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		frame.setVisible(false);
	    		JOptionPane.showMessageDialog(null, "Document Printed");
	    		Mainmenu(conn);
	    		}
	    	});
	}   //End of AdminDisplayTable function
	
	
public static void Mainmenu(Connection conn){       //Starting of Mainmenu
		
	    String[] works ={"View Records","Insert Records","Update Records","Delete Records","Search Records"
	    		,"Enter SQL"};
	    final JComboBox comboBox=new JComboBox(works);
	    JPanel panel1 = new JPanel(new FlowLayout());
	    JLabel label1 = new JLabel(" Select your choice here ");
	    panel1.add(label1);
	    panel1.add(comboBox);
     
	    JButton button = new JButton("Select");
	    JButton button1 = new JButton("Logout");
	    JFrame frame=new JFrame("Main Menu");
	    frame.setLayout(new FlowLayout());
	    frame.add(panel1);
	    frame.add(button);
	    frame.add(button1);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(300,250);
	    frame.setVisible(true);
	    
	    button1.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {   //Going back to Login function
	    		Login(conn);
	    		frame.setVisible(false);
	    		}
	    	});
        
        button.addActionListener(new ActionListener() {
          	public void actionPerformed(ActionEvent e) {
          		System.out.println("Selected option " + comboBox.getSelectedIndex());
          		Integer choice= comboBox.getSelectedIndex();
          		if(choice==5){
          			Querymenu(conn);                 //Calling Querymenu function
    	            frame.setVisible(false);
          		}
          		else{
          			Secondmenu(choice,conn);       //Calling Secondmenu function
    	            frame.setVisible(false);
          		}
				
          	}
	});

	}        //End of Mainmenu


public static void Secondmenu(int choice1, Connection conn){     //Starting of Secondmenu
	
    String[] works1 ={"Employee information","Department information","Project information","Overtime information"};
    final JComboBox comboBox=new JComboBox(works1);
    JPanel panel1 = new JPanel(new FlowLayout());
    JLabel label1 = new JLabel(" Select your choice here ");
    panel1.add(label1);
    panel1.add(comboBox);
 
    JButton button = new JButton("Back");
    JButton button1 = new JButton("Select");
    JFrame frame=new JFrame("Second Menu");
    frame.setLayout(new FlowLayout());
    frame.add(panel1);
    frame.add(button);
    frame.add(button1);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(350,250);
    frame.setVisible(true);
    
    button.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		Mainmenu(conn);                       //Going back to Mainmenu
    		frame.setVisible(false);
    		}
    	});
    
    button1.addActionListener(new ActionListener() {
      	public void actionPerformed(ActionEvent e) {
      		System.out.println("Selected option " + comboBox.getSelectedIndex());
      		Integer choice2= comboBox.getSelectedIndex();
      		String query="";
      		try {
				checkdate(conn);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(choice1==0 && choice2==0){         //Display All info of employee
				tot_sal1(conn);                   //Calling tot_sal1 function
				query = "select * from employee";
				int o=1;
				ExecuteSQL(conn, query, o);       //Calling ExecuteSQL function
				frame.setVisible(false);
			}
			else if(choice1==0 && choice2==1){     //Display all info of department
				query = "select * from department";
				int o=1;
				ExecuteSQL(conn, query, o);         //Calling ExecuteSQL function
				frame.setVisible(false);
			}
			else if(choice1==0 && choice2==2){     //Display all info of project
				query = "select * from project natural join dept_project";
				int o=1;
				ExecuteSQL(conn, query, o);         //Calling ExecuteSQL function
				frame.setVisible(false);
			}
			else if(choice1==0 && choice2==3){     //Display all info of overtime
				query = "select * from overtime natural join overtime_employee";
				int o=1;
				ExecuteSQL(conn, query, o);
				frame.setVisible(false);
			}
			else if(choice1==1 && choice2==0){    //Insert in Employee table
				Integer i=0;
				InsertMenu(i,conn);                //Calling insert function
				frame.setVisible(false);
			}
			else if(choice1==1 && choice2==1){    //Insert in Department table
				Integer i=1;
				InsertMenu(i,conn);                //Calling insert function
				frame.setVisible(false);
			}
			else if(choice1==1 && choice2==2){    //Insert in Project table
				Integer i=2;
				InsertMenu(i,conn);               //Calling insert function
				frame.setVisible(false);
			}
			else if(choice1==1 && choice2==3){    //Insert in Overtime table
				Integer i=3;
				InsertMenu(i,conn);                //Calling insert function
				frame.setVisible(false);
			}
			else if(choice1==2 && choice2==0){     //Update in Employee table
				int i=0;
				UpdateMenu(i,conn);                 //Calling update function
				frame.setVisible(false);
			}
			else if(choice1==2 && choice2==1){    //Update in Department table
				int i=1;
				UpdateMenu(i,conn);               //Calling update function
				frame.setVisible(false);
			}
			else if(choice1==2 && choice2==2){    //Update in Project table
				int i=2;
				UpdateMenu(i,conn);               //Calling update function
				frame.setVisible(false);
			}
			else if(choice1==2 && choice2==3){    //Update in Overtime table
				int i=3;
				UpdateMenu(i,conn);               //Calling update function
				frame.setVisible(false);
			}
			else if(choice1==3 && choice2==0){   //Delete  from employee table
				int i=0;
				deleteMenu(i,conn);                //Calling delete function
				frame.setVisible(false);
			}
			else if(choice1==3 && choice2==1){    //Delete  from department table
				int i=1;
				deleteMenu(i,conn);               //Calling delete function
				frame.setVisible(false);
			}
			else if(choice1==3 && choice2==2){    //Delete  from project table
				int i=2;
				deleteMenu(i,conn);               //Calling delete function
				frame.setVisible(false);
			}
			else if(choice1==3 && choice2==3){    //Delete  from overtime table
				int i=3;
				deleteMenu(i,conn);                 //Calling delete function
				frame.setVisible(false);
			}
			else if(choice1==4 && choice2==0){       //Search  from employee table
				Semployee(conn);
				frame.setVisible(false);
			}
			else if(choice1==4 && choice2==1){        //Search  from department table
				Sdepartment(conn);
				frame.setVisible(false);
			}
			else if(choice1==4 && choice2==2){          //Search  from project table
				Sproject(conn);
				frame.setVisible(false);
			}
			else if(choice1==4 && choice2==3){         //Search  from overtime table
				Sovertime(conn);
				frame.setVisible(false);
			}
			
          
      	}
});

} //End of Secondmenu


  public static void InsertMenu(int i,Connection conn){  //Starting of Insertmenu 
		if(i==0){
			String[] empstr={"Male","Female","Other"};
			String[] emptstr={"Dept. Head","Officer","Worker"};
			JFrame framee=new JFrame("Input Employee Info");
			JPanel panele=new JPanel();
			JComboBox texte3=new JComboBox(empstr);
			JComboBox texte6=new JComboBox(emptstr);
			DateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
			DateFormatter df = new DateFormatter(format);
			
			JLabel levele=new JLabel("Employee ID");
			JLabel levele1=new JLabel("Employee Name");
			JLabel levele2=new JLabel("Employee Address");
			JLabel levele3=new JLabel("Employee Sex");
			JLabel levele4=new JLabel("Employee Join Date (dd-MMM-yyyy)");
			JLabel levele5=new JLabel("Employee Contact");
			JLabel levele6=new JLabel("Employee Type");
			JLabel levele7=new JLabel("Employee Department-Id");
			JLabel levele8=new JLabel("Employee Project-Id");
			JLabel levele9=new JLabel("Employee Salary");
			JLabel levele10=new JLabel("Employee Password");
			JTextField texte=new JTextField("",20);
			JTextField texte1=new JTextField("",20);
			JTextField texte2=new JTextField("",20);
			//JTextField texte3=new JTextField("",20);
			JFormattedTextField dateField1 = new JFormattedTextField(df);
			dateField1.setPreferredSize(new Dimension(225, 20));
			JTextField texte5=new JTextField("",20);
			//JTextField texte6=new JTextField("",20);
			JTextField texte7=new JTextField("",20);
			JTextField texte8=new JTextField("",20);
			JTextField texte9=new JTextField("",20);
			//JTextField texte10=new JTextField("",20);
			JPasswordField password = new JPasswordField(20);
			
			JButton buttone=new JButton("OK");
			JButton buttone1=new JButton("Go to previous menu");
			panele.add(levele);
			panele.add(texte);
			panele.add(levele1);
			panele.add(texte1);
			panele.add(levele2);
			panele.add(texte2);
			panele.add(levele3);
			panele.add(texte3);
			panele.add(levele4);
			panele.add(dateField1);
			panele.add(levele5);
			panele.add(texte5);
			panele.add(levele6);
			panele.add(texte6);	
			panele.add(levele7);
			panele.add(texte7);
			panele.add(levele8);
			panele.add(texte8);
			panele.add(levele9);
			panele.add(texte9);
			panele.add(levele10);
			panele.add(password);
			panele.add(buttone);
			panele.add(buttone1);
			framee.add(panele);
			framee.setSize(300, 580);
			framee.setVisible(true);
			buttone1.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) { //Going back to mainmenu
					framee.setVisible(false);
					Mainmenu(conn);
					
				}
			});
			buttone.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					
					String d="Dept. Head";
					String b=texte7.getText();
					String c= (String) texte6.getSelectedItem();
					if(d.equals(c)){               //Checking Dept. Head 
						String a = "select * from employee where dept_id='"+b+"' and E_type='"+c+"'" ;
						System.out.println(a);
						Statement st;
						ResultSet rs;
						
						try{
							boolean flag = false;
							st = conn.createStatement();
							rs = st.executeQuery(a);
						    
							while(rs.next())
							{
									flag= true;
							}
							if(flag){
								
								JOptionPane.showMessageDialog(null, "Dept. Head already exists");
							}
							
							
							else{        //inserting in employee table
								String str= "insert into employee values ('" + texte.getText() + "','" +
									    texte1.getText()+ "','"+texte3.getSelectedItem()+"','" + texte2.getText() + "','"
									    +dateField1.getText()+"','"+texte5.getText()+"','"+texte6.getSelectedItem()+"','"
									    +texte7.getText()+"','"+texte9.getText()+"','"+texte9.getText()+"','"+password.getText()+"')";
										System.out.println(str);
										String str1= "insert into project_employee values ('" + texte8.getText() + "','" +
											    texte.getText()+ "')";
										int o=2;
										System.out.println(str1);
										ExecuteSQL(conn,str,o);
										ExecuteSQL(conn,str1,o);
										framee.setVisible(false);
										//System.out.println("Successfully Executed");
										//JOptionPane.showMessageDialog(null, "Successfully Executed");
										Mainmenu(conn);
							}
							
							rs.close();
							st.close();
						}catch(SQLException e1){
							e1.printStackTrace();
						}
						
						
					}
					
					
					else{              //inserting in employee table
						String str= "insert into employee values ('" + texte.getText() + "','" +
							    texte1.getText()+ "','"+texte3.getSelectedItem()+"','" + texte2.getText() + "','"
							    +dateField1.getText()+"','"+texte5.getText()+"','"+texte6.getSelectedItem()+"','"
							    +texte7.getText()+"','"+texte9.getText()+"','"+texte9.getText()+"','"+password.getText()+"')";
								System.out.println(str);
								String str1= "insert into project_employee values ('" + texte8.getText() + "','" +
									    texte.getText()+ "')";
								int o=2;
								System.out.println(str1);
								ExecuteSQL(conn,str,o);
								ExecuteSQL(conn,str1,o);
								framee.setVisible(false);
								//System.out.println("Successfully Executed");
								//JOptionPane.showMessageDialog(null, "Successfully Executed");
								Mainmenu(conn);
					}
					
										
					
				}
			});		
			
			
		}
		
		else if(i==1){                //inserting in department table
			JFrame framedept=new JFrame("Departmet Information");
			JPanel paneldept=new JPanel();
			JLabel lebeldept=new JLabel("Department ID");
			JLabel lebeldept1=new JLabel("Department Name");
			JLabel lebeldept2=new JLabel("Department Building");
			JLabel lebeldept3=new JLabel("Department Budget");
			
			JTextField textdept=new JTextField("",20);
			JTextField textdept1=new JTextField("",20);
			JTextField textdept2=new JTextField("",20);
			JTextField textdept3=new JTextField("",20);
			
			JButton buttondept=new JButton("OK");
			JButton buttondept1=new JButton("Go to previous menu");
			buttondept1.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					framedept.setVisible(false);
					Mainmenu(conn);
					
				}
			});
			buttondept.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					String strdept="insert into department values ('" + textdept.getText() + "','" + 
				    textdept1.getText()+ "','" + textdept2.getText() + "','"+textdept3.getText()+"')";
					int o=2;
				
					System.out.println(strdept);
					ExecuteSQL(conn,strdept,o);
					framedept.setVisible(false);
					//System.out.println("Successfully Executed");
					//JOptionPane.showMessageDialog(null, "Successfully Executed");
					Mainmenu(conn);
				}
			});
			
			paneldept.add(lebeldept);
			paneldept.add(textdept);
			paneldept.add(lebeldept1);
			paneldept.add(textdept1);
			paneldept.add(lebeldept2);
			paneldept.add(textdept2);
			paneldept.add(lebeldept3);
			paneldept.add(textdept3);
			paneldept.add(buttondept);
			paneldept.add(buttondept1);
			framedept.add(paneldept);
			framedept.setSize(300, 350);
			framedept.setVisible(true);
		}
		
		else if(i==2){             //inserting in project table
			JFrame framep=new JFrame("Project Information");
			JPanel panelp=new JPanel();
			JLabel levelp=new JLabel("Project ID");
			JLabel levelp6=new JLabel("Department ID");
			JLabel levelp1=new JLabel("Project Name");
			JLabel levelp3=new JLabel("Project Budget");
			JLabel levelp4=new JLabel("Project Start Date (dd-MMM-yyyy)");
			JLabel levelp5=new JLabel("Project End Date (dd-MMM-yyyy)");
			
			JTextField textp=new JTextField("",20);
			JTextField textp1=new JTextField("",20);
			JTextField textp3=new JTextField("",20);
			JTextField textp5=new JTextField("",20);
			JTextField textp6=new JTextField("",20);
			
			JComboBox<String> dept = new JComboBox<String>();
			
			try{
				Statement stmt = conn.createStatement() ;
				ResultSet rs = stmt.executeQuery("select dept_id from department");
				while(rs.next()){
					dept.addItem(rs.getString(1));
				}
			}catch(Exception e2){
				e2.printStackTrace();
			}
			
			
			
			JButton buttonp=new JButton("OK");
			JButton buttonp1=new JButton("Go To Previous Menu");
			buttonp1.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					framep.setVisible(false);
					Mainmenu(conn);
					
				}
			});
			
			buttonp.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					String dateStart = textp5.getText();
					String dateStop = textp6.getText();

					SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");

					Date d1 = null;
					Date d2 = null;
					long diffDays=0;

					try {      // Calculating project duration
						d1 = format.parse(dateStart);
						System.out.println(d1);
						d2 = format.parse(dateStop);
						long diff = d2.getTime() - d1.getTime();
					    diffDays =(1+ ( diff / (24 * 60 * 60 * 1000)));
						System.out.print(diffDays + " days, ");

					} catch (Exception e2) {
						e2.printStackTrace();
					}
					String strp= "insert into project values ('" + textp.getText() + "','" + textp1.getText()+ "'," + diffDays + ","+textp3.getText()+",'"+ textp5.getText()+"','"+textp6.getText()+"')";
					String strp1="insert into dept_project values ('" + dept.getSelectedItem().toString() + "','" +textp.getText() + "')";
					int o=2;
					System.out.println(strp);
					ExecuteSQL(conn,strp,o);
					ExecuteSQL(conn,strp1,o);
					//System.out.println("Successfully Executed");
					//JOptionPane.showMessageDialog(null, "Successfully Executed");
					framep.setVisible(false);
					Mainmenu(conn);
				}
			});
			
			panelp.add(levelp);
			panelp.add(textp);
			panelp.add(levelp6);
			panelp.add(dept);
			panelp.add(levelp1);
			panelp.add(textp1);
			panelp.add(levelp3);
			panelp.add(textp3);
			panelp.add(levelp4);
			panelp.add(textp5);
			panelp.add(levelp5);
			panelp.add(textp6);
			panelp.add(buttonp);
			panelp.add(buttonp1);
			framep.add(panelp);
			framep.setSize(300, 400);
			framep.setVisible(true);
		}
		
		else if(i==3){              //inserting in overtime table
			JFrame frameo=new JFrame("Overtime Information");
			JPanel panelo=new JPanel();
			DateFormat formato1=new SimpleDateFormat("dd-MMM-yyyy");
			DateFormatter dfo1=new DateFormatter(formato1);
			JLabel lebelo=new JLabel("Employee ID");
			JLabel lebelo1=new JLabel("Overtime ID");
			JLabel lebelo2=new JLabel("Hourly Wage");
			JLabel lebelo3=new JLabel("Overtime Duration(Hours)");
			JLabel lebelo4=new JLabel("Overtime Date (dd-MMM-yyyy)");
			
			JTextField texto=new JTextField("",20);
			JTextField texto1=new JTextField("",20);
			JTextField texto2=new JTextField("",20);
			JTextField texto3=new JTextField("",20);
			JFormattedTextField dateFieldo1 = new JFormattedTextField(dfo1);
			dateFieldo1.setPreferredSize(new Dimension(225,20));
			JButton buttono=new JButton("OK");
			JButton buttono1=new JButton("Go to previous Menu");
			
			
			buttono1.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					frameo.setVisible(false);
					Mainmenu(conn);
					
				}
			});
			
			
			buttono.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					String stro="insert into overtime_employee values ('" + texto.getText() + "','" + texto1.getText()+ "',"+texto3.getText()+",'"+dateFieldo1.getText()+"')";
					System.out.println(stro);
					
					String stro1="insert into overtime values ('" + texto1.getText() + "','" + texto2.getText()+ "')";
					
					int o=2;
					ExecuteSQL(conn,stro,o);
					ExecuteSQL(conn,stro1,o);
					
					/*String em= texto.getText();
					String hw=texto2.getText();
					tot_sal(conn,em,hw);*/
					frameo.setVisible(false);
					//System.out.println("Successfully Executed");
					//JOptionPane.showMessageDialog(null, "Successfully Executed");
					Mainmenu(conn);
				}
			});
			
			panelo.add(lebelo);
			panelo.add(texto);
			panelo.add(lebelo1);
			panelo.add(texto1);
			panelo.add(lebelo2);
			panelo.add(texto2);
			panelo.add(lebelo3);
			panelo.add(texto3);
			panelo.add(lebelo4);
			panelo.add(dateFieldo1);
			panelo.add(buttono);
			panelo.add(buttono1);
			frameo.add(panelo);
			frameo.setSize(300, 310);
			frameo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frameo.setVisible(true);
		}
	  
  } //End of insertmenu
  
  public static void UpdateMenu(int i, Connection conn){  // Starting of updatemenu
	  if(i==0){
		  String[] att ={"E_id","E_name","E_address","E_sex"
					, "E_join_date","E_contact","E_type","Dept_id"
					   ,"P_id","E_salary","E_pass"};
		    final JComboBox comboBox1=new JComboBox(att);
		    JPanel panel1 = new JPanel(new FlowLayout());
		    JPanel panel2 = new JPanel(new FlowLayout());
		    JPanel panel3 = new JPanel(new FlowLayout());
		    JLabel label1 = new JLabel("Select an Attribute     ");
		    JLabel label2 = new JLabel("New Value (use ' ' for string) ");
		    JLabel label3 = new JLabel("Where (complete expression)");
		    JTextField texta=new JTextField("",20);
			JTextField texta1=new JTextField("",20);
			panel1.add(label1);
			panel1.add(comboBox1);
			panel2.add(label2);
			panel2.add(texta);
			panel3.add(label3);
		    panel3.add(texta1);
		    
		    JButton button = new JButton("Back to Mainmenu");
		    JButton button1 = new JButton("Select");
		    JFrame frame=new JFrame("Update Menu");
            button.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					frame.setVisible(false);
					Mainmenu(conn);
					
				}
			});
			
			button1.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					String s="",s1="",s2="";
					int a=comboBox1.getSelectedIndex();
					int o=2;
					if(a==0){      
						if(texta1.getText()!=null && texta1.getText().length()>0){
							s = "update employee set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
							System.out.println(s);
							s1 = "update project_employee set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
							s2 = "update overtime_employee set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
							System.out.println(s1);
							System.out.println(s2);
							ExecuteSQL(conn,s,o);
							ExecuteSQL(conn,s1,o);
							ExecuteSQL(conn,s2,o);
							frame.setVisible(false);
							Mainmenu(conn);
						
						}
						else{
							s = "update employee set " + comboBox1.getSelectedItem().toString() + " = " + texta;
							System.out.println(s);
							s1 = "update project_employee set " + comboBox1.getSelectedItem().toString() + " = " + texta;
							s2 = "update overtime_employee set " + comboBox1.getSelectedItem().toString() + " = " + texta;
							System.out.println(s1);
							System.out.println(s2);
							ExecuteSQL(conn,s,o);
							ExecuteSQL(conn,s1,o);
							ExecuteSQL(conn,s2,o);
							frame.setVisible(false);
							Mainmenu(conn);
						}
					}
					
					else if(a==7){
						if(texta1.getText()!=null && texta1.getText().length()>0){
								s = "update employee set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
								System.out.println(s);
								s1 = "update department set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
								s2 = "update dept_project set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
								System.out.println(s1);
								System.out.println(s2);
								ExecuteSQL(conn,s,o);
								ExecuteSQL(conn,s1,o);
								ExecuteSQL(conn,s2,o);
								frame.setVisible(false);
								Mainmenu(conn);
							
							}
							else{
								s = "update employee set " + comboBox1.getSelectedItem().toString() + " = " + texta;
								System.out.println(s);
								s1 = "update department set " + comboBox1.getSelectedItem().toString() + " = " + texta;
								s2 = "update dept_project set " + comboBox1.getSelectedItem().toString() + " = " + texta;
								System.out.println(s1);
								System.out.println(s2);
								ExecuteSQL(conn,s,o);
								ExecuteSQL(conn,s1,o);
								ExecuteSQL(conn,s2,o);
								frame.setVisible(false);
								Mainmenu(conn);
							}
						}
						
						else if(a==8){
							if(texta1.getText()!=null && texta1.getText().length()>0){
								s1 = "update project set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
								s2 = "update project_employee set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
								System.out.println(s1);
								System.out.println(s2);
								ExecuteSQL(conn,s1,o);
								ExecuteSQL(conn,s2,o);
								frame.setVisible(false);
								Mainmenu(conn);
							}
							
							else{
								s1 = "update project set " + comboBox1.getSelectedItem().toString() + " = " + texta;
								s2 = "update project_employee set " + comboBox1.getSelectedItem().toString() + " = " + texta;
								System.out.println(s1);
								System.out.println(s2);
								ExecuteSQL(conn,s,o);
								ExecuteSQL(conn,s1,o);
								ExecuteSQL(conn,s2,o);
								frame.setVisible(false);
								Mainmenu(conn);
							}
						}
					  
						else{
							if(texta1.getText()!=null && texta1.getText().length()>0){
								s = "update employee set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
								System.out.println(s);
								ExecuteSQL(conn,s,o);
								frame.setVisible(false);
								Mainmenu(conn);
							
							}
							else{
								s = "update employee set " + comboBox1.getSelectedItem().toString() + " = " + texta;
								System.out.println(s);
								ExecuteSQL(conn,s,o);
								frame.setVisible(false);
								Mainmenu(conn);
							}
						}
					
					
						
					}
					
			});
		      
		    frame.setLayout(new FlowLayout());
		    frame.add(panel1);
		    frame.add(panel2);
		    frame.add(panel3);
		    frame.add(button);
		    frame.add(button1);
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    frame.setSize(430,250);
		    frame.setVisible(true);
	  }
	  
	  else if(i==1){
		  String[] att ={"Dept_id","Dept_name","Dept_building","D_budget"};
		    final JComboBox comboBox1=new JComboBox(att);
		    JPanel panel1 = new JPanel(new FlowLayout());
		    JPanel panel2 = new JPanel(new FlowLayout());
		    JPanel panel3 = new JPanel(new FlowLayout());
		    JLabel label1 = new JLabel("Select an Attribute     ");
		    JLabel label2 = new JLabel("New Value (use ' ' for string) ");
		    JLabel label3 = new JLabel("Where (complete expression)");
		    JTextField texta=new JTextField("",20);
			JTextField texta1=new JTextField("",20);
			panel1.add(label1);
			panel1.add(comboBox1);
			panel2.add(label2);
			panel2.add(texta);
			panel3.add(label3);
		    panel3.add(texta1);
		    
		    JButton button = new JButton("Back to Mainmenu");
		    JButton button1 = new JButton("Select");
		    JFrame frame=new JFrame("Update Menu");
          button.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					frame.setVisible(false);
					Mainmenu(conn);
					
				}
			});
			
			button1.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					String s="",s1="",s2="";
					int a=comboBox1.getSelectedIndex();
					int o=2;
					if(a==0){
						if(texta1.getText()!=null && texta1.getText().length()>0){
							s = "update employee set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
							System.out.println(s);
							s1 = "update department set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
							s2 = "update dept_project set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
							System.out.println(s1);
							System.out.println(s2);
							ExecuteSQL(conn,s,o);
							ExecuteSQL(conn,s1,o);
							ExecuteSQL(conn,s2,o);
							frame.setVisible(false);
							Mainmenu(conn);
						
						}
						else{
							s = "update employee set " + comboBox1.getSelectedItem().toString() + " = " + texta;
							System.out.println(s);
							s1 = "update department set " + comboBox1.getSelectedItem().toString() + " = " + texta;
							s2 = "update dept_project set " + comboBox1.getSelectedItem().toString() + " = " + texta;
							System.out.println(s1);
							System.out.println(s2);
							ExecuteSQL(conn,s,o);
							ExecuteSQL(conn,s1,o);
							ExecuteSQL(conn,s2,o);
							frame.setVisible(false);
							Mainmenu(conn);
						}
					}
						
					  
						else{
							if(texta1.getText()!=null && texta1.getText().length()>0){
								s = "update department set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
								System.out.println(s);
								ExecuteSQL(conn,s,o);
								frame.setVisible(false);
								Mainmenu(conn);
							
							}
							else{
								s = "update department set " + comboBox1.getSelectedItem().toString() + " = " + texta;
								System.out.println(s);
								ExecuteSQL(conn,s,o);
								frame.setVisible(false);
								Mainmenu(conn);
							}
						}
								
						
					}
					
			});
		      
		    frame.setLayout(new FlowLayout());
		    frame.add(panel1);
		    frame.add(panel2);
		    frame.add(panel3);
		    frame.add(button);
		    frame.add(button1);
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    frame.setSize(430,250);
		    frame.setVisible(true);
	  }
    
	  else if(i==2){
		  String[] att ={"P_id","Dept_id","P_name","P_duration","P_budget"
					, "P_sdate","P_edate"};
		    final JComboBox comboBox1=new JComboBox(att);
		    JPanel panel1 = new JPanel(new FlowLayout());
		    JPanel panel2 = new JPanel(new FlowLayout());
		    JPanel panel3 = new JPanel(new FlowLayout());
		    JLabel label1 = new JLabel("Select an Attribute     ");
		    JLabel label2 = new JLabel("New Value (use ' ' for string) ");
		    JLabel label3 = new JLabel("Where (complete expression)");
		    JTextField texta=new JTextField("",20);
			JTextField texta1=new JTextField("",20);
			panel1.add(label1);
			panel1.add(comboBox1);
			panel2.add(label2);
			panel2.add(texta);
			panel3.add(label3);
		    panel3.add(texta1);
		    
		    JButton button = new JButton("Back to Mainmenu");
		    JButton button1 = new JButton("Select");
		    JFrame frame=new JFrame("Update Menu");
          button.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					frame.setVisible(false);
					Mainmenu(conn);
					
				}
			});
			
			button1.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					String s="",s1="",s2="";
					int a=comboBox1.getSelectedIndex();
					int o=2;
					if(a==0){
						if(texta1.getText()!=null && texta1.getText().length()>0){
							s = "update project set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
							System.out.println(s);
							s1 = "update project_employee set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
							s2 = "update dept_project set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
							System.out.println(s1);
							System.out.println(s2);
							ExecuteSQL(conn,s,o);
							ExecuteSQL(conn,s1,o);
							ExecuteSQL(conn,s2,o);
							frame.setVisible(false);
							Mainmenu(conn);
						
						}
						else{
							s = "update project set " + comboBox1.getSelectedItem().toString() + " = " + texta;
							System.out.println(s);
							s1 = "update project_employee set " + comboBox1.getSelectedItem().toString() + " = " + texta;
							s2 = "update dept_project set " + comboBox1.getSelectedItem().toString() + " = " + texta;
							System.out.println(s1);
							System.out.println(s2);
							ExecuteSQL(conn,s,o);
							ExecuteSQL(conn,s1,o);
							ExecuteSQL(conn,s2,o);
							frame.setVisible(false);
							Mainmenu(conn);
						}
					}
					
					else if(a==1){
						if(texta1.getText()!=null && texta1.getText().length()>0){
								s = "update employee set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
								System.out.println(s);
								s1 = "update department set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
								s2 = "update dept_project set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
								System.out.println(s1);
								System.out.println(s2);
								ExecuteSQL(conn,s,o);
								ExecuteSQL(conn,s1,o);
								ExecuteSQL(conn,s2,o);
								frame.setVisible(false);
								Mainmenu(conn);
							
							}
							else{
								s = "update employee set " + comboBox1.getSelectedItem().toString() + " = " + texta;
								System.out.println(s);
								s1 = "update department set " + comboBox1.getSelectedItem().toString() + " = " + texta;
								s2 = "update dept_project set " + comboBox1.getSelectedItem().toString() + " = " + texta;
								System.out.println(s1);
								System.out.println(s2);
								ExecuteSQL(conn,s,o);
								ExecuteSQL(conn,s1,o);
								ExecuteSQL(conn,s2,o);
								frame.setVisible(false);
								Mainmenu(conn);
							}
						}
						
					  
						else{
							if(texta1.getText()!=null && texta1.getText().length()>0){
								s = "update project set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
								System.out.println(s);
								ExecuteSQL(conn,s,o);
								frame.setVisible(false);
								Mainmenu(conn);
							
							}
							else{
								s = "update project set " + comboBox1.getSelectedItem().toString() + " = " + texta;
								System.out.println(s);
								ExecuteSQL(conn,s,o);
								frame.setVisible(false);
								Mainmenu(conn);
							}
						}
					
					
						
					}
					
			});
		      
		    frame.setLayout(new FlowLayout());
		    frame.add(panel1);
		    frame.add(panel2);
		    frame.add(panel3);
		    frame.add(button);
		    frame.add(button1);
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    frame.setSize(430,250);
		    frame.setVisible(true);
	  
	  }
	  
	  else if(i==3){
		  String[] att ={"E_id","O_id","hourly_wage","O_duration","O_date"};
		    final JComboBox comboBox1=new JComboBox(att);
		    JPanel panel1 = new JPanel(new FlowLayout());
		    JPanel panel2 = new JPanel(new FlowLayout());
		    JPanel panel3 = new JPanel(new FlowLayout());
		    JLabel label1 = new JLabel("Select an Attribute     ");
		    JLabel label2 = new JLabel("New Value (use ' ' for string) ");
		    JLabel label3 = new JLabel("Where (complete expression)");
		    JTextField texta=new JTextField("",20);
			JTextField texta1=new JTextField("",20);
			panel1.add(label1);
			panel1.add(comboBox1);
			panel2.add(label2);
			panel2.add(texta);
			panel3.add(label3);
		    panel3.add(texta1);
		    
		    JButton button = new JButton("Back to Mainmenu");
		    JButton button1 = new JButton("Select");
		    JFrame frame=new JFrame("Overtime Update Menu");
          button.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					frame.setVisible(false);
					Mainmenu(conn);
					
				}
			});
			
			button1.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					String s="",s1="",s2="";
					int a=comboBox1.getSelectedIndex();
					int o=2;
					if(a==0){
						if(texta1.getText()!=null && texta1.getText().length()>0){
							s = "update employee set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
							System.out.println(s);
							s1 = "update project_employee set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
							s2 = "update overtime_employee set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
							System.out.println(s1);
							System.out.println(s2);
							ExecuteSQL(conn,s,o);
							ExecuteSQL(conn,s1,o);
							ExecuteSQL(conn,s2,o);
							frame.setVisible(false);
							Mainmenu(conn);
						
						}
						else{
							s = "update employee set " + comboBox1.getSelectedItem().toString() + " = " + texta;
							System.out.println(s);
							s1 = "update project_employee set " + comboBox1.getSelectedItem().toString() + " = " + texta;
							s2 = "update overtime_employee set " + comboBox1.getSelectedItem().toString() + " = " + texta;
							System.out.println(s1);
							System.out.println(s2);
							ExecuteSQL(conn,s,o);
							ExecuteSQL(conn,s1,o);
							ExecuteSQL(conn,s2,o);
							frame.setVisible(false);
							Mainmenu(conn);
						}
					}
					
					else if(a==1){
						if(texta1.getText()!=null && texta1.getText().length()>0){
								s = "update overtime_employee set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
								System.out.println(s);
								s1 = "update overtime set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
								System.out.println(s1);
								ExecuteSQL(conn,s,o);
								ExecuteSQL(conn,s1,o);
								frame.setVisible(false);
								Mainmenu(conn);
							
							}
							else{
								s = "update overtime_employee set " + comboBox1.getSelectedItem().toString() + " = " + texta;
								System.out.println(s);
								s1 = "update overtime set " + comboBox1.getSelectedItem().toString() + " = " + texta;
								System.out.println(s1);
								ExecuteSQL(conn,s,o);
								ExecuteSQL(conn,s1,o);
								frame.setVisible(false);
								Mainmenu(conn);
							}
						}
						
					  
						else{
							if(texta1.getText()!=null && texta1.getText().length()>0){
								s = "update overtime_employee set " + comboBox1.getSelectedItem().toString() + " = " + texta.getText() + " where " + texta1.getText();
								System.out.println(s);
								ExecuteSQL(conn,s,o);
								frame.setVisible(false);
								Mainmenu(conn);
							
							}
							else{
								s = "update overtime_employee set " + comboBox1.getSelectedItem().toString() + " = " + texta;
								System.out.println(s);
								ExecuteSQL(conn,s,o);
								frame.setVisible(false);
								Mainmenu(conn);
							}
						}
					
					
						
					}
					
			});
		      
		    frame.setLayout(new FlowLayout());
		    frame.add(panel1);
		    frame.add(panel2);
		    frame.add(panel3);
		    frame.add(button);
		    frame.add(button1);
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    frame.setSize(430,250);
		    frame.setVisible(true);
	  
	  }
	  
  }  //End of updatemenu
  
  
  public static void Semployee(Connection conn){       //Starting of search for employee table function
		JFrame framesemp=new JFrame("Search Employee");
		JPanel panelsemp=new JPanel();
		JTextField textsemp=new JTextField("",30);
		JLabel lebelsemp=new JLabel("Enter the complete Where cluse");
		JButton butttonsemp=new JButton("Select");
		JButton buttonsemp1=new JButton("<Back");
		buttonsemp1.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				framesemp.setVisible(false);
				Mainmenu(conn);
			}
		});
		
		butttonsemp.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String strsemp = "select * from employee where " + textsemp.getText();
				System.out.println(strsemp);
				int o=1;
				ExecuteSQL(conn,strsemp,o);
				framesemp.setVisible(false);
				
			}
		});
		
		panelsemp.add(lebelsemp);
		panelsemp.add(textsemp);
		panelsemp.add(buttonsemp1);
		panelsemp.add(butttonsemp);
		framesemp.add(panelsemp);
		framesemp.setSize(400, 150);
		framesemp.setVisible(true);
		
		
	}//End of search for employee table function
  
  public static void Sdepartment(Connection conn){      //Starting of search for department table function
		JFrame framesdep=new JFrame("Search Department");
		JPanel panelsdep=new JPanel();
		JTextField textsdep=new JTextField("",30);
		JLabel lebelsdep=new JLabel("Enter the complete Where cluse");
		JButton butttonsdep=new JButton("Select");
		JButton buttonsdep1=new JButton("<Back");
		buttonsdep1.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				framesdep.setVisible(false);
				Mainmenu(conn);
			}
		});
		
		butttonsdep.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String strsdep = "select * from department where " + textsdep.getText();
				System.out.println(strsdep);
				int o=1;
				ExecuteSQL(conn,strsdep,o);
				framesdep.setVisible(false);
			}
		});
		
		panelsdep.add(lebelsdep);
		panelsdep.add(textsdep);
		panelsdep.add(buttonsdep1);
		panelsdep.add(butttonsdep);
		framesdep.add(panelsdep);
		framesdep.setSize(400, 150);
		framesdep.setVisible(true);
		
		
	}//End of search for employee table function
  
    public static void Sproject(Connection conn){  //Starting of search for project table function
		
		JFrame framespro=new JFrame("Search Project");
		JPanel panelspro=new JPanel();
		JTextField textspro=new JTextField("",30);
		JLabel lebelspro=new JLabel("Enter the complete Where cluse");
		JButton butttonspro=new JButton("Select");
		JButton buttonspro1=new JButton("<Back");
		buttonspro1.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				framespro.setVisible(false);
				Mainmenu(conn);
			}
		});
		
		butttonspro.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String strspro = "select * from Project where " + textspro.getText();
				System.out.println(strspro);
				int o=1;
				ExecuteSQL(conn,strspro,o);
				framespro.setVisible(false);
			}
		});
		
		panelspro.add(lebelspro);
		panelspro.add(textspro);
		panelspro.add(buttonspro1);
		panelspro.add(butttonspro);
		framespro.add(panelspro);
		framespro.setSize(400, 150);
		framespro.setVisible(true);
			
	}//End of search for project table function
    
      public static void Sovertime(Connection conn){  //Starting of search for overtime table function
		
		JFrame framesover=new JFrame("Search Overtime");
		JPanel panelover=new JPanel();
		JTextField textsover=new JTextField("",30);
		JLabel lebelsover=new JLabel("Enter the complete Where cluse");
		JButton butttonsover=new JButton("Select");
		JButton buttonsover1=new JButton("<Back");
		buttonsover1.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				framesover.setVisible(false);
				Mainmenu(conn);
			}
		});
		
		butttonsover.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String strsover = "select * from overtime natural join overtime_employee where " + textsover.getText();
				System.out.println(strsover);
				int o=1;
				ExecuteSQL(conn,strsover,o);
				framesover.setVisible(false);
			}
		});
		
		panelover.add(lebelsover);
		panelover.add(textsover);
		panelover.add(buttonsover1);
		panelover.add(butttonsover);
		framesover.add(panelover);
		framesover.setSize(400, 150);
		framesover.setVisible(true);
		
	}
      
      public static void Querymenu(Connection conn){
    	JFrame frameq=new JFrame("Execute Query");
  		frameq.setSize(500, 400);
  	
  		frameq.setLayout(new FlowLayout());
  		JLabel levelq=new JLabel("Enter Your Query in the box");
  		JPanel panelq=new JPanel();
  		JPanel panelq1=new JPanel();
  		panelq.setLayout(new FlowLayout());
  		frameq.add(levelq);
  		frameq.add(panelq);
  		frameq.add(panelq1);
  		frameq.setVisible(true);
  		JTextArea textq=new JTextArea("",10,20);
  		textq.setPreferredSize(new Dimension(100, 100));
  		textq.setLineWrap(true);
  		JScrollPane scrollq=new JScrollPane(textq);
  		scrollq.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
  		scrollq.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
  		JButton buttonq=new JButton("Execute");
  		JButton buttonq1=new JButton("back");
  		
        buttonq1.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				frameq.setVisible(false);
				Mainmenu(conn);
			}
		});
        
        
        buttonq.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String s = "";
				s = textq.getText();
				int o=1;
				ExecuteSQL(conn,s,o);
				frameq.setVisible(false);
			}
		});
        
        panelq.add(scrollq);
		panelq1.add(buttonq1);
		panelq1.add(buttonq);
		frameq.setVisible(true);
		
      } //End  of search for overtime table function
      
      public static Double SALExecuteSQL(Connection conn, String s,int c){  //Starting of Salary executesql function
        
    	Double a = 0.0;
  		try {
  			Statement stmt = conn.createStatement() ;
  			ResultSet rs = stmt.executeQuery(s);
  			
  			if(c==1){
  				while(rs.next()){
   				   a = rs.getDouble("TOT_SUM");
   				  
   				}
  			}
  			else if(c==2){
  				while(rs.next()){
   				   a = rs.getDouble("E_SALARY");
   				}
  			}
  			else if(c==3){
  				while(rs.next()){
   				   a = rs.getDouble("Hourly_wage");
   				}
  			}
  			
  				
  		
  		} catch (SQLException e) {
  			// TODO Auto-generated catch block
  			if(s.isEmpty())
  				JOptionPane.showMessageDialog(null, "Empty Values");
  			else
  				JOptionPane.showMessageDialog(null, "Error in SQL");
  			e.printStackTrace();
  		}
		return a;
  	}//End of salary executesql function
      
      
      
      /*public static void tot_sal(Connection conn,String em,String hw){
    	  String td="select E_id,sum(O_duration) as tot_sum from overtime_employee group by E_id having E_id='"+em+"'";
    	  int c1=1,c2=2;
    	  Double tot_duration=SALExecuteSQL(conn, td,c1);  
    	  String s="select E_salary from employee where E_id='"+em+"'";
    	  Double sal=SALExecuteSQL(conn,s,c2);
    	  //System.out.println(sal);
    	  Double H_wage= Double.parseDouble(hw);
    	  Double tot_sal=(sal+(tot_duration * H_wage));
    	  String t_sal="update employee set tot_salary= '"+tot_sal+"' where E_id='"+em+"'";
    	  //System.out.println(t_sal);
    	  ExecuteSQL(conn,t_sal, c2);
    	  
      }*/
      
      public static void tot_sal1(Connection conn){  //Starting of tot_sal1
    	  String a1="";
    	  String s="select * from overtime natural join overtime_employee";
    		try {
    			Statement stmt = conn.createStatement() ;
    			ResultSet rs = stmt.executeQuery(s);
    			
    				while(rs.next()){
     				   a1 = rs.getString("E_id");
     				   String td="select E_id,sum(O_duration) as tot_sum from overtime_employee group by E_id having E_id='"+a1+"'";
     		    	   int c1=1,c2=2,c3=3;
     		    	   Double tot_duration=SALExecuteSQL(conn, td,c1);  
     		    	   String s2="select E_salary from employee where E_id='"+a1+"'";
     		    	   String s3="select Hourly_wage from overtime natural join overtime_employee where E_id='"+a1+"'";
     		    	   Double sal=SALExecuteSQL(conn,s2,c2);
     		    	   //System.out.println(sal);
     		    	   Double H_wage= SALExecuteSQL(conn,s3,c3);
     		    	   Double tot_sal=(sal+(tot_duration * H_wage));
     		    	   String t_sal="update employee set E_tot_salary= '"+tot_sal+"' where E_id='"+a1+"'";
     		    	   //System.out.println(t_sal);
     		    	   ExecuteSQL(conn,t_sal, c2);
     				  
     				}
    			
    			
    				
    		
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
      } //End of tot_sal1
      
      
      public static void deleteMenu(int i,Connection conn){  //Starting of deletemenu
    	  if(i==0){
    		  JFrame framdemp=new JFrame("Delete Employee");
    			JPanel paneldemp=new JPanel();
    			JTextField textdemp=new JTextField("",30);
    			JLabel lebeldemp=new JLabel("Enter the complete Where cluse");
    			JButton butttondemp=new JButton("Select");
    			JButton buttondemp1=new JButton("<Back");
    			buttondemp1.addActionListener(new ActionListener() {
    				
    				public void actionPerformed(ActionEvent arg0) {
    					framdemp.setVisible(false);
    					Mainmenu(conn);
    				}
    			});
    			
    			butttondemp.addActionListener(new ActionListener() {
    				
    				public void actionPerformed(ActionEvent e) {
    					String strdep;
    					String s="commit";
    					if(textdemp.getText()!=null && textdemp.getText().length()>0){
    						strdep = "delete from employee where " + textdemp.getText();
    						System.out.println(strdep);
    						int o=2;
    						ExecuteSQL(conn,strdep,o);
    						ExecuteSQL(conn,s,o);
    						framdemp.setVisible(false);
    						Mainmenu(conn);
    					}
    					else{
    						strdep = "delete from employee";
    						System.out.println(strdep);
    						int o=2;
    						ExecuteSQL(conn,strdep,o);
    						ExecuteSQL(conn,s,o);
    						framdemp.setVisible(false);
    						Mainmenu(conn);
    					}
    				}
    			});
    			
    			paneldemp.add(lebeldemp);
    			paneldemp.add(textdemp);
    			paneldemp.add(buttondemp1);
    			paneldemp.add(butttondemp);
    			framdemp.add(paneldemp);
    			framdemp.setSize(400, 150);
    			framdemp.setVisible(true);
    			
    	  }
    	
    	  else if(i==1){
    		    JFrame frameddep=new JFrame("Delete Department");
    			JPanel panelddep=new JPanel();
    			JTextField textddep=new JTextField("",30);
    			JLabel lebelddep=new JLabel("Enter the complete Where cluse");
    			JButton butttonddep=new JButton("Select");
    			JButton buttonddep1=new JButton("<Back");
    			buttonddep1.addActionListener(new ActionListener() {
    				
    				public void actionPerformed(ActionEvent arg0) {
    					frameddep.setVisible(false);
    					Mainmenu(conn);
    				}
    			});
    			
    			butttonddep.addActionListener(new ActionListener() {
    				
    				public void actionPerformed(ActionEvent e) {
    					String strdep;
    					String s="commit";
    					if(textddep.getText()!=null && textddep.getText().length()>0){
    						strdep = "delete from department where " + textddep.getText();
    						System.out.println(strdep);
    						int o=2;
    						ExecuteSQL(conn,strdep,o);
    						ExecuteSQL(conn,s,o);
    						frameddep.setVisible(false);
    						Mainmenu(conn);
    					}
    					else{
    						strdep = "delete from department";
    						System.out.println(strdep);
    						int o=2;
    						ExecuteSQL(conn,strdep,o);
    						ExecuteSQL(conn,s,o);
    						frameddep.setVisible(false);
    						Mainmenu(conn);
    					}
    				}
    			});
    			
    			panelddep.add(lebelddep);
    			panelddep.add(textddep);
    			panelddep.add(buttonddep1);
    			panelddep.add(butttonddep);
    			frameddep.add(panelddep);
    			frameddep.setSize(400, 150);
    			frameddep.setVisible(true);
    	  }
    	  
    	  else if(i==2){
    		    JFrame framedpro=new JFrame("Delete Project");
    			JPanel paneldpro=new JPanel();
    			JTextField textdpro=new JTextField("",30);
    			JLabel lebeldpro=new JLabel("Enter the complete Where cluse");
    			JButton butttondpro=new JButton("Select");
    			JButton buttondpro1=new JButton("<Back");
    			buttondpro1.addActionListener(new ActionListener() {
    				
    				public void actionPerformed(ActionEvent arg0) {
    					framedpro.setVisible(false);
    					Mainmenu(conn);
    				}
    			});
    			
    			butttondpro.addActionListener(new ActionListener() {
    				
    				public void actionPerformed(ActionEvent e) {
    					String strdep;
    					String s="commit";
    					if(textdpro.getText()!=null && textdpro.getText().length()>0){
    						strdep = "delete from project where " + textdpro.getText();
    						System.out.println(strdep);
    						int o=2;
    						ExecuteSQL(conn,strdep,o);
    						ExecuteSQL(conn,s,o);
    						framedpro.setVisible(false);
    						Mainmenu(conn);
    					}
    					else{
    						strdep = "delete from project";
    						System.out.println(strdep);
    						int o=2;
    						ExecuteSQL(conn,strdep,o);
    						ExecuteSQL(conn,s,o);
    						framedpro.setVisible(false);
    						Mainmenu(conn);
    					}
    				}
    			});
    			
    			paneldpro.add(lebeldpro);
    			paneldpro.add(textdpro);
    			paneldpro.add(buttondpro1);
    			paneldpro.add(butttondpro);
    			framedpro.add(paneldpro);
    			framedpro.setSize(400, 150);
    			framedpro.setVisible(true);
    	  }
    	  
    	  else if(i==3){
    		    JFrame framedover=new JFrame("Delete Overtime");
    			JPanel paneldover=new JPanel();
    			JTextField textdover=new JTextField("",30);
    			JLabel lebeldover=new JLabel("Enter the complete Where cluse");
    			JButton butttondover=new JButton("Select");
    			JButton buttondover1=new JButton("<Back");
    			buttondover1.addActionListener(new ActionListener() {
    				
    				public void actionPerformed(ActionEvent arg0) {
    					framedover.setVisible(false);
    					Mainmenu(conn);
    				}
    			});
    			
    			butttondover.addActionListener(new ActionListener() {
    				
    				public void actionPerformed(ActionEvent e) {
    					String strdep;
    					String s="commit";
    					if(textdover.getText()!=null && textdover.getText().length()>0){
    						strdep = "delete from overtime where " + textdover.getText();
    						System.out.println(strdep);
    						int o=2;
    						ExecuteSQL(conn,strdep,o);
    						ExecuteSQL(conn,s,o);
    						framedover.setVisible(false);
    						Mainmenu(conn);
    					}
    					else{
    						strdep = "delete from overtime";
    						System.out.println(strdep);
    						int o=2;
    						ExecuteSQL(conn,strdep,o);
    						ExecuteSQL(conn,s,o);
    						framedover.setVisible(false);
    						Mainmenu(conn);
    					}
    				}
    			});
    			
    			paneldover.add(lebeldover);
    			paneldover.add(textdover);
    			paneldover.add(buttondover1);
    			paneldover.add(butttondover);
    			framedover.add(paneldover);
    			framedover.setSize(400, 150);
    			framedover.setVisible(true);
    	  }
    	  
    	  
      } //End of deletemenu
      
      
      public static void Print(Connection conn,String s) throws Exception{  //Starting of print menu
    	  Statement stmt = conn.createStatement();
          String timeStamp = new Timestamp(System.currentTimeMillis()).toString();
          String ts= timeStamp;
          ts = ts.replace(':','_');
          ts = ts.replace('-','_');
          ts = ts.replace(' ','_');
          System.out.println(ts);
          String fileName = "Report_" + ts + ".pdf";
          /* Define the SQL query */
          ResultSet rs = stmt.executeQuery(s);
          /* Step-2: Initialize PDF documents - logical objects */
          Document my_pdf_report = new Document();
          PdfWriter.getInstance(my_pdf_report, new FileOutputStream(fileName));
          my_pdf_report.open(); 
          
          ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			
          //we have four columns in our table
          PdfPTable my_report_table = new PdfPTable(columnsNumber);
          //create a cell object
          PdfPCell table_cell;
         
          for(int i=1; i<=columnsNumber; i++){
      		String str = rsmd.getColumnName(i);
              table_cell=new PdfPCell(new Phrase(str));
              my_report_table.addCell(table_cell);
			}
          
          while (rs.next()) {              
          	for(int i=1; i<=columnsNumber; i++){
          		String str = rs.getString(i);
                  table_cell=new PdfPCell(new Phrase(str));
                  my_report_table.addCell(table_cell);
  			}
          }
                          
          Paragraph paragraph = new Paragraph();
			paragraph.add("Report on " + s + "\n\n\n");
			paragraph.setAlignment(Element.ALIGN_CENTER);
			my_pdf_report.add(paragraph);
                   
          /* Attach report table to PDF */
          my_pdf_report.add(my_report_table);   
          my_pdf_report.addCreationDate();
          
          
          Paragraph paragraph1 = new Paragraph();
  		  paragraph1.add("\n\n\n Report generated on " + timeStamp);
  		  paragraph1.setAlignment(Element.ALIGN_RIGHT);
  		  my_pdf_report.add(paragraph1);
  		
          my_pdf_report.close();
          
          /* Close all DB related objects */
          rs.close();
          stmt.close();
      } //end of print menu
      
      public static void checkdate(Connection conn) throws Exception{
    	  DateFormat dateFormat = new SimpleDateFormat("dd");
    	  Date date = new Date();
    	  System.out.println(dateFormat.format(date));
    	  String a=dateFormat.format(date);
    	  String b = "01";
    	  if(a.equals(b)){
    		  String s="select * from overtime_employee";
    		  Print(conn,s);
    		  String s1="update overtime_employee set o_duration=0 where o_duration>0";
    		  int o=2;
    		  ExecuteSQL(conn,s1, o);
    	  }
      }

}
