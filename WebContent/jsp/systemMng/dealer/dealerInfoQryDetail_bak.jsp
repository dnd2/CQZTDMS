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

<script type="text/javascript">
var dealerLevel=<%=Constant.DEALER_LEVEL_01%>;
function doInit()
{  	
	var dl=<c:out value="${map.DEALER_LEVEL}"/>;
   	var dt=<c:out value="${map.DEALER_TYPE}"/>;
	if(dt==<%=Constant.DEALER_TYPE_DWR%>){
		$('shfw').style.display='';
		$('zcxs').style.display='none';

	}
	else{
		$('shfw').style.display='none';
		$('zcxs').style.display='';
	}	
	if(dealerLevel==dl)
	{
		$('kpxx').style.display='';	
	}else{
		$('kpxx').style.display='none';	
	}
}
</script>
</head>
<body onload="genLocSel('txt1','txt2','txt3','<c:out value="${map.PROVINCE_ID}"/>','<c:out value="${map.CITY_ID}"/>','<c:out value="${map.COUNTIES}"/>');">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;经销商查看</div>
 <form method="post" name = "fm" >
 <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    	  <TR>
			<TH colSpan=4 align=left><IMG class=nav src="<%=contextPath%>/img/subNav.gif"> 经销商基础信息</TH>
    	  <tr>
		  <tr>
		    <td align="right" width="15%">经销商代码：</td>
		    <td align="left">${map.DEALER_CODE}
		    </td>
		    <td align="right" width="15%">经销商名称：</td>
		    <td align="left">
		    ${map.DEALER_NAME}
		    </td>
	      </tr>
	       <tr>
		   <!-- <td align="right" width="15%">经销商简称：</td>
		    <td align="left">
		    <input type='text'  class="middle_txt" name="SHORT_NAME"  id="SHORT_NAME" datatype="1,is_null,75"  value="" maxlength="75"/>
		    </td>-->
		    <td align="right" width="15%">经销商等级：</td>
		    <td colspan="3" align="left">
		    <script type="text/javascript">
		   			 document.write(getItemValue(${map.DEALER_LEVEL}))
		    </script>
		   			
		    </td>
	      </tr>
	        <tr>
		    <td align="right" width="15%">上级组织：</td>
		    <td align="left">
		    		${map.ORG_CODE}
		    </td>
		    <td align="right" width="15%">上级经销商：</td>
		    <td align="left">
		   			${map.SJDEALERCODE}
		    </td>
	      </tr>
	       <tr>
		    <td align="right" width="15%">经销商类型：</td>
		    <td align="left">
		    	<script type="text/javascript">
		   		     document.write(getItemValue(${map.DEALER_TYPE}))
		       </script>
		    </td>
		    <td align="right" width="15%">经销商公司：</td>
		    <td align="left">
		    		${map.COMPANY_SHORTNAME}
		    </td>
	      </tr>
	      
	      <tr id="zcxs">
			<td width="100%" colspan=4>
			<table  width=100% border="0" align="center" cellpadding="1" cellspacing="1">
			<tr>
			<th colSpan=4 align=left><img class=nav src="<%=contextPath%>/img/subNav.gif"> 整车销售信息</th>
			</tr>
		 <tr>
	      <td align="right" width="15%">品牌：<FONT color="red">*</FONT> <input type="hidden" id=brandq name="brandq" value=""/> </td>
	      <td align="left">
	      		<input type="hidden" id="brandh" name="brandh" value="${map.BRAND }" />
			      <c:forEach items="${brand}" var="ber">
			      <input type="checkbox" disabled="disabled"  id="brand_${ber.GROUP_CODE}"  name="brand" 	 value="${ber.GROUP_CODE}"   >${ber.GROUP_NAME}&nbsp;
			      </c:forEach>
	       </td>
			<td align="right" width="15%">是否特殊客商：</td>
		    <td align="left">
		    	<script type="text/javascript">
		   		     document.write(getItemValue(${map.IS_SPECIAL}))
		       </script>
				
			</td>
	      </tr>
			 <tr>
		      <td align="right" width="15%">信息状态：</td>
		      <td align="left">
		      <script type="text/javascript">
		   		     document.write(getItemValue(${map.SERVICE_STATUS}))
		       </script>
		      		
		      </td>
		      <td align="right" width="15%">省份：</td>
		      <td align="left">${map.PROVINCE_NAME } </td>
          </tr>
          
           <tr> 
          <td align="right" width="15%">地级市：</td>
	      <td align="left">${map.CITY_NAME }</td>  
	      <td align="right" width="15%">区/县：</td>
		  <td align="left"> ${map.COUNTIES}</td>
	     	</tr> 
           <tr>
         <td align="right" width="15%">建站日期：</td>
         <td align="left">
        	${map.SITEDATE }
         </td>
         <td align="right" width="15%">撤站日期：</td>
         <td align="left">
              ${map.DESTROYDATE }
         </td>
         
         </tr>
         <!-- <tr>
	      <td align="right" width="15%">联系人：</td>
	      <td align="left"><input type="text"  class="middle_txt" name="linkMan"  id="linkMan"  datatype="1,is_name,10" value="" maxlength="10"/></td>
	      <td align="right" width="15%">电话：</td>
	      <td align="left"><input type="text"  class="middle_txt" name="phone"  id="phone" datatype="1,my_phone,25" value="" maxlength="25"/></td> 
	     </tr> -->
	      <tr>
	      <td align="right" width="15%">传真：</td>
	      <td align="left">${map.FAX_NO}</td>
	      <td align="right" width="15%">Email：</td>
	      <td align="left">${map.EMAIL}</td>
	      </tr>
	       <tr>
			<td align="right" width="15%">邮编：</td>
		    <td align="left">${map.ZIP_CODE}</td>
		    <td align="right" width="15%">法人：</td>
		    <td align="left">${map.LEGAL}</td>
		  </tr>
	       <tr>
			<td align="right" width="15%">法人电话：</td>
		    <td align="left">${map.LEGAL_TEL}</td>
		    <td align="right" width="15%">总经理姓名：</td>
			<td align="left"> 
				${map.WEBMASTER_NAME}
			</td>
		  </tr>
	      <tr>
		<td align="right" width="15%">总经理电话：</td>
		<td align="left"> 
			${map.WEBMASTER_PHONE}
		</td>
		<td align="right" width="15%">24小时值班电话：</td>
	    <td align="left">${map.DUTY_PHONE}</td>
		</tr>  
	      <tr>
			<td align="right" width="15%">销售经理姓名：</td>
		    <td align="left">${map.MARKET_NAME}</td>
		    <td align="right" width="15%">销售经理电话：</td>
			<td align="left"> 
				${map.MARKET_TEL}
			</td>
		  </tr>
	      <tr>
		<td align="right" width="15%">行政级别：</td>
		<td align="left"> 
		${map.ADMIN_LEVEL}
		</td>
		<td align="right" width="15%">经销商状态：</td>
		      <td align="left">
		     <script type="text/javascript">
		   		     document.write(getItemValue(${map.STATUS}))
		       </script>
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
		<td align="right" width="15%">昌铃形象等级：</td>
		<td align="left"> 
		${map.IMAGE_LEVEL}
		</td>
		<td align="right" width="15%">昌汽形象等级：</td>
	    <td align="left">
		${map.IMAGE_LEVEL2}
		</td>
	</tr>     
	  <tr>
		<td align="right" width="15%">昌铃形象店地址：</td>
		<td align="left" colspan="3"> 
		${map.CH_ADDRESS}
		</td>
	</tr>
	<tr>
		<td align="right" width="15%">昌汽形象店地址：</td>
	    <td align="left" colspan="3">
		${map.CH_ADDRESS2}
		</td>
	</tr>    
	 <tr>
	        <td align="right" width="15%">营业执照注册地址：</td>
	      	<td align="left" colspan="3">
		${map.ZZADDRESS}
	      	</td>
	      </tr>
	    <tr>
	        <td align="right" width="15%">通信地址：</td>
	      	<td align="left" colspan="3">
	      		${map.ADDRESS}
	      	</td>
	      </tr>  
	       <tr>
	        <td align="right" width="15%">备注：</td>
	      	<td align="left" colspan="3">
	      		${map.REMARK}
	      	</td>
	      </tr>
			</table>
			</td>
    	  <tr>
    	  
    	  
    	  
    	  <tr id="shfw" style="display: none">
			<td width="100%" colspan=4>
			<table  width=100% border="0" align="center" cellpadding="1" cellspacing="1">
			<tr>
			<th colSpan=4 align=left><img class=nav src="<%=contextPath%>/img/subNav.gif"> 售后服务信息</th>
			</tr>
	      <!--  销售需要以下字段-->
	        <tr>
	        <td align="right" width="15%">服务站状态：</td>
		    <td align="left">
		    <script type="text/javascript">
		   		     document.write(getItemValue(${map.SERVICE_STATUS}))
		       </script>
		  		
		    </td>
		     <%
	    	java.util.Map map=(java.util.Map)request.getAttribute("map");
		    %>
		    <td align="right" width="15%">是否通过DQV：</td>
		    <td align="left">
				 <%
		    	String IS_DQV=com.infodms.dms.util.CommonUtils.checkNull(map.get("IS_DQV"));
		    	if(IS_DQV.equals("")) IS_DQV=Constant.IF_TYPE_NO.toString();
		    %>
				 <script type="text/javascript">
		   		     document.write(getItemValue(<%=IS_DQV%>))
		       </script>
				
			</td>
	      </tr>
	      
	       <tr>
	       <td align="right" width="15%">结算等级：</td>
		    <td align="left">
		    ${map.BALANCE_LEVEL}
		    </td>
		    <td align="right" width="15%">开票等级：</td>
		    <td align="left">
		    ${map.INVOICE_LEVEL}
		    </td>
	      </tr>
		 <tr>
	      <td align="right" width="15%">品牌：<FONT color="red">*</FONT><input type="hidden" id=brandq name="brandq" value=""/>  </td>
	      <td align="left">
	      	 <input type="hidden" id="brandh" name="brandh" value="${map.BRAND }" />
	      
			      <c:forEach items="${brand}" var="ber">
			      <input type="checkbox" disabled="disabled"  id="brand_${ber.GROUP_CODE}"  name="brand" 	 value="${ber.GROUP_CODE}"   >${ber.GROUP_NAME}</input>&nbsp;
			      </c:forEach>
	       </td>
			<td align="right" width="15%">是否特殊客商：</td>
		    <td align="left">
		    <script type="text/javascript">
		   		     document.write(getItemValue(${map.IS_SPECIAL}))
		       </script>
			
			</td>
	      </tr>
			 <tr>
		      <td align="right" width="15%">经销商状态：</td>
		      <td align="left">
		       <script type="text/javascript">
		   		     document.write(getItemValue(${map.STATUS}))
		       </script>
		      </td>
		      <td align="right" width="15%">省份：</td>
		      <td align="left">${map.PROVINCE_NAME } </td>
          </tr>
          
           <tr> 
          <td align="right" width="15%">地级市：</td>
	      <td align="left">${map.CITY_NAME }</td>  
	      <td align="right" width="15%">区/县：</td>
		  <td align="left">  ${map.COUNTIES}</td>
	     	</tr> 
           <tr>
         <td align="right" width="15%">建站日期：</td>
         <td align="left">
         ${map.SITEDATE }
         </td>
         <td align="right" width="15%">撤站日期：</td>
         <td align="left">
          ${map.DESTROYDATE }
         </td>
         
         </tr>
         <tr>
             <td align="right" width="15%">索赔员姓名：</td>
             
               <td align="left">${map.SPY_MAN}</td>
              <td align="right" width="15%">索赔员电话：</td>
         	  <td align="left">${map.SPY_PHONE}</td> 
         </tr>
         <!-- <tr>
	      <td align="right" width="15%">联系人：</td>
	      <td align="left"><input type="text"  class="middle_txt" name="linkMan"  id="linkMan"  datatype="1,is_name,10" value="" maxlength="10"/></td>
	      <td align="right" width="15%">电话：</td>
	      <td align="left"><input type="text"  class="middle_txt" name="phone"  id="phone" datatype="1,my_phone,25" value="" maxlength="25"/></td> 
	     </tr> -->
	      <tr>
	      <td align="right" width="15%">传真：</td>
	      <td align="left">${map.FAX_NO}</td>
	      <td align="right" width="15%">Email：</td>
	      <td align="left">${map.EMAIL}</td>
	      </tr>
	       <tr>
			<td align="right" width="15%">邮编：</td>
		    <td align="left">${map.ZIP_CODE}</td>
		    <td align="right" width="15%">法人：</td>
		    <td align="left">${map.LEGAL}</td>
		  </tr>
	       <tr>
			<td align="right" width="15%">法人电话：</td>
		    <td align="left">${map.LEGAL_TEL}</td>
		    <td align="right" width="15%">站长姓名：</td>
			<td align="left"> 
				${map.WEBMASTER_NAME}
			</td>
		  </tr>
	      <tr>
		<td align="right" width="15%">站长电话：</td>
		<td align="left"> 
		${map.WEBMASTER_PHONE}
		</td>
		<td align="right" width="15%">24小时值班电话：</td>
	    <td align="left">${map.DUTY_PHONE}</td>
		</tr>  
	      <tr>
		<td align="right" width="15%">行政级别：</td>
		<td align="left"> 
		${map.ADMIN_LEVEL}
		</td>
		<td align="right" width="15%">维修资质：</td>
	    <td align="left">
		${map.MAIN_RESOURCES}
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
		<td align="right" width="15%">昌铃形象等级：</td>
		<td align="left"> 
		${map.IMAGE_LEVEL}
		</td>
		<td align="right" width="15%">昌汽形象等级：</td>
	    <td align="left">
		${map.IMAGE_LEVEL2}
		</td>
	</tr>     
	  <tr>
		<td align="right" width="15%">昌铃形象店地址：</td>
		<td align="left" colspan="3"> 
		${map.CH_ADDRESS}
		</td>
	</tr>
	<tr>
		<td align="right" width="15%">昌汽形象店地址：</td>
	    <td align="left" colspan="3">
		${map.CH_ADDRESS2}
		</td>
	</tr>    
	 <tr>
	        <td align="right" width="15%">营业执照注册地址：</td>
	      	<td align="left" colspan="3">
	      		${map.ZZADDRESS}
	      	</td>
	      </tr>
	    <tr>
	        <td align="right" width="15%">通信地址：</td>
	      	<td align="left" colspan="3">
	      		${map.ADDRESS}
	      	</td>
	      </tr>  
	       <tr>
	        <td align="right" width="15%">备注：</td>
	      	<td class="table_query_4Col_input" nowrap="nowrap" colspan="3">
	      		${map.REMARK}
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
				<td align="right" width="15%">开票名称：</td>
				<td align="left">${invoiceListMap.TAX_NAME}&nbsp;
				</td>
			</tr>
			<tr>
				<td   align="right" width="15%">开票类型：</td>
				<td align="left">
				 ${invoiceListMap.INV_TYPE}&nbsp;
				</td>
			</tr>
			<tr>
				<td   align="right" width="15%">国税号：</td>
				<td align="left">${invoiceListMap.TAX_NO }&nbsp;
				</td>
			</tr>
			<tr>
				<td   align="right" width="15%">开户行：</td>
				<td align="left">${invoiceListMap.BANK}&nbsp;
				</td>
			</tr>
			<tr>
				<td   align="right" width="15%">账号：</td>
				<td align="left">
				${invoiceListMap.ACCOUNT}&nbsp;
				</td>
			</tr>
			<tr>
				<td   align="right" width="15%">电话：</td>
				<td align="left">${invoiceListMap.TEL}&nbsp;
				</td>
			</tr>
			<tr>
				<td   align="right" width="15%">地址：</td>
				<td align="left">${invoiceListMap.ADDR}&nbsp;
				</td>
			</tr>
			<tr>
				<td  align="right" width="15%">发票邮寄地址：</td>
				<td align="left">${invoiceListMap.MAIL_ADDR}&nbsp;
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
				${map.ERP_CODE}&nbsp;
			</td>
			<td align="right" width="15%">税号：</td>
			<td align="left"> 
				${map.TAXES_NO }&nbsp;
			</td>
		 </tr>
		 <tr>
			<td align="right" width="15%">开户行：</td>
			<td align="left"> 
				${map.BANK}&nbsp;
			</td>
			<td align="right" width="15%">账号：</td>
			<td align="left"> 
				${map.INVOICE_ACCOUNT}&nbsp;
			</td>
		 </tr>
		 <tr>
		 <td align="right" width="15%">电话：</td>
			<td align="left"> 
				${map.INVOICE_PHONE}&nbsp;
			</td>
			<td align="right" width="15%">地址：</td>
			<td align="left"> 
				${map.INVOICE_ADD}&nbsp;
			</td>
		 </tr>
		 <tr>
			<td align="right" width="15%">发票邮寄地址：</td>
			<td align="left" colspan="3"> 
				${map.INVOICE_POST_ADD}&nbsp;
			</td>
		 </tr>
		 </c:otherwise>  
	      </c:choose>
		 </table>
			</td>
    	  <tr>
	 </table>
    <br/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query" id="businessTable">
     <tr><th colspan="7"><img src="<%=contextPath%>/img/nav.gif" />经销商地址列表  </th></tr>
     <tr>
     <th>地址代码</th>
     <th>地址名称(按地址名称拼音字母排序)</th>
     <th>联系人</th>
     <th>电话</th>
     <TH>手机</TH>
     <th>状态</th>
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
     </tr>
     </c:if>
     <c:if test="${al.STATUS==10011002}">
     <tr class="table_list_row1">
     <td><FONT color="red"><c:out value="${al.ADD_CODE}"/></FONT></td>
     <td><FONT color="red"><c:out value="${al.ADDRESS}"/></FONT></td>
     <td><FONT color="red"><c:out value="${al.LINK_MAN}"/></FONT></td>
     <td><FONT color="red"><c:out value="${al.TEL}"/></FONT></td>
    
     <td><script>document.write(getItemValue(${al.STATUS }));</script></td>
     </tr>
     </c:if>
     </c:forEach>
    </table>  
</form>
</body>
</html>
