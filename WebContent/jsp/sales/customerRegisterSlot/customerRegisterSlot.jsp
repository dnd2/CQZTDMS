<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
 <!--
 var lenFlag = 2 ;
 function doInit(){
		loadcalendar();
		
		var oNowDate = document.getElementById("nowDate") ;
		if(document.getElementById("sys_date__")) {
			oNowDate.value = document.getElementById("sys_date__").value ;
		} else {
			// MyAlert() ;
		}
 }
 
 function setDisabled(value) {
	 var va1 = "cusAgr" + value ;
	 var va2 = "isSJA" + value ;
	 var va3 = "isSJ" + value ;
	 
	 var getVa = document.getElementById(va1).value ;
	 
	 if(getVa == <%= Constant.BARGAIN_CUS_SQ%> || getVa == <%= Constant.BARGAIN_CUS_GZ%>) {
		 document.getElementById(va2).disabled = false ;
		 
		 document.getElementById(va3).value = document.getElementById(va2).value ;
	 } else {
		 document.getElementById(va2).disabled = true ;
		 
		 document.getElementById(va3).value = "0" ;
	 }
 }
 
 function setHidValue(value) {
	 var va1 = "isSJ" + value ;
	 var va2 = "isSJA" + value ;
	 
	 document.getElementById(va1).value = document.getElementById(va2).value ;
 }

 function rowAdd() {
	var oTable = document.getElementById("oneTab") ;
	var rowsLen = parseInt(oTable.rows.length) ;

	oTable.insertRow(rowsLen) ;
	oTable.rows[rowsLen].setAttribute("id", "tr" + lenFlag) ;
	// oTable.rows[rowsLen].setAttribute("align", "center") ;
	// oTable.rows[rowsLen].setAttribute("className", "table_list") ;

	var oCell0= oTable.rows[rowsLen].insertCell(0) ;
	oCell0.innerHTML = "<input name='subDate'  class='short_txt' id='subDate"+lenFlag+"' type='text' readOnly='readOnly' datatype=\"1,is_date,10\"/><input class='time_ico' type='button' onClick='showcalendar(event,\"subDate"+lenFlag+"\",false);' value='&nbsp;'/><input type='hidden' name='headId' id='headId"+lenFlag+"' value=''>";
	//var oSDText = document.createElement("input") ;
	//oSDText.setAttribute("type", "text") ;
	//oSDText.setAttribute("id", "subDate"+lenFlag) ;
	//oSDText.setAttribute("name", "subDate") ;
	//oSDText.setAttribute("value", lenFlag) ;
	//oTable.rows[rowsLen].cells[0].appendChild(oSDText) ;
	
	var oCell1 = oTable.rows[rowsLen].insertCell(1) ;
	oCell1.innerHTML = "<input type='text' name='cusName' id='cusName"+lenFlag+"' class='short_txt' maxlength='30'/>";
	//var oCNText = document.createElement("input") ;
	//oCNText.setAttribute("type", "text") ;
	//oCNText.setAttribute("id", "cusName"+lenFlag) ;
	//oCNText.setAttribute("name", "cusName") ;
	//oTable.rows[rowsLen].cells[1].appendChild(oCNText) ;

	var oCell2 = oTable.rows[rowsLen].insertCell(2) ;
	oCell2.innerHTML = "<input type='text' name='linkTell' id='linkTell"+lenFlag+"' class='short_txt' maxlength='15'/>";
	//var oLTText = document.createElement("input") ;
	//oLTText.setAttribute("type", "text") ;
	//oLTText.setAttribute("id", "linkTell"+lenFlag) ;
	//oLTText.setAttribute("name", "linkTell") ;
	//oTable.rows[rowsLen].cells[2].appendChild(oLTText) ;

	var oCell3 = oTable.rows[rowsLen].insertCell(3) ;
	oCell3.innerHTML = "<input type='text' name='hopeModel' id='hopeModel"+lenFlag+"' class='middle_txt' maxlength='50'/>";
	//var oHMText = document.createElement("input") ;
	//oHMText.setAttribute("type", "text") ;
	//oHMText.setAttribute("id", "hopeModel"+lenFlag) ;
	//oHMText.setAttribute("name", "hopeModel") ;
	//oTable.rows[rowsLen].cells[3].appendChild(oHMText) ;

	var oCell4 = oTable.rows[rowsLen].insertCell(4) ;
	oCell4.innerHTML = "<input type='text' name='nowModel' id='nowModel"+lenFlag+"' class='middle_txt' maxlength='50'/>";
	//var oNMText = document.createElement("input") ;
	//oNMText.setAttribute("type", "text") ;
	//oNMText.setAttribute("id", "nowModel"+lenFlag) ;
	//oNMText.setAttribute("name", "nowModel") ;
	//oTable.rows[rowsLen].cells[4].appendChild(oNMText) ;

	var oCell5 = oTable.rows[rowsLen].insertCell(5) ;
	oCell5.innerHTML = "<input type='text' name='mainUse' id='mainUse"+lenFlag+"' class='middle_txt' maxlength='200'/>";
	//var oMUText = document.createElement("input") ;
	//oMUText.setAttribute("type", "text") ;
	//oMUText.setAttribute("id", "mainUse"+lenFlag) ;
	//oMUText.setAttribute("name", "mainUse") ;
	//oTable.rows[rowsLen].cells[5].appendChild(oMUText) ;

	var oCell6 = oTable.rows[rowsLen].insertCell(6) ;
	oCell6.innerHTML = genSelBoxStrExp("buyPoint",<%=Constant.BUY_POINT%>,"",true,"","","false",'');
	//var oBPSelect = document.createElement("select") ;
	//oBPSelect.setAttribute("id", "buyPoint"+lenFlag) ;
	//oBPSelect.setAttribute("name", "buyPoint") ;
	//var oBPOption = document.createElement("option") ;
	//oBPOption.setAttribute("value", "1") ;
	//oBPOption.appendChild(document.createTextNode("外观")) ;
	//oBPSelect.appendChild(oBPOption) ;
	//oTable.rows[rowsLen].cells[6].appendChild(oBPSelect) ;

	var oCell7 = oTable.rows[rowsLen].insertCell(7) ;
	oCell7.innerHTML = genSelBoxStrExp("info",<%=Constant.INFO_DITCH%>,"",true,"","","false",'');
	//var oILSelect = document.createElement("select") ;
	//oILSelect.setAttribute("id", "info"+lenFlag) ;
	//oILSelect.setAttribute("name", "info") ;
	//var oILOption = document.createElement("option") ;
	//oILOption.setAttribute("value", "1") ;
	//oILOption.appendChild(document.createTextNode("朋友推荐")) ;
	//oILSelect.appendChild(oILOption) ;
	//oTable.rows[rowsLen].cells[7].appendChild(oILSelect) ;

	var oCell8 = oTable.rows[rowsLen].insertCell(8) ;
	var cusAgrSel = "<select name='cusAgr' id='cusAgr" + lenFlag + "' onchange='setDisabled("+lenFlag+") ;'>" ;
	cusAgrSel += "<option value=''>" ;
	cusAgrSel += "-请选择-" ;
	cusAgrSel += "</option>" ;
	cusAgrSel += "<option value='<%= Constant.BARGAIN_CUS_SQ%>'>" ;
	cusAgrSel += "首洽成交" ;
	cusAgrSel += "</option>" ;
	cusAgrSel += "<option value='<%= Constant.BARGAIN_CUS_GZ%>'>" ;
	cusAgrSel += "跟踪成交" ;
	cusAgrSel += "</option>" ;
	cusAgrSel += "</select>" ;
	oCell8.innerHTML = cusAgrSel ;
	//genSelBoxStrExp("cusAgr",<%=Constant.BARGAIN_CUS%>,"",true,"","","false",'');
	//var oCGSelect = document.createElement("select") ;
	//oCGSelect.setAttribute("id", "cusAgr"+lenFlag) ;
	//oCGSelect.setAttribute("name", "cusAgr") ;
	//var oCGOption = document.createElement("option") ;
	//oCGOption.setAttribute("value", "1") ;
	//oCGOption.appendChild(document.createTextNode("首洽成交")) ;
	//oCGSelect.appendChild(oCGOption) ;
	//oTable.rows[rowsLen].cells[8].appendChild(oCGSelect) ;
	
	var oCell9 = oTable.rows[rowsLen].insertCell(9) ;
	var isSJSelect = "<select name='isSJA' id='isSJA" + lenFlag + "' disabled onchange='setHidValue("+lenFlag+") ;'>" ;
	isSJSelect += "<option value='0'>" ;
	isSJSelect += "-请选择-" ;
	isSJSelect += "</option>" ;
	isSJSelect += "<option value='<%= Constant.IF_TYPE_YES%>'>" ;
	isSJSelect += "是" ;
	isSJSelect += "</option>" ;
	isSJSelect += "<option value='<%= Constant.IF_TYPE_NO%>'>" ;
	isSJSelect += "否" ;
	isSJSelect += "</option>" ;
	isSJSelect += "</select>" ;
	isSJSelect += "<input type='hidden' name='isSJ' id='isSJ" + lenFlag + "' value='0'>" ;
	oCell9.innerHTML = isSJSelect ;
	// genSelBoxStrExp("isSJ",<%=Constant.IF_TYPE%>,"",true,"","","false",'');
	
	var oCell10 = oTable.rows[rowsLen].insertCell(10) ;
	oCell10.innerHTML = genSelBoxStrExp("cusAtt",<%=Constant.CUS_NATURE%>,"",true,"","","false",'');
	//var oCASelect = document.createElement("select") ;
	//oCASelect.setAttribute("id", "cusAtt"+lenFlag) ;
	//oCASelect.setAttribute("name", "cusAtt") ;
	//var oCAOption = document.createElement("option") ;
	//oCAOption.setAttribute("value", "1") ;
	//oCAOption.appendChild(document.createTextNode("新购")) ;
	//oCASelect.appendChild(oCAOption) ;
	//oTable.rows[rowsLen].cells[9].appendChild(oCASelect) ;

	var oCell11 = oTable.rows[rowsLen].insertCell(11) ;
	oCell11.innerHTML = genSelBoxStrExp("gaveUp",<%=Constant.DESERT_REASON%>,"",true,"","","false",'');
	//var oGCSelect = document.createElement("select") ;
	//oGCSelect.setAttribute("id", "gaveUp"+lenFlag) ;
	//oGCSelect.setAttribute("name", "gaveUp") ;
	//var oGCOption = document.createElement("option") ;
	//oGCOption.setAttribute("value", "1") ;
	//oGCOption.appendChild(document.createTextNode("车的外观")) ;
	//oGCSelect.appendChild(oGCOption) ;
	//oTable.rows[rowsLen].cells[10].appendChild(oGCSelect) ;

	var oCell12 = oTable.rows[rowsLen].insertCell(12) ;
	oCell12.innerHTML = "<input type='text' name='saleCus' id='saleCus"+lenFlag+"' class='middle_txt' maxlength='30'/>";
	//var oSCText = document.createElement("input") ;
	//oSCText.setAttribute("type", "text") ;
	//oSCText.setAttribute("id", "saleCus"+lenFlag) ;
	//oSCText.setAttribute("name", "saleCus") ;
	//oTable.rows[rowsLen].cells[11].appendChild(oSCText) ;

	var oCell13 = oTable.rows[rowsLen].insertCell(13) ;
	oCell13.innerHTML = "<input type='text' name='remark' id='remark"+lenFlag+"' class='long_txt' maxlength='300'/>";
	//var oRMText = document.createElement("input") ;
	//oRMText.setAttribute("type", "text") ;
	//oRMText.setAttribute("id", "remark"+lenFlag) ;
	//oRMText.setAttribute("name", "remark") ;
	//oTable.rows[rowsLen].cells[12].appendChild(oRMText) ;

	var oCell14 = oTable.rows[rowsLen].insertCell(14) ;
	oCell14.innerHTML = "<a href=\"#anchorA" + lenFlag + "\" onclick=\"rowDelete(" + lenFlag + ") ;\">[删除]</a>" ;
	//var oAnchor = document.createElement("a") ;
	//oAnchor.setAttribute("name", "anchorA" + lenFlag) ;
	//oTable.rows[rowsLen].cells[13].appendChild(oAnchor) ;
	//var oDELink = document.createElement("a") ;
	//oDELink.setAttribute("href", "#anchorA" + lenFlag) ;
	//oDELink.setAttribute("onClick", "rowDelete(" + lenFlag + ") ;") ;
	//oDELink.appendChild(document.createTextNode("[删除]")) ;
	//oTable.rows[rowsLen].cells[13].appendChild(oDELink) ;
	setTableDisplay() ;
	lenFlag++ ;
 }
 
 function rowUpdate(value, subDate, cusName, cusTell, hopeModel, nowModel, mainUse, buyPoint, infoDitch, bargainCus, cusNature, desertReason, saleAdv, remark, isSj) {
	 	allRowsDelete() ;
	 	cusName = cusName == "null" ? "" : cusName ;
	 	cusTell = cusTell == "null" ? "" : cusTell ;
	 	hopeModel = hopeModel == "null" ? "" : hopeModel ;
	 	nowModel = nowModel == "null" ? "" : nowModel ;
	 	mainUse = mainUse == "null" ? "" : mainUse ;
	 	saleAdv = saleAdv == "null" ? "" : saleAdv ;
	 	remark = remark == "null" ? "" : remark ;
	 	isSj = isSj == "null" ? "0" : isSj ;
		var oTable = document.getElementById("oneTab") ;
		var rowsLen = parseInt(oTable.rows.length) ;
	
		oTable.insertRow(rowsLen) ;
		oTable.rows[rowsLen].setAttribute("id", "tr" + lenFlag) ;
		// oTable.rows[rowsLen].setAttribute("align", "center") ;
		// oTable.rows[rowsLen].setAttribute("className", "table_list") ;

		var oCell0= oTable.rows[rowsLen].insertCell(0) ;
		oCell0.innerHTML = "<input name='subDate'  class='short_txt' id='subDate"+lenFlag+"' type='text' readOnly='readOnly' datatype=\"1,is_date,10\" value=\"" + subDate + "\" /><input class='time_ico' type='button' onClick='showcalendar(event,\"subDate"+lenFlag+"\",false);' value='&nbsp;'/><input type='hidden' name='headId' id='headId"+lenFlag+"' value='" + value + "'>";
		//var oSDText = document.createElement("input") ;
		//oSDText.setAttribute("type", "text") ;
		//oSDText.setAttribute("id", "subDate"+lenFlag) ;
		//oSDText.setAttribute("name", "subDate") ;
		//oSDText.setAttribute("value", lenFlag) ;
		//oTable.rows[rowsLen].cells[0].appendChild(oSDText) ;
		
		var oCell1 = oTable.rows[rowsLen].insertCell(1) ;
		oCell1.innerHTML = "<input type='text' name='cusName' id='cusName"+lenFlag+"' class='short_txt' maxlength='30' value='" + cusName + "' />";
		//var oCNText = document.createElement("input") ;
		//oCNText.setAttribute("type", "text") ;
		//oCNText.setAttribute("id", "cusName"+lenFlag) ;
		//oCNText.setAttribute("name", "cusName") ;
		//oTable.rows[rowsLen].cells[1].appendChild(oCNText) ;

		var oCell2 = oTable.rows[rowsLen].insertCell(2) ;
		oCell2.innerHTML = "<input type='text' name='linkTell' id='linkTell"+lenFlag+"' class='short_txt' maxlength='15' value='" + cusTell + "' />";
		//var oLTText = document.createElement("input") ;
		//oLTText.setAttribute("type", "text") ;
		//oLTText.setAttribute("id", "linkTell"+lenFlag) ;
		//oLTText.setAttribute("name", "linkTell") ;
		//oTable.rows[rowsLen].cells[2].appendChild(oLTText) ;

		var oCell3 = oTable.rows[rowsLen].insertCell(3) ;
		oCell3.innerHTML = "<input type='text' name='hopeModel' id='hopeModel"+lenFlag+"' class='middle_txt' maxlength='50' value='" + hopeModel + "' />";
		//var oHMText = document.createElement("input") ;
		//oHMText.setAttribute("type", "text") ;
		//oHMText.setAttribute("id", "hopeModel"+lenFlag) ;
		//oHMText.setAttribute("name", "hopeModel") ;
		//oTable.rows[rowsLen].cells[3].appendChild(oHMText) ;

		var oCell4 = oTable.rows[rowsLen].insertCell(4) ;
		oCell4.innerHTML = "<input type='text' name='nowModel' id='nowModel"+lenFlag+"' class='middle_txt' maxlength='50' value='" + nowModel + "' />";
		//var oNMText = document.createElement("input") ;
		//oNMText.setAttribute("type", "text") ;
		//oNMText.setAttribute("id", "nowModel"+lenFlag) ;
		//oNMText.setAttribute("name", "nowModel") ;
		//oTable.rows[rowsLen].cells[4].appendChild(oNMText) ;

		var oCell5 = oTable.rows[rowsLen].insertCell(5) ;
		oCell5.innerHTML = "<input type='text' name='mainUse' id='mainUse"+lenFlag+"' class='middle_txt' maxlength='200' value='" + mainUse + "' />";
		//var oMUText = document.createElement("input") ;
		//oMUText.setAttribute("type", "text") ;
		//oMUText.setAttribute("id", "mainUse"+lenFlag) ;
		//oMUText.setAttribute("name", "mainUse") ;
		//oTable.rows[rowsLen].cells[5].appendChild(oMUText) ;

		var oCell6 = oTable.rows[rowsLen].insertCell(6) ;
		oCell6.innerHTML = genSelBoxStrExp("buyPoint",<%=Constant.BUY_POINT%>,buyPoint,true,"","","false",'');
		//var oBPSelect = document.createElement("select") ;
		//oBPSelect.setAttribute("id", "buyPoint"+lenFlag) ;
		//oBPSelect.setAttribute("name", "buyPoint") ;
		//var oBPOption = document.createElement("option") ;
		//oBPOption.setAttribute("value", "1") ;
		//oBPOption.appendChild(document.createTextNode("外观")) ;
		//oBPSelect.appendChild(oBPOption) ;
		//oTable.rows[rowsLen].cells[6].appendChild(oBPSelect) ;

		var oCell7 = oTable.rows[rowsLen].insertCell(7) ;
		oCell7.innerHTML = genSelBoxStrExp("info",<%=Constant.INFO_DITCH%>,infoDitch,true,"","","false",'');
		//var oILSelect = document.createElement("select") ;
		//oILSelect.setAttribute("id", "info"+lenFlag) ;
		//oILSelect.setAttribute("name", "info") ;
		//var oILOption = document.createElement("option") ;
		//oILOption.setAttribute("value", "1") ;
		//oILOption.appendChild(document.createTextNode("朋友推荐")) ;
		//oILSelect.appendChild(oILOption) ;
		//oTable.rows[rowsLen].cells[7].appendChild(oILSelect) ;
		
		var oCell8 = oTable.rows[rowsLen].insertCell(8) ;
		var cusAgrSel = "<select name='cusAgr' id='cusAgr" + lenFlag + "' onchange='setDisabled("+lenFlag+") ;'>" ;
		cusAgrSel += "<option value=''>" ;
		cusAgrSel += "-请选择-" ;
		cusAgrSel += "</option>" ;
		if(bargainCus == <%= Constant.BARGAIN_CUS_SQ%>) {
			cusAgrSel += "<option value='<%= Constant.BARGAIN_CUS_SQ%>' selected>" ;
		} else {
			cusAgrSel += "<option value='<%= Constant.BARGAIN_CUS_SQ%>'>" ;
		}
		cusAgrSel += "首洽成交" ;
		cusAgrSel += "</option>" ;
		if(bargainCus == <%= Constant.BARGAIN_CUS_GZ%>) {
			cusAgrSel += "<option value='<%= Constant.BARGAIN_CUS_GZ%>' selected>" ;
		} else {
			cusAgrSel += "<option value='<%= Constant.BARGAIN_CUS_GZ%>'>" ;
		}
		cusAgrSel += "跟踪成交" ;
		cusAgrSel += "</option>" ;
		cusAgrSel += "</select>" ;
		oCell8.innerHTML = cusAgrSel ;
		//genSelBoxStrExp("cusAgr",<%=Constant.BARGAIN_CUS%>,"",true,"","","false",'');
		//var oCGSelect = document.createElement("select") ;
		//oCGSelect.setAttribute("id", "cusAgr"+lenFlag) ;
		//oCGSelect.setAttribute("name", "cusAgr") ;
		//var oCGOption = document.createElement("option") ;
		//oCGOption.setAttribute("value", "1") ;
		//oCGOption.appendChild(document.createTextNode("首洽成交")) ;
		//oCGSelect.appendChild(oCGOption) ;
		//oTable.rows[rowsLen].cells[8].appendChild(oCGSelect) ;
		
		var oCell9 = oTable.rows[rowsLen].insertCell(9) ;
		var isSJSelect = "<select name='isSJA' id='isSJA" + lenFlag + "' onchange='setHidValue("+lenFlag+") ;'>" ;
		isSJSelect += "<option value='0'>" ;
		isSJSelect += "-请选择-" ;
		isSJSelect += "</option>" ;
		if(isSj == <%= Constant.IF_TYPE_YES%>) {
			isSJSelect += "<option value='<%= Constant.IF_TYPE_YES%>' selected>" ;
		} else {
			isSJSelect += "<option value='<%= Constant.IF_TYPE_YES%>'>" ;
		}
		isSJSelect += "是" ;
		isSJSelect += "</option>" ;
		if(isSj == <%= Constant.IF_TYPE_NO%>) {
			isSJSelect += "<option value='<%= Constant.IF_TYPE_NO%>' selected>" ;
		} else {
			isSJSelect += "<option value='<%= Constant.IF_TYPE_NO%>'>" ;
		}
		isSJSelect += "否" ;
		isSJSelect += "</option>" ;
		isSJSelect += "</select>" ;
		isSJSelect += "<input type='hidden' name='isSJ' id='isSJ" + lenFlag + "' value='" + isSj + "'>" ;
		oCell9.innerHTML = isSJSelect ;

		//var oCell8 = oTable.rows[rowsLen].insertCell(8) ;
		//oCell8.innerHTML = genSelBoxStrExp("cusAgr",<%=Constant.BARGAIN_CUS%>,bargainCus,true,"","","false",'');
		//var oCGSelect = document.createElement("select") ;
		//oCGSelect.setAttribute("id", "cusAgr"+lenFlag) ;
		//oCGSelect.setAttribute("name", "cusAgr") ;
		//var oCGOption = document.createElement("option") ;
		//oCGOption.setAttribute("value", "1") ;
		//oCGOption.appendChild(document.createTextNode("首洽成交")) ;
		//oCGSelect.appendChild(oCGOption) ;
		//oTable.rows[rowsLen].cells[8].appendChild(oCGSelect) ;
		
		//var oCell9 = oTable.rows[rowsLen].insertCell(9) ;
		//oCell9.innerHTML = genSelBoxStrExp("isSJ",<%=Constant.IF_TYPE%>,cusNature,true,"","","false",'');
		
		var oCell10 = oTable.rows[rowsLen].insertCell(10) ;
		oCell10.innerHTML = genSelBoxStrExp("cusAtt",<%=Constant.CUS_NATURE%>,cusNature,true,"","","false",'');
		//var oCASelect = document.createElement("select") ;
		//oCASelect.setAttribute("id", "cusAtt"+lenFlag) ;
		//oCASelect.setAttribute("name", "cusAtt") ;
		//var oCAOption = document.createElement("option") ;
		//oCAOption.setAttribute("value", "1") ;
		//oCAOption.appendChild(document.createTextNode("新购")) ;
		//oCASelect.appendChild(oCAOption) ;
		//oTable.rows[rowsLen].cells[9].appendChild(oCASelect) ;

		var oCell11 = oTable.rows[rowsLen].insertCell(11) ;
		oCell11.innerHTML = genSelBoxStrExp("gaveUp",<%=Constant.DESERT_REASON%>,desertReason,true,"","","false",'');
		//var oGCSelect = document.createElement("select") ;
		//oGCSelect.setAttribute("id", "gaveUp"+lenFlag) ;
		//oGCSelect.setAttribute("name", "gaveUp") ;
		//var oGCOption = document.createElement("option") ;
		//oGCOption.setAttribute("value", "1") ;
		//oGCOption.appendChild(document.createTextNode("车的外观")) ;
		//oGCSelect.appendChild(oGCOption) ;
		//oTable.rows[rowsLen].cells[10].appendChild(oGCSelect) ;

		var oCell12 = oTable.rows[rowsLen].insertCell(12) ;
		oCell12.innerHTML = "<input type='text' name='saleCus' id='saleCus"+lenFlag+"' class='middle_txt' maxlength='30' value='" + saleAdv + "'/>";
		//var oSCText = document.createElement("input") ;
		//oSCText.setAttribute("type", "text") ;
		//oSCText.setAttribute("id", "saleCus"+lenFlag) ;
		//oSCText.setAttribute("name", "saleCus") ;
		//oTable.rows[rowsLen].cells[11].appendChild(oSCText) ;

		var oCell13 = oTable.rows[rowsLen].insertCell(13) ;
		oCell13.innerHTML = "<input type='text' name='remark' id='remark"+lenFlag+"' class='long_txt' maxlength='300' value='" + remark + "'/>";
		//var oRMText = document.createElement("input") ;
		//oRMText.setAttribute("type", "text") ;
		//oRMText.setAttribute("id", "remark"+lenFlag) ;
		//oRMText.setAttribute("name", "remark") ;
		//oTable.rows[rowsLen].cells[12].appendChild(oRMText) ;

		var oCell14 = oTable.rows[rowsLen].insertCell(14) ;
		oCell14.innerHTML = "<a href=\"#anchorA" + lenFlag + "\" onclick=\"rowDelete(" + lenFlag + ") ;\">[删除]</a>" ;
		//var oAnchor = document.createElement("a") ;
		//oAnchor.setAttribute("name", "anchorA" + lenFlag) ;
		//oTable.rows[rowsLen].cells[13].appendChild(oAnchor) ;
		//var oDELink = document.createElement("a") ;
		//oDELink.setAttribute("href", "#anchorA" + lenFlag) ;
		//oDELink.setAttribute("onClick", "rowDelete(" + lenFlag + ") ;") ;
		//oDELink.appendChild(document.createTextNode("[删除]")) ;
		//oTable.rows[rowsLen].cells[13].appendChild(oDELink) ;
		setDisabled(lenFlag) ;
		setTableDisplay() ;
		lenFlag++ ;
	 }

 function rowDelete(value) {
	 var oTr = document.getElementById("tr" + value) ;

	 oTr.parentNode.removeChild(oTr) ;
	 
	 setTableDisplay() ;
 }
 
 function allRowsDelete() {
	 var oTable = document.getElementById("oneTab") ;
	 var iLen = oTable.rows.length ;
	 
	 for(var i=iLen-1; i>1; i--) {
		 oTable.rows[i].parentNode.removeChild(oTable.rows[i]) ;
	 }
	 
	 setTableDisplay() ;
 }
 
 function setTableDisplay() {
	 var oTable = document.getElementById("oneTab") ;
	 
	 if(oTable.rows.length <= 2) {
		 setDisplay("oneTab", "none") ;
		 setDisplay("insertTab", "none") ;
	 } else {
		 setDisplay("oneTab", "inline") ;
		 setDisplay("insertTab", "inline") ;
	 }
 }
 
 function setDisplay(objId, commond) {
	 document.getElementById(objId).style.display = commond ;
 }
 
 function insertSubmit() {
	 var subDate = document.getElementsByName("subDate") ;
	 var cusAgr = document.getElementsByName("cusAgr") ;
	 var isSJ = document.getElementsByName("isSJ") ;
	 var cusName = document.getElementsByName("cusName");
	 var linkTell = document.getElementsByName("linkTell");
	 var iLen = subDate.length ;
	 
	 for(var i=0; i<iLen; i++) {
		 if(subDate[i].value.length == 0) {
			 MyAlert("必须录入日期！") ;
			 
			 return false ;
		 }
		 
		 var sNowTime = document.getElementById("nowDate").value ;
		 var aNowTime = sNowTime.split(",") ;
		 var sNowDate = aNowTime[0] + aNowTime[1] ;
		 
		 var sNewTime = subDate[i].value ;
		 var aNewTime = subDate[i].value.split("-") ;
		 var sNewDate = aNewTime[0] + aNewTime[1] ;
		 
		 if(sNowDate > sNewDate) {
			 MyAlert("录入日期错误：仅能提报当月集客信息！") ;
			 
			 return false ;
		 }
		 
		 if(cusAgr[i].value != '' && isSJ[i].value == 0) {
			 MyAlert("请选择是否试驾成交！") ;
			 
			 return false ;
		 }
		 //TODO 新增校验客户姓名必须填写 2012-06-12 韩晓宇
		 if(cusName[i].value == undefined || strTrim(cusName[i].value) == '') {
 			 MyAlert("必须录入客户姓名！") ;
			 
			 return false ;
		 }
		//TODO 新增校验联系电话必须填写 2012-06-12 韩晓宇
		if(linkTell[i].value == undefined || strTrim(linkTell[i].value) == '') {
 			 MyAlert("必须录入联系电话！") ;
			 
			 return false ;
		 }
	 }
	 
	 
	 MyConfirm("是否提交？", insertOperat) ;
 }
 
 //去掉字符串左右空格
 function strTrim(str)   
 {   
     return   str.replace(/(^\s*)|(\s*$)/g,   "");   
 }  
 
 function insertOperat() {
	 var url = "<%=contextPath%>/sales/customerRegisterSlot/customerRegisterSlot/operateInsert.json" ;
	 
	 makeFormCall(url, insertTip, "fm") ;
 }
 
 function insertTip(json) {
	 var retFlag = json.retFlag ;
	 
	 if(retFlag == 1) {
		 allRowsDelete() ;
		 
		 MyAlert("提交成功!") ;
		 
		 __extQuery__(1);
	 } else if(json.Exception != undefined){
		 MyAlert(json.Exception.message) ;
	 }
 }
 
 function downLoadInit() {
		$('fm').action= "<%=contextPath%>/sales/customerRegisterSlot/customerRegisterSlot/dealerDownLoad.json"
		$('fm').submit();
 }
 
 function downLoadTotalInit() {
		if(submitForm('fm')){
			$('fm').action= "<%=contextPath%>/sales/customerRegisterSlot/customerRegisterSlot/oemTotalDownLoad.json"
			$('fm').submit();
		}
 }
 //-->
 </script>
<title>来电（店）客户信息登记及跟踪操作</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 客户信息管理 &gt;  集客量信息录入</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
			<!-- 
	  			<td align="right">渠道选择：</td>
				<td>
					<select name="areaId">
						<c:forEach items="${areaList}" var="po">
						<option value="${po.AREA_ID}|${po.DEALER_ID}">${po.AREA_NAME}</option>
						</c:forEach>
            		</select>
        		</td>
        	-->
			<td align="right">日期：</td>
			<td>
				<div align="left">
	            	<input name="startDate" id="startDate" value="" type="text" class="short_txt" datatype="0,is_date,10" group="startDate,endDate" hasbtn="true" callFunction="showcalendar(event, 'startDate', false);" />
	            	&nbsp;至&nbsp;
	            	<input name="endDate" id="endDate" value="" type="text" class="short_txt" datatype="0,is_date,10" hasbtn="true" callFunction="showcalendar(event, 'endDate', false);" />
            	</div>	
			</td>
			<td align="right"></td>
			<td></td>
			
			</tr> 
			<tr>
				<td colspan="5" align="center">
					<input type="hidden" name="nowDate" id="nowDate" value="" />
					<input type="button" class="normal_btn" name="addBtn" id="addBtn" onclick="rowAdd();" value="新增" />&nbsp;
					<input type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" />&nbsp;
					<input type="button" class="cssbutton" name="button2" onclick="downLoadTotalInit();" value="汇总下载" />&nbsp;
					<input type="button" class="normal_btn" onclick="downLoadInit();" value="下载" id="downBtn" /> 
				</td>
			</tr>
		</table>
		<table class="table_query" width="100%" style="display:none" border="0" align="center" cellpadding="0" cellspacing="2" id="oneTab">
		<tr>
			<th rowspan="2" align="center">日期</th>
			<th rowspan="2" align="center">客户姓名</th>
			<th rowspan="2" align="center">联系电话</th>
			<th rowspan="2" align="center">意向车型</th>
			<th rowspan="2" align="center">现用车型</th>
			<th rowspan="2" align="center">主要用途</th>
			<th rowspan="2" align="center">购买侧重点</th>
			<th rowspan="2" align="center">信息渠道</th>
			<th colspan="3" align="center">成交客户</th>
			<th align="center">放弃客户</th>
			<th rowspan="2" align="center">销售顾问</th>
			<th rowspan="2" align="center">备注</th>
			<th rowspan="2" align="center">操作</th>
		</tr>
		<tr>
		<th align="center">成交客户</th>
		<th align="center">是否试驾</th>
		<th align="center">客户性质</th>
		<th align="center">放弃原因</th>
		</tr>
		</table>
		<table class="table_query" width="100%" style="display:none" border="0" align="center" cellpadding="0" cellspacing="2" id="insertTab">
			<tr>
				<td align="center">
					<input type="button" class="normal_btn" name="insertBtn" id="insertBtn" onclick="insertSubmit();" value="提 交"/>
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
	
</div>
<script type="text/javascript">

	var myPage;
	var url = "<%=contextPath%>/sales/customerRegisterSlot/customerRegisterSlot/operatQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
				// {header: "编号", dataIndex: 'REGISTER_SLOT_NO', align:'center'},
				{header: "日期", dataIndex: 'APPLYDATE', align:'center'},
				{header: "客户姓名", dataIndex: 'CUS_NAME', align:'center'},
				{header: "联系电话", dataIndex: 'LINK_TELL', align:'center'},
				{header: "意向车型", dataIndex: 'HOPE_MODEL', align:'center'},
				{header: "现用车型", dataIndex: 'NOW_MODEL', align:'center'},
				{header: "主要用途", dataIndex: 'MAIN_USE', align:'center'},
				{header: "购买侧重点", dataIndex: 'BUY_POINT', align:'center',renderer:getItemValue},
				{header: "信息渠道", dataIndex: 'INFO_DITCH', align:'center',renderer:getItemValue},
				{header: "客户性质", dataIndex: 'BARGAIN_CUS', align:'center',renderer:getItemValue},
				{header: "成交客户", dataIndex: 'CUS_NATURE', align:'center',renderer:getItemValue},
				{header: "是否试驾", dataIndex: 'IS_SJ', align:'center',renderer:getItemValue},
				{header: "放弃原因", dataIndex: 'DESERT_REASON', align:'center',renderer:getItemValue},
				{header: "销售顾问", dataIndex: 'SALE_ADVISER', align:'center'},
				{header: "备注", dataIndex: 'REMARK', align:'center'},
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'REGISTER_SLOT_ID',renderer:myLink}
		      ];

	function myLink(value,metaDate,record){
		var subDate = record.data.APPLYDATE ;
		var cusName = record.data.CUS_NAME ;
		var cusTell = record.data.LINK_TELL ;
		var hopeModel = record.data.HOPE_MODEL ;
		var nowModel = record.data.NOW_MODEL ;
		var mainUse = record.data.MAIN_USE ;
		var buyPoint = record.data.BUY_POINT ;
		var infoDitch = record.data.INFO_DITCH ;
		var bargainCus = record.data.BARGAIN_CUS ;
		var cusNature = record.data.CUS_NATURE ;
		var desertReason = record.data.DESERT_REASON ;
		var saleAdv = record.data.SALE_ADVISER ;
		var remark = record.data.REMARK ;
		var isSj = record.data.IS_SJ ;
		
		return String.format("<a href=\"#\" onclick=\"rowUpdate('" + value + "','" + subDate + "','" + cusName + "','" + cusTell + "','" + hopeModel + "','" + nowModel + "','" + mainUse + "','" + buyPoint + "','" + infoDitch + "','" + bargainCus + "','" + cusNature + "','" + desertReason + "','" + saleAdv + "','" + remark + "','" + isSj + "')\">[修改]</a>");    
    }
</script>    
</body>
</html>