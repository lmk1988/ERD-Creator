package jUnit;
import logic.Attribute;
import static org.junit.Assert.*;
import org.junit.Test;

public class UnitTest_Attribute {
	
	@Test
	public void test() {
		IS_ALL_ONES();
		INVERSE();
		ALL_PROPER_SUBSET_OF();
		OR();
		AND();
	}
	
	private void AND(){
		assertEquals(Attribute.AND("",""),"");
		assertEquals(Attribute.AND("1",""),"");
		assertEquals(Attribute.AND("1","0"),"0");
		assertEquals(Attribute.AND("01","10"),"00");
		assertEquals(Attribute.AND("11","10"),"10");
		assertEquals(Attribute.AND("01","11"),"01");
		assertEquals(Attribute.AND("01001","10000000"),"00000000");
		assertEquals(Attribute.AND("1111","11110000"),"00000000");
	}
	
	private void OR(){
		assertEquals(Attribute.OR("",""),"");
		assertEquals(Attribute.OR("10",""),"10");
		assertEquals(Attribute.OR("","1"),"1");
		assertEquals(Attribute.OR("111","000"),"111");
		assertEquals(Attribute.OR("01","10"),"11");
		assertEquals(Attribute.OR("000","000"),"000");
		assertEquals(Attribute.OR("101","101"),"101");
		assertEquals(Attribute.OR("0","1"),"1");
		assertEquals(Attribute.OR("00","100"),"100");
		assertEquals(Attribute.OR("000","0"),"000");
		assertEquals(Attribute.OR("10111","1"),"10111");
	}
	
	private void IS_ALL_ONES(){
		assertEquals(Attribute.IS_ALL_ONES("1111"),true);
		assertEquals(Attribute.IS_ALL_ONES("1101"),false);
		assertEquals(Attribute.IS_ALL_ONES("0101"),false);
		assertEquals(Attribute.IS_ALL_ONES("0000"),false);
		assertEquals(Attribute.IS_ALL_ONES("0001"),false);
		assertEquals(Attribute.IS_ALL_ONES("1001"),false);
		assertEquals(Attribute.IS_ALL_ONES("1"),true);
		assertEquals(Attribute.IS_ALL_ONES(""),false);
	}
	
	private void INVERSE(){
		assertEquals(Attribute.INVERSE("0000").compareTo("1111")==0,true);
		assertEquals(Attribute.INVERSE("0110").compareTo("1001")==0,true);
		assertEquals(Attribute.INVERSE("1001").compareTo("0110")==0,true);
		assertEquals(Attribute.INVERSE("1010").compareTo("0101")==0,true);
		assertEquals(Attribute.INVERSE("0").compareTo("1")==0,true);
		assertEquals(Attribute.INVERSE("").compareTo("")==0,true);
	}
	
	private void ALL_PROPER_SUBSET_OF(){
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("0000").size()==0,true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("0100").size()==0,true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1001").contains("1000"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1001").contains("0001"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1001").size()==2,true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1011").contains("1000"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1011").contains("0010"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1011").contains("0001"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1011").contains("1010"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1011").contains("1001"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1011").contains("0011"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1011").size()==6,true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1111").contains("1000"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1111").contains("0100"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1111").contains("0010"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1111").contains("0001"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1111").contains("1100"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1111").contains("1010"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1111").contains("1001"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1111").contains("0110"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1111").contains("0101"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1111").contains("0011"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1111").contains("1110"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1111").contains("1101"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1111").contains("0111"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1111").contains("1011"),true);
		assertEquals(Attribute.ALL_PROPER_SUBSET_OF("1111").size()==14,true);
	}	
}
