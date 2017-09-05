<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% 
//List<TtAsActivityPO> list=(List<TtAsActivityPO>)request.getAttribute("list"); 
%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务车辆信息及状态查询</title>
<% String contextPath = request.getContextPath(); %>
<style type="text/css">
<!--
.colorChanage {
	color: #F00;
	text-decoration: none;
}
-->
</style>
</head>

<body>	
<form name="fm" id="fm">
	<div class="navigation">
			<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务车辆信息及状态查询
	</div> 
			<table class="table_query" border="0">
			 <tr>
           
	              <td class="table_query_3Col_label_5Letter">经销商代码：</td>
	              <td align="left">
		                 <input class="middle_txt" id="dealerCode" style="cursor: pointer;" name="dealerCode" type="text" />
			             <input class="mini_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCode','','false','',true)" />
			             <input name="button2" type="button" class="normal_btn" onclick="clr();" value="清除"/>
  				</td>
  				 <td class="table_query_3Col_label_5Letter">经销商名称：</td>
	              <td align="left">
		                 <input class="middle_txt" id="dealerName" style="cursor: pointer;" name="dealerName" type="text" />
  				</td>
            </tr>
            <tr>
	                 <td class="table_query_3Col_label_5Letter" >活动名称： </td>
		             <td align="left">
			             	<input type="text"   class="middle_txt"  style="cursor: pointer;" id="activity_name" name="activity_name"  />
				            <input type="hidden" name="activityCode" id="activityCode"/>
						    <input type="button" class="mini_btn" value="..." onclick="openQueryName();"/>
						    <input type="button" class="normal_btn" value="清除" onclick="clrName();"/>
		             </td>
					<td class="table_query_3Col_label_5Letter">客户名称：</td>
					<td align="left">
					 	<input type="text"  name="customerName"  id="customerName"  class="middle_txt"  size="20"  maxlength="20"  datatype="1,is_digit_letter_cn,6"/>
					</td>
			</tr>
			<tr>
			  <td align="center" colspan="4">
			 	 <input class="normal_btn" value="查询" name="button1" type="button" onclick="__extQuery__(1);" /></td>
			</tr>
			</table>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
 <br/>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
//查询路径
	var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageVehicleStatus/serviceActivityManageVehicleStatusQuery.json";
				
	var title = null;

	var columns = [
				{header: "活动编号", dataIndex: 'ACTIVITY_CODE', align:'center'},
				{header: "活动名称", dataIndex: 'ACTIVITY_NAME', align:'center'},
				{id:'action',header: "VIN",sortable: false,dataIndex: 'VIN',align:'center'},//renderer:mySelectToo 
				{header: "车牌号码 ",dataIndex: 'LINCENSE_TAG' ,align:'center'},
				{header: "客户名称",dataIndex: 'CUSTOMER_NAME' ,align:'center'},
				{header: "客户所属地区",dataIndex: 'AREA' ,align:'center'},
				{header: "联系人",dataIndex: 'LINKMAN' ,align:'center'},
				{header: "责任经销商",dataIndex: 'DEALER_CODE' ,align:'center'},
				{header: "车辆状态",dataIndex: 'CAR_STATUS' ,align:'center',renderer:getItemValue}
				
		      ];
	//设置超链接{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:mySelect  ,align:'center'}
	function mySelect(value,meta,record){
		var carStatus = record.data.CAR_STATUS;        //车辆状态
		var ID = record.data.ID;
		if(carStatus == '<%=Constant.SERVICEACTIVITY_CAR_STATUS_01 %>'){//已经下发
	  		return String.format(
	         "<a href=\"#\" onclick='sureComplete(\""+record.data.ID+"\")'>[完成]</a><a href=\"#\" onclick='sel(\""+ID+"\")'>[明细]</a>");
		 }else if(carStatus == '<%=Constant.SERVICEACTIVITY_CAR_STATUS_02 %>'){//已经修理完成
			 return String.format(
			 "<a href=\"#\" class=\"colorChanage\" onclick='warningCom();'>[完成]</a><a href=\"#\" onclick='sel(\""+ID+"\")'>[明细]</a>");
		}else{
			 return String.format(//尚未下发
			 "<a href=\"#\" class=\"colorChanage\" onclick='warning();'>[完成]</a><a href=\"#\" onclick='sel(\""+ID+"\")'>[明细]</a>");
			 }
  	}
	//详细页面
	function sel(value){
		OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageVehicleStatus/serviceActivityManageVehicleStatusInfo.do?id='+value,800,500);
	}
	//确认完成
	function sureComplete(str){
		MyConfirm("是否确认完成?",complete,[str]);
		}
	//完成
	function complete(value){
		makeNomalFormCall("<%=contextPath%>/claim/serviceActivity/ServiceActivityManageVehicleStatus/serviceActivityManageVehicleCarStatus.json?id="+value,showForwordValue,'fm','queryBtn');
	}
	//刷新页面
	function showForwordValue(json){
		if(json.returnValue == '1')
		{
			MyAlert("完成成功！");
			__extQuery__(1);
		}else
		{
			MyAlert("完成失败！");
		}
	}
	//提示已经完成
	function warning(){
		MyAlert("请下发车辆！");
		}
	function warningCom(){
		MyAlert("已经完成！");
		}
		//清除方法
 function clr() {
	document.getElementById('dealerCode').value = "";
  }
  //设置超链接
	function mySelectToo(value,meta,record){
  		return String.format(
         "<a href=\"#\" onclick='sels(\""+record.data.VIN+"\")'>["+ value +"]</a>");
	}
	//详细页面
	function sels(value){
		OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageVehicleStatus/getActivityVehicleVinInfoInit.do?vin='+value,800,500);
	}
  function openQueryName(){
	var url = "<%=request.getContextPath()%>/jsp/claim/serviceActivity/serviceActivityShowQuery.jsp?flag=1";
	OpenHtmlWindow(url,900,500);
	}
  function clrName(){
    document.getElementById('activity_name').value = '';
  }
  function showName(activity_id,activity_code,activity_name){
  //MyAlert('activity_id:'+activity_id+'|activity_code:'+activity_code+'|activity_name:'+activity_name);
    document.getElementById('activity_name').value = activity_name;
    document.getElementById('activityCode').value = activity_code;
  }
</script>
<!--页面列表 end -->
</body>
</html>