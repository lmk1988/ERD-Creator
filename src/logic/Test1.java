package logic;
import java.util.ArrayList;

/*
 * Test File for Attribute and Relation
 * 
 */
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
		System.out.println("-------------------");
		System.out.println("Result: "+attr.GetBinAttr("00000001"));
		System.out.println("Result: "+attr.GetBinAttr("00000010"));
		System.out.println("Result: "+attr.GetBinAttr("00000100"));
		System.out.println("Result: "+attr.GetBinAttr("00001000"));
		System.out.println("Result: "+attr.GetBinAttr("00010000"));
		System.out.println("Result: "+attr.GetBinAttr("00100000"));
		System.out.println("Result: "+attr.GetBinAttr("01000000"));
		System.out.println("Result: "+attr.GetBinAttr("10000000"));
		*/
	
	Relation r = new Relation();
	ArrayList attrList1 = new ArrayList();
	attrList1.add("A");
	attrList1.add("B");
	attrList1.add("C");
	attrList1.add("D");
	
	ArrayList attrList2 = new ArrayList();
	attrList2.add("A");
	attrList2.add("C");
	attrList2.add("E");
	
	r.SetRels("R1", attrList1);
	r.SetRels("R2", attrList2);
	
	ArrayList relList = new ArrayList();
	ArrayList tempAttrList = new ArrayList();
	relList=r.GetRels();
	for(int i=0;i<relList.size();i++){
		System.out.println(((Relation)relList.get(i)).relName);
		tempAttrList=((Relation)relList.get(i)).attrList;
		for(int a=0;a<tempAttrList.size();a++){
			System.out.println(tempAttrList.get(a));
		}
		System.out.println("==================================");
	}
	
	r.UnionRel();
	}
}
