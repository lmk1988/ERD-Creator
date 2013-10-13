package jUnit;

import static org.junit.Assert.*;
import logic.FD;

import org.junit.Test;

public class UnitTest_FD {

	@Test
	public void test() {
		comparable();
		constructor();
	}
	
	private void comparable(){
		assertEquals(new FD("10","01").equals(new FD("10","01")),true); 
		assertEquals(new FD("10","01").compareTo(new FD("10","01"))==0,true); 
		assertEquals(new FD("10","01").equals(new FD("10","10")),false);
		assertEquals(new FD("10","01").compareTo(new FD("10","10"))!=0,true);
	}
	
	private void constructor(){
		FD test1 = new FD();
		assertEquals(test1.LHS,"");
		assertEquals(test1.RHS,"");
		test1 = new FD("001","010");
		assertEquals(test1.LHS,"001");
		assertEquals(test1.RHS,"010");
	}

}
