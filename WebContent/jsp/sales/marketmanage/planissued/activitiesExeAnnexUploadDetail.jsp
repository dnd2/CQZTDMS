<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>活动方案附件上传</title>
<% String contextPath = request.getContextPath();  
List  executePlans = (List)request.getAttribute("executePlans");
%>
<script type="text/javascript">
<!--
//选择经销商
function toAddDealer(){
	var areaId = document.getElementById("areaId").value ;
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

function chkDesc() {
	var sChkDesc = document.getElementById("checkDesc").value ;
	
	if(sChkDesc.length <= 0) {
		MyAlert("审核意见不能为空！") ;
		
		return false ;
	}
	
	return true ;
}

// 删除表格
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

function chkFile() {
	var oTab = document.getElementById('fileUploadTab') ;
	var iLen = oTab.rows.length ;
	
	if (iLen <= 1) {
		return false ;
	} else {
		return true ; 
	}
}

function getTotalPrice_noTax(nId, nType, nPrice) {
	spanNoTax = document.getElementById(nId) ;

	if(nType == <%= Constant.COST_TYPE_02%> || nType == <%= Constant.COST_TYPE_03%>) {
		spanNoTax.innerText = amountFormat(nPrice / parseFloat(<%= Constant.ACTIVITIES_TAX%>)) ;
	} else if(nType == <%= Constant.COST_TYPE_05%>) {
		spanNoTax.innerText = amountFormat(nPrice / parseFloat(<%= Constant.FLEET_ACTIVITIES_TAX%>)) ;
	} else {
		spanNoTax.innerText = amountFormat(nPrice) ;
	}
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
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 市场活动管理  &gt; 活动方案下发 &gt; 活动执行方案审批</div>
<form method="post" name="fm" id="fm">
<input type="hidden" value="${areaId}" name="areaId" id="areaId"></input>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;方案信息</div>
	<table id="table1" width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="5" align="left">&nbsp;方案说明</th>
		</tr>
		<tr>
				<td align="right">提报日期：</td>
				<td align="left">${map2.SUBMITS_DATE}</td>
				<td align="right"></td>
				<td align="left"></td>
				<td></td>
			</tr>
		<c:if test="${mapC.cy03 != camType}">
			<tr>
				<td align="right">经销商代码：</td>
				<td align="left">${map2.DEALER_CODE}</td>
				<td align="right">经销商名称：</td>
				<td align="left">${map2.DEALER_NAME}</td>
				<td></td>
			</tr>
		</c:if>
		<c:if test="${mapC.cy03 == camType}">
			<tr>
				<td align="right">区域代码：</td>
				<td align="left">${map2.ORG_CODE}</td>
				<td align="right">区域名称：</td>
				<td align="left">${map2.ORG_NAME}</td>
				<td></td>
			</tr>
		</c:if>
		<tr>
			<td align="right">品牌：</td>
			<td align="left">${map2.GROUP_NAME}</td>
			<td align="right">活动车型：</td>
			<td align="left"><span id="activeVehicle">${map2.PROD_NAME} </span></td>
			<td width="20%"></td>
		</tr>
		<tr>
			<td align="right">车厂方案编号：</td>
			<td align="left">${map2.CAMPAIGN_NO}</td>
			<td align="right">方案名称：</td>
			<td align="left">${map2.CAMPAIGN_NAME}</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">计划开始日期：</td>
			<td align="left">${map2.START_DATE}</td>
			<td align="right">计划结束日期：</td>
			<td align="left">${map2.END_DATE}</td>
			<td></td>
		</tr>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="12" align="left">&nbsp;可用费用
			</th>
		</tr>
		<tr class="table_list_row2">
			<td align="center">服务中心市场推广费用</td>
			<td align="center">运营大区市场推广费用</td>
			<td align="center">轿车事业部费用</td>
			<td align="center">长安汽车广告费</td>
		</tr>
		<tr class="table_list_row2">
			<td align="center"><span id='availableAmount1'></span><input type="hidden" name="availableAmount5" id="availableAmount5" value=""/></td>
			<td align="center"><span id='availableAmount2'></span><input type="hidden" name="availableAmount6" id="availableAmount6" value=""/></td>
			<td align="center"><span id='availableAmount3'></span><input type="hidden" name="availableAmount7" id="availableAmount7" value=""/></td>
			<td align="center"><span id='availableAmount4'></span><input type="hidden" name="availableAmount8" id="availableAmount8" value=""/></td>
		</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;费用类别</div>
	<table class="table_query" align="center">
		<tr class= "tabletitle">
			<th align = "right" width="10%" colspan="1">
			<label for="costType">费用类型：</label>
			</th>
			<th align = "left" colspan="2">
				<strong>
				<script type="text/javascript">
						writeItemValue('${type }');
				</script>
				</strong>
				<input type="hidden" name="costType" value="${type }" />
			</th>
			<th align="right"><strong>费用合计（元）：<font color="red"><span id="rateNoTax">0</span></font></strong>
			<th align="right"><strong>费用合计（元）[含税]：<font color="red"><span id="rateTax">0</span></font></strong></th>
		</tr>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="15" align="left">&nbsp;活动费用信息</th>
		</tr>
		<tr>
			<td align="center">省份</td>
			<td align="center">活动类型</td>
			<td align="center">开始日期</td>
			<td align="center">结束日期</td>
			<td align="center">活动主题</td>
			<td align="center">项目明细</td>
			<td align="center">规格/单位</td>
			<td align="center">项目单价（元）</td>
			<td align="center">项目数量</td>
			<td align="center">计划费用（元）</td>
			<td align="center">计划费用（元）[含税]</td>
			<td align="center">服务中心市场推广费用</td>
			<td align="center">运营大区市场推广费用</td>
			<td align="center">轿车事业部费用</td>
			<td align="center">长安汽车广告费</td>
		</tr>
		<c:forEach items="${list1}" var="list1">
			<tr class="table_list_row2" align="center">
				<td>
					<script type="text/javascript">
						writeRegionName('<c:out value="${list1.REGION}"/>') ;
					</script>
				</td>
				<td>
					<script type="text/javascript">
						writeItemValue('${list1.ACTIVITY_TYPE}');
					</script>
					<input type="hidden" name="activityType" value="${list1.ACTIVITY_TYPE}"/>
				</td>
				<td>${list1.START_DATE}</td>
				<td>${list1.END_DATE}</td>
				<td>${list1.ACTIVITY_CONTENT}</td>
				<td>${list1.ITEM_NAME}</td>
				<td>${list1.ITEM_REMARK}</td>
				<td>${list1.ITEM_PRICE}</td>
				<td>${list1.ITEM_COUNT}</td>
				<td><script>document.write(${list1.ITEM_COUNT} * ${list1.ITEM_PRICE})</script></td>
				<td>
					${list1.PLAN_COST}<input name="planCost1" type="hidden" value="${list1.PLAN_COST}"/>
				</td>
				<td>
				<c:if test="${list1.COST_TYPE == mapA.DEALER}">
					${list1.DEALER_COST}
				</c:if>
				</td>
				<td>
				<c:if test="${list1.COST_TYPE == mapA.AREA}">
					${list1.AREA_COST}
				</c:if>
				<c:if test="${mapC.cy03 == camType}">
					${amountList2}
				</c:if>
				</td>
				<td>
				<c:if test="${list1.COST_TYPE == mapA.OEM1}">
					${amountList4}
				</c:if>
				</td>
				<td>
				<c:if test="${list1.COST_TYPE == mapA.OEM2}">
					${amountList5}
				</c:if>
				</td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="9" align="right"><strong>费用小计：</strong></td>
			<td align="center"><strong><span id="noTaxActive"></span></strong></td>
			<td align="center"><strong><span id="totalPrice1"></span></strong></td>
			<td align="left"></td>
		</tr>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="18" align="left">&nbsp;媒体投放费用信息</th>
		</tr>
		<tr>
			<td align="center">省份</td>
			<td align="center">车型品牌</td>
			<td align="center">宣传主题</td>
			<td align="center">媒体类型</td>
			<td align="center">媒体名称</td>
			<td align="center">广告日期</td>
			<td align="center">栏目/套装</td>
			<td align="center">刊发位置/播出时段</td>
			<td align="center">规格/版式</td>
			<td align="center">单日频次</td>
			<td align="center">总次数</td>
			<td align="center">结算单价 元/次</td>
			<td align="center">计划费用（元）</td>
			<td align="center">计划费用（元）[含税]</td>
			<td align="center">服务中心市场推广费用</td>
			<td align="center">运营大区市场推广费用</td>
			<td align="center">轿车事业部费用</td>
			<td align="center">长安汽车广告费</td>
		</tr>
		<c:forEach items="${list2}" var="list2">
			<tr class="table_list_row2" align="center">
				<td>
						<script type="text/javascript">
							writeRegionName('<c:out value="${list2.REGION}"/>') ;
						</script>
					</td>
					<td>
						<label>
							<script type="text/javascript">
								writeItemValue("${list2.MEDIA_MODEL}") ;
							</script>
						</label>
					</td>
					<td>
						${list2.ADV_SUBJECT}
					</td>
					<td>
						<label>
							<script type="text/javascript">
								writeItemValue("${list2.MEDIA_TYPE}") ;
							</script>
						</label>
					</td>
					<td>
						${list2.MEDIA_NAME}
					</td>
					<td>
						${list2.ADV_DATE}~${list2.END_DATE}
					</td>
					<td>
						${list2.MEDIA_COLUMN}
					</td>
					<td>
						${list2.MEDIA_PUBLISH}
					</td>
					<td>
						${list2.MEDIA_SIZE}
					</td>
					<td>
						${list2.ITEM_COUNT}
					</td>
					<td>
						${list2.TOTAL_COUNT}
					</td>
					<td>
						${list2.ITEM_PRICE}
					</td>
					<td><script>document.write(${list2.TOTAL_COUNT} * ${list2.ITEM_PRICE})</script></td>
					<td>
						<span id="planCost2${list2.COST_ID}">${list2.PLAN_COST}</span><input id="cost2${list2.COST_ID}" name="planCost2" type="hidden" value="${list2.PLAN_COST}"/>
					</td>
					<td>
				<c:if test="${list2.COST_TYPE == mapA.DEALER}">
					${list2.DEALER_COST}
				</c:if>
				</td>
				<td>
				<c:if test="${list2.COST_TYPE == mapA.AREA}">
					${list2.AREA_COST}
				</c:if>
				<c:if test="${mapC.cy03 == camType}">
					${amountList2}
				</c:if>
				</td>
				<td>
				<c:if test="${list2.COST_TYPE == mapA.OEM1}">
					${amountList4}
				</c:if>
				</td>
				<td>
				<c:if test="${list2.COST_TYPE == mapA.OEM2}">
					${amountList5}
				</c:if>
				</td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="12" align="right"><strong>费用小计：</strong></td>
			<td align="center"><strong><span id="noTaxMedia"></span></strong></td>
			<td align="center"><strong><span id="totalPrice2"></span></strong></td>
			<td align="left"></td>
		</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;审批信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
	        <th colspan="7">&nbsp;审批记录</th>
		</tr>
		<tr align="center">
			<td>审批部门</td>
			<td>审批职务</td>
			<td>审批人员</td>
			<td>审批状态</td>
			<td>审批意见</td>
			<td>审批时间</td>
			<td>审批附件</td>
		</tr>
		<c:forEach items="${list3}" var="list3">
			<tr class="table_list_row2" align="center">
				<td>${list3.ORG_NAME}</td>
				<td>${list3.POSE_NAME}</td>
				<td>${list3.NAME}</td>
				<td>
					<script type="text/javascript">
						writeItemValue('${list3.CHECK_STATUS}');
					</script>
				</td>
				<td>${list3.CHECK_DESC}</td>
				<td>${list3.CHECK_DATE}</td>
				<td><a target="_blank" href="${list3.FILEURL}">${list3.FILENAME}</a></td>
			</tr>
		</c:forEach>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
	        <th colspan="3">&nbsp;审批录入</th>
		</tr>
		<tr>
			<td align="right">审批意见：</td>
			<td align="left"><textarea name="checkDesc" id="checkDesc" rows="4" cols="50"></textarea><font color="red">*</font></td>
		</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;执行方案附件上传</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
	        <th colspan="3">附件列表：<input type="hidden" id="fjids" name="fjids"/>
				<span>
					<input type="button" class="cssbutton"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
				</span>
			</th>
		</tr>
		<tr>
			<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
		<tr>
			<td colspan="4" align="center">
				<input type="hidden" name="campaignId" value="${map2.CAMPAIGN_ID}" />
				<input type="hidden" name="dealerId" value="${map2.DEALRE_ID}" />
				<input type="hidden" name="executeId" <c:if test="${mapC.cy03 != camType}">value="${map2.EXECUTE_ID}"</c:if><c:if test="${mapC.cy03 == camType}">value="${map2.SPACE_ID}"</c:if> />
				<input type="hidden" name="flag" id="flag"/>
				<input type="hidden" name="camType" id="camType" value="${camType }" />
				<input type="hidden" name="dealerIds" id="dealerIds" value=""/>
				<input type="button" class="cssbutton" name="button3" onClick="toSubmit();" value="执行"/>
				<input type="button" class="cssbutton" name="toRetrun" onClick="toReturnReal();" value="驳回"/>
				<input type="button" class="cssbutton" name="button4" onClick="toBack();" value="返回"/>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
//设置可用费用
function getAvailableAmount(){
	var availableAmount1=${amountList1} ;
	document.getElementById("availableAmount1").innerHTML= amountFormat(availableAmount1);
	document.getElementById("availableAmount5").value= availableAmount1;
   	var availableAmount2=${amountList2} ;
  	document.getElementById("availableAmount2").innerText= amountFormat(availableAmount2);
  	document.getElementById("availableAmount6").value= availableAmount2;
  	var availableAmount4=${amountList4} ;
  	var availableAmount5=${amountList5} ;
  	document.getElementById("availableAmount3").innerText= amountFormat(availableAmount4);
  	document.getElementById("availableAmount7").value= availableAmount4;
  	document.getElementById("availableAmount4").innerText= amountFormat(availableAmount5);
  	document.getElementById("availableAmount8").value= availableAmount5;
}

	//页面初始化
	function doInit(){
		document.getElementById("activeVehicle").innerHTML = "${group_code}"
		getAvailableAmount();
		totalPrice();
		
		getRateCount("rateNoTax", "noTaxActive", "noTaxMedia") ;
		getRateCount("rateTax", "totalPrice1", "totalPrice2") ;
	}
	//费用合计
	function totalPrice(){
		var planCost1 = document.getElementsByName("planCost1");
		var planCost2 = document.getElementsByName("planCost2");
		var totalPrice1 = 0;
		var totalPrice2 = 0;
		for (var i=0; i<planCost1.length; i++){  
			totalPrice1 += Number(planCost1[i].value);  
		}
		for (var i=0; i<planCost2.length; i++){  
			totalPrice2 += Number(planCost2[i].value);  
		}
		document.getElementById("totalPrice1").innerText = amountFormat(totalPrice1);
		document.getElementById("totalPrice2").innerText = amountFormat(totalPrice2);
		
		getTotalPrice_noTax("noTaxMedia", ${type}, totalPrice2) ;
		getTotalPrice_noTax("noTaxActive", ${type}, totalPrice1) ;
	}
	//审核校验
	function toSubmit(){
		/* 
		if(!chkFile()) {
			MyAlert("需要添加上传附件！") ;
			return false ;
		}
		 */
		if(!chkDesc()) {
			return false ;
		}
		
		MyConfirm("确认执行？", toConfirm);
	}
	
	function toReturnReal() {
		MyConfirm("确认驳回？", toReturn);
	}
	
	function toReturn() {
		makeNomalFormCall('<%=request.getContextPath()%>/sales/marketmanage/planissued/ActivitiesExeAnnexUpload/activitiesReturn.json',showResult,'fm');
	}
	//提交操作
	function toConfirm(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/marketmanage/planissued/ActivitiesExeAnnexUpload/activitiesConfirm.json',showResult,'fm');
	}
	//返回方法
	function toBack(){
		history.go(-1) ;
		//$('fm').action='<%=request.getContextPath()%>/sales/marketmanage/planissued/ActivitiesExeAnnexUpload/activitiesAnnexUploadInit.do';
		//$('fm').submit();
	}
	//回调方法
	function showResult(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("操作成功！");
			$('fm').action='<%=request.getContextPath()%>/sales/marketmanage/planissued/ActivitiesExeAnnexUpload/activitiesAnnexUploadInit.do';
			$('fm').submit();
		}else{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
</script>
</body>
</html>