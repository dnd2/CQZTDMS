<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件人员类型设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script language="javascript" type="text/javascript">
	$(function(){
		__extQuery__(1);
	});
	
	var myPage;
	
	var url = "<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/partUserOrderSearch.json";
	
	var title = null;
	
	var columns = [
				{header: "序号", dataIndex: 'SEQUENCE_ID',align:'center', width: '20%', renderer:getIndex},
				{header: "人员名称", dataIndex: 'NAME', align:'center',  width: '30%',},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center',  width: '30%',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ID', align:'center', width: '20%', renderer:myLink }
		      ];
	     
	
	//设置超链接
	function myLink(value,meta,record)
	{
		var orderType = record.data.ORDER_TYPE
		var disableValue = <%=Constant.STATUS_DISABLE%>;
			return String.format("<a href=\"#\" onclick='deleteData(\""+orderType+"\")'>[删除]");
			
	}
	
	//返回checkbook
	function deleteData(orderType){
     	var url = '<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/deleteOrderType.json?orderType='+orderType;
  		makeNomalFormCall(url,showResult,'fm');
			
	}

	//返回checkbook
	function getCheckBox(value,meta,record)
	{
		var defineId = record.data.DEFINE_ID
		var flagYes = <%=Constant.PART_BASE_FLAG_YES%>;
		if(flagYes == value){
			return String.format("<input type='checkbox' id='isLeader_"+defineId+"' value=\""+value+"\" checked />");
	    } else {
	    	return String.format("<input type='checkbox' id='isLeader_"+defineId+"' value=\""+value+"\" />");
		}
			
	}

	//返回checkbook
	function getCheckBox1(value,meta,record)
	{
		var defineId = record.data.DEFINE_ID
		var flagYes = <%=Constant.PART_BASE_FLAG_YES%>;
		if(flagYes == value){
			return String.format("<input type='checkbox' id='isDirect_"+defineId+"' value=\""+value+"\" checked />");
	    } else {
	    	return String.format("<input type='checkbox' id='isDirect_"+defineId+"' value=\""+value+"\" />");
		}
			
	}

	//返回checkbook
	function getCheckBox2(value,meta,record)
	{
		var defineId = record.data.DEFINE_ID
		var flagYes = <%=Constant.PART_BASE_FLAG_YES%>;
		if(flagYes == value){
			return String.format("<input type='checkbox' id='isChkZy_"+defineId+"' value=\""+value+"\" checked />");
	    } else {
	    	return String.format("<input type='checkbox' id='isChkZy_"+defineId+"' value=\""+value+"\" />");
		}
			
	}

	//保存设置
	function updateData(parms) {
		if(confirm("确定更新设置?")){
			var obj = document.getElementById("isLeader_"+parms);
			var obj1 = document.getElementById("isDirect_"+parms);
			var obj2 = document.getElementById("isChkZy_"+parms);
			btnDisable();
			var isLeader = "no";
			var isDirect = "no";
			var isChkZy = "no";
			if(obj.checked)
			{
				isLeader = "yes";
			}
			if(obj1&&obj1.checked)
			{
				isDirect = "yes";
			}
			if(obj2&&obj2.checked)
			{
				isChkZy = "yes";
			}
	     	var url = '<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/updatePartUserPost.json?updateParms='+parms+'&isLeader='+isLeader+'&curPage='+myPage.page+'&isDirect='+isDirect+'&isChkZy='+isChkZy;
	  		makeNomalFormCall(url,showResult,'fm');
	    }
	}
	
	//设置失效：
	function cel(parms) {
		if(confirm("确定失效该数据?")){
			btnDisable();
	     	var url = '<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/celPartUserPost.json?disabeParms='+parms+'&curPage='+myPage.page;
	  		makeNomalFormCall(url,showResult,'fm');
	    }
	}
	
	//设置有效：
	function enableData(parms) {
		if(confirm("确定有效该数据?")){
			btnDisable();
	     	var url = '<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/enablePartUserPost.json?enableParms='+parms+'&curPage='+myPage.page;
	  		makeNomalFormCall(url,showResult,'fm');
	    }
	}
	
	function showResult(json) {
		btnEnable();
	    if (json.errorExist != null && json.errorExist.length > 0) {
	        MyAlert(json.errorExist);
	    } else if (json.success != null && json.success == "true") {
// 	    	MyAlert("操作成功!");
	    	layer.msg("操作成功!", {icon: 1});
	    	__extQuery__(json.curPage);          
	    } else {
	        MyAlert("操作失败，请联系管理员!");
	    }
	}

	//新增订单类型
	function addNew(){
		var userId = document.getElementById("userId").value;
		var url = '<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/queryUsersOrderInit.do?userId=' + userId;
		OpenHtmlWindow(url,700,500, '新增订单类型');
	}

	//返回
	function goBack(){
		btnDisable();
		location = '<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/partUserPostInit.do' ;
	}

</script>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">	
	<form name='fm' id='fm'>
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 配件人员类型设置 &gt; 维护</div>
		<div class="form-panel">
			<h2><img class="panel-icon nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 查询条件</h2>
			<div class="form-body">
				<table class="table_query">
					<tr>
					<td class="right">人员类型：</td>
					<td >
						<input type="text" id="fixName"  name="fixName" class="middle_txt" value="${map.FIX_NAME }" readonly="readonly" disabled="disabled"/>
						<input type="hidden" name="fixValue" id="fixValue" value="${map.USER_TYPE }"/>
					</td>
					<td class="right" >人员名称：</td>
					<td>
						<input type="hidden" name="userId" id="userId" value="${userId }"/>
						<input class="middle_txt" type="text" name="userName" id="userName" disabled="disabled" value="${map.NAME }"/>
					</td>
				</tr>
				<tr>
					<td class="center" colspan="6" >
					<input type="button" name="addBtn" id="addBtn" value="新增订单类型" onclick="addNew()"  class="u-button"/>
					<input type="button" name="backBtn" id="backBtn" value="返回" onclick="goBack()"  class="u-button"/>
					</td>
				</tr>
				</table>
			</div>
		</div>
		<!-- 查询条件 end -->
		<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		<!--分页 end -->
	</form>
</div>
</body>
</html>
