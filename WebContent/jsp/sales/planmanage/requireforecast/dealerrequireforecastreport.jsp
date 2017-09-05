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
       function executeQueryOpe(){
       		var info=document.getElementById("areaId").value;
       		document.getElementById("selectstr").value=info;
       		__extQuery__(1);
       }

   	function doInit(){
   		executeQueryOpe();  //查询投诉信息导入的临时表
	}
       
       function isReport(){
      	 MyConfirm("是否确认上报信息?",reportSubmit);
       }
       
       function reportSubmit(){
       	   var url ="<%=contextPath%>/sales/planmanage/RequirementForecast/DealerRequireForecastReport/dealerForecastReport.do";
       	   var fm = document.getElementById("fm");       	   
           fm.action=url;
      	   fm.submit();
       }

 //<!--页面列表 begin -->

   // document.form1.style.display = "none";
	var HIDDEN_ARRAY_IDS=['form1'];
	var myPage;
//查询路径

	var url ="<%=contextPath%>/sales/planmanage/RequirementForecast/DealerRequireForecastReport/dealerRequireForecastReportModelSearch.json";		
		
	var title = null;

	var  columns = [
				{header: "车系代码", dataIndex: 'SERIES_CODE', align:'center'},
				{header: "车系", dataIndex: 'SERIES_NAME', align:'center'},
				{header: "车型代码", dataIndex: 'MODEL_CODE', align:'center'},
				{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
				<c:forEach items="${mapList}" var="mapList" varStatus="steps">
				{header: '${mapList.YEAR}.${mapList.MONTH}', dataIndex: 'S${steps.index}', align:'center'},
				</c:forEach>			
				{id:'action',header: "操作",sortable: false,dataIndex: 'GROUP_ID',renderer:myLink ,align:'center'}
		        ];
	//修改和删除的超链接
	function myLink(value,meta,record){
		if(record.data.SERIES_CODE)
  			return String.format("<a href='#' onclick='showOpe(\""+ value +"\")'>[预测]</a>");
		else 
			return "" ;
	}
	function showOpe(parastr){
	    if(null == parastr ||'' == parastr ){
	       MyAlert('非法操作！');
	       return false;
	    }else {
	      // document.getElementById("parastr").value=parastr;
		  var url ="<%=contextPath%>/sales/planmanage/RequirementForecast/DealerRequireForecastReport/dealerForecastSearch.do?parastr="+parastr;
		  frm.action=url;
		  frm.submit();
	    }   
	}
</script>

</head>

<body>
	<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>需求预测>需求预测上报
	</div>
<form name="fm" method="post" id="fm">
<input type="hidden" name="selectstr" id="selectstr" value="" />
		<div class="form-panel">
			<h2>需求预测上报 </h2>
			<div class="form-body">	
		<table class="table_query" id="subtab" >
		  <tr class="csstr">
		    <td style="display: none;"> 请选择业务范围：
		      <select name="areaId" id="areaId" onchange="executeQueryOpe();">
			       <c:forEach items="${areaBusList}" var="areaBusList" >
			       		<option value="${areaBusList.AREA_ID },${areaBusList.DEALER_ID }">${areaBusList.AREA_NAME }</option>
				   </c:forEach>
		      </select>
		    </td>
		    <div>
		    <td align="right">
				  		<input class="u-button u-submit" type="button" value="预测上报" onclick="isReport();" />
			     	</td>     	
		    <td>
		        <input type="hidden" name="modelId" value=""/>
		        <input type="hidden" name="areaAndDealer" value="${areaId }"/>
		    	<input type="button" id="queryBtn" class="u-button u-query"  value="查询" onclick="executeQueryOpe();" />
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


<form name="form1" id="form1">
	<table>
		<tr>
			<td align="center">
 			  <c:out value="${conPo.subStatus }"></c:out>			  
	     	</td>
		</tr>
	</table>
</form>
</div>
<p>&nbsp;</p>


<form name="frm" method="post">
   <input type="hidden" name="parastr" />
</form>
</body>
</html>
