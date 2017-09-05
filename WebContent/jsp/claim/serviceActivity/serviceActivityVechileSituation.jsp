<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动及车辆情况查询</title>
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
			<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动及车辆情况查询
	</div>
	<form method="post" name="fm" id="fm" >
        <table  class="table_query">
            <tr>
	              <td class="table_query_3Col_label_5Letter" >活动编号：</td>
	              <td align="left" >
	                  <input name="activityCode" type="text" id="activityCode" class="middle_txt" size="12" datatype="1,is_digit_letter,25"/>
	              </td>
	             <td class="table_query_3Col_label_5Letter" >活动名称：</td>
	             <td align="left">
	             	<input type="text" name="activityName" class="middle_txt"/>
	             </td>
          </tr>                  
          <tr>
	            <td  class="table_query_2Col_label_5Letter" >活动日期： </td>
	            <td align="left">
		               <div align="left">
				            	<input name="startdate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
		                       至  <input name="enddate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
		               </div>
	            </td>
	             <td  class="table_query_3Col_label_5Letter" >活动状态：</td>
	              <td align="left">
		              <script type="text/javascript">
		   					    genSelBoxExp("status",<%=Constant.SERVICEACTIVITY_STATUS%>,"",true,"short_sel","","true",'');
		  			  </script>
	              </td>
          </tr>
		  <tr>
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
	var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityVechileSituation/serviceActivityVechileSituationQuery.json";
				
	var title = null;

	var columns = [
				{header: "活动编号", dataIndex: 'ACTIVITY_CODE', align:'center'},
				{header: "活动名称",dataIndex: 'ACTIVITY_NAME' ,align:'center'},
				{header: "活动开始日期 ",dataIndex: 'STARTDATE' ,align:'center'},
				{header: "活动结束日期",dataIndex: 'ENDDATE' ,align:'center'},
				{header: "车辆数",dataIndex: 'CAR_NUM' ,align:'center'},
				{header: "完成数",dataIndex: 'NUM' ,align:'center'},
				{header: "活动状态",dataIndex: 'STATUS' ,align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ACTIVITY_ID',renderer:serviceActivityManageOptionInit ,align:'center'}
		      ];
	//功能：明细页面查询
	//描述：1.根据活动ID查询活动具体信息；2.与服务活动计划下发共用一个方法和页面
	function serviceActivityManageOptionInit(value,meta,record){
	    return String.format(// /claim/serviceActivity/ServiceActivityManageIssued/serviceActivityManageIssuedInfo.do
         "<a href=\"<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/getActivityIdInfo.do?activityId="+ value + "\">[明细]</a>");
	}
</script>
<!--页面列表 end -->
</body>
</html>