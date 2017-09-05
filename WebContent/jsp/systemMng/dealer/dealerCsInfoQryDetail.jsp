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
				<th colSpan=4 align=left><img class=nav src="<%=contextPath%>/img/subNav.gif"> 服务商基础信息</th>
    	  	<tr>
    	  	<tr>
	    	  	<td align="right" width="20%">组织机构代码：</td>
	    	  	<td width="30%">
	    	  		${dMap.COMPANY_ZC_CODE }
				</td>
				<td align="right" width="20%">单位性质：</td>
				<td align="left" width="30%">
		      		${dMap.UNION_TYPE }
				</td>
			</tr>
		  	<tr>
			    <td align="right">服务商公司：</td>
			    <td align="left">
            		${dMap.COMPANY_NAME }
            	</td>
			    <td align="right" width="20%">服务商简称：</td>
			    <td align="left">
			    	${dMap.DEALER_SHORTNAME }
			    </td>
      		</tr>
		  	<tr>
		    	<td align="right" width="20%">服务商代码：</td>
		    	<td align="left">
		    		${dMap.DEALER_CODE }
		    	</td>
		    	<td align="right" width="20%">服务商名称：</td>
		    	<td align="left">
		    		${dMap.DEALER_NAME }
		    	</td>
	      	</tr>
	       	<tr>
		    	<td align="right" width="20%">服务商等级：</td>
		    	<td align="left">
                    <script type="text/javascript">
                        document.write(getItemValue("${dMap.DEALER_LEVEL}"));
                    </script>
		    	</td>
		    	<td align="right">与一级经销商关系：</td>
		    	<td>

			    	${dMap.DEALER_RELATION }

		    	</td>
	      	</tr>
	      		       	<tr>
		    	<td align="right" width="20%">经销商名称：</td>
		    	<td align="left">

                        ${dMap.DEALER_NAME_S}
		    	</td>
	      	</tr>
	        <tr>
			    <td align="right" width="20%">上级组织：</td>
			    <td align="left">
				    ${dMap.ORG_CODE}
			    </td>
			    <td align="right" width="20%">上级服务商：</td>
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
	
		      </tr>
           	<tr>
           	             <td align="right">邮编：</td>
	             <td align="left">
	             	${dMap.ZIP_CODE}
	             </td>
      		</tr><tr>
      		             	<td align="right">企业注册地址：</td>
	          	<td align="left" colspan="2">${dMap.ADDRESS}</td>
      		</tr>
      		<tr>
             	<td align="right">服务商地址：</td>
				<td align="left" colspan="3">
					${dMap.COMPANY_ADDRESS}
				</td>
      		</tr>
      		<tr>
				<td align="right">企业注册证号：</td>
				<td align="left" colspan="3">${dMap.ZCCODE}</td>
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
		    	<td align="right" width="20%">服务商状态：</td>
			    <td align="left" colspan="3">
					<script type="text/javascript">
						document.write(getItemValue("SERVICE_STATUS"));
					</script>
			    </td>
	      	</tr>
	      	<tr>
             	<td align="right">主营范围：</td>
             	<td align="left">${dMap.ZY_SCOPE}</td>
             	<td align="right">兼营范围：</td>
             	<td align="left">${dMap.JY_SCOPE}</td>
           	</tr>
           	<tr >
             	<td align="right">建站时间：</td>
             	<td align="left">${dMap.SITEDATE}
             	</td>
             	<td align="right">撤站时间：</td>
             	<td align="left">${dMap.DESTROYDATE}
             	</td>
           	</tr>
           	<tr>
		    	<td align="right" width="20%">固定资产：</td>
		    	<td align="left">${dMap.FIXED_CAPITAL }</td>
		    	<td align="right" width="20%">注册资金：</td>
		    	<td align="left">
		    		${dMap.REGISTERED_CAPITAL }
		    	</td>
	      	</tr>
	      	<tr>
		    	<td align="right" width="20%">服务站人数：</td>
		    	<td align="left">${dMap.PEOPLE_NUMBER }</td>
		    	<td align="right" width="20%">&nbsp;</td>
		    	<td align="left">&nbsp;</td>
	      	</tr>
	      	<tr>
				<td align="right">维修资质：</td>
				<td align="left">
	             	<script type="text/javascript">
						document.write(getItemValue("${dMap.MAIN_RESOURCES}"));
					</script>
				</td>
				<td align="right">结算等级：</td>
	         	<td align="left">
	             	<script type="text/javascript">
						document.write(getItemValue("${dMap.BALANCE_LEVEL}"));
					</script>
	           	</td>
	 		</tr>
         	<tr >
               	<td align="right">开票等级：</td>
               	<td align="left">
               		<script type="text/javascript">
						document.write(getItemValue("${dMap.INVOICE_LEVEL}"));
					</script>
               	</td>
               	<td align="right">建店类别：</td>
               	<td align="left">
               		<script type="text/javascript">
						document.write(getItemValue("${dMap.IMAGE_LEVEL}"));
					</script>
               	</td>
       		</tr>
	      	<tr>
				<td width="100%" colspan=4>
					<table  width=100% border="0" align="center" cellpadding="1" cellspacing="1">
					<tr>
						<th colSpan=4 align=left><img class=nav src="<%=contextPath%>/img/subNav.gif"> <span id="up_name_00">服务站信息</span></th>
					</tr>
					</table>
				</td>
			<tr>
               	<td align="right">营业时间：</td>
               	<td align="left">${dMap.OPENING_TIME}</td>
               	<td align="right">经营类型：</td>
               	<td align="left">
               		${dMap.WORK_TYPE }
               	</td>
        	</tr>
			<tr >
               	<td align="right">维修车间面积：</td>
               	<td align="left">${dMap.MAIN_AREA}</td>
               	<td align="right">配件库面积：</td>
               	<td align="left">${dMap.PARTS_AREA}</td>
        	</tr>
           	<tr >
               	<td align="right">接待室面积：</td>
               	<td align="left">${dMap.MEETING_ROOM_AREA}</td>
               	<td align="right">停车场面积：</td>
               	<td align="left">${dMap.DEPOT_AREA}</td>
             </tr>
<!--              <tr> -->
<!--                <td align="right">市场经理：</td> -->
<%--                <td align="left">${dMap.MANAGER_NAME}</td> --%>
<!--                <td align="right">市场经理办公电话：</td> -->
<%--                <td align="left">${dMap.MANAGER_PHONE}</td> --%>
<!--              </tr> -->
<!--              <tr> -->
<!--                <td align="right">市场经理手机：</td> -->
<%--                <td align="left">${dMap.MANAGER_TELPHONE}</td> --%>
<!--                <td align="right">市场经理邮箱：</td> -->
<%--                <td align="left">${dMap.MANAGER_EMAIL}</td> --%>
<!--              </tr> -->
           <tr >
             <td align="right">24小时服务热线：</td>
             <td colspan="3" align="left">${dMap.HOTLINE}</td>
             </tr>
             <tr >
               <td align="right">服务经理：</td>
               <td align="left">${dMap.SER_MANAGER_NAME}</td>
               <td align="right">服务经理办公电话：</td>
               <td align="left">${dMap.SER_MANAGER_PHONE}</td>
             </tr>
             <tr >
               <td align="right">服务经理手机：</td>
               <td align="left">${dMap.SER_MANAGER_TELPHONE}</td>
               <td align="right">服务经理邮箱：</td>
               <td align="left">${dMap.SER_MANAGER_EMAIL}</td>
             </tr>
             <tr >
               <td align="right">索赔主管：</td>
               <td align="left">${dMap.CLAIM_DIRECTOR_NAME}</td>
               <td align="right">索赔主管办公电话：</td>
               <td align="left">${dMap.CLAIM_DIRECTOR_PHONE}</td>
             </tr>
             <tr >
               <td align="right">索赔主管手机：</td>
               <td align="left">${dMap.CLAIM_DIRECTOR_TELPHONE}</td>
               <td align="right">索赔主管邮箱：</td>
               <td align="left">${dMap.CLAIM_DIRECTOR_EMAIL}</td>
             </tr>
             <tr >
               <td align="right">索赔传真：</td>
               <td align="left">${dMap.CLAIM_DIRECTOR_FAX}</td>
               <td align="right">服务主管：</td>
               <td align="left">
               		${dMap.SER_DIRECTOR_NAME}
               </td>
             </tr>
             <tr >
               <td align="right">服务主管办公电话：</td>
               <td align="left">${dMap.SER_DIRECTOR_PHONE}</td>
               <td align="right">服务主管手机：</td>
               <td align="left">
               		${dMap.SER_DIRECTOR_TELHONE}
               </td>
             </tr>
             <tr >
               <td align="right">技术主管：</td>
               <td align="left">${dMap.TECHNOLOGY_DIRECTOR_NAME}</td>
               <td align="right">技术主管手机：</td>
               <td align="left">
               		${dMap.TECHNOLOGY_DIRECTOR_TELPHONE}
               </td>
             </tr>
             <tr >
               <td align="right">财务经理：</td>
               <td align="left">${dMap.FINANCE_MANAGER_NAME}</td>
               <td align="right">财务经理办公电话：</td>
               <td align="left">
               		${dMap.FINANCE_MANAGER_PHONE}
               </td>
             </tr>
              <tr >
               <td align="right">财务手机：</td>
               <td align="left">${dMap.FINANCE_MANAGER_TELPHONE}</td>
               <td align="right">财务邮箱：</td>
               <td align="left">
               		${dMap.FINANCE_MANAGER_EMAIL}
               </td>
             </tr>
             <tr >
               <td align="right">配件主管  ：</td>
               <td align="left">${dMap.FITTINGS_DEC_NAME}</td>
               <td align="right">配件主管办公电话 ：</td>
               <td align="left">
               		${dMap.FITTINGS_DEC_TELPHONE}
               </td>
             </tr>

              <tr >
               <td align="right">配件主管邮箱  ：</td>
               <td align="left">${dMap.FITTINGS_DEC_EMAIL}</td>
               <td align="right">配件传真 ：</td>
               <td align="left">
               		${dMap.FITTINGS_DEC_FAX}
               </td>
             </tr>
                          <tr >
               <td align="right">配件主管手机  ：</td>
               <td align="left">${dMap.FITTINGS_DEC_PHONE}</td>
             </tr>
           <tr >
             <td align="right">企业授权类型：</td>
             <td align="left">
             	${dMap.AUTHORIZATION_TYPE }
             </td>
             <td align="right">授权时间：</td>
             <td align="left">${dMap.AUTHORIZATION_DATE}
             </td>
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
	      		${dMap.REMARK}
	      	</td>
	      </tr>
    	  <tr>
    	   <tr id="kpxx" >
			<td width="100%" colspan=4>
			<table  width=100% border="0" align="center" cellpadding="1" cellspacing="1"> 
		      <tr>
				<th colSpan=4 align=left><IMG class=nav src="<%=contextPath%>/img/subNav.gif"> 开票信息</th>
	    	  </tr>
	    	  </table>
	    	 </td>
    	  <tr>
			<td align="right" width="20%">开票名称：</td>
			<td class="" > 
				${dMap.ERP_CODE}
			</td>
			<td align="right" width="20%">税号：</td>
			<td class="" > 
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
			<td align="right" width="20%">服务站地址：</td>
			<td class="" > 
				${dMap.INVOICE_ADD}
			</td>
			<td align="right" width="20%">电话：</td>
			<td class="" > 
				${dMap.INVOICE_PHONE}
			</td>
		 </tr>
		 <tr>
		   <td align="right">纳税人识别号：</td>
		   <td class="" >${dMap.TAXPAYER_NO}</td>
		   <td align="right"  class="">纳税人性质：</td>
		   <td class="" >
		   		<script type="text/javascript">
		   			document.write(getItemValue('${dMap.TAXPAYER_NATURE }'));
		   		</script>
		   	</td>
		   </tr>
		 <tr>
		   <td align="right">增值税发票：</td>
		   <td class="" >
		   		<script type="text/javascript">
					document.write(getItemValue("${dMap.TAX_INVOICE}"));
				</script>
		   </td>
		   <td align="right"  class="">开票税率：</td>
		   <td class="" >${dMap.TAX_DISRATE}</td>
		   </tr>
		 <tr>
			<td align="right" width="20%">发票邮寄地址：</td>
			<td class=""  colspan="3"> 
				${dMap.INVOICE_POST_ADD}
			</td>
		 </tr>
	</table>
</body>
</html>
