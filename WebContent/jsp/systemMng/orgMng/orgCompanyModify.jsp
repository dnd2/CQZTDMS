<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@page import="com.infodms.dms.bean.CompanyBean"%>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ page import="java.util.*" %>
<%
	String contextPath = request.getContextPath();
	CompanyBean companyBean = (CompanyBean)request.getAttribute("COMPANY_BEAN");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${path}/common/jsp_head_new.jsp" />
<title>公司维护</title>
</head>

<body onload="genLocSel('txt1','txt2','','<%=companyBean.getProvinceId()!=null? companyBean.getProvinceId():""%>','<%=companyBean.getCityId()!=null? companyBean.getCityId():""%>');">
<div class="wbox">
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：系统管理 &gt; 组织管理 &gt; 公司维护
	</div>
	<form id="fm">
		<input name="COMPANY_ID" type="hidden" value="<%=companyBean.getCompanyId()!=null? companyBean.getCompanyId():""%>"/>
		<input name="COMPANY_CODE" type="hidden" value="<%=companyBean.getCompanyCode()!=null? companyBean.getCompanyCode():""%>"/>
		<div class="form-panel">
			<h2>基本信息</h2>
			<div class="form-body">
		<table class="table_query">
			<tr>
				<td class="right" nowrap="nowrap" >公司代码：</td>
				<td class="table_info_3col_input"  nowrap="nowrap">
					<input type="text" class="middle_txt" readonly id="COMPANY_CODE" value="<%=companyBean.getCompanyCode()!=null? companyBean.getCompanyCode():""%>" disabled/>
				</td>
				<td class="right" nowrap="nowrap" >公司名称：</td>
				<td class="table_info_3col_input"  nowrap="nowrap">
					<input name="COMPANY_NAME"  type="text" class="middle_txt"  style="width:300px;" id="COMPANY_NAME" datatype="0,is_null,<%=Constant.Length_Check_Char_100 %>" value="<%=companyBean.getCompanyName()!=null? companyBean.getCompanyName():""%>"/>
				</td>	
				<td class="right" nowrap="nowrap">公司简称：</td>
				<td class="table_info_3col_input"  nowrap="nowrap" >
					<input name="COMPANY_SHORTNAME"  type="text" class="middle_txt" id="COMPANY_SHORTNAME" datatype="0,is_null,<%=Constant.Length_Check_Char_50 %>" value="<%=companyBean.getCompanyShortname()!=null? companyBean.getCompanyShortname():""%>"/>
				</td>
			</tr>
			<tr>
				<td class="right" nowrap="nowrap" >省份：</td>
				<td class="table_info_3col_input"  nowrap="nowrap">
					<select class="u-select" id="txt1"  name="PROVINCE_ID"  onchange="_genCity(this,'txt2')"></select>
				</td>
				<td class="right" nowrap="nowrap" >城市：</td>
				<td class="table_info_3col_input"  nowrap="nowrap" >
					<select class="u-select"  name="CITY_ID" id="txt2"></select>
				</td>
				<td class="right" nowrap="nowrap">联系电话：</td>
				<td class="table_info_3col_input"  nowrap="nowrap" >
					<input name="PHONE"  type="text" class="middle_txt" ID="PHONE" datatype="1,is_phone,19" maxlength="19" value="<%=companyBean.getPhone()!=null? companyBean.getPhone():""%>"/>
				</td>
				</tr>
					<tr>
				
				<td class="right" nowrap="nowrap" >邮编：</td>
				<td class="table_info_3col_input"  nowrap="nowrap">
					<input name="ZIP_CODE" maxlength="6" type="text" class="middle_txt" id="ZIP_CODE" datatype="1,is_digit,<%=Constant.Length_Check_Char_6 %>" value="<%=companyBean.getZipCode()!=null? companyBean.getZipCode():""%>"/>
				</td>
				<td class="right" nowrap="nowrap" >传真：</td>
				<td class="table_info_3col_input"  nowrap="nowrap">
					<input name="FAX"  type="text" class="middle_txt" id="FAX" datatype="1,is_phone,19" maxlength="19" value="<%=companyBean.getFax()!=null? companyBean.getFax():""%>"/>
				</td>
				<td class="right" nowrap="nowrap">状态：</td>
				<td class="table_info_3col_input"  nowrap="nowrap">
					<script type="text/javascript"> genSelBox("STATUS",<%=Constant.STATUS%>,"<%=companyBean.getStatus()!=null? companyBean.getStatus():""%>",false,"u-select","");</script><font color="red">&nbsp;*</font>
				</td>
				</tr>
		
			
			<tr>
				<td class="right" nowrap="nowrap">详细地址：</td>
				<td class="table_info_3col_input"  nowrap="nowrap" colspan="3">
					<input name="ADDRESS"  type="text" class="middle_txt" style="width:406px;" id="ADDRESS" datatype="1,is_null,<%=Constant.Length_Check_Char_50 %>" value="<%=companyBean.getAddress()!=null? companyBean.getAddress():""%>"/>
				</td>
			</tr>
			<tr>
				<td colspan="6" class="center">
					<input class="normal_btn" type="button" value="确 定" onclick="confirmUpdate();" name="addBtn" id="addBtn"/>
					<input class="normal_btn" type="button" value="返 回" onclick="goBack();"/>		</td>	
			</tr>
		</table>
</div>
</div>
	</form>
	<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</div>
<script type="text/javascript">
	function confirmUpdate(){
		if(submitForm('fm'))
		{
			MyConfirm("确定修改车厂公司信息吗？", doit) ;
		}
	}
	function doit() {
		sendAjax('<%=request.getContextPath()%>/sysmng/orgmng/SgmOrgMng/updateOemCompanyInfo.json',showResult,'fm');
	}
	function showResult(json){
		if(json.ACTION_RESULT == '1'){
			goBack();
		}else if(json.ACTION_RESULT == '2'){
			MyAlert("修改失败！请确认修改后的车厂公司信息是否已经存在！");
		}
	}
	function goBack(){
		window.location.href = "<%=contextPath%>/sysmng/orgmng/SgmOrgMng/orgCompanyInit.do";
	}
</script>
</body>
</html>
