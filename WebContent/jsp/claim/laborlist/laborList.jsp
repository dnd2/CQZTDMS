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
<TITLE>售后服务管理</TITLE>

</HEAD>
<BODY>

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;
	应税劳务清单汇总报表参数设置</div>
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
			<td width="10%" align="right">生产厂商：</td>
			<td width="20%" align="left">
				<script type="text/javascript">
					genSelBoxExp("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'');
				</script>
			</td>
			<td width="10%" align="right">查询范围：</td>
			<td width="20%" align="left">
				<input type="text" class="short_txt" name="beginDate" id="beginDate"  datatype="1,is_date,10" group="beginDate,endDate" hasbtn="true" callFunction="showcalendar(event, 'beginDate', false);"/>
					至
				<input type="text" class="short_txt" name="endDate" id="endDate"  datatype="1,is_date,10" group="beginDate,endDate" hasbtn="true" callFunction="showcalendar(event, 'endDate', false);"/>
			</td>
		</tr>
		<tr>
			<td width="10%" align="right">单据状态：</td>
			<td width="20%" align="left">
				<script>
					genSelBoxExp("status","<%=Constant.LABOR_LIST_STATUS%>","",true,"min_sel","","false","");
				</script>
			</td>
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
var url = '<%=contextPath%>/claim/laborlist/LaborListAction/mainQuery.json' ;
var title = null ;
var columns = [
               {header:'序号',width:'6%',align:'center',renderer:getIndex},
               {header:'单据编码',dataIndex:'REPORT_CODE',width:'10%',align:'center'},
               {header:'制单单位',dataIndex:'DEALER_NAME',width:'20%',align:'center'},
               {header:'制单单位编码',dataIndex:'DEALER_CODE',width:'10%',align:'center'},
               {header:'生产厂商',dataIndex:'YIELDLY',width:'6%',align:'center',renderer:getItemValue},
               {header:'接收人',dataIndex:'AUTH_PERSON_NAME',width:'6%',align:'center'},
               {header:'接收时间',dataIndex:'AUTH_TIME',width:'6%',align:'center'},
               {header:'发票号',dataIndex:'INVOICE_CODE',width:'8%',align:'center'},
               {header:'总金额',dataIndex:'AMOUNT',width:'6%',align:'center'},
               {header:'状态',dataIndex:'STATUS',width:'6%',align:'center',renderer:getItemValue},
               {header:'操作',width:'6%',align:'center',renderer:myHandler}
               ];
function myHandler(value,meta,record){
	return '<a href="<%=contextPath%>/claim/laborlist/LaborListAction/showDetail.do?id='+record.data.REPORT_ID+'">[明细]</a>' ;
}
function goAdd(){
	location = '<%=contextPath%>/claim/laborlist/LaborListAction/addUrlInit.do' ;
}


</script>
</BODY>
</html>