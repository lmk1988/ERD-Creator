package logic;

import java.util.*;
/*
 * Only allow to store up to 31 variables as each 1 bits is mapped to a var.
 * 
 */
public class Attribute {
	
	Hashtable attrTab;
	int bitCount=1;
	
	public Attribute(){
		attrTab = new Hashtable(); 
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
	
	public String AND(String inputBit1,String inputBit2){
		int bit1,bit2,bit3;
		
		bit1=Integer.parseInt(inputBit1,2);
		bit2=Integer.parseInt(inputBit2,2);
		bit3=bit1 & bit2;
		return Integer.toBinaryString(bit3);
	}
	
	public String OR(String inputBit1,String inputBit2){			//OR operation between 2 string bits
		int bit1,bit2,bit3;
		
		bit1=Integer.parseInt(inputBit1,2);
		bit2=Integer.parseInt(inputBit2,2);							//Convert integer to binary
		bit3=bit1 | bit2;
		return Integer.toBinaryString(bit3);
	}
	
	public String GetBinAttr(String input){
		String result = (String) attrTab.get(input);
		return result;
	}
	
	public void SetCandidKey(ArrayList key){
		
	}
	
	
}
