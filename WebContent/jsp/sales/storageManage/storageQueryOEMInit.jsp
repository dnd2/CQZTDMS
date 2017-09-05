<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@taglib uri="/jstl/cout" prefix="c" %>


<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>库存查询</title>
<script type="text/javascript">
<!--
function download() {
	document.getElementById('fm').action= "<%=request.getContextPath()%>/sales/storageManage/StorageQuery/donwload.json";
	document.getElementById('fm').submit();
}
function txtClr(valueId) {
	document.getElementById(valueId).value = '' ;
}
//-->
</script>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 经销商库存管理 &gt; 经销商库存查询</div>
	<form id="fm" name="fm" method="post">
		<div class="form-panel">
		<h2>查询条件</h2>
		<div class="form-body">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
				<td class="right"><div class="right">选择经销商：</div></td>
				<td width="39%" >
      			  <input name="dealerCode" type="text" id="dealerCode" class="middle_txt" value="" size="20" onclick="showOrgDealer('dealerCode', '', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', '');"/>
                  <!--<c:if test="${dutyType==10431001}">
                    <input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" onclick="showOrgDealer('dealerCode','','true', '${orgId}')" value="..." />
                  </c:if>
                  <c:if test="${dutyType==10431002}">
                    <input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" onclick="showOrgDealer('dealerCode','','true', '${orgId}')" value="..." />
                  </c:if>
                  <c:if test="${dutyType==10431003}">
                    <input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" onclick="showOrgDealer('dealerCode','','true', '${orgId}')" value="..." />
                  </c:if>
                  <c:if test="${dutyType==10431004}">
                    <input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" onclick="showOrgDealer('dealerCode','','true', '${orgId}')" value="..." />
                  </c:if>-->
                  
                  <input class="normal_btn" type="button" value="清空" onclick="txtClr('dealerCode');"/>
    			</td>
			</tr>
			<tr>
				<td class="right">
					<div class="right">
					<input type="radio" checked="checked"  name="flag"  onclick="toChangeMaterial(1);" />
						物料组选择：
					</div>
				</td>
				<td>
					<input type="text" class="middle_txt" id="materialCode" name="materialCode"  value=""  onclick="showMaterialGroup('materialCode','','true','','true');"/>
       				<input type="button" class="normal_btn" onclick="txtClr('materialCode');" value="清 空" id="clrBtn" />
				</td>
			</tr>
			<tr>
				<td class="right">
					<div class="right">
					<input type="radio"  name="flag"  onclick="toChangeMaterial(2);"/>
						物料选择：
					</div>
				</td>
				<td>
					<input type="text" class="middle_txt" id="materialCode__" name="materialCode__"  value=""  readonly="readonly" onclick="showMaterial('materialCode__','','true','true');"/>
					<input type="button" class="normal_btn" onclick="txtClr('materialCode__');" value="清 空" id="clrBtn" />
				</td>
			</tr>
			<tr>
				<td class="right"><div class="right">库存超过：</div></td>
				<td><input type="text" class="short_txt" id="days" name="days" size="10" value="" datatype="1,is_digit,10" />天</td>
			</tr>
			<tr>
				<td class="right"><div class="right">批次号：</div></td>
				<td><input type="text" class="middle_txt" id="batchNo" name="batchNo" value="" /></td>
			</tr>
			<tr>
				<td class="right"><div class="right">VIN：</div></td>
				<td  >
      				<textarea id="vin" name="vin" cols="18" rows="3" ></textarea>
    			</td>
				<td class="table_query_3Col_input" >
					<input type="button" class="normal_btn" onclick="extQuery();" value=" 查  询  " id="queryBtn" /> 
					<input type="button" class="normal_btn" onclick="download() ;" value=" 下  载  " id="downloadBtn" /> 
				</td>
			</tr>

		</table>
		</div>
		</div>
		</form>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>

<script type="text/javascript">
	document.getElementById('downloadBtn').style.display = "none" ;
	var myPage;
	
	var url = "<%=contextPath%>/sales/storageManage/StorageQuery/storageQueryOEM.json?COMMAND=1";
	
	var title = null;
	
	var columns = [
				{header: "经销商", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "车型名称", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "配置", dataIndex: 'PACKAGE_NAME', align:'center'},
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "VIN", dataIndex: 'VIN',renderer:mySelect, align:'center'},
				{header: "发动机号", dataIndex: 'ENGINE_NO',align:'center'},
				{header: "批次号 ", dataIndex: 'BATCH_NO', align:'center'},
				{header: "生产日期", dataIndex: 'PRODUCT_DATE',align:'center'},
				{header: "位置说明", dataIndex: 'VEHICLE_AREA', align:'center'},
				{header: "代管经销商", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "库存状态", dataIndex: 'LIFE_CYCLE', align:'center',renderer:getItemValue},
				{header: "入库日期", dataIndex: 'STORAGE_TIME', align:'center'},
				{header: "库存天数", dataIndex: 'DAY_COUNT', align:'center'}
		      ];
	function mySelect(value,meta,record){
	  	return String.format("<a href=\"#\" onclick='vehicleInfo(\""+value+"\")';>"+value+"</a>");
	}
	function vehicleInfo(value){
		OpenHtmlWindow('<%=contextPath%>/sales/storageManage/VehicleInfo/vehicleInfoQuery.do?vin='+value,700,500);
	}
	function extQuery() {
		document.getElementById('downloadBtn').style.display = "inline" ;
		
		__extQuery__(1);
	}
	function toChangeMaterial(type){
		var materialCode = document.getElementById("materialCode");//物料组
		var materialCode__ = document.getElementById("materialCode__");//物料
		var bt1 = document.getElementById("bt1");
		var bt2 = document.getElementById("bt2");

		//选择物料组
		if(type==1){
			materialCode__.readOnly = true;
			materialCode__.value="";
			materialCode.readOnly = false;
			bt1.disabled  = false;
			bt2.disabled  = true;
		}else{
			materialCode.readOnly = true;
			materialCode.value="";
			materialCode__.readOnly=false;
			bt1.disabled  = true;
			bt2.disabled  = false;
		}
	}
</script>    
</body>
</html>