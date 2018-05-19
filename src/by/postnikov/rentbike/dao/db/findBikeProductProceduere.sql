DELIMITER //
CREATE PROCEDURE findBikeProductProcedure(productState varchar(40), productParkingId LONG, bikeBrandId LONG, typeId LONG, bikeModel varchar(40), startId LONG, lim INT)
BEGIN
	SELECT 
		bp.id, 
        bk.id AS bikeId, 
        bk.brandId, 
        br.brand, 
        bk.model, 
        bk.wheelSize, 
        bk.speedCount, 
        bk.picture, 
        bk.bikeTypeId, 
        bt.type, 
        pk.id AS parkingId, 
        pk.address, 
        bp.value, 
        bp.rentPrice 
        FROM bikeproduct bp 
        LEFT JOIN bikes bk ON bp.bikeId=bk.id 
        LEFT JOIN brands br ON bk.brandId = br.id 
        LEFT JOIN biketype bt ON bk.bikeTypeId = bt.id 
        LEFT JOIN parkings pk ON bp.parkingId=pk.id 
        WHERE bp.state = productState
        AND bp.id > startId
        AND((productParkingId = 0) or (parkingId = productParkingId))
		AND((bikeBrandId = 0) or (bk.brandId = bikeBrandId))
        AND((typeId = 0) or (bk.biketypeId = typeId))
        AND ((bikeModel is null) or (bk.model LIKE CONCAT('%', bikeModel, '%')))
        ORDER BY bp.id
        LIMIT lim
    ;
END;
//