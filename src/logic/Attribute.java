package logic;

import java.util.*;

/*
 * Only allow to store up to 8 variables as each 1 bits is mapped to a var.
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
		for(int i=0;i<=(8-tempBit.length());i++){		//Fill the front of 1 with zero
			if(i==(8-tempBit.length())){
				tempFill+=tempBit;
			}else{
				tempFill+="0";
			}
		}
		bitCount = bitCount << 1;						//Shifting the bit pos to left by 1
		attrTab.put(tempFill, input);					//Storing the input into hashMap
		return tempFill;
	}
	
	public String GetBinAttr(String input){
		String result = (String) attrTab.get(input);
		return result;
	}
	
	public void SetCandidKey(ArrayList key){
		
	}
	
	
}

