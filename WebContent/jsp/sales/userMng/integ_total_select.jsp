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
<title>综合评价积分查询</title>
<script type="text/javascript">
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/usermng/IntegationManage/integTotalSelect.json";
	var title = null;
	var columns = [
				{header: "姓名",dataIndex: 'NAME',align:'center'},
				{header: "身份证号",dataIndex: 'ID_NO',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "创建日期", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "获得积分", dataIndex: 'GET_INTEG', align:'center'},
				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "备注", dataIndex: 'REMARK', align:'center'}
		      ];
	function executeQuery(){
		url= "<%=contextPath%>/sales/usermng/IntegationManage/integTotalSelect.json";
		__extQuery__(1);
	}
</script>
</head>

<body onload="executeQuery();">
<div class="wbox">
<div class="navigation"><img src="/dms/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 积分管理 &gt; 综合评价积分查询</div>
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
		<td align="right">状态：</td>
		  <td align="left">
		 		<script type="text/javascript">
	                genSelBoxExp("status",9999,"",true,"mini_sel","","true",'');
				</script> 
	       </td>
          <td align="right"></td>
		  <td align="left">
	       </td>
	       
	  </tr>
	  <tr>
		<td align="center" colspan="4">
			<input type="button" class="normal_btn" onclick="executeQuery();" value="查询" id="addSub" />
			<input type="button" class="normal_btn" onclick="javascript:history.back() ;" value="返 回" id="retBtn" />
		</td>
	  </tr>
	</table>
	<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end --> 
	
</form>
</div>
</body>

</html>
