package by.postnikov.rentbike.command;

public enum PageMessage {
	
	//error
	BRAND_DUBLICATE_ERROR("brandDublicateError"),
	BIKE_TYPE_DUBLICATE_ERROR("bikeTypeDublicateError"),
	
	CURRENT_PASSW_WRONG("currentPasswordWrong"),
		
	LOGIN_PASSW("loginOrPasswWrong"),
	
	ORDER_NOT_EXIST("orderNotExist"),
	
	PARKING_DUBLICATE_ERROR("parkingDublicateError"),
	
	USER_DUBLICATE_ERROR("userDublicateError"),
	USER_IS_TOO_YOUNG("userTooYoung"),
	
	VALIDATION_ERROR("validationError"),
	
	
	//notice
	PARKING_ADDED("parkingAdded"),
	PARKING_CHANGED("parkingChanged"),
	
	BIKE_ADDED("bikeAdded"),
	BIKE_CHANGED("bikeChanged"),
	
	PROFILE_CHANGED("profileChanged"),
	PASSWORD_CHANGED("passwordChanged");

	private String message;

	private PageMessage(String message) {
		this.message = message;
	}

	public String message() {
		return message;
	}

}
