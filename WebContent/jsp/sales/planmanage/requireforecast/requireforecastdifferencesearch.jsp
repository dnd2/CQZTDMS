<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<script language="javascript">
	    function doInit(){
	    	 document.getElementById("downBtn").style.display='none';
    	}
	    function clrTxt(txtId){
	    	document.getElementById(txtId).value="";
	    }
	</script>
<title>需求预测差异查询</title>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />当前位置>计划管理>需求预测>需求预测差异查询</div>
<form method="post" name="fm" id="fm">
   <table class="table_query" >
      <tr>
	      <td align="right">选择月份：</td>
	      <td>
	        <select name="year" id="year" onchange="getPlanVer();">
	        
	        <c:forEach items="${yearList}" var="yearItem">
	        	<option value="${yearItem}">${yearItem}</option>
	        </c:forEach>
	        	<%-- <%
	        		for(int i=0;i<yearList.size();i++){
	        			String year=(String)yearList.get(i);
	        	%>
	        			<option value="<%=year %>"><%=year %></option>
	        	<%
	        		}
	        	%> --%>
	        </select> 年
	        <select name="month" id="month" onchange="getPlanVer();">
	        
	        	<c:forEach  begin="1" end="12" varStatus="status">
	        		<option value="${status.index}">${status.index}</option>
	        	</c:forEach>
	        	<%-- <%
	        		for(int i=1;i<13;i++){
	        			int j=Integer.parseInt((String)monthList.get(0))+2;
	        			if(j==i){
	        	%>
	        			<option selected="selected" value="<%=i %>"><%=i %></option>
	        	<%
	        		}else{
	        			%>
	        			<option value="<%=i %>"><%=i %></option>
	        			<%
	        		}
	        		}
	        	%> --%>
	        </select> 月
	      </td>
	 </tr>    
	 <tr class="csstr">
       <td align="right">业务范围：</td>
       <td>
	      <select name="buss_area">
		       <c:forEach items="${areaBusList}" var="areaBusList" >
		       		<option value="${areaBusList.AREA_ID}">${areaBusList.AREA_NAME}</option>
			   </c:forEach>
	      </select>
   	   </td>
    </tr> 
    <tr>
         <td align="right">
         	选择区域：
         </td>
         <td>
            <input type="text" id="orgCode" name="orgCode" value="" size="15" />
			<input name="orgBtn" class="normal_btn" type="button" value="&hellip;" onclick="showOrg('orgCode','' ,'true','${orgId}');"/>
		 	<input class="normal_btn" type="button" value="清空" onclick="clrTxt('orgCode');"/>
		 </td>
     </tr>
     <tr>
        <td align="right">
        	选择物料组：
        </td>
        <td>
	       <input type="text"  name="groupCode" size="15" id="groupCode" value="" readonly="readonly" />
		   <input name="mgBtn" type="button" class="normal_btn" onclick="showMaterialGroup('groupCode','','true','4')" value="&hellip;" />
           <input class="normal_btn" type="button" value="清空" onclick="clrTxt('groupCode');"/>
        </td>
     </tr>
     <tr align="center">
         <td colspan="2">
			 <input name="queryBtn" type="button" class="normal_btn" onclick="rfdsearchSubmit();" value="查询" />
             <input name="downBtn" id="downBtn" type=button class="normal_btn" onclick="doExport();" value="下载" />
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
	var url ;		
		
	var title = null;

	var columns = [
   		{header: "大区", dataIndex: 'ORG_NAME', align:'center'},
		{header: "业务范围", dataIndex: 'AREA_NAME', align:'center'},
		{header: "年", dataIndex: 'FORECAST_YEAR', align:'center'},
		{header: "月", dataIndex: 'FORECAST_MONTH', align:'center'},
		{header: "预测配置", dataIndex: 'GROUP_CODE', align:'center'},
		{header: "经销商预测", dataIndex: 'TT2', align:'center'},
		{header: "大区预测调整", dataIndex: 'TT1', align:'center'},
		{header: "差异", dataIndex: 'COMPARE', align:'center'},
		{header: "大区预测人", dataIndex: 'NAME', align:'center'},
		{header: "大区预测调整时间", dataIndex: 'CREATE_DATE', align:'center'}
      ]; ;
	
	function rfdsearchSubmit(){
	    url="<%=contextPath%>/sales/planmanage/RequirementForecast/RequireForecastDifferenceSearch/requireForecastTotalSearch.json";
		showDbtn();
	  	__extQuery__(1);
	}	
	
    function showDbtn(){
  	  document.getElementById("downBtn").style.display='inline';
  	}
    
    //下载
	function doExport(){
		$('fm').action= "<%=request.getContextPath()%>/sales/planmanage/RequirementForecast/RequireForecastDifferenceSearch/requireForecastDifferenceExport.json";
		$('fm').submit();
	}
    
	
	
</script>
<!--页面列表 end -->
</body>
</html>
