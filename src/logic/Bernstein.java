package logic;

import java.util.*;

import ui.Log;

public class Bernstein{
	
	static ArrayList<Relation> oriRel;
	
	public static ArrayList<FD> removeTrivial(ArrayList<FD> fDArray){
		ArrayList<FD> tempArray = new ArrayList<FD>(fDArray);
		for(int i=0;i<tempArray.size();i++){
			FD currentFD = tempArray.get(i);
			
			if(currentFD.LHS.length()==0 || currentFD.RHS.length()==0 || Integer.parseInt(currentFD.LHS)==0 || Integer.parseInt(currentFD.RHS)==0){
				tempArray.remove(i);
				i--;
				continue;
			}
			
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
	
	public static Relation removeTrivial(Relation rel){
		Relation tempRel = new Relation(rel);
		
		//Remove everything associated with primary key since it is trivial
		for(int i=0;i<tempRel.priKeyList.size();i++){
			String bitString = Attribute.getInstance().getBitString(tempRel.priKeyList.get(i));
			
			for(int k=0;k<tempRel.fDList.size();k++){
				if(Attribute.IS_BIT_EQUAL(tempRel.fDList.get(k).LHS,bitString)){
					tempRel.fDList.remove(k);
					k--;
				}
			}
		}
		
		tempRel.fDList = removeTrivial(tempRel.fDList);
		
		
		return tempRel;
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
	
	public static ArrayList<FD> combineRHS(ArrayList<FD> fDArray){
		ArrayList<FD> tempArray = new ArrayList<FD>(fDArray);
		
		for(int i=0;i<tempArray.size();i++){
			for(int j=i+1;j<tempArray.size();j++){
				if(Attribute.IS_BIT_EQUAL(tempArray.get(i).LHS, tempArray.get(j).LHS)){
					tempArray.get(i).RHS = Attribute.OR(tempArray.get(i).RHS, tempArray.get(j).RHS);
					tempArray.remove(j);
					j--;
				}
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
		
		//Get All the FD
		ArrayList<FD> FDlist = new ArrayList<FD>();
		for(int i=0;i<partArray.size();i++){
			FDlist.addAll(partArray.get(i).getfDList());
		}

		//Generate closure for each partition
		ArrayList<String> closureArray = new ArrayList<String>();
		for(int i=0;i<partArray.size();i++){
			closureArray.add(Relation.computeClosure(partArray.get(i).getLHS(), FDlist));
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
	
	public static ArrayList<Relation> fix3NFLossless(ArrayList<Relation> relationArray){
		ArrayList<Relation>	relArray = new ArrayList<Relation>(relationArray);
		
		String bitFinal = "";
		while(bitFinal.length()<Attribute.getInstance().numOfAttributes()){
			bitFinal+="1";
		}
		
		//Remove all RHS from the bitFinal
		for(int i=0;i<relArray.size();i++){
			for(int j=0;j<relArray.get(i).fDList.size();j++){
				bitFinal = Attribute.AND(bitFinal, Attribute.INVERSE(relArray.get(i).fDList.get(j).RHS));
			}
		}
		
		//if more than 1 candidate key, remove
		for(int i=0;i<relArray.size();i++){
			ArrayList<String> candidateKeyBits = relArray.get(i).getCandidateBitStrings();
			if(candidateKeyBits.size()>1){
				for(int k=0;k<candidateKeyBits.size();k++){
					bitFinal = Attribute.AND(bitFinal, Attribute.INVERSE(candidateKeyBits.get(k)));
				}
			}
		}
		
		ArrayList<String> attrList = new ArrayList<String>();
		int index = bitFinal.indexOf("1");
		while(index>=0){
			String bitString = Integer.toBinaryString((int)Math.pow(2,(Attribute.getInstance().numOfAttributes()-1)-index));
			attrList.add(Attribute.getInstance().getAttrString(bitString));
			index = bitFinal.indexOf("1", index+1);
		}
		
		//Check if should add this additional relArray by checking if any of the attribute is not inside the relations
		boolean bol_foundFinal = true;
		for(int i=0;i<attrList.size() && bol_foundFinal==true;i++){
			boolean bol_foundIndividual = false;
			for(int j=0;j<relArray.size() && bol_foundIndividual==false;j++){
				if(relArray.get(j).attrList.contains(attrList.get(i).trim())){
					bol_foundIndividual = true;
				}
			}
			if(bol_foundIndividual==false){
				bol_foundFinal = false;
			}
		}
		
		if(bol_foundFinal == false){
			//one or more attribute is missing and should add a new relation to make it non-lossless
			Relation tempRel = new Relation();
			tempRel.relName = "Non-loss";
			tempRel.attrList = attrList;
			String priKey ="";
			for(int i=0;i<attrList.size();i++){
				if(i!=0){
					priKey+=",";
				}
				priKey+=attrList.get(i);
			}
			tempRel.priKeyList.add(priKey);
			relArray.add(tempRel);
		}
		
		return relArray;
	}
	
	//construct Relation using Partition
	public static ArrayList<Relation> constructRelations(ArrayList<Partition> partitionArray){
		ArrayList<Relation> relArray = new ArrayList<Relation>();
							   oriRel=new ArrayList<Relation>();
		
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
					}
					LHSpriKeyBit = Attribute.OR(LHSpriKeyBit, bitString);
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
					}
					RHSpriKeyBit = Attribute.OR(RHSpriKeyBit, bitString);
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
					}
					priKeyBit = Attribute.OR(priKeyBit, bitString);
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
			//Add join to fD as well
			for(int j=0;j<partitionArray.get(i).joinList.size();j++){
				if(!fDList.contains(partitionArray.get(i).joinList.get(j))){
					fDList.add(partitionArray.get(i).joinList.get(j));
				}
			}
			tempRelation.fDList = fDList;
			tempRelation.priKeyList = priKeyList;
			
			relArray.add(tempRelation);
		}
		oriRel=new ArrayList<Relation>(relArray);
		relArray = fix3NFLossless(relArray);
		
		return relArray;
	}

	public static ArrayList<Relation> removeSuperfluous(ArrayList<Relation> relationArray){
		ArrayList<Relation> relArray = new ArrayList<Relation>(relationArray);
		
		for(int i=0;i<relArray.size();i++){
			for(int j=0;j<relArray.get(i).attrList.size();j++){
				
				String currentAttributeBitString = Attribute.getInstance().getBitString(relArray.get(i).attrList.get(j)).trim();
				//Check for each attribute the FD associated with it
				ArrayList<FD> fPlus = getFPlus(new ArrayList<FD>(relArray.get(i).fDList));
				ArrayList<FD> associatedFD = new ArrayList<FD>();
				
				//generate for each pri key
				for(int k=0;k<relArray.get(i).priKeyList.size();k++){
					String tempLHSBit = Attribute.getInstance().getBitString(relArray.get(i).priKeyList.get(k));
					while(tempLHSBit.length()<Attribute.getInstance().numOfAttributes()){
						tempLHSBit = "0"+tempLHSBit;
					}
					String tempRHSBit = Attribute.AND(Attribute.INVERSE(tempLHSBit), relArray.get(i).getAttrBitString());
					fPlus.add(new FD(tempLHSBit,tempRHSBit));
				}
				
				fPlus = splitRHS(fPlus);
				
				for(int k=0;k<fPlus.size();k++){
					if(Attribute.IS_BIT_EQUAL(Attribute.AND(fPlus.get(k).LHS, currentAttributeBitString),currentAttributeBitString) || 
							Attribute.IS_BIT_EQUAL(Attribute.AND(fPlus.get(k).RHS, currentAttributeBitString),currentAttributeBitString)){
						//if LHS or RHS of FD includes attribute, add into associatedFD
						associatedFD.add(fPlus.get(k));
					}
				}
				
				fPlus.removeAll(associatedFD);
				
				ArrayList<FD> allfDs = new ArrayList<FD>(fPlus);
				//Check 1: check that you can form back the Relation using fDs from other relations
				for(int k=0;k<relArray.size();k++){
					if(k==i){
						continue;
					}
					allfDs.addAll(relArray.get(k).fDList);
				}
				
				String bitString = Attribute.AND(relArray.get(i).getAttrBitString(), Attribute.INVERSE(currentAttributeBitString));
				if(!Attribute.IS_BIT_EQUAL(Attribute.AND(Relation.computeClosure(bitString,allfDs),currentAttributeBitString), currentAttributeBitString)){
					//Cannot form back the relation so it is not superfluous
					continue;
				}
				
				//Check 2: check that you can get back the fD using other relations as well
				boolean bol_foundAll=true;
				for(int k=0;k<associatedFD.size() && bol_foundAll==true;k++){
					if(!Attribute.IS_BIT_EQUAL(Attribute.AND(Relation.computeClosure(associatedFD.get(k).LHS, allfDs),associatedFD.get(k).RHS),associatedFD.get(k).RHS)){
						bol_foundAll=false;
					}
				}
				
				if(bol_foundAll==false){
					//Cannot get all the fD from other relations
					continue;
				}else{
					//Has passed both check1 and check2
					//Remove primary key
					for(int k=0;k<relArray.get(i).priKeyList.size();k++){
						String[] prikey = relArray.get(i).priKeyList.get(k).split(",");
						for(int m=0;m<prikey.length;m++){
							if(prikey[m].trim().compareTo(relArray.get(i).attrList.get(j))==0){
								relArray.get(i).priKeyList.remove(k);
								k--;
								break;
							}
						}
					}
					//remove attribute from Relation
					relArray.get(i).attrList.remove(j);
					j--;
					
					//Remove FDs
					Log.getInstance().setLogging(false);
					relArray.get(i).fDList=removeTrivial(combineRHS(fPlus));
					Log.getInstance().setLogging(true);
				}
			}
		}
		return relArray;
	}
	
	public static ArrayList<Relation> getOriRel(){
		return oriRel;
	}
}
