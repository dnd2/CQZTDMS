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
<TITLE>考试管理</TITLE>

</HEAD>
<BODY>

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：信息反馈管理&gt;考试信息管理&gt;考试信息维护</div>
    <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
          <tr>
			<td width="10%" height="25" align="right">考试代码：</td>
			<td>
				<input type="text" class="middle_txt" name="examCode"/>
			</td>
			<td width="10%" align="right">考试主题：</td>
			<td>
				<input type="text" class="middle_txt" name="examName"/>
			</td>
		</tr>
		<tr>
			<td width="10%" height="25" align="right">考试时间：</td>
			<td colspan="3">
				<input type="text" class="short_txt" name="beginDate" id="beginDate"  datatype="1,is_date,10" group="beginDate,endDate" hasbtn="true" callFunction="showcalendar(event, 'beginDate', false);"/>
            至
            <input type="text" class="short_txt" name="endDate" id="endDate"  datatype="1,is_date,10" group="beginDate,endDate" hasbtn="true" callFunction="showcalendar(event, 'endDate', false);"/>
  			</td>
		</tr>
		<tr>
			<td width="10%" height="25" align="right">考试内容：</td>
			<td>
				<textarea name="examRemark" cols="50" rows="1"></textarea>
			</td>
		</tr>
		<tr>
			<td colspan="4" align="center">
				<input type="button" value="查询" class="normal_btn" onclick="__extQuery__(1)"/>
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
var url = '<%=contextPath%>/feedbackmng/exam/ExamDlrAction/mainQuery.json' ;
var title = null ;
var columns = [
               {header:'序号',width:'6%',align:'center',renderer:getIndex},
               {header:'考试代码',dataIndex:'EXAM_CODE',width:'6%',align:'center'},
               {header:'考试主题',dataIndex:'EXAM_NAME',width:'10%',align:'center'},
               {header:'考试内容',dataIndex:'EXAM_REMARK',width:'12%',align:'center'},
               {header:'开始时间',dataIndex:'EXAM_START_TIME',width:'8%',align:'center'},
               {header:'结束时间',dataIndex:'EXAM_END_TIME',width:'8%',align:'center'},
               {header:'状态',dataIndex:'EXAM_STATUS',width:'6%',align:'center',renderer:getItemValue},
               {header:'操作',width:'6%',align:'center',renderer:myHandler}
               ];
function myHandler(value,meta,record){
	var examId = record.data.EXAM_ID ;
	var status = record.data.EXAM_STATUS ;
	if('<%=Constant.STATUS_DISABLE%>'==status)
		return "<a disabled=\"disabled\" href='#'>[答题]</a>" ;
	else if(record.data.STATUS == 1)
		return "<a disabled=\"disabled\" href='#'>[已答]</a>" ;
	else
		return "<a href='<%=contextPath%>/feedbackmng/exam/ExamDlrAction/queryExamInfo.do?id="+examId+"'>[答题]</a>" ;
}
</script>
</BODY>
</html>