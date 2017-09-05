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
<title>人员关键岗位明细查询</title>
<script type="text/javascript">
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/usermng/UserManage/personDetailSelect.json";
	var title = null;
	var columns = [
				{header: "片区名称",dataIndex: 'PQ_ORG_NAME',align:'center'},
				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "姓名",dataIndex: 'NAME',align:'center'},
				{header: "性别",dataIndex: 'GENDER',align:'center'},
				{header: "身份证号",dataIndex: 'ID_NO',align:'center'},
				{header: "学历",dataIndex: 'DEGREE',align:'center'},
				{header: "联系电话", dataIndex: 'MOBILE', align:'center'},
				{header: "职位", dataIndex: 'POSITION', align:'center'},
				{header: "在职状态", dataIndex: 'POSITION_STATUS', align:'center'},
				{header: "所属银行", dataIndex: 'BANK', align:'center'},
				{header: "离职时间", dataIndex: 'LEAVE_DATE', align:'center'},
				{header: "最后审核时间", dataIndex: 'LAST_AUDIT_TIME', align:'center'}
				
		      ];
		      
	function executeQuery(){
		url= "<%=contextPath%>/sales/usermng/UserManage/personDetailSelect.json";
		__extQuery__(1);
	}
	function txtClr(value){
		$(value).value="";
	}
	function downLoad(){
		$("fm").action='<%=request.getContextPath()%>/sales/usermng/UserManage/personDetailDownLoad.json';
		$("fm").target="_self";
		$("fm").submit();
	
	}
</script>
</head>

<body onload="executeQuery(); loadcalendar();">
<div class="wbox">
<div class="navigation"><img src="/dms/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 人员管理 &gt; 人员关键岗位明细查询</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="curPage" id="curPage" value="1" />
	<table class="table_query" border="0">
		<tr>
			<td align="right">离职时间起：</td>
    			<td align="left"> <input name="leaveStartDate" type="text" id="leaveStartDate"  class="short_txt" readonly  

value='' />
      <input class="time_ico" type="button" onClick="showcalendar(event, 'leaveStartDate', false);" /></td>
			<td align="right">离职时间止：</td>
    			<td align="left"> <input name="leaveEndDate" type="text" id="leaveEndDate"  class="short_txt" readonly  

value='' />
      <input class="time_ico" type="button" onClick="showcalendar(event, 'leaveEndDate', false);" /></td>
		</tr>
	<tr>
			<td align="right">审核时间起：</td>
    			<td align="left"> <input name="auditStartDate" type="text" id="auditStartDate"  class="short_txt" readonly  

value=''  />
      <input class="time_ico" type="button" onClick="showcalendar(event, 'auditStartDate', false);" /></td>
			<td align="right">审核时间止：</td>
    			<td align="left"> <input name="auditEndDate" type="text" id="auditEndDate"  class="short_txt" readonly  

value=''  />
      <input class="time_ico" type="button" onClick="showcalendar(event, 'auditEndDate', false);" /></td>
		</tr>
		<tr>
			 <td align="right">职位：</td>
		  <td align="left">
		 		 <script type="text/javascript">
	                genSelBoxExp("position",9996,"",true,"mini_sel","","false",'');
	            </script> 
	       </td>
          <td align="right">学历：</td>
		  <td align="left"><script type="text/javascript">
	                genSelBoxExp("degree",2994,"",true,"mini_sel","","false",'');
	            </script>
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
    			<td align="right">职位状态：</td>
		  <td align="left"><script type="text/javascript">
	                genSelBoxExp("position_status",9994,"",true,"mini_sel","","false",'');
	            </script>
	       </td>
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
