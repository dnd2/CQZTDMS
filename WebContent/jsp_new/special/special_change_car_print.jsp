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
			isradio($("#is_change").val(),"is_change");
			isradio($("#old_car_deal").val(),"old_car_deal");
			isradio($("#is_insurance").val(),"is_insurance");
			isradio($("#is_warranty").val(),"is_warranty");
	});
		//公共的radio
		function isradio(val,className){
			$("."+className).each(function(){
				if(val==$(this).val()){
					$(this).attr("checked",true);
				}
				$(this).attr("disabled","disabled");
			});
		}
		function printsetup(){    
			// 打印页面设置    
			wb.execwb(8,1);    
		} 

		function printpreview(){    
			// 打印页面预览      
			wb.execwb(7,1);    
		}      
		function printit(){    
			if (confirm('确定打印吗？')){
			wb.execwb(6,6)    
			}    
		}    

		function nowprint() {   
		    window.print();   
		}   
		function window.onbeforeprint() {   
		    eval(visble_property_printview + " = \"" + visble_property_false + "\"");   
		}   
		function window.onafterprint() {   
		    eval(visble_property_printview + " = \"" + visble_property_true + "\"");   
		}   
		$('topContainer').style.height=document.viewport.getHeight();

		function sxsw(){
			WebBrowser.ExecWB(7,1); 
			window.opener=null; 
			window.close();
		}
		function printsetup()
		{       
			wb.execwb(8,1);    // 打印页面设置 
		}    
		function printpreview()
		{    
			wb.execwb(7,1);   // 打印页面预览       
		}      
		function printit()    
		{    
			if(confirm('确定打印吗？'))
			{    
				wb.execwb(6,6); 
			}    
		} 
</script>
<style media=print>
    /* 应用这个样式的在打印时隐藏 */
    .Noprint {
     display: none;
    }
   
    /* 应用这个样式的，从那个标签结束开始另算一页，之后在遇到再起一页，以此类推 */
    .PageNext {
     page-break-after: always;
    }
   </style>
</head>
<body>
  <object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;特殊费用管理&gt;特殊费用(退换车)
</div>
  <div id="topContainer" style="text-align: center;" >
<form name="fm" id="fm" method="post">
<input class="middle_txt" id="specialNo" value="${specialNo }" type="hidden"  />
<input type="hidden" id="dealerCode" value="${dealerCode }">
<input type="hidden" id="is_change" value="${t.IS_CHANGE }"/>
<input type="hidden" id="old_car_deal" value="${t.OLD_CAR_DEAL }"/>
<input type="hidden" id="is_insurance" value="${t.IS_INSURANCE }"/>
<input type="hidden" id="is_warranty" value="${t.IS_WARRANTY }"/>
<input type="hidden" id="status" name="status" value="${t.STATUS }">
<input class="middle_txt" id="type" value="${type }" type="hidden"  />
<input class="middle_txt" id="spe_id" value="${t.SPE_ID }" name="spe_id" type="hidden"  />
<input class="middle_txt" id="special_type" value="0" name="special_type" type="hidden"  />
<table class="tab_print" width="920px" >
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
<table class="tab_print" width="920px"  style="text-align: center;font-weight: bold;">
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
<table class="tab_print" width="920px"  style="text-align: center;" >
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
	<table class="tab_print" width="920px"  style="text-align: left;" >
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

<table class="tab_print" width="920px"  style="font-weight: bold;">
	<c:if test="${t.STATUS>=20331002}">
	<tr>
		<td width="4%" rowspan="8" ></td>
     	<td width="10%" nowrap="true" >
     		服务经理意见:
     	</td>
     	<td width="86%" nowrap="true" >
    		<textarea style="width: 100%;height: 40px;" id="service_manager_deal" name="service_manager_deal"  >${t.SERVICE_MANAGER_DEAL }</textarea>
     	</td>
	</tr>
	<tr>
     	<td colspan="3" nowrap="true" style="text-align: right;">
     		 <change:user value="${t.MANAGER_AUDIT_BY }" showType="0"/>  &nbsp;&nbsp;&nbsp; <fmt:formatDate value="${t.MANAGER_AUDIT_DATE }" pattern='yyyy-MM-dd HH:mm'/>&nbsp;&nbsp;
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
     	   	<change:user value="${t.REGIONAL_AUDIT_BY }" showType="0"/>  &nbsp;&nbsp;&nbsp; <fmt:formatDate value="${t.REGIONAL_AUDIT_DATE }" pattern='yyyy-MM-dd HH:mm'/>&nbsp;&nbsp;
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
     		<change:user value="${t.DIRECTOR_AUDIT_BY }" showType="0"/>  &nbsp;&nbsp;&nbsp; <fmt:formatDate value="${t.DIRECTOR_AUDIT_DATE }" pattern='yyyy-MM-dd HH:mm'/>&nbsp;&nbsp;
     	</td>
	</tr>
	</c:if>
	<c:if test="${t.STATUS>=20331007}">
	<tr>
     	<td width="10%" nowrap="true" >
     		技术支持部:
     	</td>
     	<td width="86%" nowrap="true" >
    		<textarea style="width: 100%;height: 40px;" id="tec_support_dep_deal" name="tec_support_dep_deal"  >${t.TEC_SUPPORT_DEP_DEAL }</textarea>
     	</td>
	</tr>
	<tr>
     	<td colspan="3" nowrap="true" style="text-align: right;">
			责任方代码：<input class="middle_txt"  type="hidden" readonly="readonly" id="responsibility_code" name="responsibility_code" value="${t.RESPONSIBILITY_CODE }"/>${t.RESPONSIBILITY_CODE }&nbsp;&nbsp;是否享受质保：<input type="radio" class="is_warranty" name="is_warranty"  value="是"/>是&nbsp;<input type="radio" class="is_warranty" name="is_warranty"  value="否"/>否&nbsp;&nbsp;<change:user value="${t.TEC_AUDIT_BY }" showType="0"/>  &nbsp;&nbsp;&nbsp; <fmt:formatDate value="${t.TEC_AUDIT_DATE }" pattern='yyyy-MM-dd HH:mm'/>&nbsp;&nbsp;
     	</td>
	</tr>
	</c:if>
</table>
<c:if test="${t.STATUS>=20331009}">
 <table class="tab_print" width="920px" >
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
	     			&nbsp;&nbsp;销售政策:<input class="short_txt"  id="sales_policy_money" name="sales_policy_money" maxlength="10" value="${t.SALES_POLICY_MONEY }" type="hidden"  />${t.SALES_POLICY_MONEY }元 &nbsp;&nbsp;审批金额: <input class="short_txt" id="audit_amount" name="audit_amount" maxlength="10" value="${t.AUDIT_AMOUNT }" type="hidden"  />${t.AUDIT_AMOUNT }元&nbsp;&nbsp; <change:user value="${t.SETTLEMENT_AUDIT_BY }" showType="0"/>&nbsp;&nbsp;<fmt:formatDate value="${t.SETTLEMENT_AUDIT_DATE }" pattern='yyyy-MM-dd HH:mm'/>
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
<br>
<table class="table_query" width="920px">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
				    <p class="Noprint">
						<input type=button name= button_print class="normal_btn" value="打印" onclick ="printit();">
						<input type=button name= button_print class="long_btn" value="打印页面设置" onclick ="printsetup();">
						<input type=button name= button_print class="normal_btn" value="打印预览" onclick ="printpreview();">
						<input type="button" id="back" onClick="history.back();" class="normal_btn"  style="width=8%" value="返回"/>
				    </p>
   			</td>
           	<td height="12" align=center width="33%">
     			</td>
		</tr>
</table>
</form>
</div>
</body>
</html>