package jUnit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Test;
import logic.Bernstein;
import logic.FD;

public class UnitTest_Bernstein {

	@Test
	public void test() {
		removeTrivial();
		splitRHS();
		removeExtraneousAttribute();
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
}
