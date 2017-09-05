<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>

<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网络专员行程日志</title>
<script type="text/javascript">
function doInit(){
   __extQuery__(1);
   loadcalendar(); 
}   
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：行程管理 > 网络专员行程日志 >网络专员行程日志</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align=center width="95%">
	 <tr>
	    <td width="37%" align="right" nowrap>   </td>
	    <td width="43%" class="table_query_2Col_input" nowrap>
	        <span id="data_start" class="innerHTMLStrong"></span>
			<span id="data_end"   class="innerHTMLStrong"></span>
        </td>
	    <td width="20%" align=left nowrap>&nbsp;</td>
    </tr> 
	<tr>
		<td align="right">查询日期：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="startDate" name="startDate" group="startDate,endDate" datatype="1,is_date,10" value="${today}"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value="&nbsp;" />至
				<input class="short_txt"  type="text" id="endDate" name="endDate" group="startDate,endDate" datatype="1,is_date,10" value="${today}"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
		</td>
		<td align=left nowrap>&nbsp;</td>
	</tr>
	<tr>
      <td align="right" nowrap></td>
      <td align="right" nowrap></td>
      <td align="left" nowrap></td>
    </tr>
	<tr>
      <td align="right" nowrap>&nbsp;</td>
	   <td align="center" nowrap>
	   	<input id="queryBtn" name="button22" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">
	   	<input id="addBtn" name="button22" type=button class="cssbutton" onClick="loginAdd();" value="新增">
	   </td>
	   <td></td>
	</tr>
  </table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript">
	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/crm/travel/NetSpeciaTravel/netSpeciaQuery.json";
				
	var title = null;
	
	var columns = [
				{header: "日志编码", dataIndex: 'SPECIA_ID', align:'center'},
				{header: "省份", dataIndex: 'PROVINCE_NAME', align:'center'},
				{header: "目标市场", dataIndex: 'CITY_NAME', align:'center'},
				{header: "创建时间", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "出差日期", dataIndex: 'BUSINESS_DATE', align:'center'},
				{header: "出差人", dataIndex: 'BUSINESS_TRAVELLER', align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'SPECIA_ID',renderer:myLink ,align:'center'}
		      ];		         
	
	//修改的超链接
	function myLink(value,meta,record){
		var data = record.data;
		var ret = "";
		if(record.data.SUBMIT_STATUS == '<%=Constant.FORECAST_STATUS_UNCONFIRM%>'){
			ret += "<a href='#' onclick='loginMod(\""+ value +"\")'>[修改]</a>"
			ret += "<a href='#' onclick='confirmDel(\""+ value +"\")'>[删除]</a>";
			ret += "<a href='#' onclick='confirmSubmit(\""+ value +"\")'>[提报]</a>";
		}
	
		ret += "<a href='#' onclick='confirmInfo(\""+ value +"\")'>[查看]</a>";
  		return String.format(ret);
	}
	
	function loginAdd(){
		$('fm').action = '<%=request.getContextPath()%>/crm/travel/NetSpeciaTravel/doAddInit.do';
	 	$('fm').submit();
	}
	
	function loginMod(arg){
		$('fm').action = '<%=request.getContextPath()%>/crm/travel/NetSpeciaTravel/doUpdateInit.do?speciaId='+arg
	 	$('fm').submit();
	}
	
	function confirmInfo(arg){
		$('fm').action = '<%=request.getContextPath()%>/crm/travel/NetSpeciaTravel/netSpeciaQueryInfo.do?speciaId='+arg;
	 	$('fm').submit();
	}
	
	//删除方法：
	function confirmDel(arg){
		MyConfirm("确认删除？",del,[arg]);
	}  
	//删除
	function del(arg){
		makeNomalFormCall('<%=request.getContextPath()%>/crm/travel/NetSpeciaTravel/netSpeciaDel.json?speciaId='+arg,delBack,'fm');
	}
	//删除回调方法：
	function delBack(json) {
		if(json.returnValue == '1') {
			MyAlert("删除成功！");
			__extQuery__(1);
		} else {
			MyAlert("删除失败！请联系管理员！");
		}
	} 
	
	
	function confirmSubmit(arg){
		MyConfirm("是否确认提交?",orderSubmit,[arg]);
	}
	
	function orderSubmit(arg){
		makeNomalFormCall('<%=request.getContextPath()%>/crm/travel/NetSpeciaTravel/netSpeciaSubmit.json?speciaId='+arg,showResult,'fm');
	}
	
	function showResult(json){
		if(json.returnValue == '1'){
			MyAlert("提交成功！");
			__extQuery__(1);
		}else{
			MyAlert("提交失败！请联系管理员！");
		}
	}
</script>
</body>
</html>
