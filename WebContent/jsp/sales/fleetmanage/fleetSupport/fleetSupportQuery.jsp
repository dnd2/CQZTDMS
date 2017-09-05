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
<% String contextPath = request.getContextPath(); 
	String dutyType=request.getAttribute("dutyType").toString();
%>
<script type="text/javascript">
</script>
</head>
<body onload="__extQuery__(1);">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 集团客户管理 &gt; 集团客户支持&gt; 集团客户支持查询</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td align="right">客户名称：</td>
			<td align="left">
				<input name="fleetName" type="text" id="fleetName">
			</td>
			<td align="right">客户类型：</td>
			<td align="left">
				<label>
					<script type="text/javascript">
						genSelBoxExp("fleetType",<%=Constant.FLEET_TYPE%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">申请日期：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="t1" name="startDate" datatype="1,is_date,10" group="t1,t2"  value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't1', false);" value="&nbsp;" />&nbsp;至&nbsp;
				<input class="short_txt"  type="text" id="t2" name="endDate" datatype="1,is_date,10" group="t1,t2" value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't2', false);" value="&nbsp;" />
			</td>
			<td align="right">经销商公司：</td>
			<td align="left">
			<input id="companyName" name="companyName" type="text"/>
			<input id="companyId" name="companyId" type="hidden"/>	
			<input id="dutyType" id="dutyType"  value="${dutyType}"type="hidden"/>			
			<input class="mini_btn" type="button" value="..." onclick="showCompany();"/>
			<input class="cssbutton" type="button" value="清除" onclick="toClear();"/>
			</td>
			<td width="20%" align="left"></td>
		</tr>
		<tr>
			<td align="right">申请状态：</td>
			<td align="left">
				<label>
					<script type="text/javascript">
						genSelBoxExp("supportStatus",1996,"-1",true,"short_sel",'',"false",'<%=Constant.FLEET_SUPPORT_STATUS_01%>');
					</script>
				</label>
			</td>
			<td></td>
			<td></td>
			<td  align="left"><input name="button2" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询"></td>
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
	var url = "<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetSupportQuery/fleetSupportQuery.json";
	var title = null;
	var columns = [
				{header: "申请单位",dataIndex: 'COMPANY_NAME',align:'center'},
				{header: "申请日期",dataIndex: 'REPORT_DATE',align:'center'},
				{header: "客户名称",dataIndex: 'FLEET_NAME',align:'center',renderer:myView},
				{header: "大客户代码", dataIndex: 'FLEET_CODE', align:'center'},
				{header: "客户类型", dataIndex: 'FLEET_TYPE', align:'center',renderer:getItemValue},
				{header: "区域",dataIndex: 'REGION',align:'center',renderer:getRegionName},
				{header: "需求数量",dataIndex: 'REQUEST_COUNT', align:'center'},
				{header: "完成数量",dataIndex: 'FINISH_COUNT', align:'center'},
				{header: "主要联系人",dataIndex: 'MAIN_LINKMAN', align:'center'},
				{header: "主要联系人电话",dataIndex: 'MAIN_PHONE', align:'center'},
				{header: "申请状态", dataIndex: 'SUPPORT_STATUS', align:'center',renderer:getItemValue},
				{id:'action',header: "操作", dataIndex: 'FLEET_ID', align:'center',renderer:myLink}
		      ];
    //设置超链接
    function myLink(value,meta,record){
    	if($('dutyType').value==10431001&&record.data.SUPPORT_STATUS==19961004){
    		return String.format("<a href='#' onclick='searchServiceInfo("+ value +","+record.data.INTENT_ID+")'>[明细]</a>|<a href='#' onclick='abortFleet("+ value +")'>[终止]</a>");
    	}else{
    		return String.format("<a href='#' onclick='searchServiceInfo("+ value +","+record.data.INTENT_ID+")'>[明细]</a>");
    	}
    }
 	//明细链接
	function searchServiceInfo(value1,value2){
		$('fm').action= '<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetSupportQuery/fleetSupportQueryDetail.do?&fleetId='+value1+'&intentId='+value2;
	 	$('fm').submit();
	}
	//设置超链接
	function myView(value,meta,record){
  		return String.format(
         "<a href=\"#\" onclick='sel(\""+record.data.FLEET_ID+"\",\""+record.data.INTENT_ID+"\")'>"+ value +"</a>");
	}
	//详细页面
	function sel(value,value1){
		
		OpenHtmlWindow('<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetSupportApply/fleetInfoDetailQuery.do?intendId='+value1+'&fleetId='+value,800,500);
	}
    //初始化
    function doInit(){
   		loadcalendar();  //初始化时间控件
	}
  	//经销商公司弹出
  	function showCompany(){
  		OpenHtmlWindow('<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetSupportQuery/queryCompany.do',800,450);
  	}
  	//清除按钮
  	function toClear(){
		document.getElementById("companyName").value="";
		document.getElementById("companyId").value="";
  	}
  	//终止大客户
  	function abortFleet(fleetId){
  		var url='<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetSupportQuery/fleetAbortOper.json';
  		makeCall(url,abortFleetReturn,{fleetId:fleetId}); 
  	}
  	//大客户终止回调函数
  	function abortFleetReturn(json){
  		if(json.flag==0){
  			MyAlert("大客户终止失败！");
  		}else{
  			MyAlert("大客户终止成功！");
  			__extQuery__(1);
  		}
  	}
</script>
<!--页面列表 end -->
</body>
</html>