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
	<!--
	    function doInit()
		{
		   loadcalendar(); 
		   //__extQuery__(1);
		}
		
		function addId(){
			var val = '<%=request.getAttribute("id") %>';
			document.getElementById("id").value = val;
		}
		function setDefaultTime(){
			var myDate = new Date();
		    var year = myDate.getFullYear();
		    var month = myDate.getMonth()+1;
		    if (month<10){
		        month = "0"+month;
		    }
		    var firstDay = year+"-"+month+"-01";
			//$('t1').value = firstDay;
			$('t1').value = '2010-12-10';
			myDate = new Date(year,month,0);
    		var lastDay = year+"-"+month+"-"+myDate.getDate();
			
			$('t2').value = lastDay;
			
		}

		//审核人员默认显示当前人员名字
		function showCurrentUser(){
			
			var checkUserSel = $('checkUserSel');
			for(var i = 0; i < checkUserSel.options.length; i++){
				
				if(checkUserSel.options[i].value==${currentUser}){
				
					checkUserSel.options[i].selected = 'selected';
					break;
				}
			}
		}
	
		function confirmOrder(){
			var bId = document.getElementById("id").value;
			var tarUrl ="<%=contextPath%>/claim/application/DealerBalance/queryBalanceOrderDetail.do?id="+bId;
			tarUrl = tarUrl + "&isConfirm=true";

			var width=900;
			var height=500;

			var screenW = window.screen.width-30;	
			var screenH = document.viewport.getHeight()-60;

			if(screenW!=null && screenW!='undefined')
				width = screenW;
			if(screenH!=null && screenH!='undefined')
				height = screenH;
			
			OpenHtmlWindow(tarUrl,width,height);
		}
	-->
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;结算单扣款明细查询
</div>
<form method="post" name="fm" id="fm">
<input type="hidden" value="${userType}" name="userType"/>
<input type="hidden" value="${bansorg}" name="bansorg"/>

<table align="center" class="table_query" border='0'>
	<tr>
		<td align="right" nowrap="true">上报月份：</td>
		<td align="left" nowrap="true">
			<input class="short_txt" id="t1" name="report_date" datatype="1,is_date,10"
	           	 maxlength="10" />
        	 <input class="time_ico" value=" " onclick="showcalendar(event, 't1', false);" type="button"/>
		</td>
		<!-- 艾春 9.12 添加 -->
		<td class="table_edit_2Col_label_7Letter">结算基地：</td>
		<td>
			<script type="text/javascript">
			    genSelBoxExp("YIELDLY_TYPE",<%=Constant.PART_IS_CHANGHE%>,"",true,"short_sel","","false",'');
			</script>
		</td>
		<!-- 艾春 9.12 添加 -->
	</tr>
	<tr>
		<td colspan="5"  align="center">
			
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
	
<script type="text/javascript">

		var myPage;
		var url = "<%=contextPath%>/claim/application/DealerBalance/dealerClaimBalanceInit.json?COMMAND=1";
		var title = null;
		var controlCols = "<input type=\"checkbox\" name=\"claimAll\" onclick=\"selectAll(this,'claimId')\"/>全选";
		var columns = [
					{header: "序号",align:'center',renderer:getIndex},
					{header: "维修站",dataIndex: 'DEALER_NAME',align:'center'},
					{header: "月上报时间",dataIndex: 'REPORT_DATE',align:'center'},
					{header: "申请总费用(元)",dataIndex: 'REPAIR_TOTAL',align:'center',renderer:formatCurrency},
					{header: "结算总费用(元)",dataIndex: 'BALANCE_AMOUNT',align:'center',renderer:formatCurrency},
					{header: "索赔单数量",dataIndex: 'TOTAL',align:'center',renderer:accAudut}
			      ];
	
	function checkBoxShow(value,meta,record)
	{
		var recesel = record.data.DEALER_ID +','+record.data.BALANCE_YIELDLY+','+record.data.REPORT_DATE+','+record.data.CLAIM_TYPE;
		return String.format("<input type='checkbox' id='recesel' name='recesel' value='" + recesel + "' />");
    }
    
   loadcalendar(); 
    function claimType(value,meta,record)
	{
		var claimType = '';
		if(record.data.CLAIM_TYPE == '10661002')
		{
			claimType = '免费保养';
		}else if(record.data.CLAIM_TYPE == '10661006')
		{
			claimType = '服务活动';
		}else if(record.data.CLAIM_TYPE == '10661011')
		{
			claimType = 'PDI';
		}
		return claimType;
	}
    function comitAll() 
    {
		var allChecks = document.getElementsByName("recesel");
		var allFlag = false;
		for(var i = 0;i<allChecks.length;i++)
		{
			if(allChecks[i].checked)
			{
				allFlag = true;
			}
		}
			
		if(allFlag){
				MyConfirm("确认全量审核?",changeSubmit);
		}else{
			MyAlert("请选择数据后再点击操作批量审核按钮！");
		}
	}
    
    function changeSubmit()
    {
    	var url="<%=contextPath%>/claim/application/ClaimManualAuditing/commitall.json";
		makeNomalFormCall(url,showResult22,'fm');
    }
	
	function showResult22(json)
	{
		var msg=json.msg;
		if(msg=='01'){
			MyAlert('全量审核成功');
			__extQuery__(1);
		}else{
			MyAlert('操作失败,请联系管理员');
		}
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
	//查询索赔单明细
	function claimdetail(id,balance_yieldly,report_date,claim_type){
		var fm = document.getElementById("fm");
		fm.action = '<%=contextPath%>/claim/application/DealerBalance/balancetoal.do?dealer_id='+id+'&report_date='+report_date+'&balance_yieldly='+balance_yieldly;
		fm.submit();
	}
	
</script>
</form>
</body>
</html>