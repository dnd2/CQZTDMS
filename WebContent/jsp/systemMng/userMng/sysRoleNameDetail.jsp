<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.po.TcRolePO"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
	TcRolePO rolePO = (TcRolePO)request.getAttribute("rolePO");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>系统角色维护</title>
	
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<link href="<%=contextPath %>/style/dtree1.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=contextPath %>/js/web/dtree.js"></script>
	<script type="text/javascript">
		var filecontextPath="<%=contextPath%>";
		var path = "<%=contextPath%>";
		var tree_root_id = {"tree_root_id" : ""};
		var subStr = "funlist";
		var addNodeId;
		var tgjzw = "<%=request.getAttribute("gjzw") %>";
		var roleId = "<%=request.getAttribute("funList") %>";
		var tree_url = "<%=contextPath%>/sysmng/sysrole/ActionSysRole/initFunTree.json";
		var saveUrl = "<%=contextPath%>/sysmng/sysrole/ActionSysRole/sysRolemodfi.json";
		var roleSearch = "<%=contextPath%>/sysmng/sysrole/ActionSysRole/querySysRoleInit.do";
		
		function pageload() {
			$('#dtree').html("<img src='"+path+"/img/tree/loading.gif' />载入中...");
			if(tgjzw != null && tgjzw != "") {
				var arrs = tgjzw.split(",");
				for(var i=0; i < arrs.length; i++) {
					$("#"+arrs[i]).checked = true;
				}
			}
			sendAjax(tree_url,createTree,'myfm');
		}
	
		PPPID = "10090102,10090101";
		function setsele2() {
			a.chkclickByFunId(PPPID);
		}
	
		function selType(obj,sgmCode,dealerCode) {
			if(obj.value == sgmCode) {
				$('#dtree').html("<img src='"+path+"/img/tree/loading.gif' />载入中...");
				sendAjax(tree_url,createTree,'myfm');
			}else if(obj.value == dealerCode) {
				$('#dtree').html("<img src='"+path+"/img/tree/loading.gif' />载入中...");
				sendAjax(tree_url,createTree,'myfm');
			}
		}
	
		function createTree(reobj) {
			$('#dtree').empty();
		 	a = new dTree('a','dtree','true','true');
			a.config.folderLinks=true;
			a.config.closeSameLevel=false;
			a.delegate=function (id)
			{
				addNodeId = a.aNodes[id].id;
			    var nodeID = a.aNodes[id].id;
			    for(var n=0; n<a.aNodes.length; n++) {
			    	if(a.aNodes[n].id.contains(addNodeId+"01")) {
			    		return;
			    	}
			    }
			}
			var funlistobj = reobj[subStr];
			var funcCode,parFuncId,funcId,funcName;
			for(var i=0; i<funlistobj.length; i++) {
				parFuncId = funlistobj[i].parFuncId;
				funcId = funlistobj[i].funcId;
				funcName = funlistobj[i].funcName;
				if(parFuncId == 0) { //系统根节点
					a.add(funcId,"-1",funcName);
				} else if(funcId.length<=10){
					a.add(funcId,parFuncId,funcName);
				}
			}
			a.draw();
			a.closeAll();
			if($('#ROLE_TYPE').val() == "<%=rolePO.getRoleType() %>") {
				setsele();
			}	
			setsele2();
		}
	
		function setsele() {
			var myf = "";
			if(roleId == "") {
				return false;
			}
			myf = roleId.split(","); 
			for(var i=0; i<myf.length; i++) {
				a.chkclickByFunId(myf[i]);
			}
		}
		function getFunList() {
			var flist = new Array();
			for(var n=0; n<a.aNodes.length; n++) {
				var tid = a.aNodes[n].id;
				var tsrc = $('#ckk' + a.obj + a.aNodes[n]._ai)[0].src;
				var tsrc0 = $('#i' + a.obj + a.aNodes[n]._ai)[0].src;
				if(iscontains(tsrc0, "/leaf.gif")  && iscontains(tsrc,"/checked.gif")) {
					flist.push(tid);
				}
			}
			return flist;
		}
		
		function saveRole() {
			$('#FUNS').val(getFunList().toString());
			makeNomalFormCall(saveUrl, addRoleCallBack,'myfm');
		}
	
		function addRoleCallBack(json) {
			if(json.st != null && json.st == "succeed") {
				_hide();
			}else if(json.st == "roleDesc_error") {
				showError('ermsg','erdiv','ROLE_DESC','角色名称重复,请重新输入!',170);
			}
		}
		
		$(document).ready(function(){pageload();});
	</script>
</head>
<body >
	<div class="wbox">
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 用户管理 &gt; 系统角色维护</div>
		<form action="" id="myfm">
			<input type="hidden" id="ROLE_NAME" name="ROLE_NAME" />
			<input type="hidden" id="ROLE_ID" name="ROLE_ID" value="<%=rolePO.getRoleId() %>"/>
			<input type="hidden" id="FUNS" name="FUNS" />
			<table class="table_query">
				<tr>
					<td>角色类别：</td>
					<td>
						<script type="text/javascript"> 
							genSelBox("ROLE_TYPE_SEL",<%=Constant.SYS_USER%>,"<%=String.valueOf(rolePO.getRoleType()) %>",false,"","onchange='dobj.selType(this,<%=Constant.SYS_USER_SGM%>,<%=Constant.SYS_USER_DEALER%>)' disabled");
						</script>
						<input type="hidden" name="ROLE_TYPE" id="ROLE_TYPE" value="<%=String.valueOf(rolePO.getRoleType()) %>" />
					</td>
					<td rowspan="4" valign="top">
				  		<div id='dtree' class="dtree" style="border:1px solid #5E7692;width: 300px; height: 200px; overflow:auto"></div>
				  	</td>
				</tr>
				<tr>
					<td>角色代码：</td>
					<td><%=rolePO.getRoleName() %></td>
				</tr>
				<tr>
					<td>角色名称：</td>
					<td>
						<input maxlength="30" datatype="0,is_null,30" class="middle_txt" type="text" id="ROLE_DESC" name="ROLE_DESC" value="<%=rolePO.getRoleDesc() %>" />
					</td>
				</tr>
				<tr>
					<td>状态：</td>
					<td>
						<script type="text/javascript">genSelBox("ROLE_STATUS",<%=Constant.STATUS%>,<%=rolePO.getRoleStatus() %>,false,"","");</script>
					</td>
				</tr>
			</table>
		</form>
		<br />
		<table width="100%">
			<tr>
				<td align="center">
					<input class="u-button" type="button" value="保 存" name="queryBtn" onclick="saveRole()" />
					<input class="u-button" type="button" value="关 闭" name="queryBtn" onclick="_hide()"/>
				</td>
			</tr>
		</table>
		<div id="erdiv" style="position: absolute; top:-1000px; background: #FDFFCE; height: 17px; border:1px solid #FFBA43; display: none;">
		<img style="margin-top: 1px; margin-left: 2px;" src="<%=contextPath%>/img/exclamation.gif" />
		<span id="ermsg" style="color: red; position: absolute; margin-top: 1px;"></span></div>
	</div>
</body>
</html>
