<%-- 
创建时间 : 2010.08.27
             创建人:lishuai
             功能描述：结算室审核，可对索赔单进行批量审核，和逐条审核
--%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>结算室审核</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
		function toexcel(){
		    fm.action="<%=contextPath%>/ClearingsummaryAction/DealerCountexport.do";
		    fm.submit();
		}
		
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;结算汇总
</div>
<form method="post" name="fm" id="fm">
    <%-- 经销商级别 --%>
    <input type="hidden" id="dealerLevel" value="<%=CommonUtils.checkNull(request.getAttribute("dealerLevel")) %>"/>
    <%-- 经销商ID--%>
    <input type="hidden" id="dealerId" value="<%=CommonUtils.checkNull(request.getAttribute("dealerId")) %>"/>
<table align="center" class="table_query">
	<tr>
		<td align="right" nowrap="true">结算开始结束日期：</td>
		<td align="left" nowrap="true">
			<input type="text" name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
		</td>
		<td align="right" nowrap="true">大区：</td>
		<td align="left" nowrap="true">
			<input name="ORG_NAME" value="" type="text" class="middle_txt"/>
		</td>
	</tr>
	<tr>
	    <td align="right" nowrap="true">发票上报日期：</td>
		<td align="left" nowrap="true">
			<input type="text" name="Creat_Date_start" id="t5" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t5,t6"  hasbtn="true" callFunction="showcalendar(event, 't5', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="Creat_Date_end" id="t6" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t5,t6" hasbtn="true" callFunction="showcalendar(event, 't6', false);"/>
		</td>
		<td align="right" nowrap="true">服务站代码：</td>
		<td align="left" nowrap="true">
		<input name="DEALER_CODE" value="" type="text" class="middle_txt"/>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">服务站简称：</td>
        <td>
        	<input name="DEALER_NAME" value="" type="text" class="middle_txt"/>
        </td>
		<td align="right" nowrap="true"></td> 
		<td align="left" nowrap="true">
		</td>
		
	</tr>
	<tr>
		<td align="right" nowrap="true"></td> 
		<td align="left" nowrap="true">
		</td>
		<td align="right" nowrap="true"></td>
		<td align="left" nowrap="true">
		</td>
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
		     &nbsp;&nbsp;&nbsp;&nbsp;
		    <input class="normal_btn" type="button" name="to_excel" id="to_excel" value="导出" onclick="toexcel();"/>
		
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<script type="text/javascript">
			var myPage;
			var url = "<%=contextPath%>/claim/application/DealerNewKp/DealerCountKpQuery.json";
			var title = null;
			
			var columns = [
						{header: "序号",align:'center',renderer:getIndex},
						{header: "大区",dataIndex: 'ORG_NAME',align:'center'},		
						{header: "服务站简称",dataIndex: 'DEALER_NAME',align:'center'},
						{header: "服务商名称",dataIndex: 'DEALER_CODE',align:'center'},
						{header: "开始日期",dataIndex: 'K_SUB_DATE',align:'center',renderer:formatDate1},
						{header: "结束时间",dataIndex: 'J_SUB_DATE',align:'center',renderer:formatDate1},
						{header: "旧件回运日期",dataIndex: 'RETURN_DATE',align:'center',renderer:formatDate},
						{header: "旧件签收日期",dataIndex: 'SIGN_DATE',align:'center',renderer:formatDate}, 
						{header: "旧件审核日期",dataIndex: 'IN_WARHOUSE_DATE',align:'center',renderer:formatDate}, 
						{header: "结算日期",dataIndex: 'CREATE_DATE',align:'center',renderer:formatDate}, 
						{header: "发票上报日期",dataIndex: 'CREAT_DATES',align:'center',renderer:formatDate}, 
						{header: "收票日期",dataIndex: 'COLLECT_TICKETS_DATE',align:'center',renderer:formatDate},
						{header:'验票日期',dataIndex:'CHECK_TICKETS_DATE',align:'center',renderer:formatDate},	
						{header: "转账日期",dataIndex: 'TRANSFER_TICKETS_DATE',align:'center',renderer:formatDate},
						{header:'申请总费用',dataIndex:'REPAIR_TOTAL',align:'center'},	
						{header: "结算总费用",dataIndex: 'BALANCE_AMOUNT',align:'center'},
						{header:'索赔单数量',dataIndex:'CLAIMCOUNT',align:'center'}	
				      ];
				      
	   //格式化时间为YYYY-MM-DD
   function formatDate(value,meta,record) {
	 if (value==""||value==null) {
		return "";
	 }else {
		return value.substr(0,16);
	 }
   }		
   function formatDate1(value,meta,record) {
	 if (value==""||value==null) {
		return "";
	 }else {
		return value.substr(0,10);
	 }
   }			      
	</script>
</body>
</html>