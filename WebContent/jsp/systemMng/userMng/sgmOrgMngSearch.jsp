<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=7" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<link href="/CQZTDMS/style/dtree1.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/CQZTDMS/js/web/dept_tree.js"></script>
	<script type="text/javascript" src="/CQZTDMS/js/web/dtree.js"></script>
<!-- <link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/mootools.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/framecommon/HashMap.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath %>/js/jslib/my-grid-pager.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/web/dept_tree.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath%>/js/web/dtree.js"></script>
<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
<script type="text/javascript" src="<%=contextPath%>/js/dict.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/jslib/dialog_new.js"></script> -->
<title>公司组织维护</title>
<script>
	   var filecontextPath="<%=contextPath%>";
</script>
</head>

<body onload="__extQuery__(1)">
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
	&nbsp;当前位置： 系统管理 &gt;组织管理&gt; 公司组织维护</div>
	<form id="fm" name="fm">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input id="DEPT_ID" name="DEPT_ID" type="hidden"/>
		<input type="hidden" id="orderCol" name="orderCol" value="" />
		<input type="hidden" id="order" name="order" value="" />
		<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
		<div class="dtree" id="deptt"></div>
		<div class="form-panel">
			<h2>公司组织维护</h2>
			<div class="form-body">
				<table class="table_query" border="0">
					<tr>
						<td nowrap="nowrap" align="center">上级部门：
							<input class="middle_txt" id="DEPT_NAME"  onclick="dt.initDeptTree();"
				style="cursor: pointer;" readonly="readonly" name="DEPT_NAME" type="text"/>
						</td>
						<td>
							部门编号：<input class="middle_txt" type="text" name="deptCode"/>
						</td>
						<td>
							组织类型：
							<select class="u-select" name="deptLevel">
								<option value="">==请选择==</option>
								<option value="<%=Constant.DUTY_TYPE_COMPANY %>">公司</option>
								<option value="<%=Constant.DUTY_TYPE_LARGEREGION %>">大区</option>
								<option value="<%=Constant.DUTY_TYPE_SMALLREGION %>">小区</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="center" align="left" colspan="3">
							<input class="u-button u-query" type="button" value="查 询" id="queryBtn" onclick="__extQuery__(1)"/>
							<input class="u-button" type="button" value="重 置" onclick="requery()"/>
							<input class="u-button u-submit" type="button" value="新 增" onclick="window.location.href='<%=contextPath%>/sysmng/orgmng/SgmOrgMng/sgmOrgMngAddInit.do'" value="新 增" />
						</td>
					</tr>
				</table>
			</div>
		</div>
		
	</form>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>	
<script>
var dept_tree_url = "<%=contextPath%>/sysmng/orgmng/SgmOrgMng/initOrgTree.json";

var myPage;
/* function __extQuery__(page){
	entThisPage = page;
	$("#queryBtn")[0].disabled = "disabled";
	showMask();
	sendAjax(url+(url.lastIndexOf("?") == -1?"?":"&")+"curPage="+page+getPageSizeParam(),callBack,'fm');
} */
	var url = "<%=request.getContextPath()%>/sysmng/orgmng/SgmOrgMng/sgmOrgMngQuery.json?COMMAND=1";

	var title = null;
	
	var columns = [
					{header: "序号", align:'center', renderer:getIndex,width:'7%'},
					{id:'action',header: "操作", width:70,sortable: false,dataIndex: 'orgId',renderer:myLink},
					{header: "上级部门", dataIndex: 'parentOrgName', align:'center'},
					{header: "部门代码", dataIndex: 'orgCode', align:'center', orderCol:"org_code"},
					{header: "部门名称",  dataIndex: 'orgName', align:'center'},
					{header: "状态",  dataIndex: 'status', align:'center', orderCol:"status",renderer:getItemValue}
			      ];

	function myLink(value,metadata){
	   return String.format(
	          "<a href=\"<%=contextPath%>/sysmng/orgmng/SgmOrgMng/modfiSgmOrgMngInit.do?deptId="
					+ value + "\">[修改]</a>");
	}

function requery() {
	$('#DEPT_NAME')[0].value="";
	$('#DEPT_ID')[0].value="";
}
</script>
</body>
</html>