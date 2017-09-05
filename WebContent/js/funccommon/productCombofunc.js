// JavaScript Document
/**
 * 
 * @param path 服务器路径
 * @param contralId  产品套餐需要显示的节点
 */
function productStart(path, proValue,isDisabled, isAll,contralId) {
		var url = path + "/sales/ordermanage/orderreport/UrgentOrderReport/getProductStrByCompanyId.json" ;
		makeCall(url, setProductSelStr, {contralId:contralId,isDisabled:isDisabled,proValue:proValue,isAll:isAll}) ;
}

function setProductSelStr(json) {
	var para = json.para ;
	
	if(para >= 0) {
		var mySelName = "_productName_" ;
		var mySelId = "_productId_" ;
		var sProductNames = json.productName ;
		var sProductIds = json.productId ;
		var iProValue = json.proValue ;
		var bIsDisabled = json.isDisabled ;
		var bIsAll = json.isAll ;
		var aProductNames = new Array() ;
		var aProductIds = new Array() ;
		var iLength = 0 ;
		
		var selectStr = "<td align=\"right\">产品套餐：</td>" ;
		selectStr += "<td align=\"left\">" ;
		if(sProductIds) {
			aProductNames = sProductNames.split(",") ;
			aProductIds = sProductIds.split(",") ;
			
			iLength = aProductIds.length ;
			
			selectStr += "<select id=\"_myProId_\" name=\"_myProName_\"" ;
			
			if(bIsDisabled == "true") {
				selectStr += " disabled=\"disabled\"" ;
			}
			
			selectStr += " onchange=\"_changeSel_() ;\">" ;
			
			if(bIsAll == "true") {
				selectStr += "<option value=\"-2\" >-请选择-</option>" ;
				
				if(!iProValue || iProValue == 0 || iProValue == -1) {
					iProValue = -2 ;
				}
			}
			
			for(var i=0; i<iLength; i++) {
				if(iProValue == aProductIds[i]) {
					selectStr += "<option value=\"" + aProductIds[i] + "\" selected=\"selected\">" + aProductNames[i] + "</option>" ;
				} else {
					selectStr += "<option value=\"" + aProductIds[i] + "\">" + aProductNames[i] + "</option>" ;
				}
			}
			
			selectStr += "</select>" ;
			
			if(!iProValue || iProValue == 0 || iProValue == -1) {
				iProValue = aProductIds[0] ;
			}
			
			selectStr += "<input type=\"hidden\" id=\"" + mySelId + "\" name=\"" + mySelName + "\" value=\"" + iProValue + "\">"
		} else {
			selectStr += "<font color=\"red\">未维护</font>" ;
			selectStr += "<input type=\"hidden\" id=\"" + mySelId + "\" name=\"" + mySelName + "\" value=\"-1\">"
		}
		selectStr += "</td>" ;
		
		var contralId = json.contralId == "" ? "_productControl_" : json.contralId;
		
		var oObj = document.getElementById(contralId) ;
		oObj.innerHTML = selectStr ;
		// document.write(selectStr) ;
	}
}

function _changeSel_() {
	var _mySel_ = document.getElementById("_myProId_").value ;
	document.getElementById("_productId_").value = _mySel_ ;
}

function _setSelDisabled_(tabValue, count) {
	if(document.getElementById("_myProId_")) {
		var oSel = document.getElementById("_myProId_") ;
		var oTab = document.getElementById(tabValue) ;
		var iLength = oTab.rows.length ;
		
		if(iLength > count) {
			oSel.disabled = true ;
		} else {
			oSel.disabled = false ;
		}
	}
}

function _getTip_() {
	if(document.getElementById("_productId_")) {
		var oHid = document.getElementById("_productId_") ;
		
		if(oHid.value == -2) {
			MyAlert("请选择产品套餐!") ;
			
			return false ;
		} else {
			return true ;
		}
	} else {
		return true ;
	}
}