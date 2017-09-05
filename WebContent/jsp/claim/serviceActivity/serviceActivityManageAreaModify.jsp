<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动发布范围管理</title>
<% String contextPath = request.getContextPath(); %>
</head>

<body>
	<div class="navigation">&nbsp; 
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动区域管理
	</div>
<br/>
<form method="post" name="fm" id="fm">
<input type="hidden" name="type" value="${type}">
<input type='hidden'  name=activityId  id='activityId'  value='<%=request.getAttribute("activityId")%>' >
	<table class="table_list" id="add_tab">
		<tr>
			<th colspan="6" align="left">
				<img class="nav" src="<%=contextPath %>/img/subNav.gif" />执行区域列表：
				<input type="button" class="normal_btn" value="新增"  onclick="addArea();" />
			</th>
		</tr>
		<tr class="table_list_row2">
			<td>区域代码</td>
			<td>区域名称</td>
			<td>操作</td>
		</tr>
		<c:forEach var="area" items="${list}" varStatus="st">
			<tr class="table_list_row${st.index%2+1}">
				<td>${area.orgCode}
					<input type="hidden" name="org_code" value="${area.orgCode}"/>
				</td>
				<td>${area.orgName}</td>
				<td><input type="button" class="normal_btn" value="删除" onclick="delRow1(this,${area.orgId});"/></td>
			</tr>
		</c:forEach>
	</table>
<br/>

<table class="table_edit">
	<tr>
		<td align="center">
			<input type="button" class="normal_btn" value="确定" onclick="sureHandler();">	
			&nbsp;&nbsp;
			<input type="button" class="normal_btn" value="返回" onclick="goBack();">
		</td>
	</tr>
</table>

</form>
<script type="text/javascript" >
	function goBack(){
		location= "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageDealer/serviceActivityManageDealerInit.do";
	}
	function addArea(){
		var url = '<%=contextPath%>/claim/serviceActivity/ServiceActivityManageDealer/areaSelUrlInit.do' ;
		OpenHtmlWindow(url,700,390);
	}
	function setArea(ids,codes,names){
		var tab = $('add_tab');
		var arr = document.getElementsByName('org_code');
		for(var i=0;i<ids.length;i++){
			var flag = true ;
			for(var j=0;j<arr.length;j++){
				if(codes[i].value==arr[j].value)
					flag = false ;
			}
			if(flag){
				var length = tab.rows.length;
				var insertRow = tab.insertRow(length);
				insertRow.insertCell(0);
				insertRow.insertCell(1);
				insertRow.insertCell(2);
				if(length%2==1)
					insertRow.className = 'table_list_row2' ;
				else 
					insertRow.className = 'table_list_row1' ;
				var curRow = tab.rows[length];
				curRow.cells[0].innerHTML = codes[i].value ;
				curRow.cells[1].innerHTML = names[i].value ;
				curRow.cells[2].innerHTML = '<input type="button" value="删除" class="normal_btn" onclick="delRow(this);"/><input type="hidden" name="org_code" value='+codes[i].value+'><input type="hidden" name="org_id" value='+ids[i].value+'>' ;
			}
		}
	}
	//新生成列的删除方法
	function delRow(obj){
		$('add_tab').deleteRow(obj.parentElement.parentElement.rowIndex);
	}
	//已经存在数据库中的数据删除方法
	function delRow1(obj,orgId){
		var idx = obj.parentElement.parentElement.rowIndex;
		var url = '<%=contextPath%>/claim/serviceActivity/ServiceActivityManageDealer/deleteArea.json?orgId='+orgId+'&idx='+idx ;
		makeNomalFormCall(url,delCallback,'fm');
	}
	function delCallback(json){
		if(json.flag){
			$('add_tab').deleteRow(json.idx);
			MyAlert('删除已存在的数据成功!');
		}else{
			MyAlert('删除失败!');
		}
	}
	function sureHandler(){
		if($('add_tab').rows.length==2){
			MyAlert('请至少维护一个区域!');
			return ;
		}else{
			$('fm').action = '<%=contextPath%>/claim/serviceActivity/ServiceActivityManageDealer/areaSure.do' ;
			$('fm').submit();
		}
	}
</script>
</body>
</html>