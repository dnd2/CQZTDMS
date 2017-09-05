<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<%@page import="com.infodms.dms.bean.AreaProvinceBean" %>
<%
	String contextPath = request.getContextPath();
	Map<String,Object> map = (Map)request.getAttribute("map");
	List<AreaProvinceBean> list = (List)request.getAttribute("list");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔工时单价设定</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;索赔工时单价设定&gt;修改</div>
<form name='fm' id='fm' method="post">
<input type="hidden" name="modeType" value="<%=map.get("WRGROUP_CODE")%>"/>
<table class="table_list">
	<tr class="table_list_row2">
		<td>车型组代码</td>
		<td>车型组名称</td>
<%for (int i=0;i<list.size();i++){
%>	               
	  <td><%=list.get(i).getCodeDesc()%></td>
<%}%>		
	</tr>
	<tr class="table_list_row1">
		<td><%=map.get("WRGROUP_CODE")%></td>
		<td><%=map.get("WRGROUP_NAME")%></td>
		<%for(int i=0;i<list.size();i++){%>
			<td>
				<input type="text" class="short_txt" name="js" id="<%=map.get(list.get(i).getAreaLevel())%>" value="<%=map.get(list.get(i).getAreaLevel())==null?0:map.get(list.get(i).getAreaLevel())%>" datatype="0,is_digit,4"/>
			</td>
		<%}%>
	</tr>
</table>
<br />

<table class="table_edit">
	<tr>
 		<td colspan="2" align="center">
 			<input class="normal_btn" type="button" value="确定" onclick="sureModify();"/> 
 			&nbsp;&nbsp;
 			<input class="normal_btn" type="button" onclick="history.go(-1);" value="返回"/>
 		</td>
	</tr>
</table>
</form>
<script type="text/javascript">
function sureModify(){
	var url = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/wcLabourPriceUpdate.json' ;
	makeNomalFormCall(url,cb,'fm') ;
}
function cb(json){
	if(json.flag)
		MyAlert('操作成功！');
	else
		MyAlert('操作失败！');
}
</script>
</body>
</html>
