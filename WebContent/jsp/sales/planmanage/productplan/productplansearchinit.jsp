<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
		function clrTxt(txtId){
	    	document.getElementById(txtId).value="";
	    }
	</script>
<title>月度生产计划查询</title>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />当前位置>计划管理>生产计划>月度生产计划查询</div>
<form method="post" name="fm" id="fm">
   <table class="table_query">
      <tr>
	      <td ></td>
	      <td align="right">选择查询月度：</td>
	      <td>
	        <select name="year" id="year" onchange="getPlanVer();">
	        			<option value="${curyear }">${curyear }</option>
	        			<option value="${nextyear }">${nextyear }</option>
	        </select>年
	        <select name="month" id="month" onchange="getPlanVer();">
	        	<%
	        		for(int i=1;i<13;i++){
	        			String m=i+"";
	        			if(i<10){
	        				m="0"+i;
	        			}
	        	%>
	        			<option value="<%=i %>"><%=m %></option>
	        	<%
	        		}
	        	%>
	        </select>月
	      </td>
	      <td></td>
	  </tr>    
      <tr>
          <td></td>
          <td align="right">版本号：</td>
          <td><select name="plan_ver" id="plan_ver">
             
             </select>
          </td>
          <td></td>
     </tr>
     <tr id="groupId">
         <td></td>
         <td align="right">请选择业务范围：</td>
         <td colspan="2"> 
	      <select name="buss_area" id="areaId" onchange="getPlanVer();">
		       <c:forEach items="${areaBusList}" var="areaBusList" >
		       		<option value="${areaBusList.AREA_ID }">${areaBusList.AREA_NAME }</option>
			   </c:forEach>
	      </select>
   	   </td>
         <td></td>
     </tr>
     <tr id="groupId">
         <td></td>
         <td align="right">选择物料组：</td>
         <td>
           <input type="text"  name="groupCode" size="15" id="groupCode" value="" />
		   <input name="button3" type="button" class="normal_btn" onclick="showMaterialGroup('groupCode','','true','4')" value="..." />
		   <input class="normal_btn" type="button" value="清空" onclick="clrTxt('groupCode');"/>
         </td>
         <td></td>
     </tr>
     <tr align="center">
         <td></td>
         <td colspan="2">
             <input name="button22" type="button" class="cssbutton" onclick="doQuery();" value="查询" />
             <input name="button" type=button class="cssbutton" onClick="doExport();" value="下载" /></td>
         <td ></td>
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

	var url ="<%=contextPath%>/sales/planmanage/ProductPlan/ProductPlanSearch/productPlanSearch.json";		
		
	var title = null;

	var columns;
	
    function doInit(){
    	getPlanVer();
		getWeekList();
	}
	
	//构建TH表头
	function getWeekList(){
		var year = document.getElementById("year").value;
		var month = document.getElementById("month").value;
		var url = "<%=request.getContextPath()%>/sales/planmanage/ProductPlan/ProductPlanSearch/productPlanThWeekSearch.json";
		makeCall(url,showWeekList,{year:year,month:month});
	}
	function showWeekList(json){
		columns = [
				{header: "配置代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "配置名称", dataIndex: 'GROUP_NAME', align:'center'}
		      ];
		for(var i=0;i<json.list.length;i++){
			columns.push({header: json.list[i].WEEK+"周", dataIndex: 'W'+i, align:'center'});
		}
		columns.push({header: "合计", dataIndex: 'AMOUNT', align:'center'});
	}
	//查询提交
	function doQuery(){
		getWeekList();
		__extQuery__(1);
	}
	//下载
	function doExport(){
		$('fm').action= "<%=request.getContextPath()%>/sales/planmanage/ProductPlan/ProductPlanSearch/productPlanSearchExport.json";
		$('fm').submit();
	}
	function getPlanVer(){
		    var year=document.getElementById("year").value;
		    var month=document.getElementById("month").value;
		    var areaId=document.getElementById("areaId").value;
			var url = "<%=contextPath%>/sales/planmanage/ProductPlan/ProductPlanSearch/selectMaxPlanVer.json";
			makeCall(url,showPlanVer,{year:year,month:month,areaId:areaId});
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
<!--页面列表 end -->
</body>
</html>
