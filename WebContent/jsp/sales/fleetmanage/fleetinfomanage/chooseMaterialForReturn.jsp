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

function chooseVehicle() {
	var typeFlag = "" ;
	var dealerFlag = "" ;
	var chooseFlag = false ;
	
	var aReturnInfo = document.getElementsByName("materialIds") ;
	
	
	
	var iLen = aReturnInfo.length ;
	
		
	for(var i=0; i<iLen; i++) {
		if(aReturnInfo[i].checked) {
			chooseFlag = true ;
			infoInsert(aReturnInfo[i].value) ;
		}
	}
	//选择了车型
<!--	var selectVchile=parent.frames["inIframe"].document.getElementById("selectVehiles").value ;-->
<!--	if(chooseFlag){-->
<!--		for(var i=0; i<iLen; i++) {-->
<!--			if(aReturnInfo[i].checked) {-->
<!--			 var vchile_id=aReturnInfo[i].value.split('||')[0];-->
<!--				if(selectVchile==null){-->
<!--	 				selectVchile=""+vchile_id;-->
<!--	 			}else{-->
<!--	 				selectVchile+=","+vchile_id;-->
<!--	 			}-->
<!--			}-->
<!--		}-->
<!--		document.getElementById("selectVehiles").value=selectVchile;-->
<!--	}-->
	
	if(!chooseFlag) {
		MyAlert("请选择车型配置!") ;
		return false ;
	} else {
		//parent.frames["inIframe"].document.getElementById("applyRetrun").style.display = "inline" ;
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
	oCell0.innerHTML = materialCode + "<input id=\"materialIds\" type=\"hidden\" name=\"materialIds\" value=\"" + materialId + "\" >" ;
	
	var oCell1 = oShowTab.rows[iRowLen].insertCell(1) ;
	oCell1.nowrap = "nowrap" ;
	oCell1.innerHTML = materialName ;
	
	var oCell2 = oShowTab.rows[iRowLen].insertCell(2) ;
	oCell2.nowrap = "nowrap" ;
	oCell2.innerHTML = groupName + "<input id=\"groupIds\" type=\"hidden\" name=\"groupNames\" value=\"" + materialId + "\" >";
	
	var oCell3 = oShowTab.rows[iRowLen].insertCell(3) ;
	oCell3.nowrap = "nowrap" ;
	oCell3.innerHTML =   "<input  id=\"amounts\" name=\"amounts\" style='width:80%;' datatype='0,is_digit,10' /> <font color='red'>*</font>";
	
	var oCell4 = oShowTab.rows[iRowLen].insertCell(4) ;
	oCell4.nowrap = "nowrap" ;
	oCell4.innerHTML =  "<input id=\"describes\" name=\"describes\"  style='width:90%;' >";
	
	var oCell5 = oShowTab.rows[iRowLen].insertCell(5) ;
	oCell5.nowrap = "nowrap" ;
	oCell5.innerHTML = "<a href=\"#\" onclick=\"rowDelete(" + materialId + ")\">[删除]</a>" ;
	addListener();
}

function myQuery() {
	var sVin = document.getElementById("materialName").value ;
	var sDlyNo = document.getElementById("materialCode").value ;
	var materialIds=parent.frames["inIframe"].document.getElementsByName("materialIds");
	var length=materialIds.length;
	var str="";
	for(var i=0;i<length;i++ ){
		if(i==0){
			str=materialIds[i].value
		}else{
			str+=","+materialIds[i].value;
		}
	}
	url="<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoApplication/getMaterialList.json?COMMAND=1&selectedVehicle="+str;
	__extQuery__(1) ;
}
</script>
</head>
<body onload="myQuery();"> 
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 集团客户管理&gt;集团客户信息管理&gt;车型选择</div>
	<form id="fm" name="fm" method="post">
	
	<input type="hidden" name="reason" id="reason"/>
		<table class="table_query" border="0">
			<tr>
				<td  width="20%" class="tblopt" align="right">车型编码：</td>
				<td  width="39%">
					<input type="text" id="materialCode" name="materialCode" class="middle_txt" size="20"   />
				</td>
				<td width="20%" class="tblopt"><div align="right">车型名称：</div></td>
				<td width="39%">
      				<input type="text" id="materialName" name="materialName" class="middle_txt" size="20"   />
    			</td>
    			
			</tr>
			<tr>
				<td width="20%" class="tblopt" align="right"></td>
				<td width="39%"></td>
				<td width="20%" class="tblopt" align="right"></td>
				<td class="table_query_3Col_input">
					<input type="button" class="normal_btn" onclick="myQuery() ;" value="查 询" id="queryBtn" />&nbsp;
					<input type="button" class="normal_btn" id="closeBtn" name="closeBtn" value="关 闭" onclick="_hide() ;" />
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
					<input type="button" class="normal_btn" id="chooseBtn" name="chooseBtn" value="选 择" onclick="chooseVehicle() ;" />
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
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"materialIds\")' />", width:'8%',sortable: false,dataIndex: 'VEHICLE_ID',renderer:myCheckBox},
				{header: "车型编码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "车型名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "车系", dataIndex: 'GROUP_NAME', align:'center'}
				
		      ];
		      
	//全选checkbox
	function myCheckBox(value,metaDate,record)
	{
		var materialId = record.data.MATERIAL_ID; 
		var materialCode = record.data.MATERIAL_CODE 
		var groupName = record.data.GROUP_NAME ;
		var materialName = record.data.MATERIAL_NAME ;
		
		var sValue = materialId + "||" + materialCode + "||" + materialName + "||" + groupName  ;
		
		return String.format("<input type='checkbox' name='materialIds' value='" + sValue + "' />");
	}
	
</script>    
</body>
</html>