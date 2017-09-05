<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@page import="java.util.Arrays"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Map" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<title>经销商维护</title>

<script type="text/javascript"><!--
var dealerLevel=<%=Constant.DEALER_LEVEL_01%>;
var dealerT=<%=Constant.DEALER_TYPE_DWR%>;
function doInit()
{  	loadcalendar();  //初始化时间控件	
   	var dl=<c:out value="${map.DEALER_LEVEL}"/>;
   	var dt=<c:out value="${map.DEALER_TYPE}"/>;
	if(dealerLevel==dl)
	{
		document.getElementById("sJDealerCode").disabled="true";
		document.getElementById("dealerbu").disabled="true";
		document.getElementById("orgCode").disabled="";
		document.getElementById("orgbu").disabled="";
		$('kpxx').style.display='';	
		
	}else
	{
		document.getElementById("sJDealerCode").disabled="";
		document.getElementById("dealerbu").disabled="";
		document.getElementById("orgCode").disabled="true";
		document.getElementById("orgbu").disabled="true";	
		$('kpxx').style.display='none';	
		/*if(dt!=dealerT){
			$('kpxx').style.display='none';	
		}else{
			$('kpxx').style.display='';	
		}	*/
	}
	if(dt==<%=Constant.DEALER_TYPE_DWR%>){
		
		$('up_name_00').innerHTML="售后服务信息";
		$('up_name_01').innerHTML="站长姓名：";
		$('up_name_02').innerHTML="站长电话：";
		$('up_name_03').innerHTML="服务站状态：";
		$('sh_01').style.display='';	
		$('sh_02').style.display='';
		$('sh_03').style.display='';
		$('xs_01').style.display='none';
	}else{
		$('up_name_00').innerHTML="整车销售信息";
		$('up_name_01').innerHTML="总经理姓名：";
		$('up_name_02').innerHTML="总经理电话：";
		$('up_name_03').innerHTML="信息状态：";
		$('sh_01').style.display='none';	
		$('sh_02').style.display='none';
		$('sh_03').style.display='none';
		$('xs_01').style.display='';
		

	}
	genLocSel('txt1','txt2','txt3','<c:out value="${map.PROVINCE_ID}"/>','<c:out value="${map.CITY_ID}"/>','<c:out value="${map.COUNTIES}"/>'); // 加载省份城市和县
	document.getElementById("priceId").value="${map.PRICE_ID}";
}


// 检验当前修改经销商下是否存在下级经销商，若存在，则不能执行当前操作，并在前台提示错误
function chkDealer(flag) {
	var url = '<%=contextPath%>/sysmng/dealer/DealerInfo/chkDealer.json' ;
	var iDealerId = document.getElementById('DEALER_ID').value ;
	
	makeCall(url, showErr, {dealerId: iDealerId, flag: flag}) ;
}
// 返回检验经销商错误信息
function showErr(json) {
	var dlrLel = document.getElementById('DEALERLEVEL') ;
	var dlrSta = document.getElementById('DEALERSTATUS') ;
	
	if(json.errInfo == 1) {
		if (json.flag == 0) {
			retDlrLel() ;
		}
		if (json.flag == 1) {
			retDlrSta() ;
		}
		MyAlert('经销商信息有误，请确认经销商信息的完整！') ;
	}else if(json.errInfo == 2) {
		// if (json.flag == 0) {
		// 	retDlrLel() ;
		// }
		// if (json.flag == 1) {
		// 	retDlrSta() ;
		//}
		MyAlert('该经销商存在下级经销商，注意修改下级经销商信息！') ;
		changeDealerlevel(dlrLel.value) ;
	} else {
		if (json.flag == 0) {
			changeDealerlevel(dlrLel.value) ;
		}
	}
}

// 若有检验经销商有错误信息，则返回经销商原有级别
function retDlrLel() {
	var dlrLel = document.getElementById('DEALERLEVEL') ;

	if (dlrLel.value != ${map.DEALER_LEVEL}) {
		dlrLel.value = ${map.DEALER_LEVEL} ;
	}
}

// 若有检验经销商有错误信息，则返回原有经销商状态
function retDlrSta() {
	var dlrSta = document.getElementById('DEALERSTATUS') ;

	if (dlrSta.value != ${map.STATUS}) {
		dlrSta.value = ${map.STATUS} ;
	}
}

// 对选择上级经销商是否当前修改经销商验证
function chkDlr() {
	var dlrA = document.getElementById('DEALER_ID') ;
	var dlrB = document.getElementById('sJDealerId') ;
	var dlrCode = document.getElementById('sJDealerCode') ;

	if(dlrA.value == dlrB.value) {
		dlrB.value = "" ;
		dlrCode.value = "" ;

		MyAlert("选择上级经销商不能为当前经销商！") ;
	}
}

//验证输入经销商代码是否已存在
function chkDLRA(dlrCode) {
	dlrId = ${map.DEALER_ID} ;
	url = "<%=contextPath%>/sysmng/dealer/DealerInfo/chkDlr.json" ;
	makeCall(url, printErr, {dlrCode : dlrCode, dlrId : dlrId}) ;
}

function printErr(json) {
	if(json.errInfo == 1) {
		setText("DEALER_CODE") ;
		MyAlert("输入经销商代码已存在，请重新输入") ;
	}
}

function setText(obj,setValue) {
    if(!setValue) {
    	setValue = "" ;
    }
    
	document.getElementById(obj).value = setValue ;
} 
//
--></script>
</head>
<body onload="genLocSel('txt1','txt2','txt3','<c:out value="${map.PROVINCE_ID}"/>','<c:out value="${map.CITY_ID}"/>','<c:out value="${map.COUNTIES}"/>');">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;修改经销商</div>
 <form method="post" name = "fm" >
 <input id="COMPANY_ID" name="COMPANY_ID" type="hidden" value="<c:out value="${map.COMPANY_ID}"/>"/>
 <input id="DEALER_ID" name="DEALER_ID" type="hidden" value="<c:out value="${map.DEALER_ID}"/>"/>
 <input type="hidden"  name="sJDealerId"  value="<c:out value="${map.SJDEALERID}"/>"  id="sJDealerId" onpropertychange="chkDlr();" />
 <input type="hidden"  name="orgId"  value="<c:out value="${map.ORG_ID}"/>"  id="orgId" />
 <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    	  <TR>
			<TH colSpan=4 align=left><IMG class=nav src="<%=contextPath%>/img/subNav.gif"> 经销商基础信息</TH>
    	  <tr>
		  <tr>
		    <td align="right" width="15%">经销商代码：</td>
		    <td align="left"><input type='text'  class="middle_txt" name="DEALER_CODE"  id="DEALER_CODE" datatype="0,is_name,20"  value="<c:out value="${map.DEALER_CODE}"/>" maxlength="20" onchange="chkDLRA(this.value);"/>
		    </td>
		    <td align="right" width="15%">经销商名称：</td>
		    <td align="left">
		    <input type='text'  class="middle_txt" name="DEALER_NAME"  id="DEALER_NAME" datatype="0,is_null,150"  value="<c:out value="${map.DEALER_NAME}"/>" maxlength="150"/>
		    </td>
	      </tr>
	       <tr>
		   <!-- <td align="right" width="15%">经销商简称：</td>
		    <td align="left">
		    <input type='text'  class="middle_txt" name="SHORT_NAME"  id="SHORT_NAME" datatype="1,is_null,75"  value="" maxlength="75"/>
		    </td>-->
		    <td align="right" width="15%">经销商等级：</td>
		    <td colspan="3" align="left">
		    <label>
				<script type="text/javascript">
					genSelBoxExp("DEALERLEVEL",<%=Constant.DEALER_LEVEL%>,"<c:out value="${map.DEALER_LEVEL}"/>",'',"short_sel","onchange='changeDealerlevel(this.value)'","false",'');
				</script>
		   </label>
		    </td>
	      </tr>
	        <tr>
		    <td align="right" width="15%">上级组织：</td>
		    <td align="left">
		    <input type="text"  name="orgCode" size="15" value="<c:out value="${map.ORG_CODE}"/>"  id="orgCode" class="middle_txt" readonly="readonly"/>
		    <input name="orgbu"  id="orgbu" type="button" class="mark_btn" onclick="showOrg('orgCode','orgId','false')" value="&hellip;" />
		    </td>
		    <td align="right" width="15%">上级经销商：</td>
		    <td align="left">
		    <input type="text"  name="sJDealerCode" size="15" value="<c:out value="${map.SJDEALERCODE}"/>"  id="sJDealerCode" class="middle_txt" readonly="readonly" />
		    
		    <input name="dealerbu"  id="dealerbu" type="button" class="mark_btn" onclick="showOrgDealer('sJDealerCode','sJDealerId','false','','true')" value="&hellip;" />
		    </td>
	      </tr>
	       <tr>
		    <td align="right" width="15%">经销商类型：</td>
		    <td align="left">
		    <label>
				<select name="DEALERTYPE" id="DEALERTYPE" onchange='dealerType();changeDealerlevel()'>
					<c:choose>
						<c:when test="${map.DEALER_TYPE eq 10771001}">
							<option value="<%=Constant.DEALER_TYPE_DVS %>" selected="selected">整车销售</option>
						</c:when>
						<c:otherwise>
							<option value="<%=Constant.DEALER_TYPE_DWR %>" selected="selected">售后服务</option>
							<option value="<%=Constant.DEALER_TYPE_JSZX %>">配件供应中心</option>
						</c:otherwise>
					</c:choose>
				</select>
				<font color="red">*</font>
		</label>
		    </td>
		    <td align="right" width="15%">经销商公司：</td>
		    <td align="left">
		   <input class="middle_txt"  datatype="0,is_null,75" value="<c:out value="${map.COMPANY_SHORTNAME}"/>"  id="COMPANY_NAME" style="cursor: pointer;" name="COMPANY_NAME" type="text" readonly="readonly" />
		   <input class="mark_btn" type="button" value="&hellip;" onclick="showCompanyA('<%=contextPath %>') ;"/>
		    </td>
	      </tr>
	      
	      <tr>
			<td width="100%" colspan=4>
			<table  width=100% border="0" align="center" cellpadding="1" cellspacing="1">
			<tr>
			<th colSpan=4 align=left><img class=nav src="<%=contextPath%>/img/subNav.gif"><span id="up_name_00"> 整车销售信息</span></th>
			</tr>
			 <tr id="sh_01" style="display: none">
			  <td align="right" width="15%" >维修资质：</td>
		    <td align="left">
			<label>
					<script type="text/javascript">
						genSelBoxExp("MAIN_RESOURCES",<%=Constant.MAIN_RESOURCES%>,"${map.MAIN_RESOURCES}",true,"short_sel",'',"false",'');
					</script>
			</label>
			</td>
		     <%
	    	java.util.Map map=(java.util.Map)request.getAttribute("map");
		    %>
		    <td align="right" width="15%">是否通过DQV：</td>
		    <td align="left">
		    <label>
				 <%
		    	String IS_DQV=com.infodms.dms.util.CommonUtils.checkNull(map.get("IS_DQV"));
		    	if(IS_DQV.equals("")) IS_DQV=Constant.IF_TYPE_NO.toString();
		    %>
				<script type="text/javascript">
					genSelBoxExp("IS_DQV",<%=Constant.IF_TYPE%>,"<%=IS_DQV%>",false,"short_sel",'',"false",'');
				</script>
		    </label>
		    </td>
	      </tr>
	      
	       <tr id="sh_02" style="display: none">
	       <td align="right" width="15%">结算等级：</td>
		    <td align="left">
		    <label>
				<script type="text/javascript">
					genSelBoxExp("BALANCE_LEVEL",<%=Constant.BALANCE_LEVEL%>,'${map.BALANCE_LEVEL}','true',"short_sel",'',"false",'');
				</script>
		   </label>
		    </td>
		    <td align="right" width="15%">开票等级：</td>
		    <td align="left">
		    <label>
				<script type="text/javascript">
					genSelBoxExp("INVOICE_LEVEL",<%=Constant.INVOICE_LEVEL%>,'${map.INVOICE_LEVEL}',true,"short_sel",'',"false",'');
				</script>
		    </label>
		    </td>
	      </tr>
		 <tr>
	      <td align="right" width="15%">品牌：<FONT color="red">*</FONT> <input type="hidden" id=brandq name="brandq" value=""/> </td>
	      <td align="left">
	      	<input type="hidden" id="brandh" name="brandh" value="${map.BRAND }" />
			      <c:forEach items="${brand}" var="ber">
			      <input type="checkbox"  id="brand_${ber.GROUP_CODE}"  name="brand" 	 value="${ber.GROUP_CODE}"   >${ber.GROUP_NAME}</input>&nbsp;
			      </c:forEach>
	       </td>
			<td align="right" width="15%">是否特殊客商：</td>
		    <td align="left">
			<label>
					<script type="text/javascript">
						genSelBoxExp("IS_SPECIAL",<%=Constant.IF_TYPE%>,'${map.IS_SPECIAL}',true,"short_sel",'',"false",'');
					</script>
			</label>
			</td>
	      </tr>
			 <tr>
		      <td align="right" width="15%" id="up_name_03">信息状态：</td>
		      <td align="left">
		      <label>
					<script type="text/javascript">
						genSelBoxExp("SERVICE_STATUS",<%=Constant.DLR_SERVICE_STATUS%>,"<c:out value="${map.SERVICE_STATUS}"/>",'true',"short_sel",'',"false",'');
					</script>
					<font color="red">*</font>
			  </label>
		      </td>
		      <td align="right" width="15%">省份：</td>
		      <td align="left"> <select class="min_sel" id="txt1" name="province" onchange="_genCity(this,'txt2')"></select><font color="red">*</font>  </td>
          </tr>
          
           <tr> 
          <td align="right" width="15%">地级市：</td>
	      <td align="left"><select class="min_sel" id="txt2" name="city" onchange="_genCity(this,'txt3')"></select> <font color="red">*</font></td>  
	      <td align="right" width="15%">区/县：</td>
		  <td align="left"> <select class="min_sel" id="txt3" name="COUNTIES"></select><font color="red">*</font></td>
	     	</tr> 
           <tr>
         <td align="right" width="15%">建站日期：</td>
         <td align="left">
         <input id="siteDate" class="short_txt" name="siteDate" datatype="1,is_date,10" maxlength="10" group="siteDate,destroyDate" value="<fmt:formatDate value='${map.SITEDATE }' pattern='yyyy-MM-dd'/>"/>
              <input class="time_ico" onclick="showcalendar(event, 'siteDate', false);" value=" " type="button" />
         </td>
         <td align="right" width="15%">撤站日期：</td>
         <td align="left">
               <input id="destroyDate" class="short_txt" name="destroyDate" datatype="1,is_date,10" maxlength="10" group="siteDate,destroyDate" value="<fmt:formatDate value='${map.DESTROYDATE }' pattern='yyyy-MM-dd'/>" />
              <input class="time_ico" onclick="showcalendar(event, 'destroyDate', false);" value=" " type="button" />
         </td>
         
         </tr>
         <tr id="sh_03" style="display: none">
             <td align="right" width="15%">索赔员姓名：</td>
               <td align="left"><input type="text"  class="middle_txt" name="spy_Man"  id="spy_Man"  datatype="1,is_name,10" value="${map.SPY_MAN}" maxlength="10"/></td>
              <td align="right" width="15%">索赔员电话：</td>
         	  <td align="left"><input type="text"  class="middle_txt" name="spy_phone"  id="spy_phone" datatype="1,my_phone,25" value="${map.SPY_PHONE}" maxlength="25"/></td> 
         </tr>
	      <tr>
	      <td align="right" width="15%">传真：</td>
	      <td align="left"><input type="text"  class="middle_txt" name="faxNo"  id="faxNo" value="<c:out value="${map.FAX_NO}"/>"  datatype="1,my_phone,25" maxlength="25"/></td>
	      <td align="right" width="15%">Email：</td>
	      <td align="left"><input type="text"  class="middle_txt" name="email"  id="email" datatype="1,is_email,100" value="<c:out value="${map.EMAIL}"/>" maxlength="100"/></td>
	      </tr>
	       <tr>
			<td align="right" width="15%">邮编：</td>
		    <td align="left"><input type="text"  class="middle_txt" name="zipCode"  id="zipCode" value="${map.ZIP_CODE}" maxlength="10" datatype="1,is_digit_letter,30" /></td>
		    <td align="right" width="15%">法人：</td>
		    <td align="left"><input type="text"  class="middle_txt" name="LEGAL"  datatype="0,is_null,20"  id="LEGAL" value="${map.LEGAL}"/></td>
		  </tr>
	       <tr>
			<td align="right" width="15%">法人电话：</td>
		    <td align="left"><input type="text"  class="middle_txt" name="LEGAL_TEL"  id="LEGAL_TEL" value="${map.LEGAL_TEL}" maxlength="10" datatype="1,is_digit_letter,30" /></td>
		    <td align="right" width="15%" id="up_name_01">总经理姓名：</td>
			<td align="left"> 
				<input type="text"  class="middle_txt" id="WEBMASTER_NAME" name="WEBMASTER_NAME" datatype="0,is_null,20" value="${map.WEBMASTER_NAME}"/>
			</td>
		  </tr>
	      <tr>
		<td align="right" width="15%" id="up_name_02">总经理电话：</td>
		<td align="left"> 
		<input type="text"  class="middle_txt" id="WEBMASTER_PHONE" name="WEBMASTER_PHONE" datatype="0,is_phone,20" value="${map.WEBMASTER_PHONE}"/>
		</td>
		<td align="right" width="15%">24小时值班电话：</td>
	    <td align="left"><input type="text"  class="middle_txt" name="DUTY_PHONE" datatype="0,is_phone,20"  id="DUTY_PHONE" value="${map.DUTY_PHONE}"/></td>
		</tr>  
	      <tr id="xs_01">
			<td align="right" width="15%">销售经理姓名：</td>
		    <td align="left"><input type="text"  class="middle_txt" name="MARKET_NAME"  id="MARKET_NAME" value="${map.MARKET_NAME}" datatype="0,is_null,20" /></td>
		    <td align="right" width="15%">销售经理电话：</td>
			<td align="left"> 
				<input type="text"  class="middle_txt" id="MARKET_TEL" name="MARKET_TEL" value="${map.MARKET_TEL}"/>
			</td>
		  </tr>
	      <tr>
		<td align="right" width="15%">行政级别：</td>
		<td align="left"> 
		<label>
				<script type="text/javascript">
					genSelBoxExp("ADMIN_LEVEL",<%=Constant.ADMIN_LEVEL%>,"${map.ADMIN_LEVEL}",true,"short_sel",'',"false",'');
				</script>
		</label>
		</td>
		<td align="right" width="15%">经销商状态：</td>
		      <td align="left">
		      <label>
					<script type="text/javascript">
						genSelBoxExp("DEALERSTATUS",<%=Constant.STATUS%>,"<c:out value="${map.STATUS_ENABLE}"/>",'',"short_sel",'',"false",'');
					</script>
					<font color="red">*</font>
			  </label>
		      </td>
	</tr>
	<tr>
		<td align="right" width="15%">系统开通时间：</td>
	      	<td colspan="3" align="left">
	      	<label>
	      	${map.CREATE_DATE}
	      	</label>
	      	</td>
	    </tr>
	 <tr>
	        <td align="right" width="15%">营业执照注册地址：</td>
	      	<td align="left" colspan="3">
	      		<textarea name="zzaddress" id="zzaddress" style="width: 90%;" rows="2" datatype="1,is_textarea,1000"><c:out value="${map.ZZADDRESS}"/></textarea>
	      	</td>
	      </tr>
	    <tr>
	        <td align="right" width="15%">通信地址：</td>
	      	<td align="left" colspan="3">
	      		<textarea name="address" id="address" style="width: 90%;" rows="2" datatype="1,is_textarea,1000"><c:out value="${map.ADDRESS}"/></textarea>
	      	</td>
	      </tr>  
	       <tr>
	        <td align="right" width="15%">备注：</td>
	      	<td align="left" colspan="3">
	      		<textarea name="remark" id="remark" style="width: 90%;" rows="2" datatype="1,is_textarea,1000"><c:out value="${map.REMARK}"/></textarea>
	      	</td>
	      </tr>
			</table>
			</td>
    	  <tr>   	  
    	   <tr id="kpxx" style="display: none">
			<td width="100%" colspan=4>
			<table  width=100% border="0" align="center" cellpadding="1" cellspacing="1"> 
	      <c:choose>
	      	<c:when test="${map.DEALER_TYPE==10771002 }">
	      	<TR>
				<TH colSpan=4 align=left><IMG class=nav src="<%=contextPath%>/img/subNav.gif"> 开票信息</TH>
			</tr>
			<tr>
				<td   align="right" nowrap="nowrap">开票名称：</td>
				<td align="left"><input type="text" class="maxlong_txt" id="TAX_NAME" name="TAX_NAME"
					value="${invoiceListMap.TAX_NAME}"  datatype="0,is_null,30" />
				</td>
			</tr>
			<tr>
				<td   align="right" nowrap="nowrap">开票类型：</td>
				<td align="left">
				  <script type="text/javascript">
				        genSelBoxExp("dlrInvTpe",<%=Constant.DLR_INVOICE_TYPE%>,'<c:if test="${null != invoiceListMap.INV_TYPE}">${invoiceListMap.INV_TYPE}</c:if>',true,"short_sel","","false","");
				  </script>
				</td>
			</tr>
			<tr>
				<td   align="right" nowrap="nowrap">国税号：</td>
				<td align="left"><input type="text" class="maxlong_txt" id="TAX_NO" name="TAX_NO"
					value="${invoiceListMap.TAX_NO }"  datatype="0,is_null,30"/>
				</td>
			</tr>
			<tr>
				<td   align="right" nowrap="nowrap">开户行：</td>
				<td align="left"><input type="text" class="maxlong_txt" id="BANK" name="BANK"
					value="${invoiceListMap.BANK}" datatype="0,is_null,30"/>
				</td>
			</tr>
			<tr>
				<td   align="right" nowrap="nowrap">账号：</td>
				<td align="left">
				<input type="text" class="maxlong_txt" id="INVOICE_ACCOUNT" name="INVOICE_ACCOUNT" 
				value="${invoiceListMap.ACCOUNT}" onchange="dataTypeCheck(this)" datatype="0,is_null,30"/>
				</td>
			</tr>
			<tr>
				<td   align="right" nowrap="nowrap">电话：</td>
				<td align="left"><input type="text" class="maxlong_txt" id="INVOICE_PHONE" name="INVOICE_PHONE"
					value="${invoiceListMap.TEL}"  />
				</td>
			</tr>
			<tr>
				<td   align="right" nowrap="nowrap">地址：</td>
				<td align="left"><input type="text" class="maxlong_txt" id="INVOICE_ADDR" name="INVOICE_ADDR"
					value="${invoiceListMap.ADDR}" datatype="0,is_null,50"/>
				</td>
			</tr>
			<tr>
				<td  align="right" nowrap="nowrap">发票邮寄地址：</td>
				<td align="left" colspan="3"><input type="text" class="maxlong_txt" id="MAIL_ADDR" name="MAIL_ADDR"
					value="${invoiceListMap.MAIL_ADDR}" datatype="0,is_null,50"/>
				</td>
			</tr>
	      	</c:when>      
	      	<c:otherwise>
	      <tr>
			<TH colSpan=4 align=left><IMG class=nav src="<%=contextPath%>/img/subNav.gif"> 开票信息</TH>
    	  </tr>
    	  <tr>
			<td align="right" width="15%">开票名称：</td>
			<td align="left"> 
				<input type="text"  class="middle_txt" id="erpCode" name="erpCode" value="${map.ERP_CODE}" datatype="0,is_textarea,50"/> 
			</td>
			<td align="right" width="15%">税号：</td>
			<td align="left"> 
				<input type="text"  class="middle_txt" id="taxesNo" name="taxesNo" value="${map.TAXES_NO }"  datatype="0,is_textarea,30" />
			</td>
		 </tr>
		 <tr>
			<td align="right" width="15%">开户行：</td>
			<td align="left"> 
				<input type="text"  class="middle_txt" id="BANK" name="BANK" value="${map.BANK}" datatype="0,is_textarea,50"/>
			</td>
			<td align="right" width="15%">账号：</td>
			<td align="left"> 
				<input type="text"  class="middle_txt" id="INVOICE_ACCOUNT" name="INVOICE_ACCOUNT" value="${map.INVOICE_ACCOUNT}" datatype="0,is_textarea,50"/>
			</td>
		 </tr>
		 <tr>
		 <td align="right" width="15%">电话：</td>
			<td align="left"> 
				<input type="text"  class="middle_txt" id="INVOICE_PHONE" name="INVOICE_PHONE" value="${map.INVOICE_PHONE}" datatype="0,is_textarea,50"/>
			</td>
			<td align="right" width="15%">地址：</td>
			<td align="left"> 
				<input type="text"  class="middle_txt" id="INVOICE_ADD" name="INVOICE_ADD" value="${map.INVOICE_ADD}" datatype="0,is_textarea,50"/>
			</td>
		 </tr>
		 <tr>
			<td align="right" width="15%">发票邮寄地址：</td>
			<td align="left" colspan="3"> 
				<input type="text" size=30 class="middle_txt" id="INVOICE_POST_ADD" name="INVOICE_POST_ADD" value="${map.INVOICE_POST_ADD}" datatype="0,is_textarea,50"/>
			</td>
		 </tr>
		 </c:otherwise>  
	      </c:choose>
		 </table>
			</td>
    	  <tr>
	 </table>
 
     <table class=table_query>
	 <tr>
	 <td align="center">
	<input type="button" value="修改" name="saveBtn" class="normal_btn" onclick="saveDealerInfo();"/>	
	<input type="button" value="返回" name="cancelBtn"  class="normal_btn" onclick="goBack();" />
	<input type="button" value="资金账户同步" name="priceCon"  class="long_btn" onclick="go_Price();" style="display:none" />
	</td>
	</tr>
   </table>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query" id="businessTable" style="display:none" >
     <tr><th colspan="3"><img src="<%=contextPath%>/img/nav.gif" />业务范围列表 <input type="button" value="添加业务范围" name="saveBtn" class="long_btn" onclick="addBusiness();"/></th></tr>
     <tr>
     <th>业务范围代码</th>
     <th>业务范围名称</th>
     <th>操作</th>
     </tr>
     <c:forEach items="${businessList}" var="bl" >
     <tr class="table_list_row1">
     <td><c:out value="${bl.AREA_CODE}"/></td>
     <td><label id="defaultPrice"><c:out value="${bl.AREA_NAME}"/></label></td>
     <td><a href="#" onClick="delBusiness('<c:out value="${bl.RELATION_ID}"/>')" >删除</a></td>
     </tr>
     </c:forEach>
    </table>
    <BR/>
     <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query" id="mortgageTable" style="display:none" >
     <tr><th colspan="3"><img src="<%=contextPath%>/img/nav.gif" />经销商个人信贷信息</th></tr>
     <tr>
     <th>个人信贷类型</th>
     <th>个人信贷时间</th>
  	</tr>
    
     <c:forEach items="${mortgageList}" var="ml" >
     <tr class="table_list_row1">
     <td><c:out value="${ml.CODE_DESC}"/></td>
     <td><label id="defaultPrice"><c:out value="${ml.CREDIT_DATE}"/></label></td>
     </tr>
     </c:forEach>
    </table>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query" id="addressTable">
    </table>
    <br/>
    
    
    <c:choose>
	      	<c:when test="${map.DEALER_TYPE==10771002 }">
	     	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query" id="businessTable">
			<tr>
			<th colspan="7"><img src="<%=contextPath%>/img/nav.gif" />地址列表 <input type="button" value="添加地址" name="saveBtn" class="long_btn"
					onclick="addAddress1();" /></th>
			</tr>
			<tr>
				<th>地址名称</th>
				<th>联系人</th>
				<th>电话</th>
				<th>状态</th>
				<th>操作</th>
			</tr>
			<c:forEach items="${addressList1}" var="al">
				<%-- <c:if test="${al.STATUS==10011001}"> --%>
					<tr class="table_list_row1">
						<td><c:out value="${al.ADDR}" /></td>
						<td><c:out value="${al.LINKMAN}" /></td>
						<td><c:out value="${al.TEL}" /></td>
						<td><script>document.write(getItemValue(${al.STATE }));</script></td>
						<td><a href="#" onClick="modifyAdd1('<c:out value="${al.ADDR_ID}"/>')">修改</a></td>
					</tr>
			<%-- 	</c:if> --%>
			</c:forEach>
		</table>
    </c:when>      
	      	<c:otherwise>
	      	
    
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query" id="businessTable">
     <tr><th colspan="7"><img src="<%=contextPath%>/img/nav.gif" />经销商地址列表  <input type="button" value="添加经销商地址" name="saveBtn" class="long_btn" onclick="addAddress();"/></th></tr>
     <tr>
     <th>地址代码</th>
     <th>地址名称(按地址名称拼音字母排序)</th>
     <th>联系人</th>
     <th>电话</th>
     <TH>手机</TH>
     <th>状态</th>
     <th>操作</th>
     </tr>
     <c:forEach items="${addressList}" var="al" >
     <c:if test="${al.STATUS==10011001}">
     <tr class="table_list_row1">
     <td><c:out value="${al.ADD_CODE}"/></td>
     <td><c:out value="${al.ADDRESS}"/></td>
     <td><c:out value="${al.LINK_MAN}"/></td>
     <td><c:out value="${al.TEL}"/></td>
     <td><c:out value="${al.MOBILE_PHONE }"/></td>
     <td><script>document.write(getItemValue(${al.STATUS }));</script></td>
     <td><a href="#" onClick="modifyAdd('<c:out value="${al.ID}"/>')" >修改</a></td>
     </tr>
     </c:if>
     <c:if test="${al.STATUS==10011002}">
     <tr class="table_list_row1">
     <td><FONT color="red"><c:out value="${al.ADD_CODE}"/></FONT></td>
     <td><FONT color="red"><c:out value="${al.ADDRESS}"/></FONT></td>
     <td><FONT color="red"><c:out value="${al.LINK_MAN}"/></FONT></td>
     <td><FONT color="red"><c:out value="${al.TEL}"/></FONT></td>
    
     <td><script>document.write(getItemValue(${al.STATUS }));</script></td>
     <td><a href="#" onClick="modifyAdd('<c:out value="${al.ID}"/>')" >修改</a></td>
     </tr>
     </c:if>
     </c:forEach>
    </table>
    </c:otherwise>  
	      </c:choose>
    
    
    
    <a name="anchorA"></a>
     <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query" id="businessTable" style="display:none" >
     <tr><th colspan="7"><img src="<%=contextPath%>/img/nav.gif" />使用价格</th></tr>
     <tr>

     <th>价格类型代码</th>
     <th>价格类型描述</th>
     <th>默认价格</th>
     <th>有效开始日期</th>
     <th>有效结束日期</th>
     <th>创建日期</th>
     <th>操作</th>
     </tr>
     <c:forEach items="${types}" var="al">
     <tr class="table_list_row1">
     <td><c:out value="${al.PRICE_CODE}"/></td>
     <td><c:out value="${al.PRICE_DESC}"/></td>
     <td><c:out value="${al.CODE_DESC}"/></td>
     <td><c:out value="${al.START_DATE}"/></td>
     <td><c:out value="${al.END_DATE}"/></td>
     <td><c:out value="${al.CREATE_DATE}"/></td>
     <td><a href="#anchorA" onClick="delPrice('<c:out value="${al.RELATION_ID}"/>')" >[删除]</a>
     <a href="#anchorA" onClick="defaultPrice('<c:out value="${al.RELATION_ID}"/>','<c:out value="${al.PRICE_ID}"/>')" >[设置默认]</a></td>
     </tr>
      </c:forEach>
     <tr>
     <td colspan="4" align="center"><input type="button" value="新增价格" name="saveBtn" class="long_btn" onclick="addPrice();"/></td>
     </tr>
    </table>
</form>

<script type="text/javascript" >
//加载品牌选择框
function brandCheck(){
	  var brandh= document.getElementById("brandh").value;
	  var arrayObj = new Array();
	  arrayObj=brandh.split(",");
	  for(var i=0;i<arrayObj.length;i++){
		  document.getElementById("brand_"+arrayObj[i]).checked="checked";
		  

	  }

}

brandCheck();

function changeDealerlevel()
{
	var value=document.getElementById("DEALERLEVEL").value;
	var dt=document.getElementById("DEALERTYPE").value;
	if(dealerLevel==value)
	{
		document.getElementById("sJDealerCode").disabled="true";
		document.getElementById("dealerbu").disabled="true";
		document.getElementById("orgCode").disabled="";
		document.getElementById("orgbu").disabled="";
		document.getElementById("sJDealerCode").value="";
		document.getElementById("sJDealerId").value="";
		$('kpxx').style.display='';	
		
	}else
	{
		document.getElementById("sJDealerCode").disabled="";
		document.getElementById("dealerbu").disabled="";
		document.getElementById("orgCode").disabled="true";
		document.getElementById("orgbu").disabled="true";
		document.getElementById("orgCode").value="";
		document.getElementById("orgId").value="";	
		$('kpxx').style.display='none';		
		/*if(dt!=dealerT){
			$('kpxx').style.display='none';	
		}else{
			$('kpxx').style.display='';	
		}*/
	}
}

function go_Price(){

		if(confirm("确认同步经销商资金账户,同步完需等待5分钟？"))
		{
			fm.action = '<%=contextPath%>/sysmng/dealer/DealerInfo/modifyDealerPrice.do';
		 	fm.submit();
		}
}


function saveDealerInfo()
{
	var DEALERTYPE=document.getElementById("DEALERTYPE").value;
	var DEALERSTATUS=document.getElementById("DEALERSTATUS").value;
	//var DEALERCLASS=document.getElementById("DEALERCLASS").value;
	if(DEALERTYPE==""){
		MyAlert("经销商类型不能为空！");
		return;
		}
	if(DEALERSTATUS==""){
		MyAlert("信息状态不能为空！");
		return;
	}
	//if(DEALERCLASS==""){
		//MyAlert("经销商评级不能为空！");
		//return;
	//}
	var text1=document.getElementById("txt1").value;
	var text2=document.getElementById("txt2").value;
	var text3=document.getElementById("txt3").value;
	if(text1==""){
		MyAlert('省份输入不能为空！');
		return;
	}
	if(text2==""){
		MyAlert('城市输入不能为空！');
		return;
	}
	if(text3==""){
		MyAlert('区县输入不能为空！');
		return;
	}
	if(DEALERTYPE==<%=Constant.DEALER_TYPE_DWR%>){
		var BALANCE_LEVEL=document.getElementById("BALANCE_LEVEL").value;
		var INVOICE_LEVEL=document.getElementById("INVOICE_LEVEL").value;
		var DEALERLEVEL=document.getElementById("DEALERLEVEL").value;//经销商级别
		if(BALANCE_LEVEL==''){
			MyAlert('请选择结算等级!');
			return;
		}
		if(INVOICE_LEVEL==''){
			MyAlert('请选择开票等级!');
			return;
		}
		if(DEALERLEVEL==<%=Constant.DEALER_LEVEL_01%>){//一级经销商
			if(BALANCE_LEVEL!=<%=Constant.BALANCE_LEVEL_SELF%>){
				MyAlert('一级经销商结算等级必须选择 独立结算!');
				return;
			}
			if(INVOICE_LEVEL!=<%=Constant.INVOICE_LEVEL_SELF%>){
				MyAlert('一级经销商开票等级必须选择 独立开票!');
				return;
			}
			
		}else{//二级经销商
			if(BALANCE_LEVEL==<%=Constant.BALANCE_LEVEL_HIGH%>&&INVOICE_LEVEL==<%=Constant.INVOICE_LEVEL_SELF%>){
				MyAlert('结算等级为上级结算时,不能选择独立开票!');
				return;
			}
		}
	}
	
	var dl=document.getElementById("DEALERLEVEL").value;
	if(dealerLevel==dl)
	{
		var orgId=document.getElementById("orgId").value;
		if(orgId=="")
		{
			MyAlert("请选择上级组织！");
			return;
		}
	}else
	{
		var sjId=document.getElementById("sJDealerId").value;
		if(sjId=="")
		{
			MyAlert("请选择上级经销商！");
			return;	
		}
	}
	
	var companyId=document.getElementById("COMPANY_ID").value;
	if(companyId=="")
	{
		MyAlert("请选择经销商公司！");
		return;	
	}
	var brand=document.getElementsByName("brand");
	var brandqv="";
	for(var i=0;i<brand.length;i++){
	if(brand[i].checked){
		if(i==brand.length-1){
			brandqv +=brand[i].value;
		}else{
			brandqv +=brand[i].value+',';

			}
		}
	document.getElementById("brandq").value=brandqv;

	}
	var brande=document.getElementById("brandq").value;
	if(brande == ""){
		MyAlert('请选择品牌！');
		return;	
	}
	
	if(submitForm('fm'))
	{
		if(confirm("确认修改经销商信息吗？"))
		{
			if(dl==<%=Constant.DEALER_LEVEL_01%>){
				if(DEALERTYPE==<%=Constant.DEALER_TYPE_DVS%>||DEALERTYPE==<%=Constant.DEALER_TYPE_JSZX%>||DEALERTYPE==<%=Constant.DEALER_TYPE_QYZDL%>){
						if(DEALERSTATUS==<%=Constant.STATUS_ENABLE%>){
							 sendAjax('<%=contextPath%>/sysmng/dealer/DealerInfo/querySameBusiness.json',showResultCodeCheck11,'fm');
							}
						else{
							fm.action = '<%=contextPath%>/sysmng/dealer/DealerInfo/modifyDealerInfo.do';
						 	fm.submit();
						 	MyAlert("修改成功！");
							}
					}
				else{
					fm.action = '<%=contextPath%>/sysmng/dealer/DealerInfo/modifyDealerInfo.do';
				 	fm.submit();
				 	MyAlert("修改成功！");
					}
			}

		else{
			fm.action = '<%=contextPath%>/sysmng/dealer/DealerInfo/modifyDealerInfo.do';
		 	fm.submit();
		 	MyAlert("修改成功！");
			}
	
			
		}
	 }
}
function showResultCodeCheck11(obj){

	var msg=obj.msg;
	if(msg=='true'){
		fm.action = '<%=contextPath%>/sysmng/dealer/DealerInfo/modifyDealerInfo.do';
	 	fm.submit();
	 	MyAlert("修改成功！");
				
	}else if(msg=='false1')
		{
		
		MyAlert("同一经销商不能维护到多个基地");
	}
	else if(msg=='false2'){
		MyAlert("同一基地同一经销商公司不为售后已存在一级经销商");
	}
	/**else if(msg=='false3'){
		MyAlert("经销商公司已经被使用,请选择其他经销商公司");
		return;
	}**/
}
function addBusiness()
{
	var dealerId=document.getElementById("DEALER_ID").value;
	var DEALERLEVEL=document.getElementById("DEALERLEVEL").value;
	var DEALERTYPE=document.getElementById("DEALERTYPE").value;
	var DEALERSTATUS=document.getElementById("DEALERSTATUS").value;
	OpenHtmlWindow('<%=contextPath%>/sysmng/dealer/DealerInfo/addBusiness.do?dealerId='+dealerId+'&DEALERLEVEL='+DEALERLEVEL+'&DEALERTYPE='+DEALERTYPE+'&DEALERSTATUS='+DEALERSTATUS,600,400);
}

function addPrice()
{
	var dealerId=document.getElementById("DEALER_ID").value;
	OpenHtmlWindow('<%=contextPath%>/sysmng/dealer/DealerInfo/addPrice.do?dealerId='+dealerId,700,500);
}
function addAddress()
{
	var dealerId=document.getElementById("DEALER_ID").value;
	OpenHtmlWindow('<%=contextPath%>/sysmng/dealer/DealerInfo/addAddress.do?dealerId='+dealerId,800,400);
}

//跳转到添加地址信息的页面
function addAddress1(){
	var dealerId=document.getElementById("DEALER_ID").value;
	var dealerCode=document.getElementById("DEALER_CODE").value;
	var dealerName=document.getElementById("DEALER_NAME").value;
 	OpenHtmlWindow('<%=contextPath%>/parts/baseManager/PartDealerManager/PartDlrMng/addAddressInit.do?dealerId='+dealerId+'&dealerCode='+dealerCode+'&dealerName='+dealerName,800,400); 
}

function modifyAdd(id)
{
	var dealerId=document.getElementById("DEALER_ID").value;
	OpenHtmlWindow('<%=contextPath%>/sysmng/dealer/DealerInfo/modifyAdd.do?dealerId='+dealerId+'&id='+id,800,400);
}

//修改地址信息
function modifyAdd1(id)
{
	var dealerId=document.getElementById("DEALER_ID").value;
	OpenHtmlWindow('<%=contextPath%>/parts/baseManager/PartDealerManager/PartDlrMng/modifyAdd.do?dealerId='+dealerId+'&id='+id,800,400);
}

function delAdd(id)
{
	if(confirm("确认删除该地址吗？"))
	{
		fm.action = '<%=contextPath%>/sysmng/dealer/DealerInfo/delAdd.do?id='+id;
	 	fm.submit();
	}
}
function delPrice(relationId)
{
	if(confirm("确认删除该价格吗？"))
	{
		fm.action = '<%=contextPath%>/sysmng/dealer/DealerInfo/delPrice.do?relationId='+relationId;
	 	fm.submit();
	}
}
function defaultPrice(relationId,priceId)
{
	var dealerId=document.getElementById("DEALER_ID").value;
	OpenHtmlWindow('<%=contextPath%>/sysmng/dealer/DealerInfo/defaultMyPriceView.do?relationId='+relationId+'&priceId='+priceId+'&dealerId='+dealerId,800,400);
	//if(confirm("确认设置当前价格为默认价格？"))
	//{
	// 	fm.submit();
//	}
}
function delBusiness(relationId)
{
	if(confirm("确认删除该经销的业务范围吗？"))
	{
		fm.action = '<%=contextPath%>/sysmng/dealer/DealerInfo/delBusiness.do?relationId='+relationId;
		fm.submit();
	}
 	
}
function parentMonth()
{
	fm.action='<%=contextPath%>/sysmng/dealer/DealerInfo/queryDealerInfoDetail.do';
	fm.submit();
}
function goBack()
{
	fm.action='<%=contextPath%>/sysmng/dealer/DealerInfo/queryDealerInfoInit.do';
	fm.submit();
}

function dealerType(){
if($('DEALERTYPE').value==<%=Constant.DEALER_TYPE_DWR%>){
		
		$('up_name_00').innerHTML="售后服务信息";
		$('up_name_01').innerHTML="站长姓名：";
		$('up_name_02').innerHTML="站长电话：";
		$('up_name_03').innerHTML="服务站状态：";
		$('sh_01').style.display='';	
		$('sh_02').style.display='';
		$('sh_03').style.display='';
		$('xs_01').style.display='none';
	}else{
		$('up_name_00').innerHTML="整车销售信息";
		$('up_name_01').innerHTML="总经理姓名：";
		$('up_name_02').innerHTML="总经理电话：";
		$('up_name_03').innerHTML="信息状态：";
		$('sh_01').style.display='none';	
		$('sh_02').style.display='none';
		$('sh_03').style.display='none';
		$('xs_01').style.display='';

	}
}





dealerType();
</script>

</body>
</html>
