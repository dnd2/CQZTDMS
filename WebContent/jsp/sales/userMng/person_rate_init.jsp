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
<title>人员流失率查询</title>
<script type="text/javascript">
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/usermng/UserManageReport/personRateSelect.json";
	var title = null;
	var columns = [
				{header: "片区名称",dataIndex: 'PQ_ORG_NAME',align:'center'},
				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "在职人数",dataIndex: 'ZZ',align:'center'},
				{header: "离职人数",dataIndex: 'LZ',align:'center'},
				{header: "流失率",dataIndex: 'RATE',align:'center'},
				{header: "总经理",dataIndex: 'Z_JL',align:'center'},
				{header: "销售经理", dataIndex: 'XS_JL', align:'center'},
				{header: "市场经理", dataIndex: 'XS_JL', align:'center'},
				{header: "销售顾问", dataIndex: 'XS_GW', align:'center'}
		      ];
		      
	function executeQuery(){
		url= "<%=contextPath%>/sales/usermng/UserManageReport/personRateSelect.json";
		__extQuery__(1);
	}
	function txtClr(value){
		$(value).value="";
	}
	function downLoad(){
		$("fm").action='<%=request.getContextPath()%>/sales/usermng/UserManageReport/personRateDownLoad.json';
		$("fm").target="_self";
		$("fm").submit();
	
	}
</script>
</head>

<body onload="executeQuery(); loadcalendar();">
<div class="wbox">
<div class="navigation"><img src="/dms/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 人员管理 &gt; 人员流失率查询</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="curPage" id="curPage" value="1" />
	<table class="table_query" border="0">
		<tr>
			<td align="right">时间起：</td>
    			<td align="left"> <input name="leaveStartDate" type="text" id="leaveStartDate"  class="short_txt" readonly  

value='' />
      <input class="time_ico" type="button" onClick="showcalendar(event, 'leaveStartDate', false);" /></td>
			<td align="right">时间止：</td>
    			<td align="left"> <input name="leaveEndDate" type="text" id="leaveEndDate"  class="short_txt" readonly  

value='' />
      <input class="time_ico" type="button" onClick="showcalendar(event, 'leaveEndDate', false);" /></td>
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
    			<td align="right"></td>
		 		<td align="left"></td>
	  </tr>
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
