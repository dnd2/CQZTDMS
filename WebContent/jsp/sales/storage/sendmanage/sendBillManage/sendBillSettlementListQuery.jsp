<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
List list =(List)request.getAttribute("list_logi");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>运单结算查询 </title>
<script type="text/javascript">
//初始化    
function doInit(){
	//日期控件初始化
	//__extQuery__(1);
}
</script>
</head>

<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>运单结算查询
	</div>
<form name="fm" method="post" id="fm">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
  <tr class="csstr" align="center">		
  		<td class="right">产地：</td>
  		<td align="left">
  			<select id="areaId" name="areaId" class="u-select">
  				<option value="">--请选择--</option>
  				<c:forEach items="${areaList}" var="po">
  					<option value="${po.AREA_ID }">${po.AREA_NAME }</option>
  				</c:forEach>
  			</select>
  		</td>
		 <td class="right">承运商：</td> 
		  <td align="left">
			 <select name="logiId" id="logiId" class="u-select" >
			 	<option value="">-请选择-</option>
					<c:if test="${list_logi!=null}">
						<c:forEach items="${list_logi}" var="list_logi">
							<option value="${list_logi.LOGI_ID}">${list_logi.LOGI_NAME}</option>
						</c:forEach>
					</c:if>
		  		</select>
		  </td> 	 
 </tr>
 <tr class="csstr" align="center">
 		<td class="right">结算单号：</td>
		<td align="left">
			<input type="text" maxlength="20"  name="balNo" datatype="1,is_digit_letter,30" maxlength="30" id="balNo" class="middle_txt"/>
		</td>
		<td class="right" nowrap="true">结算日期：</td>
		<td align="left" nowrap="true">
		<!-- 
			<input type="text" maxlength="20"  name="balStartDate" id="balStartDate"  type="text" maxlength="20"  class="short_txt"  hasbtn="true" datatype="1,is_date,10" callFunction="showcalendar(event, 'balStartDate', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" maxlength="20"  name="balEndDate" id="balEndDate" type="text" maxlength="20"  class="short_txt" hasbtn="true" datatype="1,is_date,10" callFunction="showcalendar(event, 'balEndDate', false);"/>
		 -->
			<input name="balStartDate" type="text" maxlength="20"  class="middle_txt" id="balStartDate" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'balStartDate', false);" />&nbsp;至&nbsp;
			<input name="balEndDate" type="text" maxlength="20"  class="middle_txt" id="balEndDate" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'balEndDate', false);" />
	
		</td>
 </tr>
 <tr align="center">
 		<td colspan="6" class="table_query_4Col_input" style="text-align: center">
 		 <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
   		  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="__extQuery__(1);" />   	
   		</td>
 </tr>
</table>
</form>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/storage/sendmanage/SendBillSettlementQuery/settlementListQuery.json";
	var title = null;
	var columns = [
	                {header: "序号",align:'center',renderer:getIndex},
					{header: "承运商名称",dataIndex: 'LOGI_NAME',align:'center'},
					{header: "结算单号",dataIndex: 'BAL_NO',align:'center'},
					{header: "结算日期",dataIndex: 'BAL_DATE',align:'center'},
					{header: "结算人",dataIndex: 'NAME',align:'center'},
					{header: "结算数量",dataIndex: 'BAL_COUNT',align:'center'},
					{header: "结算金额",dataIndex: 'BAL_AMOUNT',align:'center',renderer:formatCurrency},
					{header: "操作",dataIndex: 'BAL_ID',align:'center',renderer:myLink}
		      ];
	//清空数据
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
	}
	function myLink(value,meta,record){
// 	       return String.format(
<%-- 	       		 "<a href=\"<%=contextPath%>/sales/storage/sendmanage/SendBillSettlementQuery/toShowSettlementDetail.do?balId=" --%>
// 	               +value+"\">[查看]</a>  "
// 	               +"<a href='javascript:void(0);' onclick=\"balancePrint("+value+")\">[打印]</a>"
// 	               +"<a href='javascript:void(0);' onclick=\"cancleBalance("+value+")\">[取消]</a>");

			return String.format(
			  		 "<a href=\"<%=contextPath%>/sales/storage/sendmanage/SendBillSettlementQuery/toShowSettlementDetail.do?balId="
			          +value+"\">[查看]</a>");
	}
	function balancePrint(value){
		var tarUrl = "<%=contextPath%>/sales/storage/sendmanage/SendBillSettlementQuery/balancePrint.do?balId="+value;
		window.open(tarUrl,'','left=0,top=0,width='+screen.availWidth +'- 10,height='+screen.availHeight+'-50,toolbar=no,resizable=yes,menubar=no,scrollbars=no,location=no');

	}
	
	function cancleBalance(value){
		window.location.href = "<%=contextPath%>/sales/storage/sendmanage/SendBillSettlementQuery/toShowSettlementDetail.do?balId="+value+"&command=1";
	}
</script>
</body>
</html>
