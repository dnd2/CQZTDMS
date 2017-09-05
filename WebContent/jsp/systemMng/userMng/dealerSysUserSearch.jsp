<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/globalVariable.jsp" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
	<script type="text/javascript" src="<%=contextPath%>/js/jslib/mootools.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/my-grid-pager.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/dept_tree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/validate/validate.js"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/web/dtree.js"></script>
	<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/dict.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/dialog_new.js"></script>
<title>经销商用户维护</title>
<style>
.img {
	border: none
}
</style>
</head>

<body onload="__extQuery__(1)">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 用户管理 &gt; 经销商用户维护</div>
<form id="fm" name="fm">
<input type="hidden" name="curPage" id="curPage" value="1" />
<input id="DEPT_ID" name="DEPT_ID" type="hidden"/>
<input type="hidden" id="orderCol" name="orderCol" value="" />
<input type="hidden" id="order" name="order" value="" />
<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
<table class="table_query" border="0">
<tr>
		<td class="table_query_2Col_label_4Letter" nowrap="nowrap">部门：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
			<input class="middle_txt" id="DEPT_NAME" onblur="isCloseTreeDiv(event,this,'deptt')" onclick="showDEPT()"
style="cursor: pointer;" name="DEPT_NAME" type="text"/>
		</td>
		<td class="table_query_2Col_label_5Letter" nowrap="nowrap">&nbsp;</td>
		<td class="table_query_2Col_input" nowrap="nowrap">&nbsp;</td>
	</tr>
	<tr>	
		<td class="table_query_2Col_label_4Letter" nowrap="nowrap">用户名：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
			<input class="middle_txt" type="text" maxlength="30" datatype="1,is_noquotation,30" id="EMP_NUM" name="EMP_NUM"/>
		</td>
		<td class="table_query_2Col_label_5Letter" nowrap="nowrap">姓名：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
			<input class="middle_txt" type="text" maxlength="30" datatype="1,is_noquotation,30" id="NAME" name="NAME"/>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="4">
			<input class="normal_btn" type="button" value="查 询" id="queryBtn" onclick="__extQuery__(1)"/>
			<input class="normal_btn" type="button" value="重 置" onclick="requery()"/>
			<input class="normal_btn" type="button" value="新 增" onclick="window.location.href='<%=contextPath%>/sysmng/usemng/DealerSysUser/addDealerSysUserInit.do'" value="新 增" />
		</td>
	</tr>
</table>
</form>
<div id="_page" style="margin-top:15px;display:none;"></div>
<div id="myGrid" ></div>
<div id="myPage" class="pages"></div>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</body>
</html>
<script>
var dept_tree_url = "<%=contextPath%>/sysmng/usemng/DealerSysUser/initOrgTree.json";

var myPage;
function __extQuery__(page){
	entThisPage = page;
	$("queryBtn").disabled = "disabled";
	showMask();
	submitForm('fm') ? sendAjax(url+(url.lastIndexOf("?") == -1?"?":"&")+"curPage="+page,callBack,'fm') : ($("queryBtn").disabled = "",removeMask());
}
	var url = "<%=request.getContextPath()%>/sysmng/usemng/DealerSysUser/dealerSysUserQuery.json?COMMAND=1";
	
	var title = null;
	
	var columns = [
					{header: "序号", align:'center', renderer:getIndex,width:'7%'},
					{header: "用户名", dataIndex: 'empNum', align:'center'},
					{header: "职位",  dataIndex: 'addr', align:'center'},
					{header: "姓名",  dataIndex: 'name', align:'center'},
					{header: "状态",  dataIndex: 'userStat', align:'center', orderCol:"USER_STATUS",renderer:getItemValue},
					{id:'action',header: "操作", width:70,sortable: false,dataIndex: 'userId',renderer:myLink}
			      ];

	//设置超链接
	function myLink(value,metadata){
	   return String.format(
	          "<a href=\"<%=contextPath%>/sysmng/usemng/DealerSysUser/modfiDealerSysUserInit.do?userId="
					+ value + "\">[查看]</a>");
	}

function requery() {
	$('DEPT_NAME').value="";
	$('DEPT_ID').value="";
	$('EMP_NUM').value="";
	$('NAME').value="";
}
</script>