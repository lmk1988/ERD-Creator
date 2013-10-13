package logic;

import java.util.*;

public class Partition {
	ArrayList<FD> fDList;
	ArrayList<FD> joinList;
	
	public Partition(){
		fDList = new ArrayList<FD>();
		joinList = new ArrayList<FD>();
	}
	
	public Partition(FD fd){
		this();
		fDList.add(fd);
	}
	
	public String getLHS(){
		if(fDList.size()>0){
			return fDList.get(0).LHS;
		}else{
			return "";
		}
	}
}
