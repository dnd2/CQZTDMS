<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
    List yearList=(List)request.getAttribute("yearList");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<script language="javascript">
	    function doInit(){
   		  getPlanVer(); 
    	}
		function getPlanVer(){
		    var year=document.getElementById("year").value;
		    var month=document.getElementById("month").value;
		    var areaId=document.getElementById("buss_area").value;
			var url = "<%=contextPath%>/sales/planmanage/MonthTarget/DealerMonthPlanSearch/selectMaxPlanVer.json";
			makeCall(url,showPlanVer,{year:year,month:month,buss_area:areaId});
		}
		
		function showPlanVer(json){
			var obj1 = document.getElementById("plan_ver");
			obj1.options.length = 0;
			var maxVer=json.maxVer;
			var j=0;
			for(var i=maxVer;i>0;i--){
				obj1.options[j]=new Option(i, i);
				j++;
			}
	    }
	</script>
<title>月度任务查询</title>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />当前位置>计划管理>月度任务>月度任务查询</div>
<form method="post" name="fm" id="fm">
   <table class="table_query" >
      <!-- modify by zhaolunda 2010-08-19 增加任务类型查询 -->
<!--      <tr>-->
<!--	      <td align="right">任务类型：</td>-->
<!--	      <td>-->
<!--	        <script type="text/javascript">-->
<!--              genSelBoxExp("task_type",<%=Constant.PLAN_TYPE%>,"",true,"min_sel","","true","");-->
<!--            </script>-->
<!--	      </td>-->
<!--	  </tr> -->
      <tr>
	      <td align="right">选择月份：</td>
	      <td>
	        <select name="year" id="year" onchange="getPlanVer();">
	        	<%
	        		for(int i=0;i<yearList.size();i++){
	        			String year=(String)yearList.get(i);
	        	%>
	        			<option value="<%=year %>"><%=year %></option>
	        	<%
	        		}
	        	%>
	        </select> 年
	        <select name="month" id="month" onchange="getPlanVer();">
	        	<%
	        		for(int i=1;i<13;i++){
	        	%>
	        			<option value="<%=i %>"><%=i %></option>
	        	<%
	        		}
	        	%>
	        </select> 月
	      </td>
	  </tr>    
	  <tr>
	    <td align="right">业务范围：</td>
	    <td>
	      <select name="buss_area" id="buss_area" onchange="getPlanVer();">
		       <c:forEach items="${areaBusList}" var="areaBusList" >
		       		<option value="${areaBusList.AREA_ID }">${areaBusList.AREA_NAME }</option>
			   </c:forEach>
	      </select>
	    </td>
      </tr> 
      <tr>
          <td align="right">版本号：</td>
          <td><select name="plan_ver" id="plan_ver">
              
             </select>
          </td>
     </tr>
     <!--
     <tr>
          <td align="right">版本描述：</td>
          <td>
             <input type="text" name="plan_desc" size="20" value="" />
          </td>
     </tr>
     -->
     <tr align="center">
         <td colspan="2">
             <input name="bt1" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查询" />
             <input name="bt3" id="dbtn" type=button class="normal_btn" onclick="doExport();" value="下载" />
         </td>
     </tr>
   </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
</div>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
//查询路径

	var url = "<%=request.getContextPath()%>/sales/planmanage/MonthTarget/DealerMonthPlanSearch/dealerMonthPlanSearch.json";		
		
	var title = null;

	var columns=[
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "车系代码", dataIndex: 'GROUP_CODE', align:'center'},
				//{header: "任务类型", dataIndex: 'PLAN_TYPE_DESC', align:'center'},
				{header: "车系", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "月度任务", dataIndex: 'SALE_AMOUNT', align:'center'}
		      ];
	
    //下载
	function doExport(){
		$('fm').action= "<%=request.getContextPath()%>/sales/planmanage/MonthTarget/DealerMonthPlanSearch/dealerMonthPlanSearchExport.do";
		$('fm').submit();
	}
</script>
<!--页面列表 end -->
</body>
</html>
