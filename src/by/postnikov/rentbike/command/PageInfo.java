package by.postnikov.rentbike.command;

import java.util.ArrayDeque;
import java.util.Deque;

public class PageInfo {
	
	private final static int FIRST_PAGE = 1;
	private final static long ITEM_LAST_ID_FOR_FIRST_PAGE = 0;
	private final static String DEFAULT_ELEMENT_ON_PAGE_KEY = "default_element_on_page";
	private final static int DEFAULT_ELEMENTS_COUNT_ON_PAGE = Integer.parseInt(ApplicationProperty.takeProperty().getProperty(DEFAULT_ELEMENT_ON_PAGE_KEY));

	private int defaultElementOnPage;
	private int currentPage;
	private boolean lastPage;
	private boolean emptyList;
	private String previousUrlWithParam;
	private boolean changePageFlag;
	private Deque<Long> pagePoint;

	public PageInfo() {
		this.pagePoint = new ArrayDeque<>();
		this.defaultElementOnPage = DEFAULT_ELEMENTS_COUNT_ON_PAGE;
		this.pagePoint.addLast(ITEM_LAST_ID_FOR_FIRST_PAGE);
		this.currentPage = FIRST_PAGE;
		this.changePageFlag = false;
		this.lastPage = true;
		this.emptyList = true;
	}

	public int getDefaultElementOnPage() {
		return defaultElementOnPage;
	}

	public void setDefaultElementOnPage(int defaultElementOnPage) {
		this.defaultElementOnPage = defaultElementOnPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (changePageFlag ? 1231 : 1237);
		result = prime * result + currentPage;
		result = prime * result + defaultElementOnPage;
		result = prime * result + (lastPage ? 1231 : 1237);
		result = prime * result + ((pagePoint == null) ? 0 : pagePoint.hashCode());
		result = prime * result + ((previousUrlWithParam == null) ? 0 : previousUrlWithParam.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PageInfo other = (PageInfo) obj;
		if (changePageFlag != other.changePageFlag)
			return false;
		if (currentPage != other.currentPage)
			return false;
		if (defaultElementOnPage != other.defaultElementOnPage)
			return false;
		if (lastPage != other.lastPage)
			return false;
		if (pagePoint == null) {
			if (other.pagePoint != null)
				return false;
		} else if (!pagePoint.equals(other.pagePoint))
			return false;
		if (previousUrlWithParam == null) {
			if (other.previousUrlWithParam != null)
				return false;
		} else if (!previousUrlWithParam.equals(other.previousUrlWithParam))
			return false;
		return true;
	}

}
