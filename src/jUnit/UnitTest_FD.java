package jUnit;

import static org.junit.Assert.*;
import logic.FD;

import org.junit.Test;

public class UnitTest_FD {

	@Test
	public void test() {
		comparable();
	}
	
	private void comparable(){
		assertEquals(new FD("10","01").equals(new FD("10","01")),true); 
		assertEquals(new FD("10","01").compareTo(new FD("10","01"))==0,true); 
		assertEquals(new FD("10","01").equals(new FD("10","10")),false);
		assertEquals(new FD("10","01").compareTo(new FD("10","10"))!=0,true);
	}

}
