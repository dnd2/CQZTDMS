<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.util.CommonUtils;"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>监控工时维护</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔预授权&gt;预授权维修项目维护</div>
  <form name="fm" id="fm">
    <div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
  <input type="hidden" name="ID" id="ID" value="<%=request.getAttribute("MODELID")%>"/>
  <input id="LABOUR_OPERATION_NO" name="LABOUR_OPERATION_NO" type="hidden" />
  <input id="LABOUR_OPERATION_NAME" name="LABOUR_OPERATION_NAME" type="hidden" />
  <table class="table_query">
       <tr>            
        <td style="text-align:right">工时代码：</td>            
        <td>
			<input class="middle_txt" id="laborcode" value="" name="laborcode" type="text" />
        </td>
        <td style="text-align:right">工时名称：</td>
        <td><input type="text" name="laborname" id="laborname" value=""  class="middle_txt" /></td>   
       </tr>
       <tr>
        <td colspan="4" style="text-align:center">
        	    <input  id="queryBtn" class="normal_btn" type="button" name="queryBtn" value="查询"  onclick="__extQuery__(1);"/>
				<input class="normal_btn" type="button" name="button1" value="关闭"  onclick="_hide();"/>
        </td>
       </tr>       
 	</table>
 	</div>
 	</div>
 	 <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
  </div>
<script type="text/javascript" >
var myPage;
var url = "<%=contextPath%>/claim/authorization/ClaimLaborWatchMain/claimLaborSelect11.json";
var title = null;
var columns = [
			{header: "选择",sortable: false,dataIndex: 'LABOUR_CODE',align:'center',renderer:myLink},
			{header: "车型组",sortable: false,dataIndex: 'WRGROUP_CODE',align:'center'},
			{header: "工时代码",sortable: false,dataIndex: 'LABOUR_CODE',align:'center'},
			{header: "工时名称",sortable: false,dataIndex: 'CN_DES',align:'center'},
			{header: "工时数(小时)",sortable: false,dataIndex: 'LABOUR_HOUR',align:'center'},
			{header: "工时单价(元)",sortable: false,dataIndex: 'LABOUR_QUOTIETY',align:'center'}
	      ];

function myLink(value,metadata,record){
	return String.format(
			"<input type='hidden' id='"+record.data.LABOUR_CODE+"' name='"+record.data.LABOUR_CODE+"' value='"+record.data.CN_DES+"'/>"+
			"<input type='radio' id='"+record.data.LABOUR_CODE+"' name='"+record.data.LABOUR_CODE+"' value='"+record.data.LABOUR_CODE+"' onclick='selbyid(this);'/>"
			);
}
/* function returnBefore()
{   var laborCode = 'LABOUR_OPERATION_NO';
var laborName = 'LABOUR_OPERATION_NAME';
var code = document.getElementById("LABOUR_OPERATION_NO").value;
var name = document.getElementById("LABOUR_OPERATION_NAME").value;
if(code && code.length > 0)
	parentDocument.getElementById(laborCode).value = document.getElementById("LABOUR_OPERATION_NO").value;
if(name && name.length > 0)
   	parentDocument.getElementById(laborName).value = document.getElementById("LABOUR_OPERATION_NAME").value;	
} */
function selbyid(obj){
	  $('#LABOUR_OPERATION_NO')[0].value = obj.value;
	  $('#LABOUR_OPERATION_NAME')[0].value = document.getElementById(obj.value).value;
	  
	  var laborCode = 'LABOUR_OPERATION_NO';
	  var laborName = 'LABOUR_OPERATION_NAME';
		var code = document.getElementById("LABOUR_OPERATION_NO").value;
	var name = document.getElementById("LABOUR_OPERATION_NAME").value;
	
	var parentDocument = __parent() ;
	
		if(code && code.length > 0)
			parentDocument.document.getElementById(laborCode).value = document.getElementById("LABOUR_OPERATION_NO").value;
		if(name && name.length > 0)
		   	parentDocument.document.getElementById(laborName).value = document.getElementById("LABOUR_OPERATION_NAME").value;	
		
	_hide();
}
</script>  
</form>
</body>
</html>

