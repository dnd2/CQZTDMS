<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
    String inputCode = request.getParameter("INPUTCODE");
    String inputOldCode = request.getParameter("INPUTOLDCODE");
    String inputName = request.getParameter("INPUTNAME");
    String inputId = request.getParameter("INPUTID");
    String isMulti = request.getParameter("ISMULTI");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>通用选择框</title>
<style>.select-button{margin: 25px 0;text-align: center}</style>
</head>
<body onload="__extQuery__(1);" onbeforeunload="doSupp();">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 选择配件</div>
<form method="post" name ="fm" id="fm">
	<input id="PART_CNAME" name="PART_CNAME" type="hidden" value="" />
	<input id="PART_ID" name="PART_ID" type="hidden" value=""/>	
	<input id="PART_CODE" name="PART_CODE" type="hidden" value=""/>	
	<input id="PART_OLDCODE" name="PART_OLDCODE" type="hidden" value=""/>	
	<table class="table_edit">
    <tr>
      <td   align="right" nowrap="nowrap">配件编码：</td>
      <td class="table_query_3Col_input" nowrap="nowrap">
      	<input class="middle_txt" id="partOldCode" name="partOldCode" value="" type="text"/>
      </td>
      <td   align="right" nowrap="nowrap">配件名称：</td>
      <td>
      	<input name="partCname" type="text" id="partCname"  class="middle_txt"/>
      </td>
      <td>&nbsp;</td>
      <td>
      	<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="u-button u-query" onClick="__extQuery__(1);" >
      </td>
  	</tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<div class="select-button" id="sel">
   <input name="queren1" type="button" class="cssbutton u-button" onclick="checkAll();" value="全选" />
   <input class="cssbutton u-button" type="button" name ="queren2" value="全不选" onclick="doDisAllClick()"/>
   <input name="queren3" type="button" class="cssbutton u-button u-submit" onclick="doConfirm();" value="确认" />
</div>
<script language="JavaScript">
		var isMulti="<%=isMulti%>";
	    	if(isMulti == "true"){
		    	document.getElementById("sel").style.display = "";
	    	}
	    	else
	    	{  
	    		document.getElementById("sel").style.display = "none";
	    	}	

	var myPage;

	var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/queryPartInfo.json";
				
	var title = null;

	var columns = [
				{header: "选择", dataIndex: 'PART_ID', align:'center',renderer:seled},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: left;'},
				{header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: left;'},
				{header: "配件件号", dataIndex: 'PART_CODE', style: 'text-align: left;'},
				{header: "备注", dataIndex: 'REMARK', style: 'text-align: left;'}
		      ];
		      
	function seled(value,meta,record){
		var data = record.data;
		var isMulti = "<%=isMulti%>";
 		if(isMulti == "false"){
 			return "<input type='radio' onclick='singleSelect(\""+value+"\",\""+data.PART_CODE+"\",\""+data.PART_OLDCODE+"\",\""+data.PART_CNAME+"\")'/>";
 		}else{
        	return "<input type='checkbox' name='checkCode' id='checkCode' value='"+ value + "||" +data.PART_CODE+"||" +data.PART_OLDCODE+"||" +data.PART_CNAME+"' />";
        }
    }
    
    function singleSelect(val1,val2,val3,val4){
    	 $('#PART_ID')[0].value = val1;
    	 $('#PART_CODE')[0].value = val2;
    	 $('#PART_OLDCODE')[0].value = val3;
    	 $('#PART_CNAME')[0].value = val4;
    	_hide();
    }
  //关闭弹出窗口的时候执行该方法
    function doSupp()
	{   
		var inputName = "<%=inputName%>";
		var inputId = "<%=inputId%>";
		var inputCode = "<%=inputCode%>";
		var inputOldCode = "<%=inputOldCode%>";
		var partName = document.getElementById("PART_CNAME").value;
		var partId = document.getElementById("PART_ID").value;
		var partCode = document.getElementById("PART_CODE").value;
		var partOldCode = document.getElementById("PART_OLDCODE").value;
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
             layer.msg("请选择配件!", {icon: 15});
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
			   $('#PART_CODE')[0].value = codes;
			}   
			if(OldCodes && OldCodes.length > 0){
			   $('#PART_OLDCODE')[0].value = OldCodes;
			}   
			if(names && names.length > 0){
			   $('#PART_CNAME')[0].value = names;
			}   
			if(ids && ids.length > 0){
			   $('#PART_ID')[0].value = ids;
			}
			var flag = parentContainer.setPartDiscountDtl(ids,OldCodes,names,codes);
			if(flag){
				_hide();
			}
		}
	}
</script>
</body>
</html>