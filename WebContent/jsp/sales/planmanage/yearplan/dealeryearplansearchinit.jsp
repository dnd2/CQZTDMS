<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.Map"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
    List yearList=(List)request.getAttribute("yearList");
    List serlist=(List)request.getAttribute("serlist");
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
		    var planType=document.getElementById("planType").value;
		    var buss_area=document.getElementById("buss_area").value;
			var url = "<%=contextPath%>/sales/planmanage/YearTarget/DealerYearTargetSearch/selectMaxPlanVer.json";
			makeCall(url,showPlanVer,{year:year,planType:planType,buss_area:buss_area});
		}
		
		function showPlanVer(json){
			var obj1 = document.getElementById("plan_ver");
			obj1.options.length = 0;
			var maxVer=json.maxVer;
			var j=0;
			for(var i=0; i<maxVer.length;i++){
				obj1.options[j]=new Option(maxVer[i].PLAN_VER, maxVer[i].PLAN_VER);
				j++;
			}
	    }
	</script>
<title>年度目标查询</title>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />当前位置>计划管理>年度目标>年度目标查询</div>
<form method="post" name="fm" id="fm">
   <table class="table_query" >
   <tr>
       <td align="right" width="40%">业务范围：</td>
	      <td>
		       <select name="buss_area" id="buss_area" onchange="getPlanVer();">
			       <c:forEach items="${areaBusList}" var="areaBusList" >
			       		<option value="${areaBusList.AREA_ID }">${areaBusList.AREA_NAME }</option>
				   </c:forEach>
		       </select>
          </td>
      </tr>
      <tr>
	      <td align="right">选择年度：</td>
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
     <tr>
           <td align="right"> 计划类型： </td>
         <td>
           <script type="text/javascript">
				genSelBoxExp("planType",<%=Constant.PLAN_TYPE%>,"",false,"short_sel","onchange='getPlanVer()'","false",'');
			</script>
         </td>
     </tr>
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
    
	var url="<%=contextPath%>/sales/planmanage/YearTarget/DealerYearTargetSearch/dealerYearlyPlanSearch.json";		
		
	var title = null;

	var columns=[
				{header: "车系代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "车系", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "合计", dataIndex: 'AMOUNT', align:'center'},
				{header: "1月", dataIndex: 'M1', align:'center'},
				{header: "2月", dataIndex: 'M2', align:'center'},
				{header: "3月", dataIndex: 'M3', align:'center'},
				{header: "4月", dataIndex: 'M4', align:'center'},
				{header: "5月", dataIndex: 'M5', align:'center'},
				{header: "6月", dataIndex: 'M6', align:'center'},
				{header: "7月", dataIndex: 'M7', align:'center'},
				{header: "8月", dataIndex: 'M8', align:'center'},
				{header: "9月", dataIndex: 'M9', align:'center'},
				{header: "10月", dataIndex: 'M10', align:'center'},
				{header: "11月", dataIndex: 'M11', align:'center'},
				{header: "12月", dataIndex: 'M12', align:'center'}
		      ];
	
  //下载
	function doExport(){
		$('fm').action= "<%=request.getContextPath()%>/sales/planmanage/YearTarget/DealerYearTargetSearch/dealerYearPlanExport.do";
		$('fm').submit();
	}
</script>
<!--页面列表 end -->
</body>
</html>
