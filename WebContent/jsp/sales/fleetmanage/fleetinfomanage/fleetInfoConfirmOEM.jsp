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
<title>集团客户报备确认</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body onload="__extQuery__(1);">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 集团客户管理 &gt; 集团客户信息管理 &gt;集团客户报备确认</div>
 <form method="post" name = "fm" >
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		  <tr>
		    <td class="table_query_2Col_label_5Letter">客户名称：</td>
		    <td><input type='text'  class="middle_txt" name="fleetName"  id="fleetName" value=""/></td>
		    <td class="table_query_2Col_label_4Letter">客户类型：</td>
		    <td><script type="text/javascript">
            	genSelBoxExp("fleetType",<%=Constant.FLEET_TYPE%>,"",true,"short_sel","","true",'');
                </script></td>
          </tr>
          <tr>
		    <td class="table_query_3Col_label_5Letter">经销商公司：</td>
		    <td><input name="dealerCode" type="text" id="dealerCode" class="middle_txt" value=""/>
		  		<input name="dealerSel" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','dealerId','true','','true')" value="..." />	
		  		<input type="hidden" name="dealerId" id="dealerId" value=""/>
		    </td>
		    <td class="table_query_3Col_label_4Letter">报备日期：</td>
		    <td><input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            	&nbsp;至&nbsp;
            	<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            </td>
           </tr>
           <tr>
		    <td>&nbsp;&nbsp;&nbsp;</td>
		    <td align="right"><input name="searchBtn" id="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查询" /></td>
		    <td>&nbsp;&nbsp;&nbsp;</td>
		    <td>&nbsp;&nbsp;&nbsp;</td>
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
	var url = "<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoAppConfirm/queryFleetInfo.json";
				
	var title = null;

	var columns = [
				{header: "提报单位", dataIndex: 'COMPANY_SHORTNAME', align:'center'},
				{id:'action',header: "客户名称",sortable: false,dataIndex: 'FLEET_NAME',renderer:myView ,align:'center'},
				{header: "区域", dataIndex: 'REGION', align:'center',renderer:getRegionName},
				{header: "客户类型", dataIndex: 'FLEET_TYPE', align:'center',renderer:getItemValue},
				{header: "主要联系人", dataIndex: 'MAIN_LINKMAN', align:'center'},
				{header: "主要联系人电话", dataIndex: 'MAIN_PHONE', align:'center'},
				{header: "报备时间", dataIndex: 'SUBMIT_DATE', align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'FLEET_ID',renderer:myLink ,align:'center'}
		      ];
		      
//设置超链接  begin      
	
	//处理的超链接
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='goConfirm(\""+ value +"\")'>[确认]</a>");
	}
	
	//确认的ACTION设置，进行集团客户报备审核
	function goConfirm(value){
		$('fm').action= '<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoAppConfirm/checkFleetInfo.do?fleetId=' + value;
	 	$('fm').submit();
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
	
	
//设置超链接 end
	
</script>
<!--页面列表 end -->
</body>
</html>
