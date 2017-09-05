<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.*"%>

<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<script type="text/javascript">
	function txtClr() {
		document.getElementById("dealerCode").value = '' ;
		document.getElementById("dealerId").value = '' ;
	}
</script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>下级经销商库存查询</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 库存管理 &gt; 下级经销商库存查询</div>
	<form id="fm" name="fm" method="post">
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
				<td width="20%" class="tblopt">
					<div class="right">
					<input type="radio" checked="checked"  name="flag"  onclick="toChangeMaterial(1);" />
						物料组选择：
					</div>
				</td>
				<td width="20%">
					<input type="text" id="materialCode" name="materialCode"  value=""  />
       				<input type="button" id="bt1" value="..." class="mini_btn"  onclick="showMaterialGroup('materialCode','','true','');" />
				</td>
				<td width="20%" class="tblopt">
					<div class="right">
					<input type="radio"  name="flag"  onclick="toChangeMaterial(2);"/>
						&nbsp;&nbsp;&nbsp;物料选择：
					</div>
				</td>
				<td width="40%"> 
					<input type="text" id="materialCode__" name="materialCode__"  value=""  readonly="readonly"/>
       				<input type="button" id="bt2" value="..." class="mini_btn" disabled="disabled" onclick="showMaterial('materialCode__','','true','');" />
				</td>
			</tr>
			<tr>
				<td class="tblopt"><div class="right">库存超过：</div></td>
				<td><input type="text" id="days" name="days" size="10" value="" datatype="1,is_digit,10" />天</td>
				<td class="tblopt"><div class="right">库存状态：</div></td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("vehicle_life",<%=Constant.VEHICLE_LIFE%>,"",true,"u-select","","false",'<%=Constant.VEHICLE_LIFE_01%>,<%=Constant.VEHICLE_LIFE_02%>,<%=Constant.VEHICLE_LIFE_04%>,<%=Constant.VEHICLE_LIFE_06%>');
					</script>
				</td>
			</tr>
			<tr>
				<td class="tblopt"><div class="right">车辆所有者：</div></td>
				<td>
					<select name="vehicleOwn" id="vehicleOwn" onchange="changeWHName(this.value)">
						<option value="">-请选择-</option>
						<option value="01"><%=Constant.VEHICLE_OWN_01 %></option>
						<option value="02"><%=Constant.VEHICLE_OWN_02 %></option>
					</select>
				</td>
				<td class="tblopt"><div class="right">所在仓库：</div></td>
				<td>
					<select id="whName" name="whName" class="u-select">
						<option value="">-请选择-</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class="tblopt"><div class="right">VIN：</div></td>
				<td >
      				<textarea id="vin" name="vin" cols="18" rows="3" ></textarea>
    			</td>
    			<td class="tblopt"><div class="right">锁定状态：</div></td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("lockStatus",<%=Constant.LOCK_STATUS%>,"",true,"u-select","","false",'');
					</script>
				</td>
			</tr>
			<tr>
				<td class="tblopt"><div class="right"><input type="radio" checked="checked"  name="flag"  onclick="toChangeOrder(1);" />上级采购批售单号：</div></td>
				<td><input type="text" id="upDealerOrder" name="upDealerOrder" size="20" value="" datatype="1,is_textarea,35" /></td>
				<td class="tblopt"><div class="right"><input type="radio"  name="flag"  onclick="toChangeOrder(2);" />下级经销商采购批售单号：</div></td>
				<td><input type="text"  readonly="true" id="downDealerOrder" name="downDealerOrder" size="20" value="" datatype="1,is_textarea,35" /></td>
			</tr>
			<tr>
			<td width="20%" class="tblopt">
					<div class="right">
						下级经销商单位名称：
					</div>
				</td>
				<td width="25%">
					<input type="text" readonly="readonly" id="dealerCode" name="dealerCode"  value=""  />
       				<input type="button" id="bt1" value="..." class="mini_btn"  onclick="showNowLevelDealer('dealerCode','dealerId','false','');" />
    				<input type="hidden" value="" name="dealerId" id="dealerId"></input>
    				<input id="mybtn2" type="button" class="normal_btn" onclick="txtClr();" value="清 除" id="clrBtn" />
    			</td>
    			<td>&nbsp;</td>
				<td>
					<input type="button" class="normal_btn" onclick="sumQuery();" value=" 汇总查询  " id="queryBtn1" /> 
					<input type="button" class="normal_btn" onclick="detailQuery();" value=" 明细查询  " id="queryBtn2" /> 
					<input type="button" class="normal_btn" onclick="sumDownLoad();" value="汇总下载" id="queryBtn1" /> 
					<input type="button" class="normal_btn" onclick="detailDownLoad();" value="明细下载" id="queryBtn2" /> 
				</td>
			</tr>
		</table>
		<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    	<!--分页 end -->
	</form>
</div>

<script type="text/javascript">
	var myPage;
	var url;
	var title = null;
	var columns;		
	var calculateConfig;
	function totalQuery(){
		calculateConfig = {subTotalColumns:"ON_WAY,NO_WAY,SUM_NO|MATERIAL_NAME"};
		url = "<%=contextPath%>/sales/storageManage/StorageQuery/StorageQueryDLR_Sum_Lower.json?COMMAND=1";
		columns = [
				{header: "经销商名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
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
		url = "<%=contextPath%>/sales/storageManage/StorageQuery/StorageQueryDLR_Detail_Lower.json?COMMAND=1";
		columns = [
				{header: "经销商名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
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
				{header: "入库日期", dataIndex: 'STORAGE_TIME', align:'center'},
				{header: "库存天数", dataIndex: 'DAY_COUNT', align:'center'}
		      ];

			__extQuery__(1);
	}
	function sumDownLoad(){
		document.getElementById('fm').action="<%=contextPath%>/sales/storageManage/StorageQuery/sumDownLoad_Lower.do";
     	document.getElementById('fm').submit();
	}
	function detailDownLoad(){
		if(document.getElementById("upDealerOrder").value!='' && document.getElementById("dealerCode").value!=''){
			document.getElementById("upDealerOrder").value='';
			document.getElementById("dealerCode").value='';
			document.getElementById("dealerId").value='';
			MyAlert('上级采购批售单号，和下级经销商不能同时作为查询条件！');
		}else if(document.getElementById("downDealerOrder").value!='' && document.getElementById("dealerCode").value!=''){
			document.getElementById("downDealerOrder").value='';
			document.getElementById("dealerCode").value='';
			document.getElementById("dealerId").value='';
			MyAlert('下级级采购批售单号，和下级经销商不能同时作为查询条件！');
		}else{
			document.getElementById('fm').action="<%=contextPath%>/sales/storageManage/StorageQuery/detailDownLoad_Lower.do";
     		document.getElementById('fm').submit();
		}
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
		}else  if(document.getElementById("upDealerOrder").value!='' && document.getElementById("dealerCode").value!=''){
			document.getElementById("upDealerOrder").value='';
			document.getElementById("dealerCode").value='';
			document.getElementById("dealerId").value='';
			MyAlert('上级采购批售单号，和下级经销商不能同时作为查询条件！');
		}else if(document.getElementById("downDealerOrder").value!='' && document.getElementById("dealerCode").value!=''){
			document.getElementById("downDealerOrder").value='';
			document.getElementById("dealerCode").value='';
			document.getElementById("dealerId").value='';
			MyAlert('下级级采购批售单号，和下级经销商不能同时作为查询条件！');
		}else{
			detailQuery_();
		}
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

	function toChangeOrder(type){
		var upDealerOrder = document.getElementById("upDealerOrder");//上级采购批售单号
		var downDealerOrder = document.getElementById("downDealerOrder");//下级采购批售单号
		//var bt1 = document.getElementById("bt1");
		//var bt2 = document.getElementById("bt2");

		//输入采购批售单号
		if(type==1){
			downDealerOrder.readOnly = true;
			downDealerOrder.value="";
			upDealerOrder.readOnly = false;
			//bt1.disabled  = false;
			//bt2.disabled  = true;
		}else{
			upDealerOrder.readOnly = true;
			upDealerOrder.value="";
			downDealerOrder.readOnly=false;
			//bt1.disabled  = true;
			//bt2.disabled  = false;
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
		makeNomalFormCall("<%=contextPath%>/sales/storageManage/StorageQuery/showWHNAME__A.json?ownid="+value,showWHNAMEBack,'fm','queryBtn');
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
	
</script>    
</body>
</html>