<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
	List  executePlans = (List)request.getAttribute("executePlans");
	List  attachList   = (List)request.getAttribute("attachList");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>集团客户支持</title>
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
</head>
<body onload="showPactPart() ;">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;集团客户支持 > 集团客户支持申请> 集团客户支持查询明细</div>
<form method="post" name = "fm" >
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;提报单位信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="4" align="left">&nbsp;经销商信息</th>
		</tr>
		<tr>
			<td class="table_query_2Col_label_7Letter">经销商名称：</td>
			<td><c:out value="${fleetMap.COMPANY_SHORTNAME}"/></td>
			<td class="table_query_2Col_label_7Letter">批售经理姓名：</td>
			<td><c:out value="${fleetMap.NAME}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_7Letter">批售经理手机：</td>
			<td><c:out value="${fleetMap.PACT_MANAGE_PHONE}"/></td>
			<td class="table_query_2Col_label_7Letter">批售经理邮箱：</td>
			<td><c:out value="${fleetMap.PACT_MANAGE_EMAIL}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_7Letter">提报日期：</td>
			<td><c:out value="${fleetMap.SUBMIT_DATE}"/></td>
			<td></td>
			<td></td>
		</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;集团客户信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="4" align="left">&nbsp;客户信息</th>
		</tr>
		<tr>
			<td>大客户代码：</td>
			<td><c:out value="${fleetMap.FLEET_CODE}"/></td>
			<td></td>
			<td></td>
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
		<tr style="display:none;">
			<td class="table_query_2Col_label_4Letter">是否批售项目：</td>
		    <td align="left"><script type='text/javascript'>
				writeItemValue('<c:out value="${fleetMap.IS_PACT}"/>');
			  </script>
			  <input type="hidden" name="isPact" id="isPact" value="${fleetMap.IS_PACT}" />
			  </td>
	      	<td class="table_query_2Col_label_7Letter" id="pactPart">批售项目名称：</td>
		    <td align="left">${fleetMap.PACT_NAME}</td>
	      </tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">详细地址：</td>
			<td><c:out value="${fleetMap.ADDRESS}"/></td>
		</tr>
<!--		<tr>-->
<!--			<th colspan="4" align="left">&nbsp;联系人信息</th>-->
<!--		</tr>-->
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
		<tr style="display:none;">
			<td class="table_query_2Col_label_5Letter">其他联系人：</td>
			<td><c:out value="${fleetMap.OTHER_LINKMAN}"/></td>
			<td class="table_query_2Col_label_4Letter">职务：</td>
			<td><c:out value="${fleetMap.OTHER_JOB}"/></td>
		</tr>
		<tr style="display:none;">
			<td class="table_query_2Col_label_5Letter">电话：</td>
			<td><c:out value="${fleetMap.OTHER_PHONE}"/></td>
			<td class="table_query_2Col_label_4Letter">电子邮件：</td>
			<td><c:out value="${fleetMap.OTHER_EMAIL}"/></td>
		</tr>
		<tr>
			<th colspan="4" align="left">&nbsp;需求说明</th>
		</tr>
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
	      <tr >
	      <td align="left">备注：</td>
	      <td colspan="3" align="left">&nbsp;&nbsp;&nbsp;&nbsp;${fleetMap.REQ_REMARK}</td>
    	 </tr>
	</table>
<!--	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;购车意向</div>-->
<!--	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">-->
<!--		<tr>-->
<!--			<th colspan="4" align="left">&nbsp;意向信息</th>-->
<!--		</tr>-->
<!--		<tr>-->
<!--			<td class="table_query_2Col_label_6Letter">预计采购日期：</td>-->
<!--			<td align="left">${intentMap.PURCHASE_DATE}</td>-->
<!--			<td class="table_query_2Col_label_6Letter">信息来源：</td>-->
<!--			<td align="left">${intentMap.INFO_GIVING_MAN}</td>-->
<!--		</tr>-->
<!--		<tr>-->
<!--			<td class="table_query_3Col_label_6Letter">竞争情况说明：</td>-->
<!--			<td  colspan="3" align="left">${intentMap.COMPETE_REMARK}</td>-->
<!--		</tr>-->
<!--		<tr>-->
<!--			<td class="table_query_2Col_label_6Letter">其他说明：</td>-->
<!--			<td  colspan="3" align="left">${intentMap.INFO_REMARK}</td>-->
<!--		</tr>-->
<!--	</table>-->
		<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr >
			<th colspan="11" align="left" width="100%">&nbsp;商务支持申请
			</th>
		</tr>
		<tr align="center">
			<td width="10%" align="center">意向车系</td>
			<td width="10%" align="center">数量</td>
			<td width="10%" align="center">实际成交价</td>
			<td width="5%" align="center">启票价</td>
			<td width="10%" align="center">当前促销价</td>
<!--			<td width="10%" align="center">利润</td>-->
			<td width="10%" align="center">赠送、承诺</td>
			<td width="10%" align="center">市场开拓</td>
		
			<td width="10%" align="center">实际利润</td>
			<td width="10%" align="center">申请支持</td>
			<td width="15%" align="center">审批意见</td>
		</tr>
		<tbody id="tbody1">
			<c:forEach  items="${supportInfoList}" var="listMap">
					<tr align="center" class="table_list_row2">
						<td>${listMap.GROUP_NAME} </td>
						<td>${listMap.AMOUNT}<input type="hidden" value="${listMap.AMOUNT}" name="amount"/></td>
						<td>${listMap.REAL_PRICE} <input type="hidden" value="${listMap.REAL_PRICE}" name="realprice"/></td>
						<td>${listMap.PRICE}<input type="hidden" value="${listMap.PRICE}" name="price"/></td>
						<td>${listMap.DEPOT_PRO_PRICE}<input type="hidden" value="${listMap.DEPOT_PRO_PRICE}" name="depotproprice"/></td>
<!--						<td>${listMap.PROFIT} <input type="hidden" value="${listMap.PROFIT}" name="profit"/></td>-->
						<td>${listMap.GIVE_AND_ACCEPT} <input type="hidden" value="${listMap.GIVE_AND_ACCEPT}" name="gandaccept"/></td>
						<td>${listMap.MARKET_DEVELOP} <input type="hidden" value="${listMap.MARKET_DEVELOP}" name="marketdevelop"/></td>
						<td>${listMap.REAL_PROFIT} <input type="hidden" value="${listMap.REAL_PROFIT}" name="realprofit"/></td>
						<td>${listMap.REQUEST_SUPPORT}</td>
						<td>${listMap.AUDIT_MONEY}</td>
					</tr>
				</c:forEach>
		</tbody>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr >
	      <td align="left">备注：</td>
	      <td  align="left">${fleetMap.SUPPORT_REMARK}</td>
    	 </tr>
	</table>
<!--	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;意向附件</div>-->
<!--	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_info">-->
<!--		-->
<!--		<tr>-->
<!--			<td width="100%" colspan="1">附件名称</td>-->
<!--  		</tr>-->
<!--  		<table id="attachTab" class="table_info">-->
<!--  		<% if(attachList!=null&&attachList.size()!=0){ %>-->
<!--  		<c:forEach items="${attachList}" var="attls">-->
<!--		    <tr class="table_list_row1" id="${attls.FJID}">-->
<!--		    <td><a target="_blank" href="${attls.FILEURL}">${attls.FILENAME}</a></td>-->
<!--		    </tr>-->
<!--		</c:forEach>-->
<!--		<%} %>-->
<!--		</table>-->
<!--	</table>-->
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
					<td>${checkList.AUDIT_DATE}</td>
					<td>${checkList.USER_NAME}</td>
					<td>${checkList.AUDIT_REMARK}</td>
					<td>${checkList.CHECK_STATUS}</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr align="center">
			<td >
				<input class="cssbutton" name="button2" type="button" onclick="history.back();" value ="关闭" />
			</td>
		</tr>
	</table>
</form>
</body>
</html>