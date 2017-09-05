	// 属性申明
	var aArrayValue = new Array() ;
	var aArrayText = new Array() ;

	function clearArray() {
		aArrayText = new Array() ;
		aArrayValue = new Array() ;
	}

	// 数组初始化，向数组中新添键值
	function arrayPush(txt, value, isClear) {
		if(isClear) {
			clearArray() ;
		}
  
		aArrayText.push(txt) ;

		if(value == undefined) {
			aArrayValue.push("") ;
		} else {
			aArrayValue.push(value) ;
		}
	}

	// 数组初始化
	function setArray(arrayText, arrayValue) {
		clearArray() ;
		
		aArrayText = arrayText ;
		aArrayValue = arrayValue ;
	}

	// 获取控件左绝对位置
	function getAbsoluteLeft(value) {
		var oObj = document.getElementById(value)
		var iLeft = oObj.offsetLeft            
		while(oObj.offsetParent != null) { 
			var oParent = oObj.offsetParent ;
			iLeft += oParent.offsetLeft ;
			oObj = oParent ;
		}
		return iLeft ;
	}

	// 获取控件上绝对位置
	function getAbsoluteTop(value) {
		var oObj = document.getElementById(value) ;
		var iTop = oObj.offsetTop ;
		while(oObj.offsetParent != null)
		{  
			var oParent = oObj.offsetParent ;
			iTop += oParent.offsetTop ;  // Add parent top position
			oObj = oParent ;
		}
		return iTop ;
	}

	// 获取控件长度
	function getElementWidth(value) {
		var oObj = document.getElementById(value) ;
		return oObj.offsetHeight ;
	}

	// 设置div控件绝对位置
	function setDivLocation(objDiv, getValue) {
		objDiv.style.left = getAbsoluteLeft(getValue) ;
		objDiv.style.top = parseFloat(getAbsoluteTop(getValue)) + parseFloat(getElementWidth(getValue)) ;
	}

	function setDivNone(divId) {
		objDiv = document.getElementById(divId) ;
		objDiv.style.display = "none" ;
	}

	function setDivInline(divId) {
		objDiv = document.getElementById(divId) ;
		objDiv.style.display = "inline" ;
	}

	function changeTableColor_Over(value) {
		document.getElementById(value).style.backgroundColor = "#B0C4DE" ;
	}

	function changeTableColor_Out(value) {
		document.getElementById(value).style.backgroundColor = "#FFFAF0" ;
	}

	// 将数据插入控件
	function insertValue(txtId, hidId, iValue) {
		var oHidId = document.getElementById(hidId) ;
		var oHidCompare = document.getElementById("compareHidden") ;
		
		oHidCompare.value = document.getElementById("value" + iValue).value ;
		document.getElementById(txtId).value = document.getElementById("text" + iValue).innerHTML ;

		if(oHidId.value != oHidCompare.value) {
			oHidId.value = document.getElementById("value" + iValue).value ;
		}
	}

	// 将数据插入控件
	function insertValue_New(hidId, value) {
		var oHidId = document.getElementById(hidId) ;
		var oHidCompare = document.getElementById("compareHidden") ;
		
		oHidCompare.value = value ;

		if(oHidId.value != oHidCompare.value) {
			oHidId.value = value ;
		}
	}

	function insertValue_Mouse(divId, txtId, hidId, iValue) {
		insertValue(txtId, hidId, iValue) ;
		setDivNone(divId) ;
	}

	// 将数据插入层
	function insertDiv(divId , txtId, hidId, value, strTxt, strValue) {
		var arrTxt = new Array() ;
		var arrValue = new Array() ;
		var objDiv = document.getElementById(divId) ;

		if(!strTxt) {
			arrTxt = aArrayText ;
		} else {
			arrTxt = strTxt.split(",") ;
		}

		if(!strValue) {
			arrValue = aArrayValue ;
		} else {
			arrValue = strValue.split(",") ;
		}
		
		var iLen = arrTxt.length ;
		var flag = false ;
		var str = "" ;
		str += "<table cellpadding=\"3\" cellspacing=\"0\" bgColor=\"#FFFAF0\">" ;

		for (var i=0; i<iLen; i++) {
			if(arrTxt[i] == value.lTrim()) {
				insertValue_New(hidId, arrValue[i]) ;

				flag = true ;
			}

			if(compareValue(arrTxt[i], value.lTrim())) {
				str += "<tr>" ;
				str += "<td style=\"background-color:#FFFAF0;padding:1px,3px,1px,2px;text-indent:0px;\" onmousedown=\"insertValue_Mouse('" + divId + "', '" + txtId + "', '" + hidId + "', " + i + ") ;\" id=\"t" + i + "\" onMouseOver=\"changeTableColor_Over(this.id) ;\" onMouseOut=\"changeTableColor_Out(this.id);\">" ;
				str += "<pre><span id=\"text" + i + "\">" + arrTxt[i] + "</span></pre>";
				str += "<input type=\"hidden\" id=\"value" + i + "\" value=\"" + arrValue[i] + "\" />" ;
				str += "</td>" ;
				str += "</tr>" ;
			}
		}

		str += "</table>" ;
		
		if(!flag) {
			insertValue_New(hidId, "") ;
		}

		objDiv.innerHTML = str ;

		if(str.length == 65) {
			setDivNone(divId) ;
		} else {
			setDivLocation(objDiv, txtId) ;
			setDivInline(divId) ;
		}
	}

	function clickinsertDiv(divId, txtId, hidId, value, strTxt, strValue) {
		if(value == "　　　　") {
			document.getElementById(txtId).value = "" ;
		} else {
			insertDiv(divId, txtId, hidId, value, strTxt, strValue) ;	
		}
	}
	
	// 判断一个字符串中是否包括另一个字符串
	function compareValue(value1, value2) {
		if(value1.toString().search(value2) == -1) {
			return false ;
		} else {
			return true ;
		}
	}

	// 删除字符串左空格
	String.prototype.lTrim = function() {
		return this.replace(/(^\s*)/g,"") ;
	}
	
	// 
	function genTextBox(paramTxtId, paramTxtName, paramHidId, paramHidName, paramDiv, paramTextClass, paramDivClass, paramScript, paramTextOther, strTxt, strValue) {
		paramTxtName == "" ? paramTxtId : paramTxtName ;
		paramHidName == "" ? paramHidId : paramHidName ;

		var str = "" ;
		str += "<input type=\"text\" name=\"" + paramTxtName + "\" id=\"" + paramTxtId + "\"" ;
		
		if(paramTextClass != "") {
			str += " class=\"" + paramTextClass + "\"" ;
		}

		if(paramTextOther != "") {
			str += " " + paramTextOther ;
		}

		str += " onclick=\"clickinsertDiv('" + paramDiv + "', '" + paramTxtId + "', '" + paramHidId + "', this.value, '" + strTxt + "', '" + strValue + "');\" onpropertychange=\"insertDiv('" + paramDiv + "', '" + paramTxtId + "', '" + paramHidId + "', this.value, '" + strTxt + "', '" + strValue + "');\" onblur=\"setDivNone('" + paramDiv + "') ;\"" ;
		str += " />" ;

		str += "<div id=\"" + paramDiv + "\"" ;
		
		if(paramDivClass != "") {
			str += " class=\"" + paramDivClass + "\"" ;
		}

		str += " style=\"display: none; border: solid #808080 1px; position: absolute;\">" ;
		str += "</div>" ;
		str += "<input type=\"hidden\" name=\"" + paramHidName + "\" id=\"" + paramHidId + "\"" ;
		
		if(paramScript != "") {
			str +=  "onpropertychange=\"" + paramScript + "\"" ;
		}

		str += "/>" ;
		str += "<input type=\"hidden\" id=\"compareHidden\" />" ;

		document.write(str) ;
		
		return str;
	}
	
	function genTextBoxStr(paramTxtId, paramTxtName, paramHidId, paramHidName, paramDiv, paramTextClass, paramDivClass, paramScript, paramTextOther, strTxt, strValue) {
		paramTxtName == "" ? paramTxtId : paramTxtName ;
		paramHidName == "" ? paramHidId : paramHidName ;

		var str = "" ;
		str += "<input type=\"text\" name=\"" + paramTxtName + "\" id=\"" + paramTxtId + "\"" ;
		
		if(paramTextClass != "") {
			str += " class=\"" + paramTextClass + "\"" ;
		}

		if(paramTextOther != "") {
			str += "style=\"" + paramTextOther + "\"" ;
		}

		str += " onclick=\"clickinsertDiv('" + paramDiv + "', '" + paramTxtId + "', '" + paramHidId + "', this.value, '" + strTxt + "', '" + strValue + "');\" onpropertychange=\"insertDiv('" + paramDiv + "', '" + paramTxtId + "', '" + paramHidId + "', this.value, '" + strTxt + "', '" + strValue + "');\" onblur=\"setDivNone('" + paramDiv + "') ;\"" ;
		str += " />" ;

		str += "<div id=\"" + paramDiv + "\"" ;
		
		if(paramDivClass != "") {
			str += " class=\"" + paramDivClass + "\"" ;
		}

		str += " style=\"display: none; border: solid #808080 1px; position: absolute;\">" ;
		str += "</div>" ;
		str += "<input type=\"hidden\" name=\"" + paramHidName + "\" id=\"" + paramHidId + "\"" ;
		
		if(paramScript != "") {
			str +=  "onpropertychange=\"" + paramScript + "\"" ;
		}

		str += "/>" ;
		str += "<input type=\"hidden\" id=\"compareHidden\" />" ;

		return str;
	}