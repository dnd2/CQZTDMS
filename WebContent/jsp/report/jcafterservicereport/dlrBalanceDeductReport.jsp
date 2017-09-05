<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>结算单扣款报表</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body onload="loadcalendar();">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理&gt;轿车售后索赔报表&gt;结算单扣款报表</div>

  <form method="post" name="fm" id="fm">
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
   <!-- 查询条件 begin -->
  <table class="table_query">
		<tr>
			<td>结算单号:</td>
			<td><input class="middle_txt" id="balanceNo" name="balanceNo" value="" type="text"/></td>
			<td>结算起止时间</td>
			<td colspan="6" nowrap>
            	<div align="left">
            		<input name="beginTime" id="t1" value="" type="text" class="short_txt" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            		&nbsp;至&nbsp;
            		<input name="endTime" id="t2" value="" type="text" class="short_txt" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            	</div>
			</td>
			<td>生产基地:</td>
			<td><input class="middle_txt" id="yieldly" name="yieldly" value="" type="text"/></td>
		</tr>
       <tr>
         <td align="center" colspan="12" nowrap>
         	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询"  onclick="__extQuery__(1)"/>&nbsp;
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
	var url = "<%=contextPath%>/report/jcafterservicereport/VehicleGoodRepairReport/queryDlrBalanceDeduct.json?COMMAND=1";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'NUM',renderer:getIndex, align:'center'},
				{header: "结算单号",dataIndex: 'BALANCE_NO' ,align:'center'},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "结算起时间", dataIndex: 'START_DATE', align:'center'},
				{header: "结算止时间", dataIndex: 'END_DATE', align:'center'},
				{header: "生产基地", dataIndex: 'YIELDLY', align:'center',renderer:getItemValue},
				{header: "旧件扣款", dataIndex: 'OLD_DEDUCT', align:'center'},
				{header: "考核扣款", dataIndex: 'CHECK_DEDUCT', align:'center'},
				{header: "行政扣款", dataIndex: 'ADMIN_DEDUCT', align:'center'},
				{header: "保养扣款", dataIndex: 'FREE_DEDUCT', align:'center'},
				{header: "服务活动扣款", dataIndex: 'SERVICE_DEDUCT', align:'center'},
				{header: "财务扣款", dataIndex: 'FINANCIAL_DEDUCT', align:'center'},
				{header: "扣款总计", dataIndex: 'TOTAL_DEDUCT', align:'center'}
		      ];
		      

  
//设置超链接 end
	
</script>
<!--页面列表 end -->

</body>
</html>