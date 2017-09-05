<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html
<%String contextPath = request.getContextPath();%>
<jsp:include page="${contextPath}/common/globalVariable.jsp" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7" content="text/html; charset=utf-8" />
<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/mootools.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/my-grid-pager.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/dept_tree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/web/dtree.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/common.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/framecommon/HashMap.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/framecommon/default.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/validate/validate.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/dict.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/dialog_new.js"></script>
<title>部门维护</title>
<script type="text/javascript">
var filecontextPath="<%=contextPath%>";
</script>
<style>
.img {
	border: none
}
</style>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  客户关系管理 &gt; 基础设定 &gt; 部门维护</div>
<form id="fm" name="fm" method="post">

<input id="DEPT_ID" name="DEPT_ID" type="hidden" value="${orgCustomSearch.PARENT_ORG_ID}"/>
<input id="D_ID" name="D_ID" type="hidden" value="${orgCustomSearch.ORG_ID}"/>
<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
<input id="DEPT_CODE" name="DEPT_CODE" type="hidden" value="${orgCustomSearch.ORG_CODE}"/>

<table class="table_query" border="0">
	<tr>
    <td class="table_query_2Col_label_4Letter" nowrap="nowrap">部门代码：</td>
    <td class="table_query_4Col_input" nowrap="nowrap" class="table_query_2Col_input" id="pcode">
    	<input name="ORG_CODE" id="ORG_CODE"  type="text" value="${orgCustomSearch.ORG_CODE}" class="middle_txt" datatype="0,is_null,30"/></td>
    </td>
    <td class="table_query_2Col_label_4Letter" nowrap="nowrap">部门名称：</td>
    <td class="table_query_2Col_input" nowrap="nowrap">
      <input name="DEPT_NNAME" id="DEPT_NNAME"  type="text" value="${orgCustomSearch.ORG_NAME}" class="middle_txt" datatype="0,is_null,30"/></td>
  </tr>
  <tr>
    <td class="table_query_2Col_label_4Letter" nowrap="nowrap">上级部门：</td>
    <td class="table_query_2Col_input" nowrap="nowrap" id="sjbmm">
     <input class="middle_txt" id="DEPT_NAME" onblur="isCloseTreeDiv(event,this,'deptt')" onclick="javascript:showDEPT();"
style="cursor: pointer;" readonly="readonly" datatype="0,is_null,30" name="DEPT_NAME" type="text" value="${orgCustomSearch.PARENT_ORG_NAME}"/>
    </td>
    <td class="table_query_2Col_label_4Letter" nowrap="nowrap">部门状态：</td>
    <td class="table_query_2Col_input" nowrap="nowrap">
      <script type="text/javascript"> genSelBox("DEPT_STATE",<%=Constant.STATUS%>,"${orgCustomSearch.STATUS}",false,"","");</script>
    </td>
  </tr>
  <tr>
    <td class="table_query_2Col_label_4Letter" nowrap="nowrap">组织类型：</td>
    <td class="table_query_2Col_input" nowrap="nowrap">
   	<input name="ORG_TYPE" id="ORG_TYPE"  type="text" value="${orgCustomSearch.ORG_LEVEL}" class="middle_txt" readonly="readonly"/></td>
    </td>
    <td class="table_query_2Col_label_4Letter" nowrap="nowrap"></td>
    <td class="table_query_2Col_input" nowrap="nowrap">
    </td>
  </tr>

    <td colspan="4" align="center">
      <input name="button2" type="button" class="normal_btn" id="queryBtn" onclick="sub()" value="修 改"/>
      <input name="button" type="button" class="normal_btn" onclick="history.back();" value="取 消"/>
    </td>
  </tr>
</table>
</form>

<script type="text/javascript">
var dept_tree_url = "<%=contextPath%>/customerRelationships/baseSetting/OrgCustomSearch/initOrgTree.json?levelType=updateType";
var modify_url = "<%=contextPath%>/customerRelationships/baseSetting/OrgCustomSearch/orgCustomSearchAddOrUpdateSubmit.json";
function sub() {
	MyConfirm("是否确认修改？",editsubmit,"");
}
function editsubmit(){
	sendAjax(modify_url, subBack,'fm') ;
}

function subBack(json){
	if(json.success != null && json.success=='true'){
		document.getElementById("queryBtn").disabled = true;
		MyAlertForFun("更新成功",sendPage);
	}else{
		MyAlert("更新失败！请联系管理员");
		document.getElementById("queryBtn").disabled = false;
	}
}

function sendPage(){
	history.back();
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
 
 function deptpos(id) {
	if(id==0){
		document.getElementById("ORG_TYPE").value = "部门";
	}else{
		document.getElementById("ORG_TYPE").value = "处级";
	}
	var orgid = depttree.aNodes[id].id;
	var orgname = depttree.aNodes[id].name;
	$('DEPT_ID').value = orgid;
	$('DEPT_NAME').value = orgname;
	closeTreeDiv('deptt');
 }
  
</script>
</body>
</html>