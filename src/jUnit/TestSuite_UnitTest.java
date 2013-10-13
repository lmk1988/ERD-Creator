package jUnit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ UnitTest_Attribute.class, UnitTest_FD.class })
public class TestSuite_UnitTest {

}
