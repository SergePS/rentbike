package by.postnikov.rentbike.command.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.Command;
import by.postnikov.rentbike.command.PageConstant;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.command.SessionParameter;
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.entity.PageInfo;

public class PagingCommand implements Command {
	private static Logger logger = LogManager.getLogger();

	private final static int NEXT_PAGE = 1;
	private final static int PREV_PAGE = -1;

	@Override
	public Router execute(HttpServletRequest request) {

		Router router = new Router();
		// router.setRoute(RouteType.REDIRECT);

		HttpSession session = request.getSession(false);
		PageInfo pageInfo = (PageInfo) session.getAttribute(SessionParameter.PAGE_INFO.parameter());
		if (pageInfo == null) {
			logger.log(Level.ERROR, "pageInfo object not found");
			router.setPagePath(PageConstant.ERROR_PAGE);
		} else {
			pageInfo.setChangePageFlag(true);

			String pageAction = request.getParameter(RequestParameter.PAGE_ACTION.parameter());
			router.setPagePath(pageInfo.getPreviousUrlWithParam());

			if (RequestParameter.PREVIOUS_PAGE.parameter().equals(pageAction)) {
				if (pageInfo.getCurrentPage() > 1) {
					pageInfo.removeLastPagePoint();		//removing 2 line because user change direction of paging
					pageInfo.removeLastPagePoint();
					pageInfo.setPageAction(PREV_PAGE);
				} else {		// protection against F5
					pageInfo.removeLastPagePoint();
					pageInfo.setPageAction(PREV_PAGE);
				}
			}

			if (RequestParameter.NEXT_PAGE.parameter().equals(pageAction)) {
				if (!pageInfo.isLastPage()) {
					pageInfo.setPageAction(NEXT_PAGE);
				}
			}
		}

		return router;
	}

}
