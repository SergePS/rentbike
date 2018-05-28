package by.postnikov.rentbike.entity;

import java.util.ArrayDeque;
import java.util.Deque;

import by.postnikov.rentbike.command.ApplicationProperty;

/**
 * @author Sergey Postnikov
 * Pagination parameters
 */
public class PageInfo {

	private final static long ITEM_LAST_ID_FOR_FIRST_PAGE = 0;
	private final static String DEFAULT_ELEMENT_ON_PAGE_KEY = "default_element_on_page";
	private final static int DEFAULT_ELEMENTS_COUNT_ON_PAGE = Integer.parseInt(ApplicationProperty.takeProperty().getProperty(DEFAULT_ELEMENT_ON_PAGE_KEY));

	/** Default element count on the page. */
	private int defaultElementOnPage;
	
	/** storage for id of the last element on the page.
	 *  Used to set the first id of the range of the SQL SELECT query*/
	private Deque<Long> pagePoint;
	
	/** Last page flag. */
	private boolean lastPage;
	
	/** If emptyList flag true, then pagination menu on JSP is not available. */
	private boolean emptyList;
	
	/** request to previous page with searching parameters*/
	private String previousUrlWithParam;
	
	/** if changePageFlag if true, pageInfo is not initialized when redirect by a previous request  */
	private boolean changePageFlag;
	
	/** -1 - previously page, +1 - next page */
	private int pageAction;
	
	private int currentPage;

	public PageInfo() {
		this.pagePoint = new ArrayDeque<>();
		this.defaultElementOnPage = DEFAULT_ELEMENTS_COUNT_ON_PAGE;
		this.pagePoint.addLast(ITEM_LAST_ID_FOR_FIRST_PAGE);
		this.changePageFlag = false;
		this.lastPage = false;
		this.emptyList = true;
		this.currentPage = 1;
	}

	public int getDefaultElementOnPage() {
		return defaultElementOnPage;
	}

	public void setDefaultElementOnPage(int defaultElementOnPage) {
		this.defaultElementOnPage = defaultElementOnPage;
	}

	public String getPreviousUrlWithParam() {
		return previousUrlWithParam;
	}

	public void setPreviousUrlWithParam(String previousUrlWithParam) {
		this.previousUrlWithParam = previousUrlWithParam;
	}

	public boolean isChangePageFlag() {
		return changePageFlag;
	}

	public void setChangePageFlag(boolean changePageFlag) {
		this.changePageFlag = changePageFlag;
	}

	public void addPagePoint(Long itemId) {
		this.pagePoint.addLast(itemId);
	}

	public long removeLastPagePoint() {
		return this.pagePoint.removeLast();
	}

	public long getLastPagePoint() {
		return this.pagePoint.getLast();
	}

	public Deque<Long> getPagePoint() {
		return pagePoint;
	}

	public void setPagePoint(Deque<Long> pagePoint) {
		this.pagePoint = pagePoint;
	}

	public boolean isLastPage() {
		return lastPage;
	}

	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}

	public boolean isEmptyList() {
		return emptyList;
	}

	public void setEmptyList(boolean emptyList) {
		this.emptyList = emptyList;
	}


	public int getPageAction() {
		return pageAction;
	}

	public void setPageAction(int pageAction) {
		this.pageAction = pageAction;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	
	
}
