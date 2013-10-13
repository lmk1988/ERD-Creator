package jUnit;

import static org.junit.Assert.*;

import org.junit.Test;
import logic.Relation;
import java.util.ArrayList;
import logic.FD;

public class UnitTest_Relation {

	@Test
	public void test() {
		computeClosure();
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
	}

}
