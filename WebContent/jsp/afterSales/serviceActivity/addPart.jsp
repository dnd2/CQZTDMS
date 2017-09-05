<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>工单</title>
<% String contextPath = request.getContextPath();
String id=request.getParameter("id");
String cnt=request.getParameter("cnt");
%>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/afterSales/serviceActivity/ServiceActivityAction/addPart.json";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{id:'action',header: "选择",sortable: false,dataIndex: 'PART_ID',renderer:mySelect,align:'center'},
				{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "配件单价", dataIndex: 'CLAIM_PRICE_PARAM', align:'center'}
	      ];
		function mySelect(value,metaDate,record){
			return String.format("<input type='radio' name='rd' onclick='setMainPartCode(\""+record.data.PART_ID+"\",\""+record.data.PART_CODE+"\",\""+record.data.PART_NAME+"\")' />");
		}
		function setMainPartCode(v1,v2,v3){
			var id=document.getElementById("id").value;
			 //调用父页面方法
			if(v1==null||v1=="null"){
			 	v1 = "";
			 }
			 if(v2==null||v2=="null"){
			 	v2 = "";
			 }
			 if(v3==null||v3=="null"){
			 	v3 = "";
			 }
	 		if (parent.$('inIframe')) {
	 			__parent().setMainPartCode(v2,v3,id,v1);
	 		} else {
	 			__parent().setMainPartCode(v2,v3,id,v1);
			}
	 		parent._hide();
		}
		$(document).ready(function(){__extQuery__(1);});
</script>
<!--页面列表 end -->
</head>
<body>
<div class="wbox">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔基础数据&gt;维修配件查询
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<input type="hidden" name="id" id="id" value="<%=id%>"/>
	<input type="hidden" name="cnt" id="cnt" value="<%=cnt%>"/>
	<tr>
		
		<td class="right">配件代码：</td>
      	<td >
      		<input class="middle_txt" id="part_code"  name="part_code" maxlength="30" type="text"/>
      	</td>
        <td class="right">配件名称：</td>
      	<td >
      		<input class="middle_txt" id="part_name"  name="part_name" maxlength="30" type="text"/>
      	</td>
	</tr>
	<tr>
    	<td style="text-align: center" colspan="8">
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="u-button u-query" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="u-button u-cancel" />
    	</td>
    </tr>
</table>
	</div>
</div>
<!-- 查询条件 end -->

<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
</html>