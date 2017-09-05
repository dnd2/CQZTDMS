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
<title>配件采购订单上报</title>
</head>

<script language="JavaScript">

	//初始化方法
	function doInit()
	{
		loadcalendar();//时间控件
	}

</script>
<body>
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;经销商配件采购&gt;配件采购订单上报
<form id="fm" name="fm" method="post">
<input type="hidden" id="orderId" name="orderId" value="" />
<input type="hidden" id="itemCount" name="itemCount" value="" />
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
      <tr>
        <td class="table_query_2Col_label_7Letter">采购订单编号：</td>
        <td><input name="orderNo" type="text" id="orderNo"  class="middle_txt"/></td>
        <td class="table_query_2Col_label_5Letter">创建时间：</td>
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
        <td class="table_query_2Col_label_5Letter">&nbsp;</td>
        <td class="">&nbsp;</td>
      </tr>
      <tr>
        <td class="">&nbsp;</td>
        <td class="">&nbsp;</td>
        <td class="">&nbsp;</td>
        <td class="">&nbsp;</td>
	    <td align="right">
	    	<input type="button" name="BtnQuery22"  value="查询"  class="normal_btn"  onClick="__extQuery__(1);">
	    </td>
      </tr>
    </table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script type="text/javascript" >

	var myPage;

	var url = "<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/queryPartOrderForword.json";
				
	var title = null;

	var columns = [
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
	
	//上报操作
	function myLink(value,meta,record){
		return String.format("<a href='#' onclick='orderForword(\""+ value +"\",\""+ record.data.ITEM_COUNT +"\")'>[上报]</a>");
	}
	
	//提报操作
	function orderForword(value,itemCount){
		$("orderId").value = value;
		$("itemCount").value = itemCount;
		makeNomalFormCall("<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/orderPartCheck.json",showForwordValue,'fm','queryBtn'); 
	}
	
	//上报回调函数
	function showForwordValue(json)
	{
		if(json.returnValue == '0')
		{
			MyAlert("提报成功！");
			__extQuery__(1);
		}else if(json.returnValue == '1'){
			MyAlert("错误信息，不允许上报！");
		}else if(json.returnValue == '2'){
			MyAlert("订单行数超过规则最大行数，不允许上报！");
		}else if(json.returnValue == '3'){
			MyAlert("周期内上报次数过多，不允许上报！");
		}else if(json.returnValue == '4'){
			MyAlert("上报时间不在规则范围内，不允许提报！");
		}
	}
	
//设置超链接 end
	
</script>
</body>
</html>