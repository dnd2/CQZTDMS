<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
List list =(List)request.getAttribute("drivers");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>司机车辆绑定 </title>
</head>

<body onload="">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>司机车辆绑定 
	</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>司机车辆绑定</h2>
<div class="form-body">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
<tr class="csstr" align="center"> 
	<td class="right">交接单号：</td> 
	  <td align="left">
		 <input type="text" maxlength="20"  id="billNo" name="billNo" datatype="1,is_digit_letter,30" maxlength="30" class="middle_txt" size="15" />
	  </td>
	  <td class="right">订单号：</td>  
		<td align="left">
		 <input type="text" maxlength="20"  id="orderNo" name="orderNo" datatype="1,is_digit_letter,30" maxlength="30" class="middle_txt" size="15" />
     	 </td>
</tr>
<tr class="csstr" align="center">  
	<td class="right">组板号：</td> 
	  <td align="left" >
		  <input type="text" maxlength="20"  id="boNo" name="boNo" datatype="1,is_digit_letter,30" maxlength="30" class="middle_txt" size="15" />
	  </td>
     	 <td class="right">驾驶员姓名：</td>
   	  <td align="left">
		  <input type="text" maxlength="20"  id="driverName" name="driverName"  class="middle_txt" size="15" />
	 </td>
</tr>
<tr class="csstr" align="center">  
	<td class="right">驾驶员电话：</td> 
	  <td align="left" >
		 <input type="text"  id="driverTel" name="driverTel" datatype="1,is_digit,11" maxlength="11" class="middle_txt"/>
	  </td>
     <td class="right" nowrap="true">绑定日期：</td>
		<td align="left" nowrap="true">
			<input class="short_txt" readonly="readonly"  type="text" id="START_DATE" name="START_DATE" onFocus="WdatePicker({el:$dp.$('START_DATE'), maxDate:'#F{$dp.$D(\'END_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
			<input class="short_txt" readonly="readonly"  type="text" id="END_DATE" name="END_DATE" onFocus="WdatePicker({el:$dp.$('END_DATE'), minDate:'#F{$dp.$D(\'START_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>
		</td>	
	</tr>
	<tr align="center">
	  <td colspan="6" class="table_query_4Col_input" style="text-align: center">
    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="doQuery()" />
  		  <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
    	  <input type="button" id="saveButton" class="normal_btn"  value="绑定" onclick="doSave();" />
    	     	 	
    </td>
  </tr>
</table>
</div>
</div>
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
	var url = "<%=contextPath%>/sales/storage/sendmanage/DriverVehManage/DriverVehManageQuery.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"groupIds\")'/>",sortable: false,dataIndex: 'DTL_ID',renderer:myCheckBox},
				{header: "交接单号",dataIndex: 'BILL_NO',align:'center'},
				{header: "订单号",dataIndex: 'ORDER_NO',align:'center'},
				{header: "组板号",dataIndex: 'BO_NO',align:'center'},
				{header: "驾驶员姓名",dataIndex: 'DRIVER_NAME',align:'center',renderer:selectDriverName},
				{header: "驾驶员电话",dataIndex: 'DRIVER_TEL',align:'center',renderer:selectDriverTel},
				{header: "车型",dataIndex: 'MODEL_NAME',align:'center'},
				{header: "配置",dataIndex: 'PACKAGE_NAME',align:'center'},
				{header: "颜色",dataIndex: 'COLOR_NAME',align:'center'},
				{header: "车架号",dataIndex: 'VIN',align:'center'}//,
				//{header: "操作",dataIndex: 'BO_ID',sortable: false, align:'center',renderer:myLink}
		      ];
	//初始化    
	function doInit(){
		//__extQuery__(1);
	}
	function doQuery(){
		__extQuery__(1);
	}
	//清空数据
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
  	//全选checkbox
	function myCheckBox(value,metaDate,record){
  		//var driverTel=record.data.DRIVER_TEL;
		return String.format("<input type='checkbox' id='groupIds' name='groupIds' value='" + value + "' /><input type='hidden' name='hiddenIds' value='" + value + "' />");
	}
  	//选择驾驶员姓名
	function selectDriverName(value,metaDate,record){
		var strOption="<option value=''>-请选择-</option>";
	     <%
	     	for(int i=0;i<list.size();i++){
	     		Map map=(Map)list.get(i);
	     	%>
	     		
		     		strOption+='<option value=<%=map.get("USER_ID")%>><%=map.get("NAME").toString()%></option>';
	     	<%	
	     	}
	     %>
	     return String.format("<SELECT name='DRIVER_TEL' id='DRIVER_TEL"+record.data.DTL_ID+"' onchange='changeDriverName(this,"+record.data.DTL_ID+")'>"+strOption+"</SELECT>");
	}
	//驾驶员电话
	function selectDriverTel(value,metaDate,record){
		var strOption="<input type='text' name='DRIVER_NAME' id='DRIVER_NAME"+record.data.DTL_ID+"' value='"+value+"' disabled='true'/>";
     	
		return String.format(strOption);
	}
	//级联变换驾驶员姓名
	function changeDriverName(obj,dtlId){
		var dTel=obj.value;
		var dName=document.getElementById("DRIVER_NAME"+dtlId);
		<%
     	for(int i=0;i<list.size();i++){
     		Map map=(Map)list.get(i);
     	%>
     	if(dTel==<%=map.get("USER_ID")%>){
     		dName.value="<%=map.get("HAND_PHONE")%>";
     	}
     	<%	
     	}
     	%>
	}
	//确认绑定
	function doSave(){
		var b=0;
		var arrayObj = new Array(); 
		arrayObj=document.getElementsByName("groupIds");
		var driverTels=document.getElementsByName("DRIVER_TEL");//驾驶员电话
		var errorInfo=0;
		for(var i=0;i<arrayObj.length;i++){
			if(arrayObj[i].checked){
				b=1;//有选中
				if(driverTels[i].value==""){
						errorInfo=1;
				}
			}
			
		}
		if(b==0){
			MyAlert("请选择需要绑定的信息！");
			return ;
		}
		if(errorInfo==1){
			MyAlert("请选择选中记录的驾驶员！");
			return ;
		}
		MyConfirm("确认绑定！",saveModify);	
	}
	
	function saveModify()
	{ 
		disabledButton(["saveButton"],true);
		makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/DriverVehManage/saveDriverVehBind.json",sendBindBack,'fm','queryBtn'); 
	}
	
	function sendBindBack(json)
	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			fm.action = "<%=contextPath%>/sales/storage/sendmanage/DriverVehManage/queryInit.do";
			fm.submit();
		}else if(json.returnValue == 3)
		{
			parent.MyAlert(json.eMsg);
		}else
		{
			disabledButton(["saveButton"],false);
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
</script>
</body>
</html>
