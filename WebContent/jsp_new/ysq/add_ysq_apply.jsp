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
<%@ page import="com.infodms.dms.common.Constant"%>
<head> 
<%  String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<title>服务活动管理</title>	
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>

<script type="text/javascript">
	$(function(){
		var type=$("#type").val();
		if("add"==type){
			$("#ysq_no").val("  新增后自动生成单号");
			$("#ysq_no").attr("disabled",true);
			$("#vin").bind("blur",function(){
				findInfoByVin();
			});
			$(".pass_show").hide();
		}
		if("update"==type){
			$("#vin").bind("blur",function(){
				findInfoByVin();
			});
			$(".pass_show").hide();
		}
		if("view"==type){
			$("input[type='text']").attr("readonly","readonly");
			$("select").attr("disabled",true);
			$("textarea").attr("readonly","readonly");
			$("#chooseMainPart").hide();
			$("#chooseProducer").hide();
			$("#report_btn,#save_btn").hide();
			$(".normal_btn").each(function(){
				 if($(this).val()=="删 除" || $(this).val()=="添加附件"){
					 $(this).hide();
				} 
			});
		}
		
		if("goToYsq"==type){
			$("#ysq_no").val("  新增后自动生成单号");
			$("#ysq_no").attr("disabled",true);
			var ysq_vin=$("#ysq_vin").val();
			makeFormCall('<%=contextPath%>/YsqAction/getInfoByVin.json?vin='+ysq_vin,backVinInfo,'fm');
			$(".pass_show").hide();
		}
		
	});
	
	//清空VIN信息
	function clearVinInfo(){
		$("#model_name").val("");
		$("#package_name").val("");
		$("#mileage").val("");
		$("#color").val("");
		$("#vrLevel").val("");
		$("#car_use_desc").val("");
		$("#engine_no").val("");
		$("#invoice_date").val("");
		$("#series_id").val("");
		$("#model_id").val("");
	}
	function findInfoByVin(){
		var vin=$("#vin").val();
		if($.trim(vin).length>=18){
			MyAlert("提示：VIN只有17位！");
			$("#vin").val("");
			return;
		}
		if(""!=$.trim(vin) && vin.length>8){
			makeFormCall('<%=contextPath%>/YsqAction/getInfoByVin.json?vin='+vin,backVinInfo,'fm');
		}else{
			clearVinInfo();
		}
	}
	function backVinInfo(json){
		var v=json.data;
		if(v!=null){
			var model_name=getNull(v.MODEL_NAME);
			var package_name=getNull(v.PACKAGE_NAME);
			var color=getNull(v.COLOR);
			var car_use_desc=getNull(v.CAR_USE_DESC);
			if(""!=getNull(v.INVOICE_DATE)){
				var invoice_date=getNull(v.INVOICE_DATE).substring(0,10);
			}
			var engine_no=getNull(v.ENGINE_NO);
			var mileage=getNull(v.MILEAGE);
			var vrLevel=getNull(json.vrLevel);
			var series_id=getNull(v.SERIES_ID);
			var model_id=getNull(v.MODEL_ID);
			$("#series_id").val(series_id);
			$("#model_id").val(model_id);
			$("#model_name").val(model_name);
			$("#package_name").val(package_name);
			if(""==mileage){
				mileage=0;
			}
			$("#mileage").val(mileage);
			$("#vin_mileage").val(mileage);
			$("#color").val(color);
			$("#vrLevel").val(vrLevel);
			$("#car_use_desc").val(car_use_desc);
			$("#engine_no").val(engine_no);
			$("#invoice_date").val(invoice_date);
			return;
		}else{
			clearVinInfo();
			return;
		}
	}
	function getNull(data) {
		if (data==null) {
			return '';
		}else {
			return data;
		}
	}
	
	function changeIsRelation(obj){
		$("#producer_code").val("");
		$("#producer_name").val("");
		$("#part_code").val("");
		$("#part_name").val("");
		$("#max_estimate").val("");
		$("#is_return").val("");
		if('10661013'==$(obj).val()){
			$("#chooseOrder").attr("disabled",false);
			$("#chooseMainPart").hide();
		}else{
			$("#chooseOrder").attr("disabled",true);
			$("#chooseMainPart").show();
		}
	}
	function save_fun(){
		var vin=$("#vin").val();
		var package_name=$("#package_name").val();
		var model_name=$("#model_name").val();
		var part_code=$("#part_code").val();
		var producer_code=$("#producer_code").val();
		var max_estimate=$.trim($("#max_estimate").val());
		var trouble_desc=$.trim($("#trouble_desc").val());
		var trouble_reason=$.trim($("#trouble_reason").val());
		if(""==vin || ""==package_name ||""==model_name){
			MyAlert("提示：请输入正确的VIN并保证正确信息带出！");
			return;
		}
		if(""==part_code){
			MyAlert("提示：请选择主损件代码！");
			return;
		}
		if(""==producer_code){
			MyAlert("提示：请选择供应商代码 ！");
			return;
		}
		if(""==max_estimate){
			MyAlert("提示：请填写最大预估金额  ！");
			return;
		}
		var our_car_falg=$("#our_car_falg").val();
		var bc_apply=$("#bc_apply").val();
		if("true"==our_car_falg && ""==bc_apply){
			MyAlert("提示：索赔单申请时勾选了背车费，请填写预授权背车费  ！");
			return;
		}
		var bc_remark=$("#bc_remark").val();
		if("true"==our_car_falg && ""==bc_remark){
			MyAlert("提示：索赔单申请时勾选了背车费，请填写预授权背车费申请备注  ！");
			return;
		}
		
		if(""==trouble_desc){
			MyAlert("提示：请填写故障描述  ！");
			return;
		}
		if(""==trouble_reason){
			MyAlert("提示：请填写原因分析及处理结果  ！");
			return;
		}
		var bc_mileage=$("#bc_mileage").val();
		if(""!=bc_mileage && parseFloat(bc_mileage)<200.0){
			MyAlert("提示：请填写自备车里程大于等于200以上 ！");
			return;
		}
		MyConfirm("是否确认保存？",sureInsertCommit,[0]);
	}
	function sureInsertCommit(identify){
		$("#sure").attr("disabled",true);
		var url="<%=contextPath%>/YsqAction/ysqAddSure.json?identify="+identify;
		makeNomalFormCall1(url,function(json){
			var str="";
			if(json.identify=="0"){
				str+="保存";
			}
			if(json.identify=="1"){
				str+="上报";
			}
			if(json.succ=="1"){
				MyAlert("提示："+str+"成功！");
				var url='<%=contextPath%>/YsqAction/ysqDealerList.do';
				window.location.href=url;
			}else if(json.succ=="2"){
				MyAlert("提示:同一时间段、同一服务商、同一台车、同一故障不可重复申报!");
				$("#sure").attr("disabled",false);
				return;
			}else{
				MyAlert("提示："+str+"失败！");
				$("#sure").attr("disabled",false);
				return;
			}
		},"fm");
		
	}
	function report_fun(){
		var vin=$("#vin").val();
		var package_name=$("#package_name").val();
		var model_name=$("#model_name").val();
		var part_code=$("#part_code").val();
		var producer_code=$("#producer_code").val();
		var max_estimate=$.trim($("#max_estimate").val());
		var trouble_desc=$.trim($("#trouble_desc").val());
		var trouble_reason=$.trim($("#trouble_reason").val());
		if(""==vin||""==package_name||""==model_name){
			MyAlert("提示：请输入正确的VIN并保证正确信息带出！");
			return;
		}
		if(""==part_code){
			MyAlert("提示：请选择主损件代码！");
			return;
		}
		if(""==producer_code){
			MyAlert("提示：请选择供应商代码 ！");
			return;
		}
		if(""==max_estimate){
			MyAlert("提示：请填写最大预估金额  ！");
			return;
		}
		if(""==trouble_desc){
			MyAlert("提示：请填写故障描述  ！");
			return;
		}
		if(""==trouble_reason){
			MyAlert("提示：请填写原因分析及处理结果  ！");
			return;
		}
		var bc_mileage=$("#bc_mileage").val();
		if(""!=bc_mileage && parseFloat(bc_mileage)<200.0){
			MyAlert("提示：请填写自备车里程大于等于200以上 ！");
			return;
		}
		MyConfirm("是否确认上报？",sureInsertCommit,[1]);
	}
	function back_fun(){
		var url='<%=contextPath%>/YsqAction/ysqDealerList.do';
		window.location.href=url;
	}
	
	//弹出选择经销商页面
	function choose_producer_code(){
		var part_code = $("#part_code").val();
		if(part_code == "" || part_code == null){
			MyAlert("请先选择配件");
			return;
		}
		OpenHtmlWindow('<%=contextPath%>/ClaimAction/supplierCodeByPartCode.do?partcode='+part_code,800,500);
	}
	
	//选择供应商
	function setMainSupplierCode(maker_code,maker_shotname){
		$("#producer_code").val(maker_code);
		$("#producer_name").val(maker_shotname);
	}
	//弹出选择新增配件页面
	function addPart(){
		
		var series_id=$("#series_id").val();
		var model_id=$("#model_id").val();
		if(""!=series_id && ""!=model_id){
			var claim_type = $("#claim_type").val();
			if('10661012'==claim_type){
				OpenHtmlWindow('<%=contextPath%>/OrderAction/addPart.do?IS_SPJJ=10041001&model_id='+model_id+'&series_id='+series_id,850,550);
			}else{
				OpenHtmlWindow('<%=contextPath%>/OrderAction/addPart.do?model_id='+model_id+'&series_id='+series_id,850,550);
			}
		}else{
			MyAlert("提示：请填写正确的VIN带出信息");
		}
		
	}
	
	//选择配件
	function setMainPartCode(part_id,part_code,part_name,claim_price_param,is_return,val){
		$("#part_code").val(part_code);
		$("#part_name").val(part_name);
		$("#part_id").val(part_id);
		$("#is_return").val(is_return);
		$("#producer_code").val("");
		$("#producer_name").val("");
	}
	function checkMileage(obj){
		var vin_mileage=$("#vin_mileage").val();
		var mileage=$("#mileage").val();
		if($.trim(mileage)==""){
			MyAlert("提示：进厂里程不能为空！");
           $(obj).focus().select();
           return;
		}
		if(parseFloat(obj.value)<parseFloat(vin_mileage)){
			MyAlert("提示：您输入的里程小于了系统里程，系统值重置，请重新输入！");
			$(obj).val(vin_mileage);
		}
	}
</script>
</head>

<form method="post" name="fm" id="fm">
<body>
<div class="navigation"><img class="nav" src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔预授权&gt;新增索赔单预授权</div>
<input name="type" id="type" value="${type}" type="hidden" />

<input  id="ysq_vin" value="${ysq_vin}" type="hidden" />
<input  id="ysq_part_id" value="${ysq_part_id}" type="hidden" />
<!-- 背车费标识 -->
<input  id="our_car_falg" value="${t.OUR_CAR_FALG}" type="hidden" />
<input type = "hidden"  value = "${t.ID }" id ="id" name="id" />
<input type = "hidden"  value = "${t.RELATION_RO }" id ="relation_ro" name="relation_ro" />
<input type = "hidden"  value = "${t.SERIES_ID }" id ="series_id" name ="series_id" />
<input type = "hidden"  value = "${t.MODEL_ID }" id ="model_id" name ="model_id" />
<input type = "hidden"  value = "${t.PART_ID }" id ="part_id" name ="part_id" />
<input type = "hidden" value = "${t.PRODUCE_NAME_MAIN }" id ="producer_name" name ="producer_name"/>
<input type = "hidden" value = "${t.MILEAGE }" id ="vin_mileage" name ="vin_mileage"/>
<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
	<tr>
		<th colspan="8">
			<img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息
		</th>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" ><span style="COLOR: red">*</span>预授权号：</td>
    	<td nowrap="true" width="15%" >
    		<input type="text" readonly="readonly" name="ysq_no" id="ysq_no" value="${t.YSQ_NO }" maxlength="50" class="middle_txt"/>
    	</td>
		<td nowrap="true" width="10%" >车型：</td>
    	<td nowrap="true" width="15%" >
			 <input  readonly="readonly"  id="model_name" value="${v.MODEL_NAME }" type="text" class="middle_txt" >
    	</td>
    	<td nowrap="true" width="10%" >服务站代码：</td>
    	<td nowrap="true" width="15%" >
			<input  readonly="readonly" value="${u.DEALER_CODE }" type="text" class="middle_txt" >
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" ><span style="COLOR: red">*</span>VIN：</td>
    	<td nowrap="true" width="15%" >
    		<input type="text" name="vin" id="vin" value="${t.VIN }" maxlength="50"  class="middle_txt"/>
    	</td>
		<td nowrap="true" width="10%" >配置：</td>
    	<td nowrap="true" width="15%" >
    		<input  readonly="readonly" id="package_name" value="${v.PACKAGE_NAME }" type="text" class="middle_txt" >
    	</td>
    	<td nowrap="true" width="10%" >服务站简称：</td>
    	<td nowrap="true" width="15%" >
			<input  readonly="readonly" value="${u.DEALER_SHORTNAME }" type="text" class="middle_txt" >
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >进厂里程：</td>
    	<td nowrap="true" width="15%" >
    		<input  id="mileage" name="mileage"  onblur="checkMileage(this);" value="${t.MILEAGE }" type="text" class="middle_txt" maxlength="10" >
    	</td>
		<td nowrap="true" width="10%" >颜色：</td>
    	<td nowrap="true" width="15%" >
			<input  readonly="readonly" id="color" value="${v.COLOR }" type="text" class="middle_txt" >
    	</td>
    	<td nowrap="true" width="10%" >索赔员：</td>
    	<td nowrap="true" width="15%" >
    	 	<input  readonly="readonly" value="${u.NAME }" type="text" class="middle_txt" >
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >三包预警：</td>
    	<td nowrap="true" width="15%" >
    		<input  readonly="readonly" id="vrLevel" value="${vrLevel}" type="text" class="middle_txt" >
    	</td>
		<td nowrap="true" width="10%" >车辆用途：</td>
    	<td nowrap="true" width="15%" >
 			<input  readonly="readonly" id="car_use_desc" value="${v.CAR_USE_DESC }" type="text" class="middle_txt" >
     	</td>
    	<td nowrap="true" width="10%" >索赔员电话：</td>
    	<td nowrap="true" width="15%" >
    		 <input  readonly="readonly" value="${u.HAND_PHONE }" type="text" class="middle_txt" >
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >发动机号：</td>
    	<td nowrap="true" width="15%" >
    		<input  readonly="readonly" id="engine_no" value="${v.ENGINE_NO }" type="text" class="middle_txt" >
    	</td>
		<td nowrap="true" width="10%" >购车日期：</td>
    	<td nowrap="true" width="15%" >
			<input type='text' id='invoice_date'  class="middle_txt" readonly="readonly" value ="<fmt:formatDate value="${v.INVOICE_DATE }" type="date" dateStyle="full" pattern="yyyy-MM-dd"/>"/>
					
    	</td>
    	<td nowrap="true" width="10%" ></td>
    	<td nowrap="true" width="15%" >
    	</td>
    	<td width="12.5%"></td>
	</tr>
</table>
<br>
 <table  border="0" align="center" cellpadding="0"cellspacing="1" class="table_list"style="border-bottom: 1px solid #DAE0EE">
				<th colspan="11" align="left">
					<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
					作业项目
				</th>
				<tr align="center" class="table_list_row1">
					<td>
						维修类型
					</td>
					<td>
						主损件代码
					</td>
					<td>
						主损件名称
					</td>
					<td>
						供应商代码
					</td>
					<td>
						最大预估金额
					</td>
					<td>
						是否回运
					</td>
					<td>
						关联工单
					</td>
					
				</tr>
				<tr align="center" class="table_list_row1">
					<td>
						<script type="text/javascript">
			         	genSelBoxExp("claim_type",<%=Constant.CLA_TYPE%>,"${t.CLAIM_TYPE}",false,"short_sel","onchange=changeIsRelation(this)","false",'10661002,10661003,10661004,10661005,10661008,10661009,10661011');
			         	 </script>
					</td>
					<td>
					  <c:if test="${type=='add' || type=='goToYsq'}">
						<input type="text" name="part_code" class="middle_txt"   id="part_code" value="${t.PART_CODE_MAIN }" datatype="0,is_null" readonly="readonly" /><input type="button" onClick="addPart();" id="chooseMainPart" name="chooseMainPart"  class="mini_btn" value="..." />
					  </c:if>
					  <c:if test="${type=='update'}">
						<input type="text" name="part_code" class="middle_txt"   id="part_code" value="${t.PART_CODE }" datatype="0,is_null" readonly="readonly" /><input type="button" onClick="addPart();" id="chooseMainPart" name="chooseMainPart"  class="mini_btn" value="..." />
					  </c:if>
					</td>
					<td>
					  <c:if test="${type=='add' || type=='goToYsq'}">
						<input type="text" name="part_name" class="middle_txt"   id="part_name" value="${t.PART_NAME_MAIN }" datatype="0,is_null"  readonly="readonly"/>
					  </c:if>
					  <c:if test="${type=='update'}">
						<input type="text" name="part_name" class="middle_txt"   id="part_name" value="${t.PART_NAME }" datatype="0,is_null"  readonly="readonly"/>
					  </c:if>
					</td>
					<td>
					 <c:if test="${type=='add' || type=='goToYsq'}">
						<input type="text" name="producer_code" class="short_txt"   id="producer_code" value = "${t.PRODUCE_CODE_MAIN }" datatype="0,is_null" readonly="readonly" /><input type="button" onClick="choose_producer_code();" id="chooseProducer" name="chooseProducer"  class="mini_btn" value="..." />
					  </c:if>
					  <c:if test="${type=='update'}">
						<input type="text" name="producer_code" class="short_txt"   id="producer_code" value = "${t.PRODUCER_CODE }" datatype="0,is_null" readonly="readonly" /><input type="button" onClick="choose_producer_code();" id="chooseProducer" name="chooseProducer"  class="mini_btn" value="..." />
					  </c:if>
					</td>
					<td>
						<input type="text" name="max_estimate" class="short_txt" value="${t.MAX_ESTIMATE }"  id="max_estimate" datatype="1,isMoney,10" />
					</td>
					<td>
					 <c:if test="${t.IS_RETURNED==95361002}">
					    <select id="is_return" name="is_return">
					      <option value="95361002">不回运</option>
					    </select>
					 </c:if>
					 <c:if test="${t.IS_RETURNED!=95361002}">
					    <script type="text/javascript">
				       	genSelBoxExp("is_return",<%=Constant.IS_RETURN%>,"${t.IS_RETURN }",true,"short_sel","","false",'');
				        </script>
					 </c:if>
					</td>
					<td>
						<input type="button" onClick="selectOrder();" id="chooseOrder" class="normal_btn" disabled="disabled" value="关联工单" />
					</td>
			</tr>
</table>
<br>
<table border="0" align="center" cellpadding="1" cellspacing="1" class="table_edit">
			<th colspan="6">
				<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
			申请内容
			</th>
			<tr>
				<td  nowrap="true">
					故障描述：
				</td>
				<td  nowrap="true">
					<textarea name="trouble_reason" id="trouble_reason" style="font-weight: bold;" datatype="1,is_textarea,1000" maxlength="1000" rows='5' cols='40'>${t.TROUBLE_REASON}</textarea>
				</td>
				
				<td  nowrap="true">
					原因分析及处理结果：
				</td>
				<td  nowrap="true">
					<textarea name='trouble_desc' style="font-weight: bold;" id="trouble_desc"	datatype="1,is_textarea,1000" maxlength="1000" rows='5' cols='40'>${t.TROUBLE_DESC }</textarea>
				</td>
			</tr>
</table>
<table border="0" align="center" cellpadding="1" cellspacing="1" class="table_edit">
			<th colspan="6">
				<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
			补偿费用
			</th>
			<tr>
				<td  nowrap="true">
					&nbsp;补偿申请金额:&nbsp;&nbsp;&nbsp;<input type="text" name="com_apply" class="short_txt"   id="com_apply" value="${t.COM_APPLY }" datatype="1,isDigitAll,10"  maxlength="10" />
				</td>
				<td class="pass_show" nowrap="true">
					&nbsp;补偿审核金额:&nbsp;&nbsp;&nbsp;<input type="text" name="com_pass" class="short_txt"   id="com_pass" value="${t.COM_PASS }" datatype="1,isDigitAll,10"  readonly="readonly" maxlength="10" />
				</td>
				<td  nowrap="true">
					&nbsp;补偿申请备注:&nbsp;&nbsp;&nbsp;<input type="text" name="com_remark" class="long_txt"   id="com_remark" value="${t.COM_REMARK }"  maxlength="300" />
				</td>
			</tr>
			<th colspan="6">
				<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
			背车费用
			</th>
			<tr>
				<td  nowrap="true">
					&nbsp;背车申请金额:&nbsp;&nbsp;&nbsp;<input type="text" name="bc_apply" class="short_txt"   id="bc_apply" value="${t.BC_APPLY }" datatype="1,isMoney,10"  maxlength="10" />
				</td>
				<td class="pass_show"  nowrap="true">
					&nbsp;背车审核金额:&nbsp;&nbsp;&nbsp;<input type="text" name="bc_pass" class="short_txt"   id="bc_pass" value="${t.BC_PASS }" datatype="1,isMoney,10"  readonly="readonly"  maxlength="10" />
				</td>
				<td  nowrap="true">
					&nbsp;背车申请备注:&nbsp;&nbsp;&nbsp;<input type="text" name="bc_remark" class="long_txt"   id="bc_remark" value="${t.BC_REMARK }"  maxlength="300" />
				</td>
			</tr>
			<th colspan="6">
				<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
				自备车里程
			</th>
			<tr>
				<td  nowrap="true">
					&nbsp;&nbsp;自备车里程:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="bc_mileage" class="short_txt"   id="bc_mileage" value="${t.BC_MILEAGE }" datatype="1,isMoney,10"  maxlength="10" />
				</td>
			</tr>
</table>
</br>
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
		</br>
<change:btn mainTainSql="COMMON_BTN_2"></change:btn>
</form>
</body>
</html>