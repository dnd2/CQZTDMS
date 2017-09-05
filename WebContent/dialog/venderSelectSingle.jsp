<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>

<%
	String contextPath = request.getContextPath();
    String inputCode = request.getParameter("INPUTCODE");
    String inputId = request.getParameter("INPUTID");
    String isMulti = request.getParameter("ISMULTI");
    String notExistsPartId = request.getParameter("notExistsPartId");
    
%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>通用选择框</title>

<script type="text/javascript" >
$(document).ready(function(){
	__extQuery__(1);
});
	
var myPage;

var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/queryPartVenderInfo.json?notExistsPartId="+"<%=notExistsPartId%>";
			
var title = null;

var columns = [
			{header: "选择", dataIndex: 'VENDER_ID', align:'center',renderer:seled},
			{header: "供应商代码", dataIndex: 'VENDER_CODE',style: 'text-align:left'},
			{header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'}
	      ];
	      
function seled(value,meta,record){
	var isMulti = "<%=isMulti%>";
		if(isMulti == "false"){
			return "<input type='radio' onclick='singleSelect(\""+value+"\",\""+record.data.VENDER_NAME+"\")'/>";
		}else{
       	return "<input type='checkbox' name='checkCode' id='checkCode' value='"+ value +"' />";
       }
   }

var parentDocument = __parent().document;
function singleSelect(val1,val2){
   	document.getElementById("VENDER_ID").value = val1;
   	document.getElementById("VENDER_NAME").value = val2;
	var inputCode = "<%=inputCode%>";
	var inputId = "<%=inputId%>";
	parentDocument.getElementById(inputId).value = val1;
	parentDocument.getElementById(inputCode).value = val2;
   	 _hide();
   	//parentContainer.changeDiv();
}
   
   function doSupp()//关闭弹出窗口的时候执行该方法
{
	var inputCode = "<%=inputCode%>";
	var inputId = "<%=inputId%>";
	var venderName = document.getElementById("VENDER_NAME").value;
	var venderId = document.getElementById("VENDER_ID").value;
	if(venderName && venderName.length > 0){
		parentDocument.getElementById(inputCode).value = document.getElementById("VENDER_NAME").value;
	}
	if(venderId && venderId.length > 0){
		parentDocument.getElementById(inputId).value = document.getElementById("VENDER_ID").value;
	}
}

</script>
</head>
<body onbeforeunload="doSupp();">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 选择供应商</div>
<form method="post" name ="fm" id="fm">
	<input id="VENDER_NAME" name="VENDER_NAME" type="hidden" value="" />
	<input id="VENDER_ID" name="VENDER_ID" type="hidden" value=""/>
	<div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">	
	<table class="table_query">
    <tr>
      <td class="right" nowrap="nowrap">供应商编码：</td>
      <td class="right" nowrap="nowrap">
      	<input class="middle_txt" id="venderCode" name="venderCode" value="" type="text"/>
      </td>
      <td class="table_query_3Col_label_6Letter" nowrap="nowrap">供应商名称：</td>
      <td>
      	<input name="venderName" type="text" id="venderName"  class="middle_txt"/>
      </td>
      <td>&nbsp;</td>
      <td>
      	<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1);" >
      </td>
  	</tr>
</table>
</div>
</div>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div>
</body>
</html>