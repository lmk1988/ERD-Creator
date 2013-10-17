package jUnit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Test;

import logic.Attribute;
import logic.Bernstein;
import logic.Partition;
import logic.FD;
import logic.Relation;

public class UnitTest_Bernstein {

	@Test
	public void test() {
		removeTrivial();
		splitRHS();
		removeExtraneousAttribute();
		partitionFromFD();
		removeFDUsingCovering();
		getFPlus();
		MergeProperEquivalent();
		removeFDFromPartitions();
		eliminateTransitiveDependency();
		constructRelations();
	}

	private void removeTrivial(){
		FD fd1 = new FD("001","101");
		ArrayList<FD> testArray = new ArrayList<FD>();
		testArray.add(fd1);
		testArray = Bernstein.removeTrivial(testArray);
		assertEquals(testArray.size(),1);
		assertEquals(testArray.get(0),new FD("001","100"));
		
		testArray.clear();
		fd1 = new FD("1001","1001");
		testArray.add(fd1);
		testArray = Bernstein.removeTrivial(testArray);
		assertEquals(testArray.size(),0);
		
		testArray.clear();
		fd1 = new FD("1001","1000");
		testArray.add(fd1);
		testArray = Bernstein.removeTrivial(testArray);
		assertEquals(testArray.size(),0);
		
		testArray.clear();
		fd1 = new FD("1001","1111");
		testArray.add(fd1);
		testArray = Bernstein.removeTrivial(testArray);
		assertEquals(testArray.size(),1);
		assertEquals(testArray.get(0),new FD("1001","0110"));
		
		testArray.clear();
		fd1 = new FD("1001","0010");
		testArray.add(fd1);
		testArray = Bernstein.removeTrivial(testArray);
		assertEquals(testArray.size(),1);
		assertEquals(testArray.get(0),new FD("1001","0010"));
	}
	
	private void splitRHS(){
		FD fd1 = new FD("001","110");
		ArrayList<FD> testArray = new ArrayList<FD>();
		testArray.add(fd1);
		testArray = Bernstein.splitRHS(testArray);
		assertEquals(testArray.size(),2);
		assertEquals(testArray.get(0),new FD("001","100"));
		assertEquals(testArray.get(1),new FD("001","010"));
		
		testArray.clear();
		fd1 = new FD("10000","01101");
		testArray.add(fd1);
		testArray = Bernstein.splitRHS(testArray);
		assertEquals(testArray.size(),3);
		assertEquals(testArray.get(0),new FD("10000","01000"));
		assertEquals(testArray.get(1),new FD("10000","00100"));
		assertEquals(testArray.get(2),new FD("10000","00001"));
		
		testArray.clear();
		fd1 = new FD("100","010");
		testArray.add(fd1);
		testArray = Bernstein.splitRHS(testArray);
		assertEquals(testArray.size(),1);
		assertEquals(testArray.get(0),new FD("100","010"));
	}

	private void removeExtraneousAttribute(){
		FD fd1 = new FD("011","100");
		ArrayList<FD> testArray = new ArrayList<FD>();
		testArray.add(fd1);
		testArray = Bernstein.removeExtraneousAttribute(testArray);
		assertEquals(testArray.size(),1);
		FD fd2 = new FD("001","010");
		testArray.add(fd2);

		testArray = Bernstein.removeExtraneousAttribute(testArray);
		assertEquals(testArray.size(),2);
		assertEquals(testArray.get(0),new FD("001","100"));
		assertEquals(testArray.get(1),new FD("001","010"));
		
		testArray.clear();
		fd1 = new FD("11110000","00001111");
		fd2 = new FD("10010000","01000000");
		FD fd3 = new FD("01000000","00110000");
		testArray.add(fd1);
		testArray.add(fd2);
		testArray.add(fd3);
		testArray = Bernstein.removeExtraneousAttribute(testArray);
		assertEquals(testArray.size(),3);
		assertEquals(testArray.get(0),new FD("11000000","00001111"));
		assertEquals(testArray.get(1),new FD("10010000","01000000"));
		assertEquals(testArray.get(2),new FD("01000000","00110000"));
	}

	private void partitionFromFD(){
		ArrayList<FD> tempList = new ArrayList<FD>();
		tempList.add(new FD("100","010"));
		ArrayList<Partition> returnList;
		returnList = Bernstein.partitionFromFD(tempList);
		assertEquals(returnList.size(),1);
		
		tempList.add(new FD("010","001"));
		returnList = Bernstein.partitionFromFD(tempList);
		assertEquals(returnList.size(),2);
		
		tempList.add(new FD("100","1000"));
		returnList = Bernstein.partitionFromFD(tempList);
		assertEquals(returnList.size(),2);
		
		tempList.add(new FD("1000","10000"));
		returnList = Bernstein.partitionFromFD(tempList);
		assertEquals(returnList.size(),3);
		assertEquals(returnList.get(0).joinList.size(),0);
		assertEquals(returnList.get(0).getFDSize(),2);
		assertEquals(returnList.get(0).getfDList().get(0),new FD("100","010"));
		assertEquals(returnList.get(0).getfDList().get(1),new FD("100","1000"));
		assertEquals(returnList.get(1).joinList.size(),0);
		assertEquals(returnList.get(1).getFDSize(),1);
		assertEquals(returnList.get(2).joinList.size(),0);
		assertEquals(returnList.get(2).getFDSize(),1);
		
	}
	
	private void removeFDUsingCovering(){
		ArrayList<FD> tempList = new ArrayList<FD>();
		tempList.add(new FD("1000","0100"));
		tempList.add(new FD("0100","0010"));
		tempList.add(new FD("1000","0010"));
		
		ArrayList<FD> returnList;
		returnList = Bernstein.removeFDUsingCovering(tempList);
		assertEquals(returnList.size(),2);
		assertEquals(returnList.get(0),new FD("1000","0100"));
		assertEquals(returnList.get(1),new FD("0100","0010"));
		
		tempList.add(new FD("100000","000001"));
		tempList.add(new FD("000001","001000"));
		tempList.add(new FD("100000","000100"));
		tempList.add(new FD("100000","000010"));
		
		returnList = Bernstein.removeFDUsingCovering(tempList);
		assertEquals(returnList.size(),4);
		assertEquals(returnList.get(0),new FD("1000","0100"));
		assertEquals(returnList.get(1),new FD("0100","0010"));
		assertEquals(returnList.get(2),new FD("100000","000001"));
		assertEquals(returnList.get(3),new FD("000001","001000"));
		
		tempList.clear();
		tempList.add(new FD("11000","00001"));
		tempList.add(new FD("01000","00001"));
		tempList.add(new FD("10000","01000"));
		returnList = Bernstein.removeFDUsingCovering(tempList);
		assertEquals(returnList.size(),2);
		assertEquals(returnList.get(0),new FD("01000","00001"));
		assertEquals(returnList.get(1),new FD("10000","01000"));
	}

	private void getFPlus(){
		ArrayList<FD> tempList = new ArrayList<FD>();
		ArrayList<FD> returnList;
		returnList = Bernstein.getFPlus(tempList);
		assertEquals(returnList.size(),0);
		
		tempList.add(new FD("1000","0100"));
		tempList.add(new FD("0100","0010"));
		
		returnList = Bernstein.getFPlus(tempList);
		assertEquals(returnList.size(),3);
		assertEquals(returnList.get(0),new FD("1000","0100"));
		assertEquals(returnList.get(1),new FD("0100","0010"));
		assertEquals(returnList.get(2),new FD("1000","0010"));
		
		tempList.add(new FD("0010","0001"));
		returnList = Bernstein.getFPlus(tempList);
		assertEquals(returnList.size(),6);
		assertEquals(returnList.get(0),new FD("1000","0100"));
		assertEquals(returnList.get(1),new FD("0100","0010"));
		assertEquals(returnList.get(2),new FD("0010","0001"));
		assertEquals(returnList.get(3),new FD("1000","0010"));
		assertEquals(returnList.get(4),new FD("0100","0001"));
		assertEquals(returnList.get(5),new FD("1000","0001"));
	}

	private void MergeProperEquivalent(){
		ArrayList<Partition> tempArray = new ArrayList<Partition>();
		ArrayList<Partition> returnList;
		returnList = Bernstein.MergeProperEquivalent(tempArray);
		assertEquals(returnList.size(),0);
		
		Partition part1 = new Partition();
		Partition part2 = new Partition();
		tempArray.add(part1);
		tempArray.add(part2);
		
		part1.addFD(new FD("000001","000010"));
		part2.addFD(new FD("000010","000001"));
		
		returnList = Bernstein.MergeProperEquivalent(tempArray);
		assertEquals(returnList.size(),1);
		assertEquals(returnList.get(0).getfDList().size(),0);
		assertEquals(returnList.get(0).joinList.size(),2);
		assertEquals(returnList.get(0).joinList.get(0),new FD("000001","000010"));
		assertEquals(returnList.get(0).joinList.get(1),new FD("000010","000001"));
		
		tempArray.clear();
		part1 = new Partition();
		part2 = new Partition();
		tempArray.add(part1);
		tempArray.add(part2);
		Partition part3 = new Partition();
		tempArray.add(part3);
		
		part1.addFD(new FD("000001","000010"));
		part2.addFD(new FD("000010","000001"));
		part3.addFD(new FD("010000","100000"));
		assertEquals(tempArray.size(),3);
		returnList = Bernstein.MergeProperEquivalent(tempArray);
		
		assertEquals(returnList.size(),2);
		assertEquals(returnList.get(0).getfDList().size(),0);
		assertEquals(returnList.get(0).joinList.size(),2);
		assertEquals(returnList.get(0).joinList.get(0),new FD("000001","000010"));
		assertEquals(returnList.get(0).joinList.get(1),new FD("000010","000001"));
		
		assertEquals(returnList.get(1).getfDList().size(),1);
		assertEquals(returnList.get(1).joinList.size(),0);
		
		
		tempArray.clear();
		part1 = new Partition();
		part2 = new Partition();
		part3 = new Partition();
		tempArray.add(part1);
		tempArray.add(part2);
		tempArray.add(part3);
		part1.addFD(new FD("000001","000010"));
		part2.addFD(new FD("000010","000001"));
		part2.addFD(new FD("000010","010000"));
		part3.addFD(new FD("010000","000010"));
		
		returnList = Bernstein.MergeProperEquivalent(tempArray);
		assertEquals(returnList.size(),1);
		assertEquals(returnList.get(0).getfDList().size(),0);
		assertEquals(returnList.get(0).joinList.size(),4);
		assertEquals(returnList.get(0).joinList.get(0),new FD("000001","000010"));
		assertEquals(returnList.get(0).joinList.get(1),new FD("000010","000001"));
		assertEquals(returnList.get(0).joinList.get(2),new FD("000010","010000"));
		assertEquals(returnList.get(0).joinList.get(3),new FD("010000","000010"));
	}
	
	private void removeFDFromPartitions(){
		ArrayList<Partition> tempArray = new ArrayList<Partition>();
		ArrayList<Partition> returnList;
		Partition part1 = new Partition();
		Partition part2 = new Partition();
		Partition part3 = new Partition();
		tempArray.add(part1);
		tempArray.add(part2);
		tempArray.add(part3);
		part1.addFD(new FD("010000","100001"));
		part2.addFD(new FD("100000","001000"));
		part2.addFD(new FD("100000","010000"));
		part3.addFD(new FD("000010","000001"));
		
		returnList = Bernstein.removeFDFromPartitions(new FD("010000","100000"), tempArray);
		assertEquals(returnList.size(),3);
		returnList = Bernstein.removeFDFromPartitions(new FD("010000","100001"), tempArray);
		assertEquals(returnList.size(),2);
		
		tempArray.clear();
		tempArray.add(part1);
		tempArray.add(part2);
		tempArray.add(part3);
		part1.addFD(new FD("010000","100001"));
		part2.addFD(new FD("100000","001000"));
		part2.addFD(new FD("100000","010000"));
		part3.addFD(new FD("000010","000001"));
		
		returnList = Bernstein.removeFDFromPartitions(new FD("100000","001000"), tempArray);
		assertEquals(returnList.size(),3);
		assertEquals(returnList.get(2).getFDSize(),1);
	}

	private void eliminateTransitiveDependency(){
		ArrayList<Partition> tempArray = new ArrayList<Partition>();
		ArrayList<Partition> returnList;

		returnList = Bernstein.eliminateTransitiveDependency(tempArray);
		assertEquals(returnList.size(),0);
		
		tempArray.clear();
		Partition part1 = new Partition();
		Partition part2 = new Partition();
		tempArray.add(part1);
		tempArray.add(part2);
		
		part1.addFD(new FD("010000","000001"));
		part1.addFD(new FD("010000","000010"));
		part2.addFD(new FD("000010","000001"));
		
		assertEquals(Attribute.IS_BIT_EQUAL(Attribute.AND(Relation.computeClosure(part1.getLHS(),part1.getfDList()),part2.getLHS()),part2.getLHS()),true);
		assertEquals(Attribute.IS_BIT_EQUAL(Attribute.AND(Relation.computeClosure(part2.getLHS(),part2.getfDList()),part1.getLHS()),part1.getLHS()),false);
			
		returnList = Bernstein.eliminateTransitiveDependency(tempArray);
		assertEquals(returnList.size(),2);
		assertEquals(returnList.get(0).getFDSize(),1);
		assertEquals(returnList.get(0).getfDList().get(0),new FD("010000","000010"));
		assertEquals(returnList.get(1).getFDSize(),1);
		assertEquals(returnList.get(1).getfDList().get(0),new FD("000010","000001"));
		
		tempArray.clear();
		part1 = new Partition();
		part2 = new Partition();
		Partition part3 = new Partition();
		tempArray.add(part1);
		tempArray.add(part2);
		tempArray.add(part3);
		
		part1.addFD(new FD("010000","000001"));
		part1.addFD(new FD("010000","000010"));
		part2.addFD(new FD("100000","010000"));
		part3.addFD(new FD("001000","001000"));
		returnList = Bernstein.eliminateTransitiveDependency(tempArray);
		assertEquals(returnList.size(),3);
		assertEquals(returnList.get(0).getFDSize(),2);
		assertEquals(returnList.get(1).getFDSize(),1);
		assertEquals(returnList.get(2).getFDSize(),1);
		
		tempArray.clear();
		part1 = new Partition();
		part2 = new Partition();
		part3 = new Partition();
		tempArray.add(part1);
		tempArray.add(part2);
		tempArray.add(part3);
		
		part1.addFD(new FD("010000","000001"));
		part1.addFD(new FD("010000","100000"));
		part2.addFD(new FD("100000","001000"));
		part3.addFD(new FD("001000","000001"));
		returnList = Bernstein.eliminateTransitiveDependency(tempArray);
		assertEquals(returnList.size(),3);
		assertEquals(returnList.get(0).getFDSize(),1);
		assertEquals(returnList.get(1).getFDSize(),1);
		assertEquals(returnList.get(2).getFDSize(),1);
	}
	
	private void constructRelations(){
		ArrayList<Partition> tempArray = new ArrayList<Partition>();
		ArrayList<Relation> returnList;
		
		Partition part1 = new Partition();
		Partition part2 = new Partition();
		Partition part3 = new Partition();
		tempArray.add(part1);
		tempArray.add(part2);
		tempArray.add(part3);
		
		part1.addFD(new FD("10001","00010"));
		part2.addFD(new FD("01000","00001"));
		part3.addFD(new FD("00110","00001"));
		
		Attribute.getInstance().addAttribute("A");
		Attribute.getInstance().addAttribute("B");
		Attribute.getInstance().addAttribute("C");
		Attribute.getInstance().addAttribute("D");
		Attribute.getInstance().addAttribute("E");
		assertEquals(Attribute.getInstance().getBitString("A"),"1");
		assertEquals(Attribute.getInstance().getBitString("B"),"10");
		assertEquals(Attribute.getInstance().getBitString("C"),"100");
		assertEquals(Attribute.getInstance().getBitString("D"),"1000");
		assertEquals(Attribute.getInstance().getBitString("E"),"10000");
		assertEquals(Attribute.getInstance().getAttrString("00001"),"A");
		assertEquals(Attribute.getInstance().getAttrString("00010"),"B");
		assertEquals(Attribute.getInstance().getAttrString("00100"),"C");
		assertEquals(Attribute.getInstance().getAttrString("01000"),"D");
		assertEquals(Attribute.getInstance().getAttrString("10000"),"E");
		assertEquals(Attribute.getInstance().numOfAttributes(),5);
		
		returnList = Bernstein.constructRelations(tempArray);
		assertEquals(returnList.size(),3);
		assertEquals(returnList.get(0).fDList.size(),1);
		assertEquals(returnList.get(1).fDList.size(),1);
		assertEquals(returnList.get(2).fDList.size(),1);
		assertEquals(returnList.get(0).GetAttrList().size(),3);
		assertEquals(returnList.get(0).GetAttrList().get(0),"E");
		assertEquals(returnList.get(0).GetAttrList().get(1),"A");
		assertEquals(returnList.get(0).GetAttrList().get(2),"B");
		assertEquals(returnList.get(1).GetAttrList().size(),2);
		assertEquals(returnList.get(1).GetAttrList().get(0),"D");
		assertEquals(returnList.get(1).GetAttrList().get(1),"A");
		assertEquals(returnList.get(2).GetAttrList().size(),3);
		assertEquals(returnList.get(2).GetAttrList().get(0),"C");
		assertEquals(returnList.get(2).GetAttrList().get(1),"B");
		assertEquals(returnList.get(2).GetAttrList().get(2),"A");
		assertEquals(returnList.get(0).priKeyList.size(),1);
		assertEquals(returnList.get(0).priKeyList.get(0),"AE");
		assertEquals(returnList.get(1).priKeyList.size(),1);
		assertEquals(returnList.get(1).priKeyList.get(0),"D");
		assertEquals(returnList.get(2).priKeyList.size(),1);
		assertEquals(returnList.get(2).priKeyList.get(0),"BC");
		
		Attribute.getInstance().clear();
	}
}
