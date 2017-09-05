<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.po.TmBrandPO"%>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	List list = null;
	if (request.getAttribute("list") != null) {
		list = (LinkedList) request.getAttribute("list");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- <link href="<%=contextPath%>/style/content.css" rel="stylesheet"
	type="text/css" />
<link href="<%=contextPath%>/style/calendar.css" rel="stylesheet"
	type="text/css" />
<link href="<%=contextPath%>/style/page-info.css" rel="stylesheet"
	type="text/css" />
<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"
	type="text/css" /> -->
<title>公司维护</title>
<style>
.table_info_input_4col {
	width: 150px;
	text-align: right;
}

.table_info_input {
	width: 350px;
	text-align: left;
}
.td-width-5{width: 8.5%;}
.input-width-230{width: 230px}
table.table_query td.cell-pad-adjust{padding-left: 75px;}
.td-width-15{width: 15%}
.right-col-name{width:10%}
#REMARK_XIAOSHOU{margin-left: 10px}
</style>
<script type="text/javascript">
var dept_tree_url = "<%=contextPath%>/sysmng/usemng/SgmSysUser/initOrgTree.json";
</script>
</head>

<body onload="genLocSel('PROVINCE_ID','CITY_ID','COUNTIES');"> <!-- onunload='javascript:destoryPrototype()'loadcalendar(); -->
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 组织管理
			&gt; 公司维护
		</div>
		<form id="fm" name="fm" class="u-form-elem">
			<input type="hidden" name="COMPANY_ID" id="COMPANY_ID" value="${cb.companyId }"/> 
			<div class="form-panel">
				<h2><img class="nav" src="<%=contextPath%>/img/subNav.gif" />公司维护</h2>
				<div class="form-body">
					<table class="table_query" border="0">
						<tr>
							<th colspan="2"><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif" />旧公司信息</th>
							<th colspan="2"><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif" />新公司信息</th>
						</tr>
								<tr>
									<td class="table_info_input_4col right td-width-15" nowrap="nowrap">公司代码：</td>
									<td class="table_info_input" colspan="3">
										<input class="middle_txt" type="text" name="COMPANY_CODE" id="COMPANY_CODE" readonly="readonly" value="${cb.companyCode }" />
									</td>
								</tr>
								 <tr>
									<td class="table_info_input_4col right td-width-15" nowrap="nowrap">公司简称：</td>
									<td class="table_info_input" nowrap="nowrap">
										<input class="middle_txt" type="text" name="COMPANY_SHORTNAME" id="COMPANY_SHORTNAME" readonly="readonly" value="${cb.companyShortname }" />
									</td>
									
									<td class="table_info_input_4col right td-width-5" nowrap="nowrap">公司简称：</td>
									<td class="table_info_input" >
										<input class="middle_txt" type="text" name="COMPANY_SHORTNAME_NEW" id ="COMPANY_SHORTNAME_NEW" value="" />
									</td>
								</tr> 
								<tr>
									<td class="table_info_input_4col right td-width-15" nowrap="nowrap">公司名称：</td>
									<td class="table_info_input">
									<input class="middle_txt input-width-230" type="text" name="COMPANY_NAME" id="COMPANY_NAME" readonly="readonly" value="${cb.companyName }" /></td>
									
									<td class="table_info_input_4col right td-width-5" nowrap="nowrap">公司名称：</td>
									<td class="table_info_input" >
									<input class="middle_txt input-width-230" type="text" name="COMPANY_NAME_NEW" id="COMPANY_NAME_NEW" value="" />
									</td>
								</tr>
								<tr style="display: none;">
									<td class="table_info_input_4col right" nowrap="nowrap">状态：</td>
									<td class="table_info_input" nowrap="nowrap" colspan="4"><script
											type="text/javascript"> genSelBox("STATUS",<%=Constant.STATUS%>,"${cb.status}",false,"min_sel","");</script><font
										color="red">&nbsp;*</font></td>
								</tr> 
							 <tr>
								<td class="cell-pad-adjust" colspan="4">
									<label class="u-radiobox">
										<input checked="checked" type="radio" value="10771002" name="changeFlag" onclick="changeMod('shouHouKPXX')">
										<span></span>
									</label>
									<label class="u-label">修改售后经销商</label>
									<label class="u-radiobox">
										<input type="radio" value="10771001" name="changeFlag" onclick="changeMod('xiaoShouKPXX')">
										<span></span>
									</label>	
									<label class="u-label">修改销售经销商</label>
								</td>
							</tr> 
						</table>	
						<table class="table_query" id="shouHouKPXX" style="display: none;">
							<tr>
								<td  colspan=2>
									<table  border="0" align="left" cellpadding="1"
										cellspacing="1">
										<tr>
											<th colSpan=2 align=left><IMG class="panel-icon nav"  src="<%=contextPath%>/img/subNav.gif"> 旧开票信息</th>
										</tr>
									</table>
								</td>
								<td  colspan=2>
									<table  border="0" align="left" cellpadding="1"
										cellspacing="1">
										<tr>
											<th colSpan=2 align=left><IMG class="panel-icon nav"  src="<%=contextPath%>/img/subNav.gif"> 新开票信息</th>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td class="right" align="right" width="15%">开票名称：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt input-width-230" id="ERP_CODE_SHOUHOU_OLD" readonly="readonly" name="ERP_CODE_SHOUHOU_OLD"
									value="${FDealer.ERP_CODE}" /></td>
								<td class="right" align="right" width="15%">开票名称：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt input-width-230" id="ERP_CODE_SHOUHOU"
									name="ERP_CODE_SHOUHOU" value="${FDealer.ERP_CODE}" /></td>
							</tr>
							<tr>
								<td class="right" align="right" width="15%">开户行：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt input-width-230" id="BEGIN_BANK_SHOUHOU_OLD" readonly="readonly" name="BEGIN_BANK_SHOUHOU_OLD"
									value="${FDealer.BEGIN_BANK}" /></td>
								<td class="right" align="right" width="15%">开户行：</td>
								<td class="table_query_4Col_input input-width-230" nowrap="nowrap"><input
									type="text" class="middle_txt" id="BEGIN_BANK_SHOUHOU"
									name="BEGIN_BANK_SHOUHOU" value="${FDealer.BEGIN_BANK}" /></td>

							</tr>
							<tr>
								<td class="right" align="right" width="15%">服务站地址：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt input-width-230" id="INVOICE_ADD_SHOUHOU_OLD"
									name="INVOICE_ADD_SHOUHOU_OLD" readonly="readonly" value="${FDealer.INVOICE_ADD}" /></td>
								<td class="right" align="right" width="15%">服务站地址：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt input-width-230" id="INVOICE_ADD_SHOUHOU"
									name="INVOICE_ADD_SHOUHOU" value="${FDealer.INVOICE_ADD}" /></td>
							</tr>
							<tr>
								<td class="right" align="right">纳税人识别号：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt" id="TAXPAYER_NO_SHOUHOU_OLD"
									name="TAXPAYER_NO_SHOUHOU_OLD" readonly="readonly" value="${FDealer.TAXPAYER_NO}" /></td>
								<td class="right" align="right">纳税人识别号：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt" id="TAXPAYER_NO_SHOUHOU"
									name="TAXPAYER_NO_SHOUHOU" value="${FDealer.TAXPAYER_NO}" /></td>

							</tr>
							<tr>
								<td class="right" align="right">增值税发票：</td>
								<td class="table_query_4Col_input" nowrap="nowrap" disabled><script
										type="text/javascript">
							genSelBoxExp("TAX_INVOICE_SHOUHOU_OLD",<%=Constant.IF_TYPE%>,"${FDealer.TAX_INVOICE}",true,"short_sel u-select",'',"false",'');
						</script></td>
								<td class="right" align="right">增值税发票：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><script
										type="text/javascript">
							genSelBoxExp("TAX_INVOICE_SHOUHOU",<%=Constant.IF_TYPE%>,"${FDealer.TAX_INVOICE}",true,"short_sel u-select",'',"false",'');
						</script></td>
							</tr>
							<tr>
								<td class="right" align="right">发票邮寄地址：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" readonly="readonly" class="middle_txt" id="INVOICE_POST_ADD_SHOUHOU_OLD"
									name="INVOICE_POST_ADD_SHOUHOU_OLD" value="${FDealer.INVOICE_POST_ADD}" /></td>
								<td class="right" align="right">发票邮寄地址：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt" id="INVOICE_POST_ADD_SHOUHOU"
									name="INVOICE_POST_ADD_SHOUHOU" value="${FDealer.INVOICE_POST_ADD}" /></td>
							</tr>
							<tr>
								<td class="right" align="right" width="15%">税号：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt" readonly="readonly" id="TAXES_NO_SHOUHOU_OLD" name="TAXES_NO_SHOUHOU_OLD"
									value="${FDealer.TAXES_NO}" /></td>
								<td class="right" align="right" width="15%">税号：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt" id="TAXES_NO_SHOUHOU"
									name="TAXES_NO_SHOUHOU" value="${FDealer.TAXES_NO}" /></td>
							</tr>
							<tr>
								<td class="right" align="right" width="15%">账号：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt" readonly="readonly" id="INVOICE_ACCOUNT_SHOUHOU_OLD"
									name="INVOICE_ACCOUNT_SHOUHOU_OLD" value="${FDealer.INVOICE_ACCOUNT}" /></td>
								<td class="right" align="right" width="15%">账号：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt" id="INVOICE_ACCOUNT_SHOUHOU"
									name="INVOICE_ACCOUNT_SHOUHOU" value="${FDealer.INVOICE_ACCOUNT}" /></td>
							</tr>
							<tr>
								<td class="right" align="right" width="15%">电话：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt"  readonly="readonly"id="INVOICE_PHONE_SHOUHOU_OLD"
									name="INVOICE_PHONE_SHOUHOU_OLD" value="${FDealer.INVOICE_PHONE}" /></td>
								<td class="right" align="right" width="15%">电话：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt" id="INVOICE_PHONE_SHOUHOU"
									name="INVOICE_PHONE_SHOUHOU" value="${FDealer.INVOICE_PHONE}" /></td>
							</tr>
							<tr>
								<td align="right" nowrap="nowrap" class="table_query_4Col_input right">纳税人性质：</td>
								<td class="table_query_4Col_input" nowrap="nowrap" ><select class="u-select"
									disabled id="TAXPAYER_NATURE_SHOUHOU_OLD" name="TAXPAYER_NATURE_SHOUHOU_OLD">
										<option value="一般纳税人">一般纳税人</option>
										<option value="小规模纳税人">小规模纳税人</option>
								</select> <script type="text/javascript">
							setItemValue('TAXPAYER_NATURE_SHOUHOU_OLD','${FDealer.TAXPAYER_NATURE }');
						</script></td>
								<td align="right" nowrap="nowrap" class="table_query_4Col_input right">纳税人性质：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><select class="u-select"
									id="TAXPAYER_NATURE_SHOUHOU" name="TAXPAYER_NATURE_SHOUHOU">
										<option value="一般纳税人">一般纳税人</option>
										<option value="小规模纳税人">小规模纳税人</option>
								</select> <script type="text/javascript">
							setItemValue('TAXPAYER_NATURE_SHOUHOU','${FDealer.TAXPAYER_NATURE }');
						</script></td>
							</tr>
							<tr>
								<td align="right" nowrap="nowrap" class="table_query_4Col_input right">开票税率：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt" readonly="readonly" id="TAX_DISRATE_SHOUHOU_OLD"
									name="TAX_DISRATE_SHOUHOU_OLD" value="${FDealer.TAX_DISRATE}" /></td>

								<td align="right" nowrap="nowrap" class="table_query_4Col_input right">开票税率：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt"  readonly="readonly"id="TAX_DISRATE_SHOUHOU"
									name="TAX_DISRATE_SHOUHOU" value="${FDealer.TAX_DISRATE}" /></td>
							</tr>
								<td class="right" align="right">备注：</td>
								<td colspan="3">
									<textarea id="REMARK_SHOUHOU" class="form-control remark" name="REMARK_SHOUHOU" rows="3" cols="80"></textarea>
								</td>
						</table>
						<table class="table_query" id="xiaoShouKPXX" style="display: none;">
							<tr id="kpxx">
								<td  colspan=2>
									<table border="0" align=left cellpadding="1"
										cellspacing="1">
										<tr>
											<th colSpan=2 align=left><IMG class=nav
												src="<%=contextPath%>/img/subNav.gif"> 旧开票信息</th>
										</tr>
									</table>
								</td>
								<td  colspan=2>
									<table  border="0" align="left" cellpadding="1"
										cellspacing="1">
										<tr>
											<th colSpan=2 align=left><IMG class=nav
												src="<%=contextPath%>/img/subNav.gif"> 新开票信息</th>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td class="right" align="right" width="15%">开票名称：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt input-width-230" id="ERP_CODE_XIAOSHOU_OLD" readonly="readonly"name="ERP_CODE_XIAOSHOU_OLD"
									value="${SDealer.ERP_CODE}" /></td>
								<td class="right right-col-name" align="right">开票名称：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt input-width-230" id="ERP_CODE_XIAOSHOU"
									name="ERP_CODE_XIAOSHOU" value="${SDealer.ERP_CODE}" /></td>
							</tr>
							<tr>
								<td align="right" nowrap="nowrap" class="table_query_4Col_input right">开票地址：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt input-width-230"  readonly="readonly"id="INVOICE_ADD_XIAOSHOU_OLD"
									name="INVOICE_ADD_XIAOSHOU_OLD" value="${SDealer.INVOICE_ADD}" /></td>

								<td align="right" nowrap="nowrap" class="table_query_4Col_input right right-col-name">开票地址：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt input-width-230" id="INVOICE_ADD_XIAOSHOU"
									name="INVOICE_ADD_XIAOSHOU" value="${SDealer.INVOICE_ADD}" /></td>
							</tr>
							<tr>
								<td class="right" align="right" width="15%">开户行：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt input-width-230"  readonly="readonly"id="BEGIN_BANK_XIAOSHOU_OLD" name="BEGIN_BANK_XIAOSHOU_OLD"
									value="${SDealer.BEGIN_BANK}" /></td>
								<td class="right right-col-name" align="right">开户行：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt input-width-230" id="BEGIN_BANK_XIAOSHOU"
									name="BEGIN_BANK_XIAOSHOU" value="${SDealer.BEGIN_BANK}" /></td>

							</tr>
							<tr>
								<td class="right" align="right">纳税人识别号：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt"  readonly="readonly"id="TAXPAYER_NO_XIAOSHOU_OLD"
									name="TAXPAYER_NO_XIAOSHOU_OLD" value="${SDealer.TAXPAYER_NO}" /></td>
								<td class="right right-col-name" align="right">纳税人识别号：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt" id="TAXPAYER_NO_XIAOSHOU"
									name="TAXPAYER_NO_XIAOSHOU" value="${SDealer.TAXPAYER_NO}" /></td>

							</tr>
							<tr>
								<td class="right" align="right">增值税发票：</td>
								<td disabled class="table_query_4Col_input" nowrap="nowrap"><script
										type="text/javascript">
							genSelBoxExp("TAX_INVOICE_XIAOSHOU_OLD",<%=Constant.IF_TYPE%>,"${SDealer.TAX_INVOICE}",true,"short_sel u-select",'',"false",'');
						</script></td>
								<td class="right right-col-name" align="right">增值税发票：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><script
										type="text/javascript">
							genSelBoxExp("TAX_INVOICE_XIAOSHOU",<%=Constant.IF_TYPE%>,"${SDealer.TAX_INVOICE}",true,"short_sel u-select",'',"false",'');
						</script></td>
							</tr>
							<tr>
								<td class="right" align="right" width="15%">税号：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt"  readonly="readonly"id="TAXES_NO_XIAOSHOU_OLD" name="TAXES_NO_XIAOSHOU_OLD"
									value="${SDealer.TAXES_NO}" /></td>
								<td class="right right-col-name" align="right" width="15%">税号：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt" id="TAXES_NO_XIAOSHOU"
									name="TAXES_NO_XIAOSHOU" value="${SDealer.TAXES_NO}" /></td>
							</tr>
							<tr>
								<td class="right" align="right" width="15%">账号：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt"  readonly="readonly"id="INVOICE_ACCOUNT_XIAOSHOU_OLD"
									name="INVOICE_ACCOUNT_XIAOSHOU_OLD" value="${SDealer.INVOICE_ACCOUNT}" /></td>
								<td class="right right-col-name" align="right" width="15%">账号：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt" id="INVOICE_ACCOUNT_XIAOSHOU"
									name="INVOICE_ACCOUNT_XIAOSHOU" value="${SDealer.INVOICE_ACCOUNT}" /></td>
							</tr>
							<tr>
								<td class="right" align="right" width="15%">电话：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt"  readonly="readonly"id="INVOICE_PHONE_XIAOSHOU_OLD"
									name="INVOICE_PHONE_XIAOSHOU_OLD" value="${SDealer.INVOICE_PHONE}" /></td>
								<td class="right right-col-name" align="right" width="15%">电话：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt" id="INVOICE_PHONE_XIAOSHOU"
									name="INVOICE_PHONE_XIAOSHOU" value="${SDealer.INVOICE_PHONE}" /></td>
							</tr>
							<tr>
								<td class="right" align="right" width="15%">开票联系人手机：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt"  readonly="readonly"id="INVOICE_TELPHONE_XIAOSHOU_OLD"
									name="INVOICE_TELPHONE_XIAOSHOU_OLD" value="${SDealer.INVOICE_TELPHONE}" /></td>
								<td class="right right-col-name" align="right" width="15%">开票联系人手机：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt" id="INVOICE_TELPHONE_XIAOSHOU"
									name="INVOICE_TELPHONE_XIAOSHOU" value="${SDealer.INVOICE_TELPHONE}" /></td>
							</tr>
							<tr>
								<td class="right" align="right">开票联系人：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt"  readonly="readonly"id="INVOICE_PERSION_XIAOSHOU_OLD"
									name="INVOICE_PERSION_XIAOSHOU_OLD" value="${SDealer.INVOICE_PERSION}" /></td>
								<td class="right right-col-name" align="right">开票联系人：</td>
								<td class="table_query_4Col_input" nowrap="nowrap"><input
									type="text" class="middle_txt" id="INVOICE_PERSION_XIAOSHOU"
									name="INVOICE_PERSION_XIAOSHOU" value="${SDealer.INVOICE_PERSION}" /></td>
							</tr>
							<tr>
								<td class="right" align="right">备注：</td>
								<td colspan="3">
									<textarea id="REMARK_XIAOSHOU" class="form-control remark" name="REMARK_XIAOSHOU" rows="3" cols="80"></textarea>
								</td>
							</tr>
						<tr>
							<td class="center" colspan="4">
								<input class="u-button u-cancel" type="button" value="返回" onclick="goBack()" />
								<input class="u-button" type="button" value="保存" onclick="confirmAdd()" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			
		</form>
	</div>
	<script type="text/javascript">
	function confirmAdd(){
		var COMPANY_SHORTNAME = document.getElementById("COMPANY_SHORTNAME").value;
		var COMPANY_SHORTNAME_NEW = document.getElementById("COMPANY_SHORTNAME_NEW").value;
		var COMPANY_NAME = document.getElementById("COMPANY_NAME").value;
		var COMPANY_NAME_NEW = document.getElementById("COMPANY_NAME_NEW").value;
		
		if(COMPANY_SHORTNAME_NEW == ""||COMPANY_SHORTNAME_NEW == null||COMPANY_SHORTNAME_NEW == "undefined"){
			MyAlert("请输入公司简称");
			return false;
		}
		if(COMPANY_SHORTNAME == COMPANY_SHORTNAME_NEW){
			MyAlert("新旧公司简称一致");
			return false;
		}
		if(COMPANY_NAME_NEW == ""||COMPANY_NAME_NEW == null||COMPANY_NAME_NEW == "undefined"){
			MyAlert("请输入公司全称");
			return false;
		}
		
		if(COMPANY_NAME == COMPANY_NAME_NEW){
			MyAlert("新旧公司全称一致");
			return false;
		}
		
		sendAjax('<%=request.getContextPath()%>/sysmng/orgmng/DlrInfoMng/alterCompanyInfo.json',showResult,'fm');
	}
	function showResult(json){
		if(json.Exception) {
			MyAlert(json.Exception.message);
		} else {
			MyAlertForFun(json.message, goBack);
		}
	}
	
	function goBack(){
		window.location.href = "<%=contextPath%>/sysmng/orgmng/DlrInfoMng/queryAllDlrInfo2.do";
		}

		function clrTxt(value) {
			document.getElementById(value).value = "";
		}
		function changeMod(value) {
			document.getElementById("xiaoShouKPXX").style.display = "none";
			document.getElementById("shouHouKPXX").style.display = "none";
			document.getElementById(value).style.display = "";
		}
	</script>
</body>
</html>
