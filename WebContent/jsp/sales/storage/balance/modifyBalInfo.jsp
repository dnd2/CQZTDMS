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
<title>结算信息修改 </title>

</head>

<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理&gt;结算管理&gt;运费结算申请&gt;结算信息修改</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2>结算信息修改</h2>
	<div class="form-body">
	  <TABLE class=table_query align=center id="moneyTable">
	  <tr>
     	 <td class="right" width="15%">发运结算省份：</td>  
		 <td align="left">
			<input type="text" name="dlvProvinceM" id="dlvProvinceM" value="${dlvBalProv}" class="middle_txt" disabled="disabled"/>
     	 </td> 
     	  <td class="right" width="15%">发运结算城市：</td>  
		 <td align="left">
			<input type="text" name="dlvCityM" id="dlvCityM" value="${dlvBalCity}" class="middle_txt" disabled="disabled"/>
     	 </td> 
     	 <td class="right" width="15%">发运结算区县：</td>  
		 <td align="left">
			<input type="text" name="dlvCountyM" id="dlvCountyM" value="${dlvBalCounty}" class="middle_txt" disabled="disabled"/>
     	 </td> 
     </tr>
	   <tr>
     	 <td class="right" width="15%">财务结算省份：</td>  
		 <td align="left">
	  		<select class="u-select" id="txt1" name="JS_PROVINCE" onchange="_genCity(this,'txt2')"></select>
			<input type="hidden" name="jsProvinceM" id="jsProvinceM" value="${balProv}"/>
     	 </td> 
     	  <td class="right" width="15%">财务结算城市：</td>  
		 <td align="left">
	  		<select class="u-select" id="txt2" name="JS_CITY" onchange="_genCity(this,'txt3')"></select>
			<input type="hidden" name="jsCityM" id="jsCityM" value="${balCity}"/>
     	 </td> 
     	 <td class="right" width="15%">财务结算区县：</td>  
		 <td align="left">
	  		<select class="u-select" id="txt3" name="JS_COUNTY"></select>
			<input type="hidden" name="jsCountyM" id="jsCountyM" value="${balCounty}"/>
     	 </td> 
     </tr>
     <tr> 
      	<td colspan="6" class="table_query_4Col_input" style="text-align: center">
      		<input type="hidden" id="billId" name="billId" value="${billId}"/><!-- 交接单ID -->
      		<input type="hidden" id="otherMoney" name="otherMoney" value="${otherMoney}"/><!--其他金额-->
			<input type="button"  class="normal_btn" id="saveButton" onclick="addReservoir()" style="width:8%"  value="保存"/>&nbsp;&nbsp;
			
	   	</td>
	  </tr>
	 </TABLE>
	</div>
</div>
</form>
<!--页面列表 begin -->
<script type="text/javascript" ><!--
//初始化    
function doInit(){
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
}
//保存
function addReservoir()
{
	var txt1=document.getElementById("txt1").value;//结算省份
	var txt2=document.getElementById("txt2").value;//结算城市
	var txt3=document.getElementById("txt3").value;//结算区县
	
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
	if(!submitForm("fm")){
		return;
	}
	var url2 = "<%=contextPath%>/sales/storage/balancemanage/SalesBalanceApply/editBalAddr.json";
	makeNomalFormCall(url2,myReturn,'fm');
}

function myReturn(json) {
	if(json.returnValue == 1)
	{
		parent.document.getElementById('inIframe').contentWindow.__extQuery__(1);
		MyAlertForFun('保存成功！',_hide);
	}else{
		MyAlert("操作失败！请联系系统管理员！");
	}
}
</script>
</body>
</html>
