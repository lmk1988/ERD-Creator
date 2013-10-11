package logic;

import java.util.*;

public class Relation {
	ArrayList relList,attrList,unionList,joinList;
	String relName;
	public Relation(){
		relList = new ArrayList();
		attrList = new ArrayList();
	}
	
	public Relation(String relName,ArrayList attrList){
		this.relName=relName;
		this.attrList=attrList;
	}
	
	public ArrayList GetRels(){
		return relList;
	}
	
	public ArrayList GetComputeRels(int joinType){
		if(joinType==1){
			return unionList;
		}else{
			return joinList;
		}
	}
	
	public void SetRels(String relName,ArrayList attrList){
		Relation r=new Relation(relName,attrList);
		relList.add(r);												//Accumulate all the relations
	}
	
	public void SetComputeRels(Relation tempR,int joinType,ArrayList attrList){
		ArrayList unionList = new ArrayList();
		ArrayList joinList = new ArrayList();
		if(joinType==1){
			tempR.unionList=attrList;
		}else if(joinType==2){
			tempR.joinList=attrList;
		}
	}
	
	public Relation UnionRel(Relation tempR){
		ArrayList tempTotalRel = new ArrayList();
		ArrayList tempAttrList = new ArrayList();
		String compare;
		
		//Relation tempR=new Relation();
		for(int i=0;i<relList.size();i++){								//Looping through the different relations
			tempAttrList=((Relation)relList.get(i)).attrList;
			for(int a=0;a<tempAttrList.size();a++){						//Pass in the attribute from the supplied param for comparing 
				compare = (String)tempAttrList.get(a);
				if(tempTotalRel.size()==0){
					tempTotalRel.add(compare);
				}else{
					for(int b=0;b<tempTotalRel.size();b++){				//Loop through the Overall relation attrList to compare duplicate attribute
						if((tempTotalRel.get(b)).equals(compare)){
							break;
						}
						if(b==tempTotalRel.size()-1){
							tempTotalRel.add(compare);					//Add in the compare new attribute if reaches the end of the Overall relation attrList
							break;
						}
					}
				}
			}
		}	
		tempR.SetComputeRels(tempR,1, tempTotalRel);						//Return back the Join Relations
		return tempR;
	}
	
	public Relation IntersectRel(Relation tempR){
		ArrayList tempTotalRel = new ArrayList();
		ArrayList tempIntersectList = new ArrayList();
		ArrayList tempAttrList = new ArrayList();
		String compare;
		
		//Relation tempR=new Relation();
		for(int i=0;i<relList.size();i++){								//Looping through the different relations
			tempAttrList=((Relation)relList.get(i)).attrList;
			for(int a=0;a<tempAttrList.size();a++){						//Pass in the attribute from the supplied param for comparing 
				compare = (String)tempAttrList.get(a);
				if(tempTotalRel.size()==0){
					tempTotalRel.add(compare);
				}else{
					for(int b=0;b<tempTotalRel.size();b++){				//Loop through the Overall relation attrList to compare duplicate attribute
						if((tempTotalRel.get(b)).equals(compare)){
							tempIntersectList.add(compare);
							break;
						}
						if(b==tempTotalRel.size()-1){
							tempTotalRel.add(compare);					//Add in the compare new attribute if reaches the end of the Overall relation attrList
							break;
						}
					}
				}
			}
		}	
		tempR.SetComputeRels(tempR,2,tempIntersectList);						//Return back the Intersect Relations
		return tempR;
	}
	
	public ArrayList GetAttrList(String relName){
		return attrList;
	}
	
}
