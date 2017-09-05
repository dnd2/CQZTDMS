<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 接车 入库 </title>

</head>

<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>仓库管理> 接车入库
	</div>
<form name="fm" method="post" id="fm">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
<tr class="csstr" align="center">	
	  <td class="right" width="15%"><input type="radio" checked="checked" name="flag" onclick="toChangeMaterial(1);" />选择物料组：</td>
      <td align="left">
      	<input type="text" maxlength="20"  class="middle_txt" name="groupCode" size="15"  value="" id="groupCode" />
		<input name="button1" id="button1" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','','false','');" value="..." />
		<input class="normal_btn" type="button" value="清空" onclick="clrTxt('groupCode');"/>
      </td>
	  <td class="right" width="15%"><input type="radio" name="flag" onclick="toChangeMaterial(2);" />选择物料：</td>
      <td align="left">
      	<input type="text" maxlength="20"  class="middle_txt" name="materialCode" size="15"  value="" id="materialCode"/>
		<input name="button2" id="button2"  type="button" class="mini_btn" onclick="showMaterial('materialCode','','false');" value="..." disabled="disabled"/>
		<input class="normal_btn" type="button" value="清空" onclick="clrTxt('materialCode');"/>
      </td>
 </tr>
 <tr class="csstr" align="center">
	  <td class="right">库区：</td>  
		    <td class="table_query_2Col_input">
	  		<select name="AREA_NAME" id="AREA_NAME" class="u-select" onchange="roadCheckBox();" >
	  			<option value="">-请选择-</option>
	  			<c:if test="${list_An!=null}">
					<c:forEach items="${list_An}" var="list">
						<option value="${list.AREA_ID }">${list.AREA_NAME }</option>
					</c:forEach>
				</c:if>
	  		</select>
     	 </td>   
		  <td class="right">库道：</td>
		   	   <td class="table_query_2Col_input">
	  		<select name="ROAD_NAME" id="ROAD_NAME" class="u-select" onchange="sitCheckBox();" >
	  			<option value="">-请选择-</option>
	  			<c:if test="${list_Rn!=null}">
					<c:forEach items="${list_Rn}" var="list">
						<option value="${list.ROAD_ID }">${list.ROAD_NAME }</option>
					</c:forEach>
				</c:if>
	  		</select>
			 </td> 
 </tr>
 <tr class="csstr" align="center">
 <td class="right">库位：</td>
		   	   <td class="table_query_2Col_input">
	  		<select name="SIT_NAME" id="SIT_NAME" class="u-select">
	  			<option value="">-请选择-</option>
	  			<c:if test="${list_Sn!=null}">
					<c:forEach items="${list_Sn}" var="list">
						<option value="${list.SIT_ID}">${list.SIT_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
			 </td> 
			  <td class="right">存库超过：</td>
			 <td align="left">
		  <input type="text" maxlength="20"  id="STORAGE_DAYS" name="STORAGE_DAYS" class="middle_txt" size="15" />天
	  	</td>  
 </tr>
  <tr class="csstr" align="center">
	<td class="right" nowrap="true">下线日期：</td>
		<td align="left" nowrap="true">
			<input name="OFFLINE_STARTDATE" type="text" maxlength="20"  class="middle_txt" id="OFFLINE_STARTDATE" readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'OFFLINE_STARTDATE', false);" />  
             &nbsp;至&nbsp;
             <input name="OFFLINE_ENDDATE" type="text" maxlength="20"  class="middle_txt" id="OFFLINE_ENDDATE" readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'OFFLINE_ENDDATE', false);" />  
		</td>
		<td class="right" nowrap="true">入库日期：</td>
		<td align="left" nowrap="true">
			<input name="ORG_STORAGE_STARTDATE" type="text" maxlength="20"  class="middle_txt" id="ORG_STORAGE_STARTDATE" readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'ORG_STORAGE_STARTDATE', false);" /> 
             &nbsp;至&nbsp;
             <input name="ORG_STORAGE_ENDDATE" type="text" maxlength="20"  class="middle_txt" id="ORG_STORAGE_ENDDATE" readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'ORG_STORAGE_ENDDATE', false);" /> 
		</td>	 
		 
  </tr> 
  <tr class="csstr" align="center">
	<td class="right" nowrap="true">VIN:</td>
	<td colspan="3" align="left">
			<textarea  cols="50" rows="3" name="VIN"  id="VIN" ></textarea>
		</td>
  </tr> 
  <tr align="center">
  <td colspan="4" class="table_query_4Col_input" style="text-align: center">
    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="__extQuery__(1);" />   	
    	  <input type="button" id="queryBtn" class="normal_btn"  value="接车入库" onclick="add();" />   	
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
	//查询路径           
	var url = "<%=contextPath%>/sales/storage/storagemanage/ReceivingStorage/receivingStorageQuery.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "车型名称",dataIndex: 'MODEL_NAME',align:'center'},
				{header: "配置",dataIndex: 'PACKAGE_NAME',align:'center'},
				{header: "物料代码",dataIndex: 'MATERIAL_CODE',align:'center'},
				{header: "物料名称",dataIndex: 'MATERIAL_NAME',align:'center'},
				{header: "VIN",dataIndex: 'VIN',align:'center',renderer:myVIN},
				{header: "发动机号",dataIndex: 'ENGINE_NO',align:'center'},
				{header: "生产日期",dataIndex: 'PRODUCT_DATE',align:'center'},
				{header: "下线日期",dataIndex: 'OFFLINE_DATE',align:'center'},
				{header: "库区",dataIndex: 'AREA_NAME',align:'center'},
				{header: "库道",dataIndex: 'ROAD_NAME',align:'center'},
				{header: "库位",dataIndex: 'SIT_NAME',align:'center'},
				{header: "库存状态",dataIndex: 'LIFE_CYCLE',align:'center',renderer:getItemValue},
				{header: "入库日期",dataIndex: 'ORG_STORAGE_DATE',align:'center'},
				{header: "库存天数",dataIndex: 'STORAGE_DAYS',align:'center'},
				{header: "操作",sortable: false, dataIndex: 'VEHICLE_ID', align:'center',renderer:myLink}
		      ];


	//设置超链接
	function myLink(value,meta,record)
	{
  		return String.format("<a href='javascript:void(0);' onclick='sel(\""+value+"\")'>[打印标签]</a>");
	}
	//详细页面
	function sel(value)
	{
		window.open("<%=contextPath%>/sales/oemstorage/OemStoragePrintList/commandPrintInfo.do?reqId="+val1);

	// window.location.href='<%=contextPath%>/sales/storage/storagebase/LogisticsManage/editLogisticsInit.do?Id='+value;  
	}
	//设置VIN超链接
	function myVIN(value,meta,record)
	{
  		return String.format("<a href='javascript:void(0);' onclick='VIN(\""+value+"\")'>"+value+"</a>");
	}
	//详细页面
	function VIN(value)
	{
		//OpenHtmlWindow('<%=contextPath%>/sales/storage/storagemanage/ReceivingStorage/queryVinDetail.do?VIN='+value,800,500);
		OpenHtmlWindow('<%=contextPath%>/sales/storageManage/VehicleInfo/vehicleInfoQuery.do?vin='+value,700,500);
	}
	
	//初始化    
	function doInit(){
		//日期控件初始化
		//__extQuery__(1);
	}
	//跳转接车入库页面
	function add()
	{
		fm.action = "<%=contextPath%>/sales/storage/storagemanage/ReceivingStorage/receivingStorageMainInit.do";
		fm.submit();
	}
	//清空数据
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
    function toChangeMaterial(parm){
		if(parm==1){
			document.getElementById("button1").disabled="";
			document.getElementById("button2").disabled="disabled";
		}else{
			document.getElementById("button1").disabled="disabled";
			document.getElementById("button2").disabled="";
		}
    }
	//动态改变库区下拉框的值(库道)
	function roadCheckBox(){
		var areaId = document.getElementById("AREA_NAME").value;	
		var url = "<%=request.getContextPath()%>/sales/storage/storagebase/ReservoirPositionManage/getPositionList.json";
		makeCall(url,showRoadList,{areaId:areaId});
	}
	function showRoadList(json){
		var obj = document.getElementById("ROAD_NAME");
		obj.options.length = 0;
		roadName = new Array() ;
		roadId = new Array() ;
		for(var i=0;i<json.reList.length;i++){
			roadName[i] = json.reList[i].ROAD_NAME ;
			roadId[i] = json.reList[i].ROAD_ID ;
			obj.options[i]=new Option(json.reList[i].ROAD_NAME, json.reList[i].ROAD_ID + "");
			
		}
	}
	  //动态改变库区下拉框的值(库位)
	function sitCheckBox(){
		var areaId = document.getElementById("AREA_NAME").value;
		var roadId = document.getElementById("ROAD_NAME").value;	
		var url = "<%=request.getContextPath()%>/sales/storage/storagebase/ReservoirPositionManage/getSitList.json";
		makeCall(url,showSitList,{roadId:roadId,areaId:areaId});
	}
	function showSitList(json){
		var obj = document.getElementById("SIT_NAME");
		obj.options.length = 0;
		sitName = new Array() ;
		sitId = new Array() ;
		for(var i=0;i<json.reList.length;i++){
			sitName[i] = json.reList[i].SIT_NAME ;
			sitId[i] = json.reList[i].SIT_ID ;
			obj.options[i]=new Option(json.reList[i].SIT_NAME, json.reList[i].SIT_ID + "");
			
		}
	}
</script>
</body>
</html>
