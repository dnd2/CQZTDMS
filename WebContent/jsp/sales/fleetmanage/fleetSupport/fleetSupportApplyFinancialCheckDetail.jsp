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
<title>集团客户支持</title>
</head>
<body onload="showPactPart() ;">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;集团客户支持 > 集团客户支持申请> 集团客户支持申请财务复核</div>
<form method="post" name = "fm" >
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
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;购车意向</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="4" align="left">&nbsp;意向信息</th>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">预计采购日期：</td>
			<td align="left">${intentMap.PURCHASE_DATE}至${intentMap.PUR_END_DATE}</td>
			<td class="table_query_2Col_label_6Letter">信息来源：</td>
			<td align="left">${intentMap.INFO_GIVING_MAN}</td>
		</tr>
		<tr>
			<td class="table_query_3Col_label_6Letter">竞争情况说明：</td>
			<td  colspan="3" align="left">${intentMap.COMPETE_REMARK}</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">其他说明：</td>
			<td  colspan="3" align="left">${intentMap.INFO_REMARK}</td>
		</tr>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="7" align="left">&nbsp;意向车系
			</th>
		</tr>
		<tr align="center">
			<td width="20%" align="center">意向车系</td>	
			<td width="5%" align="center">数量</td>
			<td width="10%" align="center">支持点位</td>
			<td width="30%" align="center">备注</td>
		</tr>
		<tbody id="tbody1">
			<c:if test="${intentId!=null}">
				<c:forEach items="${intentList}" var="list">
					<tr align="center" class="table_list_row2">
						<td>${list.GROUP_NAME}</td>	
						<td>${list.AMOUNT}</td>
						<td>${list.DISCOUNT}%</td>
						<td>${list.REMARK}</td>
					</tr>
				</c:forEach>
			</c:if>
		</tbody>
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
					<td>${checkList.AUDIT_DATE}</td>
					<td>${checkList.USER_NAME}</td>
					<td>${checkList.AUDIT_REMARK}</td>
					<td>${checkList.CHECK_STATUS}</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;审核意见</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<td class="table_query_2Col_label_6Letter">审核意见：</td>
			<td align="left">
				<textarea name="remark" id="remark" rows="4" cols="60" datatype="1,is_textarea,300"></textarea>
			</td>
		</tr>
		<tr align="left">
			<td></td>
			<td>
				<input type="hidden" name="groupIds" id="Ids"/>
				<input type="hidden" name="remarks" id="remarks"/>
				<input type="hidden" name="originalPrices" id="originalPrices"/>
				<input type="hidden" name="discounts" id="discounts"/>
				<input type="hidden" name="discountPrices" id="discountPrices"/>
				<input type="hidden" name="amounts" id="amounts"/>
				<input type="hidden" name="fleetId" id="fleetId" value="${fleetMap.FLEET_ID}"/>
				<input type="hidden" name="intentId" id="intentId" value="${intentId}"/>
				<input type="hidden" name="flag" id="flag"/>
				<input class="cssbutton" name="button1" type="button" onclick="toSubmit('0');" value ="通过" />
				<input class="cssbutton" name="button3" type="button" onclick="toSubmit('1');" value ="驳回" />
				<input class="cssbutton" name="button2" type="button" onclick="toBack();" value ="返回" />
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	//审核校验
	function toSubmit(value){
		document.getElementById("flag").value=value;
		MyConfirm("确认提交？",toConfirm);
	}
	//申请提交
	function toConfirm(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetSupportFinancialCheck/fleetSupportFinancialCheckConfirm.json',showResult,'fm');
	}
	//返回
	function toBack(){
		$('fm').action='<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetSupportFinancialCheck/fleetSupportFinancialCheckInit.do';
		$('fm').submit();
	}
	//回调方法
	function showResult(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("操作成功！");
			$('fm').action='<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetSupportFinancialCheck/fleetSupportFinancialCheckInit.do';
			$('fm').submit();
		}else{
			MyAlert("操作失败！请联系系统管理员！");
		}
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