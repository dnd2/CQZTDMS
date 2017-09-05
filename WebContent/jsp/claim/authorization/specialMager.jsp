<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/globalVariable.jsp" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=7">
	<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
	<script type="text/javascript" src="<%=contextPath%>/js/jslib/mootools.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/my-grid-pager.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/dept_tree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/framecommon/HashMap.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/framecommon/default.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/validate/validate.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/web/dtree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/dict.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/dialog_new.js"></script>
	<title>特殊费用审核人员维护</title>
<style>
.img {
	border: none
}
</style>
<script>
	   var filecontextPath="<%=contextPath%>";
</script>
</head>

<body >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
	&nbsp;当前位置： 索赔授权管理 &gt; 特殊费用审核人员维护</div>
<form id="fm" name="fm">
<input type="hidden" id="kdid" name="kdid" value="${kdid}" />
<table class="table_query" border="0">
	<tr>	
		<th class="table_query_2Col_label_4Letter" style="width: 30%" nowrap="nowrap">选择<input type='checkbox' name='checkAll' onclick='selectAll(this,"checkId")' /></th>
		<th class="table_query_2Col_label_4Letter" nowrap="nowrap">审核级别</th>
		<th class="table_query_2Col_label_4Letter" nowrap="nowrap">最小金额</th>
		<th class="table_query_2Col_label_4Letter" nowrap="nowrap">最大金额</th>
	</tr>
	<c:forEach var="bean" items="${bean}" varStatus="vs">
  		<tr >
          <th width="50" align="center" nowrap="nowrap" >
          <c:if test="${bean.var == 1}"> <input type="checkbox" id="checkId" name="checkId" value="${bean.speId}" checked/></c:if>
          <c:if test="${bean.var != 1}"> <input type="checkbox" id="checkId" name="checkId" value="${bean.speId}" /></c:if>
          </th>
          <th width="220" align="center" nowrap="nowrap" >
           <script type='text/javascript'>
	       	var activityType=getItemValue('${bean.speLevel}');
	       	document.write(activityType) ;
	      </script>
           </th>
          <th width="400" align="center" nowrap="nowrap" >${bean.minAmount}</th>
           <th width="400" align="center" nowrap="nowrap" ><c:if test="${bean.maxAmount == 0.0}"></c:if> <c:if test="${bean.maxAmount != 0.0}">${bean.maxAmount}</c:if></th>
        </tr>
  	</c:forEach>
  	<tr>
		<td align="center" colspan="4">
			<input class="normal_btn" type="button" value="确定" id="queryBtn" onclick="add()"/>
			<input class="normal_btn" type="button" value="返回" id="queryBtn" onclick="javascript:history.go(-1)"/>
		</td>
	</tr>
</table>
</form>
</body>
</html>
<script>
	function selectAll(checkObj,checkBoxName){ 
    window.event.cancelBubble = true;
	var allChecks = document.getElementsByName(checkBoxName);
	if(checkObj.checked){
		for(var i = 0;i<allChecks.length;i++){
			allChecks[i].checked = true;
		}
	}else{
		for(var i = 0;i<allChecks.length;i++){
			allChecks[i].checked = false;
		}
	}
}
	function add()
	{
		makeNomalFormCall('<%=contextPath%>/sysmng/usemng/SgmSysUser/specialUpdate.json',updateBack,'fm','');
	}
	function updateBack(json)
	{
		if(json.retype == '0')
		{
			MyAlert("请选择人员审核范围！");
		}else
		{
			MyAlert("授权成功！");
			fm.action = "<%=contextPath%>/sysmng/usemng/SgmSysUser/specialInt.do";
		    fm.submit();
		}
	}
</script>