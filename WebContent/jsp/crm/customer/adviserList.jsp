<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
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
 .nx_type{width:160px;}
</style>
<title>实销信息上报</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="loadcalendar();"> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  潜客管理 &gt; 客户管理 &gt;顾问列表</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<input type="hidden" id="customerIds" name="ctmIds" value="${customerIds}"/>
		<table class="table_query" border="0">
			<tr>
				<td width="20%" class="tblopt"><div align="right">顾问：</div></td>
				<td width="39%" >
					
	              <select name="adviser" id="adviser" style="width:130px;">
		         	 	<c:forEach items="${userList}" var="po">
		         	 		<option value="${po.USER_ID}" >${po.NAME}</option>
		         	 	</c:forEach>
	         	 	</select>
    			</td>
			</tr>
			
			<tr>
				<td   colspan="2" align="center">
					<input type="button" class="normal_btn" onclick="addInsure();" value="确认" id="queryBtn" /> 
				</td>
			</tr>
		</table>
		
</form>
	
</div>
<script type="text/javascript">
	function submit_(pose_id,pose_name){
		if (parent.$('inIframe')) {
			_hide();
			parent.$('inIframe').contentWindow.showSalesManInfo(pose_id,pose_name);
		}else {
			parent._hide();
			parent.showSalesManInfo(pose_id,pose_name);
		}
	}
	function addInsure(){
		makeFormCall('<%=contextPath%>/crm/customer/CustomerManage/sureDipatch.json', sureResult, "fm") ;
	}
	//数据回写
	function sureResult(json){
		if(json.flag=="1"){
			MyAlert("分配成功！！");
			_hide();
			parent.$('inIframe').contentWindow.setCols();
			parent.$('inIframe').contentWindow.__extQuery__(1);
		}else{
			MyAlert("分配失败！！");
		}
		
		
	}
	function loadEnCompany(obj){
		$("enCompany").value=obj.getAttribute("TREE_ID");
	}
</script> 
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar1", "topbar")</script>   
</body>
</html>