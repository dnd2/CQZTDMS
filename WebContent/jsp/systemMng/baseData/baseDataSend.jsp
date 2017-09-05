<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>主数据下发</title>
</head>
<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>主数据维护>主数据下发</div>
<form name="fm" id="fm">
<div>
	<table class="table_query">
		<tr>            
        	<td class="table_query_2Col_label_5Letter">经销商代码：</td>            
        	<td>
			<textarea rows="2" cols="53" id="dealerCode" name="dealerCode"></textarea>
		     	<input name="button1" type="button" class="mini_btn" style="cursor: pointer;"  onclick="showOrgDealer('dealerCode','','true');" value="..." />        
		     	<input name="button2" type="button" class="normal_btn" onclick="clr();" value="清除"/>
        	</td>   
       	</tr>
	</table>	
	<input type="button" id="part" value="配件全量下发" onclick="sendPart()" class="long_btn"/>
	<input type="button" id="partAdd" value="新增配件下发" onclick="sendPartAdd()" class="long_btn"/>
	<input type="hidden" id="partFlag" name="partFlag" value="" />
	<input type="button" id="materialGroup" value="物料组下发" onclick="sendMaterialGroup()" class="long_btn"/>
	<input type="button" id="material" value="物料下发" onclick="sendMaterial()" class="long_btn"/>
	<input type="button" id="urlFunc" value="功能列表下发" onclick="sendUrlFunc()" class="long_btn"/>
	<input type="button" id="error" value="发送失败的经销商" onclick="exportError()" class="long_btn"/>
</div>
</form>
</body>
</html>
<script type="text/javascript" >
	//全量下发配件
	function sendPart() {
		if (!validate()) {
			return;
		}
		document.getElementById("partFlag").value = "0";
		var url = '<%=contextPath%>/sysmng/sysData/SendData/sendPart.json';
		makeNomalFormCall(url ,showBack, 'fm', 'queryBtn');
	}
	//下发新增配件
	function sendPartAdd() {
		if (!validate()) {
			return;
		}
		document.getElementById("partFlag").value = "1";
		var url = '<%=contextPath%>/sysmng/sysData/SendData/sendPart.json';
		makeNomalFormCall(url ,showBack, 'fm', 'queryBtn');
	}
	
	function showBack() {
		enableBtn();
		MyAlert('下发成功');
	}
	//下发物料组
	function sendMaterialGroup() {
		if (!validate()) {
			return;
		}
		var url = '<%=contextPath%>/sysmng/sysData/SendData/sendMaterialGroup.json';
		makeNomalFormCall(url ,showBack, 'fm', 'dd');
	}
	//下发物料
	function sendMaterial() {
		if (!validate()) {
			return;
		}
		var url = '<%=contextPath%>/sysmng/sysData/SendData/sendMaterial.json';
		makeNomalFormCall(url ,showBack, 'fm');
	}
	//下发DCS端url功能列表
	function sendUrlFunc() {
		if (!validate()) {
			return;
		}
		var url = '<%=contextPath%>/sysmng/sysData/SendData/sendUrlFunc.json';
		makeNomalFormCall(url ,showBack, 'fm');
	}
	
	function validate() {
		var dealerCodes = document.getElementById("dealerCode").value;
		if (!dealerCodes) {
			MyAlert('请选择经销商');
			return;
		}
		disableBtn();
		return true;
	}
	
	function exportError() {
		var url = '<%=contextPath%>/sysmng/sysData/SendData/expError.do';
		fm.action = url
    	fm.submit();
	}

	function disableBtn() {
		document.getElementById("part").disabled = true;
		document.getElementById("materialGroup").disabled = true;
		document.getElementById("material").disabled = true;
		document.getElementById("urlFunc").disabled = true;
		document.getElementById("error").disabled = true;
	}
	function enableBtn() {
		document.getElementById("part").disabled = false;
		document.getElementById("materialGroup").disabled = false;
		document.getElementById("material").disabled = false;
		document.getElementById("urlFunc").disabled = false;
		document.getElementById("error").disabled = false;
	}
	//清空经销商文本框
	function clr() {
		document.getElementById('dealerCode').value = "";
  	}
</script>