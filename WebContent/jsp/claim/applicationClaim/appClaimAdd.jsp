﻿<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%  String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<!-- 日历类 -->
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/jmstyle/jslib/datepicker/WdatePicker.js"></script>
<title>索赔单-新增</title>
<script type="text/javascript">
var num;
$(document).ready(function(){
	var repair_type=document.getElementById("repair_type").value;
	var activity_type=document.getElementById("activity_type").value;
	if(repair_type=='11441001'){//一般维修
		$("#sxr_message").show();//送修人
		$("#application_message").show();//申请内容
		$("#hours_message").show();//工时
		$("#parts_message").show();//配件		
	}else if(repair_type=='11441002'){//外出维修
		$("#sxr_message").show();//送修人
		$("#out_maintenance_message").show();//外出
		$("#application_message").show();//申请内容
		$("#hours_message").show();//工时
		$("#parts_message").show();//配件
		$("#outs_message").show();//外出明细
	}else if(repair_type=='11441003'){//售前维修
		if(document.getElementById("egress_Id").value!=""){
			$("#out_maintenance_message").show();//外出
			$("#outs_message").show();//外出明细
		}
		$("#sxr_message").show();//送修人
		$("#application_message").show();//申请内容
		$("#hours_message").show();//工时
		$("#parts_message").show();//配件	
	}else if(repair_type=='11441004'){//保养
		$("#maintain_message").show();//保养
		$("#hours_message").show();//工时
		$("#parts_message").show();//配件	
	}else if(repair_type=='11441005'){//服务活动	
		if(activity_type=='96281001'){//技术升级
			$("#activity_message").show();//活动信息
			$("#sxr_message").show();//送修人
			$("#application_message").show();//申请内容
			$("#hours_message").show();//工时
			$("#parts_message").show();//配件
		}else if(activity_type=='96281002'){//送保养
			$("#activity_message").show();//活动信息
			$("#sxr_message").show();//送修人
			$("#hours_message").show();//工时
			$("#parts_message").show();//配件
		}else if(activity_type=='96281003'){//送检测
			$("#activity_message").show();//活动信息
			$("#sxr_message").show();//送修人
			$("#application_message").show();//申请内容
		}		
	}else if(repair_type=='11441006'){//特殊服务
		$("#sxr_message").show();//送修人
		$("#application_message").show();//申请内容
		$("#hours_message").show();//工时
		$("#parts_message").show();//配件	
	}else if(repair_type=='11441008'){//PDI
		$("#pdi_message").show();//PDI
	}else if(repair_type=='11441009'){//备件维修
		$("#sxr_message").show();//送修人
		$("#application_message").show();//申请内容
		$("#hours_message").show();//工时
		$("#parts_message").show();//配件	
	}
	resetNumHour();
	resetNumPart();
	resetNumOut();
}); 
/**
 * 重置列表的序号
 */
function resetNumHour(){
	var tbody = $('#detailTbody');
	var nums = $(tbody).find(".numHours");
	$(nums).each(function(i){
		var n = i+1;
		$(this).html(n);
		$(this).parent().find('input[name=hours]').val(n);	
		var cls = "table_list_row1";
		if(n%2==0){
			cls = "table_list_row2";
		}
		$(this).parent().attr('class',cls);
		$(this).parent().attr('id',null);
		
		$(this).parent().find('input[name=cusProblem]').attr('id',"cusProblem"+n);
		
	});
}
function resetNumPart(){
	var tbody = $('#detailTbodyP');
	var nums = $(tbody).find(".numParts");
	$(nums).each(function(i){
		var n = i+1;
		$(this).html(n);
		$(this).parent().find('input[name=parts]').val(n);	
		var cls = "table_list_row1";
		if(n%2==0){
			cls = "table_list_row2";
		}
		$(this).parent().attr('class',cls);
		$(this).parent().attr('id',null);
		$(this).parent().find('input[name=partOldId]').attr('id',"partOldId"+n);//旧件
		$(this).parent().find('input[name=partOldCode]').attr('id',"partOldCode"+n);//
		$(this).parent().find('input[name=partOldName]').attr('id',"partOldName"+n);
		$(this).parent().find('input[name=OldPartGCode]').attr('id',"OldPartGCode"+n);//供应商
		$(this).parent().find('input[name=OldPartGName]').attr('id',"OldPartGName"+n);
	});
}
function resetNumOut(){
	var tbody = $('#detailTbodyO');
	var nums = $(tbody).find(".numOuts");
	$(nums).each(function(i){
		var n = i+1;
		$(this).html(n);
		$(this).parent().find('input[name=outs]').val(n);	
		var cls = "table_list_row1";
		if(n%2==0){
			cls = "table_list_row2";
		}
		$(this).parent().attr('class',cls);
		$(this).parent().attr('id',null);
		
	});
}
function OnCusProblem(obj){
	var tr = $(obj).parent().parent();
	var ipt = $(tr).find('input[name=hours]');
	var id='cusProblem'+$(ipt).val();
	OpenHtmlWindow("<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/cusProblem.do?id="+id,800,500);
}
function setcusProblem(maker_code,maker_shotname,id){
	document.getElementById(id).value=maker_shotname;
}
//旧件
function onServicePart(obj){
	var tr = $(obj).parent().parent();
	var ipt = $(tr).find('input[name=parts]');
	var idOldPartId='partOldId'+$(ipt).val();
	var idOldPartCode='partOldCode'+$(ipt).val();
	var idOldPartName='partOldName'+$(ipt).val();
	var repair=document.getElementById("repair_type").value;
	var mileage=document.getElementById("mileage").value;
	var arrivalDate=document.getElementById("arrivalDate").value;
	var purchasedDate=document.getElementById("purchasedDate").value;
	var partFareRate=document.getElementById("partFareRate").value;
	var url = "<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/servicePartWin.do?&mileage="+mileage
	+ "&arrivalDate="+arrivalDate+"&repair="+repair+"&purchasedDate="+purchasedDate+"&idOldPartId="+idOldPartId+"&idOldPartCode="+idOldPartCode+"&idOldPartName="+idOldPartName+"&partFareRate="+partFareRate;
OpenHtmlWindow(url,900,500);
}
function setServicePartInfo(isThreeGuarantee,partId,partCode,partCname,partPrice,id_Id,id_code,id_name){
	document.getElementById(id_Id).value=partId;
	document.getElementById(id_code).value=partCode;
	document.getElementById(id_name).value=partCname;
}
//旧件供应商
function setSupplierCode(maker_code,maker_shotname,id,id_name){
	document.getElementById(id).value=maker_code;
	document.getElementById(id_name).value=maker_shotname;
	//判断是否存在次因件
	var tr = $("#"+id).parent().parent();
	var ipt = $(tr).find('input[name=partId]');
	var rmp=document.getElementsByName("RELATION_MAIN_PART");
	for(var i=0;i<rmp.length;i++){
		if(rmp[i].value==$(ipt).val()){
			var trp = $(rmp[i]).parent().parent();
			var iptpCode = $(trp).find('input[name=OldPartGCode]');
			var iptpName = $(trp).find('input[name=OldPartGName]');
			var iptp_font_Name = $(trp).find('font[name=font_name]');
			$(iptpCode).val(maker_code);//添加次因件 供应商代码，名称
			$(iptpName).val(maker_shotname);
			$(iptp_font_Name).html(maker_shotname);
		}
	}
}
function onSupply(obj){	
	var tr = $(obj).parent().parent();
	var ipt = $(tr).find('input[name=parts]');	
	var id="OldPartGCode"+$(ipt).val();
	var id_name="OldPartGName"+$(ipt).val();
	var old_code=$("#partOldCode"+$(ipt).val()).val();//旧件代码
	OpenHtmlWindow("<%=contextPath%>/jsp_new/goodClaim/spe_show_supplier_code.jsp?old_code="+old_code+"&id_name="+id_name+"&id="+id,800,500);
}
//保存验证
function claimVerification(){	
	var url="<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/claimVerification.json";
	makeNomalFormCall(url,claimVerificationBack,"fm");
}
function claimVerificationBack(json){
	if(json.msg=="00"){
		MyAlert("工时代码为：("+json.message.substr(0,[json.message.length-1])+")暂无配件信息，请核对后重试！");
		return;
	}else{		
		MyConfirm("是否确认保存？",sureInsertCommit);
	}
	
}
//保存操作
function confirmAdd(){
	if(submitForm('fm')){
		//有索赔工时，就必须有索赔配件
		claimVerification();		
	}				
}
function sureInsertCommit(){
	var url="<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/appClaimUpdate.json";
	makeNomalFormCall(url,sureCommitBack,"fm");
}
function sureCommitBack(json){
	if(json.msg=="0"){
		MyConfirm("操作成功！",Back);
	}else{
		MyAlert("操作失败！请与管理员联系！");
	}
}
function Back(){
	var form = document.getElementById("fm");
	form.action ='<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/orderClaim.do';
	form.submit();
}
</script>
</head>

<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;经销商索赔管理&gt;新增索赔单</div>
<form method="post" name="fm" id="fm">
  <!-- 隐藏域 -->
  <input type="hidden" name="partFareRate" id="partFareRate" value="${partFareRate}"/><!-- 经销商加价率 -->
  <input type="hidden" name="mileage" id="mileage" value="${ListOrder.MILEAGE}"/>   
  <input type="hidden" name="arrivalDate" id="arrivalDate" value="${ListOrder.ARRIVAL_DATE}"/>
  <input type="hidden" name="purchasedDate" id="purchasedDate" value="${ListOrder.PURCHASED_DATE}"/>
  <input type="hidden" name="so_id" id="so_id" value="${ListOrder.SERVICE_ORDER_ID}"/>
  <input type="hidden" name="sp_id" id="sp_id" value="${ListOrder.ID}"/>
  <!-- 外出维修id --><input type="hidden" name="egress_Id" id="egress_Id" value="${ListOrder.EGRESS_ID}"/>
  <!-- 维修类型 --><input type="hidden" name="repair_type" id="repair_type" value="${ListOrder.REPAIR_TYPE}"/>
  <!-- 服务活动类型 --><input type="hidden" name="activity_type" id="activity_type" value="${ListOrder.ACTIVITY_TYPE}"/>
  <!-- 基本信息 -->
  <div class="form-panel">
  <h2>基本信息</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">经销商代码：</td>
	  <td align="left" style="width:245px">${ListOrder.DEALER_CODE}</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">经销商名称：</td>
	  <td align="left" style="width:245px">${ListOrder.DEALER_NAME}</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">经销商电话：</td>
	  <td align="left" style="width:245px">${ListOrder.PHONE}</td>
	</tr>
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">维修类型：</td>
	  <td align="left">
	    	${ListOrder.REPAIR_NAME}
	  </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">进站时间：</td>
	  <td align="left">
			${ListOrder.ARRIVAL_DATE}
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">进站里程数：</td>
	  <td align="left">
	    	${ListOrder.MILEAGE}
	  </td>
	</tr>
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">维修开始时间：</td>
	  <td align="left">
	    	${ListOrder.REPAIR_DATE_BEGIN}
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">维修结束时间：</td>
	  <td align="left">
	        ${ListOrder.REPAIR_DATE_END }
      </td>
      <td class="table_add_2Col_label_5Letter" style="text-align:right">接待员：</td>
	  <td align="left">
	        ${ListOrder.RECEPTIONIST_MAN}
      </td>
	</tr>
    </table>
    </div>
  </div>
  <!-- 车辆信息 -->
  <div class="form-panel">
  <h2>车辆信息</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">VIN：</td>
	  <td align="left" style="width:245px">
	    ${ListOrder.VIN}
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">发动机号：</td>
	  <td align="left" style="width:245px">
        ${ListOrder.ENGINE_NO}
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">牌照号：</td>
	  <td align="left" style="width:245px">
	    ${ListOrder.LICENSE_NO}
      </td>
	</tr>
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">品牌：</td>
	  <td align="left"><span id="brandName">${ListOrder.BRAND_NAME}</span></td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">车系：</td>
	  <td align="left"><span id="seriesName">${ListOrder.SERIES_NAME}</span></td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">车型：</td>
	  <td align="left"><span id="modelName">${ListOrder.MODEL_NAME}</span></td>
	</tr>
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">产地：</td>
	  <td align="left"><span id="areaName">${ListOrder.AREA_NAME}</span></td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">购车日期：</td>
	  <td align="left"><span id="purchasedDate">${ListOrder.PURCHASED_DATE}</span></td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">生产日期：</td>
	  <td align="left"><span id="productDate">${ListOrder.PRODUCT_DATE}</span></td>
	</tr>
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">配置：</td>
	  <td align="left"><span id="packageName">${ListOrder.PACKAGE_NAME}</span></td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">车主姓名：</td>
	  <td align="left"><span id="ctmName">${ListOrder.CTM_NAME}</span></td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">三包规则名称：</td>
	  <td align="left"><span id="ruleName">${ListOrder.RULE_NAME}</span></td>
	</tr>
    </table>
    </div>
  </div>
  <!-- 保养信息 -->
  <div class="form-panel" id="maintain_message" style="display: none">
  <h2>保养信息</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px">保养次数：</td>
	  <td align="left" style="width:245px">
	    ${ListOrder.CUR_FREE_TIMES}
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px">保养金额：</td>
	  <td align="left" style="width:245px">
        ${ListOrder.APPLY_MAINTAIN_PRICE}
      </td>
      <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px">&nbsp;</td>
	  <td align="left" style="width:245px">&nbsp;</td>
	</tr>
    </table>
    </div>
  </div>
  <!-- 服务活动信息 -->
  <div class="form-panel" id="activity_message" style="display: none">
  <h2>服务活动信息</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px">服务活动类型：</td>
	  <td align="left" style="width:245px">
	    ${ListOrder.ACTIVITY_NAME}
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px">服务活动金额：</td>
	  <td align="left" style="width:245px">
        ${ListOrder.APPLY_ACTIVITY_PRICE_Z}
      </td>
      <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px">&nbsp;</td>
	  <td align="left" style="width:245px">&nbsp;</td>
	</tr>
    </table>
    </div>
  </div>
  <!-- PDI信息 -->
  <div class="form-panel" id="pdi_message" style="display: none">
  <h2>PDI信息</h2> 
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px">PDI结果：</td>
	  <td align="left" style="width:245px">
	    ${ListOrder.PDI_REMARK}
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px">PDI金额：</td>
	  <td align="left" style="width:245px">
        ${ListOrder.APPLY_PDI_PRICE}
      </td>
      <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px">&nbsp;</td>
	  <td align="left" style="width:245px">&nbsp;</td>
	</tr>
    </table>
    </div>
  </div>
  <!-- 外出维修信息 -->
  <div class="form-panel" id="out_maintenance_message" style="display: none">
  <h2>外出维修信息</h2> 
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px">救急开始时间：</td>
	  <td align="left" style="width:245px">
	    ${ListOrder.E_START_DATE}
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px">救急结束时间：</td>
	  <td align="left" style="width:245px">
        ${ListOrder.E_END_DATE}
      </td>
      <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px">救急人数：</td>
	  <td align="left" style="width:245px">${ListOrder.E_NUM}</td>
	</tr>
	<tr>
		<td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px">救援车车牌号：</td>
	  <td align="left" style="width:245px">
	    ${ListOrder.E_LICENSE_NO}
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px">救急人姓名：</td>
	  <td align="left" style="width:245px" colspan="3">
	    ${ListOrder.E_NAME}
      </td>
	</tr>
    </table>
    </div>
  </div>
    <!-- 送修人信息 -->
  <div class="form-panel" id="sxr_message" style="display: none">
  <h2>送修人信息</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px">送修人姓名：</td>
	  <td align="left" style="width:245px">
	    ${ListOrder.DELIVERER_MAN_NAME}
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px">送修人电话：</td>
	  <td align="left" style="width:245px">
        ${ListOrder.DELIVERER_MAN_PHONE}
      </td>
      <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px">&nbsp;</td>
	  <td align="left" style="width:245px">&nbsp;</td>
	</tr>
    </table>
    </div>
  </div>
  <!-- 申请内容 -->
  <div class="form-panel" id="application_message" style="display: none">
  <h2>申请内容</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">故障描述：</td>
	  <td align="left">
	    <textarea id="" name="textarea" rows="3" readonly="readonly" style="width:300px;background-color:#e6e6e6;">${ListOrder.FAULT_DESC}</textarea>
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">故障原因：</td>
	  <td align="left">
        <textarea id="" name="textarea" rows="3" readonly="readonly" style="width:300px;background-color:#e6e6e6;">${ListOrder.FAULT_REASON}</textarea>
      </td>
    </tr>
    <tr>
      <td class="table_add_2Col_label_5Letter" style="text-align:right">维修措施：</td>
	  <td align="left">
	    <textarea id="" name="textarea" rows="3" readonly="readonly" style="width:300px;background-color:#e6e6e6;">${ListOrder.REPAIR_METHOD}</textarea>
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">申请备注：</td>
	  <td align="left">
        <textarea id="" name="textarea" rows="3" readonly="readonly" style="width:300px;background-color:#e6e6e6;">${ListOrder.REMARK}</textarea>
      </td>
	</tr>
    </table>
    </div>
  </div>
  <!-- 维修项目 -->
  <div class="form-panel" id="hours_message" style="display: none">
  <h2>维修项目</h2>
    <div class="form-body">
    <table border="0" id="detailTable" align="center" cellpadding="1" cellspacing="1" class="table_list" style="border-bottom:1px solid #DAE0EE;" ondrag="return false;">
    <thead class="table_list_th" id="detailThead">
      <th class="noSort">序号</th>
      <th class="noSort">工时代码</th>
	  <th class="noSort">工时名称</th>
	  <th class="noSort">工时定额</th>
	  <th class="noSort">工时单价</th>
	  <th class="noSort">工时金额(元)</th>
	  <c:choose>
   		<c:when test="${(ListOrder.REPAIR_TYPE=='11441004') || (ListOrder.REPAIR_TYPE=='11441005' && ListOrder.ACTIVITY_TYPE=='96281002')}">  
         
   		</c:when>
   		<c:otherwise> 
   			<th class="noSort">顾客问题</th>
   		</c:otherwise>
	</c:choose>
	</thead>
	<tbody id="detailTbody">
		<c:forEach items="${listHours}" var="gs">
			<tr class="table_list_row1">
	  			<td class="numHours"></td>
      			<td style="background-color:#FFFFFF;width:120px;">
      				<input type="hidden" name="hours" />
        			<input type="hidden" name="labourId" id="labourId" value="${gs.LABOUR_ID }"/>
        			<input id="labourCode" name="labourCode"  maxlength="30" value="${gs.LABOUR_CODE }" type="hidden" class="middle_txt" style="width:80px;text-align: center;" readonly="readonly"/>${gs.LABOUR_CODE }
      			</td>
	  			<td style="background-color:#FFFFFF;">
        			<input id="cnDes" name="cnDes" maxlength="30" value="${gs.CN_DES }" type="hidden" class="middle_txt" style="width:220px;text-align: center;" readonly="readonly"/>${gs.CN_DES }
      			</td>
	  			<td style="background-color:#FFFFFF;">
	    			<input id="labourHour" name="labourHour" maxlength="30" value="${gs.LABOUR_HOUR }" type="hidden" class="middle_txt" style="width:40px;text-align: center;" readonly="readonly"/>${gs.LABOUR_HOUR }
      			</td>
	  			<td style="background-color:#FFFFFF;">
        			<input id="labourPrice" name="labourPrice" maxlength="30" value="${gs.LABOUR_PRICE }" type="hidden" class="middle_txt" style="width:40px;text-align: center;" readonly="readonly"/>${gs.LABOUR_PRICE }
      			</td>
	  			<td style="background-color:#FFFFFF;">
	  				<c:choose>
   				<c:when test="${(ListOrder.REPAIR_TYPE=='11441004') || (ListOrder.REPAIR_TYPE=='11441005' && ListOrder.ACTIVITY_TYPE=='96281002')}">  
   					<input id="labourPriceTotal" name="labourPriceTotal" maxlength="30" value="0" type="hidden" class="middle_txt" style="width:40px;text-align: center;" readonly="readonly"/>0           			
   				</c:when>
   				<c:otherwise> 
					<input id="labourPriceTotal" name="labourPriceTotal" maxlength="30" value="${gs.LABOUR_AMOUNT }" type="hidden" class="middle_txt" style="width:40px;text-align: center;" readonly="readonly"/>${gs.LABOUR_AMOUNT }
   				</c:otherwise>
					</c:choose>        			
      			</td>
      			<c:choose>
   				<c:when test="${(ListOrder.REPAIR_TYPE=='11441004') || (ListOrder.REPAIR_TYPE=='11441005' && ListOrder.ACTIVITY_TYPE=='96281002')}">  
   			
   				</c:when>
   				<c:otherwise> 
					<td style="background-color:#FFFFFF;">
           				<input id="cusProblem" name="cusProblem" datatype="0,is_null,100" value="${gs.CUSTOMER_PROBLEM }" onclick="OnCusProblem(this);" type="text" class="middle_txt" style="width:250px;text-align: center;" readonly="readonly"/>
   					</td>
   				</c:otherwise>
			    </c:choose>   				      			      			
			</tr>
		</c:forEach>
	</tbody>
    </table>
    </div>
  </div>
  <!-- 维修配件 -->
  <div class="form-panel" id="parts_message" style="display: none">
  <h2>维修配件</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_list" style="border-bottom:1px solid #DAE0EE;" ondrag="return false;">
    <thead class="table_list_th" id="detailTheadP">
      <th class="noSort">序号</th>
      <th class="noSort">是否三包</th>
      <th class="noSort">旧件代码</th>
      <th class="noSort">旧件名称</th>
      <th class="noSort">新件代码</th>
	  <th class="noSort">新件名称</th>
	  <th class="noSort">新件数量</th>
	  <th class="noSort">单价</th>
	  <th class="noSort">金额(元)</th>
	  <th class="noSort">旧件供应商名称</th>
	  <!-- <th class="noSort">失效模式</th> -->
	  <th class="noSort">是否主因件</th>
	  <th class="noSort">关联主因件</th>
	  <th class="noSort">关联工时</th>
	  <th class="noSort">配件使用类型</th>
	</thead>
	<tbody id="detailTbodyP">
		<c:forEach items="${listParts}" var="ps">
	  	<tr class="table_list_row1">
	  		<td class="numParts"></td>
	  		<td style="background-color:#FFFFFF;">
	  			<input type="hidden" name="parts" />
        		<input type="hidden" id="isThreeGuarantee" readonly="readonly" name="isThreeGuarantee" style="width:30px;text-align: center;" value="${ps.IS_THREE_GUARANTEE }"/>${ps.ISG }
      		</td>
      		<td style="background-color:#FFFFFF;">
      			<c:choose>
   				<c:when test="${!empty ps.OLD_PART_CODE && ps.OLD_PART_CODE!=''}">
   					<input type="hidden" name="partOldId" id="partOldId" value="${ps.OLD_PART_ID }"/>  
           			<input id="partOldCode" name="partOldCode" datatype="0,is_null,30" value="${ps.OLD_PART_CODE }" onclick="onServicePart(this)" type="text" class="middle_txt" style="width:80px;text-align: center;" readonly="readonly"/>
   				</c:when>
   				<c:otherwise> 
   					<input type="hidden" name="partOldId" id="partOldId" value="${ps.PART_ID }"/>
					<input id="partOldCode" name="partOldCode" datatype="0,is_null,30" value="${ps.PART_CODE }" onclick="onServicePart(this)" type="text" class="middle_txt" style="width:80px;text-align: center;" readonly="readonly"/>
   				</c:otherwise>
			</c:choose>

      		</td>
      		<td style="background-color:#FFFFFF;">
      			<c:choose>
   				<c:when test="${!empty ps.OLD_PART_CODE && ps.OLD_PART_CODE!=''}">  
           			<input id="partOldName" name="partOldName" value="${ps.OLD_PART_CNAME }" type="hidden" class="middle_txt" style="width:140px;text-align: center;" readonly="readonly"/>${ps.OLD_PART_CNAME }
   				</c:when>
   				<c:otherwise> 
					<input id="partOldName" name="partOldName" value="${ps.PART_CNAME }" type="hidden" class="middle_txt" style="width:140px;text-align: center;" readonly="readonly"/>${ps.PART_CNAME }
   				</c:otherwise>
			</c:choose>       		
      		</td>
      		<td style="background-color:#FFFFFF;width:120px;">
        		<input type="hidden" name="partId" id="partId" value="${ps.PART_ID }"/>
        		<input id="partCode" name="partCode" maxlength="30" value="${ps.PART_CODE }" type="hidden" class="middle_txt" style="width:80px;text-align: center;" readonly="readonly"/>${ps.PART_CODE }
      		</td>
	  		<td style="background-color:#FFFFFF;">
        		<input id="partCname" name="partCname" maxlength="30" value="${ps.PART_CNAME }" type="hidden" class="middle_txt" style="width:140px;text-align: center;" readonly="readonly"/>${ps.PART_CNAME }
      		</td>
	  		<td style="background-color:#FFFFFF;">
	    		<input id="partNum" name="partNum" maxlength="30" value="${ps.PART_NUM }" type="hidden" class="middle_txt" style="width:40px;text-align: center;" readonly="readonly"/>${ps.PART_NUM }
      		</td>
	  		<td style="background-color:#FFFFFF;">
        		<input id="partPrice" name="partPrice" maxlength="30" value="${ps.PART_PRICE }" type="hidden" class="middle_txt" style="width:40px;text-align: center;" readonly="readonly"/>${ps.PART_PRICE }
      		</td>
	  		<td style="background-color:#FFFFFF;">
	  			<c:choose>
   				<c:when test="${(ListOrder.REPAIR_TYPE=='11441004') || (ListOrder.REPAIR_TYPE=='11441005' && ListOrder.ACTIVITY_TYPE=='96281002')}">  
   					<input id="partPriceTotal" name="partPriceTotal" maxlength="30" value="0" type="hidden" class="middle_txt" style="width:40px;text-align: center;" readonly="readonly"/>0       
   				</c:when>
   				<c:otherwise> 
					<input id="partPriceTotal" name="partPriceTotal" maxlength="30" value="${ps.PART_AMOUNT }" type="hidden" class="middle_txt" style="width:40px;text-align: center;" readonly="readonly"/>${ps.PART_AMOUNT }
   				</c:otherwise>
				</c:choose> 
      		</td>
      		<td style="background-color:#FFFFFF;">
      		<c:choose>
   				<c:when test="${ps.IS_MAIN_PART=='10041001' }">  
           			<input id="OldPartGCode" name="OldPartGCode" maxlength="30" value="${ps.CLAIM_SUPPLIER_CODE }" type="hidden" class="middle_txt" style="width:40px;text-align: center;" readonly="readonly"/>
	    			<input id="OldPartGName" name="OldPartGName" datatype="0,is_null,30" value="${ps.CLAIM_SUPPLIER_NAME }" type="text" onclick="onSupply(this);" class="middle_txt" style="width:100px;text-align: center;" readonly="readonly"/>
   				</c:when>
   				<c:otherwise> 
					<input name="OldPartGCode" maxlength="30" type="hidden" value="${ps.CLAIM_SUPPLIER_CODE }" class="middle_txt" style="width:40px;text-align: center;" readonly="readonly"/>
	    			<input name="OldPartGName" datatype="0,is_null,30" type="hidden" value="${ps.CLAIM_SUPPLIER_NAME }" class="middle_txt" style="width:100px;text-align: center;" readonly="readonly"/>
	    			<font id="font_name" name="font_name">${ps.CLAIM_SUPPLIER_NAME }</font>
   				</c:otherwise>
			</c:choose>     			
	  		</td>
	  		<%-- <td style="background-color:#FFFFFF;">
	    		<input id="FAILURE_MODE_ID" name="FAILURE_MODE_ID" maxlength="30" value="${ps.FAILURE_MODE_ID }" type="text" class="middle_txt" style="width:40px" readonly="readonly"/>
	  		</td> --%>
	  		<td style="background-color:#FFFFFF;">
	   			 <input id="IS_MAIN_PART" name="IS_MAIN_PART" maxlength="30" value="${ps.IS_MAIN_PART }" type="hidden" class="middle_txt" style="width:40px;text-align: center;" readonly="readonly"/>
	   			 <input id="RELATION_MAIN_PART" name="RELATION_MAIN_PART" maxlength="30" value="${ps.RELATION_MAIN_PART }" type="hidden" class="middle_txt" style="width:40px;text-align: center;" readonly="readonly"/>
	   			 <input id="RELATION_LABOUR" name="RELATION_LABOUR" maxlength="30" value="${ps.RELATION_LABOUR }" type="hidden" class="middle_txt" style="width:40px;text-align: center;" readonly="readonly"/>${ps.IS_MAIN_PART_NAME }
	  		</td>
	  		<td style="background-color:#FFFFFF;">
	  			${ps.RELATION_MAIN_PART_CODE }
	  		</td>
	  		<td style="background-color:#FFFFFF;">
	  			${ps.RELATION_LABOUR_CODE }
	  		</td>
	  		<td style="background-color:#FFFFFF;">
	   			 <input id="PART_USE_TYPE" name="PART_USE_TYPE" maxlength="30" value="${ps.PART_USE_TYPE }" type="hidden" class="middle_txt" style="width:40px;text-align: center;" readonly="readonly"/>${ps.PART_USE_TYPE_NAME }
	  		</td>
	  	</tr>
	  	</c:forEach>
	</tbody>
    </table>
    </div>
  </div>
  <!-- 外出项目 -->
  <div class="form-panel" id="outs_message" style="display: none">
  <h2>外出项目</h2>
    <div class="form-body">
    <table border="0" id="detailTable" align="center" cellpadding="1" cellspacing="1" class="table_list" style="border-bottom:1px solid #DAE0EE;" ondrag="return false;">
    <thead class="table_list_th" id="detailThead">
      <th class="noSort">序号</th>
      <th class="noSort">项目代码</th>
	  <th class="noSort">项目名称</th>
	  <th class="noSort">金额(元)</th>
	  <th class="noSort">备注</th>
	  <th class="noSort">关联主因件</th>
	</thead>
	<tbody id="detailTbodyO">
		<c:forEach items="${listOuts}" var="gs">
			<tr class="table_list_row1">
	  			<td class="numOuts"></td>
      			<td style="background-color:#FFFFFF;width:120px;">
      				<input type="hidden" name="outs" />
        			<input type="hidden" name="fee_id" id="fee_id" value="${gs.FEE_ID }"/>
        			<input id="fee_code" name="fee_code"  maxlength="30" value="${gs.FEE_CODE }" type="hidden" class="middle_txt" style="width:80px;text-align: center;" readonly="readonly"/>${gs.FEE_CODE }
      			</td>
	  			<td style="background-color:#FFFFFF;">
        			<input id="fee_name" name="fee_name" maxlength="30" value="${gs.FEE_NAME }" type="hidden" class="middle_txt" style="width:220px;text-align: center;" readonly="readonly"/>${gs.FEE_NAME }
      			</td>
	  			<td style="background-color:#FFFFFF;">
	    			<input id="fee_price" name="fee_price" maxlength="30" value="${gs.FEE_PRICE }" type="hidden" class="middle_txt" style="width:40px;text-align: center;" readonly="readonly"/>${gs.FEE_PRICE }
      			</td>
	  			<td style="background-color:#FFFFFF;">
        			<input id="fee_remark" name="fee_remark" maxlength="30" value="${gs.FEE_REMARK }" type="hidden" class="middle_txt" style="width:40px;text-align: center;" readonly="readonly"/>${gs.FEE_REMARK }
      			</td>
	  			<td style="background-color:#FFFFFF;">	  				    		
					<input id="fee_relation_main_part" name="fee_relation_main_part" maxlength="30" value="${gs.FEE_RELATION_MAIN_PART }" type="hidden" class="middle_txt" style="width:40px;text-align: center;" readonly="readonly"/>${gs.FEE_RELATION_MAIN_PART_CODE }  				      			
      			</td>     			 								      			      			
			</tr>
		</c:forEach>
	</tbody>
    </table>
    </div>
  </div> 
  <!-- 索赔费用 -->
  <div class="form-panel">
  <h2>索赔费用</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">工时总数量：</td>
	  <td align="left">
	    <input type="hidden" id="applyLabourHour" name="applyLabourHour" value="${hourNum }"/>
	    <span id="applyLabourHourSpan">${hourNum }</span>
	  </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">工时总金额：</td>
	  <td align="left">
	    <input type="hidden" id="applyLabourPrice" name="applyLabourPrice" value="${hourAmount }"/>
	    <span id="applyLabourPriceSpan">${hourAmount }</span>
	  </td>
      <td class="table_add_2Col_label_5Letter" style="text-align:right">配件总数量：</td>
	  <td align="left">
	    <input type="hidden" id="applyPartNum" name="applyPartNum" value="${partNum }"/>
	    <span id="applyPartNumSpan">${partNum }</span>
	  </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">配件总金额：</td>
	  <td align="left">
	    <input type="hidden" id="applyPartPrice" name="applyPartPrice" value="${partAmount }"/>
	    <span id="applyPartPriceSpan">${partAmount }</span>
	  </td>
    </tr>
    <tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">外出金额：</td>
	  <td align="left"><span id=""><input type="hidden" name="out_amount" id="out_amount" value="${OUT_AMOUNT}"/>${OUT_AMOUNT}</span></td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">服务活动金额：</td>
	  <td align="left"><span id=""><input type="hidden" name="activity_price" id="activity_price" value="${ListOrder.APPLY_ACTIVITY_PRICE_Z}"/>${ListOrder.APPLY_ACTIVITY_PRICE_Z}</span></td>
      <td class="table_add_2Col_label_5Letter" style="text-align:right">首保金额：</td>
	  <td align="left"><span id="">${ListOrder.APPLY_MAINTAIN_PRICE}</span></td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">PDI金额：</td>
	  <td align="left"><span id="">${ListOrder.APPLY_PDI_PRICE}</span></td>
    </tr>
    <tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">&nbsp;</td>
	  <td align="left">&nbsp;</td>
      <td class="table_add_2Col_label_5Letter" style="text-align:right">&nbsp;</td>
	  <td align="left">&nbsp;</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">&nbsp;</td>
	  <td align="left">&nbsp;</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">索赔总金额：</td>
	  <td align="left">
	    <input type="hidden" id="applyPriceTotal" name="applyPriceTotal" value="${Amount }"/>
	    <span id="applyPriceTotalSpan">${Amount }</span>
	  </td>
    </tr>
    </table>
    </div>
  </div>
  <c:if test="${empty SP_ID }">
  <!-- 附件信息 -->
  <div class="form-panel">
  <h2>附件信息</h2>
    <div class="form-body">
    <!-- 添加附件 开始  -->
 <table id="add_file"  width="100%" class="table_info" border="0" id="file">
	<tr>
		<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
		</tr>
		<%if(fileList!=null){
			for(int i=0;i<fileList.size();i++) { %>
	  		<script type="text/javascript">
    		addUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
    	</script>
		<%}}%>
</table> 
    </div>
  </div>
  </c:if>
  <!-- 按钮 -->
  <table border="0" cellspacing="0" cellpadding="0" class="table_query">
	<tr>
		<td colspan="6" style="text-align: center;">
			<input type="button" onClick="confirmAdd();" id="save_but" class="normal_btn" style="" value="确定" />&nbsp;
			<input type="button" onClick="history.go(-1);" class="normal_btn" style="" value="返回" />
		</td>
	</tr>
</table>
</form>

</div>
</body>
</html>