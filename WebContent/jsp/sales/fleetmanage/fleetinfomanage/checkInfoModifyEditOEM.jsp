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
<title>集团客户报备更改审核</title>
</head>
<body onload="showPactPart() ;">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 集团客户管理 &gt; 集团客户信息管理 &gt;集团客户报备更改审核</div>
 <form method="post" name = "fm" id="fm">
    <input type="hidden" name="editId" id="editId" value="<c:out value="${fleetMap.EDIT_ID}"/>"/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    <th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 提报单位信息</th>
		  <tr>
		    <td align="right">提报单位：</td>
		    <td align="left"><c:out value="${fleetMap.DEALER_SHORTNAME}"/></td>
		    <td align="right">批售经理：</td>
		    <td align="left"><c:out value="${fleetMap.NAME}"/></td>
	      </tr>
	      <tr><td align="right">提报日期：</td>
		      <td align="left"><c:out value="${fleetMap.SUBMIT_DATE}"/></td>
		       <td align="right">批售经理电话：</td>
		    <td align="left"><c:out value="${fleetMap.PACT_MANAGE_PHONE}"/></td>
		  </tr>
	<th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 集团客户信息</th>
	    <tr><th colspan="4" align="left">客户信息</th></tr>
	 	<tr>
		    <td align="right">客户名称：</td>
		    <td align="left"><c:out value="${fleetMap.FLEET_NAME}"/></td>
		    <td align="right">客户类型：</td>
		    <td align="left"><script type='text/javascript'>
				writeItemValue('<c:out value="${fleetMap.FLEET_TYPE}"/>');
			  </script></td>
	      </tr>
	       <tr>
		    <td align="right">主营业务：</td>
		    <td align="left"><script type='text/javascript'>
				writeItemValue('<c:out value="${fleetMap.MAIN_BUSINESS}"/>');
			  </script></td>
		    <td align="right">资金规模：</td>
		    <td align="left"><script type='text/javascript'>
				writeItemValue('<c:out value="${fleetMap.FUND_SIZE}"/>');
			  </script></td>
	      </tr>
	        <tr>
		    <td align="right">人员规模：</td>
		    <td align="left"><script type='text/javascript'>
				writeItemValue('<c:out value="${fleetMap.STAFF_SIZE}"/>');
			  </script></td>
		    <td align="right">邮编：</td>
		    <td align="left"><c:out value="${fleetMap.ZIP_CODE}"/></td>
	      </tr>
		  <tr>
		    <td align="right">区域：</td>
		    <td align="left"><script type='text/javascript'>
				writeRegionName('<c:out value="${fleetMap.REGION}"/>');
			  </script></td>
		    <td align="right">是否批售项目：</td>
		    <td align="left"><script type='text/javascript'>
				writeItemValue('<c:out value="${fleetMap.IS_PACT}"/>');
			  </script>
			  <input type="hidden" name="isPact" id="isPact" value="${fleetMap.IS_PACT}" />
			  </td>
	      </tr>
	      <tr id="pactPart">
	      	<td align="right">批售项目名称：</td>
		    <td align="left">${fleetMap.PACT_NAME}</td>
		    <td align="right"></td>
		    <td align="left"></td>
	      </tr>
	      <tr>
	        <td align="right">详细地址：</td>
	      	<td align="left"><c:out value="${fleetMap.ADDRESS}"/></td>
	      </tr>
	      <th colspan="4" align="left"> 联系人信息</th>
	      <tr>
		    <td align="right">主要联系人：</td>
		    <td align="left"><c:out value="${fleetMap.MAIN_LINKMAN}"/></td>
		    <td align="right">职务：</td>
		    <td align="left"><c:out value="${fleetMap.MAIN_JOB}"/></td>
	      </tr>
	      <tr>
		    <td align="right">电话：</td>
		    <td align="left"><c:out value="${fleetMap.MAIN_PHONE}"/></td>
		    <td align="right">电子邮件：</td>
		    <td align="left"><c:out value="${fleetMap.MAIN_EMAIL}"/></td>
	      </tr>
	      <tr>
		    <td align="right">其他联系人：</td>
		    <td align="left"><c:out value="${fleetMap.OTHER_LINKMAN}"/></td>
		    <td align="right">职务：</td>
		    <td align="left"><c:out value="${fleetMap.OTHER_JOB}"/></td>
	      </tr>
	       <tr>
		    <td align="right">电话：</td>
		    <td align="left"><c:out value="${fleetMap.OTHER_PHONE}"/></td>
		    <td align="right">电子邮件：</td>
		    <td align="left"><c:out value="${fleetMap.OTHER_EMAIL}"/></td>
	      </tr>
	      <th colspan="4" align="left"> 需求说明</th>
	      <tr>
		    <td align="right">拜访日期：</td>
		    <td align="left"><c:out value="${fleetMap.VISIT_DATE}"/></td>
		    <td align="right">&nbsp;</td>
		    <td align="left">&nbsp;</td>
	      </tr>
	      <tr>
		    <td align="right">需求车系：</td>
		    <td align="left"><c:out value="${fleetMap.GROUP_NAME}"/></td>
		    <td align="right">需求数量：</td>
		    <td align="left"><c:out value="${fleetMap.SERIES_COUNT}"/></td>
	      </tr>
	      <tr>
	      <td align="right">备注：</td>
	      <td colspan="3" align="left"><c:out value="${fleetMap.REQ_REMARK}"/></td>
    	 </tr>
    	 <th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />审核信息</th>
    	 <tr><td align="right">审核意见：</td>
	      	 <td colspan="3" align="left"><textarea name="auditRemark" cols="70" rows="3"></textarea>
	         </td>
	     </tr>	 
    	 <tr><td><input type="button" value="确认" name="passBtn" class="normal_btn" onclick="passaApp()"/>
    	         <input type="button" value="驳回" name="rejectBtn" class="normal_btn" onclick="rejectApp()"/>
    	         <input type="button" value="返回" name="backBtn" class="normal_btn" onclick="javascript:history.go(-1);"/></td>
    	 </tr>
     </table> 
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
<script type="text/javascript">
    // 确认的ACTION提交
	function passaApp(){
		if(submitForm('fm')){
			MyConfirm("是否确认通过?",passCnfrm);
		 }
	}
	
	// 确认处理
	function passCnfrm(){
		$('fm').action= '<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoModifyCheck/auditInfoModify.do?mark=1';
		$('fm').submit();
	}
	
	// 驳回的ACTION提交
	function rejectApp(){
		if(submitForm('fm')){
			MyConfirm("是否确认驳回?",rejectCnfrm);
		 }
	}
	
	// 驳回处理
	function rejectCnfrm(){
		$('fm').action= '<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoModifyCheck/auditInfoModify.do?mark=0';
		$('fm').submit();
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
