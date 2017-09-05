<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<title>索赔旧件管理</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<script type="text/javascript">
	
</script>
</head>
<body onload="__extQuery__(1);">
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;紧急调件索赔清单
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<tr>
		<td width="20%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">配件代码：</td>
      	<td width="10%"><input name="part_code" type="text" id="part_code" maxlength="30" class="middle_txt"/></td>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true">配件名称：</td>
		<td width="15%">
			<input name="part_name" type="text" id="part_name" class="middle_txt" maxlength="30"/>
		</td>
      	<td width="15%">
      	</td>
		<td width="25%"></td>
	</tr>
	<tr>
		<td width="20%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true"> 开始时间：</td>
      	<td width="15%"><input name="beginTime" type="text" id="beginTime" readonly="readonly" onfocus="calendar();" class="middle_txt"/></td>
        <td width="10%" class="table_query_2Col_label_5Letter" nowrap="true"> 结束时间：</td>
      	<td width="10%"><input name="endTime" type="text" id="endTime"  readonly="readonly" onfocus="calendar();" class="middle_txt"/></td>
      	<td width="15%">
		<td width="25%"><input type="hidden" id="dealer_id" value="${dealer_id }"/></td>
	</tr>
	<tr>
		<td width="20%"></td>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true">索赔单号：</td>
		<td width="15%">
			<input name="claim_no" type="text" id="claim_no" class="middle_txt" maxlength="30"/>
		</td>
        <td width="10%" class="table_query_2Col_label_5Letter" nowrap="true"> 
      		 	<input type="button" name="btnQuery" id="btnQuery"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" >
      	</td>
      	<td width="10%"></td>
      	<td width="15%">
      	</td>
		<td width="25%"></td>
	</tr>
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->

<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/claim/oldPart/EmergencyDevice/showAppListData.json?dealer_id="+$('dealer_id').value;
	var title = null;
	var columns = [
		{header: "选择", dataIndex: 'PART_ID', align:'center',renderer:myCheckBox},
		{header: "索赔申请单号", dataIndex: 'CLAIM_NO', align:'center'},
		{header: "VIN", dataIndex: 'VIN', align:'center'},
		{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
		{header: "配件名称", dataIndex: 'PART_NAME', align:'center'}
	];
	
	
	function myCheckBox(value,metaDate,record){
		return String.format("<input name='check' type='checkbox' value='"+record.data.ID+","+record.data.PART_ID+","+record.data.CLAIM_NO+","+record.data.VIN+","+record.data.PART_CODE+","+record.data.PART_NAME+","+record.data.TROUBLE_DESC+"'/>");
	}
	//返回的数据 更新页面数据
	function commit(){
			if (parent.$('inIframe')) {
				var check=document.getElementsByName("check");
				parentContainer.setBackData(check);
				_hide();
			}
	}
	//全选
	function checkAll(){
		var check=document.getElementsByName("check");
		for(var i =0;i<check.length;i++){
			check[i].checked="checked";
		}
	}
	//反选
	function checkAllChange(){
		var check=document.getElementsByName("check");
		for(var i =0;i<check.length;i++){
			if(check[i].checked){
				check[i].checked="";
			}else{
				check[i].checked="checked";
			}
		}
	}
	//清空
	function uncheckAll(){
		var check=document.getElementsByName("check");
		for(var i =0;i<check.length;i++){
			check[i].checked="";
		}
	}
	
</script>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query" style="text-align: center;">
		<tr>
			<td>
				      	<input type="button" value="全选"  class="normal_btn" onClick="checkAll();" >
				      	<input type="button" value="反选"  class="normal_btn" onClick="checkAllChange();" >
				      	<input type="button" value="清空"  class="normal_btn" onClick="uncheckAll();" >
				      	<input type="button" value="确认"  class="normal_btn" onClick="commit();" >
				      	<input type="button" value="关闭"  class="normal_btn" onClick="_hide();" >
			</td>
		</tr>
</table>
	</form>
</body>
<!--页面列表 end -->
</html>