package logic;

public class FD implements Comparable<FD>{
	public String LHS,RHS;
	
	public FD(String LHS,String RHS){
		this.LHS = LHS;
		this.RHS = RHS;
	}
	
	public FD(FD clone){
		this.LHS = clone.LHS;
		this.RHS = clone.RHS;
	}

	public String processFD(String input) {
		return input;
		
	}

	@Override
	public int compareTo(FD o) {
		if(LHS.compareTo(o.LHS)==0 && RHS.compareTo(o.RHS)==0){
			return 0;
		}else if(LHS.compareTo(o.LHS)!=0){
			return LHS.compareTo(o.LHS);
		}else{
			return RHS.compareTo(o.RHS);
		}
	}
	
	@Override 
	public boolean equals(Object aThat) {
	     if (this == aThat){
	    	 return true;
	     }
	     if (!(aThat instanceof FD)){
	    	 return false;
	     }

	     FD that = (FD)aThat;
	     if(this.LHS.compareTo(that.LHS)==0 && this.RHS.compareTo(that.RHS)==0){
	    	 return true;
	     }else{
	    	 return false;
	     }
	}
	
	@Override 
	public String toString(){
		return LHS+"->"+RHS;
	}
}
