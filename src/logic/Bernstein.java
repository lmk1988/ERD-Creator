package logic;

import java.util.*;

public class Bernstein {
	
	public static ArrayList<FD> removeTrivial(ArrayList<FD> array){
		ArrayList<FD> tempArray = new ArrayList<FD>(array);
		for(int i=0;i<tempArray.size();i++){
			FD currentFD = tempArray.get(i);
			if(currentFD.LHS.compareTo(currentFD.RHS)==0){
				//LHS and RHS is the same
				tempArray.remove(i);
				i--;
				continue;
			}
			
			String compare = Attribute.AND(currentFD.LHS, currentFD.RHS);
			if(Integer.parseInt(compare,2)>0){
				//RHS contains some or all of LHS
				currentFD.RHS = Attribute.AND(currentFD.RHS, Attribute.INVERSE(compare));
			}
		}
		return tempArray;
	}
	
	//Input an arrayList of FD
	public static ArrayList<FD> removeExtraneousAttribute(ArrayList<FD> array){
		//Require an array of FD
		for(int i=0;i<array.size();i++){
			//For each LHS, check if it can be reduced smaller by determining if there is a smaller subset whose closure can reach this LHS
			String tempAttri = array.get(i).LHS;
			
			//For each letter, find the closure of it and see if it can reach the LHS. If yes, remove all other attributes
			//If all letters could not reach the LHS, use combination of them
		}
		return new ArrayList<FD>();
	}
	
	//proper equivalent
	//Use LHS of any of the FDs in the partition, 
	//find closure using the FD in the partition. 
	//Find a partition with LHS inside that closure. 
	//Do closure for that LHS to see if it includes the first LHS. 
	//If it is, i means they are functionally equivalent and thus we should merge the partition.
	
	//F+
	//For each FD, try to see if it can be expanded further
	
	//transitive dependency
	//Find F+, compare all the RHS with each. Group those with similar RHS
	//For each group, there is a transitive dependency if one LHS closure includes another LHS
	//but that LHS closure does not include that
}
