<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
    String inputId = request.getParameter("INPUTID");
    String inputName = request.getParameter("INPUTCODE");
    String isMulti = request.getParameter("ISMULTI");
    String idVal = request.getParameter("idVal");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商选择</title>
<script type="text/javascript">

//选择车型开始
var myPage;

var url ="<%=contextPath%>/afterSales/serviceActivity/ServiceActivityAction/getDealer.json";
			
var title = null;

var columns = [
			{header: "选择", dataIndex: 'DEALER_ID', align:'center',renderer:seled},
			{header: "经销商编码", dataIndex: 'DEALER_CODE',style: 'text-align:left'},
			{header: "经销商名称", dataIndex: 'DEALER_NAME', style: 'text-align:left'}
	      ];      
function seled(value,meta,record){
	var isMulti = document.getElementById("isMulti").value;
	var idVal = document.getElementById("idVal").value;
	var isVals=idVal.split(",");
		if(isMulti == "false"){
			return String.format("<input type='radio' name='parts' value='"+record.data.DEALER_NAME+"@@"+record.data.DEALER_ID+"@@"+record.data.DEALER_CODE+"' />");
		}else{
			var html = "";
			for(var i=0;i<isVals.length;i++){
				if(record.data.DEALER_ID==isVals[i]){
					html = "<input type='checkbox' name='parts' onclick='checkValue(this)' checked value='"+record.data.DEALER_NAME+"@@"+record.data.DEALER_ID+"@@"+record.data.DEALER_CODE+"' />";
					break;
				}else{
					html = "<input type='checkbox' name='parts' onclick='checkValue(this)' value='"+record.data.DEALER_NAME+"@@"+record.data.DEALER_ID+"@@"+record.data.DEALER_CODE+"' />";
				}
				
			}
			return String.format(html);
  }
}


function checkValue(obj){
	if(obj.checked==true){
		var inputName = document.getElementById("inputName").value;
		var inputId = document.getElementById("inputId").value;
		if(__parent().setMoreDealerValue==undefined)
			MyAlert('调用父页面setMoreDealerValue方法出现异常!');
		else{
			__parent().setMoreDealerValue(inputId,inputName,obj.value,1);
		}
		
	}else{
		var inputName = document.getElementById("inputName").value;
		var inputId = document.getElementById("inputId").value;
		if(__parent().setMoreDealerValue==undefined)
			MyAlert('调用父页面setMoreDealerValue方法出现异常!');
		else{
			__parent().setMoreDealerValue(inputId,inputName,obj.value,2);
		}
	}
}
$(document).ready(function(){__extQuery__(1);});
</script>
</head>
<body >
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 选择经销商</div>
<form method="post" name ="fm" id="fm" >
<div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
	<input id="INPUT_NAME" name="INPUT_NAME" type="hidden" value="" />
	<input id="INPUT_ID" name="INPUT_ID" type="hidden" value=""/>	
	<input id="inputId" name="inputId" type="hidden" value="<%=inputId%>"/>	
	<input id="inputName" name="inputName" type="hidden" value="<%=inputName%>"/>	
	<input id="isMulti" name="isMulti" type="hidden" value="<%=isMulti%>"/>
	<input id="idVal" name="idVal" type="hidden" value="<%=idVal%>"/>
	<table class="table_query">
    <tr>
      <td class="right">经销商编码：</td>
      <td >
      	<input class="middle_txt" id="code" name="code" value="" type="text"/>
      </td>
      <td class="right">经销商名称：</td>
      <td>
      	<input name="name" type="text" id="name"  class="middle_txt"/>
      </td>
     
  	</tr>
  	<tr>
  	    <td colspan="4" style="text-align: center">
      	<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="u-button u-query" onClick="__extQuery__(1);" >
      	<input type="button" value="确 认" class="u-button u-submit" onclick="_hide();"/>
        <input type="button" value="关 闭" class="u-button u-cancel" onclick="_hide();"/>
      </td>
  	</tr>
</table>
	</div>
</div>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</div>
</body>

</html>