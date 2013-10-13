package ui;

import java.awt.EventQueue;

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

public class Home {

	private JFrame frmNfDetector;
	private final Action action = new SwingAction();
	private JTextArea txtrHi;
	private JLabel lblTheKeysAre;
	private JTextField textField;

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
		frmNfDetector.setBounds(100, 100, 450, 300);
		frmNfDetector.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmNfDetector.getContentPane().setLayout(null);
		
		JLabel lblEnterFd = new JLabel("Enter FD:");
		lblEnterFd.setBounds(10, 21, 80, 14);
		frmNfDetector.getContentPane().add(lblEnterFd);
		
		txtrHi = new JTextArea();
		txtrHi.setText("hi");
		txtrHi.setColumns(5);
		txtrHi.setRows(5);
		txtrHi.setBounds(83, 16, 154, 99);
		frmNfDetector.getContentPane().add(txtrHi);
		
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//FD fd = new FD();
				//textField.setText(fd.processFD(txtrHi.getText()));
			}
		});
		btnSubmit.setBounds(247, 17, 89, 23);
		frmNfDetector.getContentPane().add(btnSubmit);
		
		lblTheKeysAre = new JLabel("The keys are");
		lblTheKeysAre.setBounds(10, 140, 80, 14);
		frmNfDetector.getContentPane().add(lblTheKeysAre);
		
		textField = new JTextField();
		textField.setBounds(83, 137, 153, 20);
		frmNfDetector.getContentPane().add(textField);
		textField.setColumns(10);
		
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
