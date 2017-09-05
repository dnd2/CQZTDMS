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
<head>
<style media=print>
.Noprint {
	display: none;
}
 .STYLE1 {
	font-size: 24px;
	font-weight: bold;
	color: #000000;
 }
.STYLE2 {
	font-size: 14px;
	color: #000000;
}
.PageNext {
	page-break-after: always;
}

</style>
<script type="text/javascript">
//去除打印时的页眉和页脚
var HKEY_Root,HKEY_Path,HKEY_Key;    
HKEY_Root="HKEY_CURRENT_USER";    
HKEY_Path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
//设置网页打印的页眉页脚为空    
function PageSetup_Null()   
{   
   try{    
       var Wsh=new ActiveXObject("WScript.Shell");    
       HKEY_Key="header";    
       Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"");    
       HKEY_Key="footer";    
       Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"");    
   }catch(e){}    
}
function printsetup(){ 
  	// 打印页面设置
 	wb.execwb(8,1); 
} 
function printpreview(){ 
	// 打印页面预览 
	PageSetup_Null();
	wb.execwb(7,1); 
} 
function printit() { 
	PageSetup_Null();
	wb.execwb(6,6);
}
</script>
<TITLE>售后服务管理</TITLE>

</HEAD>
<BODY>

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;
	应税劳务清单</div>
<form method="post" name ="fm" id="fm">
	<table width="100%" border="2" cellspacing="0" bordercolor="#000000">
		<tr>
    		<td colspan="9" align="center" height="30"><font size=6><b>销售货物或提供应税劳务清单</b></font></td>
    	</tr>
		<tr>
			<td width="10%" align="right" height="26">开票通知单：</td>
			<td width="20%" align="left" >${tspo.balanceNo}</td>
			<td width="15%" align="right">购买方名称：</td>
			<td colspan="3" align="left">${tspo.purchaserName}</td>
		</tr>
		<tr>
			<td width="10%" align="right" height="26">销售方名称：</td>
			<td width="20%" align="left">${tspo.salesName}</td>
			<td width="15%" align="right">所属增值税专用发票代码：</td>
			<td width="20%" align="left">${tspo.invoiceNo}</td>
			<td width="10%" align="right" height="26">号码：</td>
			<td width="20%" align="left">${tspo.no}</td>
		</tr>
		<tr>
			<td width="10%" align="right">税率：</td>
			<td width="20%" align="left">${tspo.taxRate}</td>
			<td width="15%" align="right" height="26">维修开始时间：</td>
			<td width="20%" align="left">
				<fmt:formatDate value="${tspo.claimStartDate}" pattern="yyyy-MM-dd"/></td>
			<td width="10%" align="right">维修结束时间：</td>
			<td width="20%" align="left">
				<fmt:formatDate value="${tspo.claimEndDate}" pattern="yyyy-MM-dd"/>
			</td>
		</tr>
	</table>
	<br />
    <table  border=1 width="100%">
		<tr align="center">
			<td>序号</td>
			<td>货物(劳务)名称</td>
			<td>规格型号</td>
			<td>单位</td>
			<td>数量</td>
			<td>单价</td>
			<td>金额</td>
			<td>税率</td>
			<td>税额</td>
		</tr>
		<c:forEach var="detail" items="${list}">
			<tr align="center">
				<td>${detail.serialNumber}</td>
				<td>${detail.goodsName}</td>
				<td>${detail.goodsModel}</td>
				<td>${detail.goodsUnit}</td>
				<td>${detail.goodsNum}</td>
				<td>${detail.goodsUnitPrice}</td>
				<td>${detail.goodsSumAmount}</td>
				<td>${detail.taxRate}</td>
				<td>${detail.taxAmount}</td>
			</tr>
		</c:forEach>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td></td>
			<td>总计</td>
			<td colspan="4"></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td>价税合计</td>
			<td colspan="4"></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td>备注</td>
			<td colspan="4"></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td></td>
			<td>销售方(章)</td>
			<td colspan="4"></td>
			<td colspan="3">填开日期：</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td></td>
			<td>注：</td>
			<td>本清单一式两联：</td>
			<td>第一联</td>
			<td>销售方留存；</td>
			<td>第二联</td>
			<td>销售方送交购买方</td>
		</tr>
	</table>
	<br />
	
	<table class="table_edit">
		<tr>
			<td colspan="4" align="center">
				<input type="button" value="关闭" class="normal_btn" onclick="window.close();"/>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
function goBack(){
	location = '<%=contextPath%>/claim/laborlist/LaborListForTax/mainUrlInit.do' ;
}
</script>
</BODY>
</html>