<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<style>
		<%@include file="/css/bootstrap.min.css"%>
		<%@include file="/css/commonStyles.css"%>
		<%@include file="/css/header.css"%>		
		<%@include file="/css/calendar.css"%>
	</style>
	
	<%@ include file="../../WEB-INF/jspf/localizationVar.jspf" %>

	<title><c:out value="${title}"></c:out></title>

</head>
<body>
	
	<c:set var="menuLabel" value="${ordersLabel}" scope="page"/>
	<%@ include file="../../WEB-INF/jspf/smallMenu.jspf" %>
	
	<div style="padding-left: 15px; padding-right: 15px">
		<div class="row">
			<div class="col-md-2" style="background: #F0FFF0; padding-left: 10px">
				<br>
				<form id="calend" action="FrontController" method="post">
					
					<!--  ------------------------------- surname input ------------------------------------------ -->
					<div>
						<label style="margin-left:10px"><c:out value="${surnameLabel}">:</c:out></label>
						<input 	type="text"	
							name="surname"
							class="form-control"
							value="${surname}"
							placeholder="${surnamePlaceholder}"
							pattern="[a-zA-Zа-яА-ЯЁё]{3,15}"
							oninvalid="setCustomValidity('${surnameWarnLabel}')"
							oninput="setCustomValidity('')">					
					</div>
					<br>
					
					<!--  ------------------------------- from date --------------------------------------- -->
					<div>
						<label style="margin-left:10px"><c:out value="с даты">:</c:out></label>
						<input 	type="text"
								id="fromDate"
								name="fromDate" 
								class="form-control"
								style="background-color: white" 
								value="${fromDate}"
								placeholder="${birthdayPlaceholder}"
								required
								readonly
								autocomplete="off">				
					</div>
					<br>
					
					<!--  -------------------------------- to date  --------------------------------------- -->
					<div>
						<label style="margin-left:10px"><c:out value="до даты">:</c:out></label>
						<input 	type="text"
								id="toDate"
								name="toDate" 
								class="form-control"
								style="background-color: white" 
								value="${toDate}"
								placeholder="${birthdayPlaceholder}"
								required
								readonly
								autocomplete="off">				
					</div>
	
					<br>
					<br>
				
					<div style="text-align: center">
						<input type="hidden" name="command" value="find_bike" />	
						<input type="submit" class="btn btn-primary"   value="${findLabel}">		
					</div>
				</form>
			</div>
			
			<!--  --------------------------------- founded orders ------------------------------------------ -->		
			<div class="col-md-9">	
				<c:forEach items="${bikeList}" var="item">
					<table class="table">
						<thead>
							<tr>
								<th><c:out value="${orderNumberLabel}"></c:out></th>
								<th style="width: 200px"><c:out value="${bikeLabel}"></c:out></th>
								<th style="width: 300px"><c:out value="${startParkingLabel}"></c:out></th>
								<th style="width: 300px"><c:out value="${finishParkingLabel}"></c:out></th>								
								<th><c:out value="${startTimeLabel}"></c:out></th>
								<th><c:out value="${finishTimeLabel}"></c:out></th>
								<th style="width: 150px"><c:out value="${rentCostPerMinuteLabel}"></c:out></th>
								<th><c:out value="${minutesLabel}"></c:out></th>
								<th><c:out value="${amountLabel}"></c:out></th>
							</tr>
						</thead>
						<c:forEach items="${bikeOrderList}" var="item">
							<tbody>
								<tr>
									<td><c:out value="${item.id}"></c:out></td>	
									<td><c:out value="${item.bike.brand.brand}"></c:out>, <c:out value="${item.bike.model}"></c:out></td>
									<td><c:out value="${item.startParking.address}"></c:out></td>
									<td><c:out value="${item.finishParking.address}"></c:out></td>									
									<td><c:out value="${item.startTime}"></c:out></td>
									<td><c:out value="${item.finishTime}"></c:out></td>
									<td><c:out value="${item.rentPrice}"></c:out></td>
									<td></td>
									<td><c:out value="${item.payment}"></c:out></td>												
								</tr>									
							</tbody>
						</c:forEach>		
					</table>
				</c:forEach>
				
				<%@ include file="../../WEB-INF/jspf/pagination.jspf" %>
				
			</div>
		</div>
	</div>
		
	<%@ include file="../../WEB-INF/jspf/message.jspf" %>
	
<script type="text/javascript">
	<%@include file="/js/calendar.js"%>
	var fromDate = new Calendar();
	fromDate.init({lng:'${sessionScope.local}'});
	fromDate.add('fromDate', 'fromDate');
	
	var toDate = new Calendar();
	toDate.init({lng:'${sessionScope.local}'});
	toDate.add('toDate', 'toDate');
</script>		

</body>
</html>