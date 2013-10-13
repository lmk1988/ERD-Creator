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
