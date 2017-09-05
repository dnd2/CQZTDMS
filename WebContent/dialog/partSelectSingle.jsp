<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.common.FileConstant"%>

<%
    String contextPath = request.getContextPath();
    String inputCode = request.getParameter("INPUTCODE");
    String inputOldCode = request.getParameter("INPUTOLDCODE");
    String inputName = request.getParameter("INPUTNAME");
    String inputId = request.getParameter("INPUTID");
    String isMulti = request.getParameter("ISMULTI");
    String inputDefBuyPrice = request.getParameter("INPUTDEFBUYPRICE");
%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>通用选择框</title>
<script language="JavaScript">
	var myPage;

	var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/queryPartInfo.json";
				
	var title = null;

	var columns = [
				{header: "选择", dataIndex: 'PART_ID', align:'center',renderer:seled},
				{header: "配件代码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
				{header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
				{header: "配件件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
				{header: "备注", dataIndex: 'REMARK', align:'center', style: 'width: 100px;'}
    			];
		      
	function seled(value,meta,record){
		var data = record.data;
		var isMulti = "<%=isMulti%>";
 		if(isMulti == "false"){
 			return "<input type='radio' name='singleSel' id='singleSel"+value+"' value='"+ value + "||" +data.PART_CODE+"||" +data.PART_OLDCODE+"||" +data.PART_CNAME+"||" +data.DEF_BUY_PRICE+"' onclick='singleSelect(this,"+value+");' />";
 		}else{
        	return "<input type='checkbox' name='checkCode' id='checkCode' value='"+ value + "||" +data.PART_CODE+"||" +data.PART_OLDCODE+"||" +data.PART_CNAME+"||" +data.DEF_BUY_PRICE+"' />";
        }
    }

	var parentDocument = __parent().document;
    function singleSelect(obj,partId){
    	var array = obj.value.split("||");
    	if (parent.$('#inIframe')[0]) {
    		if(parentDocument.getElementById('PART_ID'))
    			parentDocument.getElementById('PART_ID').value = partId;
    		if(parentDocument.getElementById('PART_CODE'))
    			parentDocument.getElementById('PART_CODE').value = array[1];
    		if(parentDocument.getElementById('PART_OLDCODE'))
    			parentDocument.getElementById('PART_OLDCODE').value = array[2];
    		if(parentDocument.getElementById('PART_CNAME'))
    			parentDocument.getElementById('PART_CNAME').value = array[3];
    		if(parentDocument.getElementById('DEF_BUY_PRICE'))
    			parentDocument.getElementById('DEF_BUY_PRICE').value = array[4];
    	} else 
		{
			if(parent.$('PART_ID'))
				parent.$('PART_ID').value = partId;
			else if(window && window.dialogArguments)
			{
				window.dialogArguments.$('PART_ID').value = partId;
			}
			if(parent.$('PART_CODE'))
				parent.$('PART_CODE').value = array[1];
			else if(window && window.dialogArguments)
			{
				window.dialogArguments.$('PART_CODE').value = array[1];
			}
			if(parent.$('PART_OLDCODE'))
				parent.$('PART_OLDCODE').value = array[2];
			else if(window && window.dialogArguments)
			{
				window.dialogArguments.$('PART_OLDCODE').value = array[2];
			}
			if(parent.$('PART_CNAME'))
				parent.$('PART_CNAME').value = array[3];
			else if(window && window.dialogArguments)
			{
				window.dialogArguments.$('PART_CNAME').value = array[3];
			}
			if(parent.$('DEF_BUY_PRICE'))
				parent.$('DEF_BUY_PRICE').value = array[4];
			else if(window && window.dialogArguments)
			{
				window.dialogArguments.$('DEF_BUY_PRICE').value = array[4];
				try
				{
					window.dialogArguments.modalDialogWinBeforeClose();
				}catch(e){}	
			}
			if(window && window.dialogArguments)
			{
				try
				{
					window.dialogArguments.modalDialogWinBeforeClose();
				}catch(e){}	
			}
		}

    	 _hide();
    }
  //关闭弹出窗口的时候执行该方法
    function doSupp()
	{   
		var inputName = "<%=inputName%>";
		var inputId = "<%=inputId%>";
		var inputCode = "<%=inputCode%>";
		var inputOldCode = "<%=inputOldCode%>";
		var inputDefBuyPrice = "<%=inputDefBuyPrice%>";
		var partName = document.getElementById("PART_CNAME").value;
		var partId = document.getElementById("PART_ID").value;
		var partCode = document.getElementById("PART_CODE").value;
		var partOldCode = document.getElementById("PART_OLDCODE").value;
		var defBuyPrice = document.getElementById("DEF_BUY_PRICE").value;
		if(partName && partName.length > 0){
			if(parent.$('inIframe') == null){//修复不在inIframe中时参数赋值问题
				if(top.$(inputName)) {
					top.$(inputName).value = partName;
				}
			}else{
				parentDocument.getElementById(inputName).value = document.getElementById("PART_CNAME").value;
			}
		}
		if(partId && partId.length > 0){
			if(parent.$('inIframe') == null){//修复不在inIframe中时参数赋值问题
				if(top.$(inputId)) {
					top.$(inputId).value = partId;
				}
			}else{
				parentDocument.getElementById(inputId).value = document.getElementById("PART_ID").value;
			}
		}
		if(partCode && partCode.length > 0){
			if(parent.$('inIframe') == null){//修复不在inIframe中时参数赋值问题
				if(top.$(inputCode)) {
					top.$(inputCode).value = partCode;
				}
			}else{
				parentDocument.getElementById(inputCode).value = document.getElementById("PART_CODE").value;
			}
		}
		if(partOldCode && partOldCode.length > 0){
			if(parent.$('inIframe') == null){//修复不在inIframe中时参数赋值问题
				if(top.$(inputOldCode)) {
					top.$(inputOldCode).value = partOldCode;
				}
			}else{
				parentDocument.getElementById(inputOldCode).value = document.getElementById("PART_OLDCODE").value;
			}
		}
		if(defBuyPrice && defBuyPrice.length > 0){
			if(parent.$('inIframe') == null){//修复不在inIframe中时参数赋值问题
				if(top.$(inputDefBuyPrice)) {
					top.$(inputDefBuyPrice).value = defBuyPrice;
				}
			}else{
				parentDocument.getElementById(inputDefBuyPrice).value = document.getElementById("DEF_BUY_PRICE").value;
			}
		}
	}
	
	
	function doDisAllClick()
	{
		var chk = document.getElementsByName("checkCode");
		var l = chk.length;
		for(var i=0;i<l;i++)
		{        
			chk[i].checked = false;
		}
	}

	function checkAll(){
		var groupCheckBoxs=document.getElementsByName("checkCode");
		if(!groupCheckBoxs) return;
		for(var i=0;i<groupCheckBoxs.length;i++)
		{
			groupCheckBoxs[i].checked=true;
		}
	}
	
	function doConfirm()
	{
		var chk = document.getElementsByName("checkCode");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++)
		{        
			if(chk[i].checked)
			{            
				cnt++;			
			}
		}
        if(cnt==0)
        {
             MyDivAlert("请选择配件！");
        }else{
	        var codes = "";
	        var OldCodes = "";
	        var names = "";
			var ids = "";
	        for(var i=0;i<l;i++)
			{        
				if(chk[i].checked)
				{
					if(chk[i].value)
					{
						var arr = chk[i].value.split("||");
						if(ids)
						ids += "," + arr[0];
				    	else
				        ids = arr[0];
				        if(codes)
						codes += "," + arr[1];
				    	else
				        codes = arr[1];
				        if(OldCodes)
				        OldCodes += "," + arr[2];
				    	else
				    	OldCodes = arr[2];
				        if(names)
				        	names += "," + arr[3];
				    	else
				    		names = arr[3];
				    }    
				}				
			}
	
			if(codes && codes.length > 0){
			   $('PART_CODE').value = codes;
			}   
			if(OldCodes && OldCodes.length > 0){
			   $('PART_OLDCODE').value = OldCodes;
			}   
			if(names && names.length > 0){
			   $('PART_CNAME').value = names;
			}   
			if(ids && ids.length > 0){
			   $('PART_ID').value = ids;
			}
			_hide();
		}
	}

$(document).ready(function(){
	__extQuery__(1);
	var isMulti="<%=isMulti%>";
   	if(isMulti == "true"){
    	document.getElementById("sel").style.display = "";
   	}else{  
   		document.getElementById("sel").style.display = "none";
   	}	
});
</script>
</head>
<body onbeforeunload="doSupp();">
	<div class="navigation">
		<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 选择配件
	</div>
	<form method="post" name="fm" id="fm">
		<input id="PART_CNAME" name="PART_CNAME" type="hidden" value="" />
		<input id="PART_ID" name="PART_ID" type="hidden" value="" />
		<input id="PART_CODE" name="PART_CODE" type="hidden" value="" />
		<input id="PART_OLDCODE" name="PART_OLDCODE" type="hidden" value="" />
		<input id="DEF_BUY_PRICE" name="DEF_BUY_PRICE" type="hidden" value="" />

		<div class="form-panel">
			<h2>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 查询条件
			</h2>
			<div class="form-body">
				<table class="table_edit">
					<tr>
						<td class="rigth">配件编码：</td>
						<td class="rigth">
							<input class="middle_txt" id="partOldCode" name="partOldCode" type="text" value="" />
						</td>
						<td class="rigth">配件名称：</td>
						<td>
							<input class="middle_txt" id="partCname" name="partCname" type="text" />
						</td>
						<td>
							<input type="button" name="queryBtn" id="queryBtn" value="查询" class="u-button" onClick="__extQuery__(1);">
						</td>
					</tr>
				</table>
			</div>
		</div>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
	<div style="margin-top: 25px; float: left" id="sel">
		<input name="queren1" type="button" class="u-button" onclick="checkAll();" value="全选" />
		<input name="queren2" class="u-button" type="button" value="全不选" onclick="doDisAllClick()" />
		<input name="queren3" type="button" class="u-button" onclick="doConfirm();" value="确认" />
		<input class="u-button" type="button" value="关闭" onclick="parent._hide()" />
	</div>
</body>
</html>