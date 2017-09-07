<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>二次抵扣索赔单查询</title>
</head>
<body onload="doInit();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;二次抵扣</div>
<form id="fm" name="fm">
<!-- 隐藏域 -->
  <input type="hidden" name="claimId" id="claimId" value="${claimId}"/>
  <input type="hidden" name="dealerId" id="dealerId" value="${claimDeductionSecondMap.DEALER_ID}"/>
  <input type="hidden" name="applyTotalAmount" id="applyTotalAmount" value="${claimDeductionSecondMap.APPLY_TOTAL_AMOUNT}"/>
  <input type="hidden" name="deductionAmount" id="deductionAmount" value="${claimDeductionSecondMap.DEDUCTION_AMOUNT}"/>
  <input type="hidden" name="secondDeductionAmounted" id="secondDeductionAmounted" value="${claimDeductionSecondMap.SECOND_DEDUCTION_AMOUNT}"/>
  <!-- 索赔单信息 -->
  <div class="form-panel">
  <h2>索赔单信息</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">索赔单号：</td>
	  <td align="left" style="width:245px">${claimDeductionSecondMap.APP_CLAIM_NO}</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">经销商代码：</td>
	  <td align="left" style="width:245px">${claimDeductionSecondMap.DEALER_CODE}</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">经销商名称：</td>
	  <td align="left" style="width:245px">${claimDeductionSecondMap.DEALER_NAME}</td>
	</tr>
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">索赔类型：</td>
	  <td align="left" style="width:245px">${claimDeductionSecondMap.REPAIR_TYPE_NAME}</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">索赔总费用：</td>
	  <td align="left" style="width:245px">${claimDeductionSecondMap.APPLY_TOTAL_AMOUNT}</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">一次抵扣费用：</td>
	  <td align="left" style="width:245px">${claimDeductionSecondMap.DEDUCTION_AMOUNT}</td>
	</tr>
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">已二次抵扣费用：</td>
	  <td align="left" style="width:245px">${claimDeductionSecondMap.SECOND_DEDUCTION_AMOUNT}</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">剩余索赔费用：</td>
	  <td align="left" style="width:245px">${claimDeductionSecondMap.SURPLUS_DEDUCTION_AMOUNT}</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">&nbsp;</td>
	  <td align="left" style="width:245px">&nbsp;</td>
	</tr>
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">抵扣费：</td>
	  <td align="left" style="width:245px">
        <input id="secondDeductionAmount" name="secondDeductionAmount" maxlength="30" value="" type="text" class="middle_txt" />
        <font style="color:red">*</font>
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">&nbsp;</td>
	  <td align="left" style="width:245px">&nbsp;</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">&nbsp;</td>
	  <td align="left" style="width:245px">&nbsp;</td>
	</tr>
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;vertical-align:top">抵扣备注：</td>
	  <td colspan=5 align="left">
        <textarea id="secondDeductionRemark" name="secondDeductionRemark" class="form-control" rows="3" style="width:350px;display:inline-block"></textarea>
        <font style="color:red">*</font>
      </td>
	</tr>
	</table>
	</div>
  </div>
  <!-- 抵扣明细 -->
  <div class="form-panel" style="width:100%">
  <h2>抵扣明细</h2>
    <div class="form-body">
	  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    </div>
  </div>
  <!-- 功能按钮 -->
  <table id="bt" class="table_query">
	 <tr>
		<td style="text-align:center" >
			<input type="button"  onclick="secondDeductionAmountConfirm();" id="sureBtn" class="normal_btn" value="确定" />&nbsp;&nbsp;
			<input type="button" onclick="_hide();" id="backBtn" class="normal_btn" value="关闭" />&nbsp;&nbsp;
		</td>
	</tr>
  </table>
</form> 
</div>
<br>
</body>
<script type="text/javascript">
   var myPage;
   var url = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/oldPartDeductionQueryInit.json";
	
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{header: "抵扣单号",dataIndex: 'DEDUCTION_NO',align:'center'},
  				{header: "抵扣类型", dataIndex: 'DEDUCTION_TYPE', align:'center',renderer:getItemValue},
  				{header: "抵扣单状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
  				{header: "通知日期", dataIndex: 'UPDATE_DATE', align:'center'},
  				{header: "材料费扣款（元）", dataIndex: 'PART_DEDUCTION_AMOUNT', align:'center'},
  				{header: "工时费扣款（元）", dataIndex: 'HOURS_DEDUCTION_AMOUNT', align:'center'},
  				{header: "其他项目扣款(元)", dataIndex: 'OUTWARD_DEDUCTION_AMOUNT', align:'center'},
  				{header: "二次抵扣扣款(元)", dataIndex: 'SECOND_DEDUCTION_AMOUNT', align:'center'},
  				{header: "二次抵扣备注", dataIndex: 'SECOND_DEDUCTION_REMARK', align:'center'},
  				{header: "总计(元)", dataIndex: 'TOTAL_DEDUCTION_AMOUNT', align:'center'},
  				{header: "结算单号", dataIndex: 'BALANCE_NO', align:'center'}
  		      ];
  		      
   function doInit(){
	   __extQuery__(1);
	  //loadcalendar();
   }
   //二次抵扣保存
   function secondDeductionAmountConfirm(){
	   //alert(1);
	   var applyTotalAmount = document.getElementById("applyTotalAmount");
	   var deductionAmount = document.getElementById("deductionAmount");
	   var secondDeductionAmounted = document.getElementById("secondDeductionAmounted");
	   var secondDeductionRemark = document.getElementById("secondDeductionRemark");
	   
	   var secondDeductionAmount = document.getElementById("secondDeductionAmount");
	   
	   //提示信息
	   var msg = "";
	   var count = 0;
	   if(secondDeductionAmount.value==""){
		   count++;
		   msg = msg + "<div>" + count + "、抵扣费不能为空!</div>\n";
	   }else if(!isPrice(secondDeductionAmount.value)){
		   count++;
		   msg = msg + "<div>" + count + "、抵扣费必须为最多2位小数的数字!</div>\n";
	   }else if(isPrice(secondDeductionAmount.value)){
		   if(parseFloat(deductionAmount.value)+parseFloat(secondDeductionAmounted.value)+parseFloat(secondDeductionAmount.value)>parseFloat(applyTotalAmount.value)){
			   count++;
			   msg = msg + "<div>" + count + "、抵扣费超出剩余可抵扣金额!</div>\n";
		   }
	   }
	   //alert(secondDeductionRemark.value);
	   if(secondDeductionRemark.value==""){//抵扣备注
		   count++;
		   msg = msg + "<div>" + count + "、抵扣备注不能为空!</div>\n";
	   }
	   if(msg == ""){
	       MyConfirm("确认抵扣"+secondDeductionAmount.value+"(元)?",secondDeductionAmountSave,[]);
	   }else{
		   MyAlert(msg);
		   return;
	   }
   }
   function secondDeductionAmountSave(){
	   var url= "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/secondDeductionAmountSave.json";
	   makeNomalFormCall(url,secondDeductionAmountSaveResult,'fm','');
   }
   //抵扣结果
   function secondDeductionAmountSaveResult(json){
	  if(json.code=="succ"){
	     MyAlertForFun("抵扣成功！",toClaimDeuctionSecondQuery);
	  }else{
	     MyAlert(json.msg);
	  }
   }
   function toClaimDeuctionSecondQuery(){
	   window.location.href="<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/claimDeductionSecond.do";
   }
   //大于0的正整数
   function isPrice(value){
     var reg = /^(\d+\.\d{1,2}|\d+)$/;
     if (value!="") {
       if (reg.test(value)) {
         return true;
       }
     }
     return false;
   }
</script>

</html>