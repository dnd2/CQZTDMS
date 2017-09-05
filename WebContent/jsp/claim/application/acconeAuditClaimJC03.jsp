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
		   addId();
		   setDefaultTime();
		   showCurrentUser();
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
         当前位置：售后服务管理&gt;索赔结算管理&gt;结算室审核索赔单(轿车)
</div>
<form method="post" name="fm" id="fm">
<input type="hidden" name="id"/>
<input type="hidden" value="${bansorg}" name="bansorg"/>
<table align="center" class="table_query" border='0'>
	<tr>
		<td align="right" nowrap="true">索赔单号：</td>
		<td align="left" nowrap="true">
			<input class="middle_txt" id="claimNo"  name="claimNo" type="text"/>
		</td>
		<td align="right" nowrap="true">索赔类型：</td>
		<td align="left" nowrap="true">
		<c:if test="${userType == 1}">
			<script type="text/javascript">
				 genSelBoxExp("CLAIM_TYPE",<%=Constant.CLA_TYPE%>,"",true,"short_sel","","false",'');
		    </script>
		 </c:if>   
		 <c:if test="${userType == 0}">
		    <script type="text/javascript">
		    genSelBoxContainStr("CLAIM_TYPE",<%=Constant.CLA_TYPE%>,"",true,"short_sel","","false",'10661006');
		    </script>
		  </c:if>  
		</td>
		<td align="right" nowrap="true">索赔单状态：</td>
		<td align="left" nowrap="true">
		<script type="text/javascript">
			genSelBoxExp("claimStatus",<%=Constant.CLAIM_APPLY_ORD_TYPE%>,"<%=Constant.CLAIM_APPLY_ORD_TYPE_11%>",true,"short_sel","","false",'<%=Constant.CLAIM_APPLY_ORD_TYPE_01%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_02%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_03%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_04%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_05%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_08%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_09%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_07%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_10%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_06%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_12%>');
		</script>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">VIN：</td>
		<td align="left" nowrap="true">
			<input class="middle_txt" id="vin" name="vin" type="text"/>
		</td>
		<td align="right" nowrap="true">经销商名称：</td>
        <td class="table_query_4Col_input" nowrap="nowrap">
				<input class="middle_txt" id="dealerName"  name="dealerName" type="text"/>
			</td>
		<td align="right" nowrap="true">产地：</td>
		<td align="left" nowrap="true">
			<select style="width: 152px;" name="yieldly" id="yieldly">
				 <option value="" >
    				-请选择-
    			  </option>
	              <c:forEach var="Area" items="${Area}" >
 				  <option value="${Area.areaId}" >
    				<c:out value="${Area.areaName}"/>
    			  </option>
    			 </c:forEach>
             </select>
		</td>
	</tr>
	<tr>
		<td colspan="5" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
			<input class="normal_btn" style="display: none" type="button" name="button" id="queryBtn1" value="逐条审批" onclick="cliamAllAudit()"/>&nbsp;&nbsp;
			<%if ((Constant.ACC_STATUS_06).equals(status) && "true".equals(isConfirm)){//当结算单状态为"待收单"且isConfirm为true时显示 %>
			<input class="normal_btn" type="button" name="confirmBtn" id="queryBtn2" value="收单" onclick="confirmOrder()"/>&nbsp;&nbsp;	
			<%} %>
			<input class="normal_btn" style="display:none;" type="button" name="button1" id="queryBtn" value="返回" onclick="fowardPage();"/>
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
		var url = "<%=contextPath%>/claim/application/ClaimManualAuditing/acconeAuditClaimOneByOneJC03.json?COMMAND=1";
		var title = null;

		var controlCols = "<input type=\"checkbox\" name=\"claimAll\" onclick=\"selectAll(this,'claimId')\"/>全选";
		var columns = [
					{header: "序号",align:'center',renderer:getIndex},
					{header: "操作",dataIndex: 'CLAIM_ID',renderer:accAudut},
					{header: "索赔单号",dataIndex: 'CLAIM_NO',align:'center'},
					{header: "上报日期",dataIndex: 'REPORT_DATE',align:'center'},
					{header: "结算审核日期",dataIndex: 'ACCOUNT_DATE',align:'center'},
					{header: "索赔单类型",dataIndex: 'CLAIM_TYPE',align:'center',renderer:getItemValue},
					{header: "生产基地",dataIndex: 'AREA_NAME',align:'center',renderer:getyieldly},	
					{header: "维修站",dataIndex: 'DEALER_NAME',align:'center'},
					{header: "车型",dataIndex: 'GROUP_NAME',align:'center'}, 
					{header: "VIN",dataIndex: 'VIN',align:'center'},
					{header: "申请费用(元)",dataIndex: 'REPAIR_TOTAL',align:'center',renderer:formatCurrency},
					{header: "结算费用(元)",dataIndex: 'BALANCE_AMOUNT',align:'center',renderer:formatCurrency},
					//{header: "申请日期",dataIndex: 'REPORT_DATE',align:'center'},		
					{header: "状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue}
			      ];
	//修改的超链接
	
	function fowardPage(){
		fm.action = "<%=contextPath%>/claim/application/ClaimManualAuditing/claimMAccAuditInit.do?falg=1";
		fm.submit();

	}
	function getyieldly(value,meta,record)
	{
		
	  var resObj = String.format(value +'<input type="hidden" id="'+record.data.CLAIM_ID+'" value="'+value+'"/>');
	  return resObj;
	}
	function accAudut(value,meta,record)
	{
  		var resObj = String.format("<a href='#' onclick='claimdetail(\""+ value +"\",\""+record.data.BALANCE_ID+"\")'>[明细]</a>");
  		var claimStatus = record.data.STATUS;
  		var authcodeval = record.data.AUTH_CODE;
  	  		if((<%=Constant.CLAIM_APPLY_ORD_TYPE_11.toString()%>)==claimStatus )
  	  			resObj = String.format("<a href='#' onclick='audit(\""+ value +"\",\""+record.data.BALANCE_ID+"\")'>[重新审批]</a>");
  	  		if((<%=Constant.CLAIM_APPLY_ORD_TYPE_12.toString()%>)==claimStatus )	
  	  		resObj = String.format("<a href='#' onclick='audit(\""+ value +"\",\""+record.data.BALANCE_ID+"\")'>[重新审批]</a>");
  	  		if((<%=Constant.CLAIM_APPLY_ORD_TYPE_14.toString()%>)==claimStatus )	
  	  		resObj = String.format("<a href='#' onclick='audit(\""+ value +"\",\""+record.data.BALANCE_ID+"\")'>[重新审批]</a>");
  		return resObj;
	}
	
	//审批一条
	function audit(val,balance_id) 
	{
		var fm = document.getElementById("fm");
		fm.action = '<%=contextPath%>/claim/application/ClaimManualAuditing/balanceAuditingPageJC.do?isAudit=true&ID='+ val
		+'&BALANCE_ID=' + balance_id;
		fm.submit();
	}

	//审批一条
	function auditReDo(val,balance_id) 
	{
		var tarUrl = '<%=contextPath%>/claim/application/ClaimManualAuditing/balanceAuditingPageJC.do?isAudit=true&ID='+ val
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
		var tarUrl = "<%=contextPath%>/claim/application/ClaimBillStatusTrack/claimBillDetailForwardJC.do?ID="+id;
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
	    frm.action = "<%=contextPath%>/claim/application/ClaimManualAuditing/auditingClaimOneByOneJC.do";
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
	    if(<%=Constant.CLAIM_APPLY_ORD_TYPE_08%>==record.data.STATUS){
  	  		var claimStatus = record.data.STATUS;
  	  		var authcodeval = record.data.AUTH_CODE;
  	  		if((<%=Constant.CLAIM_APPLY_ORD_TYPE_08.toString()%>)==claimStatus)
				resultStr = String.format("<input type=\"checkbox\" name=\"claimId\" value=\""+record.data.CLAIM_ID+"\"/>");
		}
		return resultStr;
	}

	//批量审批
	function batchAuditingConfirm(){
		if($('yieldly').value==''){
			MyAlert("批量审核必须选择产地！");
			return;
		}
		var selectIndex = document.getElementById("yieldly").selectedIndex;
		var selectText = document.getElementById("yieldly").options[selectIndex].text ;
		
		var selectArray = document.getElementsByName('claimId');
		if(selectArray!=null && selectArray.length>0)
		{
			
			for(var i=0;i<selectArray.length;i++){
				if(selectArray[i].checked)
				{
					var yieldly= document.getElementById(selectArray[i].value).value;
					if(yieldly != selectText)
					{
						MyAlert("选择的索赔单产地必须为"+selectText);
						return;
					}
				}
					
			}
			
         }
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
				{
					return true;
				}
					
			}
		}else{
			return false;
		}
		return false;
	}
	
	function batchAuditing(val)
	{
		 var fm = document.getElementById("fm");
		fm.action = '<%=contextPath%>/claim/application/ClaimManualAuditing/balanceAuditingPageJC01.do?isAudit=true';
		fm.submit();
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