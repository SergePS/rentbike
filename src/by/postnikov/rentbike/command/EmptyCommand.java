package by.postnikov.rentbike.command;

import javax.servlet.http.HttpServletRequest;

import by.postnikov.rentbike.controller.Router;

public class EmptyCommand implements Command{

	@Override
	public Router execute(HttpServletRequest request) {
		
		Router router = new Router();
		router.setPagePath(PageConstant.ERROR_PAGE);
		
		return router;
	}

}
