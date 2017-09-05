<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title></title>
<style type="text/css">
html,body{font-size:12px;margin:0px;height:100%;}
.mesWindow{border:#666 1px solid;background:#fff;}
.mesWindowTop{border-bottom:#eee 1px solid;margin-left:4px;padding:3px;font-weight:bold;text-align:left;font-size:12px;}
.mesWindowContent{margin:4px;font-size:12px;}
.mesWindow .close{height:15px;width:28px;border:none;cursor:pointer;text-decoration:underline;background:#fff}
</style>
<style media=print>   
.Noprint{display:none;}  .PageNext{page-break-after: always;}   
</style>
</head>
<script language="javascript">
//获取选择框的值
	function getCode(value){
		var str = getItemValue(value);
	
		document.write(str);
	}

	var idx = 0;
	function getIndex(){
		idx+=1
		document.write(idx);
	}
</script>
<body>
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
 
    <c:if test="${curSta.last}">
    <div name=thisblock id=thisblock>
    </c:if>
    <c:if test="${!curSta.last}">
    <div name=thisblock id=thisblock class="PageNext">
    </c:if>
	<TABLE border=0 bordercolor=black cellpadding=3  cellspacing=0 width="91%" >	
	<br>
	<center>
	<font size="+1"><b>重庆君马新能源汽车有限公司入库单</b></font>
	</center>
	</TABLE>
	<br/>
	<br>
	<TABLE border=1 bordercolor=black cellpadding=3 align="center" cellspacing=0 width="91%" >
	    <tr align="center"> 
	        <td  colspan=1 width="1%">序号</td>
	        <td  colspan=1 width="2%">订单号</td>
	        <td  colspan=1 width="2%">入库单号</td>
	        <td  colspan=1 width="8%">配件代码</td>
	        <td  colspan=1 width="10%">配件名称</td>
	        <td  colspan=1 width="8%">配件件号</td>
	        <td  colspan=1 width="1%">单位</td>
	        <td  colspan=1 width="1%">采购数量</td>
	        <td  colspan=1 width="1%">入库数量</td>
	        <td  colspan=1 width="10%">供应商</td>
	        <td  colspan=1 width="1%">批次号</td>
	        <td  colspan=1 width="8%">库房</td>
	        <td  colspan=1 width="6%">货位</td>
	        <td  colspan=1 width="1%">税率</td>
	        <td  colspan=1 width="2%">无税单价</td>
	        <td  colspan=1 width="2%">无税金额</td>
	        <td  colspan=1 width="7%">备注</td>
	    </tr>
	    <c:forEach items="${listPo}" var="data">
	    <tr>
		    	<td>
		    		<script language="javascript">
						getIndex()
					</script>
		    	</td> 
		        <td >
		        	&nbsp;${data.ORDER_CODE}
		        </td> 
		         <td >
		        	&nbsp;${data.IN_CODE}
		        </td> 
		         <td>
		        	&nbsp;${data.PART_OLDCODE}
		        </td> 
		         <td >
		        	&nbsp;${data.PART_CNAME}
		        </td> 
		         <td >
		        	&nbsp;${data.PART_CODE}
		        </td> 
		         <td >
		        	&nbsp;${data.UNIT}
		        </td> 
		         <td style="text-align: center;">
		        	&nbsp;${data.BUY_QTY}
		        </td> 
		         <td style="text-align: center;">
		        	&nbsp;${data.IN_QTY}
		        </td>
                <td >
                    &nbsp;${data.VENDER_NAME}
               </td>
		         <td >
		        	&nbsp;${data.BATCH_NO}
		        </td> 
		         <td >
		        	&nbsp;${data.WH_NAME}
		        </td> 
		         <td >
		        	&nbsp;${data.LOC_CODE}
		        </td> 
		         <td >
		        	&nbsp;${data.TAX_RATE}
		        </td> 
		         <td >
		        	&nbsp;${data.BUY_PRICE_NOTAX}
		        </td> 
		         <td >
		        	&nbsp;${data.IN_AMOUNT_NOTAX}
		        </td>
		         <td >
		        	&nbsp;${data.REMARK}
		        </td>  
	        </tr>
	    </c:forEach>	     
	</TABLE>
	</div>
    <br>
	<TABLE border=0px align="center" width="91%">
			<tr>
			<td align="right">
				制单人：
			</td>
			<td>
			&nbsp;${userName}
			</td>
			<td align="right">
				制单时间：
			</td>
			<td>
			&nbsp;<fmt:formatDate value="${userDate}" type="date" pattern="yyyy年MM月dd日"/>
			</td>
		</tr>
		<tr >
			
			 <td align="right">
				入库员确认：
			</td>
			<td>&nbsp;</td>
			<td align="right">
				确认时间：
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日</td>
		</tr>
	</TABLE>
	<table align="center" class="Noprint page-print-buttons">  
	<tr>  
		<td align="center">
		<input type=button value="打印">
		<input type=button value="打印设置">
		<input type=button value="打印预览">
		</td>  
	</tr>
  </table>
</form>
</body>
</html>