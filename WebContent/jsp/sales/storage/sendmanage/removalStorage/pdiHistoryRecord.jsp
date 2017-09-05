<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>PDI检查历史记录查看</title>
</head>
<body onload="__extQuery__(1);">
	<form name="fm" id="fm">
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/jsp/sales/storage/sendmanage/removalStorage/pageDiv.html" />
	<!--分页 end -->
	<%-- 
	<table border="0" cellpadding="0" cellspacing="0" width="90%" align="center" style="margin-top: 20px;">
		<c:choose>
			<c:when test="${fn:length(record) > 0 }">
				<th align="center">记录</th>
				<th align="center">时间</th>
				<c:forEach items="${record }" var="po">
					<tr>
						<td align="center">${po.record }</td>
						<td align="center"><fmt:formatDate value="${po.createDate }" pattern="yyyy-MM-dd HH:mm"/></td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan="2" height="100%" align="center"><font style="font-size: 16px; color: #333333;">还没有历史记录！！</font></td>			
				</tr>
			</c:otherwise>
		</c:choose>
	</table>
	--%>
	</form>
</body>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/storage/sendmanage/PDICheck/pdiCheckHistoryRecordQuery.json?vin=${param.vin}";
	var title = "检查记录";
	
	var columns = [
				{header: "检查信息",dataIndex: 'RECORD',align:'center'},
				{header: "检查人",dataIndex: 'NAME',align:'center'},
				{header: "时间",dataIndex: 'CREATE_DATE',align:'center'}
		      ];
</script>
