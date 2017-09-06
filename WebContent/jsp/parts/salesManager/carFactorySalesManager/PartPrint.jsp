<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
	<title></title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-barcode-2.0.2.min.js"></script>	
	<style media=print>   
		.Noprint{
			display:none;
		}
		.p_next {
			page-break-after: always;
		}  
	</style>
	<style>
		#div_class{
			width: 150mm;
			height: 130mm;
			margin: 0 auto;
			padding: 4mm 5mm 1mm 5mm;
			border: 0px solid black;
		}
		body, td{font-size: 16px;color:#000}
	</style>
	<script language="javascript">
		function getBarCode(pkgNo){
			jQuery('#pkgNo_'+pkgNo).barcode(pkgNo,'code39',{barWidth:2, barHeight:70});
		}
	</script>
</head>
<body style="margin: 0px;padding: 0px;text-align: center;">
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
	<TABLE border=0 cellpadding=0 cellspacing=0 class="Noprint page-print-buttons" align="center" width=100%>
		<tr style="border: 0px;">
			<td style="border: 0px;">
				<input type=button id="printBtn" class="txtToolBarButton" value="打印">
				<input type=button id="printBtn2" class="txtToolBarButton" value="预览">
			</td>
		</tr>
	</TABLE>
	<c:forEach items="${list}" var="data" varStatus="curSta">
		<c:if test="${data.flag==true}">
		<c:choose>
			<c:when test="${curSta.last==true}">
				<div id="div_class">
			</c:when>
		<c:otherwise>
			<div class="p_next" id="div_class">
		</c:otherwise>
		</c:choose>
			<table id="tb1"  style="font-size: 18px;line-height: 16px;width:100%; height:100%;" border=1 bordercolor="black" cellpadding=0 cellspacing=0 >
			  <tr height="20mm">
				  <td colspan="2" align="center">
				  	<div id="pkgNo_${data.pkgNo}" ></div>
				  	<script type="text/javascript">getBarCode('${data.pkgNo}');</script>
				  </td>
			  </tr>
			  <tr>
			    <td>装箱单号：${listprp.PKG_NO}&nbsp;</td>
			  </tr>
			  <tr>
			    <td>服务站编码：${listprp.CONSIGNEES_CODE}&nbsp;</td>			    
			  </tr>
			  <tr>
			    <td>服务站名称：${listprp.CONSIGNEES}&nbsp;</td>
			  </tr>	
			  <tr>
			    <td>地址：${listprp.ADDR}&nbsp;</td>
			  </tr>
			  <tr>
			    <td>出库单号：${listprp.OUT_CODE}&nbsp;</td>
			  </tr>
			  <tr>
			    <td>发运单号：${listprp.TRANS_CODE}&nbsp;</td>
			  </tr>
			  <tr>
			    <td>发货方式：${listprp.TRANS_TYPE}&nbsp;</td>
			  </tr>
			  <tr>
			    <td>发货仓储：${listprp.WH_NAME}&nbsp;</td>
			  </tr>		 
			</table>
			<br/>
		</div>	
		</c:if>
	</c:forEach>
</form>
</body>
</html>
