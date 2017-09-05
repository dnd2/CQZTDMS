<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动发布范围管理</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
//日历控件初始化
function doInit()
	{
	   loadcalendar();
	}
</script>
</head>

<body>	
<form name="fm" id="fm">
	<div class="navigation">
			<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动经销商管理
	</div>
	<table class="table_query">
	        <tr>
                  <td class="table_query_2Col_label_5Letter" >活动编号： </td>
	              <td align="left">
		             	<input name="activityCode" id="activityCode" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,18" />
            	  </td>
	              <td class="table_query_2Col_label_5Letter" >活动日期：</td>
	              <td align="left">
	                   <div align="left">
	                   	<input class="short_txt" id="t1" name="startDate" datatype="1,is_date,10"
                           maxlength="10" group="t1,t2"/>
                    	<input class="time_ico" value=" " onclick="showcalendar(event, 't1', false);" type="button"/>
                   		 至
                   		 <input class="short_txt" id="t2" name="endDate" datatype="1,is_date,10"
                           maxlength="10" group="t1,t2"/>
                    		<input class="time_ico" value=" " onclick="showcalendar(event, 't2', false);" type="button"/>
            			</div>	
  				  </td>
            </tr>
         <tr>
           <td colspan="11" align="center">
                <input class="normal_btn" type="button" name="button" value="查询"  onclick="__extQuery__(1);"/>
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
	var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageDealer/serviceActivityManageDealerQuery.json";
				
	var title = null;

	var columns = [
				{header: "活动编号", dataIndex: 'ACTIVITY_CODE', align:'center'},
				{header: "活动名称",dataIndex: 'ACTIVITY_NAME' ,align:'center'},
				{header: "活动开始日期 ",dataIndex: 'STARTDATE' ,align:'center'},
				{header: "活动结束日期",dataIndex: 'ENDDATE' ,align:'center'},
				{header: "经销商数量",dataIndex: 'NUM' ,align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ACTIVITY_ID',renderer:serviceActivityManageOptionInit ,align:'center'}
		      ];
	//维护的超链接设置
	function serviceActivityManageOptionInit(value,meta,record){
	    return String.format(
         "<a href=\"<%=contextPath%>/claim/serviceActivity/ServiceActivityManageDealer/serviceActivityManageDealerMaintionQuery.do?activityId="+ value + "\">[维护]</a>");
	}
</script>
<!--页面列表 end -->
</body>
</html>