<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="com.infodms.dms.po.TtAsActivityEvaluatePO"%>

<head>
<%
TtAsActivityEvaluatePO evaluatePO =(TtAsActivityEvaluatePO)request.getAttribute("evaluatePO");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动总结查询</title>
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
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动评估管理
	</div>
  <form method="post" name="fm" id="fm">
        <table  class="table_query">
          <tr>
             <td class="table_query_3Col_label_5Letter">主题编号：</td>
            <td align="left">
                   <input type="text"  name="subject_no"  id="subject_no"  class="middle_txt"  size="25" />
			</td>
			<td  class="table_query_3Col_label_5Letter">主题名称：</td>
	             <td> <input type="text"  name="subject_name"  id="subject_name"  class="middle_txt"  size="25" />
  		   </td>            
          </tr> 
          <tr>
             <td class="table_query_3Col_label_5Letter">活动类型：</td>
            <td align="left">
                  <script type="text/javascript">
					genSelBoxExp("activityType",<%=Constant.SERVICEACTIVITY_TYPE%>,"",true,"short_sel","","false",'<%=Constant.SERVICEACTIVITY_TYPE_01%>');
 				</script>
			</td>
			<td  class="table_query_3Col_label_5Letter">活动日期：</td>
	              <td align="left">
	              <input class="short_txt" id="t1" name="startdate" datatype="1,is_date,10"
                           maxlength="10" group="t1,t2"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 't1', false);" type="button"/>
                   	 至
                    <input class="short_txt" id="t2" name="enddate" datatype="1,is_date,10"
                           maxlength="10" group="t1,t2"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 't2', false);" type="button"/>
  		   </td>            
          </tr>                  
		  <tr>	
			  <td align="center" colspan="4">
	              <input class="normal_btn" type="button" name="button1" value="查询" onclick="__extQuery__(1);" />
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
	var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageSummarySearch/serviceActivityManageSummaryEvaluateQuery.json";
				
	var title = null;

	var columns = [
				{header: "主题编号 ", dataIndex: 'SUBJECT_NO', align:'center'},
				{header: "主题名称 ",dataIndex: 'SUBJECT_NAME' ,align:'center'},
				{header: "活动类型  ",dataIndex: 'ACTIVITY_TYPE' ,align:'center',renderer:getItemValue},
				{header: "单台活动次数  ",dataIndex: 'ACTIVITY_NUM' ,align:'center'},
				{header: "活动开始日期",dataIndex: 'SUBJECT_START_DATE' ,align:'center'},
				{header: "活动结束日期",dataIndex: 'SUBJECT_END_DATE' ,align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'SUBJECT_ID',renderer:mySelect ,align:'center'}
		      ];
	//明细的超链接设置
	function mySelect(value,meta,record){
  		return String.format(
         "<a href=\"#\" onclick='sel(\""+record.data.SUBJECT_ID+"\")'>[评估]</a>");
	}
	//详细页面
	function sel(value){
	     fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageSummarySearch/serviceActivityEvaluateQuery.do?subject_id="+value ;
		fm.submit();
	}
</script>
<!--页面列表 end -->
</body>
</html>