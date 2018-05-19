package by.postnikov.rentbike.command.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.Command;
import by.postnikov.rentbike.command.PageConstant;
import by.postnikov.rentbike.command.PageInfo;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.command.SessionParameter;
import by.postnikov.rentbike.controller.RouteType;
import by.postnikov.rentbike.controller.Router;

public class PagingCommand implements Command{
	private static Logger logger = LogManager.getLogger();
	
	private final static String PREVIOUS_PAGE = "previous";

	@Override
	public Router execute(HttpServletRequest request) {
		
		Router router = new Router();
		router.setRoute(RouteType.REDIRECT);
		
		HttpSession session = request.getSession(false);
		PageInfo pageInfo = (PageInfo) session.getAttribute(SessionParameter.PAGE_INFO.parameter());
		if (pageInfo == null) {
			logger.log(Level.ERROR, "pageInfo object not found");
			router.setPagePath(PageConstant.ERROR_PAGE);			
		} else {
			pageInfo.setChangePageFlag(true);
			
			String pageAction = request.getParameter(RequestParameter.PAGE_ACTION.parameter());
			router.setPagePath(pageInfo.getPreviousUrlWithParam());
			
			if(PREVIOUS_PAGE.equals(pageAction)) {
				pageInfo.removeLastPagePoint();
				pageInfo.removeLastPagePoint();
				pageInfo.setCurrentPage(pageInfo.getCurrentPage() - 1);
			}else {
				pageInfo.setCurrentPage(pageInfo.getCurrentPage() + 1);
			}
		}

		return router;
	}

}
