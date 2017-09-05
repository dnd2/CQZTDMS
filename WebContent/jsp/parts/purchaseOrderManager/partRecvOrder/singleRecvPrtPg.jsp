<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
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
</head>
<script language="javascript">
//获取选择框的值
	function getCode(value){
		var str = getItemValue(value);
	
		document.write(str);
	}
function getIndex(){
	document.write(document.getElementById("file").rows.length-1);
}

function printOrder(){
	$("printBtn").style.display="none";
	window.print();
	$("printBtn").style.display="";
}
</script>
<body onload="">
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
<input type=textarea name=datapackager style="display:none" value=''>	
	<div name=thisblock id=thisblock>
	<TABLE border=0 bordercolor=black cellpadding=3  cellspacing=0 width="91%" >	
	<br><center><font size="+1"><b>
        	北汽幻速汽车销售有限公司配件计划领件单
	</b></font></center>
	</TABLE><br/>
	<table border=0  cellpadding=3 align="center" cellspacing=0 width="91%" >
		<tr>
			<td>
				领&nbsp;用&nbsp;单&nbsp;号：&nbsp;${mainMap.ORDER_CODE}
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				日&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;期：&nbsp;${mainMap.CREATE_DATE}
			</td>
		</tr>
		<tr>
			<td>
				供&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;方：&nbsp;${venderMap.VENDER_NAME}
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				需&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;方：&nbsp;${billMap.DEALER_NAME}
			</td>
		</tr>
		<tr>
			<td>
				联&nbsp;&nbsp;&nbsp;系&nbsp;&nbsp;&nbsp;人：&nbsp;${venderMap.LINKMAN}
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				联系地址：&nbsp;${billMap.ADDR}
			</td>
		</tr>
		<tr>
			<td>
				联&nbsp;系&nbsp;电&nbsp;话：&nbsp;${venderMap.TEL}
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				联系电话：&nbsp;${billMap.TEL} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;联系人：&nbsp;
			</td>
		</tr>
		<tr>
			<td>
				传&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;真：&nbsp;${venderMap.FAX}
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				开&nbsp;&nbsp;户&nbsp;行：&nbsp;${billMap.BANK}
			</td>
		</tr>
		<tr>
			<td>
				
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				税&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：&nbsp;${billMap.TAX_NO}
			</td>
		</tr>
		<tr>
			<td>
				
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：&nbsp;${billMap.ACCOUNT}
			</td>
		</tr>
		
	</table><br>
	<TABLE id="file" border=1 bordercolor=black cellpadding=3 align="center" cellspacing=0 width="91%" >
	    <tr align="center"> 
	        <td  colspan=1>编号</td> 
	        <td  colspan=1>代码</td> 
	        <td  colspan=1>名称 </td>
	        <td  colspan=1>件号</td>
	        <td  colspan=1>单位</td>
	        <td  colspan=1>数量 </td>
	        <td  colspan=1>要求到货期 </td>
	        <td  colspan=1>备注</td>
	    </tr>
	    <c:forEach items="${detailList}" var="data" >
	    	<tr align="center"> 
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
		        	&nbsp;${data.UNIT}
		        </td> 
		         <td >
		        	&nbsp;${data.BUY_QTY}
		        </td> 
		         <td >
		        	&nbsp;${data.FORECAST_DATE}
		        </td> 
		         <td >
		        	&nbsp;${data.REMARK}
		        </td> 
	        </tr>
	    </c:forEach>	     
	</TABLE>
	</div><br>
	<table border=0px align="center" width="91%">
	 <tr >
	    <td align="left" colspan="4">要求：</td>
	    </tr>
	    <tr>
	    <td align="left" colspan="4">一、送货时必须有携带本传真件或送货单（送货单上需注明我公司配件编号），铁路发运需注明发货箱数、每箱品数及数量；</td>
	    </tr>
	    <tr>
	    <td align="left" colspan="4">二、产品必须有固定的包装，必须标明产品生产厂家及其地址；必须开具合格证；</td>
	    </tr>
	    <tr>
	    <td align="left" colspan="4">三、收货地址：江西昌河汽车销售公司配件处零配件总库；</td>
	    </tr>
	    <tr>
	    <td align="left" colspan="4">四、保管员收到货后与供方办理签收手续（铁路运输请与运输处办理签收手续），并通知计划员办理进货单。</td>
	    </tr>
	</table><br>
	<TABLE border=0px align="center" width="91%">
		<tr >
			 <td align="left">
				编制：&nbsp;
			</td>
			 <td align="left">
				审核：&nbsp;
			</td>
			 <td align="left">
				批准：&nbsp;
			</td>
			 <td align="left">
				库房签收：&nbsp;
			</td>
		</tr>
		<tr align="center">
						<td colspan="3">
							<input type=button id="printBtn" class="txtToolBarButton" value="打印" onClick="printOrder();">
						</td>
</tr>
	</TABLE>
</form>
</body>
</html>