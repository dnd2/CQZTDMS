<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
	Map<String, Object> vehicleInfo = (Map<String, Object>)request.getAttribute("vehicleInfo");
%>
<%@taglib uri="/jstl/cout" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
<!--
	function doInit(){
   		  //初始化时间控件
   		selList__Ware('${receiver}','${orderOrgId}','${dealerId} ');
	}

	function setDisTrue(arguments) {
		var aBtn = arguments ;
		var iLen = aBtn.length ;
	
		for(var i=0; i<iLen; i++) {
			document.getElementById(arguments[i]).disabled = true ;
		}
	}

	function setDisFalse(arguments) {
		var aBtn = arguments ;
		var iLen = aBtn.length ;
	
		for(var i=0; i<iLen; i++) {
			document.getElementById(arguments[i]).disabled = false ;
		}
	}
	//-->	
</script>
<script type="text/javascript">
<!--
function ChangeDateToString(){  
	//var sNowTime = document.getElementById("sys_date__").value ;
	//var aNowTime = sNowTime.split(",") ;
	
   // var Year = aNowTime[0] ;
    //var Month = aNowTime[1] ;
    //var Day = aNowTime[2] ;
    //var Hours = parseInt(aNowTime[6]) ;
	var myDate = new Date();
	//CurrentDate=myDate.toLocaleDateString(); 
    var Year = myDate.getFullYear(); 
    var Month = myDate.getMonth()+1;  
    if(Month<10){
    	Month="0"+Month;
    }
    var Day = myDate.getDate(); 
    var Hours =myDate.getHours();
    
    var CurrentDate = Year + "-" + Month + "-" + Day ;         
    
    //document.all.t1.innerText = CurrentDate ;
    // document.all.t2.innerText = CurrentDate ;
    document.getElementById('t2').value=CurrentDate;
    document.getElementById('t1').value=CurrentDate;
	document.getElementById('arrive_time').value = Hours ; 
}  

function changeFleet() {
	var sWarehouseName = "" ;
	var sWarehouseId = "" ;
	var i = 0;
	<c:forEach var="W_warehouse" items="${wareList__A }">
		sWarehouseName =sWarehouseName+'${W_warehouse.WAREHOUSE_NAME}' + "," ;
		sWarehouseId = sWarehouseId + '${W_warehouse.WAREHOUSE_ID}' +"," ;
		i++ ;
	</c:forEach>


	
	document.getElementById('fm').warehouse__.options.length=i;
	for(var j=0; j<i; j++) {
		document.getElementById('fm').warehouse__.options[j].value=sWarehouseId.split(',')[j];
		document.getElementById('fm').warehouse__.options[j].text=sWarehouseName.split(',')[j];
	}
}

function submitAction(){
	document.getElementById("checkBtn").disabled=true;
	document.getElementById("add").disabled=true;
	var vehicleId = document.getElementById("vehicleId").value;
	makeNomalFormCall('<%=contextPath%>/sales/storageManage/CheckVehicle/checkSubmit.json?vehicleId='+vehicleId,showResult,'fm') ;
}

function showResult(json){
	if(json){
		var vin = json.vin;
		if(json.returnValue == '2'){
			MyAlert(vin + " : 此车辆已进行过车辆验收，请勿重复操作!");
		}else{
			checkInit();
		}
	}
	
	setDisFalse("button") ;
}

function checkInit(){
	document.getElementById('fm').action="<%=contextPath%>/sales/storageManage/CheckVehicle/CheckVehicleInit.do";
	document.getElementById('fm').submit();
}

function checkSubmit(){
	if(submitForm('fm')){
		var describe_Values = document.all['describe'];
		var part_Values = document.all['part'];
		if(describe_Values){
			if(describe_Values.length){
				for(var i=0; i<describe_Values.length;i++){
					if(describe_Values[i].value.replace(/(^\s*)|(\s*$)/g, "")!="" && part_Values[i].value.replace(/(^\s*)|(\s*$)/g, "")==""){
						MyAlert("请填写质损部位!");
						return;
					}
					if(describe_Values[i].value.replace(/(^\s*)|(\s*$)/g, "")=="" && part_Values[i].value.replace(/(^\s*)|(\s*$)/g, "")!=""){
						MyAlert("请填写质损描述!");
						return;
					}
				}
			}else{
				if(describe_Values.value.replace(/(^\s*)|(\s*$)/g, "")!="" && part_Values.value.replace(/(^\s*)|(\s*$)/g, "")==""){
					MyAlert("请填写质损部位!");
					return;
				}
				if(describe_Values.value.replace(/(^\s*)|(\s*$)/g, "")=="" && part_Values.value.replace(/(^\s*)|(\s*$)/g, "")!=""){
					MyAlert("请填写质损描述!");
					return;
				}
			}
		}
		var sFlag = document.getElementById('warehouse__').options.length ;
		if (sFlag == 0) {
			MyAlert("请添加仓库!");
			return false ;
		}
		MyConfirm("是否入库?",submitAction);
	}
}
//-->
</script>
<title>验收入库</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="ChangeDateToString();changeFleet(); "> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
		&nbsp;当前位置：  库存管理 &gt; 代理商库存管理 &gt; 验收入库</div>
	<form id="fm" name="fm" method="post"  enctype="multipart/form-data" >
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			 <th colspan="11"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;入库车辆信息</th>
			<tr>
				<td width="20%" class="tblopt"><div align="right">底盘号：</div></td>
				<td>
					${vehicleInfo.VIN }
				</td>
				<td width="20%" class="tblopt"><div align="right">发车日期：</div></td>
				<td>
					${vehicleInfo.DELIVERY_DATE }
				</td>
			</tr>
			<tr>
				<td width="20%" class="tblopt"><div align="right">车型：</div></td>
				<td>
					${vehicleInfo.MODEL_NAME}
				</td>
				<td width="20%" class="tblopt"><div align="right">发动机号：</div></td>
				<td>
					${vehicleInfo.ENGINE_NO }
				</td>
			</tr>
			<tr>
				<td width="20%" class="tblopt"><div align="right">颜色：</div></td>
				<td>
					${vehicleInfo.COLOR }
				</td>
				<td width="20%" class="tblopt"><div align="right">发车仓库：</div></td>
				<td>
					${vehicleInfo.WAREHOUSE_NAME }
				</td>
			</tr>
			<tr>
				<td width="20%" class="tblopt"><div align="right">操作时间：</div></td>
				<td>
					<input readonly="readonly" id="t2" value="" type="text" class="middle_txt" datatype="0,is_date,10" />
				</td>
				<td width="20%" class="tblopt"></td>
				<td width="30%"  align=left class=datadetail>
				</td>
			</tr>
			<tr>
				<td width="20%" class="tblopt"><div align="right">实际到车日期：</div></td>
				<td>
				 <input name="arrive_date" readonly="readonly" id="t1" value="" type="text" class="middle_txt" datatype="1,is_date,10" onFocus="WdatePicker({el:$dp.$('t1'), maxDate:''})"  style="cursor: pointer;width: 80px;">
             	<!--  <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" onclick="showcalendar(event, 't1', false);"/> -->
				</td>
				<td width="20%" class="tblopt"><div align="right">实际到车时间：</div></td>
				<td width="30%"  align=left class=datadetail>
					<select id="arrive_time" name="arrive_time" class="u-select">
					<%
						for(int i=1;i<=24;i++){
					%>
					<option value="<%=i %>"><%=i %></option>
					<%		
						}
					%>
		            
		          </select>
		          时 </td>
			</tr>
			<!--<tr>
				<td width="20%" class="tblopt"><div align="right">送车人员：</div></td>
				<td>
					<input type="text" id="transport_person" name="transport_person" datatype="0,is_digit_letter_cn,15" maxlength="15" />
				</td>
				<td width="20%" class="tblopt"><div align="right">验收单号：</div></td>
				<td>
					<input type="text" id="inspection_no" name="inspection_no" datatype="0,is_textarea,25" maxlength="25" />
				</td>
			</tr>-->
			
			<tr>
				<td width="20%" class="tblopt"><div align="right">验收人员：</div></td>
				<td>
					<input type="text" id="inspection_person"  class="middle_txt" name="inspection_person" datatype="0,is_digit_letter_cn,15" maxlength="15" />
				</td>
				<td width="20%" class="tblopt"><div align="right">车辆所在位置：</div></td>
				<td>
			
					<select id="warehouse__" name="warehouse__" class="u-select">
  					</select>
				</td>
			</tr>
			<tr>
				<td class=datatitle align=right><div align="right">备注：</div></td>
				<td class=datadetail align=left colspan=3>
					<input type="text" id="remark" name="remark" rows="2" cols="50"   maxlength="500" class="middle_txt"style="width: 50%;height:50px" />
					<input type="hidden" id="vehicleId" name="vehicleId" value="${vehicleInfo.VEHICLE_ID}" />
				</td>
			</tr>
			<TR>
			<TD class=datatitle align=right><div align="right">是否质损：</div></TD>
			<TD class=datadetail colSpan=3 align=left> 
				<input  type="radio" name="IS_DAMAGE"  id="IS_DAMAGE_1" value="<%=Constant.IS_DAMAGE_1%>" onclick="modifyStatus()"/>是
				<input  type="radio" name="IS_DAMAGE" id="IS_DAMAGE_0" value="<%=Constant.IS_DAMAGE_0%>" onclick="modifyStatus1()" checked="checked"/>否</TD>
				
			</TR>
		</table>
		<table class="table_edit" width="90%" border="0"  id="file" style="display:none" >
			<tr align="center">
		        <th width="600px" align="center" style=""><div align="center">损坏描述</div></th>
		        <th width="600px"align="center" style=""><div align="center">损坏部位</div></th>
		        <th><div align="center" style=""><input name="button2" type="button" class="normal_btn" value="新增" onclick="addTblRow();" id="add"  disabled="disabled"/></div></th>
	      	</tr>
		</table>
		<table class="table_query" width="90%" border="0" align="center">
			<tr>
				<td align="center" >
					<input type="hidden" id="macthId" name="macthId" value="<%=request.getParameter("macthId") %>"/>
					<input type="hidden" id="dlvryDtlId" name="dlvryDtlId" value="<%=request.getParameter("dlvryDtlId") %>"/>
					<input type="hidden" id="receiverId" name="receiverId" value="<%=request.getParameter("receiverId") %>"/>
					<input name="button" type="button" id="checkBtn" class="normal_btn" onclick="checkSubmit();" value="检查完成" /> 
					<input type="button" name="button3" class="normal_btn" onclick="history.back();" value="返回"/>
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
<script type="text/javascript" >
	function modifyStatus(){
		document.getElementById("file").style.display="inline";
		document.getElementById("add").disabled=false;
		document.getElementById("file").disabled=false;
		var del_buttons = document.getElementsByName("del_button");
		for(i=0;i<del_buttons.length;i++){
			del_buttons[i].disabled=false;
		}
		
	}
	function modifyStatus1(){
		document.getElementById("file").style.display = "none";
		document.getElementById("add").disabled=true;
		document.getElementById("file").disabled=true;
		var del_buttons = document.getElementsByName("del_button");
		for(i=0;i<del_buttons.length;i++){
			del_buttons[i].disabled=true;
		}
	}
	function addTblRow() {
		var tbl = document.getElementById('file');
		var rowObj = tbl.insertRow(tbl.rows.length);

		var cell1 = rowObj.insertCell(0);
		var cell2 = rowObj.insertCell(1);
		var cell3 = rowObj.insertCell(2);
		
		cell1.innerHTML = '<TD align="center"><div align="center"><input type="text"  class="middle_txt" maxlength="200" name="describe" style="width: 90%" /></div></TD>';
		cell2.innerHTML = '<TD align="center"><div align="center"><input type="text"  class="middle_txt" maxlength="200" name="part" style="width: 90%" /></div></TD>';
		cell3.innerHTML = '<TD align="center"><div align="center"><input name="del_button" class="normal_btn" type="button" value="删除" onclick="deleteTblRow(this);" /></div></TD></TR>';
	}
	function deleteTblRow(obj) {
		var idx = obj.parentElement.parentElement.parentElement.rowIndex;
		var tbl = document.getElementById('file');
		tbl.deleteRow(idx);		
	}
 </script>    
</body>
</html>