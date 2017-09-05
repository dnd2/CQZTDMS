<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant" %>
 	<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.TmQuickFuncPO"%>
<%
	String contextPath = request.getContextPath();
	List<TmUserInfoBean> funcList = (List<TmUserInfoBean>)request.getAttribute("funcsList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<%@page import="com.infodms.dms.bean.TmUserInfoBean"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
<jsp:include page="${path}/common/jsp_head_new.jsp" />
<title>快捷功能自定义</title>
<script type="text/javascript">

</script>
</head>

<body onload="pageload();loadcalendar();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 个人信息管理  &gt; 个人信息管理   &gt; 快捷功能自定义 </div>
	<form id="fm" name="fm" method="post">
	<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
	<input id="FUNC_IDS" name="FUNC_IDS" type="hidden" value="${FUNC_IDS }"/>

		<table border="0" style="border:1px solid #DAE0EE;width: 100%">
			<tr>
				<td colspan="6" style="width: 100%">
					<img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;快捷功能设置&nbsp;
					<input class="normal_btn" id="addDealerbtn" type="button" value="新增功能" onclick="addDealer('<%=contextPath%>')"/>&nbsp;	
				</td>		
			</tr>
		</table>
		<table class="table_list" style="border-bottom:1px solid #DAE0EE;" id="showFunction" >
			<tr>
				<th>序号</th>
				<th>功能名称</th>
				<th>设定顺序值</th>
				<th>操作</th>
			</tr>
		</table>
 
		<table class="table_info" >
			<tr>	
				<td colspan="6" align="center">
					<input class="normal_btn" type="button" value="保 存" onclick="confirmAdd();" name="addBtn" id="addBtn"/>
				</td>	
			</tr>
		</table>
 	</form>
 </div>
 <script type="text/javascript">
	function pageload() {
		showFunction($('FUNC_IDS').value);
	}

	function showFunction(funcIds) {
		delPalRow();
		$('FUNC_IDS').value = funcIds;
		if($('FUNC_IDS').value != null && $('FUNC_IDS').value != "") {
			sendAjax(getDealerByIdsUrl,backshowdealer,'fm');
		}
	}
	
	//删除行
	function delPalRow() {
		var addTable = $('showFunction');
		var rowss = addTable.rows.length;
		for(dels; rowss > dels; rowss--) { 
			addTable.deleteRow(rowss-1);
		}
	}

	//根据快捷菜单ID查询数据
	var getDealerByIdsUrl = "<%=contextPath%>/sysusermng/sysuserinfo/SysShortcutKeyManager/queryShortcutKeyHistoryInfo.json";

	//得到查询的数据，并根据查询出来的数据进行表格重新生成
	function backshowdealer(obj) {
		var list = obj.ps;
		objarr = null;
		objarr = new Array();
		if(list != null) {
			for(var i=0; i<list.length; i++) {
				var order = '';
				<% if(funcList != null && !"".equals(funcList)){ %>
					<% for(int j = 0 ; j < funcList.size() ; j ++ ){ %>
						<% TmUserInfoBean funcBean = (TmUserInfoBean)funcList.get(j); %>
						var tmp = <%=funcBean.getFuncId() %>;
						if(tmp == list[i].funcId){
							order = <%=funcBean.getFuncOrder() %>;
						}
					<% } %>
				<% } %>
				objarr.push(list[i].funcId);
				addPlanRow(list[i].funcId,list[i].funcName,order);
			}
		}
	}
	
	function addPlanRow(funcId,funcName,order)
	{	
		var addTable = $('showFunction');
		var rows = addTable.rows;
		var length = rows.length;
		var insertRow = addTable.insertRow(length);
		insertRow.className = "table_list_row1";
		insertRow.id = funcId;
		insertRow.insertCell(0);
		insertRow.insertCell(1);
		insertRow.insertCell(2);
		insertRow.insertCell(3);
		addTable.rows[length].cells[0].innerHTML =  "<td class=table_info_label nowrap=nowrap align='center'>"+(length)+"</td>";
		addTable.rows[length].cells[1].innerHTML =  "<td class=table_info_label nowrap=nowrap align='center'>"+funcName+"</td>";
		addTable.rows[length].cells[2].innerHTML =  "<td class=table_info_label nowrap=nowrap align='center'><input type='hidden' value='" +funcId +"' name='FUNCID'/><input type='text' class='short_txt' value='"+order+"' name='FUNC_ORDER'/></td>";
		addTable.rows[length].cells[3].innerHTML =  "<td class=table_info_label nowrap=nowrap align='center'><a href='javascript:void(0)' target='_self' onclick='deleteRow("+(length)+");'>[删除]</a></td>";
	}

	function deleteRow(row){
		var addTable = $('showFunction');
		var rows = addTable.rows;
		addTable.deleteRow(row);
		for(var i = row;i<addTable.rows.length;i++){
			addTable.rows[i].cells[3].innerHTML = "<td class=table_info_input nowrap=nowrap align='center'><a href='javascript:void(0)' target='_self' onclick='deleteRow("+i+");'>[删除]</a></td>";
		}
	}

	//弹出子窗体进行功能菜单查询
	function addDealer(path) {
		setDealerIds();
		OpenHtmlWindow(path+"/sysusermng/sysuserinfo/SysShortcutKeyManager/querySystemFunctionListInit.do?FUNC_IDS="+$('FUNC_IDS').value,800,450);
	}
	
	var dels = 1;

	//把已经画出来的表格的功能ID得新进行设定并传递到后台再次查询
	function setDealerIds() {
		var dealers = new Array();
		var addTable = $('showFunction');
		var rowss = addTable.rows.length;
		for(dels; rowss > dels; rowss--) {
			dealers.push(addTable.rows[rowss-1].id);
		}
		if(dealers.length > 0) {
			$('FUNC_IDS').value = dealers.toString();
		} else {
			$('FUNC_IDS').value = "";
		}
	}
	
	function confirmAdd(){
		var addTable = $('showFunction');
		var rowss = addTable.rows.length;
		var index = 5;
		if(rowss-1 > index){
			MyAlert("快捷菜单不能超过5个！");
		}else{
			setDealerIds();
			if(submitForm('fm')){
				sendAjax('<%=request.getContextPath()%>/sysusermng/sysuserinfo/SysShortcutKeyManager/saveOrUpdateShortcutKeyHistoryInfo.json',showResult,'fm');
			}
		}
			
		
	}

	function showResult(json){
		if(json.ACTION_RESULT == '1'){
			goBack();
		}else {
			MyAlert("保存快捷菜单失败！");
		}
	}

	function goBack(){
		window.location.href = "<%=contextPath%>/sysusermng/sysuserinfo/SysShortcutKeyManager/queryShortcutKeyHistoryInfoInit.do";
	}
 </script>
</body>

</html>