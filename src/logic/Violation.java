package logic;

import java.util.ArrayList;

public class Violation {

	//every non-prime attribute of R is fully dependent on each key of R
	//non trivial FD: LHS not proper subset of candidate key OR RHS is part of candidate key
	public static Boolean checkRelation2NF(Relation R)
	{
		ArrayList<String> nonprimes = getNonPrimeAttributes(R);
		ArrayList<String> keyStrings = R.getCandidateBitStrings();
		ArrayList<FD> fds = Bernstein.splitRHS(R.fDList);
		ArrayList<FD> Fplus = Bernstein.getFPlus(fds);
		for(int i = 0; i< keyStrings.size(); i++)
		{
			//get proper subset of keyStrings. 
			ArrayList<String> subsets = Attribute.ALL_PROPER_SUBSET_OF(keyStrings.get(i));
			for(int j = 0; j< nonprimes.size();j++)
			{
				for(int k = 0; k< Fplus.size(); k++)
				{
					for(int m = 0; m<subsets.size();m++)
					{
						//if non-prime attribute is dependent on a subset of key
						if(Attribute.IS_BIT_EQUAL(nonprimes.get(j),Fplus.get(k).RHS) && Attribute.IS_BIT_EQUAL(subsets.get(m),Fplus.get(k).LHS))
							return false;
					}
				}
			}			
		}
		return true;
	}
	public static Boolean check2NF(Relation R, FD fd)
	{
		ArrayList<FD> tempArray = new ArrayList<FD>();
		ArrayList<String> keyStrings = R.getCandidateBitStrings();
		ArrayList<String> nonprimes = getNonPrimeAttributes(R);

		/**start of splitRHS for single fd**/
		//Find all single variables
		int nextIndex = 0;
		int currentOne = fd.RHS.indexOf("1",nextIndex);
		while(currentOne>=0){
			String tempInput = "";
			for(int j=0;j<currentOne;j++){
				tempInput+="0";
			}
			tempInput+="1";
			for(int j=currentOne+1;j<fd.RHS.length();j++){
				tempInput+="0";
			}
			
			tempArray.add(new FD(fd.LHS,tempInput));
			nextIndex=currentOne+1;
			currentOne = fd.RHS.indexOf("1",nextIndex);
		}
		/**end of splitRHS for single fd**/

		for(int i = 0; i<tempArray.size();i++)//size depends on how many attributes on RHS
		{
			for(int j = 0; j<nonprimes.size();j++)
			{
				//if RHS is a nonprime
				if(Attribute.IS_BIT_EQUAL(tempArray.get(i).RHS , nonprimes.get(j)))
				{
					for(int k = 0; k< keyStrings.size(); k++)
					{
						//get proper subset of keyStrings. 
						ArrayList<String> subsets = Attribute.ALL_PROPER_SUBSET_OF(keyStrings.get(i));
						for(int m = 0; m<subsets.size();m++)
						{
							//if the LHS is a subset of keyString (ie nonprime not fully dependent)
							if(Attribute.IS_BIT_EQUAL(tempArray.get(i).LHS,subsets.get(m)))
								return false;
						}
					}
				}
			}
		}
		return true;		
	}
	//if in 2NF and each non-prime attribute of R is not transitively dependent on arbitrarily chosen key of R
	//non trivial FD: LHS must be superkey OR RHS is part of candidate key
	public static Boolean checkRelation3NF(Relation R)
	{
		if(!checkRelation2NF(R))
			return false;
		ArrayList<String> keyStrings = R.getCandidateBitStrings();
		ArrayList<FD> Fplus = Bernstein.getFPlus(R.fDList);
		ArrayList<String> nonprimes = getNonPrimeAttributes(R);

		for(int i = 0; i<nonprimes.size();i++)
		{
			//arbitrarily chosen key of R, just pick 1st one
			String closure = Relation.computeClosure(keyStrings.get(0), Fplus);
			ArrayList<String> temp = Attribute.ALL_PROPER_SUBSET_OF(closure);
			
			for(int j = 0; j<temp.size();j++)
			{
				//eg A->B and B->d, A and B must not be identical
				if(Attribute.IS_BIT_EQUAL(temp.get(j), keyStrings.get(0)))
					continue;
				for(int k = 0; k<Fplus.size();k++)
				{
					//if there exists a subset of the closure of the key that determines the nonprime attribute
					//ie subset of closure determines RHS
					if(Attribute.IS_BIT_EQUAL(Fplus.get(k).LHS, temp.get(j)) 
							&& Attribute.IS_BIT_EQUAL(Fplus.get(k).RHS, nonprimes.get(i)))							
					{
						for(int m = 0; m<Fplus.size();m++)
						{
							//subset does not determine LHS
							if(!(Attribute.IS_BIT_EQUAL(Fplus.get(m).LHS, temp.get(j)) 
									&& Attribute.IS_BIT_EQUAL(Fplus.get(m).RHS, keyStrings.get(0))))							
							{
								return false;
							}
						}
					}						
				}
			}
		}
		return true;		
	}
	public static Boolean check3NF(Relation R, FD fd)//eg A->B B->C if fd is A->C
	{
		ArrayList<FD> Fplus = Bernstein.getFPlus(R.fDList);

		ArrayList<FD> tempArray = new ArrayList<FD>();
		//check closure of LHS. If any subset of the closure determines RHS, 
		//and the subset does not determine LHS, then transitive
		if(!check2NF(R, fd))
		{
			System.out.println("Not in 2NF");
			return false;
		}
		/**start of splitRHS for single fd**/
		//Find all single variables
		int nextIndex = 0;
		int currentOne = fd.RHS.indexOf("1",nextIndex);
		while(currentOne>=0){
			String tempInput = "";
			for(int j=0;j<currentOne;j++){
				tempInput+="0";
			}
			tempInput+="1";
			for(int j=currentOne+1;j<fd.RHS.length();j++){
				tempInput+="0";
			}
			
			tempArray.add(new FD(fd.LHS,tempInput));
			nextIndex=currentOne+1;
			currentOne = fd.RHS.indexOf("1",nextIndex);
		}
		/**end of splitRHS for single fd**/
		for(int i = 0; i< tempArray.size();i++)//size determined by number of attributes on RHS
		{
			//compute closure of A
			String closure = Relation.computeClosure(fd.LHS, Fplus);

			//compute all subsets of closure of A
			ArrayList<String> temp = Attribute.ALL_PROPER_SUBSET_OF(closure);
			temp.add(closure);

			//loop through all subsets of closure of A
			for(int j = 0; j<temp.size();j++)
			{
				//eg A->B and B->d, A and B must not be identical
				if(Attribute.IS_BIT_EQUAL(temp.get(j), fd.LHS))
					continue;
				for(int k = 0; k<Fplus.size();k++)
				{					
					//ie subset of closure determines RHS eg B->C
					if(Attribute.IS_BIT_EQUAL(Fplus.get(k).LHS, temp.get(j)) 
							&& Attribute.IS_BIT_EQUAL(Fplus.get(k).RHS, fd.RHS))							
					{
						for(int m = 0; m<Fplus.size();m++)
						{
							//subset does not determine LHS eg B-/->A, transitive dependency exists
							if(!(Attribute.IS_BIT_EQUAL(Fplus.get(m).LHS, temp.get(j)) 
									&& Attribute.IS_BIT_EQUAL(Fplus.get(m).RHS, fd.LHS)))							
							{								
								return false;
							}
						}
					}						
				}
			}
		}
		return true;
	}
	public static Boolean checkRelationBCNF(Relation R)
	{
		ArrayList<FD> fds = Bernstein.splitRHS(R.fDList);
		for(int i = 0; i< fds.size();i++)
		{
			if(!checkBCNF(R, fds.get(i)))
				return false;
		}
		return true;
	}
	//for non trivial FD, LHS must be key or superkey
	public static Boolean checkBCNF(Relation R, FD fd)
	{
		if(FDisTrivial(fd))
			return true;
		ArrayList<String> keyStrings = R.getCandidateBitStrings();
		ArrayList<String> temp = Attribute.ALL_PROPER_SUBSET_OF(fd.LHS);
		for(int k = 0; k<keyStrings.size();k++)		
		{				
			//if LHS is a key
			if(Attribute.IS_BIT_EQUAL(fd.LHS, keyStrings.get(k)))
				return true;
			for(int j = 0; j< temp.size(); j++)
			{				
				//if any of the subset of LHS is a key, then the FD is BCNF 
				if(Attribute.IS_BIT_EQUAL(temp.get(j),keyStrings.get(k)))
					return true;
			}
		}
		
		return false;
	}
	//returns bitstrings of nonprimes
	public static ArrayList<String> getNonPrimeAttributes(Relation R)
	{
		ArrayList<String> keyStrings = R.getCandidateBitStrings();
		ArrayList<String> attributes = R.GetAttrList();
		ArrayList<String> nonprimes = new ArrayList<String>();
		String temp = "";
		//OR everything to find out all the prime attributes
		
		if(keyStrings.size()>1)
		{
			
			for(int i = 0 ; i < keyStrings.size()-1 ; i++)
			{
				temp =  Attribute.OR(keyStrings.get(i), keyStrings.get(i+1));
			}
		}
		else
			temp = keyStrings.get(0);
		
		//invert bits to get the non-prime attributes
		String temp2 = Attribute.INVERSE(temp);
		String mask = Integer.toBinaryString(1);
		for(int j = 0; j< attributes.size(); j++)
		{
			String hold = Attribute.AND(mask, temp2);
			if(Integer.parseInt(hold)>0)
				nonprimes.add(hold);			
			mask = Integer.toBinaryString(Integer.parseInt(mask, 2) << 1);
			
		}
		return nonprimes;
	}
	//FD: X -> Y is called trivial if Y is a subset of X
	public static Boolean FDisTrivial(FD fd)
	{
		if(Attribute.IS_BIT_EQUAL(fd.LHS, fd.RHS))
			return true;
		ArrayList<String> temp = Attribute.ALL_PROPER_SUBSET_OF(fd.LHS);
		for(int i = 0; i<temp.size();i++)
		{
			if(Attribute.IS_BIT_EQUAL(temp.get(i), fd.RHS))
				return true;
		}
		return false;
		
	}
}
