package by.postnikov.rentbike.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import by.postnikov.rentbike.command.util.RequestParameterHandler;
import by.postnikov.rentbike.entity.AbstractEntity;

public class PageInfoHandler {

	public static PageInfo pageInfoInit(HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		PageInfo pageInfo = (PageInfo) session.getAttribute(SessionParameter.PAGE_INFO.parameter());
		if (pageInfo == null || !pageInfo.isChangePageFlag()) {
			pageInfo = new PageInfo();
		} else {
			pageInfo.setChangePageFlag(false);
		}
		pageInfo.setPreviousUrlWithParam(RequestParameterHandler.paramToString(request));

		return pageInfo;
	}

	public static void handleAndAddToSession(PageInfo pageInfo, HttpServletRequest request,
			List<? extends AbstractEntity> itemList) {

		HttpSession session = request.getSession(false);

		if (itemList.isEmpty()) {
			pageInfo.setLastPage(true);
			pageInfo.addPagePoint(pageInfo.getLastPagePoint());

			if (pageInfo.getCurrentPage() == 1) {
				pageInfo.setEmptyList(true);
			}

		} else {
			long lastBikeId = itemList.get(itemList.size() - 1).getId();
			pageInfo.addPagePoint(lastBikeId);
			pageInfo.setEmptyList(false);
			if (pageInfo.getDefaultElementOnPage() > itemList.size()) {
				pageInfo.setLastPage(true);
			} else {
				pageInfo.setLastPage(false);
			}
		}

		session.setAttribute(SessionParameter.PAGE_INFO.parameter(), pageInfo);

	}

}
