package by.postnikov.rentbike.entity;

import by.postnikov.rentbike.command.AccessLevel;
import by.postnikov.rentbike.command.PageConstant;

public enum UserRole {

	ADMIN(PageConstant.ADMIN_PAGE, AccessLevel.ADMIN), 
	USER(PageConstant.USER_PAGE, AccessLevel.USER);

	private String homePage;
	private AccessLevel accessLevel;

	UserRole(String homePage, AccessLevel accessLevel) {
		this.homePage = homePage;
		this.accessLevel = accessLevel;
	}

	public String getHomePage() {
		return homePage;
	}

	public AccessLevel getAccessLevel() {
		return accessLevel;
	}

}
