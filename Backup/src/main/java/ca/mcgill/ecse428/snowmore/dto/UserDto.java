package ca.mcgill.ecse428.snowmore.dto;

import java.util.ArrayList;
import java.util.List;

public class UserDto {

	private String name;
	private List<PostingDto> events;

	public UserDto() {
	}

	public UserDto(String name) {
		this(name, new ArrayList<PostingDto>());
	}

	public UserDto(String name, ArrayList<PostingDto> arrayList) {
		this.name = name;
		this.events = arrayList;
	}

	public String getName() {
		return name;
	}

	public List<PostingDto> getEvents() {
		return events;
	}

	public void setEvents(List<PostingDto> events) {
		this.events = events;
	}

	public void setPostings(List<PostingDto> createPostingDtosForUser) {
		// TODO Auto-generated method stub
		
	}
}
