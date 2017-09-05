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
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;集团客户支持 > 集团客户支持申请> 集团客户支持合同维护</div>
<form method="post" name = "fm" id="fm" >
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
			<th colspan="7" align="left">&nbsp;意向车型
			</th>
		</tr>
		<tr align="center">
			<td width="20%" align="center">意向车型</td>
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
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;合同信息 </div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr align="center">
			<td>合同编号</td>
			<td>合同数量</td>
			<td>支持点位</td>
			<td>签订日期</td>
			<td>有效期起</td>
			<td>有效期止</td>
		</tr>
		<tr align="center" class="table_list_row2">
			<td><input type="text" name="contractNo" id="contractNo" size="30" maxlength="15"/></td>
			<td><input type="text" name="contractAmount" id="contractAmount" size="6" maxlength="6" datatype="1,is_digit,6"/></td>
			<td><input type="text" name="discount" id="discount" size="3" maxlength="10" datatype="1,is_double,10"/>%</td>
			<td>
				<input class="short_txt"  type="text" id="checkDate" name="checkDate" datatype="1,is_date,10" group="checkDate,startDate"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'checkDate', false);" value="&nbsp;"/>
			</td>
			<td>
				<input class="short_txt"  type="text" id="startDate" name="startDate" datatype="1,is_date,10" group="startDate,endDate"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value="&nbsp;"/>
			</td>
			<td>
				<input class="short_txt"  type="text" id="endDate" name="endDate" datatype="1,is_date,10" group="startDate,endDate"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;"/>
			</td>
		</tr>
		<tr align="center">
			<td>
				<input type="hidden" name="dlrCompanyId" id="dlrCompanyId" value="${fleetMap.DLR_COMPANY_ID}"/>
				<input type="hidden" name="fleetId" id="fleetId" value="${fleetMap.FLEET_ID}"/>
				<input type="hidden" name="intentId" id="intentId" value="${intentId}"/>
				<input class="cssbutton" name="button1" type="button" onclick="toSave();" value ="保存" />
				<input class="cssbutton" name="button2" type="button" onclick="history.back();" value ="返回" />
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	function doInit(){
		loadcalendar();  //初始化时间控件
	}
	//审核校验
	function toSave(){
		var contractNo = document.getElementById("contractNo").value;
		var contractAmount = document.getElementById("contractAmount").value;
		var discount = document.getElementById("discount").value;
		var checkDate = document.getElementById("checkDate").value;
		var startDate = document.getElementById("startDate").value;
		var endDate = document.getElementById("endDate").value;
		if(!contractNo&&contractNo!=null){
			MyAlert("请填写合同编号！");
			$('fm').contractNo.focus();
			return false;
		}
		if(!contractAmount&&contractAmount!=null){
			MyAlert("请填写合同数量！");
			$('fm').contractAmount.focus();
			return false;
		}
		if(!discount&&discount!=null){
			MyAlert("请填写支持点位！");
			$('fm').discount.focus();
			return false;
		}
		if(!checkDate&&checkDate!=null){
			MyAlert("请填写签订日期！");
			$('fm').checkDate.focus();
			return false;
		}
		if(!startDate&&startDate!=null){
			MyAlert("请填写有效期起！");
			$('fm').startDate.focus();
			return false;
		}
		if(!endDate&&endDate!=null){
			MyAlert("请填写有效期止！");
			$('fm').endDate.focus();
			return false;
		}
		submitForm(fm);
		MyConfirm("确认提交？",toConfirm);
	}
	//申请提交
	function toConfirm(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetContractsMaintain/fleetContractsMaintainConfirm.json',showResult,'fm');
	}
	//返回
	function toBack(){
		$('fm').action='<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetContractsMaintain/fleetContractsMaintainInit.do';
		$('fm').submit();
	}
	//回调方法
	function showResult(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("操作成功！");
			$('fm').action='<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetContractsMaintain/fleetContractsMaintainInit.do';
			$('fm').submit();
		}else if(json.returnValue == '2'){
			MyAlert("经销商端端已维护合同！操作失败！");
		}else{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
</script>
</body>
</html>