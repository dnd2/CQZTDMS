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

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：信息反馈管理&gt;考试信息管理&gt;考试信息管理</div>
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
  			<td width="10%" align="right">经销商代码：</td>
  			<td>
  				<textarea cols="30" rows="2" id="dealers"></textarea>
  				<input type="hidden" name="dealerIds" id="dealerIds"/>
  				<input type="button" value=" ... " onclick="showOrgDealer('dealers','dealerIds','true','',true)"  class="mini_btn"/>&nbsp;
  				<input type="button" value="清除" onclick="clearDlrs();" class="normal_btn"/>
  			</td>
			<td width="10%" height="25" align="right">考试时间：</td>
			<td>
				<input type="text" class="short_txt" name="beginDate" id="beginDate"  datatype="1,is_date,10" group="beginDate,endDate" hasbtn="true" callFunction="showcalendar(event, 'beginDate', false);"/>
            至
            <input type="text" class="short_txt" name="endDate" id="endDate"  datatype="1,is_date,10" group="beginDate,endDate" hasbtn="true" callFunction="showcalendar(event, 'endDate', false);"/>
  			</td>
		</tr>
		<tr>
			<td width="10%" height="25" align="right">考试内容：</td>
			<td colspan="3">
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
var url = '<%=contextPath%>/feedbackmng/exam/ExamOemAction/examMngQuery.json' ;
var title = null ;
var columns = [
               {header:'序号',width:'6%',align:'center',renderer:getIndex},
               {header:'考试代码',dataIndex:'EXAM_CODE',width:'6%',align:'center'},
               {header:'考试主题',dataIndex:'EXAM_NAME',width:'10%',align:'center'},
               {header:'经销商代码',dataIndex:'DEALER_CODE',width:'8%',align:'center'},
               {header:'经销商名称',dataIndex:'DEALER_NAME',width:'12%',align:'center'},
               {header:'提交时间',dataIndex:'SUBMIT_TIME',width:'8%',align:'center'},
               {header:'考试结果',dataIndex:'EXAM_RESULT',width:'6%',align:'center'},
               {header:'状态',dataIndex:'STATUS',width:'6%',align:'center',renderer:getItemValue},
               {header:'操作',width:'6%',align:'center',renderer:myHandler}
               ];
function myHandler(value,meta,record){
	var dlrId=record.data.EXAM_DLR_ID ;
	return "<a href='<%=contextPath%>/feedbackmng/exam/ExamOemAction/queryAnswerInfo.do?dlrId="+dlrId+"'>[收卷]</a>" ;
}
function clearDlrs(){
	$('dealers').value = '' ;
	var arr = document.getElementsByName('dealerIds');
	arr[0].value = '' ;
}
</script>
</BODY>
</html>