package logic;

import java.util.*;

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
		String attrLHS = Attribute.getInstance().getAttrString(LHS);
		String attrRHS = Attribute.getInstance().getAttrString(RHS);

		if(attrLHS.length()==0 || attrRHS.length()==0){
			return LHS+"->"+RHS;
		}else{
			return attrLHS+"->"+attrRHS;
		}
	}
	
	/*
	 * checkPerserve(relList) - Check the current FD for preserve through all the input relation lists.
	 * eg: fd2.checkPerserve(relList); will return true if fd2 is preserved else return false.
	 * To check for all FDs, need to loop through all the FDs and evaluate whether all are true which show these FDs are preserving. 
	 */
	public boolean checkPerserve(ArrayList relList){
		Relation tempR;
		ArrayList tempRAttr;
		String tempAttr;
		StringTokenizer stzL;
		StringTokenizer stzR;
		int tokenSizeL=0;
		int checkTokenQuota=0;
		int checkToken=0;
		
				stzL = new StringTokenizer(LHS,",");								//Check the LHS to separate attribute via ","
				stzR = new StringTokenizer(RHS,",");								//Check the RHS to separate attribute via ","
				for(int b=0;b<relList.size();b++){									//Check if the relation contains the attribute
					tempR=(Relation)relList.get(b);
					tempRAttr=tempR.GetAttrList();
					checkTokenQuota=stzL.countTokens() + stzR.countTokens(); 		//Add the total size of LHS and RHS tokens together
					tokenSizeL=stzL.countTokens();
					for(int c=0;c<checkTokenQuota;c++) {							//Loop through the LHS to check if inside the relation
						if(c<tokenSizeL){
							tempAttr=stzL.nextToken();
						}else{
							tempAttr=stzR.nextToken();
						}
							if(tempRAttr.contains(tempAttr)){
								checkToken++;										//Adding to the number of FD attribute found in relation
								if(checkToken==checkTokenQuota){									//Enter Else if FD attribute found in relation
									return true;
								}
							}else{																	//Enter Else if FD attribute not found in relation
								checkToken=0;	
								stzL = new StringTokenizer(LHS,",");								//Reset the stzL 
								stzR = new StringTokenizer(RHS,",");								//Reset the stzR 
								break;
							}
					}
				}
				if(checkToken==checkTokenQuota){
					return true;
				}else{
					return false;
				}
	}
}
