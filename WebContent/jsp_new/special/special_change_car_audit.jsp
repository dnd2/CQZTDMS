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
			$("input[type='text']").attr("readonly","readonly");
			var status=$("#status").val();
			$("textarea").attr("readonly","readonly");
			$(".normal_btn").each(function(){
				 if($(this).val()=="删 除" || $(this).val()=="添加附件"){
					 $(this).hide();
				} 
			});
			if(20331002==status){
				$("#tab1 input").removeAttr("readonly");
				$("#purchase_car_cost,#insurance_money,#discount_money").attr("readonly","readonly");
				var fields="#original_car_price,#on_card_purchase_tax,#on_card_licensing_fee,#on_card_ohers,#insurance_money,#others_money";
				$(fields).bind("blur",function(){
					var val=$(this).val();
					var reg = /^[0-9]+([.]{1}[0-9]{1,2})?$/;
					if(""!=val && !reg.test(val)){
						MyAlert("提示：请输入正确的数据！");
						$(this).val("");
						return;
					}else{
						var money=0.0;
						var str=fields.split(",");
						for(var i=0;i<str.length;i++){
							var val=$(str[i]).val();
							if(""!=val){
								money+=parseFloat(val);
							}
						}
						$("#purchase_car_cost").val(money.toFixed(2));
					}
				});
				var filedTemp="#loss_discount_money,#policy_discount_money";
				$(filedTemp).bind("blur",function(){
					var val=$(this).val();
					var reg = /^[0-9]+([.]{1}[0-9]{1,2})?$/;
					if(""!=val && !reg.test(val)){
						MyAlert("提示：请输入正确的数据！");
						$(this).val("");
						return;
					}else{
						var money=0.0;
						var str=filedTemp.split(",");
						for(var i=0;i<str.length;i++){
							var val=$(str[i]).val();
							if(""!=val){
								money+=parseFloat(val);
							}
						}
						$("#discount_money").val(money.toFixed(2));
					}
				});
				$(".is_insurance").live("click",function(){
					if($('input:radio[name="is_insurance"]:checked').val()=='无'){
						$("#insurance_money").val("");
						$("#insurance_money").attr("disabled",true);
					}else{
						$("#insurance_money").removeAttr("readonly");
						$("#insurance_money").attr("disabled",false);
					}
				});
				$("#service_manager_deal").removeAttr("readonly");
			}
			if(20331003==status){
				$("#regional_manager_deal").removeAttr("readonly");
			}
			if(20331005==status){
				$("#regional_director_deal").removeAttr("readonly");
			}
			var apply_money=$("#apply_money").val();
			if(20331005==status && parseFloat(apply_money)>=800.0){
				$("#regional_director_deal").removeAttr("readonly");
			}
			if(20331005==status && parseFloat(apply_money)<=800.0){
				$("#status").val(20331007);
				$(".tec").show();
			}
			status=$("#status").val();
			if(status>=20331007){
				$(".tec").show();
			}
			if(20331007==status){
				$("#tec_support_dep_deal").removeAttr("readonly");
				$("#responsibility_code").bind("click",function(){
					OpenHtmlWindow('<%=contextPath%>/jsp_new/special/spe_show_supplier_code.jsp',800,500);
				});
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
			}
		}
		var status=$("#status").val();
		if(status>=20331002){
			isradio($("#is_change").val(),"is_change");
		}
		if(status>=20331003){
			isradio($("#old_car_deal").val(),"old_car_deal");
			isradio($("#is_insurance").val(),"is_insurance");
		}
		if(status>=20331009){
			isradio($("#is_warranty").val(),"is_warranty");
		}
	});
	function audit(identify){
		var apply_money=$("#apply_money").val();
		var status=$("#status").val();
		if(0==identify){
			var str="提示：";
			if(20331002==status){
				var strRadio="is_insurance,old_car_deal";
				var textStrRadio="有无保险,旧车处理方式";
				var strRadios=strRadio.split(",");
				var textStrRadios= textStrRadio.split(",");
				var alertStr="";
				for(var j=0;j<strRadios.length;j++){
					if($('input:radio[name="'+strRadios[j]+'"]:checked').val()==null){
						alertStr+= " ["+textStrRadios[j]+"]";
					}
				}
				if(alertStr!=""){
					MyAlert("提示：请选择{"+alertStr+"}");
					return;
				}
				var alertStrText="";
				var strss="purchase_car_cost,original_car_price,on_card_purchase_tax,on_card_licensing_fee,on_card_ohers,others_money,discount_money,loss_discount_money,policy_discount_money,old_car_price,old_car_accept_unit";
				var textStr="购车费用,原车价格,购置税,上牌费,其他,其他费用,折旧费,损耗折旧费,政策折旧费,旧车作价,旧车接收单位";
				var strs= strss.split(",");
				var textStrs= textStr.split(",");
				for(var i=0;i<strs.length;i++){
					if($.trim($("#"+strs[i]).val()).length==0){
						alertStrText+=" ["+textStrs[i]+"] ";
					}
				}
				if(alertStrText!=""){
					MyAlert("提示：请填写必填字段{"+alertStrText+"}");
					return;
				}
				var msg="";
				if($('input:radio[name="is_insurance"]:checked').val()=='有' && $("#insurance_money").val().length==0 ){
					msg+=" [提示：有无保险选择有后,请填写具体金额！]";
				}
				if(msg!=""){
					MyAlert(msg);
					return;
				}
				if(""==$.trim($("#service_manager_deal").val())){
					MyAlert("提示：请填写审核意见！");
					return;
				}
				
				str+=" 审核通过将到区域服务经理审核！";
			}
			if(20331003==status && parseFloat(apply_money)>=800.0){
				if(""==$.trim($("#regional_manager_deal").val())){
					MyAlert("提示：请填写审核意见！");
					return;
				}
				str+=" 金额大于800,审核通过将到区域总监审核！";
			}
			if(20331003==status && parseFloat(apply_money)<=800.0){
				if(""==$.trim($("#regional_manager_deal").val())){
					MyAlert("提示：请填写审核意见！");
					return;
				}
				str+=" 金额小于800,审核通过将到技术支持部审核！";
			}
			if(20331005==status){
				if(""==$.trim($("#regional_director_deal").val())){
					MyAlert("提示：请填写审核意见！");
					return;
				}
				str+=" 审核通过将到技术支持部审核！";
			}
			if(20331007==status){
				if($('input:radio[name="is_warranty"]:checked').val()==null){
					MyAlert("提示：请选择是否享受质保！");
					return;
				}
				if(""==$.trim($("#responsibility_code").val())){
					MyAlert("提示：请选择责任方代码！");
					return;
				}
				if(""==$.trim($("#tec_support_dep_deal").val())){
					MyAlert("提示：请填写审核意见！");
					return;
				}
				str+=" 审核通过将到索赔结算室审核！";
			}
			
			if(20331009==status){
				if(""==$.trim($("#sales_policy_money").val())){
					MyAlert("提示：请填写销售政策金额！");
					return;
				}
				if(""==$.trim($("#audit_amount").val())){
					MyAlert("提示：请填写审批金额！");
					return;
				}
				$("#audit_amount,#sales_policy_money").bind("blur",function(){
					var val=$(this).val();
					var reg = /^[0-9]+([.]{1}[0-9]{1,2})?$/;
					if(""!=val && !reg.test(val)){
						MyAlert("提示：请输入正确的数据！");
						$(this).val("");
						return;
					}
				});
				if(parseFloat($.trim($("#apply_money").val()))<parseFloat($.trim($("#audit_amount").val()))){
				   MyAlert("提示：审批金额不能大于申请金额！");
				   return;
				}
				if(0.0>parseFloat($.trim($("#audit_amount").val()))){
				   MyAlert("提示：审批金额不能小于0！");
				   return;
				}
				if(""==$.trim($("#claim_settlement_deal").val())){
					MyAlert("提示：请填写审核意见！");
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
			if(20331003==status && parseFloat(apply_money)<=800.0){
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
	
	//设置供应商
	function setSupplierCode(maker_code,maker_shotname){
		$("#responsibility_code").val(maker_code);
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
	
	//公共的radio
	function isradio(val,className){
		$("."+className).each(function(){
			if(val==$(this).val()){
				$(this).attr("checked",true);
			}
			$(this).attr("disabled","disabled");
		});
	}
	
</script>
</head>
<body>
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;特殊费用管理&gt;特殊费用(退换车)
</div>
<form name="fm" id="fm" method="post">
<input class="middle_txt" id="specialNo" value="${specialNo }" type="hidden"  />
<input type="hidden" id="dealerCode" value="${dealerCode }">
<input type="hidden" id="is_change" value="${t.IS_CHANGE }"/>
<input type="hidden" id="old_car_deal" value="${t.OLD_CAR_DEAL }"/>
<input type="hidden" id="is_insurance" value="${t.IS_INSURANCE }"/>
<input type="hidden" id="is_warranty" value="${t.IS_WARRANTY }"/>
<input class="middle_txt" id="url" value="${url }" type="hidden"  />
<input type="hidden" id="status" name="status" value="${t.STATUS }">
<input class="middle_txt" id="type" value="${type }" type="hidden"  />
<input class="middle_txt" id="spe_id" value="${t.SPE_ID }" name="spe_id" type="hidden"  />
<input class="middle_txt" id="special_type" value="0" name="special_type" type="hidden"  />
<table border="0" cellpadding="0" cellspacing="0"  width="100%" >
	<tr>
		<td width="35%" nowrap="true" align="center" >
<%--      		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img alt="北汽银翔" width="100px;"  height="60px;" src="<%=request.getContextPath()%>/img/bqyx.png">
 --%>     	</td>
     	<td width="35%" nowrap="true" align="center">
     		<span style="font-weight: bold;font-size: 25px;">北汽幻速汽车退（换）车申请表</span>
     	</td>
     	<td width="30%" nowrap="true" align="right">
     	</td>
	</tr>
</table>
<table border="0" cellpadding="0" cellspacing="0"   width="100%" style="text-align: center;font-weight: bold;">
	<tr>
		<td width="15%" nowrap="true" align="center">
     		&nbsp;经销、服务商编码:
     	</td>
     	<td width="20%" nowrap="true" align="center">
     		${t.FILL_DEALER_CODE }
     		<input type="hidden" id="fill_dealer_code" name="fill_dealer_code" maxlength="35" class="middle_txt"  value="${t.FILL_DEALER_CODE }"/>  
     	</td>
     	<td width="15%" nowrap="true" align="center">
     		&nbsp;经销、服务商简称：
     	</td>
     	<td width="20%" nowrap="true" align="center">
     	${t.FILL_DEALER_SHORTNAME }
     		<input type="hidden" id="fill_dealer_shortname" name="fill_dealer_shortname" maxlength="50" class="middle_txt"  value="${t.FILL_DEALER_SHORTNAME }"/>  
     	</td>
     	<td width="30%" nowrap="true" align="center">
     		申请单号:
     		<span id="apply_no">${t.APPLY_NO }</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input class="middle_txt" id="specialNoAdd" name="apply_no" value="${t.APPLY_NO }" type="hidden"  />
     	</td>
	</tr>
</table>
<br/>
<table border="1" cellpadding="1" cellspacing="1"   class="tab_edit" width="100%" style="text-align: center;" >
	<tr>
		<td width="4%"  rowspan="6" style="writing-mode:tb-rl;color: red;" >索赔员填写</td>
     	<td width="10%" nowrap="true" >
     		申请人：
     	</td>
     	<td width="14%" nowrap="true" >
     		${t.APPLY_PERSON }
     		<input type="hidden" id="apply_person" name="apply_person" maxlength="35" class="middle_txt"  value="${t.APPLY_PERSON }"/>  
     	</td>
     	<td width="10%" nowrap="true" >
     		用户名称：
     	</td>
     	<td width="14%" nowrap="true" >
     		${t.USER_NAME }
     		<input type="hidden" id="user_name" name="user_name" maxlength="35" class="middle_txt"  value="${t.USER_NAME }"/>  
     	</td>
     	<td width="10%" nowrap="true" >
     		联系方式：
     	</td>
     	<td width="14%" nowrap="true" >
     		${t.USER_LINK }
     		<input type="hidden" id="user_link" name="user_link" maxlength="35" class="middle_txt"  value="${t.USER_LINK }"/>  
     	</td>
     	<td width="10%" nowrap="true" >
     		申请时间：
     	</td>
     	<td width="14%" nowrap="true" >
     		<fmt:formatDate value="${t.APPLY_DATE}" pattern='yyyy-MM-dd HH:mm'/>
     		<input class="middle_txt" id="apply_date" name="apply_date"  value="<fmt:formatDate value="${t.APPLY_DATE}" pattern='yyyy-MM-dd HH:mm'/>" readonly="readonly"  type="hidden"  />
     	</td>
	</tr>
	<tr>
     	<td width="10%" nowrap="true" >
     		底盘号：
     	</td>
     	<td width="14%" nowrap="true" >
     		${t.VIN }
     		<input type="hidden" id="vin" name="vin" maxlength="35" class="middle_txt"  value="${t.VIN }"/>  
     	</td>
     	<td width="10%" nowrap="true" >
     		发动机号：
     	</td>
     	<td width="14%" nowrap="true" >
     		<span id="engineNo">${t.ENGINE_NO }</span>
     	</td>
     	<td width="10%" nowrap="true" >
     		申请金额：
     	</td>
     	<td width="14%"nowrap="true" >
     		${t.APPLY_MONEY }
     		<input type="hidden" id="apply_money" name="apply_money" maxlength="35" class="middle_txt"  value="${t.APPLY_MONEY }"/>  
     	</td>
     	<td width="10%" nowrap="true" >
     	</td>
     	<td width="14%"nowrap="true" >
     	</td>
	</tr>
	<tr>
     	<td width="10%" nowrap="true" >
     		购车日期：
     	</td>
     	<td width="14%" nowrap="true" >
     		<span id="buyCarDate">${t.BUY_DATE }</span>
     	</td>
     	<td width="10%" nowrap="true" >
     		车型：
     	</td>
     	<td width="14%" nowrap="true" >
     		<span id="carType">${t.MODEL_NAME }</span>
     	</td>
     	<td width="10%" nowrap="true" >
     		颜色：
     	</td>
     	<td width="14%" nowrap="true" >
     		<span id="color">${t.COLOR }</span>
     	</td>
     	<td width="10%" nowrap="true" >
     		行驶里程：
     	</td>
     	<td width="14%" nowrap="true" >
     		${t.MILEAGE }
     		<input class="middle_txt" id="mileage" name="mileage" maxlength="30" value="${t.MILEAGE }" type="hidden"  />
     	</td>
	</tr>
	<tr>
     	<td colspan="8" nowrap="true" align="left">
     		&nbsp;&nbsp;&nbsp;&nbsp;用户要求：&nbsp;&nbsp;&nbsp;<input type="radio" name="is_change" class="is_change"  value="退车"/>退车&nbsp;&nbsp;&nbsp;<input type="radio"  class="is_change"  name="is_change" value="换车"/>换车
     	</td>
	</tr>
	<tr>
     	<td colspan="8" nowrap="true" align="left" style="font-weight: bold;">
     		&nbsp;&nbsp;&nbsp;退（换）车原因及维修记录：
     	</td>
	</tr>
	<tr>
     	<td colspan="8" nowrap="true" >
     		<textarea style="width: 100%;height: 40px;" id="change_reson" name="change_reson" >${t.CHANGE_RESON }</textarea>
     	</td>
	</tr>
	
	</table>
	<c:if test="${t.STATUS>=20331002}">
	<table id="tab1" border="1" cellpadding="1" cellspacing="1"   class="tab_edit" width="100%" style="text-align: left;" >
	<tr>
		<td width="4%"  rowspan="3" style="writing-mode:tb-rl;color: red;" >&nbsp;&nbsp;&nbsp;服务经理填写</td>
     	<td width="48%" nowrap="true" >
     			&nbsp;&nbsp;&nbsp;&nbsp;购车费用：<input class="short_txt" id="purchase_car_cost" name="purchase_car_cost" maxlength="30" value="${t.PURCHASE_CAR_COST }" type="text"  />&nbsp;元（包含以下费用）
     			<br/>
				&nbsp;1、原车价格：<input class="short_txt" id="original_car_price" name="original_car_price" maxlength="30" value="${t.ORIGINAL_CAR_PRICE }" type="text"  />&nbsp;元。  
				<br/>
				&nbsp;2、上牌相关费用：
				<br/>
				&nbsp;购置税：<input class="short_txt" id="on_card_purchase_tax" name="on_card_purchase_tax" maxlength="30" value="${t.ON_CARD_PURCHASE_TAX }" type="text"  />&nbsp;元、上牌费：<input class="short_txt" id="on_card_licensing_fee" name="on_card_licensing_fee" maxlength="30" value="${t.ON_CARD_LICENSING_FEE }" type="text"  />元、
				<br/>
				&nbsp;&nbsp;&nbsp;其他：<input class="short_txt" id="on_card_ohers" name="on_card_ohers" maxlength="30" value="${t.ON_CARD_OHERS }" type="text"  />&nbsp;元。
     	</td>
     	<td width="48%" nowrap="true" >
		     	&nbsp;3、保险： &nbsp;<input type="radio" name="is_insurance"  class="is_insurance"  value="有"/>有&nbsp;&nbsp;<input type="radio"  class="is_insurance"  name="is_insurance"  value="无"/>无&nbsp;&nbsp;&nbsp;金额：<input class="short_txt" id="insurance_money" name="insurance_money" maxlength="30" value="${t.INSURANCE_MONEY }" type="text"  />       
		     	<br/>
				&nbsp;4、其他费用：<input class="short_txt" id="others_money" name="others_money" maxlength="30" value="${t.OTHERS_MONEY }" type="text"  />
				<br/>
				&nbsp;&nbsp;&nbsp;5、折旧费：<input class="short_txt" id="discount_money" name="discount_money" maxlength="30" value="${t.DISCOUNT_MONEY }" type="text"  />
				<br/>
				&nbsp;&nbsp;损耗折旧费：<input class="short_txt" id="loss_discount_money" name="loss_discount_money" maxlength="30" value="${t.LOSS_DISCOUNT_MONEY }" type="text"  />&nbsp;元，政策折旧费：<input class="short_txt" id="policy_discount_money" name="policy_discount_money" maxlength="30" value="${t.POLICY_DISCOUNT_MONEY }" type="text"  />&nbsp;元。
				<br/>
				&nbsp;6、旧车作价：<input class="short_txt"  id="old_car_price" name="old_car_price" maxlength="30" value="${t.OLD_CAR_PRICE }" type="text"  /> 元。
     	</td>
	</tr>
	<tr>
     	<td width="48%" nowrap="true" >
     		&nbsp;旧车处理方式：&nbsp;<input type="radio" class="old_car_deal" name="old_car_deal"  value="商品车"/>商品车&nbsp;<input type="radio" class="old_car_deal" name="old_car_deal"  value="折扣车"/>折扣车&nbsp;<input type="radio" class="old_car_deal" name="old_car_deal"  value="二手车"/>二手车&nbsp;<input type="radio" class="old_car_deal" name="old_car_deal"  value="不可销售"/>不可销售&nbsp;
     	</td>
     	<td width="48%" nowrap="true" >
     		&nbsp;旧车接收单位:<input style="width:405px;height:14px;line-height:14px;border:1px solid #a6b2c8;padding-left: 2px;" id="old_car_accept_unit" name="old_car_accept_unit" maxlength="100" value="${t.OLD_CAR_ACCEPT_UNIT }" type="text"  />
     	</td>
	</tr>
</table>
</c:if>

<table id="tab2" border="1" cellpadding="1" cellspacing="1"  class="tab_edit" width="100%" style="text-align: center;font-weight: bold;">
	<c:if test="${t.STATUS>=20331002}">
	<tr>
		<td width="4%" rowspan="7" ></td>
     	<td width="10%" nowrap="true" >
     		服务经理意见:
     	</td>
     	<td width="86%" nowrap="true" >
    		<textarea style="width: 100%;height: 40px;" id="service_manager_deal" name="service_manager_deal"  >${t.SERVICE_MANAGER_DEAL }</textarea>
     	</td>
	</tr>
	<tr>
     	<td colspan="3" nowrap="true" style="text-align: right;">
     		 <change:user value="${t.MANAGER_AUDIT_BY }" showType="0"/>  &nbsp;&nbsp;&nbsp; <fmt:formatDate value="${t.MANAGER_AUDIT_DATE }" pattern='yyyy-MM-dd hh:mm'/>&nbsp;&nbsp;
     	</td>
	</tr>
	</c:if>
	 <c:if test="${t.STATUS>=20331003}">
	<tr>
     	<td width="10%" nowrap="true" >
     		区域经理意见:
     	</td>
     	<td width="86%" nowrap="true" >
    		<textarea style="width: 100%;height: 40px;" id="regional_manager_deal" name="regional_manager_deal"  >${t.REGIONAL_MANAGER_DEAL }</textarea>
     	</td>
	</tr>
	<tr>
     	<td colspan="3" nowrap="true" style="text-align: right;">
     	   	<change:user value="${t.REGIONAL_AUDIT_BY }" showType="0"/>  &nbsp;&nbsp;&nbsp; <fmt:formatDate value="${t.REGIONAL_AUDIT_DATE }" pattern='yyyy-MM-dd hh:mm'/>&nbsp;&nbsp;
     	</td>
	</tr>
	</c:if>
     <c:if test="${t.STATUS>=20331005}">
	<tr>
     	<td width="10%" nowrap="true" >
     		区域总监意见:
     	</td>
     	<td width="86%" nowrap="true" >
    		<textarea style="width: 100%;height: 40px;" id="regional_director_deal" name="regional_director_deal"  >${t.REGIONAL_DIRECTOR_DEAL }</textarea>
     	</td>
	</tr>
	<tr>
     	<td colspan="3" nowrap="true" style="text-align: right;">
     		<change:user value="${t.DIRECTOR_AUDIT_BY }" showType="0"/>  &nbsp;&nbsp;&nbsp; <fmt:formatDate value="${t.DIRECTOR_AUDIT_DATE }" pattern='yyyy-MM-dd hh:mm'/>&nbsp;&nbsp;
     	</td>
	</tr>
	</c:if>
	
	<tr class="tec">
     	<td width="10%" nowrap="true" >
     		技术支持部:
     	</td>
     	<td width="86%" nowrap="true" >
    		<textarea style="width: 100%;height: 40px;" id="tec_support_dep_deal" name="tec_support_dep_deal"  >${t.TEC_SUPPORT_DEP_DEAL }</textarea>
     	</td>
	</tr>
	<tr class="tec">
     	<td colspan="3" nowrap="true" style="text-align: right;">
			责任方代码：<input class="middle_txt"  type="text" readonly="readonly" id="responsibility_code" name="responsibility_code" value="${t.RESPONSIBILITY_CODE }"/>&nbsp;&nbsp;是否享受质保：<input type="radio" class="is_warranty" name="is_warranty"  value="是"/>是&nbsp;<input type="radio" class="is_warranty" name="is_warranty"  value="否"/>否&nbsp;&nbsp;<change:user value="${t.TEC_AUDIT_BY }" showType="0"/>  &nbsp;&nbsp;&nbsp; <fmt:formatDate value="${t.TEC_AUDIT_DATE }" pattern='yyyy-MM-dd hh:mm'/>&nbsp;&nbsp;
     	</td>
	</tr>
</table>
<c:if test="${t.STATUS>=20331009}">
 <table border="1" cellpadding="1" cellspacing="1"  class="tab_edit" width="100%" style="text-align: center;">
		<tr>
				<td nowrap="true" width="4%" rowspan="7"></td>
				<td nowrap="true" width="48%" style="font-weight: bold;" align="left">&nbsp;索赔管理部意见:</td>
				<td nowrap="true" width="48%" style="font-weight: bold;" align="left">&nbsp;服务支持中心意见:</td>
		</tr>
		<tr>
				<td nowrap="true" width="48%" >
					<textarea style="font-weight: bold;" name="claim_settlement_deal" id="claim_settlement_deal" rows="4" cols="55">${t.CLAIM_SETTLEMENT_DEAL }</textarea>
				</td>
				<td nowrap="true" width="48%" >
					<textarea style="font-weight: bold;" rows="4" cols="55" readonly="readonly"></textarea>
				</td>
		</tr>
		 <tr>
	     	<td    nowrap="true" style="text-align: left;">
	     			&nbsp;&nbsp;销售政策:<input class="short_txt" id="sales_policy_money" name="sales_policy_money" maxlength="10" value="${t.SALES_POLICY_MONEY }" type="text"  />&nbsp;元 &nbsp;审批金额: <input class="short_txt" id="audit_amount" name="audit_amount" maxlength="10" value="${t.AUDIT_AMOUNT }" type="text"  />&nbsp;元&nbsp;&nbsp;<change:user value="${t.SETTLEMENT_AUDIT_BY }" showType="0"/>&nbsp;<fmt:formatDate value="${t.SETTLEMENT_AUDIT_DATE }" pattern='yyyy-MM-dd hh:mm'/>&nbsp;
	     	</td>
	     	<td    nowrap="true" style="text-align: right;">
	     		年&nbsp;&nbsp;月&nbsp;&nbsp;日&nbsp;&nbsp;
	     	</td>
    	</tr>
		<tr>
				<td nowrap="true" width="48%" style="font-weight: bold;" align="left">&nbsp;总经理意见:</td>
				<td nowrap="true" width="48%" style="font-weight: bold;" align="left">&nbsp;总裁意见:</td>
		</tr>
		<tr>
				<td nowrap="true" width="48%" >
					<textarea style="font-weight: bold;" name="trouble_reason" id="trouble_reason" rows="4" cols="55">${t.TROUBLE_REASON }</textarea>
				</td>
				<td nowrap="true" width="48%" >
					<textarea style="font-weight: bold;" name="trouble_desc" id="trouble_desc" rows="4" cols="55">${t.TROUBLE_DESC }</textarea>
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
    	<tr>
	     	<td  colspan="2">
		    	说明：其中退车款由经销商（服务商）先行赔付，商家实际产生费用由索赔管理部确认后结算。
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
<br>
<table width=100% border="0" cellspacing="0" cellpadding="0">
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