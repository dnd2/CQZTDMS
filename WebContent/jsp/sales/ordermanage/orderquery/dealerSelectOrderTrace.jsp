<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
String path = request.getContextPath();
%>
<head>
<title>订单跟踪经销商选择</title>
<link href="<%=request.getContextPath()%>/style/content.css" rel="stylesheet" type="text/css" />
</head>

<body background="<%=path%>/img/chana/BJ-BJ.jpg">

<form id="fm" name="fm" method="post">
<input value='' type='hidden' class="normal_btn" name='deptId' />
<input value='' type='hidden' class="normal_btn" name='poseId' />
<table  width="60%" height="600" align="center">
	<tr align="left" valign="bottom" >	 
	  			<td align="left" colspan="3"> <font color="red" >本功能可以对订单执行情况、订单车辆在途情况查询,<br/>当前登录用户映射多个经销商,<br/>请从以下经销商列表中任选一个进行查看。<br/>(注：请不要设置弹出窗口屏蔽功能)</font> </td>
	</tr> 
	<tr valign="top"><td>
	<table  class="table_grid" >  
		<thead>  
			<tr >
			    <th width="10%" >经销商编码</th>  
			    <th width="30%" >经销商简称</th>
			    <th width="20%" >查看订单执行状态</th>
			</tr>
		</thead>
	    <c:forEach items="${dealerList}" var="item">
			<tr >
				<td >${item.dealerCode}</td>
				<td >${item.dealerShortname}</td>
				<td ><a href="#" onclick="goDXRedirect(${item.dealerId})">电信查看</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="goWTRedirect(${item.dealerId})">网通查看</a></td>
			</tr>
		</c:forEach>
	</table>
	</td>
	</tr>
</table>	
</form>
<script type="text/javascript">
	//域名调用
	function goRedirect(dealer_Id) {
		var fm=document.fm;
		//$('fm').action= "<%=path%>/sales/ordermanage/orderquery/DealerOrderTrace/dealerSelectedTrace.do?dealerId="+dealer_Id;
		//$('fm').submit();
		//window.location.href = "http://222.177.8.19/tms/dealer/dealerMain.action?DEALER_ID="+dealer_Id;
		window.open("http://www.changan-car.com/oms/dealer/dealerMain.action?DEALER_ID="+dealer_Id,"订单跟踪查询", "height=700, width=1000, top=50,left=100, toolbar=no, menubar=no, scrollbars=no, resizable=yes,location=yes, status=no");
	}
	// 电信调用
	function goDXRedirect(dealer_Id) {
		window.open("http://222.177.8.19/oms/dealer/dealerMain.action?DEALER_ID="+dealer_Id,"订单跟踪查询", "height=700, width=1000, top=50,left=100, toolbar=no, menubar=no, scrollbars=no, resizable=yes,location=yes, status=no");
	}
	//网通调用
	function goWTRedirect(dealer_Id) {
		window.open("http://58.17.185.174/oms/dealer/dealerMain.action?DEALER_ID="+dealer_Id,"订单跟踪查询", "height=700, width=1000, top=50,left=100, toolbar=no, menubar=no, scrollbars=no, resizable=yes,location=yes, status=no");
	}
</script>
</body>
</html>