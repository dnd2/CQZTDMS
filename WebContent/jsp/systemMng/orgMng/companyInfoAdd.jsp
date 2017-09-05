<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.TmBrandPO" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ page import="java.util.*" %> 
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	List list = null;
	if(request.getAttribute("list") != null){
		list = (LinkedList)request.getAttribute("list");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!-- <link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" /> -->
<title>公司维护</title>
<style>
	.img{ border:none}
	.table_info_input_4col {
			width: 150px;
			text-align: right;
		}
	.table_info_input {
			width: 350px;
			text-align: left;
		}
</style>
<script type="text/javascript">
var dept_tree_url = "<%=contextPath%>/sysmng/usemng/SgmSysUser/initOrgTree.json";
</script>
</head>

<body onload="genLocSel('txt1','txt2', '' ,'${cb.provinceId}','${cb.cityId }');"> <!-- onunload='javascript:destoryPrototype()' loadcalendar(); -->
<div class="wbox">
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />当前位置： 系统管理 &gt; 组织管理 &gt; 公司维护
	</div>
	<form id="fm" name="fm">
		<div class="form-panel">
			<h2><img class="panel-icon nav" src="<%=contextPath %>/img/subNav.gif" />公司信息</h2>
			<div class="form-body">
				<table class="table_query" border="0">
					<tr>
						<th colspan="5"></th>
					</tr>
					<c:choose>
						<c:when test="${cb != null }">
							<tr>
								<td class="table_info_input_4col right" nowrap="nowrap" >公司代码：</td>
								<td class="table_info_input">
									<input type="hidden" name="COMPANY_ID" id="COMPANY_ID" value="${cb.companyId }"/>
									<input class="middle_txt"  type="text" name="COMPANY_CODE" id="COMPANY_CODE" datatype="0,is_docNumber,12" style="background: rgb(204, 204, 204);" value="${cb.companyCode }"  readonly="readonly"/>
								</td>
								<td class="table_info_input_4col right" nowrap="nowrap">公司简称：</td>
								<td class="table_info_input"  nowrap="nowrap" >
									<input class="middle_txt"  type="text" name="COMPANY_SHORTNAME" id="COMPANY_SHORTNAME" datatype="0,is_null,30"  value="${cb.companyShortname }" />
								</td>
								<td  nowrap="nowrap" >&nbsp;</td>
							</tr>
							<tr>
								<td class="table_info_input_4col right" nowrap="nowrap" >公司名称：</td>
								<td class="table_info_input" >
									<input class="middle_txt"  type="text" name="COMPANY_NAME" id="COMPANY_NAME" datatype="0,is_null,50" style="width:350px; " value="${cb.companyName }"  />
								</td>
								<td class="table_info_input_4col right" nowrap="nowrap">公司状态：</td>
						<td class="table_info_input"  >
							<script type="text/javascript"> genSelBox("STATUS",<%=Constant.STATUS%>,"${cb.status}",false,"min_sel u-select","onchange='setStatus(this.value)'");</script><font color="red">&nbsp;*</font>
						</td>
							</tr>
						</c:when> 
						<c:otherwise>
							<tr>
								<td class="table_info_input_4col right" nowrap="nowrap" >公司代码：</td>
								<td class="table_info_input">
									<input type="hidden" name="COMPANY_ID" id="COMPANY_ID" value="${cb.companyId }"/>
									<input class="middle_txt"  type="text" name="COMPANY_CODE" id="COMPANY_CODE" datatype="0,is_docNumber,12" value="${cb.companyCode }"  />
								</td>
								<td class="table_info_input_4col right" nowrap="nowrap">公司简称：</td>
								<td class="table_info_input"  nowrap="nowrap" >
									<input class="middle_txt"  type="text" name="COMPANY_SHORTNAME" id="COMPANY_SHORTNAME" datatype="0,is_null,30" value="${cb.companyShortname }" />
								</td>
								<td  nowrap="nowrap" >&nbsp;</td>
							</tr>
							<tr>
								<td class="table_info_input_4col right" nowrap="nowrap" >公司名称：</td>
								<td class="table_info_input" >
									<input class="middle_txt"  type="text" name="COMPANY_NAME" id="COMPANY_NAME" datatype="0,is_null,50" style="width:350px;" value="${cb.companyName }" />
								</td>
								<td class="table_info_input_4col right" nowrap="nowrap">公司状态：</td>
						<td class="table_info_input"  >
							<script type="text/javascript"> genSelBox("STATUS",<%=Constant.STATUS%>,"${cb.status}",false,"min_sel u-select","onchange='setStatus(this.value)'");</script><font color="red">&nbsp;*</font>
						</td>
							</tr>
						</c:otherwise>	
					</c:choose>
					<%-- <c:if test="${cbS1!='noDate'}">
					<tr>
						<td class="table_info_input_4col right" nowrap="nowrap">销售经销商状态：</td>
						<td class="table_info_input"  nowrap="nowrap" colspan="4">
							<script type="text/javascript"> genSelBox("xiaoshou_STATUS",<%=Constant.STATUS%>,"${cbS}",false,"min_sel u-select","onchange='setStatus1()'");</script><font color="red">&nbsp;*</font>
						</td>
					</tr></c:if>
					<c:if test="${cbF1!='noDate'}">
					<tr>
						<td class="table_info_input_4col right" nowrap="nowrap">售后经销商状态：</td>
						<td class="table_info_input"  nowrap="nowrap" colspan="4">
							<script type="text/javascript"> genSelBox("shouhou_STATUS",<%=Constant.STATUS%>,"${cbF}",false,"min_sel u-select","onchange='setStatus1()'");</script><font color="red">&nbsp;*</font>
						</td>
					</tr></c:if> --%>
					<tr>
						<td class="table_info_3col_label_5Letter right" nowrap="nowrap">省份：</td>
						<td class="table_info_3col_input"  nowrap="nowrap">
							<select class="min_sel u-select" id="txt1" name="PROVINCE_ID"  onchange="_genCity(this,'txt2')"></select>
						</td>
						<td class="table_info_2col_label_5Letter right" nowrap="nowrap">城市：</td>
						<td class="table_info_3col_input"  nowrap="nowrap" >
							<select class="min_sel u-select" name="CITY_ID" id="txt2"></select>
						</td>
					</tr>
					<tr>	
						<td class="table_info_2col_label_5Letter right" nowrap="nowrap">联系电话：</td>
						<td class="table_info_3col_input"  nowrap="nowrap" >
							<input name="PHONE"  type="text" class="middle_txt" ID="PHONE" datatype="1,is_phone,19" maxlength="19" value="${cb.phone }" />
						</td>
						<td class="table_info_3col_label_5Letter right" nowrap="nowrap">邮编：</td>
						<td class="table_info_3col_input"  nowrap="nowrap">
							<input name="ZIP_CODE" maxlength="6" type="text" class="middle_txt" id="ZIP_CODE" datatype="1,is_digit,6" value="${cb.zipCode }" />
						</td>
					</tr>
					<tr>
						<td class="table_info_2col_label_5Letter right" nowrap="nowrap">传真：</td>
						<td class="table_info_3col_input"  nowrap="nowrap">
							<input name="FAX"  type="text" class="middle_txt" id="FAX" datatype="1,is_phone,19" maxlength="19"  value="${cb.fax }" />
						</td>
						<td class="table_info_2col_label_5Letter right" nowrap="nowrap"></td>
						<td class="table_info_3col_input"  nowrap="nowrap">
						</td>
					</tr>
					<tr>
						<td class="table_info_2col_label_5Letter right" nowrap="nowrap">详细地址：</td>
						<td class="table_info_3col_input"  nowrap="nowrap">
							<input name="ADDRESS"  type="text" class="middle_txt" id="ADDRESS" datatype="1,is_null,100" value="${cb.address }" />
						</td>
						<td></td>
						<td ></td>
					</tr>
					<tr>
						<th colspan="4" style="height: 30px; text-align: center;">
							<input class="u-button" type="button" value="保存" onclick="confirmAdd()"/>
							<input class="u-button u-cancel" type="button" value="取消" onclick="goBack()"/>
						</th>
					</tr>
				</table>
			</div>
		</div>
    	
    </form>
</div>
<script type="text/javascript">
	function confirmAdd(){
		if(submitForm('fm')){
			sendAjax('<%=request.getContextPath()%>/sysmng/orgmng/DlrInfoMng/addCompanyInfo.json',showResult,'fm');
		}
	}
	function showResult(json){
		if(json.Exception) {
			MyAlert(json.Exception.message);
		} else {
			MyAlertForFun(json.message, goBack);
		}
	}
	
	function goBack(){
		_hide();
		window.location.href = "<%=contextPath%>/sysmng/orgmng/DlrInfoMng/queryAllDlrInfo.do";
	}
	
	function clrTxt(value){
		document.getElementById(value).value = "";
	}
	function doInit(){
		if(document.getElementById("STATUS").options[1].selected==true){
			document.getElementById("xiaoshou_STATUS").disabled=true;
			document.getElementById("shouhou_STATUS").disabled=true;
		}
		//setStatus1();
		if(document.getElementById("xiaoshou_STATUS").options[1].selected==true&&
				document.getElementById("shouhou_STATUS").options[1].selected==true&&
				document.getElementById("STATUS").options[1].selected==true){
			document.getElementById("STATUS").disabled=false;
		}
	}
	function setStatus(_this){
		
		if(document.getElementById("shouhou_STATUS")==null){
			if(_this=="10011001"){
				document.getElementById("xiaoshou_STATUS").options[0].selected=true;
				
				document.getElementById("xiaoshou_STATUS").disabled=false;
			}else{
				document.getElementById("xiaoshou_STATUS").options[1].selected=true;
				
				document.getElementById("xiaoshou_STATUS").disabled=true;
			}
		}
		
		if(document.getElementById("xiaoshou_STATUS")==null){
			if(_this=="10011001"){
				document.getElementById("shouhou_STATUS").options[0].selected=true;
				
				document.getElementById("shouhou_STATUS").disabled=false;
			}else{
				document.getElementById("shouhou_STATUS").options[1].selected=true;
				
				document.getElementById("shouhou_STATUS").disabled=true;
			}
		}
		
		if(_this=="10011001"){
			document.getElementById("xiaoshou_STATUS").options[0].selected=true;
			document.getElementById("shouhou_STATUS").options[0].selected=true;
			
			document.getElementById("xiaoshou_STATUS").disabled=false;
			document.getElementById("shouhou_STATUS").disabled=false;
		}else{
			document.getElementById("xiaoshou_STATUS").options[1].selected=true;
			document.getElementById("shouhou_STATUS").options[1].selected=true;
			
			document.getElementById("xiaoshou_STATUS").disabled=true;
			document.getElementById("shouhou_STATUS").disabled=true;
		}
		
	}
	function setStatus1(){
		if(document.getElementById("shouhou_STATUS")==null){
			if(document.getElementById("xiaoshou_STATUS").options[1].selected==true){
				document.getElementById("STATUS").disabled=true;
				document.getElementById("STATUS").options[1].selected=true;
			}else{
				document.getElementById("STATUS").disabled=false;
				document.getElementById("STATUS").options[0].selected=true;
			}
		}
		
		if(document.getElementById("xiaoshou_STATUS")==null){
			if(document.getElementById("shouhou_STATUS").options[1].selected==true){
				document.getElementById("STATUS").disabled=true;
				document.getElementById("STATUS").options[1].selected=true;
			}else{
				document.getElementById("STATUS").disabled=false;
				document.getElementById("STATUS").options[0].selected=true;
			}
		}
		
		if(document.getElementById("xiaoshou_STATUS").options[1].selected==true&&
				document.getElementById("shouhou_STATUS").options[1].selected==true){
			document.getElementById("STATUS").disabled=true;
			document.getElementById("STATUS").options[1].selected=true;
		}else{
			document.getElementById("STATUS").disabled=false;
			document.getElementById("STATUS").options[0].selected=true;
		}
		
	}
</script>
</body>
</html>
