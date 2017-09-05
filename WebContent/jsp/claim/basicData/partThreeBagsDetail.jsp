<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>配件三包期查询明细</title>
<%String contextPath = request.getContextPath();%>
</head>
<body onload="__extQuery__(1);">
<form id="fm" name="fm" id="fm">
<input type="hidden" id="ruleId" name="ruleId" value="${ruleId }"/>
	<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;配件三包期查询&gt;明细
	</div>
 <!--分页开始 -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
 <!--分页 结束 --> 
</form>
<!-- 页面列表 开始 -->
		<script type="text/javascript" >
			var myPage;
			var url = "<%=contextPath%>/claim/basicData/ClaimLaborMain/partThreeBagsDetail.json";			
			var title = null;
			var columns = [
						{header: "三包规则代码", dataIndex: 'RULE_CODE', align:'center'},
						{header: "三包策略代码",dataIndex: 'GAME_CODE' ,align:'center'},
						{header: "三包策略名称 ",dataIndex: 'GAME_NAME' ,align:'center'},
						{header: "车型代码",dataIndex: 'MODEL_CODE' ,align:'center'},
						{header: "车型名称",dataIndex: 'MODEL_NAME' ,align:'center'}
				      ];
		</script>
<!-- 页面列表结束 -->
<table class="table_query">
	<tr>
		<td align="center"> 
			<input class="normal_btn" type="button" name="button" value="关闭"  onclick="_hide();"/>
		</td>
	</tr>
</table>
</body>
</html>