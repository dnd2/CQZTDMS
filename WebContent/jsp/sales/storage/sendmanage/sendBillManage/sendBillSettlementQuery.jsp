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
<title>运单结算管理 </title>

<script type="text/javascript">
//function doInit(){
	  //初始化时间控件
//}
</script>

</head>
<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>运单结算管理
	</div>
<form name="fm" method="post" id="fm">
<input type="hidden" name="vehicleIds" id="vehicleIds" />
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
  <tr class="csstr" align="center">		
  		<td class="right">产地：</td>
  		<td align="left">
  			<select id="areaId" name="areaId" class="selectlist" onchange="getLogiList()">
  				<option value="">--请选择--</option>
  				<c:forEach items="${areaList}" var="po">
  					<option value="${po.AREA_ID }">${po.AREA_NAME }</option>
  				</c:forEach>
  			</select>
  		</td>
  		<td class="right">承运商：</td>
		<td align="left">
			<select id="logiId" name="logiId" class="u-select">
				<option value="">--请选择--</option>
				<c:forEach items="${logiList }" var="logi" >
					<option value="${logi.LOGI_ID }">${logi.LOGI_NAME }</option>
				</c:forEach>
			</select>
		</td>
		 <td class="right">选择经销商：</td>
		<td align="left">
			<input type="text" maxlength="20"   name="dealerName" class="middle_txt" size="15" value="" id="dealerName"/>
			<input type="hidden"  name="dealerCode" class="middle_txt" size="15" value="" id="dealerCode"/>
			<input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','','true','','','','<%=Constant.DEALER_TYPE_DVS %>,<%=Constant.DEALER_TYPE_DP %>','dealerName');" value="..." />
			<input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCode');clrTxt('dealerName');"/>
		</td>	
 </tr>
 <tr class="csstr" align="center">
		<td class="right">省份：</td>  
		    <td align="left">
	  		<select class="u-select" id="txt1" name="PROVINCE" onchange="_genCity(this,'txt2')"></select>
     	 </td> 
     	 <td class="right">地级市：</td>  
		    <td align="left">
	  		<select class="u-select" id="txt2" name="CITY_ID"></select>
     	 </td>   
		  <td class="right">区县：</td>
		   	   <td align="left" colspan="1">
	  				<input type="text" maxlength="20"  id="COUNTY_ID" datatype="1,is_textarea,100" maxlength="100" name="COUNTY_ID" class="middle_txt"  size="15" />
			 </td> 
 </tr>
  <tr class="csstr" align="center">
		<td class="right">运单号：</td>
		<td align="left">
			<input type="text" maxlength="20"  name="billNo" id="billNo" datatype="1,is_digit_letter,30" maxlength="30" class="middle_txt"/>
		</td>
		<td class="right">VIN：</td>
		<td align="left"><input type="text" maxlength="20"  name="vin" id="vin" datatype="1,is_digit_letter,30" maxlength="30" class="middle_txt"/></td>
			 <td class="right" nowrap="true">运单确认时间：</td>
   <td align="left" nowrap="true">
			<input name="sendStartDate" type="text" maxlength="20"  class="middle_txt" id="sendStartDate" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'sendStartDate', false);" />  	
             &nbsp;至&nbsp;
             <input name="sendEndDate" type="text" maxlength="20"  class="middle_txt" id="sendEndDate" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'sendEndDate', false);" /> 
			
		</td>
		
 </tr>
  <tr class="csstr" align="center" style="display:none;">
  
  <td style="color: #252525;width: 115px;text-align: right" >是否维护城市里程数：
     	 </td> <td align="left"><select class="u-select" id="HAS_DISTANCE" name="hasDistance" >
	  			<option value="">-请选择-</option>
	  			<option value="yes">是</option>
	  			<option value="no">否</option>
	  		</select></td>
	  		</td><td style="color: #252525;width: 115px;text-align: right" >是否结算:
     	 </td> <td align="left"><select class="u-select" id="isSettlement" name="isSettlement" >
	  			<option value="">-请选择-</option>
	  			<option value="yes">是</option>
	  			<option value="no">否</option>
	  		</select></td>
			 <td class="right" nowrap="true">扫描出库时间：</td>
   <td align="left" nowrap="true" colspan="3">
			<input name="lastOutDateStart" type="text" maxlength="20"  class="middle_txt" id="sendStartDate" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'lastOutDateStart', false);" />  	
             &nbsp;至&nbsp;
             <input name="lastOutDateEnd" type="text" maxlength="20"  class="middle_txt" id="LastOutDateEnd" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'lastOutDateEnd', false);" /> 
			
		</td>
		
 </tr>
 <tr align="center">
 		<td colspan="8" align="center">
 		<input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
   		  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="__extQuery__(1);" />   	
   		   <input type="button" id="exportExl" class="normal_btn"  value="导出" onclick="excelExport();" />  
   		  <input type="button" id="createBtn" class="normal_btn" style="width: 80px" value="生成结算单" onclick="toCreateSellteAdvice()"/>  
   		</td>
 </tr>
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
<!--页面列表 begin -->
</form>
<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/storage/sendmanage/SendBillSettlement/sendBillSettlementListQuery.json";
	var title = null;
	var columns = [
					{header: "序号", renderer:getIndex, align:'center'},	
	                {id:'check',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"billIDs\")' />", width:'6%',sortable: false,dataIndex: 'BILL_ID',renderer:myCheckBox},
					{header: "运单号",dataIndex: 'BILL_NO',align:'center'},
					{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
					{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
				//	{header: "发票号",dataIndex: 'INVOICE',align:'center'},
					//{header: "运单生成时间",dataIndex: 'BILL_CRT_DATE',align:'center'},
					//{header: "扫描出库时间",dataIndex: 'OUT_DATE',align:'center'},
					{header: "承运商",dataIndex: 'LOGI_FULL_NAME',align:'center'},
					//{header: "产地",dataIndex: 'AREA_NAME',align:'center'},
					{header: "发运地址",dataIndex: 'ADDRESS',align:'center'},
					{header: "VIN",dataIndex: 'VIN',align:'center'},
					{header: "回厂确认时间",dataIndex: 'BACK_CRM_DATE',align:'center'},
					{header: "运单确认时间",dataIndex: 'CONFIRM_DATE',align:'center'},
					{header: "核定物流时间",dataIndex: 'HD_HOURSE',align:'center'},
					{header: "实际物流时间",dataIndex: 'SJ_HOURSE',align:'center'},
					{header: "超期时间",dataIndex: 'CQ_HOURSE',align:'center'},
					{header: "确认人",dataIndex: 'BACK_CRM_PER',align:'center'},
					{header: "车系",dataIndex: 'SERIES_NAME',align:'center'},
					{header: "车型",dataIndex: 'MODEL_NAME',align:'center'},
					{header: "配置",dataIndex: 'PACKAGE_NAME',align:'center'},
					{header: "颜色",dataIndex: 'COLOR_NAME',align:'center'}
					//{header: "数量",dataIndex: 'VEH_NUM',align:'center'}
		      ];
	//清空数据
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
	function doInit(){
		genLocSel('txt1','','');//支持火狐
		//__extQuery__(1);
	}
	function myCheckBox(value,metaDate,record){
		var billId = record.data.BILL_ID;
		var logiId = record.data.LOGI_ID;
		var areaId = record.data.AREA_ID;
		var vehicleId = record.data.VEHICLE_ID;
		var val = billId + "-" + logiId + "-" + areaId + "-" + vehicleId;
		return String.format("<input type=\"checkbox\" name='billIDs'  value='" + val+ "' />");
		
	}
	function toCreateSellteAdvice(){
		document.getElementsByName("createBtn").disabled = true;
		var billIds = document.getElementsByName("billIDs");
		var addFlag = false;
		for(var i=0; i<billIds.length; i++){
			if(billIds[i].checked){
				addFlag = true;
				break;
			}
		}
		if(!addFlag){
			MyAlert("请选择运单!");
			return false;
		}
		var logiId;
		var areaId;
		for(var i = 0 ; i < billIds.length ; i++){
			if(billIds[i].checked){
				var val = billIds[i].value.split("-");
				logiId = val[1];
				areaId = val[2];
				for(var j = i+1 ; j < billIds.length ; j++){
					if(billIds[j].checked){
						var val_1 = billIds[j].value.split("-");
						var logiId_1 = val_1[1];
						if(logiId != logiId_1){
							MyAlert("请选择同一个物流商下的运单!");
							return false;
						}
					}
				}
			}
		}
		var temp = "";
		var vehicleIds = "";
		for(var i = 0 ; i<billIds.length;i++){
			if(billIds[i].checked){
				var val = billIds[i].value.split("-");
				var val_ = val[0];
				temp = temp + val_ +",";
				var val_2 = val[3];
				vehicleIds = vehicleIds + val_2+",";
			}
		}
		vehicleIds = vehicleIds.substr(vehicleIds,vehicleIds.length - 1);
		document.getElementById("vehicleIds").value = vehicleIds;
		//结算时提示是否存在未维护的出发地和目的地；
		var checkPlaceUrl =  "<%=contextPath%>/sales/storage/sendmanage/SendBillSettlement/checkBillPlace.json?billIds="+temp;
		makeNomalFormCall(checkPlaceUrl, checkPlaceBack, 'fm') ;		
		
	}
	
	function checkPlaceBack(json){
		if(json.str == "ok" ){
			var url = "<%=contextPath%>/sales/storage/sendmanage/SendBillSettlement/checkSendBill.json?billIds="+json.billIds;
			makeNomalFormCall(url, showResult, 'fm') ;
			
		}else{
			MyAlert(json.str);
		}
		

	}
	
	function showResult(json){
		if(json.str != ""){
			MyAlert(json.str);
		}else{
		 var fm = document.getElementById("fm");
		     fm.action = "<%=contextPath%>/sales/storage/sendmanage/SendBillSettlement/toCreateSettleAdvice.do";
		     fm.submit();
		}
	}
	
	function getLogiList(){
		var areaId = document.getElementById("areaId").value;
		 var url = "<%=contextPath%>/sales/storage/sendmanage/SendBillSettlement/getLogiList.json?areaId="+areaId;
		 sendAjax(url,showRes,'fm'); 
	}
	
	function showRes(json){
		var logiId = document.getElementById("logiId");
		logiId.length = 0;
		logiId.options.add(new Option("--请选择--",""));
		if(json.result != ""){
			var value = json.result;
			var res = value.split("--");
			for(var i = 0 ; i < res.length ; i++){
				var temp = res[i].split(",");
				logiId.options[logiId.length] = new Option(temp[1],temp[0]);
			}
		}
	}
	
	function excelExport(){
		  fm.action =  "<%=contextPath%>/sales/storage/sendmanage/SendBillSettlement/sendBillSettlementListQuery.do?type=1";
		  fm.submit();
	}
</script>
</body>
</html>
