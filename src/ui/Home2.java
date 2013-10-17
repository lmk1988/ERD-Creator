package ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Font;


public class Home2 {

	private JFrame frmNfdetector;
	private JTabbedPane tabbedPane;
	private JTextField textField_Attr;
	private JTextField textField_LHS;
	private JTextField textField_RHS;
	private JTextField textField_Name;
	private JTextField textField_PriKeys;
	
	private DefaultListModel<String> model_Rel;
	private DefaultListModel<String> model_Attr;
	private DefaultListModel<String> model_PriKey;
	private DefaultListModel<String> model_FD;
	
	private JButton btn_AddAttr;
	private JButton btn_NewRelation;
	private JButton btn_PriKeys;
	private JButton btn_addFD;
	
	
	private JList<String> list_Attr;
	private JList<String> list_PriKeys;
	private JList<String> list_Rel;
	private JList<String> list_FD;
	
	private JPanel panel_Attributes;
	private JPanel panel_FD;
	private JPanel panel_Name;
	private JPanel panel_Relations;
	private JPanel panel_PriKeys;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home2 window = new Home2();
					window.frmNfdetector.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Home2() {
		initialize();
		setActions();
		disablePanels();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmNfdetector = new JFrame();
		frmNfdetector.setTitle("NF_Detector");
		frmNfdetector.setBounds(100, 100, 600, 416);
		frmNfdetector.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmNfdetector.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel_Create = new JPanel();
		tabbedPane.addTab("Create", null, panel_Create, null);
		panel_Create.setLayout(null);
		
		panel_Attributes = new JPanel();
		panel_Attributes.setBorder(new TitledBorder(null, "Attributes", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_Attributes.setBounds(166, 64, 133, 130);
		panel_Create.add(panel_Attributes);
		panel_Attributes.setLayout(null);
		
		btn_AddAttr = new JButton("+");
		btn_AddAttr.setBounds(82, 101, 41, 23);
		panel_Attributes.add(btn_AddAttr);
		
		textField_Attr = new JTextField();
		textField_Attr.setBounds(10, 102, 68, 20);
		panel_Attributes.add(textField_Attr);
		textField_Attr.setColumns(10);
		
		list_Attr = new JList<String>();
		list_Attr.setBounds(10, 15, 113, 76);
		panel_Attributes.add(list_Attr);
		
		panel_FD = new JPanel();
		panel_FD.setBorder(new TitledBorder(null, "Functional Dependencies", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_FD.setBounds(310, 64, 259, 263);
		panel_Create.add(panel_FD);
		panel_FD.setLayout(null);
		
		textField_LHS = new JTextField();
		textField_LHS.setColumns(10);
		textField_LHS.setBounds(10, 230, 80, 20);
		panel_FD.add(textField_LHS);
		
		JLabel lbl_arrow = new JLabel("-->");
		lbl_arrow.setFont(new Font("Cambria Math", Font.PLAIN, 11));
		lbl_arrow.setBounds(97, 233, 17, 14);
		panel_FD.add(lbl_arrow);
		
		textField_RHS = new JTextField();
		textField_RHS.setColumns(10);
		textField_RHS.setBounds(118, 230, 80, 20);
		panel_FD.add(textField_RHS);
		
		btn_addFD = new JButton("+");
		btn_addFD.setBounds(208, 229, 41, 23);
		panel_FD.add(btn_addFD);
		
		list_FD = new JList<String>();
		list_FD.setBounds(10, 20, 239, 199);
		panel_FD.add(list_FD);
		
		panel_Name = new JPanel();
		panel_Name.setBorder(new TitledBorder(null, "Relation Name", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_Name.setBounds(166, 11, 403, 42);
		panel_Create.add(panel_Name);
		panel_Name.setLayout(new GridLayout(0, 1, 0, 0));
		
		textField_Name = new JTextField();
		panel_Name.add(textField_Name);
		textField_Name.setColumns(10);
		
		panel_Relations = new JPanel();
		panel_Relations.setBorder(new TitledBorder(null, "Relations", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_Relations.setBounds(10, 11, 150, 316);
		panel_Create.add(panel_Relations);
		panel_Relations.setLayout(null);
		
		list_Rel = new JList<String>();
		list_Rel.setBounds(10, 17, 130, 254);
		panel_Relations.add(list_Rel);
		
		btn_NewRelation = new JButton("New Relation");
		btn_NewRelation.setActionCommand("");
		btn_NewRelation.setBounds(10, 282, 130, 23);
		panel_Relations.add(btn_NewRelation);
		
		panel_PriKeys = new JPanel();
		panel_PriKeys.setLayout(null);
		panel_PriKeys.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Primary Keys", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_PriKeys.setBounds(166, 197, 133, 130);
		panel_Create.add(panel_PriKeys);
		
		btn_PriKeys = new JButton("+");
		btn_PriKeys.setBounds(82, 101, 41, 23);
		panel_PriKeys.add(btn_PriKeys);
		
		textField_PriKeys = new JTextField();
		textField_PriKeys.setColumns(10);
		textField_PriKeys.setBounds(10, 102, 68, 20);
		panel_PriKeys.add(textField_PriKeys);
		
		list_PriKeys = new JList<String>();
		list_PriKeys.setBounds(10, 15, 113, 76);
		panel_PriKeys.add(list_PriKeys);
		
		JLabel lblDoubleClickTo = new JLabel("Double Click to remove undesired row");
		lblDoubleClickTo.setForeground(Color.GRAY);
		lblDoubleClickTo.setBounds(15, 330, 284, 14);
		panel_Create.add(lblDoubleClickTo);
		
		JPanel panel_Detect = new JPanel();
		tabbedPane.addTab("Detect", null, panel_Detect, null);
		
		JPanel panel_Suggest = new JPanel();
		tabbedPane.addTab("Suggest", null, panel_Suggest, null);
	}

	private void setActions(){
		model_Rel = new DefaultListModel<String>();
		list_Rel.setModel(model_Rel);
		model_Attr = new DefaultListModel<String>();
		list_Attr.setModel(model_Attr);
		model_PriKey = new DefaultListModel<String>();
		list_PriKeys.setModel(model_PriKey);
		model_FD = new DefaultListModel<String>();
		list_FD.setModel(model_FD);
		
		tabbedPane.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent e) {
		    	  //tabbedPane.getSelectedIndex();
		    	  //TODO
		    	  System.out.println("Selected Tab index: "+tabbedPane.getSelectedIndex());
		      }
		});
		
		list_Rel.addMouseListener(new MouseAdapter()
		{
		   public void mousePressed(MouseEvent evt)
		   {
			   java.awt.Point point = evt.getPoint();
			   int index = list_Rel.locationToIndex(point);
			   if(index>=0){
				   if(evt.getClickCount()==2){
					   //Double click
					   model_Rel.remove(index);
					   if(index!=0){
						   list_Rel.setSelectedIndex(index-1);
						   refreshDisplay(index-1);
					   }else if(model_Rel.size()>0){
						   list_Rel.setSelectedIndex(0);
						   refreshDisplay(0);
					   }else{
						   refreshDisplay(-1);
					   }
				   }else{
					   //Single click
					   refreshDisplay(index);
				   }
			   }
		   }
		});
		
		list_PriKeys.addMouseListener(new MouseAdapter()
		{
		   public void mousePressed(MouseEvent evt)
		   {
			   java.awt.Point point = evt.getPoint();
			   int index = list_PriKeys.locationToIndex(point);
			   if(index>=0){
				   if(evt.getClickCount()==2){
					   //Double click
					   model_PriKey.remove(index);
				   }
				   list_PriKeys.clearSelection();
			   }
		   }
		});
		
		btn_NewRelation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				model_Rel.addElement("Relation "+(model_Rel.size()+1));
				list_Rel.setSelectedIndex(model_Rel.size()-1);
				refreshDisplay(model_Rel.size()-1);
			}
		});
		
		btn_AddAttr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				textField_Attr.setText(textField_Attr.getText().trim());
				if(textField_Attr.getText().length()!=0){
					if(!model_Attr.contains(textField_Attr.getText())){
						model_Attr.addElement(textField_Attr.getText());
						textField_Attr.setText("");
					}
				}
			}
		});
	
		btn_PriKeys.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				textField_PriKeys.setText(textField_PriKeys.getText().trim());
				if(textField_PriKeys.getText().length()!=0){
					String properTemp="";
					//check if pri key exist in attributes
					String split[]=textField_PriKeys.getText().split(",");
					for(int i=0;i<split.length;i++){
						if(!model_Attr.contains(split[i].trim())){
							return;
						}else{
							if(i!=0){
								properTemp+=",";
							}
							properTemp+=split[i];
						}
					}
					
					//check if there is existing pri key
					if(!model_PriKey.contains(properTemp)){
						model_PriKey.addElement(properTemp);
						textField_PriKeys.setText("");
					}
				}
			}
		});
		
		btn_addFD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				
				textField_LHS.setText(textField_LHS.getText().trim());
				textField_RHS.setText(textField_RHS.getText().trim());
				
				if(textField_LHS.getText().length()!=0 && textField_RHS.getText().length()!=0){
					String properLHS="";
					String properRHS="";
					//Check if they are made from attributes
					String LHSsplit[]=textField_LHS.getText().split(",");
					for(int i=0;i<LHSsplit.length;i++){
						if(!model_Attr.contains(LHSsplit[i].trim())){
							return;
						}else{
							if(i!=0){
								properLHS+=",";
							}
							properLHS+=LHSsplit[i];
						}
					}
					String RHSsplit[]=textField_RHS.getText().split(",");
					for(int i=0;i<RHSsplit.length;i++){
						if(!model_Attr.contains(RHSsplit[i].trim())){
							return;
						}else{
							if(i!=0){
								properRHS+=",";
							}
							properRHS+=RHSsplit[i];
						}
					}
					
					//check if there exist the same combination in FD
					if(!model_FD.contains(properLHS+"->"+properRHS)){
						model_FD.addElement(properLHS+"->"+properRHS);
					}
				}
			}
		});
	
		
		//Auto update name of relation as user changes the name
		textField_Name.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				model_Rel.setElementAt(textField_Name.getText(), list_Rel.getSelectedIndex());
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				model_Rel.setElementAt(textField_Name.getText(), list_Rel.getSelectedIndex());
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				model_Rel.setElementAt(textField_Name.getText(), list_Rel.getSelectedIndex());
			}
		});
	}
	
	private void refreshDisplay(int index){
		if(index<0){
			disablePanels();
		}else{
			enablePanels();
			textField_Name.setText(model_Rel.get(index));
		}
	}
	
	private void enablePanels(){
		boolean bol = true;
		panel_Attributes.setEnabled(bol);
		panel_FD.setEnabled(bol);
		panel_Name.setEnabled(bol);
		panel_PriKeys.setEnabled(bol);
		textField_Attr.setEnabled(bol);
		textField_LHS.setEnabled(bol);
		textField_Name.setEnabled(bol);
		textField_PriKeys.setEnabled(bol);
		textField_RHS.setEnabled(bol);
		btn_AddAttr.setEnabled(bol);
		btn_addFD.setEnabled(bol);
		btn_PriKeys.setEnabled(bol);
	}
	
	private void disablePanels(){
		boolean bol = false;
		model_Attr.clear();
		model_PriKey.clear();
		model_FD.clear();
		textField_Attr.setText("");
		textField_LHS.setText("");
		textField_Name.setText("");
		textField_PriKeys.setText("");
		textField_RHS.setText("");
		panel_Attributes.setEnabled(bol);
		panel_FD.setEnabled(bol);
		panel_Name.setEnabled(bol);
		panel_PriKeys.setEnabled(bol);
		textField_Attr.setEnabled(bol);
		textField_LHS.setEnabled(bol);
		textField_Name.setEnabled(bol);
		textField_PriKeys.setEnabled(bol);
		textField_RHS.setEnabled(bol);
		btn_AddAttr.setEnabled(bol);
		btn_addFD.setEnabled(bol);
		btn_PriKeys.setEnabled(bol);
	}
}
