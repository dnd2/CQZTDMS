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
<title>经销商与供货方关系维护</title>

</head>
<body>
<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;基本信息查询&gt;经销商与供货方关系维护 
<form method="post" name ="fm" id="fm">
	<table class="table_edit">
     <tr>
      <td class="table_query_2Col_label_6Letter">供货方代码：</td>
      <td class="table_query_2Col_input">
      	<input class="middle_txt" id="dcCode" name="dcCode" value="" type="text"/>
      </td>
      <td class="table_query_2Col_label_6Letter">供货方名称：</td>
      <td>
      	<input name="dcName" type="text" id="dcName" class="middle_txt"/>
      </td>
  	</tr>
  	<tr>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td align="right">
      	<input type="button" name="BtnQuery" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" >
      </td>
  	</tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

<script type="text/javascript" >
	var myPage;

	var url = "<%=contextPath%>/partsmanage/infoSearch/DealerSupplierInfo/queryDCRelactionInfo.json";
				
	var title = null;

	var columns = [
				{header: "供货方代码", dataIndex: 'DC_CODE', align:'center'},
				{header: "供货方简称", dataIndex: 'SHORT_NAME', align:'center'},
				{header: "联系电话", dataIndex: 'PHONE_NUMBER', align:'center'},
				{header: "联系人", dataIndex: 'LINK_MAN', align:'center'},
				{header: "传真号码", dataIndex: 'FAX', align:'center'},
				{header: "邮编号码", dataIndex: 'ZIPCODE', align:'center'},
				{header: "经销商个数", dataIndex: 'COUNT', align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'DC_ID',renderer:myLink ,align:'center'}
		      ];
		    
//设置超链接  begin      
	
	//设置超链接
	function myLink(value,meta,record)
	{
  		return String.format("<a href=\"#\" onclick='sel(\""+value+"\")'>[维护]</a>");
	}
	
	//详细页面
	function sel(value)
	{
		fm.action = '<%=contextPath%>/partsmanage/infoSearch/DealerSupplierInfo/updateRelactionInit.do?dcId='+value;
		fm.submit();
	}
	


	
//设置超链接 end
	
</script>
</body>
</html>