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
	   __extQuery__(1);
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
<div class="wbox">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动申请查询</div>
    <!-- 查询条件 begin -->
  <form method="post" name ="fm" id="fm">
  <input type="hidden" name="typeleng" value="${typeleng}">
  <div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
    <table class="table_query">
	           <tr>
	           <input type="hidden" id="activityId" name="activityId" value="<%=request.getAttribute("activityId")%>" />
	              <td style="text-align:right">申请单号： </td>
		              <td>
			             <input name="applyNo" id="applyNo" value="" type="text" class="middle_txt"  />
	            	  </td>
		           	<td  style="text-align:right">单据新增时间：</td>
					<td>
					<input id="addStart" name="addStart" style="width:80px;"  class="middle_txt" type="text" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'addEnd\')}'})"/>
					<!-- <input name="addStart" type="text" group="dealStart,dealEnd" datatype="1,is_date,10" class="short_time_txt" id="dealStart" /> 
					<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'dealStart', false);" />  	 -->
		             &nbsp;至&nbsp;
		             <input id="addEnd"  name="addEnd" style="width:80px;" class="middle_txt" type="text" onclick="WdatePicker({minDate:'#F{$dp.$D(\'addStart\')}'})"/>
		            <!-- <input name="addEnd" type="text" group="dealStart,dealEnd" datatype="1,is_date,10" class="short_time_txt" id="dealEnd" /> 
					<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'dealEnd', false);" />  -->
				</td>	
				<td  style="text-align:right">状态：</td>
					<td align="left"  >
					 	 <script type="text/javascript">
		   					   genSelBoxExp("status",<%=Constant.SERVICEACTIVITYAPPLY_STATUS%>,"",true,"","","false",'');
		  			  </script>
					</td>
	            </tr>
                <tr>
				  <td colspan="6"  style="text-align:center">
	                <input class="normal_btn" type="button" value="查 询" name="queryBtn" id="queryBtn" onclick="__extQuery__(1);" />
					&nbsp;
          			<input id="addBtn" name="addBtn" type="button" value="新 增" class="normal_btn" onclick="addComplaint();" />
	              </td>
		       </tr>
  </table>
  </div>
  </div>
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
	var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityApply/serviceActivityApplyQuery.json";
				
	var title = null;

	var columns = [
				{header:'序号', renderer:getIndex,align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'},
	            {header: "申请单号", dataIndex: 'APPLY_NO', align:'center'},
				{header: "申请时间",dataIndex: 'CRDATE' ,align:'center'},
				{header: "服务站代码",dataIndex: 'DEALER_CODE' ,align:'center'},
				{header: "服务站名称",dataIndex: 'DEALER_NAME' ,align:'center'},
				{header: '状态',dataIndex:'STATUS',align:'center',renderer:getItemValue}
		      ];
	//超链接设置
	function myLink(value,meta,record){
		/* var str1="<input name='detailBtn' type='button' class='middle_btn' onclick='activitySearch(\""+ record.data.ID +"\")' value='查看'/>&nbsp;";
		var str2="<input name='detailBtn' type='button' class='middle_btn' onclick='subcommit(\""+ record.data.ID  +"\")' value='上报'/>&nbsp;";
		var str3="<input name='detailBtn' type='button' class='middle_btn' onclick='updateActivity(\""+ record.data.ID +"\")' value='修改'/>&nbsp;";
		var str4="<input name='detailBtn' type='button' class='middle_btn' onclick='subChecked(\""+ record.data.ID  +"\")' value='删除'/>&nbsp;"; */
		var str1="<a href=\"#\" onclick='activitySearch(\""+ record.data.ID +"\")'>[查看]</a>";
		var str2="<a href=\"#\" onclick='subcommit(\""+ record.data.ID  +"\")'>[上报]</a>";
		var str3="<a href=\"#\" onclick='updateActivity(\""+ record.data.ID  +"\")'>[修改]</a>";
		var str4="<a href=\"#\" onclick='subChecked(\""+ record.data.ID  +"\")'>[删除]</a>";
		var str=str1;
		if(record.data.STATUS == <%=Constant.SERVICEACTIVITYAPPLY_STATUS_01%>){
			str+=str2+str3+str4;
		}
		if(record.data.STATUS == <%=Constant.SERVICEACTIVITYAPPLY_STATUS_03%>){
			str+=str2+str3;
		}
		return String.format(str);
	
	}
	
	//查询
	function activitySearch(str){	
		var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityApply/serviceActivityApplySearchInit.do?activityId="+str;
		OpenHtmlWindow(url, 900, 500, '服务活动查看');
		<%-- window.location.href='<%=contextPath%>/claim/serviceActivity/ServiceActivityApply/serviceActivityApplySearchInit.do?activityId='+str; --%>
	}
	//修改
	function updateActivity(str){
		var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityApply/serviceActivityApplyUpdateInit.do?activityId="+str;
		OpenHtmlWindow(url, 900, 500, '服务活动修改');
		<%-- fm.action = '<%=contextPath%>/claim/serviceActivity/ServiceActivityApply/serviceActivityApplyUpdateInit.do?activityId='+str;
		fm.submit(); --%>
	}
	//新增
	function addComplaint(){
		var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityApply/serviceActivityApplyAddInit.do";
		OpenHtmlWindow(url, 900, 500, '服务活动申请');
		
		<%-- fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityApply/serviceActivityApplyAddInit.do";
		fm.submit(); --%>
	}
	
	//删除
	function subChecked(str) {
		     MyConfirm("是否确认删除？",serviceActivityManageDelete,[str]);
		} 
	//上报
	function subcommit(str) {
		     MyConfirm("是否确认上报？",serviceActivityManagecom,[str]);
		}
		
	function serviceActivityManagecom(str){
		makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityApply/reportServiceActivity.json?activityId='+str,returnBackcom,'fm','queryBtn');
	}
	//删除开始
	function serviceActivityManageDelete(str){
		makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityApply/deleteServiceActivity.json?activityId='+str,returnBack,'fm','queryBtn');
	}
	//删除回调函数
	function returnBack(json){
		if(json.isSuccess != null && json.isSuccess=='0'){
			__extQuery__(1);
			MyAlert("删除成功！");
		}else{
			MyAlert("删除失败！请联系管理员！");
		}
	}
	function returnBackcom(json){
		if(json.isSuccess != null && json.isSuccess=='0'){
			__extQuery__(1);
			MyAlert("上报成功！");
		}else{
			MyAlert("上报失败！请联系管理员！");
		}
	}
</script>
</div>
<!--页面列表 end -->
</body>
</html>