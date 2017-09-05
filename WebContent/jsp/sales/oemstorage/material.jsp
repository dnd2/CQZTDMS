<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>移库单管理</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit()
	{
   		var stName = '<c:out value="${stName}"/>';
   		document.getElementById("stName").value = stName;
	}

</script>
</head>

<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;移库单管理</div>
<form method="post" name="fm" id="fm">
<input type="hidden" name="stName" id="stName" value=""/>
<table width="95%" border="0" align="center" class="table_query">
	<tr>
	  <td width="13%" align="right" class="table_query_2Col_label_6Letter">物料代码：</TD>
	  <td width="35%" class="table_query_2Col_input">
			<input name="materialCode" id="materialCode" class="middle_txt"/>
	  </td>
	  <td align="right" class="table_query_2Col_label_6Letter">物料名称：</td>
	  <td class="table_query_2Col_input">
			<input name="materialName" id="materialName" class="middle_txt"/>
	  </td>
    </tr>
	<tr>
	  <td align="right" class="table_query_2Col_label_6Letter">批次号：</TD>
     	 <td class="table_query_2Col_input">
			<input name="batchNo" id="batchNo" class="middle_txt"/>
     	 </td>
    </tr>

	<tr>
      <td align="left" class="table_query_2Col_label_6Letter"></td>
	  <td class="table_query_2Col_input">&nbsp;</td>
	  <td align="right" valign="middle" class="table_query_2Col_label_6Letter">&nbsp;</td>
      <td valign="bottom" class="table_query_2Col_input">
      	<span class="table_query_2Col_label_6Letter">
        	<input name="button" type="button"  class="normal_btn"	onclick="__extQuery__(1)" value="查询" />
      	</span>
      </td>
	</tr>
</table>
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
  </form> 
<!--页面列表 begin -->
<script type="text/javascript" >

	var myPage;
//查询路径
	var url = "<%=contextPath%>/sales/oemstorage/OemStorageQuery/addMaterialList.json";
				
	var title = null;

	var columns = [
				{header: "请选择", dataIndex: 'MATERIAL_ID', align:'center',renderer:myRadio},
				{header: "物料编码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "批次号-数量", dataIndex: 'BATCH_NO', align:'center'}
		      ];
		      
//设置超链接  begin      
	
	function myRadio(value,meta,record)
	{
		return String.format("<input type='radio' onclick='setModel(\""+record.data.MATERIAL_ID+"\",\""+record.data.MATERIAL_CODE+"\",\""+record.data.MATERIAL_NAME+"\",\""+record.data.BATCH_NO+"\")'"); 
	}
	
	function setModel(valid,valcode,valname,valno)
	{
		parent._hide();
		parent.$('inIframe').contentWindow.getModel(valid,valcode,valname,valno);
		
	}
	
	//__extQuery__(1);
	
//设置超链接 end
	
</script>
<!--页面列表 end -->
</body>
</html>
