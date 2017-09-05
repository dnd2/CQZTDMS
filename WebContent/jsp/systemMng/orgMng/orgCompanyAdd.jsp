<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@page import="com.infodms.dms.bean.CompanyBean"%>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ page import="java.util.*" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${path}/common/jsp_head_new.jsp" />
<title>公司维护</title>
<style>.table_info td{height:40px}</style>
</head>

<body onload="genLocSel('txt1','txt2','','','');"> <!-- onunload='javascript:destoryPrototype()' loadcalendar(); -->
<div class="wbox">
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：系统管理 &gt; 组织管理 &gt; 新增车厂公司
	</div>
	<form id="fm">
		<div class="form-panel">
			<h2>基本信息</h2>
			<div class="form-body">
				<table class="table_query" width="100%">
					<tr>
						<td class="table_info_3col_label_5Letter right" nowrap="nowrap">公司代码：</td>
						<td class="table_info_3col_input"  nowrap="nowrap">
							<input type="text" class="middle_txt" name="COMPANY_CODE" id="COMPANY_CODE" value="" datatype="0,is_null,<%=Constant.Length_Check_Char_8 %>"/>
						</td>
						<td class="table_info_3col_label_5Letter right" nowrap="nowrap">公司名称：</td>
						<td class="table_info_3col_input"  nowrap="nowrap">
							<input name="COMPANY_NAME"  type="text" class="long_txt middle_txt" id="COMPANY_NAME" datatype="0,is_null,<%=Constant.Length_Check_Char_100 %>" value=""/>
						</td>	
						<td class="table_info_2col_label_5Letter right" nowrap="nowrap">公司简称：</td>
						<td class="table_info_3col_input"  nowrap="nowrap" >
							<input name="COMPANY_SHORTNAME"  type="text" class="middle_txt" id="COMPANY_SHORTNAME" datatype="0,is_null,<%=Constant.Length_Check_Char_50 %>" value=""/>
						</td>
					</tr>
					<tr>
						<td class="table_info_3col_label_5Letter right" nowrap="nowrap">省份：</td>
						<td class="table_info_3col_input"  nowrap="nowrap">
							<select class="min_sel u-select" id="txt1" name="PROVINCE_ID"  onchange="_genCity(this,'txt2')"></select>
						</td>
						<td class="table_info_2col_label_5Letter right" nowrap="nowrap">城市：</td>
						<td class="table_info_3col_input"  nowrap="nowrap" >
							<select class="min_sel u-select" name="CITY_ID" id="txt2"></select>
						</td>
						<td class="table_info_2col_label_5Letter right" nowrap="nowrap">联系电话：</td>
						<td class="table_info_3col_input"  nowrap="nowrap" >
							<input name="PHONE"  type="text" class="middle_txt" ID="PHONE" datatype="1,is_phone,19" maxlength="19" value=""/>
						</td>
					</tr>
					<tr>	
						<td class="table_info_3col_label_5Letter right" nowrap="nowrap">邮编：</td>
						<td class="table_info_3col_input"  nowrap="nowrap">
							<input name="ZIP_CODE" maxlength="6" type="text" class="middle_txt" id="ZIP_CODE" datatype="1,is_digit,<%=Constant.Length_Check_Char_6 %>" value=""/>
						</td>
						<td class="table_info_2col_label_5Letter right" nowrap="nowrap">传真：</td>
						<td class="table_info_3col_input"  nowrap="nowrap">
							<input name="FAX"  type="text" class="middle_txt" id="FAX" datatype="1,is_phone,19" maxlength="19" value=""/>
						</td>
						<td class="table_info_2col_label_5Letter right" nowrap="nowrap">状态：</td>
						<td class="table_info_3col_input"  nowrap="nowrap">
							<script type="text/javascript"> genSelBox("STATUS",<%=Constant.STATUS%>,"",false,"min_sel u-select","");</script><font color="red">&nbsp;*</font>
						</td>
					</tr>
					<tr>
						<td class="table_info_2col_label_5Letter right" nowrap="nowrap">详细地址：</td>
						<td class="table_info_3col_input"  nowrap="nowrap">
							<input name="ADDRESS"  type="text" class="middle_txt" id="ADDRESS" datatype="1,is_null,<%=Constant.Length_Check_Char_100 %>" value=""/>
						</td>
						<td></td>
						<td colspan="3"></td>
					</tr>
				</table>
				</div>
				</div>
				<%-- <div class="form-panel">
			<h2>组织信息</h2>
			<div class="form-body">
				<table class="table_info" width="100%">	
					<tr>
						<td class="table_info_3col_label_5Letter right" nowrap="nowrap" >组织代码：</td>
						<td class="table_info_3col_input"  nowrap="nowrap">
							<input type="text" class="middle_txt"  name="ORG_CODE" id="ORG_CODE" value="" datatype="0,is_null,<%=Constant.Length_Check_Char_30 %>"/>
						</td>
						<td class="table_info_3col_label_5Letter right" nowrap="nowrap">组织名称：</td>
						<td class="table_info_3col_input"  nowrap="nowrap">
							<input name="ORG_NAME"  type="text" class="long_txt middle_txt" id="ORG_NAME" datatype="0,is_null,<%=Constant.Length_Check_Char_30 %>" value=""/>
						</td>	
						<td></td>
					</tr>
				</table>
				</div>
				</div> --%>
				<table class="table_info" width="100%">	
					<tr>
						<td colspan="6" align="center">
							<input class="u-button" type="button" value="确 定" onclick="confirmUpdate();" name="addBtn" id="addBtn"/>
							&nbsp;&nbsp;
							<input class="u-button u-cancel" type="button" value="返 回" onclick="goBack();"/>		
						</td>	
					</tr>
				</table>
		<br/>
		<!-- 
		<table class="table_info">	
			<tr><th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;初始用户信息</th></tr>
			<tr>
				<td class="table_info_3col_label_6Letter" nowrap="nowrap" >管理员用户名：</td>
				<td class="table_info_3col_input"  nowrap="nowrap">
					<input type="text" class="middle_txt"  name="USER_NAME" id="USER_NAME" value="" datatype="0,is_null,<%=Constant.Length_Check_Char_30 %>"/>
				</td>
			</tr>
			<tr>
				<td class="table_info_3col_label_5Letter" nowrap="nowrap" >设置密码：</td>
				<td class="table_info_3col_input"  nowrap="nowrap">
					<input type="password" id="PASSWORD" name="PASSWORD" class="middle_txt"  style="width:406px;"  datatype="1,is_null,<%=Constant.Length_Check_Char_30 %>" value=""/>
				</td>	
			</tr>
		</table>
		 -->
	</form>
	<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</div>
<script type="text/javascript">
	function confirmUpdate(){
		if(submitForm('fm'))
		{
			MyConfirm("确定新增车厂公司信息吗？", doit) ;
		}
	}
	
	function doit() {
		sendAjax('<%=request.getContextPath()%>/sysmng/orgmng/SgmOrgMng/addOemCompanyInfo.json',showResult,'fm');
	}
	function showResult(json){
		if(json.ACTION_RESULT == '1'){
			goBack();
		}else if(json.ACTION_RESULT == '2'){
			MyAlert("新增失败！请联系系统管理员！");
		}
	}
	function goBack(){
		window.location.href = "<%=contextPath%>/sysmng/orgmng/SgmOrgMng/orgCompanyInit.do";
	}
</script>
</body>
</html>
