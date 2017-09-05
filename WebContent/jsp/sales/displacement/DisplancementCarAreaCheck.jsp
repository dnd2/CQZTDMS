<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/activityfunc.js"></script>
<title>二手车置换审核查询</title>
<script type="text/javascript">
<!--
	function doInit(){
		_setDate_("startDate", "endDate", "1", "0") ;
		loadcalendar();  //初始化时间控件
		genLocSel('txt1','','','','',''); // 加载省份城市和县
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
  //->
</script>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 二手车置换 &gt; 二手车置换审核查询</div>
	<form id="fm" name="fm" method="post">
		<table class="table_query" border="0">
			<tr>
				<td width="20%"><div align="right">置换编号：</div></td>
				<td width="20%"><input type="text" class="middle_txt" name="disNo" id="disNo" value="" /></td>
				<td width="20%"><div align="right">置换类型：</div></td>
				<td width="20%">
					<script type="text/javascript">
						genSelBoxExp("type",<%=Constant.DisplancementCarrequ_replace%>,"",true,"short_sel",'',"false",'');
					</script>
				</td>
				<td align="left"></td>
			</tr>
			<tr>
				<td width="20%"><div align="right">省份：</div></td>
				<td width="20%"><select class="min_sel" id="txt1" name="region"></select></td>
				<td width="20%"><div align="right">基地：</div></td>
				<td width="20%">
					<script type="text/javascript">
						genSelBoxExp("base",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel",'',"false",'');
					</script>
				</td>
				<td align="left"></td>
			</tr>
			<tr>
				<td width="20%"><div align="right">新车VIN：</div></td>
				<td width="20%">
      				<textarea id="newVin" name="newVin" cols="30" rows="3" ></textarea>
    			</td>
    			<td width="20%"><div align="right">客户名称：</div></td>
				<td width="20%">
					<input type="text" class="middle_txt" name="clientName" id="clientName" value="" />
    			</td>
    			<td>
    			</td>
			</tr>
			<tr>
				<td width="20%" align="right">销售日期：</td>
				<td width="20%" align="left"> 
					<input class="short_txt" readonly="readonly"  type="text" id="startDate" name="startDate" group="startDate,endDate" datatype="1,is_date,10"/>
					<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value="&nbsp;" />至
					<input class="short_txt"  readonly="readonly" type="text" id="endDate" name="endDate" group="startDate,endDate" datatype="1,is_date,10"/>
					<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
				</td>
				<td align="right"></td>
				<td align="left">
				</td>
				<td></td>
			</tr>
			<tr>
				<td width="20%"></td>
				<td width="20%"></td>
				<td width="20%"></td>
				<td width="20%"></td>
				<td align="left">
					<input type="hidden" name="status" id="status" value="${status }" />
					<input type="hidden" name="chkStatus" id="status" value="${chkStatus }" />
					<input type="hidden" name="areas" id="areas" value="${areas }" />
					<input type="hidden" name="dis_id" id="dis_id" />
					<input type="hidden" name="vehicle_id" id="vehicle_id" />
					<input type="button" class="normal_btn" onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" /> 
				</td>
			</tr>
		</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
	
</div>
<script type="text/javascript">

	var myPage;
	
	var url = "<%=contextPath%>/sales/displacement/DisplacementCarChk/displacementCarChkQuery.json?COMMAND=1";
	
	var title = null;
	
	var columns = [
				{header: "置换编号 ", dataIndex: 'DISPLACEMENT_NO', align:'center'},
				{header: "组织", dataIndex: 'ROOT_ORG_NAME', align:'center'},
				{header: "省份", dataIndex: 'REGION_NAME', align:'center'},
				{header: "经销商名称", dataIndex: 'ROOT_DEALER_NAME', align:'center'},
				{header: "置换类型", dataIndex: 'DISPLACEMENT_TYPE', align:'center', renderer:getItemValue},
				{header: "状态", dataIndex: 'OPERATE_STATUS', align:'center', renderer:getItemValue},
				{header: "新车vin", dataIndex: 'NEW_VIN', align:'center'},
				{header: "新车生产基地", dataIndex: 'PRODUCE_BASE', align:'center', renderer:getItemValue},
				/* {header: "旧车vin", dataIndex: 'OLD_VIN', align:'center'}, */
				{header: "新车客户名称", dataIndex: 'HOST_NAME', align:'center'},
				{header: "新车销售日期", dataIndex: 'SALES_DATE', align:'center', renderer:getDateValue},
				{id:'action',header: "操作", walign:'center',idth:70,dataIndex: 'DISPLACEMENT_ID',renderer:myLink}
		      ];
	
	function getDateValue(value, date, record) {
		return value.substring(0,10);
	}
		      
	function myLink(dis_id,metaDate,record){
		var vehicleId = record.data.VEHICLE_ID ;
        return String.format(
        		 "<a href=\"#\" onclick=\"chkArea(" + vehicleId + "," + dis_id + ");\">[${opera}]</a>");
    }

	function chkArea(vehicleId, dis_id) {
		document.getElementById('vehicle_id').value = vehicleId ;
		document.getElementById('dis_id').value = dis_id ;
		var url = "<%=contextPath%>/sales/displacement/DisplacementCarChk/infoDetailQuery.do" ;
		$('fm').action=url;
		$('fm').submit();
	}
</script>    
</body>
</html>