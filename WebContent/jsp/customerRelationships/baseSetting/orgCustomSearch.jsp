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
<meta http-equiv="X-UA-Compatible" content="IE=7" content="text/html; charset=utf-8" />
<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
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
<script type="text/javascript" src="<%=contextPath %>/js/jslib/dialog_new.js"></script>
<title>部门维护</title>
<script>
	   var filecontextPath="<%=contextPath%>";
</script>
<style>
.img {
	border: none
}
</style>
</head>

<body onload="__extQuery__(1)">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
	&nbsp;当前位置：  客户关系管理 &gt; 基础设定 &gt; 部门维护</div>
<form id="fm" name="fm">
<input type="hidden" name="curPage" id="curPage" value="1" />
<input id="DEPT_ID" name="DEPT_ID" type="hidden"/>
<input type="hidden" id="orderCol" name="orderCol" value="" />
<input type="hidden" id="order" name="order" value="" />
<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
<table class="table_query" border="0">
	<tr>
		<td nowrap="nowrap" align="center">上级部门：
			<input class="middle_txt" id="DEPT_NAME" onblur="isCloseTreeDiv(event,this,'deptt')" onclick="javascript:showDEPT();"
		style="cursor: pointer;" readonly="readonly" name="DEPT_NAME" type="text"/>
		</td>
		<td nowrap="nowrap" align="center">部门代码：
			<input class="middle_txt" id="orgCode" name="orgCode" type="text"/>
		</td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="center" colspan="2">
			<input class="normal_btn" type="button" value="查 询" id="queryBtn" onclick="__extQuery__(1)"/>
			<input class="normal_btn" type="button" value="重 置" onclick="requery()"/>
			<input class="normal_btn" type="button" value="新 增" onclick="window.location.href='<%=contextPath%>/customerRelationships/baseSetting/OrgCustomSearch/orgCustomSearchAddOrUpdate.do'" value="新 增" />
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


var dept_tree_url = "<%=contextPath%>/customerRelationships/baseSetting/OrgCustomSearch/initOrgTree.json?levelType=all";

var myPage;
function __extQuery__(page){
	entThisPage = page;
	$("queryBtn").disabled = "disabled";
	showMask();
	sendAjax(url+(url.lastIndexOf("?") == -1?"?":"&")+"curPage="+page,callBack,'fm');
}
	var url = "<%=request.getContextPath()%>/customerRelationships/baseSetting/OrgCustomSearch/queryOrgCustomSearch.json?COMMAND=1";

	var title = null;
	
	var columns = [
					{header: "序号", align:'center', renderer:getIndex,width:'7%'},
					{header: "上级部门", dataIndex: 'PARENT_ORG_NAME', align:'center'},
					{header: "部门代码", dataIndex: 'ORG_CODE', align:'center', orderCol:"ORG_CODE"},
					{header: "部门名称",  dataIndex: 'ORG_NAME', align:'center'},
					{header: "状态",  dataIndex: 'STATUS', align:'center', orderCol:"STATUS",renderer:getItemValue},
					{id:'action',header: "操作", width:70,sortable: false,dataIndex: 'ORG_ID',renderer:myLink}
			      ];

	function myLink(value,metadata){
	   return String.format(
	          "<a href=\"<%=contextPath%>/customerRelationships/baseSetting/OrgCustomSearch/orgCustomSearchAddOrUpdate.do?orgid="
					+ value + "\">[查看]</a>");
	}

function requery() {
	$('DEPT_NAME').value="";
	$('DEPT_ID').value="";
}

 function createDeptTree(reobj) {
	var orglistobj = reobj[subStr];
	var orgId,parentOrgId,orgName,orgCode,orgId;

	for(var i=0; i<orglistobj.length; i++) {
		orgId = orglistobj[i].ORG_ID;
		orgName = orglistobj[i].ORG_NAME;
		orgCode = orglistobj[i].ORG_CODE;
		parentOrgId = orglistobj[i].PARENT_ORG_ID;

		if(parentOrgId == -1) {
			depttree.add(orgId,"-1",orgName,orgCode);
		} else {
			depttree.add(orgId,parentOrgId,orgName,orgCode);
			//depttree.add(orgId+"_",orgId,"loading...","","","",depttree.icon.loading,"","");
		}
	}
	depttree.draw();
 }

 function createDeptNode(reobj) {
	var orglistobj = reobj[subStr];
	var orgId,parentOrgId,orgName,orgCode;
	depttree.remove(addDeptNodeId+"_");
	for(var i=0; i<orglistobj.length; i++) {
		orgId = orglistobj[i].ORG_ID;
		orgName = orglistobj[i].ORG_NAME;
		orgCode = orglistobj[i].ORG_CODE;
		parentOrgId = orglistobj[i].PARENT_ORG_ID;
		depttree.add(orgId,addDeptNodeId,orgName,orgCode);
		//depttree.add(orgId+"_",orgId,"loading...","","","",depttree.icon.loading,"","");
	}
	depttree.draw();
 }
</script>