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
		<td align="right" nowrap="true">收票日期：</td>
		<td align="left" nowrap="true">
			<input type="text" name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
		</td>
		<td align="right" nowrap="true">收票人：</td>
		<td align="left" nowrap="true">
			<input name="user_name" value="" type="text" class="middle_txt"/>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">服务站简称：</td>
        <td>
        	<input name="DEALER_NAME" value="" type="text" class="middle_txt"/>
        </td>
		<td align="right" nowrap="true">服务站代码：</td>
		<td align="left" nowrap="true">
		<input name="DEALER_CODE" value="" type="text" class="middle_txt"/>
		</td>
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="打印" onclick="AppprintXiao()"/>
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<script type="text/javascript">
			var myPage;
			var url = "<%=contextPath%>/claim/application/DealerNewKp/ClaimsSettlementKpQuery.json";
			var title = null;
			
			var columns = [
						{header: "序号",align:'center',renderer:getIndex},
						{header: "收票人",dataIndex: 'USER_NAME',align:'center'},		
						{header: "收票日期",dataIndex: 'COLLECT_TICKETS_DATE',align:'center'},
						{header: "服务站代码",dataIndex: 'DEALER_CODE',align:'center'},
						{header: "服务站简称",dataIndex: 'DEALER_SHORTNAME',align:'center'},
						{header: "发票号码",dataIndex: 'LABOUR_RECEIPT',align:'center'},
						{header: "税额",dataIndex: 'TAX_RATE_MONEY',align:'center'},
						{header: "开票总金额",dataIndex: 'AMOUNT_OF_MONEY',align:'center'}, 
						{header: "税率",dataIndex: 'TAX_DISRATE',align:'center'}, 
						{header: "实际结算金额",dataIndex: 'AMOUNT_SUM',align:'center'}, 
						{header: "索赔月份",dataIndex: 'END_DATE',align:'center'},
						{header:'备注',dataIndex:'REMARK',align:'center'}	
				      ];
				      
	   //格式化时间为YYYY-MM-DD
   function formatDate(value,meta,record) {
	 if (value==""||value==null) {
		return "";
	 }else {
		return value.substr(0,16);
	 }
   }	
   
    function AppprintXiao()
   {
         var startDate = document.getElementById('startDate').value;
         var endDate = document.getElementById('endDate').value;
         var user_name =  document.getElementById('user_name').value;
         var DEALER_NAME = document.getElementById('DEALER_NAME').value;
         var DEALER_CODE = document.getElementById('DEALER_CODE').value;
          window.open('<%=contextPath%>/claim/application/DealerNewKp/ClaimsSettlementKpPrint.do?startDate='+startDate+'&endDate='+endDate+'&user_name='+user_name+'&DEALER_NAME='+DEALER_NAME+'&DEALER_CODE='+DEALER_CODE,"劳务清单打印打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
   }
   		      
	</script>
</body>
</html>