<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<link href="<%=request.getContextPath()%>/style/dtree_default.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/dtree1.js"></script>
</head>
	
<body onunload='javascript:destoryPrototype()' onload="createModelTree()" >

<div style="float:right;">
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="FUNC_IDS" name="FUNC_IDS" value="${FUNC_IDS}" />
		<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
		
		<table class="table_query" >
			<tr>
				<td class="table_query_label" nowrap="nowrap">功能名称：</td>
				<td class="table_query_input"   nowrap="nowrap">
					<input class="middle_txt" type="text" id="funcName" name="funcName"/>
				</td>
				<td class="table_query_label" nowrap="nowrap">
					&nbsp;<input class="normal_btn" id="queryBtn" type="button" value="查 询" onclick="__extQuery__(1);"/>&nbsp;
					&nbsp;<input class="normal_btn" type="reset" value="重 置" />&nbsp;
					&nbsp;<input class="normal_btn" type="button" onclick="_hide();" value="关 闭" />&nbsp;
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	
		<table width="100%">
			<tr>
				<td class="table_query_4Col_input"
					style="text-align: center">
					<input name="button2" type="button" class="normal_btn"	onclick="add()" value="添 加" />
				</td>
			</tr>
		</table>
		
</div>
		
<script>
	var myPage;
	//子页面默认查询
	var url = "<%=contextPath%>/sysusermng/sysuserinfo/SysShortcutKeyManager/querySystemFunctionList.json?command=1";
		
	//设置表格标题
	var title= null;
	//设置列名属性
	var columns = [
					{header: "<input type='checkbox' id='selectAll' name='selectAll' onclick='checkselectAllBox(this)'  />", dataIndex: 'funcId',width:"50px",renderer:myLink},
					{header: "功能名",width:"50px", dataIndex: 'funcName'}
			      ];

	function myLink(value,meta,record){
		return "<input type='checkbox' name='selectOne' onclick='checkselectAllBox(this)' value=\""+record.data.funcId+"\" id=\""+record.data.funcId+"\"'  />"
	}

	var objarr = document.getElementsByName('selectOne');
	
	function checkselectAllBox(obj) {
		if(obj.id == "selectAll" && obj.checked == true) {
			for(var i=0; i<objarr.length; i++) {
				objarr[i].checked = true;
			}
		} else if(obj.id == "selectAll" && obj.checked == false) {
			for(var i=0; i<objarr.length; i++) {
				objarr[i].checked = false;
			}
		} else {
			if(obj.checked == false) {
				$('selectAll').checked = false;
			} else {
				var st = true;
				for(var i=0; i<objarr.length; i++) {
					if(objarr[i].checked == false) {
						st = false;
						break;
					}
				}
				st ? $('selectAll').checked = true : "";
			}
		}
	}

	function add() {
		var myDealer = null;
		if($('FUNC_IDS').value != "") {
			myDealer = $('FUNC_IDS').value.split(",");
		}
		var temp = new Array();
		for(var i=0; i<objarr.length; i++) {
			if(objarr[i].checked == true) {
				temp.push(objarr[i].id);
			}
		}
		if(myDealer != null) {
			for(var i=0; i<myDealer.length; i++) {
				temp.push(myDealer[i]);
			}
		}
		parentContainer.showFunction(temp.toString());
		_hide();
	}
</script>
</body>
</html>