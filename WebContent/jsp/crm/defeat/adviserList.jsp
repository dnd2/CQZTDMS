<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">

</script>
<style type="text/css" >
 .mix_type{width:100px;}
 .min_type{width:176px;}
 .mini_type{width:198px;}
 .long_type{width:545px;}
 .xlong_type{width:305px}
</style>
<title>顾问分配</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="loadcalendar();"> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  潜客管理 &gt; 战败管理 &gt;顾问分配</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="ctmId" name="ctmId" value="${ctmId}"/>
		<input type="hidden" id="decorationId" name="driveId" value="${tp.drivingId}"/>
			<br/>
				<center>
					<select name="adviser" id="adviser">
	         	 	<c:forEach items="${userList}" var="po">
	         	 		<option value="${po.USER_ID}" >${po.NAME}</option>
	         	 	</c:forEach>
	         	 	</select>
         	 	</center>
         	 	<br/>
         	 	<br/>
				<center>
					<input type="button" class="normal_btn" onclick="dispatch();" value="确 &nbsp; 认" id="queryBtn" /> 
				</center>
</form>
	
</div>
<script type="text/javascript">

	function dispatch(){
		makeFormCall('<%=contextPath%>/crm/defeat/DefeatManage/dispatchAdviser.json', dispatchResult, "fm") ;
	}
	//数据回写 重新分配顾问
	function dispatchResult(json){
		if(json.flag=='1'){
			_hide();
			parent.$('inIframe').contentWindow.__extQuery__(1);
		}else{
			MyAlert("分配失败！！！");
		}
		
	}

</script>    
</body>
</html>