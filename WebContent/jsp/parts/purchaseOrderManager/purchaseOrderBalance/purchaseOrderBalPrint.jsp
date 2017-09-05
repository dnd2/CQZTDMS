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
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <c:forEach items="${allList}" var="subList" varStatus="curSta">
    <c:if test="${curSta.last}">
    <div name=thisblock id=thisblock>
    </c:if>
    <c:if test="${!curSta.last}">
    <div name=thisblock id=thisblock class="PageNext">
    </c:if>
	<TABLE border=0 bordercolor=black cellpadding=3  cellspacing=0 width="91%" >	
	<br>
	<center>
	<font size="+1"><b>重庆君马新能源汽车有限公司配件结算单</b></font>
	</center>
	</TABLE>
	<br/>
	<table border=0  cellpadding=3 align="center" cellspacing=0 width="91%" >
		<tr>
			<td>
				结算单号：&nbsp;${mainMap.BALANCE_CODE}
			</td>
			<td>
				库房：&nbsp;${mainMap.WH_NAME}
			</td>
			<td>
				税率：&nbsp;<%=Constant.PART_TAX_RATE %>
			</td>
		</tr>
		<tr>
		    <td>
				供货商：&nbsp;${mainMap.VENDER_NAME}
			</td>
			 <td>
				发票号：&nbsp;${mainMap.INVO_NO}
			</td>
			<td>
				制单人：&nbsp;${mainMap.NAME}
			</td>
		</tr>
		<tr>
			<td>
				制单时间：&nbsp;<fmt:formatDate value="${mainMap.CREATE_DATE}" type="date" pattern="yyyy-MM-dd"/>
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				&nbsp;
			</td>
		</tr>
		
	</table>
	
	<br>
	<TABLE id="file" border=1 bordercolor=black cellpadding=3 align="center" cellspacing=0 width="91%" >
	    <tr align="center"> 
	        <td  colspan=1 width="1%">序号</td>
	        <td  colspan=1 width="7%">配件代码</td>
	        <td  colspan=1 width="10%">配件名称</td>
	        <td  colspan=1 width="7%">件号</td>
	        <td  colspan=1 width="3%">数量 </td>
	        <td  colspan=1 width="1%">单位</td>
	        <td  colspan=1 width="3%">不含税价</td>
	        <td  colspan=1 width="3%">采购单价</td>
	        <td  colspan=1 width="3%">采购金额</td>
            <td  colspan=1 width="3%">货位</td>
	        <td  colspan=1 width="5%">验收单号</td>
	        <td  colspan=1 width="3%">备注</td>
	    </tr>
	    <c:forEach items="${subList}" var="data">
	    <tr>
		    	<td>
		    		<script language="javascript">
						getIndex()
					</script>
		    	</td> 
		        <td >
		        	&nbsp;${data.PART_OLDCODE}
		        </td> 
		         <td >
		        	&nbsp;${data.PART_CNAME}
		        </td> 
		         <td>
		        	&nbsp;${data.PART_CODE}
		        </td> 
		         <td >
		        	&nbsp;${data.BAL_QTY}
		        </td> 
		         <td >
		        	&nbsp;${data.UNIT}
		        </td> 
		         <td >
		        	&nbsp;${data.BUY_PRICE_NOTAX}
		        </td> 
		         <td >
		        	&nbsp;${data.BUY_PRICE}
		        </td> 
		         <td >
		        	&nbsp;${data.BAL_AMOUNT}
		        </td>
                <td >
                    &nbsp;${data.LOC_NAME}
               </td>
		         <td >
		        	&nbsp;${data.CHECK_CODE}
		        </td> 
		         <td >
		        	&nbsp;${data.REMARK}
		        </td> 
	        </tr>
	    </c:forEach>	     
	</TABLE>
	</div>
    </c:forEach>
    <br>
	<TABLE border=0px align="center" width="91%">
	<tr >
			 <td align="left">
				总数量：&nbsp;${mainMap.BAL_QTY}
			</td>
			<td align="left">
				含税金额：&nbsp;${mainMap.BAL_AMOUNT}
			</td>
			<td align="left">
				计划金额：&nbsp;${mainMap.IN_AMOUNT}
			</td>
			 <td align="left">
				&nbsp;
			</td>
		</tr>
		<tr >
			 <td align="left">
				计划员确认：&nbsp;
			</td>
			 <td align="left">
				验收员确认：&nbsp;
			</td>
			 <td align="left">
				保管员确认：&nbsp;
			</td>
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