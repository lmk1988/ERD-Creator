package logic;

import java.util.ArrayList;

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
		if(!attrList.contains(variable)){
			attrList.add(variable);
		}
		return getBitString(variable);
	}
	
	public String getBitString(String variable){
		int index =attrList.indexOf(variable);
		if(index<0){
			return "";
		}else{
			return  Integer.toBinaryString((int)Math.pow(2,index));
		}
	}
	
	public String getAttrString(String bitString){
		int currentLength = numOfAttributes();
		while(bitString.length()<currentLength){
			bitString = "0"+bitString;
		}
		//bitString should only contain a single 1 and all zeros
		int nextIndex = bitString.indexOf("1");
		String tempAttr = "";
		while(nextIndex>=0){
			
			int attrIndex = currentLength-1-nextIndex;
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
}
