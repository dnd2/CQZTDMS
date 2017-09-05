<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
    String whId = request.getParameter("whId");
    request.setAttribute("whIdStr",whId);
    String spcpdType = request.getParameter("spcpdType");
    request.setAttribute("spcpdType",spcpdType);
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>通用选择框</title>

<script language="JavaScript">

	function doInit()
	{
		__extQuery__(1);
	}

</script>
</head>
<body onbeforeunload="doSupp();">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 选择总成件</div>
<form method="post" name ="fm" id="fm">
    <input id="whId" name="whId" type="hidden" value="${whIdStr }" />
    <input id="spcpdType" name="spcpdType" type="hidden" value="${spcpdType }" />
    <input id="PART_CNAME" name="PART_CNAME" type="hidden" value="" />
	<input id="PART_ID" name="PART_ID" type="hidden" value=""/>	
	<input id="PART_CODE" name="PART_CODE" type="hidden" value=""/>	
	<input id="PART_OLDCODE" name="PART_OLDCODE" type="hidden" value=""/>
	<input id="NORMAL_QTY" name="NORMAL_QTY" type="hidden" value=""/>
	<input id="UNIT" name="UNIT" type="hidden" value=""/>
	<input id="LOC_ID" name="LOC_ID" type="hidden" value=""/>
	<input id="LOC_CODE" name="LOC_CODE" type="hidden" value=""/>
	<input id="LOC_NAME" name="LOC_NAME" type="hidden" value=""/>
	<table class="table_edit">
    <tr>
      <td   align="right" nowrap="nowrap">配件编码：</td>
      <td class="table_query_3Col_input" nowrap="nowrap">
      	<input class="middle_txt" id="partOldCode" name="partOldCode"  type="text"/>
      </td>
      <td   align="right" nowrap="nowrap">配件名称：</td>
      <td>
      	<input name="partCname" type="text" id="partCname"  class="middle_txt"/>
      </td>
      <td>&nbsp;</td>
      <td>
      	<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1);" >
      </td>
  	</tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script type="text/javascript" >
	var myPage;

	var url = "<%=contextPath%>/parts/storageManager/partSplitManager/PartSpiltApplyManager/queryPartInfoByWhId.json";
				
	var title = null;

	var columns = [
				{header: "选择", dataIndex: 'PART_ID', align:'center',renderer:seled},
				{header: "配件件号", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_CNAME', align:'center'},
				{header: "备注", dataIndex: 'REMARK', align:'center'}
		      ];
		      
	function seled(value,meta,record){
		var data = record.data;
 		return "<input type='radio' name='singleSel' id='singleSel"+value+"' value='"+value+"||"+data.PART_CODE+"||"+data.PART_OLDCODE+"||"+data.PART_CNAME+"||"+data.NORMAL_QTY+"||"+data.UNIT+"||"+data.LOC_ID+"||"+data.LOC_CODE+"||"+data.LOC_NAME+"' onclick='singleSelect(this)'/>";
    }
    
    function singleSelect(obj){
         var array = obj.value.split("||");
    	 $('PART_ID').value = array[0];
    	 $('PART_CODE').value = array[1];
    	 $('PART_OLDCODE').value = array[2];
    	 $('PART_CNAME').value = array[3];
    	 $('NORMAL_QTY').value = array[4];
    	 $('UNIT').value = array[5];
    	 $('LOC_ID').value = array[6];
    	 $('LOC_CODE').value = array[7];
    	 $('LOC_NAME').value = array[8];
    	 _hide();
    }
  //关闭弹出窗口的时候执行该方法
    function doSupp()
	{   
		var partName = document.getElementById("PART_CNAME").value;
		var partId = document.getElementById("PART_ID").value;
		var partCode = document.getElementById("PART_CODE").value;
		var partOldCode = document.getElementById("PART_OLDCODE").value;
		var normalQty = document.getElementById("NORMAL_QTY").value;
		var unit = document.getElementById("UNIT").value;
		var locid = document.getElementById("LOC_ID").value;
		var locname = document.getElementById("LOC_NAME").value;
		var loccode = document.getElementById("LOC_CODE").value;
		if(partName && partName.length > 0){
			if(parent.$('inIframe') == null){//修复不在inIframe中时参数赋值问题
				if(top.$('PART_CNAME')) {
					top.$('PART_CNAME').value = partName;
				}
			}else{
			parentDocument.getElementById('PART_CNAME').value = partName;
			}
		}
		if(partId && partId.length > 0){
			if(parent.$('inIframe') == null){//修复不在inIframe中时参数赋值问题
				if(top.$('PART_ID')) {
					top.$('PART_ID').value = partId;
				}
			}else{
				parentDocument.getElementById('PART_ID').value = partId;
			}
		}
		if(partCode && partCode.length > 0){
			if(parent.$('inIframe') == null){//修复不在inIframe中时参数赋值问题
				if(top.$('PART_CODE')) {
					top.$('PART_CODE').value = partCode;
				}
			}else{
				parentDocument.getElementById('PART_CODE').value = partCode;
			}
		}
		if(partOldCode && partOldCode.length > 0){
			if(parent.$('inIframe') == null){//修复不在inIframe中时参数赋值问题
				if(top.$('PART_OLDCODE')) {
					top.$('PART_OLDCODE').value = partOldCode;
				}
			}else{
				parentDocument.getElementById('PART_OLDCODE').value = partOldCode;
			}
		}
		if(normalQty && normalQty.length > 0){
			if(parent.$('inIframe') == null){//修复不在inIframe中时参数赋值问题
				if(top.$('NORMAL_QTY')) {
					top.$('NORMAL_QTY').value = normalQty;
				}
			}else{
				parentDocument.getElementById('NORMAL_QTY').value = normalQty;
			}
		}
		if(unit && unit.length > 0){
			if(parent.$('inIframe') == null){//修复不在inIframe中时参数赋值问题
				if(top.$('UNIT')) {
					top.$('UNIT').value = unit;
				}
			}else{
				parentDocument.getElementById('UNIT').value = unit;
			}
		}
		if(locid && locid.length > 0){
			if(parent.$('inIframe') == null){//修复不在inIframe中时参数赋值问题
				if(top.$('LOC_ID')) {
					top.$('LOC_ID').value = locid;
				}
			}else{
				parentDocument.getElementById('LOC_ID').value = locid;
			}
		}
		if(loccode && loccode.length > 0){
			if(parent.$('inIframe') == null){//修复不在inIframe中时参数赋值问题
				if(top.$('LOC_CODE')) {
					top.$('LOC_CODE').value = loccode;
				}
			}else{
				parentDocument.getElementById('LOC_CODE').value = loccode;
			}
		}
		if(locname && locname.length > 0){
			if(parent.$('inIframe') == null){//修复不在inIframe中时参数赋值问题
				if(top.$('LOC_NAME')) {
					top.$('LOC_NAME').value = locname;
				}
			}else{
				parentDocument.getElementById('LOC_NAME').value = locname;
			}
		}
	}
</script>
</body>
</html>