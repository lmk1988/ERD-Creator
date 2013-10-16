package ui;

import javax.swing.JTextPane;

public class Log {
	private static Log instance;
	private JTextPane logView;
	private static boolean bol_log;
	
	private Log(){
		logView = null;
		bol_log = true;
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
	
	//This function is for Bernstein where some methods that do not require logging sometimes
	public void setLogging(boolean bool){
		bol_log = bool;
	}
	
	public void printLog(String text){
		if(logView!=null && bol_log){
			logView.setText(logView.getText()+"<br/>"+text);
		}
	}
	
	public void clearLog(){
		if(logView!=null){
			logView.setText("");
		}
	}
}
