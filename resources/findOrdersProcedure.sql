DELIMITER |	
CREATE PROCEDURE findOrdersProcedure(surname varchar(40), dateFrom varchar(40), dateTo varchar(40), orderId long, lim int)
BEGIN
	SELECT 
		o.id, 
        u.id as userId, 
        u.name, 
        u.surname, 
        DATE_FORMAT(o.startTime, '%d.%m.%Y %H:%i:%s') as startTime, 
        DATE_FORMAT(o.finishTime, '%d.%m.%Y %H:%i:%s') as finishTime, 
        sp.id as startParkingId, 
        sp.address as startParking, 
        fp.id as finishParkingId, 
        fp.address as finishParking, 
        o.rentPrice, 
        o.payment 
        FROM orders o 
        LEFT JOIN users u ON o.userId = u.id 
        LEFT JOIN parkings sp ON o.startParkingId = sp.id 
        LEFT JOIN parkings fp ON o.finishParkingId = fp.id 
        WHERE
			o.id > orderId
			AND ((surname is null) or (u.surname LIKE CONCAT('%', surname, '%')))
            AND o.startTime between dateFrom AND if(dateTo = "", now(), dateTo)
		LIMIT lim
    ;
END;
|
DELIMITER ;