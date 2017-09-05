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
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 客户信息管理 &gt; 日报表新增</div>
	<form id="fm" name="fm" method="post">
	<table bgcolor="#F3F4F8" border="1px" style="border-color:gray;">
	
		<tr>
			<td nowrap="nowrap">经销商</td>
			<td colspan="3">
			<c:forEach var="dealerList_D" items="${dealerList}">
				${dealerList_D.DEALER_NAME}
      		</c:forEach>
			</td>
			<td>时间</td>
			<td colspan="${cols}"><font style="width: 99%">
			<script type="text/javascript">
     		var a_names=new Array("01","02","03","04","05","06","07","08","09","10","11","12")
     		var currentTime=new Date()
     		var year=currentTime.getFullYear()
     		var month=currentTime.getMonth()
    		 var date=currentTime.getDate()
     		document.write(year+"-"+a_names[month]+"-"+date)
			</script>
			</font>  </td>
			<td></td>
		</tr>
		<tr>
			<td>项目</td>
		<c:forEach var="carType"  varStatus="current" items="${carType}" >
			<td><input type="hidden" name="carType${current.index}" id="alto${current.index}" value="${carType}"/>${carType}</td>
      	</c:forEach>
			<td><input type="hidden" name="total" id="total" value="小计"/>小计</td>
		</tr>
		<tr>
			<td nowrap="nowrap">本日展厅首次客流：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
				<td><input type="text" id="FirstPassenger${current.index}" name="FirstPassenger${current.index}"  onkeyup="totalNum('FirstPassenger','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input type="text" style="background-color: transparent;" id="FirstPassengerTotal" name="FirstPassengerTotal" readonly="true"/></td>
		</tr>
		<tr>
			<td>本日展厅邀约客流：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
			<td><input type="text" id="InvitePassenger${current.index}" name="InvitePassenger${current.index}" onkeyup="totalNum('InvitePassenger','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input style="background-color: transparent;" id="InvitePassengerTotal" name="InvitePassengerTotal" readonly="true"/></td>
		</tr>
		<tr>
			<td>本日来电客户数：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
			<td><input type="text" id="CallPassenger${current.index}" name="CallPassenger${current.index}" onkeyup="totalNum('CallPassenger','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input style="background-color: transparent;" id="CallPassengerTotal" name="CallPassengerTotal" readonly="true"/></td>
		</tr>
		<tr>
			<td>本日试驾数：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
			<td><input type="text" id="TestDrive${current.index}" name="TestDrive${current.index}" onkeyup="totalNum('TestDrive','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input style="background-color: transparent;" id="TestDriveTotal" name="TestDriveTotal" readonly="true"/></td>
		</tr>
		<tr>
			<td>本日展厅交车数量：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
			<td><input type="text" id="Delivery${current.index}" name="Delivery${current.index}" onkeyup="totalNum('Delivery','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input style="background-color: transparent;" id="DeliveryTotal" name="DeliveryTotal" readonly="true"/></td>
		</tr>
		<tr>
			<td>本日大客户交车数量：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
			<td><input type="text" id="LargerDelivery${current.index}" name="LargerDelivery${current.index}" onkeyup="totalNum('LargerDelivery','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input style="background-color: transparent;" id="LargerDeliveryTotal" name="LargerDeliveryTotal" readonly="true"/></td>
		</tr>
		<tr>
			<td>本日二网交车数量：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
				<td><input type="text" id="SecondDelivery${current.index}" name="SecondDelivery${current.index}" onkeyup="totalNum('SecondDelivery','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input style="background-color: transparent;" id="SecondDeliveryTotal" name="SecondDeliveryTotal" readonly="true"/></td>
		</tr>
		<tr>
			<td>本日新增O级订单：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
			<td><input type="text" id="O_levelOrder${current.index}" name="O_levelOrder${current.index}" onkeyup="totalNum('O_levelOrder','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input style="background-color: transparent;" id="O_levelOrderTotal" name="O_levelOrderTotal" readonly="true"/></td>
		</tr>
		<tr>
			<td>本日新增H级：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
				<td><input type="text" id="H_levelOrder${current.index}" name="H_levelOrder${current.index}" onkeyup="totalNum('H_levelOrder','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input style="background-color: transparent;" id="H_levelOrderTotal" name="H_levelOrderTotal" readonly="true"/></td>
		</tr>
		<tr>
			<td> 本日新增A级：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
			<td><input type="text" id="A_levelOrder${current.index}" name="A_levelOrder${current.index}" onkeyup="totalNum('A_levelOrder','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input style="background-color: transparent;" id="A_levelOrderTotal" name="A_levelOrderTotal" readonly="true"/></td>
		</tr>
		<tr>
			<td>本日新增B级：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
			<td><input type="text" id="B_levelOrder${current.index}" name="B_levelOrder${current.index}" onkeyup="totalNum('B_levelOrder','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input style="background-color: transparent;" id="B_levelOrderTotal" name="B_levelOrderTotal" readonly="true"/></td>
		</tr>
		<tr>
			<td>本日新增C级：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
			<td><input type="text" id="C_levelOrder${current.index}" name="C_levelOrder${current.index}" onkeyup="totalNum('C_levelOrder','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input style="background-color: transparent;" id="C_levelOrderTotal" name="C_levelOrderTotal" readonly="true"/></td>
		</tr>
		<tr>
			<td> 本日建卡数：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
			<td><input type="text" id="CreateCard${current.index}" name="CreateCard${current.index}" onkeyup="totalNum('CreateCard','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input style="background-color: transparent;" id="CreateCardTotal" name="CreateCardTotal" readonly="true"/></td>
		</tr>
		<tr>
			<td>本日战败：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
			<td><input type="text" id="Lost${current.index}" name="Lost${current.index}" onkeyup="totalNum('Lost','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input style="background-color: transparent;" id="LostTotal" name="LostTotal" readonly="true"/></td>
		</tr>
		<tr>
			<td>当日实际库存数：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
			<td><input type="text" id="RealStock${current.index}" name="RealStock${current.index}" onkeyup="totalNum('RealStock','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input style="background-color: transparent;" id="RealStockTotal" name="RealStockTotal" readonly="true"/></td>
		</tr>
		<tr>
			<td>老客户转介绍：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
			<td><input type="text" id="RegularRecommend${current.index}" name="RegularRecommend${current.index}" onkeyup="totalNum('RegularRecommend','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input style="background-color: transparent;" id="RegularRecommendTotal" name="RegularRecommendTotal" readonly="true"/></td>
		</tr>
		<tr>
			<td>旧车置换：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
			<td><input type="text" id="OldCarReplace${current.index}" name="OldCarReplace${current.index}" onkeyup="totalNum('OldCarReplace','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input style="background-color: transparent;" id="OldCarReplaceTotal" name="OldCarReplaceTotal" readonly="true"/></td>
		</tr>
		<tr>
			<td>H留存客户：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
			<td><input type="text" id="H_Retain${current.index}" name="H_Retain${current.index}" onkeyup="totalNum('H_Retain','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input style="background-color: transparent;" id="H_RetainTotal" name="H_RetainTotal" readonly="true"/></td>
		</tr>
		
		<tr>
			<td> A留存客户：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
			<td><input type="text" id="A_Retain${current.index}" name="A_Retain${current.index}" onkeyup="totalNum('A_Retain','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input style="background-color: transparent;" id="A_RetainTotal" name="A_RetainTotal" readonly="true"/></td>
		</tr>
		<tr>
			<td>B留存客户：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
			<td><input type="text" id="B_Retain${current.index}" name="B_Retain${current.index}" onkeyup="totalNum('B_Retain','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input style="background-color: transparent;" id="B_RetainTotal" name="B_RetainTotal" readonly="true"/></td>
		</tr>
		<tr>
			<td> C留存客户：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
			<td><input type="text" id="C_Retain${current.index}" name="C_Retain${current.index}" onkeyup="totalNum('C_Retain','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input style="background-color: transparent;" id="C_RetainTotal" name="C_RetainTotal" readonly="true"/></td>
		</tr>
		<tr>
			<td>本月未交订单：</td>
			<c:forEach var="carType"  varStatus="current" items="${carType}" >
			<td><input type="text" id="UnCommitOrder${current.index}" name="UnCommitOrder${current.index}" onkeyup="totalNum('UnCommitOrder','${seriesCount}');" datatype="1,is_double,8" decimal="0"/></td>
			</c:forEach>
			<td><input style="background-color: transparent;" id="UnCommitOrderTotal" name="UnCommitOrderTotal" readonly="true"/></td>
		</tr>
	</table>
	<br />
		<input id="insert" name="insert" type=button class="cssbutton" onclick="insertu();" value="保存"/> <input type="button" class="normal_btn" onclick="toBack();" value="返回"/>
	</form>
</div>

<script type="text/javascript">
function insertu(){
	makeNomalFormCall('<%=request.getContextPath()%>/sales/customerInfoManage/SalesDailyReport/DailyReportInsert.json',showResult,'fm');
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