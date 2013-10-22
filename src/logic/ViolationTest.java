package logic;

import static org.junit.Assert.*;
import logic.Attribute;
import logic.Relation;
import java.util.ArrayList;
import logic.FD;

import java.util.ArrayList;

import org.junit.Test;

public class ViolationTest {

	@Test
	public void test() {
		getNonPrimeAttributes();
		checkBCNF();
		check3NF();
		check2NF();
	}
	private void getNonPrimeAttributes()
	{
		ArrayList<String> attributes = new ArrayList<String>();
		attributes.add("A");
		attributes.add("B");
		attributes.add("C");
		attributes.add("D");
		attributes.add("E");
		
		Relation tempRel = new Relation("R",attributes);
		tempRel.fDList.add(new FD(Attribute.getInstance().getBitString("B"),Attribute.getInstance().getBitString("A")));
		tempRel.fDList.add(new FD(Attribute.getInstance().getBitString("B"),Attribute.getInstance().getBitString("C")));
		tempRel.fDList.add(new FD(Attribute.getInstance().getBitString("B"),Attribute.getInstance().getBitString("D")));
		tempRel.fDList.add(new FD(Attribute.getInstance().getBitString("B"),Attribute.getInstance().getBitString("E")));
		
		assertEquals(Violation.getNonPrimeAttributes(tempRel).get(0), "00001");//A
		assertEquals(Violation.getNonPrimeAttributes(tempRel).get(1), "00100");//C
		assertEquals(Violation.getNonPrimeAttributes(tempRel).get(2), "01000");//D
		assertEquals(Violation.getNonPrimeAttributes(tempRel).get(3), "10000");//E
		
		Attribute.getInstance().clear();
	}
	private void checkBCNF()
	{
		ArrayList<String> attributes = new ArrayList<String>();
		attributes.add("A");
		attributes.add("B");
		attributes.add("C");
		attributes.add("D");
		attributes.add("E");
		
		Relation tempRel = new Relation("R",attributes);
		tempRel.fDList.add(new FD("0111", "1000"));
		tempRel.fDList.add(new FD("1000", "1000"));
		assertEquals(Violation.checkBCNF(tempRel, tempRel.fDList.get(0)), false);
		assertEquals(Violation.checkBCNF(tempRel, tempRel.fDList.get(1)), true);
		
		assertEquals(Violation.checkRelationBCNF(tempRel), false);
		tempRel.fDList.clear();
		attributes.remove("E");
		tempRel.fDList.add(new FD("1100", "0011"));
		tempRel.fDList.add(new FD("0001", "0100"));
		assertEquals(Violation.checkBCNF(tempRel, tempRel.fDList.get(0)), true);
		assertEquals(Violation.checkBCNF(tempRel, tempRel.fDList.get(1)), false);
		assertEquals(Violation.checkRelationBCNF(tempRel), false);
		Attribute.getInstance().clear();
	}

	private void check3NF()
	{
		ArrayList<String> attributes = new ArrayList<String>();
		attributes.add("A");
		attributes.add("B");
		attributes.add("C");
		attributes.add("D");
		attributes.add("E");
		
		Relation tempRel = new Relation("R",attributes);
		tempRel.fDList.add(new FD("10000", "01000"));//A->B
		tempRel.fDList.add(new FD("01000", "00100"));//B->C
		tempRel.fDList.add(new FD("10000", "00100"));//A->C
		tempRel.fDList.add(new FD("10000", "00010"));//A->E
		tempRel.fDList.add(new FD("10000", "00001"));//A->F
		
		assertEquals(Violation.check3NF(tempRel, tempRel.fDList.get(2)), false);
		assertEquals(Violation.checkRelation3NF(tempRel), false);
		tempRel.fDList.clear();
		
		tempRel.fDList.add(new FD("10000", "01100"));//A->BC
		tempRel.fDList.add(new FD("01100", "00010"));//BC->D
		tempRel.fDList.add(new FD("10000", "00010"));//A->D
		tempRel.fDList.add(new FD("10000", "00010"));//A->E
		tempRel.fDList.add(new FD("10000", "00001"));//A->F
		
		assertEquals(Violation.check3NF(tempRel, tempRel.fDList.get(2)), false);
		tempRel.fDList.clear();
			
		attributes.clear();
		attributes.add("A");
		attributes.add("B");
		attributes.add("C");
		
		tempRel.fDList.add(new FD("100", "011"));//A->BC
		tempRel.fDList.add(new FD("010", "001"));//B->C
		assertEquals(Violation.checkRelation3NF(tempRel), false);
		
		tempRel.fDList.clear();
		tempRel.fDList.add(new FD("101", "010"));
		tempRel.fDList.add(new FD("010", "001"));
		assertEquals(Violation.checkRelation3NF(tempRel), true);
		assertEquals(Violation.checkRelationBCNF(tempRel), false);
		
		attributes.add("D");
		tempRel.fDList.add(new FD("1100", "0011"));
		tempRel.fDList.add(new FD("0010", "0001"));
		assertEquals(Violation.checkRelation3NF(tempRel), false);
		assertEquals(Violation.check3NF(tempRel, tempRel.fDList.get(0)), true);
		assertEquals(Violation.check3NF(tempRel, tempRel.fDList.get(1)), true);
		assertEquals(Violation.check3NF(tempRel, new FD("1100","0001")), false);
		assertEquals(Violation.checkRelation3NF(tempRel), false);
		Attribute.getInstance().clear();
	}
	private void check2NF()
	{
		ArrayList<String> attributes = new ArrayList<String>();
		attributes.add("A");
		attributes.add("B");
		attributes.add("C");
		attributes.add("D");
		attributes.add("E");
		
		Relation tempRel = new Relation("R",attributes);
		tempRel.fDList.add(new FD("10000", "01000"));
		tempRel.fDList.add(new FD("00010", "00001"));
		tempRel.fDList.add(new FD("11000", "00100"));
		assertEquals(Violation.check2NF(tempRel, tempRel.fDList.get(1)), false);
		
		assertEquals(Violation.checkRelation2NF(tempRel), false);
		
		tempRel.fDList.clear();
		tempRel.fDList.add(new FD("11000", "00111"));
		tempRel.fDList.add(new FD("10000", "00100"));
		tempRel.fDList.add(new FD("00010", "00001"));
		assertEquals(Violation.checkRelation2NF(tempRel), false);
		Attribute.getInstance().clear();
	}
}
