<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商与售后绑定关系</title>
<script type="text/javascript">

function downloadFunc() {
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/dealerInfoDownload.json" ;
	document.fm.action = url ;
	document.fm.submit() ;
}
</script>
</head>

<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;销售经销商与售后经销商绑定</div>
<form method="post" id="fm" name="fm">
<input name="dealerCode" type="hidden" id="dealerCode"/>
<input name="dealerName" type="hidden" id="dealerName"/>
<table class="table_query" border="0">
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">销售经销商：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
			<input name="xs_dealerCode" type="hidden" id="xs_dealerCode" class="middle_txt" value=""  />
      		<input name="xs_dealerName" type="text" id="xs_dealerName" class="middle_txt" value="" />
      		<input name="xs_dealerId" type="hidden" id="xs_dealerId" class="middle_txt" value="" />
            <input name="dlbtn" type="button" class="mini_btn" onclick="showOrgDealer('xs_dealerCode','xs_dealerId','false', '', 'true','','10771001','xs_dealerName');" value="..." />
            <input type="button" class="normal_btn" onclick="txtClr('xs_dealerId');txtClr('xs_dealerCode');txtClr('xs_dealerName');" value="清 空" id="clrBtn" /> 
		</td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">售后经销商：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
			<input id="sh_dealerCode"  name="sh_dealerCode" type="hidden"/>
            <input name="sh_dealerName" type="text" id="sh_dealerName" class="middle_txt" value="" />
            <input name="sh_dealerId" type="hidden" id="sh_dealerId" value="" />
            <input name="button1" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('sh_dealerCode','sh_dealerId','false','','true','','10771002','sh_dealerName');" value="..." />        
            <input name="button2" type="button" class="normal_btn" onClick="txtClr('sh_dealerId');txtClr('sh_dealerCode');txtClr('sh_dealerName');" value="清除"/>
		</td>
	</tr>
	
	<tr align="center">
		<td colspan="4" class="table_query_4Col_input" style="text-align: center">
			<input name="queryBtn" type="button" class="normal_btn" onclick="doQuery()" value="查 询" id="queryBtn" /> &nbsp; 
			<input name="button2" type="button" class="normal_btn" onclick="add()" value="添 加" /> &nbsp;
			<input type="button" name="btnQuery" id="btnQuery"  value="导出"  class="normal_btn" onClick="expotData();" >
		</td>
	</tr>
</table>
</form>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />	
<script>
var myPage;
var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/queryBindingRelationInfo.json";
var title= null;
var columns = [ 
                {header: "销售经销商代码",width:'20%',   dataIndex: 'XS_DEALER_CODE'},
				{header: "销售经销商名称", width:'20%', dataIndex: 'XS_DEALER_NAME'},
				{header: "销售经销商公司", width:'20%', dataIndex: 'XS_COMPANY_NAME'},
				{header: "售后经销商代码", width:'20%', dataIndex: 'SH_DEALER_CODE'},
				{header: "售后经销商名称", width:'20%', dataIndex: 'SH_DEALER_NAME'},
				{header: "售后经销商公司", width:'20%', dataIndex: 'SH_COMPANY_NAME'},
				{header: "关系是否有效", width:'20%', dataIndex: 'STATUS',dataIndex: 'STATUS',renderer:showZt},
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'RELATION_ID',renderer:myLink}
			  ];
function showZt(status){
	if(status == '10011001'){
		return "有效";
	}else{
		return "无效";
	}
}	  
function myLink(relationId){
	var link = "<a href=\"<%=contextPath%>/sysmng/dealer/DealerInfo/editBindingRelationInfo.do?relationId="+relationId+"\">[修改]</a>"; 
    return String.format(link);
} 
function add()
{
	window.location.href='<%=contextPath%>/sysmng/dealer/DealerInfo/addBindingRelationInfo.do';
}

function txtClr(value){
	document.getElementById(value).value = "";
}

function expotData(){
	fm.action="<%=contextPath%>/report/dmsReport/Application/expotDealerRelationData.do";
    fm.submit();
}

//查询
function doQuery() {
    //执行查询
    __extQuery__(1);
}
</script>
</body>
</html>
