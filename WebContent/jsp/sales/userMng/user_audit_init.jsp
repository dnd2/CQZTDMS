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
<title>人员审核查询</title>
<script type="text/javascript">
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/usermng/UserManage/userSelect.json";
	var title = null;
	var columns = [
				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "姓名",dataIndex: 'NAME',align:'center'},
				{header: "性别",dataIndex: 'GENDER',align:'center',renderer:getItemValue},
				{header: "身份证号",dataIndex: 'ID_NO',align:'center'},
				{header: "电子邮件",dataIndex: 'EMAIL',align:'center'},
				{header: "联系电话", dataIndex: 'MOBILE', align:'center'},
				{header: "职位", dataIndex: 'POSITION', align:'center',renderer:getItemValue},
				{header: "入职日期", dataIndex: 'ENTRY_DATE', align:'center'},
				{header: "是否投资人", dataIndex: 'IS_INVESTOR', align:'center',renderer:getItemValue},
				{header: "所属银行", dataIndex: 'BANK', align:'center',renderer:getItemValue},
				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				//{header: "备注", dataIndex: 'REMARK', align:'center'},
				{header: "操作", dataIndex: 'REGIST_ID', align:'center',renderer:myLink}
		      ];
	function myLink(value,meta,record){
		return String.format("<a href='#' onclick='auditInit("+record.data.REGIST_ID+")'>[审核]</a>");
	}
	function auditInit(regist_id){
		location.href="<%=contextPath%>/sales/usermng/UserManage/userAuditLoad.do?registId="+regist_id;
	}
	function executeQuery(){
		url= "<%=contextPath%>/sales/usermng/UserManage/userSelect.json";
		__extQuery__(1);
	}
</script>
</head>

<body onload="executeQuery();">
<div class="wbox">
<div class="navigation"><img src="/dms/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 人员管理 &gt; 人员审核查询</div>
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
				<input type="text" class="middle_txt" name="idNO" id="idNO" value=""  />	
			</td>
		</tr>
		<tr>
          <td align="right">电子邮件：</td>
		  <td align="left">
		  	<input type="text" class="middle_txt" name="email" id="email" value=""  />
		  </td>
		  <td align="right">联系电话：</td>
		  <td align="left">
		  	<input type="text" class="middle_txt" name="mobile" id="mobile"  />
		  </td>
	  </tr>
		<tr>
			<td align="right">性别：</td>
			<td align="left">
				<script type="text/javascript">
	                genSelBoxExp("gender",1003,"",true,"mini_sel","","false",'');
	            </script>
	         </td>
	          <td align="right">是否投资人：</td>
			  <td align="left"><script type="text/javascript">
		                genSelBoxExp("isInvestor",9995,"",true,"mini_sel","","false",'');
		            </script>
		      </td>
		</tr>
		<tr>
			 <td align="right">职位：</td>
		  <td align="left">
		 		 <script type="text/javascript">
	                genSelBoxExp("position",9996,"",true,"mini_sel","","false",'');
	            </script> 
	       </td>
         <td align="right"></td>
		  <td align="left" style="display:none;">
		 		<select name="status" >
					<option value="99991002" >已提报</option>
				</select>
	       </td>
	       
	  </tr>
	  <tr>
		<td align="center" colspan="4">
			<input type="button" class="normal_btn" onclick="executeQuery();" value="查询" id="addSub" />
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
