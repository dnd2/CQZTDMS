<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>服务活动费用查询报表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit()
	{
   		loadcalendar();  //初始化时间控件
	}

</script>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理&gt;轿车售后索赔报表&gt;服务活动费用查询报表</div>

  <form method="post" name="fm" id="fm">
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
   <!-- 查询条件 begin -->
  <table class="table_query">
 		<tr>
            <td width="7%" align="right" nowrap>所说大区：</td>
            <td align="left">
            	<select class="short_sel" name="areaName">
            		<option value="">-请选择-</option>
	            	<c:if test="${areaList!=null}">
						<c:forEach items="${areaList}" var="list1">
							<option value="${list1.ORG_ID}">${list1.ORG_NAME}</option>
						</c:forEach>
					</c:if>
				</select>
            </td>
            <td width="19%" align="right" nowrap>维修站代码：</td>
            <td colspan="2" align="left" >
            	<input class="middle_txt" id="dealerCode" name="dealerCode" value="" type="text"/>
            	<input class="mark_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCode','','true')"/>     
            </td>
          </tr>
          <tr>
            <td align="right" nowrap="nowrap">服务活动代码：</td>
            <td align="left">
				 <input type="text" name="campaignCode" class="middle_txt"/>
            </td>
            <td align="right" nowrap="nowrap">服务活动名称：</td>
            <td colspan="2" align="left" >
				<input type="text" name="campaignName" class="middle_txt"/>
			</td>
          </tr>
          <tr>
            <td align="right" nowrap>起止时间： </td>
            <td align="left">
            	<div align="left">
            		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            		&nbsp;至&nbsp;
            		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            	</div>
			</td>
			<td>&nbsp;</td>
            <td align="right" >&nbsp;</td>
            <td align="left" nowrap>
            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询"  onclick="__extQuery__(1)"/>&nbsp;
            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="下载"  onclick="exportExcel()" />
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
	var myPage;
//查询路径
	var url = "<%=contextPath%>/report/jcafterservicereport/ActivityCostReport/queryActivityCostReport.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'NUM', align:'center'},
				{header: "服务中心编码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "服务中心名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "大区", dataIndex: 'ORG_NAME', align:'center'},
				{header: "服务活动编码", dataIndex: 'CAMPAIGN_CODE', align:'center'},
				{header: "服务活动名称", dataIndex: 'ACTIVITY_NAME', align:'center'},
				{header: "活动费用(元)", dataIndex: 'CAMPAIGN_FEE', align:'center'}
		      ];
		      
//设置超链接  begin      
	function exportExcel()
	{
		fm.action = "<%=contextPath%>/report/jcafterservicereport/ActivityCostReport/activityCostReportExcel.do";
		fm.submit();
	}

  function showMonthFirstDay()     
  {     
	  var Nowdate = new Date();     
	  var MonthFirstDay = new Date(Nowdate.getYear(),Nowdate.getMonth(),1);     
	  return DateUtil.Format("yyyy-MM-dd",MonthFirstDay);  
  }     
  function showMonthLastDay()     
  {     
	  var Nowdate = new Date();     
	  var MonthNextFirstDay = new Date(Nowdate.getYear(),Nowdate.getMonth()+1,1);     
	  var MonthLastDay = new Date(MonthNextFirstDay-86400000);     
	  return DateUtil.Format("yyyy-MM-dd",MonthLastDay);   
  }

  $('beginTime').value=showMonthFirstDay();
  $('endTime').value=showMonthLastDay();
  
//设置超链接 end
	
</script>
<!--页面列表 end -->

</body>
</html>