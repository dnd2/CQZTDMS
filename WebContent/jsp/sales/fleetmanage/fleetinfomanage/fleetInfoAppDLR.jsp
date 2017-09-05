<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>集团客户信息报备</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
   		
	}
</script>
</head>
<body onload="__extQuery__(1);">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 集团客户管理 &gt; 集团客户信息管理 &gt;集团客户报备</div>
 <form method="post" name = "fm" >
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		  <tr>
		    <td class="table_query_2Col_label_4Letter">客户名称：</td>
		    <td><input type='text'  class="middle_txt" name="fleetName"  id="fleetName" value=""/></td>
		    <td class="table_query_2Col_label_4Letter">客户类型：</td>
		    <td><script type="text/javascript">
            	genSelBoxExp("fleetType",<%=Constant.FLEET_TYPE%>,"",true,"short_sel","","true",'');
                </script>
		    </td>
	      </tr>
		  <tr>
		    <td>&nbsp;&nbsp;&nbsp;</td>
		    <td>&nbsp;&nbsp;&nbsp;</td>
		    <td>&nbsp;&nbsp;&nbsp;</td>
		    <td><div align="right">
        	<input name="searchBtn" type="button" id="queryBtn" class="normal_btn" onclick="__extQuery__(1);" value="查询" />
        	<input name="addBtn" type="button" class="normal_btn" onclick="addFleetInfo()" value="新增" />
      		</div></td>
	      </tr>
     </table> 
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>

<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoApplication/queryFleetInfo.json";
				
	var title = null;

	var columns = [
				{header: "客户名称",sortable: false,dataIndex: 'FLEET_NAME',renderer:myView ,align:'center'},
				{header: "区域", dataIndex: 'REGION', align:'center',renderer:getRegionName},
				{header: "客户类型", dataIndex: 'FLEET_TYPE', align:'center',renderer:getItemValue},
				{header: "主要联系人", dataIndex: 'MAIN_LINKMAN', align:'center'},
				{header: "主要联系人电话", dataIndex: 'MAIN_PHONE', align:'center'},
				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "操作",sortable: false,dataIndex: 'FLEET_ID',renderer:myLink ,align:'center'}
		      ];
		      
//设置超链接  begin      
	
	//提交的超链接
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='updateInfo(\""+ value +"\")'>[修改]</a><a href='#' onclick='submitInfo(\""+ value +"\")'>[提交]</a>");
	}
	
	//修改的ACTION设置，修改集团客户信息
	function updateInfo(value){
		$('fm').action= '<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoApplication/updateFleetInfo.do?fleetId=' + value;
	 	$('fm').submit();
	}
	
	// 提交的ACTION设置
	function submitInfo(value){
	  MyConfirm("确认提交？",submitConfirm,[value]);
		
	}
	
	// 确认提交执行的函数
	function submitConfirm(value){
		makeNomalFormCall('<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoApplication/submitFleetInfo.json?fleetId='+value,showBack,'fm');
	}
	
	// 提交的回调函数
	function showBack(json){
		var rtnValue = json.returnValue;
		if(rtnValue==1){
			__extQuery__(1);
			MyAlert("提交成功！");
		}else{
			MyAlert("提交失败！请联系管理员！");
		}
	}
	
	//设置超链接
	function myView(value,meta,record){
  		return String.format(
         "<a href=\"#\" onclick='sel(\""+record.data.FLEET_ID+"\")'>"+ value +"</a>");
	}
	
	//详细页面
	function sel(value){
		OpenHtmlWindow('<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoApplication/viewFleetDetailInfo.do?fleetId='+value,900,600);
	}
	
	//客户投诉新增
	function addFleetInfo(){
		window.location.href='<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoApplication/addFleetInfo.do';
	}
	
	
//设置超链接 end
	
</script>
<!--页面列表 end -->
</body>
</html>
