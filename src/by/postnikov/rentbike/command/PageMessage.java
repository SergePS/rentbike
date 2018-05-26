package by.postnikov.rentbike.command;

public enum PageMessage {

	//notice
	PARKING_ADDED("parkingAdded"),
	PARKING_CHANGED("parkingChanged"),
	
	BIKE_ADDED("bikeAdded"),
	BIKE_CHANGED("bikeChanged"),
	
	PROFILE_CHANGED("profileChanged"),
	PASSWORD_CHANGED("passwordChanged"),
	
	USER_ADDED("userAdded");

	private String message;

	private PageMessage(String message) {
		this.message = message;
	}

	public String message() {
		return message;
	}

}
