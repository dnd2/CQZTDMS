<%@ page language="java" import="com.infodms.dms.common.Constant" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>Insert title here</title>
</head>
<body>
<form method="post" name="fm" id="fm">
	<c:set var="pageSize"  value="1" />
	<c:forEach items="${pf}" var="pf" varStatus="status">
		<c:forEach begin="1" end="${pf.BALANCE_QUANTITY}">
		<table class="tabprint" cellpadding="1" cellspacing="1" border="0"  style='${(status.count%pageSize==0) ? "page-break-after:always;":""}' >
			<tr align="center">
				<th colspan="2">
					<script type="text/javascript">
						document.write(getItemValue('<%=Constant.print_code%>'));
					</script>
				</th>
			</tr>
			<tr>
				<td class="tdp" nowrap>三包单号</td>
				<td class="tdp"><c:out value="${pf.CLAIM_NO}"/></td>
			</tr>
			<tr>
				<td class="tdp" nowrap>VIN码</td>
				<td class="tdp"><c:out value="${pf.VIN}"/></td>
			</tr>
			<tr>
				<td class="tdp" nowrap>生产厂家</td>
				<td class="tdp">
					<script type="text/javascript">
						if(${pf.YIELDLY}=='<%=Constant.SERVICEACTIVITY_CAR_YIELDLY_01%>'){
							document.write("重庆长安")
						}else{
							writeItemValue('<c:out value="${pf.YIELDLY}"/>')
						}
					</script>
				</td>
			</tr>
			<tr>
				<td class="tdp" nowrap>车型</td>
				<td class="tdp"><c:out value="${pf.MODEL_CODE}"/></td>
			</tr>
			<tr>
				<td class="tdp" nowrap>发动机号</td>
				<td class="tdp"><c:out value="${pf.ENGINE_NO}"/></td>
			</tr>
			<tr>
				<td class="tdp" nowrap>行驶里程</td>
				<td class="tdp"><c:out value="${pf.IN_MILEAGE}"/></td>
			</tr>
			<tr>
				<td class="tdp" nowrap>购车日期</td>
				<td class="tdp"><c:out value="${pf.PURCHASED_DATE}"/></td>
			</tr>
			<tr>
				<td class="tdp" nowrap>审核通过日期</td>
				<td class="tdp"><c:out value="${pf.AUDITING_DATE}"/></td>
			</tr>
			<tr>
				<td class="tdp" nowrap>服务商代码</td>
				<td class="tdp"><c:out value="${pf.DEALER_CODE}"/></td>
			</tr>
			<tr>
				<td class="tdp" nowrap>服务商名称</td>
				<td class="tdp"><c:out value="${pf.DEALER_NAME}"/></td>
			</tr>
			<tr>
				<td class="tdp" nowrap>零件名称</td>
				<td class="tdp"><c:out value="${pf.DOWN_PART_NAME}"/></td>
			</tr>
			<tr>
				<td class="tdp" nowrap>零件编码</td>
				<td class="tdp"><c:out value="${pf.DOWN_PART_CODE}"/></td>
			</tr>
				
			<tr>
				<td class="tdp" nowrap>配套厂家</td>
				<td class="tdp"><c:out value="${pf.DC_NAME}"/></td>
			</tr>
			<%int codeId = (Integer)request.getAttribute("codeId");%>
			<%if(codeId==Constant.chana_jc){ %>
				<tr>
					<td class="tdp" nowrap>顾客问题</td>
					<td class="tdp"><c:out value="${pf.TROUBLE_TYPE}"/></td>
				</tr>
			<%}else{ %>
			
			<tr>
				<td class="tdp" nowrap>故障</td>
				<td class="tdp"><c:out value="${pf.REMARK}"/></td>
			</tr>
			<%} %>
			<tr>
				<td class="tdp" nowrap>责任性质</td>
				<td class="tdp">
				<script type="">
				document.write(getItemValue(${pf.RESPONSIBILITY_TYPE}));
				</script>
				</td>
				
			</tr>
			<tr>
				<td class="tdp" nowrap>客户姓名</td>
				<td class="tdp"><c:out value="${pf.DELIVERER}"/></td>
			</tr>
			<tr>
				<td class="tdp" nowrap>电话</td>
				<td class="tdp"><c:out value="${pf.DELIVERER_PHONE}"/></td>
			</tr>
			<%if(codeId==Constant.chana_wc){ %>
			<tr>
				<td class="tdp" nowrap>是否回收</td>
				<td class="tdp">
					<c:if test="${pf.IS_RETURN ==1}">
						是
					</c:if>
					<c:if test="${pf.IS_RETURN ==0}">
						否
					</c:if>
				</td>
			</tr>
			<%} %>
		</table>	
		<br/>
		</c:forEach>
    </c:forEach>
    <object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="WebBrowser" width="0"></object>
	<div id="kpr" align="left">
		<input type="button" value="打印" class="normal_btn" onclick="kpr.style.display='none';document.all.WebBrowser.ExecWB(6,1)"/>
		<input type="button" value="打印设置" class="normal_btn" onclick="document.all.WebBrowser.ExecWB(8,1)"/>
		<input type="button" value="打印预览" class="normal_btn" onclick="kpr.style.display='none';document.all.WebBrowser.ExecWB(7,1)"/>
		<input type="button" value="导出到Excel" class="long_btn" onclick="exportToExcel()"/>
		<input type="button" value="关闭" class="normal_btn" onclick="self.close()"/>
		<input type="hidden" id="claimId" value="<c:out value="${claimId}"/>"/>
	</div>
</form>
<script type="text/javascript">
	function exportToExcel() {
		var orderId = document.getElementById("claimId").value;
		var url = '<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillTrack/toExcel.do?claimId=' + orderId;
		window.location = url;
	}
</script>
</body>
</html>
