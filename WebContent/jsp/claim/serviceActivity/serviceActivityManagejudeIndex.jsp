<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动情况查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
//日历控件初始化
function doInit()
	{
	   loadcalendar();
	}
//修改返回---查询出修改记录并返回主页面
function refreshOnload(){
        var flag='<%=request.getAttribute("flag")%>';
	    if("onFlag"==flag){
	        window.onload=__extQuery__(1);
	    }
}
</script>
</head>

<body onload="refreshOnload();">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置： 售后服务管理&gt;服务活动情况查询</div>
    <!-- 查询条件 begin -->
  <form method="post" name ="fm" id="fm">
    <table class="table_query">
	           <tr>
	           <input type="hidden" id="activityId" name="activityId" value="<%=request.getAttribute("activityId")%>" />
	              <td width="10%" align="right" >活动编号： </td>
		              <td width="20%" >
			             	 <input name="activityCode" id="activityCode" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,25" />
	            	  </td>
		              <td width="10%" align="right">活动主題：</td>
		              <td width="20%">
			              <select name="subjectId">
			              <option value="">-请选择-</option>
			              <c:forEach var="ttAsActivitySubjectPO" items="${ttAsActivitySubjectPO}" >
        					<option value="${ttAsActivitySubjectPO.subjectId}" >
          					<c:out value="${ttAsActivitySubjectPO.subjectName}"/>
          					</option>
          				  </c:forEach>
			              </select>
	  				</td>
	            </tr>
            	<tr>
            		<td width="10%" align="right">活动名称：</td>
            		<td>
            			<input type="text" class="middle_txt" name="activity_name"/>
            		</td>
					<td width="10%" align="right">活动状态：</td>
					<td align="left"  >
					 	 <script type="text/javascript">
		   					   genSelBoxExp("activityType",<%=Constant.SERVICEACTIVITY_STATUS%>,"",true,"short_sel","","false",'');
		  			  </script>
					</td>
				</tr>
				<tr>
					<td width="10%" align="right">活动日期： </td>
			        <td colspan="3"><div align="left">
			          <input id="checkSDate" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
			          <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
			          &nbsp;至&nbsp;
			          <input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
			          <input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
			        </div></td>
				</tr>
                <tr>
				  <td colspan="11" align="center">
	                  <input class="normal_btn" type="button" name="button" value="查询"  onclick="__extQuery__(1);">
	              </td>
		       </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form> 
<!-- 查询条件 end -->
<br/>
<!--页面列表 begin -->
<script type="text/javascript" >

	var myPage;
//查询路径
	var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/serviceActivityManageroleQuery.json";
				
	var title = null;

	var columns = [
	               {header:'序号',renderer:getIndex,align:'center'},
				{header: "活动编号", dataIndex: 'ACTIVITYCODE', align:'center'},
				{header: "活动名称", dataIndex: 'ACTIVITYNAME', align:'center'},
				{header: "活动开始日期",dataIndex: 'STARTDATE' ,align:'center'},
				{header: "活动开始日期",dataIndex: 'ENDDATE' ,align:'center'},
				{header: "车辆数",dataIndex: 'VEHICLE_NUM' ,align:'center'},
				{header: '完成数',dataIndex:'FSUMCAR',align:'center'},
				{header: '活动状态',dataIndex:'STATUS',align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ACTIVITYID',renderer:serviceActivityManageUpdateInit ,align:'center'}
		      ];
	//修改/删除/明细的超链接设置
	function serviceActivityManageUpdateInit(value,meta,record){
		var value1 = record.data.ACTIVITYID ;
	    return String.format(
         "<a href=\"<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/getActivityIdInfo.do?activityId="+ value1 + "\">[明细]</a>");
	}
	//新增服务活动管理信息
	function serviceActivityManageAddInit(){
		fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/serviceActivityManageAddInit.do";
		fm.submit();
	}
	//删除服务活动管理信息
	function subChecked(str) {
		     MyConfirm("是否确认删除？",serviceActivityManageDelete,[str]);
		} 
		function subcommit(str) {
		     MyConfirm("是否确认提交？",serviceActivityManagecom,[str]);
		} 
		function serviceActivityManagecom(str){
	makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/serviceActivitycommit.json?activityId='+str,returnBackcom,'fm','queryBtn');
	}
	//删除开始
	function serviceActivityManageDelete(str){
	makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/serviceActivityManageDelete.json?activityId='+str,returnBack,'fm','queryBtn');
	}
	//删除回调函数
	function returnBack(json){
		var del = json.returnValue;
		if(del==1){
			__extQuery__(1);
			MyAlert("删除成功！");
		}else{
			MyAlert("删除失败！请联系管理员！");
		}
	}
	function returnBackcom(json){
		var del = json.returnValue;
		if(del==1){
			__extQuery__(1);
			MyAlert("提交成功！");
		}else{
			MyAlert("提交失败！请联系管理员！");
		}
	}
	//删除结束
</script>
<!--页面列表 end -->
</body>
</html>