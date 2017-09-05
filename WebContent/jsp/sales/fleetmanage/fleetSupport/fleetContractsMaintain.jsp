<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>集团客户支持</title>
<% String contextPath = request.getContextPath();  %>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 集团客户管理&gt;集团客户支持&gt;集团客户合同维护</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td align="right">客户名称：</td>
			<td align="left">
				<input name="fleetName" type="text" class="middle_txt" id="fleetName">
			</td>
			<td align="right">客户类型：</td>
			<td align="left">
				<label>
					<script type="text/javascript">
						genSelBoxExp("fleetType",<%=Constant.FLEET_TYPE%>,"",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">经销商公司：</td>
			<td align="left">
				<input id="companyName" name="companyName" class="long_txt" type="text"/>
				<input id="companyId" name="companyId" type="hidden"/>				
				<input class="mini_btn" type="button" value="..." onclick="showCompany();"/>
				<input class="cssbutton" type="button" value="清除" onclick="toClear();"/>
			</td>
			<td align="right">合同签订日期：</td>    
			<td align="left">
				<input class="short_txt"  type="text" id="checkSDate" name="checkSDate" datatype="1,is_date,10" group="checkSDate,checkEDate" value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 'checkSDate', false);" value="&nbsp;" />&nbsp;至&nbsp;
				<input class="short_txt"  type="text" id="checkEDate" name="checkEDate" datatype="1,is_date,10" group="checkSDate,checkEDate" value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 'checkEDate', false);" value="&nbsp;" />
			</td>
		</tr>
		<tr>
			<td align="right">合同状态：</td>    
			<td align="left">
				<script type="text/javascript">
					genSelBoxExp('conStatus','<%=Constant.FLEET_CON_STATUS%>','<%=Constant.FLEET_CON_STATUS_01%>',true,'short_sel','','true','<%=Constant.FLEET_CON_STATUS_02%>');
				</script>
			</td>
			<td align="right">&nbsp;</td>
			<td align="left">&nbsp;</td>
			<td align="left"><input name="button2" type="button" id="queryBtn" class="cssbutton" onClick="__extQuery__(1)" value="查询"></td>
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
	var url = "<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetContractsMaintain/fleetContractsMaintainQuery.json";
	var title = null;
	var columns = [
				{header: "申请单位",dataIndex: 'COMPANY_NAME',align:'center'},
				//{header: "申请日期",dataIndex: 'REPORT_DATE',align:'center'},
				{header: "客户名称",dataIndex: 'FLEET_NAME',align:'center',renderer:myView},
				{header: "客户类型", dataIndex: 'FLEET_TYPE', align:'center',renderer:getItemValue},
				{header: "省份",dataIndex: 'REGION',align:'center',renderer:getRegionName},
				{header: "主要联系人",dataIndex: 'MAIN_LINKMAN', align:'center'},
				{header: "主要联系人电话",dataIndex: 'MAIN_PHONE', align:'center'},
				{header: "签订日期",dataIndex: 'CHECK_DATE', align:'center'},
				{header: "合同状态",dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "操作", dataIndex: 'FLEET_ID', align:'center',renderer:myLink}
		      ];
    //设置超链接
    function myLink(value,meta,record)
    {
       	return String.format("<a href='#' onclick='searchServiceupdate("+ value +","+record.data.CONTRACT_ID+")'>[合同审核]</a>");
    }
 	//明细链接
	function searchServiceInfo(value1,value2){
	
		$('fm').action= '<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetContractsMaintain/fleetContractsMaintainDetailQuery.do?&fleetId='+value1+'&intentId='+value2;
	 	$('fm').submit();
	}
	
	//修改链接
	function searchServiceupdate(value1,value2)
	{
		$('fm').action= '<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetContractsMaintain/fleetContractsUpdate.do?&fleetId='+value1+'&contractId='+value2;
	 	$('fm').submit();
	}
	
	//设置超链接
	function myView(value,meta,record){
  		return String.format(
         "<a href=\"#\" onclick='sel(\""+record.data.FLEET_ID+"\")'>"+ value +"</a>");
	}
	//详细页面
	function sel(value){
		OpenHtmlWindow('<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetSupportApply/fleetInfoDetailQuery.do?fleetId='+value,800,500);
	}
    //初始化
    function doInit(){
   		loadcalendar();  //初始化时间控件
	}
  	//经销商公司弹出
  	function showCompany(){
  		OpenHtmlWindow('<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetContractsMaintain/queryCompany.do',800,450);
  	}
  	//清除按钮
  	function toClear(){
		document.getElementById("companyName").value="";
		document.getElementById("companyId").value="";
  	}
</script>
<!--页面列表 end -->
</body>
</html>