<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsActivityVehiclePO"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%TtAsActivityVehiclePO vehiclePO =(TtAsActivityVehiclePO)request.getAttribute("vehiclePO"); %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动车辆信息确认</title>
<%
	String contextPath = request.getContextPath();
%>
<script type="text/javascript">
function doInit()
	{
	   loadcalendar();
	}
function modifyStatus(){
	var id=document.getElementById("id").value;
	var activityId=document.getElementById("activityId").value;
	fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageVehicle/serviceActivityManageModify.do?activityId=" + activityId+"&id="+id;
	fm.submit();
}
</script>
</head>

<body>
	<div class="navigation">
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;活动服务管理&gt;服务活动车辆信息确认
	</div>
<form name="fm" id="fm">
  <input type="hidden" name="activityId" id="activityId" value="<%=request.getAttribute("activityId") %>" />
  <input type="hidden" name="id" id="id" value="<%=vehiclePO.getId()%>" />
  <table width="95%" border="0" class="table_edit">
          <tr>
            <td class="table_query_3Col_label_8Letter" >责任经销商代码：</td>
            <td align="left">
	           <input class="middle_txt" id="dealerCode" style="cursor: pointer;" name="dealerCode" type="text" value="<%=request.getAttribute("dealerCode") %>"/>
			   <input class="mini_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCode','','false','',true)"/>
            </td>
			<td>
			</td>
          </tr>
          <tr>
            <td class="table_query_3Col_label_5Letter" >维修状态：</td>
            <td align="left">
               <script type="text/javascript">
   					genSelBoxExp("repairStatus",<%=Constant.SERVICEACTIVITY_REPAIR_STATUS%>,"<%=vehiclePO.getRepairStatus()%>",false,"short_sel","","false",'');
  				</script>
            </td>
            <td align="center"  class="zi">&nbsp;</td>
          </tr>
          <tr>
            <td class="table_query_3Col_label_5Letter">销售状态: </td>
            <td width="26%" align="left">
              <script type="text/javascript">
   					genSelBoxExp("saleStatus",<%=Constant.SERVICEACTIVITY_SALE_STATUS%>,"<%=vehiclePO.getSaleStatus()%>",false,"short_sel","","false",'');
  			  </script>
           </td>
        </table>  
        <br />
        <table class="table_query">
  	<tr> 		  
  		  <td align="center">
		      <input type="button" name="bt_new" id="bt_new_id" class="normal_btn" value="确定" onclick="modifyStatus();">
	      	  <input type="button" name="bt_return" id="bt_return_id" class="normal_btn" value="返回" onClick="history.go(-1);">
	      </td>
  	</tr>
  </table>
  </form>
</body>
</html>