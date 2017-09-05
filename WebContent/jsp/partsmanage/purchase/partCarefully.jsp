<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" 
"http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
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
<title>配件采购订单预审核</title>
</head>

<script language="JavaScript">

	//初始化方法
	function doInit()
	{
		loadcalendar();//时间控件
	}

</script>
<body>
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;经销商配件采购&gt;配件采购订单预审核
<form id="fm" name="fm" method="post">
<input type="hidden" id="orderId" name="orderId" value="" />
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
      <tr>
        <td class="table_query_2Col_label_7Letter">采购订单编号：</td>
        <td><input name="orderNo" type="text" id="orderNo"  class="middle_txt"/></td>
        <td class="table_query_2Col_label_8Letter">创建时间：</td>
        <td><div align="left">
           		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
           		&nbsp;至&nbsp;
           		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
           	</div>
        </td>
      </tr>
      <tr>
        <td class="table_query_2Col_label_7Letter">供货方：</td>
        <td><select name="dcId" id="dcId" class="short_sel">
		        <option value="">-请选择-</option>
				 <c:forEach items="${dcList}" var="dc">
				 	<option value="<c:out value="${dc.DC_ID}"/>"><c:out value="${dc.DC_NAME}"/></option>
				 </c:forEach>
            </select >
        </td>
        <td class="table_query_2Col_label_8Letter">二级经销商代码：</td>
        <td><input type="text" name="dealerCode" id="dealerCode" value=""/></td>
      </tr>
      <tr>
        <td class="">&nbsp;</td>
        <td class="">&nbsp;</td>
        <td class="">&nbsp;</td>
        <td class="">&nbsp;</td>
	    <td align="right">
	    	<input type="button" name="BtnQuery22"  value="查询"  class="normal_btn" onClick="__extQuery__(1);">
	    </td>
      </tr>
    </table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script type="text/javascript" >

	var myPage;

	var url = "<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/queryPartCarefully.json";
				
	var title = null;

	var columns = [
				{header: "二级经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "二级经销商名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "采购订单编号", dataIndex: 'ORDER_NO', align:'center',renderer:mySelect},
				{header: "供货方", dataIndex: 'DC_NAME', align:'center'},
				{header: "要求到货时间", dataIndex: 'REQUIRE_DATE', align:'center'},
				{header: "项数", dataIndex: 'ITEM_COUNT', align:'center'},
				{header: "创建人", dataIndex: 'NAME', align:'center'},
				{header: "创建时间", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "总价格(元)", dataIndex: 'ORDER_PRICE', align:'center'},
				{header: "操作", dataIndex: 'ORDER_ID', align:'center',renderer:myLink}
		      ];
		    
//设置超链接  begin

	//详细信息      
	function mySelect(value,meta,record)
	{
		return String.format("<a href='#' onclick='sel(\""+ record.data.ORDER_ID +"\")'>"+ value +"</a>");
	}
	
	//详细页面
	function sel(value){
		OpenHtmlWindow('<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/partOrderInfo.do?orderId='+value,800,500);
	}
	
	//提报操作
	function myLink(value,meta,record){
		return String.format("<a href='#' onclick='forwordOrderPart(\""+ value +"\")'>[审核]</a>");
	}
	
	//提报
	function forwordOrderPart(value){
		$("orderId").value = value;
		fm.action = "<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/forwordOrderPart.do";
		fm.submit();
	}
	
//设置超链接 end
	
</script>
</body>
</html>