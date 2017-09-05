<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预留资源查询</title>
<script type="text/javascript">

</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 计划管理 > 配额调整 > 预留资源查询</div>
<form method="POST" name="fm" id="fm">
  <table width="85%" border="0" align="center" class="table_query">
    <tr>
      <TD width="35%" align=center nowrap><div align="right">选择配额月度：</div></td>
      <TD width="29%" align="left" nowrap>
	      <select name="year">
		      <c:forEach items="${years}" var="po">
        			<c:choose>
						<c:when test="${po == year}">
							<option value="${po}" selected="selected">${po}</option>
						</c:when>
						<c:otherwise>
							<option value="${po}">${po}</option>
						</c:otherwise>
					</c:choose> 
				</c:forEach>
	      </select>
            年
          <select name="month">
	          <c:forEach items="${months}" var="po">
					<c:choose>
						<c:when test="${po == month}">
							<option value="${po}" selected="selected">${po}</option>
						</c:when>
						<c:otherwise>
							<option value="${po}">${po}</option>
						</c:otherwise>
					</c:choose> 
				</c:forEach>
          </select>
           月
      </TD>
    </tr>
    <tr>
      <td align=right nowrap>选择车系：</td>
      <td align=left nowrap>
	      <select name="serie">
	          <c:forEach items="${serieList}" var="po">
			  	<option value="${po.GROUP_ID}">${po.GROUP_NAME}</option>
			  </c:forEach>
      	  </select>
      </td>
      <td width="36%" align=left nowrap><input name="button23" type="button" class="BUTTON" onClick="__extQuery__(1);" value="查询"></td>
    </tr>
  </table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript">
	
	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/ResourceReserveQuery/resourceReserveQuery.json";
				
	var title = null;

	var columns = [
				{header: "配置代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "配置名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "生产计划周度", dataIndex: 'PLAN_WEEK', align:'center'},
				{header: "生产计划数量", dataIndex: 'PLAN_AMOUNT', align:'center'},
				{header: "预留数量", dataIndex: 'RESERVE_AMT', align:'center'}
		      ];	
</script>
</body>
</html>
