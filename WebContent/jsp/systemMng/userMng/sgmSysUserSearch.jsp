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
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<title>用户维护</title>
	<script type="text/javascript">
		var filecontextPath="<%=contextPath%>";
		var dept_tree_url = "<%=contextPath%>/sysmng/usemng/SgmSysUser/initOrgTree.json";
		var url = "<%=request.getContextPath()%>/sysmng/usemng/SgmSysUser/sgmSysUserQuery.json?COMMAND=1";
			
			//设置表格标题
		var title = null;
			
		var columns = [
			{header: "序号", align:'center', renderer:getIndex,width:'5%'},
			{id:'action',header: "操作", width:70,sortable: false,dataIndex: 'USER_ID',renderer:myLink,width:'8%'},
			{header: "账号", sortable: true, dataIndex: 'ACNT', align:'center',width:'8%'},
			{header: "姓名",  sortable: true, dataIndex: 'NAME', align:'center',width:'8%'},
			{header: "职位", sortable: true, dataIndex: 'POSE_NAME', align:'center',width:'55%'},
			{header: "状态",  sortable: true, dataIndex: 'USER_STATUS', align:'center',orderCol:"USER_STATUS",renderer:getItemValue,width:'8%'},
			{header: "最后操作人", sortable: true, dataIndex: 'UPDATE_NAME', align:'center'},
			{header: "最后更新时间", sortable: true, dataIndex: 'UPDATE_DATE', align:'center'}
	     ];
			
		//设置超链接
		function myLink(value,metadata){
		   return String.format("<a href=\"<%=contextPath%>/sysmng/usemng/SgmSysUser/modfiSgmSysUserInit.do?userId=" + value + "\">[修改]</a>");
		}

		$(function() {
			__extQuery__(1);
		});
	</script>
</head>

<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
	&nbsp;当前位置： 系统管理 &gt; 权限管理&gt; 用户维护</div>
	<form id="fm" name="fm">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input id="DEPT_ID" name="DEPT_ID" type="hidden"/>
		<input type="hidden" id="orderCol" name="orderCol" value="" />
		<input type="hidden" id="order" name="order" value="" />
		<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
		<div class="form-panel">	
			<h2><img src="/CQZTDMS/jmstyle/img/search-ico.png" style="margin-bottom: -13px;">查询条件</h2>
			<div class="form-body">
				<table class="table_query" border="0">
						<tbody><tr>
							<td class="right">部门：</td>
							<td>
								<input class="middle_txt" id="DEPT_NAME" onblur="isCloseTreeDiv(event,this,'deptt')" onclick="showDEPT()" name="DEPT_NAME" type="text"/></td>
							<td class="right">账号：</td>
							<td>
								<input class="middle_txt" type="text" maxlength="30" datatype="1,is_noquotation,30" id="ACNT" name="ACNT"/>
								<span id="rolenameerr"></span>
							</td>
							<td class="right">姓名：</td>
							<td>
								<input class="middle_txt" type="text" maxlength="30" datatype="1,is_noquotation,30" id="NAME" name="NAME"/>
							</td>
						</tr>
						<tr>
							<td colspan="6" style="text-align: center">
								<input class="u-button u-query" type="button" value="查 询" id="queryBtn" onclick="__extQuery__(1)"/>
								<input class="u-button u-cancel" type="reset" value="重 置"/>
								<input class="u-button u-submit" type="button" value="新 增" onclick="window.location.href='<%=contextPath%>/sysmng/usemng/SgmSysUser/addSgmSysUserInit.do'" value="新 增" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
</div>	
</body>
</html>
