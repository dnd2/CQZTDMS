<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<OBJECT classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 id=WebBrowser width=0></OBJECT>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>Insert title here</title>
</head>
<body>
<form method="post" name="fm" id="fm">
	<c:forEach items="${pf}" var="pf" varStatus="status">
		<table class="tabprint">
			<tr align="center"><th colspan="2">长安轿车旧件回收标签</th></tr>
			<tr>
				<td class="tdp">三包单号</td>
				<td class="tdp"><c:out value="${pf.CLAIM_NO}"/></td>
			</tr>
			<tr>
				<td class="tdp">VIN码</td>
				<td class="tdp"><c:out value="${pf.VIN}"/></td>
			</tr>
			<tr>
				<td class="tdp">生产厂家</td>
				<td class="tdp"><script type="text/javascript">writeItemValue('<c:out value="${pf.YIELDLY}"/>')</script></td>
			</tr>
			<tr>
				<td class="tdp">车型</td>
				<td class="tdp"><c:out value="${pf.MODEL_CODE}"/></td>
			</tr>
			<tr>
				<td class="tdp">发动机号</td>
				<td class="tdp"><c:out value="${pf.ENGINE_NO}"/></td>
			</tr>
			<tr>
				<td class="tdp">行驶里程</td>
				<td class="tdp" ><c:out value="${pf.IN_MILEAGE}"/></td>
			</tr>
			<tr>
				<td class="tdp">购车日期</td>
				<td class="tdp"><c:out value="${pf.PURCHASED_DATE}"/></td>
			</tr>
			<tr>
				<td class="tdp">服务商代码</td>
				<td class="tdp"><c:out value="${pf.DEALER_CODE}"/></td>
			</tr>
			<tr>
				<td class="tdp">零件名称</td>
				<td class="tdp"><c:out value="${pf.PART_NAME}"/></td>
			</tr>
			<tr>
				<td class="tdp">零件编码</td>
				<td class="tdp"><c:out value="${pf.PART_CODE}"/></td>
			</tr>
			<tr>
				<td class="tdp">配套厂家</td>
				<td class="tdp"><c:out value="${pf.DC_NAME}"/></td>
			</tr>
			<tr>
				<td class="tdp">故障</td>
				<td class="tdp"><c:out value="${pf.REMARK}"/></td>
			</tr>
			<tr>
				<td class="tdp">客户姓名</td>
				<td class="tdp"><c:out value="${pf.DELIVERER}"/></td>
			</tr>
			<tr>
				<td class="tdp">电话</td>
				<td class="tdp"><c:out value="${pf.DELIVERER_PHONE}"/></td>
			</tr>
		</table>	
    </c:forEach>
</form>
</body>
	<table width="100%" cellpadding="1" align="right" onmouseover="kpr.style.display='';" >   
	<tr>    
	<td width="100%" height="25" colspan="3"><script language="javascript">    
	  
	function printsetup(){    
	// 打印页面设置    
	wb.execwb(8,1);    
	}    
	function printpreview(){    
	// 打印页面预览      
	wb.execwb(7,1);    
	}      
	function printit()    
	{    
	if (confirm('确定打印吗？')){    
	  
	wb.execwb(6,6)    
	}    
	}    
	</script>    
	<OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height=0 id=wb name=wb width=3></OBJECT>    
	<div id="kpr" align="left">    
	<input class="ipt" type=button name= button _print value="打印" onclick ="kpr.style.display='none';javascript :printit();"/>    
	<input class="ipt" type=button name=button _setup value="打印页面设置" onclick =" javascript : printsetup();"/>    
	<input class="ipt" type=button name=button_show value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();"/>    
	<input type="button" value="导出到Excel" class="" onclick="exportToExcel()"/>
	<input class="ipt" type=button name= button _fh value="关闭" onclick =" javascript:window.close();"/>  
	<input type="hidden" id="orderId" value="<c:out value="${ORDER_ID}"/>"/>  
	</div> </tr>   
	</table>  
</html>
<script type="text/javascript">
function exportToExcel() {
	var orderId = document.getElementById("orderId").value;
	var url = '<%=request.getContextPath()%>/claim/oldPart/ClaimBackPieceBackListOrdManager/toExcel.do?ORDER_ID=' + orderId;
	window.location = url;
}
</script>