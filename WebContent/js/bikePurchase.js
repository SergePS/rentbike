function check() {
	if(document.getElementById("value").value==0){
		errorMessageShow('<c:out value="${valueZeroWarn}"></c:out>');
	}else{
		if(document.getElementById("rentPrice").value==0){
			errorMessageShow('<c:out value="${rentPriceZeroWarn}"></c:out>');
		}else{
			document.getElementById("addBikeProductForm").submit();
		}
	}
}