<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>集团客户报备更改查询</title>
</head>
<body onload="showPactPart() ;">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;集团客户报备更改详情</div>
 <form method="post" name = "fm" >
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    <th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 提报单位信息</th>
		  <tr>
		    <td class="table_query_2Col_label_4Letter">提报单位：</td>
		    <td><c:out value="${fleetlogMap.DEALER_SHORTNAME}"/></td>
		    <td class="table_query_2Col_label_4Letter">批售经理：</td>
		    <td><c:out value="${fleetlogMap.NAME}"/></td>
	      </tr>
	      <tr><td class="table_query_2Col_label_4Letter">提报日期：</td>
		      <td><c:out value="${fleetlogMap.SUBMIT_DATE}"/></td>
		        <td class="table_query_2Col_label_4Letter">批售经理电话：</td>
		    <td><c:out value="${fleetlogMap.PACT_MANAGE_PHONE}"/></td>
		  </tr>
	<th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 集团客户信息</th>
	    <tr><th colspan="4" align="left">客户信息</th></tr>
	 	<tr>
		    <td class="table_query_2Col_label_4Letter">客户名称：</td>
		    <td><c:out value="${fleetlogMap.FLEET_NAME}"/></td>
		    <td class="table_query_2Col_label_4Letter">客户类型：</td>
		    <td><script type='text/javascript'>
				writeItemValue('<c:out value="${fleetlogMap.FLEET_TYPE}"/>');
			  </script></td>
	      </tr>
	       <tr>
		    <td class="table_query_2Col_label_4Letter">主营业务：</td>
		    <td><script type='text/javascript'>
				writeItemValue('<c:out value="${fleetlogMap.MAIN_BUSINESS}"/>');
			  </script></td>
		    <td class="table_query_2Col_label_4Letter">资金规模：</td>
		    <td><script type='text/javascript'>
				writeItemValue('<c:out value="${fleetlogMap.FUND_SIZE}"/>');
			  </script></td>
	      </tr>
	        <tr>
		    <td class="table_query_2Col_label_4Letter">人员规模：</td>
		    <td><script type='text/javascript'>
				writeItemValue('<c:out value="${fleetlogMap.STAFF_SIZE}"/>');
			  </script></td>
		    <td class="table_query_2Col_label_4Letter">邮编：</td>
		    <td><c:out value="${fleetlogMap.ZIP_CODE}"/></td>
	      </tr>
		  <tr>
		    <td class="table_query_2Col_label_4Letter">区域：</td>
		    <td><script type='text/javascript'>
				writeRegionName('<c:out value="${fleetlogMap.REGION}"/>');
			  </script></td>
		    <td class="table_query_2Col_label_4Letter">是否批售项目：</td>
		    <td align="left"><script type='text/javascript'>
				writeItemValue('<c:out value="${fleetlogMap.IS_PACT}"/>');
			  </script>
			  <input type="hidden" name="isPact" id="isPact" value="${fleetlogMap.IS_PACT}" />
			  </td>
	      </tr>
	      <tr id="pactPart">
	      	<td class="table_query_2Col_label_4Letter">批售项目名称：</td>
		    <td align="left">${fleetlogMap.PACT_NAME}</td>
		    <td class="table_query_2Col_label_4Letter"></td>
		    <td align="left"></td>
	      </tr>
	      <tr>
	        <td class="table_query_2Col_label_4Letter">详细地址：</td>
	      	<td><c:out value="${fleetlogMap.ADDRESS}"/></td>
	      </tr>
	      <th colspan="4" align="left"> 联系人信息</th>
	      <tr>
		    <td class="table_query_2Col_label_5Letter">主要联系人：</td>
		    <td><c:out value="${fleetlogMap.MAIN_LINKMAN}"/></td>
		    <td class="table_query_2Col_label_4Letter">职务：</td>
		    <td><c:out value="${fleetlogMap.MAIN_JOB}"/></td>
	      </tr>
	      <tr>
		    <td class="table_query_2Col_label_4Letter">电话：</td>
		    <td><c:out value="${fleetlogMap.MAIN_PHONE}"/></td>
		    <td class="table_query_2Col_label_4Letter">电子邮件：</td>
		    <td><c:out value="${fleetlogMap.MAIN_EMAIL}"/></td>
	      </tr>
	      <tr>
		    <td class="table_query_2Col_label_5Letter">其他联系人：</td>
		    <td><c:out value="${fleetlogMap.OTHER_LINKMAN}"/></td>
		    <td class="table_query_2Col_label_4Letter">职务：</td>
		    <td><c:out value="${fleetlogMap.OTHER_JOB}"/></td>
	      </tr>
	       <tr>
		    <td class="table_query_2Col_label_4Letter">电话：</td>
		    <td><c:out value="${fleetlogMap.OTHER_PHONE}"/></td>
		    <td class="table_query_2Col_label_4Letter">电子邮件：</td>
		    <td><c:out value="${fleetlogMap.OTHER_EMAIL}"/></td>
	      </tr>
	      <th colspan="4" align="left"> 需求说明</th>
	      <tr>
		    <td class="table_query_2Col_label_4Letter">拜访时间：</td>
		    <td><c:out value="${fleetlogMap.VISIT_DATE}"/></td>
		    <td class="table_query_2Col_label_4Letter">&nbsp;</td>
		    <td>&nbsp;</td>
	      </tr>
	      <tr>
		    <td class="table_query_2Col_label_4Letter">需求车系：</td>
		    <td><c:out value="${fleetlogMap.GROUP_NAME}"/></td>
		    <td class="table_query_2Col_label_4Letter">需求数量：</td>
		    <td><c:out value="${fleetlogMap.SERIES_COUNT}"/></td>
	      </tr>
	      <tr>
	      <td class="table_query_2Col_label_4Letter">备注：</td>
	      <td colspan="3" align="left"><c:out value="${fleetlogMap.REQ_REMARK}"/></td>
    	 </tr>
    	 <th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />更改确认状态</th>
    	 <tr>
	      <td class="table_query_2Col_label_4Letter">确认状态：</td>
	      <td colspan="3" align="left"><script type='text/javascript'>
				writeItemValue('<c:out value="${fleetlogMap.STATUS}"/>');
			  </script></td>
    	 </tr>
    	 <tr>
	      <td class="table_query_2Col_label_4Letter">确认说明：</td>
	      <td colspan="3" align="left"><c:out value="${fleetlogMap.AUDIT_REMARK}"/></td>
    	 </tr>
    	 <tr><td><input type="button" value="返回" name="backBtn" class="normal_btn" onclick="closeWindow();"/></td></tr>
     </table> 
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
<script type="text/javascript">
	function closeWindow(){
		_hide();
	}
	
	function showPactPart() {
		var iIsPact = document.getElementById("isPact").value ;
		var oPactPart = document.getElementById("pactPart") ;
		
		if(iIsPact == <%=Constant.IF_TYPE_YES%>) {
			oPactPart.style.display = "inline" ;
		} else {
			oPactPart.style.display = "none" ;
		}
	}
</script>
</body>
</html>
