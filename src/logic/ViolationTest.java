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
		tempRel.fDList.add(new FD("01110", "10000"));
		tempRel.fDList.add(new FD("10000", "10000"));
		assertEquals(Violation.checkBCNF(tempRel, tempRel.fDList.get(0)), false);
		assertEquals(Violation.checkBCNF(tempRel, tempRel.fDList.get(1)), true);
		
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
		tempRel.fDList.add(new FD("10000", "00010"));//A->C
		tempRel.fDList.add(new FD("10000", "00001"));//A->C
		
		assertEquals(Violation.check3NF(tempRel, tempRel.fDList.get(2)), false);
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
		Attribute.getInstance().clear();
	}
}
