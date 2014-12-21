package soundcloud.server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ UserTest.class, UserRegistryTest.class, MessageTest.class,
				SourceListenerTest.class, //MessageCollectorTest.class, 
				ClientListenerTest.class
				})
public class TestRunner {

}
