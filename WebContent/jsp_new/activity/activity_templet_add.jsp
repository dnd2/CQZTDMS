<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head> 
<% String contextPath = request.getContextPath(); %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>

<title>服务活动模板</title>
<script type="text/javascript">
	function ro_acc_add(){
		var checkPartCode=$("input[name='part_code']").length;
	    if(checkPartCode==0){
			MyAlert("提示：正常维修的配件代码未添加，请添加后再选择添加辅料！");
			return;
		}
		$("#bntAcc").attr("disabled",true);
		var div=$('#new_acc_add');
		var str="";
		str+='<table id="accessories" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">';
		str+='<th colspan="8">';
		str+='	<img class="nav" src="../jsp_new/img/subNav.gif" />辅料费&nbsp;&nbsp;<input type="button" class="normal_btn" value="删除" onclick="delPart(this,4);"';
		str+='</th>';
		str+='<tr>';
		str+='	<td nowrap="true" width="20%" >辅料代码</td>';
		str+='	<td nowrap="true" width="20%" >辅料名称</td>';
		str+='	<td nowrap="true" width="20%" >辅料费用</td>';
		str+='<td nowrap="true" width="20%" > <input type="button"  name="bntAdd" value="添加" onclick="addAccessories();" class="normal_btn" /></td>';
		str+='</tr>';
		str+='</table>';
		str+='<br>';
		div.append(str);
	}
	function deleteTr(obj){
		$(obj).parent().parent().remove(); 
	}
	function com_add(){
		$("#bntCom").attr("disabled",true);
		var div=$('#new_com_add');
		var str="";
		str+='<table id="com" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="80%" style="text-align: center;">';
		str+='<th colspan="4">';
		str+='	<img class="nav" src="../jsp_new/img/subNav.gif" />补偿费&nbsp;&nbsp;<input type="button" class="normal_btn" value="删除" onclick="delComTable();"';
		str+='</th>';
		str+='<tr>';
		str+='	<td nowrap="true" width="20%" >补偿费申请金额</td>';
		str+='	<td nowrap="true" width="20%" >审批金额</td>';
		str+='	<td nowrap="true" width="40%" >备注</td>';
		str+='</tr>';
		str+='<tr>';
		str+='	<td nowrap="true" width="20%" >';
		str+='<input name="apply_amount" id="apply_amount"  class="middle_txt" onblur="comCheckPrice(this);" />';
		str+='</td>';
		str+='	<td nowrap="true" width="20%" >';
		str+='<input name="pass_amount" id="pass_amount" readonly="readonly"   class="middle_txt" value=""/>';
		str+='</td>';
		str+='	<td nowrap="true" width="40%" >';
		str+='<input name="remark"  maxlength="100"  size="75" value=""/>';
		str+='</tr>';
		str+='</table>';
		str+='<br>';
		div.append(str);
	}
	function addLabour(){
		OpenHtmlWindow('<%=contextPath%>/MainTainAction/addLabour.do',800,500);
	}
	function addPart(){
	    var subject_no=$("#subject_no").val();
	    if(""==$.trim(subject_no)){
			MyAlert("提示：请先选择主题编号！");
			return;
		}
		var activity_type  = $("#activity_type").val();
		<%-- if(activity_type=="10561005"){
		    OpenHtmlWindow('<%=contextPath%>/MainTainAction/addPartSpecial.do',800,500);
		}else{ --%>
		    OpenHtmlWindow('<%=contextPath%>/MainTainAction/addPart.do',800,500);
		//}
		
	}
	var object;
	function choose_old_part_code(obj){
		object=obj;
		var series_id=$("#series_id").val();
		var model_id=$("#model_id").val();
		OpenHtmlWindow('<%=contextPath%>/ClaimAction/queryOldPartCode.do?series_id='+series_id+'&model_id='+model_id,800,500);
	}
	function setOldPartCode(part_id,part_code,part_name,claim_price_param){
		var tr=$(object).parent().parent();
		tr.children("td:eq(3)").find('input').val(part_code);
	}
	function choose_producer_code(obj){
		object=obj;
		var tr= $(obj).parent().parent();
		var partcode=tr.children("td:eq(1)").find('input').val();
		OpenHtmlWindow('<%=contextPath%>/ClaimAction/supplierCodeByPartCode.do?partcode='+partcode,800,500);
	}
	function setMainSupplierCode(maker_code,maker_shotname){
		var tr=$(object).parent().parent();
		tr.children("td:eq(9)").find('input').val(maker_code);
	}
	function addAccessories(){
		var checkPartCode=$("input[name='part_code']").length;
		if(checkPartCode==0){
			MyAlert("提示：操作有误！正常维修的配件代码未添加，请不要添加后删除正常维修的配件代码再添加辅料！");
			$("#accessories").remove();
			$("#bntAcc").attr("disabled",false);
			return;
		}
		OpenHtmlWindow('<%=contextPath%>/OrderAction/accList.do',800,500);
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
		str+='<td nowrap="true" width="20%" >';
		str+='<input type="button"  name="bntDel" value="删除" onclick="deleteTr(this);" class="normal_btn" />';
		str+='</td></tr>';
		tab.append(str);
	}
	function setMainPartCode(part_id,part_code,part_name,part_price){
		var str='';
		str+='<tr>';
		str+='<td nowrap="true" width="10%" >';
		str+='配件>><input name="part_id" type="hidden" class="middle_txt" value="'+part_id+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input name="part_code" readonly="readonly" size="10" value="'+part_code+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input name="part_name" readonly="readonly" size="10" value="'+part_name+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input name="old_part_code" readonly="readonly" onclick="choose_old_part_code(this);" size="10" value="'+part_code+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input name="part_cost_price" readonly="readonly" size="10" value="'+part_price+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input name="part_quantity" onblur="insertNum(this);" size="10" value="1"/>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input name="part_cost_amount" readonly="readonly"size="10"  value="'+part_price+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" ><select class="min_sel" name="responsibility_type">';
		str+='<option value="94001001">主因件</option>';
		str+='<option value="94001002">次因件</option>';
		str+='</select></td>';
		str+='<td nowrap="true" width="10%" ><select class="min_sel" name="part_use_type">';
		str+='<option value="1">更换</option>';
		str+='<option value="0">维修</option>';
		str+='</select></td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input name="producer_code" onclick="choose_producer_code(this);" readonly="readonly" size="10" value=""/>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" ><select class="min_sel" name="is_return">';
		str+='<option value="95361001">回运</option>';
		str+='<option value="95361002">不回运</option>';
		str+='</select></td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input type="button" value="删除" onclick="deleteTr(this);" class="normal_btn" />';
		str+='</td>';
		str+='</tr>';
		$("#tab_part").append(str);
	}
	function setLabourCode(labour_code,cn_des,labour_quotiety,labour_fix,parameter_value,val){
		var str='';
		str+='<tr>';
		str+='<td nowrap="true" width="10%" >工时>></td>';
		str+='<td nowrap="true" width="20%" >';
		str+='<input name="labour_code" readonly="readonly" size="25" value="'+labour_code+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="20%" >';
		str+='<input name="labour_name" readonly="readonly" size="25" value="'+cn_des+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="20%" >';
		str+='<input name="labour_quotiety" readonly="readonly" size="25" value="'+labour_fix+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="20%" >';
		str+='<input name="labour_price" readonly="readonly" size="25" value="'+parameter_value+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="20%" >';
		str+='<input name="labour_amount" readonly="readonly" size="25" value="'+(labour_fix*parameter_value).toFixed(1)+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input type="button" value="删除" onclick="deleteTr(this);" class="normal_btn" />';
		str+='</td>';
		str+='</tr>';
		$("#tab_labour").append(str);
	}
	function delComTable(){
		$("#com").remove();
		$("#bntCom").attr("disabled",false);
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
	function sureInsert(identify){
		var temp=0;
		var templet_no=$("#templet_no").val();
		if(""==$.trim(templet_no)){
			MyAlert("提示：模板编号不能为空！");
			temp++;
			return;
		}
		var templet_name=$("#templet_name").val();
		if(""==$.trim(templet_name)){
			MyAlert("提示：模板名称不能为空！");
			temp++;
			return;
		}
		var subject_no=$("#subject_no").val();
		if(""==$.trim(subject_no)){
			MyAlert("提示：主题编号不能为空！");
			temp++;
			return;
		}
		var case_remark=$("#case_remark").val();
		if(""==$.trim(case_remark)){
			MyAlert("提示：活动方案说明不能为空！");
			temp++;
			return;
		}
		var apply_remark=$("#apply_remark").val();
		if(""==$.trim(apply_remark)){
			MyAlert("提示：索赔申请指导不能为空！");
			temp++;
			return;
		}
		var trouble_desc=$("#trouble_desc").val();
		if(""==$.trim(trouble_desc)){
			MyAlert("提示：故障描述不能为空！");
			temp++;
			return;
		}
		var trouble_reason=$("#trouble_reason").val();
		if(""==$.trim(trouble_reason)){
			MyAlert("提示：故障原因不能为空！");
			temp++;
			return;
		}
		var responsibility_type=$("select[name='responsibility_type']");
		if(responsibility_type.length==0 ){
			MyAlert("提示：请至少选择一个主因件再添加！");
			temp++;
			return;
		}
		var checkPz=0;
		var checkPc=0;
		if(responsibility_type.length>0 ){
			 checkPz=0;
			 checkPc=0;
			$("select[name='responsibility_type']").each(function(){
				var val= $(this).val();
				if(val==94001001){
					checkPz++;
				}
				if(val==94001002){
					checkPc++;
				}
			});
		}
		if(checkPz!=1){
			MyAlert("提示：索赔单上有且只有一个主因件！");
			temp++;
			return;
		}
		var producer_code=$("input[name='producer_code']");
		$(producer_code).each(function(){
			if(""==$(this).val()){
				MyAlert("提示：请选择配件的供应商！");
				temp++;
				return;
			}
		});
		if(temp==0){
			if(0==identify){
				MyConfirm("是否确认保存？",sureInsertCommit,[0]);
			}
			if(1==identify){
				MyConfirm("是否确认下发？",sureInsertCommit,[1]);
			}
		}
	}
	function sureInsertCommit(identify){
		var url="<%=contextPath%>/ActivityAction/templetAddSure.json?identify="+identify;
		makeNomalFormCall1(url,sureInsertCommitBack,"fm");
	}
	function sureInsertCommitBack(json){
		var str="";
		if(json.identify==0){
			str+="保存";
		}
		if(json.identify==1){
			str+="下发";
		}
		if(json.succ=="1"){
			MyAlert("提示："+str+"成功！");
			var url='<%=contextPath%>/ActivityAction/activityTemplet.do';
			window.location.href=url;
		}else{
			MyAlert("提示："+str+"失败！");
		}
	}
	function insertNum(obj){
		var reg = /^\d+$/;
		var val=$(obj).val();
		if(""==val && !reg.test(val)){
			MyAlert("提示：请输入正整数填入新件数量！");
			$(obj).focus(); 
		}else{
			var tr=$(obj).parent().parent();
			var nextInput=tr.children().eq(4).children();
			var nextToNextInput=tr.children().eq(6).children();
			var nextPrice=nextInput.val();
			nextToNextInput.val(nextPrice*1000*val/1000);
		}
	}
	$(function(){
		var type=$("#type").val();
		if("add"==type){
			$("#subject_no").bind("click",function(){
				relationShow();
			});
		}
		if("update"==type){
			$("#subject_no").bind("click",function(){
				relationShow();
			});
		}
		if("view"==type){
			$("input[type='button']").attr("disabled",true);
			$("input[type='text']").attr("readonly","readonly");
			$("textarea").attr("readonly","readonly");
			$("select").attr("disabled",true);
			$("#sure").hide();
			$("#back").attr("disabled",false);
		}
	});
	function relationShow(){
		OpenHtmlWindow('<%=contextPath%>/ActivityAction/relationShow.do',800,500);
	}
	function relationShowBack(subject_id,activity_type,subject_no,subject_name){
		$("#subject_id").val(subject_id);
		$("#activity_type").val(activity_type);
		$("#subject_no").val(subject_no);
		$("#subject_name").val(subject_name);
	}
</script>
</head>
<body>
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;服务活动管理&gt;服务活动模板
</div>
<form name="fm" id="fm" method="post">
<input class="middle_txt" id="type" value="${type }" name="type" type="hidden"  />
<input class="middle_txt" id="id" value="${t.ID }" name="id" type="hidden"  />

<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
	<th colspan="8">
		<img class="nav" src="../jsp_new/img/subNav.gif"/>基本信息
	</th>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" ><span style="color: red;">*</span>模板编号：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="templet_no"  value="${t.TEMPLET_NO }"  name="templet_no" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" ><span style="color: red;">*</span>模板名称：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="templet_name" value="${t.TEMPLET_NAME }"  name="templet_name" type="text" maxlength="30" />
		<td nowrap="true" width="10%" ><span style="color: red;">*</span>是否回访：</td>
    	<td nowrap="true" width="15%" >
    		<script type="text/javascript">
  					genSelBoxExp("is_return_temp",<%=Constant.IF_TYPE%>,"${t.IS_RETURN}",true,"short_sel","","false",'');
  				</script>
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" ><span style="color: red;">*</span>主题编号：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="subject_no" readonly="readonly" value="${t.SUBJECT_NO }"  name="subject_no" type="text" maxlength="30" />
    	    <input class="middle_txt" id="activity_type" readonly="readonly" value="${t.ACTIVITY_TYPE }"  name="activity_type" type="hidden" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" ><span style="color: red;">*</span>主题名称：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="subject_name" readonly="readonly" value="${t.SUBJECT_NAME }"  name="subject_name" type="text" maxlength="30" />
    		<input  id="subject_id"  value="${t.SUBJECT_ID }"   name="subject_id" type="hidden" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" ><span style="color: red;">*</span>是否提示：</td>
    	<td nowrap="true" width="15%" >
    		<script type="text/javascript">
  					genSelBoxExp("is_tips",<%=Constant.IF_TYPE%>,"${t.IS_TIPS}",true,"short_sel","","false",'');
  			</script>
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td  colspan="8" nowrap="true">
			<span style="color: red;">*</span>活动方案说明：<textarea rows="5" cols="92" name="case_remark" id="case_remark">${t.CASE_REMARK }</textarea>
		</td>
	</tr>
	<tr>
		<td  colspan="8" nowrap="true">
			<span style="color: red;">*</span>索赔申请指导：<textarea rows="5" cols="92" name="apply_remark" id="apply_remark">${t.APPLY_REMARK }</textarea>
		</td>
	</tr> 
</table>
<br>
<div style="text-align: center;">
    		<!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color: red;">费用合计: </span>
    		<span style="color: red; font-weight: bold;" id="count_span"></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="button"  name="bntCount"  id="bntCount"  value="合计" onclick="ro_type_count();" class="normal_btn" /> 
    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;提示：[点击合计可以计算当前页面的总费用]-->
    		&nbsp;&nbsp;<span style="color: red;">添加补偿请点>></span>&nbsp;&nbsp;<input type="button"  name="bntCom"  id="bntCom"  value="补偿添加" onclick="com_add();" class="normal_btn" />
    		&nbsp;&nbsp;<span style="color: red;">添加辅料请点>></span>&nbsp;&nbsp;<input type="button"  name="bntAcc"  id="bntAcc"  value="辅料添加" onclick="ro_acc_add();" class="normal_btn" />
</div> 
<br>
    		<div id="new_add">
	    		<table border="1" id="tab_part" cellpadding="1" cellspacing="1" class="table_edit" width="120%" style="text-align: center;">
					<th colspan="12">
					<img class="nav" src="../jsp_new/img/subNav.gif" />作业项目 &nbsp;&nbsp;
					</th>
					<tr>
						<td nowrap="true" width="10%" >维修配件</td>
						<td nowrap="true" width="10%" >新件代码</td>
						<td nowrap="true" width="10%" >新件名称</td>
						<td nowrap="true" width="10%" >旧件代码</td>
						<td nowrap="true" width="10%" >单价</td>
						<td nowrap="true" width="10%" >配件数量</td>
						<td nowrap="true" width="10%" >金额（元）</td>
						<td nowrap="true" width="10%" >责任性质</td>
						<td nowrap="true" width="10%" >维修方式</td>
						<td nowrap="true" width="10%" >供应商代码</td>
						<td nowrap="true" width="10%" >是否回运</td>
						<td nowrap="true" width="10%" >
							<input type="button"  name="bntAdd" value="添加" onclick="addPart();" class="normal_btn" />
						</td>
					</tr>
					<c:forEach items="${parts }" var="p">
						<tr>
						<td nowrap="true" width="10%" >
						配件>><input name="part_id" type="hidden" class="middle_txt" value="${p.PART_ID }"/>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_code" readonly="readonly" size="10" value="${p.PART_CODE }"/>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_name" readonly="readonly" size="10" value="${p.PART_NAME }"/>
						</td>
						<td nowrap="true" width="10%" >
						<input name="old_part_code" readonly="readonly"  size="10" value="${p.OLD_PART_CODE }"/>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_cost_price" readonly="readonly" size="10" value="${p.PART_COST_PRICE }"/>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_quantity" onclick="insertNum(this);"  size="10" value="${p.PART_QUANTITY }"/>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_cost_amount" readonly="readonly"size="10"  value="${p.PART_COST_AMOUNT }"/>
						</td>
						<td nowrap="true" width="10%" >
							<select class="min_sel" name="responsibility_type">
						<c:if test="${p.RESPONSIBILITY_TYPE==94001001}">
							<option value="94001001" selected="selected">主因件</option>
							<option value="94001002">次因件</option>
						</c:if>
						<c:if test="${p.RESPONSIBILITY_TYPE==94001002}">
							<option value="94001001">主因件</option>
							<option value="94001002" selected="selected">次因件</option>
						</c:if>
						</select>
						</td>
						<td nowrap="true" width="10%" ><select class="min_sel" name="part_use_type">
						<c:if test="${p.PART_USE_TYPE==1}">
								<option value="1" selected="selected">更换</option>
								<option value="0">维修</option>
						</c:if>
						<c:if test="${p.PART_USE_TYPE==0}">
								<option value="1" >更换</option>
								<option value="0" selected="selected">维修</option>
						</c:if>
						</select>
						</td>
						<td nowrap="true" width="10%" >
						<input name="producer_code"  readonly="readonly" size="10" value="${p.PRODUCER_CODE }"/>
						</td>
						<td nowrap="true" width="10%" >
						<select class="min_sel" name="is_return" id="is_return">
							<c:if test="${p.IS_RETURN==95361001}">
								<option value="95361001">回运</option>
							</c:if>
							<c:if test="${p.IS_RETURN==95361002}">
								<option value="95361002">不回运</option>
							</c:if>
						</select></td>
						<td nowrap="true" width="10%" >
						<input type="button" value="删除" onclick="deleteTr(this);" class="normal_btn" />
						</td>
						</tr>
					</c:forEach>
	    		</table>
    			<table border="1" id="tab_labour" cellpadding="1" cellspacing="1" class="table_edit" width="120%" style="text-align: center;">
					<tr>
					<td nowrap="true" width="10%" >维修工时</td>
					<td nowrap="true" width="20%" >工时代码</td>
					<td nowrap="true" width="20%" >工时名称</td>
					<td nowrap="true" width="20%" >工时定额</td>
					<td nowrap="true" width="20%" >工时单价</td>
					<td nowrap="true" width="20%" >工时金额（元）</td>
					<td nowrap="true" width="10%" >
					<input type="button"  name="bntAdd" value="添加" onclick="addLabour();" class="normal_btn" />
					</td>
					</tr>
	    			 <c:forEach items="${labours }" var="l">
			          <tr>
						<td nowrap="true" width="10%" >工时>></td>
						<td nowrap="true" width="20%" >
						<input name="labour_code" readonly="readonly" size="25" value="${l.LABOUR_CODE }"/>
						</td>
						<td nowrap="true" width="20%" >
						<input name="labour_name" readonly="readonly" size="25" value="${l.LABOUR_NAME }"/>
						</td>
						<td nowrap="true" width="20%" >
						<input name="labour_quotiety" readonly="readonly" size="25" value="${l.LABOUR_QUOTIETY  }"/>
						</td>
						<td nowrap="true" width="20%" >
						<input name="labour_price" readonly="readonly" size="25" value="${l.LABOUR_PRICE }"/>
						</td>
						<td nowrap="true" width="20%" >
						<input name="labour_amount" readonly="readonly" size="25" value="${l.LABOUR_AMOUNT }"/>
						</td>
						<td nowrap="true" width="10%" >
						<input type="button" value="删除" onclick="deleteTr(this);" class="normal_btn" />
						</td>
						</tr>
	    			 </c:forEach>
	    		</table>
</div>
 <div id="new_acc_add"  style="text-align: center; width: 100%;">
 			<c:if test="${acc!=null}">
 			<table id="accessories" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
					<th colspan="8">
						<img class="nav" src="../jsp_new/img/subNav.gif" />辅料费&nbsp;&nbsp;<input type="button" class="normal_btn" value="删除" onclick="delPart(this,4);"
					</th>
					<tr>
						<td nowrap="true" width="20%" >辅料代码</td>
						<td nowrap="true" width="20%" >辅料费用</td>
						<td nowrap="true" width="20%" >辅料名称</td>
						<td nowrap="true" width="20%" > <input type="button"  name="bntAdd" value="添加" onclick="addAccessories();" class="normal_btn" /></td>
					</tr>
    			 <c:forEach items="${acc }" var="a">
    				<tr><td nowrap="true" width="20%" >
						<input readonly="readonly" name="workHourCode" class="middle_txt" value="${a.ACC_CODE  }"/>
						</td>
						<td nowrap="true" width="20%" >
						<input readonly="readonly" name="accessoriesPrice" class="middle_txt" value="${a.ACC_PRICE  }"/>
						</td>
						<td nowrap="true" width="20%" >
						<input readonly="readonly" name="workhour_name" size="35" value="${a.ACC_NAME  }"/>
						</td>
						<td nowrap="true" width="20%" >
						<input type="button"  name="bntDel" value="删除" onclick="deleteTr(this);" class="normal_btn" />
						</td></tr>
    			 </c:forEach>
    			 </table>
    		</c:if>
</div>
<div id="new_com_add"  style="text-align: center; width: 100%;">
<c:if test="${com!=null}">
	<table id="com" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="80%" style="text-align: center;">
		<th colspan="4">
			<img class="nav" src="../jsp_new/img/subNav.gif" />补偿费&nbsp;&nbsp;
			<input type="button" class="normal_btn" value="删除" onclick="delComTable();"/>
		</th>
		<tr>
			<td nowrap="true" width="20%" >补偿费申请金额</td>
			<td nowrap="true" width="20%" >审批金额</td>
			<td nowrap="true" width="20%" >备注</td>
			<td nowrap="true" width="20%" ></td>
		</tr>
		<tr>
			<td nowrap="true" width="20%" >
			<input name="apply_amount" id="apply_amount"  size="25" onblur="comCheckPrice(this);" value="${com.COM_PRICE  }"/>
			</td>
				<td nowrap="true" width="20%" >
			<input name="pass_amount" id="pass_amount" readonly="readonly"  size="25" value="${com.COM_PRICE }"/>
			</td>
				<td nowrap="true" width="20%" >
			<input name="remark" id="remark" maxlength="100"  size="35" value="${com.REMARK }"/>
			</td>
			<td nowrap="true" width="20%" ></td>
		</tr>
	</table>
	<br>
	</c:if>
</div>
<table class="table_edit"  id="REMARKS_ID" > 
        	 <th colspan="8"  ><img src="../jsp_new/img/subNav.gif" alt="" class="nav" />
					申请内容
				</th>
          	<tr>
					<td nowrap="true" width="10%">
						<span style="color: red;">*</span>故障描述：
					</td>
					<td class="tbwhite" colspan="3">
						<textarea name='trouble_desc' maxlength="100" 	id='trouble_desc' rows='3' cols='32'>${t.TROUBLE_DESC }</textarea>
					</td>
					<td nowrap="true" width="10%">
						<span style="color: red;">*</span>故障原因：
					</td>
					<td colspan="3" class="tbwhite">
						<textarea name='trouble_reason' id='trouble_reason' maxlength="100"  rows='3' cols='32'>${t.TROUBLE_REASON }</textarea>
					</td>
				</tr>
          </table>
<br/>
<table width=100% border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="sure" onclick="sureInsert(0);"  style="width=8%" value="保存" />
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="report" onclick="sureInsert(1);"  style="width=8%" value="下发" />
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