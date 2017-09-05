<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件基本信息</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;基本信息查询&gt;配件基本信息管理
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
    <tr>
      <td class="table_query_2Col_label_6Letter">配件号：</td>
      <td><input name="PART_CODE" type="text" id="PART_CODE"  class="middle_txt"/></td>
      <td class="table_query_2Col_label_6Letter">配件名称：</td>
      <td><input name="PART_NAME" type="text" id="PART_NAME"  class="middle_txt"/></td>
    </tr>
    <tr>
      <td class="table_query_2Col_label_6Letter">供应商代码：</td>
      <td>
      	<input class="long_txt" id="SUPPLIER_CODE" name="SUPPLIER_CODE" value="" type="text"/>
        <input class="mark_btn" type="button" value="&hellip;" onclick="showSuppliar('SUPPLIER_CODE','SUPPLIER_ID','true')"/>
        <input class="normal_btn" type="button" value="清除" onclick="reset();"/>
        <input id="SUPPLIER_ID" name="SUPPLIER_ID" type="hidden" value="">
      </td>
      <td class="table_query_2Col_label_6Letter">供应商名称：</td>
      <td><input name="SUPPLIER_NAME" type="text" id="SUPPLIER_NAME"  class="middle_txt"/></td>
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
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;

	var url = "<%=contextPath%>/partsmanage/infoSearch/PartInfoSearch/queryPartInfoSearch.json";
				
	var title = null;

	var columns = [
				{header: "配件号", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "配件类型", dataIndex: 'PART_TYPE', align:'center',renderer:getItemValue},
				{header: "替代配件", dataIndex: 'REPLACE_PART_ID', align:'center'},
				{header: "停用", dataIndex: 'STOP_FLAG', align:'center'},
				{header: "是否新件", dataIndex: 'IS_NEW_PART', align:'center'},
				{header: "销售价格", dataIndex: 'SALE_PRICE', align:'center'},
				{header: "销售指导价", dataIndex: 'CUSTOMER_PRICE', align:'center'},
				{header: "索赔价格", dataIndex: 'CLAIM_PRICE', align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'PART_ID',renderer:myLink ,align:'center'}
		      ];
		      
//设置超链接  begin      
	
	//设置超链接
	function myLink(value,meta,record)
	{
  		return String.format("<a href=\"#\" onclick='sel(\""+value+"\")'>[修改]</a>");
	}
	
	//详细页面
	function sel(value)
	{
		OpenHtmlWindow('<%=contextPath%>/partsmanage/infoSearch/PartInfoSearch/queryPartDetailMod.do?partId='+value,800,500);
	}
	
	//清除供应商代码
	function reset(){
		document.getElementById("SUPPLIER_CODE").value = "";
	}
	function  viewPart(){
		__extQuery__(1);
	}
	viewPart();
	
//设置超链接 end
	
</script>
<!--页面列表 end -->
</body>
</html>