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
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>

<title>保养索赔单管理</title>
<script type="text/javascript">
	$(function(){
		var type=$("#type").val();
		if("add"==type){
			$("#claim_no").val("   新增后自动生成 ");
			$("#ro_no").val("   请点击选择 ");
			$("#vin").live("blur",function(){
				var vin=$("#vin").val();
				if(vin!=""){
					sendAjax('<%=contextPath%>/ClaimAction/showInfoByVin.json?vin='+vin,backByVin,'fm');
				}
			});
		}
		if("update"==type){
			
		}
		if("view"==type){
			$("input[type='text']").attr("readonly","readonly");
			$("textarea").attr("readonly","readonly");
			$("#report,#sure,#sureUnpass,#sureUndo,#surePass").hide();
			$(".normal_btn").each(function(){
				 if($(this).val()=="删 除" || $(this).val()=="添加附件"){
					 $(this).hide();
				} 
			});
		}
	});
	function backByVin(json){
		var t=json.info;
		if(null==t){
			$("#color").val("");
			$("#engine_no").val("");
			$("#package_name").val("");
			$("#model_name").val("");
			$("#model_id").val("");
			$("#guarantee_date").val("");
		}else{
			$("#color").val(t.COLOR);
			$("#engine_no").val(t.ENGINE_NO);
			$("#package_name").val(t.PACKAGE_NAME);
			$("#model_name").val(t.MODEL_NAME);
			$("#model_id").val(t.MODEL_ID);
			$("#guarantee_date").val(t.PURCHASED_DATE_ACT);
		}
	}
	function sureConfirm(identify){
		var temp=0;
		disableBtns();
		if(""==$("#ro_no").val()){
			MyAlert("提示：工单信息丢失！");
			temp++;
			return;
		}
		if(""==$("#vin").val()){
			MyAlert("提示：车辆信息丢失！");
			temp++;
			return;
		}
		if(""==$("#model_name").val()){
			MyAlert("提示：车型为空，车辆信息丢失！");
			temp++;
			return;
		}
		if(""==$("#color").val()){
			MyAlert("提示：颜色为空，车辆信息丢失！");
			temp++;
			return;
		}
		if(""==$("#package_name").val()){
			MyAlert("提示：配置为空，车辆信息丢失！");
			temp++;
			return;
		}
		if(""==$.trim($("#bntCountnew").val())){
		    MyAlert("提示：审核金额不能为空！");
		    temp++;
			return;
		}
		//验证审核金额
		 var Countnew = $("#bntCountnew").val();
		 var reg = /^[0-9]+([.]{1}[0-9]{1,2})?$/;
			if(""!=Countnew && !reg.test(Countnew)){
			    MyAlert("提示：请输入正确的金额！");
				$("#bntCountnew").val("");
				temp++;
				return;
			}
			if(parseFloat($.trim($("#bntCount1").val()))<parseFloat($.trim($("#bntCountnew").val()))){
				   MyAlert("提示：审批金额不能大于申请金额！");
				   temp++;
				   return;
			}
			if(0.0>parseFloat($.trim($("#bntCountnew").val()))){
				   MyAlert("提示：审批金额不能小于0！");
			       temp++;
				   return;
			}
		
	
		
		
		
		
		var url="<%=contextPath%>/ClaimBalanceAction/fitAudit.json";
		var id=document.getElementById("id").value;
		var status=document.getElementById("status").value;
		var audit_remark=document.getElementById("AUDIT_REMARK").value;
		var part_amont_2 = document.getElementsByName("part_amont_2");
		if(temp==0){
			if(0==identify){
				url = url+"?identify=0";
				MyUnCloseConfirm("确定要审核通过?", function(identify){
					sendRequest(url);
				}, [identify], enableBtns);
			}
			if(1==identify){
				if(""==audit_remark){
				    enableBtns();
                   MyAlert("提示：审核退回必须填写备注！");
                   return;
				}
				url = url+"?identify=1";
				MyUnCloseConfirm("确定要审核退回?", function(identify){
					sendRequest(url);
				}, [identify], enableBtns);
			}
			if(2==identify){
				url = url+"?identify=2";
				MyUnCloseConfirm("确定要撤销审核?", function(identify){
					sendRequest(url);
				}, [identify], enableBtns);
			}
		}
	}

	function enableBtns(){
		if(null!=document.getElementById("surePass") &&
				"undefined"!= typeof (document.getElementById("surePass").disabled))
			document.getElementById("surePass").disabled=false;
		if(null!=document.getElementById("sureUnpass") &&
				"undefined"!= typeof (document.getElementById("sureUnpass").disabled))
			document.getElementById("sureUnpass").disabled=false;
		if(null!=document.getElementById("sureUndo") &&
				"undefined"!= typeof (document.getElementById("sureUndo").disabled))
			document.getElementById("sureUndo").disabled=false;
	}
	
	function disableBtns(){
		if(null!=document.getElementById("surePass") &&
				"undefined"!= typeof (document.getElementById("surePass").disabled))
			document.getElementById("surePass").disabled=true;
		if(null!=document.getElementById("sureUnpass") &&
				"undefined"!= typeof (document.getElementById("sureUnpass").disabled))
			document.getElementById("sureUnpass").disabled=true;
		if(null!=document.getElementById("sureUndo") &&
				"undefined"!= typeof (document.getElementById("sureUndo").disabled))
			document.getElementById("sureUndo").disabled=true;
	}
	
	function sendRequest(url) {
		makeNomalFormCall1(url, function(json){
			if (json.Exception) {
				MyUnCloseAlert(json.Exception.message, enableBtns);
			} else {
				MyUnCloseAlert(json.info, function(){
					window.location.href = g_webAppName + '/ClaimBalanceAction/claimBalanceList.do';
				});
			}
		},'fm','');
	}

	function backInfo(json){
		if (json.Exception) {
			MyUnCloseAlert(json.Exception.message, enableBtns);
		} else {
			MyUnCloseAlert(json.info.message, function(){
				window.location.href = g_webAppName + '/ClaimBalanceAction/claimBalanceList.do';
			});
		}
	}
	
	function ro_type_count(){
		var count_part=0.0;
		var part_amont_2=$("input[name='part_amont_2']");
		if(part_amont_2.length>0 ){
			$(part_amont_2).each(
				function(){
					count_part+=parseFloat($(this).val());
				});
		}
		
		var count_labour=0.0;
		var labour_fix_2=$("input[name='labour_fix_2']");
		if(labour_fix_2.length>0 ){
			$(labour_fix_2).each(
				function(){
					count_labour+=parseFloat($(this).val());
				});
		}
		var count_all=(count_part*1000/1000+count_labour*1000/1000);
		var winter_money=$("#winter_money").val();
		if(undefined!=winter_money&&null!=winter_money&&""!=winter_money){
			count_all=count_all+parseFloat(winter_money);
		}
		$("#count_span").text(count_all.toFixed(2));
	}
	function showInfo(){
		var vin=$("#vin").val();
		if(""==vin){
			MyAlert("提示：请输入VIN在选择工单号！");
			return;
		}
		OpenHtmlWindow('<%=contextPath%>/ClaimAction/findRoKeepFit.do?vin='+vin,800,500);
	}
	
	function backKeepFitData(labours2,parts2,ro_no){
		$("#ro_no").val(ro_no);
		$(".deleteTr").remove();
		var tab=$("#tab_2");
		var str="";
		for(var i = 0;i < parts2.length;i++){
			str+='<tr class="deleteTr">';
			str+='<td nowrap="true" width="10%" >配件>></td>';
			str+='<td nowrap="true" width="15%" >';
			str+='<input name="part_id_2" type="hidden" class="middle_txt" value="'+parts2[i].REAL_PART_ID+'"/>';
			str+='<input name="part_code_2" readonly="readonly" type="hidden" value="'+parts2[i].PART_NO+'"/>';
			str+='<span>'+parts2[i].PART_NO+'</span>';
			str+='</td>';
			str+='<td nowrap="true" width="15%" >';
			str+='<input name="part_name_2" readonly="readonly" type="hidden" value="'+parts2[i].PART_NAME+'"/>';
			str+='<span>'+parts2[i].PART_NAME+'</span>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input name="part_quotiety_2"  size="10" type="hidden"  value="'+parts2[i].PART_QUANTITY+'"/>';
			str+='<span>'+parts2[i].PART_QUANTITY+'</span>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input name="claim_price_param_2" readonly="readonly" type="hidden" size="10" value="'+parts2[i].PART_COST_PRICE+'"/>';
			str+='<span>'+parts2[i].PART_COST_PRICE+'</span>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input readonly="readonly" name="part_amont_2" type="hidden" size="10" value="'+parts2[i].PART_COST_AMOUNT+'"/>';
			str+='<span>'+parts2[i].PART_COST_AMOUNT+'</span>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" ><select class="min_sel" name="pay_type_2" type="hidden">';
			if("11801002"==parts2[i].PAY_TYPE){
				str+='<option value="11801002">索赔</option>';
			}else{
				str+='<option value="11801001">自费</option>';
			}
			str+='</select></td>';
			if("11801002"==parts2[i].PAY_TYPE){
				str+='<span>索赔</span>';
			}else{
				str+='<span>自费</span>';
			}
			str+='<td nowrap="true" width="10%" ><select class="min_sel" name="part_use_type_2" type="hidden">';
			if("95431002"==parts2[i].PART_USE_TYPE){
				str+='<option value="95431002">更换</option>';
			}else{
				str+='<option value="95431001">维修</option>';
			}
			str+='</select></td>';
			str+='</tr>';
		}
		tab.append(str);
		var tab1=$("#tab_2_1");
		var str1="";
		for(var j = 0;j < labours2.length;j++){
			str1+='<tr class="deleteTr">';
			str1+='<td nowrap="true" width="10%" >工时>></td>';
			str1+='<td nowrap="true" width="15%" >';
			str1+='<input readonly="readonly" name="labour_code_2"  value="'+labours2[j].LABOUR_CODE+'"/>';
			str1+='</td>';
			str1+='<td nowrap="true" width="25%" >';
			str1+='<input name="cn_des_2" readonly="readonly" size="35" value="'+labours2[j].LABOUR_NAME+'"/>';
			str1+='</td>';
			str1+='<td nowrap="true" width="10%" >';
			str1+='<input name="labour_quotiety_2" readonly="readonly" size="10" value="'+labours2[j].STD_LABOUR_HOUR+'"/>';
			str1+='</td>';
			str1+='<td nowrap="true" width="10%" >';
			str1+='<input name="parameter_value_2" size="10" readonly="readonly" value="'+labours2[j].LABOUR_PRICE+'"/>';
			str1+='</td>';
			str1+='<td nowrap="true" width="10%" >';
			str1+='<input name="labour_fix_2" readonly="readonly" size="10" value="'+labours2[j].LABOUR_AMOUNT+'"/>';
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
			str1+='</tr>';
		}
		tab1.append(str1);
	}
	
	function goBack(){
		var goBackType = $("#goBackType").val();
		if("2" == goBackType ){
			_hide();
		}else{
			history.back();
		}
		
	}
</script>
</head>
<body>
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;经销商索赔单管理&gt;保养索赔单管理
</div>
<form name="fm" id="fm" method="post">
<input class="middle_txt" id="type" value="${type }" name="type" type="hidden"  />
<input class="middle_txt" id="id" value="${t.ID }" name="id" type="hidden"  />
<input class="middle_txt" id="model_id" value="${t.MODEL_ID }" name="model_id" type="hidden"  />
<input class="middle_txt" id="status" value="${t.STATUS }" name="status" type="hidden"  />
<input class="middle_txt" id="goBackType" value="${goBackType }" name="goBackType" type="hidden"  />

<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
	<th colspan="8">
		<img class="nav" src="../jsp_new/img/subNav.gif"/>基本信息
	</th>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" align="right">工单号：</td>
    	<td nowrap="true" width="15%" align="left">
    		<input class="middle_txt" id="ro_no" onclick="showInfo();" value="${t.RO_NO }" type="hidden" readonly="readonly" name="ro_no" maxlength="30" />
    		<span>${t.RO_NO }</span>
    	</td>
    	<td nowrap="true" width="10%" align="right" >保养单号：</td>
    	<td  width="15%" align="left">
    		<input class="middle_txt" id="claim_no"   value="${t.CLAIM_NO }" readonly="readonly" name="claim_no" type="hidden" maxlength="30" />
    		<span>${t.CLAIM_NO }</span>
    	</td>
		<td nowrap="true" width="10%" align="right">VIN：</td>
    	<td nowrap="true" width="15%" align="left">
    		<input class="middle_txt" id="vin" value="${t.VIN }"  name="vin" type="hidden" maxlength="30" />
    		<span>${t.VIN }</span>
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" align="right">车型：</td>
    	<td nowrap="true" width="15%" align="left">
    		<input class="middle_txt" id="model_name" value="${t.MODEL_NAME }" readonly="readonly" name="model_name" type="hidden" maxlength="30" />
    		<span>${t.MODEL_NAME }</span>
    	</td>
		<td nowrap="true" width="10%" align="right">配置：</td>
    	<td nowrap="true" width="15%" align="left">
    		<input class="middle_txt" id="package_name" value="${t.APP_PACKAGE_NAME  }" readonly="readonly" name="package_name" type="hidden" maxlength="30" />
    		<span>${t.APP_PACKAGE_NAME }</span>
    	</td>
    	<td nowrap="true" width="10%" align="right">颜色：</td>
    	<td nowrap="true" width="15%" align="left">
    		<input class="middle_txt" id="color" value="${t.APP_COLOR  }" readonly="readonly" name="color" type="hidden" maxlength="30" />
    		<span>${t.APP_COLOR }</span>
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" align="right">购车日期：</td>
    	<td nowrap="true" width="15%" align="left">
    		<input class="middle_txt" id="guarantee_date" value="<fmt:formatDate value="${t.GUARANTEE_DATE}" pattern="yyyy-MM-dd"/>" readonly="readonly" name="guarantee_date" type="hidden" maxlength="30" />
    		<span><fmt:formatDate value="${t.GUARANTEE_DATE}" pattern="yyyy-MM-dd"/></span>
    	</td>
    	<td nowrap="true" width="10%" align="right">发动机号：</td>
    	<td nowrap="true" width="15%" align="left">
    		<input class="middle_txt" id="engine_no" value="${ t.ENGINE_NO }" readonly="readonly" name="engine_no" type="hidden" maxlength="30" />
    		<span>${t.ENGINE_NO }</span>
    	</td>
		<td nowrap="true" width="10%" align="right">里程：</td>
    	<td nowrap="true" width="15%" align="left">
    	    <input class="middle_txt" id="in_mileage" value="${ t.IN_MILEAGE }" readonly="readonly" name="in_mileage" type="hidden" maxlength="30" />
    		<span>${t.IN_MILEAGE }</span>
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<c:if test="${null!=t.WINTER_MONEY}">
	   <tr class="winter_show"  >
	    <td width="12.5%"></td>
	    <td nowrap="true" width="10%" style="color: red;">冬季保养补偿费：</td>
	    <td nowrap="true" width="15%" align="left">
	       ${t.WINTER_MONEY}
	       	<input class="middle_txt" type="hidden" name="winter_money" value="${t.WINTER_MONEY}"  id="winter_money" readonly="readonly" style="color: red;" />
	    </td>
	    <td width="12.5%"></td>
	    <td width="12.5%"></td>
	    <td width="12.5%"></td>
	    <td width="12.5%"></td>
	</tr>
	</c:if>
</table>
<br>
			<div style="text-align: center;">
    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color: red;">费用合计: </span>
    		<span style="color: red; font-weight: bold;" id="count_span">${t.BALANCE_AMOUNT }</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="hidden"  name="bntCount1"  id="bntCount1"  value="${t.BALANCE_AMOUNT }"   />
    		<input type="button"  name="bntCount"  id="bntCount"  value="合计" onclick="ro_type_count();" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;提示：[点击合计可以计算当前页面的总费用]&nbsp;&nbsp;审核金额:
    		<input type="text"  name="bntCountnew"  id="bntCountnew"  value="${t.BALANCE_AMOUNT }"   />
    		</div>
    		<br>
    		<div id="new_add">
	    		<table border="1" id="tab_2" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
					<th colspan="8">
					<img class="nav" src="../jsp_new/img/subNav.gif" />免费保养 &nbsp;&nbsp;
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
					</tr>
	    			 <c:forEach items="${parts2 }" var="p2">
						<tr class="deleteTr">
						<td nowrap="true" width="10%" >配件>></td>
						<td nowrap="true" width="15%" >
						<input name="part_id_2" type="hidden"   value="${p2.REAL_PART_ID }"/>
						<input name="part_code_2" readonly="readonly" type="hidden" value="${p2.PART_CODE }"/>
						<span>${p2.PART_CODE }</span>
						</td>
						<td nowrap="true" width="15%" >
						<input name="part_name_2" readonly="readonly" type="hidden" value="${p2.PART_NAME }"/>
						<span>${p2.PART_NAME }</span>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_quotiety_2"  size="10" type="hidden" value="${p2.QUANTITY }"/>
						<span>${p2.QUANTITY }</span>
						</td><td nowrap="true" width="10%" >
						<input name="claim_price_param_2" readonly="readonly" type="hidden" size="10" value="${p2.PRICE }"/>
						<span>${p2.PRICE }</span>
						</td>
						<td nowrap="true" width="10%" >
						<input readonly="readonly" name="part_amont_2"  size="10" type="hidden" value="${p2.AMOUNT }"/>
						<span>${p2.AMOUNT }</span>
						</td>
						<td  width="10%" nowrap="true">
						<select class="min_sel" style="display:none" name="pay_type_2">
						<c:if test="${p2.PAY_TYPE==11801002}">
							<option value="11801002" selected="selected">索赔</option>
						</c:if>
						<c:if test="${p2.PAY_TYPE==11801001}">
							<option value="11801001" selected="selected">自费</option>
						</c:if>
						</select>
						<c:if test="${p2.PAY_TYPE==11801002}">
							<span>索赔</span>
						</c:if>
						<c:if test="${p2.PAY_TYPE==11801001}">
							<span>自费</span>
						</c:if>
						</td>
						<td  width="10%" nowrap="true">
						<select class="min_sel" name="part_use_type_2" style="display:none">
						<c:if test="${p2.PART_USE_TYPE==1}">
							<option value="95431002" selected="selected">更换</option>
						</c:if>
						<c:if test="${p2.PART_USE_TYPE==0}">
							<option value="95431001" selected="selected">维修</option>
						</c:if>
						</select>
						<c:if test="${p2.PART_USE_TYPE==1}">
							<span>更换</span>
						</c:if>
						<c:if test="${p2.PART_USE_TYPE==0}">
							<span>维修</span>
						</c:if>
						</td>
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
					</tr>
	    			 <c:forEach items="${labours2 }" var="l2">
			           <tr class="deleteTr">
			           <td nowrap="true" width="10%" >工时>></td>
			           <td nowrap="true" width="15%" >
			           <input readonly="readonly" name="labour_code_2" type="hidden" value="${l2.LABOUR_CODE}"/>
			           <span>${l2.LABOUR_CODE}</span>
			           </td>
			           <td nowrap="true" width="25%" >
			           <input name="cn_des_2" readonly="readonly"  size="35" type="hidden" value="${l2.LABOUR_NAME }"/>
			           <span>${l2.LABOUR_NAME}</span>
			           </td>
			           <td nowrap="true" width="10%" >
			           <input name="labour_quotiety_2" readonly="readonly" type="hidden" size="10" value="${l2.LABOUR_HOURS }"/>
			           <span>${l2.LABOUR_HOURS}</span>
			           </td>
			           <td nowrap="true" width="10%" >
			           <input name="parameter_value_2"  size="10" readonly="readonly" type="hidden" value="${l2.LABOUR_PRICE }"/>
			           <span>${l2.LABOUR_PRICE}</span>
			           </td><td nowrap="true" width="10%" >
			           <input name="labour_fix_2" readonly="readonly"  size="10" type="hidden" value="${l2.LABOUR_AMOUNT }"/>
			           <span>${l2.LABOUR_AMOUNT}</span>
			           <td nowrap="true" width="10%"  >
			           <select class="min_sel" name="pay_type_labour_2" style="display:none">
			           <c:if test="${l2.PAY_TYPE==11801002}">
							<option value="11801002" selected="selected">索赔</option>
						</c:if>
						<c:if test="${l2.PAY_TYPE==11801001}">
							<option value="11801001" selected="selected">自费</option>
						</c:if>
			           </select>
			           <c:if test="${l2.PAY_TYPE==11801002}">
							 <span>索赔</span>
						</c:if>
						<c:if test="${l2.PAY_TYPE==11801001}">
							<span>自费</span>
						</c:if>
			           </td>
			           </tr>
	    			 </c:forEach>
	    		</table>
    		</div>
<br>
<br/>
<!-- 添加附件 开始  -->
        <table id="add_file"  width="75%" class="table_info" border="0" id="file">
	    		<tr>
	        		<th>
						<input type="hidden" id="fjids" name="fjids"/>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
						<font color="red">
							<span id="span1"></span>
						</font>
			     		<input type="button" style="display:none" class="normal_btn" align="right" id="addfile" onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
					</th>
				</tr>
				<tr>
    				<td width="75%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  				</tr>
  				<%if(fileList!=null){
	  				for(int i=0;i<fileList.size();i++) { %>
					  <script type="text/javascript">
				    		addUploadRowByDbView('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
				    	</script>
				<%}}%>
			</table> 
  		<!-- 添加附件 结束 -->
		</br>
</table>
<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
<th colspan="8">
			<img class="nav" src="../jsp_new/img/subNav.gif"/>审核备注
		</th>
		<tr>
			<td height="12" align=left width="33%" colspan='2'>
				<textarea name='AUDIT_REMARK' id='AUDIT_REMARK'	datatype="1,is_textarea,100" maxlength="100" rows='2' cols='120'></textarea>
			</td>
		</tr>
</table>
<br>
<table width=100% border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
            	<c:if test="${t.STATUS==10791003 || t.STATUS==10791002}">
               	&nbsp;&nbsp;
               	<input type="button" class="cssbutton" id="surePass" onclick="sureConfirm(0);"  style="width=8%" value="审核通过" />
               	</c:if>
               	<c:if test="${t.STATUS==10791003 || t.STATUS==10791002}">
               	&nbsp;&nbsp;
               	<input type="button" class="cssbutton" id="sureUnpass" onclick="sureConfirm(1);"  style="width=8%" value="审核退回" />
               	</c:if>
               	<c:if test="${t.STATUS==10791008}">
               	&nbsp;&nbsp;
				<input type="button" id="sureUndo" onClick="sureConfirm(2);" class="cssbutton"  style="width=8%" value="撤销审核"/>
				</c:if>
               	&nbsp;&nbsp;
				<input type="button" id="back" onClick="goBack();" class="cssbutton"  style="width=8%" value="返回"/>
   			</td>
           	<td height="12" align=center width="33%">
     			</td>
		</tr>
</table>
</form>
</body>
</html>