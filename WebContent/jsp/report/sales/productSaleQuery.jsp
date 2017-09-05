<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>生产销售快报</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit()
	{
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理&gt;整车销售报表&gt;生产销售快报</div>
  <form method="post" name="fm" id="fm">
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
   <!-- 查询条件 begin -->
  <table class="table_query">
  		  <tr>
  		  		<td><div align="right">选择时间：</div></td>
				<td align="left" nowrap="true">
					<input name="chooseDate" type="text" class="short_time_txt" id="chooseDate" readonly="readonly" value=""/> 
					<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'chooseDate', false);" />
				</td>
	            <td align="center">
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
	var url = "<%=contextPath%>/report/reportOne/ProductSalesReport/getProductSaleReportInfo.json";
				
	var title = null;

	var columns = [
				{header: "车辆型号", dataIndex: 'GROUP_NAME', align:'center',renderer:myShow},
				{header: "当日入库", dataIndex: 'D_AMOUNT', align:'center',renderer:myShow},
				{header: "本月入库", dataIndex: 'M_AMOUNT', align:'center',renderer:myShow},
				{header: "本年入库", dataIndex: 'Y_AMOUNT', align:'center',renderer:myShow},
				{header: "当日开票", dataIndex: 'DF_AMOUNT', align:'center',renderer:myShow},
				{header: "本月开票", dataIndex: 'MF_AMOUNT', align:'center',renderer:myShow}	,
				{header: "本年开票", dataIndex: 'YF_AMOUNT', align:'center',renderer:myShow}	,
				{header: "可发车", dataIndex: 'SV_AMOUNT', align:'center',renderer:myShow},
				{header: "当日最终销售", dataIndex: 'DS_AMOUNT', align:'center',renderer:myShow},
				{header: "本月最终销售", dataIndex: 'MS_AMOUNT', align:'center',renderer:myShow},
				{header: "本年最终销售", dataIndex: 'YS_AMOUNT', align:'center',renderer:myShow},
				{header: "外借", dataIndex: 'B_AMOUNT', align:'center',renderer:myShow},
				{header: "库存合计", dataIndex: 'STORAGE_AMOUNT', align:'center',renderer:myShow}
		      ];
function myShow(value,metadata,record){
	var groupId = record.data.GROUP_ID
	if(groupId == "" || groupId == null){
		return String.format("<font style='color:black;font-weight:bold'>"+value+"</font>");
	}else{
		return String.format(value);
	}
	
}

function exportExcel(){
	var chooseDate = document.getElementById("chooseDate").value;
	window.location.href = "<%=contextPath%>/report/reportOne/ProductSalesReport/toExcel.do?chooseDate="+chooseDate;
}
</script>
<!--页面列表 end -->

</body>
</html>