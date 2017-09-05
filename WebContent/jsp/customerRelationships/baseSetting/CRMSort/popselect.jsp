<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查询</title>
</head>
<script type="text/javascript">
	var myPage;
	var url = "<%=contextPath%>/customerRelationships/baseSetting/CRMSortManager/getUserList.json?";
	var title = null;
	var columns = [
				{header: "选择", align:'center',dataIndex:'USER_ID',renderer:getRadio},
				{header: "帐号", dataIndex: 'ACNT', align:'center'},
				{header: "姓名", dataIndex: 'NAME', align:'center'}
				];

function getRadio(value, meta, record) {
	var formatString = "<input type='radio' value="+value+" onclick='conf("+value+",\""+record.data.ACNT+"\",\""+record.data.NAME+"\");' name='se' id='se' />";
	return String.format(formatString);
}

function conf(value, acnt, name){
	var jsonData= {};
	jsonData["USER_ID"]=value;
	jsonData["ACNT"]=acnt;
	jsonData["NAME"]=name;
	parentContainer.showUser(jsonData);
	_hide();
}

</script>
<body onload ="__extQuery__(1)">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<table border="0" class="table_query">
    <th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 查询条件</th>
    <tr>
      <td width="14%" class="table_query_2Col_label_6Letter">帐号：</td>
      <td width="14%" ><input class="middle_txt" type="text" id="ACNT" jset="para"/></td>
      
      <td width="14%" class="table_query_2Col_label_6Letter">姓名：</td>
      <td width="14%" ><input class="middle_txt" type="text" id="NAME"  jset="para"/></td>
    </tr>
  <tr>
    <td colspan="4" align="center">
    <input class="normal_btn" type="button" value="查询" onclick="__extQuery__(1);"/>
    </td>
  </tr>
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />



</form>
</body>
</html>
