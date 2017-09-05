<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
    int yes = Constant.IF_TYPE_YES;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		loadcalendar();   //初始化时间控件
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
</script>

<title>经销商个人信贷维护</title>
</head>
<body onload='__extQuery__(1);'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;经销商个人信贷维护</div>
<form id="fm" name="fm" method="post">
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>
	<input type="hidden" name="dtlIds" id="dtlIds" />
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;经销商选择</div>
<table class="table_query" border="0">
<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商代码：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="DEALER_CODE" maxlength="30" datatype="1,is_noquotation,30" id="DEALER_CODE" type="text" class="middle_txt" /></td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商简称：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="DEALER_NAME" maxlength="30" datatype="1,is_noquotation,75" id="DEALER_NAME" type="text" class="middle_txt" /></td>
			<td class="table_query_4Col_input" style="text-align: center">
			<input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1)" value="查 询 " id="queryBtn" /> &nbsp; 
		</td>
	</tr>
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<br />
<br />
<br />
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;信贷信息录入</div>
<table class="table_query" border="0">
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">信贷类型：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
			<c:forEach items="${ls}" var="ls">
			<input type="checkbox" value="${ls.CODE_ID}" id="MORTGAGETYPE" name="MORTGAGETYPE"/>${ls.CODE_DESC}
			</c:forEach>
			&nbsp;
			<font color="red">*</font>
		</td>
	</tr>
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">信贷日期：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"> 
		<input name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10"  hasbtn="true" callFunction="showcalendar(event, 't1', false);" />&nbsp;<font color="red">*</font>
		</td>
	</tr>

	<tr align="center">
		<td colspan="4" class="table_query_4Col_input" style="text-align: center">
			<input name="queryBtn1" type="button" class="normal_btn" onclick="addSub()" value="保 存" id="queryBtn1" /> &nbsp; 
	
			<input name="button2" type="button" class="normal_btn" onclick="back()" value="返 回" /> &nbsp;
		</td>
	</tr>
</table>
</form>
<script><!--

var sTheId = "${theId}" ;
aGroupDtlId = sTheId.split(",") ;
var aGroupDtlId = new Array() ;




var myPage;
var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/addNewDealerCreditInfo.json";
var title= null;

var columns = [
				{id:'check',header: "选择", width:'6%',sortable: false,dataIndex: 'DEALER_ID',renderer:myCheckBox},
				{header: "经销商代码",width:'10%',   dataIndex: 'DEALER_CODE'},
				{header: "经销商简称", width:'20%', dataIndex: 'DEALER_SHORTNAME'},
				{header: "经销商个人信贷类型", width:'10%', dataIndex: 'TYPE'},
				{header: "信贷日期", width:'10%', dataIndex: 'CREDIT_DATE'}
				
			  ];



function add()
{
	window.location.href='<%=contextPath%>/sysmng/dealer/DealerInfo/addNewDealerCredit.do';
}

function clrTxt(txtId){
   	document.getElementById(txtId).value = "";
}

function myCheckBox(value,metaDate,record){
	
	
	var theValue = value  ;
	
	var iLen = aGroupDtlId.length ;
	
	for(var i=0; i<iLen; i++) {
		if(aGroupDtlId[i] == value)
			return String.format("<input type=\"checkbox\" name='theIds' value='" + theValue + "' onclick='chkChg(this);' checked='checked'/>");
	}
	
	return String.format("<input type=\"checkbox\" name='theIds' value='" + theValue + "' onclick='chkChg(this);' />");
}

function chkChg(obj) {
	 var iLen = aGroupDtlId.length ;
	 
	 var aDtl = obj.value.split(",") ;
	 var sTheId = aDtl[0] ;
	
	 
	 if(obj.checked) {
		 aGroupDtlId.push(sTheId) ;
	 } else {
		 for(var i=0; i<iLen; i++) {
			 if(aGroupDtlId[i] == sTheId) {
				 aGroupDtlId.splice(i, 1) ;			
			 }
		 }
	 }
}
function addSub() {
	if(aGroupDtlId) {
		var iLen = aGroupDtlId.length ;
		
		if(!iLen) {
			MyAlert("请选择经销商！") ;
			
			return false ;
		} 
	} else {
		MyAlert("请选择经销商！") ;
		
		return false ;
	}

	if(fm.MORTGAGETYPE[0].checked||fm.MORTGAGETYPE[1].checked||fm.MORTGAGETYPE[2].checked)   { 
		} else{ MyAlert("请选择信贷类型！") ;
		 
		 return false ;
		 }

	if(fm.startDate.value==""){
		MyAlert("请选择日期！") ;
		 
		 return false ;
		}
		 MyConfirm("确认提交？", addSure) ;
	 
}


function addSure() {
	
	 document.getElementById("dtlIds").value = aGroupDtlId.join() ;
	 var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/addNewDealerCreditDo.json" ;
	 makeFormCall(url, returnResult, "fm") ;
}

function returnResult(json) {
	 var sFlag = json.flag ;
	 
	 if(sFlag == "success") {
		 MyAlert("操作成功！") ;
		 
		back() ;
	 } else {
		 MyAlert("操作失败！") ;
	 }
}
function back(){
	location.href="<%=contextPath%>/sysmng/dealer/DealerInfo/DealerCreditQuery.do";
	
}
--></script>
</div>
</body>
</html>