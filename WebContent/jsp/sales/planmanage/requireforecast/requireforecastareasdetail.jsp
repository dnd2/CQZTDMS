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
	    initUrl('');
	    initGroupCode();
		__extQuery__(1);
    }

</script>

<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
//查询路径
	var url;
	function initUrl(areaId){	   
		url ="<%=contextPath%>/sales/planmanage/RequirementForecast/RequireForecastManage/selectForecastAreas.json?area="+areaId;
	}
	
	var title = null;

	var  columns = [
				{header: "区域名称", dataIndex: 'ORG_NAME', align:'center'},
				{header: "区域代码", dataIndex: 'AREA_CODE', align:'center'},
				{header: "预测月份", dataIndex: 'FORECAST_MONTH', align:'center'}
		        ];	 
	
	function initGroupCode(){
		var url = "<%=contextPath%>/sales/planmanage/RequirementForecast/RequireForecastManage/findAreaVehicleType.json";
		makeCall(url,showColumn,{});
	}
		      
	
	function showColumn(json){
		      for(var i=0;i<json.groupList.length;i++){
					columns.push({header: json.groupList[i].GROUP_NAME, dataIndex: json.groupList[i].GROUP_CODE, align:'center'});
			  }
	}
</script>

</head>

<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>需求预测>需求预测上报
	</div>
	
<form name="fm" method="post" id="fm">
<input type="hidden" id="area" name="area" value="${area }" />
<table class="table_query" >
     <tr>
        <td class="table_query_3Col_label_5Letter" nowrap="nowrap">大区代码：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
		    <input class="middle_txt" id="area_code"   name="area_code"  type="text"/>
		</td>
		<td class="table_query_3Col_label_5Letter" nowrap="nowrap">大区简称：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
			<input class="middle_txt" id="area_name"  name="area_name" type="text"/>
		</td>
	    <td>
	        <input name="closebtn" type="button" class="cssbutton" onclick="__extQuery__(1);" value="查询" />
	        <input name="closebtn" type="button" class="cssbutton" onclick="_hide();" value="关闭" />
	   	</td>
    </tr>
</table>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 

</form>
<p>&nbsp;</p>


</body>
</html>
