<%-- 
创建时间 : 2010.08.27
             创建人:lishuai
             功能描述：索赔单审核，对一条结算单内的多条索赔单进行审核
--%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	TtAsWrClaimBalancePO balancePO = (TtAsWrClaimBalancePO)request.getAttribute("balancePO");
	Object authCode = request.getAttribute("authCode");//登陆人对应 授权代码（用于判断是否该人有审核权限）
	String isConfirm = (String)request.getAttribute("isConfirm");//是否进行收单操作  true: 是  false:不是
	String status = "";
	String auth_code = "";
	if(balancePO!=null){
		status = balancePO.getStatus().toString();
	}
	if(authCode!=null)
		auth_code = authCode.toString();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrClaimBalancePO"%>
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
         当前位置：售后服务管理&gt;索赔结算管理&gt;结算室审核索赔单(轿车)
</div>
<form method="post" name="fm" id="fm">
<input type="hidden" value="${userType}" id="userType" name="userType"/>
<input type="hidden" value="${bansorg}" id="bansorg" name="bansorg"/>
<table align="center" class="table_query" border='0'>
	<tr>
		<td align="right" nowrap="true">索赔单状态：</td>
		<td align="left" nowrap="true">
			<script type="text/javascript">
				genSelBoxExp("claimStatus",<%=Constant.CLAIM_APPLY_ORD_TYPE%>,"<%=Constant.CLAIM_APPLY_ORD_TYPE_08%>",true,"short_sel","","false",'<%=Constant.CLAIM_APPLY_ORD_TYPE_01%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_02%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_03%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_04%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_05%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_11%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_13%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_14%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_15%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_16%>');
			</script>
		</td>
		<td align="right" nowrap="true">维修站：</td>
        <td class="table_query_4Col_input" nowrap="nowrap">
			<input class="middle_txt" id="dealerName"  name="dealerName" type="text"/>
		</td>
	</tr>
	<tr>
	   <td align="right" nowrap="true">维修站代码：</td>
        <td class="table_query_4Col_input" nowrap="nowrap">
			<input class="middle_txt" id="dealerCode"  name="dealerCode" type="text"/>
		</td>
		<td align="right" nowrap="true">上报月份：</td>
        <td class="table_query_4Col_input" nowrap="nowrap">
      	  <input class="short_txt" id="report_date" name="report_date" datatype="1,is_date,10"
                   maxlength="10" />
           <input class="time_ico" value=" " onclick="showcalendar(event, 'report_date', false);" type="button"/>
		</td>
	</tr>
	<tr>
		<td colspan="5" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
		</td>
	</tr>
	<tr>
	<td>
	</td>
	</tr>
</table>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	
<script type="text/javascript"><!--
		var myPage;
		var url = "<%=contextPath%>/claim/application/ClaimManualAuditing/acconeAuditClaimOneByOneJC.json?COMMAND=1";
		var title = null;
		var columns = [
					{header: "序号",align:'center',renderer:getIndex},	
					{header: "维修站",dataIndex: 'DEALER_NAME',align:'center'},
					{header: "月上报时间",dataIndex: 'REPORT_DATE',align:'center'},
					{header: "索赔单类型",dataIndex: 'CLAIM_TYPE',align:'center',renderer:claimType},
					{header: "申请总费用(元)",dataIndex: 'REPAIR_TOTAL',align:'center',renderer:formatCurrency},
					{header: "索赔单数量",dataIndex: 'TOTAL',align:'center',renderer:accAudut},
					{header: "状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue}
			      ];
			      
    
   
    
    
    
	
	function claimType(value,meta,record)
	{
		var claimType = '';
		if(record.data.CLAIM_TYPE == '10661002')
		{
			claimType = '免费保养';
		}else if(record.data.CLAIM_TYPE == '10661006')
		{
			claimType = '活动';
		}else if(record.data.CLAIM_TYPE == '10661011')
		{
			claimType = 'PDI';
		}
		return claimType;
	}
	
	function accAudut(value,meta,record)
	{
		if(record.data.CLAIM_TYPE == '10661001')
		{
			var resObj = String.format("<a href='#' onclick=claimdetail("+record.data.DEALER_ID+","+record.data.BALANCE_YIELDLY+",'"+record.data.REPORT_DATE+"',"+record.data.CLAIM_TYPE+")>["+record.data.TOTAL+"]</a>");
  			return resObj;
		}else if(record.data.CLAIM_TYPE == '10661006')
		{
			var resObj = String.format("<a href='#' onclick=claimdetail("+record.data.DEALER_ID+","+record.data.BALANCE_YIELDLY+",'"+record.data.REPORT_DATE+"',"+record.data.CLAIM_TYPE+")>["+record.data.TOTAL+"]</a>");
  			return resObj;
		}else
		{
			var resObj = String.format("<a href='#' onclick=claimdetail("+record.data.DEALER_ID+","+record.data.BALANCE_YIELDLY+",'"+record.data.REPORT_DATE+"',"+record.data.CLAIM_TYPE+")>["+record.data.TOTAL+"]</a>");
  			return resObj;
		}
		
		
  		
	}
	function claimdetail(id,balance_yieldly,report_date,claim_type){
	    var chanage_url =  '<%=contextPath%>/claim/application/ClaimManualAuditing/judeorder.json?dealer_id='+id+"&balance_yieldly="+balance_yieldly+"&claim_type="+claim_type+"&report_date="+report_date;
		makeNomalFormCall(chanage_url,refreshPage,'fm','');
	}
	function refreshPage(json)
	{
		
	   if(json.type == 'no')
	   {
	      MyAlert('此经销商所有单子旧件入库后才能审核');
	   }else if(json.type == 'yes')
	   {
	   		var fm = document.getElementById("fm");
			fm.action = '<%=contextPath%>/claim/application/ClaimManualAuditing/balancetoal.do?dealer_id='+json.dealer_id+"&balance_yieldly="+json.balance_yieldly+"&claim_type="+json.claim_type+"&report_date="+json.report_date;
			fm.submit();
	   }
	   
	}
	
	function doCusChange(obj)
	{
		__extQuery__(1);
	}
	
</script>
</form>
</body>
</html>