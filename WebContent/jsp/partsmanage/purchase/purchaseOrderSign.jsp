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
<title>配件采购订单签收</title>
</head>

<script language="JavaScript">

	//初始化方法
	function doInit()
	{
		loadcalendar();//时间控件
	}

</script>
<body>
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;经销商配件采购&gt;配件订单签收
<form id="fm" name="fm" method="post">
<input type="hidden" id="oNo" name="oNo" value="" />
<input type="hidden" id="dNo" name="dNo" value="" />
<input type="hidden" id="oid" name="oid" value="" />
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
      <tr>
        <td class="table_query_2Col_label_5Letter">采购日期：</td>
        <td><div align="left">
           		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
           		&nbsp;至&nbsp;
           		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
           	</div>
        </td>
        <td class="table_query_2Col_label_5Letter">采购单号：</td>
        <td><input name="orderNo" type="text" id="orderNo"  class="middle_txt"/></td>
      </tr>  
      <tr>   	
        <td class="table_query_2Col_label_5Letter">货运单号：</td>
        <td><input name="doNo" type="text" id="doNo"  class="middle_txt"/></td>
        <td align="right">
	    	<input type="button" name="BtnQuery22" id="queryBtn" value="查询"  class="normal_btn"  onClick="__extQuery__(1);">
	    </td> 	
      </tr>
    </table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script type="text/javascript" >

	var myPage;

	var url = "<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/queryDirsignList.json";
				
	var title = null;

	var columns = [
				{header: "货运单号", dataIndex: 'DO_NO', align:'center'},
				{header: "采购订单号", dataIndex: 'ORDER_NO', align:'center'},
				{header: "采购日期", dataIndex: 'RAISE_DATE', align:'center'},
				{header: "要求到货日期", dataIndex: 'REQUIRE_DATE', align:'center'},
				{header: "货运单项数", dataIndex: 'COUT', align:'center'},
				//{header: "创建人", dataIndex: 'NAME', align:'center'},
				{header: "操作", dataIndex: 'IS_SIGNED', align:'center',renderer:myLink}
		      ];
		    
//设置超链接  begin

	//操作 
	function myLink(value,meta,record){
		var is_sign = value;
		if(is_sign == 1)
		{
			return String.format("<a href='#' onclick='donoInfo(\""+ record.data.DO_NO +"\",\""+ record.data.ORDER_NO +"\",\""+ record.data.ORDER_ID +"\")'>[明细]</a>");
		}else
		{
			return String.format("<a href='#' onclick='orderPartSign(\""+ record.data.DO_NO +"\",\""+ record.data.ORDER_NO +"\",\""+ record.data.ORDER_ID +"\")'>[签收]</a>");
		}
		
	}
	
	//详细页面
	function donoInfo(doNo, orderNo, orderId)
	{
		$("oNo").value = orderNo;
		$("dNo").value = doNo;
		$("oid").value = orderId
		fm.action = "<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/doNoSignInfo.do";
		fm.submit();
	}
	
	//签收页面
	function orderPartSign(doNo, orderNo, orderId)
	{
		$("oNo").value = orderNo;
		$("dNo").value = doNo;
		$("oid").value = orderId
		fm.action = "<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/orderPartSign.do";
		fm.submit();
	}
	
//设置超链接 end
	
</script>
</body>
</html>