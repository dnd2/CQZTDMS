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
<TITLE>售后服务管理</TITLE>

</HEAD>
<BODY>

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;
	应税劳务清单汇总报表参数设置</div>
<form method="post" name ="fm" id="fm">
    <input type="hidden" name="reportCode" value="${reportCode}"/>
    <input type="hidden" name="date" value="<fmt:formatDate value='${date}' pattern='yyyy-MM-dd HH:mm:ss'/>"/>
    <input type="hidden" name="amount" id="amount2"/>
    <input type="hidden" name="yieldly" id="yieldly"/> 
    <table  class="table_query">
		<tr>
			<td width="10%" align="right">单据编码：</td>
			<td width="20%" align="left">${reportCode}</td>
			<td width="10%" align="right">制单人姓名：</td>
			<td width="20%" align="left">${userName}</td>
		</tr>
		<tr>
			<td width="10%" align="right">制单单位名称：</td>
			<td width="20%" align="left">${dealer.dealerName}</td>
			<td width="10%" align="right">制单单位编码：</td>
			<td width="20%" align="left">${dealer.dealerCode}</td>
		</tr>
		<tr>
			<td width="10%" align="right">生产厂商：</td>
			<td width="20%" align="left">
				<script type="text/javascript">
					genSelBoxExp("yieldly1",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'');
				</script>
			</td>
			<td width="10%" align="right">发票号：</td>
			<td width="20%" align="left">
				<input type="text" name="incoiceCode" datatype="0,is_null" id="incoiceCode" class="middle_txt"/>
			</td>
		</tr>
		<tr>
			<td width="10%" align="right">制单日期：</td>
			<td width="20%" align="left">
				<fmt:formatDate value="${date}" pattern="yyyy-MM-dd"/>
			</td>
			<td width="10%" align="right">总金额：</td>
			<td width="20%" align="left">
				<div id="amount1"></div>
				<input type="hidden" id="amount" name="amount"/>
			</td>
		</tr>
		<tr>
			<td width="10%" align="right">接收时间：</td>
			<td width="20%" align="left">
				
			</td>
			<td width="10%" align="right">接收人：</td>
			<td width="20%" align="left">
				
			</td>
		</tr>
	</table>
	<br />
	
	<table class="table_list" id="add_tab">
		<tr class="table_list_row2">
			<td>开票通知单</td>
			<td>开票金额</td>
			<td>维修站编码</td>
			<td>维修站名称</td>
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
var obj = $('yieldly1') ;
obj.attachEvent('onchange',setYieldly);
function setYieldly(){
	$('yieldly').value = $('yieldly1').value ;
	$('yieldly1').disabled = true; 
}
function showBalance(){
	var yieldly = $('yieldly').value ;
	if(yieldly == ''){
		MyAlert('生产厂商为必填项！');
		return ;
	}		
	var arr = document.getElementsByName('notice');
	var arrStr = "('";
	for(var i = 0 ;i < arr.length;i++)
		arrStr = arrStr+arr[i].value+"," ;
	arrStr = arrStr.substr(0,arrStr.length-1);
	arrStr = arrStr+"')" ;
	var url = '<%=contextPath%>/claim/laborlist/LaborListAction/openWinUrlInit.do?code='+arrStr+'&yieldly='+yieldly ;
	OpenHtmlWindow(url,800,500);
}
function goBack(){
	location = '<%=contextPath%>/claim/laborlist/LaborListAction/firstUrlInit.do' ;
}
function setNotice(no,amounts,dlr_id,code,name,b_id){
	var tab = $('add_tab');
	var arr = document.getElementsByName('notice');
	for(var i=0;i<no.length;i++){
		var flag = true ;
		for(var j = 0;j<arr.length;j++){
			if(arr[j].value == no[i]){
				flag = false ;
			}
		}
		if(flag){
			$('amount').value=$('amount').value*1+amounts[i]*1;
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
			curRow.cells[0].innerHTML = no[i]+'<input type="hidden" value='+no[i]+' name="notice"/><input type="hidden" name="_money" value='+amounts[i]+'><input type="hidden" name="dealer_name" value='+name[i]+'><input type="hidden" name="balanceId" value='+b_id[i]+'><input type="hidden" name="dealer_code" value='+code[i]+'><input type="hidden" name="dealer_id" value='+dlr_id[i]+'>' ;
			curRow.cells[1].innerHTML = amounts[i];
			curRow.cells[2].innerHTML = code[i];
			curRow.cells[3].innerHTML = name[i] ;
			curRow.cells[4].innerHTML = '<input type="button" class="normal_btn" value="删除" onclick="delRow(this,'+amounts[i]+');">' ;
		}
	}
	$('amount1').innerHTML = fmoney($('amount').value,2);
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
function delRow(obj,value){
	$('amount').value=$('amount').value*1-value*1;
	$('add_tab').deleteRow(obj.parentElement.parentElement.rowIndex);
	$('amount1').innerHTML = fmoney($('amount').value,2);
}
function doCusChange(){
	setNotice('','','','','','','');
}
function saveAdd(){
	if(submitForm(fm)==false)return ;
	if($('add_tab').rows.length==1)
		MyAlert('开票通知单未填写！');
	else
		MyConfirm('你确定保存吗？',sureAdd);		
}
function sureAdd(){
	$('fm').action = '<%=contextPath%>/claim/laborlist/LaborListAction/save.do' ;
	$('fm').submit();
}
</script>
</BODY>
</html>