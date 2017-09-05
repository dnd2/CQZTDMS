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
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 集团客户管理 &gt; 集团客户支持&gt; 集团客户实销信息审核查询</div>
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
						genSelBoxExp("fleetType",<%=Constant.FLEET_TYPE%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">经销商公司：</td>
			<td align="left">
			<input id="companyName" name="companyName" class="middle_txt" type="text" readonly="readonly" />
			<input id="companyId" name="companyId" type="hidden"/>				
			<input class="mini_btn" type="button" value="..." onclick="showCompany();"/>
			<input class="cssbutton" type="button" value="清除" onclick="toClear();"/>
			</td>
			<td align="right">物料组：</td>
			<td align="left">
				<input type="text" name="groupCode" id="groupCode" class="middle_txt" />
				<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','','true','4');" value="..." />
				<input class="cssbutton" type="button" value="清除" onclick="togroupClear();"/>
			</td>
			<td align="left"></td>
		</tr>
		<tr>
			<td align="right">合同编号：</td>
			<td align="left">
				<input type="text"  name="contractNo" class="middle_txt" id="contractNo"/>
			</td>
			<td align="right">底盘号（VIN）：</td>
			<td align="left">
				<textarea id="vin" name="vin" cols="18" rows="3" ></textarea>
	  		</td>
			<td align="left"></td>
		</tr>
		<tr>
			<td align="right">上报日期：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="t1" name="startDate" datatype="1,is_date,10" group="t1,t2"  value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't1', false);" value="&nbsp;" />&nbsp;至&nbsp;
				<input class="short_txt"  type="text" id="t2" name="endDate" datatype="1,is_date,10" group="t1,t2" value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't2', false);" value="&nbsp;" />
			</td>
			<td align="right">审核状态：</td>
			<td align="left">
				<label>
					<script type="text/javascript">
						genSelBoxExp("checkStatus",<%=Constant.Fleet_SALES_CHECK_STATUS%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
	  		</td>
		</tr>
		<tr>
			<td align="right">审核日期：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="logsdate" name="logsdate" datatype="1,is_date,10" group="logsdate,logedate"  value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 'logsdate', false);" value="&nbsp;" />&nbsp;至&nbsp;
				<input class="short_txt"  type="text" id="logedate" name="logedate" datatype="1,is_date,10" group="logsdate,logedate" value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 'logedate', false);" value="&nbsp;" />
			</td>
			<td align="right">&nbsp;</td>
			<td align="left">&nbsp;</td>
			<td width="15%" align="left"><input name="button2" type="button" id="queryBtn" class="cssbutton" onClick="__extQuery__(1);" value="查询">
			<input name="button2" type="button" id="queryBtn" class="cssbutton" onClick="downLoad();" value="实销下载">
			</td>
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
function downLoad(){
	 $('fm').action= "<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetSalesInfoCheckQuery/downloadTemple.json";
	$('fm').submit();
}
	var myPage;
	//查询路径 
	var url = "<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetSalesInfoCheckQuery/fleetSalesInfoCheckQuery.json";
	var title = null;
	var columns = [
				{header: "上报单位",dataIndex: 'DEALER_SHORTNAME',align:'center'},
				{header: "上报日期",dataIndex: 'SALES_DATE',align:'center'},
				{header: "客户名称",dataIndex: 'FLEET_NAME',align:'center',renderer:myView},
				{header: "审核时间", dataIndex: 'LOG_DATE', align:'center'},
				{header: "审核人", dataIndex: 'AUDITNAME', align:'center'},
				{header: "审核意见", dataIndex: 'REMARK', align:'center'},
				{header: "客户类型", dataIndex: 'FLEET_TYPE', align:'center'},
				{header: "合同编号",dataIndex: 'CONTRACT_NO',align:'center'},
				{header: "产品代码",dataIndex: 'MODCODE', align:'center'},
				{header: "产品名称",dataIndex: 'MODNAME', align:'center'},
				{header: "车型代码",dataIndex: 'PACKCODE', align:'center'},
				{header: "车型名称",dataIndex: 'PACKNAME', align:'center'},
				{header: "颜色",dataIndex: 'COLOR', align:'center'},
				{header: "底盘号（VIN）",dataIndex:'VIN', align:'center'},
				{header: "状态", dataIndex:'FLEET_STATUS', align:'center',renderer:getItemValue}
		      ];
	//设置超链接
	
	function getIValue(value,meta,record)
    {
    	if(value==-1)
    	{
    		return String.format("批售项目");	
    	}
    	else
    	{
    		return String.format(getItemValue(value));
    	}
    	
    }
    
	function myView(value,meta,record){
		var val = record.data.FLEET_TYPE;
		if(val==-1)
		{
			return String.format("<a href=\"#\" onclick='selPact(\""+record.data.FLEET_ID+"\")'>"+ value +"</a>");
		}
		else
		{
			return String.format("<a href=\"#\" onclick='sel(\""+record.data.FLEET_ID+"\")'>"+ value +"</a>");
		}
	}
	
	function selPact(value)
	{
		OpenHtmlWindow('<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetSupportApply/fleetInfoDetailQuery1.do?fleetId='+value,800,500);
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
  	//清除按钮
  	function togroupClear(){
  		document.getElementById("groupCode").value="";
  	}
</script>
<!--页面列表 end -->
</body>
</html>