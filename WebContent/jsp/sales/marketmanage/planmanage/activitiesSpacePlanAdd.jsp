<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>活动执行方案提报(区域)</title>
<% String contextPath = request.getContextPath();  %>
<script type="text/javascript">
<!--
function chkBigCus() {
	var iIsFleet = document.getElementById("isFleet").value ;
	var oCostType = document.getElementById("myCostType") ;
	
	if(iIsFleet == 0) {//是集团客户
		oCostType.innerHTML = genSelBoxStrExp("costType",<%=Constant.COST_TYPE%>,"",false,"","onchange=\"changeCost();\"","false",'<%=Constant.COST_TYPE_01%>,<%=Constant.COST_TYPE_02%>,<%=Constant.COST_TYPE_03%>,<%=Constant.COST_TYPE_04%>,<%=Constant.COST_TYPE_06%>,<%=Constant.COST_TYPE_07%>');
	} else if(iIsFleet == 1) {//不是集团客户
		oCostType.innerHTML = genSelBoxStrExp("costType",<%=Constant.COST_TYPE%>,"",false,"","onchange=\"changeCost();\"","false",'<%=Constant.COST_TYPE_01%>,<%=Constant.COST_TYPE_05%>,<%=Constant.COST_TYPE_02%>,<%=Constant.COST_TYPE_03%>,<%=Constant.COST_TYPE_07%>');
	}
	
	changeCost();
}

function notifySession() {
	
	url = "<%=request.getContextPath()%>/sales/marketmanage/planmanage/ActivitiesPlanReport/notifySession.json" ;
	func = "area's activity to report!" ;
	makeCall(url, doNull,{func:func}) ;
}

function doNull(json) {
	
}

function toChangeNum2_New(value1){
	var count = document.getElementById("mediaPrice"+value1).value;
	var price = document.getElementById("totalCount"+value1).value;
	var planCost = 0 ;

	planCost = Number(count) * Number(price) ;

	document.getElementById("planCost22"+value1).innerText=amountFormat(planCost);
	document.getElementById("cost22"+value1).value = planCost;
	var planCost = document.getElementsByName("planCost22");
	var totalPrice = 0;
	for (var i=0; i<planCost.length; i++){  
		totalPrice += Number(planCost[i].value);  
	}
	document.getElementById("totalPrice22").innerText = amountFormat(totalPrice);
}

function toChangeNum1_New(value1){
	var count = document.getElementById("itemCount"+value1).value;
	var price = document.getElementById("itemPrice"+value1).value;
	var planCost =0 ;
	
	planCost = Number(count) * Number(price) ;
	
	document.getElementById("planCost11"+value1).innerText=amountFormat(planCost);
	document.getElementById("cost11"+value1).value = planCost;
	var planCost = document.getElementsByName("planCost11");
	var totalPrice = 0;
	for (var i=0; i<planCost.length; i++){  
		totalPrice += Number(planCost[i].value);  
	}
	document.getElementById("totalPrice11").innerText = amountFormat(totalPrice);
}
//验证
function checkForm(){
	var newRow = document.getElementById("tbody1"); 
	var mylength = newRow.rows.length;
	var newRow2 = document.getElementById("tbody2");
	var iLen = newRow2.rows.length ;
	if(mylength < 1 && iLen < 1){
		MyAlert("请添加费用！");
		return false;
	}
	
	var callsHousesCntTgt = document.getElementById('callsHousesCntTgt').value ;
	var reserveCntTgt = document.getElementById('reserveCntTgt').value ;
	var orderCntTgt = document.getElementById('orderCntTgt').value ;
	var deliveryCntTgt = document.getElementById('deliveryCntTgt').value ;
	var itemNames = document.getElementsByName("itemName");
	var itemPrices = document.getElementsByName("itemPrice");
	var itemCounts = document.getElementsByName("itemCount");
	var regions = document.getElementsByName("region");
	var advSubject = document.getElementsByName("advSubject");
	var mediaName = document.getElementsByName("mediaName");
	var advDate = document.getElementsByName("advDate");
	var myendDate = document.getElementsByName("myendDate");
	var mediaColumn = document.getElementsByName("mediaColumn");
	var publish = document.getElementsByName("publish");
	var mediaSize = document.getElementsByName("mediaSize");
	var mediaCount = document.getElementsByName("mediaCount");
	var totalCount = document.getElementsByName("totalCount");
	var mediaPrice = document.getElementsByName("mediaPrice");
	
	var len = itemNames.length ;
	var ln = regions.length ;
	
	var isMoney = /^(([1-9]\d*)|0)(\.\d{1,2})?$/;
	var isNumber = /^\+?[1-9][0-9]*$/; 
	
	if(!t_trim(callsHousesCntTgt) || t_trim(callsHousesCntTgt) == "0") {
		MyAlert("请正确填写来电来店数!");
		return false;
	}
	if(!t_trim(reserveCntTgt) || t_trim(reserveCntTgt) == "0") {
		MyAlert("请正确填写信息留存量!");
		return false;
	}
	if(!t_trim(orderCntTgt) || t_trim(orderCntTgt) == "0") {
		MyAlert("请正确填写启票数!");
		return false;
	}
	if(!t_trim(deliveryCntTgt) || t_trim(deliveryCntTgt) == "0") {
		MyAlert("请正确填写实销数!");
		return false;
	}
	
	for(var i = 0; i < len; i++){
		if(!t_trim(itemNames[i].value)){
			MyAlert("请正确填写项目名称!");
			return false;
		}
		if(!t_trim(itemPrices[i].value) || "0" == t_trim(itemPrices[i].value) || !isMoney.test(t_trim(itemPrices[i].value))){
			MyAlert("请正确填写项目单价!");
			return false;
		}
		if(!t_trim(itemCounts[i].value) || "0" == t_trim(itemCounts[i].value) || !isNumber.test(t_trim(itemCounts[i].value))){
			MyAlert("请正确填写项目数量!");
			return false;
		}
	}

	for(var j = 0; j< ln; j++){
		if(!t_trim(advSubject[j].value)){
			MyAlert("请正确填写宣传主题");
			return false;
		}
		if(!t_trim(mediaName[j].value)){
			MyAlert("请正确填写媒体名称");
			return false;
		}
		if(!t_trim(advDate[j].value)){
			MyAlert("请正确填写广告开始日期");
			return false;
		}
		if(!t_trim(myendDate[j].value)){
			MyAlert("请正确填写广告结束日期");
			return false;
		}
		if(t_trim(myendDate[j].value) < t_trim(advDate[j].value)) {
			MyAlert("广告开始日期不能大于结束日期");
			return false;
		}
		if(!t_trim(mediaColumn[j].value)){
			MyAlert("请正确填写栏目/套装");
			return false;
		}
		if(!t_trim(publish[j].value)){
			MyAlert("请正确填写刊发位置/播出时段");
			return false;
		}
		if(!t_trim(mediaSize[j].value)){
			MyAlert("请正确填写规格/版式");
			return false;
		}
		if(!t_trim(mediaCount[j].value) || "0" == t_trim(mediaCount[j].value) || !isNumber.test(t_trim(mediaCount[j].value))){
			MyAlert("请正确填写单日频次");
			return false;
		}
		if(!t_trim(totalCount[j].value) || "0" == t_trim(totalCount[j].value) || !isNumber.test(t_trim(totalCount[j].value))){
			MyAlert("请正确填写总次数");
			return false;
		}
		if(parseInt(t_trim(totalCount[j].value)) < parseInt(t_trim(mediaCount[j].value))) {
			MyAlert("总次数不能小于单日频次");
		 	return false;
		}
		// if(t_trim(totalCount[j].value) < t_trim(mediaCount[j].value)) {
		// 	MyAlert("总次数不能小于单日频次");
		//	return false;
		// }
		if(!t_trim(mediaPrice[j].value) || "0" == t_trim(mediaPrice[j].value) || !isMoney.test(t_trim(mediaPrice[j].value))){
			MyAlert("请正确填写结算价格");
			return false;
		}
	}
	
	return true ;
}  

// 操作确认
function toSubmit(json) {
	
	if(json.result == true) {
		MyAlert("提交失败！用户操作超时，请重新登陆！");
		return false;
	}
	
	var value = json.value;
	
	if(checkForm()){
		var title = '' ;
		
		if(value == 0) {
			title = '确认提报？'
		} else if (value == 1) {
			title = '确认保存？'
		}
		if(submitForm('fm')){
			if(!isAfterNowTime(document.getElementById('startDate').value)) {
				MyAlert('计划开始日期不能小于当前日期！');

				return false ;
			}

			if(!document.getElementById("isProtocol").value) {
				MyAlert("请选择“是否全是招标媒体”!") ;
				
				return false ;
			} 
			
			var aGgTime = document.getElementsByName("advDate") ;
			var aEdTime = document.getElementsByName("myendDate") ;
			var aBeginDate = document.getElementsByName("aBeginDate") ;
			var cAmPurpose = document.getElementById('campaignPurpose').value ;
			
			if(cAmPurpose.length >= 500) {
				MyAlert("活动目的不能超出500个汉字！") ;

				return false ;
			}
			var cAmNeed = document.getElementById('campaignNeed').value ;
			
			if(cAmNeed.length >= 50) {
				MyAlert("活动要求不能超出50个汉字！") ;

				return false ;
			}
			
			var cAmDesc = document.getElementById('campaignDesc').value ;
			
			if(cAmDesc.length >= 500) {
				MyAlert("活动主要内容不能超出500个汉字！") ;

				return false ;
			}			
			var eXeDesc = document.getElementById('execAddDesc').value ;
			
			if(eXeDesc.length >= 250) {
				MyAlert("活动地点说明不能超出250个汉字！") ;

				return false ;
			}

			var aDvDesc = document.getElementById('adviceDesc').value ;
			
			if(aDvDesc.length >= 250) {
				MyAlert("区域建议及整改意见不能超出250个汉字！") ;

				return false ;
			}
			var rEmark = document.getElementById('remark').value ;
			
			if(rEmark.length >= 150) {
				MyAlert("申请意见不能超出150个汉字！") ;

				return false ;
			}
			var aEndDate = document.getElementsByName("aEndDate") ;
			
			if(aBeginDate) {
				var iLen = aBeginDate.length ;
				
				for(var i=0; i<iLen; i++) {
					if(!limitTime(aBeginDate[i].value, aEndDate[i].value)) {
						MyAlert("活动费用录入：日期范围必须在计划日期范围内！") ;

						return false ;
					}
				}
			}
			
			if(aGgTime) {
				var iLen = aGgTime.length ;
				
				for(var i=0; i<iLen; i++) {
					if(!limitTime(aGgTime[i].value, aEdTime[i].value)) {
						MyAlert("媒体投放费用录入：日期范围必须在计划日期范围内！") ;

						return false ;
					}
				}
			}
			
			var remarkValue = document.getElementById("remark").value ;
			
			if(remarkValue.length == 0) {
				MyAlert("请填写申请意见！") ;
				
				return false ;
			}
			
			MyConfirm(title,toConfirm, [value]); // 0表示为提报,1表示为保存
		}
	}
}

//设置可用费用
function getAvailableAmount(AreaId){
	var url = "<%=request.getContextPath()%>/sales/marketmanage/planmanage/ActivitiesSpacePlanReport/changeCost.json" ;
	
	makeCall(url,getChangePrice,{areaId:AreaId});
}

function getChangePrice(json){
	var costSC = json.costSC ;
	
	document.getElementById("costSC").innerHTML = costSC;
	    
	document.getElementById("costSC_A").value= costSC;
}

function setDisTrue() {
	var aBtn = arguments ;
	var iLen = aBtn.length ;

	for(var i=0; i<iLen; i++) {
		document.getElementById(arguments[i]).disabled = true ;
	}
}

function setDisFalse() {
	var aBtn = arguments ;
	var iLen = aBtn.length ;

	for(var i=0; i<iLen; i++) {
		document.getElementById(arguments[i]).disabled = false ;
	}
}

//选择经销商
function toAddDealer(){
	var areaId = document.getElementById("area_id").value ;
	var campaignId = '${campaignId}' ;
	OpenHtmlWindow('<%=contextPath%>/sales/marketmanage/planissued/ActivitiesPlanMake/queryDealerInit.do?&campaignId='+campaignId+'&areaId='+areaId,800,450);
}

//接收子窗口传回的值
function getDealerInfo(dealerIds,str){
 	var ids = document.getElementById("dealerIds").value = dealerIds;
 	var arrIDs = ids.split(",");
 	var arrInfo = str.split("@");
 	for(var i = 0;i<arrIDs.length;i++){
 		var arr = arrInfo[i].split(",");
 		var code = arr[0];
 		var name = arr[1];
 		var org = arr[2];
 		var prn = arr[3];
 		if(prn==null||prn.indexOf("null")==0){
 			prn = "";
 		}
 		var trNode = document.getElementById(arrIDs[i]);
 		if(trNode!=null){
 			var trID = trNode.id;
 			if(trID.indexOf(arrIDs[i])==0){
        		MyAlert("该经销商已存在，请选择其他经销商！");
	    		return ;
	    	}else{
	    		addRow(arrIDs[i],name,code,org,prn);
	    	}
 		}else{
 			addRow(arrIDs[i],name,code,org,prn);
 		}
 		
 	}
}

//根据取到的值动态生成表格
function addRow(dealerId,dealerShortname,dealerCode,orgName,province){
    var addTable = document.getElementById("dealerTable");
	var rows = addTable.rows;
	var length = rows.length;
	var insertRow = addTable.insertRow(length);
	insertRow.className = "table_list_row";
	insertRow.id = dealerId;
	insertRow.insertCell(0);
	insertRow.insertCell(1);
	insertRow.insertCell(2);
	insertRow.insertCell(3);
	insertRow.insertCell(4);
	insertRow.insertCell(5);
	addTable.rows[length].cells[0].innerHTML =  "<td nowrap='nowrap'>"+orgName+"</td>";
	addTable.rows[length].cells[1].innerHTML =  "<td nowrap='nowrap'>"+province+"</td>";
	addTable.rows[length].cells[2].innerHTML =  "<td nowrap='nowrap'>"+dealerCode+"<input type='hidden' name='dlrIds' value='"+dealerId+"'/></td>";
	addTable.rows[length].cells[3].innerHTML =  "<td nowrap='nowrap'>"+dealerShortname+"</td>";
	addTable.rows[length].cells[4].innerHTML =  "<td nowrap='nowrap'><input type='button' class='normal_btn' onclick='delRow("+dealerId+")' value='删除'/></td>";
	addTable.rows[length].cells[5].innerHTML =  "<td id='0'></td>";
	
}


//删除表格
function delRow(dealerId){
	var oDealerIds = document.getElementById("dealerIds") ;
	var aDealerIds = oDealerIds.value.split(",") ;
	for (i in aDealerIds) {
		if (aDealerIds[i] == dealerId) {
			aDealerIds.splice(i, 1) ;
		}
	}

	oDealerIds.value = aDealerIds.join() ;
	
    var tbodyNode=document.getElementById("dealerTable");
    var trNode=document.getElementById(dealerId);
    
	tbodyNode.removeChild(trNode);
}

function isAfterNowTimeSssss(value) {
	//var type = document.getElementById("costType").value ;
	
	//if(type != 11291002) {
	//	return true ;
	//}
	var aDates = value.split("-") ;
	var time_ = document.getElementById("sys_date__").value ;
	var times = time_.split(",") ;
    var year = 0;   
    var month = 0;   
    // var day = 0; 
          
    year  = times[0];   
    month  = times[1];   
    // day  = times[2];       

    if (aDates[0] < year) {
		return false ;
    } else if (aDates[0] == year){
    	if (aDates[1] <= month) {
    		return false ;
        //} else if (aDates[1] == month){
        //	if (aDates[2] < day) {
        //		return false ;
        //    }
        }
    }
    
    return true ;
}

function isAfterNowTime(value) {
	var aDates = value.split("-") ;
	var time_ = document.getElementById("sys_date__").value ;
	var times = time_.split(",") ;
    var year = 0;   
    var month = 0;   
    var day = 0; 
          
    year  = times[0];   
    month  = times[1];   
    day  = times[2];       

    //if (aDates[0] < year) {
	//	return false ;
    //} else if (aDates[0] == year){
    //	if (aDates[1] <= month) {
    //		return false ;
    //    } else if (aDates[1] == month){
    //    	if (aDates[2] < day) {
    //    		return false ;
    //        }
    //    }
    //}
    
    var compareDate = year + month + day ;
    var nowDate = value.replaceAll("-", "") ;

	if(nowDate < compareDate) {
		return false ;
	} else {
		return true ;
	}
    
    //return true ;
}

function limitTime(begin, end) {
	var start = document.getElementById("startDate").value ;
	var over = document.getElementById("endDate").value ;

	if(start <= begin && over >= end) {
		return true ;
	} else {
		return false ;
	}
}

function changeCost() {
	var type = document.getElementById('costType').value ;
	
	var len1 = document.getElementById("tbody1").rows.length ;
	var len2 = document.getElementById("tbody2").rows.length ;

	if(len1 > 0) {
		var count1 = document.getElementsByName("itemCount") ;
		var price1 = document.getElementsByName("itemPrice") ;
		
		var planCost1 = 0 ;
		var totalCost1 = 0 ;
		
		for(var i=0; i<len1; i++) {
			if(type == <%= Constant.COST_TYPE_03%> || type == <%= Constant.COST_TYPE_02%>) {
				planCost1 = Number(count1[i].value)*Number(price1[i].value)*parseFloat(<%= Constant.ACTIVITIES_TAX%>) ;
			} else if(type == <%= Constant.COST_TYPE_05%>) {
				planCost1 = Number(count1[i].value)*Number(price1[i].value)*parseFloat(<%= Constant.FLEET_ACTIVITIES_TAX%>) ;
			} else {
				planCost1 = Number(count1[i].value)*Number(price1[i].value) ;
			}
			
			var timeValue = count1[i].id.substr(9) ;
			document.getElementById('planCost1' + timeValue).innerHTML = amountFormat(planCost1) ;
			document.getElementById('cost1' + timeValue).value = amountFormat(planCost1) ;
			// document.getElementById("tbody1").rows[i].cells[6].innerHTML = "<span id='planCost1"+timeValue+"'>" + amountFormat(planCost1) + "</span><input id='cost1"+timeValue+"' name='planCost1' type='hidden' value='" + planCost1 + "'/>";
			
			totalCost1 += planCost1 ;
		}
		
		document.getElementById('totalPrice1').innerText = amountFormat(totalCost1) ;
	}
	
	if(len2 > 0) {
		var count2 = document.getElementsByName("totalCount") ;
		var price2 = document.getElementsByName("mediaPrice") ;
		
		var planCost2 = 0 ;
		var totalCost2 = 0 ;
		
		for(var j=0; j<len2; j++) {
			if(type == <%= Constant.COST_TYPE_03%> || type == <%= Constant.COST_TYPE_02%>) {
				planCost2 = Number(count2[j].value)*Number(price2[j].value)*parseFloat(<%= Constant.ACTIVITIES_TAX%>) ;
			} else if(type == <%= Constant.COST_TYPE_05%>) {
				planCost2 = Number(count2[j].value)*Number(price2[j].value)*parseFloat(<%= Constant.FLEET_ACTIVITIES_TAX%>) ;
			} else {
				planCost2 = Number(count2[j].value)*Number(price2[j].value) ;
			}
			
			var timeValue = count2[j].id.substr(10) ;
			document.getElementById('planCost2' + timeValue).innerHTML = amountFormat(planCost2) ;
			document.getElementById('cost2' + timeValue).value = amountFormat(planCost2) ;
			// document.getElementById("tbody2").rows[j].cells[13].innerHTML = "<span id='planCost2"+timeValue+"'>" + amountFormat(planCost2) + "</span><input id='cost2"+timeValue+"' name='planCost2' type='hidden' value='" + planCost2 + "'/>";
			
			totalCost2 += planCost2 ;
		}
		
		document.getElementById('totalPrice2').innerText = amountFormat(totalCost2) ;
	}
	
	getRateCount("rateNoTax", "totalPrice11", "totalPrice22") ;
	getRateCount("rateTax", "totalPrice1", "totalPrice2") ;
}

function getRateCount(spanId) {
	var iTotalPrice_A = document.getElementById(arguments[1]).innerHTML.replace(/,/g, "") ;
	var iTotalPrice_B = document.getElementById(arguments[2]).innerHTML.replace(/,/g, "") ;
	
	var totalPrice = Number(iTotalPrice_A) + Number(iTotalPrice_B) ;
	
	document.getElementById(spanId).innerHTML = amountFormat(totalPrice) ;
}
//-->
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 市场活动管理 &gt; 活动方案管理 &gt; 活动执行方案提报(区域)</div>
<form method="post" name="fm" id="fm">
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;业务范围</div>
	<table class="table_query" align="center">
		<tr class= "tabletitle">
			<th align = "right" width="10%">选择业务范围：</th>
			<th align = "left" >
				<select name="areaId" id="area_id" onchange="getDealerAreaId(this.value);">
					<c:forEach items="${areaList}" var="areaList">
						<option value="${areaList.AREA_ID}" >${areaList.AREA_NAME}</option>
					</c:forEach>
				</select>
			</th>
		</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;大客户审核</div>
	<table class="table_query" align="center">
		<tr class= "tabletitle">
			<th align = "right" width="10%">是否集团客户活动：</th>
			<th align = "left" >
				<select name="isFleet" id="isFleet" class="mini_sel" onchange="chkBigCus() ;">
						<option value="0">是</option>
						<option value="1" selected="selected">否</option>
				</select>
			</th>
		</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;方案信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="5" align="left">&nbsp;方案录入</th>
		</tr>
		<tr>
			<td  align="right">品牌：</td>
			<td align="left">
				<select name="groupId" id="groupId" class="short_sel" onchange="toClear();">
					<c:forEach items="${list}" var="list">
						<option value="${list.GROUP_ID}">${list.GROUP_NAME}</option>
					</c:forEach>
				</select>
			</td>
			<td align="right">活动车型：</td>
			<td align="left">
				<input type="text" name="campaignModel" id="campaignModel" readonly="readonly" datatype="0,is_null,1000" size="30"/>
				<input type="hidden" name="modelId" id="modelId"/>
				<!--
				<input type="button" name="selectbutton" id="selectbutton" class="mini_btn" value="..." onclick="showMaterialGroupToModel('campaignModel','modelId','true','3','true');"/>
				-->
				<input name="button3" type="button" class="mini_btn" onclick="showMaterialCarType_market('campaignModel','','true',3);" value="..." />
				<input type="button" name="clearbutton" id="clearbutton" class="cssbutton" value="清除" onClick="toClear();"/>
			</td>
			<td width="20%"></td>
		</tr>
		<tr>
			<td align="right">车厂方案编号：</td>
			<td align="left">
				<input type="text" name="campaignNo" id="campaignNo" value="${campaignNo}" readonly="readonly"/>
			</td>
			<td align="right">方案名称：</td>
			<td align="left">
				<input type="text" name="campaignName" id="campaignName" datatype="0,is_textarea,50" size="30" maxlength="50"/>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">计划开始日期：</td>
			<td align="left">
				<input class="short_txt"  type="text" readonly="readonly" id="startDate" name="startDate" group="startDate,endDate" datatype="0,is_date,10"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value="&nbsp;" />
			</td>
			<td align="right">计划结束日期：</td>
			<td align="left">
				<input class="short_txt"  type="text" readonly="readonly" id="endDate" name="endDate" group="startDate,endDate" datatype="0,is_date,10"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">活动主题：</td>
			<td colspan="4" align="left">
				<input type="text" name="campaignSubject" id="campaignSubject" datatype="1,is_textarea,50" size="90" maxlength="50"/>
			</td>
		</tr>
		<tr>
			<td align="right">活动对象：</td>
			<td colspan="4" align="left">
				<input type="text" name="campaignObject" id="campaignObject" size="90" maxlength="50"/>
			</td>
		</tr>
		<tr>
			<td align="right">活动目的：</td>
			<td colspan="4" align="left">
				<textarea name="campaignPurpose" id="campaignPurpose" rows="4" cols="70"></textarea>
			</td>
		</tr>
		<tr>
			<td align="right">活动要求：</td>
			<td colspan="4" align="left">
				<textarea name="campaignNeed" id="campaignNeed" rows="4" cols="70"></textarea>
			</td>
		</tr>
		<tr>
			<td align="right">活动主要内容：</td>
			<td colspan="4" align="left">
				<textarea name="campaignDesc" id="campaignDesc" rows="4" cols="70"></textarea>
			</td>
		</tr>
		<tr>
			<th colspan="5" align="left">&nbsp;区域说明：</th>
		</tr>
		<tr>
			<td align="right">活动地点说明：</td>
			<td colspan="4" align="left">
				<textarea name="execAddDesc" id="execAddDesc" rows="4"   cols="70"></textarea>
			</td>
		</tr>
		<tr>
		<td align="right">区域建议及整改意见：</td>
		<td colspan="4" align="left">
				<textarea name="adviceDesc" id="adviceDesc" rows="4"  cols="70"></textarea>
		</td>
		</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;执行方案信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="5" align="left">&nbsp;目标录入</th>
		</tr>
		<tr>
			<td align="center">项目</td>
			<td align="center">目标</td>
		</tr>
		<tr class="table_list_row2">
			<td align="center">来电来店数</td>
			<td align="center"><input type="text" name="callsHousesCntTgt" id="callsHousesCntTgt" datatype=”1,is_digit,10” /><font color="red">*</font></td>
		</tr>
		<tr class="table_list_row2">
			<td align="center">信息留存量</td>
			<td align="center"><input type="text" name="reserveCntTgt" id="reserveCntTgt" datatype=”1,is_digit,10” /><font color="red">*</font></td>
		</tr>
		<tr class="table_list_row2">
			<td align="center">启票数</td>
			<td align="center"><input type="text" name="orderCntTgt" id="orderCntTgt" datatype=”1,is_digit,10” /><font color="red">*</font></td>
		</tr>
		<tr class="table_list_row2">
			<td align="center">实销数</td>
			<td align="center"><input type="text" name="deliveryCntTgt" id="deliveryCntTgt" datatype=”1,is_digit,10” /><font color="red">*</font></td>
		</tr>
	</table>
	<%-- <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="12" align="left">&nbsp;可用费用
			</th>
		</tr>
		<tr class="table_list_row2">
			<td align="center">运营大区市场推广费用</td>
		</tr>
		<tr class="table_list_row2">
			<td align="center">
				<span id='costSC'></span>
				<input type="hidden" id="costSC__A" name="costSC__A" value="${costSC }">
			</td>
		</tr>
	</table> --%>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;费用类别</div>
	<table class="table_query" align="center">
		<tr class= "tabletitle">
			<th align = "right" width="10%" colspan="1">
			<label for="costType">费用类型：</label>
			</th>
			<th align = "left" colspan="2">
			<span id="myCostType">
			<!--  
			<script type="text/javascript">
				genSelBoxExp("costType",<%=Constant.COST_TYPE%>,"",false,"","onchange=\"changeCost();\"","false",'<%=Constant.COST_TYPE_01%>');
			</script>
			-->
			</span>
			</th>
			<th align="right"><strong>费用合计（元）：<font color="red"><span id="rateNoTax">0</span></font></strong>
			<th align="right"><strong>费用合计（元）[含税]：<font color="red"><span id="rateTax">0</span></font></strong></th>
		</tr>
		<tr class= "tabletitle">
			<th align = "right" width="10%" colspan="1">
			<label for="costType">是否全是招标媒体：</label>
			</th>
			<th align = "left" colspan="2">
			<script type="text/javascript">
				genSelBoxExp("isProtocol",<%=Constant.IF_TYPE%>,"",true,"","","false",'');
			</script>&nbsp;&nbsp;<font style="color:red">*</font>
			</th>
			<th align="right">&nbsp;</th>
			<th align="right">&nbsp;</th>
		</tr>
	</table>
	<a href="#anchor_A" ></a>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="12" align="left">&nbsp;活动费用录入
				<input class="cssbutton" name="add22" type="button" onclick="addTbody1();" value ='新增' />
			</th>
		</tr>
		<tr>
			<td align="center">省份</td>
			<td align="center">活动类别</td>
			<td align="center">开始日期</td>
			<td align="center">结束日期</td>
			<td align="center">活动主题</td>
			<td align="center">项目明细</td>
			<td align="center">规格/单位</td>
			<td align="center">项目单价（元）</td>
			<td align="center">项目数量</td>
			<td align="center">计划费用（元）</td>
			<td align="center">计划费用（元）[含税]</td>
			<td align="center">操作</td>
		</tr>
		<tbody id="tbody1">
		</tbody>
		<tr>
			<td colspan="9" align="right"><strong>费用小计：</strong></td>
			<td align="center"><strong><span id="totalPrice11">0</span></strong></td>
			<td align="center"><strong><span id="totalPrice1">0</span></strong></td>
			<td align="left"></td>
			<td align="left"></td>
			<td align="left"></td>
			<td align="left"></td>
		</tr>
	</table>
	<a href="#anchor_B"></a>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="16" align="left">&nbsp;媒体投放费用录入
				<input class="cssbutton" name="add11" type="button" onclick="addTbody2();" value ='新增' />
				<strong><font color="red">结算单价按“元/次”换算</font></strong>
			</th>
		</tr>
		<tr>
			<td align="center">省份</td>
			<td align="center">车型品牌</td>
			<td align="center">宣传主题</td>
			<td align="center">媒体类型</td>
			<td align="center">媒体名称</td>
			<td align="center">广告开始日期</td>
			<td align="center">广告结束日期</td>
			<td align="center">栏目/套装</td>
			<td align="center">刊发位置/播出时段</td>
			<td align="center">规格/版式</td>
			<td align="center">单日频次</td>
			<td align="center">总次数</td>
			<td align="center">结算单价 元/次</td>
			<td align="center">金额（元）</td>
			<td align="center">金额（元）[含税]</td>
			<td align="center">操作</td>
		</tr>
		<tbody id="tbody2">
		</tbody>
		<tr>
			<td colspan="13" align="right"><strong>费用小计：</strong></td>
			<td align="center"><strong><span id="totalPrice22">0</span></strong></td>
			<td align="center"><strong><span id="totalPrice2">0</span></strong></td>
			<td align="left"></td>
			<td align="left"></td>
			<td align="left"></td>
			<td align="left"></td>
			<td align="left"></td>
		</tr>
	</table>
	<!-- 添加附件start -->
	<table class="table_info" border="0" id="file">
	    <tr>
	        <th>附件列表：<input type="hidden" id="fjids" name="fjids"/>
				<span>
					<input type="button" class="cssbutton"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
				</span>
			</th>
		</tr>
		<tr>
			<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
	</table>
	<!-- 添加附件end -->
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;活动方案下发范围</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_list">
		<tr>
			<th colspan="5" align="left">经销商范围
				<input type="button" class="cssbutton" name="button1" onClick="toAddDealer();" value="新增"/>
			</th>
		</tr>
		<tr>
		<td colspan="5" align="left"></td>
		</tr>
		<tr>
		<th>区域</th>
		<th>省份</th>
		<th>经销商代码</th>
		<th>经销商简称</th>
		<th>操作</th>
		</tr>
		<tbody id="dealerTable"></tbody>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;申请操作</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<td align="right">申请意见：</td>
			<td colspan="4" align="left">
				<textarea name="remark" id="remark" rows="4" cols="70"></textarea>&nbsp;&nbsp;<font color="red">*</font>
			</td>
		</tr>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<td colspan="4" align="center">
				<input type="hidden" name="costTypes1" id="costTypes1"/>
				<input type="hidden" name="itemNames" id="itemNames"/>
				<input type="hidden" name="itemRemarks" id="itemRemarks"/>
				<input type="hidden" name="itemPrices" id="itemPrices"/>
				<input type="hidden" name="itemCounts" id="itemCounts"/>
				<input type="hidden" name="costSources1" id="costSources1"/>
				<input type="hidden" name="planCosts1" id="planCosts1"/>
				<input type="hidden" name="costTypes2" id="costTypes2"/>
				<input type="hidden" name="advDates" id="advDates"/>
				<input type="hidden" name="endDates" id="endDates"/>
				<input type="hidden" name="advSubjects" id="advSubjects"/>
				<input type="hidden" name="advMedias" id="advMedias"/>
				<input type="hidden" name="mediaSizes" id="mediaSizes"/>
				<input type="hidden" name="mediaPrices" id="mediaPrices"/>
				<input type="hidden" name="mediaCounts" id="mediaCounts"/>
				<input type="hidden" name="costSources2" id="costSources2"/>
				<input type="hidden" name="planCosts2" id="planCosts2"/>
				
				<input type="hidden" name="dealerIds" id="dealerIds" value=""/>
				<input type="hidden" name="subm" id="subm" value=""/>
				<input type="button" class="cssbutton" name="button6" onClick="userValidate('1');" value="保存"/>
				<input type="button" class="cssbutton" name="button5" onClick="userValidate('0');" value="提报"/>
				<input type="button" class="cssbutton" name="button4" onClick="toBack();" value="返回"/>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript"><!--
	//页面初始化
	function doInit(){
		loadcalendar();
		getDealerAreaId(document.all.areaId.value);
		chkBigCus() ;
	}
	//设置业务范围ID,经销商ID
	function getDealerAreaId(value){
		getAvailableAmount(value);
	}
	//选择活动车型
	function toSelect(){
		OpenHtmlWindow('<%=contextPath%>/sales/marketmanage/planissued/ActivitiesPlanMake/queryModelInit.do',800,450);
	}
	//清除功能
	function toClear(){
		document.getElementById("campaignModel").value="";
		document.getElementById("modelId").value="";
	}
	//新增列表1
	function addTbody1(){
		var timeValue = new Date().getTime();
		var newRow = document.getElementById("tbody1").insertRow();
		newRow.className  = "table_list_row2";
		var region__B = 'reg' + timeValue ;
		
		var newCell = newRow.insertCell(0);
		newCell.align = "center";
		newCell.innerHTML = "<select id=\"reg" + timeValue + "\" name=\"reg\"></select>";
		newCell = newRow.insertCell(1);
		newCell.align = "center";
		newCell.innerHTML = genSelBoxStrExp("campaignType",<%=Constant.COSTACTIVITY_TYPE%>,"",false,"","","false",'');
		// 开始日期
		newCell = newRow.insertCell(2);
		newCell.align = "center"; 
		newCell.innerHTML = "<input name='aBeginDate'  class='short_txt' id='aBeginDate"+timeValue+"' type='text' readOnly='readOnly' group='aBeginDate"+timeValue+",aEndDate"+timeValue+"' datatype=\"0,is_date,10\"/> <input class='time_ico' type='button' onClick='showcalendar(event,\"aBeginDate"+timeValue+"\",false);' value='&nbsp;'/>";
		// 结束日期
		newCell = newRow.insertCell(3);
		newCell.align = "center"; 
		newCell.innerHTML = "<input name='aEndDate' class='short_txt' id='aEndDate"+timeValue+"' type='text' readOnly='readOnly' datatype=\"0,is_date,10\"/> <input class='time_ico' type='button' onClick='showcalendar(event,\"aEndDate"+timeValue+"\",false);' value='&nbsp;'/>";
		newCell = newRow.insertCell(4);
		newCell.align = "center";
		newCell.innerHTML = "<input id='itemContent"+timeValue+"' name='itemContent' type='text' size='20' maxlength='50'/>";
		newCell = newRow.insertCell(5);
		newCell.align = "center";
		newCell.innerHTML = "<input id='itemName"+timeValue+"' name='itemName' type='text' size='15' maxlength='15'/>";
		newCell = newRow.insertCell(6);
		newCell.align = "center";
		newCell.innerHTML = "<input id='itemRemark"+timeValue+"' name='itemRemark' type='text' size='20' maxlength='50'/>";
		newCell = newRow.insertCell(7);
		newCell.align = "center";
		newCell.innerHTML = "<input id='itemPrice"+timeValue+"' name='itemPrice' type='text' size='8' maxlength='10' onBlur='toChangeNum1(\""+timeValue+"\");'/>";
		newCell = newRow.insertCell(8);
		newCell.align = "center";
		newCell.innerHTML = "<input id='itemCount"+timeValue+"' name='itemCount' type='text' size='3' maxlength='6' onBlur='toChangeNum1(\""+timeValue+"\");'/>";
		newCell = newRow.insertCell(9);
		newCell.align = "center";
		newCell.innerHTML = "<span id='planCost11"+timeValue+"'>0</span><input id='cost11"+timeValue+"' name='planCost11' type='hidden' value='0'/>";
		newCell = newRow.insertCell(10);
		newCell.align = "center";
		newCell.innerHTML = "<span id='planCost1"+timeValue+"'>0</span><input id='cost1"+timeValue+"' name='planCost1' type='hidden' value='0'/>";
		newCell = newRow.insertCell(11);
		newCell.align = "center";
		newCell.innerHTML = "<a href='#anchor_A' onclick='delTbody1();'>[删除]</a>";
		
		_genPro__(region__B);
		
		notifySession() ;
	}
	//计划费用1
	function toChangeNum1(value1){
		var type = document.getElementById('costType').value ;
		var count = document.getElementById("itemCount"+value1).value;
		var price = document.getElementById("itemPrice"+value1).value;
		var planCost =0 ;
		if(type == <%= Constant.COST_TYPE_02%> || type == <%= Constant.COST_TYPE_03%>) {
			planCost = Number(count) * Number(price) * parseFloat(<%= Constant.ACTIVITIES_TAX%>);
		} else if(type == <%= Constant.COST_TYPE_05%>) {
			planCost = Number(count) * Number(price) * parseFloat(<%= Constant.FLEET_ACTIVITIES_TAX%>) ;
		} else {
			planCost = Number(count) * Number(price) ;
		}
		document.getElementById("planCost1"+value1).innerText=amountFormat(planCost);
		document.getElementById("cost1"+value1).value = planCost;
		var planCost = document.getElementsByName("planCost1");
		var totalPrice = 0;
		for (var i=0; i<planCost.length; i++){  
			totalPrice += Number(planCost[i].value);  
		}
		document.getElementById("totalPrice1").innerText = amountFormat(totalPrice);
		
		toChangeNum1_New(value1) ;
		
		getRateCount("rateNoTax", "totalPrice11", "totalPrice22") ;
		getRateCount("rateTax", "totalPrice1", "totalPrice2") ;
	}
	function toDelChangeNum1(){
		var planCost = document.getElementsByName("planCost1");
		var totalPrice = 0;
		for (var i=0; i<planCost.length; i++){  
			totalPrice += Number(planCost[i].value);  
		}
		document.getElementById("totalPrice1").innerText = amountFormat(totalPrice);
		
		var planCost11 = document.getElementsByName("planCost11");
		var totalPrice11 = 0;
		for (var i=0; i<planCost11.length; i++){  
			totalPrice11 += Number(planCost11[i].value);  
		}
		document.getElementById("totalPrice11").innerText = amountFormat(totalPrice11);
		
		getRateCount("rateNoTax", "totalPrice11", "totalPrice22") ;
		getRateCount("rateTax", "totalPrice1", "totalPrice2") ;
	}
	//删除列表1
	function delTbody1(){	
	  	document.getElementById("tbody1").deleteRow(window.event.srcElement.parentElement.parentElement.rowIndex-2);  
	  	toDelChangeNum1();
	}
	//新增列表2
	function addTbody2(){
		loadcalendar();
		var timeValue = new Date().getTime();
		var newRow = document.getElementById("tbody2").insertRow();
		newRow.className  = "table_list_row2";
		var region__A = 'region' + timeValue ;
		
		// 省份
		var newCell = newRow.insertCell(0);
		newCell.align = "center";
		newCell.innerHTML = "<select id=\"region" + timeValue + "\" name=\"region\"></select>";
		// 车型品牌
		var newCell = newRow.insertCell(1);
		newCell.align = "center";
		newCell.innerHTML = genSelBoxStrExp("mediaModel",<%=Constant.MEDIA_MODEL%>,"",false,'',"","false",'');
		// 宣传主题
		newCell = newRow.insertCell(2);
		newCell.align = "center";
		newCell.innerHTML = "<input id='advSubject"+timeValue+"' name='advSubject' type='text' size='10' maxlength='15'/>";
		// 媒体类型
		newCell = newRow.insertCell(3);
		newCell.align = "center";
		newCell.innerHTML = genSelBoxStrExp("mediaType",<%=Constant.MEDIA_TYPE%>,"",false,'',"","false",'');
		// 媒体名称
		newCell = newRow.insertCell(4);
		newCell.align = "center";
		newCell.innerHTML = "<input id='mediaName"+timeValue+"' name='mediaName' type='text' size='10' maxlength='50'/>";
		// 广告开始日期
		newCell = newRow.insertCell(5);
		newCell.align = "center"; 
		newCell.innerHTML = "<input name='advDate'  class='short_txt' id='advDate"+timeValue+"' type='text' readOnly='readOnly' datatype=\"1,is_date,10\"/> <input class='time_ico' type='button' onClick='showcalendar(event,\"advDate"+timeValue+"\",false);' value='&nbsp;'/>";
		// 广告结束日期
		newCell = newRow.insertCell(6);
		newCell.align = "center"; 
		newCell.innerHTML = "<input name='myendDate' class='short_txt' id='myendDate"+timeValue+"' type='text' readOnly='readOnly' datatype=\"1,is_date,10\"/> <input class='time_ico' type='button' onClick='showcalendar(event,\"myendDate"+timeValue+"\",false);' value='&nbsp;'/>";
		// 栏目/套装
		newCell = newRow.insertCell(7);
		newCell.align = "center";
		newCell.innerHTML = "<input id='mediaColumn"+timeValue+"' name='mediaColumn' type='text' size='10' maxlength='50'/>";
		// 刊发位置/播出时段
		newCell = newRow.insertCell(8);
		newCell.align = "center";
		newCell.innerHTML = "<input id='publish"+timeValue+"' name='publish' type='text' size='10' maxlength='50'/>";
		// 规格/版式
		newCell = newRow.insertCell(9);
		newCell.align = "center";
		newCell.innerHTML = "<input id='mediaSize"+timeValue+"' name='mediaSize' type='text' size='10' maxlength='15'/>";
		// 单日频次
		newCell = newRow.insertCell(10);
		newCell.align = "center";
		newCell.innerHTML = "<input id='mediaCount"+timeValue+"' name='mediaCount' type='text' size='3' maxlength='6'/>";
		// 总次数
		newCell = newRow.insertCell(11);
		newCell.align = "center";
		newCell.innerHTML = "<input id='totalCount"+timeValue+"' name='totalCount' type='text' size='3' maxlength='6' onBlur='toChangeNum2(\""+timeValue+"\");'/>";
		// 结算单价 元/次、天、月
		newCell = newRow.insertCell(12);
		newCell.align = "center";
		newCell.innerHTML = "<input id='mediaPrice"+timeValue+"' name='mediaPrice' type='text' size='8' maxlength='10' onBlur='toChangeNum2(\""+timeValue+"\");'/>";
		// 金额（元）
		newCell = newRow.insertCell(13);
		newCell.align = "center";
		newCell.innerHTML = "<span id='planCost22"+timeValue+"'>0</span><input id='cost22"+timeValue+"' name='planCost22' type='hidden' value='0'/>";
		// 金额（元）
		newCell = newRow.insertCell(14);
		newCell.align = "center";
		newCell.innerHTML = "<span id='planCost2"+timeValue+"'>0</span><input id='cost2"+timeValue+"' name='planCost2' type='hidden' value='0'/>";
		// 操作
		newCell = newRow.insertCell(15);
		newCell.align = "center";
		newCell.innerHTML = "<a href='#anchor_B' onclick='delTbody2();'>[删除]</a>";

		_genPro__(region__A);
		
		notifySession() ;
	}
	//计划费用2
	function toChangeNum2(value1){
		var type = document.getElementById('costType').value ;
		var count = document.getElementById("mediaPrice"+value1).value;
		var price = document.getElementById("totalCount"+value1).value;
		var planCost = 0 ;
		if(type == <%= Constant.COST_TYPE_02%> || type == <%= Constant.COST_TYPE_03%>) {
			planCost = Number(count) * Number(price) * parseFloat(<%= Constant.ACTIVITIES_TAX%>);
		} else if(type == <%= Constant.COST_TYPE_05%>) {
			planCost = Number(count) * Number(price) * parseFloat(<%= Constant.FLEET_ACTIVITIES_TAX%>) ;
		} else {
			planCost = Number(count) * Number(price) ;
		}
		document.getElementById("planCost2"+value1).innerText=amountFormat(planCost);
		document.getElementById("cost2"+value1).value = planCost;
		var planCost = document.getElementsByName("planCost2");
		var totalPrice = 0;
		for (var i=0; i<planCost.length; i++){  
			totalPrice += Number(planCost[i].value);  
		}
		document.getElementById("totalPrice2").innerText = amountFormat(totalPrice);
		
		toChangeNum2_New(value1) ;
		
		getRateCount("rateNoTax", "totalPrice11", "totalPrice22") ;
		getRateCount("rateTax", "totalPrice1", "totalPrice2") ;
	}
	
	String.prototype.replaceAll  = function(s1,s2){    
		return this.replace(new RegExp(s1,"gm"),s2);    
		}   
	
	function toDelChangeNum2(){
		var planCost = document.getElementsByName("planCost2");
		var totalPrice = 0;
		for (var i=0; i<planCost.length; i++){  
			totalPrice += Number(planCost[i].value);  
		}
		document.getElementById("totalPrice2").innerText = amountFormat(totalPrice);
		
		var planCost22 = document.getElementsByName("planCost22");
		var totalPrice22 = 0;
		for (var i=0; i<planCost22.length; i++){  
			totalPrice22 += Number(planCost22[i].value);  
		}
		document.getElementById("totalPrice22").innerText = amountFormat(totalPrice22);
		
		getRateCount("rateNoTax", "totalPrice11", "totalPrice22") ;
		getRateCount("rateTax", "totalPrice1", "totalPrice2") ;
	}
	
	//删除列表1
	function delTbody2(){	
	  	document.getElementById("tbody2").deleteRow(window.event.srcElement.parentElement.parentElement.rowIndex-2); 
	  	toDelChangeNum2(); 
	}

	function t_trim(str){
		return str.replace(/(^\s*)|(\s*$)/g, "");
	}

	String.prototype.replaceAll  = function(s1,s2){    
		return this.replace(new RegExp(s1,"gm"),s2);    
		}   
	
	
	function userValidate(value) {
		var userId = this.parent.document.getElementById('userId').value;
		var sessionId = this.parent.document.getElementById('sessionId').value;
		makeCall('<%=request.getContextPath()%>/util/UserValidator/validate.json', toSubmit , {userId : userId, sessionId : sessionId, value : value});
	}
	
	
	//操作提交
	function toConfirm(value){
		document.getElementById('subm').value = value ;
		setDisTrue('button4','button5','button6') ;
		
		makeNomalFormCall('<%=contextPath%>/sales/marketmanage/planmanage/ActivitiesSpacePlanReport/spacePlanReportConfirm.json',showResult,'fm') ;

		setTimeout("setDisFalse('button4','button5','button6')", 10000) ;
	}

	//返回方法
	function toBack(){
		setDisTrue('button4','button5','button6') ;
		$('fm').action= '<%=contextPath%>/sales/marketmanage/planmanage/ActivitiesSpacePlanReport/activitiesPlanReportInit.do';
		$('fm').submit();
	}

	//回调方法
	function showResult(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("操作成功！");
			$('fm').action='<%=request.getContextPath()%>/sales/marketmanage/planmanage/ActivitiesSpacePlanReport/activitiesPlanReportInit.do';
			$('fm').submit();
		}else{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
--></script>
</body>
</html>