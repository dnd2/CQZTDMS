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



<title>经销商日报表新增</title>
</head>
<body  > 
<div class="wbox" >
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 客户信息管理 &gt; 日报表</div>
	<form id="fm" name="fm" method="post">
	<table bgcolor="#F3F4F8" border="1px" style="border-color:gray;">
	
		<tr>
			<td nowrap="nowrap">经销商</td>
			<td colspan="3">
			<c:forEach var="dealerListName" items="${dealerList}">
				${dealerListName.DEALER_NAME}
      		</c:forEach>
			</td>
			<td>时间</td>
			<td colspan="${cols}">
			<font style="width: 99%">
				<c:forEach var="dailyList" items="${dailyList1}">
				${dailyList.CREATE_DATE}
      			</c:forEach>
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
			<c:forEach var="dailyList" items="${TotalList}">
				<td>
				<input type="text" style="background-color: transparent;" value="<c:if test="${dailyList.FIRST_PASSENGER!=0}">${dailyList.FIRST_PASSENGER}</c:if>" readonly="readonly"/>
      			</td>
      		</c:forEach>
			
		</tr>
		<tr>
			<td>本日展厅邀约客流：</td>
			<c:forEach var="dailyList" items="${TotalList}">
				<td>
				<input type="text" style="background-color: transparent;" value="<c:if test='${dailyList.INVITE_PASSENGER!=0}'>${dailyList.INVITE_PASSENGER}</c:if>" readonly="readonly"/>
      			</td>
      		</c:forEach>
      	</tr>
		<tr>
			<td>本日来电客户数：</td>
			<c:forEach var="dailyList" items="${TotalList}">
				<td>
				<input type="text" style="background-color: transparent;" value="<c:if test='${dailyList.CALL_PASSENGER!=0}'>${dailyList.CALL_PASSENGER}</c:if>" readonly="readonly"/>
      			</td>
      		</c:forEach>
			
      	</tr>
		<tr>
			<td>本日试驾数：</td>
			<c:forEach var="dailyList" items="${TotalList}">
			<td>
				<input type="text" style="background-color: transparent;" value="<c:if test='${dailyList.TEST_DRIVER!=0}'>${dailyList.TEST_DRIVER}</c:if>" readonly="readonly"/>
      		</td>
      		</c:forEach>
			
      	</tr>
		<tr>
			<td>本日展厅交车数量：</td>
			<c:forEach var="dailyList" items="${TotalList}">
				<td>
				<input type="text" style="background-color: transparent;" value="<c:if test='${dailyList.DELIVERY!=0}'>${dailyList.DELIVERY}</c:if>" readonly="readonly"/>
      			</td>
      		</c:forEach>
			
      	</tr>
		<tr>
			<td>本日大客户交车数量：</td>
			<c:forEach var="dailyList" items="${TotalList}">
				<td>
				<input type="text" style="background-color: transparent;" value="<c:if test='${dailyList.LARGER_DELIVERY!=0}'>${dailyList.LARGER_DELIVERY}</c:if>" readonly="readonly"/>
      			</td>
      			</c:forEach>
			
      	</tr>
		<tr>
			<td>本日二网交车数量：</td>
			<c:forEach var="dailyList" items="${TotalList}">
			<td>
				<input type="text" style="background-color: transparent;" value="<c:if test='${dailyList.SECOND_DELIVERY!=0}'>${dailyList.SECOND_DELIVERY}</c:if>" readonly="readonly"/>
      			</td>
      		</c:forEach>
			
      	</tr>
		<tr>
			<td>本日新增O级订单：</td>
			<c:forEach var="dailyList" items="${TotalList}">
			<td>
				<input type="text" style="background-color: transparent;" value="<c:if test='${dailyList.O_LEVEL_ORDER!=0}'>${dailyList.O_LEVEL_ORDER}</c:if>" readonly="readonly"/>
      		</td>
      		</c:forEach>
			
      	</tr>
		<tr>
			<td>本日新增H级：</td>
			<c:forEach var="dailyList" items="${TotalList}">
				<td>
				<input type="text" style="background-color: transparent;" value="<c:if test='${dailyList.H_LEVEL_ORDER!=0}'>${dailyList.H_LEVEL_ORDER}</c:if>" readonly="readonly"/>
      			</td>
      		</c:forEach>
			
      	</tr>
		<tr>
			<td> 本日新增A级：</td>
			<c:forEach var="dailyList" items="${TotalList}">
			<td>
				<input type="text" style="background-color: transparent;" value="<c:if test='${dailyList.A_LEVEL_ORDER!=0}'>${dailyList.A_LEVEL_ORDER}</c:if>" readonly="readonly"/>
      		</td>
			</c:forEach>
      	</tr>
		<tr>
			<td>本日新增B级：</td>
			<c:forEach var="dailyList" items="${TotalList}">
			<td>
				<input type="text" style="background-color: transparent;" value="<c:if test='${dailyList.B_LEVEL_ORDER!=0}'>${dailyList.B_LEVEL_ORDER}</c:if>" readonly="readonly"/>
      		</td>
      		</c:forEach>
			
      	</tr>
		<tr>
			<td>本日新增C级：</td>
			<c:forEach var="dailyList" items="${TotalList}">
			<td>
				<input type="text" style="background-color: transparent;" value="<c:if test='${dailyList.C_LEVEL_ORDER!=0}'>${dailyList.C_LEVEL_ORDER}</c:if>" readonly="readonly"/>
      		</td>
      		</c:forEach>
			
      	</tr>
		<tr>
			<td> 本日建卡数：</td>
			<c:forEach var="dailyList" items="${TotalList}">
			<td>
				<input type="text" style="background-color: transparent;" value="<c:if test='${dailyList.CREATE_CARD!=0}'>${dailyList.CREATE_CARD}</c:if>" readonly="readonly"/>
      		</td>
      		</c:forEach>
			
      	</tr>
		<tr>
			<td>本日战败：</td>
			<c:forEach var="dailyList" items="${TotalList}">
			<td>
				<input type="text" style="background-color: transparent;" value="<c:if test='${dailyList.LOST!=0}'>${dailyList.LOST}</c:if>" readonly="readonly"/>
      		</td>
      		</c:forEach>
			
      	</tr>
		<tr>
			<td>当日实际库存数：</td>
			<c:forEach var="dailyList" items="${TotalList}">
				<td>
				<input type="text" style="background-color: transparent;" value="<c:if test='${dailyList.REAL_STOCK!=0}'>${dailyList.REAL_STOCK}</c:if>" readonly="readonly"/>
      			</td>
      		</c:forEach>
			
      	</tr>
		<tr>
			<td>老客户转介绍：</td>
			<c:forEach var="dailyList" items="${TotalList}">
				<td>
				<input type="text" style="background-color: transparent;" value="<c:if test='${dailyList.REGULAR_RECOMMEND!=0}'>${dailyList.REGULAR_RECOMMEND}</c:if>" readonly="readonly"/>
      			</td>
      		</c:forEach>
			
      	</tr>
		<tr>
			<td>旧车置换：</td>
			<c:forEach var="dailyList" items="${TotalList}">
			<td>
				<input type="text" style="background-color: transparent;" value="<c:if test='${dailyList.OLD_CAR_REPLACE!=0}'>${dailyList.OLD_CAR_REPLACE}</c:if>" readonly="readonly"/>
      		</td>
      		</c:forEach>
			
      	</tr>
		<tr>
			<td>H留存客户：</td>
			<c:forEach var="dailyList" items="${TotalList}">
			<td>
				<input type="text" style="background-color: transparent;" value="<c:if test='${dailyList.H_RETAIN!=0}'>${dailyList.H_RETAIN}</c:if>" readonly="readonly"/>
      		</td>
      		</c:forEach>
      			
      	</tr>
		
		<tr>
			<td> A留存客户：</td>
			<c:forEach var="dailyList" items="${TotalList}">
			<td>
				<input type="text" style="background-color: transparent;" value="<c:if test='${dailyList.A_RETAIN!=0}'>${dailyList.A_RETAIN}</c:if>" readonly="readonly"/>
      		</td>
      		</c:forEach>
			
      	</tr>
		<tr>
			<td>B留存客户：</td>
			<c:forEach var="dailyList" items="${TotalList}">
				<td>
				<input type="text" style="background-color: transparent;" value="<c:if test='${dailyList.B_RETAIN!=0}'>${dailyList.B_RETAIN}</c:if>" readonly="readonly"/>
      			</td>
      		</c:forEach>
			
      	</tr>
		<tr>
			<td> C留存客户：</td>
			<c:forEach var="dailyList" items="${TotalList}">
				<td>
				<input type="text" style="background-color: transparent;" value="<c:if test='${dailyList.C_RETAIN!=0}'>${dailyList.C_RETAIN}</c:if>" readonly="readonly"/>
      			</td>
      		</c:forEach>
		
      	</tr>
		<tr>
			<td>本月未交订单：</td>
			<c:forEach var="dailyList" items="${TotalList}">
			<td>
				<input type="text" style="background-color: transparent;" value="<c:if test='${dailyList.UNCOMMIT_ORDER!=0}'>${dailyList.UNCOMMIT_ORDER}</c:if>" readonly="readonly"/>
      		</td>
      		</c:forEach>
			
      	</tr>
	</table>
	<br />
		<input type="button" class="normal_btn" onclick="toBack();" value="返回"/>
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
	//fm.action='<%=request.getContextPath()%>/sales/customerInfoManage/SalesDailyReport/DailyReportInit.do';
	//fm.submit();
	history.back();
}
</script>

</body>
</html>