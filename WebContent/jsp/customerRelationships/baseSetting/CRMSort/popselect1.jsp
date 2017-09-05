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
    var jsonData={};
	var myPage;
	var url = "<%=contextPath%>/customerRelationships/baseSetting/CRMSortManager/getShiftKindList.json";
	var title = null;
	var columns = [
				{header: "选择", align:'center',dataIndex:'SHIFT_KIND',renderer:getCheck},
				{header: "坐席业务", dataIndex: 'SHIFT_KIND_DESC', align:'center'}
				];

function getCheck(value, meta, record) {
	var formatString = "<input type='checkbox' value="+value+" data="+JSON.stringify(record.data)+" name='ck' id='ck' />";
	return String.format(formatString);
}

function conf(){
	var objs=document.getElementsByName("ck");
	var arrayJSON = new Array();
	for(var i=0;i<objs.length;i++){
		if(objs[i].checked){
		    var data=objs[i].getAttribute("data");
		    var json=eval("(" + data + ")");
		    arrayJSON.push(json);
		}
	}
	parentContainer.showSelect1(arrayJSON);
	_hide();
}

</script>
<body onload ="__extQuery__(1)">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />

<table border="0" class="table_query">
  <tr>
    <td colspan="4" align="center">
    <input class="normal_btn" type="button" value="查询" onclick="__extQuery__(1);"/>
    <input class="normal_btn" type="button" value="确定" onclick="conf();"/>
    </td>
  </tr>
</table>

</form>
</body>
</html>
