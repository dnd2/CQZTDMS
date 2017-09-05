<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商开票实销查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit()
	{
   		loadcalendar();  //初始化时间控件
	}
	
	function txtClr(v1,v2,v3){
		document.getElementById(v1).value = "";
		document.getElementById(v2).value = "";
		document.getElementById(v3).value = "";
	}
</script>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理&gt;整车销售报表&gt;经销商开票实销报表</div>
  <form method="post" name="fm" id="fm">
   <!-- 查询条件 begin -->
   <input type="hidden" id="command" name="command" value="${command }"/>
  <table class="table_query">
  		<tr>
  			 <td align="right">上报日期：</td>
		      <td> 
					<div align="left">
	            		<input name="startDate" id="t1" value="${startDate }" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);" />
	            		&nbsp;至&nbsp;
	            		<input name="endDate" id="t2" value="${endDate }" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);" />
            		</div>	
				</td>
  			</td>
  			<c:if test="${command == '3' }">
			<td><div align="right">选择区域：</div></td>
			<td>
			    <input type="hidden" class="middle_txt" id="orgId" name="orgId" value="" size="15" />
	            <input type="text" class="middle_txt" id="orgCode" name="orgCode" value="" size="15" />
				<input name="obtn" id="obtn"  class="mini_btn" type="button" value="&hellip;" onclick="showOrg('orgCode','orgId' ,'true','');"/>
				<input class="normal_btn" type="button" value="清空" onclick="txtClr('orgCode','orgId');"/>
  			</td>
  			</c:if>
  			<!-- 
  			<c:if test="${command == '4' || command == '3' }">
  			<td colspan="2" align="left">选择经销商：</td>
  			<td>
  				<input type="hidden" class="middle_txt" name="dealerId" id="dealerId" size="15" value=""/>
    			<input type="hidden" class="middle_txt" name="dealerCode" id="dealerCode" size="15" value=""/>
    			<input type="text" class="middle_txt" name="dealerName" id="dealerName" size="15" value=""/>
    			<input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode', 'dealerId', 'true', '', 'true','','','dealerName');" value="..." />
    			<input type="button" class="normal_btn" onclick="txtClr('dealerId','dealerCode','dealerName');" value="清 空" id="clrBtn" />
			</td>
			</c:if>
			 -->
	  	</tr>
          <tr>
	            <td align="center" colspan="4">
	            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询"  onclick="firstQuery();"/>&nbsp;
	             	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="导出"  onclick="exportExcel()" />  
	            </td>
          </tr>
         
  </table>
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>

<!--页面列表 begin -->
<script type="text/javascript" >
function exportExcel(){
	var command  = document.getElementById("command").value;
	var url ;
	if(command == '1' || command == '2'){
		url = "<%=contextPath%>/report/reportOne/DealerSalesReport/toDealerSalesExcel.do";
	}
	if(command == '3' || command == '4'){
		url = "<%=contextPath%>/report/reportOne/DealerSalesReport/toOemDealerSalesExcel.do";
	}
	var fm = document.getElementById("fm");
		fm.action = url;
		fm.submit();
}

function firstQuery(){
	var fm = document.getElementById("fm");
	fm.target="_blank";
	fm.action = "<%=contextPath%>/report/reportOne/DealerSalesCheReport/getDealerSalesCheckInfo.do";
	fm.submit();
}

</script>
<!--页面列表 end -->

</body>
</html>