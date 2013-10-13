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
	}
}
