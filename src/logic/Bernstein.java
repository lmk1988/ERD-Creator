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
				int tempInt =  Integer.parseInt(currentFD.RHS,2)-Integer.parseInt(compare,2);
				if(tempInt==0){
					tempArray.remove(i);
					i--;
					continue;
				}
				String tempStr = Integer.toBinaryString(tempInt);
				while(tempStr.length()<currentFD.LHS.length()){
					tempStr = "0"+tempStr;
				}
				currentFD.RHS = tempStr;
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
				if(Attribute.IS_BIT_EQUAL(Attribute.AND(closure, tempLHS), tempLHS)){
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
			if(Attribute.IS_BIT_EQUAL(Attribute.AND(closure, tempArray.get(i).RHS),tempArray.get(i).RHS)){
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
				if(Attribute.IS_BIT_EQUAL(tempArray.get(i).RHS,tempArray.get(j).LHS) && !Attribute.IS_BIT_EQUAL(tempArray.get(i).LHS,tempArray.get(j).RHS)){
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
				if(Attribute.IS_BIT_EQUAL(partArray.get(j).getLHS(),LHS)){
					partArray.get(j).addFD(tempArray.get(i));
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
			Fplus.addAll(partArray.get(i).getfDList());
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
				if(Attribute.IS_BIT_EQUAL(Attribute.AND(partArray.get(i).getLHS(),closureArray.get(j)),partArray.get(i).getLHS())){
					//if LHS is inside that closure, check if this partition's closure includes that partition's LHS
					if(Attribute.IS_BIT_EQUAL(Attribute.AND(partArray.get(j).getLHS(),closureArray.get(i)),partArray.get(j).getLHS())){
						//if it is, they are proper equivalent and should join
						//Merge partition in J into I (remove J)
						FD fd1 = new FD(partArray.get(i).getLHS(),partArray.get(j).getLHS());
						FD fd2 = new FD(partArray.get(j).getLHS(),partArray.get(i).getLHS());
						partArray.get(i).joinList.add(fd1);
						partArray.get(i).joinList.add(fd2);
						partArray.get(i).addFDs(partArray.get(j).getfDList());
						//Can remove like this because FD has implemented comparable
						partArray.get(i).removeFD(fd1);
						partArray.get(i).removeFD(fd2);
						
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
			Fplus.addAll(partArray.get(i).getfDList());
		}
		
		Fplus = getFPlus(Fplus);
		
		for(int i=0;i<Fplus.size();i++){
			for(int j=i+1;j<Fplus.size();j++){
				//Compare those with similar RHS
				if(Attribute.IS_BIT_EQUAL(Fplus.get(i).RHS,Fplus.get(j).RHS)){
					boolean bol_i_is_in_closureJ = (Attribute.IS_BIT_EQUAL(Attribute.AND(Relation.computeClosure(Fplus.get(j).LHS, Fplus), Fplus.get(i).LHS),Fplus.get(i).LHS));
					boolean bol_j_is_in_closureI = (Attribute.IS_BIT_EQUAL(Attribute.AND(Relation.computeClosure(Fplus.get(i).LHS, Fplus), Fplus.get(j).LHS),Fplus.get(j).LHS));
						
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
			partArray.get(i).removeFD(fd);
			//If partition is empty, just remove from array
			if(partArray.get(i).getFDSize()==0 && partArray.get(i).joinList.size()==0){
				partArray.remove(i);
				i--;
			}
		}
		return partArray;
	}
	
	//construct Relation using Partition
	public static ArrayList<Relation> constructRelations(ArrayList<Partition> partitionArray){
		ArrayList<Relation> relArray = new ArrayList<Relation>();
		
		for(int i=0;i<partitionArray.size();i++){
			ArrayList<String> attrList = new ArrayList<String>();
			ArrayList<Integer> priKeyIndex = new ArrayList<Integer>();
			
			//Add attributes from join
			for(int j=0;j<partitionArray.get(j).joinList.size();j++){
				String attribute = Attribute.getInstance().getAttrString(partitionArray.get(j).joinList.get(j).LHS);
				if(!attrList.contains(attribute)){
					attrList.add(attribute);
					priKeyIndex.add(attrList.size()-1);
				}
				attribute = Attribute.getInstance().getAttrString(partitionArray.get(j).joinList.get(j).RHS);
				if(!attrList.contains(attribute)){
					attrList.add(attribute);
					priKeyIndex.add(attrList.size()-1);
				}
			}
			
			//Add attributes from FDs
			ArrayList<FD> fDList = partitionArray.get(i).getfDList();
			for(int j=0;j<fDList.size();j++){
				String attribute = Attribute.getInstance().getAttrString(fDList.get(j).LHS);
				if(!attrList.contains(attribute)){
					attrList.add(attribute);
					priKeyIndex.add(attrList.size()-1);
				}
				attribute = Attribute.getInstance().getAttrString(fDList.get(j).RHS);
				if(!attrList.contains(attribute)){
					attrList.add(attribute);
				}
			}
			
			Relation tempRelation = new Relation("R"+(i+1),attrList);
			tempRelation.fDList = fDList;
			tempRelation.priKeyIndex = priKeyIndex;
			relArray.add(tempRelation);
		}
		
		return relArray;
	}
}
