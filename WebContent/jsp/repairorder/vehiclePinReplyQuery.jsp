<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息变更申请</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;车辆PIN订单查询申请
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td class="table_query_2Col_label_5Letter">维修站名称：</td>
      	<td><input name="companyName" type="text" id="companyName" class="middle_txt"/></td>
		<td class="table_query_2Col_label_5Letter">VIN：</td>
      	<td><input name="vin" type="text" id="vin" class="middle_txt"/></td>
      	<td class="table_query_2Col_label_6Letter">单据编码：</td>
      	<td>
      		<input name="pinCode" type="text" id="pinCode" class="middle_txt"/>
		</td>
    </tr>
    <tr>
      <td class="table_query_2Col_label_5Letter">单据状态：</td>
      <td>
      	<script type="text/javascript">
        	genSelBoxExp("status",<%=Constant.VEHICLE_PIN%>,"<%=Constant.VEHICLE_PIN_01%>",true,"short_sel","","false",'');
        </script>			
      </td>
      <td class="table_query_2Col_label_6Letter">制单日期：</td>
            	<td>
			            <input class="short_txt" type="text" name="PIN_CREATE_DATE" id="PIN_CREATE_DATE"  datatype="1,is_date,10" group="PIN_CREATE_DATE,PIN_END_DATE" hasbtn="true" callFunction="showcalendar(event, 'PIN_CREATE_DATE', false);"/>
            至
            <input class="short_txt" type="text" name="PIN_END_DATE" id="PIN_END_DATE"  datatype="1,is_date,10" group="PIN_CREATE_DATE,PIN_END_DATE" hasbtn="true" callFunction="showcalendar(event, 'PIN_END_DATE', false);"/>
		</td>
		          	<td class="table_query_2Col_label_7Letter">经销商：</td>
             <td nowrap="nowrap">
           			<input name="dealerCode" id="dealerCode" type="text" class="middle_txt" readonly="readonly" />
            		<input class="mini_btn" type="button" id="dealerBtn" value="&hellip;" onclick="showOrgDealer('dealerCode','dealerId','true','',true)"/> 
           			<input type="hidden" name="dealerId" id="dealerId" value=""/>    
           			<input class="normal_btn" type="button" value="清空" onclick="clrDlr('dealerId', 'dealerCode');"/>
  			</td>
    </tr>
    <tr>
    	<td align="center" colspan="6">
    		<input type="button" name="BtnQuery" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" >
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
	var myPage;
	var url = "<%=contextPath%>/repairOrder/RoMaintainMain/vehiclePinReplyQuery.json?COMMAND=1";
	var title = null;
	var columns = [
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "单据编码", dataIndex: 'PIN_CODE', align:'center',renderer:myHref},
				{header: "经销商编码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				//{header: "维修站编码", dataIndex: 'DEALER_CODE', align:'center'},
				//{header: "维修站名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "创建时间", dataIndex: 'MAKE_DATE', align:'center'},
				//{header: "创建人", dataIndex: 'USER_NAME', align:'center'},
				{header: "申请状态", dataIndex: 'STATUS', align:'center', renderer:getItemValue},
				{header: "审核人", dataIndex: 'USER_BY', align:'center'},
				{header: "审核时间", dataIndex: 'UPDATE_DATE', align:'center'},
				{id:'action', header: "操作", sortable: false, align:'center', renderer:myLink}
		      ];
		      
		      function myLink(value,meta,record){
		      		var link = "<a href='#' onclick='reply("+record.data.PIN_ID+");'>回复</a>";
		      		var status = record.data.STATUS;
		      		if(<%=Constant.VEHICLE_PIN_02%>==status){
		      			link = "<a href='#' onclick='view("+record.data.PIN_ID+","+record.data.CREATE_BY+","+record.data.DEALER_ID+");'>查看</a>";
		      		}
		      		return String.format(link);
		      }
		      function reply(pinId){
		      		//fm.method = 'post';
					//fm.action = '<%=contextPath%>/repairOrder/RoMaintainMain/vehiclePinReplyQueryDetail.do?pinId='+pinId;
					//fm.submit();
		      	window.location.href = "<%=contextPath%>/repairOrder/RoMaintainMain/vehiclePinReplyQueryDetail.do?pinId="+pinId;
		      }
			//申请PIN查询VIN
			function addPin(){
				window.location.href = "<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/addVehiclePinRequest.do";
				//fm.method = 'post';
				//fm.action = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/addVehiclePinRequest.do';
				//fm.submit();
			}		
			function myHref(value,meta,record){
	      		var link = "<a href='#' onclick='view("+record.data.PIN_ID+","+record.data.CREATE_BY+","+record.data.DEALER_ID+");'>"+value+"</a>";
	      		return String.format(link);
			}      
			function view(pinId,userId,dealerId){
		      	OpenHtmlWindow("<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/viewVehiclePinRequest.do?pinId="+pinId+"&userId="+userId+"&dealerId="+dealerId,800,500);
		      	//OpenHtmlWindow("<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/viewVehiclePinRequest?pinId="+pinId+"&userId="+userId,800,500);
		    }
	      function clrDlr()
		  {
			  document.getElementById('dealerCode').value='';
			  document.getElementById('dealerId').value='';
		  }
		      	
</script>
<!--页面列表 end -->
</body>
</html>