package logic;

import java.util.*;
import ui.Log;

public class Bernstein{
	
	public static ArrayList<FD> removeTrivial(ArrayList<FD> fDArray){
		ArrayList<FD> tempArray = new ArrayList<FD>(fDArray);
		for(int i=0;i<tempArray.size();i++){
			FD currentFD = tempArray.get(i);
			if(Attribute.IS_BIT_EQUAL(currentFD.LHS, currentFD.RHS)){
				Log.getInstance().println("Trivial "+currentFD+" is removed");
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
					Log.getInstance().println("Trivial "+tempArray.get(i)+" is removed");
					tempArray.remove(i);
					i--;
					continue;
				}
				String tempStr = Integer.toBinaryString(tempInt);
				while(tempStr.length()<currentFD.LHS.length()){
					tempStr = "0"+tempStr;
				}
				
				String strLog = "Trivial "+currentFD+"is converted to ";
				currentFD.RHS = tempStr;
				strLog+=currentFD;
				Log.getInstance().println(strLog);
			}
		}
		return tempArray;
	}
	
	//Input an arrayList of FD
	public static ArrayList<FD> removeExtraneousAttribute(ArrayList<FD> fDArray){
		ArrayList<FD> tempArray = new ArrayList<FD>(fDArray);
		Log.getInstance().setLogging(false);
		tempArray = removeTrivial(tempArray);
		Log.getInstance().setLogging(true);
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
					Log.getInstance().print(tempArray.get(i).toString()+" becomes ");
					tempArray.get(i).LHS=tempProperSubset.get(j);
					Log.getInstance().println(tempArray.get(i).toString());
					break;
				}
			}
		}
		return tempArray;
	}
	
	public static ArrayList<FD> removeFDUsingCovering(ArrayList<FD> fDArray){
		ArrayList<FD> tempArray = new ArrayList<FD>(fDArray);
		Log.getInstance().setLogging(false);
		tempArray = removeTrivial(tempArray);
		Log.getInstance().setLogging(true);
		for(int i=0;i<tempArray.size();i++){
			ArrayList<FD> exclusionArray = new ArrayList<FD>(tempArray);
			//exclusionArray includes all FD except the current FD we are checking
			exclusionArray.remove(tempArray.get(i));
			//Find closure of LHS using the other FDs
			String closure = Relation.computeClosure(tempArray.get(i).LHS, exclusionArray);
			//If RHS is inside the closure, it means that this FD can be remove due to covering
			if(Attribute.IS_BIT_EQUAL(Attribute.AND(closure, tempArray.get(i).RHS),tempArray.get(i).RHS)){
				Log.getInstance().println("Remove "+tempArray.get(i));
				tempArray.remove(i);
				i--;
			}
		}
		return tempArray;
	}
	
	//F+
	//For each FD, try to see if it can be expanded further
	public static ArrayList<FD> getFPlus(ArrayList<FD> fDArray){
		Log.getInstance().setLogging(false);
		ArrayList<FD> tempArray = new ArrayList<FD>(fDArray);
		tempArray = removeTrivial(tempArray);
		tempArray = splitRHS(tempArray);
		
		//Brute force search and add if you can create a new FD via transitive
		for(int i=0;i<tempArray.size();i++){
			for(int j=0;j<tempArray.size();j++){
				if(i==j){
					continue;
				}
				
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
		Log.getInstance().setLogging(true);
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
		int partitionCount = 1;
		
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
				Partition newPart = new Partition(tempArray.get(i));
				newPart.partitionName = "H"+partitionCount++;
				partArray.add(newPart);
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
		
		//Remove FD that are closure of the join
		for(int i=0;i<partArray.size();i++){
			for(int j=0;j<partArray.get(i).getFDSize();j++){
				String closure = Relation.computeClosure(partArray.get(i).getfDList().get(j).LHS,partArray.get(i).joinList);
				if(Attribute.IS_BIT_EQUAL(Attribute.AND(closure, partArray.get(i).getfDList().get(j).RHS),partArray.get(i).getfDList().get(j).RHS)){
					partArray.get(i).removeFD(partArray.get(i).getfDList().get(j));
					j--;
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
						//remove j since j can determine i which can then determine the RHS
						partArray = removeFDFromPartitions(Fplus.get(j),partArray); //there is a chance that the FD is not in any of the partitions
						Fplus.remove(j);
						j--;
						//Not required to break because it is the inner loop, there is possibility for more transitive dependency
					}else if(!bol_i_is_in_closureJ && bol_j_is_in_closureI){
						//Transitive found
						//remove the i since i can determine j which can then determine the RHS
						partArray = removeFDFromPartitions(Fplus.get(i),partArray); //there is a chance that the FD is not in any of the partitions
						Fplus.remove(i);
						i--;
						break;	
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
	
	public static ArrayList<Relation> convertBCNF(ArrayList<Relation> relationArray){
		ArrayList<Relation> relArray = new ArrayList<Relation>(relationArray);
		
		for(int i=0;i<relArray.size();i++){
			//For each relation, check for FDs that violates BCNF
			for(int j=0;j<relArray.get(i).fDList.size();j++){
				boolean bol_isSuperKey = false;
				
				//Run through all candidate keys to check if it is a super key
				ArrayList<String> candidateKeyBits = relArray.get(i).getCandidateBitStrings();
				for(int p=0;p<candidateKeyBits.size() && bol_isSuperKey==false;p++){
					if(Attribute.IS_SUPERKEY(candidateKeyBits.get(p), relArray.get(i).fDList.get(j).LHS)){
						bol_isSuperKey=true;
					}
				}
				
				//Found FD with LHS not a super key
				if(bol_isSuperKey==false){
					//The FD violates BCNF, decompose
					Relation tempRel = new Relation();
					tempRel.relName = relArray.get(i).relName+"2";
					relArray.get(i).relName+="1";
					tempRel.fDList.add(relArray.get(i).fDList.get(j));
					relArray.get(i).fDList.remove(j);
					j--;
					relArray.add(tempRel);
					
					//Find the attributes to remove/add
					String strLHS = Attribute.getInstance().getAttrString(tempRel.fDList.get(0).LHS);
					String strRHS = Attribute.getInstance().getAttrString(tempRel.fDList.get(0).RHS);
					String[] splitLHS = strLHS.split(",");
					String[] splitRHS = strRHS.split(",");
					
					//add all attributes to new relation
					for(int k=0;k<splitLHS.length;k++){
						if(!tempRel.attrList.contains(splitLHS[k])){
							tempRel.attrList.add(splitLHS[k]);
						}
					}
					for(int k=0;k<splitRHS.length;k++){
						if(!tempRel.attrList.contains(splitRHS[k])){
							tempRel.attrList.add(splitRHS[k]);
						}
					}
					
					//Set pri key of new relation as LHS
					tempRel.priKeyList.add(strLHS);
					
					//Remove RHS attribute from the original
					for(int k=0;k<splitRHS.length;k++){
						relArray.get(i).attrList.remove(splitRHS[k]);
					}
					//Remove from primary Keys if neccesary
					for(int k=0;k<relArray.get(i).priKeyList.size();k++){
						String[] splitPri = relArray.get(i).priKeyList.get(k).split(",");
						String finalPri = "";
						for(int q=0;q<splitPri.length;q++){
							boolean bol_found = false;
							for(int w=0;w<splitRHS.length && bol_found==false;w++){
								if(splitRHS[w].trim().compareTo(splitPri[q].trim())==0){
									bol_found = true;
									break;
								}
							}
							if(bol_found==false){
								if(finalPri.length()!=0){
									finalPri+=",";
								}
								finalPri+=splitPri[q];
							}
						}
						relArray.get(i).priKeyList.set(k, finalPri);
					}
					
					//Remove RHS from all the FDs in it as well
					String bitInverse = Attribute.INVERSE(tempRel.fDList.get(0).RHS);
					for(int k=0;k<relArray.get(i).fDList.size();k++){
						relArray.get(i).fDList.get(k).LHS = Attribute.AND(relArray.get(i).fDList.get(k).LHS, bitInverse);
						relArray.get(i).fDList.get(k).RHS = Attribute.AND(relArray.get(i).fDList.get(k).RHS, bitInverse);
					}
				}
			}
		}
		
		return relArray;
	}
	
	//construct Relation using Partition
	public static ArrayList<Relation> constructRelations(ArrayList<Partition> partitionArray){
		ArrayList<Relation> relArray = new ArrayList<Relation>();
		
		for(int i=0;i<partitionArray.size();i++){
			ArrayList<String> attrList = new ArrayList<String>();
			ArrayList<String> priKeyList = new ArrayList<String>();
			
			//Add attributes from join
			for(int j=0;j<partitionArray.get(i).joinList.size();j++){
				while(partitionArray.get(i).joinList.get(j).LHS.length()<Attribute.getInstance().numOfAttributes()){
					partitionArray.get(i).joinList.get(j).LHS="0"+partitionArray.get(i).joinList.get(j).LHS;
				}
				
				int index = partitionArray.get(i).joinList.get(j).LHS.indexOf("1");
				String LHSpriKeyBit = "";
				while(index>=0 && index<partitionArray.get(i).joinList.get(j).LHS.length()){
					String bitString = Integer.toBinaryString((int)Math.pow(2,(Attribute.getInstance().numOfAttributes()-1)-index));
					String attribute = Attribute.getInstance().getAttrString(bitString);
					if(!attrList.contains(attribute)){
						attrList.add(attribute);
						LHSpriKeyBit = Attribute.OR(LHSpriKeyBit, bitString);
					}
					index = partitionArray.get(i).joinList.get(j).LHS.indexOf("1",index+1);
				}
				
				String LHS = Attribute.getInstance().getAttrString(LHSpriKeyBit);
				if(LHS.length()>0 && !priKeyList.contains(LHS)){
					priKeyList.add(LHS);
				}
				
				while(partitionArray.get(i).joinList.get(j).RHS.length()<Attribute.getInstance().numOfAttributes()){
					partitionArray.get(i).joinList.get(j).RHS="0"+partitionArray.get(i).joinList.get(j).RHS;
				}
				index = partitionArray.get(i).joinList.get(j).RHS.indexOf("1");
				String RHSpriKeyBit = "";
				while(index>=0 && index<partitionArray.get(i).joinList.get(j).RHS.length()){
					String bitString = Integer.toBinaryString((int)Math.pow(2,(Attribute.getInstance().numOfAttributes()-1)-index));
					String attribute = Attribute.getInstance().getAttrString(bitString);
					if(!attrList.contains(attribute)){
						attrList.add(attribute);
						RHSpriKeyBit = Attribute.OR(RHSpriKeyBit, bitString);
					}
					index = partitionArray.get(i).joinList.get(j).RHS.indexOf("1",index+1);
				}
				
				String RHS = Attribute.getInstance().getAttrString(RHSpriKeyBit);
				if(RHS.length()>0 && !priKeyList.contains(RHS)){
					priKeyList.add(RHS);
				}
			}
			
			//Add attributes from FDs
			ArrayList<FD> fDList = partitionArray.get(i).getfDList();
			for(int j=0;j<fDList.size();j++){
				String priKeyBit = "";
				while(fDList.get(j).LHS.length()<Attribute.getInstance().numOfAttributes()){
					fDList.get(j).LHS="0"+fDList.get(j).LHS;
				}
				int index = fDList.get(j).LHS.indexOf("1");
				while(index>=0 && index<fDList.get(j).LHS.length()){
					String bitString = Integer.toBinaryString((int)Math.pow(2,(Attribute.getInstance().numOfAttributes()-1)-index));
					String attribute = Attribute.getInstance().getAttrString(bitString);
					if(!attrList.contains(attribute)){
						attrList.add(attribute);
						priKeyBit = Attribute.OR(priKeyBit, bitString);
					}
					index = fDList.get(j).LHS.indexOf("1",index+1);
				}
				
				String LHS = Attribute.getInstance().getAttrString(priKeyBit);
				if(LHS.length()>0 && !priKeyList.contains(LHS)){
					priKeyList.add(LHS);
				}
				
				while(fDList.get(j).RHS.length()<Attribute.getInstance().numOfAttributes()){
					fDList.get(j).RHS="0"+fDList.get(j).RHS;
				}
				index = fDList.get(j).RHS.indexOf("1");
				while(index>=0 && index<fDList.get(j).RHS.length()){
					String bitString = Integer.toBinaryString((int)Math.pow(2,(Attribute.getInstance().numOfAttributes()-1)-index));
					String attribute = Attribute.getInstance().getAttrString(bitString);
					if(!attrList.contains(attribute)){
						attrList.add(attribute);
						//Do not add primary key for RHS
					}
					index = fDList.get(j).RHS.indexOf("1",index+1);
				}
			}
			
			Relation tempRelation = new Relation("R"+(i+1),attrList);
			tempRelation.fDList = fDList;
			tempRelation.priKeyList = priKeyList;
			relArray.add(tempRelation);
		}
		
		return relArray;
	}
}
