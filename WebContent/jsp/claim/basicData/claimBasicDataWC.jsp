<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.bean.AreaProvinceBean"%>
<%@page import="com.infodms.dms.util.StringUtil"%>
<%
	String contextPath = request.getContextPath();
	List<AreaProvinceBean> lists = (List<AreaProvinceBean>)request.getAttribute("list");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔工时单价设定</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;索赔工时单价设定</div>
<form name='fm' id='fm' method="post">
<table class="table_edit" >
	<tr>
 		<td width="10%" align="right" height="36">车型组代码：</td>
 		<td width="20%">
 			<input type="text" name="groupCode" class="middle_txt" />
 		</td>
 		<td width="10%" align="right">车型组名称：</td>
 		<td width="20%">
 			<input type="text" name="groupName" class="middle_txt" />
 		</td>
	</tr>
	<tr>
 		<td colspan="4" align="center">
 			<input class="normal_btn" type="button" value="查询" onclick="mainQuery();"/> 
 		</td>
	</tr>
</table>

	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script type="text/javascript">
	var myPage;
	var url = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/wcLaborPriceMainQuery.json' ;
	
	var title = null ;

	var columns = [
	               {header:'序号',width:'8%',align:'center',renderer:getIndex},
	               {header:'车型组代码',width:'12%',align:'center',dataIndex:'WRGROUP_CODE'},
	               {header:'车型组名称',width:'14%',align:'center',dataIndex:'WRGROUP_NAME'},
<%
 for (int i=0;i<lists.size();i++){
%>	               
	               {header:'<%=lists.get(i).getCodeDesc()%>',width:'10%',align:'center',dataIndex:'<%=lists.get(i).getAreaLevel()%>'},

<%}%>
	               {header:'操作',width:'8%',align:'center',renderer:myHandler}
		           	];
	function mainQuery(){
		__extQuery__(1);
	}
	function wrapOut(){
		$('dealer_code').value = '' ;
		$('dealer_id').value = '' ;
	}
	function myHandler(value,meta,record){
		var did = record.data.WRGROUP_CODE ;
		return '<a href="<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/wcLabourPriceUpdateInit.do?modeType='+did+'">[修改]</a>' ;
	}
</script>
</body>
</html>
