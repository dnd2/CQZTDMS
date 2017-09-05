<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件采购订单查询</title>

<script language="JavaScript">

	//初始化方法
	function doInit()
	{
		loadcalendar();  //初始化时间控件
	}

</script>
</head>
<body>
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;经销商配件采购&gt;配件采购订单查询
<form method="post" name ="fm" id="fm">
<input type="hidden" name="dcId" value="<c:out value="${dcId}"/>"/>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
     <tr>
      <td class="table_query_2Col_label_6Letter">采购订单号：</td>
      <td><input class="middle_txt" id="orderNo" name="orderNo" value="" type="text"/></td>
      <td align="right" nowrap="nowrap">&nbsp;</td>
      <td colspan="2" align="left" >&nbsp;</td>
	  </tr>
	  <tr>
	    <td class="table_query_2Col_label_6Letter">创建时间：</td>
        <td><div align="left">
           		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
           		&nbsp;至&nbsp;
           		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
           	</div>
        </td>
        <td class="table_query_2Col_label_6Letter">订单状态：</td>
	    <td><script type="text/javascript">     
 				  genSelBoxExp("orderStatus",<%=Constant.PART_ORDER_STATUS%>,"",true,"short_sel","","false",'<%=Constant.PART_ORDER_STATUS_01%>');
			 </script>
	    </td>
  	  </tr>
  	  <tr>
	    <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
      	<td align="right">
      		<input type="button" name="BtnQuery" id="queryBtn" value="查询"  class="normal_btn" onClick="__extQuery__(1);" >
      	</td>
  	  </tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script type="text/javascript" >

	var myPage;

	var url = "<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/purchaseOrderSearch.json";
				
	var title = null;

	var columns = [
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "订单号", dataIndex: 'ORDER_NO', align:'center',renderer:mySelect},
				{header: "要求到货时间", dataIndex: 'REQUEST_DATE', align:'center'},
				{header: "运输方式", dataIndex: 'TRANS_TYPE', align:'center',renderer:getItemValue},
				{header: "创建日期", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "订单状态", dataIndex: 'ORDER_STATUS', align:'center',renderer:getItemValue},
				{header: "操作部门", dataIndex: 'ORG_NAME', align:'center'},
				{header: "操作时间", dataIndex: 'UPDATE_DATE', align:'center'}
		      ];
		    
//设置超链接  begin      
	//设置超链接
	function mySelect(value,meta,record){
  		return String.format(
         "<a href=\"#\" onclick='sel(\""+record.data.ORDER_ID+"\")'>"+ value +"</a>");
	}
	
	//详细页面
	function sel(value){
		OpenHtmlWindow('<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/partOrderInfo.do?orderId='+value,800,500);
	}

//设置超链接 end
	
</script>
</body>
</html>