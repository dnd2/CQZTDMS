<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.TmBrandPO" %>
<%@page import="com.infodms.dms.bean.CompanyBean"%>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ page import="java.util.*" %>
<%
	String contextPath = request.getContextPath();
	CompanyBean companyBean = (CompanyBean)request.getAttribute("COMPANY_BEAN");
	List sgmlist = null;
	List dlrsgmlist = null;
	if(request.getAttribute("DLR_SGM_LIST") != null){
		dlrsgmlist = (LinkedList)request.getAttribute("DLR_SGM_LIST");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<script type="text/javascript" src="<%=FileConstant.regionJsUrl%>"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
<jsp:include page="${path}/common/jsp_head_new.jsp" />
<title>公司维护</title>
</head>

<body onunload='javascript:destoryPrototype()' onload="loadcalendar();genLocSel('txt1','txt2','','<%=companyBean.getProvinceId()!=null? companyBean.getProvinceId():""%>','<%=companyBean.getCityId()!=null? companyBean.getCityId():""%>');">
<div class="wbox">
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：系统管理 &gt; 组织管理 &gt; 公司维护
	</div>
	<form id="fm">
		<input name="D_COMPANY_ID" type="hidden" value="<%=companyBean.getCompanyId()!=null? companyBean.getCompanyId():""%>"/>
		<input name="D_COMPANY_CODE" type="hidden" value="<%=companyBean.getCompanyCode()!=null? companyBean.getCompanyCode():""%>"/>
		<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
		<table class="table_info">
			<tr><th colspan="2"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;公司信息</th></tr>
			<tr>
				<td class="table_info_3col_label_5Letter" nowrap="nowrap" >车厂公司：</td>
				<td class="table_info_3col_input"  nowrap="nowrap">
					<%=companyBean.getOemCompanyShortname()!=null? companyBean.getOemCompanyShortname():""%>
				</td>
			</tr>
			<tr>
				<td class="table_info_3col_label_5Letter" nowrap="nowrap" >公司代码：</td>
				<td class="table_info_3col_input"  nowrap="nowrap">
					<input type="text" class="middle_txt" readonly id="COMPANY_CODE" value="<%=companyBean.getCompanyCode()!=null? companyBean.getCompanyCode():""%>" disabled/>
				</td>
			</tr>
			<tr>
				<td class="table_info_3col_label_5Letter" nowrap="nowrap" >公司名称：</td>
				<td class="table_info_3col_input"  nowrap="nowrap">
					<input name="D_COMPANY_NAME"  type="text" class="long_txt"  style="width:406px;" id="D_COMPANY_NAME" datatype="0,is_null,<%=Constant.Length_Check_Char_30 %>" value="<%=companyBean.getCompanyName()!=null? companyBean.getCompanyName():""%>"/>
				</td>	
			</tr>
			<tr>
				<td class="table_info_2col_label_5Letter" nowrap="nowrap">公司简称：</td>
				<td class="table_info_3col_input"  nowrap="nowrap" >
					<input name="COMPANY_SHORTNAME"  type="text" class="middle_txt" id="COMPANY_SHORTNAME" datatype="0,is_null,<%=Constant.Length_Check_Char_30 %>" value="<%=companyBean.getCompanyShortname()!=null? companyBean.getCompanyShortname():""%>"/>
				</td>
			</tr>
			<tr>
			<td class="table_info_3col_label_5Letter" nowrap="nowrap">公司类型：
				</td>
				<td class="table_info_3col_input"  nowrap="nowrap">
				<script>document.write(getItemValue(<%=companyBean.getCompanyType()%>));</script>
				    <input type="hidden" name="COMPANY_TYPE" value="<%=companyBean.getCompanyType()%>"/>
				</td>
			</tr>
			<tr>
				<td class="table_info_3col_label_5Letter" nowrap="nowrap" >省份：</td>
				<td class="table_info_3col_input"  nowrap="nowrap">
					<select class="min_sel" id="txt1" name="PROVINCE_ID"  onchange="_genCity(this,'txt2')"></select>
				</td>
				</tr>
			<tr>
				<td class="table_info_2col_label_5Letter" nowrap="nowrap" >城市：</td>
				<td class="table_info_3col_input"  nowrap="nowrap" >
					<select class="min_sel" name="CITY_ID" id="txt2"></select>&nbsp;<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td class="table_info_2col_label_5Letter" nowrap="nowrap">联系电话：</td>
				<td class="table_info_3col_input"  nowrap="nowrap" >
					<input name="PHONE"  type="text" class="middle_txt" ID="PHONE" datatype="0,is_phone,19" maxlength="19" value="<%=companyBean.getPhone()!=null? companyBean.getPhone():""%>"/>
				</td>
				</tr>
					<tr>
				
				<td class="table_info_3col_label_5Letter" nowrap="nowrap" >邮编：</td>
				<td class="table_info_3col_input"  nowrap="nowrap">
					<input name="ZIP_CODE" maxlength="6" type="text" class="middle_txt" id="ZIP_CODE" datatype="1,is_digit,<%=Constant.Length_Check_Char_6 %>" value="<%=companyBean.getZipCode()!=null? companyBean.getZipCode():""%>"/>
				</td>
				</tr>
			<tr>
				<td class="table_info_2col_label_5Letter" nowrap="nowrap" >传真：</td>
				<td class="table_info_3col_input"  nowrap="nowrap">
					<input name="FAX"  type="text" class="middle_txt" id="FAX" datatype="1,is_phone,19" maxlength="19" value="<%=companyBean.getFax()!=null? companyBean.getFax():""%>"/>
				</td>
			</tr>
		
			
			<tr>
				<td class="table_info_2col_label_5Letter" nowrap="nowrap">详细地址：</td>
				<td class="table_info_3col_input"  nowrap="nowrap" colspan="3">
					<input name="ADDRESS"  type="text" class="middle_txt" style="width:406px;" id="ADDRESS" datatype="1,is_null,<%=Constant.Length_Check_Char_30 %>" value="<%=companyBean.getAddress()!=null? companyBean.getAddress():""%>"/>
				</td>
			</tr>
			<tr>
				<td class="table_info_2col_label_5Letter" nowrap="nowrap">状态：</td>
				<td class="table_info_3col_input"  nowrap="nowrap">
					<script type="text/javascript"> genSelBox("STATUS",<%=Constant.STATUS%>,"<%=companyBean.getStatus()!=null? companyBean.getStatus():""%>",false,"min_sel","");</script><font color="red">&nbsp;*</font>
				</td>
			</tr>
			<!-- add by xieyj -->
			<tr>
				<td class="table_info_3col_label_6Letter" nowrap="nowrap">是否同一法人 ：
				</td>
				<td class="table_info_3col_input"  nowrap="nowrap">
					<script type="text/javascript"> genSelBox("IS_SAME_PERSON",<%=Constant.IF_TYPE%>,"<%=companyBean.getIsSamePerson()!=null? companyBean.getIsSamePerson():""%>",true,"min_sel","");</script>
		    	</td>
		    </tr>
		    <tr>
				<td class="table_info_3col_label_6Letter" nowrap="nowrap">是否前店后厂 ：
				</td>
				<td class="table_info_3col_input"  nowrap="nowrap">
					<script type="text/javascript"> genSelBox("IS_BEFORE_AFTER",<%=Constant.IF_TYPE%>,"<%=companyBean.getIsBeforeAfter()!=null? companyBean.getIsBeforeAfter():""%>",true,"min_sel","");</script>
		    	</td>
		    </tr>
		    <tr>
				<td class="table_info_3col_label_6Letter" nowrap="nowrap">是否手拉手 ：
				</td>
				<td class="table_info_3col_input"  nowrap="nowrap">
					<script type="text/javascript"> genSelBox("HANDLE_BY_HANDLE",<%=Constant.IF_TYPE%>,"<%=companyBean.getHandleByHandle()!=null? companyBean.getHandleByHandle():""%>",true,"min_sel","");</script>
		    	</td>
		    </tr>
		    <tr>
				<td class="table_query_2Col_label_6Letter">关联公司：</td>
			    <td>
			    		<input id="DEALER_NAME" name="DEALER_NAME" type="hidden"/>
			    		 <input id="SHORT_NAME" name="SHORT_NAME" type="hidden"/>
			    		 <input id="DEALER_CODE" name="DEALER_CODE" type="hidden"/>
			    		 <input id="COMPANY_ID" name="COMPANY_ID" type="hidden" value="${po.companyId }"/>
					   <input class="middle_txt"  datatype="1,is_null,75"  id="COMPANY_NAME" style="cursor: pointer;" name="COMPANY_NAME" type="text" readonly="readonly" value="${po.companyName }"/>
					   <input class="mark_btn" type="button" value="&hellip;" onclick="showCompanyA() ;"/><input class="cssbutton" type="button" value="清空" onclick="clrTxt('COMPANY_NAME');clrTxt('COMPANY_ID');"/>

			    </td>
		    </tr>
			<!-- end -->
			<tr>
				<td colspan="6" align="center">
					<input class="normal_btn" type="button" value="确 定" onclick="confirmUpdate();" name="addBtn" id="addBtn"/>
					<input class="normal_btn" type="button" value="返 回" onclick="goBack();"/>		</td>	
			</tr>
		</table>

	</form>
	<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</div>
<script type="text/javascript">
	validateConfig.isOnBlur = false;
	
	var dept_tree_url = "<%=contextPath%>/sysmng/usemng/SgmSysUser/initOrgTree.json";
	function confirmUpdate(){
		if(submitForm('fm')){
			var sCity = document.getElementById("txt2").value ;
			
			if(!sCity) {
				MyAlert("请选择城市!") ;
				
				return false ;
			}
			
			sendAjax('<%=request.getContextPath()%>/sysmng/orgmng/DlrInfoMng/updateCompanyInfo.json',showResult,'fm');
		}
	}
	function showResult(json){
		if(json.ACTION_RESULT == '1'){
			goBack();
		//	MyConfirm("修改成功！点击确认返回查询界面或者点击左边菜单进入其他功能！","window.location.href = '<%=request.getContextPath()%>/sysmng/orgmng/DlrInfoMng/queryAllDlrInfo.do'");
		}else if(json.ACTION_RESULT == '2'){
			MyAlert("修改失败！请确认修改后的经销商信息（经销商名称、简称与SAP帐号）是否已经存在！");
		}
	}
	function goBack(){
		window.location.href = "<%=contextPath%>/sysmng/orgmng/DlrInfoMng/queryAllDlrInfo.do";
	}
	
	function showCompanyA(){ 
		OpenHtmlWindow('<%=contextPath%>/common/OrgMng/queryCompanyA.do?command=1',800,450);
	}
	
	function clrTxt(value){
		document.getElementById(value).value = "";
	}
</script>
</body>
</html>
