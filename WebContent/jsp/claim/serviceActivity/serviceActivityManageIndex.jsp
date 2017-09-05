<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动管理</title>
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
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动管理</div>
    <!-- 查询条件 begin -->
  <form method="post" name ="fm" id="fm">
  <input type="hidden" name="typeleng" value="${typeleng}">
    <table class="table_query">
	           <tr>
	           <input type="hidden" id="activityId" name="activityId" value="<%=request.getAttribute("activityId")%>" />
	              <td width="10%" align="right" >活动编号： </td>
		              <td width="20%" >
			             	 <input name="activityCode" id="activityCode" value="" type="text" class="middle_txt"  />
	            	  </td>
		              <td width="10%" align="right">活动主題：</td>
		              
		              <td width="30%" >
						<input type="text" readonly="readonly" name="subjectName" id="subjectName" class="long_txt"/>
						<input type="hidden" name="subjectId" id="subjectId"/>
						<input type="button" class="mini_btn" value="..." onclick="showsubjectId('subjectName','subjectId');"/>
		            	<input type="button" class="normal_btn" value="清除" onclick="wrapOut2();"/>
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
				  <td colspan="11" align="center">
	                  <input class="normal_btn" type="button" name="button" value="查询"  onclick="__extQuery__(1);">
				      <input class="normal_btn" type="button" name="button1" value="新增" onclick="serviceActivityManageAddInit();"/>
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
	var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/serviceActivityManageQuery.json";
				
	var title = null;

	var columns = [
	               {header:'序号',renderer:getIndex,align:'center'},
				{header: "活动编号", dataIndex: 'ACTIVITY_CODE', align:'center'},
				{header: "活动名称",dataIndex: 'ACTIVITY_NAME' ,align:'center'},
				{header: "主题编号",dataIndex: 'SUBJECT_NO' ,align:'center'},
				{header: "主题名称",dataIndex: 'SUBJECT_NAME' ,align:'center'},
				{header: '活动状态',dataIndex:'STATUS',align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ACTIVITY_ID',renderer:serviceActivityManageUpdateInit ,align:'center'}
		      ];
	//修改/删除/明细的超链接设置
	function serviceActivityManageUpdateInit(value,meta,record){
		var value1 = record.data.ACTIVITY_ID ;
		var type = record.data.STATUS;
		if(type == '<%=Constant.SERVICEACTIVITY_STATUS_02%>')
		{
			return String.format(
            "<a href=\"<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/getActivityIdInfo.do?activityId="+ value1 + "\">[明细]</a> <a href=\"<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/getActivityIdInfoAdd.do?activityId="+ value1 + "\">[提报新增] </a><a href=\"<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/activityIdInfoModifyPage.do?activityId="+ value1 + "\">[修改]</a>");
		}else
		{
			return String.format(
         "<a href=\"<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/serviceActivityManageUpdateInit.do?activityId="+ value1 + "&typeleng=${typeleng}\">[修改]</a>"+"<a href=\"#\" onclick=\"subChecked("+value1+")\">[删除]</a>"+"<a href=\"<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/getActivityIdInfo.do?activityId="+ value1 + "\">[明细]</a>"+"<a href=\"#\" onclick=\"subcommit("+value1+")\">[提交]</a>");
		}
	    
	}
	//新增服务活动管理信息
	
	function wrapOut2()
	{
	  document.getElementById('subjectName').value = '';
	  document.getElementById('subjectId').value = '';
	}
	
	function showsubjectId(subjectName,subjectId)
	{
		OpenHtmlWindow('<%=contextPath%>/dialog/subjectName.jsp?subjectName='+subjectName+"&subjectId="+subjectId+"&type="+${typeleng},800,460);
	}
	
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