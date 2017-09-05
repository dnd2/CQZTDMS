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
<title> 城市里程数维护 </title>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type=text/javascript>
	function init(){		

		//出发地
		var area = document.getElementById("YIELDLY");
		var areaId = document.getElementById("areaId");
		for(var i = 0;i < area.length;i++){
			if(area[i].value == areaId.value){
				area[i].selected = true;
				break;
			}
		}
		//运输方式
		var transId = document.getElementById("transId");
		
		var trans = document.getElementById("TRANS_WAY");
		for(var i = 0;i < trans.length;i++){
			if(trans[i].value == transId.value){
				trans[i].selected = true;
				break;
			}
		}
		
		genLocSel('txt1','txt2','txt3');//支持火狐
		//省市
		var pro_h = document.getElementById("pro_h");
		_genCity(pro_h,'txt2');
		var pro = document.getElementById("txt1");
		for(var i = 0;i<pro.length;i++){
			if(pro[i].value == pro_h.value){
				pro[i].selected = true;
				break;
			}
		}
		//地级市
		var city_h = document.getElementById("city_h");
		_genCity(city_h,'txt3');
		var city = document.getElementById("txt2");
		for(var i = 0;i<city.length;i++){
			if(city[i].value == city_h.value){
				city[i].selected = true;
				break;
			}
		}
		//区县
		var county_h = document.getElementById("county_h");
		var county = document.getElementById("txt3");
		for(var i = 0;i<county.length;i++){
			if(county[i].value == county_h.value){
				county[i].selected = true;
				break;
			}
		}
	}
</script>
</head>
<body onload="init();">
<div class="wbox">
  <div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置&gt;整车物流管理&gt;储运基础信息&gt;城市里程数维护</div>
  <form action="" id="myform">
		<input type="hidden" id="areaId" name="areaId" value="${AREA_ID}" />
  		<input type="hidden" id="pro_h" value="${REGION_CODE}" />
	 	<input type="hidden" id="city_h" value="${CITY_CODE}" />
		<input type="hidden" id="county_h" value="${COUNTY_CODE}" />
		<input type="hidden" id="dis_id" name='dis_id' value="${DIS_ID }"/>
		<input type="hidden" id="carId" value="${CAR_TIE_ID}" />
		<input type="hidden" id="transId" value="${TRANS_WAY }"/>
  <div class="form-panel">
	<h2>城市里程维护</h2>
  <div class="form-body">
	  <table class="table_query">
	      <tr>
	      <td class="right">出发仓库：</td> 
		  <td align="left">
			 <select name="YIELDLY" id="YIELDLY" class="u-select" >
			 	<c:if test="${list!=null}">
						<c:forEach items="${list}" var="list">
							<option value="${list.WAREHOUSE_ID}">${list.WAREHOUSE_NAME}</option>
						</c:forEach>
					</c:if>
		  		</select>
			</td>
	      	<td class="right">里程数(公里)：</td>
	      	<td align="left">
	      		<input type="text" maxlength="20"  name="DISTANCE" id="DISTANCE" value="${DISTANCE}"  maxlength="30" datatype="0,is_digit,30" class="middle_txt"/>
	      	</td>
	      </tr>
	  <tr> 
			 <td class="right">目的省份：</td>  
			    <td align="left">
		  		<select class="u-select" id="txt1" name="PROVINCE" onchange="_genCity(this,'txt2')"></select>
	     	 </td> 
	     	 <td class="right">目的地级市：</td>  
			    <td align="left">
		  		<select class="u-select" id="txt2" name="CITY_ID" onchange="_genCity(this,'txt3')"></select>
	     	 </td>   
		  	<td class="right">目的区县：</td>
		   	   <td align="left">
	  				<select class="u-select" id="txt3" name="COUNTY_ID"></select>
			 </td> 
  		
	      <tr>
	      	<td class="right">到达天数：</td>
	      	<td align="left"><input type="text" maxlength="20"  name="ARRIVE_DAYS" id="ARRIVE_DAYS" value="${ARRIVE_DAYS}"  maxlength="20" datatype="0,is_digit,20" class="middle_txt"/></td>
	      	<td class="right">运输方式：</td>
	      	<td align="left">
	      		<script type="text/javascript">
					genSelBoxExp("TRANS_WAY",9559,"",true,"u-select","","false",'');
				</script>
			</td>
			<td class="right">单价：</td>
	      	<td align="left"><input id="SINGLE_PLACE" name="SINGLE_PLACE" datatype="0,is_double,20" maxlength="20" type="text" maxlength="20"  value="${SINGLE_PLACE}" class="middle_txt"/></td>
	      	
	      </tr>
	       <tr>
	      	<td class="right">有效期：</td>
	      	<td align="left">
		      	<input class="short_txt" readonly="readonly"  type="text" value="${beginDate }" id="beginDate" name="beginDate" onFocus="WdatePicker({el:$dp.$('beginDate'), maxDate:'#F{$dp.$D(\'endDate\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
				<input class="short_txt" readonly="readonly"  type="text" value="${endDate }" id="endDate" name="endDate" onFocus="WdatePicker({el:$dp.$('endDate'), minDate:'#F{$dp.$D(\'beginDate\')}'})"  style="cursor: pointer;width: 80px;"/>
          </td>
          <td class="right">手工运价：</td>
	      	<td align="left"><input type="text" id="HAND_PRICE"  maxlength="20"  value="${HAND_PRICE}" name="HAND_PRICE" class="short_txt"/><font color="red">默认等于(单价*里程数)</font></td>
          <td class="right">备注：</td>
	      	<td align="left"><input type="text" id="REMARK"  maxlength="100"  value="${REMARK}" name="REMARK" class="middle_txt"/></td>
	      </tr>
	  </table>
	</div>
   </div>
  </form>
  <table width="100%" align="center">
    <tr>
      <td height="2"></td>
    </tr>
    <tr>
      <td align="center">
      	<input class="normal_btn" type="button" value="保 存" name="button5" onclick="add();"/>&nbsp;        
      	<input class="normal_btn" type="button" value="返 回" name="button2" onclick="history.back();"/></td>
    </tr>
    <tr>
      <td height="1"></td>
    </tr>
  </table>
</div>
<script type=text/javascript>
	function open1()
	{
		OpenHtmlWindow('<%=contextPath%>/sales/storage/storagebase/CityMileageManage/queryCity.do',700,150);
	}
	function add(){
		//var obj = document.getElementById("FUEL_COEFFICIENT").value;
		var pro = document.getElementById("txt1").value;
		if(pro == 0 || pro == null || pro == "")
		{
			MyAlert("请选择省份!");
			document.getElementById("txt1").select();
			return false;
		}
		var city = document.getElementById("txt2").value;
		if(city == 0 || city == null || city == "")
		{
			MyAlert("请选择地级市!");
			document.getElementById("txt2").select();
			return false;
		}
		var con = document.getElementById("txt3").value;
		if(con == 0 || con == null || con == "")
		{
			MyAlert("请选择区县!");
			document.getElementById("txt3").select();
			return false;
		}
		//var car = document.getElementById("group_id").value;
		//if(car == 0 || car == null || car == "")
		//{
		//	MyAlert("请选择车系!");
		//	document.getElementById("group_id").select();
		//	return false;
		//}
		
		var trans = document.getElementById("TRANS_WAY").value;
		if(trans == 0 || trans == null || trans == "")
		{
			MyAlert("请选择运输方式!");
			document.getElementById("TRANS_WAY").select();
			return false;
		}
		//手工运价非空时，需判断是否等于 单价*里程数，若不等，提示填写备注
		var handPrice = document.getElementById("HAND_PRICE").value;
		if(handPrice != null && handPrice != "")
		{
			var distance = document.getElementById("DISTANCE").value;
			var singlePrice = document.getElementById("SINGLE_PLACE").value;
			var remark = document.getElementById("REMARK").value;
			var sum=parseFloat(distance*singlePrice).toFixed(2);
			var hp = parseFloat(handPrice).toFixed(2);
			if(sum!=hp){
				if(remark==null||remark==""){
					MyAlert("手工运价不等于（单价*里程数），备注不能为空!");
					return false;
				}
			}
			
		}
		if(!submitForm("myform")){
			return;
		}
		MyConfirm("确认保存信息！",saveCityMileage);
	}	
	function saveCityMileage()
	{
		var url = "<%=contextPath%>/sales/storage/storagebase/CityMileageManage/saveCityMileage.json";
		sendAjax(url,function(date){
			if(date.message == "操作成功") {
				//MyAlert(date.message);
				parent.MyAlert(date.message);
				back();
			} else {
				MyAlert(date.Exception.message);
			}
		},'myform');
	}

	function back(){
		window.location.href="<%=contextPath%>/sales/storage/storagebase/CityMileageManage/cityMileageInit.do";
	}
	
	function setText(text){
		document.getElementById("END_PLACE").innerText = text;
	}
</script>
</body>
</html>
 

