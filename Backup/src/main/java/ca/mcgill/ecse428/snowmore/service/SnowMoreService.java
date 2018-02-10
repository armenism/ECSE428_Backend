package ca.mcgill.ecse428.snowmore.service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import ca.mcgill.ecse428.snowmore.model.Posting;
import ca.mcgill.ecse428.snowmore.model.Registration;
import ca.mcgill.ecse428.snowmore.model.RegistrationManager;
import ca.mcgill.ecse428.snowmore.model.User;
import ca.mcgill.ecse428.snowmore.persistence.PersistenceXStream;

@Service
public class SnowMoreService {

	private RegistrationManager rm;

	public SnowMoreService(RegistrationManager rm) {
		this.rm = rm;
	}

	public User createUser(String name) throws InvalidInputException {
		if (name == null || name.trim().length() == 0) {
			throw new InvalidInputException("User name cannot be empty!");
		}
		for (User par : getUsers()) {
			if (name.equals(par.getName())) {
				throw new InvalidInputException("User name already exists!");
			}
		}
		User p = new User(name, null, null, null, null, 0);

		rm.addUser(p);
		PersistenceXStream.saveToXMLwithXStream(rm);
		return p;
	}

	public List<User> getUsers() {
		return rm.getUsers();
	}

	public List<User> removeUser(String name) throws InvalidInputException {
		if (name == null) {
			throw new InvalidInputException("User name cannot be empty!");
		}
		rm.removeUser(getUserByName(name));

		PersistenceXStream.saveToXMLwithXStream(rm);
		return rm.getUsers();
	}

	public List<User> findAllUsers() {
		return rm.getUsers();
	}

	public Posting createPosting(String name, Date date, Time startTime, Time endTime) throws InvalidInputException {

		String error = "";

		// check for empty inputs
		if (name == null || name.trim().length() == 0) {
			error += "Posting name cannot be empty! ";
		}
		if (date == null) {
			error += "Posting date cannot be empty! ";
		}
		if (startTime == null) {
			error += "Posting start time cannot be empty! ";
		}
		if (endTime == null) {
			error += "Posting end time cannot be empty!";
		}

		// check for invalid inputs
		if (endTime != null && startTime != null && endTime.before(startTime)) {
			error += "Posting end time cannot be before posting start time!";
		}

		// throw error if applicable
		if (error != "") {
			throw new InvalidInputException(error);
		}

		for (Posting ev : getPostings()) {
			if (name.equals(ev.getName())) {
				throw new InvalidInputException("Posting name already exists!");
			}
		}
		Posting e = new Posting(name, null, date, startTime, endTime);
		rm.addPosting(e);
		PersistenceXStream.saveToXMLwithXStream(rm);
		return e;
	}

	public List<Posting> getPostings() {
		return rm.getPostings();
	}

	public List<Posting> removePosting(String name) throws InvalidInputException {
		if (name == null) {
			throw new InvalidInputException("Posting name cannot be empty!");
		}

		rm = (RegistrationManager) PersistenceXStream.loadFromXMLwithXStream();
		rm.removePosting(getPostingByName(name));

		PersistenceXStream.saveToXMLwithXStream(rm);
		return rm.getPostings();
	}

	public List<Posting> getPostingsForUser(User p) {
		List<Posting> Postings = Lists.newArrayList();

		for (Registration registration : rm.getRegistrations()) {
			if (registration.getUser().getName().equals(p.getName())) {
				Postings.add(registration.getPosting());
			}
		}
		return Postings;
	}

	public User getUserByName(String name) {
		for (User User : rm.getUsers()) {
			if (User.getName().equals(name)) {
				return User;
			}
		}
		return null;
	}

	public Posting getPostingByName(String name) {
		for (Posting Posting : rm.getPostings()) {
			if (Posting.getName().equals(name)) {
				return Posting;
			}
		}
		return null;
	}

	public Registration register(User u, Posting p) throws InvalidInputException {

		for (Registration r : rm.getRegistrations()) {
			if (r.getUser().getName().equals(u.getName()) && r.getPosting().getName().equals(p.getName())) {
				throw new InvalidInputException("Registration already exists!");
			}
		}
		Registration registration = new Registration(u, p);

		rm.addRegistration(registration);

		PersistenceXStream.saveToXMLwithXStream(rm);
		return registration;
	}

	public List<User> getUsersForPosting(Posting p) {
		List<User> PostingUsers = Lists.newArrayList();

		for (Registration registration : rm.getRegistrations()) {
			if (registration.getPosting().getName().equals(p.getName())) {
				PostingUsers.add(registration.getUser());
			}
		}

		return PostingUsers;
	}

	public List<Registration> getRegistrations() {
		return rm.getRegistrations();
	}

	public List<Registration> removeRegistration(User u, Posting p) {

		for (Registration r : rm.getRegistrations()) {
			if (r.getUser().getName().equals(u.getName()) && r.getPosting().getName().equals(p.getName())) {
				rm.removeRegistration(r);
				break;
			}
		}
		PersistenceXStream.saveToXMLwithXStream(rm);
		return rm.getRegistrations();
	}
	
	public void clearAll() {

		rm.delete();
		PersistenceXStream.saveToXMLwithXStream(rm);
	}
}
