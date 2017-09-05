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

		function clearInput(inputId){
			var inputVar = document.getElementById(inputId);
			inputVar.value = '';
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;结算室收单
</div>
<form method="post" name="fm" id="fm">
<table align="center" class="table_query">
	<tr>
		<td align="right" nowrap="true">经销商代码：</td>
		<td align="left" nowrap="true">
			<input class="long_txt" id="dealerCode"  name="dealerCode" type="text"/>
            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput('dealerCode');" value="清除"/>
		</td>
		<td align="right" nowrap="true">生产基地：</td>
		<td align="left" nowrap="true">
			<script type="text/javascript">
				genSelBoxContainStr("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'<%=CommonUtils.checkNull(request.getAttribute("yieldly"))%>');
		    </script>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">制单日期：</td>
		<td align="left" nowrap="true">
			<input type="text" name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
		</td>
		<td align="right" nowrap="true">结算单号：</td>
        <td>
        	<input name="balanceNo" value="" type="text" class="middle_txt"/>
        </td>
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input type="hidden" name="status" value="<%=Constant.ACC_STATUS_06%>"/><!-- 收单状态 -->
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
		var url = "<%=contextPath%>/claim/application/ClaimManualAuditing/claimAccAuditQuery.json";
		var title = null;
		
		var columns = [
					{header: "序号",align:'center',renderer:getIndex},
					{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
					{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
					{header: "结算单号",dataIndex: 'BALANCE_NO',align:'center'},		
					{header: "索赔单数量",dataIndex: 'CLAIM_COUNT',align:'center'}, 
					<%--{header: "索赔单申请金额(元)",dataIndex: 'AMOUNT_SUM',align:'center',renderer:formatCurrency},--%>
					{header: "索赔单结算金额(元)",dataIndex: 'BALANCE_AMOUNT',align:'center',renderer:formatCurrency}, 
					{header: "制单日期",dataIndex: 'CREATE_DATE',align:'center'},		
					{header: "操作",dataIndex: 'ID',align:'center', renderer:toDetail}
			      ];
	      
		//修改的超链接
		function toDetail(value,meta,record)
		{ 
	  		var res = String.format("<a href='#' onclick='audit(\""+ value +"\")'>[收单]</a>");
	  		var balanceStatus = record.data.STATUS;
	  		return res;
		}
		
		function audit(val)
		{
			fm.action = "<%=contextPath%>/claim/application/ClaimManualAuditing/auditClaimInit.do?id="+val+"&isConfirm=true";
			fm.submit();
		}	
	</script>
</body>
</html>