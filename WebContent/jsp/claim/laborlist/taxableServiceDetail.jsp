<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.po.TtTaxableServicePO"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
TtTaxableServicePO tspo = (TtTaxableServicePO)request.getAttribute("tspo");
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
		yieldlyName = '重庆长安汽车股份有限公司';
	}else if(yieldly=='<%=Constant.SERVICEACTIVITY_CAR_YIELDLY_02%>'){//河北
		yieldlyName = '河北长安商用汽车销售有限公司';
	}else if(yieldly=='<%=Constant.SERVICEACTIVITY_CAR_YIELDLY_03%>'){//南京
		yieldlyName = '南京川渝长安汽车销售有限公司';
	}else if(yieldly=='<%=Constant.SERVICEACTIVITY_CAR_YIELDLY_04%>'){ 
		yieldlyName = '江西昌河汽车有限责任公司九江销售分公司' ;
	}
    return yieldlyName;
}
</script>
<TITLE>售后服务管理</TITLE>

</HEAD>
<BODY>

<form method="post" name ="fm" id="fm">
<table align="center"  border="0" class="tabptest">
<thead style="display:table-header-group;"> 
 	<tr>
		<td height="10" colspan="9" align="center">
			<table >
				<tr align="center">
					<td align="center" height="10" width="100%" colspan="6">
						<font size=5>销售货物或提供应税劳务清单</font>
					</td>
	        	</tr>
  	  		</table>
  	  	</td>
	</tr>
	<tr>
		<td colspan="9">
			<table class="tabptest">
				<tr style="width:100%">
					<td class="tdp" width="30%">开票通知单：${tspo.balanceNo}</td>
					<td class="tdp" colspan="2">购买方名称：
						<script type="text/javascript">
							document.write(changeYieldly(${tspo.purchaserId}));
						</script>
					</td>
				</tr>
				<tr>
					<td class="tdp">销售方名称：${tspo.salesName}</td>
					<td class="tdp">所属增值税专用发票代码：
						<% String str = tspo.getInvoiceNo() ;
							String s1 = "" ;
							String s2 = "" ;
							if(str.length()>11){
								s1 = str.substring(0,11) ;
								s1+="<br />";
								s2 = str.substring(11,str.length());
							}else {
								s1 = str ;
								s2 = "" ;
							}
							
						%>	
						<%=s1 %><%=s2 %>
					</td>
					<td class="tdp">号码：
						<% String str2 = tspo.getNo() ;
							String s21 = "" ;
							String s22 = "" ;
							if(str2.length()>27){
								s21 = str2.substring(0,27) ;
								s21+="<br />";
								s22 = str2.substring(27,str2.length());
							}else {
								s21 = str2 ;
								s22 = "" ;
							}
							
						%>	
						<%=s21 %><%=s22 %>
					</td>
				</tr>
				<tr>
					<td class="tdp">税率：${tspo.taxRate}</td>
					<td class="tdp">维修开始时间：
						<fmt:formatDate value="${tspo.claimStartDate}" pattern="yyyy-MM-dd"/></td>
					<td class="tdp">维修结束时间：
						<fmt:formatDate value="${tspo.claimEndDate}" pattern="yyyy-MM-dd"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="9" height="6"></td>
	</tr>
	<tr>
		<td class="tdp" nowrap>序号</td>
		<td class="tdp">货物(劳务)名称</td>
		<td class="tdp" nowrap>规格型号</td>
		<td class="tdp" nowrap width="24">单位</td>
		<td class="tdp" nowrap>数量</td>
		<td class="tdp" nowrap>单价</td>
		<td class="tdp" nowrap>金额</td>
		<td class="tdp" nowrap>税率</td>
		<td class="tdpright" nowrap>税额</td>
	</tr>
</thead>
<tbody>
	<c:set var="pageSize"  value="40" />
		<c:forEach var="detail" items="${list}" varStatus="st">
			<tr style='${(st.count%pageSize==0) ? "page-break-after:always;":""},width:100%'>
				<td class="tdp" width="5%">
					${st.index+1}
					<c:set var="idx" value="${st.index}"/>
					<!-- ${detail.serialNumber} -->
				</td>
				<td class="tdp" width="25%">${detail.goodsName}</td>
				<td class="tdp" width="15%">${detail.goodsCode}</td>
				<td class="tdp" width="5%">${detail.goodsUnit}</td>
				<td class="tdp" width="10%">
					<c:if test="${listSize-10<=st.index}"></c:if>
					<c:if test="${listSize-10>st.index}">
						<fmt:formatNumber value="${detail.goodsNum}" pattern="##,###"/>	
					</c:if>	
				</td>
				<td class="tdp" width="10%">
					<c:if test="${listSize-10<=st.index}"></c:if>
					<c:if test="${listSize-10>st.index}">
						<fmt:formatNumber value="${detail.goodsUnitPrice}" pattern="##,###.##"/>
					</c:if>	
				</td>
				<td class="tdp" width="10%">
					<c:if test="${listSize-1<=st.index}"></c:if>
					<c:if test="${listSize-1>st.index}">
						<fmt:formatNumber value="${detail.goodsExclTaxAmount}" pattern="##,###.##"/>
					</c:if>
				</td>
				<td class="tdp" width="10%">
					<c:if test="${listSize-2<=st.index}"></c:if>
					<c:if test="${listSize-2>st.index}">
						${detail.taxRate}
					</c:if>	
				</td>
				<td class="tdpright" width="10%">
					<c:if test="${listSize-2<=st.index}"></c:if>
					<c:if test="${listSize-2>st.index}">
						<fmt:formatNumber value="${detail.taxAmount}" pattern="##,###.##"/>
					</c:if>
				</td>
			</c:forEach>
			<tr>
				<td class="tdp">
					${idx+2}
					<c:set var="idx" value="${idx+2}"/>
				</td>
				<td class="tdp"></td>
				<td class="tdp"></td>
				<td class="tdp"></td>
				<td class="tdp"></td>
				<td class="tdp"></td>
				<td class="tdp"></td>
				<td class="tdp"></td>
				<td class="tdpright"></td>
			</tr>
			<tr>
				<td class="tdp">
					${idx+1}
					<c:set var="idx" value="${idx+1}"/>
				</td>
				<td class="tdp">销售方(章)</td>
				<td class="tdp"></td>
				<td class="tdp"></td>
				<td class="tdp"></td>
				<td class="tdp"></td>
				<td class="tdp">填单日期</td>
				<td class="tdp"></td>
				<td class="tdpright"></td>
			</tr>
			<tr>
				<td class="tdp">
					${idx+1}
					<c:set var="idx" value="${idx+1}"/>
				</td>
				<td class="tdp"></td>
				<td class="tdp"></td>
				<td class="tdp"></td>
				<td class="tdp"></td>
				<td class="tdp"></td>
				<td class="tdp"></td>
				<td class="tdp"></td>
				<td class="tdpright"></td>
			</tr>
			<tr>
				<td class="tdp">
					${idx+1}
					<c:set var="idx" value="${idx+1}"/>
				</td>
				<td class="tdp">注：</td>
				<td class="tdp">本清单一式两联：</td>
				<td class="tdp">第一联</td>
				<td class="tdp">销售方<br />保留</td>
				<td class="tdp">第二联</td>
				<td class="tdp">销售方送交购买方</td> 
				<td class="tdp"></td>
				<td class="tdpright"></td>
			</tr>
		</tbody>
</table>
<br/>
</form>
<script type="text/javascript">
</script>
</BODY>
<table class="table_edit">
		<tr>
			<td colspan="4" align="center">
				<OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height=0 id=wb name=wb width=3></OBJECT>    
				<div id="kpr" align="center">    
					<input class="ipt" type=button name= button _print value="打印" onclick ="kpr.style.display='none';javascript :printit();">    
					<input class="ipt" type=button name=button _setup value="打印页面设置" onclick =" javascript : printsetup();">    
					<input class="ipt" type=button name=button_show value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();">    
					<input class="ipt" type=button name= button _fh value="关闭" onclick =" javascript:window.close();"> 
				</div> 
			</td>
		</tr>  
	</table>
</html>