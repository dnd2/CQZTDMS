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
<title>可用金额明细</title>
<script type="text/javascript">
	function doInit(){
		__extQuery__(1);
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 财务管理 &gt; 可用金额查询 &gt;可用金额明细</div>
<form method="post" name = "fm" >
    <table width=100% border="0" align="center"  class="table_list">
	    <tr>
	    	<th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 可用金额明细</th>
	    </tr>
    	<tr>
			<td  colspan="4"><font color="red"><strong>兵财融资可用金额计算：兵财融资账户金额 - （轿车所有基地兵财融资冻结金额 + 微车所有基地兵财融资冻结金额）</strong></font></td>
		</tr>
	    <tr align="center">
			<th  align="center">资金类型</th>
			<th  align="center">账面余额</th>
			<th  align="center">预扣款</th>
			<th  align="center">可用金额</th>
		</tr>
		<c:forEach var="list1" items="${myList1}">
		<tr class="table_list_row1">
			<td align="center"><lable>${list1.TYPE_NAME}</lable></td>
			<td>
				<script>document.write(amountFormat(${list1.BALANCE_AMOUNT}));</script>
			</td>
			<td><script>document.write(amountFormat(${list1.FREEZE_AMOUNT}));</script></td>
			<td><script>document.write(amountFormat(${list1.AVAILABLE_AMOUNT}));</script></td>
		</tr>
		</c:forEach>
		<tr class="table_list_row2">
			<td  align="center">合计：</td>
			<td><script>document.write(amountFormat(${map1.BALANCE_AMOUNT}));</script></td>
			<td><script>document.write(amountFormat(${map1.FREEZE_AMOUNT}));</script></td>
			<td><script>document.write(amountFormat(${map1.AVAILABLE_AMOUNT}));</script></td>
		</tr> 
	</table>
	<br>

    <!--<table width=100% border="0" align="center"  class="table_list">
	    <tr>
	    	<th colspan="5" align="left">
	    		<img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 
	    		订做车预扣款明细<input type="hidden" id="dealerId" name="dealerId" value="${dealerId }" />
	    	</th>
	    </tr>
	</table>
	<br>
	<table class="table_list" style="border-bottom:1px solid #DAE0EE" >
	  <tr align="center" class="tabletitle">
	    <th>开票单位代码</th>
	    <th>开票单位名称</th>
	    <th>需求状态</th>
	    <th>需求提报日期</th>
	    <th>资金类型</th>
	    <th>冻结金额</th>
	  </tr>
	  <c:forEach items="${specialList}" var="po">
		  <tr class="table_list_row1">
		    <td align="center">${po.DEALER_CODE}</td>
		    <td align="center">${po.DEALER_SHORTNAME}</td>
		    <td align="center">
		    <script type="text/javascript">
		    	writeItemValue(${po.REQ_STATUS});
		    </script>
		    </td>
		    <td align="center" >${po.RAISE_DATE}</td>
		    <td align="center" >${po.TYPE_NAME}</td>
		    <td align="center">
		    <script type="text/javascript">
		    	document.write(amountFormat(${po.FREEZE_AMOUNT}));
		    </script>
		    </td>
		  </tr>
	  </c:forEach>
	</table>
	<br>
	--><table width=100% border="0" align="center"  class="table_list">
	    <tr>
	    	<th colspan="5" align="left">
	    		<img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 
	    		扣款明细<input type="hidden" id="dealerId" name="dealerId" value="${dealerId }" />
	    	</th>
	    </tr>
	</table>
	
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    
</form>
<table class=table_query>
	<tr align="center">
		<td><input type="button" value="关闭" name="cancelBtn"  class="normal_btn" onclick="onClose();" /></td>
	</tr>
 </table>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/sales/financemanage/OemAccountBalance/queryFreezeList.json?COMMAND=1";
	var title = null;
	var calculateConfig = {totalColumns:"TYPE_NAME"};
	var columns = [
				{header: "开票单位代码", dataIndex: 'DEALER_CODE',align:'center'},
				{header: "开票单位名称", dataIndex: 'DEALER_SHORTNAME',align:'center'},
				{header: "订单号", dataIndex: 'ORDER_NO', align:'center'},
				{header: "订单状态", dataIndex: 'ORDER_STATUS', align:'center',renderer:getItemValue},
				{header: "订单提报日期", dataIndex: 'RAISE_DATE', align:'center'},
				{header: "资金类型", dataIndex: 'TYPE_NAME', align:'center'},
				{header: "冻结金额", dataIndex: 'FREEZE_AMOUNT', align:'center',renderer:amountFormat},
				{header: "扣款类型", dataIndex: 'FREEZE_TYPE', align:'center'}
	      ];
		      
//设置超链接  begin      
	
	//保存的ACTION设置
	function saveFleetInfo(){
	 if(submitForm('fm')){
		$('fm').action= '<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoApplication/saveFleetInfo.do';
	 	$('fm').submit();
	 }
	}
	
	//显示已有集团客户信息
	function showFleet(){
		OpenHtmlWindow('<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoApplication/showFleetList.do',900,600);
	}
	
	//取得子窗口选择的集团客户信息
	function fetchFleetInfo(fleetName,fleetType,region,mainBusiness,fundSize,staffSize,purpose,zipCode,address,mainLinkman,mainPhone,mainJob,mainEmail,otherLinkman,otherPhone,otherJob,otherEmail,status,submitUser,submitDate){

		document.getElementById("fleetName").value = fleetName;
		$('fm').fleetType.value = fleetType;
		$('fm').mainBusiness.value = mainBusiness;
		$('fm').fundSize.value= fundSize;
		$('fm').staffSize.value= staffSize;
		$('fm').purpose.value = purpose;
		$('fm').region.value = region;
		document.getElementById("zipCode").value = zipCode;
		document.getElementById("address").value = address;
		document.getElementById("mainLinkman").value = mainLinkman;
		document.getElementById("mainJob").value = mainJob;
		document.getElementById("mainPhone").value = mainPhone;
		document.getElementById("mainEmail").value = mainEmail;
		document.getElementById("otherLinkman").value = otherLinkman;
		document.getElementById("otherPhone").value = otherPhone;
		document.getElementById("otherJob").value = otherJob;
		document.getElementById("otherEmail").value = otherEmail;
	}
	
	// 提交的ACTION设置
	function submission(){
		if(submitForm('fm')){
			MyConfirm("是否确认提交?",submitCnfrm);
		 }
	
	}
	function onClose(){
		_hide();
	}
	
	// 确认提交的ACTION
	function submitCnfrm(){
		$('fm').action= '<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoApplication/addSubmitInfo.do';
		$('fm').submit();
	
	}
//设置超链接 end
	
</script>
</body>
</html>
