<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>服务活动费用查询报表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit()
	{
   		loadcalendar();  //初始化时间控件
	}
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
</script>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理&gt;轿车售后索赔报表&gt;服务活动费用查询报表</div>

  <form method="post" name="fm" id="fm">
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
   <!-- 查询条件 begin -->
  <table class="table_query">       
          <tr>
            <td align="right" nowrap>登记起止时间：</td>
            <td align="left">
            	<div align="left">
            		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            		&nbsp;至&nbsp;
            		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            	</div>
			</td>
			<td align="right" nowrap>订货方：</td>
			<td align="left" nowrap>
				<input type="text"  name="dealerCode" size="15" value="" readonly="readonly" id="dealerCode"/>
				<input name="button3" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','','false', '${orgId}',true,'')" value="..." />
            	<input class="cssbutton" type="button" value="清空" onclick="clrTxt('dealerCode');"/>
            </td>
            <td align="left" nowrap>
            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询"  onclick="__extQuery__(1)"/>&nbsp;
            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="下载"  onclick="exportExcel()" />
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
	var url = "<%=contextPath%>/sales/ordermanage/orderquery/AllProvinceInvoiceDetail/queryAllProvinceInvoiceDetail.json";
				
	var title = null;

	var columns = [
				{header: "登记日期", dataIndex: 'BILLING_DATE', align:'center'},
				{header: "采购订单号", dataIndex: 'ORDER_NO', align:'center'},
				{header: "总部销售订单", dataIndex: 'ERP_ORDER', align:'center'},
				{header: "启票价格", dataIndex: 'BILLING_AMOUNT', align:'center'},
				{header: "车辆编码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "数量", dataIndex: 'DELIVERY_AMOUNT', align:'center'},
				{header: "生产组织", dataIndex: 'CODE_DESC', align:'center'},
				{header: "发运地点", dataIndex: 'ADDRESS', align:'center'},
				{header: "所属单位", dataIndex: 'DEALER_NAME1', align:'center'},
				{header: "采购单位", dataIndex: 'DEALER_NAME2', align:'center'},
				{header: "收车单位", dataIndex: 'DEALER_NAME3', align:'center'}
		      ];
		      
//设置超链接  begin      
	function exportExcel()
	{
		$('fm').action= "<%=contextPath%>/sales/ordermanage/orderquery/AllProvinceInvoiceDetail/AllProvinceInvoiceDetailExcel.do";
		$('fm').submit();
	}

  function showMonthFirstDay()     
  {     
	  var Nowdate = new Date();     
	  var MonthFirstDay = new Date(Nowdate.getYear(),Nowdate.getMonth(),1);     
	  return DateUtil.Format("yyyy-MM-dd",MonthFirstDay);  
  }     
  function showMonthLastDay()     
  {     
	  var Nowdate = new Date();     
	  var MonthNextFirstDay = new Date(Nowdate.getYear(),Nowdate.getMonth()+1,1);     
	  var MonthLastDay = new Date(MonthNextFirstDay-86400000);     
	  return DateUtil.Format("yyyy-MM-dd",MonthLastDay);   
  }

  $('beginTime').value=showMonthFirstDay();
  $('endTime').value=showMonthLastDay();
  
//设置超链接 end
	
</script>
<!--页面列表 end -->

</body>
</html>