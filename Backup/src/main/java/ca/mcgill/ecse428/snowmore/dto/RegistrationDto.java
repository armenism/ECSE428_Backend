package ca.mcgill.ecse428.snowmore.dto;

public class RegistrationDto {

	private UserDto user;
	private PostingDto participant;

	public RegistrationDto() {
	}

	public RegistrationDto(UserDto participant, PostingDto event) {
		this.user = participant;
		this.participant = event;
	}

	public UserDto getParticipant() {
		return user;
	}

	public void setParticipant(UserDto participant) {
		this.user = participant;
	}

	public PostingDto getEvent() {
		return participant;
	}

	public void setEvent(PostingDto event) {
		this.participant = event;
	}
}
