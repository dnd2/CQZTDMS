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
<script type="text/javascript" src="<%=request.getContextPath() %>/jsp/sales/storage/storagemanage/storageUtil/storageUtil.js"></script>
<title> 车辆位置调整 </title>

</head>

<body>
<!--  	<OBJECT style="display:none;" ID="tecPrint" CLASSID="CLSID:82139C0A-F89F-4EB4-9DDF-38798CBF53B6" CODEBASE="<%=request.getContextPath()%>/jsp/sales/storage/storagemanage/printUtilCab/tecPrinter.CAB#version=1,0,0,0"></OBJECT>-->
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>仓库管理> 车辆位置调整
	</div>
<form name="fm" method="post" id="fm">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
<tr class="csstr" align="center">	
	  <td class="right" width="15%"><input type="radio" checked="checked" name="flag" onclick="toChangeMaterial(1);" />选择物料组：</td>
      <td align="left">
      	<input type="text" class="middle_txt" name="groupCode" size="15"  value="" id="groupCode" />
		<input name="button1" id="button1" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','','true','');" value="..." />
		<input class="normal_btn" type="button" value="清空" onclick="clrTxt('groupCode');"/>
      </td>
	  <td class="right" width="15%"><input type="radio" name="flag" onclick="toChangeMaterial(2);" />选择物料：</td>
      <td align="left">
      	<input type="text" class="middle_txt" name="materialCode" size="15"  value="" id="materialCode"/>
		<input name="button2" id="button2"  type="button" class="mini_btn" onclick="showMaterial('materialCode','','true');" value="..." disabled="disabled"/>
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
	  		</select>
			 </td> 
 </tr>
 <tr class="csstr" align="center">
 <td class="right">库位：</td>
		   	   <td class="table_query_2Col_input">
	  		<select name="SIT_NAME" id="SIT_NAME" class="u-select">
	  			<option value="">-请选择-</option>
	  		</select>
			 </td> 
			  <td class="right">存库超过：</td>
			 <td align="left">
		  <input type="text" id="STORAGE_DAYS" name="STORAGE_DAYS" class="middle_txt" datatype="1,is_digit,20" maxlength="20" size="15" />天
	  	</td>  
 </tr>
  <tr class="csstr" align="center">
	<td class="right" nowrap="true">下线日期：</td>
		<td align="left" nowrap="true">
			<input name="OFFLINE_STARTDATE" type="text" class="middle_txt" id="OFFLINE_STARTDATE" readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'OFFLINE_STARTDATE', false);" />  
             &nbsp;至&nbsp;
             <input name="OFFLINE_ENDDATE" type="text" class="middle_txt" id="OFFLINE_ENDDATE" readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'OFFLINE_ENDDATE', false);" />  
		</td>
		<td class="right" nowrap="true">入库日期：</td>
		<td align="left" nowrap="true">
			<input name="ORG_STORAGE_STARTDATE" type="text" class="middle_txt" id="ORG_STORAGE_STARTDATE" readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'ORG_STORAGE_STARTDATE', false);" /> 
             &nbsp;至&nbsp;
             <input name="ORG_STORAGE_ENDDATE" type="text" class="middle_txt" id="ORG_STORAGE_ENDDATE" readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'ORG_STORAGE_ENDDATE', false);" /> 
		</td>	 
  </tr> 
  <tr class="csstr" align="center">
	<td class="right" nowrap="true">VIN:</td>
	<td colspan="1" align="left">
			<textarea  cols="30" rows="2" name="VIN"  id="VIN" ></textarea>
		</td>
	<td class="right" width="15%">产地：</td> 
	  <td align="left">
		 <select name="YIELDLY" id="YIELDLY" class="u-select" >
		 <option value="">--请选择--</option>
				<c:if test="${list!=null}">
					<c:forEach items="${list}" var="list">
						<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
		</td> 	
  </tr> 
  <tr align="center">
  <td colspan="4" class="table_query_4Col_input" style="text-align: center">
  		  <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="__extQuery__(1);" />   		
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
	var url = "<%=contextPath%>/sales/storage/storagemanage/VehicleSiteAdjust/vehicleSiteAdjustQuery.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "操作",sortable: false, dataIndex: 'VEHICLE_ID', align:'center',renderer:myLink},
				{header: "锁定状态",dataIndex:'LOCK_STATUS',renderer:getItemValue},
				{header: "车型",dataIndex: 'MODEL_NAME',align:'center'},
				{header: "物料代码",dataIndex: 'MATERIAL_CODE',align:'center'},
				{header: "物料名称",dataIndex: 'MATERIAL_NAME',align:'center'},
				{header: "VIN",dataIndex: 'VIN',align:'center',renderer:myVin},
				{header: "库区",dataIndex: 'AREA_NAME',align:'center'},
				{header: "库道",dataIndex: 'ROAD_NAME',align:'center'},
				{header: "库位",dataIndex: 'SIT_NAME',align:'center'},
				{header: "发动机号",dataIndex: 'ENGINE_NO',align:'center'},
				{header: "生产日期",dataIndex: 'PRODUCT_DATE',align:'center'},
				{header: "下线日期",dataIndex: 'OFFLINE_DATE',align:'center'},
				{header: "库存状态",dataIndex: 'LIFE_CYCLE',align:'center',renderer:getItemValue},
				{header: "入库日期",dataIndex: 'ORG_STORAGE_DATE',align:'center'},
				{header: "库存天数",dataIndex: 'STORAGE_DAYS',align:'center'},
				{header: "最新备注",dataIndex: 'LAST_ADJUST_REMARK',align:'center'}
		      ];


	//设置超链接
	function myLink(value,meta,record)
	{
			var vin=record.data.VIN;
		    var aName=record.data.AREA_NAME;
	        var rName=record.data.ROAD_NAME;
	        var sName=record.data.SIT_NAME;
	        var perCode=record.data.PER_CODE;
	        var sitCode=aName+"-"+rName+"-"+sName+"*"+((perCode==null || perCode=='null')?'':perCode);
  		return String.format("<a href='javascript:void(0);' onclick='add(\""+value+"\")'>[调整]</a><a href='javascript:void(0);' onclick='printSitCode(\""+vin+"\",\""+sitCode+"\")'>[打印条码]</a>");
	}
	 function myVin(value,meta,record){
		  return String.format("<a href='javascript:void(0);' onclick='vinInfo(\""+value+"\")';>"+value+"</a>");
	}
	function vinInfo(value){
		OpenHtmlWindow('<%=contextPath%>/sales/storage/storagemanage/ReceivingStorage/queryVinDetail.do?vin='+value,600,300);
	}
	//初始化    
	function doInit(){
		//日期控件初始化
		todayDate('ORG_STORAGE_STARTDATE','yyyy-MM-dd');
		todayDate('ORG_STORAGE_ENDDATE','yyyy-MM-dd');
		//__extQuery__(1);
	}
	//跳转接车入库页面
	function add(value)
	{
		window.open("<%=contextPath%>/sales/storage/storagemanage/VehicleSiteAdjust/vehicleSiteAdjustMainInit.do?Id="+value);
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
    //打印库位码
	function printSitCode(vin,sitcode){	
		try {
			tecPrint.printCode(vin,sitcode,'0060','0038','0060','0165','0040','0120','0040','0170'); 
		}catch(e) {
			MyAlert('打印异常,请检查条码打印机');
		}
	}
</script>
</body>
</html>
