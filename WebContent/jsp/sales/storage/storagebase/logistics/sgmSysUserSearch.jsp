<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.infodms.dms.common.Constant" %>


<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>用户维护</title>
</head>
<body >
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>基础管理>物流人员管理</div>
<form id="fm" name="fm">
<div class="form-panel">
	<h2><img src="/CQZTDMS/jmstyle/img/search-ico.png" style="margin-bottom: -13px;">查询条件</h2>
<div class="form-body">
<input type="hidden" name="curPage" id="curPage" value="1" />
<input id="DEPT_ID" name="DEPT_ID" type="hidden"/>
<input type="hidden" id="orderCol" name="orderCol" value="" />
<input type="hidden" id="order" name="order" value="" />
<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
<table class="table_query" border="0">
<!-- ----------------------------------------------------------------------------------------------------------------- -->
<tr>
	<td class="right">物流商名称：</td>
	<td>
		<input class="middle_txt" type="text" maxlength="20"  maxlength="30" id="logiName" name="logiName"/>
	</td>
	<td class="right">账号：</td>
	<td>
		<input class="middle_txt" type="text" maxlength="20"  maxlength="30" datatype="1,is_noquotation,30" id="ACNT" name="ACNT"/>
	</td>
	<td class="right">姓名：</td>
	<td>
		<input class="middle_txt" type="text" maxlength="20"  maxlength="30" datatype="1,is_noquotation,30" id="NAME" name="NAME"/>
	</td>
</tr>
<!-- ----------------------------------------------------------------------------------------------------------------- -->
	<tr >
		<td colspan="6" class="table_query_4Col_input" style="text-align: center">
			<input class="u-button u-query" type="button" value="查 询" id="queryBtn" onclick="__extQuery__(1)"/>&nbsp;
			<input class="u-button u-cancel" type="reset" value="重 置"/>
			<input class="u-button u-submit" type="button" value="新 增" onclick="window.location.href='<%=contextPath%>/sales/storage/storagebase/LogisticUserManage/addSgmSysUserInit.do'" value="新 增" />&nbsp;
		</td>
	</tr>
<!-- ----------------------------------------------------------------------------------------------------------------- -->
</table>
</div>
</div>
</form>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</body>
</html>
<script>
	var myPage;
	var url = "<%=request.getContextPath()%>/sales/storage/storagebase/LogisticUserManage/sgmSysUserQuery.json?COMMAND=1";
	
	//设置表格标题
	var title = null;
	
	var columns = [
					{header: "序号", align:'center', renderer:getIndex,width:'5%'},
					{header: "账号", sortable: true, dataIndex: 'ACNT', align:'center'},
					{header: "姓名",  sortable: true, dataIndex: 'NAME', align:'center'},
					{header: "物流商代码", sortable: true, dataIndex: 'LOGI_CODE', align:'center'},
					{header: "物流商名称", sortable: true, dataIndex: 'LOGI_NAME', align:'center'},
					{header: "状态",  sortable: true, dataIndex: 'USER_STATUS', align:'center',renderer:getItemValue},
					{header: "最后操作人", sortable: true, dataIndex: 'UPDATE_NAME', align:'center'},
					{header: "最后更新时间", sortable: true, dataIndex: 'UPDATE_DATE', align:'center'},
					{id:'action',header: "操作", width:70,sortable: false,dataIndex: 'USER_ID',renderer:myLink,width:'8%'}
			      ];

	
	//设置超链接
	function myLink(value,metadata){
	   return String.format(
	         "<a href=\"<%=contextPath%>/sales/storage/storagebase/LogisticUserManage/modfiSgmSysUserInit.do?userId="
					+ value + "\">[查看]</a>");
	}

function requery() {
	document.getElementById('DEPT_NAME').value="";
	document.getElementById('DEPT_ID').value="";
	document.getElementById('ACNT').value="";
	document.getElementById('NAME').value="";
}
</script>