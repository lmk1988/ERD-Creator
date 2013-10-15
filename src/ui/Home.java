package ui;

import java.awt.EventQueue;

import javax.swing.ComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import java.awt.event.ActionListener;

import javax.swing.JTextArea;

import logic.FD;
import logic.Relation;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.SwingConstants;

public class Home {

	private JFrame frmNfDetector;
	private final Action action = new SwingAction();
	private JTextField text_rName;
	private JTextField attrTxt;
	private JLabel[] aNamelblList;
	private JTextField[] aNametxtList;
	private JTextPane rTxtPane;
	private JTextPane aList1, aList2;
	private StringTokenizer st;
	private Relation r;
	private JTextField pk_txt;
	private String result = "<html>";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home window = new Home();
					window.frmNfDetector.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Home() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmNfDetector = new JFrame();
		frmNfDetector.setTitle("NF Detector");
		frmNfDetector.setBounds(100, 100, 673, 539);
		frmNfDetector.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmNfDetector.getContentPane().setLayout(null);
		
		JPanel rpanel = new JPanel();
		rpanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Step 1: Create Relation", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		rpanel.setBounds(10, 23, 271, 129);
		frmNfDetector.getContentPane().add(rpanel);
		rpanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblRelationName = new JLabel("Relation Name:");
		rpanel.add(lblRelationName, "2, 2");
		
		text_rName = new JTextField();
		rpanel.add(text_rName, "4, 2");
		text_rName.setColumns(10);
		
		JLabel lblAttributes = new JLabel("Attributes:");
		lblAttributes.setHorizontalAlignment(SwingConstants.TRAILING);
		rpanel.add(lblAttributes, "2, 4");
		
		attrTxt = new JTextField();
		rpanel.add(attrTxt, "4, 4");
		attrTxt.setColumns(10);
		
		JLabel lblPk = new JLabel("Primary Keys:");
		lblPk.setHorizontalAlignment(SwingConstants.TRAILING);
		rpanel.add(lblPk, "2, 6, right, default");
		
		pk_txt = new JTextField();
		pk_txt.setColumns(10);
		rpanel.add(pk_txt, "4, 6, fill, default");
		
		JButton btnCreate = new JButton("Save");
		rpanel.add(btnCreate, "4, 8");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<String> aList = new ArrayList<String>();
				String attrs = attrTxt.getText();
				String attr = "";
				st = new StringTokenizer(attrs, ",");
				
				while(st.hasMoreTokens()) {
						aList.add(st.nextToken());
				}
				r = new Relation(text_rName.getText(), aList);
				ArrayList<String> attrList = r.GetAttrList();
				String pk = pk_txt.getText();
				r.priKey = pk;
				result = text_rName.getText() + "(";

				for(int i = 0; i < attrList.size(); i++) {
					if(attrList.get(i).equals(r.priKey)) {
						result += "<u>" + attrList.get(i) + "</u>";
					}else {
						result += attrList.get(i);
					}
						if(i > -1 && i < attrList.size()-1) {
							
							result += ",";
						}
				}
				result += ")";
				rTxtPane.setText(result);
				
			}
		});
		
		JPanel log_panel = new JPanel();
		log_panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Logs", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		log_panel.setBounds(291, 23, 328, 421);
		frmNfDetector.getContentPane().add(log_panel);
		log_panel.setLayout(null);
		
		rTxtPane = new JTextPane();
		rTxtPane.setEditable(false);
		rTxtPane.setContentType("text/html");
		rTxtPane.setBounds(10, 23, 308, 387);
		log_panel.add(rTxtPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Step 2: Add Functional Dependencies", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 174, 271, 145);
		frmNfDetector.getContentPane().add(panel);
		panel.setLayout(null);
		
		aList1 = new JTextPane();
		aList1.setBounds(10, 16, 81, 118);
		panel.add(aList1);
		
		aList2 = new JTextPane();
		aList2.setBounds(178, 16, 83, 118);
		panel.add(aList2);
		
		JButton button = new JButton("> >");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				st = new StringTokenizer(aList1.getText(), "\n");
				StringTokenizer st1 = new StringTokenizer(aList2.getText(), "\n");
				String lhs, rhs;
				while(st.hasMoreTokens()) {
					lhs = st.nextToken().trim();
					rhs = st1.nextToken().trim();
					r.fDList.add(new FD(lhs, rhs));
				}
				ArrayList<FD> fdList = r.fDList;
				FD fd;
				result += "<br/>";
				for(int i = 0; i < fdList.size(); i++){
					fd = fdList.get(i);
					result += fd.LHS + "->" + fd.RHS;
					if(i > -1 && i < fdList.size()-1 ) {
						result += ", ";
					}
				}

				ArrayList<String> candid = new ArrayList<String>();
				result += "<br/>The candidate keys are: ";
				for(int i = 0; i < candid.size(); i++) {
					
					result += candid.get(i);
				}
				rTxtPane.setText(result);
			}
		});
		button.setBounds(90, 16, 89, 118);
		panel.add(button);
		
	}
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "SwingAction_1");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
}
