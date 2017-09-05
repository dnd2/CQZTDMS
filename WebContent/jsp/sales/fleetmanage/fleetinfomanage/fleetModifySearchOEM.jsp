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
<title>集团客户报备更改查询</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
   		
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 集团客户管理 &gt; 集团客户信息管理 &gt;集团客户报备更改查询</div>
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
		    <td class="table_query_2Col_label_5Letter">经销商公司：</td>
		    <td><input name="dealerCode" type="text" id="dealerCode" class="middle_txt" value=""/>
		  		<input name="dealerSel" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','dealerId','true','','true')" value="..." />
		  		<input type="hidden" name="dealerId" id="dealerId" value=""/>  
		  		<input name="clearText" type="button" class="cssbutton" onclick="clrTxt('dealerId','dealerCode')" value="清空"/>	  	
		    </td>
		    <td class="table_query_2Col_label_4Letter">申请日期：</td>
		    <td><input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            	&nbsp;至&nbsp;
            	<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            </td>
	      </tr>
		  <tr>
		    <td class="table_query_2Col_label_5Letter">审核状态：</td>
		    <td><script type="text/javascript">
            	genSelBoxExp("auditStatus",<%=Constant.FLEET_INFO_TYPE%>,"",true,"short_sel","","true",'<%=Constant.FLEET_INFO_TYPE_01%>');
                </script></td>
		    <td>&nbsp;&nbsp;&nbsp;</td>
		    <td><input name="searchBtn" type="button" id="queryBtn" class="normal_btn" onclick="__extQuery__(1);" value="查询" /></td>
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
	var url = "<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoModifySearch/queryFleetInfoModify.json";
				
	var title = null;

	var columns = [
				{header: "申请单位", dataIndex: 'COMPANY_SHORTNAME', align:'center'},
				{header: "客户名称",sortable: false,dataIndex: 'FLEET_NAME',renderer:myView ,align:'center'},
				{header: "区域", dataIndex: 'REGION', align:'center',renderer:getRegionName},
				{header: "客户类型", dataIndex: 'FLEET_TYPE', align:'center',renderer:getItemValue},
				{header: "申请时间", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "审核状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "审核意见", dataIndex: 'AUDIT_REMARK', align:'center'},
				{header: "审核时间", dataIndex: 'AUDIT_DATE', align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'EDIT_ID',renderer:myLink ,align:'center'}
		      ];
		      
//设置超链接  begin    
    
    //查看明细的超链接 
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='viewDetail(\""+ value +"\")'>[查看明细]</a>");
	}
	
	//查看明细的ACTION设置，修改集团客户信息
	function viewDetail(value){
		OpenHtmlWindow('<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoModifySearch/viewModifyDetail.do?editId='+value,900,600);
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
	
	
	//清空
	function clrTxt(value1,value2){
		document.getElementById(value1).value = "";
		document.getElementById(value2).value = "";
	
	}
//设置超链接 end
	
</script>
<!--页面列表 end -->
</body>
</html>
