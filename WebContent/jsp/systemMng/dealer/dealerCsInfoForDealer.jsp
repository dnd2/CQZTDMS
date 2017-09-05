<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商维护</title>
<script type="text/javascript">
var dealerLevel=<%=Constant.DEALER_LEVEL_01%>;
var dealerT=<%=Constant.DEALER_TYPE_DWR%>;

window.onload=function(){
	//genLocSel('PROVINCE_ID','CITY_ID','COUNTIES');
	loadcalendar();
}

function showCompanyA(path){ 
	OpenHtmlWindow(path+'/common/OrgMng/queryCompanyA.do',800,450);
}

function changeDealerlevel(){
	var dealerLevel = document.getElementById("DEALER_LEVEL").value;
   	if(dealerLevel == '<%=Constant.DEALER_LEVEL_01%>')
	{
		document.getElementById("dealerBtn").disabled = true;
	}
	else
	{
		document.getElementById("dealerBtn").disabled = false;
	}
}

function setItemValue(selectName, objItemValue) {   

	var objSelect = document.getElementById(selectName);
	if(!objSelect) {return;}
	if(!objItemValue || objItemValue == '-1' || objItemValue == '') {return;}
	
    for (var i = 0; i < objSelect.options.length; i++) {        
        if (objSelect.options[i].value == objItemValue) {        
        	objSelect.options[i].selected = true;       
            break;        
        }        
    }        
} 
function clrTxt(PROXY_AREA,PROXY_AREA_NAME)
{
	document.getElementById(PROXY_AREA).value = "";
	document.getElementById(PROXY_AREA_NAME).value = "";
}
function showDisId(disIds,codes){
	var old_code = document.getElementById("PROXY_AREA_NAME").value;
	if(old_code==''){
		document.getElementById("PROXY_AREA_NAME").value = codes;
	}else{
			var ids = old_code +","+ codes;
	        var s = ids.split(',');
	        var dic = {};
	        for (var i = s.length; i--; ) {
	            dic[s[i]]=s[i];
	        }
	        var r = [];
	        for (var v in dic) {
	       	 	r.push(dic[v]);
	        }
		
		document.getElementById("PROXY_AREA_NAME").value = r;
	}
	
	var old_id = document.getElementById("PROXY_AREA").value;
	if(old_id==''){
		document.getElementById("PROXY_AREA").value = disIds;
	}else{
		var code = old_id +","+ disIds;
        var s = code.split(',');
        var dic = {};
        for (var i = s.length; i--; ) {
            dic[s[i]]=s[i];
        }
        var r = [];
        for (var v in dic) {
       	 	r.push(dic[v]);
        }
		document.getElementById("PROXY_AREA").value =r ;
	}
}
function doInit() {   
	var CITY_ID = '${CITY_ID}';
	if(document.getElementById("PROXY_AREA").value==''){
		document.getElementById("PROXY_AREA").value = CITY_ID;
	}
	var CITY_NAME = '${CITY_NAME}';
	if(document.getElementById("PROXY_AREA_NAME").value==''){
		document.getElementById("PROXY_AREA_NAME").value = CITY_NAME;
	}
	document.getElementById("PROXY_AREA_DEF").value = CITY_ID;
	document.getElementById("PROXY_AREA_NAME_DEF").value = CITY_NAME;
	var orgId = document.getElementById("orgId");
	var ROOT_ORG_ID = document.getElementById("ROOT_ORG_ID").value;
	for (var int = 0; int < orgId.length; int++) {
		if(orgId[int].value==ROOT_ORG_ID){
			orgId[int].selected=true;
			break;
		}
	}
} 
</script>
</head>
<body onunload="javascript:destoryPrototype()">
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;
		服务站备案信息变更申请
<%-- 		<c:if test="${is_Mod == true}">修改经销商</c:if> --%>
	</div>
	<form method="post" name="fm" id="fm">
		<input id="COMPANY_ID" name="COMPANY_ID" type="hidden" value="${dMap.COMPANY_ID }" /> 
		<input id="CHANGE_AUTID_STATUS" name="CHANGE_AUTID_STATUS" type="hidden" value="${dMap.CHANGE_AUTID_STATUS }" /> 
		<input id="ROOT_ORG_ID" name="ROOT_ORG_ID" type="hidden" value="${dMap.ROOT_ORG_ID }" /> 
		<input
			id="DEALER_ID" name="DEALER_ID" type="hidden" value="${dMap.DEALER_ID }" />
		<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<tr>
				<th colSpan=4 align=left><img class=nav src="<%=contextPath%>/img/subNav.gif"> 服务商基础信息</th>
			<tr>
			<tr>
				<td align="right">组织机构代码：</td>
				<td><input type='text' class="middle_txt" readonly="readonly" name="COMPANY_ZC_CODE" id="COMPANY_ZC_CODE" value="${dMap.COMPANY_ZC_CODE }" maxlength="150" /></td>
				<td align="right">单位性质：</td>
				<td align="left"><select id="UNION_TYPE" name="UNION_TYPE">
						<option value="国营">国营</option>
						<option value="民营">民营</option>
						<option value="合资">合资</option>
						<option value="其它">其它</option>
				</select> <script type="text/javascript">
		      			setItemValue('UNION_TYPE','${dMap.UNION_TYPE }');
		      		</script></td>
			</tr>
			<tr>
				<td align="right">服务商公司：</td>
				<td align="left"><input class="middle_txt" id="COMPANY_NAME"  name="COMPANY_NAME" type="text" readonly="readonly"
					value="${dMap.COMPANY_NAME }" /></td>
				<td align="right" width="15%">服务商简称：</td>
				<td align="left"><input type='text' class="middle_txt" readonly="readonly" name="DEALER_SHORTNAME" id="DEALER_SHORTNAME"  value="${dMap.DEALER_SHORTNAME }"
					maxlength="150" /></td>
			</tr>
			<tr>
				<td align="right" width="15%">服务商代码：</td>
				<td align="left"><c:if test="${dMap.DEALER_CODE==null }">
						<input type='text' class="middle_txt" name="DEALER_CODE" id="DEALER_CODE"  value="${dMap.DEALER_CODE }" />
					</c:if> <c:if test="${dMap.DEALER_CODE!=null }">
						<input type='text' class="middle_txt" readonly name="DEALER_CODE" id="DEALER_CODE"  value="${dMap.DEALER_CODE }" />
					</c:if></td>
				<td align="right" width="15%">服务商名称：</td>
				<td align="left"><input type='text' class="middle_txt" readonly="readonly" name="DEALER_NAME" id="DEALER_NAME"  value="${dMap.DEALER_NAME }" maxlength="150" /></td>
			</tr>
			<tr>
				<td align="right" width="15%">服务商等级：</td>
				<td align="left">
					<select name="DEALER_LEVEL"><option value="10851001">一级经销商</option></select>
                </td>
				<td align="right">与一级经销商关系：</td>
				<td><select id="DEALER_RELATION" name="DEALER_RELATION">
						<option value="自有">自有</option>
						<option value="控股">控股</option>
						<option value="合作">合作</option>
				</select> <script type="text/javascript">
			    		setItemValue('DEALER_RELATION','${dMap.DEALER_RELATION }');
			    	</script></td>
			</tr>
			<tr>
				<td align="right" width="15%">上级组织：</td>
				<td align="left">
				<input type="text" name="orgCode" size="15" value="${dMap.ORG_CODE}" id="orgCode" class="middle_txt" readonly="readonly"  /> 
				<input type="hidden" id="DEALER_ORG_ID" name="DEALER_ORG_ID"  />
				</td>
				<td align="right" width="15%">上级服务商：</td>
				<td align="left"><input type="text" name="PARENT_DEALER_NAME" size="15" value="${dMap.PARENT_DEALER_NAME }" id="PARENT_DEALER_NAME" class="middle_txt" readonly="readonly" />
					<input type="text" name="PARENT_DEALER_D" value="${dMap.PARENT_DEALER_D }" id="PARENT_DEALER_D" style="display: none;" /> <input name="dealerBtn" disabled="disabled"
					id="dealerBtn" type="button" class="mark_btn" onclick="showOrgDealer('PARENT_DEALER_NAME','PARENT_DEALER_D','false','','true')" value="&hellip;" /></td>
			</tr>
			<tr>
				<td align=right>选择大区：</td>
				<td align=left><select id="orgId" name="orgId"">
						<c:forEach items="${orglist }" var="list">
							<option value="${list.ORG_ID }">${list.ORG_NAME }</option>
						</c:forEach>
				</select></td>
				<td align="right">省份：</td>
				<td align="left"><select class="min_sel"  id="PROVINCE_ID" name="PROVINCE_ID" onchange="_genCity(this,'CITY_ID')"></select></td>
			</tr>
			<tr>
				<td align="right">城市：</td>
				<td align="left"><select class="min_sel"  id="CITY_ID" name="CITY_ID" onchange="_genCity(this,'COUNTIES')"></select></td>

				<td align="right">区县：</td>
				<td align="left"><select class="min_sel"  id="COUNTIES" name="COUNTIES" datatype="0,is_null,200"></select></td>
			</tr>
			<tr>
				<td align="right">邮编：</td>
				<td align="left"><input type='text' class="middle_txt" name="ZIP_CODE" readonly="readonly" id="ZIP_CODE" value="${dMap.ZIP_CODE}" /></td>

			</tr>
			<tr>

				<td align="right">企业注册地址：</td>
				<td align="left" colspan="3"><input type='text' class="middle_txt" name="ADDRESS" readonly="readonly" id="ADDRESS" value="${dMap.ADDRESS}" style="width: 380px;" /></td>
			</tr>
			<tr>
				<td align="right">服务商地址：</td>
				<td align="left" colspan="3"><input type='text' class="middle_txt" name="COMPANY_ADDRESS" readonly="readonly" id="COMPANY_ADDRESS" value="${dMap.COMPANY_ADDRESS}" style="width: 380px;" /></td>
			</tr>
			<tr>
				<td align="right">企业注册证号：</td>
				<td align="left" colspan="3"><input type='text' class="middle_txt" name="ZCCODE" readonly="readonly" id="ZCCODE" value="${dMap.ZCCODE}" /></td>
			</tr>
			<tr>
				<td align="right">法人：</td>
				<td align="left"><input type='text' class="middle_txt" name="LEGAL" readonly="readonly" id="LEGAL" datatype="0,is_null,20" value="${dMap.LEGAL}" /></td>
				<td align="right">法人手机：</td>
				<td align="left"><input type='text' class="middle_txt" name="LEGAL_TELPHONE" readonly="readonly" id="LEGAL_TELPHONE" value="${dMap.LEGAL_TELPHONE}" /></td>
			</tr>
			<tr>
				<td align="right">法人办公室电话：</td>
				<td align="left"><input type='text' class="middle_txt" name="LEGAL_PHONE" readonly="readonly" id="LEGAL_PHONE" value="${dMap.LEGAL_PHONE}" /></td>
				<td align="right">法人邮箱：</td>
				<td align="left"><input type='text' class="middle_txt" name="LEGAL_EMAIL" readonly="readonly" id="LEGAL_EMAIL" value="${dMap.LEGAL_EMAIL}" /></td>
			</tr>
			<tr>
				<td align="right" width="15%">服务商类型：</td>
				<td align="left"><select id="DEALER_TYPE" name="DEALER_TYPE" onchange="dealerType();changeDealerlevel()">
						<option value="<%=Constant.DEALER_TYPE_DWR%>">售后服务</option>
				</select> <font color="red">*</font></td>
				<td align="right" width="15%">服务商状态：</td>
				<td align="left"><script type="text/javascript">
						genSelBoxExp("SERVICE_STATUS",<%=Constant.DLR_SERVICE_STATUS%>,"${dMap.SERVICE_STATUS}",'true',"short_sel",'',"false",'');
					</script> <font color="red">*</font></td>
			</tr>
			<tr>
				<td align="right">主营范围：</td>
				<td align="left"><input type='text' class="middle_txt" name="ZY_SCOPE"  id="ZY_SCOPE" value="${dMap.ZY_SCOPE}" /></td>
				<td align="right">兼营范围：</td>
				<td align="left"><input type='text' class="middle_txt" name="JY_SCOPE"  id="JY_SCOPE" value="${dMap.JY_SCOPE}" /></td>
			</tr>
			<tr>
				<td align="right">建站时间：</td>
				<td align="left"><input name="SITEDATE" type="text" class="short_time_txt" id="SITEDATE" readonly="readonly" value="${dMap.SITEDATE}" /> 
<!-- 				<input name="button5" -->
<!-- 					type="button" class="time_ico" onclick="showcalendar(event, 'SITEDATE', false);" /> -->
					</td>
				<td align="right">撤站时间：</td>
				<td align="left"><input name="DESTROYDATE" type="text" class="short_time_txt" id="DESTROYDATE" value="${dMap.DESTROYDATE}" /> 
<!-- 				<input name="button7" -->
<!-- 					type="button" class="time_ico" onclick="showcalendar(event, 'DESTROYDATE', false);" /> -->
					</td>
			</tr>
			<tr>
				<td align="right" width="15%">固定资产：</td>
				<td align="left"><input type='text' class="middle_txt" name="FIXED_CAPITAL" readonly="readonly" id="FIXED_CAPITAL" datatype="1,is_double,20" value="${dMap.FIXED_CAPITAL }" /></td>
				<td align="right" width="15%">注册资金：</td>
				<td align="left"><input type='text' class="middle_txt" name="REGISTERED_CAPITAL" readonly="readonly" id="REGISTERED_CAPITAL" datatype="1,is_double,20" value="${dMap.REGISTERED_CAPITAL }"
					maxlength="150" /></td>
			</tr>
			<tr>
				<td align="right" width="15%">服务站人数：</td>
				<td align="left"><input type='text' class="middle_txt" name="PEOPLE_NUMBER"  id="PEOPLE_NUMBER" datatype="1,isDigit,20" value="${dMap.PEOPLE_NUMBER }" /></td>
				<td align="right" width="15%">辐射区域</td>
				<td align="left"><input type='text' class="middle_txt" name="THE_AGENTS"  id="THE_AGENTS" value="${dMap.THE_AGENTS }" /></td>
			</tr>
			<tr>
				<td align="right">维修资质：</td>
				<td align="left"><script type="text/javascript">
						genSelBoxExp("MAIN_RESOURCES",<%=Constant.MAIN_RESOURCES%>,"${dMap.MAIN_RESOURCES}",true,"short_sel",'',"false",'');
					</script> <font color="red">*</font></td>
				<td align="right">结算等级：</td>
				<td align="left"><script type="text/javascript">
						genSelBoxExp("BALANCE_LEVEL",<%=Constant.BALANCE_LEVEL%>,'${dMap.BALANCE_LEVEL}','true',"short_sel",'',"false",'');
					</script> <font color="red">*</font></td>
			</tr>
			<tr>
				<td align="right">开票等级：</td>
				<td align="left"><script type="text/javascript">
						genSelBoxExp("INVOICE_LEVEL",<%=Constant.INVOICE_LEVEL%>,'${dMap.INVOICE_LEVEL}',true,"short_sel",'',"false",'');
					</script> <font color="red">*</font></td>
				<td align="right">建店类别：</td>
				<td align="left"><script type="text/javascript">
						genSelBoxExp("IMAGE_LEVEL",<%=Constant.IMAGE_LEVEL%>,"${dMap.IMAGE_LEVEL}",true,"short_sel",'',"false",'');
					</script></td>
			</tr>
			<tr>
				<td align="right">是否有二级服务站：</td>
				<td align="left"><script type="text/javascript">
					genSelBoxExp("IS_LOW_SER",<%=Constant.IF_TYPE%>,"${dMap.IS_LOW_SER}",true,"short_sel",'',"false",'');
				</script></td>
<!-- 				<td align="center"><input class="normal_btn" name="fj" id="fj" type="button" onclick="showVehicleUpload('','')" value ="添加附件"/></td>'; -->
			</tr>
			<tr>
				<td width="100%" colspan=4>
					<table width=100% border="0" align="center" cellpadding="1" cellspacing="1">
						<tr>
							<th colSpan=4 align=left><img class=nav src="<%=contextPath%>/img/subNav.gif"> <span id="up_name_00">服务站信息</span></th>
						</tr>
					</table>
				</td>
			<tr>
				<td align="right">营业时间：</td>
				<td align="left"><input type='text' class="middle_txt" name="OPENING_TIME" id="OPENING_TIME" value="${dMap.OPENING_TIME}" /></td>
				<td align="right">经营类型：</td>
				<td align="left"><select id="WORK_TYPE" name="WORK_TYPE">
						<option value="代理">代理</option>
						<option value="直营">直营</option>
						<option value="托管">托管</option>
				</select></td>
				<script type="text/javascript"> 
		   			setItemValue('WORK_TYPE','${dMap.WORK_TYPE }'); 
   				</script>
			</tr>
			<tr>
				<td align="right">维修车间面积：</td>
				<td align="left"><input type='text' class="middle_txt" name="MAIN_AREA" id="MAIN_AREA" value="${dMap.MAIN_AREA}" /></td>
				<td align="right">配件库面积：</td>
				<td align="left"><input type='text' class="middle_txt" name="PARTS_AREA" id="PARTS_AREA" value="${dMap.PARTS_AREA}" /></td>
			</tr>
			<tr>
				<td align="right">接待室面积：</td>
				<td align="left"><input type='text' class="middle_txt" name="MEETING_ROOM_AREA" id="MEETING_ROOM_AREA" value="${dMap.MEETING_ROOM_AREA}" /></td>
				<td align="right">停车场面积：</td>
				<td align="left"><input type='text' class="middle_txt" name="DEPOT_AREA" id="DEPOT_AREA" value="${dMap.DEPOT_AREA}" /></td>
			</tr>
			<tr>
				<td align="right">24小时服务热线：</td>
				<td colspan="3" align="left"><input type="text" class="middle_txt" name="HOTLINE" id="HOTLINE" datatype="1,is_null,30" value="${dMap.HOTLINE}" /></td>
			</tr>
			<tr>
				<td align="right">服务经理：</td>
				<td align="left"><input type='text' class="middle_txt" name="SER_MANAGER_NAME" id="SER_MANAGER_NAME" datatype="0,is_null,50" value="${dMap.SER_MANAGER_NAME}" /></td>
				<td align="right">服务经理办公电话：</td>
				<td align="left"><input type='text' class="middle_txt" name="SER_MANAGER_PHONE" id="SER_MANAGER_PHONE" value="${dMap.SER_MANAGER_PHONE}" /></td>
			</tr>
			<tr>
				<td align="right">服务经理手机：</td>
				<td align="left"><input type='text' class="middle_txt" name="SER_MANAGER_TELPHONE" id="SER_MANAGER_TELPHONE" value="${dMap.SER_MANAGER_TELPHONE}" /></td>
				<td align="right">服务经理邮箱：</td>
				<td align="left"><input type='text' class="middle_txt" name="SER_MANAGER_EMAIL" id="SER_MANAGER_EMAIL" value="${dMap.SER_MANAGER_EMAIL}" /></td>
			</tr>
			<tr>
				<td align="right">索赔主管：</td>
				<td align="left"><input type='text' class="middle_txt" name="CLAIM_DIRECTOR_NAME" id="CLAIM_DIRECTOR_NAME" value="${dMap.CLAIM_DIRECTOR_NAME}" /></td>
				<td align="right">索赔主管办公电话：</td>
				<td align="left"><input type='text' class="middle_txt" name="CLAIM_DIRECTOR_PHONE" id="CLAIM_DIRECTOR_PHONE" value="${dMap.CLAIM_DIRECTOR_PHONE}" /></td>
			</tr>
			<tr>
				<td align="right">索赔主管手机：</td>
				<td align="left"><input type='text' class="middle_txt" name="CLAIM_DIRECTOR_TELPHONE" id="CLAIM_DIRECTOR_TELPHONE" value="${dMap.CLAIM_DIRECTOR_TELPHONE}" /></td>
				<td align="right">索赔主管邮箱：</td>
				<td align="left"><input type='text' class="middle_txt" name="CLAIM_DIRECTOR_EMAIL" id="CLAIM_DIRECTOR_EMAIL" value="${dMap.CLAIM_DIRECTOR_EMAIL}" /></td>
			</tr>
			<tr>
				<td align="right">索赔传真：</td>
				<td align="left"><input type='text' class="middle_txt" name="CLAIM_DIRECTOR_FAX" id="CLAIM_DIRECTOR_FAX" value="${dMap.CLAIM_DIRECTOR_FAX}" /></td>
				<td align="right">服务主管：</td>
				<td align="left"><input type='text' class="middle_txt" name="SER_DIRECTOR_NAME" id="SER_DIRECTOR_NAME" value="${dMap.SER_DIRECTOR_NAME}" /></td>
			</tr>
			<tr>
				<td align="right">服务主管办公电话：</td>
				<td align="left"><input type='text' class="middle_txt" name="SER_DIRECTOR_PHONE" id="SER_DIRECTOR_PHONE" value="${dMap.SER_DIRECTOR_PHONE}" /></td>
				<td align="right">服务主管手机：</td>
				<td align="left"><input type='text' class="middle_txt" name="SER_DIRECTOR_TELHONE" id="SER_DIRECTOR_TELHONE" value="${dMap.SER_DIRECTOR_TELHONE}" /></td>
			</tr>
			<tr>
				<td align="right">技术主管：</td>
				<td align="left"><input type='text' class="middle_txt" name="TECHNOLOGY_DIRECTOR_NAME" id="TECHNOLOGY_DIRECTOR_NAME" value="${dMap.TECHNOLOGY_DIRECTOR_NAME}" /></td>
				<td align="right">技术主管手机：</td>
				<td align="left"><input type='text' class="middle_txt" name="TECHNOLOGY_DIRECTOR_TELPHONE" id="TECHNOLOGY_DIRECTOR_TELPHONE" value="${dMap.TECHNOLOGY_DIRECTOR_TELPHONE}" />
				</td>
			</tr>
			<tr>
				<td align="right">财务经理：</td>
				<td align="left"><input type='text' class="middle_txt" name="FINANCE_MANAGER_NAME" id="FINANCE_MANAGER_NAME" datatype="0,is_null,50" value="${dMap.FINANCE_MANAGER_NAME}" /></td>
				<td align="right">财务经理办公电话：</td>
				<td align="left"><input type='text' class="middle_txt" name="FINANCE_MANAGER_PHONE" id="FINANCE_MANAGER_PHONE" value="${dMap.FINANCE_MANAGER_PHONE}" /></td>
			</tr>
			<tr>
				<td align="right">财务手机：</td>
				<td align="left"><input type='text' class="middle_txt" name="FINANCE_MANAGER_TELPHONE" id="FINANCE_MANAGER_TELPHONE" value="${dMap.FINANCE_MANAGER_TELPHONE}" /></td>
				<td align="right">财务邮箱：</td>
				<td align="left"><input type='text' class="middle_txt" name="FINANCE_MANAGER_EMAIL" id="FINANCE_MANAGER_EMAIL" value="${dMap.FINANCE_MANAGER_EMAIL}" /></td>
			</tr>
			<tr>
				<td align="right">配件主管 ：</td>
				<td align="left"><input type='text' class="middle_txt" name="FITTINGS_DEC_NAME" id="FITTINGS_DEC_NAME" value="${dMap.FITTINGS_DEC_NAME}" /></td>
				<td align="right">配件主管办公电话 ：</td>
				<td align="left"><input type='text' class="middle_txt" name="FITTINGS_DEC_TELPHONE" id="FITTINGS_DEC_TELPHONE" value="${dMap.FITTINGS_DEC_TELPHONE}" /></td>
			</tr>
			<tr>
				<td align="right">配件主管手机 ：</td>
				<td align="left"><input type='text' class="middle_txt" name="FITTINGS_DEC_PHONE" id="FITTINGS_DEC_PHONE" value="${dMap.FITTINGS_DEC_PHONE}" /></td>
			</tr>
			<tr>
				<td align="right">配件主管邮箱 ：</td>
				<td align="left"><input type='text' class="middle_txt" name="FITTINGS_DEC_EMAIL" id="FITTINGS_DEC_EMAIL" value="${dMap.FITTINGS_DEC_EMAIL}" /></td>
				<td align="right">配件传真 ：</td>
				<td align="left"><input type='text' class="middle_txt" name="FITTINGS_DEC_FAX" id="FITTINGS_DEC_FAX" value="${dMap.FITTINGS_DEC_FAX}" /></td>
			</tr>
			<tr>
				<td align="right">企业授权类型：</td>
				<td align="left"><select name="AUTHORIZATION_TYPE" id="AUTHORIZATION_TYPE">
						<option value="形象店">形象店</option>
						<option value="特约站">特约站</option>
						<option value="代理库">代理库</option>
						<option value="专卖店">专卖店</option>
				</select></td>
				<td align="right">授权时间：</td>
				<td align="left"><input name="AUTHORIZATION_DATE" type="text" class="short_time_txt" id="AUTHORIZATION_DATE" readonly="readonly" value="${dMap.AUTHORIZATION_DATE}" /> <input
					name="button9" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'AUTHORIZATION_DATE', false);" /></td>
			</tr>
			<tr>
				<td align="right">是否经营其他品牌：</td>
				<td align="left"><script type="text/javascript">
					genSelBoxExp("IS_ACTING_BRAND",<%=Constant.IF_TYPE%>,"${dMap.IS_ACTING_BRAND}",true,"short_sel",'',"false",'');
				</script></td>
				<td align="right">代理其它品牌名称：</td>
				<td align="left"><input type="text" class="middle_txt" name="ACTING_BRAND_NAME" id="ACTING_BRAND_NAME" value="${dMap.ACTING_BRAND_NAME}" maxlength="10" /></td>
			</tr>
			<tr>
				<td align="right">配件储备金额（万元）：</td>
				<td align="left"><input type="text" class="middle_txt" name="PARTS_STORE_AMOUNT" id="PARTS_STORE_AMOUNT" datatype="1,is_null,10" value="${dMap.PARTS_STORE_AMOUNT}"
					maxlength="10" /></td>
				<td align="right">验收形象等级：</td>
				<td align="left"><script type="text/javascript">
					genSelBoxExp("IMAGE_COMFIRM_LEVEL",<%=Constant.IMAGE_LEVEL%>,"${dMap.IMAGE_COMFIRM_LEVEL}",true,"short_sel",'',"false",'');
				</script></td>
			</tr>
			<tr>
				<td align="right">代理区域：</td>
      		<td class="table_query_4Col_input" nowrap="nowrap">
      			<input type="text"  readonly="readonly"  name="PROXY_AREA_NAME" size="15" value="${ProxyAreaName}"  id="PROXY_AREA_NAME" class="middle_txt" />
				<input type="hidden"  readonly="readonly"  name="PROXY_AREA" size="15" value="${ProxyArea}"  id="PROXY_AREA" class="middle_txt" />
				<input type="hidden"  readonly="readonly"  name="PROXY_AREA_DEF" size="15" value=""  id="PROXY_AREA_DEF" class="middle_txt" />
				<input type="hidden"  readonly="readonly"  name="PROXY_AREA_NAME_DEF" size="15" value=""  id="PROXY_AREA_NAME_DEF" class="middle_txt" />
				<input name="regbu"  id="regbu" type="button" class="mark_btn" onclick="showCityMileageForDealer('','','true')" value="..." />
				<input type="hidden"  name="regionId" size="15" value=""  id="regionId" />
				<input type="button"  class="cssbutton" value="清除" onclick="clrTxt('PROXY_AREA','PROXY_AREA_NAME');"/>
			</td>		
				<td align="right">代理车型：</td>
				<td align="left"><input type="text" class="middle_txt" name="PROXY_VEHICLE_TYPE" id="PROXY_VEHICLE_TYPE" value="${dMap.PROXY_VEHICLE_TYPE}" maxlength="10" /></td>
			</tr>
			<tr>
				<td align="right" width="15%">备注：</td>
				<td align="left" colspan="3"><textarea name="REMARK" id="REMARK" style="width: 90%;" rows="2" datatype="1,is_textarea,1000">${dMap.REMARK}</textarea></td>
			</tr>
			<tr>
			<tr id="kpxx">
				<td width="100%" colspan=4>
					<table width=100% border="0" align="center" cellpadding="1" cellspacing="1">
						<tr>
							<th colSpan=4 align=left><IMG class=nav src="<%=contextPath%>/img/subNav.gif"> 开票信息</th>
						</tr>
					</table>
				</td>
			<tr>
				<td align="right" width="15%">开票名称：</td>
				<td class="table_query_4Col_input" nowrap="nowrap"><input type="text" class="middle_txt" readonly="readonly" id="ERP_CODE" name="ERP_CODE" value="${dMap.ERP_CODE}" /></td>
				<td align="right" width="15%">税号：</td>
				<td class="table_query_4Col_input" nowrap="nowrap"><input type="text" class="middle_txt" readonly="readonly" id="TAXES_NO" name="TAXES_NO" value="${dMap.TAXES_NO}" /></td>
			</tr>
			<tr>
				<td align="right" width="15%">开户行：</td>
				<td class="table_query_4Col_input" nowrap="nowrap"><input type="text" class="middle_txt" readonly="readonly" id="BEGIN_BANK" name="BEGIN_BANK" value="${dMap.BEGIN_BANK}" /></td>
				<td align="right" width="15%">账号：</td>
				<td class="table_query_4Col_input" nowrap="nowrap"><input type="text" class="middle_txt" readonly="readonly" id="INVOICE_ACCOUNT" name="INVOICE_ACCOUNT" value="${dMap.INVOICE_ACCOUNT}" /></td>
			</tr>
			<tr>
				<td align="right" width="15%">服务站地址：</td>
				<td class="table_query_4Col_input" nowrap="nowrap"><input type="text" class="middle_txt" readonly="readonly" id="INVOICE_ADD" name="INVOICE_ADD" value="${dMap.INVOICE_ADD}" /></td>
				<td align="right" width="15%">电话：</td>
				<td class="table_query_4Col_input" nowrap="nowrap"><input type="text" class="middle_txt" readonly="readonly" id="INVOICE_PHONE" name="INVOICE_PHONE" value="${dMap.INVOICE_PHONE}" /></td>
			</tr>
			<tr>
				<td align="right">纳税人识别号：</td>
				<td class="table_query_4Col_input" nowrap="nowrap"><input type="text" class="middle_txt" readonly="readonly" id="TAXPAYER_NO" name="TAXPAYER_NO" value="${dMap.TAXPAYER_NO}" /></td>
				<td align="right" nowrap="nowrap" class="table_query_4Col_input">纳税人性质：</td>
<!-- 				<td class="table_query_4Col_input" nowrap="nowrap"> -->
<!-- 				<select  id="TAXPAYER_NATURE" name="TAXPAYER_NATURE"> -->
<!-- 						<option value="一般纳税人">一般纳税人</option> -->
<!-- 						<option value="小规模纳税人">小规模纳税人</option> -->
<!-- 				</select> 
					<script type="text/javascript"> -->
<%-- // 		   			setItemValue('TAXPAYER_NATURE','${dMap.TAXPAYER_NATURE }'); --%>
<!-- 		   		</script> -->
				<c:choose>
					<c:when test="${dMap.TAXPAYER_NATURE == '一般纳税人 '}">
					<input type="hidden"   id="TAXPAYER_NATURE" name="TAXPAYER_NATURE" value="${dMap.TAXPAYER_NATURE}" />
					<td class="table_query_4Col_input" nowrap="nowrap"><input type="text" class="middle_txt" readonly="readonly"  value="${dMap.TAXPAYER_NATURE}" /></td>
					</c:when>
					<c:otherwise>
					<input type="hidden"   id="TAXPAYER_NATURE" name="TAXPAYER_NATURE" value="${dMap.TAXPAYER_NATURE}" />
					<td class="table_query_4Col_input" nowrap="nowrap"><input type="text" class="middle_txt" readonly="readonly"  value="${dMap.TAXPAYER_NATURE}" /></td>
					</c:otherwise>
				</c:choose>
<!-- 		   		</td> -->
			</tr>
			<tr>
				<td align="right">增值税发票：</td>
				<c:choose>
					<c:when test="${dMap.TAX_INVOICE == 10041001 }">
					<input type="hidden"   id="TAX_INVOICE" name="TAX_INVOICE" value="${dMap.TAX_INVOICE}" />
					<td class="table_query_4Col_input" nowrap="nowrap"><input type="text" class="middle_txt" readonly="readonly"  value="是" /></td>
					</c:when>
					<c:otherwise>
					<input type="hidden"   id="TAX_INVOICE" name="TAX_INVOICE" value="${dMap.TAX_INVOICE}" />
					<td class="table_query_4Col_input" nowrap="nowrap"><input type="text" class="middle_txt" readonly="readonly"  value="否" /></td>
					</c:otherwise>
				</c:choose>
				<td align="right" nowrap="nowrap" class="table_query_4Col_input">开票税率：</td>
				<td class="table_query_4Col_input" nowrap="nowrap"><input type="text" class="middle_txt" readonly="readonly" id="TAX_DISRATE" name="TAX_DISRATE" value="${dMap.TAX_DISRATE}" /></td>
			</tr>
			<tr>
				<td align="right" width="15%">发票邮寄地址：</td>
				<td class="table_query_4Col_input" nowrap="nowrap" colspan="3"><input type="text" class="middle_txt" readonly="readonly" id="INVOICE_POST_ADD" name="INVOICE_POST_ADD"
					value="${dMap.INVOICE_POST_ADD}" /></td>
			</tr>
		</table>
		<table class=table_query>
			<tr>
				<td align="center"><input type="button" value="提报" name="saveBtn" class="normal_btn" onclick="saveDealerInfo()" /> &nbsp;&nbsp; <input type="button" value="返回"
					name="cancelBtn" class="normal_btn" onclick="history.back();" /></td>
			</tr>
		</table>
	</form>

	<script type="text/javascript">

function saveDealerInfo()
{    
	var DEALERTYPE = document.getElementById("DEALER_TYPE").value;
	if(DEALERTYPE==<%=Constant.DEALER_TYPE_DWR%>){
		var BALANCE_LEVEL=document.getElementById("BALANCE_LEVEL").value;
		var INVOICE_LEVEL=document.getElementById("INVOICE_LEVEL").value;
		var DEALERLEVEL=document.getElementById("DEALER_LEVEL").value;//经销商级别
		if(BALANCE_LEVEL==''){
			MyAlert('请选择结算等级!');
			return;
		}
		if(INVOICE_LEVEL==''){
			MyAlert('请选择开票等级!');
			return;
		}
		// 一级经销商
		if(DEALERLEVEL==<%=Constant.DEALER_LEVEL_01%>)
		{
			if(BALANCE_LEVEL!=<%=Constant.BALANCE_LEVEL_SELF%>){
				MyAlert('一级经销商结算等级必须选择 独立结算!');
				return;
			}
			if(INVOICE_LEVEL!=<%=Constant.INVOICE_LEVEL_SELF%>){
				MyAlert('一级经销商开票等级必须选择 独立开票!');
				return;
			}
		}
		else
		{
			// 二级经销商
			if(BALANCE_LEVEL==<%=Constant.BALANCE_LEVEL_HIGH%>&&INVOICE_LEVEL==<%=Constant.INVOICE_LEVEL_SELF%>){
				MyAlert('结算等级为上级结算时,不能选择独立开票!');
				return;
			}
		}
	}
	if(document.getElementById("CITY_ID").value == '') {MyAlert('请选择城市!'); return;}
	
	if(document.getElementById("COUNTIES").value == '') {MyAlert('请选择区县!'); return;}
	
	if(document.getElementById("SERVICE_STATUS").value == '') {MyAlert("请选择服务商状态!"); return;}

	if(confirm("信息可处理,是否继续?"))
	{
		var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/saveDealerInfoForDealer.json";
		makeNomalFormCall(url,function(json){
			if(json.Exception) {
				MyAlert(json.Exception);
			} else {
				MyAlert(json.message);
				window.location.href = "<%=contextPath%>/sysmng/dealer/DealerInfo/queryDealerCsInfoForDealerInit.do";
							}
						}, 'fm', '');
			}
		}
	</script>

	<script type="text/javascript">
		genLocSel('PROVINCE_ID', 'CITY_ID', 'COUNTIES');

		var p = document.getElementById("PROVINCE_ID");
		setItemValue('PROVINCE_ID', '${dMap.PROVINCE_ID}');
		_genCity(p, 'CITY_ID');
		var c = document.getElementById("CITY_ID");
		setItemValue('CITY_ID', '${dMap.CITY_ID}');
		_genCity(c, 'COUNTIES');
		var t = document.getElementById("COUNTIES");
		setItemValue('COUNTIES', '${dMap.COUNTIES}');

		changeDealerlevel();
	</script>
</body>
</html>
