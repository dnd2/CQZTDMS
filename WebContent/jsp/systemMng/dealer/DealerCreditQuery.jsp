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
	function toClearDealers(){
		}
</script>

<title>经销商个人信贷维护</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;经销商个人信贷维护</div>
<form id="fm" name="fm">
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>
<table class="table_query" border="0">
	<tr>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">选择大区：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<input type="text" id="orgCode" style="width:100px" name="orgCode" value="" size="15" class="middle_txt" readonly="readonly" />
				<input name="obtn" id="obtn"  class="mini_btn" type="button" value="&hellip;" onclick="showOrg('orgCode','orgId' ,'true','${orgId}');"/>
				<input type="hidden" id="orgId" name="orgId" />
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('orgCode');clrTxt('orgId');" />
			</td>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">选择省份：</td>
      		<td class="table_query_4Col_input" nowrap="nowrap">
				<input type="text"  readonly="readonly"  name="regionCode" size="15" value=""  id="regionCode" class="middle_txt" />
				<input name="regbu"  id="regbu" type="button" class="mark_btn" onclick="showRegion('regionCode','regionId','true')" value="..." />
				<input type="hidden"  name="regionId" size="15" value=""  id="regionId" />
				<input type="button"  class="cssbutton" value="清除" onclick="clrTxt('regionCode');clrTxt('regionId');"/>
			</td>
			</tr>
			<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商代码：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="DEALER_CODE" maxlength="30" datatype="1,is_noquotation,30" id="DEALER_CODE" type="text" class="middle_txt" /></td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商简称：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="DEALER_NAME" maxlength="30" datatype="1,is_noquotation,75" id="DEALER_NAME" type="text" class="middle_txt" /></td>
	</tr>
	<tr>
	<td class="table_query_3Col_label_6Letter" nowrap="nowrap">信贷类型：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><script type="text/javascript">
					genSelBoxExp("MORTGAGETYPE",<%=Constant.MORTGAGE_TYPE%>,"-1",true,"short_sel",'',"false",'');
				</script></td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">信贷日期：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"> 
			<input name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);" />
			&nbsp;至&nbsp;
	         <input name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);" />
		</td>
	
	</tr>

	<tr align="center">
		<td colspan="4" class="table_query_4Col_input" style="text-align: center">
			<input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> &nbsp; 
			 <input type="button" class="normal_btn" id="downloadIt" name="downloadIt" onclick="downloadFunc() ;" value="下 载" />&nbsp; 
			<input name="button2" type="button" class="normal_btn" onclick="add()" value="维 护" /> &nbsp;
		</td>
	</tr>
</table>
</form>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />	
<script>
var myPage;
var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/individualTrustQuery.json";
var title= null;

var columns = [
				{header: "大区", width:'20%', dataIndex: 'ROOT_ORG_NAME'},
				{header: "省份", width:'20%', dataIndex: 'REGION_NAME'},
				{header: "经销商代码",width:'10%',   dataIndex: 'DEALER_CODE'},
				{header: "经销商简称", width:'20%', dataIndex: 'DEALER_SHORTNAME'},
				{header: "个人信贷业务类型", width:'10%', dataIndex: 'CODE_DESC'},
				{header: "信贷日期", width:'10%', dataIndex: 'CREDIT_DATE'}
				
			  ];



function add()
{
	window.location.href='<%=contextPath%>/sysmng/dealer/DealerInfo/addNewDealerCredit.do';
}

function clrTxt(txtId){
   	document.getElementById(txtId).value = "";
}

function downloadFunc() {
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/dealerCreditDownload.json" ;
	document.fm.action = url ;
	document.fm.submit() ;
}
</script>
</div>
</body>
</html>