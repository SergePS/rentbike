package by.postnikov.rentbike.command;

public enum AccessLevel {
	
	USER(1), ADMIN(2);
	
	private int level;
	
	private AccessLevel(int level) {
		this.level = level;
	}

	public int level() {
		return level;
	}

}
