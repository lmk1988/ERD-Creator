package ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
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
import java.util.ArrayList;

import javax.swing.JTextPane;

import logic.Attribute;
import logic.Relation;
import logic.Bernstein;
import logic.FD;
import logic.Partition;
import logic.Violation;

public class Home2 {

	private JFrame frmNfdetector;
	private JTabbedPane tabbedPane;
	private JTextField textField_Attr;
	private JTextField textField_LHS;
	private JTextField textField_RHS;
	private JTextField textField_Name;
	private JTextField textField_PriKeys;

	private DefaultListModel<String> data_Rel;
	private ArrayList<DefaultListModel<String>> datalist_Attr;
	private ArrayList<DefaultListModel<String>> datalist_PriKey;
	private ArrayList<DefaultListModel<String>> datalist_FD;

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
	private JTextPane textPane_Detect;
	private JTextPane textPane_Suggest;
	
	
	private ArrayList<Relation> arrayRel;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home2 window = new Home2();
					window.frmNfdetector.setVisible(true);
					window.frmNfdetector.setLocationRelativeTo(null);
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
		setTabChangeListener();
		setCreateTabActions();
		disablePanels();
	}

	/**
	 * Initialize the contents of the frame. (UI stuffs only)
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
		JScrollPane scroll_Attr = new JScrollPane(list_Attr);
		scroll_Attr.setBounds(10, 15, 113, 76);
		panel_Attributes.add(scroll_Attr);

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
		JScrollPane scroll_FD = new JScrollPane(list_FD);
		scroll_FD.setBounds(10, 20, 239, 199);
		panel_FD.add(scroll_FD);

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
		JScrollPane scroll_Rel = new JScrollPane(list_Rel);
		scroll_Rel.setBounds(10, 17, 130, 254);
		panel_Relations.add(scroll_Rel);

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
		JScrollPane scroll_PriKeys = new JScrollPane(list_PriKeys);
		scroll_PriKeys.setBounds(10, 15, 113, 76);
		panel_PriKeys.add(scroll_PriKeys);

		JLabel lblDoubleClickTo = new JLabel("Double Click to remove undesired row");
		lblDoubleClickTo.setForeground(Color.GRAY);
		lblDoubleClickTo.setBounds(15, 330, 284, 14);
		panel_Create.add(lblDoubleClickTo);

		JPanel panel_Detect = new JPanel();
		tabbedPane.addTab("Detect", null, panel_Detect, null);
		panel_Detect.setLayout(null);
		
		textPane_Detect = new JTextPane();
		textPane_Detect.setEditable(false);
		textPane_Detect.setContentType("text/html");
		JScrollPane scroll_Detect = new JScrollPane(textPane_Detect);
		scroll_Detect.setBounds(10, 11, 559, 328);
		panel_Detect.add(scroll_Detect);

		JPanel panel_Suggest = new JPanel();
		tabbedPane.addTab("Suggest", null, panel_Suggest, null);
		panel_Suggest.setLayout(null);
		
		textPane_Suggest = new JTextPane();
		textPane_Suggest.setEditable(false);
		textPane_Suggest.setContentType("text/html");
		JScrollPane scroll_Suggest = new JScrollPane(textPane_Suggest);
		scroll_Suggest.setBounds(10, 11, 559, 328);
		panel_Suggest.add(scroll_Suggest);
	}

	private void setCreateTabActions(){
		data_Rel = new DefaultListModel<String>();
		datalist_Attr = new ArrayList<DefaultListModel<String>>();
		datalist_PriKey = new ArrayList<DefaultListModel<String>>();
		datalist_FD = new ArrayList<DefaultListModel<String>>();
		
		list_Rel.setModel(data_Rel);

		list_Rel.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent evt)
			{
				java.awt.Point point = evt.getPoint();
				int index = list_Rel.locationToIndex(point);
				if(index>=0){
					if(evt.getClickCount()==2){
						//Double click
						data_Rel.remove(index);
						datalist_Attr.remove(index);
						datalist_PriKey.remove(index);
						datalist_FD.remove(index);
						if(index!=0){
							list_Rel.setSelectedIndex(index-1);
							refreshDisplay(index-1);
						}else if(data_Rel.size()>0){
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
						String [] pkList = datalist_PriKey.get(list_Rel.getSelectedIndex()).get(index).split(",");
						for(int i = 0; i < pkList.length; i++) {
							String pk = pkList[i];
							String [] attrArr =new String[datalist_Attr.get(list_Rel.getSelectedIndex()).size()];
							datalist_Attr.get(list_Rel.getSelectedIndex()).copyInto(attrArr);
							String fds = "";
							for(int j = 0; j < attrArr.length; j++) {
								if(!attrArr[j].equals(pk)) {
									fds += pk + "->" + attrArr[j];
								}
								if(j > 0 && j < attrArr.length-1) {
									
									fds += ", ";
								}
							}
							int choice = JOptionPane.showConfirmDialog(null, "Deleting this Primary Key will also remove the following FDs: " + fds + " \nDo you want to Proceed?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
							if(choice == JOptionPane.YES_OPTION) {
							//JOptionPane.showConfirmDialog(null, "Deleting this Primary Key will also remove the following FDs: \nDo you want to Proceed?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
								for(int j = 0; j < attrArr.length; j++) {
									if(!attrArr[j].equals(pk)) {
										datalist_FD.get(list_Rel.getSelectedIndex()).removeElement(pk + "->" + attrArr[j]);
									}
								}
								if(!datalist_PriKey.get(list_Rel.getSelectedIndex()).isEmpty()) {
									datalist_PriKey.get(list_Rel.getSelectedIndex()).remove(index);
								}
						}
						}
					}
					list_PriKeys.clearSelection();
				}
			}
		});

		list_FD.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent evt)
			{
				java.awt.Point point = evt.getPoint();
				int index = list_FD.locationToIndex(point);
				if(index>=0){
					if(evt.getClickCount()==2){
						//Double click
						datalist_FD.get(list_Rel.getSelectedIndex()).remove(index);
					}
					list_FD.clearSelection();
				}
			}
		});
		
		list_Attr.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent evt)
			{
				java.awt.Point point = evt.getPoint();
				int index = list_Attr.locationToIndex(point);
				if(index>=0 && list_Rel.getSelectedIndex()>=0){
					if(evt.getClickCount()==2){
						//Double click
						//Remove everything related to this attribute
						for(int i=0;i<datalist_PriKey.get(list_Rel.getSelectedIndex()).size();i++){
							String[] keySplit = datalist_PriKey.get(list_Rel.getSelectedIndex()).get(i).split(",");
							for(int j=0;j<keySplit.length;j++){
								if(keySplit[j].compareTo(datalist_Attr.get(list_Rel.getSelectedIndex()).get(index))==0){
									datalist_PriKey.remove(i);
									i--;
									break;
								}
							}		
						}
						
						for(int i=0;i<datalist_FD.get(list_Rel.getSelectedIndex()).size();i++){
							String[] keySplit = datalist_FD.get(list_Rel.getSelectedIndex()).get(i).split("->");
							if(keySplit.length!=2){
								System.out.println("Error, FD unable to parse properly");
								return;
							}
							String[] LHSSplit = keySplit[0].split(",");
							String[] RHSSplit = keySplit[1].split(",");
							boolean bol_found = false;
							for(int j=0;j<LHSSplit.length && !bol_found;j++){
								if(LHSSplit[j].compareTo(datalist_Attr.get(list_Rel.getSelectedIndex()).get(index))==0){
									datalist_FD.get(list_Rel.getSelectedIndex()).remove(i);
									i--;
									bol_found=true;
									break;
								}
							}
							for(int j=0;j<RHSSplit.length && !bol_found;j++){
								if(RHSSplit[j].compareTo(datalist_Attr.get(list_Rel.getSelectedIndex()).get(index))==0){
									datalist_FD.get(list_Rel.getSelectedIndex()).remove(i);
									i--;
									bol_found=true;
									break;
								}
							}
						}
						
						datalist_Attr.get(list_Rel.getSelectedIndex()).remove(index);
					}
					list_Attr.clearSelection();
				}
			}
		});
		
		btn_NewRelation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				data_Rel.addElement("Relation "+(data_Rel.size()+1));
				datalist_Attr.add(new DefaultListModel<String>());
				datalist_PriKey.add(new DefaultListModel<String>());
				datalist_FD.add(new DefaultListModel<String>());
				list_Rel.setSelectedIndex(data_Rel.size()-1);
				refreshDisplay(data_Rel.size()-1);
			}
		});

		btn_AddAttr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				textField_Attr.setText(textField_Attr.getText().replaceAll(",", "").replaceAll("->","").replaceAll("\\{","").replaceAll("\\}","").trim());
				if(textField_Attr.getText().length()!=0 && list_Rel.getSelectedIndex()>=0){
					if(!datalist_Attr.get(list_Rel.getSelectedIndex()).contains(textField_Attr.getText())){
						datalist_Attr.get(list_Rel.getSelectedIndex()).addElement(textField_Attr.getText());
						textField_Attr.setText("");
						
					}
				}
			}
		});

		btn_PriKeys.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				textField_PriKeys.setText(textField_PriKeys.getText().replaceAll("->","").replaceAll("\\{","").replaceAll("\\}","").trim());
				if(textField_PriKeys.getText().length()!=0 && list_Rel.getSelectedIndex()>=0){
					String properTemp="";
					//check if pri key exist in attributes
					String[] split=textField_PriKeys.getText().split(",");
					for(int i=0;i<split.length;i++){
						if(!datalist_Attr.get(list_Rel.getSelectedIndex()).contains(split[i].trim())){
							JOptionPane.showMessageDialog(null, "Unable to assign Primary Key. Attribute " +  split[i].trim() + " is not found in the Relation.", "Error", JOptionPane.ERROR_MESSAGE);
						}else{
							if(i!=0){
								properTemp+=",";
							}
							properTemp+=split[i];
							String pk = split[i].trim();
							String [] attrArr =new String[datalist_Attr.get(list_Rel.getSelectedIndex()).size()];
							datalist_Attr.get(list_Rel.getSelectedIndex()).copyInto(attrArr);;
							for(int j = 0; j < attrArr.length; j++) {
								if(!attrArr[j].equals(pk)) {
									datalist_FD.get(list_Rel.getSelectedIndex()).addElement(pk + "->" + attrArr[j]);
								}
							}
						}
					}

					//check if there is existing pri key
					if(!datalist_PriKey.get(list_Rel.getSelectedIndex()).contains(properTemp)){
						datalist_PriKey.get(list_Rel.getSelectedIndex()).addElement(properTemp);
						textField_PriKeys.setText("");
					}
				}
			}
		});

		btn_addFD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				textField_LHS.setText(textField_LHS.getText().replaceAll("->","").replaceAll("\\{","").replaceAll("\\}","").trim());
				textField_RHS.setText(textField_RHS.getText().replaceAll("->","").replaceAll("\\{","").replaceAll("\\}","").trim());

				if(list_Rel.getSelectedIndex()>=0 && textField_LHS.getText().length()!=0 && textField_RHS.getText().length()!=0){
					String properLHS="";
					String properRHS="";
					//Check if they are made from attributes
					String[] LHSsplit=textField_LHS.getText().split(",");
					for(int i=0;i<LHSsplit.length;i++){
						if(!datalist_Attr.get(list_Rel.getSelectedIndex()).contains(LHSsplit[i].trim())){
							JOptionPane.showMessageDialog(null, "Attribute " +  LHSsplit[i].trim() + " is not found in the Relation. Please add Attribute first.", "Error", JOptionPane.ERROR_MESSAGE);
							textField_LHS.grabFocus();
						}else{
							if(i!=0){
								properLHS+=",";
							}
							properLHS+=LHSsplit[i];
						}
					}
					String[] RHSsplit=textField_RHS.getText().split(",");
					for(int i=0;i<RHSsplit.length;i++){
						if(!datalist_Attr.get(list_Rel.getSelectedIndex()).contains(RHSsplit[i].trim())){
							JOptionPane.showMessageDialog(null, "Attribute " + RHSsplit[i].trim() + " is not found in the Relation. Please add Attribute first.", "Error", JOptionPane.ERROR_MESSAGE);
							textField_RHS.grabFocus();
						}else{
							if(i!=0){
								properRHS+=",";
							}
							properRHS+=RHSsplit[i];
						}
					}

					//check if there exist the same combination in FD
					if(!datalist_FD.get(list_Rel.getSelectedIndex()).contains(properLHS+"->"+properRHS)){
						if(properLHS.length() == 0 || properRHS.length() == 0) {
							
						}else {
						datalist_FD.get(list_Rel.getSelectedIndex()).addElement(properLHS+"->"+properRHS);
						textField_LHS.setText("");
						textField_RHS.setText("");
						textField_LHS.grabFocus();
						}
					}
				}else {
					if(textField_LHS.getText().length()==0 && textField_RHS.getText().length()==0) {
						JOptionPane.showMessageDialog(null, "Please enter an Attribute on both LHS and RHS", "Error", JOptionPane.ERROR_MESSAGE);
					}else if(textField_LHS.getText().length()==0 && textField_RHS.getText().length()!=0) {
						
						JOptionPane.showMessageDialog(null, "Please enter an Attribute on LHS", "Error", JOptionPane.ERROR_MESSAGE);
					}else if(textField_LHS.getText().length()!=0 && textField_RHS.getText().length()==0) {
						
						JOptionPane.showMessageDialog(null, "Please enter an Attribute on RHS", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});


		//Auto update name of relation as user changes the name
		textField_Name.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				resetName();
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				resetName();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				resetName();
			}
			
			private void resetName(){
				if(!data_Rel.isEmpty()){
					data_Rel.setElementAt(textField_Name.getText(), list_Rel.getSelectedIndex());
				}
			}
		});
	
		//Set enter key
		textField_Attr.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	//auto triggered by enter key
            	btn_AddAttr.doClick();
        }});
		
		textField_PriKeys.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	//auto triggered by enter key
            	btn_PriKeys.doClick();
        }});
		
		textField_LHS.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	//auto triggered by enter key
            	btn_addFD.doClick();
        }});
		
		textField_RHS.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	//auto triggered by enter key
            	btn_addFD.doClick();
        }});
	}

	private void setTabChangeListener(){
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				switch(tabbedPane.getSelectedIndex()){
				case 0:
					//Create
					break;
				case 1:
					//Detect
					createModelsFromInput();
					Log.getInstance().setLogView(textPane_Detect);
					Log.getInstance().clearLog();
					performDetection();
					break;
				case 2:
					//Suggest
					createModelsFromInput();
					Log.getInstance().setLogView(textPane_Suggest);
					Log.getInstance().clearLog();
					performSuggestion();
					break;
				default:
					System.out.println("Error: unknown tab selected");
					break;
				}
			}
		});
	}
	
	//Refresh the panels in create when user choose a new relation
	private void refreshDisplay(int index){
		if(index<0){
			disablePanels();
			((DefaultListModel<String>)list_FD.getModel()).removeAllElements();
			((DefaultListModel<String>)list_Attr.getModel()).removeAllElements();
			((DefaultListModel<String>)list_PriKeys.getModel()).removeAllElements();
		}else{
			enablePanels();
			textField_Name.setText(data_Rel.get(index));
			list_FD.setModel(datalist_FD.get(index));
			list_Attr.setModel(datalist_Attr.get(index));
			list_PriKeys.setModel(datalist_PriKey.get(index));
		}
	}

	//Enable input panels in create tab
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

	//Disable input panels in create tab
	private void disablePanels(){
		boolean bol = false;
		datalist_Attr.clear();
		datalist_PriKey.clear();
		datalist_FD.clear();
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

	private void createModelsFromInput(){
		arrayRel = new ArrayList<Relation>();
		Attribute.getInstance().clear();
		for(int i=0;i<data_Rel.size();i++){
			//Add Attributes
			String strName = data_Rel.get(i);
			ArrayList<String> tempAttrArray = new ArrayList<String>();
			for(int j=0;j<datalist_Attr.get(i).size();j++){
				tempAttrArray.add(datalist_Attr.get(i).get(j));
			}
			Relation tempRel = new Relation(strName,tempAttrArray);
			arrayRel.add(tempRel);
			
			//Add Primary Key
			for(int j=0;j<datalist_PriKey.get(i).size();j++){
				tempRel.priKeyList.add(datalist_PriKey.get(i).get(j));
			}
			
			//Add FD
			for(int j=0;j<datalist_FD.get(i).size();j++){
				String[] stringFD = datalist_FD.get(i).get(j).split("->");
				if(stringFD.length!=2){
					System.out.println("Error in createModelsFromInput: stringFD not length 2");
					return;
				}
				String[] LHS = stringFD[0].split(",");
				String LHSBit = "";
				for(int k=0;k<LHS.length;k++){
					String tempattr = Attribute.getInstance().getBitString(LHS[k]);
					if(tempattr.length()==0){
						System.out.println("Error in createModelsFromInput: attr bitString not found");
						return;
					}
					LHSBit = Attribute.OR(LHSBit, tempattr);
				}
				
				String[] RHS = stringFD[1].split(",");
				String RHSBit = "";
				
				for(int k=0;k<RHS.length;k++){
					String tempattr = Attribute.getInstance().getBitString(RHS[k]);
					if(tempattr.length()==0){
						System.out.println("Error in createModelsFromInput: attr bitString not found");
						return;
					}
					RHSBit = Attribute.OR(RHSBit, tempattr);
				}
				
				FD tempFD = new FD(LHSBit,RHSBit);
				if(!tempRel.fDList.contains(tempFD)){
					tempRel.fDList.add(tempFD);
				}
			}
			
		}
	}

	private void performDetection(){
		Violation v = new Violation();
		for(int i=0;i<arrayRel.size();i++){
			//Show R(A,B,C) with underline of current primary keys			
			Log.getInstance().println(arrayRel.get(i).getRelationDisplay()+" is in "+v.checkRelationNF(arrayRel.get(i)));
			String printString="Testing closure of all Attributes";
			Log.getInstance().println(printString);
			ArrayList<FD> fd = arrayRel.get(i).fDList;
			ArrayList<String> attrList = arrayRel.get(i).GetAttrList();
			if(fd.isEmpty()) {
				printString = "Error: There are currently no Functional Dependencies defined.";
				Log.getInstance().println(printString);
				
			}else {
				for(int x = 0; x < attrList.size(); x++) {
						printString = "";
						String closure = arrayRel.get(i).computeClosure(Attribute.getInstance().getBitString(attrList.get(x)));
						printString += "{" + attrList.get(x) + "} =";
						printString += "{" + Attribute.getInstance().getAttrString(closure) + "}";
						Log.getInstance().println(printString);
				}
			}
			//Show candidate keys
			ArrayList<String> tempCandidate = arrayRel.get(i).getCandidateKeys();
			printString = "";
			for(int j=0;j<tempCandidate.size();j++){
				if(j!=0){
					printString+=", ";
				}
				printString+='{'+tempCandidate.get(j)+'}';
			}
			Log.getInstance().println("Possible candidate keys are: " +printString);
			String nf = v.checkRelationNF(arrayRel.get(i)).trim();
			boolean is3NF, isBCNF, is2NF, is1NF;
			if(!(nf.equals("3NF") || nf.equals("BCNF"))) {
				Log.getInstance().println("List of Functional Dependencies which violate 3NF/BCNF:");
				for(int y = 0; y<fd.size();y++)
				{
					is3NF = v.check3NF(arrayRel.get(i), fd.get(y));
					if(!is3NF) {
						Log.getInstance().println(fd.get(y).toString());
					}else {
						isBCNF = v.checkBCNF(arrayRel.get(i), fd.get(y));
						if(!isBCNF) {
							Log.getInstance().println(fd.get(y).toString());
						}
					}

				}
				Log.getInstance().println("Recommendation: Please modify the Functional Dependencies or Click on Suggest tab to start Normalization.");
			}else  {
				
				Log.getInstance().println("Recommendation: No action required. The Relation is already in 3NF/BCNF.");
				
			}
			//new line for next relation
			Log.getInstance().newln();
		}
	}

	private void performSuggestion(){
		Log.getInstance().println("<b>No. of Relation:</b> "+arrayRel.size());
		
		//Do not continue if there are no relation
		if(arrayRel.isEmpty()){
			return;
		}
		
		
		Relation unionRelation = Relation.UNION(arrayRel);
		unionRelation.relName = "R";
		if(arrayRel.size()>1){
			Log.getInstance().println("<b>Combine:</b>");
		}
		
		//Show R(A,B,C) with underline of current primary keys			
		Log.getInstance().println(unionRelation.getRelationDisplay());
		Log.getInstance().println(unionRelation.getFDDisplay());
		
		
		Log.getInstance().newln();
		Log.getInstance().println("<b>Preprocessing: Split RHS of FD</b>");
		Log.getInstance().println(unionRelation.getRelationDisplay());
		unionRelation.fDList = Bernstein.splitRHS(unionRelation.fDList);
		Log.getInstance().println(unionRelation.getFDDisplay());
		
		Log.getInstance().newln();
		Log.getInstance().println("<b>Preprocessing: Remove Trivial FD</b>");
		Log.getInstance().println(unionRelation.getRelationDisplay());
		unionRelation.fDList = Bernstein.removeTrivial(unionRelation.fDList);
		Log.getInstance().println(unionRelation.getFDDisplay());

		
		Log.getInstance().newln();
		Log.getInstance().println("<b>Step 1: Eliminate Extraneous Attributes</b>");
		Log.getInstance().println(unionRelation.getRelationDisplay());
		unionRelation.fDList = Bernstein.removeExtraneousAttribute(unionRelation.fDList);
		Log.getInstance().println(unionRelation.getFDDisplay());

		
		Log.getInstance().newln();
		Log.getInstance().println("<b>Step 2: Find Covering</b>");
		Log.getInstance().println(unionRelation.getRelationDisplay());
		unionRelation.fDList = Bernstein.removeFDUsingCovering(unionRelation.fDList);
		Log.getInstance().println(unionRelation.getFDDisplay());

		Log.getInstance().newln();
		Log.getInstance().println("<b>Step 3: Partition</b>");
		ArrayList<Partition> tempPartArray = Bernstein.partitionFromFD(unionRelation.fDList);
		for(int i=0;i<tempPartArray.size();i++){
			Log.getInstance().println(tempPartArray.get(i).toString());
		}
		
		Log.getInstance().newln();
		Log.getInstance().println("<b>Step 4: Merge Groups</b>");
		tempPartArray = Bernstein.MergeProperEquivalent(tempPartArray);
		for(int i=0;i<tempPartArray.size();i++){
			Log.getInstance().println(tempPartArray.get(i).toString());
		}
		
		Log.getInstance().newln();
		Log.getInstance().println("<b>Step 5: Eliminate Transitive Dependencies</b>");
		tempPartArray = Bernstein.eliminateTransitiveDependency(tempPartArray);
		for(int i=0;i<tempPartArray.size();i++){
			Log.getInstance().println(tempPartArray.get(i).toString());
		}
		
		Log.getInstance().newln();
		Log.getInstance().println("<b>Step 6: Construct Relations</b>");
		arrayRel = Bernstein.constructRelations(tempPartArray);
		for(int i=0;i<arrayRel.size();i++){
			Log.getInstance().println(arrayRel.get(i).getRelationDisplay());
			Log.getInstance().println(arrayRel.get(i).getFDDisplay());
		}	
		Log.getInstance().println("Lossless result: "+Attribute.getInstance().checkLossless(unionRelation.fDList, Bernstein.getOriRel()));
		
		for(int f=0;f<unionRelation.fDList.size();f++){
			Log.getInstance().println("FD: "+unionRelation.fDList.get(f)+" preserved?: "+((FD)unionRelation.fDList.get(f)).checkPerserve(arrayRel));
		}
		
		Log.getInstance().newln();
		Log.getInstance().println("<b>Alternative Decomposition</b>");
		ArrayList<Relation> BCNFArray = Bernstein.convertBCNF(arrayRel);
		for(int i=0;i<BCNFArray.size();i++){
			Log.getInstance().println(BCNFArray.get(i).getRelationDisplay());
			Log.getInstance().println(BCNFArray.get(i).getFDDisplay());
		}	
	}
}
