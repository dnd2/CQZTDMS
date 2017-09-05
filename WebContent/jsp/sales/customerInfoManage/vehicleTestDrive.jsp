<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<body onunload="javascript:destoryPrototype();">
<div id="loader" style="position: absolute; z-index: 200; background-color: rgb(255, 204, 0); padding: 1px; top: 4px; left: 455px; display: none; background-position: initial initial; background-repeat: initial initial; "> 正在载入中... </div>

<title>实销信息上报</title>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif">&nbsp;当前位置：  整车销售 &gt; 客户信息管理 &gt; 试乘试驾</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1"/>
		<input type="hidden" name="dutyType" id="dutyType" value="${dutyType}"/>
		<table class="table_query" border="0">
			<tbody><tr>
				<td width="12%" class="tblopt"><div align="right">姓名：</div></td>
				<td align="left" width="10%">
					<input type="text" class="middle_txt" id="customer_Name" name="customer_Name" value=""/>
       				
				</td>
				<%
					AclUserBean user=(AclUserBean)request.getAttribute("logonUser");
					if(!user.getDutyType().equals(Constant.DUTY_TYPE_DEALER.toString())){
				%>
				<td align="right" width="10%">选择经销商：</td>
				<td colspan="1" width="25%" align="left">
					<input type="hidden" name="dealerId" size="15" value="" id="dealerId"/>
					<input type="text" class="middle_txt"  name="dealerCodes" size="15" value="" id="dealerCodes"/>
					<c:if test="${dutyType==10431001}">
				      	<input class="mini_btn"  name="button2" type="button" onclick="showOrgDealer('dealerCodes','','true', '${orgId}')" value="..." />
				    </c:if>
				    <c:if test="${dutyType==10431002}">
				      	<input class="mini_btn"  name="button2" type="button" onclick="showOrgDealer3('dealerCodes','','true', '${orgId}')" value="..." />
				    </c:if>
				    <c:if test="${dutyType==10431003}">
				      	<input class="mini_btn"  name="button2" type="button" onclick="showOrgDealer3('dealerCodes','','true', '${orgId}')" value="..." />
				    </c:if>
				    <c:if test="${dutyType==10431004}">
				      	<input class="mini_btn"  name="button2" type="button" onclick="showOrgDealer('dealerCodes','','true', '${orgId}')" value="..." />
				    </c:if>
					<input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCodes');"/>
				</td>
				<%
					}else{
				%>
				 	<td width="30%"></td>
				<%
				 	}
				%>
				<td class="tblopt"><div align="right">机动车驾车号：</div></td>
				<td align="left">
      				<input type="text" class="middle_txt" id="driveNo" name="driveNo" value=""/>
    			</td>
			</tr>
			<tr>
				<td align="center" colspan="6">
					<input type="hidden" name="vehicle_test_id" id="vehicle_test_id"/>
					<input type="button" class="normal_btn" onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" style="border: 1px solid rgb(94, 118, 146); background-color: rgb(238, 240, 252); color: rgb(30, 57, 136); background-position: initial initial; background-repeat: initial initial; "/> 
					<input type="button" class="normal_btn"  value="重 置" onclick="requery();"/>
					<%
						AclUserBean logonUser=(AclUserBean)request.getAttribute("logonUser");
						if(logonUser.getDutyType().equals(Constant.DUTY_TYPE_DEALER.toString())){
					%>
					<input type="button" class="normal_btn" onclick="window.location.href=' <%=contextPath%>/sales/customerInfoManage/VehicleTestDrive/addVehicleTestDriveInit.do '" value=" 新 增 " id="addTest" />
					<%
						}
					%>
				</td>
			</tr>
		</tbody>
	</table>
	 <!-- 查询条件 end -->
 	 <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end --> 
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
	
</div>
<script type="text/javascript">

	var myPage;
	
	var url = "<%=contextPath%>/sales/customerInfoManage/VehicleTestDrive/vehicleTestDriveQuery.json";
	
	var title = null;
	
	var columns = [
				{header: "经销商代码", dataIndex: 'ENTITY_CODE', align:'center'},
				{header: "经销商简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "客户姓名", dataIndex: 'CUSTOMER_NAME', align:'center'},
				{header: "机动车驾车号", dataIndex: 'VEHICLE_DRIVE_NO', align:'center'},
				{header: "颜色", dataIndex: 'COLOR_NAME', align:'center'},
				{header: "联系电话", dataIndex: 'MAIN_PHONE', align:'center'},
				{header: "车型", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "试车日期", dataIndex: 'TEST_DRIVE_DATE', align:'center'},
				{header: "了解途径", dataIndex: 'CODE_DESC', align:'center'},
				{id:'action',header: "操作", walign:'center',idth:70,dataIndex: 'DRIVE_INFO_ID',renderer:myLink}
		      ];
		      
	function myLink(value,meta,record){
		var dutyType=document.getElementById("dutyType").value;
		if(dutyType==<%=Constant.DUTY_TYPE_DEALER%>){
        	return String.format(
        		 "<a href=\"#\" onclick='updateDrive(\""+ value +"\")'>[修改]</a><a href=\"#\" onclick='dropDrive(\""+ value + "\")'>[删除]</a>");
        }else{
        	return String.format(
        		 "<a href=\"#\" onclick='queryDrive(\""+ value +"\")'>[查看]</a>");
        }
    }
    
    //欲购车型内容清空
	function clrTxt(valueId) {
		document.getElementById(valueId).value = '' ;
	}
    //查看信息
    function queryDrive(value){
    	location = '<%=contextPath%>/sales/customerInfoManage/VehicleTestDrive/queryVehicleTestDrive.do?DRIVE_INFO_ID='+value;
    }
	//修改
	function updateDrive(value){
		location = '<%=contextPath%>/sales/customerInfoManage/VehicleTestDrive/updateVehicleTestDriveInit.do?DRIVE_INFO_ID='+value;
	}
	//删除
	function dropDrive(test_drive_id){
		MyConfirm("是否确认删除？",drop,[test_drive_id]);
	}
	
	function drop(test_drive_id){
		makeNomalFormCall('<%=contextPath%>/sales/customerInfoManage/VehicleTestDrive/deleteVehicleTestDrive.json?DRIVE_INFO_ID='+test_drive_id,dropCall,'fm','');
	}

	function dropCall(json) {
	if(json.flag!= null && json.flag== true) {
		MyAlert(" 删除成功！");
		__extQuery__(1);
	} else {
		MyAlert("删除失败！请联系管理员！");
		}
	}
	
	//查询条件重置
	function requery() {
		$('customer_Name').value="";
		$('driveNo').value="";
		$('dealerCodes').value="";
		
	}
</script>    

<div id="checkMsgDiv0" style="visibility: hidden; " class="tipdiv"> 
 <table width="120" height="28" border="0" cellpadding="0" cellspacing="0" id="checkMsgTable">    
  <tbody>
	 <tr>       
	 	<td valign="bottom"><img src="<%=contextPath%>/js/validate/alert_top.gif" width="120" height="6"></td>    
 	</tr>    
 	<tr>       
 		<td valign="top">          
 			<table width="120" border="0" cellpadding="0" cellspacing="0" id="checkMsgTable" style="font:9pt 宋体;">           
				<tbody>
					<tr>                 
						<td width="136" valign="top" id="checkMsgDiv0_msg" background="<%=contextPath%>/js/validate/alert_middle.gif" align="center" style="font:9pt 宋体;">test </td>
			         </tr>          
			     </tbody>
	        </table>      
	    </td>    
	</tr>    
	<tr>     
		<td height="10" valign="top">
			<img src="<%=contextPath%>/js/validate/alert_bottom.gif" width="120"/>
		</td>    
	</tr>  
  </tbody>
</table>
</div>
<div>
	<embed id="lingoes_plugin_object" type="application/lingoes-npruntime-capture-word-plugin" hidden="true" width="0" height="0">
</div>
</body>
</html>
