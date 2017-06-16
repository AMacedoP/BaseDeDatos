package net.roseindia.jtableExample;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.*;
import java.awt.event.*;

public class BaseDeDatos implements ActionListener{
	JFrame frame, frame1;
	JTextField textbox;
	JLabel label;
	JButton button;
	JPanel panel;
	JLabel salida;
	static JTable table;
	
	String driverName = "oracle.jdbc.driver.OracleDriver";
	String url="jdbc:oracle:thin:@dbinf.inf.pucp.edu.pe:1521/DBINF";
	String userName = "A20150087";
	String password = "w86j19eh";
	String[] columnNames = {"ID", "ID del País", "Nombre del país", "ID de la Región"};
	
	public void createUI(){
		frame = new JFrame("Resultado de la búsqueda");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		textbox = new JTextField();
		textbox.setBounds(273,30,150,20); 
		label = new JLabel("Ingresa el ID de la región");
		label.setBounds(53, 30, 150, 20);
		button = new JButton("Hola qlo");
		button.setBounds(171,118,150,20);
		button.addActionListener(this);
		salida = new JLabel("", SwingConstants.CENTER);
		salida.setBounds(108, 88, 263, 14);
		
		frame.getContentPane().add(textbox);
		frame.getContentPane().add(label);
		frame.getContentPane().add(button);
		frame.getContentPane().add(salida);
		
		frame.setVisible(true);
		frame.setSize(500, 200); 
	} 
	
	public void actionPerformed(ActionEvent ae){
		button = (JButton)ae.getSource();
		//System.out.println("Showing Table Data.......");
		//showTableData();
		showCountry();
	}
	
	public void showCountry(){
		try{ 
			Class.forName(driverName); 
			Connection con = DriverManager.getConnection(url, userName, password);
			CallableStatement stmt = con.prepareCall("{call prueba2(?,?)}");
			stmt.setString(1, textbox.getText());
			stmt.registerOutParameter(2, Types.VARCHAR);
			stmt.execute();
			System.out.println("Ejecutando el procedimiento..");
			salida.setText("El país es: " + stmt.getString(2));
		}
		catch(Exception ex){
			salida.setText("El pais no esta en la base de datos");
			/*JOptionPane.showMessageDialog(null, ex.getMessage(),"Error",
			JOptionPane.ERROR_MESSAGE);*/
		}
	}
	
	public void showTableData(){
		frame1 = new JFrame("Database Search Result");
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.getContentPane().setLayout(new BorderLayout()); 
		//TableModel tm = new TableModel();
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columnNames);
		//DefaultTableModel model = new DefaultTableModel(tm.getData1(), tm.getColumnNames()); 
		//table = new JTable(model);
		table = new JTable();
		table.setModel(model); 
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setFillsViewportHeight(true);
		JScrollPane scroll = new JScrollPane(table);
		scroll.setHorizontalScrollBarPolicy(
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		String textvalue = textbox.getText();
		String countryID = "";
		String countryName = "";
		String regionID = "";
		try{ 
			Class.forName(driverName); 
			Connection con = DriverManager.getConnection(url, userName, password);
			String sql = "prueba( " +textvalue+")";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			int i =0;
			while(rs.next()){
				i++;
				countryID = rs.getString("COUNTRY_ID");
				countryName = rs.getString("COUNTRY_NAME");
				regionID = rs.getString("REGION_ID"); 
				model.addRow(new Object[]{i, countryID, countryName, regionID}); 
			}
			if(i <1){
				JOptionPane.showMessageDialog(null, "No Record Found","Error",
				JOptionPane.ERROR_MESSAGE);
			}
			if(i ==1){
				System.out.println(i+" Record Found");
			}
			else{
				System.out.println(i+" Records Found");
			}
		}
		catch(Exception ex){
			JOptionPane.showMessageDialog(null, ex.getMessage(),"Error",
			JOptionPane.ERROR_MESSAGE);
		}
		frame1.getContentPane().add(scroll);
		frame1.setVisible(true);
		frame1.setSize(400,300);
	}
	
	public static void main(String args[]){
		BaseDeDatos sr = new BaseDeDatos();
		sr.createUI(); 
	}
}
