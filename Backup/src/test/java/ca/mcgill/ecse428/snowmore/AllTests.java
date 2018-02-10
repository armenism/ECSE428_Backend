package ca.mcgill.ecse428.snowmore;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ca.mcgill.ecse428.snowmore.persistence.TestPersistence;
import ca.mcgill.ecse428.snowmore.service.TestEventRegistrationService;

@RunWith(Suite.class)
@SuiteClasses({ TestEventRegistrationService.class, TestPersistence.class })
public class AllTests {

}
