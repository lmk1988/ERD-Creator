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
		instance();
		IS_BIT_EQUAL();
	}
	
	private void instance(){
		assertEquals(Attribute.getInstance().addAttribute("A"),"1");
		assertEquals(Attribute.getInstance().addAttribute("B"),"10");
		assertEquals(Attribute.getInstance().addAttribute("C"),"100");
		assertEquals(Attribute.getInstance().addAttribute("D"),"1000");
		assertEquals(Attribute.getInstance().addAttribute("E"),"10000");
		assertEquals(Attribute.getInstance().addAttribute("F"),"100000");
		assertEquals(Attribute.getInstance().addAttribute("G"),"1000000");
		assertEquals(Attribute.getInstance().addAttribute("H"),"10000000");
		assertEquals(Attribute.getInstance().addAttribute("I"),"100000000");
		assertEquals(Attribute.getInstance().addAttribute("J"),"1000000000");
		assertEquals(Attribute.getInstance().addAttribute("K"),"10000000000");
		assertEquals(Attribute.getInstance().addAttribute("L"),"100000000000");
		assertEquals(Attribute.getInstance().addAttribute("M"),"1000000000000");
		assertEquals(Attribute.getInstance().addAttribute("N"),"10000000000000");
		assertEquals(Attribute.getInstance().addAttribute("O"),"100000000000000");
		assertEquals(Attribute.getInstance().addAttribute("P"),"1000000000000000");
		assertEquals(Attribute.getInstance().addAttribute("Q"),"10000000000000000");
		assertEquals(Attribute.getInstance().addAttribute("R"),"100000000000000000");
		assertEquals(Attribute.getInstance().addAttribute("S"),"1000000000000000000");
		assertEquals(Attribute.getInstance().addAttribute("T"),"10000000000000000000");
		assertEquals(Attribute.getInstance().addAttribute("U"),"100000000000000000000");
		assertEquals(Attribute.getInstance().addAttribute("V"),"1000000000000000000000");
		assertEquals(Attribute.getInstance().addAttribute("W"),"10000000000000000000000");
		assertEquals(Attribute.getInstance().addAttribute("X"),"100000000000000000000000");
		assertEquals(Attribute.getInstance().addAttribute("Y"),"1000000000000000000000000");
		assertEquals(Attribute.getInstance().addAttribute("Z"),"10000000000000000000000000");
		assertEquals(Attribute.getInstance().addAttribute("Apple"),"100000000000000000000000000");
		assertEquals(Attribute.getInstance().addAttribute("Banana"),"1000000000000000000000000000");
		assertEquals(Attribute.getInstance().addAttribute("Grapes"),"10000000000000000000000000000");
		assertEquals(Attribute.getInstance().addAttribute("Durian"),"100000000000000000000000000000");
		assertEquals(Attribute.getInstance().addAttribute("Cherry"),"1000000000000000000000000000000");
		assertEquals(Attribute.getInstance().addAttribute("A"),"1");
		assertEquals(Attribute.getInstance().addAttribute("B"),"10");
		
		assertEquals(Attribute.getInstance().getBitString("A"),"1");
		assertEquals(Attribute.getInstance().getBitString("B"),"10");
		assertEquals(Attribute.getInstance().getBitString("C"),"100");
		assertEquals(Attribute.getInstance().getBitString("D"),"1000");
		assertEquals(Attribute.getInstance().getBitString("Grapes"),"10000000000000000000000000000");
		assertEquals(Attribute.getInstance().addAttribute("Cherry"),"1000000000000000000000000000000");
		
		assertEquals(Attribute.getInstance().getAttrString("1"),"A");
		assertEquals(Attribute.getInstance().getAttrString("10"),"B");
		assertEquals(Attribute.getInstance().getAttrString("10000000000000000000000000"),"Z");
		assertEquals(Attribute.getInstance().getAttrString("1000000000000000000000"),"V");
		assertEquals(Attribute.getInstance().getAttrString("100000000000000000000000000000"),"Durian");
		assertEquals(Attribute.getInstance().getAttrString("1000000000000000000000000000000"),"Cherry");
		assertEquals(Attribute.getInstance().getAttrString("1000000000000000000000000000010"),"B,Cherry");
	}
	
	private void IS_BIT_EQUAL(){
		assertEquals(Attribute.IS_BIT_EQUAL("",""),true);
		assertEquals(Attribute.IS_BIT_EQUAL("1",""),false);
		assertEquals(Attribute.IS_BIT_EQUAL("","1"),false);
		assertEquals(Attribute.IS_BIT_EQUAL("001","1"),true);
		assertEquals(Attribute.IS_BIT_EQUAL("010","10"),true);
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
		assertEquals(Attribute.AND("10001","1"),"00001");
		assertEquals(Attribute.AND("10001","10"),"00000");
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
		assertEquals(Attribute.OR("10001","10"),"10011");
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
