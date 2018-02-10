package ca.mcgill.ecse428.snowmore.persistence;

import java.io.File;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.mcgill.ecse428.snowmore.model.Posting;
import ca.mcgill.ecse428.snowmore.model.Registration;
import ca.mcgill.ecse428.snowmore.model.RegistrationManager;
import ca.mcgill.ecse428.snowmore.model.User;
import ca.mcgill.ecse428.snowmore.persistence.PersistenceXStream;

public class TestPersistence {
	
	private RegistrationManager rm;

	@Before
	public void setUp() throws Exception {

	    rm = new RegistrationManager();

	    // create Users
	    User pa = new User("Martin", null, null, null, null, 0);
	    User pa2 = new User("Jennifer", null, null, null, null, 0);

	    // create Posting
	    Calendar c = Calendar.getInstance();
	    c.set(2015,Calendar.SEPTEMBER,15,8,30,0);
	    Date PostingDate = new Date(c.getTimeInMillis());
	    Time startTime = new Time(c.getTimeInMillis());
	    c.set(2015,Calendar.SEPTEMBER,15,10,0,0);
	    Time endTime = new Time(c.getTimeInMillis());
	    Posting ev = new Posting("Concert", null, PostingDate, startTime, endTime);

	    // register Users to Posting
	    Registration re = new Registration(pa, ev);
	    Registration re2 = new Registration(pa2, ev);

	    // manage registrations
	    rm.addRegistration(re);
	    rm.addRegistration(re2);
	    rm.addPosting(ev);
	    rm.addUser(pa);
	    rm.addUser(pa2);
	}

	@After
	public void tearDown() throws Exception {
	    rm.delete();
	}

	@Test
	public void test() {
	    // initialize model file
	    PersistenceXStream.initializeModelManager("output"+File.separator+"data.xml");
	    // save model that is loaded during test setup
	    if (!PersistenceXStream.saveToXMLwithXStream(rm))
	        fail("Could not save file.");

	    // clear the model in memory
	    rm.delete();
	    assertEquals(0, rm.getUsers().size());
	    assertEquals(0, rm.getPostings().size());
	    assertEquals(0, rm.getRegistrations().size());

	    // load model
	    rm = (RegistrationManager) PersistenceXStream.loadFromXMLwithXStream();
	    if (rm == null)
	        fail("Could not load file.");

	    // check Users
	    assertEquals(2, rm.getUsers().size());
	    assertEquals("Martin", rm.getUser(0).getName());
	    assertEquals("Jennifer", rm.getUser(1).getName());
	    // check Posting
	    assertEquals(1, rm.getPostings().size());
	    assertEquals("Concert", rm.getPosting(0).getName());
	    Calendar c = Calendar.getInstance();
	    c.set(2015,Calendar.SEPTEMBER,15,8,30,0);
	    Date PostingDate = new Date(c.getTimeInMillis());
	    Time startTime = new Time(c.getTimeInMillis());
	    c.set(2015,Calendar.SEPTEMBER,15,10,0,0);
	    Time endTime = new Time(c.getTimeInMillis());
	    //assertEquals(PostingDate.toString(), rm.getPosting(0).getPostingDate().toString());
	    assertEquals(startTime.toString(), rm.getPosting(0).getStartTime().toString());
	    assertEquals(endTime.toString(), rm.getPosting(0).getEndTime().toString());
	    // check registrations
	    assertEquals(2, rm.getRegistrations().size());
	    assertEquals(rm.getPosting(0), rm.getRegistration(0).getPosting());
	    assertEquals(rm.getUser(0), rm.getRegistration(0).getUser());
	    assertEquals(rm.getPosting(0), rm.getRegistration(1).getPosting());
	    assertEquals(rm.getUser(1), rm.getRegistration(1).getUser());
	}

}
