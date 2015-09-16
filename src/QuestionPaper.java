import java.sql.Connection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;

public class QuestionPaper {
	
	Connection connection;
	HashMap jasperParameter;
	/* JasperReport is the object
	that holds our compiled jrxml file */
	JasperReport jasperReport;
	String output;

	Statement stmt = null;

	/* JasperPrint is the object contains
	report after result filling process */
	JasperPrint jasperPrint;	
	ResultSet rs;
	
	
	public void createReport() throws ClassNotFoundException, SQLException, IOException{
	
	// connection is the data source we used to fetch the data from
 
		// jasperParameter is a Hashmap contains the parameters
		// passed from application to the jrxml layout
	
		// jrxml compiling process
		
		//File for writing answer data;	
		String filepath = new File(".").getAbsolutePath();
		filepath = filepath.replaceFirst("\\.", "\\Config.txt");
		System.out.println(filepath);
		
		File file = new File(filepath);
		FileInputStream input = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		input.read(data);
		input.close();

		String str = new String(data, "UTF-8");		
		JSONObject obj = new JSONObject(str);
		
		String serverIP = obj.getString("server");
		String port = obj.getString("port");
		String dbname = obj.getString("databasename");
		String username = obj.getString("username");
		String password = obj.getString("password");
		
		
		Class.forName("org.postgresql.Driver");
		
		String postgresURL = "jdbc:postgresql://"+serverIP+":"+port+"/"+dbname+"";
		connection = DriverManager.getConnection(postgresURL,username,password);
		connection.setAutoCommit(false);
		System.out.println("Connection Established");
		
		stmt = connection.createStatement();		
		
//		String path = ".//Question_Paper.jrxml";
		String path = ".//Question_Paper.jasper";
		try {
//			jasperReport = JasperCompileManager.compileReport
//			(path);
			
			jasperReport = (JasperReport)JRLoader.loadObjectFromFile(path);
			
			System.out.println("Report compiled");
		}
		catch (JRException e) {}
	}
	
	
	
	public void fillReport(int i,int queCount,PrintWriter pw){
	// filling report with data from data source
		
		

		int j = 0;
		output = i+1 + " - ";
		try
		{

		ArrayList<DataBean> dataBeanList = new ArrayList<DataBean>();		
		j = 1;			
		
		//Logic for adding Arithmatic questions to DataBean;	
		rs = stmt.executeQuery( "SELECT * FROM public.\"questions\" where cat = 'ArithmaticL1' order by random() limit 4" );
		while (rs.next()){
			dataBeanList.add(produce(rs.getString("question"),rs.getString("option1"),rs.getString("option2"),rs.getString("option3"),rs.getString("option4")));
			output = output + j++ +":" + rs.getString("answer")+", ";
		}

		//Logic for adding Arithmatic level2 questions to DataBean;	
		rs = stmt.executeQuery( "SELECT * FROM public.\"questions\" where cat = 'ArithmaticL2' order by random() limit 6" );
		while (rs.next()){
			dataBeanList.add(produce(rs.getString("question"),rs.getString("option1"),rs.getString("option2"),rs.getString("option3"),rs.getString("option4")));
			output = output + j++ +":" + rs.getString("answer")+", ";
		}		
		
		//Logic for adding C Theory questions to DataBean;	
		rs = stmt.executeQuery( "SELECT * FROM public.\"questions\" where cat = 'ProgCTh' order by random() limit 4" );		
		
		while (rs.next()){
			dataBeanList.add(produce(rs.getString("question"),rs.getString("option1"),rs.getString("option2"),rs.getString("option3"),rs.getString("option4")));
			output = output + j++ +":" + rs.getString("answer")+", ";
		}

		//Logic for adding Java Theory questions to DataBean;	
		rs = stmt.executeQuery( "SELECT * FROM public.\"questions\" where cat = 'ProgJTh' order by random() limit 4" );		
		
		while (rs.next()){
			dataBeanList.add(produce(rs.getString("question"),rs.getString("option1"),rs.getString("option2"),rs.getString("option3"),rs.getString("option4")));
			output = output + j++ +":" + rs.getString("answer")+", ";
		}
		
		//Logic for adding C Programs L1 questions to DataBean;	
		rs = stmt.executeQuery( "SELECT * FROM public.\"questions\" where cat = 'ProgCL1' order by random() limit 5" );		
		
		while (rs.next()){
			dataBeanList.add(produce(rs.getString("question"),rs.getString("option1"),rs.getString("option2"),rs.getString("option3"),rs.getString("option4")));
			output = output + j++ +":" + rs.getString("answer")+", ";
		}
		
		//Logic for adding Java Programs L1 questions to DataBean;	
		rs = stmt.executeQuery( "SELECT * FROM public.\"questions\" where cat = 'ProgJL1' order by random() limit 5" );		
		
		while (rs.next()){
			dataBeanList.add(produce(rs.getString("question"),rs.getString("option1"),rs.getString("option2"),rs.getString("option3"),rs.getString("option4")));
			output = output + j++ +":" + rs.getString("answer")+", ";
		}		
		
		//Logic for adding Programs L2 questions to DataBean;	
		rs = stmt.executeQuery( "SELECT * FROM public.\"questions\" where cat = 'ProgL2' order by random() limit 2" );		
		
		while (rs.next()){
			dataBeanList.add(produce(rs.getString("question"),rs.getString("option1"),rs.getString("option2"),rs.getString("option3"),rs.getString("option4")));
			output = output + j++ +":" + rs.getString("answer")+", ";
		}				
		
		JRBeanCollectionDataSource beanColDataSource =
			      new JRBeanCollectionDataSource(dataBeanList);

		jasperParameter = new HashMap();
		jasperParameter.put("PaperCount",Integer.toString(i+1));
		jasperPrint = JasperFillManager.fillReport(jasperReport,jasperParameter, beanColDataSource);
		
		// exporting process
		// 1- export to PDF
		JasperExportManager.exportReportToPdfFile(jasperPrint, ".//sample_report"+i+".pdf");
		
//		JasperPrintManager.printReport(jasperPrint, true);
		System.out.println(output);
		pw.println(output);
		}
		catch(Exception exception)
		{
		exception.printStackTrace();
		System.out.println("Connection Failed");
		}	
	
	}
	
	private DataBean produce(String question, String option1,String option2,String option3,String option4) {
		DataBean dataBean = new DataBean();
		dataBean.setQuestion(question);
		dataBean.setOption1(option1);
		dataBean.setOption2(option2);
		dataBean.setOption3(option3);
		dataBean.setOption4(option4);
		
		return dataBean;
	}	
	
}



	
//	public static void main(String args[]) throws ClassNotFoundException{
//		new QuestionPaper().createReport();
//	}
	

