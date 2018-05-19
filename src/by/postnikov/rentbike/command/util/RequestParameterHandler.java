package by.postnikov.rentbike.command.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import by.postnikov.rentbike.command.RequestParameter;

public class RequestParameterHandler {

	public static Map<String, String> requestParamToMap(HttpServletRequest request) {

		Map<String, String[]> parametrs = request.getParameterMap();

		Map<String, String> requestParameters = new HashMap<>();

		Set<Map.Entry<String, String[]>> set = parametrs.entrySet();

		for (Map.Entry<String, String[]> mapEntry : set) {

			if (!RequestParameter.PASSWORD.parameter().equals(mapEntry.getKey())) {
				String tempValue = "";
				for (String item : mapEntry.getValue()) {
					tempValue = tempValue + item;
				}
				requestParameters.put(mapEntry.getKey(), tempValue);
			}

		}

		return requestParameters;

	}

	public static String paramToString(HttpServletRequest request) {

		String urlWithParam = request.getServletPath();

		Map<String, String[]> parametrs = request.getParameterMap();
		Set<Map.Entry<String, String[]>> set = parametrs.entrySet();

		int countParams = 0;
		for (Map.Entry<String, String[]> i : set) {

			String tempValue = "";
			for (String k : i.getValue()) {
				tempValue = tempValue + k;
			}

			if (countParams == 0) {
				urlWithParam = urlWithParam + "?" + i.getKey() + "=" + tempValue;
				countParams++;
			} else {
				urlWithParam = urlWithParam + "&" + i.getKey() + "=" + tempValue;
			}
		}
		return urlWithParam;
	}

}
