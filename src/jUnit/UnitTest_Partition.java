package jUnit;

import static org.junit.Assert.*;

import org.junit.Test;
import logic.Partition;
import logic.FD;
import java.util.ArrayList;


public class UnitTest_Partition {

	@Test
	public void test() {
		constructor();
		FD_functions();
		getLHS();
	}
	
	private void constructor(){
		Partition test = new Partition();
		assertEquals(test.joinList.size(),0);
		assertEquals(test.getFDSize(),0);
		test = new Partition(new FD("11","00"));
		assertEquals(test.getFDSize(),1);
		assertEquals(test.joinList.size(),0);
	}
	
	private void FD_functions(){
		Partition test = new Partition();
		test.addFD(new FD("100","010"));
		assertEquals(test.getFDSize(),1);
		test.addFD(new FD("100","010"));
		assertEquals(test.getFDSize(),1);
		test.addFD(new FD("100","001"));
		assertEquals(test.getFDSize(),2);
		test.addFD(new FD("010","100"));
		assertEquals(test.getFDSize(),2);
		test.addFD(new FD("001","010"));
		assertEquals(test.getFDSize(),2);
		test.removeFD(new FD("001","010"));
		assertEquals(test.getFDSize(),2);
		test.removeFD(new FD("011","010"));
		assertEquals(test.getFDSize(),2);
		test.removeFD(new FD("100","010"));
		assertEquals(test.getFDSize(),1);
		
		test = new Partition(new FD("0110","1000"));
		assertEquals(test.getFDSize(),1);
		FD fd1 = new FD("0110","0100");
		FD fd2 = new FD("0110","0001");
		FD fd3 = new FD("0110","1000");
		ArrayList<FD> tempArray = new ArrayList<FD>();
		tempArray.add(fd1);
		tempArray.add(fd2);
		tempArray.add(fd3);
		test.addFDs(tempArray);
		assertEquals(test.getFDSize(),3);
		test.addFD(new FD("1000","0001"));
		assertEquals(test.getFDSize(),3);
		
		test.joinList.add(new FD("1000","0110"));
		test.joinList.add(new FD("0110","1000"));
		assertEquals(test.getFDSize(),3);
		test.addFD(new FD("1000","0001"));
		assertEquals(test.getFDSize(),4);
	}
	
	private void getLHS(){
		Partition test = new Partition();
		assertEquals(test.getLHS(),"");
		test.addFD(new FD("0100","1000"));
		assertEquals(test.getLHS(),"0100");
		test.joinList.add(new FD("0001","0100"));
		test.joinList.add(new FD("0100","0001"));
		assertEquals(test.getLHS(),"0100"); //getLHS should only get the first one
	}

}
