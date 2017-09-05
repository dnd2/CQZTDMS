<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/jstl/change" prefix="change" %>
<head> 
<% String contextPath = request.getContextPath(); %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>

<title>售后工单新增</title>
<script type="text/javascript">
	function ro_type_add(){
		var wrgroup_id=$("#wrgroup_id").val();
		var package_id=$("#package_id").val();
		var series_id=$("#series_id").val();
		var model_id=$("#model_id").val();
		var vin=$("#vin").val();
		if(""==wrgroup_id||""==package_id||""==series_id||""==model_id||""==vin){
			MyAlert("提示：请先输入VIN并确认是否信息正确带出！");
			return;
		}
		var ro_type="";
		$("input[name='ro_type']").each(
			function(){
				ro_type+=$(this).val()+",";
			}
		);
		OpenHtmlWindow('<%=contextPath%>/OrderAction/chooseRoType.do?ro_type='+ro_type,400,200);
	}
	function chooseType(val){
		var div=$('#new_add');
		var str="";
		//配件====================================
		if("93331001"==val){
			str+='<table border="1" id="tab_1" cellpadding="1" cellspacing="1" class="table_edit" width="100%"  style="text-align: center;">';
			str+='<th colspan="9"><input type="hidden" name="ro_type" value="93331001"><img class="nav" src="../jsp_new/img/subNav.gif" />正常维修&nbsp;&nbsp;<input type="button"  value="删除" onclick="delPart(this,93331001);" class="normal_btn" /></th>';
			str+='<tr>';
			str+='<td nowrap="true" width="10%" >维修配件</td>';
			str+='<td nowrap="true" width="15%" >新件代码</td>';
			str+='<td nowrap="true" width="15%" >新件名称</td>';
			str+='<td nowrap="true" width="10%" >新件数量</td>';
			str+='<td nowrap="true" width="10%" >单价</td>';
			str+='<td nowrap="true" width="10%" >金额（元）</td>';
			str+='<td nowrap="true" width="10%" >付费方式</td>';
			str+='<td nowrap="true" width="10%" >维修方式</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='	<input type="button"  name="bntAdd" value="添加" onclick="addPart(93331001);" class="normal_btn" />';
			str+='</td>';
			str+='</tr>';
		}
		if("93331002"==val){
			str+='<table border="1" id="tab_2" cellpadding="1" cellspacing="1" class="table_edit" width="100%"  style="text-align: center;">';
			str+='<th colspan="9"><input type="hidden" name="ro_type" value="93331002"><img class="nav" src="../jsp_new/img/subNav.gif" />免费保养&nbsp;&nbsp;<input type="button"  value="删除" onclick="delPart(this,93331002);" class="normal_btn" />&nbsp;&nbsp;&nbsp;&nbsp;<input type="button"  name="bntAdd" value="选择厂家保养项" onclick="chooseKeepFit();" class="long_btn" /></th>';
			str+='<tr>';
			str+='<td nowrap="true" width="10%" >维修配件</td>';
			str+='<td nowrap="true" width="15%" >新件代码</td>';
			str+='<td nowrap="true" width="15%" >新件名称</td>';
			str+='<td nowrap="true" width="10%" >新件数量</td>';
			str+='<td nowrap="true" width="10%" >单价</td>';
			str+='<td nowrap="true" width="10%" >金额（元）</td>';
			str+='<td nowrap="true" width="10%" >付费方式</td>';
			str+='<td nowrap="true" width="10%" >维修方式</td>';
			str+='<td nowrap="true" width="10%" >';
			str+=' 模板配件';
			str+='</td>';
			str+='</tr>';
		}
		if("93331003"==val){
			str+='<table border="1" id="tab_3" cellpadding="1" cellspacing="1" class="table_edit" width="100%"  style="text-align: center;">';
			str+='<th colspan="9"><input type="hidden" name="ro_type" value="93331003"><img class="nav" src="../jsp_new/img/subNav.gif" />自费保养&nbsp;&nbsp;<input type="button"  value="删除" onclick="delPart(this,93331003);" class="normal_btn" /></th>';
			str+='<tr>';
			str+='<td nowrap="true" width="10%" >维修配件</td>';
			str+='<td nowrap="true" width="15%" >新件代码</td>';
			str+='<td nowrap="true" width="15%" >新件名称</td>';
			str+='<td nowrap="true" width="10%" >新件数量</td>';
			str+='<td nowrap="true" width="10%" >单价</td>';
			str+='<td nowrap="true" width="10%" >金额（元）</td>';
			str+='<td nowrap="true" width="10%" >付费方式</td>';
			str+='<td nowrap="true" width="10%" >维修方式</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='	<input type="button"  name="bntAdd" value="添加" onclick="addPart(93331003);" class="normal_btn" />';
			str+='</td>';
			str+='</tr>';
		}
		str+='</table>';
		//工时====================================
		if("93331001"==val){
			str+='<table border="1" id="tab_1_1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">';
			str+='<tr>';
			str+='<td nowrap="true" width="10%" >维修工时</td>';
			str+='<td nowrap="true" width="15%" >作业代码</td>';
			str+='<td nowrap="true" width="25%" >作业名称</td>';
			str+='<td nowrap="true" width="10%" >工时定额</td>';
			str+='<td nowrap="true" width="10%" >工时单价</td>';
			str+='<td nowrap="true" width="10%" >金额（元）</td>';
			str+='<td nowrap="true" width="10%" >付费方式</td>';
			str+='<td nowrap="true" width="10%">';
			str+='<input type="button"  name="bntAdd" value="添加" onclick="addLabour(93331001);" class="normal_btn" />';
			str+='</td>';
			str+='</tr>';
		}
		if("93331002"==val){
			str+='<table border="1" id="tab_2_1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">';
			str+='<tr>';
			str+='<td nowrap="true" width="10%" >维修工时</td>';
			str+='<td nowrap="true" width="15%" >作业代码</td>';
			str+='<td nowrap="true" width="25%" >作业名称</td>';
			str+='<td nowrap="true" width="10%" >工时定额</td>';
			str+='<td nowrap="true" width="10%" >工时单价</td>';
			str+='<td nowrap="true" width="10%" >金额（元）</td>';
			str+='<td nowrap="true" width="10%" >付费方式</td>';
			str+='<td nowrap="true" width="10%">';
			str+='模板工时';
			str+='</td>';
			str+='</tr>';
		}
		if("93331003"==val){
			str+='<table border="1" id="tab_3_1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">';
			str+='<tr>';
			str+='<td nowrap="true" width="10%" >维修工时</td>';
			str+='<td nowrap="true" width="15%" >作业代码</td>';
			str+='<td nowrap="true" width="25%" >作业名称</td>';
			str+='<td nowrap="true" width="10%" >工时定额</td>';
			str+='<td nowrap="true" width="10%" >工时单价</td>';
			str+='<td nowrap="true" width="10%" >金额（元）</td>';
			str+='<td nowrap="true" width="10%" >付费方式</td>';
			str+='<td nowrap="true" width="10%">';
			str+='<input type="button"  name="bntAdd" value="添加" onclick="addLabour(93331003);" class="normal_btn" />';
			str+='</td>';
			str+='</tr>';
		}
		str+='</table>';
		div.append(str);
	}
	function backKeepFitData(labours2,parts2){
		var tab=$("#tab_2");
		var str="";
		for(var i = 0;i < parts2.length;i++){
			str+='<tr>';
			str+='<td nowrap="true" width="10%" >配件>></td>';
			str+='<td nowrap="true" width="15%" >';
			str+='<input name="part_id_2" type="hidden" class="middle_txt" value="'+parts2[i].REAL_PART_ID+'"/>';
			str+='<input name="part_code_2" readonly="readonly"  value="'+parts2[i].PART_CODE+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="15%" >';
			str+='<input name="part_name_2" readonly="readonly"value="'+parts2[i].PART_NAME+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input name="part_quotiety_2" readonly="readonly"  size="10"  value="'+parts2[i].PART_NUM+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input name="claim_price_param_2" readonly="readonly" size="10" value="'+parts2[i].PRICE+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input readonly="readonly" name="part_amont_2"  size="10" value="'+parts2[i].AMOUNT+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" ><select class="min_sel" name="pay_type_2">';
			if("11801002"==parts2[i].PAY_TYPE){
				str+='<option value="11801002">索赔</option>';
			}else{
				str+='<option value="11801001">自费</option>';
			}
			str+='</select></td>';
			str+='<td nowrap="true" width="10%" ><select class="min_sel" name="part_use_type_2">';
			if("95431002"==parts2[i].PART_USE_TYPE){
				str+='<option value="95431002">更换</option>';
			}else{
				str+='<option value="95431001">维修</option>';
			}
			str+='</select></td>';
			str+='<td nowrap="true" width="10%" >';
			str+='</td></tr>';
		}
		tab.append(str);
		var tab1=$("#tab_2_1");
		var str1="";
		for(var j = 0;j < labours2.length;j++){
			str1+='<tr>';
			str1+='<td nowrap="true" width="10%" >工时>></td>';
			str1+='<td nowrap="true" width="15%" >';
			str1+='<input readonly="readonly" name="labour_code_2"  value="'+labours2[j].LABOUR_CODE+'"/>';
			str1+='</td>';
			str1+='<td nowrap="true" width="25%" >';
			str1+='<input name="cn_des_2" readonly="readonly" size="35" value="'+labours2[j].LABOUR_NAME+'"/>';
			str1+='</td>';
			str1+='<td nowrap="true" width="10%" >';
			str1+='<input name="labour_quotiety_2" readonly="readonly" size="10" value="'+labours2[j].LABOUR_NUM+'"/>';
			str1+='</td>';
			str1+='<td nowrap="true" width="10%" >';
			str1+='<input name="parameter_value_2" size="10" readonly="readonly" value="'+labours2[j].PRICE+'"/>';
			str1+='</td>';
			str1+='<td nowrap="true" width="10%" >';
			str1+='<input name="labour_fix_2" readonly="readonly" size="10" value="'+labours2[j].AMOUNT+'"/>';
			str1+='</td>';
			str1+='<td nowrap="true" width="10%"  >';
			str1+='<select class="min_sel" name="pay_type_labour_2">';
			if("11801002"==labours2[j].PAY_TYPE){
				str1+='  <option value="11801002">索赔</option>';
			}else{
				str1+=' <option value="11801001">自费</option>';
			}
			str1+='</select>';
			str1+='</td>';
			str1+='<td nowrap="true" width="10%">';
			str1+='</td>';
			str1+='</tr>';
		}
		tab1.append(str1);
	}
	function addLabour(val){
		var wrgroup_id=$("#wrgroup_id").val();
		var package_id=$("#package_id").val();
		var labour_codes_1="";
		$("input[name='labour_code_1']").each(function(){
			labour_codes_1+=$(this).val()+",";
		});
		var labour_codes_3="";
		$("input[name='labour_code_3']").each(function(){
			labour_codes_3+=$(this).val()+",";
		});
		OpenHtmlWindow('<%=contextPath%>/OrderAction/addLabour.do?wrgroup_id='+wrgroup_id+'&package_id='+package_id+'&val='+val+'&labour_codes_1='+labour_codes_1+'&labour_codes_3='+labour_codes_3,850,550);
	}
	function addPart(val){
		var series_id=$("#series_id").val();
		var model_id=$("#model_id").val();
		var part_id_1="";
		$("input[name='part_id_1']").each(function(){
			part_id_1+=$(this).val()+",";
		});
		var part_id_3="";
		$("input[name='part_id_3']").each(function(){
			part_id_3+=$(this).val()+",";
		});
		OpenHtmlWindow('<%=contextPath%>/OrderAction/addPart.do?model_id='+model_id+'&series_id='+series_id+'&val='+val+'&part_id_1='+part_id_1+'&part_id_3='+part_id_3,850,550);
	}
	function delPart(obj,val){
		if("93331001"==val){
			$("#tab_1").remove();
			$("#tab_1_1").remove();
			//记得移除配件代码信息
			$("select[name='accessoriesOutMainPart']").each(function(){
				 $(this).empty(); 
			});
		}
		if("93331002"==val){
			$("#tab_2").remove();
			$("#tab_2_1").remove();
		}
		if("93331003"==val){
			$("#tab_3").remove();
			$("#tab_3_1").remove();
		}
		if("4"==val){
			$("#accessories").remove();
			$("#bntAcc").attr("disabled",false);
		}
	}
	$(function(){
		var type=$("#type").val();
		$("#noBalance,#balance,#auditPass,#auditUnPass").hide();
		if("add"==type){
			$("#ro_no").val(" 新增保存后自动生成 ");
			$("#warning").val(" 新增保存后自动判断等级 ");
			$("#btn1,#btn3").remove();
			$("#vin").live("blur",function(){
				var vin=$("#vin").val();
				if(vin!=""){
					sendAjax('<%=contextPath%>/OrderAction/showInfoByVin.json?vin='+vin,backByVin,'fm');
				}
			});
			$("#license_no").live("blur",function(){
				var license_no=$("#license_no").val();
				if(license_no!=""){
					OpenHtmlWindow('<%=contextPath%>/OrderAction/showInfoBylicenseNo.do?license_no='+license_no,800,500);
				}
			});
		}
		if("update"==type){
			$("#vin").attr("readonly","readonly");
			var acc=$("select[name='accessoriesOutMainPart']");
			if(acc.length>0){
				$("#bntAcc").attr("disabled",true);
			}
		}
		if("view"==type){
			$(".hideClass").hide();
			$("input[type='button']").attr("disabled",true);
			$("select").attr("disabled",true);
			$("input[type='text']").attr("readonly","readonly");
			$("#btn1,#btn3,#back").attr("disabled",false);
			$("#time_show").show();
			$("#repairText").attr("readonly","readonly");
		}
		if("balance"==type){
			$(".hideClass").hide();
			$("input[type='button']").attr("disabled",true);
			$("select").attr("disabled",true);
			$("input[type='text']").attr("readonly","readonly");
			$("#btn1,#btn3,#back").attr("disabled",false);
			$("#time_show").show();
			$("#repairText").attr("readonly","readonly");
			$("#balance").show();
			$("#balance").attr("disabled",false);
		}
		if("cancelbalance"==type){
			$(".hideClass").hide();
			$("input[type='button']").attr("disabled",true);
			$("select").attr("disabled",true);
			$("input[type='text']").attr("readonly","readonly");
			$("#btn1,#btn3,#back").attr("disabled",false);
			$("#time_show").show();
			$("#repairText").attr("readonly","readonly");
			$("#noBalance").show();
			$("#noBalance").attr("disabled",false);
		}if("audit"==type){
			$(".hideClass").hide();
			$("input[type='button']").attr("disabled",true);
			$("select").attr("disabled",true);
			$("input[type='text']").attr("readonly","readonly");
			$("#btn1,#btn3,#back").attr("disabled",false);
			$("#time_show").show();
			$("#repairText").attr("readonly","readonly");
			$("#auditPass,#auditUnPass").show();
			$("#auditPass,#auditUnPass").attr("disabled",false);
		}
	});
	function backBylicenseNo(engine_no,vin){
		$("#vin").val(vin);
		$("#engine_no").val(engine_no);
		sendAjax('<%=contextPath%>/OrderAction/showInfoByVin.json?vin='+vin,backByVin,'fm');
	}
	//设置添加配件
	function setMainPartCode(part_id,part_code,part_name,claim_price_param,val){
		if(93331001==val){
			var tab=$("#tab_1");
			var str="";
			str+='<tr>';
			str+='<td nowrap="true" width="10%" >配件>></td>';
			str+='<td nowrap="true" width="15%" >';
			str+='<input name="part_id_1" type="hidden" class="middle_txt" value="'+part_id+'"/>';
			str+='<input name="part_code_1" readonly="readonly"  value="'+part_code+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="15%" >';
			str+='<input name="part_name_1" readonly="readonly"  value="'+part_name+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input name="part_quotiety_1" onblur="insertNum(this);" style="color: green;" size="10"  value="1"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input name="claim_price_param_1" readonly="readonly"  size="10" value="'+claim_price_param+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input readonly="readonly" name="part_amont_1" size="10" value="'+claim_price_param+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" ><select class="min_sel" name="pay_type_1">';
			str+='<option value="11801002">索赔</option>';
			str+='<option value="11801001">自费</option>';
			str+='</select></td>';
			str+='<td nowrap="true" width="10%" ><select class="min_sel" name="part_use_type_1">';
			str+='<option value="95431002">更换</option>';
			str+='<option value="95431001">维修</option>';
			str+='</select></td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input type="button"  name="bntDel" value="删除" onclick="deleteTrByNormal(this);" class="normal_btn" />';
			str+='</td></tr>';
			tab.append(str);
		}
		if(93331003==val){
			var tab=$("#tab_3");
			var str="";
			str+='<tr>';
			str+='<td nowrap="true" width="10%" >配件>></td>';
			str+='<td nowrap="true" width="15%" >';
			str+='<input name="part_id_3" type="hidden" class="middle_txt" value="'+part_id+'"/>';
			str+='<input name="part_code_3" readonly="readonly"  value="'+part_code+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="15%" >';
			str+='<input name="part_name_3" readonly="readonly"value="'+part_name+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input name="part_quotiety_3" onblur="insertNum(this);" style="color: green;" size="10"  value="1"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input name="claim_price_param_3" readonly="readonly" size="10" value="'+claim_price_param+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input readonly="readonly" name="part_amont_3"  size="10" value="'+claim_price_param+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" ><select class="min_sel" name="pay_type_3">';
			//str+='<option value="11801002">索赔</option>';
			str+='<option value="11801001">自费</option>';
			str+='</select></td>';
			str+='<td nowrap="true" width="10%" ><select class="min_sel" name="part_use_type_3">';
			str+='<option value="95431002">更换</option>';
			str+='<option value="95431001">维修</option>';
			str+='</select></td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input type="button"  name="bntDel" value="删除" onclick="deleteTr(this);" class="normal_btn" />';
			str+='</td></tr>';
			tab.append(str);
		}
	}
	function insertNum(obj){
		var reg = /^\d+$/;
		var val=$(obj).val();
		if(""!=val && !reg.test(val)){
			MyAlert("提示：请输入正整数填入新件数量！");
			$(obj).val("1"); 
			$(obj).focus(); 
		}else{
			var tr=$(obj).parent().parent();
			var nextInput=tr.children().eq(4).children();
			var nextToNextInput=tr.children().eq(5).children();
			var nextPrice=nextInput.val();
			nextToNextInput.val(nextPrice*1000*val/1000);
		}
	}
	//设置添加工时
	function setLabourCode(labour_code,cn_des,labour_quotiety,labour_fix,parameter_value,val){
		if(93331001==val){
			var tab=$("#tab_1_1");
			var str="";
			str+='<tr>';
			str+='<td nowrap="true" width="10%" >工时>></td>';
			str+='<td nowrap="true" width="15%" >';
			str+='<input readonly="readonly" name="labour_code_1"  value="'+labour_code+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="25%" >';
			str+='<input name="cn_des_1" readonly="readonly" size="35" value="'+cn_des+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input name="labour_quotiety_1" readonly="readonly" size="10" value="'+labour_fix+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input name="parameter_value_1" size="10" readonly="readonly" value="'+parameter_value+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input name="labour_fix_1" readonly="readonly" size="10" value="'+(labour_fix*parameter_value).toFixed(1)+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%"  >';
			str+='<select class="min_sel" name="pay_type_labour_1">';
			str+='  <option value="11801002">索赔</option>';
			str+=' <option value="11801001">自费</option>';
			str+='</select>';
			str+='</td>';
			str+='<td nowrap="true" width="10%">';
			str+='<input type="button"  name="bntDel" value="删除" onclick="deleteTr(this);" class="normal_btn" />';
			str+='</td>';
			str+='</tr>';
			tab.append(str);
		}
		if(93331003==val){
			var tab=$("#tab_3_1");
			var str="";
			str+='<tr>';
			str+='<td nowrap="true" width="10%" >工时>></td>';
			str+='<td nowrap="true" width="15%" >';
			str+='<input readonly="readonly" name="labour_code_3"  value="'+labour_code+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="25%" >';
			str+='<input name="cn_des_3" readonly="readonly" size="35" value="'+cn_des+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input name="labour_quotiety_3" readonly="readonly" size="10" value="'+labour_fix+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input name="parameter_value_3" size="10" readonly="readonly" value="'+parameter_value+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input name="labour_fix_3" readonly="readonly" size="10" value="'+(labour_fix*parameter_value).toFixed(1)+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%"  >';
			str+='<select class="min_sel" name="pay_type_labour_3">';
			str+=' <option value="11801001">自费</option>';
			//str+='  <option value="11801002">索赔</option>';
			str+='</select>';
			str+='</td>';
			str+='<td nowrap="true" width="10%">';
			str+='<input type="button"  name="bntDel" value="删除" onclick="deleteTr(this);" class="normal_btn" />';
			str+='</td>';
			str+='</tr>';
			tab.append(str);
		}
	}
	function ro_acc_add(){
		var checkPartCode=$("input[name='part_code_1']").length;
		if(checkPartCode==0){
			MyAlert("提示：正常维修的配件代码未添加，请添加后再选择添加辅料！");
			return;
		}
		$("#bntAcc").attr("disabled",true);
		var div=$('#new_acc_add');
		var str="";
		str+='<table id="accessories" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">';
		str+='<th colspan="8">';
		str+='	<img class="nav" src="../jsp_new/img/subNav.gif" />辅料项目&nbsp;&nbsp;<input type="button" class="normal_btn" value="删除" onclick="delPart(this,4);"';
		str+='</th>';
		str+='<tr>';
		str+='	<td nowrap="true" width="20%" >辅料代码</td>';
		str+='	<td nowrap="true" width="20%" >辅料名称</td>';
		str+='	<td nowrap="true" width="20%" >辅料费用</td>';
		str+='<td nowrap="true" width="20%" >关联配件</td>';
		str+='<td nowrap="true" width="20%" > <input type="button"  name="bntAdd" value="添加" onclick="addAccessories();" class="normal_btn" /></td>';
		str+='</tr>';
		str+='</table>';
		str+='<br>';
		div.append(str);
	}
	function addAccessories(){
		var checkPartCode=$("input[name='part_code_1']").length;
		if(checkPartCode==0){
			MyAlert("提示：操作有误！正常维修的配件代码未添加，请不要添加后删除正常维修的配件代码再添加辅料！");
			$("#accessories").remove();
			$("#bntAcc").attr("disabled",false);
			return;
		}
		OpenHtmlWindow('<%=contextPath%>/OrderAction/accList.do',800,500);
	}
	//取到当前为正常维修的配件代码数据，并先清空再循环插入下拉里面
	var partCodes=null;
	function getPartCode(){
		$("select[name='accessoriesOutMainPart']").each(function(){
				 $(this).empty(); 
		});
		partCodes=new Array();
		$("input[name='part_code_1']").each(function(){
			var part_code=$(this).val();
			partCodes.push(part_code);
		});
		var strJ="";
		for(var i=0;i<partCodes.length;i++){
			strJ+="<option value='"+partCodes[i]+"'>"+partCodes[i]+"</option>";
		}
		$("select[name='accessoriesOutMainPart']").each(function(){
			$(this).append(strJ); 
		});
	}
	function deleteTr(obj){
		$(obj).parent().parent().remove(); 
	}
	function deleteTrByNormal(obj){//单独为正常维修的配件代码做删除，目的为了不让辅料代码有BUG
		$(obj).parent().parent().remove(); 
		//记得移除配件代码信息
		$("select[name='accessoriesOutMainPart']").each(function(){
			 $(this).empty(); 
		});
	}
	//维修历史按钮方法
	function maintaimHistory(){
		var vin = $("#vin").val() ;
		window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/maintaimHistory.do?VIN='+vin);
	}
	//保养历史按钮方法
	function freeMaintainHistory(){
		var vin = $("#vin").val() ;
		window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/freeMaintainHistory.do?VIN='+vin);
	}
	function sureInsert(){
		var vin = $("#vin").val() ;
		var repairText=$("#repairText").val();
		var deliverer=$("#deliverer").val();
		var deliverer_phone=$("#deliverer_phone").val();
		var in_mileage=$("#in_mileage").val();
		if(""==vin){
			MyAlert("提示：请填写VIN并带出信息！");
			return;
		}
		if(""==in_mileage){
			MyAlert("提示：请填写行驶里程！");
			return;
		}
		if($.trim(deliverer)==""){
			MyAlert("提示：请填写送修人姓名！");
			$("#deliverer").val("");
			return;
		}
		if($.trim(deliverer_phone)==""){
			MyAlert("提示：请填写送修人电话！");
			$("#deliverer_phone").val("");
			return;
		}
		if($.trim(repairText)=="" || $.trim(repairText).length>1000){
			MyAlert("提示：请填写维修项目内容！");
			$("#repairText").val("");
			return;
		}
		var temp=0;
		var part_quotiety_1=$("input[name='part_quotiety_1']");
		var part_quotiety_3=$("input[name='part_quotiety_3']");
		var part_quotiety_2=$("input[name='part_quotiety_2']");
		if(part_quotiety_1.length==0 && part_quotiety_3.length==0 && part_quotiety_2.length==0 ){
			MyAlert("提示：请至少新增一个维修类型,并添加维修一个配件！");
			temp++;
			return;
		}
		if(part_quotiety_1.length>0 ){
			$(part_quotiety_1).each(
				function(){
					if(""==$(this).val()){
						MyAlert("提示：请填写正常维修的新件数量！");
						temp++;
						return;
					}
			});
		}
		if(part_quotiety_3.length>0 ){
			$(part_quotiety_3).each(function(){
					if(""==$(this).val()){
						MyAlert("提示：请填写自费保养的新件数量！");
						temp++;
						return;
					}
			});
		}
		var acc=$("select[name='accessoriesOutMainPart']");
		if(acc.length>0 ){
			$(acc).each(function(){
				if(null==$(this).val()||""==$(this).val()){
					MyAlert("提示：请选择关联配件,并与正常维修里的配件代码关联！");
					temp++;
					return;
				}
			});
		}
		if(temp==0){
			MyConfirm("是否确认保存？",roInsertSure,"");
		}
		
	}
	function roInsertSure(){
		var url="<%=contextPath%>/OrderAction/roInsert.do";
		$("#fm").attr("action",url);
		$("#fm").submit();
	}
	function backByVin(json){
		if(json.flag==true){
			if(null!=json.res&&""!=json.res){
				MyAlert(json.res);//提示服务活动未做的！
			}
			var t=json.info;
			if(null==t){
				$("#main_phone").val("");
				$("#ctm_name").val("");
				$("#deliverer_adress").val("");
				$("#in_mileage").val("");
				$("#color").val("");
				$("#engine_no").val("");
				$("#car_use_desc").val("");
				$("#car_use_type").val("");
				$("#guarantee_date").val("");
				$("#package_name").val("");
				$("#free_times").val("");
				$("#model_name").val("");
				$("#model").val("");
				$("#model_id").val("");
				$("#package_id").val("");
				$("#wrgroup_id").val("");
				$("#series_id").val("");
				$("#license_no").val("");
			}else{
				$("#main_phone").val(t.MAIN_PHONE);
				$("#ctm_name").val(t.CUSTOMER_NAME);
				$("#deliverer_adress").val(t.ADDRESS);
				//$("#in_mileage").val(t.MILEAGE);
				$("#color").val(t.COLOR);
				if(""!=$("#engine_no").val()){
					$("#engine_no").val(engine_no);
				}
				$("#engine_no").val(t.ENGINE_NO);
				$("#car_use_desc").val(t.CAR_USE_DESC);
				$("#car_use_type").val(t.CAR_USE_TYPE);
				$("#guarantee_date").val(t.PURCHASED_DATE_ACT);
				$("#package_name").val(t.PACKAGE_NAME);
				$("#free_times").val(t.FREE_TIMES);
				$("#model_name").val(t.MODEL_NAME);
				$("#model").val(t.MODEL);
				$("#model_id").val(t.MODEL_ID);
				$("#package_id").val(t.PACKAGE_ID);
				$("#wrgroup_id").val(t.WRGROUP_ID);
				$("#series_id").val(t.SERIES_ID);
				$("#license_no").val(t.LICENSE_NO);
			}
		}else{
			MyAlert("提示：系统中同一个VIN号只能存在一张未结算状态的工单!");
			$("#vin").val("");
		}
	}
	function ro_type_count(){
		var count_part=0.0;
		var part_amont_1=$("input[name='part_amont_1']");
		if(part_amont_1.length>0 ){
			$(part_amont_1).each(
				function(){
					count_part+=parseFloat($(this).val());
				});
		}
		var part_amont_2=$("input[name='part_amont_2']");
		if(part_amont_2.length>0 ){
			$(part_amont_2).each(
				function(){
					count_part+=parseFloat($(this).val());
				});
		}
		var part_amont_3=$("input[name='part_amont_3']");
		if(part_amont_3.length>0 ){
			$(part_amont_3).each(
				function(){
					count_part+=parseFloat($(this).val());
				});
		}
		var count_labour=0.0;
		var labour_fix_1=$("input[name='labour_fix_1']");
		if(labour_fix_1.length>0 ){
			$(labour_fix_1).each(
				function(){
					count_labour+=parseFloat($(this).val());
				});
		}
		var labour_fix_2=$("input[name='labour_fix_2']");
		if(labour_fix_2.length>0 ){
			$(labour_fix_2).each(
				function(){
					count_labour+=parseFloat($(this).val());
				});
		}
		var labour_fix_3=$("input[name='labour_fix_3']");
		if(labour_fix_3.length>0 ){
			$(labour_fix_3).each(
				function(){
					count_labour+=parseFloat($(this).val());
				});
		}
		var accessoriesPrice=$("input[name='accessoriesPrice']");
		if(accessoriesPrice.length>0 ){
			$(accessoriesPrice).each(function(){
					count_labour+=parseFloat($(this).val());
				});
		}
		var count_all=(count_part*1000/1000+count_labour*1000/1000);
		$("#count_span").text(count_all.toFixed(2));
	}
	function setAcc(workhour_code,workhour_name,price){
		var tab=$("#accessories");
		var str='<tr><td nowrap="true" width="20%" >';
		str+='<input readonly="readonly" name="workHourCode" class="middle_txt" value="'+workhour_code+'"/>';
		str+='</td><td nowrap="true" width="20%" >';
		str+='<input readonly="readonly" name="workhour_name" class="middle_txt" value="'+workhour_name+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="20%" >';
		str+='<input readonly="readonly" name="accessoriesPrice" class="middle_txt" value="'+price+'"/>';
		str+='</td><td nowrap="true" width="20%" >';
		str+='<select class="short_sel" name="accessoriesOutMainPart" ></select>&nbsp;<a href="#" onclick="getPartCode();" >点击获取</a>';
		str+='</td>';
		str+='<td nowrap="true" width="20%" >';
		str+='<input type="button"  name="bntDel" value="删除" onclick="deleteTr(this);" class="normal_btn" />';
		str+='</td></tr>';
		tab.append(str);
		getPartCode();
	}

	function roDoCancelBalance(){
		MyConfirm("是否取消确认结算？",cancelBalance,"");
	}
	function doRepairBalance(){
		var dt = document.getElementById("for_balance_time").value;
		var create_time = document.getElementById("ro_create_date").value;
		if("undefined" == typeof dt || dt==""){
			MyAlert("请先填写 结算时间!");
			return;
		}
		if(create_time>dt){
			MyAlert("工单结算时间只能在工单开始时间以后!");
			return;
		}
		MyConfirm("是否确认结算？",roDoBalance,"");
	}

	function roDoBalance(){
		var url="<%=contextPath%>/OrderBalanceAction/roBalance.do";
		$("#fm").attr("action",url);
		$("#fm").submit();
	}
	function cancelBalance(){
		var id = document.getElementById("id").value;
		var roNo = document.getElementById("ro_no").value;
		sendAjax('<%=contextPath%>/OrderBalanceAction/roCancelBalance.json?id='+id+'&ro_no='+roNo,auditBackInfo,'fm');
	}

	function roDoCancelBalanceAudit(type){
		if(type=="1"){
			MyConfirm("是否审核通过取消结算申请？",roBalanceAuditPass,"");
		}else if(type=="2"){
			MyConfirm("是否驳回取消结算申请？",roBalanceAuditUnPass,"");
		}
	}

	function roBalanceAuditPass(){
		var id = document.getElementById("id").value;
		var roNo = document.getElementById("ro_no").value;
		sendAjax('<%=contextPath%>/OrderBalanceAction/roCancelBalanceAudit.json?id='+id+'&ro_no='+roNo+"&check_status=1",auditBackInfo,'fm');
	}
	function roBalanceAuditUnPass(){
		var id = document.getElementById("id").value;
		var roNo = document.getElementById("ro_no").value;
		sendAjax('<%=contextPath%>/OrderBalanceAction/roCancelBalanceAudit.json?id='+id+'&ro_no='+roNo+"&check_status=2",auditBackInfo,'fm');
	}

	function auditBackInfo(json){
		if (json.Exception) {
			MyUnCloseAlert(json.Exception.message, enableBtns);
		} else {
			MyUnCloseAlert(json.info, function(){
				window.location.href = g_webAppName + '/OrderBalanceAction/roCancelAuditInit.do';
			});
		}
	}
	function chooseKeepFit(){
		OpenHtmlWindow('<%=contextPath%>/OrderAction/keepFitTemplate.do',800,400);
	}
	function checkMileage(obj){
		var vin=$("#vin").val();
		var reg = /^\d+$/;
		var val=$(obj).val();
		var temp=0;
		if(""==$("#vin").val()){
			MyAlert("提示：请先输入VIN，再输入里程！");
			$(obj).val("");
			temp++;
			return;
		}
		if(""!=val && !reg.test(val)){
			MyAlert("提示：请输入正确的数据！");
			$(obj).val("");
			temp++;
			return;
		}
		if(temp==0){
			sendAjax('<%=contextPath%>/OrderAction/checkMileage.json?vin='+vin+'&mileage='+obj.value,checkMileageBack,'fm');
		}
	}
	function checkMileageBack(json){
		if(json.res!=""){
			MyAlert(json.res);
			$("#in_mileage").val("");
		}
	}
	function ro_normal_count(){
		var count_part=0.0;
		var part_amont_1=$("input[name='part_amont_1']");
		if(part_amont_1.length>0 ){
			$(part_amont_1).each(
				function(){
					count_part+=parseFloat($(this).val());
				});
		}
		var count_labour=0.0;
		var labour_fix_1=$("input[name='labour_fix_1']");
		if(labour_fix_1.length>0 ){
			$(labour_fix_1).each(
				function(){
					count_labour+=parseFloat($(this).val());
				});
		}
		
		var accessoriesPrice=$("input[name='accessoriesPrice']");
		if(accessoriesPrice.length>0 ){
			$(accessoriesPrice).each(function(){
					count_labour+=parseFloat($(this).val());
				});
		}
		var count_all=(count_part*1000/1000+count_labour*1000/1000);
		$("#ro_normal_count").text("共："+count_all.toFixed(2)+"元");
	}
	function ro_free_count(){
		var count_part=0.0;
		var part_amont_3=$("input[name='part_amont_3']");
		if(part_amont_3.length>0 ){
			$(part_amont_3).each(
				function(){
					count_part+=parseFloat($(this).val());
				});
		}
		var count_labour=0.0;
		var labour_fix_3=$("input[name='labour_fix_3']");
		if(labour_fix_3.length>0 ){
			$(labour_fix_3).each(
				function(){
					count_labour+=parseFloat($(this).val());
				});
		}
		var count_all=(count_part*1000/1000+count_labour*1000/1000);
		$("#ro_free_count").text("共："+count_all.toFixed(2)+"元");
	}
	//改单价数据重新算金额，根据为自费的进行修改
	function changePrice(obj){
		var val=$(obj).val();
		var reg = /^[0-9]+([.]{1}[0-9]{1,2})?$/;
		if(""!=val && !reg.test(val)){
			MyAlert("提示：请输入正确的数据！");
			$(obj).val($(obj).attr("tempVal"));
			return;
		}
		var tr=$(obj).parent().parent();
		var num=tr.children("td:eq(3)").find('input');
		var price=tr.children("td:eq(4)").find('input');
		var amount=tr.children("td:eq(5)").find('input');
		var valTemp=(parseFloat($(price).val())*parseFloat($(num).val())*1000/1000).toFixed(2);
		$(amount).val(valTemp);
	}
	//改配件的单价
	function changeInputByFree(obj){
		var tr=$(obj).parent().parent();
		var val=$(obj).val();
		var price=tr.children("td:eq(4)").find('input');
		if(val==11801002){
			$(price).attr("readonly","readonly").css("color","");
			$(price).val($(price).attr("tempVal"));
		}else{
			$(price).removeAttr("readonly").css("color","green");
			MyAlert("提示：当前付费方式为自费，可以修改当列的单价，进行重新计算金额！");
		}
	}
	//改工时的单价
	function changeInputByFreeL(obj){
		var tr=$(obj).parent().parent();
		var val=$(obj).val();
		var price=tr.children("td:eq(4)").find('input');
		if(val==11801002){
			$(price).attr("readonly","readonly").css("color","");
			$(price).val($(price).attr("tempVal"));
		}else{
			$(price).removeAttr("readonly").css("color","green");
			MyAlert("提示：当前付费方式为自费，可以修改当列的单价，进行重新计算金额！");
		}
	}
</script>
</head>
<body>
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;维修登记&gt;维修工单管理
</div>
<form name="fm" id="fm" method="post">
<input class="middle_txt" id="type" value="${type }" name="type" type="hidden"  />
<input class="middle_txt" id="id" value="${ro.ID }" name="id" type="hidden"  />
<input class="middle_txt" id="brand_name" value="" name="brand_name" type="hidden"  />
<input class="middle_txt" id="engine_no" value="${ ro.ENGINE_NO }" name="engine_no" type="hidden"  />
<input class="middle_txt" id="series_name" value="" name="series_name" type="hidden" />
<input class="middle_txt" id="model_id" value="${ro.RO_MODEL_ID }" name="model_id" type="hidden"  />
<input class="middle_txt" id="model" value="" name="model" type="hidden"  />
<input class="middle_txt" id="package_id" value="${ro.RO_PACKAGE_ID }" name="package_id" type="hidden"  />
<input class="middle_txt" id="wrgroup_id" value="${ro.WRGROUP_ID }" name="wrgroup_id" type="hidden"  />
<input class="middle_txt" id="series_id" value="${ro.SERIES }" name="series_id" type="hidden"  /> 
<input class="middle_txt" id="car_use_type" value="" name="car_use_type" type="hidden"  /> 
<input class="middle_txt" id="dealer_shortname" value="${userInfo.DEALER_SHORTNAME }"  name="dealer_shortname" type="hidden"  />
<input class="middle_txt" id="dealer_code" value="${userInfo.DEALER_CODE }"  name="dealer_code" type="hidden"  />
<input class="middle_txt" id="user_name" value="${userInfo.NAME }"  name="user_name" type="hidden"  />

<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
	<th colspan="8">
		<img class="nav" src="../jsp_new/img/subNav.gif"/>基本信息
	</th>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >工单号：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="ro_no"  value="${ro.RO_NO }" readonly="readonly" name="ro_no" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" >车型：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="model_name" value="${ro.MODEL_NAME }" readonly="readonly" name="model_name" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" >颜色：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="color" value="${ro.COLOR }" readonly="readonly" name="color" type="text" maxlength="30" />
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" ><span class="shouldFill" style="color: red;">*</span>VIN：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="vin" value="${ro.VIN }"  name="vin" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" >配置：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="package_name" value="${ro.PACKAGE_NAME }" readonly="readonly" name="package_name" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" >车辆用途：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="car_use_desc" value="${ro.CAR_USE_DESC }" readonly="readonly" name="car_use_desc" type="text" maxlength="30" />
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >车牌号：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="license_no" value="${ro.LICENSE }"  name="license_no" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" >三包预警：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="warning" value="${ro.WARNING_LEVEL }" readonly="readonly" name="warning" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" >购车日期：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="guarantee_date"  value="<fmt:formatDate value="${ro.GUARANTEE_DATE }" pattern="yyyy-MM-dd"/>" readonly="readonly" name="guarantee_date" type="text" maxlength="30" />
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" ><span class="shouldFill" style="color: red;">*</span>行驶里程：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="in_mileage" value="${ro.IN_MILEAGE }" onblur="checkMileage(this);"  name="in_mileage" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" >保养次数:</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="free_times" value="${ro.FREE_TIMES }" readonly="readonly" name="free_times" type="text" maxlength="30" />
    	</td>
		
    	<td width="12.5%"></td>
	</tr>
	<tr id="time_show" style="display: none;">
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >工单开始时间：</td>
    	<td nowrap="true" width="15%" >
			<input name="ro_create_date" type="text" id="ro_create_date" value="<fmt:formatDate value="${ro.RO_CREATE_DATE }" pattern='yyyy-MM-dd HH:mm'/>" readonly="readonly" class="middle_txt"/>
		</td>
	    <td nowrap="true" width="10%" >工单结算时间：</td>
    	<td nowrap="true" width="15%" >
			<input name="for_balance_time" type="text" id="for_balance_time"  onfocus="calendar();" value="${ro.FOR_BALANCE_TIME }" readonly="readonly" class="middle_txt"/>
    	</td>
    	<td width="12.5%"></td>
	</tr>
</table>
<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
	<th colspan="8">
		<img class="nav" src="../jsp_new/img/subNav.gif" />用户信息
	</th>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >车主姓名：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="ctm_name" value="${ro.OWNER_NAME }" readonly="readonly" name="ctm_name" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" >车主电话：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="main_phone" value="${ro.OWNER_PHONE }" readonly="readonly" name="main_phone" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" >车主地址:</td>
    	<td nowrap="true" width="15%"  >
    		<input class="long_txt"  id="deliverer_adress" value="${ro.DELIVERER_ADRESS }" readonly="readonly" name="deliverer_adress" type="text" maxlength="30" />
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" ><span class="shouldFill" style="color: red;">*</span>送修人姓名：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="deliverer" value="${ro.DELIVERER }" name="deliverer" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" ><span class="shouldFill" style="color: red;">*</span>送修人电话：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="deliverer_phone" value="${ro.DELIVERER_PHONE }"  name="deliverer_phone" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" ></td>
    	<td nowrap="true" width="15%" >
    	</td>
    	<td width="12.5%"></td>
	</tr>
</table>
<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
	<th colspan="8">
		<img class="nav" src="../jsp_new/img/subNav.gif" />报修项目
	</th>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" ></td>
    	<td nowrap="true" width="15%" >
    	</td>
		<td nowrap="true" width="10%" ></td>
    	<td nowrap="true" width="15%" >
    	</td>
		<td nowrap="true" width="10%" ></td>
    	<td nowrap="true" width="15%" >
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td nowrap="true" width="100%">
			<textarea name="repairText" id="repairText" rows="3" style="width: 80%;">${ro.REMARKS }</textarea><span class="shouldFill" style="color: red;">*</span>
		</td>
	</tr>
</table>
<br>
			<div  style="text-align: center; width: 100%;white-space: normal;">
			<span class="hideClass">
    		<span style="color: red;">温馨提示：新增配件工时，请选择新增按钮>> </span><input type="button"  name="bntAdd"  id="bntAdd"  value="新增" onclick="ro_type_add();" class="normal_btn" />
    		&nbsp;&nbsp;<span style="color: red;">费用合计: </span><span style="color: red; font-weight: bold;" id="count_span">${ro.BALANCE_AMOUNT }</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button"  name="bntCount"  id="bntCount"  value="合计" onclick="ro_type_count();" class="normal_btn" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;提示：[点击合计可以计算当前页面的总费用]
    		&nbsp;&nbsp;<span style="color: red;">添加辅料请点>></span>&nbsp;&nbsp;<input type="button"  name="bntAcc"  id="bntAcc"  value="辅料添加" onclick="ro_acc_add();" class="normal_btn" />
    		</div>
    		</span>
    		<br/>
    		<div id="new_add"  style="text-align: center; width: 100%;">
    			<c:if test="${parts1!=null}">
	    		<table border="1" id="tab_1" cellpadding="1" cellspacing="1" class="table_edit" width="100%"  style="text-align: center;">
					<th colspan="7" nowrap="true" width="80%" ><input type="hidden" name="ro_type" value="93331001">
					<img class="nav" src="../jsp_new/img/subNav.gif" />正常维修&nbsp;&nbsp;
					<input type="button"  value="删除" onclick="delPart(this,93331001);" class="normal_btn" />
					</th>
					<th  nowrap="true" style="text-align: center;" width="10%" >
					<span style="color: red; font-weight: bold;" id="ro_normal_count"></span>
						&nbsp;&nbsp;
					</th>
					<th  nowrap="true" style="text-align: center;" width="10%" >
						<input type="button"  name="bntCount"  value="小计" onclick="ro_normal_count();" class="normal_btn" />
					</th>
					<tr>
					<td nowrap="true" width="10%" >维修配件</td>
					<td nowrap="true" width="15%" >新件代码</td>
					<td nowrap="true" width="15%" >新件名称</td>
					<td nowrap="true" width="10%" >新件数量</td>
					<td nowrap="true" width="10%" >单价</td>
					<td nowrap="true" width="10%" >金额（元）</td>
					<td nowrap="true" width="10%" >付费方式</td>
					<td nowrap="true" width="10%" >维修方式</td>
					<td nowrap="true" width="10%" >
						<input type="button"  name="bntAdd" value="添加" onclick="addPart(93331001);" class="normal_btn" />
					</td>
					</tr>
	    			 <c:forEach items="${parts1 }" var="p1">
						<tr>
						<td nowrap="true" width="10%" >配件>></td>
						<td nowrap="true" width="15%" >
						<input name="part_id_1" type="hidden" class="middle_txt" value="${p1.REAL_PART_ID }"/>
						<input name="ro_repair_part_1" type="hidden" class="middle_txt" value="${p1.ID }"/>
						<input name="part_code_1" type="hidden" readonly="readonly"  value="${p1.PART_NO }"/><span>${p1.PART_NO }</span>
						</td>
						<td nowrap="true" width="15%" >
						<input name="part_name_1" readonly="readonly"type="hidden"  value="${p1.PART_NAME }"/><span>${p1.PART_NAME }</span>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_quotiety_1" size="10"  style="color: green;" onblur="insertNum(this);"  value="${p1.PART_QUANTITY }"/>
						</td>
						<td nowrap="true" width="10%" >
						<c:if test="${p1.PAY_TYPE==11801001}">
							<input name="claim_price_param_1" tempVal="${p1.PART_COST_PRICE }" size="10" style="color: green;"  onblur="changePrice(this);" value="${p1.PART_COST_PRICE }"/>
						</c:if>
						<c:if test="${p1.PAY_TYPE==11801002}">
							<input name="claim_price_param_1" tempVal="${p1.PART_COST_PRICE }" size="10" readonly="readonly" onblur="changePrice(this);" value="${p1.PART_COST_PRICE }"/>
						</c:if>
						</td>
						<td nowrap="true" width="10%" >
						<input readonly="readonly"  size="10" name="part_amont_1"  value="${p1.PART_COST_AMOUNT }"/>
						</td>
						<td nowrap="true" width="10%" >
						<c:if test="${p1.PAY_TYPE==11801002}">
						<select class="min_sel" name="pay_type_1" onchange="changeInputByFree(this);">
							<option value="11801002" selected="selected">索赔</option>
							<option value="11801001">自费</option>
						</select>
						</c:if>
						<c:if test="${p1.PAY_TYPE==11801001}">
						<select class="min_sel" name="pay_type_1" onchange="changeInputByFree(this);">
							<option value="11801002" >索赔</option>
							<option value="11801001" selected="selected">自费</option>
						</select>
						</c:if>
						</td>
						<td  width="10%" nowrap="true">
						<c:if test="${p1.PART_USE_TYPE==95431002}">
							<input name="part_use_type_1" type="hidden"  size="10" value="${p1.PART_USE_TYPE}"/>
							<span>更换</span>
						</c:if>
						<c:if test="${p1.PART_USE_TYPE==95431001}">
							<input name="part_use_type_1" type="hidden"  size="10" value="${p1.PART_USE_TYPE}"/>
							<span>维修</span>
						</c:if>
						</td>
						<td nowrap="true" width="10%" >
						<input type="button"  name="bntDel" value="删除" onclick="deleteTrByNormal(this);" class="normal_btn" />
						</td>
						</tr>
	    			 </c:forEach>
	    		</table>
    			<table border="1" id="tab_1_1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
					<tr>
					<td nowrap="true" width="10%" >维修工时</td>
					<td nowrap="true" width="15%" >作业代码</td>
					<td nowrap="true" width="25%" >作业名称</td>
					<td nowrap="true" width="10%" >工时定额</td>
					<td nowrap="true" width="10%" >工时单价</td>
					<td nowrap="true" width="10%" >金额（元）</td>
					<td nowrap="true" width="10%" >付费方式</td>
					<td nowrap="true" width="10%">
					<input type="button"  name="bntAdd" value="添加" onclick="addLabour(93331001);" class="normal_btn" />
					</td>
					</tr>
	    			 <c:forEach items="${labours1 }" var="l1">
			           <tr>
			           <td nowrap="true" width="10%" >工时>></td>
			           <td nowrap="true" width="15%" >
			           <input readonly="readonly" name="labour_code_1" type="hidden"  value="${l1.LABOUR_CODE}"/><span>${l1.LABOUR_CODE }</span>
			           </td>
			           <td nowrap="true" width="25%" >
			           <input name="cn_des_1" readonly="readonly" size="35" type="hidden" value="${l1.LABOUR_NAME }"/><span>${l1.LABOUR_CODE }</span>
			           </td>
			           <td nowrap="true" width="10%" >
			           <input name="labour_quotiety_1"  size="10"readonly="readonly"  value="${l1.STD_LABOUR_HOUR }"/>
			           </td>
			           <td nowrap="true" width="10%" >
			           <c:if test="${l1.PAY_TYPE==11801001}">
			           <input name="parameter_value_1" size="10" tempVal="${l1.LABOUR_PRICE }" style="color: green;"  onblur="changePrice(this);" value="${l1.LABOUR_PRICE }"/>
			           </c:if>
			           <c:if test="${l1.PAY_TYPE==11801002}">
			           <input name="parameter_value_1" size="10" tempVal="${l1.LABOUR_PRICE }" readonly="readonly" onblur="changePrice(this);" value="${l1.LABOUR_PRICE }"/>
			           </c:if>
			           </td>
			           <td nowrap="true" width="10%" >
			           <input name="labour_fix_1" size="10" readonly="readonly" value="${l1.LABOUR_AMOUNT }"/>
			           </td>
			           <td nowrap="true" width="10%"  >
			            <c:if test="${l1.PAY_TYPE==11801002}">
			           <select class="min_sel" name="pay_type_labour_1" onchange="changeInputByFreeL(this);">
						  <option value="11801002"  selected="selected">索赔</option>
						  <option value="11801001">自费</option>
						</select>
						</c:if>
						  <c:if test="${l1.PAY_TYPE==11801001}">
			           <select class="min_sel" name="pay_type_labour_1" onchange="changeInputByFreeL(this);">
						  <option value="11801002">索赔</option>
						  <option value="11801001" selected="selected">自费</option>
						</select>
						</c:if>
			           </td>
			           <td nowrap="true" width="10%">
			           <input type="button"  name="bntDel" value="删除" onclick="deleteTr(this);" class="normal_btn" />
			           </td>
			           </tr>
	    			 </c:forEach>
	    		</table>
    			</c:if>
    			<c:if test="${parts2!=null}">
	    		<table border="1" id="tab_2" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
					<th colspan="9"><input type="hidden" name="ro_type" value="93331002">
					<img class="nav" src="../jsp_new/img/subNav.gif" />免费保养 &nbsp;&nbsp;<input type="button"  value="删除" onclick="delPart(this,93331002);" class="normal_btn" /></th>
					<tr>
					<td nowrap="true" width="10%" >维修配件</td>
					<td nowrap="true" width="15%" >新件代码</td>
					<td nowrap="true" width="15%" >新件名称</td>
					<td nowrap="true" width="10%" >新件数量</td>
					<td nowrap="true" width="10%" >单价</td>
					<td nowrap="true" width="10%" >金额（元）</td>
					<td nowrap="true" width="10%" >付费方式</td>
					<td nowrap="true" width="10%" >维修方式</td>
					<td nowrap="true" width="10%" >
					</td>
					</tr>
	    			 <c:forEach items="${parts2 }" var="p2">
						<tr>
						<td nowrap="true" width="10%" >配件>></td>
						<td nowrap="true" width="15%" >
						<input name="ro_repair_part_2" type="hidden" class="middle_txt" value="${p2.ID }"/>
						<input name="part_id_2" type="hidden"   value="${p2.REAL_PART_ID }"/>
						<input name="part_code_2" type="hidden"  readonly="readonly"  value="${p2.PART_NO }"/><span>${p2.PART_NO }</span>
						</td>
						<td nowrap="true" width="15%" >
						<input name="part_name_2" readonly="readonly" type="hidden"  value="${p2.PART_NAME }"/><span>${p2.PART_NAME }</span>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_quotiety_2"  size="10" type="hidden" readonly="readonly" value="${p2.PART_QUANTITY }"/><span>${p2.PART_QUANTITY }</span>
						</td><td nowrap="true" width="10%" >
						<input name="claim_price_param_2" readonly="readonly" type="hidden" size="10" value="${p2.PART_COST_PRICE }"/><span>${p2.PART_COST_PRICE }</span>
						</td>
						<td nowrap="true" width="10%" >
						<input readonly="readonly" name="part_amont_2" type="hidden" size="10" value="${p2.PART_COST_AMOUNT }"/><span>${p2.PART_COST_AMOUNT }</span>
						</td>
						<td  width="10%" nowrap="true">
						<c:if test="${p2.PAY_TYPE==11801002}">
							<input name="pay_type_2" type="hidden"  size="10" value="${p2.PAY_TYPE}"/>
							<span>索赔</span>
						</c:if>
						<c:if test="${p2.PAY_TYPE==11801001}">
							<input name="pay_type_2" type="hidden"  size="10" value="${p2.PAY_TYPE}"/>
							<span>自费</span>
						</c:if>
						</td>
						<td  width="10%" nowrap="true">
						<c:if test="${p2.PART_USE_TYPE==95431002}">
							<input name="part_use_type_2" type="hidden"  size="10" value="${p2.PART_USE_TYPE}"/>
							<span>更换</span>
						</c:if>
						<c:if test="${p2.PART_USE_TYPE==95431001}">
							<input name="part_use_type_2" type="hidden"  size="10" value="${p2.PART_USE_TYPE}"/>
							<span>维修</span>
						</c:if>
						<!-- </select> -->
						</td>
						<td nowrap="true" width="10%" >(模板配件)
						</td>
						</tr>
	    			 </c:forEach>
	    		</table>
    			<table border="1" id="tab_2_1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
					<tr>
					<td nowrap="true" width="10%" >维修工时</td>
					<td nowrap="true" width="15%" >作业代码</td>
					<td nowrap="true" width="25%" >作业名称</td>
					<td nowrap="true" width="10%" >工时定额</td>
					<td nowrap="true" width="10%" >工时单价</td>
					<td nowrap="true" width="10%" >金额（元）</td>
					<td nowrap="true" width="10%" >付费方式</td>
					<td nowrap="true" width="10%">
					</td>
					</tr>
	    			 <c:forEach items="${labours2 }" var="l2">
			           <tr>
			           <td nowrap="true" width="10%" >工时>></td>
			           <td nowrap="true" width="15%" >
			           <input readonly="readonly" name="labour_code_2" type="hidden" value="${l2.LABOUR_CODE}"/><span>${l2.LABOUR_CODE }</span>
			           </td>
			           <td nowrap="true" width="25%" >
			           <input name="cn_des_2" readonly="readonly" type="hidden" size="35" value="${l2.LABOUR_NAME }"/><span>${l2.LABOUR_NAME }</span>
			           </td>
			           <td nowrap="true" width="10%" >
			           <input name="labour_quotiety_2" readonly="readonly" type="hidden" size="10" value="${l2.STD_LABOUR_HOUR }"/><span>${l2.STD_LABOUR_HOUR }</span>
			           </td>
			           <td nowrap="true" width="10%" >
			           <input name="parameter_value_2"  size="10" type="hidden" readonly="readonly" value="${l2.LABOUR_PRICE }"/><span>${l2.LABOUR_PRICE }</span>
			           </td><td nowrap="true" width="10%" >
			           <input name="labour_fix_2" type="hidden" readonly="readonly"  size="10" value="${l2.LABOUR_AMOUNT }"/><span>${l2.LABOUR_AMOUNT }</span>
			           <td nowrap="true" width="10%"  >
			           <c:if test="${l2.PAY_TYPE==11801002}">
							<input name="pay_type_labour_2" type="hidden"  size="10" value="${l2.PAY_TYPE }"/>
							<span>索赔</span>
						</c:if>
						<c:if test="${l2.PAY_TYPE==11801001}">
							<input name="pay_type_labour_2" type="hidden"  size="10" value="${l2.PAY_TYPE }"/>
							<span>自费</span>
						</c:if>
			           <!-- </select> -->
			           </td>
			           </td><td nowrap="true" width="10%">(模板工时)
			           </td>
			           </tr>
	    			 </c:forEach>
	    		</table>
    			</c:if>
    			<c:if test="${parts3!=null}">
	    		<table border="1" id="tab_3" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
					<th colspan="7"><input type="hidden" name="ro_type" value="93331003">
					<img class="nav" src="../jsp_new/img/subNav.gif" />自费保养 &nbsp;&nbsp;
					<input type="button"  value="删除" onclick="delPart(this,93331003);" class="normal_btn" />
					</th>
					<th  nowrap="true" style="text-align: center;" width="10%" >
					<span style="color: red; font-weight: bold;" id="ro_free_count"></span>
						&nbsp;&nbsp;
					</th>
					<th  nowrap="true" style="text-align: center;" width="10%" >
						<input type="button"  name="bntCount"  value="小计" onclick="ro_free_count();" class="normal_btn" />
					</th>
					<tr>
					<td nowrap="true" width="10%" >维修配件</td>
					<td nowrap="true" width="15%" >新件代码</td>
					<td nowrap="true" width="15%" >新件名称</td>
					<td nowrap="true" width="10%" >新件数量</td>
					<td nowrap="true" width="10%" >单价</td>
					<td nowrap="true" width="10%" >金额（元）</td>
					<td nowrap="true" width="10%" >付费方式</td>
					<td nowrap="true" width="10%" >维修方式</td>
					<td nowrap="true" width="10%" >
					<input type="button"  name="bntAdd" value="添加" onclick="addPart(93331003);" class="normal_btn" />
					</td>
					</tr>
	    			 <c:forEach items="${parts3 }" var="p3">
						<tr>
						<td nowrap="true" width="10%" >配件>></td>
						<td nowrap="true" width="15%" >
						<input name="ro_repair_part_3" type="hidden" class="middle_txt" value="${p3.ID }"/>
						<input name="part_id_3" type="hidden"   value="${p3.REAL_PART_ID }"/>
						<input name="part_code_3" readonly="readonly" type="hidden" value="${p3.PART_NO }"/><span>${p3.PART_NO }</span>
						</td>
						<td nowrap="true" width="15%" >
						<input name="part_name_3" readonly="readonly" type="hidden" value="${p3.PART_NAME }"/><span>${p3.PART_NAME }</span>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_quotiety_3" onblur="insertNum(this);" style="color: green;" size="10" value="${p3.PART_QUANTITY }"/>
						</td><td nowrap="true" width="10%" >
						<input name="claim_price_param_3" readonly="readonly" size="10" value="${p3.PART_COST_PRICE }"/>
						</td>
						<td nowrap="true" width="10%" >
						<input readonly="readonly" name="part_amont_3"  size="10" value="${p3.PART_COST_AMOUNT }"/>
						</td>
						<td  width="10%" nowrap="true">
						<c:if test="${p3.PAY_TYPE==11801002}">
							<input name="pay_type_3" type="hidden"  size="10" value="${p3.PAY_TYPE}"/>
							<span>索赔</span>
						</c:if>
						<c:if test="${p3.PAY_TYPE==11801001}">
							<input name="pay_type_3" type="hidden"  size="10" value="${p3.PAY_TYPE}"/>
							<span>自费</span>
						</c:if>
						</td>
						<td  width="10%" nowrap="true">
						<c:if test="${p3.PART_USE_TYPE==95431002}">
							<input name="part_use_type_3" type="hidden"  size="10" value="${p3.PART_USE_TYPE}"/>
							<span>更换</span>
						</c:if>
						<c:if test="${p3.PART_USE_TYPE==95431001}">
							<input name="part_use_type_3" type="hidden"  size="10" value="${p3.PART_USE_TYPE}"/>
							<span>维修</span>
						</c:if>
						</td>
						<td nowrap="true" width="10%" >
						<input type="button"  name="bntDel" value="删除" onclick="deleteTr(this);" class="normal_btn" />
						</td>
						</tr>
	    			 </c:forEach>
	    		</table>
    			<table border="1" id="tab_3_1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
					<tr>
					<td nowrap="true" width="10%" >维修工时</td>
					<td nowrap="true" width="15%" >作业代码</td>
					<td nowrap="true" width="25%" >作业名称</td>
					<td nowrap="true" width="10%" >工时定额</td>
					<td nowrap="true" width="10%" >工时单价</td>
					<td nowrap="true" width="10%" >金额（元）</td>
					<td nowrap="true" width="10%" >付费方式</td>
					<td nowrap="true" width="10%">
					<input type="button"  name="bntAdd" value="添加" onclick="addLabour(93331003);" class="normal_btn" /></td></tr>
	    			 <c:forEach items="${labours3 }" var="l3">
			           <tr>
			           <td nowrap="true" width="10%" >工时>></td>
			           <td nowrap="true" width="15%" >
			           <input readonly="readonly" name="labour_code_3" type="hidden" value="${l3.LABOUR_CODE}"/><span>${l3.LABOUR_CODE }</span>
			           </td>
			           <td nowrap="true" width="25%" >
			           <input name="cn_des_3" readonly="readonly" type="hidden" size="35" value="${l3.LABOUR_NAME }"/><span>${l3.LABOUR_NAME }</span>
			           </td>
			           <td nowrap="true" width="10%" >
			           <input name="labour_quotiety_3" readonly="readonly"  size="10" value="${l3.STD_LABOUR_HOUR }"/>
			           </td>
			           <td nowrap="true" width="10%" >
			           <input name="parameter_value_3"  size="10" readonly="readonly" value="${l3.LABOUR_PRICE }"/>
			           </td><td nowrap="true" width="10%" >
			           <input name="labour_fix_3" readonly="readonly"  size="10" value="${l3.LABOUR_AMOUNT }"/>
			           <td nowrap="true" width="10%"  >
			           <c:if test="${l3.PAY_TYPE==11801002}">
							<input name="pay_type_labour_3" type="hidden"  size="10" value="${l3.PAY_TYPE }"/>
							<span>索赔</span>
						</c:if>
						<c:if test="${l3.PAY_TYPE==11801001}">
							<input name="pay_type_labour_3" type="hidden"  size="10" value="${l3.PAY_TYPE }"/>
							<span>自费</span>
						</c:if>
			           </td>
			           </td><td nowrap="true" width="10%">
			           <input type="button"  name="bntDel" value="删除" onclick="deleteTr(this);" class="normal_btn" />
			           </td>
			           </tr>
	    			 </c:forEach>
	    		</table>
    			</c:if>
    		</div>
 <div id="new_acc_add"  style="text-align: center; width: 100%;">
 			<c:if test="${acc!=null}">
 			<table id="accessories" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
					<th colspan="8">
						<img class="nav" src="../jsp_new/img/subNav.gif" />辅料项目&nbsp;&nbsp;<input type="button" class="normal_btn" value="删除" onclick="delPart(this,4);"
					</th>
					<tr>
						<td nowrap="true" width="20%" >辅料代码</td>
						<td nowrap="true" width="20%" >辅料名称</td>
						<td nowrap="true" width="20%" >辅料费用</td>
						<td nowrap="true" width="20%" >关联配件</td>
						<td nowrap="true" width="20%" > <input type="button"  name="bntAdd" value="添加" onclick="addAccessories();" class="normal_btn" /></td>
					</tr>
    			 <c:forEach items="${acc }" var="a">
    				<tr><td nowrap="true" width="20%" >
						<input readonly="readonly" name="workHourCode" class="middle_txt" value="${a.WORKHOUR_CODE }"/>
						</td><td nowrap="true" width="20%" >
						<input readonly="readonly" name="workhour_name" class="middle_txt" value="${a.WORKHOUR_NAME }"/>
						</td>
						<td nowrap="true" width="20%" >
						<input readonly="readonly" name="accessoriesPrice" class="middle_txt" value="${a.PRICE }"/>
						</td><td nowrap="true" width="20%" >
						<select class="short_sel" name="accessoriesOutMainPart" >
							<option value="${a.MAIN_PART_CODE }">${a.MAIN_PART_CODE }</option>
						</select><span class="hideClass">&nbsp;<a href="#" onclick="getPartCode();" >点击获取</a></span>
						</td>
						<td nowrap="true" width="20%" >
						<input type="button"  name="bntDel" value="删除" onclick="deleteTr(this);" class="normal_btn" />
						</td></tr>
    			 </c:forEach>
    			 </table>
    		</c:if>
</div>
<br/>
<table width=100% border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
				<input type="button" id="back" onClick="_hide();" class="normal_btn"  style="width=8%" value="关闭"/>
   			</td>
           	<td height="12" align=center width="33%">
     			</td>
		</tr>
</table>
</form>
</body>
</html>