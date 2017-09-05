<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>需求预测上报  </title>
<script language="JavaScript" src="<%=contextPath %>/js/ut.js"></script>
<script language="JavaScript">
	function doInit(){
	    initUrl(${area});
		__extQuery__(1);
    }

</script>
</head>

<body>
	<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>需求预测>需求预测上报
	</div>
<form name="fm" method="post" id="fm">
<input type="hidden" name="area" value="${area }" />
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 

</form>
<form name="form1" method="post" id="form1">  
 <table class="table_query" >
     <tr>
	    <td>
	        <input name="closebtn" type="button" class="cssbutton" onclick="_hide();" value="关闭" />
	        <input name="loadbtn" type="button" class="cssbutton" onclick="doExport();" value="下载" />
	   	</td>
    </tr>
</table>
</form>
</div>
<p>&nbsp;</p>

<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
//查询路径

	var url;
	
	function initUrl(areaId){
		url ="<%=contextPath%>/sales/planmanage/RequirementForecast/RequireForecastManage/selectLessForecastDealers.json?area="+areaId;
	}		
		
	var title = null;

	var  columns = [
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "预测月份", dataIndex: 'FORECAST_MONTH', align:'center'},
				{header: "联系人", dataIndex: 'LINK_MAN', align:'center'},
				{header: "联系方式", dataIndex: 'PHONE', align:'center'},
				{header: "物料代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "预测数量", dataIndex: 'AMOUNT', align:'center'},
				{header: "目标数量", dataIndex: 'SALE_AMOUNT', align:'center'}
		        ];
    //下载
	function doExport(){
		$('fm').action= "<%=request.getContextPath()%>/sales/planmanage/RequirementForecast/RequireForecastManage/oemRequireForecastLessExport.do";
		$('fm').submit();
	}
</script>
</body>
</html>
