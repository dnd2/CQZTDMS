<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>人员注册</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
<!--
function addSubmit() {
	var url = "<%=contextPath%>/sales/usermng/UserManage/userRegister.do";
	$('fm').action = url ;
	$('fm').submit();
}


//-->
</script>
</head>
<body onload="executeQuery();">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 人员管理 &gt; 人员注册</div>
<form method="post" name="fm" id="fm">
<input type="hidden" name="flagType" value="flagA"/>
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td align="right">业务范围：</td>
			<td align="left">
				<select name="areaId" id="areaId" class="short_sel">
					<option value="">-请选择-</option>
					<c:forEach items="${areaList}" var="list">
						<option value="${list.AREA_ID}"><c:out value="${list.AREA_NAME}"/></option>
					</c:forEach>
				</select><input type="hidden" name="area" id="area"/>
			</td>
		</tr>
		<tr>
			<td align="right">状态：</td>
			<td align="left">
				<select name="status">
					<option value="">请选择</option>
					<option value="99991001" >未提报</option>
					<option value="99991004">驳回</option>
				</select>
			</td>
		</tr>
		<tr>
			<td align="center" colspan="4">
				<input type="hidden" name="areaIds" id="areaIds" value="${areaIds }" />
				<input name="addBtn" id="addBtn" type="button" class="cssbutton" onClick="addSubmit() ;" value="新增">&nbsp;
				<input name="qryBtn" id="queryBtn" type="button" class="cssbutton" onClick="executeQuery();" value="查询">
			</td>
		</tr>
	</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
<!--
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/usermng/UserManage/userSelect.json";
	var title = null;
	var columns = [
				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_SHORTNAME',align:'center'},
				{header: "姓名",dataIndex: 'NAME',align:'center'},
				{header: "性别",dataIndex: 'GENDER',align:'center',renderer:getItemValue},
				{header: "身份证号",dataIndex: 'ID_NO',align:'center'},
				{header: "电子邮件",dataIndex: 'EMAIL',align:'center'},
				{header: "联系电话", dataIndex: 'MOBILE', align:'center'},
				{header: "职位", dataIndex: 'POSITION', align:'center',renderer:getItemValue},
				{header: "入职日期", dataIndex: 'ENTRY_DATE', align:'center'},
				{header: "是否投资人", dataIndex: 'IS_INVESTOR', align:'center',renderer:getItemValue},
				{header: "所属银行", dataIndex: 'BANK', align:'center',renderer:getItemValue},
				//{header: "备注", dataIndex: 'REMARK', align:'center'},
				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "操作", dataIndex: 'REGIST_ID', align:'center',renderer:myLink}
		      ];
		function myLink(value,meta,record){
			return String.format("<a href='#' onclick='updateInit("+record.data.REGIST_ID+")'>[修改]</a>|<a href='#' onclick='submitUser("+record.data.REGIST_ID+")'>[提报]</a>|<a href='#' onclick='deleteUser("+record.data.REGIST_ID+")'>[删除]</a>");
		}
		function updateInit(regist_id){
			location.href="<%=contextPath%>/sales/usermng/UserManage/userUpdateLoad.do?registId="+regist_id;
		}
		function deleteUser(regist_id){
			if(confirm("确认删除?")){
				location.href="<%=contextPath%>/sales/usermng/UserManage/deleteUser.do?registId="+regist_id;
			}
			
		}
		function executeQuery(){
			
			url= "<%=contextPath%>/sales/usermng/UserManage/userSelect.json";
			__extQuery__(1);
		}
		function submitUser(value){
			if(confirm("确认提报?")){
				url= "<%=contextPath%>/sales/usermng/UserManage/submitUser.json?registId="+value;
				makeFormCall(url, submitReturn, "fm") ;
			}
		}
		function submitReturn(json){
			if(json.count>0){
				MyAlert("提报成功！！！");
				url= "<%=contextPath%>/sales/usermng/UserManage/userSelect.json";
				__extQuery__(1);
			}else{
				MyAlert("提报失败！！！");
			}
		}
</script>

</body>
</html>