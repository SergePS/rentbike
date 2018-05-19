package by.postnikov.rentbike.command;

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
	
	public String parameter() {
		return parameter;
	}
	
}