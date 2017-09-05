<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
		
	String fleetType = String.valueOf(request.getAttribute("fleetType"));
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>集团客户支持</title>
</head>
<body onload="showPactPart() ;">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;集团客户管理 > 集团客户支持 > 集团客户支持信息审核> 集团客户实销信息审核明细</div>
<form method="post" name = "fm" >
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;提报单位信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="4" align="left">&nbsp;单位信息</th>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">经销商代码：</td>
			<td><c:out value="${vhclInfoMap.DEALER_CODE}"/></td>
			<td class="table_query_2Col_label_6Letter">经销商名称：</td>
			<td><c:out value="${vhclInfoMap.DEALER_NAME}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">提报日期：</td>
			<td><c:out value="${vhclInfoMap.SALES_DATE}"/></td>
		</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;车辆信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="4" align="left">&nbsp;车辆资料</th>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">底盘号（VIN）：</td>
			<td align="left">${vhclInfoMap.VIN}</td>
			<td class="table_query_2Col_label_6Letter">发动机号：</td>
			<td align="left">${vhclInfoMap.ENGINE_NO}</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">车系：</td>
			<td align="left">${vhclInfoMap.SERIES_NAME}</td>
			<td class="table_query_2Col_label_6Letter">车型：</td>
			<td align="left">${vhclInfoMap.MODEL_NAME}</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">物料代码：</td>
			<td align="left">${vhclInfoMap.MATERIAL_CODE}</td>
			<td class="table_query_2Col_label_6Letter">物料名称：</td>
			<td align="left">${vhclInfoMap.MATERIAL_NAME}</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">颜色：</td>
			<td  colspan="3" align="left">${vhclInfoMap.COLOR_NAME}</td>
		</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;实销信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="4" align="left">&nbsp;实销记录</th>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">车主：</td>
			<td align="left">${vhclInfoMap.CTM_NAME}</td>
			<td class="table_query_2Col_label_6Letter">联系电话：</td>
			<td align="left"><c:if test="${vhclInfoMap.CTM_TYPE==begin}" >${vhclInfoMap.MAIN_PHONE}</c:if>
			<c:if test="${vhclInfoMap.CTM_TYPE==end}" >${vhclInfoMap.COMPANY_PHONE}</c:if>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">车牌号：</td>
			<td align="left">${vhclInfoMap.VEHICLE_NO}</td>
			<td class="table_query_2Col_label_6Letter">合同编号：</td>
			<td align="left">${vhclInfoMap.CONTRACT_NO}</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">开票日期：</td>
			<td align="left">${vhclInfoMap.INVOICE_DATE}</td>
			<td class="table_query_2Col_label_6Letter">发票编号：</td>
			<td align="left">${vhclInfoMap.INVOICE_NO}</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">保险公司：</td>
			<td align="left">${vhclInfoMap.INSURANCE_COMPANY}</td>
			<td class="table_query_2Col_label_6Letter">保险日期：</td>
			<td align="left">${vhclInfoMap.INSURANCE_DATE}</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">车辆交付日期：</td>
			<td align="left">${vhclInfoMap.CONSIGNATION_DATE}</td>
			<td class="table_query_2Col_label_6Letter">交付时公里数：</td>
			<td align="left">${vhclInfoMap.MILES}</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">付款方式：</td>
			<td align="left">${vhclInfoMap.PAYMENT}</td>
			<td class="table_query_2Col_label_6Letter">价格：</td>
			<td align="left">${vhclInfoMap.PRICE}</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">备注：</td>
			<td  colspan="3" align="left">${vhclInfoMap.MEMO}</td>
		</tr>
	</table>
	<%
		if(!fleetType.equals("-1")){
	%>
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
			<td class="table_query_2Col_label_4Letter" style="width:90px">是否批售项目：</td>
		    <td align="left"><script type='text/javascript'>
				writeItemValue('<c:out value="${fleetMap.IS_PACT}"/>');
			  </script>
			  <input type="hidden" name="isPact" id="isPact" value="${fleetMap.IS_PACT}" />
			  </td>
	      	<td class="table_query_2Col_label_4Letter" style="width:90px" id="pactPart">批售项目名称：</td>
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
	</table>
	
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;合同信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="4" align="left">&nbsp;合同信息</th>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">合同编号：</td>
			<td>
				<c:out value="${map3.CONTRACT_NO}"/>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">买方：</td>
			<td>
				<c:out value="${map3.BUY_FROM}"/>
			</td>
			<td class="table_query_2Col_label_6Letter">卖方:</td>
			<td>
				<c:out value="${map3.SELL_TO}"/>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">有效期起：</td>
			<td>
				<c:out value="${map3.START_DATE}"/>
			</td>
			<td class="table_query_2Col_label_6Letter">有效期止：</td>
			<td>
				<c:out value="${map3.END_DATE}"/>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">合同签订日期：</td>
			<td>
				<c:out value="${map3.CHECK_DATE}"/>
			</td>
			<td class="table_query_2Col_label_6Letter">特殊需求：</td>
			<td>
				<c:out value="${map3.OTHER_REMARK}"/>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">特殊金额(元)：</td>
			<td>
				<script type="text/javascript">
					document.write(amountFormatNew(<c:out value="${map3.OTHER_AMOUNT}"/>))
				</script>
			</td>
			<td class="table_query_2Col_label_6Letter">折让总额(元)：</td>
			<td>
				<script type="text/javascript">
					document.write(amountFormatNew(<c:out value="${map3.DIS_AMOUNT}"/>))
				</script>
			</td>
		</tr>
	</table>
	<%
		}else
		{
	%>
		<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;批售项目信息</div>
		<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<tr>
				<th colspan="4" align="left">&nbsp;批售项目</th>
			</tr>
			<tr>
				<td class="table_query_2Col_label_4Letter">项目代码：</td>
				<td>
					<c:out value="${fleetMap.PACT_NO}"/>
				</td>
				<td class="table_query_2Col_label_4Letter">项目名称：</td>
				<td>
					<c:out value="${fleetMap.PACT_NAME}"/>
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_4Letter">备注：</td>
				<td colspan="3">
					<c:out value="${fleetMap.REMART}"/>
				</td>
			</tr>
		</table>
	<%
		}
	%>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;集团客户实销信息审核</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<td class="table_query_2Col_label_6Letter">审核意见：</td>
			<td align="left">
				<textarea name="remark" id="remark" rows="4" cols="60" datatype="1,is_digit_letter_cn,300"></textarea>
			</td>
		</tr>
		<tr align="left">
			<td></td>
			<td>
				<input type="hidden" name="orderId" id="orderId" value="${orderId}"/>
				<input id="tongguo" class="cssbutton" name="button1" type="button" onclick="toSubmit('0')" value ="通过" />
				<input id="bohui" class="cssbutton" name="button3" type="button" onclick="toSubmit('1')" value ="驳回" />
				<input class="cssbutton" name="button2" type="button" onclick="toBack();" value ="返回" />
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	//审核校验
	function toSubmit(value){
		var b = new Object();
		var a = new Object();
		a = document.getElementById("bohui");
		b = document.getElementById("tongguo");
		b.disabled=true;
		a.disabled=true;
		if(confirm("确认提交？")){
			 	toConfirm(value);
			}else{
				b.disabled=false;
				a.disabled=false;
		}
	}
	//申请提交
	function toConfirm(value){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetSalesInfoCheck/fleetSalesInfoCheckConfirm.json?mysubmit='+value,showResult,'fm');
	}
	//返回
	function toBack(){
		///$('fm').action='<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetSalesInfoCheck/fleetSalesInfoCheckInit.do';
		//$('fm').submit();
		history.go(-1);
	}
	//回调方法
	function showResult(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("操作成功！");
			//$('fm').action='<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetSalesInfoCheck/fleetSalesInfoCheckInit.do';
			//$('fm').submit();
			history.go(-1);
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