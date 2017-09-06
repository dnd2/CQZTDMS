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
<title>发运组板管理 </title>

</head>

<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理&gt;发运管理&gt;发运组板管理&gt;中转信息修改</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2>中转信息修改</h2>
	<div class="form-body">
	  <TABLE class=table_query align=center id="moneyTable">
	  <tr>
	    <td class="right" width="15%">是否中转：</td>  
		    <td align="left">
		    <select id="isMiddleTurn" onchange="selectZz(this)" name="isMiddleTurn" class="u-select">
		    	<option value='10041002'>否</option>
		    	<option value='10041001'>是</option>
		    </select>
		    <input type="hidden" name="isZzM" id="isZzM" value="${IS_ZZM}"/>
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
			  <input type="hidden" name="zzWareIdM" id="zzWareIdM" value="${ZZ_WAREM}"/>
     	 </td> 
     	
     </tr>
      <tr>
      	<td class="right" width="15%">中转省份：</td>  
		 <td align="left">
	  		<select class="u-select" id="zZProvince" name="zZProvince" onchange="_genCity(this,'zZCity')" disabled="disabled"></select>
	  		<input type="hidden" name="zZProvinceM" id="zZProvinceM" value="${ZZ_PROVINCEM}"/>
     	 </td> 
	   <td class="right" width="15%">中转城市：</td>  
		 <td align="left">
	  		<select class="u-select" id="zZCity" name="zZCity" onchange="_genCity(this,'zZCounty')" disabled="disabled"></select>
	  		<input type="hidden" name="zZCityM" id="zZCityM" value="${ZZ_CITYM}"/>
     	 </td> 
     	  
     </tr>
     
     <tr>
     	 <td class="right" width="15%">中转区县：</td>  
		 <td align="left">
	  		<select class="u-select" id="zZCounty" name="zZCounty" disabled="disabled"></select>
	  		<input type="hidden" name="zZCountyM" id="zZCountyM" value="${ZZ_COUNTYM}"/>
     	 </td>
     	 <td class="right" width="15%"></td>  
		 <td align="left">
	  		
     	 </td> 
     </tr>
     <tr> 
      	<td colspan="4" class="table_query_4Col_input" style="text-align: center">
      		<input type="hidden" id="index" name="index" value="${index}"/><!-- 序号 -->
			<input type="button"  class="normal_btn" id="saveButton" onclick="addReservoir()" style="width:8%"  value="保存"/>
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
		//生成下拉框：结算省市县
		genLocSel('zZProvince','zZCity','zZCounty');//支持火狐
		//省市
		//var pro_z = document.getElementById("zZProvinceM");
		//_genCity(pro_z,'zZCity');
		//var proz = document.getElementById("zZProvince");
		//for(var i = 0;i<proz.length;i++){
		//	if(proz[i].value == pro_z.value){
		//		proz[i].selected = true;
		//		break;
		//	}
		//}
		//地级市
		//var city_z = document.getElementById("zZCityM");
		//_genCity(city_z,'zZCounty');
		//var cityz = document.getElementById("zZCity");
		//for(var i = 0;i<cityz.length;i++){
		//	if(cityz[i].value == city_z.value){
		//		cityz[i].selected = true;
		//		break;
		//	}
		//}
		//区县
		//var county_z = document.getElementById("zZCountyM");
		//var countyz = document.getElementById("zZCounty");
		//for(var i = 0;i<countyz.length;i++){
		//	if(countyz[i].value == county_z.value){
		//		countyz[i].selected = true;
		//		break;
		//	}
		//}
	}else{
		zzWareId.disabled=true;
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
	var isMiddleTurn=document.getElementById("isMiddleTurn").value;//是否中转
	var zzWareId = document.getElementById("zzWareId").value;//中转仓库
	if(isMiddleTurn==10041001){//需选择中转省市县
		
		if(zzWareId==""){
			MyAlert("请选择中转仓库！");
			return;
		}
	}
	var index=document.getElementById("index").value;
	var zZProvince=document.getElementById("zZProvince").value;
	var zZCity=document.getElementById("zZCity").value;
	var zZCounty=document.getElementById("zZCounty").value;
	//MyAlert(isMiddleTurn+"-"+zzWareId+"-"+index+"-"+zZProvince+"-"+zZCity+"-"+zZCounty);
	//将中转信息存放到父页面
	if(parent.document.getElementById('inIframe'))
		{
			parent.document.getElementById('inIframe').contentWindow.document.getElementById("IS_ZZ"+index).value = isMiddleTurn;
			parent.document.getElementById('inIframe').contentWindow.document.getElementById("ZZ_WARE"+index).value = zzWareId;	
			parent.document.getElementById('inIframe').contentWindow.document.getElementById("ZZ_PROVINCE"+index).value=zZProvince;
			parent.document.getElementById('inIframe').contentWindow.document.getElementById("ZZ_CITY"+index).value=zZCity;
			parent.document.getElementById('inIframe').contentWindow.document.getElementById("ZZ_COUNTY"+index).value=zZCounty;
		}
	_hide();
	
}

</script>
</body>
</html>
