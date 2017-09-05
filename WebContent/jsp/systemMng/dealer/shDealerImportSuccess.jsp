<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.math.BigDecimal"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@ page import=" com.infodms.dms.util.CommonUtils"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>售后经销商信息导入确认</title>
</head>
<body onunload='javascript:destoryPrototype()'>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置 >系统管理>经销商管理>售后经销商维护>导入确认</div>	 
<form  name="fm" id="fm">
<table class="table_query" id="subtab">
  <tr class="csstr" align="center"> 
		<th>
			<div align="left">
				<input class="cssbutton" type="button" id="savebtn" name='saveResButton' onclick='isSave();' value='保存' />
				<input class="cssbutton" type='button' name='saveResButton' onclick='history.back();' value='返回' />
			</div>
		</th>	
  	</tr>
</table>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<script type="text/javascript">
var myPage;
//查询路径           
var url = "<%=contextPath%>/sysmng/dealer/ShDealerImport/shImportOperateQuery.json";
var title = null;
var columns = [
			{header: "序号",align:'center',renderer:getIndex},
			{header: "大区",dataIndex: 'DX_NAME',align:'center'},
			{header: "所属组织",dataIndex: 'DEALER_ORG_ID',align:'center'},
			{header: "省份",dataIndex: 'PROVINCE_ID',align:'center'},
			{header: "所属行政区域城市",dataIndex: 'CITY_ID',align:'center'},
			{header: "所属行政区域区县",dataIndex: 'COUNTIES',align:'center'},
			{header: "邮编",dataIndex: 'ZIP_CODE',align:'center'},
			{header: "服务商编码",dataIndex: 'DEALER_CODE',align:'center'},
			{header: "服务商等级",dataIndex: 'DEALER_TYPE',align:'center'},
			{header: "状态",dataIndex: 'SERVICE_STATUS',align:'center'},
			{header: "服务商全称",dataIndex: 'DEALER_NAME',align:'center'},
			{header: "服务商简称",dataIndex: 'DEALER_SHORTNAME',align:'center'},
			{header: "上级服务商",dataIndex: 'PARENT_DEALER_D',align:'center'},
			{header: "与一级经销商关系",dataIndex: 'DEALER_RELATION',align:'center'},
			{header: "企业注册地址",dataIndex: 'ADDRESS',align:'center'},
			{header: "注册证号",dataIndex: 'ZCCODE',align:'center'},
			{header: "主营范围",dataIndex: 'ZY_SCOPE',align:'center'},
			{header: "兼营范围",dataIndex: 'JY_SCOPE',align:'center'},
			{header: "组织机构代码",dataIndex: 'COMPANY_ZC_CODE',align:'center'},
			{header: "维修资质",dataIndex: 'MAIN_RESOURCES',align:'center'},
			{header: "建厂时间",dataIndex: 'SITE_DATE',align:'center'},
			{header: "法人代表",dataIndex: 'LEGAL',align:'center'},
			{header: "单位性质",dataIndex: 'UNION_TYPE',align:'center'},
			{header: "固定资产（万元）",dataIndex: 'FIXED_CAPITAL',align:'center'},
			{header: "注册资金（万元）",dataIndex: 'REGISTERED_CAPITAL',align:'center'},
			{header: "服务站人数",dataIndex: 'PEOPLE_NUMBER',align:'center'},
			{header: "维修车间面积（平方米）",dataIndex: 'MAIN_AREA',align:'center'},
			{header: "接待室面积（平方米）",dataIndex: 'RECEIVE_AREA',align:'center'},
			{header: "配件库面积（平方米）",dataIndex: 'PARTS_AREA',align:'center'},
			{header: "停车场面积（平方米）",dataIndex: 'DEPOT_AREA',align:'center'},
			{header: "营业时间",dataIndex: 'OPENING_TIME',align:'center'},
			{header: "月平均维修能力(台次)",dataIndex: 'ONLY_MONTH_COUNT',align:'center'},
			{header: "经营类型",dataIndex: 'WORK_TYPE',align:'center'},
			{header: "是否4S店",dataIndex: 'IS_FOUR_S',align:'center'},
			{header: "建店类别",dataIndex: 'IMAGE_LEVEL',align:'center'},
			{header: "地址（省、市、县、区、路、号）",dataIndex: 'COMPANY_ADDRESS',align:'center'},
			{header: "有无二级服务站",dataIndex: 'IS_LOW_SER',align:'center'},
			{header: "企业授权类型",dataIndex: 'AUTHORIZATION_TYPE',align:'center'},
			{header: "授权时间",dataIndex: 'AUTHORIZATION_DATE',align:'center'},
			{header: "是否经营其他品牌",dataIndex: 'IS_ACTING_BRAND',align:'center'},
			{header: "代理其它品牌名称",dataIndex: 'ACTING_BRAND_NAME',align:'center'},
			{header: "24小时服务热线",dataIndex: 'HOTLINE',align:'center'},
			{header: "服务经理",dataIndex: 'SER_MANAGER_NAME',align:'center'},
			{header: "服务经理手机",dataIndex: 'SER_MANAGER_TELPHONE',align:'center'},
			{header: "服务经理邮箱",dataIndex: 'SER_MANAGER_EMAIL',align:'center'},
			{header: "索赔主管",dataIndex: 'CLAIM_DIRECTOR_NAME',align:'center'},
			{header: "索赔主管办公电话",dataIndex: 'CLAIM_DIRECTOR_PHONE',align:'center'},
			{header: "索赔主管手机",dataIndex: 'CLAIM_DIRECTOR_TELPHONE',align:'center'},
			{header: "索赔主管邮箱",dataIndex: 'CLAIM_DIRECTOR_EMAIL',align:'center'},
			{header: "索赔传真",dataIndex: 'CLAIM_DIRECTOR_FAX',align:'center'},
			{header: "服务主管",dataIndex: 'SER_DIRECTOR_NAME',align:'center'},
			{header: "服务主管办公电话",dataIndex: 'SER_DIRECTOR_PHONE',align:'center'},
			{header: "服务主管手机",dataIndex: 'SER_DIRECTOR_TELHONE',align:'center'},
			{header: "技术主管",dataIndex: 'TECHNOLOGY_DIRECTOR_NAME',align:'center'},
			{header: "技术主管手机",dataIndex: 'TECHNOLOGY_DIRECTOR_TELPHONE',align:'center'},
			{header: "配件主管",dataIndex: 'FITTINGS_DEC_NAME',align:'center'},
			{header: "配件主管办公电话",dataIndex: 'FITTINGS_DEC_TELPHONE',align:'center'},
			{header: "配件主管手机",dataIndex: 'FITTINGS_DEC_PHONE',align:'center'},
			{header: "配件主管邮箱",dataIndex: 'FITTINGS_DEC_EMAIL',align:'center'},
			{header: "配件传真",dataIndex: 'FITTINGS_DEC_FAX',align:'center'},
			{header: "配件储备金额（万元）",dataIndex: 'PARTS_STORE_AMOUNT',align:'center'},
			{header: "开户行",dataIndex: 'BEGIN_BANK',align:'center'},
			{header: "开票名称",dataIndex: 'ERP_CODE',align:'center'},
			{header: "银行帐号",dataIndex: 'INVOICE_ACCOUNT',align:'center'},
			{header: "开票电话",dataIndex: 'INVOICE_PHONE',align:'center'},
			{header: "开票地址",dataIndex: 'INVOICE_ADD',align:'center'},
			{header: "纳税人识别号",dataIndex: 'TAXPAYER_NO',align:'center'},
			{header: "纳税人性质",dataIndex: 'TAXPAYER_NATURE',align:'center'},
			{header: "增值税发票",dataIndex: 'TAX_INVOICE',align:'center'},
			{header: "开票税率",dataIndex: 'TAX_DISRATE',align:'center'},
			{header: "财务经理",dataIndex: 'FINANCE_MANAGER_NAME',align:'center'},
			{header: "财务经理办公电话",dataIndex: 'FINANCE_MANAGER_PHONE',align:'center'},
			{header: "财务手机",dataIndex: 'FINANCE_MANAGER_TELPHONE',align:'center'},
			{header: "财务邮箱",dataIndex: 'FINANCE_MANAGER_EMAIL',align:'center'},
			{header: "备注",dataIndex: 'REMARK',align:'center'},
			{header: "结算等级",dataIndex: 'BALANCE_LEVEL',align:'center'},
			{header: "开票等级",dataIndex: 'INVOICE_LEVEL',align:'center'},
			{header: "索赔员",dataIndex: 'SPY_MAN',align:'center'},
			{header: "服务商电话",dataIndex: 'PHONE',align:'center'},
			{header: "辐射区域",dataIndex: 'THE_AGENTS',align:'center'},
			{header: "代理车型",dataIndex: 'PROXY_VEHICLE_TYPE',align:'center'},
			{header: "验收形象等级",dataIndex: 'IMAGE_C_LEVEL',align:'center'}
	      ];
//初始化    
function doInit(){
	__extQuery__(1);
}
function isSave(){
    if(submitForm('fm')){
	    MyConfirm("是否确认保存信息?",importSave);
    }
 }
function importSave() {
	if(submitForm('fm')){
			fm.action = "<%=contextPath %>/sysmng/dealer/ShDealerImport/importExcel.do";
			fm.submit();
		}
}
</script>
</body>
</html>
