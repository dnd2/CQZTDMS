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
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<!-- 日历类 -->
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/jmstyle/jslib/datepicker/WdatePicker.js"></script>
<title>索赔单-审核</title>
<script type="text/javascript">
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
//审核操作
function confirmAdd(value){
	var ministerAuditingReamrk=document.getElementById("auditRemark").value;
		if(value=='1'){//同意
			MyConfirm("是否确认同意？",sureInsertCommit,[1]);
		}if(value=='2'){//退回
			if(ministerAuditingReamrk!="" && ministerAuditingReamrk!=null){
				MyConfirm("是否确认退回？",sureInsertCommit,[2]);
			}else{
				MyAlert("审核意见不能为空！");
			}			
		}if(value=='3'){//拒绝			
			if(ministerAuditingReamrk!="" && ministerAuditingReamrk!=null){
				MyConfirm("是否确认拒绝？",sureInsertCommit,[3]);
			}else{
				MyAlert("审核意见不能为空！");
			}
		}				
}
function sureInsertCommit(identify){
	var id=document.getElementById("id").value;
	var idArray = new Array();
	idArray.push(id);
	var audit=document.getElementById("audit").value;
	var url="<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/auditApp.json?flag=t&s="+identify+"&idArray="+idArray+"&audit="+audit;
	makeNomalFormCall(url,sureCommitBack,"fm");
}
function sureCommitBack(json){
	if(json.msg=="0"){
		var audit=document.getElementById("audit").value;
		if(audit=="one"){
			MyConfirm("操作成功！",Back1);
		}else{
			MyConfirm("操作成功！",Back2);
		}
		
	}else{
		MyAlert("操作失败！请与管理员联系！");
	}
}
function Back1(){
	var form = document.getElementById("fm");
	form.action ='<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/applicationClaimOne.do';
	form.submit();
}
function Back2(){
	var form = document.getElementById("fm");
	form.action ='<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/applicationClaimTwo.do';
	form.submit();
}
function bz(){
	var auditRemark=document.getElementById("auditRemark").value;
	if(auditRemark.length>200){	
		auditRemark=auditRemark.substr(0,200);
		document.getElementById("auditRemark").value=auditRemark;
		MyAlert("审核意见字数超出限制长度！");
		return;
	}
}
</script>
</head>

<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔审核管理&gt;索赔单审核</div>
<form method="post" name="fm" id="fm">
  <!-- 隐藏域 -->
  <%-- <input type="hidden" name="partFareRate" id="partFareRate" value="${partFareRate}"/><!-- 经销商加价率 -->
  <input type="hidden" name="mileage" id="mileage" value="${ListOrder.MILEAGE}"/>   
  <input type="hidden" name="arrivalDate" id="arrivalDate" value="${ListOrder.ARRIVAL_DATE}"/>
  <input type="hidden" name="purchasedDate" id="purchasedDate" value="${ListOrder.PURCHASED_DATE}"/>
  <input type="hidden" name="so_id" id="so_id" value="${ListOrder.SERVICE_ORDER_ID}"/> --%>
    <!-- 外出维修id --><input type="hidden" name="egress_Id" id="egress_Id" value="${ListOrder.EGRESS_ID}"/>
  <input type="hidden" name="id" id="id" value="${ListOrder.ID}"/>
  <input type="hidden" name="audit" id="audit" value="${audit}"/>
  <input type="hidden" name="repair_type" id="repair_type" value="${ListOrder.REPAIR_TYPE}"/>
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
        <textarea id="" name="textarea" rows="3" readonly="readonly" style="width:300px;background-color:#e6e6e6;">${ListOrder.APPLY_REMARK}</textarea>
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
    <tr class="table_list_th" id="detailThead">
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
	</tr>
	<tbody id="detailTbody">
		<c:forEach items="${listHours}" var="gs">
			<tr class="table_list_row1">
	  			<td class="numHours"></td>
      			<td style="background-color:#FFFFFF;width:120px;">
      				<input type="hidden" name="hours" />
        			${gs.LABOUR_CODE }
      			</td>
	  			<td style="background-color:#FFFFFF;">
        			${gs.CN_DES }
      			</td>
	  			<td style="background-color:#e6e6e6;">
	    			${gs.LABOUR_HOUR }
      			</td>
	  			<td style="background-color:#e6e6e6;">
        			${gs.LABOUR_PRICE }
      			</td>
	  			<td style="background-color:#e6e6e6;">
        			<c:choose>
   				<c:when test="${(ListOrder.REPAIR_TYPE=='11441004') || (ListOrder.REPAIR_TYPE=='11441005' && ListOrder.ACTIVITY_TYPE=='96281002')}">  
           			0
   				</c:when>
   				<c:otherwise> 
					${gs.LABOUR_AMOUNT }
   				</c:otherwise>
					</c:choose>
      			</td>
      			<c:choose>
   				<c:when test="${(ListOrder.REPAIR_TYPE=='11441004') || (ListOrder.REPAIR_TYPE=='11441005' && ListOrder.ACTIVITY_TYPE=='96281002')}">  
   			
   				</c:when>
   				<c:otherwise> 
					<td style="background-color:#FFFFFF;">
        				${gs.CUSTOMER_PROBLEM }
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
    <tr class="table_list_th" id="detailTheadP">
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
	</tr>
	<tbody id="detailTbodyP">
		<c:forEach items="${listParts}" var="ps">
	  	<tr class="table_list_row1">
	  		<td class="numParts"></td>
	  		<td style="background-color:#FFFFFF;">
	  			<input type="hidden" name="parts" />
        		${ps.ISG }
      		</td>
      		<td style="background-color:#FFFFFF;">
        		${ps.OLD_PART_CODE }
      		</td>
      		<td style="background-color:#FFFFFF;">
        		${ps.OLD_PART_CNAME }
      		</td>
      		<td style="background-color:#FFFFFF;width:120px;">
        		${ps.PART_CODE }
      		</td>
	  		<td style="background-color:#FFFFFF;">
        		${ps.PART_CNAME }
      		</td>
	  		<td style="background-color:#FFFFFF;">
	    		${ps.PART_NUM }
      		</td>
	  		<td style="background-color:#e6e6e6;">
        		${ps.SALE_PRICE }
      		</td>
	  		<td style="background-color:#e6e6e6;">
        		${ps.PART_APPLY_AMOUNT }
      		</td>
      		<td style="background-color:#FFFFFF;">	
				${ps.CLAIM_SUPPLIER_NAME }   			
	  		</td>
	  		<%-- <td style="background-color:#FFFFFF;">
	    		<input id="FAILURE_MODE_ID" name="FAILURE_MODE_ID" maxlength="30" value="${ps.FAILURE_MODE_ID }" type="text" class="middle_txt" style="width:40px" readonly="readonly"/>
	  		</td> --%>
	  		<td style="background-color:#FFFFFF;">
	   			 ${ps.IS_MAIN_PART_NAME }
	  		</td>
	  	    <td style="background-color:#FFFFFF;">
	  			${ps.RELATION_MAIN_PART_CODE }
	  		</td>
	  		<td style="background-color:#FFFFFF;">
	  			${ps.RELATION_LABOUR_CODE }
	  		</td>
	  		<td style="background-color:#FFFFFF;">
	   			 ${ps.PART_USE_TYPE_NAME }
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
        			${gs.FEE_CODE }
      			</td>
	  			<td style="background-color:#FFFFFF;">
        			${gs.FEE_NAME }
      			</td>
	  			<td style="background-color:#FFFFFF;">
	    			${gs.FEE_PRICE }
      			</td>
	  			<td style="background-color:#FFFFFF;">
        			${gs.FEE_REMARK }
      			</td>
	  			<td style="background-color:#FFFFFF;">	  				    		
					${gs.FEE_RELATION_MAIN_PART_CODE }  				      			
      			</td>     			 								      			      			
			</tr>
		</c:forEach>
	</tbody>
    </table>
    </div>
  </div> 
  <!-- 申请费用 -->
  <div class="form-panel">
  <h2>申请费用</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">工时数量：</td>
	  <td align="left">	    
	    <span id="applyLabourHourSpan">${hourNum }</span>
	  </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">工时金额：</td>
	  <td align="left">
	    <span id="applyLabourPriceSpan">${ListOrder.HOURS_APPLY_AMOUNT }</span>
	  </td>
      <td class="table_add_2Col_label_5Letter" style="text-align:right">配件数量：</td>
	  <td align="left">
	    <span id="applyPartNumSpan">${partNum }</span>
	  </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">配件金额：</td>
	  <td align="left">
	    <span id="applyPartPriceSpan">${ListOrder.PART_APPLY_AMOUNT }</span>
	  </td>
    </tr>
    <tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">外出金额：</td>
	  <td align="left"><span id="">${ListOrder.OUT_AMOUNT }</span></td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">服务活动金额：</td>
	  <td align="left"><span id="">${ListOrder.APPLY_ACTIVITY_PRICE_Z}</span></td>
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
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">总金额：</td>
	  <td align="left">
	    <input type="hidden" id="applyPriceTotal" name="applyPriceTotal" value="0"/>
	    <span id="applyPriceTotalSpan">${ListOrder.APPLY_TOTAL_AMOUNT }</span>
	  </td>
    </tr>
    </table>
    </div>
  </div>
  <!-- 审核意见 -->
  <div class="form-panel">
  <h2>审核意见</h2>
    <div class="form-body">
    <table border="0" id="detailTable" align="center" cellpadding="1" cellspacing="1" class="table_list" style="border-bottom:1px solid #DAE0EE;" ondrag="return false;">
			<tr class="table_list_th">
				<td> 审核意见：</td>
	        <td class="table_info_3col_input">
	        	<textarea style="width: 800px;height: 80px" onblur="bz();" name="auditRemark" id="auditRemark"></textarea>
	        </td> 
			</tr> 
    </table>
    </div>
  </div>
  <!-- 按钮 -->
  <table border="0" cellspacing="0" cellpadding="0" class="table_query">
	<tr>
		<td colspan="6" style="text-align: center;">
			<input type="button" onClick="confirmAdd(1);" id="save_but" class="normal_btn" style="" value="同意" />&nbsp;
			<input type="button" onClick="confirmAdd(2);" id="save_but" class="normal_btn" style="" value="退回" />&nbsp;
			<input type="button" onClick="confirmAdd(3);" id="save_but" class="normal_btn" style="" value="拒绝" />&nbsp;
			<input type="button" onClick="history.go(-1);" class="normal_btn" style="" value="返回" />
		</td>
	</tr>
</table>
<!-- 审核记录 -->
  <div class="form-panel">
  <h2>审核记录</h2>
    <div class="form-body">
    <table border="0" id="detailTable" align="center" cellpadding="1" cellspacing="1" class="table_list" style="border-bottom:1px solid #DAE0EE;" ondrag="return false;">
    <tr class="table_list_th">
    		<th style="text-align:center" width="20%" class="noSort">审核时间</th>
			<th style="text-align:center" width="40%" class="noSort">审核意见</th>
			<th style="text-align:center" width="20%" class="noSort">审核人</th>
			<th style="text-align:center" width="20%" class="noSort">审核状态</th>
    	</tr>
		<c:forEach items="${rList}" var="applyR">
			<tr class="table_list_row2">
				<td style="background-color:#FFFFFF;">${applyR.AUDIT_DATE}</td>
				<td style="background-color:#FFFFFF;">${applyR.AUDIT_RECORD}</td>
				<td style="background-color:#FFFFFF;">${applyR.NAME}</td>
				<td style="background-color:#FFFFFF;">${applyR.OPERA_STSTUS}</td> 
			</tr> 
		</c:forEach>
    </table>
    </div>
  </div>
</form>
</div>
</body>
</html>