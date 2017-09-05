<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
	Map<String, Object> vehicleInfo = (Map<String, Object>)request.getAttribute("vehicleInfo");
%>
<%@taglib uri="/jstl/cout" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>运单结算管理</title>
<script type="text/javascript">
	function formatMoney(dataStr){
		var str = formatCurrency(dataStr);
		document.write(str);
	}
	//生成运单
	function saveOld(){
		var url = "<%=contextPath%>/sales/storage/sendmanage/SendBillSettlement/checkOnWayAndVeicleIn.json";
		makeNomalFormCall(url,sureSave,'fm');
	}
	
	
	function sureSave(){
		 if(json.failrecord == 0){
			 MyConfirm("确认生成结算单！",settleSubmit);	
		  }else{
			 MyAlert(json.message);
		  }
		
		
	}
	
	function save(){
		 MyConfirm("确认生成结算单?",settleSubmit);	
	}
	
	function settleSubmit(json){
       document.getElementById("queren").disabled = true;
       document.getElementById("fanhui").disabled = true;
	   makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/SendBillSettlement/settleSubmit.json",settleBack,'fm'); 
	}
	function settleBack(json){
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功!结算单号："+json.returnBalNo);
		    fm.action = "<%=contextPath%>/sales/storage/sendmanage/SendBillSettlement/sendBillSettlementInit.do";
		    fm.submit();
        }else{
        	document.getElementById("queren").disabled = false;
            document.getElementById("fanhui").disabled = false;
        	parent.MyAlert("操作失败，请刷新后重试!");
        }
	}
	
	//验证数字
	function validateNumber(nm){
		try {
	    	if(isNaN(nm)){
	    		MyAlert("只能输入数字!");
				return false;
	    	}
	    	if(parseFloat(nm)<0){
	    		MyAlert("不能输入小于0的数字!");
				return false;
	 	    }
	    	return true;
		} catch (e) {
			MyAlert("只能输入数字!");
			return false;
		}
	}
	
	//计算结算金额
	function countSE(vehicleId){
		var yf = document.getElementById("yf"+vehicleId).value;
		var kk_amount = document.getElementById("kk_amount"+vehicleId).value;
		var other_amount = document.getElementById("other_amount"+vehicleId).value;
		var spC = document.getElementById("sp"+vehicleId);
		//验证数字
		if(!validateNumber(kk_amount)){
			document.getElementById("kk_amount"+vehicleId).value = 0;
			return;
		}
		if(!validateNumber(other_amount)) {
			document.getElementById("other_amount"+vehicleId).value = 0;
			return;
		}
		var endAmount = parseFloat(yf)-parseFloat(kk_amount)+parseFloat(other_amount);
		spC.innerHTML = formatCurrency(endAmount)+'<input type="hidden" name="settle_amount" id="settle_amount'+vehicleId+'" value="'+endAmount+'" />';
		countMoney();
	}
	
	function countMoney(){
		var kk_amount = document.getElementsByName("kk_amount");
		var other_amount = document.getElementsByName("other_amount");
		var settle_amount = document.getElementsByName("settle_amount");
		var ckkAmount = 0;
		var cotherAmount = 0;
		var csettleAmount = 0;
		if(kk_amount != null && kk_amount.length > 0) {
			for(var i = 0;i < kk_amount.length;i++) {
				ckkAmount += parseFloat(kk_amount[i].value);
				cotherAmount += parseFloat(other_amount[i].value);
				csettleAmount += parseFloat(settle_amount[i].value);
			}
			document.getElementById("countKK").innerHTML = formatCurrency(ckkAmount);
			document.getElementById("countOther").innerHTML = formatCurrency(cotherAmount);
			document.getElementById("countSE").innerHTML = formatCurrency(csettleAmount);
		}
	}
</script>
</head>
<body >
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
		&nbsp;当前位置：  整车物流管理&gt; 发运管理 &gt;运单结算管理</div>
	<form id="fm" name="fm" method="post"  enctype="multipart/form-data" >
		<input type="hidden" id="logiId" name="logiId" value="${logiId }"/>
		<input type="hidden" id="areaId" name="areaId" value="${areaId }"/>
		<input type="hidden" id="billIDs" name="billIDs" value="${billIDs }"/>
		<input type="hidden" id="vehicleIds" name="vehicleIds" value="${vehicleIds }"/>
		<table class="table_list" border="1">
			<tr align="left"><th colspan="5">&nbsp;车系汇总信息</th></tr>
			<tr>
				<th>序号</th>
				<th>车系代码</th>
				<th>车系名称</th>
				<th>数量</th>	
				<th>运费</th>
			</tr>	
				<c:forEach items="${series_list}" var="po">
					<tr align="center">
						<td>${po.ROW_NUM}</td>
						<td>${po.GROUP_CODE }</td>
						<td>${po.GROUP_NAME }</td>
						<td>${po.VEHICLE_NUM }</td>
						<td><script>
							formatMoney(${po.COUNT });
						</script></td>
					</tr>
				</c:forEach>
			<tr>
				<td colspan="3">&nbsp;</td>
				<td><strong>合计:</strong></td>
				<td>${settle_amount_1 }</td>
			</tr>
		</table>
		<br />
		<table class="table_list" border="1">
			<tr align="left"><th colspan="18">&nbsp;车辆明细信息</th></tr>
				    <tr>
				    	<th>序号</th>
						<th>组板号</th>
						<th>发运时间</th>
						<th>车系</th>
					<!-- 	<th>物料名称</th>	 -->
						<th>经销商</th>
						<th>VIN</th>
						<th>交接单号</th>
						<th>发动机号</th>
						<th>车厂出库日期</th>
						<th>验收入库日期</th>
						<th>发运里程</th>
						<th>里程单价</th>
						<th>运费</th>
						<th>超期时间</th>
						<th>扣款金额</th>
						<th>其他费用</th>
						<th>备注</th>
						<th>结算金额</th>
				    </tr>	
				    <c:forEach items="${vehicle_detail_list}" var="po">
						<tr align="center">
							
							<td>${po.ROW_NUM }</td>
							<td>${po.BO_NO }</td>
							<td>${po.BILL_CRT_DATE }</td>
							<td>${po.GROUP_NAME }</td>
						<!-- 	<td>${po.MATERIAL_NAME }</td>  -->
							<td>${po.DEALER_NAME }</td>
							<td>${po.VIN }</td>
							<td>${po.PASS_NO }</td>
							<td>${po.ENGINE_NO }</td>
							<td>${po.OUT_DATE }</td>
							<td>${po.STORAGE_DATE }</td>
							<td>${po.DISTANCE }</td>
							<td><script>
							formatMoney(${po.AMOUNT});
							</script></td>				
							<td><input type="hidden" id="yf${po.VEHICLE_ID }" name="yf" value="${po.SETTLE_AMOUNT }" /><script>
							formatMoney(${po.SETTLE_AMOUNT });
							</script>
							</td>
							<td>${po.CQ_HOURSE }</td>
							<td><input type="text" name="kk_amount" id="kk_amount${po.VEHICLE_ID }" class="short_txt" value="0" onblur="countSE(${po.VEHICLE_ID });"/></td>
							<td><input type="text" name="other_amount" id="other_amount${po.VEHICLE_ID }" class="short_txt" value="0" onblur="countSE(${po.VEHICLE_ID });"/></td>
							<td><input type="text" name="remark" id="remark${po.VEHICLE_ID }" class="short_txt"></td>
							<td><span id="sp${po.VEHICLE_ID }"><script>formatMoney(${po.SETTLE_AMOUNT });</script><input type="hidden" name="settle_amount" id="settle_amount${po.VEHICLE_ID }" value="${po.SETTLE_AMOUNT }" /></span></td>
							<input type="hidden" id="areaAndAmount" name="areaAndAmount" value="${po.AREA_ID }-${po.SETTLE_AMOUNT}"/>
							<input type="hidden" id="vehicleId${po.VEHICLE_ID }" name="vehicleId" value="${po.VEHICLE_ID }" />
						</tr>
					</c:forEach>
					<tr>
						<td colspan="11">&nbsp;</td>
						<td><strong>合计:</strong></td>
						<td>${settle_amount }</td>
						<td>&nbsp;</td>
						<td><span id="countKK">0</span></td>
						<td><span id="countOther">0</span></td>
						<td>&nbsp;</td>
						<td><span id="countSE">${settle_amount }</span></td>
					</tr>
		</table>
		<table class="table_query" width="90%" border="0" align="center">
			<tr>
				<td align="center" >
					<input id="queren" name="button" type="button" class="normal_btn" onclick="save();" value="结算" />
					<input id="fanhui" name="button" type="button" class="normal_btn" onclick="window.history.back()" value="返回" />
				</td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>