<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>


<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>发运分派修改 </title>

</head>

<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理&gt;发运管理&gt;发运分派管理&gt;发运信息修改</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2>发运信息修改</h2>
	<div class="form-body">
	  <TABLE class=table_query align=center id="moneyTable">
	   <tr>
	    <td class="right" width="15%">发运仓库：</td>  
		    <td align="left">
	  		<select name="sendWareId" id="sendWareId" class="u-select" >
				 	<option value="">--请选择--</option>
						<c:if test="${list!=null}">
							<c:forEach items="${list}" var="list">
								<option value="${list.WAREHOUSE_ID}">${list.WAREHOUSE_NAME}</option>
							</c:forEach>
						</c:if>
			 </select>
			 <input type="hidden" name="sendWareIdM" id="sendWareIdM" value="${map.REQ_WH_ID}"/>
     	 </td> 
     	 <td class="right" width="15%">结算省份：</td>  
		 <td align="left">
	  		<select class="u-select" id="txt1" name="JS_PROVINCE" onchange="_genCity(this,'txt2')"></select>
			<input type="hidden" name="jsProvinceM" id="jsProvinceM" value="${map.DLV_BAL_PROV_ID}"/>
     	 </td> 
     </tr>
     <tr>
	     <td class="right" width="15%">结算城市：</td>  
		 <td align="left">
	  		<select class="u-select" id="txt2" name="JS_CITY" onchange="_genCity(this,'txt3')"></select>
			<input type="hidden" name="jsCityM" id="jsCityM" value="${map.DLV_BAL_CITY_ID}"/>
     	 </td> 
     	 <td class="right" width="15%">结算区县：</td>  
		 <td align="left">
	  		<select class="u-select" id="txt3" name="JS_COUNTY"></select>
			<input type="hidden" name="jsCountyM" id="jsCountyM" value="${map.DLV_BAL_COUNTY_ID}"/>
     	 </td> 
     </tr>
     <tr>
	    <td class="right" width="15%">是否中转：</td>  
		    <td align="left">
		    <select id="isMiddleTurn" onchange="selectZz(this)" name="isMiddleTurn" class="u-select">
		    	<option value='10041002'>否</option>
		    	<option value='10041001'>是</option>
		    </select>
		    
			<input type="hidden" name="isZzM" id="isZzM" value="${map.DLV_IS_ZZ}"/>
     	 </td> 
     	 <td class="right" width="15%">中转仓库：</td>  
		    <td align="left">
	  		<select name="zzWareId" id="zzWareId" class="u-select"  onchange="getZzRegion(this)">
				 	<option value="">--请选择--</option>
						<c:if test="${list!=null}">
							<c:forEach items="${list}" var="list">
								<option value="${list.WAREHOUSE_ID}">${list.WAREHOUSE_NAME}</option>
							</c:forEach>
						</c:if>
			 </select>
			 <input type="hidden" name="zzWareIdM" id="zzWareIdM" value="${map.ZZ_WH_ID}"/>
     	 </td> 
     	
     </tr>
      <tr>
      	<td class="right" width="15%">中转省份：</td>  
		 <td align="left">
	  		<select class="u-select" id="zZProvince" name="zZProvince" onchange="_genCity(this,'zZCity')" disabled="disabled"></select>
			<input type="hidden" name="zZProvinceM" id="zZProvinceM" value="${map.DLV_ZZ_PROV_ID}"/>
     	 </td> 
	   <td class="right" width="15%">中转城市：</td>  
		 <td align="left">
	  		<select class="u-select" id="zZCity" name="zZCity" onchange="_genCity(this,'zZCounty')" disabled="disabled"></select>
			<input type="hidden" name="zZCityM" id="zZCityM" value="${map.DLV_ZZ_CITY_ID}"/>
     	 </td> 
     	  
     </tr>
     <tr>
     	<td class="right" width="15%">中转区县：</td>  
		 <td align="left">
	  		<select class="u-select" id="zZCounty" name="zZCounty" disabled="disabled"></select>
			<input type="hidden" name="zZCountyM" id="zZCountyM" value="${map.DLV_ZZ_COUNTY_ID}"/>
     	 </td>
	   <td class="right" width="15%">发运方式：</td>  
		 <td align="left">
	  		<script type="text/javascript">
						genSelBoxExp("transType",<%=Constant.TT_TRANS_WAY %>,"-1",true,"u-select",'',"false",'');
			</script>
			<input type="hidden" name="transTypeM" id="transTypeM" value="${map.SHIP_DESC}"/>
     	 </td> 
     	 <td class="right" width="15%"></td>  
		 <td align="left">
	  		
     	 </td> 
     </tr>
     <tr> 
      	<td colspan="4" class="table_query_4Col_input" style="text-align: center">
      		<input type="hidden" id="reqId" name="reqId" value="${reqId}"/><!-- 发运申请ID -->
      		<input type="hidden" id="isSand" name="isSand" value="${isSand}"/><!-- 是否散单 -->
      		<input type="hidden" id="logiName" name="logiName" value="${logiName}"/><!-- 承运商名称 -->
      		<input type="hidden" id="remark" name="remark" value=""/><!-- 分派备注 -->
      		<input type="hidden" id="flag" name="flag" value="${flag}"/><!-- 1表示分派管理，2表示分派更改 -->
			<input type="button"  class="normal_btn" id="saveButton" onclick="addReservoir()" style="width:8%"  value="确认分派"/>&nbsp;&nbsp;
			<input type="hidden" name="receiveWareId" id="receiveWareId"/>
	   	</td>
	  </tr>
	 </TABLE>
	</div>
</div>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
//初始化    
function doInit(){
	//加载父页面分派备注
	var remark=parent.document.getElementById('inIframe').contentWindow.document.getElementById('REMARK');
	document.getElementById("remark").value=remark.value;
	//加载下拉框选中的值
	var sendWareId = document.getElementById("sendWareId");
	var sendWareIdM = document.getElementById("sendWareIdM");
	for(var i = 0;i<sendWareId.length;i++){
		if(sendWareId[i].value == sendWareIdM.value){
			sendWareId[i].selected = true;
			break;
		}
	}
	//生成下拉框：结算省市县
	genLocSel('txt1','txt2','txt3');//支持火狐
	//省市
	var pro_h = document.getElementById("jsProvinceM");
	_genCity(pro_h,'txt2');
	var pro = document.getElementById("txt1");
	for(var i = 0;i<pro.length;i++){
		if(pro[i].value == pro_h.value){
			pro[i].selected = true;
			break;
		}
	}
	//地级市
	var city_h = document.getElementById("jsCityM");
	_genCity(city_h,'txt3');
	var city = document.getElementById("txt2");
	for(var i = 0;i<city.length;i++){
		if(city[i].value == city_h.value){
			city[i].selected = true;
			break;
		}
	}
	//区县
	var county_h = document.getElementById("jsCountyM");
	var county = document.getElementById("txt3");
	for(var i = 0;i<county.length;i++){
		if(county[i].value == county_h.value){
			county[i].selected = true;
			break;
		}
	}
	//发运方式
	var transTypeM = document.getElementById("transTypeM");
	var transType = document.getElementById("transType");
	for(var i = 0;i<transType.length;i++){
		if(transType[i].value == transTypeM.value){
			transType[i].selected = true;
			break;
		}
	}
	//是否中转
	var isZzM = document.getElementById("isZzM");
	var isMiddleTurn = document.getElementById("isMiddleTurn");
	for(var i = 0;i<isMiddleTurn.length;i++){
		if(isMiddleTurn[i].value == isZzM.value){
			isMiddleTurn[i].selected = true;
			break;
		}
	}
	
	if(isZzM.value==10041001){//是否中转为是，选择中转的省市县
		//加载默认选中中转仓库
		var zzWareId = document.getElementById("zzWareId");
		var zzWareIdM = document.getElementById("zzWareIdM");
		for(var i = 0;i<zzWareId.length;i++){
			if(zzWareId[i].value == zzWareIdM.value){
				zzWareId[i].selected = true;
				break;
			}
		}
		//生成下拉框：结算省市县
		genLocSel('zZProvince','zZCity','zZCounty');//支持火狐
		//省市
		var pro_z = document.getElementById("zZProvinceM");
		_genCity(pro_z,'zZCity');
		var proz = document.getElementById("zZProvince");
		for(var i = 0;i<proz.length;i++){
			if(proz[i].value == pro_z.value){
				proz[i].selected = true;
				break;
			}
		}
		//地级市
		var city_z = document.getElementById("zZCityM");
		_genCity(city_z,'zZCounty');
		var cityz = document.getElementById("zZCity");
		for(var i = 0;i<cityz.length;i++){
			if(cityz[i].value == city_z.value){
				cityz[i].selected = true;
				break;
			}
		}
		//区县
		var county_z = document.getElementById("zZCountyM");
		var countyz = document.getElementById("zZCounty");
		for(var i = 0;i<countyz.length;i++){
			if(countyz[i].value == county_z.value){
				countyz[i].selected = true;
				break;
			}
		}
	}else{//中转省市县不可选择
		var zzWareId = document.getElementById("zzWareId");
		zzWareId.disabled=true;
		//var proz = document.getElementById("zZProvince");
		//var cityz = document.getElementById("zZCity");
		//var countyz = document.getElementById("zZCounty");
		//proz.disabled=true;
		//cityz.disabled=true;
		//countyz.disabled=true;
	}
	
}
//根据是否中转控制省市县的选择
function selectZz(obj){
	var isZz=obj.value;
	var zzWareId = document.getElementById("zzWareId");
	
	var proz = document.getElementById("zZProvince");
	var cityz = document.getElementById("zZCity");
	var countyz = document.getElementById("zZCounty");
	if(isZz==10041001){
		zzWareId.disabled=false;
		
		//proz.disabled=false;
		//cityz.disabled=false;
		//countyz.disabled=false;
		//生成下拉框：结算省市县
		genLocSel('zZProvince','zZCity','zZCounty');//支持火狐
		//省市
		var pro_z = document.getElementById("zZProvinceM");
		_genCity(pro_z,'zZCity');
		var proz = document.getElementById("zZProvince");
		for(var i = 0;i<proz.length;i++){
			if(proz[i].value == pro_z.value){
				proz[i].selected = true;
				break;
			}
		}
		//地级市
		var city_z = document.getElementById("zZCityM");
		_genCity(city_z,'zZCounty');
		var cityz = document.getElementById("zZCity");
		for(var i = 0;i<cityz.length;i++){
			if(cityz[i].value == city_z.value){
				cityz[i].selected = true;
				break;
			}
		}
		//区县
		var county_z = document.getElementById("zZCountyM");
		var countyz = document.getElementById("zZCounty");
		for(var i = 0;i<countyz.length;i++){
			if(countyz[i].value == county_z.value){
				countyz[i].selected = true;
				break;
			}
		}
	}else{
		zzWareId.disabled=true;
		//proz.disabled=true;
		//cityz.disabled=true;
		//countyz.disabled=true;
	}
}
//根据中转仓库获取中转省市县
function getZzRegion(obj){
	var rvalue=obj.value;
	
	if(rvalue!=''){
		document.getElementById("receiveWareId").value=rvalue;
		var url = "<%=contextPath%>/sales/storage/sendmanage/DispatchOrderManage/getAddrByReceiveWr.json";
		sendAjax(url,function(date){
			//省市
			var pro_z = document.getElementById("zZProvinceM");
			pro_z.value=date.addrlist.PROV_CODE;
			_genCity(pro_z,'zZCity');
			var proz = document.getElementById("zZProvince");
			for(var i = 0;i<proz.length;i++){
				if(proz[i].value == pro_z.value){
					proz[i].selected = true;
					break;
				}
			}
			//地级市
			var city_z = document.getElementById("zZCityM");
			city_z.value=date.addrlist.CITY_CODE;
			_genCity(city_z,'zZCounty');
			var cityz = document.getElementById("zZCity");
			for(var i = 0;i<cityz.length;i++){
				if(cityz[i].value == city_z.value){
					cityz[i].selected = true;
					break;
				}
			}
			//区县
			var county_z = document.getElementById("zZCountyM");
			county_z.value=date.addrlist.COUNTY_CODE;
			var countyz = document.getElementById("zZCounty");
			for(var i = 0;i<countyz.length;i++){
				if(countyz[i].value == county_z.value){
					countyz[i].selected = true;
					break;
				}
			}
		},'fm');
	}
}
//保存
function addReservoir()
{
	var sendWare=document.getElementById("sendWareId").value;//发运仓库
	var transType=document.getElementById("transType").value;//发运方式
	var txt1=document.getElementById("txt1").value;//结算省份
	var txt2=document.getElementById("txt2").value;//结算城市
	var txt3=document.getElementById("txt3").value;//结算区县
	if(sendWare==""){
		MyAlert("请选择发运仓库！");
		return;
	}
	if(transType==""){
		MyAlert("请选择发运方式！");
		return;
	}
	if(txt1==""){
		MyAlert("请选择结算省份！");
		return;
	}
	if(txt2==""){
		MyAlert("请选择结算城市！");
		return;
	}
	if(txt3==""){
		MyAlert("请选择结算区县！");
		return;
	}
	var isMiddleTurn=document.getElementById("isMiddleTurn").value;//是否中转
	if(isMiddleTurn==10041001){//需选择中转省市县
		var zzWareId = document.getElementById("zzWareId").value;//中转仓库
		if(zzWareId==""){
			MyAlert("请选择中转仓库！");
			return;
		}
		//var zZProvince=document.getElementById("zZProvince").value;//中转省份
		//var zZCity=document.getElementById("zZCity").value;//中转城市
		//var zZCounty=document.getElementById("zZCounty").value;//中转区县
		//if(zZProvince==""){
		//	MyAlert("请选择中转省份！");
		//	return;
		//}
		//if(zZCity==""){
		//	MyAlert("请选择中转城市！");
		//	return;
		//}
		//if(zZCounty==""){
		//	MyAlert("请选择中转区县！");
		//	return;
		//}
	}
	if(!submitForm("fm")){
		return;
	}
	var url2 = "<%=contextPath%>/sales/storage/sendmanage/SendAssignment/modifyInfoDo.json";
	makeNomalFormCall(url2,myReturn,'fm');
}

function myReturn(json) {
	if(json.returnValue == 1)
	{
		parent.document.getElementById('inIframe').contentWindow.myQuery();
		MyAlertForFun('分派成功！',_hide);
	}else if(json.returnValue == 2){//表示可用库存数不足
		MyAlert("所选发运仓库的可用库存数不足！");
	}else{
		MyAlert("操作失败！请联系系统管理员！");
	}
}
</script>
</body>
</html>
