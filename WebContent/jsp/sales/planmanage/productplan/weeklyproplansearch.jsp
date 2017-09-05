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
<title> 周度滚动计划查询 </title>
<script language="JavaScript" src="<%=contextPath %>/js/ut.js"></script>

</head>

<body>
	<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>生产计划>周度滚动计划查询
	</div>
<form name="fm" method="post" id="fm">
 <table class="table_query" align=center width="95%">
  <tr>
    <td  align="right" >选择查询周度：</td>
    <td  align="left" >
	      <select name="year" id="year">
	          <option value="${curyear }" selected>${curyear }</option>
              <option value="${nexyear }" >${nexyear }</option>
	      </select>年
	  
	    <select name="week" id="week">
	         <%
	      	  for(int i=1;i<=53;i++){
	      	  %>
	      	  <option value="<%=i %>" ><%=i %></option>
	      	  <%
	      	  }
	      	  %>
	    </select>周
    </td>
  </tr>
  <tr>
    <td align="right">选择物料组：</td>
    <td>
       <input type="text"  name="groupCode" size="15" id="groupCode" value="" />
	   <input name="button3" type="button" class="normal_btn" onclick="showMaterialGroup('groupCode','','true','4')" value="..." />
	   <input class="normal_btn" type="button" value="清空" onclick="clrTxt('groupCode');"/>
    </td>
  </tr>
  <tr>
    <td colspan="2" align="center">
      <input name="button" type=button class="normal_btn" onclick="doQuery();" value="查询" />
      <input name="button2" type=button class="normal_btn" onclick="doExport();" value="下载" />
    </td>
 </tr>
  </table>
<!-- 查询条件 end -->   
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</div>
<p>&nbsp;</p>
</body>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
//查询路径
	calculateConfig = {bindTableList:"myTable",totalColumns:"COLOR_NAME"};
	var url = "<%=contextPath %>/sales/planmanage/ProductPlan/WeeklyProPlanSearch/weeklyProPlanSearch.json";
				
	var title = null;
	
    var columns;
    function doInit(){
		getWeekDate();
	}
	
	//构建TH表头
	function getWeekDate(){
		var year = document.getElementById("year").value;
		//var month = document.getElementById("month").value;
		var week = document.getElementById("week").value;
		var url = "<%=request.getContextPath()%>/sales/planmanage/ProductPlan/WeeklyProPlanSearch/getWeekDate.json";
		makeCall(url,showWeekDate,{year:year,week:week});
	}
	function showWeekDate(json){
	    columns = [
				{header: "配置代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "配置名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "颜色", dataIndex: 'COLOR_NAME', align:'center'},
				{header: "周度合计", dataIndex: 'WEEK_AMT', align:'center'}
				];
				var d;
				for(var i=0;i<json.list.length;i++){
				var ss;
				d=json.list[i].setDate;
				d=d.substring(4,6)+'.'+d.substring(6,8);
				switch (i) {
						case 0:
							ss="ONE_AMT";
							break;
						case 1:
							ss="TWO_AMT";
							break;
						case 2:
							ss="THREE_AMT";
							break;
						case 3:
							ss="FOUR_AMT";
							break;
						case 4:
							ss="FIVE_AMT";
							break;
						case 5:
							ss="SIX_AMT";
							break;
						case 6:
							ss="SEVEN_AMT";
							break;
						default:
							break;
						}
				columns.push({header: d, dataIndex: ss, align:'center'});
	           	}
	}
	function doQuery(){
		getWeekDate();
		__extQuery__(1);
	}
	//下载
	function doExport(){
		$('fm').action= "<%=request.getContextPath()%>/sales/planmanage/ProductPlan/WeeklyProPlanSearch/weeklyProPlanExport.json";
		$('fm').submit();
	}
	function clrTxt(txtId){
	    	document.getElementById(txtId).value="";
    }
</script>	
</html>
