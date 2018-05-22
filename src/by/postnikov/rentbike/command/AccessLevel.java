package by.postnikov.rentbike.command;

/**
 * @author Sergey Postnikov
 * Type of user role.
 *
 */
public enum AccessLevel {
	
	USER(1), ADMIN(2);
	
	private int level;
	
	private AccessLevel(int level) {
		this.level = level;
	}

	/**
	 * @return access level of the role.
	 */
	public int level() {
		return level;
	}

}
