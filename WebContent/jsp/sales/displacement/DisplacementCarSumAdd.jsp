<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>       
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript">
function doInit(){
   	loadcalendar();  //初始化时间控件
}
</script>
<TITLE>客户信息管理管理</TITLE>

</HEAD>
<BODY>

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：客户信息管理&gt;总部汇总打印&gt;
</div>
<form method="post" name ="fm" id="fm">
    <input type="hidden" name="reportCode" value="${reportCode}"/>
    <input type="hidden" name="date" value="<fmt:formatDate value='${date}' pattern='yyyy-MM-dd HH:mm:ss'/>"/>
    <input type="hidden" name="yieldly" id="yieldly"/> 
    <input type="hidden" name="reportId" id="reportId" value="${reportId}" />
    <input type="hidden" name="reportCode" value="${reportCode}"></input>
    <input type="hidden" name="userName" value="${userName}"></input>
    <input type="hidden" name="date" value="${date}"></input>
    <table  class="table_query">
		<tr>
			<td width="10%" align="right">单据编码：</td>
			<td width="20%" align="left">${reportCode}</td>
			<td width="10%" align="right">制单人姓名：</td>
			<td width="20%" align="left">${userName}</td>
		</tr>
		<tr>
			<td width="10%" align="right">生产厂商：</td>
			<td width="20%" align="left">
				<script type="text/javascript">
					genSelBoxExp("yieldly1",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'');
				</script>
			</td>
			<td width="10%" align="right">制单日期：</td>
			<td width="20%" align="left">
					<fmt:formatDate value="${date}" pattern="yyyy-MM-dd"/>
			</td>
		</tr>
	</table>
	<br />
	
	<table class="table_list" id="add_tab">
		<tr class="table_list_row2">
			<td>新车底盘号</td>
			<td>新车生产基地</td>
			<td>新车购车日期</td>
			<td>新车型号名称</td>
			<td><input type="button" value="新增" class="normal_btn" onclick="showBalance();"/></td>
		</tr>
	</table>
	<br />
	
	<table class="table_edit">
		<tr>
			<td colspan="4" align="center">
				<input type="button" value="保存" class="normal_btn" onclick="saveAdd();"/>
				&nbsp;&nbsp;
				<input type="button" value="返回" class="normal_btn" onclick="goBack();"/>
			</td>
		</tr>
  </table>
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
<script type="text/javascript">

function showBalance(){
	var yieldly1=document.getElementById("yieldly1").value;
	document.getElementById("yieldly").value=yieldly1;
	if(yieldly1==''){
		MyAlert('生产厂商为必填项！');
		return ;
	}		
	var url = '<%=contextPath%>/sales/displacement/DisplacementCar/OpenWinDisplacementUrl.do?yieldly='+yieldly1;
	OpenHtmlWindow(url,800,500);
}
function goBack(){
	$('fm').action ='<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementSumCarQuery.do';
	$('fm').submit();
}
function setNotice(newVin,newSalesDate,newArea,newModelName,displacementId){
	var tab = $('add_tab');
	var arr = document.getElementsByName('newVin');
	for(var i=0;i<newVin.length;i++){
		var flag = true ;
		for(var j = 0;j<arr.length;j++){
			if(arr[j].value == newVin[i]){
				flag = false ;
			}
		}
		if(flag){
			var length = tab.rows.length ;
			var insertRow = tab.insertRow(length);
			if(length%2==1)
				insertRow.className = 'table_list_row1' ;
			else 
				insertRow.className = 'table_list_row2' ;
			insertRow.insertCell(0);
			insertRow.insertCell(1);
			insertRow.insertCell(2);
			insertRow.insertCell(3);
			insertRow.insertCell(4);
			var curRow = tab.rows[length];
			curRow.cells[0].innerHTML = newVin[i]+'<input type="hidden" value='+newVin[i]+' name="newVin"/> <input type="hidden" value='+displacementId[i]+' name="displacementId"/>';
			curRow.cells[1].innerHTML = newSalesDate[i];
			curRow.cells[2].innerHTML = newArea[i];
			curRow.cells[3].innerHTML = newModelName[i] ;
			curRow.cells[4].innerHTML = '<input type="button" class="normal_btn" value="删除" onclick="delRow(this);">' ;
		}
	}
}
function fmoney(s, n)//将数字转换成逗号分隔的样式,保留两位小数s:value,n:小数位数      
{   
	if(!s) return 0;
    n = n > 0 && n <= 20 ? n : 2;   
    s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";   
    var l = s.split(".")[0].split("").reverse(),   
    r = s.split(".")[1];   
    t = "";   
    for(i = 0; i < l.length; i ++ )   
    {   
    t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");   
    }   
    return t.split("").reverse().join("") + "." + r;   
} 
function delRow(obj){
	$('add_tab').deleteRow(obj.parentElement.parentElement.rowIndex);
}
function doCusChange(){
	setNotice('','','','','','','');
}
function saveAdd(){
		MyConfirm('你确定保存吗？',sureAdd);		
}
function sureAdd(){
	$('fm').action = '<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementAddDj.do' ;
	$('fm').submit();
}
</script>
</BODY>
</html>