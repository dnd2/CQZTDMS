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
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：通用显示车型
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->

<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->

<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/ApplicationAction/showCarType.json?query=true";
	var title = null;
	var columns = [
		{header: "选择", dataIndex: 'GROUP_ID', align:'center',renderer:myCheckBox},
		{header: "车型", dataIndex: 'GROUP_NAME', align:'center'}
	];
	
	
	function myCheckBox(value,metaDate,record){
		return String.format("<input name='check' type='checkbox' value='"+record.data.GROUP_ID+","+record.data.GROUP_NAME+"'/>");
	}
	//返回的数据 更新页面数据
	function commit(){
		
		if (parent.$('inIframe')) {
			var checks=document.getElementsByName("check");
			checks=getCheck(checks);
			var carValueStr="";
			var carNameStr="";
			for (var i = 0; i< checks.length; i++) {
				var checkValue=checks[i].value;
				carId =checkValue.substring(0,checkValue.indexOf(","));
				 carName=checkValue.substring(checkValue.indexOf(",")+1,checkValue.length);
				if(i!=checks.length-1){
					carNameStr+=carName+",";
				}else{
					carNameStr+=carName;
				}
				if(i!=checks.length-1){
					carValueStr+=carId+",";
				}else{
					carValueStr+=carId;
				}
			}
			parentDocument.getElementById("cars").value=carNameStr;
			parentDocument.getElementById("carModel").value=carValueStr;
		}
		_hide();
	}
	
	
	function getCheck(checks){
		var array=new Array();
		for (var i = 0; i< checks.length; i++) {
			
			if(checks[i].checked==true){
				array.push(checks[i]);
			}
		}
		return array;
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