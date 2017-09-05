<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>配件销售价格变更申请--查看明细</title>
</head>

<body onload="__extQuery__(1);">
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：基础信息管理 &gt; 技术相关信息维护 &gt; 配件价格变更申请（查看 ）</div>
    <form name="fm" id="fm" method="post" enctype="multipart/form-data">
    	<input type="hidden" name="APPLY_NO" id="APPLY_NO" value="${APPLY_NO}"/>
  		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
  		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
</div>
</body>
<script type="text/javascript" >
	var myPage;
	var url = g_webAppName+"/parts/baseManager/partSalePrice/PartSalePriceChange/lookDetail.json?APPLY_NO=${APPLY_NO}";
	var title = null;
	var columns = [
				{header: "序号",  style: 'text-align:left',renderer:getIndex},
                {header: "变更申请号", dataIndex: 'APPLY_NO',  style: 'text-align:left'},
				{header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align:left'},
				{header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align:left'},
				{header: '变更前${map.prciceName1}(元)', dataIndex: 'SVC_PRICE',  style: 'text-align:right'},
				{header: '变更后${map.prciceName1}(元)', dataIndex: 'SVC_PRICE_C',  style: 'text-align:right'},
				{header: '变更前${map.prciceName2}(元)', dataIndex: 'RETAIL_PRICE',  style: 'text-align:right'},
				{header: '变更后${map.prciceName2}(元)', dataIndex: 'RETAIL_PRICE_C',  style: 'text-align:right'},
				{header: "创建日期", dataIndex: 'CREATE_DATE',  style: 'text-align:center'},
				{header: "创建人", dataIndex: 'NAME',  style: 'text-align:left'}
		      ];
</script>
</html>
