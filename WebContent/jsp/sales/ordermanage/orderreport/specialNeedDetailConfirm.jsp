<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	Boolean boo =  (Boolean)request.getAttribute("boo");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单提报</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订做车需求查询 > 订做车需求明细</div>
<form method="POST" name="fm" id="fm">
	<table width="100%" class=table_query>
		<tr class=cssTable >
			<td align="right" width="25%">业务范围：</td>
			<td align="left" width="25%">
				<select name="areaId" id="areaId" class="short_sel" disabled="disabled">
					<c:forEach items="${areaList}" var="po">
						<option <c:if test="${po.AREA_ID==areaId}">selected</c:if> value="${po.AREA_ID}|${po.DEALER_ID}">${po.AREA_NAME}</option>
					</c:forEach>
				</select>
			</td>
			<td align="right" width="20%">价格列表：</td>
			<td align="left" width="30%">
				<c:out value="${priceName}"/>
				&nbsp;
			</td>
		</tr>
	</table>
	<table class=table_list style="border-bottom:1px solid #DAE0EE" >  
		<tr class=cssTable >
			<th nowrap="nowrap">车系</th>
			<th nowrap="nowrap">车型编号</th>
			<th nowrap="nowrap">配置编号</th>
			<th nowrap="nowrap">配置名称</th>
			<th nowrap="nowrap">需求数量</th>
			<th nowrap="nowrap">价格变动</th>
			<th nowrap="nowrap">预计交付周期</th>
			<th nowrap="nowrap">订做批次号</th> 
			<%
				if(!boo){
			%>
			<th nowrap="nowrap">价格</th>
			<th nowrap="nowrap">审核后价格</th>
			<%}%>
			
		</tr>
    	<c:forEach items="${list}" var="po">
    		<tr class="table_list_row2">
		      <td align="center">${po.SERIES_NAME}</td>
		      <td align="center">${po.MODEL_CODE}</td>
		      <td align="center">${po.GROUP_CODE}</td>
		      <td align="center">${po.GROUP_NAME}</td>
		      <td align="center">${po.AMOUNT}</td>
		      <td align="center">${po.CHANGE_PRICE}</td>
		      <td align="center">${po.EXPECTED_PERIOD}天</td>
		      <td align="center">${po.BATCH_NO}</td>
		      <%
				if(!boo){
			  %>
		      <td align="center">${po.SALES_PRICE}</td>
		      <td align="center">${po.SP_PRICE}</td>
		      <%} %>
		    </tr>
    	</c:forEach>
	</table>	
	<br>
	<table class=table_query>
		<tr>
			<td align="right" id="a1">资金类型：</td>
			<td align="left" id="a2">
				<select name="typeId" id="typeId" class="short_sel" onchange="showAccountAmount();" disabled="disabled">
					<c:if test="${accTypeId != null && accTypeId != 0}">
						<c:forEach items="${accList}" var="acclist">
							<c:choose>   
			    				<c:when test="${acclist.TYPE_ID == reqPO.accountTypeId}" >   
									<option value="<c:out value="${acclist.TYPE_ID}"/>" selected="selected"><c:out value="${acclist.TYPE_NAME}"/></option>
								</c:when>
								<c:otherwise>  
									<option value="<c:out value="${acclist.TYPE_ID}"/>"><c:out value="${acclist.TYPE_NAME}"/></option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:if>
					<input type="hidden" id="accTypeId" name="accTypeId" value="${accTypeId}" />
				</select>
			</td>
			  <%
				if(!boo){
			  %>
			<td align="right" id="a3">
				可用余额：
			</td>
			<td align="left" id="accountAmount"></td>
			 <%} %>
			<td align="left" id="allAccount">
			<input type="hidden" name="accountId" id="accountId" value=""/>
			    <input type="hidden" name="availableAmount" id="availableAmount" value=""/>
				<input type="hidden" name="freezeAmount" id="freezeAmount" value=""/>
			</td>
			<td align="right">预付款金额：</td>
			<td align="left">
				<script>
					document.write(amountFormat(${reqPO.preAmount}));
				</script>
			</td>
		</tr>
		<tr class=cssTable>
			<td width="7%" align="right">集团客户：</td>
			<td width="50%" colspan="6" align="left">${fleetName}</td>
		</tr>
		<tr class=cssTable>
			<td width="7%" align="right">改装说明：</td>
			<td width="50%" colspan="6" align="left"><c:out value="${remark}"/><input type="hidden" name="remark" value="${remark}"/></td>
		</tr>
	</table>
	<br>
	<table class="table_list" style="border-bottom:1px solid #DAE0EE" >
		<tr class="table_list_row1">
			<th  align="center" nowrap="nowrap" >日期</th>
			<th align="center" nowrap="nowrap"  >单位</th>
			<th align="center" nowrap="nowrap"  >操作人</th>
			<th align="center" nowrap="nowrap"  >审核结果</th>
			<th align="center" nowrap="nowrap"  >审核描述</th>
		</tr>
		<c:forEach items="${checkList}" var="po">
			<tr class="table_list_row1">
				<td align="center" nowrap="nowrap" class="table_list_row1" >${po.CHECK_DATE}</td>
				<td align="center" nowrap="nowrap" class="table_list_row1"  >${po.ORG_NAME}</td>
				<td align="center" nowrap="nowrap" class="table_list_row1"  >${po.NAME}</td>
				<td align="center" nowrap="nowrap" class="table_list_row1"  >${po.CHECK_STATUS}</td>
				<td align="center" nowrap="nowrap"  >${po.CHECK_DESC}</td>
			</tr>
		</c:forEach>
	</table>
	<br>
	<table class=table_query>
		<tr class=cssTable >
			<td>&nbsp;</td>
			<td colspan="3" align="left">
				<input type="hidden" name="reqId" value="${reqId}"/>
				<input type="hidden" name="areaId" value="${areaId}"/>
				<input type="hidden" name="dealerId" value="${parentDlrId}"/>
				<input type="button" name="button4" class="cssbutton" onclick="requirementConfirm()" value="需求确认" id="queryBtn4" /> 
				<!--<input type="button" name="button1" class="long_btn" onclick="toReport();" value="生成订做车订单" id="queryBtn1" /> 
				--><input type="button" name="button2" class="cssbutton" onclick="toConfirm();" value="放弃订购" id="queryBtn2" /> 
				<input type="button" name="button3" class="cssbutton" onclick="history.back();" value="返回" id="queryBtn3" /> 
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	//初始化
	function doInit(){
		showAccountAmount();
		changeAllAccount();
		selectAction();
	}
	
	//资金类型与余额联动
	function showAccountAmount(){
		var typeId = document.getElementById("typeId").value;
		<c:forEach items="${accList}" var="list">
	     var id=<c:out value="${list.TYPE_ID}"/>
	     var accountId=<c:out value="${list.ACCOUNT_ID}"/>
	     if(typeId==id){
	    	 var availableAmount=<c:out value="${list.AVAILABLE_AMOUNT}"/>
	    	 var freezeAmount=<c:out value="${list.FREEZE_AMOUNT}"/>
	    	 if(document.getElementById("accountAmount")){
	    	 	document.getElementById("accountAmount").innerText= amountFormat(availableAmount);
	    	 }
	    	 document.getElementById("accountId").value = accountId;
	    	 document.getElementById("availableAmount").value = availableAmount;
	    	 document.getElementById("freezeAmount").value = freezeAmount;
	     }
		</c:forEach>
	}
	
	//需求确认
	function requirementConfirm(){
		var typeId = document.getElementById("typeId").value;
		var accTypeId = document.getElementById("accTypeId").value;
		if(accTypeId != 0 && accTypeId != null && !typeId){
			MyAlert("无资金类型，不能进行此操作!");
			return;
		}
		
		if(("-" + <%= Constant.STATUS_DISABLE%>) != "${parentErpCode}") {
			if(document.getElementById("accountAmount")){
				var avaAmount = document.getElementById("availableAmount").value;
				var preAmount = '${reqPO.preAmount}';
				if(parseFloat(avaAmount, 10) < parseFloat(preAmount, 10)){
					MyAlert("预付款金额不能大于可用余额!");
					return;
				}
			} else {
				var avaAmount = document.getElementById("availableAmount").value;
				var preAmount = '${reqPO.preAmount}';
				if(parseFloat(avaAmount, 10) < parseFloat(preAmount, 10)){
					MyAlert("预付款金额已大于可用余额,请确认资金后操作!");
					return;
				}
			}
		}
		MyConfirm("是否提交需求确认?",requirementAction);
	}
	//需求确认提交
	function requirementAction(){
		$('fm').action='<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedConfirm/requirementConfirm.do';
		$('fm').submit();
	}
	//提交校验
	function toConfirm(){
		MyConfirm("确认放弃订购？",toSubmit);
	}
	//提交
	function toSubmit(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedConfirm/specialNeedDetailConfirm.json',showResult,'fm');
	}
	//生成订单链接
	function toReport(){
		$('fm').action='<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedConfirm/specialNeedToReportInit.do';
		$('fm').submit();
	}
	//回调方法
	function showResult(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("提交成功！");
			$('fm').action='<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedConfirm/specialNeedConfirmInit.do';
			$('fm').submit();
		}else{
			MyAlert("新增失败！请联系系统管理员！");
		}
	}
</script>
</body>
</html>
