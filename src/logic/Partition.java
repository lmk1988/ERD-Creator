package logic;

import java.util.*;

public class Partition {
	//Warning: developer might add a wrong FD with different LHS
	private ArrayList<FD> fDList;
	public ArrayList<FD> joinList;
	public String partitionName;
	
	public Partition(){
		fDList = new ArrayList<FD>();
		joinList = new ArrayList<FD>();
		partitionName = "";
	}
	
	public Partition(FD fd){
		this();
		fDList.add(fd);
	}
	
	public ArrayList<FD> getfDList(){
		return new ArrayList<FD>(fDList);
	}
	
	public int getFDSize(){
		return fDList.size();
	}
	
	public void addFD(FD fd){
		if(fDList.isEmpty() || (fd.LHS.compareTo(getLHS())==0 && !fDList.contains(fd))){
			fDList.add(fd);
		}else if(!joinList.isEmpty()){
			//Check if joinList has a LHS as the fd
			for(int i=0;i<joinList.size();i++){
				if(joinList.get(i).LHS.compareTo(fd.LHS)==0){
					fDList.add(fd);
					return;
				}
			}
		}
	}
	
	public void addFDs(ArrayList<FD> fd){
		for(int i=0;i<fd.size();i++){
			addFD(fd.get(i));
		}
	}
	
	public void removeFD(FD fd){
		fDList.remove(fd);
	}
	
	public String getLHS(){
		//Only returns the LHS of FDs before join.
		//Reason being, after join, if the join LHS is functionalEquivalent to something else, it should be covered by F+
		if(fDList.size()>0){
			return fDList.get(0).LHS;
		}else{
			return "";
		}
	}
	
	@Override 
	public String toString(){
		String printString = "";
		
		for(int i=0;i<fDList.size();i++){
			if(i!=0){
				printString+="&emsp;";
			}
			printString+=fDList.get(i);
		}
		printString=partitionName+'{'+printString+'}';
		
		if(!joinList.isEmpty()){
			String joinString = "";
			for(int i=0;i<joinList.size();i++){
				if(i!=0){
					joinString+="&emsp;";
				}
				joinString+=joinList.get(i);
			}
			joinString = "J{"+joinString+"}";
			printString = joinString +"<br/>"+ printString;
		}
		
		return printString;
	}
}
