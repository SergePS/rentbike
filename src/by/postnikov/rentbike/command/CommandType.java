package by.postnikov.rentbike.command;

import by.postnikov.rentbike.command.impl.AddBikeCommand;
import by.postnikov.rentbike.command.impl.AddBikeProductCommand;
import by.postnikov.rentbike.command.impl.AddBikeTypeCommand;
import by.postnikov.rentbike.command.impl.AddBrandCommand;
import by.postnikov.rentbike.command.impl.AddParkingCommand;
import by.postnikov.rentbike.command.impl.ChangeLocalizationCommand;
import by.postnikov.rentbike.command.impl.ChooseBikeCommand;
import by.postnikov.rentbike.command.impl.ChooseBikeProductCommand;
import by.postnikov.rentbike.command.impl.CloseOrderCommand;
import by.postnikov.rentbike.command.impl.CreateOrderCommand;
import by.postnikov.rentbike.command.impl.FindBikeCommand;
import by.postnikov.rentbike.command.impl.FindBikeProductCommand;
import by.postnikov.rentbike.command.impl.GoToAddBikePageCommand;
import by.postnikov.rentbike.command.impl.GoToAddParkingPageCommand;
import by.postnikov.rentbike.command.impl.GoToBikeCatalogPageCommand;
import by.postnikov.rentbike.command.impl.GoToBikeProductCatalogPageCommand;
import by.postnikov.rentbike.command.impl.GoToBikePurchasePageCommand;
import by.postnikov.rentbike.command.impl.GoToHomePageCommand;
import by.postnikov.rentbike.command.impl.GoToOrderPageCommand;
import by.postnikov.rentbike.command.impl.GoToParkingPageCommand;
import by.postnikov.rentbike.command.impl.LoginCommand;
import by.postnikov.rentbike.command.impl.LogoutCommand;
import by.postnikov.rentbike.command.impl.PagingCommand;
import by.postnikov.rentbike.command.impl.TakeAllUserCommand;
import by.postnikov.rentbike.command.impl.UpdateBikeCommand;
import by.postnikov.rentbike.command.impl.UpdateParkingCommand;
import by.postnikov.rentbike.command.impl.UpdatePasswordCommand;
import by.postnikov.rentbike.command.impl.UpdateUserCommand;
import by.postnikov.rentbike.command.impl.RegisterUserCommand;
import by.postnikov.rentbike.command.impl.FindUserOrdersCommand;
import by.postnikov.rentbike.command.impl.GoToOrderReportPage;
import by.postnikov.rentbike.command.impl.FindOrderByParametersCommand;

/**
 * @author Sergey Postnikov
 *
 */
/**
 * @author SergeWork
 *
 */
public enum CommandType {
	
	LOGIN(new LoginCommand(), AccessLevel.USER),
	LOGOUT(new LogoutCommand(), AccessLevel.USER), 
	REGISTER(new RegisterUserCommand(), AccessLevel.USER),
	HOME(new GoToHomePageCommand(), AccessLevel.USER),
	TAKE_ALL_USER(new TakeAllUserCommand(), AccessLevel.ADMIN),
	UPDATE_USER(new UpdateUserCommand(), AccessLevel.USER),
	UPDATE_PASSWORD(new UpdatePasswordCommand(), AccessLevel.USER),
	
	CHANGE_LOCALIZATION(new ChangeLocalizationCommand(), AccessLevel.USER),
	
	ADD_BIKE(new AddBikeCommand(), AccessLevel.ADMIN),
	ADD_BRAND(new AddBrandCommand(), AccessLevel.ADMIN),
	ADD_BIKE_TYPE(new AddBikeTypeCommand(), AccessLevel.ADMIN),
	BIKECATALOG(new GoToBikeCatalogPageCommand(), AccessLevel.USER),
	FIND_BIKE(new FindBikeCommand(), AccessLevel.USER),
	GO_TO_ADD_BIKE_PAGE(new GoToAddBikePageCommand(), AccessLevel.ADMIN),
	GO_TO_BIKE_PURCHASE(new GoToBikePurchasePageCommand(), AccessLevel.ADMIN),
	UPDATE_BIKE(new UpdateBikeCommand(), AccessLevel.ADMIN),
	CHOOSE_BIKE(new ChooseBikeCommand(), AccessLevel.USER),
	ADD_BIKE_PRODUCT(new AddBikeProductCommand(), AccessLevel.ADMIN),

	ADD_PARKING(new AddParkingCommand(), AccessLevel.ADMIN),
	GO_TO_PARKING_PAGE(new GoToParkingPageCommand(), AccessLevel.ADMIN),
	GO_TO_ADD_PARKING_PAGE(new GoToAddParkingPageCommand(), AccessLevel.ADMIN),
	UPDATE_PARKING(new UpdateParkingCommand(), AccessLevel.ADMIN),
	
	GO_TO_BIKE_PRODUCT_CATALOG_PAGE(new GoToBikeProductCatalogPageCommand(), AccessLevel.USER),
	FIND_BIKE_PRODUCT(new FindBikeProductCommand(), AccessLevel.USER),
	CHOOSE_BIKE_PRODUCT(new ChooseBikeProductCommand(), AccessLevel.USER),

	GO_TO_ORDER_PAGE(new GoToOrderPageCommand(), AccessLevel.USER),
	CREATE_ORDER(new CreateOrderCommand(), AccessLevel.USER),
	CLOSE_ORDER(new CloseOrderCommand(), AccessLevel.USER),
	FIND_USER_ORDERS(new FindUserOrdersCommand(), AccessLevel.USER),
	GO_TO_ORDER_REPORT_PAGE(new GoToOrderReportPage(), AccessLevel.ADMIN),
	FIND_ORDER_BY_PARAMETERS(new FindOrderByParametersCommand(), AccessLevel.ADMIN),
	
	PAGINATION(new PagingCommand(), AccessLevel.USER);
	
	
	/** Command object */
	private Command command;
	
	/** Access level for page */
	private AccessLevel accessLevel;
	
	private CommandType(Command command, AccessLevel accessLevel){
		this.command = command;
		this.accessLevel = accessLevel;
	}
	
	public Command getCommand() {
		return command;
	}

	public AccessLevel getAccessLevel() {
		return accessLevel;
	}

}
