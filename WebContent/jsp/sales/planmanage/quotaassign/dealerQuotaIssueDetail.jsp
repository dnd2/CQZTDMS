<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商配额确认下发</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 计划管理 > 配额分配 > 经销商配额确认下发</div>
<form method="POST" name="fm" id="fm">
	<table class=table_list style="border-bottom:1px solid #DAE0EE">
	    <tr class="tabletitle"align="center">
	      <th>经销商代码</th>
	      <th>经销商名称</th>
	      <th>车系代码</th>
	      <th>车系名称</th>
	      <th>总量</th>
	    </tr>
	    <c:forEach items="${list}" var="po">
		    <tr class="table_list_row1">
		      <td nowrap>${po.DEALER_CODE}</td>
		      <td nowrap>${po.DEALER_NAME}</td>
		      <td align="center" nowrap>${po.SERIE_NAME}</td>
		      <td align="center" >${po.SERIE_NAME}</td>
		      <td nowrap>${po.QUOTA_AMT}</td>
		    </tr>
	    </c:forEach>
  	</table>
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
	<table width="95%" align="center" class="table_query">
      <tr>
        <td>
	        <div align="left">
		        <input type='hidden' name='quotaYear' value='${quotaYear}' />
				<input type='hidden' name='quotaMonth' value='${quotaMonth}' />
				<input type='hidden' name='areaId' value='${areaId}' />
				<input type='hidden' name='dealerId' value='${dealerId}' />
				<input type='hidden' name='groupId' value='${groupId}' />
		        <input name="button4" type="button" class="button" onClick="_hide();" value="关 闭">
	        </div>
        </td>
      </tr>
    </table>
</form>
<script type="text/javascript">
	
	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/DealerQuotaConfirmIssue/dealerQuotaIssueDetailQuery.json";;
				
	var title = null;

	var columns = [
				{header: "配额周次", dataIndex: 'QUOTA_DATE', align:'center'},
				{header: "产品组代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "产品组名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "配额", dataIndex: 'QUOTA_AMT', align:'center'}
		      ];
	
	function doInit(){
		__extQuery__(1);
	}
	

</script>
</body>
</html>
