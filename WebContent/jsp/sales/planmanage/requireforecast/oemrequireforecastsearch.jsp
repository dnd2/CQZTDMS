<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
    List yearList=(List)request.getAttribute("yearList");
    List serlist=(List)request.getAttribute("serlist");
    List monthList=(List)request.getAttribute("monthList");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<script language="javascript">
	    function doInit(){
	    	//setDate() ;
	    	 document.getElementById("downbtn").style.display='none';
    	}
	    function checkOrg(){
	    	document.getElementById("obtn").disabled=false;
	    	document.getElementById("dbtn").disabled=true;
	    }
	    function checkDealer(){
	    	document.getElementById("obtn").disabled=false;
	    	document.getElementById("dbtn").disabled=false;
	    }
	    function clrTxt(txtId){
	    	document.getElementById(txtId).value="";
	    }
	    
	    function setDate() {
	    	var oYear = document.getElementById("year") ;
	    	var oMonth = document.getElementById("month") ;
	    	var sDate = document.getElementById("sys_date__").value ;
	    	
	    	var iYear = parseInt(sDate.split(",")[0]) ;
	    	var iMonth = parseInt(sDate.split(",")[1]) ;
	    	
	    	var iCurrentMonth = iMonth + 1 ;
	    	
	    	if(iCurrentMonth > 12) {
	    		iYear += 1 ;
	    		iMonth = iCurrentMonth - 12 ;
	    	}
	    	
	    	oYear.options.value = iYear ;
	    	oMonth.options.value = iMonth ;
	    }	
	//<!--页面列表 begin -->
	var myPage;
//查询路径

	var url ;		
		
	var title = null;

	var columns;
	
	// var calculateConfig;

	function getAreaGroup(){
        var areaId=0;//document.getElementById("buss_area").value;
		var url = "<%=contextPath%>/sales/planmanage/RequirementForecast/OemRequireForecastSearch/selectAreaGroup.json";
		makeCall(url,showColumn1,{buss_area:areaId});
	}
	function search2Submit(){
		document.getElementById("dthid").value=2;
		// calculateConfig=null;
		var dutyType=document.getElementById("dutyType").value
		//alert(dutyType);
		url="<%=contextPath%>/sales/planmanage/RequirementForecast/OemRequireForecastSearch/oemRequireForecastDetailSearch.json";
		var chngType=getRdValue();
		if(chngType==1){
		    columns = columns3;
		}else{
			if(dutyType==10431003){
				columns=columns4;
			}else if(dutyType==10431001){
				columns=columns5;
			}
			
		}
		showDbtn();
		__extQuery__(1);
	}

	function search1Submit(){
		document.getElementById("dthid").value=1;
	    // calculateConfig = {bindTableList:"myTable",subTotalColumns:"SERIES_CODE|GROUP_NAME",totalColumns:"GROUP_NAME"};
	    url="<%=contextPath%>/sales/planmanage/RequirementForecast/OemRequireForecastSearch/oemRequireForecastTotalSearch.json";
		   var chngType = getRdValue() ;
		   if (chngType == 1) {
				columns = [
		                   {header : '经销商代码', dataIndex : 'DEALER_CODE', align: 'center'},
		                   {header : '经销商名称', dataIndex : 'DEALER_SHORTNAME', align: 'center'}
		                   ] ;
		   } else {
				columns = [
			               {header : '组织代码', dataIndex : 'ORG_CODE', align : 'center'},
			               {header : '组织名称', dataIndex : 'ORG_NAME', align : 'center'}
			           	] ;  
		   }
		   getAreaGroup() ;
		   showDbtn();
		  __extQuery__(1);
	}
	function showColumn1(json){
	      for(var i=0;i<json.serList.length;i++){
				columns.push({header: json.serList[i].GROUP_NAME, dataIndex: 'S'+i, align:'center'});
		  }
			    columns.push({header: "合计", dataIndex: 'FA', align:'center'});
			    MyAlert( json.serList[i].GROUP_NAME);
	}
	var column_=[
				{header: "车系代码", dataIndex: 'SERIES_CODE', align:'center'},
				{header: "车系名称", dataIndex: 'SERIES_NAME', align:'center'},
				{header: "车型代码", dataIndex: 'MODEL_CODE', align:'center'},
				{header: "车型名称", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "配置代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "配置名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "下级汇总数量", dataIndex: 'REPORT_AMOUNT', align:'center'},
				{header: "预测数量", dataIndex: 'FORECAST_AMOUNT', align:'center'}
		      ]; 
   	              
    var columns3=[
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "月份", dataIndex: 'FORECAST_MONTH', align:'center'},
				{header: "产品组代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "产品组名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "数量", dataIndex: 'FORECAST_AMOUNT', align:'center'}
		      ];
   var columns4 = [
				{header: "组织代码", dataIndex: 'ORG_CODE', align:'center'},
				{header: "组织名称", dataIndex: 'ORG_NAME', align:'center'},
				{header: "月份", dataIndex: 'FORECAST_MONTH', align:'center'},
				{header: "产品组代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "产品组名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "大区数量", dataIndex: 'LARGE_FORECAST_AMOUNT', align:'center'},
				{header: "经销商数量", dataIndex: 'DEALER_FORECAST_AMOUNT', align:'center'}
		      ];
   var columns5 = [
		{header: "组织代码", dataIndex: 'ORG_CODE', align:'center'},
		{header: "组织名称", dataIndex: 'ORG_NAME', align:'center'},
		{header: "月份", dataIndex: 'FORECAST_MONTH', align:'center'},
		{header: "产品组代码", dataIndex: 'GROUP_CODE', align:'center'},
		{header: "产品组名称", dataIndex: 'GROUP_NAME', align:'center'},
		{header: "车厂数量", dataIndex: 'FORECAST_AMOUNT', align:'center'},
		{header: "大区数量", dataIndex: 'LARGE_FORECAST_AMOUNT', align:'center'},
		{header: "经销商数量", dataIndex: 'DEALER_FORECAST_AMOUNT', align:'center'}
     ];
   function getRdValue(){
   		var rd1=document.getElementById("rd1");
   		var rd2=document.getElementById("rd2");
   		if(rd1.checked){
   			return 1;
   		}else{
   			return 2;
   		}
   }
    function showDbtn(){
  	  document.getElementById("downbtn").style.display='inline';
  }
    //下载
	function doExport(){
    	var fm = document.getElementById("fm");
		fm.action= "<%=request.getContextPath()%>/sales/planmanage/RequirementForecast/OemRequireForecastSearch/oemRequireForecastExport.json";
		fm.submit();
	}
    
    
    function showInfo(code,name){
    	document.getElementById("orgCode").value =code ;
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
    
    /*
     * 选择大区
     * */
    function showOrgLargerEgion( )
    {
    	
    	OpenHtmlWindow(g_webAppName+'/dialog/showOrgLargerEgion.jsp',800,500);
    }
    
</script>
<!--页面列表 end -->


<title>需求预测查询</title>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />当前位置>计划管理>需求预测>需求预测查询</div>
<form method="post" name="fm" id="fm">
<input type="hidden" id="dutyType" name="dutyType" value="${dutyType }"/>
<div class="form-panel">
	<h2>需求预测查询</h2>
	<div class="form-body">	
   <table class="table_query" >
      <tr>
	      <td class="right">选择月份：</td>
	      <td>
	        <select name="year" id="year" class="u-select" style ="width:50px" onchange="getPlanVer();">
	        	<%
	        		for(int i=0;i<yearList.size();i++){
	        			String year=(String)yearList.get(i);
	        	%>
	        			<option value="<%=year %>"><%=year %></option>
	        	<%
	        		}
	        	%>
	        </select> 
	        <select name="month" id="month" class="u-select" style ="width:50px" onchange="getPlanVer();">
	        	<%
	        		for(int i=1;i<13;i++){
	        				int j=Integer.parseInt((String)monthList.get(0))+1;
	        				if(i==j){
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
	  <tr class="csstr" style="display: none;">
       <td class="right">业务范围：</td>
       <td>
	      <select name="buss_area" class="u-select" style ="width:100px">
		       <c:forEach items="${areaBusList}" var="areaBusList" >
		       		<option value="${areaBusList.AREA_ID }">${areaBusList.AREA_NAME }</option>
			   </c:forEach>
	      </select>
   	   </td>
    </tr>
    <!-- zxf add -->
     <tr class="csstr">
       <td class="right">预测类型：</td>
       <td>
	      <select name="forecasttype" class="u-select" style ="width:100px" >
		       <c:forEach items="${forecastTypeList}" var="forecastTypeList" >
		       		<option value="${forecastTypeList.CODE_ID }">${forecastTypeList.CODE_DESC }</option>
			   </c:forEach>
	      </select>
   	   </td>
    </tr>
    <!-- zxf end -->
      
     
  
             <tr id="org">
                 <td class="right">
                    <input type="radio" id="rd2" name=chngType value="2" checked="checked" onclick="checkOrg();"/>按大区查询：
                 </td>
                 <td>
                    <input type="text" id="orgCode" class="middle_txt" name="orgCode" value="" size="15"  onclick="showOrgLargerEgion();" />
                    <input class="normal_btn" type="button" value="清空" onclick="clrTxt('orgCode');"/>
                 </td>
             </tr>
    
     
     <tr id="dealer">
         <td class="right">
	          <input type="radio" id="rd1" name="chngType" value="1" checked="checked"   onclick="checkDealer();"/>按经销商查询：
         </td>
         <td>
           <input type="text"  name="dealerCode" class="middle_txt" size="15"  id="dealerCode" onclick="showOrgDealer('dealerCode', 'dealerId', 'true', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', 'dealerName');" readonly="readonly"/>
            <input type="hidden"  name="dealerId" size="15"  id="dealerId" readonly="readonly"/>
            <input type="hidden"  name="dealerName" size="15"  id="dealerName" readonly="readonly"/>
        	<input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCode');"/>
         </td>
     </tr>
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
             <input name="bt1" type="button" class="u-button u-query" onclick="search1Submit();" value="汇总查询" />
             <input name="bt2" type=button class="u-button u-query" onclick="search2Submit();" value="明细查询" />
             <input name="donwtype" id="dthid" type="hidden"  value="" />
             <input name="bt3" id="downbtn" type=button class="u-button u-submit" onclick="doExport();" value="下载" />
         </td>
         <td class="right">
             页面大小：<input name="pageSize" id="pageSize" type="text" class="mini_txt" value="10" datatype="0,isDigit,3"/>
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
