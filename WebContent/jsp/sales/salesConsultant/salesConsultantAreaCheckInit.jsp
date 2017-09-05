<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>销售顾问申请</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
<!--
function clrTxt(txtId){
	document.getElementById(txtId).value = "";
}

function checkIt(value) {
	if(submitForm("form1")) {
		var headIds = document.getElementsByName("headIds");
		var addFlag = false;
		for(var i=0; i<headIds.length; i++){
			if(headIds[i].checked){
				addFlag = true;
				break ;
			}
		}  
		
		if(addFlag) {
			MyConfirm("确认操作?", checkSure, [value]) ;
		} else {
			MyAlert("请选择需要审核的信息!") ;
			return false ;
		}
	}
}

function checkSure(value) {
	document.getElementById("passIt").disabled = true ;
	document.getElementById("returnIt").disabled = true ;
	
	document.getElementById("status").value = value ;
	var desc = document.getElementById("desc").value ;
	
	var headIds = document.getElementsByName("headIds");
	var headIdsStr = "" ;
	for(var i=0; i< headIds.length; i++){
		if(headIds[i].checked){
			if(headIdsStr.length != 0) {
				headIdsStr += "," + headIds[i].value ;
			} else {
				headIdsStr = headIds[i].value ;
			}
		}
	}
	document.getElementById("descr").value = desc ;
	document.getElementById("heIds").value = headIdsStr ;
	
	var url = "<%=contextPath%>/sales/salesConsultant/SalesConsultant/areaChecks.json";
	makeFormCall(url, checkTip, "fm") ;
}

function checkTip(json) {
	var subFlag = json.subFlag ;
	
	if(subFlag == 'success') {
		MyAlert("操作成功!") ;
		
		__extQuery__(1);
	} else {
		MyAlert("操作失败!") ;
	}
	
	clrTxt("heIds") ;
	clrTxt("desc") ;
	
	document.getElementById("passIt").disabled = false ;
	document.getElementById("returnIt").disabled = false ;
}
//-->
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 销售顾问管理 &gt; 销售顾问申请</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td align="right">业务范围：</td>
			<td align="left">
				<select name="areaId" id="areaId" class="short_sel">
					<option value="">-请选择-</option>
					<c:forEach items="${areaList}" var="list">
						<option value="${list.AREA_ID}"><c:out value="${list.AREA_NAME}"/></option>
					</c:forEach>
				</select><input type="hidden" name="area" id="area"/>
			</td>
		</tr>
		<tr>
			<td align="right">经销商：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="dealerCode" value="" readonly="readonly" id="dealerCode"/>
				<input name="button3" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','dealerId','true', '${orgId}')" value="..." />
				<input type="hidden" name="dealerId" id="dealerId" value="" />
            	<input class="cssbutton" type="button" value="清空" onclick="clrTxt('dealerCode');clrTxt('dealerId');"/>
            </td>
		</tr>
		<tr>
			<td align="right">销售顾问姓名：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="salesConName" id="salesConName" value="" />
			</td>
		</tr>
		<tr>
			<td align="center" colspan="2">
				<input type="hidden" name="areaIds" id="areaIds" value="${areaIds }" />
				<input type="hidden" name="status" id="status" value="" />
				<input type="hidden" name="heIds" id="heIds" />
				<input type="hidden" name="descr" id="descr" value="" />
				<input name="qryBtn" id="queryBtn" type="button" class="cssbutton" onClick="__extQuery__(1);" value="查询">
			</td>
		</tr>
	</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
</form>
<form  name="form1" id="form1">
<table class="table_query" align="center">
	<tr>
		<td>
			<div align="right">审核意见：</div>
		</td>
		<td>
			<textarea rows="3" cols="50" name="desc" id="desc"></textarea>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="2">
			<input type="button" class="normal_btn" onclick="checkIt(<%=Constant.SALES_CONSULTANT_STATUS_PASS %>);" value="通过" id="passIt" />
			<input type="button" class="normal_btn" onclick="checkIt(<%=Constant.SALES_CONSULTANT_STATUS_RETURN %>);" value="驳回" id="returnIt" />
		</td>
	</tr>
</table>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
<!--
	document.form1.style.display = "none";
	var HIDDEN_ARRAY_IDS=['form1'];
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/salesConsultant/SalesConsultant/areaCheckQuery.json";
	var title = null;
	var columns = [
{id:'check',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"headIds\");' />", width:'6%',sortable: false,dataIndex: 'ID',renderer:myCheckBox},
				{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "姓名",dataIndex: 'NAME',align:'center'},
				{header: "性别",dataIndex: 'SEX',align:'center',renderer:getItemValue},
				{header: "年龄",dataIndex: 'AGE',align:'center'},
				{header: "学历",dataIndex: 'ACADEMIC_RECORDS',align:'center',renderer:getItemValue},
				{header: "从事汽车行业年份", dataIndex: 'TRADEYEAR', align:'center'},
				{header: "从事长安汽车行业年份", dataIndex: 'CHANATRADEYEAR', align:'center'},
				{header: "联系电话", dataIndex: 'TEL', align:'center'},
				{header: "申请原因", dataIndex: 'REASON', align:'center'},
				{header: "操作",sortable: false, dataIndex: 'ID', align:'center',renderer:myLink}
		      ];

	function myLink(value,meta,record){
		return String.format("<a href='#' onclick='checkInit(\"" + value + "\")'>[审核]</a>");
	}

	function checkInit(value){ 
		$('fm').action= "<%=contextPath%>/sales/salesConsultant/SalesConsultant/areaCheckDtlInit.do?headId=" + value ;
		$('fm').submit();
	}
	
	function myCheckBox(value,metaDate,record){
		return String.format("<input type=\"checkbox\" name='headIds' value='" + value + "' />");
	}
--></script>
<!--页面列表 end -->
</body>
</html>