package logic;

import java.util.*;

public class Bernstein{
	
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
	public static ArrayList<FD> getFPlus(ArrayList<FD> fDArray){
		ArrayList<FD> tempArray = new ArrayList<FD>(fDArray);
		tempArray = removeTrivial(tempArray);
		tempArray = splitRHS(tempArray);
		
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
	
	//Split all RHS (e.g. A->BC = A->B and A->C)
	public static ArrayList<FD> splitRHS(ArrayList<FD> fDArray){
		ArrayList<FD> tempArray = new ArrayList<FD>();
		
		for(int i=0;i<fDArray.size();i++){
			//Find all single variables
			int nextIndex = 0;
			int currentOne = fDArray.get(i).RHS.indexOf("1",nextIndex);
			while(currentOne>=0){
				String tempInput = "";
				for(int j=0;j<currentOne;j++){
					tempInput+="0";
				}
				tempInput+="1";
				for(int j=currentOne+1;j<fDArray.get(i).RHS.length();j++){
					tempInput+="0";
				}
				
				tempArray.add(new FD(fDArray.get(i).LHS,tempInput));
				nextIndex=currentOne+1;
				currentOne = fDArray.get(i).RHS.indexOf("1",nextIndex);
			}
		}
		
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
	
	//proper equivalent
	//Return an array list of FD that is properEquivalent e.g. A->B B->A
	//Only checks between 2 partitions. Please loop it if required to check for a list of partition
	//fDArray is required because there might be a hidden transitive FD that is properEquivalent
	public static ArrayList<Partition> MergeProperEquivalent(ArrayList<Partition> partitionArray){
		ArrayList<Partition> partArray = new ArrayList<Partition>(partitionArray);
		//Characteristic of Partition is that all the LHS has to be the same
		
		//Get F+
		ArrayList<FD> Fplus = new ArrayList<FD>();
		for(int i=0;i<partArray.size();i++){
			Fplus.addAll(partArray.get(i).fDList);
		}
		Fplus = getFPlus(Fplus);

		//Generate closure for each partition
		ArrayList<String> closureArray = new ArrayList<String>();
		for(int i=0;i<partArray.size();i++){
			closureArray.add(Relation.computeClosure(partArray.get(i).getLHS(), Fplus));
		}
		
		//Find a partition with LHS inside each closure.
		for(int i=0;i<partArray.size();i++){
			for(int j=i+1;j<closureArray.size();j++){
				//Check if the LHS of this partition is inside any of the other closure
				if(Attribute.AND(partArray.get(i).getLHS(),closureArray.get(j)).compareTo(partArray.get(i).getLHS())==0){
					//if LHS is inside that closure, check if this partition's closure includes that partition's LHS
					if(Attribute.AND(partArray.get(j).getLHS(),closureArray.get(i)).compareTo(partArray.get(j).getLHS())==0){
						//if it is, they are proper equivalent and should join
						//Merge partition in J into I (remove J)
						FD fd1 = new FD(partArray.get(i).getLHS(),partArray.get(j).getLHS());
						FD fd2 = new FD(partArray.get(j).getLHS(),partArray.get(i).getLHS());
						partArray.get(i).joinList.add(fd1);
						partArray.get(i).joinList.add(fd2);
						partArray.get(i).fDList.addAll(partArray.get(j).fDList);
						//Can remove like this because FD has implemented comparable
						partArray.get(i).fDList.remove(fd1);
						partArray.get(i).fDList.remove(fd2);
						
						//Remove J from both sides
						closureArray.remove(j);
						partArray.remove(j);
						j--; //Continue checking due to possible many proper equivalent partitions
					}
				}
			}
		}
		
		return partArray;
	}
		
	
	//transitive dependency
	public static ArrayList<Partition> eliminateTransitiveDependency(ArrayList<Partition> partitionArray){
		ArrayList<Partition> partArray = new ArrayList<Partition>(partitionArray);
		
		//Get F+
		ArrayList<FD> Fplus = new ArrayList<FD>();
		for(int i=0;i<partArray.size();i++){
			Fplus.addAll(partArray.get(i).fDList);
		}
		
		Fplus = getFPlus(Fplus);
		
		for(int i=0;i<Fplus.size();i++){
			for(int j=i+1;j<Fplus.size();j++){
				//Compare those with similar RHS
				if(Fplus.get(i).RHS.compareTo(Fplus.get(j).RHS)==0){
					boolean bol_i_is_in_closureJ = (Attribute.AND(Relation.computeClosure(Fplus.get(j).LHS, Fplus), Fplus.get(i).LHS).compareTo(Fplus.get(i).LHS)==0);
					boolean bol_j_is_in_closureI = (Attribute.AND(Relation.computeClosure(Fplus.get(i).LHS, Fplus), Fplus.get(j).LHS).compareTo(Fplus.get(j).LHS)==0);
						
					if(bol_i_is_in_closureJ && !bol_j_is_in_closureI){
						//Transitive found
						//remove the i since i can determine j which can then determine the RHS
						partArray = removeFDFromPartitions(Fplus.get(i),partArray); //there is a chance that the FD is not in any of the partitions
						Fplus.remove(i);
						i--;
						break;
					}else if(!bol_i_is_in_closureJ && bol_j_is_in_closureI){
						//Transitive found
						//remove j since j can determine i which can then determine the RHS
						partArray = removeFDFromPartitions(Fplus.get(j),partArray); //there is a chance that the FD is not in any of the partitions
						Fplus.remove(j);
						j--;
						//Not required to break because it is the inner loop, there is possibility for more transitive dependency
					}
				}
			}
		}
		
		return partArray;
	}
	
	public static ArrayList<Partition> removeFDFromPartitions(FD fd,ArrayList<Partition> partitionArray){
		ArrayList<Partition> partArray = new ArrayList<Partition>(partitionArray);
		for(int i=0;i<partArray.size();i++){
			//don't need check if contains, just remove
			partArray.get(i).fDList.remove(fd);
			//If partition is empty, just remove from array
			if(partArray.get(i).fDList.size()==0 && partArray.get(i).joinList.size()==0){
				partArray.remove(i);
				i--;
			}
		}
		return partArray;
	}
	
	//Compute Relation using Partition
}
