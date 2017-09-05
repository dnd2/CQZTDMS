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

</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;经销商查看</div>

 <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    	  <tr>
			<TH colSpan=4 align=left><IMG class=nav src="<%=contextPath%>/img/subNav.gif"> 经销商基础信息</TH>
    	  </tr>
    	  <tr>
    	  	<td align="right" width="20%">组织机构代码：</td>
    	  	<td width="30%">
    	  		${dMap.COMPANY_ZC_CODE }
    	  	</td>
            <td align="right" width="20%">经营类型：</td>
             <td align="left" width="30%">
             		${dMap.SHOP_TYPE}
             </td>
          </tr>
		  <tr>
		    <td align="right">经销商公司：</td>
		    <td align="left">
            	${dMap.COMPANY_NAME }
            </td>
		    <td align="right" width="20%">经销商简称：</td>
		    <td align="left">
		    	${dMap.DEALER_SHORTNAME }
		    </td>
      </tr>
		  <tr>
		    <td align="right" width="20%">经销商代码：</td>
		    <td align="left">
		    	${dMap.DEALER_CODE }
		    </td>
		    <td align="right" width="20%">经销商全称：</td>
		    <td align="left">
		    	${dMap.DEALER_NAME }
		    </td>
	      </tr>
	       <tr>
		    <td align="right" width="20%">经销商等级：</td>
		    <td colspan="3" align="left">
                 <script type="text/javascript">
                     document.write(getItemValue("${dMap.DEALER_LEVEL}"));
                 </script>
		    </td>
	      </tr>
	        <tr>
		    <td align="right" width="20%">上级组织：</td>
		    <td align="left">
			    ${dMap.ORG_CODE }
		    </td>
		    <td align="right" width="20%">上级经销商：</td>
		    <td align="left">
			    ${dMap.PARENT_DEALER_NAME }
		    </td>
	      </tr>
           <tr>
                      	 <td align="right">大区：</td>
              <td align="left">
              		${dMap.ROOT_ORG_NAME}
              </td>
              <td align="right">省份：</td>
              <td align="left">
              		${dMap.PROVINCE_ID}
              </td>
             
      		</tr>
           <tr>
            <td align="right">城市：</td>
              <td align="left">
              		${dMap.CITY_ID}
              </td>
          	 <td align="right">区县：</td>
              <td align="left">
              		${dMap.COUNTIES}
              </td>
            
	      </tr><tr>
	       <td align="right">邮编：</td>
             <td align="left">
             	${dMap.ZIP_CODE}
             </td></tr>
	      <tr>
             <td align="right">经销企业注册资金：</td>
	          <td align="left" >
	          	${dMap.REGISTERED_CAPITAL}
	          </td>
	          <td align="right">&nbsp;</td>
	          <td align="left">&nbsp;</td>
      </tr>
      		<tr>
             <td align="right">企业注册地址：</td>
	          <td align="left"  colspan="3">${dMap.ADDRESS}</td>
      		</tr>
      		<tr>
             <td align="right">销售展厅地址：</td>
	          <td align="left" colspan="3">${dMap.COMPANY_ADDRESS}</td>
      		</tr>
           <tr>
             <td align="right">法人：</td>
             <td align="left">${dMap.LEGAL}</td>
             <td align="right">法人手机：</td>
             <td align="left">${dMap.LEGAL_TELPHONE}</td>
           </tr>
           <tr>
             <td align="right">法人办公室电话：</td>
             <td align="left">${dMap.LEGAL_PHONE}</td>
             <td align="right">法人邮箱：</td>
             <td align="left">${dMap.LEGAL_EMAIL}</td>
           </tr>
           <tr>
		    <td align="right">经销商状态：</td>
		    <td align="left" colspan="3">
				<script type="text/javascript">
                    document.write(getItemValue("${dMap.SERVICE_STATUS}"));
                </script>
		    </td>
	      </tr>
	      <tr>
	      	<td align="right">单位性质：</td>
	      	<td>
	      		<script type="text/javascript">
	      			document.write(getItemValue('${dMap.UNION_TYPE }'));
	      		</script>
	      	</td>
	      	<td align="right">经销商传真：</td>
	      	<td>
	      		${dMap.FAX_NO }
	      	</td>
	      </tr>
	      <tr id="zcxs">
			<td width="100%" colspan=4>
			<table  width=100% border="0" align="center" cellpadding="1" cellspacing="1">
			<tr>
			<th colSpan=4 align=left><img class=nav src="<%=contextPath%>/img/subNav.gif"> <span id="up_name_00">整车销售信息</span></th>
			</tr>
             <tr >
               <td align="right" width="20%">总经理：</td>
               <td align="left" width="30%">${dMap.WEBMASTER_NAME}</td>
               <td width="20%" align="right">总经理办公电话：</td>
               <td align="left" width="30%">${dMap.WEBMASTER_PHONE}</td>
             </tr>
             <tr >
               <td align="right">总经理手机：</td>
               <td align="left">${dMap.WEBMASTER_TELPHONE}</td>
               <td align="right">总经理邮箱：</td>
               <td align="left">${dMap.WEBMASTER_EMAIL}</td>
             </tr>
             <tr >
               <td align="right">销售经理：</td>
               <td align="left">${dMap.MARKET_NAME}</td>
               <td align="right">销售经理办公电话：</td>
               <td align="left">${dMap.MARKET_PHONE}</td>
             </tr>
             <tr >
               <td align="right">销售经理手机：</td>
               <td align="left">${dMap.MARKET_TELPHONE}</td>
               <td align="right">销售经理邮箱：</td>
               <td align="left">${dMap.MARKET_EMAIL}</td>
             </tr>
             <tr >
               <td align="right">市场经理：</td>
               <td align="left">${dMap.MANAGER_NAME}</td>
               <td align="right">市场经理办公电话：</td>
               <td align="left">${dMap.MANAGER_PHONE}</td>
             </tr>
             <tr >
               <td align="right">市场经理手机：</td>
               <td align="left">${dMap.MANAGER_TELPHONE}</td>
               <td align="right">市场经理邮箱：</td>
               <td align="left">${dMap.MANAGER_EMAIL}</td>
             </tr>
             <tr >
               <td align="right">财务经理：</td>
               <td align="left">${dMap.FINANCE_MANAGER_NAME}</td>
               <td align="right">财务经理办公电话：</td>
               <td align="left">${dMap.FINANCE_MANAGER_PHONE}</td>
             </tr>
             <tr >
               <td align="right">财务经理手机：</td>
               <td align="left">${dMap.FINANCE_MANAGER_TELPHONE}</td>
               <td align="right">财务经理邮箱：</td>
               <td align="left">${dMap.FINANCE_MANAGER_EMAIL}</td>
             </tr>
             <tr >
               <td align="right">信息员：</td>
               <td align="left">${dMap.MESSAGER_NAME}</td>
               <td align="right">信息员办公电话：</td>
               <td align="left">${dMap.MESSAGER_PHONE}</td>
             </tr>
             <tr >
               <td align="right">信息员手机：</td>
               <td align="left">${dMap.MESSAGER_TELPHONE}</td>
               <td align="right">信息员QQ：</td>
               <td align="left">${dMap.MESSAGER_QQ}</td>
             </tr>
             <tr >
               <td align="right">信息员邮箱：</td>
               <td align="left">${dMap.MESSAGER_EMAIL}</td>
               <td align="right">销售热线：</td>
               <td align="left">${dMap.HOTLINE}</td>
             </tr>
             <tr >
             <td align="right">VI建设申请日期：</td>
             <td align="left">
             	${dMap.VI_APPLAY_DATE}
             </td>
             <td align="right">VI建设开工日期：</td>
             <td align="left">
             	${dMap.VI_BEGIN_DATE}
             </td>
           </tr>
           <tr >
             <td align="right">VI建设竣工日期：</td>
             <td align="left">
             	${dMap.VI_COMPLETED_DATE}
             </td>
             <td align="right">VI形象验收日期：</td>
             <td align="left">
             	${dMap.VI_CONFRIM_DATE}
             </td>
           </tr>
           <tr >
             <td align="right">形象等级：</td>
             <td align="left">
             	<script type="text/javascript">
					document.write(getItemValue("${dMap.IMAGE_LEVEL}"));
				</script>
             </td>
             <td align="right">验收形象等级：</td>
             <td align="left">
				<script type="text/javascript">
					document.write(getItemValue("${dMap.IMAGE_COMFIRM_LEVEL}"));
				</script>
             </td>
           </tr>
           <tr >
             <td align="right">VI支持总金额：</td>
             <td align="left">${dMap.VI_SUPPORT_AMOUNT}</td>
             <td align="right">VI支持首批比例：</td>
             <td align="left">${dMap.VI_SUPPORT_RATIO}</td>
           </tr>
           <tr >
             <td align="right">VI支持后续支持方式：</td>
             <td align="left">${dMap.VI_SUPPORT_TYPE}</td>
             <td align="right">VI支持起始时间：</td>
             <td align="left">${dMap.VI_SUPPORT_DATE}</td>
           </tr>
           <tr >
             <td align="right">VI支持截止时间：</td>
             <td align="left">${dMap.VI_SUPPORT_END_DATE}
             </td>
             <td align="right">首次提车时间：</td>
             <td align="left">${dMap.FIRST_SUB_DATE}
             </td>
           </tr>
           <tr >
             <td align="right">首次到车日期：</td>
             <td align="left">
             	${dMap.FIRST_GETCAR_DATE}
             </td>
             <td align="right">首次销售时间：</td>
             <td align="left">
             	${dMap.FIRST_SAELS_DATE}
             </td>
           </tr>
           <tr >
             <td align="right">企业授权类型：</td>
             <td align="left">${dMap.AUTHORIZATION_TYPE}</td>
             <td align="right">授权时间：</td>
             <td align="left">${dMap.AUTHORIZATION_DATE}</td>
           </tr>
           <tr >
             <td align="right">是否经营其他品牌：</td>
             <td align="left">
             	<script type="text/javascript">
					document.write(getItemValue("${dMap.IS_ACTING_BRAND}"));
				</script>
             </td>
             <td align="right">代理其它品牌名称：</td>
             <td align="left">
             	${dMap.ACTING_BRAND_NAME}
             </td>
           </tr>
           <tr >
             <td align="right">配件储备金额（万元）：</td>
             <td align="left">${dMap.PARTS_STORE_AMOUNT}</td>
             <td align="right">&nbsp;</td>
             <td align="left">&nbsp;</td>
           </tr>  
	       <tr>
	        <td align="right" width="20%">备注：</td>
	      	<td align="left" colspan="3">
	      		
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
			<td align="right" width="20%">开票名称：</td>
			<td class="" width="30%"> 
				${dMap.ERP_CODE}
			</td>
			<td align="right" width="20%">税号：</td>
			<td class="" width="30%"> 
				${dMap.taxesNo}
			</td>
		 </tr>
		 <tr>
			<td align="right" width="20%">开户行：</td>
			<td class="" > 
				${dMap.BEGIN_BANK}
			</td>
			<td align="right" width="20%">账号：</td>
			<td class="" > 
				${dMap.INVOICE_ACCOUNT}
			</td>
		 </tr>
		 <tr>
			<td align="right" width="20%">开票地址：</td>
			<td class="" > 
				${dMap.INVOICE_ADD}
			</td>
			<td align="right" width="20%">电话：</td>
			<td class="" > 
				${dMap.INVOICE_PHONE}
			</td>
		 </tr>
		 <tr>
		   <td align="right">开票联系人：</td>
		   <td class="" >${dMap.INVOICE_PERSION}</td>
		   <td align="right"  class="">开票联系人手机：</td>
		   <td class="" >${dMap.INVOICE_TELPHONE}</td>
		   </tr>
		 <tr>
		   <td align="right">纳税人识别号：</td>
		   <td class="" >${dMap.TAXPAYER_NO}</td>
		   <td align="right">增值税发票：</td>
		   <td class="" >${dMap.TAX_INVOICE}</td>
		   </tr>
		 </table>
         <table  width=100% border="0" align="center" cellpadding="1" cellspacing="1"> 
	      <tr>
			<th colSpan=4 align=left><IMG class=nav src="<%=contextPath%>/img/subNav.gif">国家品牌</th>
    	  
          <tr>
			<td align="right" width="20%">国家品牌授权城市：</td>
			<td class="" width="30%"> 
				<script type="text/javascript">
					document.write(getItemValue("${dMap.IS_AUTHORIZE_CITY}"));
				</script>
			</td>
			<td align="right" width="25%">国家品牌授权区县：</td>
			<td class="" width="25%"> 
				<script type="text/javascript">
					document.write(getItemValue("${dMap.IS_AUTHORIZE_COUNTY}"));
				</script>
			</td>
		 </tr>
          <tr>
            <td align="right">国家品牌授权：</td>
            <td class="" >${dMap.AUTHORIZE_BRAND}</td>
            <td align="right">国家品牌授权信息收集时间：</td>
            <td class="" >
            	${dMap.AUTHORIZE_GET_DATE}
            </td>
          </tr>
          <tr>
            <td align="right">国家品牌授权提交时间：</td>
            <td class="" >
            	${dMap.AUTHORIZE_SUB_DATE}
            </td>
            <td align="right">国家品牌授权起始时间：</td>
            <td class="" >
            	${dMap.AUTHORIZE_EFFECT_DATE}
            </td>
          </tr>
          <tr>
            <td align="right">工商总局公告号：</td>
            <td class="" >
            	${dMap.ANNOUNCEMENT_NO}
            </td>
            <td align="right">工商总局公布日期：</td>
            <td class="" >
            	${dMap.ANNOUNCEMENT_DATE}
            </td>
          </tr>
          <tr>
            <td align="right">国家品牌授权截止时间：</td>
            <td class="" >
            	${dMap.ANNOUNCEMENT_END_DATE}
            </td>
            <td align="right">&nbsp;</td>
            <td class="" >&nbsp;</td>
          </tr>
 </table>
 
</body>
</html>
