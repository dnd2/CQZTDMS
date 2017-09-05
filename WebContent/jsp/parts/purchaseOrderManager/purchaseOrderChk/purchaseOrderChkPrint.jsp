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
</script>
<body onload="">
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
<input type=textarea name=datapackager style="display:none" value=''>	
	<div name=thisblock id=thisblock>
	<TABLE border=0 bordercolor=black cellpadding=3  cellspacing=0 width="90%" >	
	<br><center><font size="+1"><b>
        	北汽幻速汽车销售有限公司配件验货单
	</b></font></center>
	</TABLE>
	<table border=0  cellpadding=3 align="center" cellspacing=0 width="91%" >
		<tr>
			<td>
				验货单号：
			</td>
			<td>
				&nbsp;${dataMap.mainMap.chkCode}
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				采购订单号：
			</td>
			<td>
				&nbsp;${dataMap.mainMap.orderCode}
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				库房：
			</td>
			<td>
				&nbsp;${dataMap.mainMap.whName}
			</td>		
		</tr>
		<tr>
			<td>
				供货商：
			</td>
			<td>
				&nbsp;${dataMap.mainMap.venderName}
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				税率：
			</td>
			<td>
				&nbsp;${dataMap.mainMap.rate}
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				&nbsp;
			</td>		
			<td>
				制单人：
			</td>
			<td>
				&nbsp;${dataMap.mainMap.name}
			</td>	
		</tr>
		<tr>
			<td>
				供货厂家：
			</td>
			<td>
				&nbsp;${dataMap.mainMap.makerName}
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				&nbsp;
			</td>		
			<td>
				时间：
			</td>
			<td>
				&nbsp;${dataMap.mainMap.date}
			</td>	
		</tr>
	</table>
	<TABLE id="file" border=1 bordercolor=black cellpadding=3 align="center" cellspacing=0 width="91%" >
	    <tr> 
	        <td  colspan=1>序号</td> 
	        <td  colspan=1>配件代码</td> 
	        <td  colspan=1>配件名称 </td>
	        <td  colspan=1>件号</td>
	        <td  colspan=1>数量 </td>
	        <td  colspan=1>单位</td>
	        <td  colspan=1>不含税价 </td>
	        <td  colspan=1>计划单价</td>
	        <td  colspan=1>计划金额</td>
	        <td  colspan=1>货位</td>
	    </tr>
	    <c:forEach items="${dataMap.detailList}" var="data" >
	    	<tr> 
		    	<td>
		    		<script language="javascript">
						getIndex()
					</script>
		    	</td> 
		        <td>
		        	&nbsp;${data.PART_CODE}
		        </td> 
		        <td >
		        	&nbsp;${data.PART_OLDCODE}
		        </td> 
		        <td >
		        	&nbsp;${data.PART_CNAME}
		        </td> 
		         <td >
		        	&nbsp;
					${data.GENERATE_QTY}
		        </td> 
		         <td >
		        	&nbsp;${data.UNIT}
		        </td> 
		         <td >
		        	&nbsp;${data.PLAN_PRICE}
		        </td> 
		         <td >
		        	&nbsp;${data.PLAN_PRICE}
		        </td> 
		         <td >
		        	&nbsp;${data.FAMOUNT}
		        </td> 
		         <td >
		        	&nbsp;${data.LOC_NAME}
		        </td> 
	        </tr>
	    </c:forEach>	     
	</TABLE>
	</div>
	<TABLE border=0px  cellpadding=3 align="center" cellspacing=8 width="91%" style="position:absolute;bottom:0;right:5px;">
	    
		<tr>
			 <td width="100px" align="left">
				总数量：&nbsp;${dataMap.mainMap.allQty}
			</td>
			 <td width="150px" align="left">
				含税金额：&nbsp;${dataMap.mainMap.ramount}
			</td>
			 <td width="200px" align="left">
				计划金额：&nbsp;${dataMap.mainMap.amount}
			</td>
		</tr>
		  <tr> 
	         <td width="100px" align="left">
				计划员确认：
			 </td>
			 <td width="150px" align="left">
				验收员确认：
			 </td>
			 <td width="200px" align="left">
				保管员确认：
			 </td>
		</tr>	
		<tr align="right">
						<td>
							<input type=button class="txtToolBarButton" value="打印" onClick="window.print()">
						</td>
</tr>
	</TABLE>
</form>
</body>
</html>