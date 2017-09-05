<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>收票报表</title>
</head>
<body>
<br/>
<center><strong><font size="6">收票报表</font></strong></center>
<br><br><br>
<div align="center">
<table width="700px" border="1" cellSpacing= "0" cellPadding= "0" class="tabp">
	<tr>
		<td colspan="3" class="tdp" align="left">签收时间起：${bDate}</td>
		<td colspan="3" class="tdp" align="left">签收时间止：${eDate}</td>
		<td colspan="6" class="tdpright" align="left">签收人：${name }</td>
	</tr>
	<tr>
		<td height="2px" colspan="12">&nbsp;</td>
	</tr>
	<c:if test="${list!=null}">
		<tr>
			<td class="tdp" nowrap>序号</td>
			<td class="tdp" nowrap>汇总单号</td>
			<td class="tdp" nowrap>经销商代码</td>
			<td class="tdp">经销商名称</td>
			<td class="tdp" nowrap>省份</td>
			<td class="tdp" nowrap>发票号码</td>
			<td class="tdp" nowrap>发票金额</td>
			<td class="tdp" nowrap>税率</td>
			<td class="tdp" nowrap>收票时间</td>
			<td class="tdp" nowrap>挂账时间</td>
			<!-- <td class="tdp" nowrap>财务人员</td> -->
			<td class="tdpright" nowrap>签名栏</td>
		</tr>
	</c:if>
	<c:set var="pageSize"  value="25" />
	<c:forEach items="${list}" var="map" varStatus="st">
		<tr  style='${(st.count%pageSize==0) ? "page-break-after:always;":""}'>
			<td class="tdp">${st.index+1}</td>
			<td class="tdp">
				<script>
					var code = '${map.REPORT_CODE}' ;
					document.write(code.substring(0,10)+'<br />'+code.substring(10,code.length));
				</script>
			</td>
			<td class="tdp">${map.DEALER_CODE}</td>
			<td class="tdp">
				<script type="text/javascript">
					var name = '${map.DEALER_NAME}' ;
					var n1 ;
					if(name.length<=8){
						document.write(name);
					}else if(name.length>8 && name.length.length<=16){
						n1 = name.substring(0,8)+'<br />'+name.substring(8,name.length) ;
						document.write(n1);
					}else{
						n1 = name.substring(0,8)+'<br />'+name.substring(8,16)+'<br />'+name.substring(16,name.length) ;
						document.write(n1);
					}
				</script>
			</td>
			<td class="tdp">
				<script>
					var code = '${map.REGION_NAME}' ;
					document.write(code.substring(0,4)+'<br />'+code.substring(4,code.length));
				</script>
			</td>
			<td class="tdp" width="100">
				<script type="text/javascript">
					var name = '${map.INVOICE_CODE}' ;
					var n1 ;
					if(name.length<=18){
						document.write(name);
					}else if(name.length>18 && name.length<=36){
						n1 = name.substring(0,18)+'<br />'+name.substring(18,name.length) ;
						document.write(n1);
					}else if(name.length>36 && name.length<=54){
						n1 = name.substring(0,18)+'<br />'+name.substring(18,36)+'<br />'+name.substring(36,name.length) ;
						document.write(n1);
					}else{
						n1 = name.substring(0,18)+'<br />'+name.substring(18,36)+'<br />'+name.substring(36,54)+'<br />'+name.substring(54,name.length)
						document.write(n1);
					}
				</script>
			</td>
			<td class="tdp">${map.AMOUNT}</td>
			<td class="tdp">${map.TAX_RATE}</td>
			<td class="tdp">${map.AUTH_TIME}</td>
			<td class="tdp">${map.GZTIME}</td>
			<!-- <td class="tdp">${map.AUTH_PERSON_NAME}</td> -->
			<td class="tdpright"></td>
		</tr>
	</c:forEach>
</table>
<br><br><br>
<table width="100%" cellpadding="1" align="center" onmouseover="kpr.style.display='';" >   
	<tr>    
		<td width="100%" height="25" colspan="3">   
		<object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
			<div id="kpr">
				<input class="ipt" type="button" value="打印" onclick="kpr.style.display='none';javascript:printit();">    
				<input class="ipt" type="button" value="打印页面设置" onclick="javascript:printsetup();">    
				<input class="ipt" type="button" value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();">
			</div>
		</td>
	</tr>
</table>
</div>
<script language="javascript">

	function printsetup()
	{
		wb.execwb(8,1);// 打印页面设置
	}
	function printpreview()
	{
		wb.execwb(7,1);// 打印页面预览
	}
	function printit()
	{
		if(confirm('确定打印吗？'))
		{
			wb.execwb(6,6)
		}
	}
	
</script> 
</body>
</html>
