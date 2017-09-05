<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>监控配件维护</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;整车三包关注部位维护</div>
  <form name='fm' id='fm' method="post">
  <TABLE class="table_query">
       <tr>            
        <td style="color: #252525;width: 115px;text-align: right">关注部位代码：</td>            
        <td>
			<input  class="middle_txt" id="POS_CODE"  name="POS_CODE" type="text" datatype="1,is_null,27"/>
        </td>
        <td class="table_query_3Col_label_6Letter">关注部位名称：</td>
        <td><input type="text" name="POS_NAME" id="POS_NAME" datatype="1,is_null,30" class="middle_txt" value=""/></td>
        <td class="table_query_3Col_label_6Letter">&nbsp;</td>
        <td>&nbsp;</td>     
       </tr>  
       <tr>
       <td colspan="4" align="center">
        	    <input   id="queryBtn" class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1)"/>
        	     <input   id="queryBtn" class="normal_btn" type="button" name="button1" value="新增"  onclick="addPosition()"/>
        </td>
       </tr>    
 	</table>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
  </body>
</html>
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/repairOrder/RoMaintainMain/positionQuery.json";
	var title = null;
	
	var columns = [
				{header: "关注部位代码",sortable: false,dataIndex: 'posCode',align:'center'},
				{header: "关注部位名称",sortable: false,dataIndex: 'posName',align:'center'},	
				{header: "三包月份",sortable: false,dataIndex: 'wrMonths',align:'center'},	
				{header: "三包里程",sortable: false,dataIndex: 'wrMileage',align:'center'},	
				{header: "关注天数",sortable: false,dataIndex: 'attDays',align:'center'},	
				{header: "关注里程",sortable: false,dataIndex: 'attMileage',align:'center'},			
				{header: "操作",sortable: false,dataIndex: 'posId',renderer:myLink ,align:'center'}
		      ];
//设置超链接  begin      
	
	//修改的超链接设置
	function myLink(value,meta,record){
	    return String.format(
         "<a href='#' onclick = 'openMilage("+value+")' >[维护]</a><a href='#' onclick = 'update("+value+")' >[修改]</a><a href='#' onclick = 'del("+value+")' >[删除]</a>");
	}
	
	function openMilage(value){
		OpenHtmlWindow('<%=contextPath%>/repairOrder/RoMaintainMain/positionModForword.do?ID='+value,800,500);
	}
	function update(value)
	{
		$('fm').action = "<%=contextPath%>/repairOrder/RoMaintainMain/updatePositioninit.do?id="+value;
	   	$('fm').submit();
	}
	
	function addPosition()
	{
		$('fm').action = "<%=contextPath%>/repairOrder/RoMaintainMain/addPositioninit.do"
	   	$('fm').submit();
	}
	//删除方法：
	function sel(str){
		MyConfirm("是否确认删除？",del,[str]);
	}  
	//删除
	function del(str){
       MyConfirm("是否确认删除？",delcomit,[str]);
	}
	function delcomit(str)
	{
		makeNomalFormCall('<%=contextPath%>/repairOrder/RoMaintainMain/postionDel.json?ID='+str,delBack,'fm','');
	}
	//删除回调方法：
	function delBack(json) {
		if(json.success != null && json.success == "yes") {
			MyAlert("删除成功！");
			__extQuery__(1);
		} else {
			MyAlert("删除失败！请联系管理员！");
		}
	}	
	
</script>  