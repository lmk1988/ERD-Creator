package logic;

import java.util.*;

//Stores 31 max attributes at the moment

public class Attribute{
	private ArrayList<String> attrList;
	private static Attribute instance;
	
	private Attribute(){
		attrList = new ArrayList<String>();
	}
	
	public static Attribute getInstance(){
		if(instance==null){
			instance = new Attribute();
		}
		return instance;
	}
	
	public void clear(){
		attrList = new ArrayList<String>();
	}
	
	public int numOfAttributes(){
		return attrList.size();
	}
	
	public String addAttribute(String variable){
		if(!attrList.contains(variable) && numOfAttributes()<31){
			attrList.add(variable);
		}
		return getBitString(variable);
	}
	
	//Returns the bitString representing the variable
	public String getBitString(String variable){
		int index =attrList.indexOf(variable);
		if(index<0){
			//check if it is multiple of variables
			if(variable.indexOf(",")>=0){
				// comma will mean it has many variables in it
				String strFinal = "";
				String[] strSplit = variable.split(",");
				for(int i=0;i<strSplit.length;i++){
					strFinal = OR(strFinal,getBitString(strSplit[i].trim()));
				}
				return strFinal;
			}
			return "";
		}else{
			return  Integer.toBinaryString((int)Math.pow(2,index));
		}
	}
	
	//returns the attribute represented by the bitString
	//For display purpose, please put {} when showing on UI
	public String getAttrString(String bitString){
		int currentLength = numOfAttributes();
		if(currentLength==0){
			return "";
		}
		
		while(bitString.length()<currentLength){
			bitString = "0"+bitString;
		}
		//bitString should only contain a single 1 and all zeros
		int nextIndex = bitString.indexOf("1");
		String tempAttr = "";
		while(nextIndex>=0){
			int attrIndex = currentLength-1-nextIndex;
			if(tempAttr.length()!=0){
				tempAttr = ","+tempAttr;
			}
			tempAttr = attrList.get(attrIndex)+tempAttr;
			nextIndex = bitString.indexOf("1", nextIndex+1);
		}
		
		return tempAttr;
	}
	
	public static String AND(String inputBit1,String inputBit2){
		if(inputBit1.length()==0 || inputBit2.length()==0){
			return "";
		}
		
		int bit1,bit2,bit3;
		
		bit1=Integer.parseInt(inputBit1,2);
		bit2=Integer.parseInt(inputBit2,2);
		bit3=bit1 & bit2;
		String tempOutput = Integer.toBinaryString(bit3);
		while(tempOutput.length()<inputBit1.length() || tempOutput.length()<inputBit2.length()){
			tempOutput = "0"+tempOutput;
		}
		return tempOutput;
	}
	
	public static String OR(String inputBit1,String inputBit2){			//OR operation between 2 string bits
		if(inputBit1.length()==0){
			return inputBit2;
		}else if(inputBit2.length()==0){
			return inputBit1;
		}
		
		int bit1,bit2,bit3;
		
		bit1=Integer.parseInt(inputBit1,2);
		bit2=Integer.parseInt(inputBit2,2);							//Convert integer to binary
		bit3=bit1 | bit2;
		String tempOutput = Integer.toBinaryString(bit3);
		while(tempOutput.length()<inputBit1.length() || tempOutput.length()<inputBit2.length()){
			tempOutput = "0"+tempOutput;
		}
		return tempOutput;
	}
	
	public static String INVERSE(String inputBit){
		if(inputBit.length()==0){
			return "";
		}
		int bit = Integer.parseInt(inputBit,2);
		String output = Integer.toBinaryString(~bit);
		return output.substring(output.length()-inputBit.length());
	}
	
	public static ArrayList<String> ALL_PROPER_SUBSET_OF(String inputBit){
		ArrayList<String> finalArray = new ArrayList<String>();
		
		//Find all single variables first
		int nextIndex = 0;
		int currentOne = inputBit.indexOf("1",nextIndex);
		while(currentOne>=0){
			String tempInput = "";
			for(int i=0;i<currentOne;i++){
				tempInput+="0";
			}
			tempInput+="1";
			for(int i=currentOne+1;i<inputBit.length();i++){
				tempInput+="0";
			}
			
			if(!IS_BIT_EQUAL(inputBit,tempInput)){
				finalArray.add(tempInput);
			}
			nextIndex=currentOne+1;
			currentOne = inputBit.indexOf("1",nextIndex);
		}
		
		//Mix and match the others using the single attributes
		int singleVariablesSize = finalArray.size();
		int currentSize = finalArray.size();
		for(int i=0;i<singleVariablesSize;i++){
			for(int j=i+1;j<currentSize;j++){
				if(Integer.parseInt(Attribute.AND(finalArray.get(i), finalArray.get(j)),2)==0){
					String merge = OR(finalArray.get(i),finalArray.get(j));
					if(!IS_BIT_EQUAL(merge,inputBit) && !finalArray.contains(merge)){
						finalArray.add(merge);
					}
				}
			}
			currentSize = finalArray.size();
		}
		
		return finalArray;
	}
	
	public static boolean IS_BIT_EQUAL(String inputBit1, String inputBit2){
		while(inputBit2.length()<inputBit1.length()){
			inputBit2 = "0"+inputBit2;
		}
		while(inputBit1.length()<inputBit2.length()){
			inputBit1 = "0"+inputBit1;
		}
		return (inputBit1.compareTo(inputBit2)==0);
	}
	
	//Counts the number of ones 
	public static int NUM_OF_ONES(String inputBit){
		int count=0;
		int index = inputBit.indexOf("1");
		while(index>=0){
			count++;
			index = inputBit.indexOf("1",index+1);
		}
		return count;
	}
	
	public static boolean IS_ALL_ONES(String inputBit){
		if(inputBit.length()==0){
			return false;
		}
		
		int bit = Integer.parseInt(inputBit,2);
		return bit==((Math.pow(2,(inputBit.length())))-1);
	}
	
	public static boolean IS_SUPERKEY(String bitStringKey, String bitStringSuper){
		if(IS_BIT_EQUAL(AND(bitStringKey,bitStringSuper),bitStringKey)){
			return true;
		}else{
			return false;
		}
	}
	
	/*
	 * checkLossless(ArrayList fdList, ArrayList relList) check if the relations are lossless with the input of FDs in fdList and all the relations in relList. 
	 * It will return true if lossless.
	 */
	public boolean checkLossless(ArrayList fdList, ArrayList relList){
		ArrayList tAttrList = new ArrayList();
		ArrayList evalRelList = new ArrayList();
		ArrayList rAttrList = new ArrayList();
		ArrayList tFDList = new ArrayList();
		tFDList=(ArrayList) fdList.clone();
		int attrSize=attrList.size();
		int totalFDcount=0;
		int lossCount=0;
		int counter=0;
		int FDcounter=0;											//Set a temp LHS FDcount to check total LHS is ok before moving to RHS
		int totalTokenL=0;
		int totalTokenR=0;
		String tempCompare="";
		String tempFD,tempFD1,tempFD2;
		StringTokenizer stzL,stzR;
		int tempIndex=-1;
		boolean result = false;
		boolean attrSlot[] = new boolean[attrSize];		
		boolean tAttrSlot[] = new boolean[attrSize];	
		
		for(int i=0;i<relList.size();i++){						//Setting up the different relation slots for evaluation
			evalRelList.add(attrSlot.clone());
		}
		for(int i=0;i<relList.size();i++){
			rAttrList=((Relation) relList.get(i)).GetAttrList();
			tAttrSlot=(boolean[]) evalRelList.get(i);
			for(int a=0;a<rAttrList.size();a++){
				tempCompare=rAttrList.get(a).toString();								//Compare with origin attribute list
				tempIndex = attrList.indexOf(tempCompare);								//Record the index of the match and mark it out in evalRelList later
				tAttrSlot[tempIndex]=true;														//Mark those found attribute in the relations as true.
			}
			evalRelList.set(i,tAttrSlot);
			tAttrSlot= new boolean[attrSize];						//Reset temp AttrSlot
			tempIndex=-1;											//Reset Index
		}
		
	while((!tFDList.isEmpty()) & totalFDcount<=(fdList.size()*2)){						//Loop again to go through another round of remaining FDs
		for(int c=0;c<tFDList.size();c++){
			tempFD=((FD)tFDList.get(c)).LHS;
			stzL= new StringTokenizer(tempFD,",");		
			totalTokenL=stzL.countTokens();
			while(stzL.hasMoreTokens()){
				tempFD1=stzL.nextToken();
				tempIndex=attrList.indexOf(tempFD1);
				for(int i=0;i<evalRelList.size();i++){					//Walk through the list to check if tempFD1 is contains in all the list
					tAttrSlot=(boolean[]) evalRelList.get(i);
					if(tAttrSlot[tempIndex]==true){
						counter++;
					}
				}
				if(counter==evalRelList.size()){						//Check if the current FD attribute contains in all relations before checking another one
					counter=0;
					FDcounter++;
					if(FDcounter==totalTokenL){
						tempFD=((FD)tFDList.get(c)).RHS;					//Setting the RHS of the FD
						stzR=new StringTokenizer(tempFD,",");		
						totalTokenR=stzR.countTokens();
								while(stzR.hasMoreTokens()){
									tempFD2=stzR.nextToken();
									tempIndex=attrList.indexOf(tempFD2);
									for(int i=0;i<evalRelList.size();i++){					//Walk through the list to check if tempFD1 is contains in all the list
										tAttrSlot=(boolean[]) evalRelList.get(i);
										tAttrSlot[tempIndex]=true;
										}
									}
						FDcounter=0;									//Reset 
						tFDList.remove(c);
					}
					continue;
				}else{
					counter=0;
					FDcounter=0;										//Reset
					break;
				}
			}
			totalFDcount++;
		}
	}	

		for(int i=0;i<evalRelList.size();i++){							//counter to check for lossless relations
			tAttrSlot=(boolean[]) evalRelList.get(i);
			for(int b=0;b<tAttrSlot.length;b++){
				if(tAttrSlot[b]){
					lossCount++;
				}
			}
			if(lossCount==tAttrSlot.length){
				result=true;											//The rel is lossless.
				break;
			}else{
				lossCount=0;
			}
		}
		return result;
	}
}
