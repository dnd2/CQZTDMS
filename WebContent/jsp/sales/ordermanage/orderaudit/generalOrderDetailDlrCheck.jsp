<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单审核</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;销售订单管理 &gt; 订单审核&gt; 常规订单审核</div>
<form method="post" name="fm" id="fm">
	<table class="table_list">
		<tr align="center" class="tabletitle">
			<th>周度<input type="hidden" name="areaId" id="areaId" value="<c:out value="${areaId}"/>"/>
					<input type="hidden" name="area" id="area" value="<c:out value="${area}"/>"/>
					</th>
			<th>配置代码</th>
			<th>配置名称</th>
			<th>生产计划数量</th>
			<th>可用生产计划</th>
			<th>提报数量</th>
			<th>已审核数量</th>
			<th>当前库存</th>
		</tr>
		<c:forEach items="${list1}" var="list1">
			<tr align="center" class="table_list_row2">
				<td><c:out value="${list1.ORDER_YEAR}"/>.<c:out value="${list1.ORDER_WEEK}"/>
					<input type="hidden" name="orderYear" id="orderYear" value="<c:out value="${list1.ORDER_YEAR}"/>"/>
					<input type="hidden" name="orderWeek" id="orderWeek" value="<c:out value="${list1.ORDER_WEEK}"/>"/>
				</td>
				<td><c:out value="${list1.GROUP_CODE}"/><input type="hidden" name="groupId" id="groupId" value="<c:out value="${list1.GROUP_ID}"/>"/></td>
				<td><c:out value="${list1.GROUP_NAME}"/></td>
				<td><c:out value="${list1.PLAN_AMOUNT}"/></td>
				<td><c:out value="${list1.AMOUNT}"/><input type="hidden" name="amount" id="amount" value="<c:out value="${list1.AMOUNT}"/>"></input></td>
				<td><c:out value="${list1.ORDER_AMOUNT}"/></td>
				<td><span id="totailAmount"><c:out value="${list1.CHECK_AMOUNT}"/></span></td>
				<td><c:out value="${list1.STOCK_AMOUNT}"/></td>
			</tr>
		</c:forEach> 
	</table>
	<br>
	<table class="table_list">
		<tr align="center" class="tabletitle">
			<th>经销商代码</th>
			<th>经销商名称</th>
			<th>配置代码</th>
			<th>配置名称</th>
			<th>配额数量</th>
			<th>最小提报量</th>
			<th>提报数量</th>
			<th>已审核数量</th>
		</tr>
		<c:forEach items="${list2}" var="list2">
			<tr align="center" class="table_list_row2">
				<td><c:out value="${list2.DEALER_CODE}"/><input type="hidden" name="dealerId" id="dealerId" value="<c:out value="${list2.DEALER_ID}"/>"/></td>
				<td><c:out value="${list2.DEALER_NAME}"/></td>
				<td><c:out value="${list2.GROUP_CODE}"/></td>
				<td><c:out value="${list2.GROUP_NAME}"/></td>
				<td><c:out value="${list2.QUOTA_AMT}"/></td>
				<td><c:out value="${list2.MIN_AMOUNT}"/></td>
				<td><c:out value="${list2.ORDER_AMOUNT}"/></td>
				<td><SPAN id="detailAmount"><c:out value="${list2.CHECK_AMOUNT}"/></SPAN></td>
			</tr>
		</c:forEach> 
	</table>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
	<table class="table_list">
		<tr class="table_list_row2">
			<td align="left">
				<input type="hidden" name="detailIds" value="detailIds"/>
				<input type="hidden" name="vers" value="vers"/>
				<input type="hidden" name="checkAmounts" value="checkAmounts"/>
				<input type="hidden" name="amounts" value="amounts"/>
				<input type="hidden" name="oldAmounts" value="oldAmounts"/>
				<input type="hidden" name="materialIds" value="materialIds"/>
				<input type="button" name="button1" class="cssbutton" onclick="toSaveCheck();" value="保存"/>
				<input type="button" name="button1" class="cssbutton" onclick="toBack();" value="返回"/>
			</td>
		</tr>
	</table>
<!--分页 end --> 
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/ordermanage/orderaudit/GeneralOrderCheck/generalOrderDetailDlrQuery.json";
	var title = null;
	var columns = [
				{header: "订单号码",dataIndex: 'ORDER_NO',align:'center'},
				{header: "物料代码",dataIndex: 'MATERIAL_CODE',align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "颜色", dataIndex: 'COLOR_NAME', align:'center'},
				{header: "提报数量", dataIndex: 'ORDER_AMOUNT', align:'center'},
				{header: "审核数量", dataIndex: 'CHECK_AMOUNT', align:'center',renderer:myInput}
		      ];
	//超链接设置
	function myInput(value,meta,record){
		var data = record.data;
  		return String.format("<input type='text' name='checkAmount' value='"+data.ORDER_AMOUNT+"' size='4'/><input type='hidden' name='oldAmount' value='"+value+"'/><input type='hidden' name='orderAmount' value='"+data.ORDER_AMOUNT+"'/><input type='hidden' name='detailId' value='"+data.DETAIL_ID+"'/><input type='hidden' name='materialId' value='"+data.MATERIAL_ID+"'/><input type='hidden' name='ver' value='"+data.VER+"'/>");
	}
	//保存时校验
	function toSaveCheck(){
		var cnt = 0;
		var amounts=0;
		var checkAmounts = '';
		var orderAmounts = '';
		var detailIds ='';
		var vers ='';
		var oldAmounts ='';
		var materialIds ='';
		var amount = document.getElementById("amount").value;
		var checkAmount = document.getElementsByName("checkAmount");
		var oldAmount = document.getElementsByName("oldAmount");
		var materialId = document.getElementsByName("materialId");
		var orderAmount = document.getElementsByName("orderAmount");
		var detailId = document.getElementsByName("detailId");
		var ver = document.getElementsByName("ver");
		for(var i=0 ;i< detailId.length; i++){
			cnt = cnt+Number(checkAmount[i].value);
			if(Number(checkAmount[i].value) > Number(orderAmount[i].value)){
				MyDivAlert("审核数量不能大于提报数量！");
				return false;
			}
			amounts = Number(checkAmount[i].value)+amounts;
			checkAmounts = checkAmount[i].value + ',' + checkAmounts;
			detailIds = detailId[i].value + ',' + detailIds;
			vers = ver[i].value + ',' + vers;
			materialIds = materialId[i].value + ',' + materialIds;
			oldAmounts = orderAmount[i].value + ',' + oldAmounts;
		}
		if(cnt==0){
			MyDivAlert("审核数量不能全部为零！");
            return;
		}
		if(cnt>Number(amount)){
			//MyDivAlert("审核数量大于可用资源数量，请重新输入！");
            ///return;
		}
		document.getElementById("amounts").value=amounts;
		document.getElementById("oldAmounts").value=oldAmounts;
		document.getElementById("detailIds").value=detailIds;
		document.getElementById("vers").value=vers;
		document.getElementById("checkAmounts").value=checkAmounts;
		document.getElementById("materialIds").value=materialIds;
		MyDivConfirm("确认保存？",putForword);
	}
	//审核保存提交
	function putForword(){
		makeNomalFormCall("<%=contextPath%>/sales/ordermanage/orderaudit/GeneralOrderCheck/generalOrderCheck.json",showForwordValue,'fm','queryBtn');
	}
	//回调函数
	function showForwordValue(json){
		if(json.returnValue == '1'){
			var rowObj = parent.$('inIframe').contentWindow.rowObj;
			var rowIndex = parent.$('inIframe').contentWindow.rowObjNum;
			var totailAmount = document.getElementById("totailAmount").innerText;
			var detailAmount = document.getElementById("detailAmount").innerText;
			var amounts = document.getElementById("amounts").value;
			rowObj.cells[7].childNodes[0].innerText= amounts;
			parent.$('inIframe').contentWindow.checkAmount.innerText = Number(totailAmount)-Number(detailAmount)+Number(amounts);
			parent._hide();
			MyAlert("保存成功！");
		}else if(json.returnValue == '2'){
			parent._hide();
			MyAlert("数据已被修改，操作失败！");
		}else{
			MyDivAlert("保存失败！请联系系统管理员！");
		}
	}
	function toBack(){
		parent._hide();
	}
	function doInit(){
		__extQuery__(1);
	}
</script>
<!--页面列表 end -->
</body>
</html>