<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>账户余额查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 财务管理 &gt; 账户余额查询</div>
<form method="post" name="fm" id="fm">  
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	function doInit(){
		__extQuery__(1);
	}
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/financemanage/DlrAccountBalance/dlrAccountBalanceQuery.json";
	var title = null;
	var columns = [
				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center',rowspan:2},
				{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center',rowspan:2},
				{header: "余额", dataIndex: 'ZY_YE', align:'center',renderer:myformat,colspan:3,colspanName:'自有资金'},
				{header: "预扣", dataIndex: 'ZY_YK', align:'center',renderer:myformat,colspanName:'自有资金'},
				{header: "可用余额", dataIndex: 'ZY_KY', align:'center',renderer:myformat,colspanName:'自有资金'},
				{header: "余额", dataIndex: 'JR_YE', align:'center',renderer:myformat,colspan:3,colspanName:'金融服务'},
				{header: "预扣", dataIndex: 'JR_YK', align:'center',renderer:myformat,colspanName:'金融服务'},
				{header: "可用余额", dataIndex: 'JR_KY', align:'center',renderer:myformat,colspanName:'金融服务'},
				{header: "余额", dataIndex: 'XY_YE', align:'center',renderer:myformat,colspan:3,colspanName:'长安信贷'},
				{header: "预扣", dataIndex: 'XY_YK', align:'center',renderer:myformat,colspanName:'长安信贷'},
				{header: "可用余额", dataIndex: 'XY_KY', align:'center',renderer:myformat,colspanName:'长安信贷'},
				{header: "可用合计", dataIndex: 'KYHJ', align:'center',renderer:myformat,rowspan:2}
		      ];
    //设置金钱格式
    function myformat(value,metaDate,record){
        return String.format(amountFormat(value));
    }
   
</script>
<!--页面列表 end -->
</body>
</html>