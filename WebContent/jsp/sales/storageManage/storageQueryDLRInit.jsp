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
<title>库存查询</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 库存管理 &gt; 库存查询</div>
	<form id="fm" name="fm" method="post">
				<div class="form-panel">
				<h2> 库存查询</h2>
				<div class="form-body">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
		<!--<tr>
			<td class="right">选择业务范围：</td>
			<td align="left">
				<select name="areaId" class="u-select">
				<option value="">---请选择---</option>
					<c:forEach items="${areaList}" var="po">
						<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
					</c:forEach>
				</select>
				<input type="hidden" name="dealerId" id="dealerId" />
				<input type="hidden" name="area_id" id="area_id" value="" /></td>
			<td></td>
		</tr>
			--><tr>
				<td  class="right">
					<input type="radio" checked="checked"  name="flag"  onclick="toChangeMaterial(1);" />
						物料组选择：
				</td>
				<td >
					<input type="text" class="middle_txt" id="materialCode" name="materialCode"  value=""   onclick="showMaterialGroup('materialCode','materialName','true','','true');"/>
					<input type="hidden" name="materialName" size="20" id="materialName" value="" />
				</td>
				<td class="right">
					<input type="radio"  name="flag"  onclick="toChangeMaterial(2);"/>
						物料选择：
				</td>
				<td > 
					<input type="text" class="middle_txt" id="materialCode__" name="materialCode__"  disabled="disabled" value=""  readonly="readonly" onclick="showMaterial('materialCode__','','true','true');" />
				</td>
			</tr>
			<tr>
				<td class="right">库存超过：</td>
				<td><input type="text" class="short_txt" id="days" name="days" size="10" value="" datatype="1,is_digit,10" />天</td>
				<td class="right">库存状态：</td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("vehicle_life",<%=Constant.VEHICLE_LIFE%>,"",true,"","","false",'<%=Constant.VEHICLE_LIFE_01%>,<%=Constant.VEHICLE_LIFE_02%>,<%=Constant.VEHICLE_LIFE_04%>,<%=Constant.VEHICLE_LIFE_06%>');
					</script>
					
				</td>
			</tr>
			<tr>
				<td class="right">所在仓库：</td>
				<td>
					<select id="whName" name="whName" class="u-select">
						<option value="">-请选择-</option>
					</select>
				</td>
				<td class="right">入库时间:</td>
				<td>
						<input name="storStartDate" id="t1" value=""  type="text" class="short_txt" 
		            			onFocus="WdatePicker({el:$dp.$('t1'), maxDate:'#F{$dp.$D(\'t2\')}'})"  style="cursor: pointer;width: 80px;"/>
		            	至
		            	<input name="storEndDate" id="t2" value="" type="text" class="short_txt" 
		            			onFocus="WdatePicker({el:$dp.$('t2'), minDate:'#F{$dp.$D(\'t1\')}'})" style="cursor: pointer;width: 80px;"/>
				</td>
			</tr>
			<tr>
				<td class="right">VIN：</td>
				<td >
      				<textarea id="vin" name="vin" cols="18" rows="3" class="form-control" style="width:140px"></textarea>
    			</td>
    			<td class="right">锁定状态：</td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("lockStatus",<%=Constant.LOCK_STATUS%>,"",true,"","","false",'');
					</script>
				</td>
			</tr>
			<tr>

				<td colspan="4" class="center">
					<input type="button" class="normal_btn" onclick="sumQuery();" value=" 汇总查询  " id="queryBtn1" /> 
					<input type="button" class="normal_btn" onclick="detailQuery();" value=" 明细查询  " id="queryBtn2" /> 
					<input type="button" class="normal_btn" onclick="sumDownLoad();" value="汇总下载" id="queryBtn1" /> 
					<input type="button" class="normal_btn" onclick="detailDownLoad();" value="明细下载" id="queryBtn2" /> 
				</td>
			</tr>
		</table>

    	</div>
    	</div>
	</form>
		<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    	<!--分页 end -->
</div>

<script type="text/javascript">
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
		url = "<%=contextPath%>/sales/storageManage/StorageQuery/StorageQueryDLR_Detail.json?COMMAND=1";
		columns = [
				{header: "车型名称", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "配置", dataIndex: 'PACKAGE_NAME', align:'center'},
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "批次号", dataIndex: 'BATCH_NO', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "发动机号", dataIndex: 'ENGINE_NO', align:'center'},
				{header: "生产日期", dataIndex: 'PRODUCT_DATE', align:'center'},
				{header: "位置说明", dataIndex: 'WAREHOUSE_NAME', align:'center'},
				{header: "经销商", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "库存状态", dataIndex: 'LIFE_CYCLE', align:'center',renderer:getItemValue},
				{header: "锁定状态", dataIndex: 'LOCK_STATUS', align:'center',renderer:getItemValue},
				{header: "验收入库日期", dataIndex: 'STORAGE_TIME', align:'center'},
				{header: "库存天数", dataIndex: 'DAY_COUNT', align:'center'}
		      ];
		
		__extQuery__(1);
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
		var days = document.getElementById("days").value;
		if(days && (days.search("^-?\\d+$")!=0)){
			MyAlert("请正确输入库存天数");
			return false;
		}else{
			detailQuery_();
		}
	}	

	function toChangeMaterial(type){
		var materialCode = document.getElementById("materialCode");//物料组
		var materialCode__ = document.getElementById("materialCode__");//物料

		//选择物料组
		if(type==1){
			materialCode.disabled  = false;
			materialCode__.value="";
			materialCode__.disabled  = true;
		}else{
			materialCode__.disabled  = false;
			materialCode.value="";
			materialCode.disabled  = true;
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
	
</script>    
</body>
</html>