<%-- 
创建时间 : 2010.08.27
             创建人:lishuai
             功能描述：结算单审核(财务)，财务室对结算单审核
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
	<title>结算单复核申请审核</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;结算单复核申请审核
</div>
<form method="post" name="fm" id="fm">
<table align="center" class="table_query">
	<tr>
		<td align="right" nowrap="nowrap">经销商代码：</td>
		<td align="left" nowrap="nowrap">
			<input class="long_txt" id="dealerCode"  name="dealerCode" type="text"/>
            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/>
		</td>
		<td align="right" nowrap="nowrap">生产基地：</td>
		<td align="left" nowrap="nowrap">
			<script type="text/javascript">
			genSelBoxContainStr("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'<%=CommonUtils.checkNull(request.getAttribute("yieldly"))%>');
		    </script>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="nowrap">结算单号：</td>
        <td>
        	<input name="balanceNo" value="" type="text" class="middle_txt"/>
        </td>
		<td align="right" nowrap="nowrap">制单日期：</td>
		<td align="left" nowrap="nowrap">
			<input type="text" name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
		</td>
	</tr>
	
	<tr>
		<td align="right" nowrap="nowrap">复核人：</td>
        <td>
        	<input name="reviewBy" value="" type="text" class="middle_txt"/>
        </td>
		<td align="right" nowrap="nowrap">复核日期：</td>
		<td align="left" nowrap="nowrap">
			<input type="text" name="reviewBeginDate" id="t3" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4"  hasbtn="true" callFunction="showcalendar(event, 't3', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="reviewEndDate" id="t4" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4" hasbtn="true" callFunction="showcalendar(event, 't4', false);"/>
		</td>
	</tr>
	
	<tr>
		<td align="right" nowrap="nowrap">状态：</td>
		<td>
			<script type="text/javascript">
				 genSelBoxExp("status",<%=Constant.ACC_STATUS%>,"<%=Constant.ACC_STATUS_03%>",true,"short_sel","","true",'<%=Constant.ACC_STATUS_07%>,<%=Constant.ACC_STATUS_08%>,<%=Constant.ACC_STATUS_09%>');
		    </script>
		</td>
		<td colspan="2">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">
		var myPage;
		var url = "<%=contextPath%>/claim/authorization/BalanceMain/financialMainQuery.json";
		var title = null;
		
		var columns = [
					{header: "序号",dataIndex: 'NUM',align:'center'},
					{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
					{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
					{header: "结算单号",dataIndex: 'BALANCE_NO',align:'center'},		
					{header: "索赔单数量",dataIndex: 'CLAIM_COUNT',align:'center'}, 
					{header: "状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue}, 
					{header: "制单日期",dataIndex: 'CREATE_DATE',align:'center'},
					{header: "复核人",dataIndex: 'REVIEW_APPLICATION_BY',align:'center'},	
					{header: "复核日期",dataIndex: 'REVIEW_APPLICATION_TIME',align:'center'},			
					{header: "操作",dataIndex: 'ID',align:'center', renderer:accAudut}
			      ];
	      
	//修改的超链接
	function accAudut(value,meta,record)
	{
		var val = record.data.STATUS;
		
		if(val == '<%=Constant.ACC_STATUS_03%>')
		{
			return String.format("<a href='#' onclick='modeAudit(\""+ value +"\")'>[审核]</a>");
		}
		else
		{
			return String.format("<a href='#' onclick='queryInfo(\""+ value +"\")'>[查看]</a>");
		}
  		
	}
	
	function queryInfo(val)
	{
		fm.action = "<%=contextPath%>/claim/authorization/BalanceMain/queryFinancialInfoView.do?id="+val;
		fm.submit();
	}
	
	function modeAudit(val)
	{
		fm.action = "<%=contextPath%>/claim/authorization/BalanceMain/queryFinancialInfo.do?id="+val;
		fm.submit();
	}
	
</script>
</body>
</html>