<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.po.TtLaborListPO"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
	TtLaborListPO lpo = (TtLaborListPO)request.getAttribute("lpo");
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>       
<head>
<style media=print>
.Noprint {
	display: none;
}
 .STYLE1 {
	font-size: 30px;
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
function PageSetup_Null(){   
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

function printsetup(){    
	// 打印页面设置    
	wb.execwb(8,1);    
}    
function printpreview(){    
	// 打印页面预览      
	wb.execwb(7,1);    
}      
function printit(){    
	if (confirm('确定打印吗？')){    
		wb.execwb(6,6)    
	}    
}  
function changeYieldly(yieldly){
	var yieldlyName = '';
	if(yieldly=='<%=Constant.SERVICEACTIVITY_CAR_YIELDLY_01%>'){//重庆
		yieldlyName = '重庆长安';
	}else if(yieldly=='<%=Constant.SERVICEACTIVITY_CAR_YIELDLY_02%>'){//河北
		yieldlyName = '河北长安';
	}else if(yieldly=='<%=Constant.SERVICEACTIVITY_CAR_YIELDLY_03%>'){//南京
		yieldlyName = '南京长安';
	}
    return yieldlyName;
}
</script>
<TITLE>售后服务管理</TITLE>

</HEAD>
<BODY>

<form method="post" name ="fm" id="fm">
<table width="756px;"  align="center"  border="0" class="tabptest">
<thead style="display:table-header-group;"> 
 	<tr >
		<td height="10" colspan="11" align="center">
			<table >
				<tr align="center">
					<td align="center" height="30" width="100%" colspan="6">
						<font size=4>销售货物或提供应税劳务清单汇总报表</font>
					</td>
				</tr>
			</table>
		</td>
	</tr>
 	<tr >
		<td colspan="11">
			<table class="tabptest">
				<tr style="width:100%">
					<td class="tdp" width="30%">报表参数编码：${lpo.reportCode}</td>
					<td class="tdp" width="30%">开票单位编码：${dealer.dealerCode}</td>
					<td class="tdpright" width="40%">开票单位名称：${dealer.dealerName}</td>
				</tr>
				<tr>
					<td class="tdp">税率：${tax}</td>
					<td class="tdp">厂家：
						<script>
							document.write(changeYieldly(${lpo.yieldly}));
						</script>
						
					</td>
					<td colspan="2" class="tdpright">发票号：
						<% 
							String str = lpo.getInvoiceCode() ;
							String s1 = "" ;
							String s2 = "" ;
							if(str!=""&&str!=null){
								if(str.length()>27){
									s1 = str.substring(0,27) ;
									s1+="<br />";
									s2 = str.substring(27,str.length());
								}else {
									s1 = str ;
									s2 = "" ;
								}
							}
						
						%>	
						<%=s1 %><%=s2 %>
					</td>
				</tr>
				<tr>
					<td colspan="7" height="6"></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr align="center">
		<td class="tdp">序号</td>
		<td class="tdp">产品名称</td>
		<td class="tdp">不含税金额</td>
		<td class="tdp">税额</td>
		<td class="tdp">合计</td>
		<td class="tdpright">税率</td>
	</tr>
</thead>
<tbody>
	<c:forEach var="bean" items="${list}" varStatus="st">
		<tr>
			<td class="tdp">
				${st.index+1}
				<!-- ${bean.ord} -->
			</td>
			<td class="tdp">
				<c:if test="${bean.modelCode!=null}">${bean.modelCode}系列${bean.type}</c:if>
				<c:if test="${bean.modelCode==null}">${bean.modelCode}${bean.type}</c:if>
			</td>
			<td class="tdp">
				<fmt:formatNumber value="${bean.balanceAmount/(1+tax)}" pattern="##,###.##"/>
			</td>
			<td class="tdp">
				<fmt:formatNumber value="${bean.balanceAmount-bean.balanceAmount/(1+tax)}" pattern="##,###.##"/>
			</td>
			<td class="tdp">
				<fmt:formatNumber value="${bean.balanceAmount}" pattern="##,###.##"/>
			</td>
			<td class="tdpright">${tax}</td>
		</tr>
	</c:forEach>
</tbody>
</table>
<br />
	
</form>
<script type="text/javascript">
</script>
</BODY>
<table class="table_edit">
	<tr>
		<td colspan="4" align="center">
			<OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height=0 id=wb name=wb width=3></OBJECT>    
			<div id="kpr" align="center">    
				<input class="ipt" type=button name= button _print value="打印" onclick ="javascript :printit();">    
				<input class="ipt" type=button name=button _setup value="打印页面设置" onclick =" javascript : printsetup();">    
				<input class="ipt" type=button name=button_show value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();">    
				<input class="ipt" type=button name= button _fh value="关闭" onclick =" javascript:window.close();"> 
			</div> 
		</td>
	</tr>  
</table>
</html>