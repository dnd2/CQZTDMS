<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆维修与保养记录</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理&gt;轿车售后索赔报表&gt;车辆维修与保养记录</div>

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
	var url = "<%=contextPath%>/report/jcafterservicereport/VehicleRepairReport/maintainReport.json";
				
	var title = null;

	var columns = [
				{header: "序号", renderer:getIndex, align:'center'},
				{header: "用户名称", dataIndex: 'CTM_NAME', align:'center'},
				{header: "VIN码",sortable: false,dataIndex: 'VIN' ,align:'center'},
				{header: "购车时间", dataIndex: 'PURCHASED_DATE', align:'center'},
				{header: "车型", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "用户地址", dataIndex: 'ADDRESS', align:'center'},
				{header: "用户电话", dataIndex: 'PHONE', align:'center'},
				{header: "维修次数", dataIndex: 'WEIXIU', align:'center',renderer:myLink1},
				{header: "保养次数", dataIndex: 'BAOYANG', align:'center',renderer:myLink2},
				{header: "服务活动次数", dataIndex: 'FUWUHUODONG', align:'center',renderer:myLink3}
		      ];
		      
//设置超链接  begin      
	function exportExcel()
	{
		fm.action = "<%=contextPath%>/report/jcafterservicereport/VehicleRepairReport/maintainReportExcel.do";
		fm.submit();
	}

	function myLink1(value,meta,rec){
		var vin = rec.data.VIN ;
		var str = '<a href="#" onclick="myHandler1()">'+rec.data.WEIXIU+'</a>' ;
		return str ;
	}

	function myHandler1(){
		var value = $('vin').value ;
		var url = '<%=contextPath%>/report/jcafterservicereport/VehicleRepairReport/weixiu.do?vin='+value ; 
		window.open(url) ;
	}

	function myLink2(value,meta,rec){
		var vin = rec.data.VIN ;
		var str = '<a href="#" onclick="myHandler2()">'+rec.data.BAOYANG+'</a>' ;
		return str ;
	}
	
	function myHandler2(){
		var value = $('vin').value ;
		var url = '<%=contextPath%>/report/jcafterservicereport/VehicleRepairReport/baoyang.do?vin='+value ; 
		window.open(url) ;
	}

	function myLink3(value,meta,rec){
		var vin = rec.data.VIN ;
		var str = '<a href="#" onclick="myHandler3()">'+rec.data.FUWUHUODONG+'</a>' ;
		return str ;
	}
	
	function myHandler3(){
		var value = $('vin').value ;
		var url = '<%=contextPath%>/report/jcafterservicereport/VehicleRepairReport/fuwuhuodong.do?vin='+value ; 
		window.open(url) ;
	}
 
  
//设置超链接 end
	
</script>
<!--页面列表 end -->

</body>
</html>