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

import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

public class Home {

	private JFrame frmNfDetector;
	private final Action action = new SwingAction();
	private JTextField text_rName;
	private JComboBox comboBox;
	private JPanel aTxtPanel;
	private JLabel aNamelbl1;
	private JTextField aNametxt1;
	private JLabel aNamelbl2;
	private JTextField aNametxt2;
	private JLabel aNamelbl3;
	private JTextField aNametxt3;
	private JLabel aNamelbl4;
	private JTextField aNametxt4;
	private JLabel aNamelbl5;
	private JTextField aNametxt5;
	private JLabel aNamelbl6;
	private JTextField aNametxt6;
	private JLabel aNamelbl7;
	private JTextField aNametxt7;
	private JLabel aNamelbl8;
	private JTextField aNametxt8;
	private JLabel[] aNamelblList;
	private JTextField[] aNametxtList;
	private JLabel rlbl;

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
		
		JLabel lblRelationName = new JLabel("Relation Name:");
		lblRelationName.setBounds(10, 22, 127, 14);
		frmNfDetector.getContentPane().add(lblRelationName);
		
		text_rName = new JTextField();
		text_rName.setBounds(147, 16, 197, 20);
		frmNfDetector.getContentPane().add(text_rName);
		text_rName.setColumns(10);
		
		JLabel lblAttributes = new JLabel("No of Attributes:");
		lblAttributes.setBounds(10, 61, 127, 14);
		frmNfDetector.getContentPane().add(lblAttributes);
		
		comboBox = new JComboBox();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				int state = arg0.getStateChange();
				if(state == ItemEvent.SELECTED) {
					int textNo = Integer.parseInt(arg0.getItem().toString());
					for(int i = 0; i < textNo; i++) {
						
						aNamelblList[i].setVisible(true);
						aNametxtList[i].setVisible(true);
					}
				}
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8"}));
		comboBox.setBounds(147, 55, 59, 20);
		frmNfDetector.getContentPane().add(comboBox);
		
		aTxtPanel = new JPanel();
		aTxtPanel.setBounds(10, 102, 354, 283);
		aTxtPanel.setVisible(true);
		frmNfDetector.getContentPane().add(aTxtPanel);
		aTxtPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		aNamelbl1 = new JLabel("Name of Attribute 1:");
		aTxtPanel.add(aNamelbl1, "2, 2, right, default");
		aNamelbl1.setVisible(true);
		
		aNametxt1 = new JTextField();
		aTxtPanel.add(aNametxt1, "4, 2, fill, default");
		aNametxt1.setColumns(10);
		aNametxt1.setVisible(true);
		
		aNamelbl2 = new JLabel("Name of Attribute 2:");
		aTxtPanel.add(aNamelbl2, "2, 4, right, default");
		aNamelbl2.setVisible(false);
		
		aNametxt2 = new JTextField();
		aNametxt2.setColumns(10);
		aTxtPanel.add(aNametxt2, "4, 4, fill, default");
		aNametxt2.setVisible(false);
		
		aNamelbl3 = new JLabel("Name of Attribute 3:");
		aTxtPanel.add(aNamelbl3, "2, 6, right, default");
		aNamelbl3.setVisible(false);
		
		aNametxt3 = new JTextField();
		aNametxt3.setColumns(10);
		aTxtPanel.add(aNametxt3, "4, 6, fill, default");
		aNametxt3.setVisible(false);
		
		aNamelbl4 = new JLabel("Name of Attribute 4:");
		aTxtPanel.add(aNamelbl4, "2, 8, right, default");
		aNamelbl4.setVisible(false);
		
		aNametxt4 = new JTextField();
		aNametxt4.setColumns(10);
		aTxtPanel.add(aNametxt4, "4, 8, fill, default");
		aNametxt4.setVisible(false);
		
		aNamelbl5 = new JLabel("Name of Attribute 5:");
		aTxtPanel.add(aNamelbl5, "2, 10, right, default");
		aNamelbl5.setVisible(false);
		
		aNametxt5 = new JTextField();
		aNametxt5.setColumns(10);
		aTxtPanel.add(aNametxt5, "4, 10, fill, default");
		aNametxt5.setVisible(false);
		
		aNamelbl6 = new JLabel("Name of Attribute 6:");
		aTxtPanel.add(aNamelbl6, "2, 12, right, default");
		aNamelbl6.setVisible(false);
		
		aNametxt6 = new JTextField();
		aNametxt6.setColumns(10);
		aTxtPanel.add(aNametxt6, "4, 12, fill, default");
		aNametxt6.setVisible(false);
		
		aNamelbl7 = new JLabel("Name of Attribute 7:");
		aTxtPanel.add(aNamelbl7, "2, 14, right, default");
		aNamelbl7.setVisible(false);
		
		aNametxt7 = new JTextField();
		aNametxt7.setColumns(10);
		aTxtPanel.add(aNametxt7, "4, 14, fill, default");
		aNametxt7.setVisible(false);
		
		aNamelbl8 = new JLabel("Name of Attribute 8:");
		aTxtPanel.add(aNamelbl8, "2, 16, right, default");
		aNamelbl8.setVisible(false);
		
		aNametxt8 = new JTextField();
		aNametxt8.setColumns(10);
		aTxtPanel.add(aNametxt8, "4, 16, fill, default");
		aNametxt8.setVisible(false);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Relation r;
				ArrayList<String> aList = new ArrayList<String>();
				for(int i = 0; i < aNametxtList.length; i++) {
					
					if(!aNametxtList[i].getText().equals("")) {
						
						aList.add(aNametxtList[i].getText());
					}
				}
				r = new Relation(text_rName.getText(), aList);
				ArrayList<String> attrList = r.GetAttrList();
				String result = rlbl.getText();
				result += text_rName.getText() + "(";

				for(int i = 0; i < attrList.size(); i++) {
						result += attrList.get(i);
						if(i > -1 && i < attrList.size()-1) {
							
							result += ",";
						}
				}
				result += ")";
				rlbl.setText("The new relation created are: " + "\n" + result);
			}
		});
		btnCreate.setBounds(10, 396, 89, 23);
		frmNfDetector.getContentPane().add(btnCreate);
		
		rlbl = new JLabel();
		rlbl.setBounds(380, 8, 267, 147);
		frmNfDetector.getContentPane().add(rlbl);
		
		aNamelblList = new JLabel[]{aNamelbl1, aNamelbl2, aNamelbl3, aNamelbl4, aNamelbl5, aNamelbl6, aNamelbl7, aNamelbl8};
		aNametxtList = new JTextField[]{aNametxt1, aNametxt2, aNametxt3, aNametxt4, aNametxt5, aNametxt6, aNametxt7, aNametxt8};
		
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
