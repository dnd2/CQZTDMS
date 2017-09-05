<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>供应商信息查询</title>

<script language="JavaScript">

	//初始化方法
	function doInit()
	{
		
	}

</script>
</head>
<body>
<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;基本信息查询&gt;供应商信息查询
<form method="post" name ="fm" id="fm">
	<table class="table_edit">
    <tr>
      <td class="table_query_2Col_label_6Letter">供应商代码：</td>
      <td class="table_query_2Col_input">
      	<input class="long_txt" id="SUPPLIER_CODE" name="SUPPLIER_CODE" value="" type="text"/>
        <input class="mark_btn" type="button" value="&hellip;" onclick="showSuppliar('SUPPLIER_CODE','SUPPLIER_ID','true')"/>
        <input class="normal_btn" type="button" value="清除" onclick="reset();"/>
        <input id="SUPPLIER_ID" name="SUPPLIER_ID" type="hidden" value="">
      </td>
      <td class="table_query_2Col_label_6Letter">供应商名称：</td>
      <td>
      	<input name="SUPPLIER_NAME" type="text" id="SUPPLIER_NAME"  class="middle_txt"/>
      </td>
  	</tr>
  	<tr>
      <td class="">&nbsp;</td>
      <td class="">&nbsp;</td>
      <td class="">&nbsp;</td>
	  <td class="">&nbsp;</td>
      <td class="">&nbsp;</td>
      <td class="">&nbsp;</td>
      <td class="table_query_2Col_label_4Letter">
      	<input type="button" name="BtnQuery" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" >
      </td>
  	</tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

<script type="text/javascript" >
	var myPage;

	var url = "<%=contextPath%>/partsmanage/infoSearch/SupplierInfoSearch/querySupplierInfoSearch.json";
				
	var title = null;

	var columns = [
				{header: "供应商代码", dataIndex: 'SUPPLIER_CODE', align:'center'},
				{header: "供应商名称", dataIndex: 'SUPPLIER_NAME', align:'center'},
				{header: "简称", dataIndex: 'SHORT_NAME', align:'center'},
				{header: "地址", dataIndex: 'ADDRESS', align:'center'},
				{header: "邮编", dataIndex: 'ZIPCODE', align:'center'},
				{header: "传真", dataIndex: 'FAX', align:'center'},
				{header: "联系人", dataIndex: 'LINK_MAN', align:'center'},
				{header: "联系电话", dataIndex: 'PHONE_NUMBER', align:'center'},
				{header: "备注", dataIndex: 'REMARK', align:'center'}
		      ];
		      
    //清除供应商代码
	function reset(){
		document.getElementById("SUPPLIER_CODE").value = "";
	}
</script>
</body>
</html>