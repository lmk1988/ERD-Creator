package logic;

import java.util.*;

public class Bernstein {
	
	public static ArrayList<FD> removeTrivial(ArrayList<FD> fDArray){
		ArrayList<FD> tempArray = new ArrayList<FD>(fDArray);
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
	public static ArrayList<FD> removeExtraneousAttribute(ArrayList<FD> fDArray){
		ArrayList<FD> tempArray = new ArrayList<FD>(fDArray);
		tempArray = removeTrivial(tempArray);
		//Require an array of FD
		for(int i=0;i<tempArray.size();i++){
			//For each LHS, check if it can be reduced smaller by determining if there is a smaller subset whose closure can reach this LHS
			String tempLHS = tempArray.get(i).LHS;
			ArrayList<String> tempProperSubset = Attribute.ALL_PROPER_SUBSET_OF(tempLHS);
			for(int j=0;j<tempProperSubset.size();j++){
				//For each letter, find the closure of it and see if it can reach the LHS. If yes, remove all other attributes
				//the ALL_PROPER_SUBSET_OF will return the smallest subset first
				ArrayList<FD> exclusionArray = new ArrayList<FD>(tempArray);
				//exclusionArray includes all FD except the current FD we are checking
				exclusionArray.remove(tempArray.get(i));
				//Find closure
				String closure = Relation.computeClosure(tempProperSubset.get(j), exclusionArray);
				//If it can reach LHS, it would be the minimum variables needed
				if(Attribute.AND(closure, tempLHS).compareTo(tempLHS)==0){
					tempArray.get(i).LHS=tempProperSubset.get(j);
					break;
				}
			}
		}
		return tempArray;
	}
	
	public static ArrayList<FD> removeFDUsingCovering(ArrayList<FD> fDArray){
		ArrayList<FD> tempArray = new ArrayList<FD>(fDArray);
		tempArray = removeTrivial(tempArray);
		for(int i=0;i<tempArray.size();i++){
			ArrayList<FD> exclusionArray = new ArrayList<FD>(tempArray);
			//exclusionArray includes all FD except the current FD we are checking
			exclusionArray.remove(tempArray.get(i));
			//Find closure of LHS using the other FDs
			String closure = Relation.computeClosure(tempArray.get(i).LHS, exclusionArray);
			//If RHS is inside the closure, it means that this FD can be remove due to covering
			if(Attribute.AND(closure, tempArray.get(i).RHS).compareTo(tempArray.get(i).RHS)==0){
				tempArray.remove(i);
				i--;
			}
		}
		return tempArray;
	}
	
	//F+
	//For each FD, try to see if it can be expanded further
	public static ArrayList<FD> expandFDs(ArrayList<FD> fDArray){
		ArrayList<FD> tempArray = new ArrayList<FD>(fDArray);
		tempArray = removeTrivial(tempArray);
		//Brute force search and add if you can create a new FD via transitive
		for(int i=0;i<tempArray.size();i++){
			for(int j=i+1;j<tempArray.size();j++){
				//If there is a transitive and the LHS is not RHS (trivial case)
				if(tempArray.get(i).RHS.compareTo(tempArray.get(j).LHS)==0 && tempArray.get(i).LHS.compareTo(tempArray.get(j).RHS)!=0){
					FD tempFD = new FD(tempArray.get(i).LHS,tempArray.get(j).RHS);
					//Check if tempArray already contains this tempFD
					//(FD implements custom comparable so it should be ok to do this)
					if(!tempArray.contains(tempFD)){
						tempArray.add(tempFD);
					}
				}
			}
		}
		//Remove trivial again just in case
		tempArray = removeTrivial(tempArray);
		return tempArray;
	}
	
	
	//partition from FDs
	public static ArrayList<Partition> partitionFromFD(ArrayList<FD> fDArray){
		ArrayList<FD> tempArray = new ArrayList<FD>(fDArray);
		ArrayList<Partition> partArray = new ArrayList<Partition>();
		
		for(int i=0;i<tempArray.size();i++){
			String LHS = tempArray.get(i).LHS;
			boolean bol_found = false;
			//Find existing partition that has the same LHS
			for(int j=0;j<partArray.size() && bol_found==false;j++){
				if(partArray.get(j).getLHS().compareTo(LHS)==0){
					partArray.get(j).fDList.add(tempArray.get(i));
					bol_found=true;
				}
			}
			
			//If no partition found, add a new partition
			if(bol_found==false){
				partArray.add(new Partition(tempArray.get(i)));
			}
		}
		
		return partArray;
	}
	
	
	//transitive dependency
	/*public static ArrayList<FD> eliminateTransitiveDependency(ArrayList<FD> fDArray){
		ArrayList<FD> tempArray = new ArrayList<FD>(fDArray);
		tempArray = removeTrivial(tempArray);
		ArrayList<FD> fPlus = expandFDs(fDArray);
		//Find F+, compare all the RHS with each. Group those with similar RHS
		
		
		
		//For each group, there is a transitive dependency if one LHS closure includes another LHS
		//but that LHS closure does not include that
		
		return tempArray;
	}*/
	
	
	
	
	//proper equivalent
	//Return an array list of FD that is properEquivalent e.g. A->B B->A
	//Only checks between 2 partitions. Please loop it if required to check for a list of partition
	//fDArray is required because there might be a hidden transitive FD that is properEquivalent
	/*public static ArrayList<FD> getProperEquivalent(Partition part1, Partition part2, ArrayList<FD> fDArray){
		ArrayList<FD> equivalentArray = new ArrayList<FD>();
		ArrayList<FD> tempArray = new ArrayList<FD>(fDArray);
		tempArray = removeTrivial(tempArray);
		
		//Characteristic of Partition is that all the LHS has to be the same
		
		
		//Use LHS of any of the FDs in the partition,
		//find closure using the FD in the partition. 
		//Find a partition with LHS inside that closure. 
		//Do closure for that LHS to see if it includes the first LHS. 
		//If it is, i means they are functionally equivalent and thus we should merge the partition.
		
		return equivalentArray;
	}*/
	
	
}
