<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>经销商配额确认下发</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 计划管理 > 配额调整 > 经销商配额确认下发</div>
<form method="POST" name="fm" id="fm">
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript">

	function doInit(){
		__extQuery__(1);
	}
	
	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/DealerQuotaConfirmIssue/dealerQuotaConfirmQuery.json";
				
	var title = null;

	var columns = [
				{header: "业务范围", dataIndex: 'AREA_NAME', align:'center'},
				{header: "月份", dataIndex: 'QUOTA_DATE', align:'center'},
				{header: "配额类型", dataIndex: 'QUOTA_TYPE', align:'center', renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'AREA_ID',renderer:myLink ,align:'center'}
		      ];		         
	
	//修改的超链接
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='loginIssue(\""+ value +"\",\""+ record.data.QUOTA_YEAR +"\",\""+ record.data.QUOTA_MONTH +"\")'>[确认下发]</a>");
	}
	
	function loginIssue(arg1, arg2, arg3){
		$('fm').action= '<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/DealerQuotaConfirmIssue/dealerQuotaIssuePre.do?areaId='+arg1+'&quotaYear='+arg2+'&quotaMonth='+arg3;
	 	$('fm').submit();
	}
</script>
</body>
</html>
