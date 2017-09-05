<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<title>市场处理工单查询</title>
<script language="javaScript">
var url = "<%=contextPath%>/report/feedbackmngreport/WorkSummary/query.json";
var title = null;
var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "大区", width:'15%', dataIndex: 'ORG_NAME'},
				{header: "服务中心", width:'15%', dataIndex: 'DEALER_NAME'},
				{header: "申请日期", width:'15%', dataIndex: 'ORDER_DATE',renderer:formatDate},
				{header: "车型", width:'15%', dataIndex: 'MODEL_CODE'},
				{header: "车架号", width:'7%', dataIndex: 'VIN'},
				{header: "客户姓名", width:'15%', dataIndex: 'CTM_NAME'},
				{header: "投诉类型", width:'15%', dataIndex: 'COMP_TYPE'},
				{header: "涉及金额", width:'15%', dataIndex: 'MONEY'},
				{header: "审批通过人（区域服务经理姓名）", width:'15%', dataIndex: 'AUDIT_BY_QY'},
				{header: "审批通过人（大区经理姓名）", width:'15%', dataIndex: 'AUDIT_BY_DQ'},
				{header: "客户服务部审批情况（审批通过人姓名）", width:'15%', dataIndex: 'AUDIT_BY_KF'}
	      ];

//格式化时间为YYYY-MM-DD
function formatDate(value,meta,record) {
	if (value==""||value==null) {
		return "";
	}else {
		return value.substr(0,10);
	}
}


function doInit()
{
   loadcalendar();
}

function toClearDealers()
{
	$("dealerId").value="";
	$("dealerCode").value="";
}
function toClearOrgs()
{
	$("orgId").value="";
	$("orgCode").value="";
}

function mydownload()
{
	if(submitForm("fm")==true)
	{		
		fm.action="<%=contextPath%>/report/feedbackmngreport/WorkSummary/download.json";
		fm.submit();
	}
}
</script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;
    &nbsp;当前位置：  报表查询  &gt; 市场处理工单查询</div>

<form name="fm" id="fm"  method="post">
<table class="table_edit">
	<tr>
	    <td align="right" width="20%">日期：</td>
	    <td align="left" width="30%">
	        <input type="text" name="startDate" id="t1" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  hasbtn="true" callFunction="showcalendar(event, 't1', false);"/> 至  
	        <input type="text" name="endDate" id="t2" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>         
        </td>
	    <td align="right" width="20%">大区：</td>
	    <td align="left" width="30%">
	        <input type="hidden"  name="orgId" size="15" value=""  id="orgId" class="middle_txt" datatype="1,is_noquotation,75" />
		    <input type="text"  readonly="readonly"  name="orgCode" size="15" value=""  id="orgCode" class="middle_txt" datatype="1,is_noquotation,75" />
		    <input name="orgbu"  id="orgbu" type="button" class="mark_btn" onclick="showOrg('orgCode','orgId','false')" value="..." />
		    <input type="button" name="orgbuclearbutton" id="orgbuclearbutton" class="cssbutton" value="清除" onClick="toClearOrgs();"/>
        </td>		
	</tr>	
	<tr>
	    <td align="right" width="20%">服务中心：</td>
	    <td align="left" width="30%">
	        <input type="hidden" id="dealerId" name="dealerId"/>
	        <input type="text" class="middle_txt" readonly="readonly" name="dealerCode" size="15" id="dealerCode" value="" />
			<input type="button"  class="mini_btn" onclick="showOrgDealer('dealerCode','dealerId','true');" value="..." />
			<input type="button"  class="cssbutton" value="清除" onClick="toClearDealers();"/>
        </td>
	    <td align="right" width="20%">审批状态：</td>
	    <td align="left" width="30%">
	        <script type="text/javascript">
	            genSelBoxExp("status",<%=Constant.MARKET_BACK_STATUS_TYPE%>,"",true,"min_sel","","false",'');
		    </script>
        </td>		
	</tr>	
	<tr>
	    <td colspan="2" align="right"><input type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">&nbsp;</td>
	    <td colspan="2" align="left"><input type=button class="cssbutton" onClick="mydownload();" value="下载"></td>	   	
	</tr>
</table>
<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
</div>
</body>
</html>
