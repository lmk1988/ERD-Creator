package ui;

import javax.swing.JTextPane;

public class Log {
	private static Log instance;
	private JTextPane logView;
	private String innerText;
	private static boolean bol_log;
	
	private Log(){
		logView = null;
		bol_log = true;
		innerText = ""; //need to use innerText because if you use getText() it returns all the HTML as well
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
	
	public void println(String text){
		if(logView!=null && bol_log){
			innerText=innerText+text+"<br/>";
			logView.setText(innerText);
		}
	}
	
	public void newln(){
		if(logView!=null && bol_log){
			innerText=innerText+"<br/>";
			logView.setText(innerText);
		}
	}
	
	public void print(String text){
		if(logView!=null && bol_log){
			innerText=innerText+text;
			logView.setText(innerText);
		}
	}
	
	public void clearLog(){
		if(logView!=null){
			innerText="";
			logView.setText(innerText);
		}
	}
}
