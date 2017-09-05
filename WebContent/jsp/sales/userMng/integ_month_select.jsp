<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
	String isSpecialCar = String.valueOf(request.getAttribute("isSpecialCar"));
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>月度积分查询</title>
<script type="text/javascript">
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/usermng/IntegationManage/integMonthSelect.json";
	var title = null;
	var columns = [
				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "姓名",dataIndex: 'NAME',align:'center'},
				//{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "身份证号",dataIndex: 'ID_NO',align:'center'},
				{header: "创建日期",dataIndex: 'CREATE_DATE',align:'center'},
				{header: "职位", dataIndex: 'POSITION', align:'center',renderer:getItemValue},
				{header: "月份", dataIndex: 'MONTH', align:'center'},
				{header: "年份", dataIndex: 'YEAR', align:'center'},
				{header: "本月业绩积分", dataIndex: 'MONTH_PERFORMANCE_INTEG', align:'center'},
				{header: "本月认证积分", dataIndex: 'MONTH_AUTHENTICATION_INTEG', align:'center'},
				{header: "本月任职年限积分", dataIndex: 'MONTH_YEAR_INTEG', align:'center'},
				//{header: "本月综合评价积分", dataIndex: 'MONTH_INTEG', align:'center'},
				{header: "本月积分", dataIndex: 'MONTH_INTEG', align:'center'},
				//{header: "剩余积分", dataIndex: 'REMAIN_INTEG', align:'center'},
				{header: "在职状态", dataIndex: 'POSITION_STATUS', align:'center',renderer:getItemValue},
				{header: "连续三个月业绩为零", dataIndex: 'THREE_MONTH_ZERO', align:'center',renderer:getItemValue}
		      ];
	function executeQuery(){
		url= "<%=contextPath%>/sales/usermng/IntegationManage/integMonthSelect.json";
		__extQuery__(1);
	}
	function txtClr(value){
		$(value).value="";
	}
	function downLoad(){
		$("fm").action='<%=request.getContextPath()%>/sales/usermng/IntegationManage/integMonthDownLoad.json';
		$("fm").target="_self";
		$("fm").submit();
	
	}
</script>
</head>

<body onload="executeQuery();">
<div class="wbox">
<div class="navigation"><img src="/dms/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 积分管理 &gt; 月度积分查询</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="curPage" id="curPage" value="1" />
	<table class="table_query" border="0">
		<tr>
			<td align="right">人员姓名：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="name" id="name" value=""  />
			</td>
			<td align="right">身份证号：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="idNo" id="idNo" value=""  />	
			</td>
		</tr>
		
	  <tr>
	  <td align="right">职位状态：</td>
		  <td align="left">
		  	<script type="text/javascript">
	                genSelBoxExp("positionStatus",9994,"",true,"mini_sel","","true",'');
				</script> 
	       </td>
		<td align="right">月份：</td>
		  <td align="left">
		 		<select name="month">
		 			<option value="">-请选择-</option>
		 			<option value="1">1</option>
		 			<option value="2">2</option>
		 			<option value="3">3</option>
		 			<option value="4">4</option>
		 			<option value="5">5</option>
		 			<option value="6">6</option>
		 			<option value="7">7</option>
		 			<option value="8">8</option>
		 			<option value="9">9</option>
		 			<option value="10">10</option>
		 			<option value="11">11</option>
		 			<option value="12">12</option>
		 		</select>
	       </td>
	       
	  </tr>
	  <tr>
			<td width="20%" class="tblopt"><div align="right">选择经销商：</div></td>
				<td width="39%" >
      			  <input name="dealerCode" type="text" id="dealerCode" class="middle_txt" value="" size="20" />
                  <c:if test="${dutyType==10431001}">
                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer('dealerCode','','true', '${orgId}')" value="..." />
                  </c:if>
                  <c:if test="${dutyType==10431002}">
                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer3('dealerCode','','true', '${orgId}')" value="..." />
                  </c:if>
                  <c:if test="${dutyType==10431003}">
                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer3('dealerCode','','true', '${orgId}')" value="..." />
                  </c:if>
                  <c:if test="${dutyType==10431004}">
                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer6('dealerCode','','true', '${orgId}')" value="..." />
                  </c:if>                    
                    <input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn" />
    			</td>
    			<td align="right">职位：</td>
				  <td align="left">
				 		 <script type="text/javascript">
			                genSelBoxExp("position",9996,"",true,"mini_sel","","false",'');
			            </script> 
			       </td>
		</tr>
		<tr>
		 <td align="right">年份：</td>
	        <td align="left">
		  	<select name="year">
		  		<option value="">-请选择-</option>
		  		<c:forEach var="s" items="${dateList}">
		  			<option value="${s}">${s}</option>
		  		</c:forEach>
		  	</select>
	       </td>
	       <td align="right">&nbsp;</td>
	        <td align="left">&nbsp;</td>
		</tr>
		<tr>
	<tr>
		<td align="center" colspan="2">
			<input type="button" class="normal_btn" onclick="executeQuery();" value="查询" id="addSub" />
			<input type="button" class="normal_btn" onclick="downLoad();" value="下载" id="downLoads" />
			
		</td>
		<td align="right" colspan="2">
			<input name="pagesizes" id="pagesizes" value="10" datatype="0,is_digit,20" style="width:30px"/>
		</td>
	</tr>
	</table>
	<br />
	<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end --> 
	
	
</form>
</div>
</body>

</html>
