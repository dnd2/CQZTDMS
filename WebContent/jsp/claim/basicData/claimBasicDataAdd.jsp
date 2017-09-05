<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔工时单价设定</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="wbox">
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;索赔工时单价设定&gt;新增</div>
<form name='fm' id='fm' method="post">
<div class="form-panel">
<h2>基本信息</h2>
			<div class="form-body">
<table class="table_query" >
	<tr>
 		<td style="text-align:right">经销商代码：</td>
 		<td >
 			<textarea cols="50" rows="2" id="dealer_code" name="dealer_code" readonly="readonly" onclick="showOrgDealer('dealer_code','dealer_id','true','','true','','10771002');"></textarea>&nbsp;
 			<span style="color:red">*</span>
 			<input type="hidden" name="dealer_id" id="dealer_id"/>
 			<!-- <input type="button" value="..." class="mini_btn" onclick="showOrgDealer('dealer_code','dealer_id','true','','true','','10771002');"/>&nbsp; -->
 			<input type="button" value="清除" class="normal_btn" onclick="wrapOut();"/>
 		</td>
	</tr>
</table>
<br />

<table class="table_list" id="add_tab">
	<tr>
		<th>车系</th>
		<th>工时单价</th>
		<th><a class="u-anchor" onclick="showLabor();">车系选择</a></th>
	</tr>
</table>
<br />

<table class="table_query">
	<tr>
 		<td colspan="2" style="text-align:center">
 			<input class="normal_btn" type="button" value="确定" onclick="sureSave();"/> 
 			&nbsp;&nbsp;
 			<input class="normal_btn" type="button" onclick="goBack();" value="返回"/>
 		</td>
	</tr>
</table>
</div>
</div>
</form>
</div>
<script type="text/javascript">
	function wrapOut(){
		document.getElementById("dealer_code").value = '' ;
		document.getElementById("dealer_id").value = '' ;
	}
	function showLabor(){
		var url = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/laborListInit.do' ;
		OpenHtmlWindow(url,700,400);
	}
	function setLaborList(codes,price){
		var tab = document.getElementById("add_tab");
		for(var i = 0 ;i<codes.length;i++){
			var idx = tab.rows.length ;
			var insert_row = tab.insertRow(idx);
			if(idx%2==0)
				insert_row.className = 'table_list_row2';
			else 
				insert_row.className = 'table_list_row1' ;
			insert_row.insertCell(0);
			insert_row.insertCell(1);
			insert_row.insertCell(2);
			var cur_row = tab.rows[idx];
			cur_row.cells[0].innerHTML = codes[i]+'<input type="hidden" name="labor_code" value='+codes[i]+'>' ;
			cur_row.cells[1].innerHTML = '<input type="text" class="middle_txt" datatype="0,isDigit" name="price" value="'+price+'"/><span style="color:red">*</span>' ;
			cur_row.cells[2].innerHTML = '<a class="u-anchor" onclick="delRow(this);">删除</a>' ;
		}
	}
	function delRow(obj){
		var tab = document.getElementById("add_tab");
		var idx = obj.parentElement.parentElement.rowIndex;
		tab.deleteRow(idx);
	}
	function sureSave(){
		if(!document.getElementById("dealer_id").value){
			MyAlert('经销商未维护!');
			return ;
		}
		if(document.getElementById("add_tab").rows.length==1){
			MyAlert('车型大类未维护!');
			return ;
		}
		var reg = /^(\d+\.\d{1,2}|\d+)$/;
		var str = document.getElementsByName("price");
		for(var j=0;j<str.length;j++){
		if(str[j].value==null||str[j].value==""){
			MyAlert("工时单价未维护!");
			str[j].focus();
			return false;
		}
		if(!reg.test(str[j].value)){
		MyAlert("请输入最多2位小数的数字!");
		str[j].focus();
		return false;
		}
		}
		MyConfirm('此操作将更改已存在的数据,确定操作?', sub);
	}
	function sub() {
		document.getElementById("fm").action = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/laborPriceAdd.do' ;
		document.getElementById("fm").submit() ;
	}
	function goBack(){
		location = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/labourPriceInit.do' ;
	}
</script>
</body>
</html>
