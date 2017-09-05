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
function printOrder(){
	var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/printPkgOrder.json?";
	$('printBtn').disabled = true;
	sendAjax(url, getResult, 'fm');
}
function getResult(jsonObj){
	$('printBtn').disabled = false;
	if(jsonObj!=null){
	  	var error = jsonObj.error;
	    var exceptions = jsonObj.Exception;
	    if(error){
	    	MyAlert(error);
	    	_hide();
	    	return;
	    }else if(exceptions){
	    	MyAlert(exceptions.message);
	    	_hide();
	    	return;
		}
	  window.print();
	  MyAlert("打印去向头！");
	  var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/printQxt.json?";
	  sendAjax(url, getQxtResult, 'fm');
	  _hide();
	}
}
function getQxtResult(jsonObj){
	MyAlert();
}

</script>
</head>
<body onload="">
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
<input name="pickOrderId" id="pickOrderId" value="${dataMap.pickOrderId}" type="hidden" />
<input type=textarea name=datapackager style="display:none" value=''>	
	<div name=thisblock id=thisblock>
	<TABLE border=0  cellpadding=3 align="center" cellspacing=0 width="91%" height="20px">
		<tr>
			<td>
				&nbsp;
			</td>
		</tr>
	</TABLE>
	<TABLE border=0 bordercolor=black cellpadding=3 align="center" cellspacing=5 style="border-top:#aedef2 thin solid" width="90%" >	
		<br><center><font size="+2"><b>
	        	昌河汽车装箱清单
		</b></font></center>
	</TABLE>
	<TABLE border=0  cellpadding=3 align="center" cellspacing=0 width="91%" height="20px">
		<tr>
			<td>
				&nbsp;
			</td>
		</tr>
	</TABLE>
	<table border=0  cellpadding=3 align="center" cellspacing=5 width="91%" >
		<tr>
			<td width="33%">
				购货单位：&nbsp;${mainMap.DEALER_NAME}
			</td>
			
				
			<td width="33%">
				电话：&nbsp;${mainMap.TEL}
			</td>
			
			<td width="33%">
				物流方式：&nbsp;${mainMap.transType}
			</td>
			
		</tr>
		<tr>
			<td width="33%">
				出库库房：&nbsp;${mainMap.whName}
			</td >
			
			
			
			
			<td width="33%">
				制单时间：&nbsp;${mainMap.CREATE_DATE}
			</td>
			
			<td width="33%">
				
			</td>	
		</tr>
	</table>
	<TABLE border=0  cellpadding=3 align="center" cellspacing=0 width="91%" height="20px">
		<tr>
			<td>
				&nbsp;
			</td>
		</tr>
	</TABLE>
	<TABLE id="file" border=1 bordercolor=black cellpadding=3 align="center" cellspacing=0 width="91%" >
	    <tr> 
	        <td width=3% style="text-align: center" colspan=1>序号</td> 
	        <td width=10% style="text-align: center" colspan=1>箱号 </td>
	        <td width=15% style="text-align: center" colspan=1>配件编码</td> 
	        <td width=10% style="text-align: center" colspan=1>配件名称 </td>
	        <td width=10% style="text-align: center" colspan=1> 装箱数量 </td>
	        <td width=10% style="text-align: center" colspan=1> 件数 </td>
	       
	    </tr>
	    <c:forEach items="${detailList}" var="data" >
	    	<tr> 
		    	<td style="text-align: center">  
		    		<script language="javascript">
						getIndex()
					</script>
		    	</td> 
		        <td style="text-align: center" >
		        	&nbsp;${data.PKG_NO}
		        </td>
		        <td style="text-align: center" >
		        	<font size="4" >
		        		&nbsp;${data.PART_OLDCODE}
		        	</font>
		        </td> 
		          <td style="text-align: center" >
		        	&nbsp;${data.PART_CNAME}
		        </td> 
		        <td style="text-align: center" >
		        	<font size="4" >
		        		&nbsp;${data.QTY}
		        	</font>
		        </td> 
		        <td style="text-align: center" >
		        	&nbsp;${data.BOXQTY}
		        </td> 
		       
		        
	        </tr>
	    </c:forEach>	     
	</TABLE>
	<TABLE border=0  cellpadding=3 align="center" cellspacing=0 width="91%" height="50px">
		<tr>
			<td>
				&nbsp;
			</td>
		</tr>
	</TABLE>
	
			 
		</tr>	
	</TABLE>
	</div>
	<SPAN width="90%" ID="PRINT">
	<TABLE border=0 cellpadding=0 cellspacing=0 height=30>
		<tr>
			<td width=1000px>
				&nbsp;
			</td>
			<td width=5%>
				<table border=0 width=100%>
					<tr>
						<td>
							<input type=button id="printBtn" class="txtToolBarButton" value="打印" onClick="printOrder()">
							
						</td>
						<td>
							<input type=button id="printBtn" class="txtToolBarButton" value="关闭" onClick="_hide();">
						</td>
					</tr>
				</table>
			</td>
			<td width=5%>
				&nbsp;
			</td>
		</tr>
	</TABLE>
	</SPAN>
</form>
</form>
</body>
</html>