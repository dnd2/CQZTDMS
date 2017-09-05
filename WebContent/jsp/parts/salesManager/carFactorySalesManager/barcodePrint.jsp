<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%String contextPath = request.getContextPath();%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title></title>
</head>
<script language="javascript">
	function doPrint(){
		var partId = $('partId').value;
		var start = $('con').value;
		

		if(""==start){
			MyAlert("请打印张数!");
			return;
		}
		var re = /^[0-9]+[0-9]*]*$/;
		if(!re.test(start)){
			MyAlert("打印张数应为正整数!");
			return;
		}
		
		var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderInManager/selectPartId.json?partId=' + partId+'&start='+start;
        sendAjax(url, getResult, 'fm');
	}
	function getResult(jsonObj) {
	    //var vBarCode = jsonObj.vBarCode;
	    var vpoldcode = jsonObj.vpoldcode==null?"":jsonObj.vpoldcode;
	    var vpCname = jsonObj.vpCname==null?"":jsonObj.vpCname;
	    var vpEname = jsonObj.vpEname==null?"":jsonObj.vpEname;
	    var vpCode = jsonObj.vpCode==null?"":jsonObj.vpCode;
	    var vQty = jsonObj.vQty==null?"1":jsonObj.vQty
	    var start = jsonObj.start;

	    //for(var i=1;i<=start;i++){
            //MyAlert($('barCode').value);
	    	parentContainer.doPrint($('barCode').value,vpoldcode,vpCname,vpEname,vpCode,vQty,padLeft(start,4));
		//}
		_hide();
	}

	function padLeft(str,lenght){
	    if(str.length >= lenght)
	        return str;
	    else
	        return padLeft("0" +str,lenght);
	}
</script>
<body style="background-color:#87CEFA;">
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
<table width="100%" height="100%">
	<tr height="40px">
		
		<td>
			&nbsp;
		</td>
	</tr>
	
	<tr height="20px">
		
		<td>
			&nbsp;
			<input type="hidden" id="partId" name="partId" value="<%=request.getParameter("partId")%>"/>
			<input type="hidden" id="barCode" name="barCode" value="<%=request.getParameter("barCode")%>"/>
		</td>
	</tr>
	<tr>
		<td align="center">
			<font size=2>打印张数：</font>
		  	<input type="text" id="con" name="con" style="width:60px;text-align:center"/>
		</td>
	</tr>
</table>
<div style="position:absolute;top:150px;left:340px">
	<input id="btn" name="btn" type="button" value="确  定" style="width:60px" onclick="doPrint();"/>
	<input id="btn" name="btn" type="button" value="关  闭" style="width:60px" onclick="_hide()"/>
</div>
</form>
</body>
</html>