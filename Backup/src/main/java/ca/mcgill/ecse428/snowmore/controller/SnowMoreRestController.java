package ca.mcgill.ecse428.snowmore.controller;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

import ca.mcgill.ecse428.snowmore.dto.PostingDto;
import ca.mcgill.ecse428.snowmore.dto.UserDto;
import ca.mcgill.ecse428.snowmore.dto.RegistrationDto;
import ca.mcgill.ecse428.snowmore.model.Posting;
import ca.mcgill.ecse428.snowmore.model.User;
import ca.mcgill.ecse428.snowmore.model.Registration;
import ca.mcgill.ecse428.snowmore.service.InvalidInputException;
import ca.mcgill.ecse428.snowmore.service.SnowMoreService;

@RestController
public class SnowMoreRestController {

	@Autowired
	private SnowMoreService service;

	@Autowired
	private ModelMapper modelMapper;

	@RequestMapping("/")
	public String index() {
		return "Posting registration application root. Web-based frontend is a TODO. Use the REST API to manage Postings and Users.\n";
	}

	/*
	 * User RESTController
	 */
	@PostMapping(value = { "/Users/{name}", "/Users/{name}/" })
	public UserDto createUser(@PathVariable("name") String name) throws InvalidInputException {
		User User = service.createUser(name);
		return convertToDto(User);
	}

	@GetMapping(value = { "/Users", "/Users/" })
	public List<UserDto> getUsers() throws InvalidInputException {
		List<User> Users = service.getUsers();
		return convertToUserDtoList(Users);
	}

	@DeleteMapping(value = { "/Users/{name}", "/Users/{name}/" })
	public List<UserDto> removeUser(@PathVariable("name") String name) throws InvalidInputException {
		List<User> Users = service.removeUser(name);
		return convertToUserDtoList(Users);
	}

	/*
	 * Posting RESTController
	 */
	@PostMapping(value = { "/Postings/{name}", "/Postings/{name}/" })
	public PostingDto createPosting(@PathVariable("name") String name, @RequestParam Date date,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime)
			throws InvalidInputException {
		@SuppressWarnings("deprecation")
		Time startTimeSql = new Time(startTime.getHour(), startTime.getMinute(), 0);
		@SuppressWarnings("deprecation")
		Time endTimeSql = new Time(endTime.getHour(), endTime.getMinute(), 0);
		Posting Posting = service.createPosting(name, date, startTimeSql, endTimeSql);
		return convertToDto(Posting);
	}

	@GetMapping(value = { "/Postings", "/Postings/" })
	public List<PostingDto> getPostings() throws InvalidInputException {
		List<Posting> Postings = service.getPostings();
		return convertToPostingDtoList(Postings);
	}

	@DeleteMapping(value = { "/Postings/{name}", "/Postings/{name}/" })
	public List<PostingDto> removePosting(@PathVariable("name") String name) throws InvalidInputException {
		List<Posting> Postings = service.removePosting(name);
		return convertToPostingDtoList(Postings);
	}

	/*
	 * Registration RESTController
	 */
	@PostMapping(value = { "/register", "/register/" })
	public RegistrationDto registerUserForPosting(@RequestParam(name = "User") UserDto pDto,
			@RequestParam(name = "Posting") PostingDto eDto) throws InvalidInputException {
		// In this example application, we assumed that Users and Postings are
		// identified by their names
		User p = service.getUserByName(pDto.getName());
		Posting e = service.getPostingByName(eDto.getName());
		Registration r = service.register(p, e);
		return convertToDto(r, p, e);
	}

	@GetMapping(value = { "/registrations", "/registrations/" })
	public List<RegistrationDto> getRegistrations() throws InvalidInputException {
		List<Registration> registrations = service.getRegistrations();

		return convertToRegistrationDtoList(registrations);
	}

	@GetMapping(value = { "/registrations/User/{name}", "/registrations/User/{name}/" })
	public List<PostingDto> getPostingsOfUser(@PathVariable("name") UserDto pDto) {
		User p = convertToDomainObject(pDto);
		return createPostingDtosForUser(p);
	}

	@GetMapping(value = { "/registrations/Posting/{name}", "/registrations/Posting/{name}/" })
	public List<UserDto> getUsersOfPosting(@PathVariable("name") PostingDto eDto) {
		Posting e = convertToDomainObject(eDto);
		return createUserDtosForPosting(e);
	}

	@PostMapping(value = { "/clear", "/clear/" })
	public void clearAll() {
		service.clearAll();
	}

	@DeleteMapping(value = { "/registrations", "/registrations/" })
	public List<RegistrationDto> removeRegistration(@RequestParam(name = "User") UserDto pDto,
			@RequestParam(name = "Posting") PostingDto eDto) {
		User p = service.getUserByName(pDto.getName());
		Posting e = service.getPostingByName(eDto.getName());
		List<Registration> registrations = service.removeRegistration(p, e);

		return convertToRegistrationDtoList(registrations);
	}

	// Conversion methods (not part of the API)

	private PostingDto convertToDto(Posting p) {
		// In simple cases, the mapper service is convenient
		return modelMapper.map(p, PostingDto.class);
	}

	private UserDto convertToDto(User u) {
		UserDto UserDto = modelMapper.map(u, UserDto.class);
		UserDto.setPostings(createPostingDtosForUser(u));
		return UserDto;
	}

	private RegistrationDto convertToDto(Registration r, User u, Posting p) {
		PostingDto eDto = convertToDto(p);
		UserDto pDto = convertToDto(u);
		return new RegistrationDto(pDto, eDto);
	}

	private List<UserDto> convertToUserDtoList(List<User> users) {
		List<UserDto> UserDtos = Lists.newArrayList();

		for (User p : users) {
			UserDtos.add(convertToDto(p));
		}
		return UserDtos;
	}

	private List<PostingDto> convertToPostingDtoList(List<Posting> postings) {
		List<PostingDto> PostingDtos = Lists.newArrayList();

		for (Posting e : postings) {
			PostingDtos.add(convertToDto(e));
		}
		return PostingDtos;
	}

	private List<RegistrationDto> convertToRegistrationDtoList(List<Registration> registrations) {
		List<RegistrationDto> registrationDtos = Lists.newArrayList();

		for (Registration r : registrations) {
			registrationDtos.add(convertToDto(r, r.getUser(), r.getPosting()));
		}
		return registrationDtos;
	}

	private User convertToDomainObject(UserDto pDto) {
		// Mapping DTO to the domain object without using the mapper
		List<User> allUsers = service.getUsers();
		for (User User : allUsers) {
			if (User.getName().equals(pDto.getName())) {
				return User;
			}
		}
		return null;
	}

	private Posting convertToDomainObject(PostingDto eDto) {
		// Mapping DTO to the domain object without using the mapper
		List<Posting> allPostings = service.getPostings();
		for (Posting Posting : allPostings) {
			if (Posting.getName().equals(eDto.getName())) {
				return Posting;
			}
		}
		return null;
	}
	
	private List<PostingDto> createPostingDtosForUser(User u) {
		List<Posting> PostingsForUser = service.getPostingsForUser(u);
		List<PostingDto> Postings = Lists.newArrayList();
		for (Posting Posting : PostingsForUser) {
			Postings.add(convertToDto(Posting));
		}
		return Postings;
	}

	private List<UserDto> createUserDtosForPosting(Posting p) {
		List<User> UsersForPosting = service.getUsersForPosting(p);
		List<UserDto> Users = Lists.newArrayList();
		for (User User : UsersForPosting) {
			Users.add(convertToDto(User));
		}
		return Users;
	}

}
