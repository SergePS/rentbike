package by.postnikov.rentbike.command.impl;

import javax.servlet.http.HttpServletRequest;

import by.postnikov.rentbike.command.Command;
import by.postnikov.rentbike.command.PageConstant;
import by.postnikov.rentbike.controller.Router;

public class GoToOrderReportPage implements Command{

	@Override
	public Router execute(HttpServletRequest request) {
		
		Router router = new Router();
		router.setPagePath(PageConstant.ORDER_REPORT);

		return router;
	}

}
