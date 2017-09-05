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
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="com.infodms.dms.po.TmBusinessAreaPO;"%>
<%   List<TmBusinessAreaPO> list = (List<TmBusinessAreaPO>)request.getAttribute("list"); %>
<html xmlns="http://www.w3.org/1999/xhtml">
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
         当前位置：售后服务管理&gt;索赔结算管理&gt;结算室审核
</div>
<form method="post" name="fm" id="fm">
<table align="center" class="table_query">
	<tr>
		<td align="right" nowrap="true">经销商代码：
		<input type="hidden" name="falg" id="falg" value="${falg }"/>
		</td>
		<td align="left" nowrap="true">
			<input class="long_txt" id="dealerCode"  name="dealerCode" type="text" value="${dealerCode1 }" />
            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput('dealerCode');" value="清除"/>
		</td>
		<td align="right" nowrap="true">生产基地：</td>
		<td align="left" nowrap="true">
			<select name="yieldly" id="yieldly" >
			<option value="">-请选择-</option>
			<%for(int i=0;i<list.size();i++){%>
				<option value="<%=list.get(i).getAreaId() %>"><%=list.get(i).getAreaName() %></option>
		<%	} %>
			</select>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">结算单号：</td>
        <td>
        	<input name="balanceNo"  type="text" class="middle_txt" value="${balanceNo1}"/>
        </td>
		<td align="right" nowrap="true">单据状态：</td>
		<td align="left" nowrap="true">
			<script type="text/javascript">
				 genSelBoxExp("status",<%=Constant.ACC_STATUS%>,"<%=Constant.ACC_STATUS_01%>",true,"short_sel","","true",'<%=Constant.ACC_STATUS_07%>,<%=Constant.ACC_STATUS_08%>,<%=Constant.ACC_STATUS_09%>');
		    </script>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">制单日期：</td>
		<td align="left" nowrap="true">
			<input type="text" name="startDate" id="t1" value="${startDate1 }" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="endDate" id="t2" value="${endDate1 }" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
		</td>
	</tr>
	<tr>
		<td colspan="4" align="center">
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
					{header: '省份',dataIndex:'REGION_NAME',align:'center'},
					{header: "结算单号",dataIndex: 'BALANCE_NO',align:'center'},		
					{header: "结算单起止时间",dataIndex: '',align:'center',renderer:MyDate},
					{header: "索赔单数量",dataIndex: 'COUNT_APPL',align:'center'}, 
					{header: "索赔单已审数量",dataIndex: 'CLAIM_COUNT_AL',align:'center'}, 
					{header: "特殊费用单数量",dataIndex: 'SP_COUNT',align:'center',renderer:markForm}, 
					{header: "特殊费用单已审数量",dataIndex: 'SP_COUNT_AL',align:'center'}, 
					{header: "索赔单结算金额(元)",dataIndex: 'BALANCE_AMOUNT',align:'center',renderer:formatCurrency}, 
					{header: "运费(元)",dataIndex: 'RETURN_AMOUNT',align:'center'}, 
					{header: "制单日期",dataIndex: 'CREATE_DATE',align:'center'},
					{header: "单据状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue},		
					{header: "操作",dataIndex: 'ID',align:'center', renderer:accAudut}
			      ];



		//设置时间
		function MyDate(value,meta,record)
		{ 
			var START_DATE = record.data.START_DATE;
	  		var END_DATE = record.data.END_DATE;
	  		return String.format(START_DATE+"至"+END_DATE);
		}
		function markForm(value,meta,record){
			var orderId = record.data.ID;
			
				var tarUrl = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/forwordAuditingMain.do?orderId="+orderId;
				return String.format("<a href='#' onclick='openWindowDialog(\""+tarUrl+"\")'>"+value+"</a>");
		}
	    function openWindowDialog(targetUrl){
		    var height = 500;
		    var width = 800;
		    var iTop = (window.screen.availHeight-30-height)/2;  //获得窗口的垂直位置      
		    var iLeft = (window.screen.availWidth-10-width)/2;   //获得窗口的水平位置
		    var params = "width="+width+",height="+height+",top="+iTop+",left="+iLeft+",scrollbars=yes,resizable=yes,status=no";
		    window.open(targetUrl,null,params);
	    }
	      
	//修改的超链接
	function accAudut(value,meta,record)
	{   
  		var res = String.format("<a href='#' onclick='audit(\""+ value +"\")'>[明细]</a>");
  		var balanceStatus = record.data.STATUS;
  		var msg;
  		if(balanceStatus=='<%=Constant.ACC_STATUS_01%>'){
  	  		<%
  	  			if(CommonUtils.getCurCompanyCode().equals(Constant.COMPANY_CODE_CVS)){
  	  		%>
  					msg="<a href='#' onclick='audit(\""+ value +"\")'>[审批]</a>";
  			<%
  	  			}else{
  			%>
  					msg="<a href='#' onclick='audit(\""+ value +"\")'>[审批]</a><a href='#' id='ITALL' onclick='confirmAuditAll(\""+ value +"\")'>[批量审批]</a>";
  			<%
  	  			}
  			%>
  			
  			var CLAIM_COUNT=record.data.COUNT_APPL;// 索赔单数量
  			var CLAIM_COUNT_AL=record.data.CLAIM_COUNT_AL;// 索赔单已审数量
  			var SP_COUNT=record.data.SP_COUNT;// 特殊用工单
  			var SP_COUNT_AL=record.data.SP_COUNT_AL;// 特殊用已审工单
  			if(CLAIM_COUNT==CLAIM_COUNT_AL&&SP_COUNT==SP_COUNT_AL){
  				msg+="<a id='completeDoHerf' href='javascript:complete(\""+ value +"\")'>[完成]</a>";	
  			}
  			res = String.format(msg); 
  		}
  		return res;
	}

	function complete(value){
		if(document.getElementById('completeDoHerf').disabled){
				MyAlert("请不要重复提交");
		}else{
		MyConfirm("是否确定完成?",completeDo,[value]);
		document.getElementById('completeDoHerf').disabled=true;}
	}
	function completeDo(value){
		document.getElementById('completeDoHerf').href="javascript:MyAlert('请勿重复操作')";
		makeNomalFormCall("<%=contextPath%>/claim/application/ClaimManualAuditing/settComplete.json?blanceId="+value,showResultComplete,'fm');
	}
	function showResultComplete(json){
		var msg=json.msg;
		if(msg=="true"){
			__extQuery__(1)
		}else{
			MyAlert("操作失败,请联系管理员!");
		}
	}
	function audit(val)
	{
		fm.action = "<%=contextPath%>/claim/application/ClaimManualAuditing/auditClaimInit.do?id="+val;
		fm.submit();
	}

	function confirmAuditAll(val){
		MyConfirm("是否批量审核",auditAll,[val]);
	}
	
	function auditAll(val)
	{
		$('ITALL').disabled=true;
		makeNomalFormCall("<%=contextPath%>/claim/application/ClaimManualAuditing/auditAllClaim.json?id="+val,showAuditValue,'fm','queryBtn'); 
	}
	
	//批量审批回调函数
	function showAuditValue(json)
	{
		if(json.returnValue == '1')
		{
			MyAlert("批量审批成功！");
			$('ITALL').disabled=false;
		}else if(json.returnValue == '2')
		{
			MyAlert("结算单中没有索赔单！操作失败！")
		}else if(json.returnValue == '100'){
			MyAlert("其他用户正在审核该结算单，请稍候再审核！")
		}else{
			MyAlert("审批失败！请联系系统管理员！");
		}
		__extQuery__(1);
	}	

	    if($('falg').value==1){
	 	   __extQuery__(1);
	     }
	
	
</script>
</body>
</html>