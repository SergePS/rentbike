package by.postnikov.rentbike.command;

/**
 * @author Sergey Postnikov
 * 
 * Parameters received and sent to the session.
 */
public enum SessionParameter {

	BIKE_PRODUCT("bikeProduct"), 
	LANGUAGE("lang"), 
	LOCAL("local"), 
	USER("user"), 
	PAGE_INFO("pageInfo"), 
	BIKE_CATALOG_WITH_CHOICE("bikeCatalogWithChoise");

	private String parameter;

	private SessionParameter(String paramName) {
		this.parameter = paramName;
	}

	/**
	 * @return String representation of the parameter.
	 */
	public String parameter() {
		return parameter;
	}

}
