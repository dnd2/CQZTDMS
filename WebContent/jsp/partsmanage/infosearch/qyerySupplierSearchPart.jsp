<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
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
		__extQuery__(1);
	}

</script>
</head>
<body>
<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;基本信息查询&gt;供应商信息管理
<form method="post" name ="fm" id="fm">
	<table class="table_edit">
    <tr>
      <td>
      <%
      	String partId = request.getParameter("partId");
       %>
      	<input name="partId" type="hidden" id="partId" class="middle_txt" value="<%=partId %>"/>
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
      	<input type="button" name="BtnQuery" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1)" >
      </td>
      <td class="table_query_2Col_label_4Letter">
       	<input type="button" value="后退" onclick="javascript:history.go(-1)" class="normal_btn"/> 	
      </td>
  	</tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	
<table class="table_edit">
    <tr>
      <td>
      	<input value="确定" type="button" id="SUPPLIER_NAME" class="normal_btn" onclick="addSure()"/>
      </td>
  	</tr>
</table>
</form>

<script type="text/javascript" >
	var myPage;

	var url = "<%=contextPath%>/partsmanage/infoSearch/SupplierInfoSearch/querySupplierInfoSearch.json";
				
	var title = null;

	var columns = [
	            {id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"sp\")'/>", width:'8%',sortable: false,dataIndex: 'SUPPLIER_ID',renderer:myCheckBox},
				{header: "供应商代码", dataIndex: 'SUPPLIER_CODE', align:'center'},
				{header: "供应商名称", dataIndex: 'SUPPLIER_NAME', align:'center'}
		      ];
		      
    //清除供应商代码
	function reset(){
		document.getElementById("SUPPLIER_CODE").value = "";
	}
		//全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='sp' value='" + value + "' />");
	}
	function addSure() {
		var chk = document.getElementsByName("sp");
		var len = chk.length;
		var sel = false; //判断是否选择
		for(var i=0;i<len;i++){
			if (chk[i].checked) {
			 	sel = true;
			 	break;
			}
		}
		if (!sel) {
			MyAlert('请至少选择一个供应商');
			return;
		}
		var sure = confirm('确认添加');
		if (sure) {
			var url = "<%=contextPath%>/partsmanage/infoSearch/SupplierInfoSearch/addSupplier.do";
			fm.action = url;
			fm.submit();
		}
	}

</script>
</body>
</html>