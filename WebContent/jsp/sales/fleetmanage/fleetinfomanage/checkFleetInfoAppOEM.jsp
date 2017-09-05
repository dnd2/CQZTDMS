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
<title>集团客户报备确认</title>
</head>
<body onload="showPactPart() ;">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 集团客户管理 &gt; 集团客户信息管理 &gt;集团客户报备确认</div>
 <form method="post" name="fm" id="fm">
    <input type="hidden" name="fleetId" id="fleetId" value="<c:out value="${fleetMap.FLEET_ID}"/>"/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    <th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 提报单位信息</th>
		  <tr>
		    <td align="right">提报单位：</td>
		    <td align="left"><c:out value="${fleetMap.COMPANY_SHORTNAME}"/></td>
		    <td align="right">批售经理：</td>
		    <td align="left"><c:out value="${fleetMap.NAME}"/></td>
	      </tr>
	      <tr><td align="right">提报日期：</td>
		      <td align="left"><c:out value="${fleetMap.SUBMIT_DAY}"/></td>
		        <td align="right">批售经理电话：</td>
		    <td align="left"><c:out value="${fleetMap.PACT_MANAGE_PHONE}"/></td>
		  </tr>
	<th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 集团客户信息</th>
	    <tr><th colspan="4" align="left">客户信息</th></tr>
	 	<tr>
		    <td align="right">客户名称：</td>
		    <td align="left">
		    	<c:out value="${fleetMap.FLEET_NAME}"/>
		    	<input name="button1" style="display:none;" type="button" class="mini_btn" style="cursor: pointer;" onclick="showFieetName('<c:out value="${fleetMap.FLEET_NAME}"/>','<c:out value="${fleetMap.FLEET_ID}"/>');" value="..." />    
		    </td>
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
		    <td align="right" class="table_query_2Col_label_5Letter">拜访时间：</td>
		    <td colspan="3"><c:out value="${fleetMap.VISIT_DATE}"/></td>
	      </tr>
<!--	      <tr>-->
<!--		    <td align="right">车系选择：</td>-->
<!--		    <td><c:out value="${fleetMap.GROUP_NAME}"/></td>-->
<!--		    <td align="right">数量：</td>-->
<!--		    <td><c:out value="${fleetMap.SERIES_COUNT}"/></td>-->
<!--	      </tr>-->
	<tr>
		<td class="table_query_2Col_label_5Letter" width="30%">市场信息：</td>
		<td colspan="3">
			${fleetMap.MARKET_INFO}
		</td>
		
    </tr>
    <tr>
		<td class="table_query_2Col_label_5Letter">配置要求：</td>
		<td colspan="3">
			${fleetMap.CONFIG_RQUIRE}
		</td>
		
    </tr>
    <tr>
		<td class="table_query_2Col_label_5Letter">大客户要求折让：</td>
		<td colspan="3">
			${fleetMap.FLEETREQ_DISCOUNT}
		</td>
		
    </tr>
    <tr>
		<td class="table_query_2Col_label_5Letter">其他竞争车型和优惠政策：</td>
		<td colspan="3">
			${fleetMap.OTHERCOMP_FAVORPOL}
		</td>
		
    </tr>
	
    <tr>
    <td colspan="4">
		<div id="showDiv" style="width:100%;">
			<table class="table_list" style="width:100%;border:1px solid #B0C4DE" id="showTab" border="1" cellpadding="1" bordercolor="#B0C4DE" >
				<tr>
					<th nowrap="nowrap" width="20%" ><center>预计车型编码</center></th>
					<th nowrap="nowrap" width="20%" ><center>预计车型名称</center></th>
					<th nowrap="nowrap" width="10%" ><center>车系</center></th>
					<th nowrap="nowrap" width="10%" ><center>预计数量</center></th>
					<th nowrap="nowrap" width="20%" ><center>说明</center></th>
				</tr>
				<c:forEach items="${tfrdMapList}" var="frdMap">
				<tr id="tr${frdMap.DETAIL_ID}">
					<td><input type="hidden" name="materialIds" value="${frdMap.MATERIAL_ID}" >${frdMap.MATERIAL_CODE}</td>
					<td>${frdMap.MATERIAL_NAME}</td>
					<td>${frdMap.GROUP_NAME}</td>
					<td> ${frdMap.AMOUNT}</td>
					<td>${frdMap.DISCRIBE}</td>
				</tr>
				</c:forEach>
			</table>
		</div>
	</td>
	</tr>
	      <tr>
	      <td align="right">备注：</td>
	      <td colspan="3" align="left"><c:out value="${fleetMap.REQ_REMARK}"/></td>
    	 </tr>
    	    </table> 
      <c:if test="${checkList!=null}">
		<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;审核信息</div>
		<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<tr>
			<th colspan="5" align="left">&nbsp;审核记录</th>
			</tr>
			<tr align="center">
				<td>审核部门</td>
				<td>审核时间</td>
				<td>审核人</td>
				<td>审核意见</td>
				<td>审核结果</td>
			</tr>
			<c:forEach items="${checkList}" var="checkList">
				<tr align="center">
					<td>${checkList.ORG_NAME}</td>
					<td>${checkList.AUDITDATE}</td>
					<td>${checkList.NAME}</td>
					<td>${checkList.AUDIT_REMARK}</td>
					<td>${checkList.CODE_DESC}</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
<div><th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />报备信息确认</th></div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    	 <tr>
    	 	 <td align="right">确认说明：</td>
	      	 <td colspan="3" align="left">
	      	 	<textarea name="auditRemark" id="auditRemark" cols="70" rows="2" datatype="0,is_null,200"></textarea>
	         </td>
	     </tr>	 
    	 <tr>
    	 	<td>
    	 		<input type="button" value="确认" name="passBtn" class="normal_btn" onclick="passaApp();"/>
    	        <input type="button" value="驳回" name="rejectBtn" class="normal_btn" onclick="rejectApp();"/>
    	    </td>
    	 </tr>
    	 </table>
</form>
<script type="text/javascript">
    // 确认的ACTION提交
	function passaApp(){
		var val = document.getElementById("auditRemark").value;
		if(!val)
		{
			MyAlert("请填写确认说明");
			$('fm').auditRemark.focus();
			return;
		}
		if(submitForm('fm')){
			MyConfirm("是否确认通过?",passCnfrm);
		 }
	}
	
	// 确认处理
	function passCnfrm(){
		$('fm').action= '<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoAppConfirm/auditFleetInfoApp.do?flag=1';
		$('fm').submit();
	}
	
	function rejectApp(){
		if(submitForm('fm')){
			MyConfirm("是否确认驳回?",rejectCnfrm);
		 }
	}
	
	function rejectCnfrm(){
		$('fm').action= '<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoAppConfirm/auditFleetInfoApp.do?flag=0';
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
	
	function showFieetName(val1,val2)
	{
		var name1=encodeURI(val1);
        var name2=encodeURI(name1);
		OpenHtmlWindow('<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoAppConfirm/showFieetInit.do?val1='+name2+'&val2='+val2,800,500);
	}
</script>
</body>
</html>
