<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%@page import="com.infodms.dms.common.Constant"%>
<head> 
<%  
	String contextPath = request.getContextPath(); 
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>服务活动新增</title>
</head>
<body>
<div class="wbox">
<div class="navigation">
<img src="<%=contextPath%>/jsp_new/img/nav.gif" />&nbsp;当前位置：服务活动管理&gt;服务活动明细页面
</div>
<form name="fm" id="fm" method="post">
<input type="hidden" id="isPass" name="isPass" value="">
<div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
<table border="0" cellpadding="1" cellspacing="1" class="table_query" width="100%" style="text-align: center;">
	<tr>
		<td style="text-align:right">服务站代码:</td>
    	<td>
    	${map.DEALER_CODE }
    	</td>
		<td style="text-align:right">服务站名称:</td>
    	<td>
    	${map.DEALER_NAME }
    	</td>
    	<td style="text-align:right">单据新增时间:</td>
    	<td>
    	${map.NOWDATE }
    	</td>
	</tr>
	<tr>
		<td style="text-align:right">活动时间:</td>
    	<td>
			${map.START_DATE }
		             &nbsp;至&nbsp;
		 	${map.END_DATE }
		</td>	
		<td style="text-align:right">所属区域:</td>
    	<td>
			${map.ORG_NAME }
    	</td>
	</tr>
	<tr>
		<td style="text-align:right">活动申请内容:</td>
		<td colspan="5" >
		<textarea name='remark' id='remark' maxlength="100"  rows='3' cols='80' disabled="disabled">${map.ACTIVITY_CONTENT}</textarea>
		</td>
	</tr>
	
</table>
</div>
</div>
<div class="form-panel">
		<h2>审核记录</h2>
			<div class="form-body">
<table class="table_list">
				<tr >
					<th style="text-align:center" width="20%" >审核时间</th>
					<th style="text-align:center" width="60%">审核内容</th>
					<th style="text-align:center" width="8%">审核人</th>
					<th style="text-align:center" width="12%">审核状态</th>
				</tr>
				<c:forEach items="${applyRecordList}" var="applyR">
					<tr class="table_list_row2">
						<td align="center">${applyR.CDDATE}</td>
						<td align="left">${applyR.AUDIT_CONTENT}</td>
						<td align="center">${applyR.NAME}</td>
						<td align="center">${applyR.STATUS}</td>
					</tr>
				</c:forEach>
			</table>
<table class="table_query">
		<tr>
			<td height="6" style="text-align:center">
				<input type="button" id="back" onClick="_hide() ;" class="normal_btn"   value="返 回"/>
          	</td>
		</tr>
</table>
</div>
</div>
</form>
</div>
</body>
</script>
</html>