package logic;

import java.util.ArrayList;


public class Test1 {
	static int bitCount=1;
	
	public static void main(String[] args) {
		Attribute attr = new Attribute();
		/*
		System.out.println("return: "+attr.AddAttr("A"));
		System.out.println("return: "+attr.AddAttr("B"));
		System.out.println("return: "+attr.AddAttr("C"));
		System.out.println("return: "+attr.AddAttr("D"));
		System.out.println("return: "+attr.AddAttr("E"));
		System.out.println("return: "+attr.AddAttr("F"));
		System.out.println("return: "+attr.AddAttr("G"));
		System.out.println("return: "+attr.AddAttr("H"));
		System.out.println("return: "+attr.AddAttr("I"));
		System.out.println("return: "+attr.AddAttr("J"));
		System.out.println("return: "+attr.AddAttr("K"));
		System.out.println("return: "+attr.AddAttr("L"));
		System.out.println("return: "+attr.AddAttr("M"));
		System.out.println("return: "+attr.AddAttr("N"));
		System.out.println("return: "+attr.AddAttr("O"));
		System.out.println("return: "+attr.AddAttr("P"));
		System.out.println("return: "+attr.AddAttr("Q"));
		System.out.println("return: "+attr.AddAttr("R"));
		System.out.println("return: "+attr.AddAttr("S"));
		System.out.println("return: "+attr.AddAttr("T"));
		System.out.println("return: "+attr.AddAttr("U"));
		System.out.println("return: "+attr.AddAttr("V"));
		System.out.println("return: "+attr.AddAttr("W"));
		System.out.println("return: "+attr.AddAttr("X"));
		System.out.println("return: "+attr.AddAttr("Y"));
		System.out.println("return: "+attr.AddAttr("Z"));
		System.out.println("return: "+attr.AddAttr("AA"));
		System.out.println("return: "+attr.AddAttr("BB"));
		System.out.println("return: "+attr.AddAttr("CC"));
		System.out.println("return: "+attr.AddAttr("DD"));
		System.out.println("return: "+attr.AddAttr("EE"));

		

		System.out.println("-------------------");
		System.out.println("Result: "+attr.GetBinAttr("0000000000000000000000000000001"));
		System.out.println("Result: "+attr.GetBinAttr("0000000000000001000000000000000"));
		System.out.println("Result: "+attr.GetBinAttr("1000000000000000000000000000000"));
		
		*/
	
		Relation r = new Relation();
		ArrayList<String> attrList1 = new ArrayList<String>();
		attrList1.add("A");
		attrList1.add("B");
		attrList1.add("C");
		attrList1.add("D");
	
		ArrayList<String> attrList2 = new ArrayList<String>();
		attrList2.add("A");
		attrList2.add("C");
		attrList2.add("E");
	
		r.SetRels("R1", attrList1);
		r.SetRels("R2", attrList2);

		ArrayList relList = new ArrayList();
		ArrayList tempAttrList = new ArrayList();
		relList=r.GetRels();
		System.out.println("=========Test inserting attribute to relations========");
		for(int i=0;i<relList.size();i++){
			System.out.println(((Relation)relList.get(i)).relName);
			tempAttrList=((Relation)relList.get(i)).attrList;
			for(int a=0;a<tempAttrList.size();a++){
				System.out.println(tempAttrList.get(a));
			}
			System.out.println("==================================");
		}
	
		r=r.UnionRel(r);
		relList=r.GetComputeRels(1);
		System.out.println("========Test UnionRel=======");
		for(int i=0;i<relList.size();i++){
			System.out.println(relList.get(i));
		}
		System.out.println("==================================");
	
		r=r.IntersectRel(r);
		relList=r.GetComputeRels(2);
		System.out.println("========Test IntersectRel=======");
		for(int i=0;i<relList.size();i++){
			System.out.println(relList.get(i));
		}
		System.out.println("==================================");
	
	}
}
