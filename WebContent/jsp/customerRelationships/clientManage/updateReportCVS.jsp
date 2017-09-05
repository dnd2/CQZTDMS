<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="com.infodms.dms.po.TtDealerActualSalesPO"%>
<%@ page import="com.infodms.dms.po.TtCustomerPO"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
	String contextPath = request.getContextPath();
	Map<String,Object> vehicleInfo = (Map<String,Object>)request.getAttribute("vehicleInfo");
	String vehicleId = vehicleInfo.get("VEHICLE_ID").toString();
	String oldflag = Constant.IS_OLD_CTM_02+"";
	int s_ctmType = Constant.CUSTOMER_TYPE_02;//客户类型：公司客户
	int yes = Constant.IF_TYPE_YES;			  //"是"
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<script type="text/javascript">
	function doInit(){
		loadcalendar();   //初始化时间控件
		genLocSel('txt1','txt2','txt3','','',''); // 加载省份城市和县
		// genLocSel('txt4','txt5','txt6','','',''); // 加载省份城市和县
		//setJson("1");
	}

</script>

<title>实销信息上报</title>
</head>
<body onunload='javascript:destoryPrototype();' onload='initData();'>
<div class="wbox" id="wbox" >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 整车销售 &gt; 客户信息管理 &gt; 实销信息上报</div>
<form id="fm" name="fm" method="post"><input type="hidden" name="curPage" id="curPage" value="1" /> 
<input type="hidden" name="knowaddress" id="knowaddress" value="<%=Constant.KNOW_ADDRESS%>"></input>
<input type="hidden" name="ndate" id="ndate" value="${d }"/>
<input type="hidden" id="dlrId" name="dlrId" value="" />
<c:if test="${salesLogList != null }">
	<table class="table_edit" align="center" border="1">
		
			<tr class="tabletitle">
				<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />审核记录</th>
			</tr>
			<tr>
				<td align="center" width="20%">审核部门</td>
				<td align="center" width="10%">审核人</td>
				<td align="center" width="50%">内容</td>
				<td align="center" width="20%">审核时间</td>
			</tr>
			<c:forEach var="logs" items="${salesLogList }">
				<tr>
					<td align="center">${logs.ORG_NAME }</td>
					<td align="center">${logs.NAME }</td>
					<td align="center">${logs.REMARK }</td>
					<td align="center">${logs.LOG_DATE }</td>
				</tr>
			</c:forEach>
	</table>
</c:if>
<table class="table_edit" align=center>
	<tr class="tabletitle">
		<th colspan="4">
			<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
				车辆资料<br/>
		</th>
	</tr>
	<tr>
		<td width="15%" align=right>VIN：</td>
		<td align=left>${vehicleInfo.VIN }</td>
		<td align=right>发动机号：</td>
		<td align=left>${vehicleInfo.ENGINE_NO }</td>
	</tr>
	<tr>
		<td align=right>车系：</td>
		<td align=left>${vehicleInfo.SERIES_NAME }</td>
		<td align=right>车型：</td>
		<td align=left>${vehicleInfo.MODEL_NAME }<input type="hidden" id="model_name" name="model_name" value="${vehicleInfo.MODEL_NAME }" /></td>
	</tr>
	<tr>
		<td align=right>物料代码：</td>
		<td align=left>${vehicleInfo.MATERIAL_CODE }</td>
		<td align=right>物料名称：</td>
		<td align=left>${vehicleInfo.MATERIAL_NAME }</td>
	</tr>
	<tr>
		<td align=right>颜色：</td>
		<td align=left>${vehicleInfo.COLOR }</td>
		<td align=right>&nbsp;</td>
		<td align=left>&nbsp;</td>
	</tr>
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />销售信息</th>
		<input type="hidden" id="orderId" name="orderId" value="${salesInfo.orderId }" /><!-- 实销主键ID修改用 -->
		<input type="hidden" id="ctmId" name="ctmId" value="${customerInfo.ctmId }" /><!-- 客户信息主键ID修改用 -->
	</tr>
	<tr>
		<td align="right">车牌号：</td>
		<td width="35%" align="left">
			<input id="vehicle_no" name="vehicle_no" type="text" class="middle_txt" value="${salesInfo.vehicleNo }" datatype="1,is_carno,20"
			maxlength="20" />
		</td>
		<td width="15%" align="right">合同编号：</td>
		<td width="35%" align="left">
			<input id="contract_no" name="contract_no" type="text" class="middle_txt" value="${salesInfo.contractNo }" datatype="1,is_textarea,25"
			maxlength="25" />
		</td>
	</tr>
	<tr>
		<td align="right">开票日期：</td>
		<td align="left">
			<input name="invoice_date" id="invoice_date" type="text" readonly="readonly" class="short_txt" value="${invoiceDate }" datatype="0,is_date,10" hasbtn="true"
			callFunction="showcalendar(event, 'invoice_date', false);" /></td>
		<td align="right">发票编号：</td>
		<td align="left">
			<input id="invoice_no" name="invoice_no" type="text" class="middle_txt" value="${salesInfo.invoiceNo }" datatype="0,is_textarea,25" maxlength="25" />
		</td>
	</tr>
	<tr>
		<td align="right">保险公司：</td>
		<td align="left"><input id="insurance_company" name="insurance_company" type="text" class="middle_txt" value="${salesInfo.insuranceCompany }"
			datatype="1,is_textarea,50" maxlength="50" /></td>
		<td align="right">保险日期：</td>
		<td align="left"><input name="insurance_date" id="insurance_date" type="text" readonly="readonly" class="short_txt" value="${insuranceDate }" datatype="1,is_date,10" hasbtn="true"
			callFunction="showcalendar(event, 'insurance_date', false);" /></td>
	</tr>
	<tr>
		<td align="right">车辆交付日期：</td>
		<td align="left"><input name="consignation_date" id="consignation_date" readonly="readonly" type="text" class="short_txt" value="${consignationDate }" datatype="0,is_date,10" hasbtn="true"
			callFunction="showcalendar(event, 'consignation_date', false);" /></td>
		<td align="right">交付时公里数：</td>
		<td align="left"><input id="miles" name="miles" type="text" class="middle_txt" value="${salesInfo.miles }" datatype="1,is_double,6" maxlength="6" /></td>
	</tr>
	<tr>
		<td align="right">付款方式：</td>
		<td align="left"><label> <script type="text/javascript">
					genSelBoxExp("payment",<%=Constant.PAYMENT%>,"<%=Constant.PAYMENT_02%>",false,"short_sel","onchange='changeMortgageType(this.value)'","false",'');
					$("payment").value = ${salesInfo.payment };
				</script> </label><font color="red">*</font></td>
		<td align="right">价格：</td>
		<td align="left"><input id="price" name="price" type="text" class="middle_txt" value="${salesInfo.price }" datatype="0,is_double,12" maxlength="10" /></td>
	</tr>
	<tr>
		<td align="right">车辆性质：</td>
		<td align="left"><label> 
			<script type="text/javascript">
					genSelBoxExp("carCharactor",<%=Constant.SERVICEACTIVITY_CHARACTOR%>,<%=Constant.SERVICEACTIVITY_CHARACTOR_16%>,false,"short_sel",'',"false",'10721004,10721005');
			</script><font color="red">*</font></label>
		</td>
		<td align="right">销售顾问：</td>
		<td align="left">
			<select class="short_sel" name="salesCon" id="salesCon">
				<option value="">-请选择-</option>
				<c:forEach items="${scList }" var="scMap">
					<option value="${scMap.ID }">${scMap.NAME }</option>
				</c:forEach>
			</select>
			<script type="text/javascript">
				$("salesCon").value = ${salesInfo.salesConId};
			</script>
			<font color="red">*</font>
		</td>
	</tr>
	<tr id="MORTGAGE_TYPE"  style="display: none;">
	
		<td align="right">按揭类型：</td>
		<td align="left"><label> 
			<script type="text/javascript">
					genSelBoxExp("mortgageType",<%=Constant.MORTGAGE_TYPE%>,'',true,"short_sel",'',"false",'');
			</script><font color="red">*</font></label>
		</td>
		<td align="right">首付比例：</td>
		<td align="left"><input id="FirstPrice" name="FirstPrice" type="text" class="middle_txt" value=""  />%<font color="red">*</font></td>
		
	</tr>
	<tr id="Loans"  style="display: none;">
	
		<td align="right">贷款方式：</td>
		<td align="left"><label> 
			<script type="text/javascript">
					genSelBoxExp("LoansType",<%=Constant.Loans%>,'',true,"short_sel",'',"false",'');
			</script><font color="red">*</font></label>
		</td>
		<td align="right">贷款年限：</td>
		<td align="left"><input id="LoansYear" name="LoansYear" type="text" class="middle_txt" value=""  /><font color="red">*</font></td>
		
	</tr>
	<tr>
	<!-- 
		<td align="right">是否本店销售：</td>
		<td align="left">
			<script type="text/javascript">
				genSelBoxExp("isOwnSale",<%=Constant.IF_TYPE%>,'',true,"short_sel",'',false,'');
			</script>
			<font color="red">*</font>
		</td>
	 -->	
		 	<td align="right">是否异地客户：</td>
			<td align="left">
			<script type="text/javascript">
				genSelBoxExp("isUnCust",<%=Constant.IF_TYPE%>,'<%=Constant.IF_TYPE_NO%>',true,"short_sel",'',false,'');
				$("isUnCust").value = ${salesInfo.isUnCust};
			</script>
			<td align="right">是否营运：</td>
			<td align="left">
			<script type="text/javascript">
				genSelBoxExp("isForBusi",<%=Constant.IF_TYPE%>,'<%=Constant.IF_TYPE_NO%>',false,"short_sel",'',false,'');
				$("isForBusi").value = ${salesInfo.isForBusi};
			</script>
			<font color="red">*</font>
			</td>
	</tr>
	<tr>
		<td align="right">备注：</td>
		<td align="left"><input type="text" id="memo" name="memo" value="${salesInfo.memo }" datatype="1,is_textarea,60" maxlength="60" size="50" /></td>
		<td align="right">销售类型：</td>
		<td align="left">
			<select class="short_sel" name="sale_type" id="sale_type">
				<option value="">-请选择-</option>
				<option value="1">个人销售</option>
				<option value="2">公司销售</option>
			</select>
			<script type="text/javascript">
				$("sale_type").value = ${salesInfo.saleType};
			</script>
			<font color="red">*</font>
		</td>
	</tr>
</table>
<table class="table_edit" align="center" id="customer_type_table">
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />客户类型</th>
	</tr>
	<tr>
		<td width="15%" align=right id="select_ctm_type_id">选择客户类型：</td>
		<td width="35%" align="left" id="select_ctm_type_id_1">
			<script type="text/javascript">
				genSelBox("customer_type",<%=Constant.CUSTOMER_TYPE%>,"",false,"short_sel","onchange='changeCustomerType(this.value)'");
			</script><font color="red">*</font>
		</td>
		<td width="15%" align="right">是否团购车：</td>
		<td width="35%" align="left">
			<input type="radio" id="is_fleet_yes" name="is_fleet" onclick="is_fleet_Sel(<%=Constant.IF_TYPE_YES %>);" value="<%=Constant.IF_TYPE_YES %>" />是 
			<input type="radio" id="is_fleet_no" name="is_fleet" onclick="is_fleet_Sel(<%=Constant.IF_TYPE_NO %>);" value="<%=Constant.IF_TYPE_NO %>" checked="checked" />否
		</td>
	</tr>
	<tbody id="is_fleet_show" style="display: none;">
		<tr>
			<td align="right">集团客户名称：</td>
			<td align="left">
				<input id="fleet_name" name="fleet_name" type="text" value="" class="middle_txt" datatype="0,is_textarea,30" size="30" readonly="readonly" /> 
				<input id="fleet_id" name="fleet_id" value="" type="hidden" class="middle_txt" /> 
				<input id="type" name="type" value="" type="hidden"/> 
				<input type="button" value="..." class="mini_btn" onclick="toFleetList();" /></td>
			<td align="right" style="display:none">集团客户合同：</td>
			<td align="left">
				<!--<input id="fnoid" name="fleet_contract_no" type="text" class="middle_txt" value="" datatype="0,is_textarea,20" size="20" readonly="readonly" /> 
				 --><!-- 大客户合同id -->
				<!--<input id="fleet_contract_no_id" value="${salesInfo.contractId }" name="fleet_contract_no_id" type="hidden" />
				<input type="button" value="..." class="mini_btn" onclick="toFleetContractList();" />
				 --><!-- 大客户审核状态 --> 
				<input id="fleet_check_status" name="fleet_check_status" type="hidden" />
				<input id="fleet_contract_no_id" value="${salesInfo.contractId}" name="fleet_contract_no_id" type="hidden" />
				<input id="fleet_contract_no" name="fleet_contract_no" type="hidden" />
				<span id="contractSpan" style="display:none"></span>
			</td>
		</tr>
	</tbody>
</table>
<table class="table_edit" align="center" id="company_table" style="display: none;">
	<tr class="tabletitle">
		<th colspan="4" align="right"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />公司客户信息</th>
	</tr>

	<tr>
		<td width="15%" align="right">公司名称：</td>
		<td width="35%" align="left"><input id="company_name" name="company_name" value="" type="text" class="middle_txt"
			datatype="0,is_textarea,60" maxlength="60" /></td>
		<td width="15%" align="right">公司简称：</td>
		<td width="35%" align="left"><input id="company_s_name" name="company_s_name" value="" type="text" class="middle_txt"
			datatype="1,is_textarea,60" maxlength="60" /></td>
	</tr>
	<tr>
		<td align="right">公司电话：</td>
		<td align="left"><input id="company_phone" name="company_phone" value="" type="text" class="middle_txt" size="20"
			datatype="0,is_digit,12" maxlength="12" /></td>
		<td align="right">公司规模 ：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("level_id",<%=Constant.COMPANY_SCOPE%>,"",false,"short_sel",'',"false",'');
				</script></td>
	</tr>
	<tr>
		<td align="right">公司性质：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("kind",<%=Constant.COMPANY_KIND%>,"",false,"short_sel",'',"false",'');
			</script></td>
		<td align="right">目前车辆数：</td>
		<td align="left"><input id="vehicle_num" name="vehicle_num" value="" type="text" class="middle_txt" size="20" datatype="1,is_digit,6"
			maxlength="6" /></td>
	</tr>
	<tr>
	<td align="right">客户来源：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("myctm_form",<%=Constant.CUSTOMER_FROM%>,"",false,"short_sel",'',"false",'');
			</script></td>
	</tr>
</table>
<div id="customerInfoId">
<table class="table_edit" align="center" id="ctm_table_id">
	<tr>
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />个人客户信息</th>
	</tr>
	<tr>
		<td width="15%" align="right" id="tcmtd">客户姓名：</td>
		<td width="35%" align="left">
			<input id="ctm_name" name="ctm_name" value="" type="text" class="middle_txt"  size="10" datatype="0,is_textarea,30" maxlength="30" /> 
			<input type="radio" id="sel_cus_type_new" name="sel_cus_type" value="<%=Constant.IS_OLD_CTM_01 %>" checked="checked" onclick="showCustSel(this.value)" />新客户
			<input type="radio" id="sel_cus_type_old" name="sel_cus_type" value="<%=Constant.IS_OLD_CTM_02 %>" onclick="showCustSel(this.value)" />老客户 
			<input type="button" id="showCustomerListID" value="..." class="mini_btn" onclick="showCustomerList();" disabled="disabled" /></td>
		<td width="15%" align="right" id="sextd">性别：</td>
		<td align="left">
			<script type="text/javascript">
					genSelBoxExp("sex",<%=Constant.GENDER_TYPE%>,"",false,"short_sel",'',"false",'');
			</script>
		</td>
	</tr>
</table>
<table class="table_edit" align="center" id="ctm_table_id_2">
	<tr>
		<td width="15%" align="right">证件类别：</td>
		<td width="35%" align="left"><script type="text/javascript">
					genSelBoxExp("card_type",<%=Constant.CARD_TYPE%>,"",false,"short_sel",'',"false",'');
			</script></td>
		<td width="15%" align="right">证件号码：</td>
		<td width="35%" align="left"><input id="card_num" name="card_num" value="" type="text" class="middle_txt" size="20"
			datatype="0,is_digit_letter,30" maxlength="30" /></td>
	</tr>
	<tr>
		<td align="right">主要联系电话：</td>
		<td align="left"><input id="main_phone" name="main_phone" value="" type="text" class="middle_txt" size="20" datatype="0,is_digit,15"
			maxlength="15" /></td>
		<td align="right">其他联系电话：</td>
		<td align="left"><input id="other_phone" value="" name="other_phone" type="text" class="middle_txt" size="20" datatype="1,is_digit,15"
			maxlength="15" /></td>
	</tr>
	<tr>
		<td align="right">电子邮件：</td>
		<td align="left"><input id="email" name="email" value="" type="text" class="middle_txt" size="20" datatype="1,is_email,70" maxlength="70" /></td>
		<td align="right">邮编：</td>
		<td align="left"><input id="post_code" name="post_code" value="" type="text" class="middle_txt" size="20" datatype="1,is_digit,6" maxlength="6"/></td>
	</tr>
	<tr>
		<td align="right">出生年月：</td>
		<td align="left"><input name="birthday" id="birthday" value="" type="text" class="short_txt" datatype="0,is_date,10" hasbtn="true" readonly="readonly"
			callFunction="showcalendar(event, 'birthday', false);" /></td>
		<td align="right">客户来源：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("ctm_form",<%=Constant.CUSTOMER_FROM%>,"",false,"short_sel",'',"false",'<%=Constant.CUSTOMER_FROM_01%>,<%=Constant.CUSTOMER_FROM_03%>,<%=Constant.CUSTOMER_FROM_12%>');
			</script><font color="red">*</font></td>
	</tr>
	<tr>
		<td align="right">家庭月收入：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("income",<%=Constant.EARNING_MONTH%>,"",false,"short_sel",'',"false",'');
			</script><font color="red">*</font></td>
		<td align="right">教育程度：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("education",<%=Constant.EDUCATION_TYPE%>,"",false,"short_sel",'',"false",'');
			</script><font color="red">*</font></td>
	</tr>
	<tr>
		<td align="right">所在行业：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("industry",<%=Constant.TRADE_TYPE%>,"",false,"short_sel",'',"false",'');
			</script><font color="red">*</font></td>
		<td align="right">婚姻状况：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("is_married",<%=Constant.MARRIAGE_TYPE%>,"",false,"short_sel",'',"false",'');
			</script><font color="red">*</font></td>
	</tr>
	<tr>
		<td align="right">职业：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("profession",<%=Constant.PROFESSION_TYPE%>,"",false,"short_sel",'',"false",'');
			</script><font color="red">*</font></td>
		<td align="right">职务：</td>
		<td align="left"><input id="job" name="job" value="" type="text" class="middle_txt" size="20" datatype="1,is_textarea,50" maxlength="50" /></td>
	</tr>
	<tr>
		<td align="right">购买用途：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("salesaddress",<%=Constant.SALES_ADDRESS%>,"",false,"short_sel",'',"false",'');
			</script><font color="red">*</font></td>
		<td align="right">购买原因：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("salesreson",<%=Constant.SALES_RESON%>,"",false,"short_sel",'',"false",'');
			</script><font color="red">*</font></td>
	</tr>
</table>
</div>
<table  class="table_edit" align="center">
	<tr>
		<td align="right"  width="15%">客户星级：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("custStars",<%=Constant.GUEST_STARS%>,"",false,"short_sel",'',"false",'');
			</script></td>
	</tr>
	<tr style="display: none">
		<td align="right"  width="15%">二手车置换：</td>
		<td align="left" colspan="2">
			<input type="radio" name="secondeVeh" id="sYes" value="10041001"/>是
			<input type="radio" name="secondeVeh" id="sNo" value="10041002" checked="checked" />否
		</td>
	</tr>
	<tr>
		<td align="right"><strong>所在地选择</strong></td>
		<td align="left" colspan="3"><strong><font color="red">∨</font></strong></td>
	</tr>
	<tr>
		<td align="right">省份：</td>
		<td align="left" colspan="3">
			 <select class="short_sel" id="txt1" name="province" onchange="_genCity(this,'txt2')"></select> 
		</td>
	</tr>
	<tr>
		<td align="right">地级市：</td>
		<td align="left" colspan="3">
			 <select class="short_sel" id="txt2" name="city" onchange="_genCity(this,'txt3')"></select> 
		</td>
	</tr>
	<tr>
		<td align="right">区、县：</td>
		<td align="left" colspan="3">
			 <select class="short_sel" id="txt3" name="district"></select><font color="red">*</font>
		</td>
	</tr>
	<tr>
		<td align="right">详细地址：</td>
		<td align="left" colspan="3">
			<input id="address" name="address" type="text" class="long_txt" style="width:41%" size="80" value="" datatype="0,is_textarea,200" maxlength="200" />
		</td>
	</tr>
</table>
<table class="table_list" id="file">
    <tr class= "tabletitle">
      <td align = "left" colspan="3"><img class="nav" src="<%=contextPath%>/img/nav.gif" />其他联系人信息 <img src="<%=contextPath%>/img/add.png" alt="新增" width="16" height="16" align="absmiddle"  onclick="addTblRow();"/></td>
    </tr>
    <tr>
      <th width="13%" nowrap="nowrap" >姓名</th>
      <th width="14%" nowrap="nowrap" >主要联系电话</th>
      <th width="16%" nowrap="nowrap" >其他联系电话</th>
      <th width="45%" nowrap="nowrap" >联系目的</th>
      <th width="12%" nowrap="nowrap" >操作</th>
    </tr>
</table>
<table class="table_query" id="submitTable">
	<tr align="center">
		<td>
			<input type="button" id="re_Id" value="保 存" class="normal_btn" onclick="doReport();" /> 
			<input type="button" value="返 回" class="normal_btn" onclick="goback();" /> 
			<!--1.上报车辆id  --> 
			<input type="hidden" id="vehicle_id" name="vehicle_id" value="<%=vehicleInfo.get("VEHICLE_ID") %>" />
			<!--2.用户选择的老客户id  --> 
			<input type="hidden" id="oldCustomerId" name="oldCustomerId" value="" /> 
			<!--3.默认值为0，如果从接口得到的JSON值不为空，则值为1  --> 
			<input type="hidden" id="isJson" name="isJson" value="0" /> 
		</td>
	</tr>
</table>
</form>
</div>

<script type="text/javascript">

	

	//查询大客户
	function toFleetList(){
		OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/toQueryFleetList.do',800,600);
	}
	//查询大客户合同
	function toFleetContractList(){
		var fleet_id = document.getElementById("fleet_id").value;
		if(fleet_id == 0 || ""==fleet_id){
			MyAlert("请选择大客户!");
		}else{
			OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/toQueryFleetContractList.do',800,600);
		}
	}
	
	function checkSalesCon() {
		var itVal = document.getElementById("salesCon").value ;
		
		if(itVal.length == "") {
			return true ;
		}
		
		return true ;
	}
	//2013-7-16 rj insert 
	function isDisabled(type){
		//
		document.getElementById("select_ctm_type_id").disabled =type ;
		document.getElementById("select_ctm_type_id_1").disabled =type ;
		document.getElementById("company_table").disabled =type;
		document.getElementById("ctm_table_id_2").disabled =type;
		document.getElementById("ctm_name").disabled =type;
		document.getElementById("tcmtd").disabled =type;
		document.getElementById("sex").disabled =type;
		document.getElementById("sextd").disabled =type;
		document.getElementById("sYes").disabled = type;
		document.getElementById("sNo").disabled = type;
		document.getElementById("txt1").disabled =type;
		document.getElementById("txt2").disabled =type;
		document.getElementById("txt3").disabled =type;
		document.getElementById("address").disabled =type;
		document.getElementById("custStars").disabled = type;
	}
	//“新、老”客户
	function showCustSel(status){
		var oldCustomerId = document.getElementById("oldCustomerId").value;
		if(oldCustomerId=="" &&  status == "<%=oldflag%>"){
			clearTxt();
			document.getElementById("showCustomerListID").disabled =false;
			isDisabled(true);
			//queryOtherLinkMan(oldCustomerId);
			//新客户清除文泵框的值
		}else if(oldCustomerId!="" && status == "<%=oldflag%>"){//老客户已选页面跳过来 rj insert
			document.getElementById("showCustomerListID").disabled =false;
			isDisabled(false);
		}else{
			document.getElementById("showCustomerListID").disabled =true;
			clearTxt();
			isDisabled(false);
			//queryOtherLinkMan(" ");
		}
	} 
	//清除文泵框的值
	function clearTxt(){
		document.getElementById("oldCustomerId").value="";//老客户ID清除
		document.getElementById("ctm_name").value="";//客户名称
		document.getElementById("txt1").value="";
		document.getElementById("txt2").value="";
		document.getElementById("txt3").value="";
		document.getElementById("address").value="";
		document.getElementById("card_num").value="";//证件号码
		document.getElementById("main_phone").value="";//主要联系电话
		document.getElementById("other_phone").value="";//其他联系电话
		document.getElementById("email").value="";//电子邮件
		document.getElementById("post_code").value="";//邮编
		document.getElementById("birthday").value="";//出生年月
	}
	function changeCustomerType(value){
		var ctm_type = <%=Constant.CUSTOMER_TYPE_01%>;//个人客户
		if(value == ctm_type){
			$("carCharactor").options.remove(3);
			$("carCharactor").options.remove(3);
			document.getElementById("company_table").style.display = "none";
			document.getElementById("customerInfoId").style.display = "inline";
			document.getElementById("file").style.display = "inline";
		}else{
			$("carCharactor").options.add(new Option("试乘试驾车","10721004"));
			$("carCharactor").options.add(new Option("售后服务车","10721005"));
			document.getElementById("company_table").style.display = "inline";
			document.getElementById("customerInfoId").style.display = "none";
			document.getElementById("file").style.display = "none";
		}
	}	
	//是否是大客户
	function is_fleet_Sel(value){
		var yes = '<%=yes%>';
		var s_ctmType = '<%=s_ctmType%>';
		var customer_type = document.getElementsByName("customer_type");
		if(yes == value){
			document.getElementById("is_fleet_yes").checked=true;
			document.getElementById("is_fleet_show").style.display = "inline";
			//document.getElementById("company_table").style.display = "none";
			//document.getElementById("ctm_table_id").style.display = "none";
			//document.getElementById("ctm_table_id_2").style.display = "none";
			//document.getElementById("select_ctm_type_id").disabled =true ;
			//document.getElementById("select_ctm_type_id_1").disabled =true ;
			//document.getElementById("sel_cus_type_new").disabled =true ;
			//document.getElementById("sel_cus_type_old").disabled =true ;

			//document.getElementById("file").style.display = "none";
		}else{
			document.getElementById("is_fleet_no").checked=true;
			document.getElementById("is_fleet_show").style.display = "none";
			//document.getElementById("ctm_table_id").style.display = "inline";
			//document.getElementById("ctm_table_id_2").style.display = "inline";
			//document.getElementById("select_ctm_type_id").disabled =false ;
			//document.getElementById("select_ctm_type_id_1").disabled =false ;
			//if(s_ctmType == customer_type[0].value){
			//	document.getElementById("company_table").style.display = "inline";
			//}else{
			//	document.getElementById("company_table").style.display = "none";
			//}
			//document.getElementById("sel_cus_type_new").disabled =false ;
			//document.getElementById("sel_cus_type_old").disabled =false ;
			//document.getElementById("file").style.display = "inline";
		}
	}
	
	//得到iframe信息，验证其他联系人填写是否正确
	function check_Link(){
		var check_Link_flag = true;
		var linkMan_names = document.getElementsByName("linkMan_name");
		var linkMan_main_phones = document.getElementsByName("linkMan_main_phone");
		var linkMan_other_phones = document.getElementsByName("linkMan_other_phone");
		var linkMan_contract_reasons = document.getElementsByName("linkMan_contract_reason");
		if(linkMan_names){
			if(linkMan_names.length>0){
				for(var i=0; i<linkMan_names.length;i++){
					if(linkMan_names[i].value.replace(/(^\s*)|(\s*$)/g, "")!="" && linkMan_main_phones[i].value.replace(/(^\s*)|(\s*$)/g, "")==""){
						MyAlert("请填写主要联系电话!");
						check_Link_flag = false;
						return check_Link_flag;
					}
					if(linkMan_names[i].value.replace(/(^\s*)|(\s*$)/g, "")=="" && linkMan_main_phones[i].value.replace(/(^\s*)|(\s*$)/g, "")!=""){
						MyAlert("请填写其他联系人姓名!");
						check_Link_flag = false;
						return check_Link_flag;
					}
					if(linkMan_main_phones[i].value){
						var str = /^[0-9]*$/;
						if(!str.test(linkMan_main_phones[i].value.replace(/(^\s*)|(\s*$)/g, ""))){
							MyAlert("其他联系人信息:\"主要联系电话\"请输入数字!");
							check_Link_flag = false;
							return check_Link_flag;
						}
					}
					if(linkMan_other_phones[i].value){
						var str = /^[0-9]*$/;
						if(!str.test(linkMan_other_phones[i].value.replace(/(^\s*)|(\s*$)/g, ""))){
							MyAlert("其他联系人信息:\"其他联系电话\"请输入数字!");
							check_Link_flag = false;
							return check_Link_flag;
						}
					}
				}
			}
		}
		return check_Link_flag;
	}
	
	function _$(_id) {
		if(typeof _id === "string")
			return document.getElementById(_id);
		return _id;
	}

	function doReport(){
		if(submitForm('fm')){
			if(document.getElementById("customer_type").value == <%= Constant.CUSTOMER_TYPE_01%>) {
				var re=/^[\u4E00-\u9FA5A-Za-z0-9\s]+$/; //验证客户姓名必需是中文 YH.2011.3.24
				var ctm_name=document.getElementById('ctm_name').value;
				if(ctm_name){
					if(re.test(ctm_name)){
						} else{
							MyAlert("对不起，你输入的客户姓名不合法！不能输入中文、字母及数字以外的字符！");
							return false;
						}
				}
			}
			
			if(_$("salesCon").value == "") {
				MyAlert("请选择销售顾问!") ;
				return false ;
			}
			
			if(_$("sale_type").value == "") {
				MyAlert("请选择销售类型!");
				return false;
			}
			
			var invoice_date = document.getElementById("invoice_date").value;//用户选择的开票时间

			var is_fleet = document.getElementsByName("is_fleet");
			var fleetValur = 0;
			var yes = '<%=yes%>';
			if(is_fleet){
				for(var i=0; i< is_fleet.length; i++){
					if(is_fleet[i].checked){
						fleetValur = is_fleet[i].value;
					}
				}
			}
			//如果是“大客户”，校验大客户及大客户合同是否选择
			if(fleetValur == yes){
				var fleet_id = document.getElementById("fleet_id").value;
				var fleet_contract_no_id = document.getElementById("fleet_contract_no_id").value;
				var type = document.getElementById("type").value;
				if(fleet_id == 0){
					MyAlert("请选择大客户");
					return;
				}
				/*
				if(type=='FLEET' && fleet_contract_no_id == 0){
					MyAlert("请选择大客户合同");
					return;
				}
				*/
				
				//MyConfirm("是否提交?",doReportAction);
			}
			var sel_cus_type = document.getElementsByName("sel_cus_type");
			var oldCustomerId = document.getElementById("oldCustomerId").value;
			var oldflag = '<%=oldflag %>';
			var selvalue = 0;
			for(var i=0;i<sel_cus_type.length;i++){
				if(sel_cus_type[i].checked){
					selvalue = sel_cus_type[i].value;
				}
			}
			var ccar = document.fm.carCharactor.options[document.fm.carCharactor.selectedIndex].value;

			if(ccar==null||ccar=="")
			{
				MyAlert("请选择车辆性质！");
				return;
			}
			var txt1 = document.getElementById("txt1").value;
			var txt2 = document.getElementById("txt2").value;
			var txt3 = document.getElementById("txt3").value;
			
			var payment = document.getElementById("payment").value;
			var customer_type = document.getElementById("customer_type").value;
			var ctm_form = document.getElementById("ctm_form").value;
			
			if(document.getElementById("MORTGAGE_TYPE").style.display=='inline'){
				
				var First_Price = document.getElementById("FirstPrice").value;
				var mortgageType = document.getElementById("mortgageType").value;

				var LoansType = document.getElementById("LoansType").value;

				var LoansYear = document.getElementById("LoansYear").value;

				if(LoansType=="" ||LoansType==null){
					MyAlert("请选择贷款方式！");
					return;
					}
				
				if(mortgageType=="" ||mortgageType==null){
					MyAlert("请选择按揭类型！");
					return;
					}
		              
		              	//1、判断不为空
			if(LoansYear==""){     
		                  
				MyAlert("贷款年限不能为空！");
				return;
		                //2、判断输入的是不是数字
			}else {
		                         if(/^(\+|-)?\d+($|\.\d+$)/.test(LoansYear)) {
		                                    //进入这里，表明是一个数字
		                    //转换为数字格式
		                    var value=parseInt(LoansYear);
		                    if(value>=1&&value<=100){
		                                           //表明输入的数字是0-100之间的
		                       
		                                   }else{
		                                           //表明输入的数字不在0-100之间
		                                       
		                       				MyAlert("贷款年限必须为0-100的数字！");
		                       				return;
		                                   }      
		                         }  else{
		                                   //表明输入的不是数字
		                             
		             				MyAlert("贷款年限必须为0-100数字！");
		             				return;
		                        } 
			                        }     

           	//1、判断不为空
			if(First_Price==""){     
		                  
				MyAlert("首付比例不能为空！");
				return;
		                //2、判断输入的是不是数字
			}else {
		                         if(/^(\+|-)?\d+($|\.\d+$)/.test(First_Price)) {
		                                    //进入这里，表明是一个数字
		                    //转换为数字格式
		                    var value=parseInt(First_Price);
		                    if(value>=1&&value<=100){
		                                           //表明输入的数字是0-100之间的
		                       
		                                   }else{
		                                           //表明输入的数字不在0-100之间
		                                       
		                       				MyAlert("必须为0-100的数字！");
		                       				return;
		                                   }      
		                         }  else{
		                                   //表明输入的不是数字
		                             
		             				MyAlert("必须为0-100数字！");
		             				return;
		                        } 
			                        }     
				}
			
			
					

			
			if(payment==null||payment=="")
			{
				MyAlert("请选择付款方式！");
				return;
			}
			if(customer_type==null||customer_type=="")
			{
				MyAlert("请选择客户类型！");
				return;
			}
			if(ctm_form==null||ctm_form=="")
			{
				MyAlert("请选择客户来源！");
				return;
			}
			var customer_type=document.getElementById("customer_type").value;
			var myctm_type = <%=Constant.CUSTOMER_TYPE_01%>
			
			if(txt1==null||txt1=="") {
				MyAlert("请选择省份！");

				return;
			}

			if(txt2==null||txt2=="") {
				MyAlert("请选择地级市！");
					
				return;
			}
				
			if(txt3==null||txt3=="") {
				MyAlert("请选择区、县！");
					
				return;
			}
			
			//老客户
			if(selvalue == oldflag){
				if(!oldCustomerId){
					MyAlert("请选择老客户");
					return;
				}
			}
			
			var addContent = document.getElementById("address").value ;
			if(addContent.indexOf("\"") == 0 || addContent.indexOf("“") == 0) {
				MyAlert("详细地址的首字符不能为双引号(\")!") ;
				return false ;
			}
			
		     /*var isOwnSale = document.getElementById('isOwnSale').value;
		     if(!isOwnSale) {
		    	 MyAlert("请选择是否本店销售");
				 return;
		     }*/
		     //验证开票日期要早于当前日期
				var sdate =parseInt(document.getElementById("ndate").value);
				var invoiceDate = document.getElementById("invoice_date").value;
				var attr = invoiceDate.split("-");
				var temp ="";
				for(var i= 0;i<attr.length;i++){
					temp += attr[i];
				}
				var idate = parseInt(temp);
				if(sdate < idate){
					MyAlert("开票日期不能超过当前日期");
					return false;
				}
			var card_type = document.getElementById("card_type").value ;
			if(card_type==null || card_type=="") {
				MyAlert("证件类型不能为空");
				return false;
			}
			if(check_Link()){
				MyConfirm("确认修改?",doReportAction);
			}
		}
	}
	
	function doReportAction(){
		document.getElementById("re_Id").disabled = true;
		makeNomalFormCall('<%=contextPath%>/customerRelationships/clientManage/ClientInforManage/saveUpdateSalesInfo.json',showReportRes,'fm');
	}
	
	function showReportRes(json){
		if(json.message == "操作成功"){
			parent.MyAlert("操作成功!");
			goback();
		} else{
			MyAlert(json.message);
		}
	}
	
	//客户列表
	function showCustomerList(){
		OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/toQueryCtmList.do',800,600);
	}
	//显示大客户信息
	function showFleetInfo(fleet_id,fleet_name,type){
		document.getElementById("fleet_name").value = fleet_name;
		document.getElementById("fleet_id").value = fleet_id;
		document.getElementById("type").value = type;
		document.getElementById("fleet_contract_no").value ="";
		document.getElementById("fleet_contract_no_id").value = "";
		var contractSpan = document.getElementById("contractSpan");
		if(type == 'FLEET'){
			contractSpan.innerHTML = "<select name='fleetContract' class='short_sel' onchange='getContractNo();'></select>";
			getFleetContractList();//获得合同列表
		}
		else{
			contractSpan.innerHTML = fleet_name;
		}
	}
	//显示大客户合同信息
	function showFleetContractInfo(contract_id,contract_no){
		document.getElementById("fleet_contract_no").value = contract_no;
		document.getElementById("fleet_contract_no_id").value = contract_id;
	}

	//显示客户信息
	function showCustomerInfo(ctmId_Old){
		var url = "<%=request.getContextPath()%>/sales/customerInfoManage/SalesReport/getOldCustomerInfo.json";
		makeCall(url,doShowCustomerInfo,{ctmId_Old:ctmId_Old}); 
	}
	//回显老客户信息
	function doShowCustomerInfo(json){
		document.getElementById("ctm_name").value=objToStr(json.oldCustomerInfo.ctmName);
		document.getElementById("company_phone").value=objToStr(json.oldCustomerInfo.companyPhone);
		document.getElementById("company_name").value=objToStr(json.oldCustomerInfo.companyName);
		document.getElementById("ctm_form").value=objToStr(json.oldCustomerInfo.ctmForm);
		document.getElementById("customer_type").value=objToStr(json.oldCustomerInfo.ctmType);
		
		//如果客户类型为10831001，公司客户信息table隐藏
		if(document.getElementById("customer_type").value+"" != '<%=s_ctmType%>'+""){
			document.getElementById("company_table").style.display = "none";
		}else{
			document.getElementById("company_table").style.display = "inline";
		}
		
		var isSecond = objToStr(json.oldCustomerInfo.isSecond);
		if(isSecond) {
			if(isSecond = <%=Constant.IF_TYPE_YES %>) {
				document.getElementById("sYes").checked = true ;
			} else if(isSecond = <%=Constant.IF_TYPE_NO %>) {
				document.getElementById("sNo").checked = true ;
			}
		}
		document.getElementById("address").value=objToStr(json.oldCustomerInfo.address);
		document.getElementById("job").value=objToStr(json.oldCustomerInfo.job);
		document.getElementById("sex").value=objToStr(json.oldCustomerInfo.sex);
		document.getElementById("is_married").value=objToStr(json.oldCustomerInfo.isMarried);
		document.getElementById("profession").value=objToStr(json.oldCustomerInfo.profession);
		
		document.getElementById("email").value=objToStr(json.oldCustomerInfo.email);
		
		document.getElementById("kind").value=objToStr(json.oldCustomerInfo.kind);
		if(!(json.oldCustomerInfo.birthday == null || json.oldCustomerInfo.birthday.length < 10) ){
			document.getElementById("birthday").value=objToStr(json.oldCustomerInfo.birthday.substring(0,10));
		}	
		document.getElementById("company_s_name").value=objToStr(json.oldCustomerInfo.companySName);
		document.getElementById("card_type").value=objToStr(json.oldCustomerInfo.cardType);
		
		document.getElementById("other_phone").value=objToStr(json.oldCustomerInfo.otherPhone);
		document.getElementById("custStars").value = objToStr(json.oldCustomerInfo.guestStars);

		var town_=json.oldCustomerInfo.town;
		document.getElementById("main_phone").value=objToStr(json.oldCustomerInfo.mainPhone);
		document.getElementById("vehicle_num").value=objToStr(json.oldCustomerInfo.vehicleNum);
		document.getElementById("industry").value=objToStr(json.oldCustomerInfo.industry);
		document.getElementById("income").value=objToStr(json.oldCustomerInfo.income);
		var province_=json.oldCustomerInfo.province;
		document.getElementById("post_code").value=objToStr(json.oldCustomerInfo.postCode);
		document.getElementById("card_num").value=objToStr(json.oldCustomerInfo.cardNum);
		document.getElementById("level_id").value=objToStr(json.oldCustomerInfo.levelId);
		document.getElementById("education").value=objToStr(json.oldCustomerInfo.education);
		document.getElementById("oldCustomerId").value=objToStr(json.oldCustomerInfo.ctmId);
		var city_=json.oldCustomerInfo.city;
		genLocSel('txt1','txt2','txt3',province_,city_,town_); // 加载省份城市和县
		showCustSel(<%=Constant.IS_OLD_CTM_02 %>,2);//可以修改
		queryOtherLinkMan(json.oldCustomerInfo.ctmId);
		
	}
	function queryOtherLinkMan1(value){
		var sum = document.getElementById("iframe");
		sum.src = "<%=contextPath%>/sales/customerInfoManage/SalesReport/queryOtherLinkMan.do?oldCustomerId="+value;
	}
	function queryOtherLinkMan(value){
		makeCall('<%=contextPath%>/sales/customerInfoManage/SalesReport/queryOtherLinkMan.json',showOtherLinkMan,{oldCustomerId:value}); 
	//	makeNomalFormCall('<%=contextPath%>/sales/customerInfoManage/SalesReport/queryOtherLinkMan.json?oldCustomerId='+value,showOtherLinkMan,'fm');
	}
	function showOtherLinkMan(json){
		var tab = document.getElementById("file");
		var rowNum = tab.rows.length;
		if(rowNum > 2){
			for(var i=2;i<rowNum;i++){
				tab.deleteRow(2);
			}
		}
				
		if(json.linkmanList){
			for(var i=0; i<json.linkmanList.length;i++){
				var rowObj = tab.insertRow(tab.rows.length);
				rowObj.className  = "table_list_row2";
				var cell1 = rowObj.insertCell(0);
				var cell2 = rowObj.insertCell(1);
				var cell3 = rowObj.insertCell(2);
				var cell4 = rowObj.insertCell(3);
				var cell5 = rowObj.insertCell(4);
				cell1.innerHTML = '<TD align="center"><div align="center"><input type="text" maxlength="20" name="linkMan_name" value="'+json.linkmanList[i].NAME+'" style="width: 80%" /><font color="#FF0000">*</font></div></TD>';
				cell2.innerHTML = '<TD align="center"><div align="center"><input type="text" maxlength="15" name="linkMan_main_phone" value="'+json.linkmanList[i].MAIN_PHONE+'" style="width: 70%" /><font color="#FF0000">*</font></div></TD>';
				cell3.innerHTML = '<TD align="center"><div align="center"><input type="text" maxlength="15" name="linkMan_other_phone"  value="'+objToStr(json.linkmanList[i].OTHER_PHONE)+'" style="width: 80%" /></div></TD>';
				cell4.innerHTML = '<TD align="center"><div align="center"><input type="text" maxlength="200" name="linkMan_contract_reason"  value="'+objToStr(json.linkmanList[i].CONTRACT_REASON)+'" style="width: 80%" /></div></TD>';
				cell5.innerHTML = '<TD align="center"><div align="center"><input class="normal_btn" type="button" value="删除" width="80%" onclick="deleteOtherLink('+objToStr(json.linkmanList[i].LM_ID)+');" /></div></TD></TR>';
			}
		}
	}

	function deleteOtherLink(lm_id){
		MyConfirm("是否删除?",deleteOtherLinkAction,[lm_id]);
	}

	function deleteOtherLinkAction(lm_id){
		makeCall('<%=contextPath%>/sales/customerInfoManage/SalesReport/delOtherLinkMan.json',showResult1,{lm_id:lm_id}); 
		//makeNomalFormCall('<%=contextPath%>/sales/customerInfoManage/SalesReport/delOtherLinkMan.json?lm_id='+lm_id,showResult1,'fm');
	}

	function showResult1(json){
		var oldCustomerId = document.getElementById("oldCustomerId").value;
		queryOtherLinkMan(oldCustomerId);
	}
	
	function goback(){
		history.go(-1);
	}
	
	function objToStr(itemValue){
		itemValue = itemValue==null?"":itemValue;
		itemValue = itemValue=='null'?"":itemValue;
		return itemValue;
	}
	function addTblRow() {
		var tbl = document.getElementById('file');
		var rowObj = tbl.insertRow(tbl.rows.length);
		rowObj.className  = "table_list_row2";
		var cell1 = rowObj.insertCell(0);
		var cell2 = rowObj.insertCell(1);
		var cell3 = rowObj.insertCell(2);
		var cell4 = rowObj.insertCell(3);
		var cell5 = rowObj.insertCell(4);
		
		cell1.innerHTML = '<TD align="center"><div align="center"><input type="text" maxlength="20" name="linkMan_name" style="width: 80%" /><font color="#FF0000">*</font></div></TD>';
		cell2.innerHTML = '<TD align="center"><div align="center"><input type="text" maxlength="15" name="linkMan_main_phone" style="width: 70%" /><font color="#FF0000">*</font></div></TD>';
		cell3.innerHTML = '<TD align="center"><div align="center"><input type="text" maxlength="15" name="linkMan_other_phone" style="width: 80%" /></div></TD>';
		cell4.innerHTML = '<TD align="center"><div align="center"><input type="text" maxlength="200" name="linkMan_contract_reason" style="width: 80%" /></div></TD>';
		cell5.innerHTML = '<TD align="center"><div align="center"><input class="normal_btn" type="button" value="删除" width="80%" onclick="deleteTblRow(this);" /></div></TD></TR>';
	}
	
	function deleteTblRow(obj) {
		var idx = obj.parentElement.parentElement.parentElement.rowIndex;
		var tbl = document.getElementById('file');
		tbl.deleteRow(idx);		
	}

	//读下端接口数据
	function getServiceName(){
		return "ClaimOrder";
	}
	
	function getSystemName(){
		return "ChangAn";
	}
	function setJson(str){
		//var json = str.evalJSON();
		var json = {REPORT_JSON:
						[{
							vehicle_no:'吉JSON888',contract_no:'C-JSON-2010',invoice_date:'2010-04-09',invoice_no:'2010JSON',
							insurance_company:'太平洋保险',insurance_date:'2010-01-03',consignation_date:'2010-03-03',miles:'0.5',payment:10361002,
							price:'666666',memo:'备注JOSN',customer_type:'10831001',is_fleet:'10041002',company_name:'公司名称JSON',company_s_name:'公司简称JSON',
							company_phone:'13500009999',level_id:'10901005',kind:'10921007',vehicle_num:199,ctm_name:'JSON张',
							sex:'10031002',card_type:'10931003',card_num:'2222222222',main_phone:'13544445555',other_phone:'043188885555',
							email:'JOSN@163.COM',post_code:'130012',birthday:'1990-01-03',ctm_form:10941003,income:10951004,
							education:10961005,industry:10971005,is_married:10981002,profession:10991002,job:'秘书',
							province:220000,city:2201,district:220100,address:'详细地址：JSON'
						}],
					OTHER_LINKMAN_JSON:
						[
							{linkMan_names:'联系人1',linkMan_main_phones:13578788787,linkMan_other_phone:13688889999,linkMan_contract_reasons:'联系原因1'},
							{linkMan_names:'联系人2',linkMan_main_phones:13512345678,linkMan_other_phone:13612349999}
						]
					};

		if(Object.keys(json).length>0){
			document.getElementById("isJson").value=1;
		}
	
		document.getElementById("vehicle_no").value = objToStr(json.REPORT_JSON[0].vehicle_no);					//车牌号
		document.getElementById("contract_no").value = objToStr(json.REPORT_JSON[0].contract_no);				//合同编号
		document.getElementById("invoice_date").value = objToStr(json.REPORT_JSON[0].invoice_date);				//开票日期
		document.getElementById("invoice_no").value = objToStr(json.REPORT_JSON[0].invoice_no);					//发票编号
		document.getElementById("insurance_company").value = objToStr(json.REPORT_JSON[0].insurance_company);	//保险公司
		document.getElementById("insurance_date").value = objToStr(json.REPORT_JSON[0].insurance_date);         //保险日期
		document.getElementById("consignation_date").value = objToStr(json.REPORT_JSON[0].consignation_date);	//车辆交付日期
		document.getElementById("miles").value = objToStr(json.REPORT_JSON[0].miles);							//交付时公里数
		document.getElementsByName("payment").value = objToStr(json.REPORT_JSON[0].payment);					//付款方式
		document.getElementById("price").value = objToStr(json.REPORT_JSON[0].price);							//价格
		document.getElementById("memo").value = objToStr(json.REPORT_JSON[0].memo);								//备注
		document.getElementById("customer_type").value = objToStr(json.REPORT_JSON[0].customer_type);			//客户类型
		document.getElementById("is_fleet").value = objToStr(json.REPORT_JSON[0].is_fleet);						//是否大客户
		document.getElementById("company_name").value = objToStr(json.REPORT_JSON[0].company_name);				//公司名称
		document.getElementById("company_s_name").value = objToStr(json.REPORT_JSON[0].company_s_name);			//公司简称
		document.getElementById("company_phone").value = objToStr(json.REPORT_JSON[0].company_phone);			//公司电话
		document.getElementById("level_id").value = objToStr(json.REPORT_JSON[0].level_id);						//公司规模
		document.getElementById("kind").value = objToStr(json.REPORT_JSON[0].kind);								//公司性质
		document.getElementById("vehicle_num").value = objToStr(json.REPORT_JSON[0].vehicle_num);				//目前车辆数
		document.getElementById("ctm_name").value = objToStr(json.REPORT_JSON[0].ctm_name);						//客户姓名
		document.getElementById("sex").value = objToStr(json.REPORT_JSON[0].sex);								//性别
		document.getElementById("card_type").value = objToStr(json.REPORT_JSON[0].card_type);					//证件类别
		document.getElementById("card_num").value = objToStr(json.REPORT_JSON[0].card_num);						//证件号码
		document.getElementById("main_phone").value = objToStr(json.REPORT_JSON[0].main_phone);					//主要联系电话
		document.getElementById("other_phone").value = objToStr(json.REPORT_JSON[0].other_phone);				//其他联系电话
		document.getElementById("email").value = objToStr(json.REPORT_JSON[0].email);							//电子邮件
		document.getElementById("post_code").value = objToStr(json.REPORT_JSON[0].post_code);					//邮编
		document.getElementById("birthday").value = objToStr(json.REPORT_JSON[0].birthday);						//出生年月
		document.getElementById("ctm_form").value = objToStr(json.REPORT_JSON[0].ctm_form);						//客户来源
		document.getElementById("income").value = objToStr(json.REPORT_JSON[0].income);							//家庭月收入
		document.getElementById("education").value = objToStr(json.REPORT_JSON[0].education);					//教育程度
		document.getElementById("industry").value = objToStr(json.REPORT_JSON[0].industry);						//所在行业
		document.getElementById("is_married").value = objToStr(json.REPORT_JSON[0].is_married);					//婚姻状况
		document.getElementById("profession").value = objToStr(json.REPORT_JSON[0].profession);					//职业
		document.getElementById("job").value = objToStr(json.REPORT_JSON[0].job);								//职务
		changeCustomerType(objToStr(json.REPORT_JSON[0].customer_type));
		is_fleet_Sel(objToStr(json.REPORT_JSON[0].is_fleet));
		var province__ = objToStr(json.REPORT_JSON[0].province);												//省份
		var city__ = objToStr(json.REPORT_JSON[0].city);														//地级市
		var town__ = objToStr(json.REPORT_JSON[0].district);													//区、县
		genLocSel('txt1','txt2','txt3',province__,city__,town__); 												//加载省份城市和县
		document.getElementById("address").value = objToStr(json.REPORT_JSON[0].address);						//详细地址

		
		if(json.OTHER_LINKMAN_JSON.length){
			var tab = document.getElementById("file");
			for(var i=0; i<json.OTHER_LINKMAN_JSON.length;i++){
				var rowObj = tab.insertRow(tab.rows.length);
				rowObj.className  = "table_list_row2";
				var cell1 = rowObj.insertCell(0);
				var cell2 = rowObj.insertCell(1);
				var cell3 = rowObj.insertCell(2);
				var cell4 = rowObj.insertCell(3);
				var cell5 = rowObj.insertCell(4);
				
				cell1.innerHTML = '<TD align="center"><div align="center"><input type="text" maxlength="20" name="linkMan_name" value="'+objToStr(json.OTHER_LINKMAN_JSON[i].linkMan_names)+'" style="width: 80%" /><font color="#FF0000">*</font></div></TD>';
				cell2.innerHTML = '<TD align="center"><div align="center"><input type="text" maxlength="15" name="linkMan_main_phone" value="'+objToStr(json.OTHER_LINKMAN_JSON[i].linkMan_main_phones)+'" style="width: 70%" /><font color="#FF0000">*</font></div></TD>';
				cell3.innerHTML = '<TD align="center"><div align="center"><input type="text" maxlength="15" name="linkMan_other_phone"  value="'+objToStr(json.OTHER_LINKMAN_JSON[i].linkMan_other_phone)+'" style="width: 80%" /></div></TD>';
				cell4.innerHTML = '<TD align="center"><div align="center"><input type="text" maxlength="200" name="linkMan_contract_reason"  value="'+objToStr(json.OTHER_LINKMAN_JSON[i].linkMan_contract_reasons)+'" style="width: 80%" /></div></TD>';
				cell5.innerHTML = '<TD align="center"><div align="center"><input class="normal_btn" type="button" value="删除" width="80%" onclick="deleteTblRow(this);" /></div></TD></TR>';
			}	
		}
	}
	
	function getFleetContractList(){
		var fleet_id = document.getElementById("fleet_id").value;
		var url = "<%=request.getContextPath()%>/sales/customerInfoManage/SalesReport/getContractList.json";
		makeCall(url,showFleetContractList,{fleet_id:fleet_id}); 
	}
	
	function showFleetContractList(json){
		var obj = document.getElementById("fleetContract");
		obj.options.length = 0;
		for(var i=0;i<json.contractList.length;i++){
			obj.options[i]=new Option(json.contractList[i].CONTRACT_NO, json.contractList[i].CONTRACT_ID + "|" + json.contractList[i].CONTRACT_NO);
		}
		getContractNo();
	}
	
	function getContractNo(){
		var contract = document.getElementById("fleetContract").value;
		if(contract != ""){
			var contractId = contract.split("|")[0];
			var contractNo = contract.split("|")[1];
			document.getElementById("fleet_contract_no_id").value = contractId;
			document.getElementById("fleet_contract_no").value = contractNo;
		}
	}

function changeMortgageType(value){
		
		var mg_type = <%=Constant.PAYMENT_03%>;//按揭
		if(value == mg_type){
			document.getElementById("MORTGAGE_TYPE").style.display = "inline";
			document.getElementById("Loans").style.display = "inline";
		
		}else{
			document.getElementById("MORTGAGE_TYPE").style.display = "none";
			document.getElementById("Loans").style.display = "none";
			document.getElementById("mortgageType").value="";
			document.getElementById("Loans").value="";
		}

		
	}	
	
	function initData(){
		var val = "${salesInfo.payment }";
		var mg_type = <%=Constant.PAYMENT_03%>;//按揭
		if(val == mg_type){
			document.getElementById("MORTGAGE_TYPE").style.display = "inline";
			document.getElementById("Loans").style.display = "inline";
			
			//设置值
			$("mortgageType").value = "${salesInfo.mortgageType }";
			var shoufuRatio = "${salesInfo.shoufuRatio }";
			var firstPrice = parseFloat(shoufuRatio)*100;
			$("FirstPrice").value = firstPrice;
			$("LoansType").value = "${salesInfo.loansType }";
			$("LoansYear").value = "${salesInfo.loansYear }";
			
		}
		
		//客户类型
		$("customer_type").value = "${customerInfo.ctmType}";
		changeCustomerType($("customer_type").value);
		//在选择完客户类型后操作车辆性质的值
		$("carCharactor").value = "${salesInfo.carCharactor}";
		
		//团购车、大客户
		var isFleet = "${salesInfo.isFleet}";
		var fleet = "<%=Constant.IF_TYPE_YES %>";
		if(isFleet == fleet){
			//为团购车设置大客户
			$("is_fleet_yes").checked = true;
			$("is_fleet_no").checked = false;
			var id = "${fleetId}";
			var name = "${fleetName}";
			var type = "${type}";
			showFleetInfo(id,name,type);
			if(type == "FLEET"){
				$("fleetContract").value = "${contractNo}";
			}
		} else {
			$("is_fleet_yes").checked = false;
			$("is_fleet_no").checked = true;
		}
		
		if($("customer_type").value == "<%=Constant.CUSTOMER_TYPE_01 %>"){
			//个人客户
			var ctmId = "${customerInfo.ctmId }";
			if(ctmId != ""){
				showCustomerInfo(ctmId);
			}
		}else{
			//公司客户
			$("company_name").value = "${customerInfo.companyName }";
			$("company_s_name").value = "${customerInfo.companySName }";
			$("company_phone").value = "${customerInfo.companyPhone }";
			$("level_id").value = "${customerInfo.levelId }";
			
			$("kind").value = "${customerInfo.kind }";
			$("vehicle_num").value = "${customerInfo.vehicleNum }";
			$("myctm_form").value = "${customerInfo.ctmForm }";
			
			var ctmId = "${customerInfo.ctmId }";
			if(ctmId != ""){
				showCustomerInfo(ctmId);
			}
		}
	}
</script>
</body>
</html>