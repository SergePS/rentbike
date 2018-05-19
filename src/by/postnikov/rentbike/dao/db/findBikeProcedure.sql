DELIMITER //
CREATE PROCEDURE findBikeProcedure(bikeBrandId LONG, bikeModel varchar(40), typeId LONG, speedCountMin INT, speedCountMax INT, startId LONG, lim INT)
BEGIN
	SELECT 
		bk.id, 
        bk.brandId, 
        br.brand, 
        bk.model, 
        bk.wheelSize, 
        bk.speedCount, 
        bk.picture, 
        bk.biketypeId, 
        bt.type 
        FROM bikes bk 
        LEFT JOIN brands br ON bk.brandId = br.id  
        LEFT JOIN biketype bt ON bk.bikeTypeId = bt.id 
        WHERE
		bk.id > startId
        AND((bikeBrandId = 0) or (bk.brandId = bikeBrandId))
        AND ((bikeModel is null) or (bk.model LIKE CONCAT('%', bikeModel, '%')))
        AND((typeId = 0) or (bk.biketypeId = typeId))
        AND(bk.speedCount BETWEEN speedCountMin AND (IF(speedCountMax=0, 1000, speedCountMax)))
        ORDER BY bk.id
        LIMIT lim
    ;
END;
//