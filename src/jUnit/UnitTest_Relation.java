package jUnit;

import static org.junit.Assert.*;

import org.junit.Test;

import logic.Attribute;
import logic.Relation;
import java.util.ArrayList;
import logic.FD;

public class UnitTest_Relation {

	@Test
	public void test() {
		computeClosure();
		getCandidateKeys();
	}
	
	private void computeClosure(){
		FD fd1 = new FD("001","010");
		FD fd2 = new FD("010","100");
		ArrayList<FD> tempArray = new ArrayList<FD>();
		tempArray.add(fd1);
		tempArray.add(fd2);
		assertEquals(Relation.computeClosure("001",tempArray),"111");
		assertEquals(Relation.computeClosure("010",tempArray),"110");
		assertEquals(Relation.computeClosure("100",tempArray),"100");
		
		tempArray.clear();
		fd1 = new FD("10000","01100");
		fd2 = new FD("01000","00010");
		FD fd3 = new FD("00010","10000");
		tempArray.add(fd1);
		tempArray.add(fd2);
		tempArray.add(fd3);
		assertEquals(Relation.computeClosure("10000",tempArray),"11110");
		assertEquals(Relation.computeClosure("01000",tempArray),"11110");
		assertEquals(Relation.computeClosure("00010",tempArray),"11110");
		assertEquals(Relation.computeClosure("00001",tempArray),"00001");
		
		tempArray.clear();
		fd1 = new FD("1","10");
		tempArray.add(fd1);
		assertEquals(Relation.computeClosure("10000",tempArray),"10000");
		assertEquals(Relation.computeClosure("10001",tempArray),"10011");
	}

	private void getCandidateKeys(){
		ArrayList<String> attributes = new ArrayList<String>();
		attributes.add("A");
		attributes.add("B");
		attributes.add("C");
		attributes.add("D");
		attributes.add("E");
		
		Relation tempRel = new Relation("R",attributes);
		assertEquals(tempRel.getCandidateKeys().size(),1);
		assertEquals(tempRel.getCandidateKeys().get(0),"ABCDE");
		tempRel.fDList.add(new FD(Attribute.getInstance().getBitString("A"),Attribute.getInstance().getBitString("B")));
		assertEquals(tempRel.fDList.size(),1);
		assertEquals(tempRel.getCandidateKeys().size(),1);
		assertEquals(tempRel.getCandidateKeys().get(0),"ACDE");
		tempRel.fDList.add(new FD(Attribute.getInstance().getBitString("B"),Attribute.getInstance().getBitString("C")));
		assertEquals(tempRel.fDList.size(),2);
		assertEquals(tempRel.getCandidateKeys().size(),1);
		assertEquals(tempRel.getCandidateKeys().get(0),"ADE");
		tempRel.fDList.add(new FD(Attribute.getInstance().getBitString("C"),Attribute.getInstance().getBitString("A")));
		assertEquals(tempRel.fDList.size(),3);
		assertEquals(tempRel.getCandidateKeys().size(),3);
		assertEquals(tempRel.getCandidateKeys().get(0),"CDE");
		assertEquals(tempRel.getCandidateKeys().get(1),"BDE");
		assertEquals(tempRel.getCandidateKeys().get(2),"ADE");
	}
}
