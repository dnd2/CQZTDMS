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
		$("input[type='text']").attr("readonly","readonly");
		$("select").attr("disabled",true);
		$("textarea").attr("readonly","readonly");
		$(".normal_btn").each(function(){
			 if($(this).val()=="删 除" || $(this).val()=="添加附件"){
				 $(this).hide();
			} 
		});
	});
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
     		<span id="dealer_name">${t.DEALER_SHORTNAME }</span>
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
		<td width="5%" nowrap="true" ></td>
     	<td width="12.5%" nowrap="true" >
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
     	<td width="17.5%" nowrap="true" >
     		<input class="middle_txt" name="supply_code_dealer" id="supply_code_dealer" value="${t.SUPPLY_CODE_DEALER }" readonly="readonly" type="hidden"  /><span>${t.SUPPLY_CODE_DEALER }</span>
     	</td>
     	<td width="5%" nowrap="true" ></td>
	</tr>
</table>
<table border="1" cellpadding="1" cellspacing="1"  class="tab_edit" width="100%" style="text-align: center;">
	<tr>
		<td width="5%" nowrap="true" ></td>
     	<td width="12.5%" nowrap="true" >
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
     	<td width="17.5%" nowrap="true" >
     		<span id="carType">${t.MODEL_NAME }</span>
     	</td>
     	<td width="5%" nowrap="true" ></td>
	</tr>
	<tr>
		<td width="5%" nowrap="true" ></td>
     	<td width="12.5%" nowrap="true" >
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
     	<td width="17.5%" nowrap="true" >
     		<input class="middle_txt" id="mileage" name="mileage" maxlength="30" value="${t.MILEAGE }" type="hidden"  /><span>${t.MILEAGE }</span>
     	</td>
     	<td width="5%" nowrap="true" ></td>
	</tr>
	<tr>
		<td width="5%" nowrap="true" ></td>
     	<td width="12.5%" nowrap="true" >
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
     	<td width="17.5%" nowrap="true" >
     		<input class="middle_txt" id="problem_date" name="problem_date"  value="<fmt:formatDate value="${t.PROBLEM_DATE}" pattern='yyyy-MM-dd HH:mm'/>" readonly="readonly"  onfocus="$(this).calendar()" type="hidden"  /><span><fmt:formatDate value="${t.PROBLEM_DATE}" pattern='yyyy-MM-dd HH:mm'/></span>
     	</td>
     	<td width="5%" nowrap="true" ></td>
	</tr>
	<tr>
		<td width="5%" nowrap="true" ></td>
     	<td width="12.5%" nowrap="true" style="font-weight: bold;">
     		车主信息
     	</td>
     	<td colspan="5"></td>
     	<td width="5%" nowrap="true" ></td>
	</tr>
	<tr>
		<td width="5%" nowrap="true" ></td>
     	<td width="12.5%" nowrap="true" >
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
     	<td width="17.5%" nowrap="true" >
     		<input class="middle_txt" id="job" name="job" maxlength="30" value="${t.JOB }" type="hidden"  /><span>${t.JOB }</span>
     	</td>
     	<td width="5%" nowrap="true" ></td>
	</tr>
	<tr>
		<td width="5%" nowrap="true" ></td>
     	<td width="12.5%" nowrap="true" >
     		地址：
     	</td>
     	<td colspan="5">
     		<span id="userAddr">${t.ADDRESS }</span>
     	</td>
     	<td width="5%" nowrap="true" ></td>
     </tr>
     <tr>
		<td width="5%" nowrap="true" ></td>
     	<td width="12.5%" nowrap="true" >
     		申请金额：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<input class="middle_txt" id="apply_money" name="apply_money" maxlength="30" value="${t.APPLY_MONEY }" type="hidden"  /><span>${t.APPLY_MONEY }</span>
     	</td>
     	<td width="12.5%" colspan="4" nowrap="true" >
     	</td>
     	<td width="5%" nowrap="true" ></td>
	</tr>
</table>
<table border="1" cellpadding="1" cellspacing="1"  class="tab_edit" width="100%" style="text-align: center;">
	<tr>
		<td width="5%" nowrap="true" ></td>
     	<td  width="12.5%" nowrap="true" style="font-weight: bold;" >
     		事件主题:
    	</td>
    	<td  width="77.5%">
    		<textarea style="width: 100%;height: 40px;" readonly="readonly" id="event_theme" name="event_theme" >${t.EVENT_THEME }</textarea>
    	</td>
    	<td width="5%" nowrap="true" ></td>
    </tr>
    <tr>
		<td width="5%" nowrap="true" ></td>
     	<td  width="12.5%" nowrap="true" >
     		投诉内容及车主意见:
    	</td>
    	<td  width="77.5%">
    		<textarea style="width: 100%;height: 40px;" readonly="readonly" id="complain_advice" name="complain_advice" >${t.COMPLAIN_ADVICE }</textarea>
    	</td>
    	<td width="5%" nowrap="true" ></td>
    </tr>
    <c:if test="${t.STATUS>=20331002}">
    <tr>
		<td width="5%" nowrap="true" ></td>
     	<td  width="12.5%" nowrap="true" >
     		服务经理处理结果:
    	</td>
    	<td  width="77.5%">
    		<textarea style="width: 100%;height: 40px;" id="service_manager_deal" name="service_manager_deal"  >${t.SERVICE_MANAGER_DEAL }</textarea>
    	</td>
    	<td width="5%" nowrap="true" ></td>
    </tr>
    <tr>
		<td width="5%" nowrap="true" ></td>
     	<td  colspan="2"  nowrap="true" style="text-align: right;">
     		<change:user value="${t.MANAGER_AUDIT_BY }" showType="0"/>  &nbsp;&nbsp;&nbsp; <fmt:formatDate value="${t.MANAGER_AUDIT_DATE }" pattern='yyyy-MM-dd HH:mm'/>&nbsp;&nbsp;
     	</td>
    	<td width="5%" nowrap="true" ></td>
    </tr>
    </c:if>
     <c:if test="${t.STATUS>=20331003}">
    <tr>
		<td width="5%" nowrap="true" ></td>
     	<td  width="12.5%" nowrap="true" >
     		区域经理意见:
    	</td>
    	<td  width="77.5%">
    		<textarea style="width: 100%;height: 40px;" id="regional_manager_deal" name="regional_manager_deal"  >${t.REGIONAL_MANAGER_DEAL }</textarea>
    	</td>
    	<td width="5%" nowrap="true" ></td>
    </tr>
    <tr>
		<td width="5%" nowrap="true" ></td>
     	<td  colspan="2"  nowrap="true" style="text-align: right;">
     	   	<change:user value="${t.REGIONAL_AUDIT_BY }" showType="0"/>  &nbsp;&nbsp;&nbsp; <fmt:formatDate value="${t.REGIONAL_AUDIT_DATE }" pattern='yyyy-MM-dd HH:mm'/>&nbsp;&nbsp;
     	</td>
    	<td width="5%" nowrap="true" ></td>
    </tr>
    </c:if>
     <c:if test="${t.STATUS>=20331005}">
    <tr>
		<td width="5%" nowrap="true" ></td>
     	<td  width="12.5%" nowrap="true" >
     		区域总监意见:
    	</td>
    	<td  width="77.5%">
    		<textarea style="width: 100%;height: 40px;" id="regional_director_deal" name="regional_director_deal"  >${t.REGIONAL_DIRECTOR_DEAL }</textarea>
    	</td>
    	<td width="5%" nowrap="true" ></td>
    </tr>
    <tr>
		<td width="5%" nowrap="true" ></td>
     	<td  colspan="2"  nowrap="true" style="text-align: right;">
     		<change:user value="${t.DIRECTOR_AUDIT_BY }" showType="0"/>  &nbsp;&nbsp;&nbsp; <fmt:formatDate value="${t.DIRECTOR_AUDIT_DATE }" pattern='yyyy-MM-dd HH:mm'/>&nbsp;&nbsp;
     	</td>
    	<td width="5%" nowrap="true" ></td>
    </tr>
    </c:if>
    <c:if test="${t.STATUS>=20331007}">
    <tr>
		<td width="5%" nowrap="true" ></td>
     	<td  width="12.5%" nowrap="true" >
     		技术支持部意见:
    	</td>
    	<td  width="77.5%">
    		<textarea style="width: 100%;height: 40px;" id="tec_support_dep_deal" name="tec_support_dep_deal"  >${t.TEC_SUPPORT_DEP_DEAL }</textarea>
    	</td>
    	<td width="5%" nowrap="true" ></td>
    </tr>
    <tr>
		<td width="5%" nowrap="true" ></td>
     	<td  colspan="2"  nowrap="true" style="text-align: right;">
     		 主因件代码：<input class="middle_txt"  type="hidden" readonly="readonly" id="tec_part_code" name="tec_part_code" value="${t.TEC_PART_CODE }"/>${t.TEC_PART_CODE }&nbsp;&nbsp;供应商代码：<input class="middle_txt"  type="hidden" id="tec_supply_code" name="tec_supply_code" value="${t.TEC_SUPPLY_CODE }" readonly="readonly" />${t.TEC_SUPPLY_CODE }&nbsp;<change:user value="${t.TEC_AUDIT_BY }" showType="0"/>  &nbsp;&nbsp;&nbsp; <fmt:formatDate value="${t.TEC_AUDIT_DATE }" pattern='yyyy-MM-dd HH:mm'/>&nbsp;&nbsp;
     	</td>
    	<td width="5%" nowrap="true" ></td>
    </tr>
    </c:if>
</table>
<c:if test="${t.STATUS>=20331009}">
 <table border="1" cellpadding="1" cellspacing="1"  class="tab_edit" width="100%" style="text-align: center;">
		<tr>
				<td nowrap="true" width="5%" ></td>
				<td nowrap="true" width="45%" style="font-weight: bold;" align="left">&nbsp;索赔管理部意见:</td>
				<td nowrap="true" width="45%" style="font-weight: bold;" align="left">&nbsp;服务支持中心意见:</td>
				<td nowrap="true" width="5%" ></td>
		</tr>
		<tr>
				<td nowrap="true" width="5%" ></td>
				<td nowrap="true" width="45%" >
					<textarea style="font-weight: bold;" name="claim_settlement_deal" id="claim_settlement_deal" rows="4" cols="55">${t.CLAIM_SETTLEMENT_DEAL }</textarea>
				</td>
				<td nowrap="true" width="45%" >
					<textarea style="font-weight: bold;" rows="4" cols="55" readonly="readonly"></textarea>
				</td>
				<td nowrap="true" width="5%" ></td>
		</tr>
		 <tr>
			<td width="5%" nowrap="true" ></td>
	     	<td    nowrap="true" style="text-align: right;">
	     		审核费：<input class="middle_txt"  name="audit_amount" id="audit_amount" type="hidden"  value="${t.AUDIT_AMOUNT }" />${t.AUDIT_AMOUNT }&nbsp;&nbsp;&nbsp;<change:user value="${t.SETTLEMENT_AUDIT_BY }" showType="0"/>  &nbsp;&nbsp;&nbsp; <fmt:formatDate value="${t.SETTLEMENT_AUDIT_DATE }" pattern='yyyy-MM-dd HH:mm'/>&nbsp;&nbsp;
	     	</td>
	     	<td    nowrap="true" style="text-align: right;">
	     		年&nbsp;&nbsp;月&nbsp;&nbsp;日&nbsp;&nbsp;
	     	</td>
	    	<td width="5%" nowrap="true" ></td>
    	</tr>
		<tr>
				<td nowrap="true" width="5%" ></td>
				<td nowrap="true" width="45%" style="font-weight: bold;" align="left">&nbsp;总经理意见:</td>
				<td nowrap="true" width="45%" style="font-weight: bold;" align="left">&nbsp;总裁意见:</td>
				<td nowrap="true" width="5%" ></td>
		</tr>
		<tr>
				<td nowrap="true" width="5%" ></td>
				<td nowrap="true" width="45%" >
					<textarea style="font-weight: bold;" name="trouble_reason" id="trouble_reason" rows="4" cols="55">${t.TROUBLE_REASON }</textarea>
				</td>
				<td nowrap="true" width="45%" >
					<textarea style="font-weight: bold;" name="trouble_desc" id="trouble_desc" rows="4" cols="55">${t.TROUBLE_DESC }</textarea>
				</td>
				<td nowrap="true" width="5%" ></td>
		</tr>
		<tr>
			<td width="5%" nowrap="true" ></td>
	     	<td    nowrap="true" style="text-align: right;">
	     		年&nbsp;&nbsp;月&nbsp;&nbsp;日&nbsp;&nbsp;
	     	</td>
	     	<td    nowrap="true" style="text-align: right;">
	     		年&nbsp;&nbsp;月&nbsp;&nbsp;日&nbsp;&nbsp;
	     	</td>
	    	<td width="5%" nowrap="true" ></td>
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
  		
<table width=100% border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
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