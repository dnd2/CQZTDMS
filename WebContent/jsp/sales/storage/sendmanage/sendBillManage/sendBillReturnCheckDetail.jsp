<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
	Map<String, Object> vehicleInfo = (Map<String, Object>)request.getAttribute("vehicleInfo");
%>
<%@taglib uri="/jstl/cout" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>运单回厂确认</title>
<script type="text/javascript">
function doInit(){
		  //初始化时间控件
		choiseTake("${result.SEND_DEALER_ID}","${result.ADDRESS_ID}");
}

function checkSubmit(){
	var chks = document.getElementsByName("chk");
	var vehicleIds = "";
	if(chks != null && chks.length > 0) {
		for(var i = 0;i < chks.length;i++) {
			if(chks[i].checked){
				vehicleIds += chks[i].value+",";
			}
		}
		if(vehicleIds == ""){
			MyAlert("请选择要处理的数据!");
			return;
		}
		vehicleIds = vehicleIds.substr(0,vehicleIds.length - 1);
	} else {
		MyAlert("无待处理数据!");
		return;
	}
	MyConfirm("确认提交?",submitAction,[vehicleIds]);
}
function submitAction(vehicleIds){
	document.getElementById("queren").disabled = true;
	var billId = document.getElementById("billId").value;
	document.getElementById("vehicleIds").value=vehicleIds;
    fm.action = "<%=contextPath%>/sales/storage/sendmanage/SendBillReturnCheck/checkSubmit.do?billId="+billId;
    fm.submit();
}
function queryVin(){
	var billId = document.getElementById("billId").value;
	var vin = document.getElementById("vin").value;
	var vins = document.getElementsByName("vinHidden");
	for(var i = 0;i < vins.length; i++) {
		document.getElementById("tr"+i).style["display"]="none";
	}
	makeCall("<%=contextPath%>/sales/storage/sendmanage/SendBillReturnCheck/queryVinInfo.json?billId="+billId+"&vin="+vin,function(json){
		if(json.vinList != null){
			for(var i = 0; i < json.vinList.length;i++){
				for(var j = 0;j < vins.length; j++) {
					if(json.vinList[i].VIN == vins[j].value){
						document.getElementById("tr"+j).style["display"]="inline";
					} 
				}
			}
		}
	},"");
}

function checkAll(){
	var chkBox = document.getElementById("chkAll");
	var chks = document.getElementsByName("chk");
	for(var i = 0; i < chks.length; i++){
		if(chkBox.checked) {
			chks[i].checked = true;
		} else {
			chks[i].checked = false;
		}
	}
}

function choiseTake(dealerId,selValue){
	if(dealerId == ""){
		return;
	}
	var url = "<%=contextPath%>/common/AjaxSelectAction/getAddressListByDealerId.json";
	var timetmp = new Date().getTime();
	makeCall(url, function(json) {
		
		var obj = document.getElementById("deliveryAddress");

		obj.options.length = 0;

		for ( var i = 0; i < json.info.length; i++) {
			obj.options[i] = new Option(json.info[i].ADDRESS, json.info[i].ID + '');
			obj.options[i].lang = json.info[i].CITY_ID;
			if(json.info[i].ID == selValue) {
				obj.options[i].selected = true;
			}
		}
		
	}, { dealerId : dealerId, time : timetmp });
}
</script>
</head>
<body onunload='javascript:destoryPrototype();' onload="ChangeDateToString() ;changeFleet();" onload="doInit();">
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
		&nbsp;当前位置：  整车物流管理&gt; 发运管理 &gt;运单回厂确认</div>
	<form id="fm" name="fm" method="post"  enctype="multipart/form-data" >
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="billId" name="billId" value="${result.BILL_ID }"/>
		<input type="hidden" id="logiId" name="logiId" value="${result.LOGI_ID }"/>
		<input type="hidden" id="vehicleIds" name="vehicleIds" />
		<table class="table_query" border="0">
			<th colspan="11">&nbsp;基本信息</th>
			<tr>
				<td class="right">运单号：</td>
				<td>${result.BILL_NO }</td>
				<td class="right">验收时间：</td>
				<td>
					<c:if test='${checkDate == ""}'>
						暂无车辆验收
					</c:if>
					<c:if test='${checkDate != ""}'>
						${checkDate }
					</c:if>
				</td>
			</tr>
			<tr>
				<td class="right">经销商代码：</td>
				<td>${result.DEALER_CODE }</td>
				<td class="right">经销商名称：</td>
				<td>${result.DEALER_NAME }</td>
			</tr>
			<tr>
				<td class="right">运单生成时间：</td>
				<td>${result.BILL_CRT_DATE }</td>
				<td class="right">运单生成人：</td>
				<td>${result.NAME }</td>
			</tr>
			<tr>
				<td class="right">承运商：</td>
				<td>${result.LOGI_FULL_NAME }</td>
				<td class="right">车辆数量：</td>
				<td>${result.VEH_NUM}</td>
			</tr>
			<tr>
				<td class="right">已确认数量：</td>
				<td>${result.BACKCRM_NUM }</td>
				<td class="right">未确认数量：</td>
				<td>${result.NO_CRM_NUM}</td>
			</tr>
			<tr>
				<td class="right">预计到达时间：</td>
				<td>${expectArriveDate}</td>
				<td class="right">实际到达时间：</td>
				<td>
					<input name="arrive_date" readonly="readonly" value="${expectArriveDate}"  id="t1" value='' type="text" maxlength="20"  size="20" class="middle_txt" datatype="1,is_date,10" />
     			    <input class="time_ico" onclick="showcalendar(event, 't1', false);" type="button" />
				</td>
			</tr>
			<tr>
				<td class="right">发运地址：</td>
				<td colspan="3">
					<select id="deliveryAddress" name="deliveryAddress" style="width: 379px;"></select>
				</td>
			</tr>
			<tr>
				<td class="right">备注：</td>
				<td colspan="3">
					<textarea rows="3" cols="50" id="remark" datatype="1,is_textarea,1000" maxlength="1000" name="remark"></textarea>
				</td>
			</tr>
		</table>
		<table class=table_query id="showCheckInfo" style="" border="1">
		      <tr>
		        <th colspan=11 nowrap align=left><img  src="<%=contextPath%>/img/subNav.gif" />车辆明细&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
		      </tr>
		      <tr>
		      	<td class="right" style="border:none;">VIN:</td>
		      	<td align="left" style="width:200px;border:none;"><input type="text" name="vin" id="vin" /></td>
		      	<td align="center" style="border:none;" align="9"><input name="buttonQy" type="button" class="normal_btn" onclick="queryVin();" value="查找" /></td>
		      </tr>
		      <tr>
		      		<td align="center" width="3%">序号</td>
		      		<td align="center" width="3%"><input type="checkbox" name="chkAll" id="chkAll" onclick="checkAll();" /></td>
		      		<td align="center" width="10%">VIN</td>
		      		<td align="center" width="10%">交接单号</td>
		      		<td align="center" width="5%">车系</td>
		      		<td align="center" width="10%">车型</td>
		      		<td align="center" width="5%">配置</td>
		      		<td align="center" width="5%">颜色</td>
		      		<td align="center" width="5%">是否验收</td>
		      		<td align="center" width="10%">验收人</td>
		      		<td align="center" width="10%">验收时间</td>
		      </tr>
		      <c:forEach var="vh" items="${vehicles }" varStatus="i">
		      	 <tr id="tr${i.index }">
		      	 	<input type="hidden" name="vinHidden" value="${vh.VIN }" />
		      		<td align="center">${i.index+1 }</td>
		      		<td align="center"><input type="checkbox" name="chk" value="${vh.VEHICLE_ID }" /></td>
		      		<td align="center"><span id="vin${i.index }">${vh.VIN }</span></td>
		      		<td align="center">${vh.PASS_NO }</td>
		      		<td align="center">${vh.SERIES_NAME }</td>
		      		<td align="center">${vh.MODEL_NAME }</td>
		      		<td align="center">${vh.PACKAGE_NAME }</td>
		      		<td align="center">${vh.COLOR_NAME }</td>
		      		<td align="center">${vh.IS_ACC_STR }</td>
		      		<td align="center">${vh.ACC_BY }</td>
		      		<td align="center">${vh.ACC_DATE }</td>
		      	</tr>
		      </c:forEach>
  			</table>
  			<table class="table_query" width="90%" border="0" align="center">
				<tr>
					<td align="center" >
						<input id="queren" name="button" type="button" class="normal_btn" onclick="checkSubmit();" value="确认" />
						<input id="fanhui" name="button" type="button" class="normal_btn" onclick="window.history.back()" value="返回" />
					</td>
				</tr>
			</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
</body>
</html>