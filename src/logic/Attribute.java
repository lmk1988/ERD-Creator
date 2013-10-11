package logic;

import java.util.*;
/*
 * Only allow to store up to 31 variables as each 1 bits is mapped to a var.
 * 
 */
public class Attribute{
	
	Hashtable<String,String> attrTab;
	int bitCount=1;
	
	public Attribute(){
		attrTab = new Hashtable<String,String>(); 
	}
	
	public Attribute(Attribute clone){
		attrTab = new Hashtable<String,String>(clone.attrTab);
		bitCount = clone.bitCount;
	}
	
	public String AddAttr(String input){
		String tempBit;
		String tempFill="";
		
		tempBit=String.valueOf(Integer.toBinaryString(bitCount));
		for(int i=0;i<=(31-tempBit.length());i++){		//Fill the front of 1 with zero
			if(i==(31-tempBit.length())){
				tempFill+=tempBit;
			}else{
				tempFill+="0";
			}
		}
		bitCount = bitCount << 1;						//Shifting the bit pos to left by 1
		attrTab.put(tempFill, input);					//Storing the input into hashMap
		return tempFill;
	}
	
	public static String AND(String inputBit1,String inputBit2){
		int bit1,bit2,bit3;
		
		bit1=Integer.parseInt(inputBit1,2);
		bit2=Integer.parseInt(inputBit2,2);
		bit3=bit1 & bit2;
		return Integer.toBinaryString(bit3);
	}
	
	public static String OR(String inputBit1,String inputBit2){			//OR operation between 2 string bits
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
	
	public String GetBinAttr(String input){
		String result = (String) attrTab.get(input);
		return result;
	}
	
	public static String INVERSE(String inputBit){
		if(inputBit.length()==0){
			return "";
		}
		int bit = Integer.parseInt(inputBit,2);
		String output = Integer.toBinaryString(~bit);
		return output.substring(output.length()-inputBit.length());
	}
	
	public static ArrayList<String> ALL_SUBSET_OF(String inputBit){
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
			finalArray.add(tempInput);
			nextIndex=currentOne+1;
			currentOne = inputBit.indexOf("1",nextIndex);
		}
		
		//Mix and match the others using the single attributes
		int singleVariablesSize = finalArray.size();
		int currentSize = finalArray.size();
		for(int i=0;i<singleVariablesSize;i++){
			for(int j=i+1;j<currentSize;j+=1+i){
				String merge = OR(finalArray.get(i),finalArray.get(j));
				finalArray.add(merge);
			}
			currentSize = finalArray.size();
		}
		
		return finalArray;
	}
	
	public void SetCandidKey(ArrayList key){
		
	}
	
}
