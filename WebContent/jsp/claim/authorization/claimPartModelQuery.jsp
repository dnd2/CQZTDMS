<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>监控配件维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body onload="__extQuery__(1) ;">
<div class="wbox">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔授权管理&gt;监控配件维护</div>
  <form name='fm' id='fm'>
  <div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
  <table class="table_query">
       <tr>            
        <td style="text-align:right">配件代码：
        <input id="PART_CODE" name="PART_CODE" type="hidden" />
  		<input id="PART_NAME" name="PART_NAME" type="hidden" />
        </td>            
        <td>
			<input  class="middle_txt" id="partcode"  name="partcode" type="text" datatype="1,is_null,20"/>
        </td>
        <td style="text-align:right">配件名称：</td>
        <td><input type="text" name="partname" id="partname" datatype="1,is_null,30" class="middle_txt" value=""/></td>   
       </tr>
       <tr>
        <td colspan="4" style="text-align:center">
        	    <input  id="queryBtn" class="normal_btn" type="button" name="queryBtn" value="查询"  onclick="__extQuery__(1) ；"/>
				<input class="normal_btn" type="button" name="button1" value="关闭"  onclick="_hide();"/>
        </td>
       </tr>       
 	</table>
 	</div>
 	</div>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</div>
<script type="text/javascript" >
var myPage;
var url = "<%=request.getContextPath()%>/claim/authorization/ClaimPartWatchMain/claimPartSelect.json?query=1";
var title = null;
var columns = [
			{header: "选择",sortable: false,dataIndex: 'PART_ID',align:'center',renderer:myLink},
			{header: "配件代码",sortable: false,dataIndex: 'PART_CODE',align:'center'},
			{header: "配件名称",sortable: false,dataIndex: 'PART_NAME',align:'center'}
		//	{header: "价格",sortable: false,dataIndex: 'STOCK_PRICE',align:'center'}
	      ];
function myLink(value,metadata,record){
	return String.format(
			"<input type='hidden' id='"+record.data.PART_CODE+"' name='"+record.data.PART_CODE+"' value='"+record.data.PART_NAME+"'/>"+
			"<input type='radio' id='"+record.data.PART_CODE+"' name='"+record.data.PART_CODE+"' value='"+record.data.PART_CODE+"' onclick='selbyid(this);'/>"
			);
}
function selbyid(obj){
	$('#PART_CODE')[0].value=obj.value;
	$('#PART_NAME')[0].value = document.getElementById(obj.value).value;
	
	var partCode = 'PART_CODE';
    var partName = 'PART_NAME';
	var code = document.getElementById("PART_CODE").value;
	var name = document.getElementById("PART_NAME").value;
	var parentDocument = __parent() ; 
	if(code && code.length > 0)
		parentDocument.document.getElementById(partCode).value = code;
	if(name && name.length > 0)
	   	parentDocument.document.getElementById(partName).value = name;	
	
	_hide();
}
</script> 
</form>
</body>
</html>