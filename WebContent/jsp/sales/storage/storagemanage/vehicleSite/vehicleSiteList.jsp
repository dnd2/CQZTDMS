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
<title> 详细车籍查询 </title>

</head>

<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>仓库管理>详细车籍查询</div>
<%--	<OBJECT style="display:none;" ID="tecPrint" CLASSID="CLSID:82139C0A-F89F-4EB4-9DDF-38798CBF53B6" CODEBASE="<%=request.getContextPath()%>/jsp/sales/storage/storagemanage/printUtilCab/tecPrinter.CAB#version=1,0,0,0"></OBJECT>--%>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>详细车籍查询</h2>
<div class="form-body">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
 <tr class="csstr" align="center">	
	  <td class="right" width="15%">仓库：</td>
      <td align="left">
      	 <select name="YIELDLY" id="YIELDLY" class="u-select" >
		 <option value="">-请选择-</option>
				<c:if test="${list!=null}">
					<c:forEach items="${list}" var="list">
						<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
      </td>
	  <td class="right" width="15%">选择车系：</td>
      <td align="left">
      	<input size=15 name="groupCode" id="groupCode" type="text" maxlength="20"  class="middle_txt" style="width:80px;">
        <input class="mini_btn" onclick="showMaterialGroup('groupCode' ,'' ,'true' ,'2', 'false')" value="..." type=button id=button1>
        <input class="normal_btn" onclick="clrTxt('groupCode');" value=清空 type="button">
      </td>
 </tr>
 <tr class="csstr" align="center">	
	<td class="right">库存状态：</td> 
	  <td align="left">
		 <label>
				<script type="text/javascript">
						genSelBoxExp("LIFE_CYCLE",<%=Constant.VEHICLE_LIFE%>,"<%=Constant.VEHICLE_LIFE_02%>",true,"u-select","onchange='checkStatus(this.value);'","false",'');
					</script>
			</label>
	  </td> 
	  <td class="right" width="15%">选择车型：</td>
      <td align="left">
      	<input size=15 name="modelCode" id="modelCode" type="text" maxlength="20"  class="middle_txt" style="width:80px;">
        <input class="mini_btn" onclick="showMaterialGroup('modelCode' ,'' ,'true' ,'3', 'false')" value="..." type=button id=button1 >
        <input class="normal_btn" onclick="clrTxt('modelCode');" value=清空 type="button">
      </td>
 </tr>
<tr class="csstr" align="center">	
	  <td class="right" nowrap="true">生产日期：</td>
		<td align="left" nowrap="true">
			<input class="short_txt" readonly="readonly"  type="text" id="productDateStart" name="productDateStart" onFocus="WdatePicker({el:$dp.$('productDateStart'), maxDate:'#F{$dp.$D(\'productDateEnd\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
			<input class="short_txt" readonly="readonly"  type="text" id="productDateEnd" name="productDateEnd" onFocus="WdatePicker({el:$dp.$('productDateEnd'), minDate:'#F{$dp.$D(\'productDateStart\')}'})"  style="cursor: pointer;width: 80px;"/>
		</td>
	  <td class="right" width="15%">选择配置：</td>
      <td align="left">
      	<input size=15 name="packageCode" id="packageCode" type="text" maxlength="20"  class="middle_txt" style="width:80px;">
	        <input class="mini_btn" onclick="showMaterialGroup('packageCode' ,'' ,'true' ,'4', 'false')" value="..." type=button id=button1 >
	        <input class="normal_btn" onclick="clrTxt('packageCode');" value=清空 type="button">
      </td>
	  
 </tr>
  <tr class="csstr" align="center">
	<td class="right" nowrap="true">下线日期：</td>
	<td align="left" nowrap="true">			
		<input class="short_txt" readonly="readonly"  type="text" id="OFFLINE_STARTDATE" name="OFFLINE_STARTDATE" onFocus="WdatePicker({el:$dp.$('OFFLINE_STARTDATE'), maxDate:'#F{$dp.$D(\'OFFLINE_ENDDATE\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
		<input class="short_txt" readonly="readonly"  type="text" id="OFFLINE_ENDDATE" name="OFFLINE_ENDDATE" onFocus="WdatePicker({el:$dp.$('OFFLINE_ENDDATE'), minDate:'#F{$dp.$D(\'OFFLINE_STARTDATE\')}'})"  style="cursor: pointer;width: 80px;"/>
	</td>		 
	<td class="right" width="15%">选择物料：</td>
      <td align="left">
      	<input size=15 type="text" maxlength="20"  id="materialCode" name="materialCode" class="middle_txt" style="width:80px;">
    	<input id="button2" class="mini_btn" onclick="showMaterial('materialCode' ,'' ,'true' ,null, 'false');" value="…" type=button >
    	<input class="normal_btn" onclick="clrTxt('materialCode');" value=清空 type=button>
      </td>
  </tr> 
  <tr class="csstr" align="center">
	<td class="right" nowrap="true">入库日期：</td>
	<td align="left" nowrap="true">			
		<input class="short_txt" readonly="readonly"  type="text" id="ORG_STORAGE_STARTDATE" name="ORG_STORAGE_STARTDATE" onFocus="WdatePicker({el:$dp.$('ORG_STORAGE_STARTDATE'), maxDate:'#F{$dp.$D(\'ORG_STORAGE_ENDDATE\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
		<input class="short_txt" readonly="readonly"  type="text" id="ORG_STORAGE_ENDDATE" name="ORG_STORAGE_ENDDATE" onFocus="WdatePicker({el:$dp.$('ORG_STORAGE_ENDDATE'), minDate:'#F{$dp.$D(\'ORG_STORAGE_STARTDATE\')}'})"  style="cursor: pointer;width: 80px;"/>
	</td>
	<td class="right" nowrap="true">VIN：</td>
	<td align="left">
		<input type="text" name="VIN" id="VIN"  class="middle_txt"/><!-- datatype="1,is_digit,17" -->
	</td>	
  </tr> 
  <tr align="center">
  	<td colspan="4" class="table_query_4Col_input" style="text-align: center">
		<input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="__extQuery__(1);" />
		<input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
		<input type="button" id="queryBtn1" class="normal_btn"  value="导出"  onclick="exportVin1();" /> 
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
	var url = "<%=contextPath%>/sales/storage/storagemanage/VehicleSite/vehicleSiteQuery.json";
	var title = null;
	var columns = [
	           	
				{header: "序号",align:'center',renderer:getIndex},
				{header: "库存状态",dataIndex: 'LIFE_CYCLE',align:'center',renderer:getItemValue},
				{header: "锁定状态",dataIndex: 'LOCK_STATUS',align:'center',renderer:getItemValue},
				{header: "仓库",dataIndex: 'AREA_NAME',align:'center'},
				{header: "车型",dataIndex: 'MODEL_NAME',align:'center'},
				{header: "配置",dataIndex: 'PACKAGE_NAME',align:'center'},
				{header: "物料编码",dataIndex: 'MATERIAL_CODE',align:'center'},
				{header: "颜色",dataIndex: 'COLOR_NAME',align:'center'},
				{header: "VIN",dataIndex: 'VIN',align:'center',renderer:myVin},
				{header: "生产日期",dataIndex: 'PRODUCT_DATE',align:'center'},
				{header: "下线日期",dataIndex: 'OFFLINE_DATE',align:'center'},
				{header: "入库日期",dataIndex: 'ORG_STORAGE_DATE',align:'center'},
				{header: "出库日期",dataIndex: 'FACTORY_DATE',align:'center'},
				{header: "实销日期",dataIndex: 'PURCHASED_DATE',align:'center'},
				{header: "发动机号",dataIndex: 'GEARBOX_NO',align:'center'},
				{header: "变速箱号",dataIndex: 'ENGINE_NO',align:'center'}
		      ];
	//初始化    
	function doInit(){
		//日期控件初始化
		todayDate('ORG_STORAGE_STARTDATE','yyyy-MM-dd');
		todayDate('ORG_STORAGE_ENDDATE','yyyy-MM-dd');
		__extQuery__(1);
	}
	//清空数据
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
	//清空数据
	function txtClr(valueId1,valueId2) {
		document.getElementById(valueId1).value = '' ;
		document.getElementById(valueId2).value = '' ;
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
    function checkStatus(value)
    {
    	if(value == <%=Constant.VEHICLE_LIFE_03 %>)
    	{
    		document.getElementById("mydiv").style.display = "inline";
    		document.getElementById("mydiv1").style.display = "inline";
    		
    	}
    	else
    	{
    		document.getElementById("mydiv").style.display = "none";
    		document.getElementById("mydiv1").style.display = "none";
    	}
    }
    function printS(value,meta,record){
          var lf=record.data.LIFE_CYCLE;
          if(lf==<%=Constant.VEHICLE_LIFE_02 %>){
			  var aName=record.data.AREA_NAME;
			  var rName=record.data.ROAD_NAME;
			  var sName=record.data.SIT_NAME;
			  var perCode=record.data.PER_CODE;
			  var sitCode=aName+"-"+rName+"-"+sName+"*"+((perCode==null || perCode=='null')?'':perCode);
			  return String.format("<a href='javascript:void(0);' onclick='xx(\""+value+"\",\""+sitCode+"\")'>[打印条码]</a>");
          }else{
        	  return String.format(" ");
          }
    }
    function myVin(value,meta,record){
	  	return String.format("<a href='javascript:void(0);' onclick='vinInfo(\""+value+"\")';>"+value+"</a>");
	}
	function vinInfo(value){
		OpenHtmlWindow('<%=contextPath%>/sales/storage/storagemanage/ReceivingStorage/queryVinDetail.do?vin='+value,800,370);
	}
	//打印库位码
	function xx(vin,sitcode){	
		try {
			tecPrint.printCode(vin,sitcode,'0060','0038','0060','0165','0040','0120','0040','0170'); 
		}catch(e) {
			MyAlert('打印异常,请检查条码打印机');
		}
	}
	function exportVin(){
		fm.action = '<%=contextPath%>/sales/storage/storagemanage/VehicleSite/vehicleSiteQuery.json?_type=2' ;
		fm.submit();
	}
	function exportVin1(){
		fm.action = '<%=contextPath%>/sales/storage/storagemanage/VehicleSite/vehicleSiteQuery.json?_type=3' ;
		fm.submit();
	}
</script>
</body>
</html>
