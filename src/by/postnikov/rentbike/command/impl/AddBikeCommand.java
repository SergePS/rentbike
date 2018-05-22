package by.postnikov.rentbike.command.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.ApplicationProperty;
import by.postnikov.rentbike.command.Command;
import by.postnikov.rentbike.command.MessagePage;
import by.postnikov.rentbike.command.PageConstant;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.command.util.RequestParameterHandler;
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.entity.Bike;
import by.postnikov.rentbike.entity.BikeType;
import by.postnikov.rentbike.entity.Brand;
import by.postnikov.rentbike.exception.ConvertPrintStackTraceToString;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.BikeService;
import by.postnikov.rentbike.service.ServiceFactory;

public class AddBikeCommand implements Command {

	private static Logger logger = LogManager.getLogger();
	
	private static final String UPLOAD_DIR_PROP_KEY = "upload_dir";
	private static final String UPLOAD_DIR = ApplicationProperty.takeProperty().getProperty(UPLOAD_DIR_PROP_KEY);
	
	private final static String DOT_SEPARATOR = "\\.";
	
	@Override
	public Router execute(HttpServletRequest request) {

		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		BikeService bikeService = serviceFactory.getBikeService();
		
		Router router = new Router();
		
		Map<String, String> requestParameters = RequestParameterHandler.requestParamToMap(request);
		
		//create picture's file name
		String appPath = request.getServletContext().getRealPath("");
		String savePath = appPath + File.separator + UPLOAD_DIR;
		
		Part filePart = null;
		String filePath = "";
		String filePictureName = "";
		
		try {
			for (Part part : request.getParts()) {
				String fileName = extractFileName(part);
				fileName = new File(fileName).getName();
	
				if (!fileName.isEmpty()) {
					String 	fileExtension = fileName.split(DOT_SEPARATOR)[1];
					fileExtension = "." + fileExtension;
					filePictureName = File.createTempFile("bike", fileExtension, null).getName();
					requestParameters.put(RequestParameter.PICTURE.parameter(), filePictureName);
					filePath = savePath + File.separator + filePictureName;
					filePart = part;
				}
			}
		} catch (IOException | ServletException e) {
			logger.log(Level.ERROR, "Upload picture error, " + ConvertPrintStackTraceToString.convert(e));
		}
		
		//add bike
		Bike bike = new Bike();
		try {
			String errorMessage = bikeService.addBike(requestParameters, bike);
			
			List<BikeType> bikeTypeList = bikeService.takeAllBikeType();
			request.setAttribute(RequestParameter.BIKE_TYPE_LIST.parameter(), bikeTypeList);
			
			List<Brand> brandList = bikeService.takeAllBrand();
			request.setAttribute(RequestParameter.BRAND_LIST.parameter(), brandList);
			
			router.setPagePath(PageConstant.ADD_BIKE_PAGE);
			
			if(errorMessage.isEmpty() && filePart!=null) {
				filePart.write(filePath);
				request.setAttribute(RequestParameter.MESSAGE.parameter(), MessagePage.BIKE_ADDED.message());
			}else {
				request.setAttribute(RequestParameter.ERROR.parameter(), errorMessage);
			}
			
		} catch (ServiceException e) {
			logger.log(Level.ERROR, "Add bike exception, " + ConvertPrintStackTraceToString.convert(e));
			router.setPagePath(PageConstant.ERROR_PAGE);
		} catch (IOException e) {
			logger.log(Level.ERROR, "Write picture error, " + ConvertPrintStackTraceToString.convert(e));
			router.setPagePath(PageConstant.ADD_BIKE_PAGE);
		}
				
		request.setAttribute(RequestParameter.BIKE.parameter(), bike);

		return router;
	}
	
	private String extractFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length() - 1);
			}
		}
		return "";

	}

}
