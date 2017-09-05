<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动评估</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
function doInit()
	{
	   loadcalendar();
	}
</script>
</head>

<body>
	<div class="navigation">
			<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动评估
	</div>
  <form method="post" name ="fm" id="fm">
        <table class="table_query">
          <TR>
        <TD width="10%" align=right>主题编号： </TD>
        <TD width="20%"><INPUT id=activityCode class=middle_txt name=activityCode datatype="1,is_digit_letter,25"></TD>
        <td width="10%" align="right">活动类型：</td>
        <td width="20%">
         <script type="text/javascript">
		genSelBoxExp("activityType",<%=Constant.SERVICEACTIVITY_TYPE%>,"",true,"short_sel","","false",'<%=Constant.SERVICEACTIVITY_TYPE_01%>');
 		</script>
		</td>
      </TR>
      <TR>
        <TD width="10%" align=right>主题名称：</TD>
        <TD><INPUT class=middle_txt name=activity_name></TD>
        <TD width="10%" align=right>活动日期：</TD>
        <TD align=left><DIV align=left>
            <input id="checkSDate" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
            <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
            &nbsp;至&nbsp;
            <input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
            <input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
          </DIV></TD>
      </TR>
		         <td align="center" colspan="4">
                       <input class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1);">
                 </td>
		</tr>
        </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
 <br/>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
//查询路径
	var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageSummary/ServiceActivityManageMotiveQuery.json";
				
	var title = null;

	var columns = [
				  {header:'序号',renderer:getIndex,align:'center'},
				{header: "主题编号", dataIndex: 'SUBJECT_NO', align:'center'},
				{header: "主题名称",dataIndex: 'SUBJECT_NAME' ,align:'center'},
				{header: "活动类型 ",dataIndex: 'ACTIVITY_TYPE' ,align:'center',renderer:getItemValue},
				{header: "单台次活动次数",dataIndex: 'ACTIVITY_NUM' ,align:'center'},
				{header: "活动开始日期",dataIndex: 'SUBJECT_START_DATE' ,align:'center',renderer:getValue},
				{header: "活动结束日期",dataIndex: 'SUBJECT_END_DATE' ,align:'center',renderer:getValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'SUBJECT_ID',renderer:serviceActivitySummaryQuery ,align:'center'}
		      ];
	//编辑的超链接设置
	function getValue(str)
	{
		var type = str.split(' ');
		return type[0];
	}
	function serviceActivitySummaryQuery(value,meta,record){
	    return String.format(
         "<a href=\"<%=contextPath%>/claim/serviceActivity/ServiceActivityManageSummary/serviceActivitySummaryQuery.do?subjectid="+ value + "\">[编辑]</a>");
	}
</script>
<!--页面列表 end -->
</body>
</html>