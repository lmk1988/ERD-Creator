package logic;

import java.util.*;

public class Relation {
	ArrayList<String> attrList; //stores Alphabets only
	public ArrayList<FD> fDList;
	public ArrayList<String> priKeyList; 
	public String relName;
/**
 * Create a new Relation
 */
	public Relation(){
		attrList = new ArrayList<String>();
		fDList = new ArrayList<FD>();
		priKeyList = new ArrayList<String>();
		relName = "";
	}
	/**
	 * Create a new Relation
	 * @param relName
	 * @param attrList
	 */
	public Relation(String relName,ArrayList<String> attrList){
		this();
		this.relName=relName;
		this.attrList=attrList;
		for(int i=0;i<attrList.size();i++){
			Attribute.getInstance().addAttribute(attrList.get(i));
		}
	}
/**
 * Create a new Relation 
 * @param rel
 */
	public Relation(Relation rel){
		relName = rel.relName;
		attrList = new ArrayList<String>(rel.attrList);
		fDList = new ArrayList<FD>(rel.fDList);
		priKeyList = new ArrayList<String>(rel.priKeyList);
	}
	/**
	 * Union 2 relations together
	 * @param rel1
	 * @param rel2
	 * @return Relation
	 */
	public static Relation UNION(Relation rel1, Relation rel2){
		ArrayList<String> tempAttrList = new ArrayList<String>();
		
		for(int i=0;i<rel1.attrList.size();i++){
			if(!tempAttrList.contains(rel1.attrList.get(i))){
				tempAttrList.add(rel1.attrList.get(i));
			}
		}
		
		for(int i=0;i<rel2.attrList.size();i++){
			if(!tempAttrList.contains(rel2.attrList.get(i))){
				tempAttrList.add(rel2.attrList.get(i));
			}
		}
		
		//Merge FD list as well
		ArrayList<FD> tempFDList = new ArrayList<FD>();
		for(int i=0;i<rel1.fDList.size();i++){
			if(!tempFDList.contains(rel1.fDList.get(i))){
				tempFDList.add(rel1.fDList.get(i));
			}
		}
		
		for(int i=0;i<rel2.fDList.size();i++){
			if(!tempFDList.contains(rel2.fDList.get(i))){
				tempFDList.add(rel2.fDList.get(i));
			}
		}
		
		//leave primary key empty though
		
		Relation returnRelation = new Relation(rel1.relName+" U "+rel2.relName,tempAttrList);
		returnRelation.fDList = tempFDList;
		
		return returnRelation;
	}
	/**
	 * Union all the relations
	 * @param relList
	 * @return Relation
	 */
	public static Relation UNION(ArrayList<Relation> relList){
		if(relList.isEmpty()){
			return new Relation();
		}
		
		Relation finalRel = relList.get(0);
		for(int i=1;i<relList.size();i++){
			finalRel = UNION(finalRel,relList.get(i));
		}
		return finalRel;
	}

	/**
	 * Get list of attributes
	 * @return ArrayList<String>
	 */
	public ArrayList<String> GetAttrList(){
		return new ArrayList<String>(attrList);
	}
	/**
	 * Get the number of attribute
	 * @return int
	 */
	public int numOfAttr(){
		return attrList.size();
	}
	/**
	 * Compute the closure
	 * @param inputBit
	 * @return String
	 */
	public String computeClosure(String inputBit){
		return computeClosure(inputBit,fDList);
	}
	/**
	 * Compute the Closure with addition input of FDs
	 * @param inputBit
	 * @param FDs
	 * @return String
	 */
	public static String computeClosure(String inputBit,ArrayList<FD> FDs){
		String currentClosure = inputBit;
		String ClosureBefore;
		ArrayList<FD> tempFDList = new ArrayList<FD>(FDs);//Clone
		
		do{
			ClosureBefore = currentClosure;
			for(int i=0;i<tempFDList.size();i++){
				FD currentFD = tempFDList.get(i);
				if(Attribute.IS_BIT_EQUAL(Attribute.AND(currentFD.LHS, currentClosure),currentFD.LHS)){
					//Remove FD from temp list because it has already served its purpose of generating a larger closure
					tempFDList.remove(i);
					i--; //minus again due to previously removing it
					currentClosure = Attribute.OR(currentFD.RHS, currentClosure);
				}
			}
		}while(!Attribute.IS_BIT_EQUAL(currentClosure,ClosureBefore) && !Attribute.IS_ALL_ONES(currentClosure));
		
		return currentClosure;
	}
	/**
	 * Get attribute in bit string
	 * @return String
	 */
	public String getAttrBitString(){
		if(attrList.isEmpty()){
			return "";
		}
		
		String bitString = Attribute.getInstance().getBitString(attrList.get(0));
		for(int i=1;i<attrList.size();i++){
			String nextAttrBitString = Attribute.getInstance().getBitString(attrList.get(i));
			bitString = Attribute.OR(bitString, nextAttrBitString);
		}
		
		return bitString;
	}
	/**
	 * Get list of candidate keys in bit string
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getCandidateBitStrings(){
		ArrayList<String> tempArrayList = new ArrayList<String>();
		String attrBitString = getAttrBitString();
		ArrayList<String> subSets = Attribute.ALL_PROPER_SUBSET_OF(attrBitString);

		for(int i=0;i<subSets.size();i++){
			if(!tempArrayList.isEmpty() && Attribute.NUM_OF_ONES(tempArrayList.get(0))<Attribute.NUM_OF_ONES(subSets.get(i))){
				continue;
			}
			
			String closure = computeClosure(subSets.get(i));
			if(Attribute.IS_BIT_EQUAL(Attribute.AND(closure, attrBitString),attrBitString)){
				while(subSets.get(i).length()<Attribute.getInstance().numOfAttributes()){
					subSets.set(i, "0"+subSets.get(i));
				}
				tempArrayList.add(subSets.get(i));
			}
		}
		
		if(tempArrayList.isEmpty()){
			tempArrayList.add(attrBitString);
		}
		
		return tempArrayList;
	}
	/**
	 * Get list of candidate keys
	 * @return ArrayList
	 */
	public ArrayList<String> getCandidateKeys(){
		ArrayList<String> tempArrayList = getCandidateBitStrings();
		ArrayList<String> tempAttrList = new ArrayList<String>();
		for(int i=0;i<tempArrayList.size();i++){
			tempAttrList.add(Attribute.getInstance().getAttrString(tempArrayList.get(i)));
		}
		return tempAttrList;
	}
	/**
	 * Get Relation attribute and also print out the primary key for display 
	 * @return String
	 */
	public String getRelationDisplay(){
		//Show R(A,B,C) with underline of current primary keys
		String printString = "";
		for(int j=0;j<priKeyList.size();j++){
			if(j!=0){
				printString+=", ";
			}
			printString+="<u>"+priKeyList.get(j)+"</u>";
		}
		
		for(int j=0;j<attrList.size();j++){
			boolean bol_shouldPrint = true;
			for(int k=0;k<priKeyList.size();k++){
				if(priKeyList.get(k).indexOf(attrList.get(j))>=0){
					bol_shouldPrint=false;
					break;
				}
			}
			if(bol_shouldPrint){
				if(printString.length()!=0){
					printString+=", ";
				}
				printString+=attrList.get(j);
			}
		}
		
		String finalPrint = relName+"("+printString+")";
		
		ArrayList<String> tempPriKeyList = new ArrayList<String>(priKeyList);
		for(int j=0;j<tempPriKeyList.size();j++){
			//Check for primary keys that have the same keys
			//only pick one for those that have the same key
			for(int k=j+1;k<tempPriKeyList.size();k++){
				String[] compare1 = tempPriKeyList.get(j).split(",");
				String[] compare2 = tempPriKeyList.get(k).split(",");
				boolean bol_found = false;
				for(int m=0;m<compare1.length && bol_found==false;m++){
					for(int n=0;n<compare2.length && bol_found==false;n++){
						if(compare1[m].trim().compareTo(compare2[n].trim())==0){
							tempPriKeyList.remove(k);
							k--;
							bol_found=true;
						}
					}
				}
			}
		}
		
		
		//Copy and paste the same printing algo again
		if(tempPriKeyList.size()!=priKeyList.size()){
			finalPrint+=" = ";
			printString="";
			
			for(int j=0;j<tempPriKeyList.size();j++){
				if(j!=0){
					printString+=", ";
				}
				printString+="<u>"+tempPriKeyList.get(j)+"</u>";
			}
			
			for(int j=0;j<attrList.size();j++){
				boolean bol_shouldPrint = true;
				for(int k=0;k<tempPriKeyList.size();k++){
					if(tempPriKeyList.get(k).indexOf(attrList.get(j))>=0){
						bol_shouldPrint=false;
						break;
					}
				}
				if(bol_shouldPrint){
					if(printString.length()!=0){
						printString+=", ";
					}
					printString+=attrList.get(j);
				}
			}
			finalPrint+= relName+"("+printString+")";
		}
		
		return finalPrint;
	}
	/**
	 * Get Left hand side and Right hand side FDs and display properly with " -> "
	 * @return String
	 */
	public String getFDDisplay(){
		String printString = "";
		for(int i=0;i<fDList.size();i++){
			if(i!=0){
				printString+="&emsp;";
			}
			
			String LHS = Attribute.getInstance().getAttrString(fDList.get(i).LHS);
			String RHS = Attribute.getInstance().getAttrString(fDList.get(i).RHS);
			printString+=LHS+"->"+RHS;
		}
		return printString;
	}
}
