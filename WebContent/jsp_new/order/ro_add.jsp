<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/jstl/change" prefix="change" %>
<head> 
<% String contextPath = request.getContextPath(); %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/zyw/jquery-1.3.2.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/js/jslib/zyw/jquery-calendar.js"></script> 
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/js/jslib/zyw/jquery-calendar.css" /> 
<title>售后工单新增</title>
<script type="text/javascript">

	function setActityData(activity_code,templet_id,is_return){
		$("#cam_code").val(activity_code);
		$("#is_visit").val(is_return);
		var div=$('#new_add');
		div.empty();
		var cam_code=$("#cam_code").val();
		if(""!=cam_code){
			$("#bntAcc").attr("disabled",true);
			$("#bntAdd").attr("disabled",true);
		}
		sendAjax('<%=contextPath%>/ActivityAction/findActityData.json?id='+templet_id,setActityDataBack,'fm');
	}
	function setActityDataBack(json){

        //=======在添加服务活动的时候要将保养单号移除
		$("#keep_fit_no").val("");
		var div=$('#new_add');
		var str="";
		//配件====================================
		str+='<table border="1" id="tab_1" cellpadding="1" cellspacing="1" class="table_edit" width="100%"  style="text-align: center;">';
		str+='<th colspan="7"><input type="hidden" name="ro_type" value="93331001"><img class="nav" src="../jsp_new/img/subNav.gif" />正常维修&nbsp;&nbsp;</th>';
		str+='<th  nowrap="true" style="text-align: center;" width="10%" >';
		str+='<span style="color: red; font-weight: bold;" id="ro_normal_count"></span>';
		str+='	&nbsp;&nbsp;';
		str+='</th>';
		str+='<th  nowrap="true" style="text-align: center;" width="10%" >';
		str+='<input type="button"  name="bntCount"  value="小计" onclick="ro_normal_count();" class="normal_btn" />';
		str+='</th>';
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
		//str+='	<input type="button"  name="bntAdd" value="添加" onclick="addPart(93331001);" class="normal_btn" />';
		str+='</td>';
		str+='</tr>';
		str+='</table>';
		//工时====================================
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
		//str+='<input type="button"  name="bntAdd" value="添加" onclick="addLabour(93331001);" class="normal_btn" />';
		str+='</td>';
		str+='</tr>';
		str+='</table>';
		div.append(str);
		
		var tab=$("#tab_1");
		var activity_type=json.t.ACTIVITY_TYPE;
		var labours=json.labours;
		var parts=json.parts;
		//var acc=json.acc;
		//var com=json.com;
		if(null!=parts && parts.length>0){
			for(var i=0;i<parts.length;i++){
				var part_id=parts[i].PART_ID;
				var part_code=parts[i].PART_CODE;
				var part_name=parts[i].PART_NAME;
				var part_quantity=parts[i].PART_QUANTITY;
				var part_cost_amount=parts[i].PART_COST_AMOUNT;
				var part_cost_price=parts[i].PART_COST_PRICE;
				var part_use_type=parts[i].PART_USE_TYPE;
				var str="";
				str+='<tr>';
				str+='<td nowrap="true" width="10%" >配件>></td>';
				str+='<td nowrap="true" width="15%" >';
				str+='<input name="part_id_1" type="hidden"  value="'+part_id+'"/>';
				str+='<input name="is_gua_1" type="hidden"  value="1"/>';
				str+='<input name="part_camcode" type="hidden"  value="1"/>';//服务活动标示
				str+='<input name="part_code_1" readonly="readonly"  value="'+part_code+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="15%" >';
				str+='<input name="part_name_1" readonly="readonly"  value="'+part_name+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="10%" >';
				if(part_use_type==1){
					str+='<input name="part_quotiety_1" readonly="readonly" style="color: green;" size="10"  value="'+part_quantity+'"/>';
				}else{
					str+='<input name="part_quotiety_1" readonly="readonly" style="color: green;" size="10"  value="0"/>';
				}
				str+='</td>';
				str+='<td nowrap="true" width="10%" >';
				str+='<input name="claim_price_param_1" readonly="readonly" size="10" value="'+part_cost_price+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="10%" >';
				if(part_use_type==1){
					if(10561005==activity_type){
						str+='<input readonly="readonly" name="part_amont_1" size="10" value="0.0"/>';
					}else{
						str+='<input readonly="readonly" name="part_amont_1" size="10" value="'+part_cost_amount+'"/>';
					}
				}else{
					str+='<input readonly="readonly" name="part_amont_1" size="10" value="0.0"/>';
				}
				str+='</td>';
				str+='<td nowrap="true" width="10%" ><select class="min_sel" name="pay_type_1">';
				str+='<option value="11801002">索赔</option>';
				str+='</select></td>';
				str+='<td nowrap="true" width="10%" ><select class="min_sel"  name="part_use_type_1">';
				if(part_use_type==1){
					str+='<option value="95431002">更换</option>';
				}else{
					str+='<option value="95431001">维修</option>';
				}
				str+='</select></td>';
				str+='<td nowrap="true" width="10%" >服务活动</td></tr>';
				tab.append(str);
			}
		}
		var tab1=$("#tab_1_1");
		if(null!=labours && labours.length>0){
			for(var i=0;i<labours.length;i++){
				var labour_code=labours[i].LABOUR_CODE;
				var cn_des=labours[i].LABOUR_NAME;
				var labour_fix=labours[i].LABOUR_QUOTIETY;
				var parameter_value=labours[i].LABOUR_PRICE;
				var labour_amount=labours[i].LABOUR_AMOUNT;
				var str="";
				str+='<tr>';
				str+='<td nowrap="true" width="10%" >工时>></td>';
				str+='<td nowrap="true" width="15%" >';
				str+='<input name="lab_camcode" type="hidden"  value="1"/>';//服务活动标示
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
				str+='<input name="labour_fix_1" readonly="readonly" size="10" value="'+labour_amount+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="10%"  >';
				str+='<select class="min_sel" name="pay_type_labour_1">';
				str+='  <option value="11801002">索赔</option>';
				str+='</select>';
				str+='</td>';
				str+='<td nowrap="true" width="10%" >服务活动</td></tr>';
				tab1.append(str);
			}
		}
	}
	function changeInputVal(obj){
		var val=obj.value;
		var tr=$(obj).parent().parent();
		var nextInput3=tr.children().eq(3).children().eq(0);
		var nextInput5=tr.children().eq(5).children().eq(0);
		var nextInput3val=tr.children().eq(3).children().eq(1).val();
		var nextInput5val=tr.children().eq(5).children().eq(1).val();
		if("95431001"==val){//维修
			nextInput3.val("0");
			nextInput3.attr("readonly","readonly");
			nextInput5.val("0.0");
			nextInput5.attr("readonly","readonly");
		}
		if("95431002"==val){//更换
			nextInput3.val(nextInput3val);
			nextInput5.val(nextInput5val);
			nextInput3.removeAttr("readonly");
			nextInput5.removeAttr("readonly");
			InsertNum(1);//重新计算
		}
	}
	function ro_type_add(){
		var wrgroup_id=$("#wrgroup_id").val();
		var package_id=$("#package_id").val();
		var series_id=$("#series_id").val();
		var model_id=$("#model_id").val();
		var in_mileage=$("#in_mileage").val();
		var vin=$("#vin").val();
		if(""==wrgroup_id){
			MyAlert("提示：该车不在车型组里，请联系管理员维护！");
			return;
		}
		if(""==package_id||""==series_id||""==model_id||""==vin){
			MyAlert("提示：请输入正确的VIN并保证正确信息带出！");
			return;
		}
		if(""==in_mileage){
			MyAlert("提示：请先输入行驶里程！");
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
			str+='<th colspan="7"><input type="hidden" name="ro_type" value="93331001"><img class="nav" src="../jsp_new/img/subNav.gif" />正常维修&nbsp;&nbsp;<input type="button"  value="删除" onclick="delPart(this,93331001);" class="normal_btn" /></th>';
			str+='<th  nowrap="true" style="text-align: center;" width="10%" >';
			str+='<span style="color: red; font-weight: bold;" id="ro_normal_count"></span>';
			str+='	&nbsp;&nbsp;';
			str+='</th>';
			str+='<th  nowrap="true" style="text-align: center;" width="10%" >';
			str+='<input type="button"  name="bntCount"  value="小计" onclick="ro_normal_count();" class="normal_btn" />';
			str+='</th>';
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
			str+='<th colspan="7"><input type="hidden" name="ro_type" value="93331003"><img class="nav" src="../jsp_new/img/subNav.gif" />自费保养&nbsp;&nbsp;<input type="button"  value="删除" onclick="delPart(this,93331003);" class="normal_btn" /></th>';
			str+='<th  nowrap="true" style="text-align: center;" width="10%" >';
			str+='<span style="color: red; font-weight: bold;" id="ro_free_count"></span>';
			str+='	&nbsp;&nbsp;';
			str+='</th>';
			str+='<th  nowrap="true" style="text-align: center;" width="10%" >';
			str+='<input type="button"  name="bntCount"  value="小计" onclick="ro_free_count();" class="normal_btn" />';
			str+='</th>';
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
	function backKeepFitData(keep_fit_no,labours2,parts2){
		$("#keep_fit_no").val(keep_fit_no);
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
		var part_id_1="";
		$("input[name='part_id_1']").each(function(){
			part_id_1+=$(this).val()+",";
		});
		var part_id_3="";
		$("input[name='part_id_3']").each(function(){
			part_id_3+=$(this).val()+",";
		});
		if(part_id_1=="" && part_id_3==""){
			MyAlert("提示：请先选择配件，然后会对应找出维护的对应的工时！");
			return;
		}
		OpenHtmlWindow('<%=contextPath%>/OrderAction/addLabour.do?wrgroup_id='+wrgroup_id+'&package_id='+package_id+'&val='+val+'&labour_codes_1='+labour_codes_1+'&labour_codes_3='+labour_codes_3+'&part_id_1='+part_id_1+'&part_id_3='+part_id_3,850,550);
	}
	function addPart(val){
		var in_mileage=$("#in_mileage").val();
		if(""==in_mileage){
			MyAlert("提示：请输入里程在进行添加配件");
			return;
		}
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
			$("#keep_fit_no").val("");
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
		if("5"==val){
			$("#outData").remove();
			$("#bntOut").attr("disabled",false);
		}
	}
	$(function(){
		$("#noBalance,#balance,#auditPass,#auditUnPass").hide();
		var type=$("#type").val();
		var cam_code=$("#cam_code").val();
		if(""!=cam_code){
			$(".show_activity").show();
		}
		if("add"==type){
			$("#ro_no").val(" 新增保存后自动生成 ");
			$("#warning").val(" 新增保存后自动判断等级 ");
			$("#vin").bind("change",function(){
				var vin=$("#vin").val();
				if($.trim(vin).length>=18){
					MyAlert("提示：VIN只有17位！");
					$("#vin").val("");
					return;
				}
				var len=$.trim(vin).length;
				if(len==17){
					$("#tab_1,#tab_1_1").remove();
					sendAjax('<%=contextPath%>/OrderAction/showInfoByVin.json?vin='+vin,backByVin,'fm');
				}else if (len>=8 && len<17 ){
					$("#tab_1,#tab_1_1").remove();
					sendAjax('<%=contextPath%>/OrderAction/findVinListByVin.json?vin='+vin,backVinDataByVin,'fm');
				}
			});
			$("#license_no").live("keyup",function(){
				var license_no=$("#license_no").val();
				if($.trim(license_no).length>=4){
					var url='<%=contextPath%>/OrderAction/showInfoBylicenseNo.json?license_no='+license_no;
					sendAjax(url,backBylicenseNo,"fm");
				}
			});
			$("#in_mileage").bind("blur",function(){
				checkMileage();
			});
		}
		if("update"==type){
			$("#vin").attr("readonly","readonly");
			var acc=$("select[name='accessoriesOutMainPart']");
			if(acc.length>0){
				$("#bntAcc").attr("disabled",true);
			}
			var addr=$("input[name='address']");
			if(addr.length>0){
				$("#bntOut").attr("disabled",true);
			}
			var cam_code=$("#cam_code").val();
			if(""!=cam_code){
				$("#bntAcc").attr("disabled",true);
				$("#bntAdd").attr("disabled",true);
			}
			$("#in_mileage").bind("blur",function(){
				checkMileage();
			});
		}
		if("view"==type){
			$(".hideClass").hide();
			$("#sure").remove();
			$("#back").unbind();
			$("#for_balance_time").unbind();
			$("select").attr("disabled",true);
			$(".dealer").css("display","block");
			$("input[type='button']").attr("disabled",true);
			$("input[name='bntDel']").attr("disabled",true);
			$("input[type='text']").attr("readonly","readonly");
			$("#btn1,#btn3,#back").attr("disabled",false);
			$("#time_show").show();
			$("#repairText").attr("readonly","readonly");
		}
		if("balance"==type){
			$(".hideClass").hide();
			$("input[type='button']").attr("disabled",true);
			//$("select option:last").remove();
			$("input[type='text']").attr("readonly","readonly");
			$("#btn1,#btn3,#back").attr("disabled",false);
			$("#time_show").show();
			$("#repairText").attr("readonly","readonly");
			var date = new Date();
			var year=date.getFullYear();
			var month = date.getMonth()+1;
			var day = date.getDate();
			var hour = date.getHours();
			var minute = date.getMinutes();
			var Second = date.getSeconds();
			var dateNow=year+'-'+month+'-'+day+' '+hour+':'+minute+':'+Second;
			$("#for_balance_time").val(dateNow);
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
			$("#for_balance_time").attr("disabled",true);
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
			$("#for_balance_time").attr("disabled",true);
		}
	});
	function backVinDataByVin(json){
		if(json.vinData!=null && json.vinData.length>0){
			$("#divTemp").css('display','block'); 
			$("#vinDiv").empty();
			for(var i=0;i<=json.vinData.length;i++){
				var vinDivInner="<tr><td onclick='checkThisVin(this);'>"+json.vinData[i].VIN+"</td></tr>";
				$("#vinDiv").append(vinDivInner);
			}
		}else{
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
			$("#divTemp").css('display','none'); 
			$("#vinDiv").empty();
		}
	}
	function checkThisVin(obj){
		$("#vin").val($(obj).text());
		var vin=$("#vin").val();
		if($.trim(vin).length>=12){
			sendAjax('<%=contextPath%>/OrderAction/showInfoByVin.json?vin='+vin,backByVin,'fm');
		}
		$("#divTemp").css('display','none'); 
	}
	function backBylicenseNo(json){
		if(json.licenseData!=null && json.licenseData.length>0){
			$("#divLicense").css('display','block'); 
			$("#licenseDiv").empty();
			for(var i=0;i<=json.licenseData.length;i++){
				var vin=json.licenseData[i].VIN;
				var inner="<tr><td onclick='checkThisLicense(this,\""+vin+"\");'>"+json.licenseData[i].LICENSE+"</td></tr>";
				$("#licenseDiv").append(inner);
			}
		}
	}
	function checkThisLicense(obj,vin){
		$("#vin").val(vin);
		$("#license_no").val(obj.value);
		if($.trim(vin).length>=12){
			sendAjax('<%=contextPath%>/OrderAction/showInfoByVin.json?vin='+vin,backByVin,'fm');
		}
		$("#divLicense").css('display','none');
	}
	//设置添加配件
	function setMainPartCode(part_id,part_code,part_name,claim_price_param,is_return,val){
		if(93331001==val){
			var temp=0;
			$("input[name=part_id_1]").each(function(){
				if(part_id==$(this).val()){
					MyAlert("提示：您选择的该类型配件已经存在，请重新选择!");
					temp++;
				}
			})
			if(temp==0){
				var tab=$("#tab_1");
				var vin=$("#vin").val();
				var inMileage=$("#in_mileage").val();
				var purchasedDate=$("#guarantee_date").val();
				var url='<%=contextPath%>/OrderAction/getGuaFlag.json?vin='+vin+'&partCode='+part_code+'&inMileage='+inMileage+'&purchasedDate='+purchasedDate.substr(0,10);
				sendAjax(url,function(json){
				var is_gua=0;
				var flag=json.map.flag;
				var notice=json.map.notice;
				var noNo=json.map.noNo;
				if(flag){
					is_gua=1;
				}
				var str="";
				str+='<tr>';
				str+='<td nowrap="true" width="10%" >配件>></td>';
				str+='<td nowrap="true" width="15%" >';
				str+='<input name="part_camcode" type="hidden"  value="0"/>';//服务活动标示
				str+='<input name="part_id_1" type="hidden" class="middle_txt" value="'+part_id+'"/>';
				str+='<input name="is_gua_1" type="hidden" class="middle_txt" value="'+is_gua+'"/>';
				str+='<input name="part_code_1" readonly="readonly"  value="'+part_code+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="25%" >';
				str+='<input name="part_name_1" style="width:200px;" readonly="readonly"  value="'+part_name+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="5%" >';
				str+='<input name="part_quotiety_1" onblur="insertNum(this);" style="color: green;" size="10"  value="1"/>';
				str+='<input type="hidden"   style="color: green;" size="10"  value="1"/>';
				str+='</td>';
				str+='<td nowrap="true" width="5%" >';
				str+='<input name="claim_price_param_1" readonly="readonly" onblur="changePrice(this);" size="10" value="'+claim_price_param+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="10%" >';
				str+='<input readonly="readonly" name="part_amont_1" size="10" value="'+claim_price_param+'"/>';
				str+='<input readonly="readonly" type="hidden"  size="10" value="'+claim_price_param+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="10%" ><select class="min_sel" name="pay_type_1">';
				if(is_gua==1){
					str+='<option value="11801002">索赔</option>';
					str+='<option value="11801001">自费</option>';
				}else{
					str+='<option value="11801001">自费</option>';		
					str+='<option value="11801002">索赔</option>';
				}
				str+='</select></td>';
				str+='<td nowrap="true" width="10%" ><select class="min_sel" name="part_use_type_1" onchange="changeInputVal(this);">';
				str+='<option value="95431002">更换</option>';
				str+='<option value="95431001">维修</option>';
				str+='</select></td>';
				str+='<td nowrap="true" width="10%" >';
				str+='<input type="button"  name="bntDel" value="删除" style="color: red;" onclick="deleteTrByNormal(this);" class="normal_btn" />';
				str+='</td></tr>';
				tab.append(str);
				},'fm');
			}
		}
		if(93331003==val){
			var temp=0;
			$("input[name=part_id_3]").each(function(){
				if(part_id==$(this).val()){
					MyAlert("提示：您选择的该类型配件已经存在，请重新选择!");
					temp++;
				}
			})
			if(temp==0){
				var tab=$("#tab_3");
				var str="";
				str+='<tr>';
				str+='<td nowrap="true" width="10%" >配件>></td>';
				str+='<td nowrap="true" width="15%" >';
				str+='<input name="part_id_3" type="hidden" class="middle_txt" value="'+part_id+'"/>';
				str+='<input name="part_code_3" readonly="readonly"  value="'+part_code+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="25%" >';
				str+='<input name="part_name_3" style="width:200px;" readonly="readonly"value="'+part_name+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="5%" >';
				str+='<input name="part_quotiety_3" onblur="insertNum(this);" style="color: green;" size="10"  value="1"/>';
				str+='</td>';
				str+='<td nowrap="true" width="5%" >';
				str+='<input name="claim_price_param_3" readonly="readonly" size="10" value="'+claim_price_param+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="10%" >';
				str+='<input readonly="readonly" name="part_amont_3"  size="10" value="'+claim_price_param+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="10%" ><select class="min_sel" name="pay_type_3">';
				//str+='<option value="11801002">索赔</option>';
				str+='<option value="11801001">自费</option>';
				str+='</select></td>';
				str+='<td nowrap="true" width="10%" ><select class="min_sel" name="part_use_type_3" onchange="changeInputVal(this);">';
				str+='<option value="95431002">更换</option>';
				str+='<option value="95431001">维修</option>';
				str+='</select></td>';
				str+='<td nowrap="true" width="10%" >';
				str+='<input type="button"  name="bntDel" value="删除" style="color: red;" onclick="deleteTr(this);" class="normal_btn" />';
				str+='</td></tr>';
				tab.append(str);
			}
		}
	}
	function insertNum(obj){
		var reg = /^\d+$/;
		var val=$(obj).val();
		//============获取维修方式
			var tr=$(obj).parent().parent();
			var claimtt=tr.children().eq(7).children();
			var nextclaim=claimtt.val();
		if(""!=val && !reg.test(val)){
			MyAlert("提示：请输入正整数填入新件数量！");
			$(obj).val("1"); 
			$(obj).focus(); 
		}else if ("0"==val && "95431002"==nextclaim) {
			MyAlert("提示：维修方式为更换的配件数量至少需填一个件！");
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
			var temp=0;
			$("input[name=labour_code_1]").each(function(){
				if(labour_code==$(this).val()){
					MyAlert("提示：您选择的该类型工时已经存在，请重新选择!");
					temp++;
				}
			})
			if(temp==0){
			var tab=$("#tab_1_1");
			var str="";
			str+='<tr>';
			str+='<td nowrap="true" width="10%" >工时>></td>';
			str+='<td nowrap="true" width="15%" >';
			str+='<input name="lab_camcode" type="hidden"  value="0"/>';//服务活动标示
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
			str+='<input type="button"  name="bntDel" value="删除" style="color: red;" onclick="deleteTr(this);" class="normal_btn" />';
			str+='</td>';
			str+='</tr>';
			tab.append(str);
			}
		}
		if(93331003==val){
			var temp=0;
			$("input[name=labour_code_3]").each(function(){
				if(labour_code==$(this).val()){
					MyAlert("提示：您选择的该类型工时已经存在，请重新选择!");
					temp++;
				}
			})
			if(temp==0){
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
			str+='<input type="button"  name="bntDel" value="删除" style="color: red;" onclick="deleteTr(this);" class="normal_btn" />';
			str+='</td>';
			str+='</tr>';
			tab.append(str);
			}
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
		if(""==vin){
			MyAlert("提示：请输入正确的vin");
			return;
		}
		window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/maintaimHistory.do?VIN='+vin);
	}
	//保养历史按钮方法
	function freeMaintainHistory(){
		var vin = $("#vin").val() ;
		if(""==vin){
			MyAlert("提示：请输入正确的vin");
			return;
		}
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
		if($.trim(repairText)==""){
			MyAlert("提示：请填写维修项目内容！");
			$("#repairText").val("");
			return;
		}
		if($.trim(repairText).length>1000){
			MyAlert("提示：维修项目内容长度最大1000个字符！");
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
		if($("#outData").length>0){
		   var address = $("input[name='address']").val().trim();
		   var out_mileage = $("input[name='out_mileage']").val().trim();
		   var out_amount = $("input[name='out_amount']").val().trim();
		   if(""==address){
		      MyAlert("提示：请输入外出地点！");
		      temp++;
			  return;
		   }
		    var reg = /^\d+$/;
		   if(""!=out_mileage && !reg.test(out_mileage)){
			MyAlert("提示：请输入正确的数据(外出里程)！");
			$("#out_mileage").val("");
			temp++;
			return;
		    }
		    if(""==out_mileage){
		      MyAlert("提示：请输入外出里程！");
		      temp++;
			  return;
		   }
		    if(""==out_amount){
		      MyAlert("提示：请输入外出费用！");
		      temp++;
			  return;
		   }
		}
		if(temp==0){
			sureYj();
		}
		
	}
	function sureYj()
	{
	    var url="<%=contextPath%>/OrderAction/sureYj.json";
		makeNomalFormCall1(url,sureYjBack,"fm");
	}
	function sureYjBack(json)
	{
	    if(json.Yj.length > 2)
	    {
	      MyAlert(json.Yj);
	    }
	    ro_type_count();
	    MyConfirm("是否确认保存？",roInsertSure,"");
	}
	
	function roInsertSure(){
		var url="<%=contextPath%>/OrderAction/roInsert.json";
		$("#sure").attr("disabled",true);
		makeNomalFormCall1(url,roInsertSureBack,"fm");
	}
	function roInsertSureBack(json){
		if(json.succ=="1"){
			//MyAlert("提示：保存成功！");
			var url='<%=contextPath%>/OrderAction/orderList.do';
			window.location.href=url;
		}else{
			MyAlert("提示：保存失败！");
			$("#sure").attr("disabled",false);
		}
	}
	function doActivity(){
		$(".show_activity").show();	
		$("#is_visit").val("");
		$("#cam_code").val("请选择").css("color","red");
		$("#cam_code").bind("click",function(){
			var in_mileage=$("#in_mileage").val();
			var vin=$("#vin").val();
			OpenHtmlWindow('<%=contextPath%>/OrderAction/doActivity.do?in_mileage='+in_mileage+'&vin='+vin,800,500);
		});
		return;
	}
	//null返回“”
	function getNull(data) {
		if (data==null) {
			return '';
		}else {
			return data;
		}
	}
	function backByVin(json){
		$(".show_activity").hide();
		$("#cam_code").val("").css("color","");
		$("#cam_code").unbind("click");
		if(json.flag==false){
			MyAlert("提示：系统中同一个VIN号只能存在一张未结算状态的工单!");
			$("#vin").val("");
			return;
		}
		if(json.flag==true){
			var map=json.map;
			var checkParts=map.checkParts;
			var res=map.res;
			var is_tips=map.is_tips;
			if(10041001==is_tips){//如果是才提示
				if(""!=checkParts || ""!=res){
					//提示服务活动未做的！
					MyConfirm(res+checkParts,doActivity,"");
				}else{
					doActivity();
				}
			}else{
				doActivity();
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
				$("#main_phone").val(getNull(t.MAIN_PHONE));
				$("#ctm_name").val(getNull(t.CUSTOMER_NAME));
				$("#deliverer_adress").val(getNull(t.ADDRESS));
				$("#color").val(getNull(t.COLOR));
				if(""!=$("#engine_no").val()){
					$("#engine_no").val(getNull(t.ENGINE_NO));
				}else{
					$("#engine_no").val(getNull(t.ENGINE_NO));
				}
				$("#car_use_desc").val(getNull(t.CAR_USE_DESC));
				$("#car_use_type").val(getNull(t.CAR_USE_TYPE));
				//购车日期修改为实际开票时间2015-8-27
				if(""==getNull(t.INVOICE_DATE) || "null"==getNull(t.INVOICE_DATE)){
					$("#guarantee_date").val("");
				}else{
					$("#guarantee_date").val(getNull(t.INVOICE_DATE).substr(0,10));
				}
				$("#package_name").val(getNull(t.PACKAGE_NAME));
				$("#free_times").val(getNull(t.FREE_TIMES));
				$("#model_name").val(getNull(t.MODEL_NAME));
				$("#model").val(getNull(t.MODEL));
				$("#model_id").val(getNull(t.MODEL_ID));
				$("#package_id").val(getNull(t.PACKAGE_ID));
				$("#wrgroup_id").val(getNull(t.WRGROUP_ID));
				$("#series_id").val(getNull(t.SERIES_ID));
				if("null"==getNull(t.LICENSE_NO)){
					$("#license_no").val("");
				}else{
					$("#license_no").val(getNull(t.LICENSE_NO));
				}
			}
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
		var out_amount=$("input[name='out_amount']");
		if(out_amount.length>0 ){
			$(out_amount).each(function(){
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
		str+='<input type="button"  name="bntDel" value="删除" style="color: red;" onclick="deleteTr(this);" class="normal_btn" />';
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
		var url="<%=contextPath%>/OrderBalanceAction/roBalance.json";
		makeNomalFormCall1(url,roDoBalanceBack,"fm");
	}
	function roDoBalanceBack(json){
		MyAlert(json.BALANCE_SUCCESS);
		var url='<%=contextPath%>/OrderBalanceAction/orderBalanceList.do';
		window.location.href=url;
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
		var vin = document.getElementById("vin").value;
		var ro_type = document.getElementsByName('ro_type');
		var ro_type_dis = '';
		for(var  i = 0 ; i < ro_type.length; i++ ){
		   if(ro_type[i].value == '93331002'){
		      ro_type_dis+= '93331002,';//模板
		   }if(ro_type[i].value == '93331003'){
			   ro_type_dis+='93331003,';//自费
		   }
		}
		
		
		sendAjax('<%=contextPath%>/OrderBalanceAction/roCancelBalanceAudit.json?id='+id+'&ro_no='+roNo+"&check_status=1"+'&vin='+vin+'&ro_type='+ro_type_dis,auditBackInfo,'fm');
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
		$("#keep_fit_no").val("");
		var vin=$("#vin").val();
		var in_mileage=$("#in_mileage").val();
		$("#tab_2 tr:gt(1)").remove();
		$("#tab_2_1 tr:gt(0)").remove();
 		OpenHtmlWindow('<%=contextPath%>/OrderAction/keepFitTemplate.do?vin='+vin+'&&in_mileage='+in_mileage,800,400);
	}
	function checkMileage(){
		var vin=$("#vin").val();
		var reg = /^\d+$/;
		var val=$("#in_mileage").val();
		var temp=0;
		if(""==$("#vin").val()){
			MyAlert("提示：请先输入VIN，再输入里程！");
			$("#in_mileage").val("");
			temp++;
			return;
		}
		if(""!=val && !reg.test(val)){
			MyAlert("提示：请输入正确的数据！");
			$("#in_mileage").val("");
			temp++;
			return;
		}
		
		if(temp==0){
			sendAjax('<%=contextPath%>/OrderAction/checkMileage.json?vin='+vin+'&mileage='+val,checkMileageBack,'fm');
		}
		if($("#in_mileage").attr("tempVal")!=""&&$("#in_mileage").attr("tempVal")!=$("#in_mileage").val()){
			MyAlert("提示：如需要修改里程，则先会删除当前正常维修数据，再重新添加配件判断三包规则！");
			$("#tab_1,#tab_1_1").remove();
		}
		
	}
	function checkMileageBack(json){
		var type=$("#type").val();
		if(json.res!=""){
			MyAlert(json.res);
			$("#in_mileage").val("");
		}else{
			if(type=="add"){
				$("#in_mileage").attr("tempVal",$("#in_mileage").val());
			}
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
			$(price).focus();
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
			$(price).focus();
		}else{
			$(price).removeAttr("readonly").css("color","green");
			MyAlert("提示：当前付费方式为自费，可以修改当列的单价，进行重新计算金额！");
		}
	}
	function ro_out_add(){
		$("#bntOut").attr("disabled",true);
		var str='';
		str+='<table id="outData" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">';
		str+='<th colspan="8">';
		str+='<img class="nav" src="../jsp_new/img/subNav.gif" />外出服务&nbsp;&nbsp;<input type="button" class="normal_btn" value="删除" onclick="delPart(this,5);"';
		str+='</th>';
		str+='<tr>';
		str+='	<td nowrap="true" width="40%" >外出地点</td>';
		str+='	<td nowrap="true" width="20%" >外出里程(KM单程)</td>';
		str+='	<td nowrap="true" width="20%" >付费方式</td>';
		str+='		<td nowrap="true" width="20%" >外出费用</td>';
		str+='</tr>';
		str+='	<tr>';
		str+='		<td nowrap="true" width="20%" >';
		str+='<input name="address" size="65" maxlength="100" value=""/>';
		str+='</td>';
		str+='<td nowrap="true" width="20%" >';
		str+='<input  name="out_mileage" class="middle_txt" value=""/>';
		str+='</td>';
		str+='<td nowrap="true" width="20%" >';
		str+='	<select class="short_sel" name="pay_type" >';
		str+='		<option value="自费">自费</option>';
		str+='		<option value="索赔">索赔</option>';
		str+='	</select>';
		str+='</td>';
		str+='<td nowrap="true" width="20%" >';
		str+='<input  name="out_amount" class="middle_txt" value=""/>';
		str+='</td>';
		str+='</tr>';
		str+='</table>';
		$("#new_add").append(str);
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
<input class="middle_txt" id="keep_fit_no" value="${ro.KEEP_FIT_NO }" name="keep_fit_no" type="hidden"  /> 
<input class="middle_txt" id="is_visit" value="${ro.IS_VISIT }" name="is_visit" type="hidden"  /> 
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
    		<input class="middle_txt" id="vin" value="${ro.VIN }"  name="vin"  type="text" maxlength="30" />
    		<div id="divTemp" style="position:absolute;background-color:#000; filter:alpha(Opacity=50); opacity: 0.5;  left:235px; top:100px; width:155px; height:100px;display: none">
    			<table id='vinDiv'  border="1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;color:blue;">
    		</table>
    		</div>
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
    		<div id="divLicense" style="position:absolute;background-color:#000; filter:alpha(Opacity=50); opacity: 0.5; left:235px; top:122px; width:155px; height:80px;display: none">
    			<table id='licenseDiv'  border="1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;color:blue;">
    		</table>
    		</div>
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
    		<input class="middle_txt" tempVal="${ro.IN_MILEAGE }" id="in_mileage" value="${ro.IN_MILEAGE }"   name="in_mileage" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" >保养次数:</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="free_times" value="${ro.FREE_TIMES }" readonly="readonly" name="free_times" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" class="show_activity" style="display: none">活动编码：</td>
    	<td nowrap="true" width="15%" class="show_activity" style="display: none">
    		<input class="middle_txt" id="cam_code" readonly="readonly" value="${ro.CAM_CODE }" name="cam_code" type="text" maxlength="30" />
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr id="time_show" style="display: none;">
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >工单开始时间：</td>
    	<td nowrap="true" width="15%" >
			<input name="ro_create_date" type="text" id="ro_create_date" value="<fmt:formatDate value="${ro.RO_CREATE_DATE }" pattern='yyyy-MM-dd HH:mm:ss'/>" readonly="readonly" class="middle_txt"/>
		</td>
	    <td nowrap="true" width="10%" >工单结算时间：</td>
    	<td nowrap="true" width="15%" >
			<input type="text" id="for_balance_time" name="for_balance_time" maxlength="16"  value="<fmt:formatDate value="${ro.FOR_BALANCE_TIME }" pattern='yyyy-MM-dd HH:mm:ss'/>" readonly="readonly" class="middle_txt"/>
    	</td>
    	<td nowrap="true" width="10%">
    	  
    	</td>
    	<td nowrap="true" width="15%">
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
			<textarea rows="" cols="" id="deliverer_adress" name="deliverer_adress" maxlength="30"  readonly="readonly" >${ro.DELIVERER_ADRESS }</textarea>
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
		<td nowrap="true" width="10%">
    	<div style="display: none;"  class="dealer">
		 服务站简称:
    	</div>
		 </td>
    	<td nowrap="true" width="15%">
    	<div style="display: none ;" class="dealer">
    	     <input type="text" id="DEALER_SHORTNAME" name="DEALER_SHORTNAME" maxlength="16"  value="${ro.DEALER_SHORTNAME }" readonly="readonly" class="middle_txt"/>
    	</div>
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
    		<%-- &nbsp;&nbsp;<span style="color: red;">费用合计: </span><span style="color: red; font-weight: bold;" id="count_span">${ro.BALANCE_AMOUNT }</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button"  name="bntCount"  id="bntCount"  value="合计" onclick="ro_type_count();" class="normal_btn" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;提示：[点击合计可以计算当前页面的总费用] --%>
    		&nbsp;<input type="button"  name="bntAcc"  id="bntAcc"  value="辅料添加" onclick="ro_acc_add();" class="normal_btn" />&nbsp;&nbsp;<input type="button"  name="bntOut"  id="bntOut"  value="外出服务" onclick="ro_out_add();" class="normal_btn" />
    		</div>
    		</span>
    		<br/>
    		<div id="new_add"  style="text-align: center; width: 100%;">
    			<c:if test="${parts1!=null}">
	    		<table border="1" id="tab_1" cellpadding="1" class="table_edit"  cellspacing="1"  width="100%"  style="text-align: center;">
					<th colspan="7" nowrap="true" width="80%" ><input type="hidden" name="ro_type" value="93331001">
					<img class="nav" src="../jsp_new/img/subNav.gif" />正常维修&nbsp;&nbsp;
					<c:if test="${ro.CAM_CODE!=null}">
					</c:if>
					<c:if test="${ro.CAM_CODE==null}">
					<input type="button"  value="删除" onclick="delPart(this,93331001);" class="normal_btn" />
					</c:if>
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
					<td nowrap="true" width="10%" >新件代码</td>
					<td nowrap="true" width="35%" >新件名称</td>
					<td nowrap="true" width="2%" >新件数量</td>
					<td nowrap="true" width="3%" >单价</td>
					<td nowrap="true" width="10%" >金额（元）</td>
					<td nowrap="true" width="10%" >付费方式</td>
					<td nowrap="true" width="10%" >维修方式</td>
					<td nowrap="true" width="10%" >
						<c:if test="${ro.CAM_CODE!=null}">
						</c:if>
						<c:if test="${ro.CAM_CODE==null}">
							<input type="button"  name="bntAdd" value="添加" onclick="addPart(93331001);" class="normal_btn" />
						</c:if>
					</td>
					</tr>
	    			 <c:forEach items="${parts1 }" var="p1" varStatus="status">
						<tr>
						<td nowrap="true" width="10%" >${status.index+1}</td>
						<td nowrap="true" width="15%" >
						<input name="is_gua_1" type="hidden" class="middle_txt" value="${p1.IS_GUA }"/>
						<input name="part_id_1" type="hidden" class="middle_txt" value="${p1.REAL_PART_ID }"/>
						<input name="part_camcode" type="hidden"  value="${p1.PART_CAMCODE }"/>
						<input name="ro_repair_part_1" type="hidden" class="middle_txt" value="${p1.ID }"/>
						<input name="part_code_1" type="hidden" readonly="readonly"  value="${p1.PART_NO }"/><span>${p1.PART_NO }</span>
						</td>
						<td nowrap="true" width="15%" >
						<input name="part_name_1" readonly="readonly"type="hidden"  value="${p1.PART_NAME }"/><span>${p1.PART_NAME }</span>
						</td>
						<td nowrap="true" width="10%" >
						<c:if test="${p1.PART_CAMCODE==1}">
							<input type="text" name="part_quotiety_1" size="10"  style="color: green;"  readonly="readonly"   value="${p1.PART_QUANTITY }"/>
						</c:if>
						<c:if test="${p1.PART_CAMCODE!=1}">
							<input type="text" name="part_quotiety_1" size="10"  style="color: green;" onblur="insertNum(this);"  value="${p1.PART_QUANTITY }"/>
						</c:if>
						</td>
						<td nowrap="true" width="10%" >
						<c:if test="${p1.PAY_TYPE==11801001}">
							<input type="text" name="claim_price_param_1" tempVal="${p1.PART_COST_PRICE }" size="10" style="color: green;"  onblur="changePrice(this);" value="${p1.PART_COST_PRICE }"/>
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
						<c:if test="${p1.PART_CAMCODE==1}">
							<input name="pay_type_1" type="hidden"  value="11801002"/><span>索赔</span>
						</c:if>
						<c:if test="${p1.PART_CAMCODE!=1}">
							<select class="min_sel" name="pay_type_1" onchange="changeInputByFree(this);">
								<option value="11801002" selected="selected">索赔</option>
								<option value="11801001">自费</option>
							</select>
						</c:if>
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
						<c:if test="${p1.PART_CAMCODE==1}">
							服务活动
						</c:if>
						 <c:if test="${p1.PART_CAMCODE!=1}">
							<input type="button"  name="bntDel" style="color: red;" value="删除" onclick="deleteTrByNormal(this);" class="normal_btn" />
						</c:if>
						</td>
						</tr>
	    			 </c:forEach>
	    		</table>
    			<table border="1" id="tab_1_1" cellpadding="1" class="table_edit"  cellspacing="1" width="100%" style="text-align: center;">
					<tr>
					<td nowrap="true" width="10%" >维修工时</td>
					<td nowrap="true" width="15%" >作业代码</td>
					<td nowrap="true" width="25%" >作业名称</td>
					<td nowrap="true" width="10%" >工时定额</td>
					<td nowrap="true" width="10%" >工时单价</td>
					<td nowrap="true" width="10%" >金额（元）</td>
					<td nowrap="true" width="10%" >付费方式</td>
					<td nowrap="true" width="10%">
					<c:if test="${ro.CAM_CODE!=null}">
					</c:if>
					<c:if test="${ro.CAM_CODE==null}">
							<input type="button"  name="bntAdd" value="添加" onclick="addLabour(93331001);" class="normal_btn" />
					</c:if>
					</td>
					</tr>
	    			 <c:forEach items="${labours1 }" var="l1" varStatus="status">
			           <tr>
			           <td nowrap="true" width="10%" >${status.index+1}</td>
			           <td nowrap="true" width="15%" >
			           <input name="lab_camcode" type="hidden"  value="${l1.LAB_CAMCODE}"/>
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
			           <input type="text" name="parameter_value_1" size="10" tempVal="${l1.LABOUR_PRICE }" style="color: green;"  onblur="changePrice(this);" value="${l1.LABOUR_PRICE }"/>
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
			            	<c:if test="${l1.LAB_CAMCODE==1}">
								<input name="pay_type_labour_1" type="hidden"  value="11801002"/><span>索赔</span>
							</c:if>
							<c:if test="${l1.LAB_CAMCODE!=1}">
								<select class="min_sel" name="pay_type_labour_1" onchange="changeInputByFreeL(this);">
								  <option value="11801002"  selected="selected">索赔</option>
								  <option value="11801001">自费</option>
								</select>
							</c:if>
						</c:if>
						  <c:if test="${l1.PAY_TYPE==11801001}">
			           <select class="min_sel" name="pay_type_labour_1" onchange="changeInputByFreeL(this);">
						  <option value="11801002">索赔</option>
						  <option value="11801001" selected="selected">自费</option>
						</select>
						</c:if>
			           </td>
			           <td nowrap="true" width="10%">
			           <c:if test="${l1.LAB_CAMCODE==1}">
							服务活动
						</c:if>
						<c:if test="${l1.LAB_CAMCODE!=1}">
							<input type="button"  name="bntDel" value="删除" style="color: red;" onclick="deleteTr(this);" class="normal_btn" />
						</c:if>
			           </td>
			           </tr>
	    			 </c:forEach>
	    		</table>
    			</c:if>
    			<c:if test="${parts2!=null}">
	    		<table border="1" id="tab_2" cellpadding="1" cellspacing="1" class="table_edit"   width="100%" style="text-align: center;">
					<th colspan="9"><input type="hidden" name="ro_type" value="93331002">
					<img class="nav" src="../jsp_new/img/subNav.gif" />免费保养 &nbsp;&nbsp;<input type="button"  value="删除" onclick="delPart(this,93331002);" class="normal_btn" /></th>
					<tr>
					<td nowrap="true" width="10%" >维修配件</td>
					<td nowrap="true" width="10%" >新件代码</td>
					<td nowrap="true" width="35%" >新件名称</td>
					<td nowrap="true" width="2%" >新件数量</td>
					<td nowrap="true" width="3%" >单价</td>
					<td nowrap="true" width="10%" >金额（元）</td>
					<td nowrap="true" width="10%" >付费方式</td>
					<td nowrap="true" width="10%" >维修方式</td>
					<td nowrap="true" width="10%" >
					</td>
					</tr>
	    			 <c:forEach items="${parts2 }" var="p2" varStatus="status">
						<tr>
						<td nowrap="true" width="10%" >${status.index+1}</td>
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
	    			 <c:forEach items="${labours2 }" var="l2" varStatus="status">
			           <tr>
			           <td nowrap="true" width="10%" >${status.index+1}</td>
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
					<td nowrap="true" width="10%" >新件代码</td>
					<td nowrap="true" width="35%" >新件名称</td>
					<td nowrap="true" width="2%" >新件数量</td>
					<td nowrap="true" width="3%" >单价</td>
					<td nowrap="true" width="10%" >金额（元）</td>
					<td nowrap="true" width="10%" >付费方式</td>
					<td nowrap="true" width="10%" >维修方式</td>
					<td nowrap="true" width="10%" >
					<input type="button"  name="bntAdd" value="添加" onclick="addPart(93331003);" class="normal_btn" />
					</td>
					</tr>
	    			 <c:forEach items="${parts3 }" var="p3" varStatus="status">
						<tr>
						<td nowrap="true" width="10%" >${status.index+1}</td>
						<td nowrap="true" width="15%" >
						<input name="ro_repair_part_3" type="hidden" class="middle_txt" value="${p3.ID }"/>
						<input name="part_id_3" type="hidden"   value="${p3.REAL_PART_ID }"/>
						<input name="part_code_3" readonly="readonly" type="hidden" value="${p3.PART_NO }"/><span>${p3.PART_NO }</span>
						</td>
						<td nowrap="true" width="15%" >
						<input name="part_name_3" readonly="readonly" type="hidden" value="${p3.PART_NAME }"/><span>${p3.PART_NAME }</span>
						</td>
						<td nowrap="true" width="10%" >
						<input type="text" name="part_quotiety_3" onblur="insertNum(this);" style="color: green;" size="10" value="${p3.PART_QUANTITY }"/>
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
						<input type="button"  name="bntDel" value="删除" style="color: red;" onclick="deleteTr(this);" class="normal_btn" />
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
	    			 <c:forEach items="${labours3 }" var="l3" varStatus="status">
			           <tr>
			           <td nowrap="true" width="10%" >${status.index+1}</td>
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
			           <input type="button"  name="bntDel" value="删除" style="color: red;" onclick="deleteTr(this);" class="normal_btn" />
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
						<img class="nav" src="../jsp_new/img/subNav.gif" />辅料项目&nbsp;&nbsp;<input type="button" class="normal_btn" value="删除" onclick="delPart(this,4);">
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
						<input type="button"  name="bntDel" value="删除" style="color: red;" onclick="deleteTr(this);" class="normal_btn" />
						</td></tr>
    			 </c:forEach>
    			 </table>
    		</c:if>
    		<c:if test="${outData!=null}">
    				<table id="outData" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
					<th colspan="8">
						<img class="nav" src="../jsp_new/img/subNav.gif" />外出服务&nbsp;&nbsp;<input type="button" class="normal_btn" value="删除" onclick="delPart(this,5);">
					</th>
					<tr>
						<td nowrap="true" width="40%" >外出地点</td>
						<td nowrap="true" width="20%" >外出里程(KM往返)</td>
						<td nowrap="true" width="20%" >付费方式</td>
						<td nowrap="true" width="20%" >外出费用</td>
					</tr>
					<c:forEach items="${outData }" var="out">
	    				<tr>
		    				<td nowrap="true" width="20%" >
								<input name="address" size="65" maxlength="100" value="${out.ADDRESS}"/>
							</td>
							<td nowrap="true" width="20%" >
							<input  name="out_mileage" class="middle_txt" value="${out.MILEAGE }"/>
							</td>
							<td nowrap="true" width="20%" >
								<select class="short_sel" name="pay_type" >
									<option value="${out.PAY_TYPE  }">${out.PAY_TYPE  }</option>
								</select>
							</td>
							<td nowrap="true" width="20%" >
								<input readonly="readonly" name="out_amount" class="middle_txt" value="${out.OUT_AMOUNT }"/>
							</td>
						</tr>
					</c:forEach>
    			</table>
    		</c:if>
</div>
<br>
<div  id="balanceShow">
	<table  width=100% border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td nowrap="true" width="10%"></td>
			<td nowrap="true" width="10%">&nbsp;&nbsp;合计：&nbsp;&nbsp;
				<input readonly="readonly" id="ro_balance_amount" class="middle_txt" value="${ro.BALANCE_AMOUNT }"/>
			</td>
			<td nowrap="true" width="10%">&nbsp;&nbsp;索赔：&nbsp;&nbsp;
				<input readonly="readonly" id="ro_balance_amount" class="middle_txt" value="${ro.ESTIMATE_AMOUNT }"/>
			</td>
			<td nowrap="true" width="10%">&nbsp;&nbsp;自费：&nbsp;&nbsp;
				<input readonly="readonly" id="ro_balance_amount" class="middle_txt" value="${ro.DERATE_AMOUNT }"/>
			</td>
			<td nowrap="true" width="10%">&nbsp;&nbsp;折扣：&nbsp;&nbsp;
				<input  name="discount_amount" class="middle_txt" value="${ro.DISCOUNT_AMOUNT }"/>
			</td>
			<td nowrap="true" width="10%">&nbsp;&nbsp;实收：&nbsp;&nbsp;
				<input readonly="readonly" id="real_amount" name="real_amount" class="middle_txt" value="${ro.REAL_AMOUNT }"/>
			</td>
			<td nowrap="true" width="10%"></td>
		</tr>
	</table>
</div>
<br/>
<table width=100% border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
               	&nbsp;&nbsp;
               	<input class="normal_btn" type="button" id="btn1" value="维修历史" onclick="maintaimHistory();"/>&nbsp;&nbsp;
                <input class="normal_btn" type="button" id="btn3" value="保养历史" onclick="freeMaintainHistory();"/>&nbsp;&nbsp;
                <input type="button" class="normal_btn"  id="balance"  onclick="doRepairBalance();" style="width=8%" value="结算" />&nbsp;&nbsp;
				<input type="button" id="noBalance"  class="normal_btn"  style="width=8%" onclick="roDoCancelBalance();" value="取消结算"/>&nbsp;&nbsp;
				<input type="button" id="auditPass"  class="normal_btn"  style="width=8%" onclick="roDoCancelBalanceAudit(1);" value="通过"/>&nbsp;&nbsp;
				<input type="button" id="auditUnPass"  class="normal_btn"  style="width=8%" onclick="roDoCancelBalanceAudit(2);" value="驳回"/>&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="sure" onclick="sureInsert();"  style="width=8%" value="确定" />&nbsp;&nbsp;
				<input type="button" id="back" onClick="history.back();" class="normal_btn"  style="width=8%" value="返回"/>
   			</td>
           	<td height="12" align=center width="33%">
     			</td>
		</tr>
</table>
</form>
</body>
</html>