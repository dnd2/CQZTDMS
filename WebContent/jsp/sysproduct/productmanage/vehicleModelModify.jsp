<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>车辆型号维护</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：系统管理 > 产品维护 > 车辆型号维护</div>
<form method="POST" name="fm" id="fm">
<input type="hidden" id="vehicleModelId" name="vehicleModelId" value="${po.vehicleModelId }">
  	<table class="table_query" border="0">
		<tr>
			<td  nowrap="nowrap" align="right">车系：</td>
			<td>
				${seriesName }
			</td>
		    <td  nowrap="nowrap" align="right">车辆型号代码：</td>
		    <td  nowrap="nowrap">
		    	<input type="text" class="middle_txt" id="vehicleModelCode" name="vehicleModelCode"  value="${po.vehicleModelCode }"  />
		    </td>
		    <td  nowrap="nowrap" align="right">车辆型号名称：</td>
		    <td  nowrap="nowrap">
		    	<input type="text" class="middle_txt" id="vehicleModelName" name="vehicleModelName" value="${po.vehicleModelName }" />
		    </td>
	  	</tr>
	</table>
	<table class="table_query" id="package_list">
				<tr class="table_query_th">
					<th colspan="5"><img class="nav"
						src="/CHDMS/img/subNav.gif" />&nbsp;配置列表
						<input type="button" class="normal_btn" value="增加" onclick="showMaterialGroup('groupCode','','true','4','true')"/> </th>
				</tr>
				<tr class="table_list_row1" align="center">
					<td width="8%" nowrap="nowrap">序号</td>
					<td width="35%" nowrap="nowrap">配置代码</td>
					<td width="47%" nowrap="nowrap">配置名称</td>
					<td width="10%" nowrap="nowrap">操作</td>
				</tr>
				<c:forEach items="${list }" var="po" varStatus="s">
					<tr class="table_list_row1" align="center">
						<Td width="8%" nowrap="nowrap">${s.index+1 }</Td>
						<td width="35%" nowrap="nowrap">${po.GROUP_CODE }</td>
						<td width="47%" nowrap="nowrap">${po.GROUP_NAME}</td>
						<Td width="10%" nowrap="nowrap">
							<input type='hidden' id='packageId' name='packageIds' value="${po.GROUP_ID }">
							<input type=button value="删除" class="normal_btn" name="remain" onClick="deleteRow(this)">
						</Td>
					</tr>
				</c:forEach>
			</table>
			<div id="mydiv" style="display: none;color: red"></div>
	<table align="center">
	  	<tr>
		    <td colspan="4" align="center">
		      <input name="button2" type="button" class="normal_btn" onclick="save();" value="保存"/>
		      <input name="button" type="button" class="normal_btn" onclick="history.back();" value="返回"/>
		    </td>
	  	</tr>
	</table>
</form>
<script type="text/javascript">
function showMaterialGroup(inputCode ,inputName ,isMulti ,groupLevel, isAllArea)
{
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	if(!groupLevel){ groupLevel = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/showMaterialPackage.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&GROUPLEVEL="+groupLevel+"&ISALLAREA="+isAllArea,850,480);
}

function addPackage(reId,reCode,reName){
	var ids = reId.split(",");
	var codes = reCode.split(",");
	var names = reName.split(",");
	var packageIds = document.getElementsByName("packageIds");
	if(ids.length>0){
		var mytable = document.getElementById("package_list");
		for(var i = 0 ; i < ids.length ; i++){
			var id = ids[i];
			var code = codes[i];
			var name = names[i];
			//没循环插入行一次，判断该行对应配置在页面是否已经存在
			var flag = true;
			if(packageIds.length>0){
				for(var j = 0 ; j< packageIds.length;j++){
					var packageId = packageIds[j].value;	
					if(packageId == id){
						flag = false;
						break;
					}
				}
			}
			if(flag){
				var newTr = mytable.insertRow();//增加一行
				var newTd1 = newTr.insertCell();
				var newTd2 = newTr.insertCell();
				var newTd3 = newTr.insertCell();
				var newTd4 = newTr.insertCell();
				newTr.className = "table_list_row1";
				newTr.align = 'center';
				newTd1.innerHTML = mytable.rows.length-2;
				newTd2.innerText = code;
				newTd3.innerText = name;
				newTd4.innerHTML = "<input type='hidden' id='packageId' name='packageIds' value='"+id+"'>"+'<input type=button value="删除" class="normal_btn" name="remain" onClick="deleteRow(this)">';
			}
		}
	}
}

//删除一行
function deleteRow(obj)
{
	var mytable = document.getElementById("package_list");
	var rowIndex = obj.parentElement.parentElement.rowIndex; 
	mytable.deleteRow(rowIndex);
	//重新排列序号
	 for(i=rowIndex;i<mytable.rows.length;i++){
		 if(i >= 2){
			 mytable.rows[i].cells[0].innerHTML = i-1;
		 }
	 }
}

function save(){
	/**var series = document.getElementById("series").value;
	if(series == null || series == ""){
		MyAlert("请选择车系!");
		return false;
	}**/
	var packageIds = document.getElementsByName("packageIds");
	var vehicleModelId = document.getElementById("vehicleModelId").value;
	if(packageIds == null || packageIds.length == 0){
		MyAlert("请选择配置!");
		return false;
	}else{
		var pid = "";
		for(var i =  0 ; i < packageIds.length ; i++){
			if(i == packageIds.length-1){
				pid += packageIds[i].value;
			}else{
				pid += packageIds[i].value + ",";
			}
		}
		makeCall("<%=request.getContextPath()%>/sysproduct/productmanage/VehicleModelManage/checkPackage.json",makedecide,{pid:pid,vehicleModelId:vehicleModelId});
	}
}
	function makedecide(json){
		if(json.returnValue == "1"){
			MyConfirm("确认修改?",function(){
				makeNomalFormCall("<%=request.getContextPath()%>/sysproduct/productmanage/VehicleModelManage/modifyVehicleModel.json",showResult,'fm');
			});	
		}else{
			var s = json.returnValue;
			var mydiv = document.getElementById("mydiv");
			mydiv.innerHTML = s;
			mydiv.style.display = "block";
		}
	}

	


function showResult(json){
	if(json.value == "1"){
		MyAlert("该车辆型号代码或名称在系统中已经存在!");
		return false;
	}
	if(json.value == "2"){
		MyAlertForFun("修改成功!",function(){
			window.location.href = "<%=request.getContextPath()%>/sysproduct/productmanage/VehicleModelManage/vehicleModelManageInit.do";
		});		
	}
}
</script>
</body>
</html>
