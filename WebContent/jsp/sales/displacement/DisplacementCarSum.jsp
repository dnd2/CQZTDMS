<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript">
function doInit(){
   	loadcalendar();  //初始化时间控件
}
</script>
<TITLE></TITLE>

</HEAD>
<BODY>

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：客户信息管理&gt;总部汇总打印</div>
    <form method="post" name ="fm" id="fm">
    <table  class="table_query">
		<tr>
			<td width="10%" align="right">单据编码：</td>
			<td width="20%" align="left">
				<input type="text" class="middle_txt" name="reportCode"/>
			</td>
			<td width="10%" align="right">制单单位：</td>
			<td width="20%" align="left">
				<input type="text" class="middle_txt" name="dealerName"/>
			</td>
		</tr>
		<tr>
			<td width="10%" align="right">生产基地：</td>
			<td width="20%" align="left">
				<script type="text/javascript">
					genSelBoxExp("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'');
				</script>
			</td>
			<td width="10%" align="right"></td>
			<td width="20%" align="left"></td>
		</tr>
		<tr>
			<td width="10%" align="right"></td>
			<td width="20%" align="left"></td>
		</tr>
		<tr>
			<td colspan="4" align="center">
				<input type="button" value="查询" class="normal_btn" onclick="__extQuery__(1)"/>
				&nbsp;&nbsp;
				<input type="button" value="新增" class="normal_btn" onclick="goAdd();"/>
				&nbsp;&nbsp;
				<!-- <input type="button" value="联动" class="normal_btn" onclick="location='<%=contextPath%>/common/AreaProvinceDealerAction/mainUrlInit.do'"/> -->
			</td>
		</tr>
  </table>
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
<script type="text/javascript">
var url = '<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementDjQueryInit.json' ;
var title = null ;
var columns = [
               {header:'序号',width:'6%',align:'center',renderer:getIndex},
               {header:'单据编码',dataIndex:'DJ_CODE',width:'10%',align:'center'},
               {header:'制单单位',dataIndex:'ORDER_CREATE_NAME',width:'20%',align:'center'},
               {header:'生产基地',dataIndex:'ORDER_AREA',width:'6%',align:'center',renderer:getItemValue},
               {header:'操作',width:'6%',align:'center',renderer:myHandler}
               ];
function myHandler(value,meta,record){
	
	return String.format("<a href='#' onclick='infoquery("+record.data.DJ_ID+")'>[打印]</a>");
}
function infoquery (value1){
	window.open("<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementDjDetailInit.do?id="+value1);
}
function goAdd(){
	location = '<%=contextPath%>/sales/displacement/DisplacementCar/addUrlInit.do' ;
}
</script>
</BODY>
</html>