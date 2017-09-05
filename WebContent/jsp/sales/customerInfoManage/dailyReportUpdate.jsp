<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	int yes = Constant.IF_TYPE_YES;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/summation.js"></script>


<title>经销商日报表新增</title>
</head>
<body  > 
<div class="wbox" >
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 客户信息管理 &gt; 日报表</div>
	<form id="fm" name="fm" method="post">
	<c:forEach var="dailyReportId"   items="${dailyReportIds}" varStatus="current" >
				<input type="hidden" name="dailyReportId${current.index}" id="dailyReportId${current.index}" style="background-color: transparent;" value="${dailyReportId.DAILY_REPORT_ID}"/>
      	</c:forEach>
	<table bgcolor="#F3F4F8" border="1px" style="border-color:gray;width:100%;">
	
		<tr with="100%">
			<td  style="width:20%;" nowrap >经销商</td>
			<td colspan="3" style="width:30%">
			<c:forEach var="dealerList_D" items="${dealerList}">
				${dealerList_D.DEALER_NAME}
				<input type="hidden" name="contentId" id="contentId" value="${contentId}"/>
      		</c:forEach>
			</td>
			<td style="width:20%">时间</td>
			<td colspan="${cols}" style="width:30%">
			<font style="width: 99%">
				<c:forEach var="dailyList" items="${dailyList1}">
				${dailyList.INSERT_TIME}
      			</c:forEach>
			</font>  </td>
		</tr>
		<tr>
			<td>项目</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
				<td><input type="hidden" name="carType${current.index}" id="alto${current.index}" value="${carType}"/>${carType}</td>
      		</c:forEach>
			<td><input type="hidden" name="total" id="total" value="小计"/>小计
      		</td>
		</tr>
		<tr>
			<td nowrap>本日展厅首次客流：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
				<td>
				<input type="text" id="FirstPassenger${current.index}" name="FirstPassenger${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('FirstPassenger','${seriesCount}');" value="${dailyList.FIRST_PASSENGER}"/>
      			</td>
      		</c:forEach>
      		<td>
      		<c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text" id="FirstPassengerTotal" name="FirstPassengerTotal"  datatype="1,is_double,8" decimal="0"  value="${dailyList.FIRST_PASSENGER}" style="background-color: transparent;"/>
      		</c:forEach >
      		</td>
		</tr>
		<tr>
			<td>本日展厅邀约客流：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
			<td>
				<input type="text"  id="InvitePassenger${current.index}" name="InvitePassenger${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('InvitePassenger','${seriesCount}');" value="${dailyList.INVITE_PASSENGER}"/>
      		</td>
      		</c:forEach>
      		<td>
      		<c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text"  id="InvitePassengerTotal" name="InvitePassengerTotal"  datatype="1,is_double,8" decimal="0" readonly value="${dailyList.INVITE_PASSENGER}" style="background-color: transparent;"/>
      		</c:forEach>
      		</td>
			
      	</tr>
		<tr>
			<td>本日来电客户数：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
				<td>
				<input type="text"  id="CallPassenger${current.index}" name="CallPassenger${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('CallPassenger','${seriesCount}');" value="${dailyList.CALL_PASSENGER}"/>
      			</td>
      		</c:forEach>
      		<td>
      		<c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text"  id="CallPassengerTotal" name="CallPassengerTotal"  datatype="1,is_double,8" decimal="0" onkeyup="summation();" value="${dailyList.CALL_PASSENGER}" style="background-color: transparent;"/>
      			</c:forEach>
      		</td>
      	</tr>
		<tr>
			<td>本日试驾数：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
				<td>
				<input type="text"  id="TestDrive${current.index}" name="TestDrive${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('TestDrive','${seriesCount}');" value="${dailyList.TEST_DRIVER}"/>
      			</td>
      		</c:forEach>
      			<td>
      			<c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text"  id="TestDriveTotal" name="TestDriveTotal"  datatype="1,is_double,8" decimal="0" readonly value="${dailyList.TEST_DRIVER}" style="background-color: transparent;"/>
      			</c:forEach>
      			</td>
			
      	</tr>
		<tr>
			<td>本日展厅交车数量：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
				<td>
				<input type="text"  id="Delivery${current.index}" name="Delivery${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('Delivery','${seriesCount}');" value="${dailyList.DELIVERY}"/>
      			</td>
      		</c:forEach>
      			<td>
      			<c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text"  id="DeliveryTotal" name="DeliveryTotal"  datatype="1,is_double,8" decimal="0" readonly value="${dailyList.DELIVERY}" style="background-color: transparent;"/>
      			</c:forEach>
      			</td>
			
      	</tr>
		<tr>
			<td>本日大客户交车数量：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
				<td>
				<input type="text"  id="LargerDelivery${current.index}" name="LargerDelivery${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('LargerDelivery','${seriesCount}');" value="${dailyList.LARGER_DELIVERY}"/>
      			</td>
      			</c:forEach>
      			<td><c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text"  id="LargerDeliveryTotal" name="LargerDeliveryTotal"  datatype="1,is_double,8" decimal="0" onkeyup="summation();" value="${dailyList.LARGER_DELIVERY}" style="background-color: transparent;"/>
      			</c:forEach>
      			</td>
			
      	</tr>
		<tr>
			<td>本日二网交车数量：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
				<td>
				<input type="text"  id="SecondDelivery${current.index}" name="SecondDelivery${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('SecondDelivery','${seriesCount}');" value="${dailyList.SECOND_DELIVERY}"/>
      			</td>
      		</c:forEach>
      			<td>
      			<c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text"  id="SecondDeliveryTotal" name="SecondDeliveryTotal"  datatype="1,is_double,8" decimal="0" readonly value="${dailyList.SECOND_DELIVERY}" style="background-color: transparent;"/>
      			</c:forEach>
      			</td>
			
      	</tr>
		<tr>
			<td>本日新增O级订单：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
				<td>
				<input type="text"  id="O_levelOrder${current.index}" name="O_levelOrder${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('O_levelOrder','${seriesCount}');" value="${dailyList.O_LEVEL_ORDER}"/>
      			</td>
      			</c:forEach>
      			<td>
      			<c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text"  id="O_levelOrderTotal" name="O_levelOrderTotal"  datatype="1,is_double,8" decimal="0" readonly value="${dailyList.O_LEVEL_ORDER}" style="background-color: transparent;"/>
      			</c:forEach>
      			</td>
			
      	</tr>
		<tr>
			<td>本日新增H级：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
				<td>
				<input type="text"  id="H_levelOrder${current.index}" name="H_levelOrder${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('H_levelOrder','${seriesCount}');" value="${dailyList.H_LEVEL_ORDER}"/>
      			</td>
      			</c:forEach>
      			<td>
      			<c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text"  id="H_levelOrderTotal" name="H_levelOrderTotal"  datatype="1,is_double,8" decimal="0" readonly value="${dailyList.H_LEVEL_ORDER}" style="background-color: transparent;"/>
      			</c:forEach>
      			</td>
		
      	</tr>
		<tr>
			<td> 本日新增A级：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
				<td>
				<input type="text"  id="A_levelOrder${current.index}" name="A_levelOrder${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('A_levelOrder','${seriesCount}');" value="${dailyList.A_LEVEL_ORDER}"/>
      			</td>
      		</c:forEach>
      		<td>
      		<c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text"  id="A_levelOrderTotal" name="A_levelOrderTotal"  datatype="1,is_double,8" decimal="0" readonly value="${dailyList.A_LEVEL_ORDER}" style="background-color: transparent;"/>
      		</c:forEach>
      		</td>
			
      	</tr>
		<tr>
			<td>本日新增B级：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
				<td>
				<input type="text"  id="B_levelOrder${current.index}" name="B_levelOrder${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('B_levelOrder','${seriesCount}');" value="${dailyList.B_LEVEL_ORDER}"/>
      			</td>
      		</c:forEach>
      			<td>
      			<c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text"  id="B_levelOrderTotal" name="B_levelOrderTotal"  datatype="1,is_double,8" decimal="0" readonly  value="${dailyList.B_LEVEL_ORDER}" style="background-color: transparent;"/>
      			</c:forEach>
      			</td>
			
      	</tr>
		<tr>
			<td>本日新增C级：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
				<td>
				<input type="text"  id="C_levelOrder${current.index}" name="C_levelOrder${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('C_levelOrder','${seriesCount}');" value="${dailyList.C_LEVEL_ORDER}"/>
      			</td>
      		</c:forEach>
      			<td>
      			<c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text"  id="C_levelOrderTotal" name="C_levelOrderTotal"  datatype="1,is_double,8" decimal="0" readonly value="${dailyList.C_LEVEL_ORDER}" style="background-color: transparent;"/>
      			</c:forEach>
      			</td>
			
      	</tr>
		<tr>
			<td> 本日建卡数：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
				<td>
				<input type="text"  id="CreateCard${current.index}" name="CreateCard${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('CreateCard','${seriesCount}');" value="${dailyList.CREATE_CARD}"/>
      			</td>
      		</c:forEach>
      			<td>
      			<c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text"  id="CreateCardTotal" name="CreateCardTotal"  datatype="1,is_double,8" decimal="0" onkeyup="summation();" value="${dailyList.CREATE_CARD}" style="background-color: transparent;"/>
      			</c:forEach>
      			</td>
			
      	</tr>
		<tr>
			<td>本日战败：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
				<td>
				<input type="text"  id="Lost${current.index}" name="Lost${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('Lost','${seriesCount}');" value="${dailyList.LOST}"/>
      			</td>
      			</c:forEach>
      			<td>
      			<c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text"  id="LostTotal" name="LostTotal"  datatype="1,is_double,8" decimal="0" onkeyup="summation();" value="${dailyList.LOST}"  style="background-color: transparent;"/>
      			</c:forEach>
      			</td>
			
      	</tr>
		<tr>
			<td>当日实际库存数：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
				<td>
				<input type="text"  id="RealStock${current.index}" name="RealStock${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('RealStock','${seriesCount}');" value="${dailyList.REAL_STOCK}"/>
      			</td>
      		</c:forEach>
      			<td>
      			<c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text"  id="RealStockTotal" name="RealStockTotal"  datatype="1,is_double,8" decimal="0" readonly value="${dailyList.REAL_STOCK}" style="background-color: transparent;"/>
      			</c:forEach>
      			</td>
			
      	</tr>
		<tr>
			<td>老客户转介绍：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
				<td>
				<input type="text"  id="RegularRecommend${current.index}" name="RegularRecommend${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('RegularRecommend','${seriesCount}');" value="${dailyList.REGULAR_RECOMMEND}"/>
      			</td>
      		</c:forEach>
      			<td>
      			<c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text"  id="RegularRecommendTotal" name="RegularRecommendTotal"  datatype="1,is_double,8" decimal="0"  value="${dailyList.REGULAR_RECOMMEND}" readonly  style="background-color: transparent;"/>
      			</c:forEach>
      			</td>
		
      	</tr>
		<tr>
			<td>旧车置换：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
				<td>
				<input type="text"  id="OldCarReplace${current.index}" name="OldCarReplace${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('OldCarReplace','${seriesCount}');" value="${dailyList.OLD_CAR_REPLACE}"/>
      			</td>
      		</c:forEach>
      			<td>
      			<c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text"  id="OldCarReplaceTotal" name="OldCarReplaceTotal"  datatype="1,is_double,8" decimal="0" readonly value="${dailyList.OLD_CAR_REPLACE}" readonly   style="background-color: transparent;"/>
      			</c:forEach>
      			</td>
      	</tr>
		<tr>
			<td>H留存客户：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
				<td>
				<input type="text"  id="H_Retain${current.index}" name="H_Retain${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('H_Retain','${seriesCount}');" value="${dailyList.H_RETAIN}"/>
      			</td>
      		</c:forEach>
      		<td>
      		<c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text"  id="H_RetainTotal" name="H_RetainTotal"  datatype="1,is_double,8" decimal="0" readonly value="${dailyList.H_RETAIN}"  style="background-color: transparent;"/>
      			</c:forEach>
      			</td>
      	</tr>
		
		<tr>
			<td> A留存客户：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
				<td>
				<input type="text"  id="A_Retain${current.index}" name="A_Retain${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('A_Retain','${seriesCount}');" value="${dailyList.A_RETAIN}"/>
      			</td>
      		</c:forEach>
      		<td>
      		<c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text"  id="A_RetainTotal" name="A_RetainTotal"  datatype="1,is_double,8" decimal="0" readonly  value="${dailyList.A_RETAIN}" style="background-color: transparent;"/>
      			</c:forEach>
      			</td>
      	</tr>
		<tr>
			<td>B留存客户：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
				<td>
				<input type="text"  id="B_Retain${current.index}" name="B_Retain${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('B_Retain','${seriesCount}');" value="${dailyList.B_RETAIN}"/>
      			</td>
      		</c:forEach>
      			<td>
      			<c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text"  id="B_RetainTotal" name="B_RetainTotal"  datatype="1,is_double,8" decimal="0" readonly value="${dailyList.B_RETAIN}"  style="background-color: transparent;"/>
      			</c:forEach>
      			</td>
      		
      	</tr>
		<tr>
			<td> C留存客户：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
				<td>
				<input type="text"  id="C_Retain${current.index}" name="C_Retain${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('C_Retain','${seriesCount}');" value="${dailyList.C_RETAIN}"/>
      			</td>
      		</c:forEach>
      		<td>
      		<c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text"  id="C_RetainTotal" name="C_RetainTotal"  datatype="1,is_double,8" decimal="0" readonly value="${dailyList.C_RETAIN}"  style="background-color: transparent;"/>
      			</c:forEach>
      			</td>
      	</tr>
		<tr>
			<td>本月未交订单：</td>
			<c:forEach var="dailyList" items="${TotalList}" varStatus="current">
			<td>
				<input type="text"  id="UnCommitOrder${current.index}" name="UnCommitOrder${current.index}"  datatype="1,is_double,8" decimal="0" onkeyup="totalNum('UnCommitOrder','${seriesCount}');" value="${dailyList.UNCOMMIT_ORDER}"/>
      		</td>
      		</c:forEach>
      		<td>
      		<c:forEach var="dailyList" items="${smallList}" varStatus="current">
				<input type="text"  id="UnCommitOrderTotal" name="UnCommitOrderTotal"  datatype="1,is_double,8" decimal="0" readonly value="${dailyList.UNCOMMIT_ORDER}"  style="background-color: transparent;"/>
      		</c:forEach>
      		</td>
      	</tr>
	</table>
	<br />
		<input id="insert" name="insert" type="button" class="cssbutton" onclick="updateDaily(contentId);" value="修改"/> <input type="button" class="normal_btn" onclick="toBack();" value="返回"/>
	</form>
</div>

<script type="text/javascript">
function updateDaily(){
	var contentId=document.getElementById("contentId").value;
	makeNomalFormCall('<%=request.getContextPath()%>/sales/customerInfoManage/SalesDailyReport/updateDaily.json?contentId='+contentId,showResult,'fm');
}
function showResult(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("操作成功！");
			fm.action='<%=request.getContextPath()%>/sales/customerInfoManage/SalesDailyReport/DailyReportInit.do';
			fm.submit();
		}else{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	

function toBack(){
	fm.action='<%=request.getContextPath()%>/sales/customerInfoManage/SalesDailyReport/DailyReportInit.do';
	fm.submit();
}
</script>

</body>
</html>