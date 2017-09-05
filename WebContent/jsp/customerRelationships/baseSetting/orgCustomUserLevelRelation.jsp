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
<title>延期授权人员维护</title>
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
	&nbsp;当前位置：  客户关系管理 &gt; 基础设定 &gt; 延期授权人员维护</div>
<form id="fm" name="fm">
<input type="hidden" name="curPage" id="curPage" value="1" />
<input id="DEPT_ID" name="DEPT_ID" type="hidden"/>
<input type="hidden" id="orderCol" name="orderCol" value="" />
<input type="hidden" id="order" name="order" value="" />
<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
	<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<th colspan="5"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />延期授权人员维护</th>
		
		<tr>
			<td align="right" nowrap="true">用户名：</td>
			<td align="left" nowrap="true">
				<input type="text" id="name" name="name"/>
			</td>
			<td align="right" nowrap="true">部门：</td>
			<td align="left" nowrap="true">
				<input class="middle_txt" id="DEPT_NAME" onblur="isCloseTreeDiv(event,this,'deptt')" onclick="javascript:showDEPT();"
					style="cursor: pointer;" readonly="readonly" name="DEPT_NAME" type="text"/>
				<input class="normal_btn" type="button" value="重 置" onclick="requery()"/>
			</td>
			<td  align="center">
				<input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="__extQuery__(1);" />
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
	var url = "<%=request.getContextPath()%>/customerRelationships/baseSetting/OrgCustomUserLevelRelation/queryOrgCustomUserLevelRelation.json";

	var title = null;
	
	var columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{header: "用户名",dataIndex: 'NAME',align:'center'},
				{header: "帐号", dataIndex: 'ACNT', align:'center'},
				{header: "客户组织", dataIndex: 'ORGNAME', align:'center'},
				{header: "延期授权", dataIndex: 'ORGLEVEL', align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ORGID',renderer:myLink}
		      ];


	function myLink(value,metadata,record){
		if(record.data.ORGLEVEL == null){
			return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='updateEvent(\""+ record.data.USERID +"\")' value='修改'/>");
		}else{
			return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='updateEvent(\""+ record.data.USERID +"\")' value='修改'/><input name='detailBtn' type='button' class='normal_btn' onclick='deleteEvent(\""+ record.data.USERID +"\")' value='注销'/>");
		}		
	}
	function updateEvent(userid){
		window.location.href='<%=contextPath%>/customerRelationships/baseSetting/OrgCustomUserLevelRelation/orgCustomUserLevelRelationUpdate.do?userid='+userid ;
	}
	function deleteEvent(userid){
		makeNomalFormCall('<%=contextPath%>/customerRelationships/baseSetting/OrgCustomUserLevelRelation/orgCustomUserLevelRelationDelete.json?userid='+userid,delBack,'fm','');
	}
	function delBack(json){
		if(json.success != null && json.success=='true'){
			MyAlertForFun("注销成功",sendPage);
		}else{
			MyAlert("注销失败！请联系管理员");
		}
	}
	
	//页面跳转：
	function sendPage(){
		window.location.href = "<%=contextPath%>/customerRelationships/baseSetting/OrgCustomUserLevelRelation/orgCustomUserLevelRelationInit.do";
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
