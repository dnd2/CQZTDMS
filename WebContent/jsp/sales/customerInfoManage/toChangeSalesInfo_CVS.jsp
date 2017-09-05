<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="com.infodms.dms.po.TtDealerActualSalesPO"%>
<%@ page import="com.infodms.dms.po.TtCustomerPO"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
	Map vehicleInfo = (Map)request.getAttribute("vehicleInfo");
	TtDealerActualSalesPO salesInfo = (TtDealerActualSalesPO)request.getAttribute("salesInfo");	//车辆实销信
	int isFleet = salesInfo.getIsFleet();	 													//是否是集团客户
	TtCustomerPO customerInfo = (TtCustomerPO)request.getAttribute("customerInfo");				//客户信息
	Long CtmId = new Long(0);
	if(null != customerInfo){
		CtmId = customerInfo.getCtmId();
	}
	String vehicleId = vehicleInfo.get("VEHICLE_ID").toString();
	int s_ctmType = Constant.CUSTOMER_TYPE_02;									//客户类型：公司客户
	int yes = Constant.IF_TYPE_YES;			  									//"是"
	//集团客户相关信息
	String fleetId = (String)request.getAttribute("fleetId");					//集团客户id
	String fleetContractId = (String)request.getAttribute("fleetContractId");	//集团客户合同id
	String fleetName = (String)request.getAttribute("fleetName");				//集团客户名称
	String contractNo = (String)request.getAttribute("contractNo");				//集团客户合同号 
	
	
	Long province__ = new Long(0);			  //省
	Long city__ = new Long(0);	  			  //市
	Long town__ = new Long(0);	  			  //县
	int ctmType = 0;			  			  //客户类型
	if(null != customerInfo && !"".equals(customerInfo)){
		province__ = customerInfo.getProvince();
		city__ = customerInfo.getCity();
		town__ = customerInfo.getTown();
		ctmType = customerInfo.getCtmType();
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		loadcalendar();   //初始化时间控件
		genLocSel('txt1','txt2','txt3','<%=province__%>','<%=city__%>','<%=town__%>'); // 加载省份城市和县
		genLocSel('txt4','txt5','txt6','<%=province__%>','<%=city__%>','<%=town__%>'); // 加载省份城市和县
		//queryOtherLinkMan();
		var ctmType = '<%=ctmType%>';
		var s_ctmType = '<%=s_ctmType%>';
		if(ctmType == s_ctmType){
			document.getElementById("company_table").style.display = "inline";
			document.getElementById("customerInfoId").style.display = "none";
			document.getElementById("file").style.display = "none";
		}
	}

</script>

<title>实销信息上报</title>
</head>
<body onunload='javascript:destoryPrototype();'>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 整车销售 &gt; 客户信息管理 &gt; 实销信息更改申请</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="knowaddress" id="knowaddress" value="<%=Constant.KNOW_ADDRESS%>"></input>
<input type="hidden" name="pName" id="pName" value="${pName}"></input>
<input type="hidden" name="curPage" id="curPage" value="1" /> 
<input type="hidden" id="dlrId" name="dlrId" value="" />
<table class="table_edit">
    <tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />修改说明</th>
	</tr>
  </table>
  <table class="table_edit" width="90%" align="center" id="person2">
    <tr>
      <td width="15%" align="right">修改原因：<br /></td>
      <td width="85%" align="left"><span class="datadetail">
        <textarea id="edit_remark" name="edit_remark" cols="40" rows="3" datatype="0,is_textarea,300" ></textarea>
      </span></td>
    </tr>
  </table>

<table class="table_edit" align=center id="tab1">
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />车辆资料</th>
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
		<td align=left>${vehicleInfo.MODEL_NAME }</td>
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
	<tr>
		<td colspan="4">
	<table class="table_edit" align=center id="aaa">
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />销售信息</th>
	</tr>
	<tr>
		<td align="right">车牌号：</td>
		<td width="35%" align="left"><input id="vehicle_no"
			name="vehicle_no" type="text" class="middle_txt"
			value="${salesInfo.vehicleNo }" datatype="1,is_carno,20"
			maxlength="20" /></td>
		<td width="15%" align="right">合同编号：</td>
		<td width="35%" align="left"><input id="contract_no"
			name="contract_no" type="text" class="middle_txt"
			value="${salesInfo.contractNo }" datatype="1,is_textarea,25"
			maxlength="25" /></td>
	</tr>
	<tr>
		<td align="right">开票日期：</td>
		<td align="left"><input name="invoice_date" id="invoice_date" type="text"
			class="short_txt"
			value="<%=salesInfo==null?"":(salesInfo.getInvoiceDate()==null?"":CommonUtils.printDate(salesInfo.getInvoiceDate()))%>"
			datatype="0,is_date,10" hasbtn="true"
			callFunction="showcalendar(event, 't1', false);" /></td>
		<td align="right">发票编号：</td>
		<td align="left"><input id="invoice_no" name="invoice_no"
			type="text" class="middle_txt" value="${salesInfo.invoiceNo }"
			datatype="0,is_textarea,25" maxlength="25" /></td>
	</tr>
	<tr>
		<td align="right">保险公司：</td>
		<td align="left"><input id="insurance_company"
			name="insurance_company" type="text" class="middle_txt"
			value="${salesInfo.insuranceCompany }" datatype="1,is_textarea,50"
			maxlength="50" /></td>
		<td align="right">保险日期：</td>
		<td align="left"><input name="insurance_date" id="t2"
			type="text" class="short_txt"
			value="<%=salesInfo==null?"":(salesInfo.getInsuranceDate()==null?"":CommonUtils.printDate(salesInfo.getInsuranceDate())) %>"
			datatype="1,is_date,10" hasbtn="true"
			callFunction="showcalendar(event, 't2', false);" /></td>
	</tr>
	<tr>
		<td align="right">车辆交付日期：</td>
		<td align="left"><input name="consignation_date" id="t3"
			type="text" class="short_txt"
			value="<%=salesInfo==null?"":(salesInfo.getConsignationDate()==null?"":CommonUtils.printDate(salesInfo.getConsignationDate())) %>"
			datatype="0,is_date,10" hasbtn="true"
			callFunction="showcalendar(event, 't3', false);" /></td>
		<td align="right">交付时公里数：</td>
		<td align="left"><input id="miles" name="miles" type="text"
			class="middle_txt" value="${salesInfo.miles }"
			datatype="1,is_double,6" maxlength="6" /></td>
	</tr>
	<tr>
		<td align="right">付款方式：</td>
		<td align="left"><label> <script type="text/javascript">
					genSelBoxExp("payment",<%=Constant.PAYMENT%>,"<%=salesInfo==null?"":(salesInfo.getPayment()==null?"":salesInfo.getPayment())%>",false,"short_sel","onchange='changeMortgageType(this.value)'","false",'');
				</script> </label></td>
		<td align="right">价格：</td>
		<td align="left"><input id="price" name="price" type="text"
			class="middle_txt" value="${salesInfo.price }"
			datatype="0,isMoney,10" maxlength="10" /></td>
	</tr>
	<% if(salesInfo.getPayment().toString().equals(Constant.PAYMENT_03.toString())){%>
	<tr id="MORTGAGE_TYPE"  style="display: inline;">
	<%}else {%>
		<tr id="MORTGAGE_TYPE"  style="display: none;">
	<% }%>
		<td align="right">按揭类型：</td>
		<td align="left"><label> 
			<script type="text/javascript">
					genSelBoxExp("mortgageType",<%=Constant.MORTGAGE_TYPE%>,'<%=salesInfo.getMortgageType() %>',true,"short_sel",'',"false",'');
			</script><font color="red">*</font></label>
		</td>
		<td align="right">首付比例：</td>
		<td align="left"><input id="FirstPrice" name="FirstPrice" type="text" class="middle_txt" value="<%=salesInfo.getShoufuRatio()*100 %>"  />%<font color="red">*</font></td>
		
	</tr>
	<% if(salesInfo.getPayment().toString().equals(Constant.PAYMENT_03.toString())){%>
	<tr id="Loans"  style="display: inline;">
	<%}else {%>
		<tr id="Loans"  style="display: none;">
	<% }%>

	
		<td align="right">贷款方式：</td>
		<td align="left"><label> 
			<script type="text/javascript">
					genSelBoxExp("LoansType",<%=Constant.Loans%>,'<%=salesInfo.getLoansType() %>',true,"short_sel",'',"false",'');
			</script><font color="red">*</font></label>
		</td>
		<td align="right">贷款年限：</td>
		<td align="left"><input id="LoansYear" name="LoansYear" type="text" class="middle_txt" value="<%=salesInfo.getLoansYear() %>"  /><font color="red">*</font></td>
		
	</tr>
	
	<tr>
		<td align="right">备注：</td>
		<td colspan="2" align="left"><input type="text" id="memo"
			name="memo" value="${salesInfo.memo }" datatype="1,is_textarea,60"
			maxlength="60" size="50" /></td>
		<td align="left">&nbsp;</td>
	</tr>
	</table>
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
			<!--<script type="text/javascript">
				genSelBox("customer_type",<%=Constant.CUSTOMER_TYPE%>,"<%=customerInfo==null?"":(customerInfo.getCtmType()==null?"":customerInfo.getCtmType())%>",false,"short_sel","onchange='changeCustomerType(this.value)'");
			</script>
			-->
			<script>document.write(getItemValue(<%=customerInfo.getCtmType()%>));</script>
			<input type="hidden" name="customer_type" value="<%=customerInfo.getCtmType()%>" />
		</td>
		<td width="15%" align="right">是否集团客户：</td>
		<td width="35%" align="left">
			<c:if test="${salesInfo.isFleet==10041001}">
				<script>document.write(getItemValue(${salesInfo.isFleet}));</script>	
			</c:if>
			<c:if test="${salesInfo.isFleet==10041002}">
				<input type="radio" id="is_fleet_yes" name="is_fleet" onclick="is_fleet_Sel(<%=Constant.IF_TYPE_YES %>);" value="<%=Constant.IF_TYPE_YES %>" />是 
				<input type="radio" id="is_fleet_no" name="is_fleet" onclick="is_fleet_Sel(<%=Constant.IF_TYPE_NO %>);" value="<%=Constant.IF_TYPE_NO %>" checked="checked" />否
			</c:if>
		</td>
	</tr>
	<c:if test="${pName!=null}">
	<tr>
			<td align="right">集团客户名称：</td>
			<td align="left"><label>${pName}</label></td>
			<td align="right">集团客户合同：</td>
			<td align="left"><label>${pName}</label>
			</td>
		</tr>
	</c:if>
	<c:if test="${pName==null}">
	<tbody id="is_fleet_show" style="display: none;">
		<tr>
			<td align="right">集团客户名称：</td>
			<td align="left">
				<input id="fleet_name" name="fleet_name" type="text" value="" class="middle_txt" datatype="0,is_textarea,30" size="30" readonly="readonly" /> 
				<input id="fleet_id" name="fleet_id" value="${fleetId }" type="hidden" class="middle_txt" /> 
				<input id="type" name="type" value="" type="hidden"/> 
				<input type="button" value="..." class="mini_btn" onclick="toFleetList();" /></td>
			<td align="right">集团客户合同：</td>
			<td align="left">
				<!--<input id="fnoid" name="fleet_contract_no" type="text" class="middle_txt" value="" datatype="0,is_textarea,20" size="20" readonly="readonly" /> 
				 --><!-- 大客户合同id -->
				<!--<input id="fleet_contract_no_id" value="${salesInfo.contractId }" name="fleet_contract_no_id" type="hidden" />
				<input type="button" value="..." class="mini_btn" onclick="toFleetContractList();" />
				 --><!-- 大客户审核状态 --> 
				<input id="fleet_check_status" name="fleet_check_status" type="hidden" />
				<input id="fleet_contract_no_id" value="${salesInfo.contractId}" name="fleet_contract_no_id" type="hidden" />
				<input id="fleet_contract_no" name="fleet_contract_no" type="hidden" />
				<span id="contractSpan"></span>
			</td>
		</tr>
	</tbody>
	</c:if>
</table>
<table class="table_edit" align="center" id="company_table" style="display: none;">
	<tr class="tabletitle">
		<th colspan="4" align="right"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />公司客户信息</th>
	</tr>

	<tr>
		<td width="15%" align="right">公司名称：</td>
		<td width="35%" align="left"><input type="text" class="middle_txt" id="company_name" name="company_name" value="${customerInfo.companyName }" datatype="0,is_textarea,60" maxlength="60" /></td>
		<td width="15%" align="right">公司简称：</td>
		<td width="35%" align="left"><input id="company_s_name"
			name="company_s_name" value="${customerInfo.companySName }"
			type="text" class="middle_txt" datatype="1,is_textarea,60"
			maxlength="60" /></td>
	</tr>
	<tr>
		<td align="right">公司电话：</td>
		<td align="left"><input id="company_phone" name="company_phone" value="${customerInfo.companyPhone }" type="text" class="middle_txt" size="20"
			datatype="0,is_digit,11" maxlength="11" /></td>
		<td align="right">公司规模 ：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("level_id",<%=Constant.COMPANY_SCOPE%>,"<%=customerInfo==null?"":(customerInfo.getLevelId()==null?"":customerInfo.getLevelId())%>",false,"short_sel",'',"false",'');
				</script></td>
	</tr>
	<tr>
		<td align="right">公司性质：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("kind",<%=Constant.COMPANY_KIND%>,"<%=customerInfo==null?"":(customerInfo.getKind()==null?"":customerInfo.getKind())%>",false,"short_sel",'',"false",'');
			</script></td>
		<td align="right">目前车辆数：</td>
		<td align="left"><input id="vehicle_num" name="vehicle_num"
			value="${customerInfo.vehicleNum }" type="text" class="middle_txt"
			size="20" datatype="1,is_digit,6" maxlength="6" /></td>
	</tr>
	<tr>
		<td align="right">了解途径：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("myctm_form",<%=Constant.CUSTOMER_FROM%>,"<%=customerInfo==null?"":(customerInfo.getCtmForm()==null?"":customerInfo.getCtmForm())%>",false,"short_sel",'',"false",'<%=Constant.CUSTOMER_FROM_01%>,<%=Constant.CUSTOMER_FROM_03%>,<%=Constant.CUSTOMER_FROM_04%>,<%=Constant.CUSTOMER_FROM_12%>');
			</script></td>
	</tr>
	<tr>
		<td align="right">所在地：</td>
		<td colspan="3" align="left">省份： <select class="min_sel"
			id="txt1" name="province" onchange="_genCity(this,'txt2')"></select>
		地级市： <select class="min_sel" id="txt2" name="city"
			onchange="_genCity(this,'txt3')"></select> 区、县 <select
			class="min_sel" id="txt3" name="district"></select></td>
	</tr>
</table>
<div id="customerInfoId">
<table class="table_edit" align="center" id="ctm_table_id">
	<tr>
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />个人客户信息</th>
	</tr>
	<tr>
		<td width="15%" align="right" id="tcmtd">客户姓名：</td>
		<td width="35%" align="left">${customerInfo.ctmName }<input type="hidden" name="ctmName" value="${customerInfo.ctmName }" /></td>
		<td width="15%" align="right" id="sextd">性别：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("sex",<%=Constant.GENDER_TYPE%>,"<%=customerInfo==null?"":(customerInfo.getSex()==null?"":customerInfo.getSex())%>",false,"short_sel",'',"false",'');
			</script></td>
	</tr>
</table>
<table class="table_edit" align="center" id="ctm_table_id_2">
	<tr>
		<td width="15%" align="right">证件类别：</td>
		<td width="35%" align="left"><script type="text/javascript">
					genSelBoxExp("card_type",<%=Constant.CARD_TYPE%>,"<%=customerInfo==null?"":(customerInfo.getCardType()==null?"":customerInfo.getCardType())%>",false,"short_sel",'',"false",'');
			</script></td>
		<td width="15%" align="right">证件号码：</td>
		<td width="35%" align="left"><input id="card_num"
			name="card_num" value="${customerInfo.cardNum }" type="text"
			class="middle_txt" size="20" datatype="0,is_digit_letter,30"
			maxlength="30" /></td>
	</tr>
	<tr>
		<td align="right">主要联系电话：</td>
		<td align="left"><input id="main_phone" name="main_phone"
			value="${customerInfo.mainPhone }" type="text" class="middle_txt"
			size="20" datatype="0,is_digit,15" maxlength="15" /></td>
		<td align="right">其他联系电话：</td>
		<td align="left"><input id="other_phone"
			value="${customerInfo.otherPhone }" name="other_phone" type="text"
			class="middle_txt" size="20" datatype="1,is_digit,15" maxlength="15" /></td>
	</tr>
	<tr>
		<td align="right">电子邮件：</td>
		<td align="left"><input id="email" name="email"
			value="${customerInfo.email }" type="text" class="middle_txt"
			size="20" datatype="1,is_email,70" maxlength="70" /></td>
		<td align="right">邮编：</td>
		<td align="left"><input id="post_code" name="post_code"
			value="${customerInfo.postCode }" type="text" class="middle_txt"
			size="20" datatype="1,is_digit,10" maxlength="10" /></td>
	</tr>
	<tr>
		<td align="right">出生年月：</td>
		<td align="left"><input name="birthday" id="birthday"
			value="<%=customerInfo==null?"":(customerInfo.getBirthday()==null?"":CommonUtils.printDate(customerInfo.getBirthday())) %>"
			type="text" class="short_txt" datatype="0,is_date,10" hasbtn="true"
			callFunction="showcalendar(event, 'birthday', false);" /></td>
		<td align="right">了解途径：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("ctm_form",<%=Constant.CUSTOMER_FROM%>,"<%=customerInfo==null?"":(customerInfo.getCtmForm()==null?"":customerInfo.getCtmForm())%>",false,"short_sel",'',"false",'<%=Constant.CUSTOMER_FROM_01%>,<%=Constant.CUSTOMER_FROM_03%>,<%=Constant.CUSTOMER_FROM_04%>,<%=Constant.CUSTOMER_FROM_12%>');
			</script></td>
	</tr>
	<tr>
		<td align="right">家庭月收入：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("income",<%=Constant.EARNING_MONTH%>,"<%=customerInfo==null?"":(customerInfo.getIncome()==null?"":customerInfo.getIncome())%>",false,"short_sel",'',"false",'');
			</script></td>
		<td align="right">教育程度：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("education",<%=Constant.EDUCATION_TYPE%>,"<%=customerInfo==null?"":(customerInfo.getEducation()==null?"":customerInfo.getEducation())%>",false,"short_sel",'',"false",'');
			</script></td>
	</tr>
	<tr>
		<td align="right">所在行业：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("industry",<%=Constant.TRADE_TYPE%>,"<%=customerInfo==null?"":(customerInfo.getIndustry()==null?"":customerInfo.getIndustry())%>",false,"short_sel",'',"false",'');
			</script></td>
		<td align="right">婚姻状况：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("is_married",<%=Constant.MARRIAGE_TYPE%>,"<%=customerInfo==null?"":(customerInfo.getIsMarried()==null?"":customerInfo.getIsMarried())%>",false,"short_sel",'',"false",'');
			</script></td>
	</tr>
	<tr>
		<td align="right">职业：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("profession",<%=Constant.PROFESSION_TYPE%>,"<%=customerInfo==null?"":(customerInfo.getProfession()==null?"":customerInfo.getProfession())%>",false,"short_sel",'',"false",'');
			</script></td>
		<td align="right">职务：</td>
		<td align="left"><input id="job" name="job"
			value="${customerInfo.job }" type="text" class="middle_txt" size="20"
			datatype="1,is_textarea,50" maxlength="50" /></td>
	</tr>
	<tr>
	<td align="right">购买用途：</td>
		<td align="left"><script>genSelBoxExp("salesAddress",<%=Constant.SALES_ADDRESS%>,"<%=salesInfo==null?"":(salesInfo.getSalesAddress()==null?"":salesInfo.getSalesAddress())%>",false,"short_sel",'',"false",'');</script></td>		
		<td align="right">购买原因：</td>
		<td align="left"><script>genSelBoxExp("salesreson",<%=Constant.SALES_RESON%>,"<%=salesInfo==null?"":(salesInfo.getSalesReson()==null?"":salesInfo.getSalesReson())%>",false,"short_sel",'',"false",'');</script></td>
	</tr>
	<tr>
</tr>
	<tr>
		<td align="right">所在地：</td>
		<td colspan="3" align="left">省份： <select class="min_sel"
			id="txt4" name="province1" onchange="_genCity(this,'txt5')"></select>
		地级市： <select class="min_sel" id="txt5" name="city1"
			onchange="_genCity(this,'txt6')"></select> 区、县 <select
			class="min_sel" id="txt6" name="district1"></select></td>
	</tr>
	<tr>
		<td align="right">详细地址：</td>
		<td colspan="3" align="left"><input id="address" name="address"
			type="text" size="80" value="${customerInfo.address }"
			datatype="0,is_textarea,200" maxlength="200" /></td>
	</tr>
	<tr>
		<td align="right">二手车置换：  </td>
		<td align="left" colspan="3">
			<c:choose>
				<c:when test="${customerInfo.isSecond == 10041001}">
					<input type="radio" name="secondeVeh" id="sYes" value="${customerInfo.isSecond}" checked="checked" />是
					<input type="radio" name="secondeVeh" id="sNo" value="10041002" />否
				</c:when>
				<c:when test="${customerInfo.isSecond == 10041002}">
					<input type="radio" name="secondeVeh" id="sYes" value="10041001" />是
					<input type="radio" name="secondeVeh" id="sNo" value="${customerInfo.isSecond}"  checked="checked"/>否
				</c:when>
				<c:otherwise>
					<input type="radio" name="secondeVeh" id="sYes" value="10041001" checked="checked" />是
					<input type="radio" name="secondeVeh" id="sNo" value="10041002"  />否
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
</div>
<!--<div id="iframediv">
<iframe src="" frameborder="0" marginheight="0" marginwidth="0" frameborder="0" scrolling="auto" id="iframe" name="iframe" width="100%"></iframe>
</div>
-->
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
     <c:forEach items="${linkManList}" var="linkManList" varStatus="vstatus">
		<tr id="linkmanList${vstatus.index }" class="<c:if test='${vstatus.index%2==0}'>table_list_row1</c:if><c:if test='${vstatus.index%2!=0}'>table_list_row2</c:if>">
			<td><div align="center">${linkManList.NAME}</div></td>
			<td><div align="center">${linkManList.MAIN_PHONE}</div></td>
			<td><div align="center">${linkManList.OTHER_PHONE}</div></td>
			<td><div align="center">${linkManList.CONTRACT_REASON}</div></td>
			<td><div align="center"><input class="normal_btn" type="button" value="删除" width="80%" onclick="deleteOtherLink(${linkManList.LM_ID },this)" /></div></td>
		</tr>
	</c:forEach>
</table>
<table class="table_query" id="submitTable">
	<tr align="center">
		<td>
			<input type="button" value="上 报" class="normal_btn" onclick="doReport();" /> 
			<input type="button" value="返 回" class="normal_btn" onclick="goback();" /> 
			<!--1.上报车辆id  --> 
			<input type="hidden" id="vehicle_id" name="vehicle_id" value="<%=vehicleInfo.get("VEHICLE_ID") %>" />
			<!--2.实销id  --> 
			<input type="hidden" id="orderId" name="orderId" value="${salesInfo.orderId }" />
			<!--3.客户id  --> 
			<input type="hidden" id="ctmId" name="ctmId" value="${customerInfo.ctmId }" />
			<!--4.实销时间  --> 
			<input type="hidden" id="salesDate" name="salesDate" value="<%=CommonUtils.printDate(salesInfo.getSalesDate()) %>" />
			
		</td>
	</tr>
</table>
</form>
</div>

<script type="text/javascript">
	//查询集团客户
	function toFleetList(){
		OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/toQueryFleetList.do',800,600);
	}
	//查询集团客户合同
	function toFleetContractList(){
		var fleet_id = document.getElementById("fleet_id").value;
		MyAlert(fleet_id);
		if(fleet_id == 0 || ""==fleet_id){
			MyAlert("请选择集团客户!");
		}else{
			OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/toQueryFleetContractList.do',800,600);
		}
	}
	
	function changeCustomerType(value){
		var ctm_type = <%=Constant.CUSTOMER_TYPE_01%>;//个人客户
		if(value == ctm_type){
			document.getElementById("company_table").style.display = "none";
		}else{
			document.getElementById("company_table").style.display = "inline";
		}
	}	
	//是否是集团客户
	function is_fleet_Sel(value){
		var yes = '<%=yes%>';
		var s_ctmType = '<%=s_ctmType%>';
		var customer_type = document.getElementsByName("customer_type");
		if(yes == value){
			document.getElementById("is_fleet_yes").checked=true;
			document.getElementById("is_fleet_show").style.display = "inline";
			
			/*var tab1 = document.getElementById("tab1");
			var inputs = tab1.getElementsByTagName("input");
			for(i=0;i <inputs.length;i++){ 
				if(inputs[i].type=="text"){
					inputs[i].readOnly=true;
				}
			}*/
			document.getElementById("aaa").style.display = "none";
			document.getElementById("customerInfoId").style.display = "none";
		}else{
			document.getElementById("is_fleet_no").checked=true;
			document.getElementById("is_fleet_show").style.display = "none";

			document.getElementById("aaa").style.display = "inline";
			document.getElementById("customerInfoId").style.display = "inline";
			/*var tab1 = document.getElementById("tab1");
			var inputs = tab1.getElementsByTagName("input");
			for(i=0;i <inputs.length;i++){ 
				if(inputs[i].type=="text"){
					inputs[i].readOnly=false;
				}
			}*/
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

	function doReport(){

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
		if(submitForm('fm')){
			var edit_remark = document.getElementById("edit_remark").value.replace(/(^\s*)|(\s*$)/g, "");
			if(!edit_remark){
				MyAlert("请填写修改原因!");
				return;
			}
			var invoice_date = document.getElementById("invoice_date").value;//用户选择的开票时间
			if(!checkSys_Sel_Date(invoice_date,"yyyy-MM-dd")){
				MyAlert("开票时间不能大于当前时间!");
				return;
			}
			if(check_Link()){
				MyConfirm("是否提交?",doReportAction,[${salesInfo.isFleet}]);
			}
		}
	}
	function doReportAction(isFleet){
		var fsm = document.getElementById("fm");
		fsm.action= "<%=request.getContextPath()%>/sales/customerInfoManage/SalesInfoChange/salesInfoChangeAction.do?isFleet="+isFleet;
		fsm.submit();
	}
	//客户列表
	function showCustomerList(){
		OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/toQueryCtmList.do',800,600);
	}
	//显示集团客户信息
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
	//显示集团客户合同信息
	function showFleetContractInfo(contract_id,contract_no){
		document.getElementById("fleet_contract_no").value = contract_no;
		document.getElementById("fleet_contract_no_id").value = contract_id;
	}

	//显示客户信息
	function showCustomerInfo(ctmId_Old){
		var url = "<%=request.getContextPath()%>/sales/customerInfoManage/SalesReport/getOldCustomerInfo.json";
		makeCall(url,doShowCustomerInfo,{ctmId_Old:ctmId_Old}); 
	}
	
	function queryOtherLinkMan1(){
		var CtmId = '<%=CtmId%>';
		var sum = document.getElementById("iframe");
		sum.src = "<%=contextPath%>/sales/customerInfoManage/SalesReport/queryOtherLinkMan.do?oldCustomerId="+CtmId;
	}
	function goback(){
		window.location.href = "<%=request.getContextPath()%>/sales/customerInfoManage/SalesReport/salesReportInit.do";
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
	function deleteOtherLink(lm_id,obj){
		MyConfirm("是否删除?",deleteOtherLinkAction,[lm_id,obj]);
	}

	var obj_=0;
	function deleteOtherLinkAction(lm_id,obj){
		makeNomalFormCall('<%=contextPath%>/sales/customerInfoManage/SalesReport/delOtherLinkMan.json?lm_id='+lm_id,showResult1,'fm');
		//var idx = obj.parentElement.parentElement.parentElement.rowIndex;
		//var tbl = document.getElementById('file');
		//tbl.deleteRow(idx);
		obj_ = obj;
	}
	
	function del(obj){
		var idx = obj.parentElement.parentElement.parentElement.rowIndex;
		var tbl = document.getElementById('file');
		tbl.deleteRow(idx);
	}
	function showResult1(json){
		if(json.returnValue == 1){
			del(obj_);
			return true;
		}else{
			MyAlert("删除失败，请联系管理员!");
			return false;
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
			document.getElementById("FirstPrice").value="";
			document.getElementById("LoansType").value="";
			document.getElementById("LoansYear").value="";
		}

		
	}	
</script>
</body>
</html>