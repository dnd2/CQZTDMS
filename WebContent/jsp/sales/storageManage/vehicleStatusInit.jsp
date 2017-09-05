<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.*"%>

<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆状态查询</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 库存管理 &gt; 车辆状态查询</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
		<tr>
				<td width="20%" class="tblopt">
					<div class="right">
<!--					<input type="radio" checked="checked"  name="flag"  onclick="toChangeMaterial(1);" />-->
						物料组选择：
					</div>
				</td>
				<td width="20%">
					<input type="text" class="middle_txt" id="materialCode" name="materialCode"  value=""  />
					<input type="hidden" name="materialName" size="20" id="materialName" value="" />
       				<input type="button" id="bt1" value="..." class="mini_btn"  onclick="showMaterialGroup('materialCode','materialName','true','');" />
				</td>
<!--				<td width="20%" class="tblopt">-->
<!--					<div class="right">-->
<!--					<input type="radio"  name="flag"  onclick="toChangeMaterial(2);"/>-->
<!--						&nbsp;&nbsp;&nbsp;物料选择：-->
<!--					</div>-->
<!--				</td>-->
<!--				<td width="40%"> -->
<!--					<input type="text" class="middle_txt" id="materialCode__" name="materialCode__"  value=""  readonly="readonly"/>-->
<!--       				<input type="button" id="bt2" value="..." class="mini_btn" disabled="disabled" onclick="showMaterial('materialCode__','','true','');" />-->
<!--				</td>-->
			</tr>
			<tr>
<!--				<td class="tblopt"><div class="right">库存超过：</div></td>-->
<!--				<td><input type="text" class="short_txt" id="days" name="days" size="10" value="" datatype="1,is_digit,10" />天</td>-->
				<td class="tblopt"><div class="right">车辆状态：</div></td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("vehicle_life",<%=Constant.VEHICLE_LIFE%>,"",true,"u-select","","false",'<%=Constant.VEHICLE_LIFE_01%>,<%=Constant.VEHICLE_LIFE_02%>,<%=Constant.VEHICLE_LIFE_06%>');
					</script>
					
				</td>
				<td class="right">车身颜色：</td>
		        <td align="left"><input type="text" name="bodyColor" id="bodyColor"/></td>
			</tr>
			<tr>
<!--				<td class="tblopt"><div class="right">所在仓库：</div></td>-->
<!--				<td>-->
<!--					<select id="whName" name="whName" >-->
<!--						<option value="">-请选择-</option>-->
<!--					</select>-->
<!--				</td>-->
<!--				<td class="tblopt" class="right">入库时间:</td>-->
<!--				<td>-->
<!--					<input name="storStartDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);" />-->
<!--	            		&nbsp;至&nbsp;-->
<!--	            		<input name="storEndDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);" />-->
<!--				</td>-->
			</tr>
			<tr>
				<td class="tblopt"><div class="right">VIN：</div></td>
				<td >
      				<textarea id="vin" name="vin" cols="25" rows="5" ></textarea>
    			</td>
    			<td class="tblopt"><div class="right">锁定状态：</div></td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("lockStatus",<%=Constant.LOCK_STATUS%>,"",true,"u-select","","false",'');
					</script>
				</td>
			</tr>
			<tr>
				<td class="tblopt"></td>
				<td >
    			</td>
    			<td>&nbsp;</td>
				<td>
<!--					<input type="button" class="normal_btn" onclick="sumQuery();" value=" 汇总查询  " id="queryBtn1" /> -->
					<input type="button" class="normal_btn" onclick="detailQuery();" value=" 明细查询  " id="queryBtn2" /> 
<!--					<input type="button" class="normal_btn" onclick="sumDownLoad();" value="汇总下载" id="queryBtn1" /> -->
<!--					<input type="button" class="normal_btn" onclick="detailDownLoad();" value="明细下载" id="queryBtn2" /> -->
				</td>
			</tr>
		</table>
		<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    	<!--分页 end -->
	</form>
</div>

<script type="text/javascript"><!--
	var myPage;
	var url;
	var title = null;
	var columns;		
	var calculateConfig;
	function totalQuery(){
		calculateConfig = {subTotalColumns:"ON_WAY,NO_WAY,SUM_NO|MATERIAL_NAME"};
		url = "<%=contextPath%>/sales/storageManage/StorageQuery/StorageQueryDLR_Sum.json?COMMAND=1";
		columns = [
				{header: "车系", dataIndex: 'SERIES_NAME', align:'center'},
				{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "配置", dataIndex: 'PACKAGE_NAME', align:'center'},
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "在途数量", dataIndex: 'ON_WAY', align:'center'},
				{header: "在库数量", dataIndex: 'NO_WAY', align:'center'},
				{header: "合计", dataIndex: 'SUM_NO', align:'center'}
		      ];
		__extQuery__(1);
	}
	
	function detailQuery_(){
		calculateConfig = {};
		url = "<%=contextPath%>/sales/storageManage/VehicleStatusQuery/vehicleStatusQuery.json?COMMAND=1";
		columns = [
			{header: "经销商", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "车型名称", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "配置", dataIndex: 'PACKAGE_NAME', align:'center'},
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				//{header: "批次号", dataIndex: 'BATCH_NO', align:'center'},
				{header: "车身颜色", dataIndex: 'BODY_COLOR', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center',renderer:mySelect},
				{header: "发动机号", dataIndex: 'ENGINE_NO', align:'center'},
				{header: "生产日期", dataIndex: 'PRODUCT_DATE', align:'center'},
				{header: "出厂日期", dataIndex: 'FACTORY_DATE', align:'center'},
				//{header: "位置说明", dataIndex: 'WAREHOUSE_NAME', align:'center'},
				{header: "库存状态", dataIndex: 'LIFE_CYCLE', align:'center',renderer:getItemValue},
				{header: "锁定状态", dataIndex: 'LOCK_STATUS', align:'center',renderer:getItemValue}
				//{header: "验收入库日期", dataIndex: 'STORAGE_TIME', align:'center'},
				//{header: "库存天数", dataIndex: 'DAY_COUNT', align:'center'}
		      ];
		
		__extQuery__(1);
	}
	function mySelect(value,meta,record){
	  	return String.format("<a href=\"#\" onclick='vehicleInfo(\""+value+"\")';>"+value+"</a>");
	}
	function vehicleInfo(value){
		OpenHtmlWindow('<%=contextPath%>/sales/storageManage/VehicleInfo/vehicleStatusInfoQuery.do?vin='+value,700,500);
	}
	function sumDownLoad(){
				document.getElementById('fm').action="<%=contextPath%>/sales/storageManage/StorageQuery/sumDownLoad.json";
     			document.getElementById('fm').submit();
		}
	function detailDownLoad(){
				document.getElementById('fm').action="<%=contextPath%>/sales/storageManage/StorageQuery/detailDownLoad.json";
     			document.getElementById('fm').submit();
		}
	function sumQuery(){
		var days = document.getElementById("days").value;
		if(days && (days.search("^-?\\d+$")!=0)){
			MyAlert("请正确输入库存天数");
			return false;
		}else{
			totalQuery();
		}
	}
	function detailQuery(){
		//var days = document.getElementById("days").value;
		//if(days && (days.search("^-?\\d+$")!=0)){
		//	MyAlert("请正确输入库存天数");
		//	return false;
		//}else{
			
	//	}
		detailQuery_();
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
	
	function sumQuery1(){
		var days = document.getElementById("days").value;
		if(days && (days.search("^-?\\d+$")!=0)){
			MyAlert("请正确输入库存天数");
			return false;
		}
		var sum = document.getElementById("iframe");
		var materialCode = document.getElementById("materialCode").value;
		var days = document.getElementById("days").value;
		var vin = document.getElementById("vin").value;
		sum.src = "<%=contextPath%>/sales/storageManage/StorageQuery/StorageQueryDLR_Sum.do?materialCode="+materialCode+"&days="+days+"&vin="+vin;
	}
	function detailQuery1(){
		var days = document.getElementById("days").value;
		if(days && (days.search("^-?\\d+$")!=0)){
			MyAlert("请正确输入库存天数");
			return false;
		}
		var detail = document.getElementById("iframe");
		detail.src = "<%=contextPath%>/jsp/sales/storageManage/storageQueryDLR_Detail.jsp";
	}
	
	//车辆所有者和仓库的联动
	function changeWHName(value){
		makeNomalFormCall("<%=contextPath%>/sales/storageManage/StorageQuery/showWHNAME.json?ownid="+value,showWHNAMEBack,'fm','queryBtn');
	}
	
	//联动回调函数json.partInfoSet.length
	function showWHNAMEBack(json){
		var obj = document.getElementById("whName");
		obj.options.length = 0;
		obj.options.add(new Option("-请选择-" , ""));
		if(json.list){
			for(var i=0;i<json.list.length;i++){
				var varItem = new Option(json.list[i].WAREHOUSE_NAME , json.list[i].WAREHOUSE_ID+"");
				obj.options.add(varItem);
			}
		}
	}
	changeWHName('01');
	function doInit(){
		  
	}
	
--></script>    
</body>
</html>