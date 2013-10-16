package ui;

import javax.swing.JTextPane;

public class Log {
	private static Log instance;
	private JTextPane logView;
	
	private Log(){
		logView = null;
	}
	
	public static Log getInstance(){
		if(instance==null){
			instance = new Log();
		}
		return instance;
	}
	
	public void setLogView(JTextPane view){
		logView = view;
	}
	
	public void printLog(String text){
		if(logView!=null){
			logView.setText(logView.getText()+"<br/>"+text);
		}
	}
	
	public void clearLog(){
		if(logView!=null){
			logView.setText("");
		}
	}
}
