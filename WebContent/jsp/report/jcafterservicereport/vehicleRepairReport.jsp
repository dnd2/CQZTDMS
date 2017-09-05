<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆维修记录</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理&gt;轿车售后索赔报表&gt;车辆维修记录</div>

  <form method="post" name="fm" id="fm">
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
   <!-- 查询条件 begin -->
  <table class="table_query">
       <tr>
         <td align="right" nowrap >VIN码：</td>
         <td colspan="6" nowrap>
         	<input type="text" name="vin" class="middle_txt" id="vin" datatype="0,is_vin,17"/>
         </td>
         <td>&nbsp;</td> 
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
	var url = "<%=contextPath%>/report/jcafterservicereport/VehicleRepairReport/vehicleRepairReport.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'NUM', align:'center'},
				{header: "维修类型", dataIndex: 'CLAIM_TYPE', align:'center',renderer:getItemValue},
				{header: "VIN码",sortable: false,dataIndex: 'VIN' ,align:'center'},
				{header: "维修站", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "维修时间", dataIndex: 'RO_STARTDATE', align:'center'},
				{header: "行驶里程", dataIndex: 'IN_MILEAGE', align:'center'},
				{header: "出厂日期", dataIndex: 'PRODUCT_DATE', align:'center'},
				{header: "故障模式", dataIndex: 'TROUBLE_CODE_NAME', align:'center'},
				{header: "故障部位", dataIndex: 'DAMAGE_AREA_NAME', align:'center'},
				{header: "故障件数量", dataIndex: 'COUNTID', align:'center'}
		      ];
		      
//设置超链接  begin      
	function exportExcel()
	{
		fm.action = "<%=contextPath%>/report/jcafterservicereport/VehicleRepairReport/queryVehicleRepairReportExcel.do";
		fm.submit();
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
  $('pbeginTime').value=showMonthFirstDay();
  $('pendTime').value=showMonthLastDay();
  $('bbeginTime').value=showMonthFirstDay();
  $('bendTime').value=showMonthLastDay();

	function clrTxt()
	{
		document.getElementById("groupCode").value = "";
	}
  
//设置超链接 end
	
</script>
<!--页面列表 end -->

</body>
</html>