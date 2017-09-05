<%-- 
创建时间 : 2010.08.27
             创建人:lishuai
             功能描述：索赔单审核，对一条结算单内的多条索赔单进行审核
--%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<%@page import="com.infodms.dms.po.TtAsWrClaimBalancePO"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>结算室审核</title>
	<script type="text/javascript">
	<!--
	    function doInit()
		{
		   addId();
		   loadcalendar(); 
		   __extQuery__(1);
		}
		
		function addId(){
			var val = '<%=request.getAttribute("id") %>';
			document.getElementById("id").value = val;
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
         当前位置：售后服务管理&gt;索赔结算管理&gt;结算室审核
</div>
<form method="post" name="fm" id="fm">
<input type="hidden" name="id"/>
<table align="center" class="table_query">
	<tr>
		<td align="right" nowrap="true">索赔单号：</td>
		<td align="left" nowrap="true">
			<input class="middle_txt" id="claimNo"  name="claimNo" type="text"/>
		</td>
		<td align="right" nowrap="true">索赔类型：</td>
		<td align="left" nowrap="true">
			<script type="text/javascript">
				 genSelBoxExp("CLAIM_TYPE",<%=Constant.CLA_TYPE%>,"",true,"short_sel","","true",'');
		    </script>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">经销商代码：</td>
		<td align="left" nowrap="true">
			<input class="middle_txt" id="dealerCode"  name="dealerCode" type="text"/>
            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/>
		</td>
		<td align="right" nowrap="true">VIN：</td>
		<td align="left" nowrap="true">
			<input class="middle_txt" id="vin" name="vin" type="text"/>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">物料组：</td>
        <td class="table_query_4Col_input" nowrap="nowrap">
				<input class="middle_txt" type="text" name="modelCode" size="20" id="modelCode" value="" />
				<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('modelCode','','false','3','true')" value="..." />
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('modelCode');"/>
			</td>
		<td align="right" nowrap="true">制单日期：</td>
		<td align="left" nowrap="true">
			<input type="text" name="startDate" id="t1" value=" " type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="endDate" id="t2" value=" " type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
		</td>
	</tr>
	<tr>
		<td align="right"><input type="button" value="批量审核" class="normal_btn" onclick="batchAuditingConfirm();"/></td>
		<td colspan="3" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
			<%if((Constant.ACC_STATUS_01).equals(status)){//当该结算单状态为 "结算单审核中"时显示该按钮 %>
			<input class="normal_btn" type="button" name="button" id="queryBtn1" value="逐条审批" onclick="cliamAllAudit()"/>&nbsp;&nbsp;
			<%}
			if ((Constant.ACC_STATUS_06).equals(status) && "true".equals(isConfirm)){//当结算单状态为"待收单"且isConfirm为true时显示 %>
			<input class="normal_btn" type="button" name="confirmBtn" id="queryBtn2" value="收单" onclick="confirmOrder()"/>&nbsp;&nbsp;	
			<%} %>
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="返回" onclick="fowardPage();"/>
		</td>
	</tr>
	<tr>
	<td>
	<input type="hidden" name="endDate1" value="${endDate}"/>
	<input type="hidden" name="yieldly1" value="${yieldly}"/>
	<input type="hidden" name="balanceNo1" value="${balanceNo}"/>
	<input type="hidden" name="dealerCode1" value="${dealerCode}"/>
	<input type="hidden" name="endDate1" value="${endDate}"/>
	</td>
	</tr>
</table>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	
<script type="text/javascript"><!--
		var myPage;
		var val = '<c:out value="${dealerInfo.REPLACE_PART_ID}"/>';
		var url = "<%=contextPath%>/claim/application/ClaimManualAuditing/claimOneByOneAuditQuery.json";
		var title = null;

		var controlCols = "<input type=\"checkbox\" name=\"claimAll\" onclick=\"selectAll(this,'claimId')\"/>全选";
		
		var columns = [
					{header: "序号",align:'center',renderer:getIndex},
					<%if((Constant.ACC_STATUS_01).equals(status)){//当该结算单状态为 "结算单审核中"时显示该按钮  %>
					{header: controlCols,align:'center',renderer:createCheckbox},
					<%} %>
					{header: "索赔单号",dataIndex: 'CLAIM_NO',align:'center'},
					{header: "索赔单类型",dataIndex: 'CLAIM_TYPE',align:'center',renderer:getItemValue},
					{header: "生产基地",dataIndex: 'YIELDLY',align:'center',renderer:getItemValue},	
					{header: "维修站",dataIndex: 'DEALER_NAME',align:'center'},
					{header: "车型",dataIndex: 'MODEL_NAME',align:'center'}, 
					{header: "VIN",dataIndex: 'VIN',align:'center'},
					{header: "申请费用",dataIndex: 'REPAIR_TOTAL',align:'center',renderer:formatCurrency},
					{header: "结算费用",dataIndex: 'BALANCE_AMOUNT',align:'center',renderer:formatCurrency},
					{header: "申请日期",dataIndex: 'REPORT_DATE',align:'center'},		
					{header: "审核通过日期",dataIndex: 'AUDITING_DATE',align:'center'},
					{header: "操作",dataIndex: 'CLAIM_ID',renderer:accAudut}
			      ];
	//修改的超链接
	
	function fowardPage(){
		fm.action = "<%=contextPath%>/claim/application/ClaimManualAuditing/claimMAccAuditInit.do?falg=1";
		fm.submit();

	}
	function accAudut(value,meta,record)
	{
  		var resObj = String.format("<a href='#' onclick='claimdetail(\""+ value +"\",\""+record.data.BALANCE_ID+"\")'>[明细]</a>");
  		var claimStatus = record.data.STATUS;
  		var authcodeval = record.data.AUTH_CODE;
  		if((<%=Constant.ACC_STATUS_01%>)==(<%=status%>)){
  	  		if((<%=Constant.CLAIM_APPLY_ORD_TYPE_08.toString()%>)==claimStatus && <%=auth_code%>==authcodeval)
  	  			resObj = String.format("<a href='#' onclick='audit(\""+ value +"\",\""+record.data.BALANCE_ID+"\")'>[审批]</a>");
	  	  	if(claimStatus==<%=Constant.CLAIM_APPLY_ORD_TYPE_07%>){
	  			resObj = String.format("<a href='#' onclick='auditReDo(\""+ value +"\",\""+record.data.BALANCE_ID+"\")'>[重新审批]</a>");
	  		}	
  		}

  		return resObj;
	}
	
	//审批一条
	function audit(val,balance_id) 
	{
		var tarUrl = '<%=contextPath%>/claim/application/ClaimManualAuditing/balanceAuditingPage.do?isAudit=true&ID='+ val
		+'&BALANCE_ID=' + balance_id;
		var width=900;
		var height=500;

		var screenW = window.screen.width-30;	
		var screenH = document.viewport.getHeight();

		if(screenW!=null && screenW!='undefined')
			width = screenW;
		if(screenH!=null && screenH!='undefined')
			height = screenH;
		
		OpenHtmlWindow(tarUrl,width,height);
	}

	//审批一条
	function auditReDo(val,balance_id) 
	{
		var tarUrl = '<%=contextPath%>/claim/application/ClaimManualAuditing/balanceAuditingPage.do?isAudit=true&ID='+ val
		+'&BALANCE_ID=' + balance_id+"&IS_REDO=YES";
		var width=900;
		var height=500;

		var screenW = window.screen.width-30;	
		var screenH = document.viewport.getHeight();

		if(screenW!=null && screenW!='undefined')
			width = screenW;
		if(screenH!=null && screenH!='undefined')
			height = screenH;
		
		OpenHtmlWindow(tarUrl,width,height);
	}
	
	//查询索赔单明细
	function claimdetail(id,balance_id){
		var tarUrl = "<%=contextPath%>/claim/application/ClaimBillStatusTrack/claimBillDetailForward.do?ID="+id;
		var width=900;
		var height=500;
		var screenW = window.screen.width-30;	
		var screenH = document.viewport.getHeight();
		if(screenW!=null && screenW!='undefined')
			width = screenW;
		if(screenH!=null && screenH!='undefined')
			height = screenH;
		
		OpenHtmlWindow(tarUrl,width,height);
	}
	
	//依次审批全部索赔单信息
	function cliamAllAudit()
    {
	    var frm = document.getElementById("fm");
	    frm.action = "<%=contextPath%>/claim/application/ClaimManualAuditing/auditingOneByOne.do";
	    frm.submit();
    }
	
	//清空物料组信息
	function clrTxt(txtId)
	{
    	document.getElementById(txtId).value = "";
    }

	//生成批量审核使用的选择框
	function createCheckbox(value,meta,record){
	    var resultStr = "--";
		if((<%=Constant.ACC_STATUS_01%>)==(<%=status%>)){
  	  		var claimStatus = record.data.STATUS;
  	  		var authcodeval = record.data.AUTH_CODE;
  	  		if((<%=Constant.CLAIM_APPLY_ORD_TYPE_08.toString()%>)==claimStatus && <%=auth_code%>==authcodeval)
				resultStr = String.format("<input type=\"checkbox\" name=\"claimId\" value=\""+record.data.CLAIM_ID+"\"/>");
		}
		return resultStr;
	}

	//批量审批
	function batchAuditingConfirm(){
		var selectArray = document.getElementsByName('claimId');
		if(isSelectCheckBox(selectArray)){
			MyConfirm("是否批量审核!",batchAuditing,[]);
		}else{
			MyAlert("请选择要审批的索赔单！");
		}
	}
	
	//检测是否选择了索赔单
	function isSelectCheckBox(cbArray){
		if(cbArray!=null && cbArray.length>0){
			for(var i=0;i<cbArray.length;i++){
				if(cbArray[i].checked)
					return true;
			}
		}else{
			return false;
		}
		return false;
	}
	
	function batchAuditing(val)
	{
		makeNomalFormCall("<%=contextPath%>/claim/application/ClaimManualAuditing/batchAuditingBlance.json?",showAuditValue,'fm','queryBtn'); 
	}

    //清空经销商框
	function clearInput(){
		var target = document.getElementById('dealerCode');
		target.value = '';
	}
	
	function showAuditValue(json){
		if(json.SUCCESS=='FAILURE'){
			MyAlert("批量审核失败！");
		}else if(json.SUCCESS=='LOCK'){
			MyAlert("其他用户正在审核该结算单，请稍候再审核！");
		}
		__extQuery__(1);
	}
--></script>
</form>
</body>
</html>