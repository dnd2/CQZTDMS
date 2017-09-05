<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.*" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>二级经销商维护</title>
<script type="text/javascript">
var dealerLevel=<%=Constant.DEALER_LEVEL_01%>;
var dealerT=<%=Constant.DEALER_TYPE_DWR%>;

window.onload=function(){
	loadcalendar();
}

function showCompanyA(path){ 
	OpenHtmlWindow(path+'/common/OrgMng/queryCompanyA.do',800,450);
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
	
// 	var orgId = document.getElementById("orgId");
// 	var ROOT_ORG_ID = document.getElementById("ROOT_ORG_ID").value;
// 	for (var int = 0; int < orgId.length; int++) {
// 		if(orgId[int].value==ROOT_ORG_ID){
// 			orgId[int].selected=true;
// 			break;
// 		}
// 	}
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
function changeDealerlevel(){
	var dealerLevel = document.getElementById("DEALER_LEVEL").value;
   	var dealerType = document.getElementById("DEALER_TYPE").value;

	if(dealerLevel == '<%=Constant.DEALER_LEVEL_01 %>')
	{
		//document.getElementById("COMPANY_ID").disabled = false;
		//document.getElementById("COMPANY_NAME").disabled = false;
		//document.getElementById("orgbu").disabled = false;
		document.getElementById("dealerBtn").disabled = true;
	}
	else
	{
		//document.getElementById("COMPANY_ID").disabled = true;
		//document.getElementById("COMPANY_NAME").disabled = true;
		//document.getElementById("orgbu").disabled = true;
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
</script>
</head>
<body onunload='javascript:destoryPrototype()' onload="">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;<c:if test="${is_Add == true}">新增二级经销商</c:if><c:if test="${is_Mod == true}">修改经销商</c:if></div>
 <form method="post" name = "fm"  id="fm">
 	<input id="COMPANY_ID" name="COMPANY_ID" type="hidden" value="${dMap.COMPANY_ID }"/>
 	<input id="DEALER_ID" name="DEALER_ID" type="hidden" value="${dMap.DEALER_ID }"/>
 	<input id="ROOT_ORG_ID" name="ROOT_ORG_ID" type="hidden" value="${dMap.ROOT_ORG_ID }"/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    	  <TR>
			<TH colSpan=4 align=left><IMG class=nav src="<%=contextPath%>/img/subNav.gif"> 经销商基础信息</TH>
    	  <tr>
    	  <tr>
    	  	<td align="right">组织机构代码：</td>
    	  	<td>
    	  		<input type='text' class="middle_txt" name="COMPANY_ZC_CODE" id="COMPANY_ZC_CODE"  value="${dMap.COMPANY_ZC_CODE }" maxlength="150"/>
    	  	</td>
            <td align="right">经营类型：</td>
             <td align="left">
             	<select id="SHOP_TYPE" name="SHOP_TYPE">
             		<option value="代理">代理</option>
             		<option value="直营">重点客户</option>
             	</select>
             	<script type="text/javascript">
             		setItemValue('SHOP_TYPE', '${dMap.SHOP_TYPE}');
             	</script>
             </td>
          </tr>
		  <tr>
		    <td align="right">经销商公司：</td>
		    <td align="left">
            	<input class="middle_txt"  datatype="0,is_null,75"  id="COMPANY_NAME" style="cursor: pointer;" name="COMPANY_NAME" type="text" readonly="readonly"  value="${dMap.COMPANY_NAME }"/>
		  	 	<input class="mark_btn" type="button" value="&hellip;" onclick="showCompanyA('<%=contextPath %>') ;"/>
            </td>
		    <td align="right" width="15%">经销商简称：</td>
		    <td align="left">
		    	<input type='text'  class="middle_txt" name="DEALER_SHORTNAME"  id="DEALER_SHORTNAME" datatype="0,is_null,150"  value="${dMap.DEALER_SHORTNAME }" maxlength="150"/>
		    </td>
      </tr>
		  <tr>
		    <td align="right" width="15%">经销商代码：</td>
		    <td align="left"><input type='text'  class="middle_txt" name="DEALER_CODE"  id="DEALER_CODE" datatype="0,is_name,20"  value="${dMap.DEALER_CODE }" />
		    </td>
		    <td align="right" width="15%">经销商全称：</td>
		    <td align="left">
		    	<input type='text'  class="middle_txt" name="DEALER_NAME"  id="DEALER_NAME" datatype="0,is_null,150"  value="${dMap.DEALER_NAME }" maxlength="150"/>
		    </td>
	      </tr>
	        <tr>
		    <td align="right" width="15%">上级组织：</td>
		    <td align="left">
			    <input type="text"  name="orgCode" size="15" value="${dMap.ORG_CODE}"  id="orgCode" class="middle_txt" readonly="readonly" datatype="0,is_null,150"/>
			    <input name="orgbu"  id="orgbu" type="button" class="mark_btn" onclick="showOrg('orgCode','DEALER_ORG_ID','false')" value="&hellip;"/>
			    <input type="hidden" id="DEALER_ORG_ID" name="DEALER_ORG_ID" value="${dMap.DEALER_ORG_ID}" datatype="0,is_null,150"/>
		    </td>
		               <td align=right>大区：</td>
		        <td align=left>${ROOT_ORG_NAME }
<!-- 					<select id="orgId" name="orgId" "> -->
<%-- 		        		<c:forEach items="${orglist }" var="list"> --%>
<%-- 		        			<option value="${list.ORG_ID }">${list.ORG_NAME }</option> --%>
<%-- 		        		</c:forEach> --%>
<!-- 		        	</select> -->
				</td>
	      </tr>
           <tr> 

              <td align="right">省份：</td>
              <td align="left"><select class="min_sel" id="PROVINCE_ID" name="PROVINCE_ID" onchange="_genCity(this,'CITY_ID')"></select></td>
                         <td align="right">城市：</td>
              <td align="left"><select class="min_sel" id="CITY_ID" name="CITY_ID" onchange="_genCity(this,'COUNTIES')"></select></td>
      </tr>
           <tr> 

           
             <td align="right">区县：</td>
              <td align="left"><select class="min_sel" id="COUNTIES" name="COUNTIES" datatype="0,is_null,200"></select></td>
                                 <td align="right">邮编：</td>
              <td align="left">
              	<input type='text' class="middle_txt" name="ZIP_CODE"  id="ZIP_CODE"  value="${dMap.ZIP_CODE}" /></td> 
           </tr> 
                   
      <tr>
             <td align="right">经销企业注册资金：</td>
	          <td align="left" >
	          	<input type='text'  class="middle_txt" name="REGISTERED_CAPITAL"  id="REGISTERED_CAPITAL" value="${dMap.REGISTERED_CAPITAL}" maxlength="20" />
	          </td>
	          <td align="right">&nbsp;</td>
	          <td align="left">&nbsp;</td>
      </tr>
      		<tr>
             <td align="right">企业注册地址：</td>
	          <td align="left"  colspan="3"><input type='text'  class="middle_txt" name="ADDRESS"  id="ADDRESS" value="${dMap.ADDRESS}" style="width:380px;"/></td>
      		</tr>
      		<tr>
             <td align="right">销售展厅地址：</td>
	          <td align="left" colspan="3"><input type='text'  class="middle_txt" name="COMPANY_ADDRESS"  id="COMPANY_ADDRESS"  value="${dMap.COMPANY_ADDRESS}" maxlength="300" style="width:380px;"/></td>
      		</tr>
           <tr>
             <td align="right">法人：</td>
             <td align="left"><input type='text'  class="middle_txt" name="LEGAL"  id="LEGAL" datatype="0,is_name,20"  value="${dMap.LEGAL}" /></td>
             <td align="right">法人手机：</td>
             <td align="left"><input type='text'  class="middle_txt" name="LEGAL_TELPHONE"  id="LEGAL_TELPHONE"  value="${dMap.LEGAL_TELPHONE}" /></td>
           </tr>
           <tr>
             <td align="right">法人办公室电话：</td>
             <td align="left"><input type='text'  class="middle_txt" name="LEGAL_PHONE"  id="LEGAL_PHONE"  value="${dMap.LEGAL_PHONE}"/></td>
             <td align="right">法人邮箱：</td>
             <td align="left"><input type='text'  class="middle_txt" name="LEGAL_EMAIL"  id="LEGAL_EMAIL"  value="${dMap.LEGAL_EMAIL}"/></td>
           </tr>
           <tr>
		    <td align="right" width="15%">经销商类型：</td>
		    <td align="left">
		    <label>
				<select id="DEALER_TYPE" name="DEALER_TYPE" onchange="dealerType();changeDealerlevel()">
					<option value="<%=Constant.DEALER_TYPE_DVS%>">整车销售</option>
				</select>
				<font color="red">*</font>
		</label>
		    </td>
		    <td align="right" width="15%">经销商状态：</td>
		    <td align="left">
					<script type="text/javascript">
							genSelBoxExp("SERVICE_STATUS",'<%=Constant.DLR_SERVICE_STATUS%>',"${dMap.SERVICE_STATUS}",'',"short_sel",'',"false",'');
					</script>
					<font color="red">*</font>
		    </td>
	      </tr>
	      <tr>
	      	<td align="right" width="15%">单位性质：</td>
	      	<td>
	      		<select id="UNION_TYPE" name="UNION_TYPE">
	      			<option value="国营">国营</option>
	      			<option value="民营">民营</option>
	      			<option value="合资">合资</option>
	      			<option value="其它">其它</option>
	      		</select>
	      		<script type="text/javascript">
	      			setItemValue('UNION_TYPE','${dMap.UNION_TYPE }');
	      		</script>
	      	</td>
	      	<td align="right" width="15%">经销商传真：</td>
	      	<td>
	      		<input type="text" name="FAX_NO" id="FAX_NO" class="middle_txt" value="${dMap.FAX_NO }"/>
	      	</td>
	      </tr>
	      <tr id="zcxs">
			<td width="100%" colspan=4>
			<table  width=100% border="0" align="center" cellpadding="1" cellspacing="1">
			<tr>
			<th colSpan=4 align=left><img class=nav src="<%=contextPath%>/img/subNav.gif"> <span id="up_name_00">整车销售信息</span></th>
			</tr>
             <tr >
               <td align="right">总经理：</td>
               <td align="left"><input type='text'  class="middle_txt" name="WEBMASTER_NAME"  id="WEBMASTER_NAME"   value="${dMap.WEBMASTER_NAME}" /></td>
               <td width="15%" align="right">总经理办公电话：</td>
               <td align="left"><input type='text'  class="middle_txt" name="WEBMASTER_PHONE"  id="WEBMASTER_PHONE"   value="${dMap.WEBMASTER_PHONE}" datatype="1,is_phone,20"/></td>
             </tr>
             <tr >
               <td align="right">总经理手机：</td>
               <td align="left"><input type='text'  class="middle_txt" name="WEBMASTER_TELPHONE"  id="WEBMASTER_TELPHONE"   value="${dMap.WEBMASTER_TELPHONE}" /></td>
               <td align="right">总经理邮箱：</td>
               <td align="left"><input type='text'  class="middle_txt" name="WEBMASTER_EMAIL"  id="WEBMASTER_EMAIL"   value="${dMap.WEBMASTER_EMAIL}" /></td>
             </tr>
             <tr >
               <td align="right">销售经理：</td>
               <td align="left"><input type='text'  class="middle_txt" name="MARKET_NAME"  id="MARKET_NAME"   value="${dMap.MARKET_NAME}" /></td>
               <td align="right">销售经理办公电话：</td>
               <td align="left"><input type='text'  class="middle_txt" name="MARKET_PHONE"  id="MARKET_PHONE"   value="${dMap.MARKET_PHONE}" datatype="1,is_phone,20"/></td>
             </tr>
             <tr >
               <td align="right">销售经理手机：</td>
               <td align="left"><input type='text'  class="middle_txt" name="MARKET_TELPHONE"  id="MARKET_TELPHONE"   value="${dMap.MARKET_TELPHONE}" /></td>
               <td align="right">销售经理邮箱：</td>
               <td align="left"><input type='text'  class="middle_txt" name="MARKET_EMAIL"  id="MARKET_EMAIL"   value="${dMap.MARKET_EMAIL}" /></td>
             </tr>
             <tr >
               <td align="right">市场经理：</td>
               <td align="left"><input type='text'  class="middle_txt" name="MANAGER_NAME"  id="MANAGER_NAME"   value="${dMap.MANAGER_NAME}" /></td>
               <td align="right">市场经理办公电话：</td>
               <td align="left"><input type='text'  class="middle_txt" name="MANAGER_PHONE"  id="MANAGER_PHONE"   value="${dMap.MANAGER_PHONE}" datatype="1,is_phone,20"/></td>
             </tr>
             <tr >
               <td align="right">市场经理手机：</td>
               <td align="left"><input type='text'  class="middle_txt" name="MANAGER_TELPHONE"  id="MANAGER_TELPHONE"   value="${dMap.MANAGER_TELPHONE}" datatype="1,is_phone,20"/></td>
               <td align="right">市场经理邮箱：</td>
               <td align="left"><input type='text'  class="middle_txt" name="MANAGER_EMAIL"  id="MANAGER_EMAIL"   value="${dMap.MANAGER_EMAIL}" /></td>
             </tr>
             <tr >
               <td align="right">财务经理：</td>
               <td align="left"><input type='text'  class="middle_txt" name="FINANCE_MANAGER_NAME"  id="FINANCE_MANAGER_NAME"   value="${dMap.FINANCE_MANAGER_NAME}" /></td>
               <td align="right">财务经理办公电话：</td>
               <td align="left"><input type='text'  class="middle_txt" name="FINANCE_MANAGER_PHONE"  id="FINANCE_MANAGER_PHONE"   value="${dMap.FINANCE_MANAGER_PHONE}" datatype="1,is_phone,20"/></td>
             </tr>
             <tr >
               <td align="right">财务经理手机：</td>
               <td align="left"><input type='text'  class="middle_txt" name="FINANCE_MANAGER_TELPHONE"  id="FINANCE_MANAGER_TELPHONE"   value="${dMap.FINANCE_MANAGER_TELPHONE}" datatype="1,is_phone,20"/></td>
               <td align="right">财务经理邮箱：</td>
               <td align="left"><input type='text'  class="middle_txt" name="FINANCE_MANAGER_EMAIL"  id="FINANCE_MANAGER_EMAIL"   value="${dMap.FINANCE_MANAGER_EMAIL}" /></td>
             </tr>
             <tr >
               <td align="right">信息员：</td>
               <td align="left"><input type='text'  class="middle_txt" name="MESSAGER_NAME"  id="MESSAGER_NAME"   value="${dMap.MESSAGER_NAME}" /></td>
               <td align="right">信息员办公电话：</td>
               <td align="left"><input type='text'  class="middle_txt" name="MESSAGER_PHONE"  id="MESSAGER_PHONE"   value="${dMap.MESSAGER_PHONE}" datatype="1,is_phone,20"/></td>
             </tr>
             <tr >
               <td align="right">信息员手机：</td>
               <td align="left"><input type='text'  class="middle_txt" name="MESSAGER_TELPHONE"  id="MESSAGER_TELPHONE"   value="${dMap.MESSAGER_TELPHONE}" datatype="1,is_phone,20"/></td>
               <td align="right">信息员QQ：</td>
               <td align="left"><input type='text'  class="middle_txt" name="MESSAGER_QQ"  id="MESSAGER_QQ"   value="${dMap.MESSAGER_QQ}" /></td>
             </tr>
             <tr >
               <td align="right">信息员邮箱：</td>
               <td align="left"><input type='text'  class="middle_txt" name="MESSAGER_EMAIL"  id="MESSAGER_EMAIL"   value="${dMap.MESSAGER_EMAIL}" /></td>
               <td align="right">销售热线：</td>
               <td align="left"><input type='text'  class="middle_txt" name="HOTLINE"  id="HOTLINE"   value="${dMap.HOTLINE}" /></td>
             </tr>
             <tr >
             <td align="right">VI建设申请日期：</td>
             <td align="left">
             	<input name="VI_APPLAY_DATE" type="text" class="short_time_txt" id="VI_APPLAY_DATE" readonly="readonly" value="${dMap.VI_APPLAY_DATE}"/> 
             	<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'VI_APPLAY_DATE', false);" />
             </td>
             <td align="right">VI建设开工日期：</td>
             <td align="left">
             	<input name="VI_BEGIN_DATE" type="text" class="short_time_txt" id="VI_BEGIN_DATE" readonly="readonly" value="${dMap.VI_BEGIN_DATE}"/>
               	<input name="button3" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'VI_BEGIN_DATE', false);" />
             </td>
           </tr>
           <tr >
             <td align="right">VI建设竣工日期：</td>
             <td align="left">
             	<input name="VI_COMPLETED_DATE" type="text" class="short_time_txt" id="VI_COMPLETED_DATE" readonly="readonly" value="${dMap.VI_COMPLETED_DATE}"/>
             	<input name="button2" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'VI_COMPLETED_DATE', false);" />
             </td>
             <td align="right">VI形象验收日期：</td>
             <td align="left">
             	<input name="VI_CONFRIM_DATE" type="text" class="short_time_txt" id="VI_CONFRIM_DATE" readonly="readonly" value="${dMap.VI_CONFRIM_DATE}"/>
             	<input name="button4" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'VI_CONFRIM_DATE', false);" />
             </td>
           </tr>
           <tr >
             <td align="right">形象等级：</td>
             <td align="left">
             	<script type="text/javascript">
					genSelBoxExp("IMAGE_LEVEL",<%=Constant.IMAGE_LEVEL%>,"${dMap.IMAGE_LEVEL}",true,"short_sel",'',"false",'');
				</script>
             </td>
             <td align="right">验收形象等级：</td>
             <td align="left">
             	<script type="text/javascript">
					genSelBoxExp("IMAGE_COMFIRM_LEVEL",<%=Constant.IMAGE_LEVEL%>,"${dMap.IMAGE_COMFIRM_LEVEL}",true,"short_sel",'',"false",'');
				</script>
             </td>
           </tr>
           <tr >
             <td align="right">VI支持总金额：</td>
             <td align="left"><input type="text"  class="middle_txt" name="VI_SUPPORT_AMOUNT"  id="VI_SUPPORT_AMOUNT"  datatype="1,is_double,20" value="${dMap.VI_SUPPORT_AMOUNT}" /></td>
             <td align="right">VI支持首批比例：</td>
             <td align="left"><input type="text"  class="middle_txt" name="VI_SUPPORT_RATIO"  id="VI_SUPPORT_RATIO"  datatype="1,is_double,20" value="${dMap.VI_SUPPORT_RATIO}" /></td>
           </tr>
           <tr >
             <td align="right">VI支持后续支持方式：</td>
             <td align="left"><input type="text"  class="middle_txt" name="VI_SUPPORT_TYPE"  id="VI_SUPPORT_TYPE"  datatype="1,is_name,10" value="${dMap.VI_SUPPORT_TYPE}" /></td>
             <td align="right">VI支持起始时间：</td>
             <td align="left"><input name="VI_SUPPORT_DATE" type="text" class="short_time_txt" id="VI_SUPPORT_DATE" readonly="readonly" value="${dMap.VI_SUPPORT_DATE}"/>
             <input name="button16" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'VI_SUPPORT_DATE', false);" /></td>
           </tr>
           <tr >
             <td align="right">VI支持截止时间：</td>
             <td align="left"><input name="VI_SUPPORT_END_DATE" type="text" class="short_time_txt" id="VI_SUPPORT_END_DATE" readonly="readonly" value="${dMap.VI_SUPPORT_END_DATE}"/>
             <input name="button5" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'VI_SUPPORT_END_DATE', false);" /></td>
             <td align="right">首次提车时间：</td>
             <td align="left"><input name="FIRST_SUB_DATE" type="text" class="short_time_txt" id="FIRST_SUB_DATE" readonly="readonly" value="${dMap.FIRST_SUB_DATE}"/>
             <input name="button7" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'FIRST_SUB_DATE', false);" /></td>
           </tr>
           <tr >
             <td align="right">首次到车日期：</td>
             <td align="left">
             	<input name="FIRST_GETCAR_DATE" type="text" class="short_time_txt" id="FIRST_GETCAR_DATE" readonly="readonly" value="${dMap.FIRST_GETCAR_DATE}"/>
             	<input name="button6" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'FIRST_GETCAR_DATE', false);" />
             </td>
             <td align="right">首次销售时间：</td>
             <td align="left">
             	<input name="FIRST_SAELS_DATE" type="text" class="short_time_txt" id="FIRST_SAELS_DATE" readonly="readonly" value="${dMap.FIRST_SAELS_DATE}"/>
             	<input name="button8" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'FIRST_SAELS_DATE', false);" />
             </td>
           </tr>
           <tr >
             				<td align="right">企业授权类型：</td>
				<td align="left"><select name="AUTHORIZATION_TYPE" id="AUTHORIZATION_TYPE">
						<option value="">请选择</option>
						<option value="形象店">形象店</option>
						<option value="特约站">特约站</option>
						<option value="代理库">代理库</option>
						<option value="专卖店">专卖店</option>
				</select></td>             <td align="right">授权时间：</td>
             <td align="left"><input name="AUTHORIZATION_DATE" type="text" class="short_time_txt" id="AUTHORIZATION_DATE" readonly="readonly" value="${dMap.AUTHORIZATION_DATE}"/>
             <input name="button9" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'AUTHORIZATION_DATE', false);" /></td>
           </tr>
           <tr >
             <td align="right">是否经营其他品牌：</td>
             <td align="left">
             	<script type="text/javascript">
					genSelBoxExp("IS_ACTING_BRAND",<%=Constant.IF_TYPE%>,"${dMap.IS_ACTING_BRAND}",true,"short_sel",'',"false",'');
				</script>
             </td>
             <td align="right">代理其它品牌名称：</td>
             <td align="left">
             	<input type="text"  class="middle_txt" name="ACTING_BRAND_NAME"  id="ACTING_BRAND_NAME"  value="${dMap.ACTING_BRAND_NAME}" />
             </td>
           </tr>
           <tr >
             <td align="right">配件储备金额（万元）：</td>
             <td align="left"><input type="text"  class="middle_txt" name="PARTS_STORE_AMOUNT"  id="PARTS_STORE_AMOUNT"  datatype="1,is_double,10" value="${dMap.PARTS_STORE_AMOUNT}" /></td>
             <td align="right">&nbsp;</td>
             <td align="left">&nbsp;</td>
           </tr>  
           <tr>
            <td align="right">代理区域：</td>
      		<td class="table_query_4Col_input" nowrap="nowrap">
      			<input type="text"  readonly="readonly"  name="PROXY_AREA_NAME" size="15" value="${ProxyAreaName}"  id="PROXY_AREA_NAME" class="middle_txt" />
				<input type="hidden"  readonly="readonly"  name="PROXY_AREA" size="15" value="${ProxyArea}"  id="PROXY_AREA" class="middle_txt" />
				<input type="hidden"  readonly="readonly"  name="PROXY_AREA_DEF" size="15" value=""  id="PROXY_AREA_DEF" class="middle_txt" />
				<input type="hidden"  readonly="readonly"  name="ROOT_ORG_ID" size="15" value="${ROOT_ORG_ID}"  id="ROOT_ORG_ID" class="middle_txt" />
				<input type="hidden"  readonly="readonly"  name="PROXY_AREA_NAME_DEF" size="15" value=""  id="PROXY_AREA_NAME_DEF" class="middle_txt" />
				<input name="regbu"  id="regbu" type="button" class="mark_btn" onclick="showCityMileageForDealer('','','true')" value="..." />
				<input type="hidden"  name="regionId" size="15" value=""  id="regionId" />
				<input type="button"  class="cssbutton" value="清除" onclick="clrTxt('PROXY_AREA','PROXY_AREA_NAME');"/>
			</td>
             <td align="right">代理车型：</td>
             <td align="left">
             	<input type="text"  class="middle_txt" name="PROXY_VEHICLE_TYPE"  id="PROXY_VEHICLE_TYPE" value="${dMap.PROXY_VEHICLE_TYPE}" maxlength="10"/>
             </td>
           </tr> 
	       <tr>
	        <td align="right" width="15%">备注：</td>
	      	<td align="left" colspan="3">
	      		<textarea name="REMARK" id="REMARK" style="width: 90%;" rows="2" datatype="1,is_textarea,1000"></textarea>
	      	</td>
	      </tr>
			</table>
			</td>
    	  <tr>
    	   <tr id="kpxx" >
			<td width="100%" colspan=4>
			<table  width=100% border="0" align="center" cellpadding="1" cellspacing="1"> 
	      <tr>
			<th colSpan=4 align=left><IMG class=nav src="<%=contextPath%>/img/subNav.gif"> 开票信息</th>
    	  <tr>
    	  <tr>
			<td align="right" width="15%">开票名称：</td>
			<td class="table_query_4Col_input" nowrap="nowrap"> 
				<input type="text"  class="middle_txt" id="ERP_CODE" name="ERP_CODE" value="${dMap.ERP_CODE}" />
			</td>
			<td align="right" width="15%">税号：</td>
			<td class="table_query_4Col_input" nowrap="nowrap"> 
				<input type="text"  class="middle_txt" id="TAXES_NO" name="TAXES_NO" value="${dMap.TAXES_NO}"   />
			</td>
		 </tr>
		 <tr>
			<td align="right" width="15%">开户行：</td>
			<td class="table_query_4Col_input" nowrap="nowrap"> 
				<input type="text"  class="middle_txt" id="BEGIN_BANK" name="BEGIN_BANK" value="${dMap.BEGIN_BANK}" />
			</td>
			<td align="right" width="15%">账号：</td>
			<td class="table_query_4Col_input" nowrap="nowrap"> 
				<input type="text"  class="middle_txt" id="INVOICE_ACCOUNT" name="INVOICE_ACCOUNT" value="${dMap.INVOICE_ACCOUNT}" />
			</td>
		 </tr>
		 <tr>
			<td align="right" width="15%">开票地址：</td>
			<td class="table_query_4Col_input" nowrap="nowrap"> 
				<input type="text"  class="middle_txt" id="INVOICE_ADD" name="INVOICE_ADD" value="${dMap.INVOICE_ADD}"/>
			</td>
			<td align="right" width="15%">电话：</td>
			<td class="table_query_4Col_input" nowrap="nowrap"> 
				<input type="text"  class="middle_txt" id="INVOICE_PHONE" name="INVOICE_PHONE" value="${dMap.INVOICE_PHONE}" datatype="1,is_phone,20"/>
			</td>
		 </tr>
		 <tr>
		   <td align="right">开票联系人：</td>
		   <td class="table_query_4Col_input" nowrap="nowrap"><input type="text"  class="middle_txt" id="INVOICE_PERSION" name="INVOICE_PERSION" value="${dMap.INVOICE_PERSION}" /></td>
		   <td align="right" nowrap="nowrap" class="table_query_4Col_input">开票联系人手机：</td>
		   <td class="table_query_4Col_input" nowrap="nowrap"><input type="text"  class="middle_txt" id="INVOICE_TELPHONE" name="INVOICE_TELPHONE" value="${dMap.INVOICE_TELPHONE}" /></td>
		   </tr>
		 <tr>
		   <td align="right">纳税人识别号：</td>
		   <td class="table_query_4Col_input" nowrap="nowrap"><input type="text"  class="middle_txt" id="TAXPAYER_NO" name="TAXPAYER_NO" value="${dMap.TAXPAYER_NO}" /></td>
		   <td align="right">增值税发票：</td>
		   <td class="table_query_4Col_input" nowrap="nowrap"><input type="text"  class="middle_txt" id="TAX_INVOICE" name="TAX_INVOICE" value="${dMap.TAX_INVOICE}"/></td>
		   </tr>
		 </table>
         <table  width=100% border="0" align="center" cellpadding="1" cellspacing="1"> 
	      <tr>
			<th colSpan=4 align=left><IMG class=nav src="<%=contextPath%>/img/subNav.gif">国家品牌</th>
    	  
          <tr>
			<td align="right" width="15%">国家品牌授权城市：</td>
			<td class="table_query_4Col_input" nowrap="nowrap"> 
				<script type="text/javascript">
					genSelBoxExp("IS_AUTHORIZE_CITY",<%=Constant.IF_TYPE%>,"${dMap.IS_AUTHORIZE_CITY}",true,"short_sel",'',"false",'');
				</script>
			</td>
			<td align="right" width="15%">国家品牌授权区县：</td>
			<td class="table_query_4Col_input" nowrap="nowrap"> 
				<script type="text/javascript">
					genSelBoxExp("IS_AUTHORIZE_COUNTY",<%=Constant.IF_TYPE%>,"${dMap.IS_AUTHORIZE_COUNTY}",true,"short_sel",'',"false",'');
				</script>
			</td>
		 </tr>
          <tr>
            <td align="right">国家品牌授权：</td>
            <td class="table_query_4Col_input" nowrap="nowrap"><input type="text"  class="middle_txt" id="AUTHORIZE_BRAND" name="AUTHORIZE_BRAND" value="${dMap.AUTHORIZE_BRAND}" /></td>
            <td align="right">国家品牌授权信息收集时间：</td>
            <td class="table_query_4Col_input" nowrap="nowrap">
            	<input name="AUTHORIZE_GET_DATE" type="text" class="short_time_txt" id="AUTHORIZE_GET_DATE" readonly="readonly" value="${dMap.AUTHORIZE_GET_DATE}"/>
            	<input name="button14" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'AUTHORIZE_GET_DATE', false);" />
            </td>
          </tr>
          <tr>
            <td align="right">国家品牌授权提交时间：</td>
            <td class="table_query_4Col_input" nowrap="nowrap">
            	<input name="AUTHORIZE_SUB_DATE" type="text" class="short_time_txt" id="AUTHORIZE_SUB_DATE" readonly="readonly" value="${dMap.AUTHORIZE_SUB_DATE}"/>
            	<input name="button10" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'AUTHORIZE_SUB_DATE', false);" />
            </td>
            <td align="right">国家品牌授权起始时间：</td>
            <td class="table_query_4Col_input" nowrap="nowrap">
            	<input name="AUTHORIZE_EFFECT_DATE" type="text" class="short_time_txt" id="AUTHORIZE_EFFECT_DATE" readonly="readonly" value="${dMap.AUTHORIZE_EFFECT_DATE}"/>
            	<input name="button13" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'AUTHORIZE_EFFECT_DATE', false);" />
            </td>
          </tr>
          <tr>
            <td align="right">工商总局公告号：</td>
            <td class="table_query_4Col_input" nowrap="nowrap">
            	<input name="ANNOUNCEMENT_NO " type="text" class="short_time_txt" id="ANNOUNCEMENT_NO " value="${dMap.ANNOUNCEMENT_NO}"/>
            </td>
            <td align="right">工商总局公布日期：</td>
            <td class="table_query_4Col_input" nowrap="nowrap">
            	<input name="ANNOUNCEMENT_DATE" type="text" class="short_time_txt" id="ANNOUNCEMENT_DATE" readonly="readonly" value="${dMap.ANNOUNCEMENT_DATE}"/>
            	<input name="button15" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'ANNOUNCEMENT_DATE', false);" />
            </td>
          </tr>
          <tr>
            <td align="right">国家品牌授权截止时间：</td>
            <td class="table_query_4Col_input" nowrap="nowrap">
            	<input name="ANNOUNCEMENT_END_DATE" type="text" class="short_time_txt" id="ANNOUNCEMENT_END_DATE" readonly="readonly" value="${dMap.ANNOUNCEMENT_END_DATE}"/>
            	<input name="button12" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'ANNOUNCEMENT_END_DATE', false);" />
            </td>
            <td align="right">&nbsp;</td>
            <td class="table_query_4Col_input" nowrap="nowrap">&nbsp;</td>
          </tr>
         </table>
			</td>
    	  <tr>
          
	 </table> 
     <table class=table_query>
	 <tr>
	 <td align="center">
	<input type="button" value="保存" name="saveBtn" class="normal_btn" onclick="saveDealerInfo()"/>	&nbsp;&nbsp;
	<input type="button" value="返回" name="cancelBtn"  class="normal_btn" onclick="history.back();" /></td>
	</tr>
   </table>
</form>

<script type="text/javascript" >

function saveDealerInfo()
{    
	
	if(document.getElementById("CITY_ID").value == '') {MyAlert('请选择城市!'); return;}
	
	if(document.getElementById("COUNTIES").value == '') {MyAlert('请选择区县!'); return;}
	
	if(document.getElementById("SERVICE_STATUS").value == '') {MyAlert("请选择经销商状态!"); return;}
	
	if(document.getElementById("HOTLINE").value == '') {MyAlert("请输入销售热线!"); return;}
	
	makeNomalFormCall('<%=contextPath%>/sysmng/dealer/DealerInfo/checkDealerCode.json',showResultCodeCheck,'fm');
}
function showResultCodeCheck(obj){
	var msg=obj.msg;
	if(msg=='true'){
		if(confirm("信息可处理,是否继续?"))
		{
			var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/saveDealerInfo.json";
			makeNomalFormCall(url,function(json){
				if(json.Exception) {
					MyAlert(json.Exception);
				} else {
					MyAlert(json.message);
					window.location.href = "<%=contextPath%>/sysmng/dealer/DealerInfo/queryDealerInfoInit.do";
				}
			},'fm','');
		}		
	}else if(msg == "false"){
		MyAlert('经销商代码已经存在,请换其它代码!');
	}
	/**else if(msg == "1"){
		MyAlert('经销商公司已经被使用,请选择其他经销商公司!');
		return;
	}**/
}
</script>
<script type="text/javascript">
	genLocSel('PROVINCE_ID','CITY_ID','COUNTIES');
	
	var p = document.getElementById("PROVINCE_ID");
	setItemValue('PROVINCE_ID', '${dMap.PROVINCE_ID}');
	_genCity(p,'CITY_ID');
	var c = document.getElementById("CITY_ID");
	setItemValue('CITY_ID', '${dMap.CITY_ID}');
	_genCity(c,'COUNTIES');
	var t = document.getElementById("COUNTIES");
	setItemValue('COUNTIES', '${dMap.COUNTIES}');

	changeDealerlevel();
</script>
</body>
</html>
