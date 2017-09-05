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
<title>经销商维护</title>
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
	
	var orgIdObj = document.getElementById("orgId");
	var rootOrgId = document.getElementById("ROOT_ORG_ID").value;
	for (var i = 0; i < orgIdObj.length; i++) {
		if(orgIdObj[i].value==rootOrgId){
			orgIdObj[i].selected=true;
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
function changeDealerlevel(){
	var dealerLevel = document.getElementById("DEALER_LEVEL").value;
   	var dealerType = document.getElementById("DEALER_TYPE").value;

	if(dealerLevel == '<%=Constant.DEALER_LEVEL_01 %>')
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
</script>
</head>
<body onunload='javascript:destoryPrototype()' onload="">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;<c:if test="${is_Add == true}">新增经销商</c:if><c:if test="${is_Mod == true}">修改经销商</c:if></div>
 <form method="post" name = "fm"  id="fm">
 	<input id="COMPANY_ID" name="COMPANY_ID" type="hidden" value="${companyInfo.companyId }"/>
 	<c:if test="${ is_Mod == true}"><input id="DEALER_ID" name="DEALER_ID" type="hidden" value="${dealerInfo.dealerId }"/></c:if>
 	<input id="ROOT_ORG_ID" name="ROOT_ORG_ID" type="hidden" value="${vwOrgDealerInfo.rootOrgId }"/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    	  <TR>
			<TH colSpan=4 align=left><IMG class=nav src="<%=contextPath%>/img/subNav.gif"> 经销商基础信息</TH>
    	  <tr>
    	  <tr>
    	  	<td align="right">组织机构代码：</td>
    	  	<td>
    	  	<c:if test="${is_Mod == true }">
    	  		<input type='text' class="middle_txt" name="COMPANY_ZC_CODE" id="COMPANY_ZC_CODE"  value="${dMap.COMPANY_ZC_CODE }" maxlength="150"/></c:if>
    	  		    	  	<c:if test="${is_Add == true }">
    	  		<input type='text' class="middle_txt" name="COMPANY_ZC_CODE" id="COMPANY_ZC_CODE"  value="${COMPANY_ZC_CODE }" maxlength="150"/></c:if>
    	  	</td>
            <!--<td align="right">经营类型：</td>
             <td align="left">
             	<select id="SHOP_TYPE" name="SHOP_TYPE">
             		<option value="代理">代理</option>
              		<option value="直营">重点客户</option> 
             	</select>
             	<script type="text/javascript">
             		setItemValue('SHOP_TYPE', '${dMap.SHOP_TYPE}');
             	</script>
             </td>
          --></tr>
		  <tr>
		    <td align="right">经销商公司：</td>
		    <td align="left">
            	<input class="middle_txt"  datatype="0,is_null,75"  id="COMPANY_NAME" style="cursor: pointer;" name="COMPANY_NAME" type="text" readonly="readonly"  value="${companyInfo.companyName }"/>
            </td>
		    <td align="right" width="15%">分销网点简称：</td>
		    <c:if test="${is_Mod == true }">
		    <td align="left">
		    	<input type='text'  class="middle_txt" name="DEALER_SHORTNAME"  id="DEALER_SHORTNAME" datatype="0,is_null,150"  value="${dealerInfo.dealerShortname }" maxlength="150"/>
		    </td></c:if>
		    <c:if test="${is_Add ==true}">
		    <td align="left">
		    	<input type='text'  class="middle_txt" name="DEALER_SHORTNAME"  id="DEALER_SHORTNAME" datatype="0,is_null,150"  value="" maxlength="150"/>
		    </td></c:if>
      </tr>
		  <tr>
		    <td align="right" width="15%">分销网点代码：</td>
		    <c:if test="${is_Add ==true}">
		    <td align="left"><input type='text'  class="middle_txt" name="DEALER_CODE"  id="DEALER_CODE" datatype="0,is_name,20" readonly="readonly" value="${dealer_code_secend}" /></c:if>
		      <c:if test="${is_Mod ==true}"><td align="left"><input type='text'  class="middle_txt" name="DEALER_CODE"  id="DEALER_CODE" datatype="0,is_name,20" readonly="readonly" value="${dealerInfo.dealerCode}" /></c:if>
		    
		    
		    </td>
		    <td align="right" width="15%">分销网点全称：</td>
		    <c:if test="${is_Add ==true}">
		    <td align="left">
		    	<input type='text'  class="middle_txt" name="DEALER_NAME"  id="DEALER_NAME" datatype="0,is_null,150"  value="" maxlength="150"/>
		    </td></c:if>
		    <c:if test="${is_Mod ==true}">
		    <td align="left">
		    	<input type='text'  class="middle_txt" name="DEALER_NAME"  id="DEALER_NAME" datatype="0,is_null,150"  value="${dealerInfo.dealerName }" maxlength="150"/>
		    </td></c:if>
	      </tr>
	       <tr>
		    <td align="right" width="15%">经销商等级：</td>
				<td align="left"><select id="DEALER_LEVEL" name="DEALER_LEVEL">
						<option value="10851002">二级经销商</option>
				</select></td>
	      </tr>
	        <tr>
		    <td align="right" width="15%">上级组织：</td>
		    <td align="left">
			    <input type="text"  name="orgCode" size="15" value="${vwOrgDealerInfo.orgCode}"  id="orgCode" class="middle_txt" readonly="readonly" datatype="0,is_null,150"/>
			    <input name="orgbu"  id="orgbu" type="hidden"  value="${vwOrgDealerInfo.orgId}"/>
			    <input type="hidden" id="DEALER_ORG_ID" name="DEALER_ORG_ID" datatype="0,is_null,150"/>
		    </td>
		    <td align="right" width="15%">上级经销商：</td>
		    <td align="left">
			    <input type="text"  name="PARENT_DEALER_NAME" size="15" value="${dealerInfo.dealerName }"  id="PARENT_DEALER_NAME" class="middle_txt" readonly="readonly" />
			    <c:if test="${is_Mod == true }">
			    <input type="text"  name="PARENT_DEALER_D"  value="${dealerInfo.parentDealerD }"  id="PARENT_DEALER_D" style="display:none;"/></c:if>
			    <c:if test="${is_Add == true }">
			    <input type="text"  name="PARENT_DEALER_D"  value="${dealerInfo.dealerId }"  id="PARENT_DEALER_D" style="display:none;"/></c:if>
<%-- 			    <input name="dealerBtn" disabled="disabled" id="dealerBtn" type="button" class="mark_btn" onclick="showOrgDealer('PARENT_DEALER_NAME','PARENT_DEALER_D','false','','false','','<%=Constant.DEALER_TYPE_DVS %>','')" value="&hellip;" /> --%>
		    </td>
	      </tr>
           <tr> <td align=right>选择大区：</td>
		        <td align=left>
					<select id="orgId" name="orgId" ">
					<option value="">-请选择-</option>
		        		<c:forEach items="${orglist }" var="list">
		        			<option value="${list.ORG_ID }">${list.ORG_NAME }</option>
		        		</c:forEach>
		        	</select>
				</td>
              <td align="right">省份：</td>
              <td align="left"><select class="min_sel" id="PROVINCE_ID" name="PROVINCE_ID" onchange="_genCity(this,'CITY_ID')"></select></td>
      </tr>
           <tr> <td align="right">城市：</td>
              <td align="left"><select class="min_sel" id="CITY_ID" name="CITY_ID" onchange="_genCity(this,'COUNTIES')"></select></td>
           
             <td align="right">区县：</td>
              <td align="left"><select class="min_sel" id="COUNTIES" name="COUNTIES" datatype="0,is_null,200"></select></td>
                   </tr> 
                   <tr>
                   <td align="right">邮编：</td>
                   <c:if test="${is_Mod == true }">
              <td align="left">
              	<input type='text' class="middle_txt" name="ZIP_CODE"  id="ZIP_CODE" datatype="1,is_digit,20" value="${dealerInfo.zipCode}" /></td> </c:if>
             <c:if test="${is_Add == true }">
              <td align="left">
              	<input type='text' class="middle_txt" name="ZIP_CODE" datatype="1,is_digit,20" id="ZIP_CODE" /></td> </c:if>
              	</tr>
                   
      <!--<tr>
             <td align="right">经销企业注册资金：</td>
	          <td align="left" >
	          <c:if test="${is_Mod == true }">
	          	<input type='text'  class="middle_txt" name="REGISTERED_CAPITAL" datatype="0,is_money_z,20" id="REGISTERED_CAPITAL" value="${dMap.REGISTERED_CAPITAL}" maxlength="20" />
	           </c:if>
	           	<c:if test="${is_Add == true }">
	          	<input type='text'  class="middle_txt" name="REGISTERED_CAPITAL" datatype="0,is_money_z,20" id="REGISTERED_CAPITAL" value="" maxlength="20" />
	           </c:if>
	          </td>
	          <td align="right">&nbsp;</td>
	          <td align="left">&nbsp;</td>
      </tr>
      		<tr>
             <td align="right">企业注册地址：</td>
              <c:if test="${is_Mod == true }">
	          <td align="left"  colspan="3"><input type='text'  class="middle_txt" name="ADDRESS"  id="ADDRESS" value="${dealerInfo.address}" style="width:380px;"/></td> </c:if>
	                        <c:if test="${is_Add == true }">
	          <td align="left"  colspan="3"><input type='text'  class="middle_txt" name="ADDRESS"  id="ADDRESS" value="" style="width:380px;"/></td> </c:if>
      		</tr>
      		<tr>-->
             <td align="right">销售展厅地址：</td>
	          <td align="left" colspan="3"><input type='text'  class="middle_txt" name="COMPANY_ADDRESS"  id="COMPANY_ADDRESS"  value="${dMap.COMPANY_ADDRESS}" maxlength="300" style="width:380px;"/><font color="red">*</font></td>
      		</tr>
           <tr>
             <td align="right">负责人：</td>
             <c:if test="${is_Mod == true }">
             <td align="left"><input type='text'  class="middle_txt" name="LEGAL"  id="LEGAL" datatype="0,is_name,20"  value="${dealerInfo.legal}" /></td></c:if>
             <c:if test="${is_Add == true }">
             <td align="left"><input type='text'  class="middle_txt" name="LEGAL"  id="LEGAL" datatype="0,is_name,20" /></td></c:if>
             <td align="right">负责人手机：</td>
             <c:if test="${is_Add == true }">
             <td align="left"><input type='text' datatype="1,is_phone,20" class="middle_txt" name="LEGAL_TELPHONE"  id="LEGAL_TELPHONE" /><font color="red">*</font></td></c:if>
             <c:if test="${is_Mod == true }">
             <td align="left"><input type='text' datatype="1,is_phone,20" class="middle_txt" name="LEGAL_TELPHONE"  id="LEGAL_TELPHONE"  value="${dealerInfo.legalTelphone}" /><font color="red">*</font></td></c:if>
           </tr>
           <tr>
             <td align="right">负责人办公电话：</td>
             <c:if test="${is_Add == true }">
             <td align="left"><input type='text'  class="middle_txt" name="LEGAL_PHONE"  id="LEGAL_PHONE" datatype="1,is_phone,20" value=""/></td></c:if>
             <c:if test="${is_Mod == true }">
             <td align="left"><input type='text'  class="middle_txt" name="LEGAL_PHONE"  id="LEGAL_PHONE"  datatype="1,is_phone,20" value="${dealerInfo.legalPhone}"/></td></c:if>
             <td align="right">负责人邮箱：</td>
             <c:if test="${is_Add == true }">
             <td align="left"><input type='text'  class="middle_txt" name="LEGAL_EMAIL"  id="LEGAL_EMAIL"  value=""/></td></c:if>
             <c:if test="${is_Mod == true }">
             <td align="left"><input type='text'  class="middle_txt" name="LEGAL_EMAIL"  id="LEGAL_EMAIL"  value="${dealerInfo.legalEmail}"/></td></c:if>
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
							genSelBoxExp("SERVICE_STATUS",'<%=Constant.DLR_SERVICE_STATUS_SECEND%>',"${dealerInfo1.serviceStatus}",'',"short_sel",'',"false",'');
					</script>
					<font color="red">*</font>
		    </td>
	      </tr>
	      <tr>
	      	<!--<td align="right" width="15%">单位性质：</td>
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
	      	</td>-->
	      	<td align="right" width="15%">经销商传真：</td>
	      	<td>
	      		<input type="text" name="FAX_NO" id="FAX_NO" class="middle_txt" value="${dMap.FAX_NO }"/>
	      	</td>
	      </tr>
	            <c:if test="${is_Add == true}">
 <tr>
   <td colspan="5">
     <jsp:include page="${contextPath}/uploadDiv.jsp" /> 
     <input type="button" class="normal_btn"  onclick="showDealerUpload('<%=contextPath%>')" value ='添加附件'/>
 	</td>
 </tr></c:if>
 	            <c:if test="${is_Mod == true}">
 <tr>
   <td colspan="5">
     <jsp:include page="${contextPath}/uploadDiv.jsp" /> 
     <input type="button" class="normal_btn"  onclick="showDealerUpload('<%=contextPath%>')" value ='添加附件'/>
 	</td>
 	  		 <c:if test="${attachList!=null}">
  			<c:forEach items="${attachList}" var="list">
  				<script type="text/javascript">
  				addUploadRowByDbView_01('${list.FILENAME}','${list.FILEID}','${list.FILEURL}','${list.FJID}');
	 	 		</script>
  			</c:forEach>
  		 </c:if>
 </tr></c:if>
	      <tr id="zcxs">
			<td width="100%" colspan=4>
			<table  width=100% border="0" align="center" cellpadding="1" cellspacing="1">
			<tr>
			<th colSpan=4 align=left><img class=nav src="<%=contextPath%>/img/subNav.gif"> <span id="up_name_00">整车销售信息</span></th>
			</tr>
             <tr >
               <td align="right">服务经理：</td>
               <td align="left"><input type='text'  class="middle_txt" name="WEBMASTER_NAME"  id="WEBMASTER_NAME"   value="${dMap.WEBMASTER_NAME}" /></td>
               <td width="15%" align="right">服务经理办公电话：</td>
               <td align="left"><input type='text'  class="middle_txt" name="WEBMASTER_PHONE" datatype="1,is_phone,20" id="WEBMASTER_PHONE"   value="${dMap.WEBMASTER_PHONE}"/></td>
             </tr>
             <tr >
               <td align="right">服务经理手机：</td>
               <td align="left"><input type='text'  class="middle_txt" name="WEBMASTER_TELPHONE"  id="WEBMASTER_TELPHONE"   value="${dMap.WEBMASTER_TELPHONE}" datatype="1,is_phone,20"/></td>
               <td align="right">服务经理邮箱：</td>
               <td align="left"><input type='text'  class="middle_txt" name="WEBMASTER_EMAIL"  id="WEBMASTER_EMAIL"   value="${dMap.WEBMASTER_EMAIL}" /></td>
             </tr>
             <!--<tr >
               <td align="right">销售经理：</td>
               <td align="left"><input type='text'  class="middle_txt" name="MARKET_NAME"  id="MARKET_NAME"   value="${dMap.MARKET_NAME}" /></td>
               <td align="right">销售经理办公电话：</td>
               <td align="left"><input type='text'  class="middle_txt" name="MARKET_PHONE"  id="MARKET_PHONE"   value="${dMap.MARKET_PHONE}" datatype="1,is_phone,20"/></td>
             </tr>
             <tr >
               <td align="right">销售经理手机：</td>
               <td align="left"><input type='text'  class="middle_txt" name="MARKET_TELPHONE"  id="MARKET_TELPHONE"  datatype="1,is_phone,20" value="${dMap.MARKET_TELPHONE}" /></td>
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
             --><tr >
               <td align="right">信息员：</td>
               <td align="left"><input type='text'  class="middle_txt" name="MESSAGER_NAME"  id="MESSAGER_NAME"   value="${dMap.MESSAGER_NAME}" datatype="0,is_name,20"/></td>
               <td align="right">信息员办公电话：</td>
               <td align="left"><input type='text'  class="middle_txt" name="MESSAGER_PHONE"  id="MESSAGER_PHONE"   value="${dMap.MESSAGER_PHONE}" datatype="1,is_phone,20"/></td>
             </tr>
             <tr >
               <td align="right">信息员手机：</td>
               <td align="left"><input type='text'  class="middle_txt" name="MESSAGER_TELPHONE"  id="MESSAGER_TELPHONE"   value="${dMap.MESSAGER_TELPHONE}" datatype="0,is_phone,20"/></td>
               <td align="right">信息员QQ：</td>
               <td align="left"><input type='text'  class="middle_txt" name="MESSAGER_QQ"  id="MESSAGER_QQ"   value="${dMap.MESSAGER_QQ}" /></td>
             </tr>
             <tr >
               <td align="right">信息员邮箱：</td>
               <td align="left"><input type='text'  class="middle_txt" name="MESSAGER_EMAIL"  id="MESSAGER_EMAIL"   value="${dMap.MESSAGER_EMAIL}" /></td>
               <td align="right">销售热线：</td>
               <td align="left"><input type='text'  class="middle_txt" name="HOTLINE"  id="HOTLINE" datatype="0,is_phone,20"  value="${dMap.HOTLINE}" /></td>
             </tr>
           <tr >
             <!--<td align="right">是否经营其他品牌：</td>
             <td align="left">
             	<script type="text/javascript">
					genSelBoxExp("IS_ACTING_BRAND",<%=Constant.IF_TYPE%>,"${dMap.IS_ACTING_BRAND}",true,"short_sel",'',"false",'');
				</script>
             </td>
             --><td align="right">代理其它品牌名称：</td>
             <td align="left">
             	<input type="text"  class="middle_txt" name="ACTING_BRAND_NAME"  id="ACTING_BRAND_NAME"  value="${dMap.ACTING_BRAND_NAME}" />
             </td>
           </tr>
           <tr>
            <td align="right">代理区域：</td>
      		<td class="table_query_4Col_input" nowrap="nowrap">
      			<input type="text"  readonly="readonly"  name="PROXY_AREA_NAME" size="15" value="${ProxyAreaName}"  id="PROXY_AREA_NAME" class="middle_txt" /><font color="red">*</font>
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
        	 <td align="right">是否具备自有服务网点：</td>
							<c:if test="${is_Add == true }">
								<td align="left"><select name="HAVE_SERVICE" id="HAVE_SERVICE">
										<option value="是">是</option>
										<option value="否">否</option>
										</select><font color="red">*</font></td>
							</c:if>
							<c:if test="${is_Mod == true }">
								<td align="left"><select name="HAVE_SERVICE" id="HAVE_SERVICE">
										<c:choose>
											<c:when test="${dMap.HAVE_SERVICE=='是'}">
												<option value="是">是</option>
												<option value="否">否</option>
											</c:when>
											<c:otherwise>
												<option value="是">是</option>
												<option selected="selected" value="否">否</option>
											</c:otherwise>
										</c:choose>
								</select><font color="red">*</font></td>
							</c:if>
							</td>
             <td align="right">服务站面积(平方米)：</td>
             <c:if test="${is_Add == true }">
             <td align="left">
             	<input type="text"  class="middle_txt" name="SERVICE_AREA"  id="SERVICE_AREA" />
             </td></c:if>
             <c:if test="${is_Mod == true }">
             <td align="left">
             	<input type="text"  class="middle_txt" name="SERVICE_AREA"  id="SERVICE_AREA" value="${dMap.SERVICE_AREA }"/>
             </td></c:if>
           </tr> 
                      <tr>
        	 <td align="right">服务站地址：</td>
        	 <c:if test="${is_Add == true }">
             <td align="left">
             	<input type="text"  class="middle_txt" name="SERVICE_ADDRESS"  id="SERVICE_ADDRESS" />
             </td></c:if>
        	 <c:if test="${is_Mod == true }">
             <td align="left">
             	<input type="text"  class="middle_txt" name="SERVICE_ADDRESS"  id="SERVICE_ADDRESS" value="${dMap.SERVICE_ADDRESS }"/>
             </td></c:if>             
			</td>
             <td align="right">24小时服务热线：</td>
             <c:if test="${is_Add == true }">
             <td align="left">
             	<input type="text"  class="middle_txt" name="SERVICE_HOTLINE" datatype="1,is_phone,20"  id="SERVICE_HOTLINE" />
             </td></c:if>
             <c:if test="${is_Mod == true }">
             <td align="left">
             	<input type="text"  class="middle_txt" name="SERVICE_HOTLINE" datatype="1,is_phone,20"  id="SERVICE_HOTLINE" value="${dMap.SERVICE_HOTLINE }"/>
             </td></c:if>
           </tr> 
                      <tr>
        	 <td align="right">最低库存：</td>
        	 <c:if test="${is_Add == true }">
             <td align="left">
             	<input type="text"  class="middle_txt" name="MIN_STOCK" datatype="0,is_digit,20" id="MIN_STOCK" />
             </td></c:if>
        	 <c:if test="${is_Mod == true }">
             <td align="left">
             	<input type="text"  class="middle_txt" name="MIN_STOCK" datatype="0,is_digit,20" id="MIN_STOCK" value="${dMap.MIN_STOCK }"/>
             </td></c:if>             
			</td>
             <td align="right">北汽幻速专营面积（平方米）：</td>
             <c:if test="${is_Add == true }">
             <td align="left">
             	<input type="text"  class="middle_txt" name="OEM_AREA" datatype="1,is_digit,20" id="OEM_AREA" />
             </td></c:if>
                          <c:if test="${is_Mod == true }">
             <td align="left">
             	<input type="text"  class="middle_txt" name="OEM_AREA" datatype="1,is_digit,20" id="OEM_AREA" value="${dMap.OME_AREA }"/>
             </td></c:if>
           </tr> 
           <tr>
        	 <td align="right">北汽幻速专营销售人员数：</td>
        	 <c:if test="${is_Add == true }">
             <td align="left">
             	<input type="text"  class="middle_txt" name="OEM_PEOPLE_TOTAL" datatype="1,is_digit,20" id="OEM_PEOPLE_TOTAL" />
             </td></c:if>
             <c:if test="${is_Mod == true }">
             <td align="left">
             	<input type="text"  class="middle_txt" name="OEM_PEOPLE_TOTAL" datatype="1,is_digit,20" id="OEM_PEOPLE_TOTAL" value="${dMap.OME_PEOPLE_TOTAL }"/>
             </td></c:if>
             <td align="right">全年任务量：</td>
             <c:if test="${is_Add == true }">
             <td align="left">
             	<input type="text"  class="middle_txt" name="YEAR_PLAN" datatype="0,is_digit,20" id="YEAR_PLAN" />
             </td></c:if>
             <c:if test="${is_Mod == true }">
             <td align="left">
             	<input type="text"  class="middle_txt" name="YEAR_PLAN" datatype="0,is_digit,20" id="YEAR_PLAN" value="${dMap.YEAR_PLAN }"/>
             </td></c:if>
           </tr> 
           <tr>
        	 <td align="right">分销网点邮箱：</td>
        	 <c:if test="${is_Mod == true }">
             <td align="left">
             	<input type="text"  class="middle_txt" name="EMAIL"  id="EMAIL" value="${dMap.EMAIL }"/>
             </td></c:if>
             <c:if test="${is_Add== true }">
             <td align="left">
             	<input type="text"  class="middle_txt" name="EMAIL"  id="EMAIL"/>
             </td></c:if>
           </tr>
           <tr >
               <td align="right">整车收货联系人：</td>
               <td align="left"><input type='text'  class="middle_txt" name="VHCL_SH_NAME"  id="VHCL_SH_NAME" value="${dMap.VHCL_SH_NAME }" datatype="0,is_name,20"/></td>
               <td align="right">整车收货联系人手机：</td>
               <td align="left"><input type='text'  class="middle_txt" name="VHCL_SH_PHONE"  id="VHCL_SH_PHONE"   value="${dMap.VHCL_SH_PHONE }" datatype="0,is_phone,11"/></td>
             </tr>
             <tr >
               <td align="right">整车收货联系人性别：</td>
               <td align="left">
               		<script type="text/javascript">
						genSelBoxExp("VHCL_SH_SEX",<%=Constant.GENDER_TYPE%>,"${dMap.VHCL_SH_SEX }",false,"short_sel",'',"false",'');
					</script>
               </td>
               <td align="right">整车收货联系人办公电话：</td>
               <td align="left"><input type='text'  class="middle_txt" name="VHCL_SH_TEL"  id="VHCL_SH_TEL"   value="${dMap.VHCL_SH_TEL }" /></td>
             </tr>
             <tr >
               <td align="right">整车收货地址：</td>
               <td align="left"><input type='text'  class="middle_txt" name="VHCL_SH_ADDRESS"  id="MESSAGER_NAME" maxlength="300" style="width:380px;"   value="${dMap.VHCL_SH_ADDRESS }" datatype="0,is_null,300"/></td>
             </tr>
           <tr > 
			</table>
			</td>
    	  <tr>
    	   <tr id="kpxx" >
			<td width="100%" colspan=4>
			</td>
    	  <tr>
    	  
    	  <tr>
    	  	<td colspan="4" width="100%">
    	  		<table width=100% border="0" align="center" cellpadding="1" cellspacing="1">
    	  			<tr>
    	  				<th colspan="4" align="left"><img class=nav src="<%=contextPath%>/img/subNav.gif"> <span id="up_name_00">其它信息</span></th>
    	  			</tr>
    	  			<tr>
    	  				<td align="right">二级网络性质：</td>
    	  				<td align="left">
    	  					<script type="text/javascript">
    	  						genSelBoxExp("SECOND_LEVEL_NETWORK_NATURE",<%=Constant.SECOND_LEVEL_NETWORK_NATURE%>,"${tmDealerSecondLevelPO.secondLevelNetworkNature }",false,"short_sel",'',"false",'');
							</script><font color="red">*</font>
        	  			</td>
    	  				<td align="right">竞品品牌：</td>
    	  				<td align="left"><input type="text"  class="middle_txt" datatype="1,is_null,20" name="COMPETING_BRAND"  id="COMPETING_BRAND" value="${tmDealerSecondLevelPO.competingBrand }"/></td>
    	  			</tr>
    	  			<tr>
    	  				<td align="right">与竞品行驶距离（米）：</td>
    	  				<td align="left">
    	  					<input type="text"  class="middle_txt" datatype="1,is_double,10" decimal="2" name="AND_COMPETING_RUN_DISTANCE"  id="AND_COMPETING_RUN_DISTANCE" value="${tmDealerSecondLevelPO.andCompetingRunDistance }"/>
        	  			</td>
    	  				<td align="right">月均销量：</td>
    	  				<td align="left"><input type="text"  class="middle_txt" datatype="1,is_digit,20" name="MONTH_AVERAGE_SALES"  id="MONTH_AVERAGE_SALES" value="${tmDealerSecondLevelPO.monthAverageSales }"/></td>
    	  			</tr>
    	  			<tr>
    	  				<td align="right">门头长度：</td>
    	  				<td align="left"><input type="text"  class="middle_txt" datatype="1,is_digit,20" name="DOORHEAD_LENGTH"  id="DOORHEAD_LENGTH" value="${tmDealerSecondLevelPO.doorheadLength }"/></td>
    	  			</tr>
    	  			<tr>
    	  				<td align="right">是否具有销售门头：</td>
    	  				<td align="left">
    	  					<script type="text/javascript">
								genSelBoxExp("IS_HAVE_SALES_DOORHEAD",<%=Constant.IF_TYPE%>,"${tmDealerSecondLevelPO.isHaveSalesDoorhead}",true,"short_sel",'',"false",'');
							</script>
        	  			</td>
    	  				<td align="right">是否具有销售形象墙：</td>
    	  				<td align="left">
    	  					<script type="text/javascript">
								genSelBoxExp("IS_HAVE_SALES_IMAGE_WALL",<%=Constant.IF_TYPE%>,"${tmDealerSecondLevelPO.isHaveSalesImageWall}",true,"short_sel",'',"false",'');
							</script>
    	  				</td>
    	  			</tr>
    	  			<!--<tr>
    	  				<td align="right">代理区域人口数量：</td>
    	  				<td align="left">
    	  					<input type="text"  class="middle_txt" datatype="0,is_digit,20" name="AGENT_ZONE_POPULATION_COUNT"  id="AGENT_ZONE_POPULATION_COUNT" value="${tmDealerSecondLevelPO.agentZonePopulationCount }"/>
        	  			</td>
    	  				<td align="right">销售顾问（人员数量）：</td>
    	  				<td align="left"><input type="text"  class="middle_txt" datatype="0,is_digit,20" name="SALES_CONSULTANT_COUNT"  id="SALES_CONSULTANT_COUNT" value="${tmDealerSecondLevelPO.salesConsultantCount }"/></td>
    	  			</tr>
    	  			--><tr>
    	  				<td align="right">服务网点性质：</td>
    	  				<td align="left">
    	  					<script type="text/javascript">
								genSelBoxExp("SERVICE_NETWORK_NATURE",<%=Constant.SERVICE_NETWORK_NATURE%>,"${tmDealerSecondLevelPO.serviceNetworkNature}",true,"short_sel",'',"false",'');
							</script>
        	  			</td>
    	  				<td align="right">维修资质：</td>
    	  				<td align="left">
    	  					<script type="text/javascript">
								genSelBoxExp("REPAIR_APTITUDE",<%=Constant.REPAIR_APTITUDE%>,"${tmDealerSecondLevelPO.repairAptitude}",true,"short_sel",'',"false",'');
							</script>
    	  				</td>
    	  			</tr>
    	  			<tr>
    	  				<td align="right">服务车间面积：</td>
    	  				<td align="left">
    	  					<input type="text"  class="middle_txt" datatype="1,is_double,20" decimal="2" name="SERVICE_WORKSHOP_AREA"  id="SERVICE_WORKSHOP_AREA" value="${tmDealerSecondLevelPO.serviceWorkshopArea }"/>
        	  			</td>
    	  				<td align="right">是否具有服务门头：</td>
    	  				<td align="left">
    	  					<script type="text/javascript">
								genSelBoxExp("IS_HAVE_SERVICE_DOORHEAD",<%=Constant.IF_TYPE%>,"${tmDealerSecondLevelPO.isHaveServiceDoorhead}",true,"short_sel",'',"false",'');
							</script>
    	  				</td>
    	  			</tr>
    	  			<tr>
    	  				<td align="right">是否具有服务形象墙：</td>
    	  				<td align="left">
    	  					<script type="text/javascript">
								genSelBoxExp("IS_HAVE_SERVICE_IMAGE_WALL",<%=Constant.IF_TYPE%>,"${tmDealerSecondLevelPO.isHaveServiceImageWall}",true,"short_sel",'',"false",'');
							</script>
        	  			</td>
    	  				<td align="right">服务离销售网点距离：</td>
    	  				<td align="left">
    	  					<input type="text"  class="middle_txt" datatype="1,is_double,10" decimal="2" name="SERVICE_SALES_NETWORK_DISTANCE"  id="SERVICE_SALES_NETWORK_DISTANCE" value="${tmDealerSecondLevelPO.serviceSalesNetworkDistance }"/>
    	  				</td>
    	  			</tr>
    	  			<tr>
    	  				<td align="right">维修技术师最低配备 （人员数量）：</td>
    	  				<td align="left">
    	  					<input type="text"  class="middle_txt" datatype="1,is_digit,20" name="REPAIR_ENGINEER_LOWEST_DEPLOY"  id="REPAIR_ENGINEER_LOWEST_DEPLOY" value="${tmDealerSecondLevelPO.repairEngineerLowestDeploy }"/>
        	  			</td>
    	  				<!--<td align="right">星级申报：</td>
    	  				<td align="left">
    	  					<script type="text/javascript">
								genSelBoxExp("LEVEL_REPORT",<%=Constant.LEVEL_REPORT%>,"${tmDealerSecondLevelPO.levelReport}",true,"short_sel",'',"false",'');
							</script><font color="red">*</font>
    	  				</td>
    	  			--></tr>
    	  		</table>
    	  	</td>
    	  </tr>
	 </table> 
     <table class=table_query>
	 <tr>
	 <td align="center">
	<input type="button" value="保存" name="saveBtn" class="normal_btn" onclick="saveDealerInfo(1);"/>	&nbsp;&nbsp;
	<input type="button" value="上报" name="saveBtn" class="normal_btn" onclick="saveDealerInfo(2);"/>	&nbsp;&nbsp;
	<input type="button" value="返回" name="cancelBtn"  class="normal_btn" onclick="history.back();" /></td>
	</tr>
   </table>
</form>

<script type="text/javascript" ><!--
function updateFsFile(json) {
	var sss = document.getElementById('fileUploadTab');
	MyAlert(sss.rows.length);
	var id = json.ID;
	for (var i=1;i<sss.rows.length;i++) {
		var fjid = json.FJID[i-1];
		var oldfjid = sss.rows[i].FJID;
		MyAlert(oldfjid);
		makeFormCall('<%=request.getContextPath()%>/sysmng/dealer/DealerInfo/updateFileUploadLast.json?ID='+id+'&fjid='+fjid+'&oldfjid='+oldfjid,updateFileUp,'fm');
	}
}
function updateFileUp(json) {
	if(json.result==0){
		MyAlert("更新附件失败!");
	}
	window.location.href = "<%=contextPath%>/sysmng/dealer/DealerInfo/query2ndDealerCsInfoInit.do";
}
function saveDealerInfo(cmd){
	
	if(document.getElementById("CITY_ID").value == '') {MyAlert('请选择城市!'); return;}
	
	if(document.getElementById("COUNTIES").value == '') {MyAlert('请选择区县!'); return;}
	
	if(document.getElementById("SERVICE_STATUS").value == '') {MyAlert("请选择经销商状态!"); return;}
	
	if(document.getElementById("COMPANY_ADDRESS").value == '') {MyAlert("请输入销售展厅地址!"); return;}
	if(document.getElementById("LEGAL_TELPHONE").value == '') {MyAlert("请输入负责人手机!"); return;}
	//if(document.getElementById("WEBMASTER_NAME").value == '') {MyAlert("请输入总经理!"); return;}
	//if(document.getElementById("WEBMASTER_TELPHONE").value == '') {MyAlert("请输入总经理手机!"); return;}
	if(document.getElementById("HOTLINE").value == '') {MyAlert("请输入销售热线!"); return;}
	
	//if(document.getElementById("ACTING_BRAND_NAME").value == '') {MyAlert("请输入代理其他品牌名称!"); return;}
	if(document.getElementById("PROXY_AREA_NAME").value == '') {MyAlert("请输入代理区域!"); return;}
	if(document.getElementById("HAVE_SERVICE").value == '') {MyAlert("请输入是否具备自有服务网点!"); return;}
	if(document.getElementById("orgId").value == '') {MyAlert("请选择大区!"); return;}
	if(document.getElementById("DEALER_SHORTNAME").value == '') {MyAlert("请输入二级网点简称!"); return;}
	if(document.getElementById("DEALER_NAME").value == '') {MyAlert("请输入二级网点全称!"); return;}
	//if(document.getElementsByName("fjid").length==0){
		//MyAlert("请添加附件!"); 
		//return;
	//}
	var re = /^[0-9]*[1-9][0-9]*$/;
	
	var MIN_STOCK = document.getElementById("MIN_STOCK").value;
    if (!re.test(MIN_STOCK)) {
        MyAlert("最低库存"+"必须是正整数!");
        return false;
    }
    
	var OEM_AREA = document.getElementById("OEM_AREA").value;
    if (OEM_AREA && !re.test(OEM_AREA)) {
        MyAlert("北汽幻速专营面积"+"必须是正整数!");
        return false;
    }
    var SERVICE_AREA = document.getElementById("SERVICE_AREA").value;
    if(document.getElementById("HAVE_SERVICE").value == '是'){
        if (!re.test(SERVICE_AREA)) {
            MyAlert("服务站面积"+"必须是正整数!");
            return false;
        }
    }else{
        if (SERVICE_AREA!='') {
            MyAlert("没有服务站请勿输入服务站面积");
            return false;
        }
    }

	var YEAR_PLAN = document.getElementById("YEAR_PLAN").value;
    if (!re.test(YEAR_PLAN)) {
        MyAlert("全年任务量"+"必须是正整数!");
        return false;
    }
	var OEM_PEOPLE_TOTAL = document.getElementById("OEM_PEOPLE_TOTAL").value;
    if (OEM_PEOPLE_TOTAL && !re.test(OEM_PEOPLE_TOTAL)) {
        MyAlert("北汽幻速专营销售人员数"+"必须是正整数!");
        return false;
    }
	//var REGISTERED_CAPITAL = document.getElementById("REGISTERED_CAPITAL").value;
    //if (!re.test(REGISTERED_CAPITAL)) {
      //  MyAlert("经销企业注册资金"+"必须是正整数!");
        //return false;
    //}
	var ZIP_CODE = document.getElementById("ZIP_CODE").value;
    if (ZIP_CODE && !re.test(ZIP_CODE)) {
        MyAlert("邮编"+"必须是正整数!");
        return false;
    }
    
	if(confirm("信息可处理,是否继续?"))
	{
		var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/saveDealerInfo2nd.json?cmd="+ cmd+"&list="+list;
		makeNomalFormCall(url,function(json){
			if(json.Exception) {
				MyAlert(json.Exception);
			} else {
				if(json.isExistBydealerCode){
					MyAlert(json.isExistBydealerCode);
				}
				MyAlert(json.message);
				window.location.href = "<%=contextPath%>/sysmng/dealer/DealerInfo/query2ndDealerCsInfoInit.do";
			}
		},'fm','');
	}		
}
--></script>
<script type="text/javascript">
	genLocSel('PROVINCE_ID','CITY_ID','COUNTIES');
	var p = document.getElementById("PROVINCE_ID");
	setItemValue('PROVINCE_ID', '${dealerInfo1.provinceId}');
	_genCity(p,'CITY_ID');
	var c = document.getElementById("CITY_ID");
	setItemValue('CITY_ID', '${CITY_ID}');
	_genCity(c,'COUNTIES');
	var t = document.getElementById("COUNTIES");
	setItemValue('COUNTIES', '${dealerInfo1.counties}');

</script>
</body>
</html>
