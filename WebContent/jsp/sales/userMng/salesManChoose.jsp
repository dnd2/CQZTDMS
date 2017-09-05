<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<% String contextPath = request.getContextPath(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title>物料选择</title>
<script type="text/javascript">
function doInit() {
	var aVehicle = parent.frames["inIframe"].document.getElementsByName("vehicleIds") ;
	var aTypeId = parent.frames["inIframe"].document.getElementsByName("accountTypeIds") ;
	var aDlrId = parent.frames["inIframe"].document.getElementsByName("dealerIds") ;
	var oExVeh = document.getElementById("exVeh") ;
	var oTypeName = document.getElementById("typeName") ;
	var oDlrId = document.getElementById("dealerId") ;
	
	if(aVehicle) {
		var iLen = aVehicle.length ;
		
		for(var i=0; i<iLen; i++) {
			if(oExVeh.value.length == 0) {
				oExVeh.value = aVehicle[i].value ;
			} else {
				oExVeh.value += "," + aVehicle[i].value ;
			}
		}
		
		oTypeName.value = aTypeId[0].id ;
		oDlrId.value = aDlrId[0].value ;
	}
}

function choosePerson() {
	var typeFlag = "" ;
	var dealerFlag = "" ;
	var chooseFlag = false ;
	var aReturnInfo = document.getElementsByName("salesIds") ;
	
	var iLen = aReturnInfo.length ;
	for(var i=0; i<iLen; i++) {
		if(aReturnInfo[i].checked) {
			chooseFlag = true ;
			infoInsert(aReturnInfo[i].value) ;
		}
	}
	if(!chooseFlag) {
		MyAlert("请选择销售人员!") ;
		return false ;
	} else {
		_hide() ;
	
	}
}

function infoInsert(value) {
	var aReturnInfo = value.split("\||") ;
	var materialId=aReturnInfo[0];
    var materialCode = aReturnInfo[1] ;
	var materialName = aReturnInfo[2] ;
	var groupName = aReturnInfo[3] ;
	
	var oShowTab = parent.frames["inIframe"].document.getElementById("showTab") ;
	var iRowLen = oShowTab.rows.length ;
	oShowTab.insertRow(iRowLen) ;
	oShowTab.rows[iRowLen].setAttribute("id", "tr" + materialId) ;
	
	
	var oCell0 = oShowTab.rows[iRowLen].insertCell(0) ;
	oCell0.nowrap = "nowrap" ;
	oCell0.innerHTML = materialCode + "<input id=\"salesIds\" type=\"hidden\" name=\"salesIds\" value=\"" + materialId + "\" >" ;
	
	var oCell1 = oShowTab.rows[iRowLen].insertCell(1) ;
	oCell1.nowrap = "nowrap" ;
	oCell1.innerHTML = "<input id=\"webone\"  name=\"webone\" datatype='0,is_textarea,30'>" ;
	
	var oCell2 = oShowTab.rows[iRowLen].insertCell(2) ;
	oCell2.nowrap = "nowrap" ;
	oCell2.innerHTML = "<input id=\"webtwo\"  name=\"webtwo\">"
	
	var oCell3 = oShowTab.rows[iRowLen].insertCell(3) ;
	oCell3.nowrap = "nowrap" ;
	oCell3.innerHTML =   "<input  id=\"webthree\" name=\"webthree\"/>";
	
	var oCell4 = oShowTab.rows[iRowLen].insertCell(4) ;
	oCell4.nowrap = "nowrap" ;
	oCell4.innerHTML =  "<input id=\"blog_integ\" name=\"blog_integ\"  datatype='0,is_digit,10'> ";
	
	var oCell5 = oShowTab.rows[iRowLen].insertCell(5) ;
	oCell5.nowrap = "nowrap" ;
	oCell5.innerHTML = "<a href=\"#\" onclick=\"rowDelete(" + materialId + ")\">[删除]</a>" ;
	addListener();
}

function myQuery() {
	var salesIds=parent.frames["inIframe"].document.getElementsByName("salesIds");
	var length=salesIds.length;
	var str="";
	for(var i=0;i<length;i++ ){
		if(i==0){
			str=salesIds[i].value
		}else{
			str+=","+salesIds[i].value;
		}
	}
	url="<%=contextPath%>/sales/usermng/MiniBlogManage/getPersonList.json?COMMAND=1&selectedMan="+str;
	__extQuery__(1) ;
}
</script>
</head>
<body onload="myQuery();"> 
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 经销商实销管理&gt;微博积分管理&gt;顾问查询</div>
	<form id="fm" name="fm" method="post">
	
	<input type="hidden" name="reason" id="reason"/>
		<table class="table_query" border="0" style="display: none;">
			<tr>
				<td width="20%" class="tblopt" align="right"></td>
				<td width="39%"></td>
				<td width="20%" class="tblopt" align="right"></td>
				<td class="table_query_3Col_input">
					<input type="button" class="normal_btn" style="display:none;" onclick="myQuery() ;" value="查 询" id="queryBtn" />&nbsp;
				</td>
			</tr>
		</table>
		<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    	<!--分页 end -->
	</form>
	<br />
	<br />
	<form id="frm">
		<table class="table_query" style="display:inline">
			<tr>
				<td align="center">
					<input type="button" class="normal_btn" id="chooseBtn" name="chooseBtn" value="选 择" onclick="choosePerson() ;" />&nbsp;&nbsp;&nbsp;
					<input type="button" class="normal_btn" id="closeBtn" name="closeBtn" value="关 闭" onclick="_hide() ;" />
				</td>
			</tr>
		</table>
	</form>
<script type="text/javascript">
	document.getElementById("frm").style.display = "none";
	
	var HIDDEN_ARRAY_IDS=['frm'];

	var myPage;
	
	var title = null;
	
	var url = "<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoApplication/getMaterialList.json?COMMAND=1";
	
	var	columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"salesIds\")' />", width:'8%',sortable: false,dataIndex: 'VEHICLE_ID',renderer:myCheckBox},
				{header: "名称", dataIndex: 'NAME', align:'center'},
				{header: "电话", dataIndex: 'MOBILE', align:'center'},
				{header: "邮箱", dataIndex: 'EMAIL', align:'center'}
				
		      ];
		      
	//全选checkbox
	function myCheckBox(value,metaDate,record)
	{
		var personId = record.data.PERSON_ID; 
		var personName = record.data.NAME 
		var moblile = record.data.MOBILE ;
		var email = record.data.EMAIL ;
		
		var sValue = personId + "||" + personName + "||" + moblile + "||" + email  ;
		
		return String.format("<input type='checkbox' name='salesIds' value='" + sValue + "' />");
	}
	
</script>    
</body>
</html>