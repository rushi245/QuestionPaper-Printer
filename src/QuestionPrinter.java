import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.NumberFormat;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;


public class QuestionPrinter extends JFrame {

	private JPanel contentPane;
	JFormattedTextField txtQueCount;
	JFormattedTextField txtPaperCount;
	JLabel status;
	PrintWriter writer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					QuestionPrinter frame = new QuestionPrinter();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public QuestionPrinter() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(4,2));
		
		NumberFormat longFormat = NumberFormat.getIntegerInstance();

		NumberFormatter numberFormatter = new NumberFormatter(longFormat);
		numberFormatter.setValueClass(Integer.class); //optional, ensures you will always get a long value
		numberFormatter.setAllowsInvalid(false); //this is the key!!
		numberFormatter.setMinimum(0); //Optional	
		numberFormatter.setMaximum(Integer.MAX_VALUE);
		
		JLabel questionCount = new JLabel("Question Count");
		txtQueCount = new JFormattedTextField(numberFormatter);
//		txtQueCount.setValue("");
		JLabel paperCount = new JLabel("Paper Count");
		txtPaperCount = new JFormattedTextField(numberFormatter);
//		txtPaperCount.setValue("");
		JButton print = new JButton("Print");
		JButton reset = new JButton("Reset");
		status = new JLabel("Printing Not Started");
		contentPane.add(questionCount);
		contentPane.add(txtQueCount);
		contentPane.add(paperCount);
		contentPane.add(txtPaperCount);
		contentPane.add(print);
		contentPane.add(reset);
		contentPane.add(status);
		
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				txtPaperCount.setValue(0);
				txtQueCount.setValue(0);
			}
		});
		
		print.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				QuestionPaper qp = new QuestionPaper();
				try {
					try {
						qp.createReport();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (NumberFormatException | ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					writer = new PrintWriter("./answer.txt");
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				for (int i = 0 ; i < Integer.parseInt(txtPaperCount.getText()); i++){
					qp.fillReport(i,Integer.parseInt(txtQueCount.getText()),writer);
					status.setText("Printing : "+i);
				}
				writer.close();
			}
		});	
	}
}