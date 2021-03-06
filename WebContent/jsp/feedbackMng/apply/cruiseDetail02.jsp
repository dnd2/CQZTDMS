<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<title>巡航服务线路明细页面</title>
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
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：信息反馈管理 &gt;信息反馈提报 &gt;巡航服务线路明细</div>
<form method="post" name ="fm" id="fm">
<input type="hidden" name="id" value="${cruise.id}" />
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
		<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
	</table>
    <table class="table_info">
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
    			${cruise.crWhither }
    		</td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">总里程：</td>
    		<td width="20%">
    			${cruise.crMileage }
    		</td>
    		<td width="10%" align="right">巡航天数：</td>
    		<td width="20%">
    			${cruise.crDay }
    		</td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">巡航负责人：</td>
    		<td width="20%">
    			${cruise.crPrincipal }
    		</td>
    		<td width="10%" align="right">巡航服务电话：</td>
    		<td width="20%">
    			${cruise.crPhone }
    		</td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">每月定点日期：</td>
    		<td width="20%">
    			${cruise.fixPointDate }
    		</td>
    		<td width="10%" align="right">单据状态：</td>
    		<td width="20%" id="statusBak">
    			<script>
    				writeItemValue(${cruise.status});
    			</script>
    		</td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">巡航原因：</td>
    		<td colspan="3">
    			${cruise.crCause }
    		</td>
    	</tr>
    </table>
    <br />
   	<c:if test="${list2!=null}">
    <table class="table_list">
    	<tr class="table_list_row2">
    		<td>审核人</td>
    		<td>审核时间</td>
    		<td>审核建议</td>
    		<td>审核状态</td>
    	</tr>
    	<c:forEach items="${list2}" var="audit" varStatus="st">
    		<tr class="table_list_row${st.index+1}">
    			<td>${audit.name }</td>
    			<td>
    				<fmt:formatDate value='${audit.auditingDate }' pattern='yyyy-MM-dd' />
    			</td>
    			<td>${audit.auditingOpinion }</td>
    			<td>
    				<script>
    					writeItemValue(${audit.status });
    				</script>
    			</td>
    		</tr>
    	</c:forEach>
    </table>
    <br />
    </c:if>
    <table class="table_edit">
    	<tr>
    		<td colspan="4" align="center">
    			<c:if test="${cruise.status == 13631006}">
    				<input type="button" class="normal_btn" id="suspendBtn" value = "中止" onclick="doSuspend(${cruise.id})"/> &nbsp;&nbsp;
    			</c:if>
    			<input type="button" class="normal_btn" value="返回" onclick="history.go(-1);" />
    		</td>
    	</tr>
    </table>
        
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<script type="text/javascript">
	function doSuspend(value){
		if(confirm("确定中止？")){
			$('suspendBtn').disabled = true ;
			var url = '<%=contextPath%>/feedbackmng/apply/CruiseAction/auditCruise.json?type=5' ;
			sendAjax(url,UOAbak,'fm');
		}
	}
	function UOAbak(json){
		if(json.flag==5 || json.flag == 6){
			$('statusBak').innerText = '中止' ;
			MyAlert('操作成功！');
		}else {
			$('suspendBtn').disabled = false ;
			MyAlert('操作失败，请与系统管理员联系！');
		}
	}
</script>
</BODY>
</html>