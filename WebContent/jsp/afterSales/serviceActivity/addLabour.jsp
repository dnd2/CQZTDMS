<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>工单</title>
<% 
String contextPath = request.getContextPath(); 
String id=request.getParameter("id");
String cnt=request.getParameter("cnt");
%>

<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/afterSales/serviceActivity/ServiceActivityAction/addLabour.json";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{id:'action',header: "选择",sortable: false,dataIndex: 'LABOUR_CODE',renderer:mySelect,align:'center'},
				{header: "工时代码", dataIndex: 'LABOUR_CODE', align:'center'},
				{header: "工时名称", dataIndex: 'CN_DES', align:'center'},
				{header: "索赔工时", dataIndex: 'LABOUR_HOUR', align:'center'}
	      ];
		function mySelect(value,metaDate,record){
			 return String.format("<input type='radio' name='rd' onclick='setLabourCode(\""+record.data.LABOUR_CODE+"\",\""+record.data.CN_DES+"\",\""+record.data.ID+"\",\""+record.data.LABOUR_HOUR+"\")' />");
		}
		function setLabourCode(v1,v2,v3,v4){
			var id=document.getElementById("id").value;
			 //调用父页面方法
			if(v1==null||v1=="null"){
			 	v1 = "";
			 }
			 if(v2==null||v2=="null"){
			 	v2 = "";
			 }
	 		if (parent.$('inIframe')) {
	 			__parent().setLabourCode(v1,v2,id,v3,v4);
	 		} else {
	 			__parent().setLabourCode(v1,v2,id,v3,v4);
			}
	 		parent._hide();
		}
		$(document).ready(function(){__extQuery__(1);});
</script>
<!--页面列表 end -->
</head>
<body >
<div class="wbox">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔基础数据&gt;维修工时查询
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
		
		<td  class="right">工时代码：</td>
      	<td  nowrap="true">
            <input name="labour_code" id="labour_code" type="text" class="middle_txt" maxlength="30" />
      	</td>
        <td class="right">工时名称：</td>
      	<td nowrap="true">
            <input name="cn_des" id="cn_des" type="text" class="middle_txt" maxlength="30" />
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