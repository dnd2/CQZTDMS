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
	</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;结算室审核索赔单(轿车)
</div>
<form method="post"  name="fm" id="fm">
<input type="hidden" name="quanxian" value="${quanxian}"/>
<input type="hidden" name="dealer_id" value="${dealer_id}"/>
<input type="hidden" name="balance_yieldly" id="balance_yieldly" value="${balance_yieldly}"/>
<input type="hidden" name="report_date" value="${report_date}"/>
<input type="hidden" name="claim_type" value="${claim_type}"/>
<input type="hidden" name="userType" value="${userType}"/>
<input type="hidden" name="claimStatus" value="${claimStatus}"/>
<input type="hidden" name="verson" value="${verson}"  />
<input type="hidden" name="section" id="section" value="${section}" />
<input type="hidden" name="admin" id="section" value="${admin}" />
<table align="center" border='0' >
   <c:if test="${admin == '1'}">
   	<tr>
   	    <td   align="center">
			<input class="normal_btn" type="button" name="button1" value="导出" onclick="improt();"/>
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
		</td>
		<td align="right" nowrap="true">索赔单号：</td>
        <td class="table_query_4Col_input" nowrap="nowrap" width="530px;">
			<input class="middle_txt"  style="width: 530px;" id="CLAIM_NO"  name="CLAIM_NO" type="text"/>
		</td>
	</tr>
   </c:if>
	

</table>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	
<script type="text/javascript">
		var myPage;
		var url = "<%=contextPath%>/claim/application/ClaimManualAuditing/balancetoal.json?COMMAND=1";
		var title = null;

		var controlCols = "<input type=\"checkbox\" name=\"claimAll\" onclick=\"selectAll(this,'claimId')\"/>全选";
		if('${claim_type}' == '10661011')
		{
			var columns = [
					{header: "序号",align:'center',renderer:getIndex},
					{header: "操作",dataIndex: 'ID',renderer:accAudut},
					{header: "索赔单号",dataIndex: 'CLAIM_NO',align:'center'},
					{header: "索赔单类型",dataIndex: 'CLAIM_TYPE',align:'center',renderer:getItemValue},
					{header: "维修站",dataIndex: 'DEALER_NAME',align:'center'},
					{header: "VIN",dataIndex: 'VIN',align:'center'},
					{header: "申请费用(元)",dataIndex: 'REPAIR_TOTAL',align:'center',renderer:formatCurrency},
					{header: "生产日期",dataIndex: 'PRODUCT_DATE',align:'center'},
					{header: "购车日期",dataIndex: 'PURCHASED_DATE',align:'center'},
					{header: "审核状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue},
					{header: "勾选",dataIndex: 'ID',renderer:Check},
					{header: "审核意见",dataIndex: 'AUDIT_OPINION',align:'center',renderer:audit_opinion}
			      ];
		}else if('${admin}' == '1')
		{
			var columns = [
					{header: "序号",align:'center',renderer:getIndex},
					{header: "操作",dataIndex: 'ID',renderer:accAudut},
					{header: "索赔单号",dataIndex: 'CLAIM_NO',align:'center'},
					{header: "索赔单类型",dataIndex: 'CLAIM_TYPE',align:'center',renderer:getItemValue},
					{header: "维修站",dataIndex: 'DEALER_NAME',align:'center'},
					{header: "VIN",dataIndex: 'VIN',align:'center'},
					{header: "申请工时(元)",dataIndex: 'LABR_COUNT',align:'center',renderer:formatCurrency},
					{header: "结算工时(元)",dataIndex: 'BALANCE_LABR_COUNT',align:'center',renderer:formatCurrency},
					{header: "申请材料(元)",dataIndex: 'PART_COUNT',align:'center',renderer:formatCurrency},
					{header: "结算材料(元)",dataIndex: 'BALANCE_PART_COUNT',align:'center',renderer:formatCurrency},
					{header: "申请外派(元)",dataIndex: 'NETITEM_COUNT',align:'center',renderer:formatCurrency},
					{header: "结算外派(元)",dataIndex: 'BALANCE_NETITEM_COUNT',align:'center',renderer:formatCurrency},
					{header: "申请费用(元)",dataIndex: 'REPAIR_TOTAL',align:'center',renderer:formatCurrency},
					{header: "结算费用(元)",dataIndex: 'DIS_BALANCE_AMOUNT',align:'center',renderer:formatCurrency},
					{header: "审核状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue},
					{header: "勾选",dataIndex: 'ID',renderer:Check},
					{header: "审核意见",dataIndex: 'AUDIT_OPINION',align:'center',renderer:audit_opinion}
			      ];
		}else
		{
			var columns = [
					{header: "序号",align:'center',renderer:getIndex},
					{header: "操作",dataIndex: 'ID',renderer:accAudut},
					{header: "索赔单号",dataIndex: 'CLAIM_NO',align:'center'},
					{header: "索赔单类型",dataIndex: 'CLAIM_TYPE',align:'center',renderer:getItemValue},
					{header: "维修站",dataIndex: 'DEALER_NAME',align:'center'},
					{header: "VIN",dataIndex: 'VIN',align:'center'},
					{header: "申请费用(元)",dataIndex: 'REPAIR_TOTAL',align:'center',renderer:formatCurrency},
					{header: "审核状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue},
					{header: "勾选",dataIndex: 'ID',renderer:Check},
					{header: "审核意见",dataIndex: 'AUDIT_OPINION',align:'center',renderer:audit_opinion}
			      ];
		}
		
	//修改的超链接
	 __extQuery__(1);
	function fowardPage(){
		fm.action = "<%=contextPath%>/claim/application/ClaimManualAuditing/claimMAccAuditInit.do?falg=1";
		fm.submit();

	}
	function improt()
	{
		fm.action = '<%=contextPath%>/claim/application/ClaimManualAuditing/improt.do';
		fm.submit();
	}
	
	function Check(value,meta,record)
	{
		var resObj = String.format(	'<input type="hidden" name="idAll" value ='+value+' /><input name = "id" onclick= read('+record.data.ID+',this) value ='+value+'  type="checkbox"  checked="checked" />' );
		return resObj;
	}
	
	function read(val,obj)
	{
		if(obj.checked)
		{
			document.getElementById(val).readOnly  = true;
			document.getElementById(val).value = '';
		}else
		{
			document.getElementById(val).readOnly  = false;
		}
	}
	
	function commitall()
	{
	 	 var audit_opinion = document.getElementsByName('audit_opinion');
	 	 for(var  i = 0 ;i < audit_opinion.length; i ++)
	 	 {
	 	 	if(!(audit_opinion[i].readOnly))
	 	 	{
	 	 		 var b = audit_opinion[i].value;
	 	 		if( b == '' )
	 	 		{
	 	 		  MyAlert("不同意请给出理由 ！");
	 	 		  return;
	 	 		}
	 	 	}else
	 	 	{
	 	 	   audit_opinion[i].value = '';
	 	 	}
	 	 }
		MyConfirm("确认批量审核?",changeSubmit);
	   
	}
	function changeSubmit()
  	{
  		if('${quanxian}' == '0')
  		{
  			var url="<%=contextPath%>claim/application/ClaimManualAuditing/commitallZR.json";
			makeNomalFormCall(url,showResult,'fm');
  		}else if('${quanxian}' == '1')
  		{
  			var url="<%=contextPath%>claim/application/ClaimManualAuditing/commitall.json";
			makeNomalFormCall(url,showResult,'fm');
  		}
  	    
  	   
  	}
  	function showResult(json)
  	{
  		var balance_yieldly =  document.getElementById('balance_yieldly').value;
  		if(json.msg == '01')
  		{
  			MyAlert('科员正在开单有驳回操作的请稍后 ！');
  			return;
  		}	
  		MyAlert("审批成功");
  		
  		if('${quanxian}' == '0')
  		{
  		   if(balance_yieldly == '95411002')
  		   {
  		   		fm.action = '<%=contextPath%>/claim/application/ClaimManualAuditing/acconeAuditClaimOneByOneOrgJC02.do';
		    	fm.submit();
  		   }else if(balance_yieldly == '95411001')
  		   {
  		   		fm.action = '<%=contextPath%>/claim/application/ClaimManualAuditing/acconeAuditClaimOneByOneJC02.do';
		    	fm.submit();
  		   }
  		   
  			
  		}
  		else if ('${quanxian}' == '1')
  		{
  		
  			if(balance_yieldly == '95411002')
  		   {
  		   		fm.action = '<%=contextPath%>/claim/application/ClaimManualAuditing/acconeAuditClaimOneByOneOrgJC.do';
		    	fm.submit();
  		   }else if(balance_yieldly == '95411001')
  		   {
  		   		fm.action = '<%=contextPath%>/claim/application/ClaimManualAuditing/acconeAuditClaimOneByOneJC.do';
		   		fm.submit();
  		   }		
  		   
  		}
 
  	}
	
	function audit_opinion(value,meta,record)
	{
		var resObj = '';
		if(record.data.STATUS == '10791012' || record.data.STATUS == '10791006')
		{
			resObj = String.format('<input type="text"  readonly="readonly"  id='+record.data.ID+' name="audit_opinion" value="科员意见：'+value+'"/>');
		}
	    else if(value.length > 0 )
	    {
	    	resObj = String.format('<input type="text"  readonly="readonly"  id='+record.data.ID+' name="audit_opinion" value="室主任意见：'+value+'"/>');
		   
	    }else
	    {
	       resObj = String.format('<input type="text"  readonly="readonly"  id='+record.data.ID+' name="audit_opinion" value="'+value+'"/>');
	    }
	   return resObj;
	}  
	
	function accAudut(value,meta,record)
	{
	     var  section = document.getElementById('section').value;
	     if(section == '1')
	     {
	     	if(record.data.IS_INVOICE == '1')
	     	{
	     		document.getElementById('queryBtn').disabled=true;
	     	}
	     }
  	     var resObj = String.format("<a href='#' onclick='claimdetail(\""+ value +"\",\""+record.data.BALANCE_ID+"\")'>[明细]</a>");
  		 return resObj;
	}
	
	//审批一条
	function audit(val) 
	{
		fm.action = '<%=contextPath%>/claim/application/ClaimManualAuditing/balanceAuditingPageJC.do?isAudit=true&ID='+ val;
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
	</script>
<table align="center" class="table_query" border='0'>
   <tr>
		<td align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="审批" onclick="commitall();"/>
			 <input class=normal_btn onclick="javascript:history.go(-1)" value=返回 type=button name=bt_back/>
		</td>
	</tr>
</table>
</form>
</body>
</html>