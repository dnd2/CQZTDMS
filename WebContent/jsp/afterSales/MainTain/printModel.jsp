<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<style media="print">.no-print{display:none}</style>
<%
	String contextPath = request.getContextPath();
%>
<title>外出维修申请单打印</title>
</head>
<body>
<br>
<br>
<br>
<table width="100%" cellpadding="1" align="center" class="no-print">   
	<tr>    
		<td>   
			<div id="kpr" class="page-print-buttons" align="center">    
				<input class="ipt" type="button" value="打印"/>    
				<input class="ipt" type="button" value="打印设置"/>    
				<input class="ipt" type="button" value="打印预览"/>    
			</div>
		</td>
	</tr>     
</table>	
<div align="center">
<form  method="post" name="fm" id="fm">
<!--主表信息-->
  <table border="1" cellpadding="0" cellspacing="0" style="text-align: center;">
  		<tr>
  			<td colspan="16" style="height:50px;font-weight:bold;font-size: 14pt">
  				外出救援申请单
  			</td>
  		</tr>
      <tr>
        <td colspan="2" height="35px" align="center">单据编码：</td>
        <td colspan="2" height="35px" align="center">${info.EGRESS_NO }</td>
        <td colspan="2" height="35px" align="center">制单人：</td>
        <td colspan="2" height="35px" align="center">${info.CREATE_BY }</td>
        <td colspan="2" height="35px" align="center">制单日期：</td>
        <td colspan="6" height="35px" align="center">${info.CREATE_DATE }</td>
      </tr>
      <tr>
        <td colspan="2" height="35px" align="center">服务站编码：</td>
        <td colspan="2" height="35px" align="center">${info.DEALER_CODE }</td>
        <td colspan="2" height="35px" align="center">服务站：</td>
        <td colspan="2" height="35px" align="center">${info.DEALER_NAME }</td>
        <td colspan="2" height="35px" align="center">联系电话：</td>
        <td colspan="6" height="35px" align="center">${info.PHONE }</td>
      </tr>
      <tr>
        <td colspan="2" height="35px" align="center">VIN码：</td>
        <td colspan="2" height="35px" align="center">${info.VIN }</td>
        <%-- <td colspan="2" height="35px" align="center">发动机号：</td>
        <td colspan="2" height="35px" align="center">${info.ENGINE_NO }</td> --%>
        <td colspan="2" height="35px" align="center">车系名称：</td>
        <td colspan="2" height="35px" align="center">${info.SE_NAME }</td>
        <td colspan="2" height="35px" align="center">车型名称：</td>
        <td colspan="6" height="35px" align="center">${info.GROUP_NAME }</td>
      </tr>

      <tr>       
        <td colspan="2" height="35px" align="center">配置：</td>
        <td colspan="2" height="35px" align="center">${info.PZ_NAME }</td>
        <td colspan="2" height="35px" align="center">车牌号：</td>
        <td colspan="2" height="35px" align="center">${info.LICENSE_NO }</td>
        <td colspan="2" height="35px" align="center">车主名字：</td>
        <td colspan="6" height="35px" align="center">${info.CTM_NAME }</td>
      </tr>
      <tr>     	
      	<td colspan="2" height="35px" align="center">出厂时间：</td>
        <td colspan="2" height="35px" align="center">${info.FACTORY_DATE }</td>
        <td colspan="2" height="35px" align="center">购车日期：</td>
        <td colspan="2" height="35px" align="center">${info.PURCHASED_DATE }</td>
        <td colspan="2" height="35px" align="center">用户地址：</td>
        <td colspan="6" height="35px" align="center">${info.ADDRESS }</td>
      </tr>
      <tr>        
      	<td colspan="2" height="35px" align="center">客户名字：</td>
        <td colspan="2" height="35px" align="center">${info.CUSTOMER_NAME }</td>
        <td colspan="2" height="35px" align="center">客户电话：</td>
        <td colspan="2" height="35px" align="center">${info.TELEPHONE }</td>
        
        <td colspan="2" height="35px" align="center">行驶里程：</td>
        <td colspan="6" height="35px" align="center">${info.MILEAGE }</td>
      </tr>
      <tr>       
        <td colspan="2" height="35px" align="center">单程救急里程(公里)：</td>
        <td colspan="2" height="35px" align="center">${info.RELIEF_MILEAGE }</td>
        <td colspan="2" height="35px" align="center">救急所在省：</td>
        <td colspan="2" height="35px" align="center">${info.PROVINCE }</td>
        <td colspan="2" height="35px" align="center">救急所在市：</td>
        <td colspan="6" height="35px" align="center">${info.CITY }</td>
      </tr>
      <tr>       
        <td colspan="2" height="35px" align="center">救急所在县(区)：</td>
        <td colspan="2" height="35px" align="center">${info.COUNTY}</td>
        <td colspan="2" height="35px" align="center">详细地址：</td>
        <td colspan="2" height="35px" align="center">${info.TOWN }</td>
        <td colspan="2" height="35px" align="center">救急开始时间：</td>
        <td colspan="6" height="35px" align="center">${info.E_START_DATE }</td>
      </tr>
       <tr>      
        <td colspan="2" height="35px" align="center">救急结束时间：</td>
        <td colspan="2" height="35px" align="center">${info.E_END_DATE }</td>
        <td colspan="2" height="35px" align="center">救急人数：</td>
        <td colspan="2" height="35px" align="center">${info.E_NUM }</td>
        <td colspan="2" height="35px" align="center">派车车牌号：</td>
        <td colspan="6" height="35px" align="center">${info.E_LICENSE_NO }</td>
       </tr>
      <tr>
      	<td colspan="2" height="35px" align="center">救急人名字：</td>
        <td colspan="14" height="35px" align="center">${info.E_NAME }</td>
      </tr>
      <tr>
        <td colspan="2" height="35px" align="center">审批人：：</td>
        <td colspan="2" height="35px" align="center">${info.MINISTER_AUDITING_BY }</td>
        <td colspan="2" height="35px" align="center">审批时间：</td>
        <td colspan="2" height="35px" align="center">${info.MINISTER_AUDITING_DATE }</td>
        <td colspan="2" height="35px" align="center"></td>
        <td colspan="6" height="35px" align="center"></td>
       </tr>
      <tr>
        <td colspan="2" height="35px" align="center">申请内容：</td>
        <td colspan="14" height="35px">${info.EGRESS_REAMRK }</td>
      </tr>
      <tr>
        <td colspan="2" height="35px" align="center">审核意见：</td>
        <td colspan="14" height="35px">${info.MINISTER_AUDITING_REAMRK }</td>
      </tr>
  </table>
</form>
</div>
</body>
</html>