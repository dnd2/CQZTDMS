<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
    List yearList=(List)request.getAttribute("yearList");
    List monthList=(List)request.getAttribute("monthList");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<script language="javascript">
	    function doInit(){
    	}
    	function clrTxt(txtId){
	    	document.getElementById(txtId).value="";
	    }
	
	//<!--页面列表 begin -->

	var myPage;
//查询路径

	var url="<%=contextPath%>/sales/planmanage/RequirementForecast/DealerRequireForecastSearch/oemRequireForecastTotalSearch.json";		
		
	var title = null;

	var  calculateConfig = {bindTableList:"myTable",subTotalColumns:"SERIES_CODE|GROUP_NAME",totalColumns:"GROUP_NAME"};
	
	var columns=[
				{header: "车系代码", dataIndex: 'SERIES_CODE', align:'center'},
				{header: "车系名称", dataIndex: 'SERIES_NAME', align:'center'},
				{header: "车型代码", dataIndex: 'MODEL_CODE', align:'center'},
				{header: "车型名称", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "配置代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "配置名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "预测数量", dataIndex: 'REPORT_AMOUNT', align:'center'}
		      ];
    //下载
	function doExport(){
    	var fm = document.getElementById('fm');
    	fm.action= "<%=request.getContextPath()%>/sales/planmanage/RequirementForecast/DealerRequireForecastSearch/dealerRequireForecastExport.do";
    	fm.submit();
	}
    
    var temp_value;
    function showMaterialGroupByAdd(){
    	temp_value ='';
    	//showMaterialGroupByAddOrder('groupCode','groupName','true','','4','')
    	showMaterialGroup('groupCode','groupName','true','4',true);
    }
    
    function addMaterial(){
    	var  value = document.getElementById("groupCode").value;   	
    	if(value!=null||value!=''){
    		if(temp_value!=''){
    			temp_value = temp_value+","+value;
    		}
    		else{
    			temp_value += value;
    		}
    	}
    	document.getElementById("groupCode").value = temp_value;
    }
</script>
<!--页面列表 end -->


<title>需求预测查询</title>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />当前位置>计划管理>需求预测>需求预测查询</div>
<form method="post" name="fm" id="fm">
<div class="form-panel">
	<h2>需求预测查询</h2>
	<div class="form-body">
   <table class="table_query" >
      <tr>
	      <td class="right">选择月份：</td>
	      <td>
	        <select name="year" id="year" class="u-select" style="width: 20px;" onchange="getPlanVer();">
	        	<%
	        		for(int i=0;i<yearList.size();i++){
	        			String year=(String)yearList.get(i);
	        	%>
	        			<option value="<%=year %>"><%=year %></option>
	        	<%
	        		}
	        	%>
	        </select> 年
	        <select name="month" id="month" class="u-select" style="width: 20px;" onchange="getPlanVer();">
	        	<%
	        		for(int i=1;i<13;i++){
	        				int j=Integer.parseInt((String)monthList.get(0))+1;
	        				if(i==j){
	        			
	        			//for(int i=0;i<monthList.size();i++){
	        				//String month=(String)monthList.get(i);
	        	%>
	        			<option selected="selected" value="<%=i%>"><%=i %></option>
	        	<%
	        			
	        			}else{%>
	        				<option  value="<%=i%>"><%=i %></option>
	        			<%	}
	        				}
	        			
	        	%>
	        </select> 月
	      </td>
	 </tr>    
	 
	  <!-- zxf add -->
     <tr class="csstr">
       <td class="right">预测类型：</td>
       <td>
	      <select name="forecasttype" class="u-select" style="width: 100px;">
		       <c:forEach items="${forecastTypeList}" var="forecastTypeList" >
		       		<option value="${forecastTypeList.CODE_ID }">${forecastTypeList.CODE_DESC }</option>
			   </c:forEach>
	      </select>
   	   </td>
    </tr>
    <!-- zxf end -->
    
     <tr>
        <td class="right">选择物料组：</td>
        <td>
	       <input type="text"  name="groupCode" class="middle_txt" size="15" id="groupCode" onclick="showMaterialGroupByAdd()" value="" />
	       <input type="hidden" name="groupName" size="20" id="groupName" value="" />
		   <input class="normal_btn" type="button" value="清空" onclick="clrTxt('groupCode');"/>
        </td>
     </tr>
     <tr >
     	 <td class="right"></td>
         <td >
             <input name="bt1" type="button" class="u-button u-query" onclick="__extQuery__(1);" value="查询" />
             <input name="bt3" id="downbtn" type=button class="u-button u-submit" onclick="doExport();" value="下载" />
         </td>
     </tr>
   </table>
   
   
  </div>
  </div>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
</div>

</body>
</html>
