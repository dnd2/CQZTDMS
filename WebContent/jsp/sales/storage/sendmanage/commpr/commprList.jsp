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
<title> 商品车出库查询</title>
</head>

<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理> 商品车出库查询
	</div>
<form name="fm" method="post" id="fm">
<div>
<table class="table_list">
 <tr class="table_list_row2" align="center">
  <td align="left">
  	查询方式：
  	<input type="radio" id="only" name="addType" value="only" checked="checked"  onclick="addType1(this);">VIN</input>
	<input type="radio" id="more" name="addType" value="more" onclick="addType1(this);">时间段</input>  	 	
    </td>
</tr>
</table>
</div>
<!-- 查询条件 begin -->
<div id="onlyTr" >
<table class="table_list" >
 <tr class="table_list_row2" align="center">
  <td class="right">VIN：</td>
		   	   <td align="left">
	  				<input type="text" maxlength="20"  id="vin" name="vin" class="middle_txt"  size="15" />
			 </td> 
			  <td align="left">
    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="_function(1);" />    	 	
    </td>
</tr>
</table>
</div>
<div id="moreTr"  style="display: none">
<table class="table_list">
  <tr class="table_list_row2" align="center">
   <td class="right" nowrap="true">日期：</td>
		<td align="left" nowrap="true">
			<input name="BILL_STARTDATE" type="text" maxlength="20"  class="middle_txt" id="BILL_STARTDATE" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'BILL_STARTDATE', false);" />  	
             &nbsp;至&nbsp;
             <input name="BILL_ENDDATE" type="text" maxlength="20"  class="middle_txt" id="BILL_ENDDATE" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'BILL_ENDDATE', false);" /> 
		</td>
		 <td class="right">产地：</td>
		   	   <td align="left">
	  				 <select name="YIELDLY" id="YIELDLY" class="selectlist"  onchange="getGroup();">
					 <option value="">--请选择--</option>
							<c:if test="${list!=null}">
								<c:forEach items="${list}" var="list">
									<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
								</c:forEach>
					</c:if>
	  		</select>
			 </td> 
			 <td class="right">车系：</td>  
		    <td align="left">
	  		<select name="GROUP_ID" id="GROUP_ID" class="selectlist" >
	  			<option value="">-请选择-</option>
	  			<c:if test="${list_vchl!=null}">
					<c:forEach items="${list_vchl}" var="list">
						<option value="${list.GROUP_ID }">${list.GROUP_NAME }</option>
					</c:forEach>
				</c:if>
	  		</select>
     	 </td> 
</tr>
<tr class="table_list_row2" align="center">
	<td class="right">承运商：</td> 
	  <td align="left">
		 <select name="LOGI_NAME_SEACH" id="LOGI_NAME_SEACH" class="selectlist" >
		 	<option value="">-请选择-</option>
				<c:if test="${list_logi!=null}">
					<c:forEach items="${list_logi}" var="list_logi">
						<option value="${list_logi.LOGI_ID}">${list_logi.LOGI_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
	  </td>
<td class="right">流水号：</td>
		   	   <td align="left">
	  				<input type="text" maxlength="20"  id="SD_NUMBER" name="SD_NUMBER" class="middle_txt"  size="15" />
			 </td>
			 <td class="right">订单类型：</td>
		   	  
		   	   <td  align="left"><script>
    			genSelBoxExp("ORDER_TYPE",<%=Constant.ORDER_TYPE%>,"-1",true,"u-select",'',"false",'');
				</script></td>
			
</tr>
  <tr class="table_list_row2" align="center">
		 <td class="right" colspan="6">
		 <input type="button" class="u-button u-reset" id="resetButton"  value="重置" onclick="_reset();" />
    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="_functionDate(1);" />  
    	  <input type="button" id="queryBtn" class="normal_btn"  value="导出" onclick="_functionDate(2);" />   	 	
    </td>
</tr>
</table>
<div id="moreTwoTr">
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</div>
</div>
</form>
<table border="1" cellpadding="3" cellspacing="0" width="100%" id="tabresult" style="display: none" class="table_list">
	<tr class="table_list_row2">
		<td colspan="4" class="table_query_4Col_input" style="text-align: center" class="table_list_row2"><font size="3">商品车出库查询详细明细</font></td>
	</tr>
	<tr class="table_list_row2">
		<td>VIN：</td>
		<td  colspan="3"><span id="vinV"></span></td>
	</tr>
	<tr class="table_list_row2">
		<td>经销商名称：</td>
		<td><span id="dear_name"></span></td>
		<td>省份：</td>
		<td><span id="region_name"></span></td>
	</tr>
	<tr class="table_list_row2">
		<td>城市：</td>
		<td><span id="region_name1"></span></td>
		<td>地区：</td>
		<td><span id="region_name2"></span></td>
	</tr>
	
	<tr class="table_list_row2">
		<td>车系：</td>
		<td><span id="ser_name"></span></td>
		<td>物料：</td>
		<td><span id="mat_name"></span></td>
	</tr>
	<tr class="table_list_row2">
		<td>入库时间：</td>
		<td><span id="org_date"></span></td>
		<td>出库时间：</td>
		<td><span id="out_date"></span></td>
	</tr>
		<tr class="table_list_row2">
		<td>承运商：</td>
		<td><span id="logi_name"></span></td>
		<td>承运车队：</td>
		<td><span id="car_item"></span></td>
	</tr>
	</table>
	<table border="1" cellpadding="3" cellspacing="0" width="100%" id="tabresult1" style="display: none" class="table_list">
	<tr class="table_list_row2">
		<td colspan="4" class="table_query_4Col_input" style="text-align: center"><font size="4" color="red">查无此数据！</font></td>
	</tr>
	</table>
<!--页面列表 begin -->

<script type="text/javascript" >
var myPage;
//查询路径           
var url = "<%=contextPath%>/sales/storage/sendmanage/CommprManage/CommprQuery.json";
var title = null;
var columns = [
	           	
				{header: "序号",align:'center',renderer:getIndex},
				{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "省份",dataIndex: 'REGION_NAME',align:'center'},
				{header: "城市",dataIndex: 'REGION_NAME1',align:'center'},
				{header: "地区",dataIndex: 'REGION_NAME2',align:'center'},
				{header: "VIN",dataIndex: 'VIN',align:'center'},
				{header: "车系",dataIndex: 'SERIES_NAME',align:'center'},
				{header: "物料",dataIndex: 'MATERIAL_CODE',align:'center'},
				{header: "入库时间",dataIndex: 'ORG_STORAGE_DATE',align:'center'},
				{header: "出库时间",dataIndex: 'OUT_DATE',align:'center'},
				{header: "承运商",dataIndex: 'LOGI_NAME',align:'center'},
				{header: "承运车队",dataIndex: 'CAR_TEAM',align:'center'},
				{header: "流水号",dataIndex: 'SD_NUMBER',align:'center'},
				{header: "是否中转",dataIndex: 'ISZZ',align:'center'}
		      ];
function _functionDate(type){
	var sdNumber=document.getElementById("SD_NUMBER");
	if(sdNumber.value!="" && !isNumber(sdNumber.value)){
		sdNumber.value="";
		sdNumber.focus();
		MyAlert("流水号请输出数字！");
		return ;
	}
	if(type==2){
		fm.action="<%=contextPath%>/sales/storage/sendmanage/CommprManage/CommprQuery.json?common=2";  
	   	fm.submit();
	}else{
	  __extQuery__(1);
	  document.getElementById("moreTwoTr").style.display="";
	}
}
function _function(type){
	var vin=document.getElementById("vin").value.trim();
	if(vin==""){
		MyAlert("请输入查询信息的VIN");
		return ;
	}
	if(vin.length!=17){
		MyAlert("输入的VIN号有误！");
		return ;
	}
	makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/CommprManage/CommprQuery.json",commprBack,'fm','queryBtn'); 	
}
function commprBack(json)
{
	if(json.returnValue == 1)
	{
		valueInnerHTML("dear_name",json.valueMap.DEALER_NAME);
		valueInnerHTML("region_name",json.valueMap.REGION_NAME);
		valueInnerHTML("region_name1",json.valueMap.REGION_NAME1);
		valueInnerHTML("region_name2",json.valueMap.REGION_NAME2);
		valueInnerHTML("vinV",json.valueMap.VIN);
		valueInnerHTML("ser_name",json.valueMap.SERIES_NAME);
		valueInnerHTML("mat_name",json.valueMap.MATERIAL_CODE);
		valueInnerHTML("org_date",json.valueMap.ORG_STORAGE_DATE);
		valueInnerHTML("out_date",json.valueMap.OUT_DATE);
		valueInnerHTML("logi_name",json.valueMap.LOGI_NAME);
		valueInnerHTML("car_item",json.valueMap.CAR_TEAM);
		document.getElementById("tabresult").style.display="";
		document.getElementById("tabresult1").style.display="none";
	}else{
		document.getElementById("tabresult1").style.display="";
		document.getElementById("tabresult").style.display="none";
	}
}
function valueInnerHTML(obj,objValue){
	 document.getElementById(obj).innerHTML=objValue;
}
function addType1(va){
	if(va.value=="only"){
		document.getElementById("BILL_STARTDATE").value="";
		document.getElementById("BILL_ENDDATE").value="";
		document.getElementById("GROUP_ID").value="";
		document.getElementById("YIELDLY").value="";
		document.getElementById("LOGI_NAME_SEACH").value="";
		document.getElementById("SD_NUMBER").value="";
		document.getElementById("ORDER_TYPE").value="";
		document.getElementById("onlyTr").style.display="";
		document.getElementById("moreTr").style.display="none";
		document.getElementById("tabresult").style.display="none";
		document.getElementById("tabresult1").style.display="none";
	}else{
		document.getElementById("vin").value="";
		document.getElementById("moreTr").style.display="";
		document.getElementById("onlyTr").style.display="none";
		document.getElementById("tabresult").style.display="none";
		document.getElementById("tabresult1").style.display="none";
		document.getElementById("moreTwoTr").style.display="none";
	}
}
function doInit(){
	//日期控件初始化
}
//重置
function _reset(){
	document.getElementById("BILL_STARTDATE").value="";
	document.getElementById("BILL_ENDDATE").value="";
	document.getElementById("GROUP_ID").value="";
	document.getElementById("YIELDLY").value="";
	document.getElementById("LOGI_NAME_SEACH").value="";
	document.getElementById("SD_NUMBER").value="";
	document.getElementById("ORDER_TYPE").value="";
}
//获取车系
function getGroup(){
	var areaId = document.getElementById("YIELDLY").value;
		var url = "<%=contextPath%>/sales/storage/storagebase/SpecialFareSet/getVhclMsg.json";
		makeCall(url, showGroupList, {
			yieldly : areaId
		});
}
function showGroupList(json) {
	var obj = document.getElementById("GROUP_ID");
	obj.options.length = 0;
	obj.options[0] = new Option("-请选择-", "");
	for ( var i = 0; i < json.groupList.length; i++) {
		obj.options[i + 1] = new Option(json.groupList[i].GROUP_NAME,
				json.groupList[i].GROUP_ID + "");

	}
}
</script>
</body>
</html>
