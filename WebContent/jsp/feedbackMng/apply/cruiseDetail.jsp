<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<title>巡航服务线路修改页面</title>
<% 
   String contextPath = request.getContextPath();
  %>
</head>
<script type="text/javascript">
function doInit(){
   loadcalendar();
}
</script>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：信息反馈管理 &gt;信息反馈提报 &gt;巡航服务线路修改</div>
<form method="post" name ="fm" id="fm">
<input type="hidden" name="id" value="${cruise.id}" />
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
		<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
	</table>
    <table class="table_edit">
    	<tr>
    		<td width="10%" align="right">服务站代码：</td>
    		<td width="20%">${map.DEALER_CODE}</td>
    		<td width="10%" align="right">服务站名称：</td>
    		<td width="20%">${map.DEALER_NAME}</td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">制单日期：</td>
    		<td width="20%">
    			<fmt:formatDate value="${cruise.makeDate}" pattern='yyyy-MM-dd' />
    		</td>
    		<td width="10%" align="right">目的地：</td>
    		<td width="20%">
    			<input type="text" class="middle_txt" name="address" id="address" datatype="0,is_null" value="${cruise.crWhither }"/>
    		</td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">总里程：</td>
    		<td width="20%">
    			<input type="text" class="middle_txt" name="mileage" id="mileage" datatype="0,id_digit,5" value=<fmt:formatNumber value='${cruise.crMileage }' pattern="###.##"/> />
    		</td>
    		<td width="10%" align="right">巡航天数：</td>
    		<td width="20%">
    			<input type="text" class="middle_txt" name="days" id="days" datatype="0,is_digit,5" value=<fmt:formatNumber value='${cruise.crDay }' pattern='###.##'/> />
    		</td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">巡航负责人：</td>
    		<td width="20%">
    			<input type="text" class="middle_txt" name="man" id="man" datatype="0,is_null" value="${cruise.crPrincipal }" />
    		</td>
    		<td width="10%" align="right">巡航服务电话：</td>
    		<td width="20%">
    			<input type="text" class="middle_txt" name="phone" id="phone" datatype="0,is_phone" value="${cruise.crPhone }" />
    		</td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">每月定点日期：</td>
    		<td width="20%">
    			<input type="text" class="middle_txt" name="point" id="point" datatype="0,is_null,10" value="${cruise.fixPointDate }" />
    		</td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">巡航原因：</td>
    		<td colspan="3">
    			<textarea cols="80" rows="3" name="cause">${cruise.crCause }</textarea>
    		</td>
    	</tr>
    	<tr>
    		<td colspan="4">&nbsp;</td>
    	</tr>
    	<tr>
    		<td colspan="4" align="center">
    			<input type="button" class="normal_btn" value="保存" id="btn_1" onclick="updateOrApply(1);" />
    			&nbsp;
    			<input type="button" class="normal_btn" value="提报" id="btn_2" onclick="updateOrApply(2);" />
    			&nbsp;
    			<input type="button" class="normal_btn" value="返回" onclick="history.go(-1);" />
    		</td>
    	</tr>
    </table>
        
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<script type="text/javascript">
	function updateOrApply(val){
		var mileage = $('mileage').value ;
		var pattern = /^(\d){0,100}$/ ;
		if(!pattern.exec(mileage)){
			MyAlert('里程只能输入正整数！');
			return ;
		}
			
		var url = '<%=contextPath%>/feedbackmng/apply/CruiseAction/checkOutDays.json?type='+val ;
		sendAjax(url,checkBK,'fm');
	}
	function checkBK(json){
		var outDay = $('days').value ;
		if(json.flag){
			MyAlert('此里程不在标准定义表中定义，请联系管理员！');
			return ;
		}
		if(json.outDay*1<outDay*1){
			MyAlert('外出天数填写过大！');
			return ;
		}
		var url = '<%=contextPath%>/feedbackmng/apply/CruiseAction/updateOrApplyCruise.json?type='+json.type ;
		sendAjax(url,UOAbak,'fm');
	}
	function UOAbak(json){
		if(json.flag==1){
			location = '<%=contextPath%>/feedbackmng/apply/CruiseAction/cruiseApplyInit.do' ;
			//MyAlert('保存成功!');
		}else if(json.flag==2){
			location = '<%=contextPath%>/feedbackmng/apply/CruiseAction/cruiseApplyInit.do' ;
			//MyAlert('提报成功!');
		}else {
			MyAlert('操作失败，请与系统管理员联系！');
		}
	}
	function myBtnSet(){
		var status = ${cruise.status} ;
		if(status>'<%=Constant.CRUISE_STATUS_01%>'){
			$('btn_1').disabled = 'disabled' ;
			$('btn_2').disabled = 'disabled' ;
		}
	}
	myBtnSet();
</script>
</BODY>
</html>