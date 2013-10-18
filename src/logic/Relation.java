package logic;

import java.util.*;

public class Relation {
	ArrayList<String> attrList; //stores Alphabets only
	public ArrayList<FD> fDList;
	public ArrayList<String> priKeyList; 
	public String relName;
	
	public Relation(){
		attrList = new ArrayList<String>();
		fDList = new ArrayList<FD>();
		priKeyList = new ArrayList<String>();
		relName = "";
	}
	
	public Relation(String relName,ArrayList<String> attrList){
		this();
		this.relName=relName;
		this.attrList=attrList;
		for(int i=0;i<attrList.size();i++){
			Attribute.getInstance().addAttribute(attrList.get(i));
		}
	}
	
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
		
		Relation returnRelation = new Relation(rel1.relName+" U "+rel2.relName,tempAttrList);
		returnRelation.fDList = tempFDList;
		
		return returnRelation;
	}

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
	
	public static Relation INTERSECT(Relation rel1,Relation rel2){
		//GetAttrList will return a clone of that list
		ArrayList<String> tempAttrList1 = rel1.GetAttrList(); 
		ArrayList<String> tempAttrList2 = rel2.GetAttrList();
		ArrayList<String> finalAttrList = new ArrayList<String>();
		//
		for(int i=0;i<tempAttrList1.size();i++){
			for(int j=0;j<tempAttrList2.size();j++){
				if(tempAttrList1.get(i).compareTo(tempAttrList2.get(j))==0){
					finalAttrList.add(tempAttrList2.get(j));
					tempAttrList2.remove(j);
					tempAttrList1.remove(i);
					j--;
					i--;
					break;
				}
			}
		}
		return new Relation(rel1.relName+" Intersect "+rel2.relName,finalAttrList);
	}
	
	public static Relation INTERSECT(ArrayList<Relation> relList){
		if(relList.isEmpty()){
			return new Relation();
		}
		
		Relation finalRel = relList.get(0);
		for(int i=1;i<relList.size();i++){
			finalRel = INTERSECT(finalRel,relList.get(i));
		}
		return finalRel;
	}
	
	public ArrayList<String> GetAttrList(){
		return new ArrayList<String>(attrList);
	}
	
	public int numOfAttr(){
		return attrList.size();
	}
	
	public String computeClosure(String inputBit){
		return computeClosure(inputBit,fDList);
	}

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
	
	public ArrayList<String> getCandidateBitStrings(){
		ArrayList<String> tempArrayList = new ArrayList<String>();
		String attrBitString = getAttrBitString();
		ArrayList<String> subSets = Attribute.ALL_PROPER_SUBSET_OF(attrBitString);

		for(int i=0;i<subSets.size();i++){
			if(!tempArrayList.isEmpty() && Attribute.NUM_OF_ONES(tempArrayList.get(0))<Attribute.NUM_OF_ONES(subSets.get(i))){
				break;
			}
			
			String closure = computeClosure(subSets.get(i));
			if(Attribute.IS_BIT_EQUAL(Attribute.AND(closure, attrBitString),attrBitString)){
				tempArrayList.add(subSets.get(i));
			}
		}
		
		if(tempArrayList.isEmpty()){
			tempArrayList.add(attrBitString);
		}
		
		return tempArrayList;
	}
	
	public ArrayList<String> getCandidateKeys(){
		ArrayList<String> tempArrayList = getCandidateBitStrings();
		ArrayList<String> tempAttrList = new ArrayList<String>();
		for(int i=0;i<tempArrayList.size();i++){
			tempAttrList.add(Attribute.getInstance().getAttrString(tempArrayList.get(i)));
		}
		return tempAttrList;
	}
	
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
		
		return relName+"("+printString+")";
	}

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
