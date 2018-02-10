package ca.mcgill.ecse428.snowmore.service;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import java.io.File;
import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.mcgill.ecse428.snowmore.model.Posting;
import ca.mcgill.ecse428.snowmore.model.User;
import ca.mcgill.ecse428.snowmore.model.RegistrationManager;
import ca.mcgill.ecse428.snowmore.persistence.PersistenceXStream;
import ca.mcgill.ecse428.snowmore.service.InvalidInputException;
import ca.mcgill.ecse428.snowmore.service.SnowMoreService;

public class TestEventRegistrationService {

	private RegistrationManager rm;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PersistenceXStream.initializeModelManager("output" + File.separator + "data.xml");
	}

	@Before
	public void setUp() throws Exception {
		rm = new RegistrationManager();
	}

	@After
	public void tearDown() throws Exception {
		rm.delete();
	}

	@Test
	public void testCreateUser() {
		assertEquals(0, rm.getUsers().size());

		String name = "Oscar";
		SnowMoreService erc = new SnowMoreService(rm);
		try {
			erc.createUser(name);
		} catch (InvalidInputException e) {
			// Check that no error occured
			fail();
		}

		RegistrationManager rm1 = rm;
		checkResultUser(name, rm1);

		RegistrationManager rm2 = (RegistrationManager) PersistenceXStream.loadFromXMLwithXStream();
		checkResultUser(name, rm2);

		rm2.delete();
	}

	@Test
	public void testCreateUserNull() {
		assertEquals(0, rm.getUsers().size());
		String name = null;
		String error = null;

		SnowMoreService erc = new SnowMoreService(rm);
		try {
			erc.createUser(name);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals("User name cannot be empty!", error);

		// check no change in memory
		assertEquals(0, rm.getUsers().size());
	}

	@Test
	public void testCreateUserEmpty() {
		assertEquals(0, rm.getUsers().size());

		String name = "";

		String error = null;
		SnowMoreService erc = new SnowMoreService(rm);
		try {
			erc.createUser(name);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals("User name cannot be empty!", error);

		// check no change in memory
		assertEquals(0, rm.getUsers().size());
	}

	@Test
	public void testCreateUserSpaces() {
		assertEquals(0, rm.getUsers().size());

		String name = " ";

		String error = null;
		SnowMoreService erc = new SnowMoreService(rm);
		try {
			erc.createUser(name);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals("User name cannot be empty!", error);

		// check no change in memory
		assertEquals(0, rm.getUsers().size());
	}

	@Test
	public void testFindAllUsers() {
		assertEquals(0, rm.getUsers().size());

		String[] names = { "John Doe", "Foo Bar" };

		SnowMoreService erc = new SnowMoreService(rm);
		for (String name : names) {
			try {
				erc.createUser(name);
			} catch (InvalidInputException e) {
				// Check that no error occured
				fail();
			}
		}

		List<User> registeredUsers = erc.findAllUsers();

		// check number of registered Users
		assertEquals(2, registeredUsers.size());

		// check each User
		for (int i = 0; i < names.length; i++) {
			assertEquals(names[i], registeredUsers.get(i).getName());
		}

	}

	@Test
	public void testCreatePosting() {
		RegistrationManager rm = new RegistrationManager();
		assertEquals(0, rm.getPostings().size());

		String name = "Soccer Game";
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date PostingDate = new Date(c.getTimeInMillis());
		Time startTime = new Time(c.getTimeInMillis());
		c.set(2017, Calendar.MARCH, 16, 10, 30, 0);
		Time endTime = new Time(c.getTimeInMillis());
		// test model in memory
		SnowMoreService erc = new SnowMoreService(rm);
		try {
			erc.createPosting(name, PostingDate, startTime, endTime);
		} catch (InvalidInputException e) {
			fail();
		}
		checkResultPosting(name, PostingDate, startTime, endTime, rm);
		// test file
		RegistrationManager rm2 = (RegistrationManager) PersistenceXStream.loadFromXMLwithXStream();
		checkResultPosting(name, PostingDate, startTime, endTime, rm2);
		rm2.delete();
	}

	private void checkResultPosting(String name, Date PostingDate, Time startTime, Time endTime, RegistrationManager rm2) {
		assertEquals(0, rm2.getUsers().size());
		assertEquals(1, rm2.getPostings().size());
		assertEquals(name, rm2.getPosting(0).getName());
//		assertEquals(PostingDate.toString(), rm2.getPosting(0).getPostingDate().toString());
		assertEquals(startTime.toString(), rm2.getPosting(0).getStartTime().toString());
		assertEquals(endTime.toString(), rm2.getPosting(0).getEndTime().toString());
		assertEquals(0, rm2.getRegistrations().size());
	}

	@Test
	public void testRegister() throws InvalidInputException {
		RegistrationManager rm = new RegistrationManager();
		assertEquals(0, rm.getRegistrations().size());

		String nameP = "Oscar";
		User User = new User(nameP, null, null, null, null, 0);
		rm.addUser(User);
		assertEquals(1, rm.getUsers().size());

		String nameE = "Soccer Game";
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date PostingDate = new Date(c.getTimeInMillis());
		Time startTime = new Time(c.getTimeInMillis());
		c.set(2017, Calendar.MARCH, 16, 10, 30, 0);
		Time endTime = new Time(c.getTimeInMillis());
		Posting Posting = new Posting(nameE, null, PostingDate, startTime, endTime);
		rm.addPosting(Posting);
		assertEquals(1, rm.getPostings().size());

		SnowMoreService erc = new SnowMoreService(rm);
		erc.register(User, Posting);
		checkResultRegister(nameP, nameE, PostingDate, startTime, endTime, rm);

		RegistrationManager rm2 = (RegistrationManager) PersistenceXStream.loadFromXMLwithXStream();
		// check file contents
		checkResultRegister(nameP, nameE, PostingDate, startTime, endTime, rm2);
		rm2.delete();
	}

	private void checkResultUser(String name, RegistrationManager rm2) {
		assertEquals(1, rm2.getUsers().size());
		assertEquals(name, rm2.getUser(0).getName());
		assertEquals(0, rm2.getPostings().size());
		assertEquals(0, rm2.getRegistrations().size());
	}

	private void checkResultRegister(String nameP, String nameE, Date PostingDate, Time startTime, Time endTime,
			RegistrationManager rm2) {
		assertEquals(1, rm2.getUsers().size());
		assertEquals(nameP, rm2.getUser(0).getName());
		assertEquals(1, rm2.getPostings().size());
		assertEquals(nameE, rm2.getPosting(0).getName());
//		assertEquals(PostingDate.toString(), rm2.getPosting(0).getPostingDate().toString());
		assertEquals(startTime.toString(), rm2.getPosting(0).getStartTime().toString());
		assertEquals(endTime.toString(), rm2.getPosting(0).getEndTime().toString());
		assertEquals(1, rm2.getRegistrations().size());
		assertEquals(rm2.getPosting(0), rm2.getRegistration(0).getPosting());
		assertEquals(rm2.getUser(0), rm2.getRegistration(0).getUser());
	}

	@Test
	public void testCreatePostingNull() {
		assertEquals(0, rm.getRegistrations().size());

		String name = null;
		Date PostingDate = null;
		Time startTime = null;
		Time endTime = null;

		String error = null;
		SnowMoreService erc = new SnowMoreService(rm);
		try {
			erc.createPosting(name, PostingDate, startTime, endTime);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals(
				"Posting name cannot be empty! Posting date cannot be empty! Posting start time cannot be empty! Posting end time cannot be empty!",
				error);
		// check model in memory
		assertEquals(0, rm.getPostings().size());
	}

	@Test
	public void testCreatePostingEmpty() {
		assertEquals(0, rm.getPostings().size());

		String name = "";
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.FEBRUARY, 16, 10, 00, 0);
		Date PostingDate = new Date(c.getTimeInMillis());
		Time startTime = new Time(c.getTimeInMillis());
		c.set(2017, Calendar.FEBRUARY, 16, 11, 30, 0);
		Time endTime = new Time(c.getTimeInMillis());

		String error = null;
		SnowMoreService erc = new SnowMoreService(rm);
		try {
			erc.createPosting(name, PostingDate, startTime, endTime);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals("Posting name cannot be empty! ", error);
		// check model in memory
		assertEquals(0, rm.getPostings().size());
	}

	@Test
	public void testCreatePostingSpaces() {
		assertEquals(0, rm.getPostings().size());

		String name = " ";
		Calendar c = Calendar.getInstance();
		c.set(2016, Calendar.OCTOBER, 16, 9, 00, 0);
		Date PostingDate = new Date(c.getTimeInMillis());
		Time startTime = new Time(c.getTimeInMillis());
		c.set(2016, Calendar.OCTOBER, 16, 10, 30, 0);
		Time endTime = new Time(c.getTimeInMillis());

		String error = null;
		SnowMoreService erc = new SnowMoreService(rm);
		try {
			erc.createPosting(name, PostingDate, startTime, endTime);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		// check error
		assertEquals("Posting name cannot be empty! ", error);
		// check model in memory
		assertEquals(0, rm.getPostings().size());
	}

	@Test
	public void testCreatePostingEndTimeBeforeStartTime() {
		assertEquals(0, rm.getPostings().size());

		String name = "Soccer Game";
		Calendar c = Calendar.getInstance();
		c.set(2016, Calendar.OCTOBER, 16, 9, 00, 0);
		Date PostingDate = new Date(c.getTimeInMillis());
		Time startTime = new Time(c.getTimeInMillis());
		c.set(2016, Calendar.OCTOBER, 16, 8, 59, 59);
		Time endTime = new Time(c.getTimeInMillis());

		String error = null;
		SnowMoreService erc = new SnowMoreService(rm);
		try {
			erc.createPosting(name, PostingDate, startTime, endTime);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals("Posting end time cannot be before Posting start time!", error);

		// check model in memory
		assertEquals(0, rm.getPostings().size());
	}

}
