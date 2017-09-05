<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>

<%
	String contextPath = request.getContextPath();
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
		//var str = '{"OTHER_LINKMAN_JSON":[{"linkMan_main_phones":"","linkMan_names":"\u5f6d\u53d4\u7965","linkMan_other_phone":"13648215228"}],"REPORT_JSON":[{"card_type":"10931001","vin":"LFV10000000001352","sex":"10031001","job":"","PHONE":"13648215228","BALANCE_CLOSE_TIME":"2010-09-19 11:14:10","payment":"10251001","series_name":"\u60a6\u7fd4","main_phone":"","invoice_date":"","AUDITED_BY":"","vehicle_no":"\u65e0\u724c\u7167","SOLD_BY":"50010000000104","SERIES_CODE":"B201_B202_B206","MODEL_CODE":"SC7151A","company_phone":"13648215228","CONFIG_CODE":"SC7151A.V3A","OBLIGATED_OPERATOR":"0008","birthday":"1970-03-25","SO_STATUS":"13011075","SHEET_CREATE_DATE":"2010-09-19 10:30:34","CONFIRMED_DATE":"2010-09-19 10:43:32","material_name":"\u8f7f\u8f66 SC7151A.V3A.SA8 \u95ea\u5149\u661f\u6cb3\u94f6\u7070","material_code":"SC7151A.V3A.SA8","COLOR_NAME":"\u95ea\u5149\u661f\u6cb3\u94f6\u7070","contract_no":"","ORDER_SUM":"57855","engine_no":"A4HB049977","RE_AUDITED_BY":"\u4f55\u78a7\u82f1","industry":"0","SALES_DATE":"2010-09-19 10:47:55","district":"500240","BUSINESS_TYPE":"13001001","ctm_form":"10941001","invoice_no":"","CONTRACT_DATE":"","PAY_OFF":"12781001","ENTITY_CODE":"CA500100","consignation_date":"2010-09-19","card_num":"513521197003253673","OWNED_BY":"50010000000104","address":"\u77f3\u67f1\u53bf\u5357\u5bbe\u9547\u57ce\u5357\u6751\u767d\u5ca9\u7ec4340\u53f76\u5e621\u5355\u51431-1","province":"500000","email":"","CONTACTOR_NAME":"\u5f6d\u53d4\u7965","is_married":"0","profession":"0","so_no":"SN1009190004","customer_type":"10831001","income":"0","memo":"","color":"SA8","city":"5001","car_charactor":"10721001","DELIVERING_DATE":"2010-09-18 15:00:00","company_name":"\u5f6d\u53d4\u7965","post_code":"409100","IS_SPEEDINESS":"12781002","education":"0","CUSTOMER_NO":"PU1009190002","model_name":"SC7151A","price":"57855"}]}';
		//setJson(str);
	}

</script>

<title>实销信息上报</title>
</head>
<body onunload='javascript:destoryPrototype();'>
<div class="wbox" id="wbox" >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 整车销售 &gt; 客户信息管理 &gt; 实销信息上报</div>
<form id="fm" name="fm" method="post"><input type="hidden" name="curPage" id="curPage" value="1" /> <input type="hidden" id="dlrId" name="dlrId" value="" />
<table class="table_edit" align=center id="vehicleInfo">
	<tr class="tabletitle">
		<th colspan="4">
			<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
				车辆资料<br/>
		</th>
	</tr>
	<tr>
		<td width="15%" align=right>VIN：</td>
		<td align="left"></td>
		<td align=right>发动机号：</td>
		<td align=left></td>
	</tr>
	<tr>
		<td align=right>车系：</td>
		<td align=left></td>
		<td align=right>车型：<input type="hidden" id="model_name" name="model_name" value="" /></td>
		<td align=left></td>
	</tr>
	<tr>
		<td align=right>物料代码：</td>
		<td align=left></td>
		<td align=right>物料名称：</td>
		<td align=left></td>
	</tr>
	<tr>
		<td align=right>颜色：</td>
		<td align=left></td>
		<td align=right>&nbsp;</td>
		<td align=left>&nbsp;</td>
	</tr>
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />销售信息</th>
	</tr>
	<tr>
		<td align="right">车牌号：</td>
		<td width="35%" align="left">
			<input id="vehicle_no" name="vehicle_no" type="text" class="middle_txt" value="" datatype="1,is_carno,20"
			maxlength="20" />
		</td>
		<td width="15%" align="right">合同编号：</td>
		<td width="35%" align="left">
			<input id="contract_no" name="contract_no" type="text" class="middle_txt" value="" datatype="1,is_textarea,25"
			maxlength="25" />
		</td>
	</tr>
	<tr>
		<td align="right">开票日期：</td>
		<td align="left">
			<input name="invoice_date" id="invoice_date" type="text" class="short_txt" value="" datatype="0,is_date,10" hasbtn="true"
			callFunction="showcalendar(event, 'invoice_date', false);" /></td>
		<td align="right">发票编号：</td>
		<td align="left">
			<input id="invoice_no" name="invoice_no" type="text" class="middle_txt" value="" datatype="0,is_textarea,25" maxlength="25" />
		</td>
	</tr>
	<tr>
		<td align="right">保险公司：</td>
		<td align="left"><input id="insurance_company" name="insurance_company" type="text" class="middle_txt" value=""
			datatype="1,is_textarea,50" maxlength="50" /></td>
		<td align="right">保险日期：</td>
		<td align="left"><input name="insurance_date" id="insurance_date" type="text" class="short_txt" value="" datatype="1,is_date,10" hasbtn="true"
			callFunction="showcalendar(event, 'insurance_date', false);" /></td>
	</tr>
	<tr>
		<td align="right">车辆交付日期：</td>
		<td align="left"><input name="consignation_date" id="consignation_date" type="text" class="short_txt" value="" datatype="0,is_date,10" hasbtn="true"
			callFunction="showcalendar(event, 'consignation_date', false);" /></td>
		<td align="right">交付时公里数：</td>
		<td align="left"><input id="miles" name="miles" type="text" class="middle_txt" value="" datatype="1,is_double,6" maxlength="6" /></td>
	</tr>
	<tr>
		<td align="right">付款方式：</td>
		<td align="left"><label> <script type="text/javascript">
					genSelBoxExp("payment",<%=Constant.PAYMENT%>,"<%=Constant.PAYMENT_02%>",false,"short_sel","onchange='changeMortgageType(this.value)'","false",'');
				</script> </label></td>
		<td align="right">价格：</td>
		<td align="left"><input id="price" name="price" type="text" class="middle_txt" value="" datatype="0,isMoney,10" maxlength="10" /></td>
	</tr>
	<tr>
		<td align="right">车辆性质：</td>
		<td align="left"><label> <script type="text/javascript">
					genSelBoxExp("carCharactor",<%=Constant.SERVICEACTIVITY_CHARACTOR%>,"",false,"short_sel",'',"false",'');
				</script> </label><font color="red">*</font></td>
		<td align="right">销售顾问：</td>
		<td align="left">
			<select class="short_sel" name="salesCon" id="salesCon">
				<option value="">-请选择-</option>
				<c:forEach items="${scList }" var="scMap">
					<option value="${scMap.ID }">${scMap.NAME }</option>
				</c:forEach>
			</select>&nbsp;<font color="red">*</font>
		</td>
<!--		</td>-->
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
		<td align="right">备注：</td>
		<td colspan="2" align="left"><input type="text" id="memo" name="memo" value="" datatype="1,is_textarea,60" maxlength="60" size="50" /></td>
		<td align="left">&nbsp;</td>
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
			</script>
		</td>
		<td width="15%" align="right">是否集团客户：</td>
		<td width="35%" align="left">
			<input type="radio" id="is_fleet_yes" name="is_fleet" onclick="is_fleet_Sel(<%=Constant.IF_TYPE_YES %>);" value="<%=Constant.IF_TYPE_YES %>" />是 
			<input type="radio" id="is_fleet_no" name="is_fleet" onclick="is_fleet_Sel(<%=Constant.IF_TYPE_NO %>);" value="<%=Constant.IF_TYPE_NO %>" checked="checked" />否
		</td>
	</tr>
	<tbody id="is_fleet_show" style="display: none;">
		<tr>
			<td align="right">集团客户名称：</td>
			<td align="left">
				<input id="fleet_name" name="fleet_name" type="text" value="" class="middle_txt" datatype="0,is_textarea,20" size="20" readonly="readonly" /> 
				<input id="fleet_id" name="fleet_id" value="" type="hidden" class="middle_txt" /> 
				<input type="button" value="..." class="mini_btn" onclick="toFleetList();" /></td>
			<td align="right">集团客户合同：</td>
			<td align="left">
				<input id="fnoid" name="fleet_contract_no" type="text" class="middle_txt" value="" datatype="0,is_textarea,20" size="20" readonly="readonly" /> 
				<!-- 集团客户合同id -->
				<input id="fleet_contract_no_id" value="${salesInfo.contractId }" name="fleet_contract_no_id" type="hidden" />
				<input type="button" value="..." class="mini_btn" onclick="toFleetContractList();" /> 
				<!-- 集团客户审核状态 --> 
				<input id="fleet_check_status" name="fleet_check_status" type="hidden" />
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
			datatype="0,is_digit,11" maxlength="11" /></td>
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
			<input id="ctm_name" name="ctm_name" value="" type="text"  size="10" datatype="0,is_textarea,30" maxlength="30" /> 
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
		<td align="left"><input id="post_code" name="post_code" value="" type="text" class="middle_txt" size="20" datatype="1,is_digit,10"
			maxlength="10" /></td>
	</tr>
	<tr>
		<td align="right">出生年月：</td>
		<td align="left"><input name="birthday" id="birthday" value="" type="text" class="short_txt" datatype="0,is_date,10" hasbtn="true"
			callFunction="showcalendar(event, 'birthday', false);" /></td>
		<td align="right">客户来源：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("ctm_form",<%=Constant.CUSTOMER_FROM%>,"",false,"short_sel",'',"false",'');
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
		<td align="right"  width="15%">二手车置换：</td>
		<td align="left" colspan="3">
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
</div>
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
			<input id="submit" type="button" value="上 报" class="normal_btn" onclick="doReport();" /> 
			<input type="button" value="返 回" class="normal_btn" onclick="goback();" /> 
			<!--1.上报车辆id  --> 
			<input type="hidden" id="vehicle_id" name="vehicle_id" value="" />
			<!--2.用户选择的老客户id  --> 
			<input type="hidden" id="oldCustomerId" name="oldCustomerId" value="" /> 
			<!--3.默认值为0，如果从接口得到的JSON值不为空，则值为1  --> 
			<input type="hidden" id="isJson" name="isJson" value="0" /> 
			<!--3.接口请求  --> 
			<input type="hidden" id="vin" name="vin" value="" /> 
			<!--4.下端的单号  --> 
			<input type="hidden" id="soNo" name="soNo" value="" /> 
		</td>
	</tr>
</table>
<!--</form>-->
<!--</div>-->

<script type="text/javascript">
function chkStr(value, tipValue) {
	var chkValue = document.getElementById(value).value ;
	if(!chkValue) {
		MyAlert(tipValue) ;
		return false ;
	} else {
		return true ;
	}
}
	

	//查询集团客户
	function toFleetList(){
		OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/toQueryFleetList.do',800,600);
	}
	//查询集团客户合同
	function toFleetContractList(){
		var fleet_id = document.getElementById("fleet_id").value;
		if(fleet_id == 0 || ""==fleet_id){
			MyAlert("请选择集团客户!");
		}else{
			OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/toQueryFleetContractList.do',800,600);
		}
	}
	//“新、老”客户
	function showCustSel(status){
		if(status == "<%=Constant.IS_OLD_CTM_02 %>"){
			document.getElementById("showCustomerListID").disabled =false;
			document.getElementById("select_ctm_type_id").disabled =true ;
			document.getElementById("select_ctm_type_id_1").disabled =true ;
			document.getElementById("company_table").disabled =true;
			document.getElementById("ctm_table_id_2").disabled =true;
			document.getElementById("ctm_name").disabled =true;
			document.getElementById("tcmtd").disabled =true;
			document.getElementById("sex").disabled =true;
			document.getElementById("sextd").disabled =true;
			var oldCustomerId = document.getElementById("oldCustomerId").value;
			queryOtherLinkMan(oldCustomerId);
		}else{
			document.getElementById("showCustomerListID").disabled =true;
			document.getElementById("select_ctm_type_id").disabled =false;
			document.getElementById("select_ctm_type_id_1").disabled =false;
			document.getElementById("company_table").disabled =false;
			document.getElementById("ctm_table_id_2").disabled =false;
			document.getElementById("ctm_name").disabled =false;
			document.getElementById("tcmtd").disabled =false;
			document.getElementById("sex").disabled =false;
			document.getElementById("sextd").disabled =false;
			queryOtherLinkMan(" ");
		}
	} 
	function changeCustomerType(value){
		var ctm_type = <%=Constant.CUSTOMER_TYPE_01%>;//个人客户
		if(value == ctm_type){
			document.getElementById("company_table").style.display = "none";
			document.getElementById("customerInfoId").style.display = "inline";
			document.getElementById("file").style.display = "inline";
		}else{
			document.getElementById("company_table").style.display = "inline";
			document.getElementById("customerInfoId").style.display = "none";
			document.getElementById("file").style.display = "none";
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
	//是否是集团客户
	function is_fleet_Sel(value){
		var s_ctmType = <%=Constant.CUSTOMER_TYPE_02%>;//客户类型：公司客户
		var customer_type = document.getElementsByName("customer_type");
		if(<%=Constant.IF_TYPE_YES %> == value){
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

	function doReport(){
		if(submitForm('fm')){
			if(!checkSalesCon()) {
				MyAlert("请选择销售顾问!") ;
				
				return false ;
			}
			
			var invoice_date = document.getElementById("invoice_date").value;//用户选择的开票时间
			if(!checkSys_Sel_Date(invoice_date,"yyyy-MM-dd")){
				MyAlert("开票时间不能大于当前时间!");
				return;
			}
			var is_fleet = document.getElementsByName("is_fleet");
			var fleetValur = 0;
			var yes = '<%=Constant.IF_TYPE_YES%>';
			if(is_fleet){
				for(var i=0; i< is_fleet.length; i++){
					if(is_fleet[i].checked){
						fleetValur = is_fleet[i].value;
					}
				}
			}
			//如果是“集团客户”，校验集团客户及集团客户合同是否选择
			if(fleetValur == yes){
				var fleet_id = document.getElementById("fleet_id").value;
				var fleet_contract_no_id = document.getElementById("fleet_contract_no_id").value;
				if(fleet_id == 0){
					MyAlert("请选择集团客户");
					return;
				}
				if(fleet_contract_no_id == 0){
					MyAlert("请选择集团客户合同");
					return;
				}
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
			if($('fm').carCharactor.selectedIndex==-1){
				MyAlert("请选择车辆性质！");
				return;
			}
			var ccar = $('fm').carCharactor.options[$('fm').carCharactor.selectedIndex].value;
			if(ccar==null||ccar=="")
			{
				MyAlert("请选择车辆性质！");
				return;
			}
			
			if(document.getElementById("txt3").value==null||document.getElementById("txt3").value==""){
				MyAlert("请选择所在地！");
				return;
			}
			
			var txt1 = document.getElementById("txt1").value;
			var txt2 = document.getElementById("txt2").value;
			var txt3 = document.getElementById("txt3").value;
			
			var payment = document.getElementById("payment").value;
			var customer_type = document.getElementById("customer_type").value;
			var ctm_form = document.getElementById("ctm_form").value;
			
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
			if(!chkStr('ctm_form','请选择客户来源!')) {
				return false ;
			}
			if(customer_type==<%=Constant.CUSTOMER_TYPE_01%>){
				if(!chkStr('card_type','请选择证件类别!')) {
					return false ;
				}
				if(!chkStr('income','请选择家庭月收入!')) {
					return false ;
				}
				if(!chkStr('education','请选择教育程度!')) {
					return false ;
				}
				if(!chkStr('industry','请选择所在行业!')) {
					return false ;
				}
				if(!chkStr('is_married','请选择婚姻状况!')) {
					return false ;
				}
				if(!chkStr('profession','请选择职业!')) {
					return false ;
				}
				if(!chkStr('salesaddress','请选择购买用途!')) {
					return false ;
				}
				if(!chkStr('salesreson','请选择购买原因!')) {
					return false ;
				}
			}
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
			

			//新客户
			if(selvalue != oldflag){
				if(check_Link()){
					MyConfirm("是否提交?",doReportAction);
				}
			}
			//老客户
			if(selvalue == oldflag){
				if(!oldCustomerId){
					MyAlert("请选择老客户");
					return;
				}
			var addContent = document.getElementById("address").value ;
			if(addContent.indexOf("\"") == 0 || addContent.indexOf("“") == 0) {
				MyAlert("详细地址的首字符不能为双引号(\")!") ;
				return false ;
			}
				if(check_Link()){
					MyConfirm("是否提交?",doReportAction);
				}
			}
		}
	}
	
	function checkSalesCon() {
		var itVal = document.getElementById("salesCon").value ;
		
		if(itVal.length == "") {
			return false ;
		}
		
		return true ;
	}
	
	function doReportAction(){
		//$('fm').action= "<%=request.getContextPath()%>/sales/customerInfoManage/SalesReport/doReportAction_CVS.do";
		//$('fm').submit();
		document.getElementById('submit').disabled="true";
		makeNomalFormCall('<%=contextPath%>/sales/customerInfoManage/SalesReport/doReportAction_CVS.json',showReportRes,'fm');
	}
	
	function showReportRes(json){
		if(json.returnValue == '2'){
			MyAlert("此车辆已进行过实效上报，无法重复操作!");
		}else if(json.returnValue == '5'){
			MyAlert("开票日期不能超过当前日期!");
		}else if(json.returnValue == '6'){
			MyAlert("上报成功!");
		}else{
			MyAlert("上报失败!");
		}
	}
	function callBack() {
		MyAlert("上报成功");
	}
	//客户列表
	function showCustomerList(){
		OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/toQueryCtmList.do',800,600);
	}
	//显示集团客户信息
	function showFleetInfo(fleet_id,fleet_name){
		document.getElementById("fleet_name").value = fleet_name;
		document.getElementById("fleet_id").value = fleet_id;
		document.getElementById("fleet_contract_no").value ="";
		document.getElementById("fleet_contract_no_id").value = "";
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
		document.getElementById("job").value=objToStr(json.oldCustomerInfo.job);
		document.getElementById("sex").value=objToStr(json.oldCustomerInfo.sex);
		document.getElementById("is_married").value=objToStr(json.oldCustomerInfo.isMarried);
		document.getElementById("profession").value=objToStr(json.oldCustomerInfo.profession);
		document.getElementById("email").value=objToStr(json.oldCustomerInfo.email);
		document.getElementById("kind").value=objToStr(json.oldCustomerInfo.kind);
		document.getElementById("birthday").value=objToStr(json.oldCustomerInfo.birthday.substring(0,10));
		document.getElementById("company_s_name").value=objToStr(json.oldCustomerInfo.companySName);
		document.getElementById("card_type").value=objToStr(json.oldCustomerInfo.cardType);
		document.getElementById("other_phone").value=objToStr(json.oldCustomerInfo.otherPhone);
		document.getElementById("address").value=objToStr(json.oldCustomerInfo.address);
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
		queryOtherLinkMan(json.oldCustomerInfo.ctmId);
	}
	function queryOtherLinkMan1(value){
		var sum = document.getElementById("iframe");
		sum.src = "<%=contextPath%>/sales/customerInfoManage/SalesReport/queryOtherLinkMan.do?oldCustomerId="+value;
	}
	function queryOtherLinkMan(value){
		makeNomalFormCall('<%=contextPath%>/sales/customerInfoManage/SalesReport/queryOtherLinkMan.json?oldCustomerId='+value,showOtherLinkMan,'fm');
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
		makeNomalFormCall('<%=contextPath%>/sales/customerInfoManage/SalesReport/delOtherLinkMan.json?lm_id='+lm_id,showResult1,'fm');
	}

	function showResult1(json){
		var oldCustomerId = document.getElementById("oldCustomerId").value;
		queryOtherLinkMan(oldCustomerId);
		
		_hide() ;
	}
	
	function goback(){
		//window.location.href = "<%=request.getContextPath()%>/sales/customerInfoManage/SalesReport/salesReportInit.do";
		window.close() ;
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
		var json = str.evalJSON();
		/*
		if(Object.keys(json).length>0){
			document.getElementById("isJson").value=1;
		}*/
		//vehicleInfo
		var table = document.getElementById("vehicleInfo");
		table.rows[1].cells[1].innerHTML = objToStr(json.REPORT_JSON[0].vin);
		table.rows[1].cells[3].innerHTML = objToStr(json.REPORT_JSON[0].engine_no);
		table.rows[2].cells[1].innerHTML = objToStr(json.REPORT_JSON[0].series_name);
		table.rows[2].cells[3].innerHTML = objToStr(json.REPORT_JSON[0].model_name);
		document.getElementById("model_name").value = objToStr(json.REPORT_JSON[0].model_name);
		table.rows[3].cells[1].innerHTML = objToStr(json.REPORT_JSON[0].material_code);
		table.rows[3].cells[3].innerHTML = objToStr(json.REPORT_JSON[0].material_name);
		table.rows[4].cells[1].innerHTML = objToStr(json.REPORT_JSON[0].color);
		
		document.getElementById("soNo").value = objToStr(json.REPORT_JSON[0].so_no);
		
		//销售信息  客户类型 个人客户信息
		//document.getElementById("vehicle_id").value = objToStr(json.REPORT_JSON[0].vin);
		document.getElementById("vin").value = objToStr(json.REPORT_JSON[0].vin);
		document.getElementById("vehicle_no").value = objToStr(json.REPORT_JSON[0].vehicle_no);					//车牌号 
		document.getElementById("contract_no").value = objToStr(json.REPORT_JSON[0].contract_no);				//合同编号
		document.getElementById("invoice_date").value = objToStr(json.REPORT_JSON[0].invoice_date);				//开票日期
		document.getElementById("invoice_no").value = objToStr(json.REPORT_JSON[0].invoice_no);					//发票编号
		document.getElementById("insurance_company").value = objToStr(json.REPORT_JSON[0].insurance_company);	//保险公司   下端无
		document.getElementById("insurance_date").value = objToStr(json.REPORT_JSON[0].insurance_date);         //保险日期   下端无
		document.getElementById("consignation_date").value = objToStr(json.REPORT_JSON[0].consignation_date);	//车辆交付日期
		document.getElementById("miles").value = objToStr(json.REPORT_JSON[0].miles);							//交付时公里数  下端无
		document.getElementsByName("payment").value = objToStr(json.REPORT_JSON[0].payment);					//付款方式
		document.getElementById("price").value = objToStr(json.REPORT_JSON[0].price);							//价格
		document.getElementById("carCharactor").value = objToStr(json.REPORT_JSON[0].car_charactor);				//车辆性质
		document.getElementById("memo").value = objToStr(json.REPORT_JSON[0].memo);								//备注
		document.getElementById("customer_type").value = objToStr(json.REPORT_JSON[0].customer_type);			//客户类型
		document.getElementById("is_fleet").value = objToStr(json.REPORT_JSON[0].is_fleet);						//是否集团客户   下端无
		document.getElementById("company_name").value = objToStr(json.REPORT_JSON[0].company_name);				//公司名称
		document.getElementById("company_s_name").value = objToStr(json.REPORT_JSON[0].company_s_name);			//公司简称    下端无
		document.getElementById("company_phone").value = objToStr(json.REPORT_JSON[0].company_phone);			//公司电话     
		document.getElementById("level_id").value = objToStr(json.REPORT_JSON[0].level_id);						//公司规模    下端无
		document.getElementById("kind").value = objToStr(json.REPORT_JSON[0].kind);								//公司性质     无
		document.getElementById("vehicle_num").value = objToStr(json.REPORT_JSON[0].vehicle_num);				//目前车辆数   无
		document.getElementById("ctm_name").value = objToStr(json.REPORT_JSON[0].company_name);					//客户姓名
		document.getElementById("sex").value = objToStr(json.REPORT_JSON[0].sex);								//性别
		document.getElementById("card_type").value = objToStr(json.REPORT_JSON[0].card_type);					//证件类别
		document.getElementById("card_num").value = objToStr(json.REPORT_JSON[0].card_num);						//证件号码
		document.getElementById("main_phone").value = objToStr(json.REPORT_JSON[0].main_phone);					//主要联系电话  
		document.getElementById("other_phone").value = objToStr(json.REPORT_JSON[0].company_phone);				//其他联系电话  用的公司电话
		document.getElementById("email").value = objToStr(json.REPORT_JSON[0].email);							//电子邮件
		document.getElementById("post_code").value = objToStr(json.REPORT_JSON[0].post_code);					//邮编
		document.getElementById("birthday").value = objToStr(json.REPORT_JSON[0].birthday);						//出生年月
		document.getElementById("ctm_form").value = objToStr(json.REPORT_JSON[0].ctm_form);						//客户来源
		document.getElementById("income").value = objToStr(json.REPORT_JSON[0].income);							//家庭月收入
		document.getElementById("education").value = objToStr(json.REPORT_JSON[0].education);					//教育程度
		//document.getElementById("industry").value = objToStr(json.REPORT_JSON[0].industry);					//所在行业  上下端对应不上
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
		//清空联系人表格
		var tab = document.getElementById("file");
        var row = tab.rows.length;
        var k = 2;
        while (k < row) {
        	tab.deleteRow(k);
        	row--;
        }
		//其他联系人
		if(json.OTHER_LINKMAN_JSON.length){
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
</script>
</body>
</html>