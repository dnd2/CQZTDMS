<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
	//List fileList = (LinkedList)request.getAttribute("fileList");
	//request.setAttribute("fileList",fileList);
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>集团客户支持</title>
</head>
<body onload="showPactPart() ;">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;集团客户支持>集团客户支持申请>集团客户支持合同维护</div>
<form method="post" name = "fm" id="fm" >
<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;审核信息</div>
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="15%" align=right>审批意见：</td>
		<td width="85%" align="left"><c:out value="${cmap.BACK_RESON}"/></td>
	</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;提报单位信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="4" align="left">&nbsp;单位信息</th>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">提报单位：</td>
			<td><c:out value="${fleetMap.COMPANY_SHORTNAME}"/></td>
			<td class="table_query_2Col_label_4Letter">批售经理：</td>
			<td><c:out value="${fleetMap.NAME}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">提报日期：</td>
			<td><c:out value="${fleetMap.SUBMIT_DATE}"/></td>
				<td class="table_query_2Col_label_4Letter">批售经理电话：</td>
			<td><c:out value="${fleetMap.PACT_MANAGE_PHONE}"/></td>
		</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;集团客户信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="4" align="left">&nbsp;客户信息</th>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">客户名称：</td>
			<td><c:out value="${fleetMap.FLEET_NAME}"/></td>
			<td class="table_query_2Col_label_4Letter">客户类型：</td>
			<td>
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.FLEET_TYPE}"/>');
				</script>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">主营业务：</td>
			<td>
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.MAIN_BUSINESS}"/>');
				</script>
			</td>
			<td class="table_query_2Col_label_4Letter">资金规模：</td>
			<td>
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.FUND_SIZE}"/>');
				</script>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">人员规模：</td>
			<td>
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.STAFF_SIZE}"/>');
				</script>
			</td>
			<td class="table_query_2Col_label_4Letter">购车用途：</td>
			<td>
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.PURPOSE}"/>');
				</script>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">区域：</td>
			<td>
				<script type='text/javascript'>
					writeRegionName('<c:out value="${fleetMap.REGION}"/>');
				</script>
			</td>
			<td class="table_query_2Col_label_4Letter">邮编：</td>
			<td><c:out value="${fleetMap.ZIP_CODE}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">是否批售项目：</td>
		    <td align="left"><script type='text/javascript'>
				writeItemValue('<c:out value="${fleetMap.IS_PACT}"/>');
			  </script>
			  <input type="hidden" name="isPact" id="isPact" value="${fleetMap.IS_PACT}" />
			  </td>
	      	<td class="table_query_2Col_label_4Letter" id="pactPart">批售项目名称：</td>
		    <td align="left">${fleetMap.PACT_NAME}</td>
	      </tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">详细地址：</td>
			<td><c:out value="${fleetMap.ADDRESS}"/></td>
		</tr>
		<tr>
			<th colspan="4" align="left">&nbsp;联系人信息</th>
		</tr>
		<tr>
			<td class="table_query_2Col_label_5Letter">主要联系人：</td>
			<td><c:out value="${fleetMap.MAIN_LINKMAN}"/></td>
			<td class="table_query_2Col_label_4Letter">职务：</td>
			<td><c:out value="${fleetMap.MAIN_JOB}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_5Letter">电话：</td>
			<td><c:out value="${fleetMap.MAIN_PHONE}"/></td>
			<td class="table_query_2Col_label_4Letter">电子邮件：</td>
			<td><c:out value="${fleetMap.MAIN_EMAIL}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_5Letter">其他联系人：</td>
			<td><c:out value="${fleetMap.OTHER_LINKMAN}"/></td>
			<td class="table_query_2Col_label_4Letter">职务：</td>
			<td><c:out value="${fleetMap.OTHER_JOB}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_5Letter">电话：</td>
			<td><c:out value="${fleetMap.OTHER_PHONE}"/></td>
			<td class="table_query_2Col_label_4Letter">电子邮件：</td>
			<td><c:out value="${fleetMap.OTHER_EMAIL}"/></td>
		</tr>
		<tr>
			<th colspan="4" align="left">&nbsp;需求说明</th>
		</tr>
		<tr>
			<td class="table_query_2Col_label_5Letter">需求车系：</td>
			<td><c:out value="${fleetMap.GROUP_NAME}"/></td>
			<td class="table_query_2Col_label_4Letter">需求数量：</td>
			<td><c:out value="${fleetMap.SERIES_COUNT}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_5Letter">备注：</td>
			<td colspan="3" align="left"><c:out value="${fleetMap.REQ_REMARK}"/></td>
		</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;合同信息 </div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr align="center">
			<td class="table_query_2Col_label_6Letter">合同编号：</td>
			<td align="left" colspan="3">${cmap.CONTRACT_NO}</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">买方：</td>
			<td>${cmap.BUY_FROM}</td>
			<td class="table_query_2Col_label_6Letter">卖方：</td>
			<td>${cmap.SELL_TO}</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">有效期起：</td>
			<td>${cmap.START_DATE}</td>
			<td class="table_query_2Col_label_6Letter">有效期止：</td>
			<td>${cmap.END_DATE}</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">签订日期：</td>
			<td>${cmap.CHECK_DATE}</td>
			<td class="table_query_2Col_label_6Letter">特殊需求：</td>
			<td>${cmap.OTHER_REMARK}</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">特殊金额：</td>
			<td>
				<script type="text/javascript">
					document.write(amountFormatNew('${cmap.OTHER_AMOUNT}'))
				</script>
			</td>
			<td class="table_query_2Col_label_6Letter">折让总额：</td>
			<td>
				<script type="text/javascript">
					document.write(amountFormatNew('${cmap.DIS_AMOUNT}'))
				</script>
			</td>
		</tr>
		</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;购车意向</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="7" align="left">&nbsp;签约车系
			</th>
		</tr>
		<tr align="center">
			<td align="center">签约车系</td>
			<td align="center">数量</td>
			<td align="center">标准价(元)</td>
			<td align="center">合计金额(元)</td>
			<td align="center">支持点位</td>
			<td align="center">备注</td>
		</tr>
		<c:if test="${intentList!=null}">
			<c:forEach items="${intentList}" var="list">
				<tr align="center" class="table_list_row2">
					<td>${list.GROUP_NAME}</td>
					<td>${list.INTENT_COUNT}</td>
					<td>${list.NORM_AMOUNT}</td>
					<td>${list.COUNT_AMOUNT}</td>
					<td>${list.INTENT_POINT}%</td>
					<td>${list.REMARK}</td>
				</tr>
			</c:forEach>
		</c:if>
	</table>
<!-- 附件 开始  -->
	<c:if test="${fileFlag!=0}">
	   <table class="table_info" border="0" id="file">
		 <tr>
    		<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv_01.jsp" /></td>
  		 </tr>
  		 <c:if test="${fileList!=null}">
  			<c:forEach items="${fileList}" var="list">
  				<script type="text/javascript">
	 	 			showUploadRowByDb('${list.FILENAME}','${list.FILEID}','${list.FILEURL}','${list.FJID}');
	 	 		</script>
  			</c:forEach>
  		 </c:if>
	  </table> 
	</c:if>
<!-- 附件 结束 -->
	<table class="table_edit">
		<tr>
			<td>&nbsp;</td>
		</tr>
	</table>
	<table class="table_edit">
		<tr>
			<td align="center">
				<input class="cssbutton" name="button2" type="button" onclick="history.back();" value ="返回" />
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
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