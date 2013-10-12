package logic;

import java.util.*;

public class Relation {
	ArrayList<Relation> relList;
	ArrayList<String> attrList;
	ArrayList<FD> fDList;
	ArrayList unionList,joinList;
	String relName;
	public Relation(){
		relList = new ArrayList<Relation>();
		attrList = new ArrayList<String>();
		fDList = new ArrayList<FD>();
	}
	
	public Relation(String relName,ArrayList<String> attrList){
		this.relName=relName;
		this.attrList=attrList;
	}
	
	public ArrayList<Relation> GetRels(){
		return relList;
	}
	
	public ArrayList GetComputeRels(int joinType){
		if(joinType==1){
			return unionList;
		}else{
			return joinList;
		}
	}
	
	public void SetRels(String relName,ArrayList<String> attrList){
		Relation r=new Relation(relName,attrList);
		relList.add(r);												//Accumulate all the relations
	}
	
	public void SetComputeRels(Relation tempR,int joinType,ArrayList<Attribute> attrList){
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
	
	public ArrayList<String> GetAttrList(String relName){
		return attrList;
	}
	
	
	//Attr should be in bitString and not words
	public String computeClosure(String inputBit){
		return computeClosure(inputBit,fDList);
	}
	
	//Attr should be in bitString and not words
	public static String computeClosure(String inputBit,ArrayList<FD> FDs){
		String currentClosure = inputBit;
		String ClosureBefore = inputBit;
		ArrayList<FD> tempFDList = new ArrayList<FD>(FDs);//Clone
		
		do{
			for(int i=0;i<tempFDList.size();i++){
				FD currentFD = tempFDList.get(i);
				if(Attribute.AND(currentFD.LHS, currentClosure).compareTo(currentFD.LHS)==0){
					//Remove FD from temp list because it has already served its purpose of generating a larger closure
					tempFDList.remove(i);
					i--; //minus again due to previously removing it
					currentClosure = Attribute.OR(currentFD.RHS, currentClosure);
				}
			}
		}while(currentClosure.compareTo(ClosureBefore)!=0);
		
		return currentClosure;
	}
}
