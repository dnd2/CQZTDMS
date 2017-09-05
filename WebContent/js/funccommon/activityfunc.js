// 市场活动默认时间时间
function _setDate_(startDate, endDate, totMon, befMon) {
	var totMon = totMon - 1 ;
	var sDate = document.getElementById("sys_date__").value ;
	var aDate = sDate.split(",") ;
	var iYear = aDate[0] ;
	var iMonth = aDate[1] ;
	
	var iTheMonth = parseFloat(iMonth) - befMon ;
	var endMonth = iTheMonth + totMon ;
	
	if(iTheMonth <= 0) {
		iTheMonth = 12 - parseInt(befMon%12) + 1 ;
		iYear = iYear - parseInt(befMon/12) - 1 ;
	}
	
	if(iTheMonth < 10) {
		iTheMonth = "0" + iTheMonth ;
	}
	
	document.getElementById(startDate).value = iYear + "-" + iTheMonth + "-" + "01" ;
	
	var iNextMonth = parseFloat(iTheMonth) + totMon ;
	
	if(iNextMonth > 12) {
		iYear = parseFloat(iYear) + 1 ;
		iNextMonth = iNextMonth - 12 ;
	}
	
	if(endMonth > 12) {
		endMonth = endMonth - 12 ;
	}
	
	var dEnd = new Date(iYear + "/" + (iNextMonth+1) + "/0") ;
	
	if(endMonth < 10) {
		endMonth = "0" + endMonth ;
	}
	
	document.getElementById(endDate).value = iYear + "-" + endMonth + "-" + dEnd.getDate() ;
}