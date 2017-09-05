<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<% String contextPath = request.getContextPath(); %>
<script language="javascript" type="text/javascript">
	function prtPage(obj)
	{
		obj.style.display="none";  
		window.print();//打印  
		obj.style.display="";  
	}
</script>
<style type="text/css">
 body
 {
 	font-size: 12px;
 }
 tr
 {
	line-height: 25px;
	text-align: center;
 }
 td
 {
 }
 div
 {
 	float: left;
 	width: 100%;
 }
</style>
</head>
<body>
  <form method="post" name ="fm" id="fm">
	<div style="width: 100%; min-height: 600px;" >
	  <c:choose> 
		<c:when test="${'res' eq pageType}">
		  <div style="text-align: center; width: 30%; line-height: 30px; font-size:20px;margin-left: 30%;">零&nbsp;&nbsp;&nbsp;&nbsp;售&nbsp;&nbsp;&nbsp;&nbsp;单</div>
		</c:when>
		<c:otherwise>
		  <div style="text-align: center; width: 30%; line-height: 30px; font-size:20px;margin-left: 30%;">领&nbsp;&nbsp;&nbsp;&nbsp;件&nbsp;&nbsp;&nbsp;&nbsp;单</div>
		</c:otherwise>
	  </c:choose>
	  <div style="width: 100%; line-height: 25px;">
		<div style="width: 35%;margin-left: 1%;">出库仓库：${map.WH_CNAME}</div>
		<div style="width: 30%;"> 
		<c:choose><c:when test="${'res' eq pageType}">零售</c:when><c:otherwise>领用</c:otherwise></c:choose>单号：${map.RETAIL_CODE}
		</div>
		<div style="width: 30%;"> 制单日期：${currDate}</div>
	  </div>
	  <div style="width: 100%; line-height: 25px;">
		<div style="width: 35%;margin-left: 1%;"><c:choose><c:when test="${'res' eq pageType}">采购单位</c:when><c:otherwise>领用人</c:otherwise></c:choose>：${map.LINKMAN}</div>
		<div style="width: 30%;"> 联系电话：${map.TEL}</div>
		<div style="width: 30%;"> 用途：${map.PURPOSE}</div>
	  </div>
	
	  <div  style="min-height: 300px;width: 100%;">
	    <table border="1px" style="width: 99%; height: 100%; color: black;" cellpadding="0px" cellspacing="0px">
		<tr>
			<td width="10%">
			货位
			</td>
		    <td width="10%">
		    配件编码
		    </td>
		    <td width="20%">
		     配件名称
		    </td>
		    <td width="15%">
		    件号
		    </td>
		    <td width="9%">
			   <c:choose> 
				<c:when test="${'res' eq pageType}">
				 零售数量
				</c:when>
				<c:otherwise>
				  领用数量
				</c:otherwise>
			   </c:choose>
		    </td>
		     <td width="9%">
		    实领数量
		    </td>
		    <td width="9%">
		    单位
		    </td>
		    <td width="9%">
		       <c:choose> 
				<c:when test="${'res' eq pageType}">
				 零售单价
				</c:when>
				<c:otherwise>
				  领用单价
				</c:otherwise>
			   </c:choose>
		    </td>
		    <td width="9%">
		       <c:choose> 
				<c:when test="${'res' eq pageType}">
				 零售金额
				</c:when>
				<c:otherwise>
				  领用金额
				</c:otherwise>
			   </c:choose>
		    </td>
		  </tr>
		  <c:if test="${list !=null}">
		  <c:forEach items="${list}" var="list" varStatus="_sequenceNum">
			<tr>
			  <td align="left">
			    &nbsp;${list.LOC_NAME}
			  </td>
			  <td align="left">
			    &nbsp;${list.PART_OLDCODE}
			  </td>
			  <td align="left">
			    &nbsp;${list.PART_CNAME}
			  </td>
			  <td align="left">
			    &nbsp;${list.PART_CODE}
			  </td>
			  <td>
			  	${list.QTY}
			  </td>
			  <td align="left"> 
			  &nbsp;
			  </td>
			  <td align="center">
			    ${list.UNIT}
			  </td>
			  <td align="right"> 
			  	${list.SALE_PRICE }&nbsp;
			  </td>
			  <td align="right"> 
			  	${list.SALE_AMOUNT }&nbsp;
			  </td>
			</tr>
		  </c:forEach>
		</c:if>
		</table>
	  </div>
	  <div style="margin-top: 50px; font-weight: bold;">
		<div style="width: 40%;"> 总金额(大写)：${chineseAmount }</div>
		<div style="width: 30%;"> 总金额(小写)：${totalAmount }</div>
		<div style="width: 20%;margin-left: 1%;">总数量：${totalCount }</div>
	  </div>
	  <div style="margin-top: 50px; font-weight: bold;;">
	    <div style="width: 40%;margin-left: 1%;">采购人：</div>
		<div style="width: 30%;"> 审核人：</div>
		<div style="width: 20%;"> 出库人：</div>
	  </div>
	  <div style="margin-top: 50px;">
	    <div style="text-align: center; margin-left: 30%; width: 30%;">
	      <input type=button class="txtToolBarButton" value="打  印" onclick="prtPage(this)">
	    </div>
	  </div>
	</div>
  </form>
</body>
</html>