<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>集团客户合同查询</title>
<% String contextPath = request.getContextPath();  %>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 集团客户管理&gt;集团客户支持&gt;集团客户合同查询</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td align="right">客户名称：</td>
			<td align="left">
				<input name="fleetName" type="text" id="fleetName" class="middle_txt">
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
			<td align="right">合同编号：</td>
			<td align="left">
				<input type="text" name="contractNo" id="contractNo" class="middle_txt"/>
			</td>
			<td align="right">合同状态：</td>
			<td align="left">
				<script type="text/javascript">
					genSelBoxExp("fleetStatus",<%=Constant.FLEET_CON_STATUS%>,"",true,"short_sel",'',"false",'');
				</script>		
			</td>
			<td width="30%" align="left"></td>
		</tr>
		<tr>
			<td align="right">合同签订日期：</td>    
			<td align="left">
				<input class="short_txt"  type="text" id="checkSDate" name="checkSDate" datatype="1,is_date,10" group="checkSDate,checkEDate" value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 'checkSDate', false);" value="&nbsp;" />&nbsp;至&nbsp;
				<input class="short_txt"  type="text" id="checkEDate" name="checkEDate" datatype="1,is_date,10" group="checkSDate,checkEDate" value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 'checkEDate', false);" value="&nbsp;" />
			</td>
			<td align="right">有效期：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="t1" name="startDate" datatype="1,is_date,10" group="t1,t2"  value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't1', false);" value="&nbsp;" />&nbsp;至&nbsp;
				<input class="short_txt"  type="text" id="t2" name="endDate" datatype="1,is_date,10" group="t1,t2" value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't2', false);" value="&nbsp;" />
			</td>
			<td width="30%" align="left"><input name="button2" id="queryBtn" type="button" class="cssbutton" onClick="__extQuery__(1);" value="查询"></td>
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
	var url = "<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetContractsSearch/fleetContractsSearch.json";
	var title = null;
	var columns = [
				{header: "合同编号",dataIndex: 'CONTRACT_NO',align:'center'},
				{header: "客户名称",dataIndex: 'FLEET_NAME',align:'center',renderer:myView},
				{header: "客户类型", dataIndex: 'FLEET_TYPE', align:'center',renderer:getItemValue},
				{header: "主要联系人",dataIndex: 'MAIN_LINKMAN', align:'center'},
				{header: "主要联系人电话",dataIndex: 'MAIN_PHONE', align:'center'},
				//{header: "折扣",dataIndex: 'DISCOUNT', align:'center',renderer:myText1},
				//{header: "合同数量",dataIndex: 'CONTRACT_AMOUNT', align:'center'},
				//{header: "已完成数量",dataIndex: 'AMOUNT', align:'center'},
				{header: "合同签订日期",dataIndex: 'CHECK_DATE', align:'center'},
				{header: "有效期",align:'center',renderer:myText2},
				{header: "合同状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue},
				{header: "操作", dataIndex: 'FLEET_ID', align:'center',renderer:myLink}
		      ];
	//设置超链接
	function myLink(value,meta,record){
       	return String.format("<a href='#' onclick='searchServiceupdate("+ value +","+record.data.CONTRACT_ID+","+record.data.INTENT_ID+")'>[明细]</a>");
    }
    
    //修改链接
	function searchServiceupdate(value1,value2,value3){
		$('fm').action= '<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetContractsQuery/fleetContractsInfo.do?&fleetId='+value1+'&intentId='+value3+'&contractId='+value2;
	 	$('fm').submit();
	}
	
	function myView(value,meta,record){
  		return String.format("<a href=\"#\" onclick='sel(\""+record.data.FLEET_ID+"\")'>"+ value +"</a>");
	}
	//设置文本
	function myText1(value,meta,record){
  		return String.format(value +"%");
	}
	//设置文本
	function myText2(value,meta,record){
  		return String.format(record.data.START_DATE+"~"+ record.data.END_DATE);
	}
	//详细页面
	function sel(value){
		OpenHtmlWindow('<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetSupportApply/fleetInfoDetailQuery.do?fleetId='+value,800,500);
	}
    //初始化
    function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
<!--页面列表 end -->
</body>
</html>