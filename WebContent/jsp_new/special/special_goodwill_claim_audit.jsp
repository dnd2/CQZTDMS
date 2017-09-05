<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<head> 
<%  String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/zyw/jquery-1.3.2.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/js/jslib/zyw/jquery-calendar.js"></script> 
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/js/jslib/zyw/jquery-calendar.css" /> 
<title>质保手册服务站</title>
<script type="text/javascript">
	$(function(){
		var type=$("#type").val();
		
		if("audit"==type){
			$(".tec").hide();
		 //   $("input[type='text']").attr("readonly","readonly");
			var status=$("#status").val();
			$("textarea").attr("readonly","readonly");
			$(".normal_btn").each(function(){
				 if($(this).val()=="删 除" || $(this).val()=="添加附件"){
					 $(this).hide();
				} 
			});
			if(20331002==status){
				$("#service_manager_deal").removeAttr("readonly");
			}
			if(20331003==status){
				$("#regional_manager_deal").removeAttr("readonly");
			}
			var apply_money=$("#apply_money").val();
			if(20331005==status && parseFloat(apply_money)>=800.0){
				$("#regional_director_deal").removeAttr("readonly");
			}
			if(20331005==status && parseFloat(apply_money)<800.0){
				$("#status").val(20331007);
				$(".tec").show();
			}
			status=$("#status").val();
			if(status>=20331007){
				$(".tec").show();
			}
			if(20331007==status){
				$("#tec_support_dep_deal").removeAttr("readonly");
				$("#tec_part_code").bind("click",function(){
					OpenHtmlWindow('<%=contextPath%>/jsp_new/special/spe_show_part_code.jsp',800,500);
				});
				$("#tec_supply_code").bind("click",function(){
					OpenHtmlWindow('<%=contextPath%>/jsp_new/special/spe_show_supplier_code.jsp',800,500);
				});
			}
			if(20331009==status){
				$("#claim_settlement_deal").removeAttr("readonly");
				$("#audit_amount").removeAttr("readonly");
				$("#audit_amount").bind("blur",function(){
					var val=$(this).val();
					var reg = /^[0-9]+([.]{1}[0-9]{1,2})?$/;
					if(""!=val && !reg.test(val)){
						MyAlert("提示：请输入正确的数据！");
						$(this).val("");
						return;
					}
				});
			}
		}
		
	});
	//设置供应商
	function setSupplierCode(maker_code,maker_shotname){
		$("#tec_supply_code").val(maker_code);
	}
	//设置主因件
	function setPartCode(part_code,part_name){
		$("#tec_part_code").val(part_code);
	}
	function audit(identify){
		var apply_money=$("#apply_money").val();
		var status=$("#status").val();
		if(0==identify){
			var str="提示：";
			if(20331002==status){
				if(""==$.trim($("#service_manager_deal").val())){
					MyAlert("提示：请填写审核意见！");
					return;
				}
				str+=" 审核通过将到区域服务经理审核！";
			}
			if(20331003==status && parseFloat(apply_money)>=800.0){
			   var val=$("#audit_money").val();
					var reg = /^[0-9]+([.]{1}[0-9]{1,2})?$/;
					if(""!=val && !reg.test(val)){
						MyAlert("提示：请输入正确的数据！");
						$("#audit_money").val("");
						return;
					}
					if(""==$.trim($("#audit_money").val())){
				      MyAlert("提示：审批金额必须填写！");
				      return;
				    }
				if(parseFloat($.trim($("#apply_money").val()))<parseFloat($.trim($("#audit_money").val()))){
				   MyAlert("提示：审批金额不能大于申请金额！");
				   return;
				}
				if(0.0>parseFloat($.trim($("#audit_money").val()))){
				   MyAlert("提示：审批金额不能小于0！");
				   return;
				}
				if(""==$.trim($("#regional_manager_deal").val())){
					MyAlert("提示：请填写审核意见！");
					return;
				}
				str+=" 金额大于800,审核通过将到区域总监审核！";
			}
			if(20331003==status && parseFloat(apply_money)<800.0){
			       var val=$("#audit_money").val();
					var reg = /^[0-9]+([.]{1}[0-9]{1,2})?$/;
					if(""!=val && !reg.test(val)){
						MyAlert("提示：请输入正确的数据！");
						$("#audit_money").val("");
						return;
					}
					if(""==$.trim($("#audit_money").val())){
				     MyAlert("提示：审批金额必须填写！");
				      return;
				    }
				if(parseFloat($.trim($("#apply_money").val()))<parseFloat($.trim($("#audit_money").val()))){
				   MyAlert("提示：审批金额不能大于申请金额！");
				   return;
				}
				if(0.0>parseFloat($.trim($("#audit_money").val()))){
				   MyAlert("提示：审批金额不能小于0！");
				   return;
				}
				if(""==$.trim($("#regional_manager_deal").val())){
					MyAlert("提示：请填写审核意见！");
					return;
				}
				str+=" 金额小于800,审核通过将到技术支持部审核！";
			}
			if(20331005==status){
			    var val=$("#balance_Approval_Money").val();
			    var reg = /^[0-9]+([.]{1}[0-9]{1,2})?$/;
					if(""!=val && !reg.test(val)){
						MyAlert("提示：请输入正确的数据！");
						$("#balance_Approval_Money").val("");
						return;
					}
					if(""==$.trim($("#balance_Approval_Money").val())){
				     MyAlert("提示：审批金额必须填写！");
				      return;
				    }
				if(parseFloat($.trim($("#apply_money").val()))<parseFloat($.trim($("#balance_Approval_Money").val()))){
				   MyAlert("提示：审批金额不能大于申请金额！");
				   return;
				}
				if(0.0>parseFloat($.trim($("#balance_Approval_Money").val()))){
				   MyAlert("提示：审批金额不能小于0！");
				   return;
				}
				if(""==$.trim($("#regional_director_deal").val())){
					MyAlert("提示：请填写审核意见！");
					return;
				}
				str+=" 审核通过将到技术支持部审核！";
			}
			if(20331007==status){
				if(""==$.trim($("#tec_support_dep_deal").val())){
					MyAlert("提示：请填写审核意见！");
					return;
				}
				var tec_supply_code=$("#tec_supply_code").val();
				var tec_part_code=$("#tec_part_code").val();
				if(""==tec_part_code){
					MyAlert("提示：请填写主因件代码！");
					return;
				}
				if(""==tec_supply_code){
					MyAlert("提示：请填写供应商代码！");
					return;
				}
				str+=" 审核通过将到索赔结算室审核！";
			}
			if(20331009==status){
			$("#claim_settlement_deal").removeAttr("readonly");
			$("#audit_amount,#sales_policy_money").removeAttr("readonly");
			$("#audit_amount,#sales_policy_money").bind("blur",function(){
					var val=$(this).val();
					var reg = /^[0-9]+([.]{1}[0-9]{1,2})?$/;
					if(""!=val && !reg.test(val)){
						MyAlert("提示：请输入正确的数据！");
						$(this).val("");
						return;
					}
				});
				if(""==$.trim($("#claim_settlement_deal").val())){
					MyAlert("提示：请填写审核意见！");
					return;
				}
				if(""==$.trim($("#audit_amount").val())){
				   MyAlert("提示：审批金额必须填写！");
				   return;
				}
				if(parseFloat($.trim($("#apply_money").val()))<parseFloat($.trim($("#audit_amount").val()))){
				   MyAlert("提示：审批金额不能大于申请金额！");
				   return;
				}
				if(0.0>parseFloat($.trim($("#audit_amount").val()))){
				   MyAlert("提示：审批金额不能小于0！");
				   return;
				}
				str+=" 审核通过流程关闭,服务站可打印单据！";
			}
			MyConfirm("是否确认通过？"+str,auditCommit,[0]);
		}
		if(1==identify){//驳回
			if(20331002==status){
				if(""==$.trim($("#service_manager_deal").val())){
					MyAlert("提示：请填写审核意见！");
					return;
				}
			}
			if(20331003==status && parseFloat(apply_money)>=800.0){
				if(""==$.trim($("#regional_manager_deal").val())){
					MyAlert("提示：请填写审核意见！");
					return;
				}
			}
			if(20331003==status && parseFloat(apply_money)<800.0){
				if(""==$.trim($("#regional_manager_deal").val())){
					MyAlert("提示：请填写审核意见！");
					return;
				}
			}
			if(20331005==status){
				if(""==$.trim($("#regional_director_deal").val())){
					MyAlert("提示：请填写审核意见！");
					return;
				}
			}
			if(20331007==status){
				if(""==$.trim($("#tec_support_dep_deal").val())){
					MyAlert("提示：请填写审核意见！");
					return;
				}
			//	var tec_supply_code=$("#tec_supply_code").val();
			//	var tec_part_code=$("#tec_part_code").val();
			//	if(""==tec_part_code){
			//		MyAlert("提示：请填写主因件代码！");
			//		return;
			//	}
			//	if(""==tec_supply_code){
			//		MyAlert("提示：请填写供应商代码！");
			//		return;
			//	}
			}
			if(20331009==status){
				if(""==$.trim($("#claim_settlement_deal").val())){
					MyAlert("提示：请填写审核意见！");
					temp++;
					return;
				}
			}
			MyConfirm("是否确认驳回给服务站？",auditCommit,[1]);
		}
	}
	
	function auditCommit(identify){
		$("#pass").attr("disabled",true);
		$("#rebut").attr("disabled",true);
		var url="<%=contextPath%>/SpecialAction/audit.json?identify="+identify;
		makeNomalFormCall1(url,auditCommitBack,"fm");
	}
	function auditCommitBack(json){
		var str="";
		if(json.identify=="0"){
			str+="审核通过";
		}
		if(json.identify=="1"){
			str+="审核驳回";
		}
		if(json.succ=="1"){
			MyAlert("提示："+str+"成功！");
			var urlTemp=$("#url").val();
			var url='<%=contextPath%>/SpecialAction/';
			url+=urlTemp;
			url+='.do';
			window.location.href=url;
		}else{
			MyAlert("提示："+str+"失败,请检查数据！");
			$("#pass").attr("disabled",false);
			$("#rebut").attr("disabled",false);
		}
	}
	function remove(){
	  $(".audit_money").removeAttr("readonly");
	}

</script>
</head>
<body>
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;特殊费用管理&gt;特殊费用(善意索赔)
</div>
<form name="fm" id="fm" method="post">
<input class="middle_txt" id="specialNo" value="${specialNo }" type="hidden"  />
<input type="hidden" id="dealerCode" value="${dealerCode }">
<input type="hidden" id="status" name="status" value="${t.STATUS }">
<input type="hidden" id="is_claim" value="${t.IS_CLAIM }"/>
<input class="middle_txt" id="type" value="${type }" type="hidden"  />
<input class="middle_txt" id="url" value="${url }" type="hidden"  />
<input class="middle_txt" id="spe_id" value="${t.SPE_ID }" name="spe_id" type="hidden"  />
<input class="middle_txt" id="special_type" value="1" name="special_type" type="hidden"  />
<table border="0" cellpadding="0" cellspacing="0"  width="100%" >
	<tr>
		<td width="35%" nowrap="true" align="center" >
     		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img alt="北汽银翔" width="100px;"  height="60px;" src="<%=request.getContextPath()%>/img/bqyx.png">
     	</td>
     	<td width="35%" nowrap="true" align="center">
     		<span style="font-weight: bold;font-size: 25px;">善意索赔申请单</span>
     	</td>
     	<td width="30%" nowrap="true" align="right">
     		申请单号:<span id="apply_no">${t.APPLY_NO }</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input class="middle_txt" id="specialNoAdd" name="apply_no" value="${t.APPLY_NO }" type="hidden"  />
     	</td>
	</tr>
</table>
<table border="1" cellpadding="1" cellspacing="1"  class="tab_edit" width="100%" style="text-align: center;">
	<tr>
		<td width="35%" nowrap="true" align="center">
     		服务商简称（盖公章）
     	</td>
     	<td width="20%" nowrap="true" align="center">
     		服务商代码
     	</td>
     	<td width="25%" nowrap="true" align="center">
     		服务商联系人
     	</td>
     	<td width="15%" nowrap="true" align="center">
     		<input class="middle_txt" maxlength="30" id="dealer_contact" name="dealer_contact" value="${t.DEALER_CONTACT }" type="hidden"  /><span>${t.DEALER_CONTACT }</span>
     	</td>
	</tr>
	<tr height="30px;">
		<td width="35%" nowrap="true" align="center">
     		${t.DEALER_SHORTNAME }
     	</td>
     	<td width="20%" nowrap="true" align="center">
     		<input type="hidden" name="dealer_code" value="${t.DEALER_CODE }"><span id="dealer_code">${t.DEALER_CODE }</span>
     	</td>
     	<td width="25%" nowrap="true" align="center">
     		服务商联系电话
     	</td>
     	<td width="15%" nowrap="true" align="center">
     		<input class="middle_txt" maxlength="30" id="dealer_phone" name="dealer_phone" value="${t.DEALER_PHONE }" type="hidden"  /><span>${t.DEALER_PHONE }</span>
     	</td>
	</tr>
</table>
<table border="1" cellpadding="1" cellspacing="1"   class="tab_edit" width="100%" style="text-align: center;font-weight: bold;" >
	<tr>
     	<td width="17.5%" nowrap="true" >
     		是否索赔：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<c:if test="${t.IS_CLAIM==1 }">
     			是
     		</c:if>
     		<c:if test="${t.IS_CLAIM==0 }">
     			否
     		</c:if>
     	</td>
     	<td width="12.5%" nowrap="true" >
     		主因件代码：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<input class="middle_txt" name="part_code_dealer" id="part_code_dealer" value="${t.PART_CODE_DEALER }" readonly="readonly" type="hidden"  /><span>${t.PART_CODE_DEALER }</span>
     	</td>
     	<td width="12.5%" nowrap="true" >
     		供应商代码：
     	</td>
     	<td width="22.5%" nowrap="true" >
     		<input class="middle_txt" name="supply_code_dealer" id="supply_code_dealer" value="${t.SUPPLY_CODE_DEALER }" readonly="readonly" type="hidden"  /><span>${t.SUPPLY_CODE_DEALER }</span>
     	</td>
	</tr>
</table>
<table border="1" cellpadding="1" cellspacing="1"  class="tab_edit" width="100%" style="text-align: center;">
	<tr>
     	<td width="17.5%" nowrap="true" >
     		索赔单号：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<input class="middle_txt" readonly="readonly" id="claim_no" name="claim_no" value="${t.CLAIM_NO }" type="hidden"  /><span>${t.CLAIM_NO }</span>
     	</td>
     	<td width="12.5%" nowrap="true" >
     		车牌号：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<span id="carNo">${t.LICENSE_NO }</span>
     	</td>
     	<td width="12.5%" nowrap="true" >
     		车型：
     	</td>
     	<td width="22.5%" nowrap="true" >
     		<span id="carType">${t.MODEL_NAME }</span>
     	</td>
	</tr>
	<tr>
     	<td width="17.5%" nowrap="true" >
     		VIN码：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<input class="middle_txt" id="vin" name="vin" maxlength="30" value="${t.VIN }" type="hidden"  /><span>${t.VIN }</span>
     	</td>
     	<td width="12.5%" nowrap="true" >
     		发动机号：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<span id="engineNo">${t.ENGINE_NO }</span>
     	</td>
     	<td width="12.5%" nowrap="true" >
     		行驶里程：
     	</td>
     	<td width="22.5%" nowrap="true" >
     		<input class="middle_txt" id="mileage" name="mileage" maxlength="30" value="${t.MILEAGE }" type="hidden"  /><span>${t.MILEAGE }</span>
     	</td>
	</tr>
	<tr>
     	<td width="17.5%" nowrap="true" >
     		生产日期：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<span id="productDate">${t.PRODUCT_DATE }</span>
     	</td>
     	<td width="12.5%" nowrap="true" >
     		购车日期：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<span id="buyCarDate">${t.BUY_DATE }</span>
     	</td>
     	<td width="12.5%" nowrap="true" >
     		故障发生日期：
     	</td>
     	<td width="22.5%" nowrap="true" >
     		<input class="middle_txt" id="problem_date" name="problem_date"  value="<fmt:formatDate value="${t.PROBLEM_DATE}" pattern='yyyy-MM-dd HH:mm'/>" readonly="readonly"  onfocus="$(this).calendar()" type="hidden"  /><span><fmt:formatDate value="${t.PROBLEM_DATE}" pattern='yyyy-MM-dd HH:mm'/></span>
     	</td>
	</tr>
	<tr>
     	<td width="17.5%" nowrap="true" style="font-weight: bold;">
     		车主信息
     	</td>
     	<td colspan="5"></td>
	</tr>
	<tr>
     	<td width="17.5%" nowrap="true" >
     		姓名：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<span id="userName">${t.CTM_NAME }</span>
     	</td>
     	<td width="12.5%" nowrap="true" >
     		电话：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<span id="userPhone">${t.PHONE }</span>
     	</td>
     	<td width="12.5%" nowrap="true" >
     		职业：
     	</td>
     	<td width="22.5%" nowrap="true" >
     		<input class="middle_txt" id="job" name="job" maxlength="30" value="${t.JOB }" type="hidden"  /><span>${t.JOB }</span>
     	</td>
	</tr>
	<tr>
     	<td width="17.5%" nowrap="true" >
     		地址：
     	</td>
     	<td colspan="5">
     		<span id="userAddr">${t.ADDRESS }</span>
     	</td>
     </tr>
     <tr>
     	<td width="17.5%" nowrap="true" >
     		申请金额(元)：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<input class="middle_txt" id="apply_money" name="apply_money" maxlength="30" value="${t.APPLY_MONEY }" type="hidden"  /><span>${t.APPLY_MONEY }</span>
     	</td>
     	<td width="17.5%" colspan="4" nowrap="true" >
     	</td>
	</tr>
</table>
<table border="1" cellpadding="1" cellspacing="1"  class="tab_edit" width="100%" style="text-align: center;">
	<tr>
     	<td  width="17.5%" nowrap="true" style="font-weight: bold;" >
     		事件主题:
    	</td>
    	<td  width="82.5%">
    		<textarea style="width: 100%;height: 40px;" readonly="readonly" id="event_theme" name="event_theme" >${t.EVENT_THEME }</textarea>
    	</td>
    </tr>
    <tr>
     	<td  width="17.5%" nowrap="true" >
     		投诉内容及车主意见:
    	</td>
    	<td  width="82.5%">
    		<textarea style="width: 100%;height: 40px;" readonly="readonly" id="complain_advice" name="complain_advice" >${t.COMPLAIN_ADVICE }</textarea>
    	</td>
    </tr>
    <c:if test="${t.STATUS>=20331002}">
    <tr>
     	<td  width="17.5%" nowrap="true" >
     		服务经理处理结果:
    	</td>
    	<td  width="82.5%">
    		<textarea style="width: 100%;height: 40px;" id="service_manager_deal" name="service_manager_deal"  >${t.SERVICE_MANAGER_DEAL }</textarea>
    	</td>
    </tr>
    <tr>
     	<td  colspan="2"  nowrap="true" style="text-align: right;">
     		<change:user value="${t.MANAGER_AUDIT_BY }" showType="0"/>  &nbsp;&nbsp;&nbsp; <fmt:formatDate value="${t.MANAGER_AUDIT_DATE }" pattern='yyyy-MM-dd hh:mm'/>&nbsp;&nbsp;
     	</td>
    </tr>
    </c:if>
     <c:if test="${t.STATUS>=20331003}">
    
    <tr>
     	<td  width="17.5%" nowrap="true" >
     		区域经理意见:
    	</td>
    	<td  width="82.5%">
    		<textarea style="width: 100%;height: 40px;" id="regional_manager_deal" name="regional_manager_deal"  >${t.REGIONAL_MANAGER_DEAL }</textarea>
    	</td>
    </tr>
     <tr>
		<td width="17.5%" nowrap="true" >
     		审核金额(元)：
     	</td>
     	<td width="12.5%" nowrap="true" align="left">
     	  <c:if test="${t.STATUS==20331003}">
     		<input  id="audit_money" name="audit_money" class="middle_txt"  type="text" onfocus="remove();" />
     	 </c:if>
     	 <c:if test="${t.STATUS>20331003}">
     		<input  id="audit_money" name="audit_money" class="middle_txt" value="${t.APPROVAL_MONEY }" type="hidden" readonly="readonly" />&nbsp;&nbsp;<span >${t.APPROVAL_MONEY }</span>
     	 </c:if>
     	</td>
    </tr>
    <tr>
     	<td  colspan="2"  nowrap="true" style="text-align: right;">
     	   	<change:user value="${t.REGIONAL_AUDIT_BY }" showType="0"/>  &nbsp;&nbsp;&nbsp; <fmt:formatDate value="${t.REGIONAL_AUDIT_DATE }" pattern='yyyy-MM-dd hh:mm'/>&nbsp;&nbsp;
     	</td>
    </tr>
    </c:if>
     <c:if test="${t.STATUS>=20331005}">
    <tr>
     	<td  width="17.5%" nowrap="true" >
     		区域总监意见:
    	</td>
    	<td  width="82.5%">
    		<textarea style="width: 100%;height: 40px;" id="regional_director_deal" name="regional_director_deal"  >${t.REGIONAL_DIRECTOR_DEAL }</textarea>
    	</td>
    	
    </tr>
     <tr>
     <td width="17.5%" nowrap="true" >
     		审核金额(元)：
     	</td>
     	<td width="12.5%" nowrap="true" align="left">
     	  <c:if test="${t.STATUS==20331005 && t.APPLY_MONEY>=800}">
     		<input  id="balance_Approval_Money" name="balance_Approval_Money" class="middle_txt"  type="text" onfocus="remove();" />
     	 </c:if>
     	 <c:if test="${t.STATUS>20331005  }">
     		<input  id="balance_Approval_Money" name="balance_Approval_Money" class="middle_txt" value="${t.BALANCE_APPROVAL_MONEY }" type="hidden" readonly="readonly" />&nbsp;&nbsp;<span >${t.BALANCE_APPROVAL_MONEY }</span>
     	 </c:if>
     	</td>
    </tr>
    <tr>
     	<td  colspan="2"  nowrap="true" style="text-align: right;">
     		<change:user value="${t.DIRECTOR_AUDIT_BY }" showType="0"/>  &nbsp;&nbsp;&nbsp; <fmt:formatDate value="${t.DIRECTOR_AUDIT_DATE }" pattern='yyyy-MM-dd hh:mm'/>&nbsp;&nbsp;
     	</td>
    </tr>
    </c:if>
    <tr class="tec">
     	<td  width="17.5%" nowrap="true" >
     		技术支持部意见:
    	</td>
    	<td  width="82.5%">
    		<textarea style="width: 100%;height: 40px;" id="tec_support_dep_deal" name="tec_support_dep_deal"  >${t.TEC_SUPPORT_DEP_DEAL }</textarea>
    	</td>
    </tr>
    <tr class="tec">
		<td width="5%" nowrap="true" ></td>
     	<td  colspan="2"  nowrap="true" style="text-align: right;">
     		 主因件代码：<input class="middle_txt"  type="text" readonly="readonly" id="tec_part_code" name="tec_part_code" value="${t.TEC_PART_CODE }"/>&nbsp;&nbsp;供应商代码：<input class="middle_txt"  type="text" id="tec_supply_code" name="tec_supply_code" value="${t.TEC_SUPPLY_CODE }" readonly="readonly" />&nbsp;<change:user value="${t.TEC_AUDIT_BY }" showType="0"/>  &nbsp;&nbsp;&nbsp; <fmt:formatDate value="${t.TEC_AUDIT_DATE }" pattern='yyyy-MM-dd hh:mm'/>&nbsp;&nbsp;
     	</td>
    	<td width="5%" nowrap="true" ></td>
    </tr>
</table>
<c:if test="${t.STATUS>=20331009}">
 <table border="1" cellpadding="1" cellspacing="1"  class="tab_edit" width="100%" style="text-align: center;">
		<tr>
				<td nowrap="true" width="50%" style="font-weight: bold;" align="left">&nbsp;索赔管理部意见:</td>
				<td nowrap="true" width="50%" style="font-weight: bold;" align="left">&nbsp;服务支持中心意见:</td>
		</tr>
		<tr>
				<td nowrap="true" width="50%" >
					<textarea style="font-weight: bold;border: 1px solid #FFFFFF;" name="claim_settlement_deal" id="claim_settlement_deal" rows="4" cols="69">${t.CLAIM_SETTLEMENT_DEAL }</textarea>
				</td>
				<td nowrap="true" width="50%" >
					<textarea style="font-weight: bold;border: 1px solid #FFFFFF;" rows="4" cols="69" readonly="readonly"></textarea>
				</td>
		</tr>
		 <tr>
	     	<td    nowrap="true" style="text-align: right;">
	     		审核费：<input class="middle_txt"  name="audit_amount" id="audit_amount" type="text"  value="${t.AUDIT_AMOUNT }" />&nbsp;&nbsp;&nbsp;<change:user value="${t.SETTLEMENT_AUDIT_BY }" showType="0"/>  &nbsp;&nbsp;&nbsp; <fmt:formatDate value="${t.SETTLEMENT_AUDIT_DATE }" pattern='yyyy-MM-dd hh:mm'/>&nbsp;&nbsp;
	     	</td>
	     	<td    nowrap="true" style="text-align: right;">
	     		年&nbsp;&nbsp;月&nbsp;&nbsp;日&nbsp;&nbsp;
	     	</td>
    	</tr>
		<tr>
				<td nowrap="true" width="50%" style="font-weight: bold;" align="left">&nbsp;总经理意见:</td>
				<td nowrap="true" width="50%" style="font-weight: bold;" align="left">&nbsp;总裁意见:</td>
		</tr>
		<tr>
				<td nowrap="true" width="50%" >
					<textarea style="font-weight: bold;border: 1px solid #FFFFFF;" name="trouble_reason" id="trouble_reason" rows="4" cols="69">${t.TROUBLE_REASON }</textarea>
				</td>
				<td nowrap="true" width="50%" >
					<textarea style="font-weight: bold;border: 1px solid #FFFFFF;" name="trouble_desc" id="trouble_desc" rows="4" cols="69">${t.TROUBLE_DESC }</textarea>
				</td>
		</tr>
		<tr>
	     	<td    nowrap="true" style="text-align: right;">
	     		年&nbsp;&nbsp;月&nbsp;&nbsp;日&nbsp;&nbsp;
	     	</td>
	     	<td    nowrap="true" style="text-align: right;">
	     		年&nbsp;&nbsp;月&nbsp;&nbsp;日&nbsp;&nbsp;
	     	</td>
    	</tr>
</table>
</c:if>
<!-- 添加附件 开始  -->
 <table id="add_file"  width="75%" class="table_info" border="0" id="file">
 		<tr>
     		<th>
		<input type="hidden" id="fjids" name="fjids"/>
		<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
		<font color="red">
			<span id="span1"></span>
		</font>
    		<input type="button" class="normal_btn" align="right" id="addfile" onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
	</th>
</tr>
<tr>
				<td width="75%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
		</tr>
		<%if(fileList!=null){
			for(int i=0;i<fileList.size();i++) { %>
	  <script type="text/javascript">
    		addUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
    	</script>
<%}}%>
</table> 
<!-- 添加附件 结束 -->
  		
<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="pass" onclick="audit(0);"  style="width=8%" value="通过" />
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="rebut" onclick="audit(1);"  style="width=8%" value="驳回" />
               	&nbsp;&nbsp;
				<input type="button" id="back" onClick="history.back();" class="normal_btn"  style="width=8%" value="返回"/>
   			</td>
           	<td height="12" align=center width="33%">
     			</td>
		</tr>
</table>
</form>
</body>
</html>