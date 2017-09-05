<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单免责</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	var myPage;
	//查询路径           
	var url = null;
	var title = null;
	var columns = [
				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_SHORTNAME',align:'center'},
				{header: "订单号",dataIndex: 'ORDER_NO',align:'center',renderer:orderLink},
				{header: "年",dataIndex: 'ORDER_YEAR',align:'center'},
				{header: "周",dataIndex: 'ORDER_WEEK',align:'center'},
<!--				{header: "免责状态",dataIndex: 'RESPOND_STATUS',align:'center',renderer:getItemValue},-->
				{header: "操作", dataIndex: 'ORDER_ID', align:'center',renderer:myLink}
		      ];
	function myLink(value,meta,record){
		//var isLock=0;
		var isRespond=0;
		//isLock=record.data.IS_LOCK;
		isRespond=record.data.IS_RESPOND;
		var dutyType=record.data.DUTY_TYPE;
		var respond_status=record.data.RESPOND_STATUS;
		var str="";
		//if(isLock=='10041001'){
		//	str+="<a href='#' onclick='lockInit("+record.data.ORDER_ID+",10041002)'>[解锁资源]</a>";
		//}else{
		//	str+="<a href='#' onclick='lockInit("+record.data.ORDER_ID+",10041001)'>[锁定资源]</a>";
		//}
		//if(isRespond=='10041001'){
			if(dutyType=="10431001"){
				if(respond_status==90011004||""==respond_status||null==respond_status){
					str+="<a href='#' onclick='respondInit("+record.data.ORDER_ID+",10041002)'>[修改]</a>";
					str+="<a href='#' onclick='commitRespond("+record.data.ORDER_ID+")'>[提报]</a>";
				}
			}
			str+="<a href='#' onclick='downLoadInit("+record.data.ORDER_ID+")'>[下载]</a>";
	//	}else{
		//	if(dutyType=="10431001"){
		//		str+="<a href='#' onclick='respondInit("+record.data.ORDER_ID+",10041001)'>[免责]</a>";
		//	}
		//		str+="<a href='#' onclick='downLoadInit("+record.data.ORDER_ID+")'>[下载]</a>";
		//}
		return String.format(str);
	}
	function orderLink(value,meta,record){
		var str="";
		str+="<a href='#' onclick='linkInit("+record.data.ORDER_ID+")'>"+value+"</a>";
		return String.format(str);
	}
	//查看明细信息
	function linkInit(orderId){
		var url="<%=contextPath%>/sales/ordermanage/orderdetail/OrderOperation/resourceQueryDetail.do?orderId="+orderId;
		OpenHtmlWindow(url,1000,500);
	}
	function downLoadInit(orderId){
		var url="<%=contextPath%>/sales/ordermanage/orderdetail/OrderOperation/getOrderDetailDownload.json?orderId="+orderId;
			document.fm.action=url;
			document.fm.target="_self";
			document.fm.submit();
			//makeFormCall(url, downResult, "fm") ;
	}
	function respondInit(order_id){
		var url="<%=contextPath%>/sales/ordermanage/orderdetail/OrderOperation/respondUpdateInit.do?orderId="+order_id;
		location.href=url;
		
	}
	function respondUpdate(order_id){
		var url="<%=contextPath%>/sales/ordermanage/orderdetail/OrderOperation/respondUpdate.do?orderId="+order_id;
		makeFormCall(url, respondResult, "fm") ;
	}
	function respondResult(json){
		if(json.flag=='1'){
			MyAlert("操作成功！！！");
			__extQuery__(1);
		}else{
			MyAlert("操作失败！！！");
		}
	}
	function commitRespond(order_id){
		var url="<%=contextPath%>/sales/ordermanage/orderdetail/OrderOperation/respondCommit.json?orderId="+order_id;
		if(confirm("确定提报吗？？")){
			makeFormCall(url, commitResult, "fm") ;
		}
		
	}
	function commitResult(json){
		if(json.flag=='1'){
			MyAlert("操作成功！！！");
			__extQuery__(1);
		}else{
			MyAlert("操作失败！！！");
		}
	}
	function executeQuery(){
		showDate();
		var orderNO = document.getElementById("orderNO").value;
		url = "<%=contextPath%>/sales/ordermanage/orderdetail/OrderOperation/orderAllQuery.json?&orderNO="+orderNO;
		__extQuery__(1);
	}
	//清除数据
	function clrTxt(id){
		$(id).value='';
	}
	
	function downLoadData(){
			document.fm.action='<%=request.getContextPath()%>/sales/ordermanage/orderdetail/OrderOperation/downLoadResData.json';
			document.fm.target="_self";
			document.fm.submit();
	}
	
	function showDate(){
		var startYear = document.getElementById("startYear").value;
		var startWeek = document.getElementById("startWeek").value;
		var endYear = document.getElementById("endYear").value;
		var endWeek = document.getElementById("endWeek").value;
		makeNomalFormCall('<%=request.getContextPath()%>/sales/showDate/ShowDateInfo/showDate.json?startYear='+startYear+'&startWeek='+startWeek+'&endYear='+endYear+'&endWeek='+endWeek,showDateInfo,'fm');
	}
	function showDateInfo(json){
		if(json.returnValue == '1'){
			document.getElementById("dateInfo").innerHTML = json.showDate;
		}
	}
</script>
</head>
<body  onload="executeQuery();">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单发运 &gt;常规订单免责</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query">
		<tr>
		  <td align="right" nowrap="nowrap">销售订单号：</td>
	      <td align="left" nowrap="nowrap">
			<input name="orderNo" id="orderNo" style="width:180px;"/>
	      </td>
		<td align="right" nowrap="nowrap">是否免责：</td>
	      <td align="left" nowrap="nowrap">
			<select id="isRespond" name="isRespond" style="width:150px;">
				<option value="">--请选择--</option>
				<option value="10041001">是</option>
				<option value="10041002">否</option>
			</select>
	      </td>
	     
		</tr>  
		<tr>
		  <td align="right" >订单周度起：</td>
		  <td colspan="3">
	     <select id="startYear" name="year" class="min_sel" style="width:70px" onchange="showDate();">
        		<c:forEach items="${years}" var="po">
        			<c:choose>
						<c:when test="${po == curYear}">
							<option value="${po}" selected="selected">${po}</option>
						</c:when>
						<c:otherwise>
							<option value="${po}">${po}</option>
						</c:otherwise>
					</c:choose> 
				</c:forEach>
          	</select>
          	&nbsp;年&nbsp;
		  	<select  id="startWeek" name="week" class="min_sel" style="width:60px" onchange="showDate();">
        		<c:forEach items="${weeks}" var="po">
					<c:choose>
						<c:when test="${po == curWeek}">
							<option value="${po}" selected="selected">${po}</option>
						</c:when>
						<c:otherwise>
							<option value="${po}">${po}</option>
						</c:otherwise>
					</c:choose> 
				</c:forEach>
		  	</select>
	   		至
	     <select id="endYear" name="endYear" class="min_sel" style="width:70px" onchange="showDate();">
        		<c:forEach items="${years}" var="po">
        			<c:choose>
						<c:when test="${po == curYear}">
							<option value="${po}" selected="selected">${po}</option>
						</c:when>
						<c:otherwise>
							<option value="${po}">${po}</option>
						</c:otherwise>
					</c:choose> 
				</c:forEach>
          	</select>
          	&nbsp;年&nbsp;
		  	<select  id="endWeek" name="endWeek" class="min_sel" style="width:60px" onchange="showDate();">
        		<c:forEach items="${weeks}" var="po">
					<c:choose>
						<c:when test="${po == curWeek}">
							<option value="${po}" selected="selected">${po}</option>
						</c:when>
						<c:otherwise>
							<option value="${po}">${po}</option>
						</c:otherwise>
					</c:choose> 
				</c:forEach>
		  	</select>&nbsp;&nbsp;&nbsp;&nbsp;<span id="dateInfo" class="innerHTMLStrong"></span>
		  	</td>
	     	
	    </tr> 
	    <tr>
	    	<td align="right" >选择经销商：</td>
			<td  colspan="2">
				<input type="text" class="middle_txt"  name="dealerCodes" style="width:90px;" value="" id="dealerCodes"/>
<!--				  <input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('dealerCodes','','true');" value="..." />-->
				<c:if test="${dutyType==10431001}">
			      	<input class="mini_btn"  name="button2" type="button" onclick="showOrgDealer('dealerCodes','','true', '${orgId}')" value="..." />
			     </c:if>
			      <c:if test="${dutyType==10431002}">
			      	<input class="mini_btn"  name="button2" type="button" onclick="showOrgDealer3('dealerCodes','','true', '${orgId}')" value="..." />
			      </c:if>
			      <c:if test="${dutyType==10431003}">
			      	<input class="mini_btn"  name="button2" type="button" onclick="showOrgDealer3('dealerCodes','','true', '${orgId}')" value="..." />
			      </c:if>
			      <c:if test="${dutyType==10431004}">
			      	<input class="mini_btn"  name="button2" type="button" onclick="showOrgDealer('dealerCodes','','true', '${orgId}')" value="..." />
			      </c:if>
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCodes');"/>
			</td>
			<td align="center" nowrap>&nbsp;</td>
	    </tr>
		<tr>
			<td  colspan="2" >&nbsp;</td>
			<td ><input name="queryBtn" type=button class="cssbutton" onClick="executeQuery();" value="查询"></td>
			<td ><input name="downLoad" type=button class="cssbutton" onClick="downLoadData();" value="下载"></td>
			<td align="right" colspan="2"><input value="10" style="width:30px;" id="pageSize" name="pageSize" datatype="0,isDigit,3"/></td>
		</tr>   
	</table>
		<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		<!--分页 end --> 
</form>
       <!-- 查询条件 end -->
</body>
</html>