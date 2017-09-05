<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>发运申请数量查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
    function download(){
		if(submitForm('fm')){
			document.fm.action='<%=request.getContextPath()%>/sales/ordermanage/delivery/DeliveryAmount/doDownload.json';
			document.fm.target="_self";
			document.fm.submit();
		}
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：销售订单管理 &gt; 订单查询 &gt; 初审订单未通过车型统计</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
	<tr>
		<td class="table_query_2Col_label_6Letter">审核日期： </td>
         <td nowrap="nowrap">
          <input name="start_date" id="start_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="start_date,end_date" hasbtn="true" callFunction="showcalendar(event, 'start_date', false);">
         	&nbsp;至&nbsp;
          <input name="end_date" id="end_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="start_date,end_date" hasbtn="true" callFunction="showcalendar(event, 'end_date', false);">
          </td>
	    <td align="right">发运单号：</td>
		<td>
			<input type="text" name="dlvryNo" class="middle_txt" id="dlvryNo" value="" />
		</td>
		<td align="right">订单号：</TD>
		<td align="left">
			<input type="text" name="orderNo" class="middle_txt" id="orderNo" value="" />
		</td>
	 	<c:if test="${dutyType != 10431005}">
            	<td align="right" nowrap>选择经销商：</td>
           	 <td align="left">
                <input type="text" name="dealerCode" size="20" value="" id="dealerCode"/>
                <input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" onclick="showOrgDealer('dealerCode','','true', '${orgId}','','')" value="..." />               
                <input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCode');"/>
            	</td>
        </c:if>	
    </tr>
	<tr>
		<td align="center" colspan="8">
			<input name="queryBtn" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">
 			<input name="downloadBtn" type=button class="cssbutton" onClick="download();" value="下载">		</td>
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
	var url = "<%=contextPath%>/sales/ordermanage/delivery/DeliveryAmount/deliveryAmountQuery.json?COMMAND=1";
	var title = null;
	var columns = [
				{header: "审核时间", dataIndex: 'AUDIT_TIME', align:'center'},
				{header: "大区", dataIndex: 'ROOT_ORG_NAME', align:'center'},
				{header: "省份", dataIndex: 'PQ_ORG_NAME', align:'center'},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "发运单号", dataIndex: 'DLVRY_REQ_NO', align:'center'},
				{header: "订单号", dataIndex: 'ORDER_NO', align:'center'},
				{header: "物料编码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "审核状态", dataIndex: 'REQ_STATUS', align:'center',renderer:getItemValue},
				{header: "申请未发数量", dataIndex: 'AMOUNT', align:'center'}
		      ];
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
	function downLoad(){
    	$('fm').action="<%=contextPath%>/sales/ordermanage/orderquery/DealerResourceQuery/resourceQueryListDownLoad.do";
     	$('fm').submit();
    }
</script>
<!--页面列表 end -->
</body>
</html>