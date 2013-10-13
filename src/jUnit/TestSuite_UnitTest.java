package jUnit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	UnitTest_Attribute.class, 
	UnitTest_FD.class,
	UnitTest_Partition.class,
	UnitTest_Relation.class,
	UnitTest_Bernstein.class
	})
public class TestSuite_UnitTest {

}
