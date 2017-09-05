<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@ page import="java.util.List"%>


<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
	<head>
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<title>批发申请</title>
		<script type="text/javascript">
			function clearTxt(value__A) {
				document.getElementById(value__A).value = "" ;
			}
			function checkFirst(){
				document.getElementById("button1").disabled=false;
				document.getElementById("secondeDel__").disabled=true;
				document.getElementById("dealerCode").disabled=false;
				clearTxt('dealerId') ;
			}
			function checkSeconde(){
				var sDealerId;
				clearTxt('dealerCode') ;
				document.getElementById("secondeDel__").disabled=false;
				document.getElementById("dealerCode").disabled=true;
				document.getElementById("button1").disabled=true;
				sDealerId = document.getElementById('secondeDel__').value ;
				document.getElementById('dealerId').value = sDealerId ;
			}
			function chgDel() {
				var sDealerId ;
				sDealerId = document.getElementById('secondeDel__').value ;
				document.getElementById('dealerId').value = sDealerId ;
			}
			var HIDDEN_ARRAY_IDS=['form1'];
			var myPage;
			function myCheckBox(value,metaDate,record){
				return String.format("<input type='checkbox' name='vehicleIds' value='" + value + "' />");
			}
			//申请批发
			function dispatch(){
				var dealerCode = document.getElementById("dealerId").value;
				var transferreason = document.getElementById("transfer_reason").value;
				if(!dealerCode){
					MyAlert("请选择批发经销商");
					return;
				}
				if(transferreason.length<1){
					MyAlert("请填写批发原因！");
					return;
				}
				var sDlr = '${dlrId}' ;
				if(sDlr) {
					var aDlr = sDlr.split(",") ;
					var iLen = aDlr.length ;
					for(var i=0; i<iLen; i++) {
						if (dealerCode == aDlr[i]) {
							clearTxt('dealerCode') ;
							clearTxt('dealerId') ;
							MyAlert("不能批发给当前经销商！");
							return;
						}
					}
				}
				var vehicleIds = document.getElementsByName("vehicleIds");
				if(vehicleIds.length > 0){
					MyConfirm("是否提交?",dispatchAction);
				}else{
					MyAlert("请选择车辆信息!");
				}
			}
			//执行批发申请地址
			function dispatchAction(){
				document.getElementById("button2").disabled = true;
				var vehicleIds_ = document.getElementsByName("vehicleIds");
				var vehicleIds="";//批发车辆
				if(vehicleIds_.length){
					for(var i=0; i<vehicleIds_.length; i++){
						vehicleIds=vehicleIds+vehicleIds_[i].value+",";
					}
				}
				var dealerCode = document.getElementById("dealerId").value;//批发经销商
				var transfer_reason = document.getElementById("transfer_reason").value;//批发原因
				makeNomalFormCall('<%=contextPath%>/sales/storageManage/VehicleDispatch/vehicleDispatchSubmit.json?vehicleIds='+vehicleIds+'&dealerId='+dealerCode,showResult,'form1');
			}
			function showResult(json){
				if(json.returnValue == '1'){
					 window.location.href = '<%=contextPath%>/sales/storageManage/VehicleDispatch/VehicleDispatchInit.do';
				}else if(json.returnValue == '2'){
					var str = json.str;
					MyAlert("批发车辆"+str+"不符合调入方经销商的业务范围,不能批发！");
				}else if(json.returnValue == '3'){
					var myVinStr = json.myVinStr;
					MyAlert(myVinStr);
				} else if(json.returnValue == '4') {
					var str = json.str;
					MyAlert("批发车辆"+str+"不满足批发条件,不能批发！");
				}else{
					MyAlert("申请失败！请联系系统管理员！");
				}
				document.getElementById("dealerId").value = '' ;
				document.getElementById("dealerCode").value = "";
				document.getElementById("secondeDel__").value="";
				document.getElementById("transfer_reason").value = "";
				document.getElementById("button2").disabled = false ;
			}
			function turnQuery() {
			}
			function rowDelete(value) {
			 	var oTr = document.getElementById("tr" + value) ;
			 	oTr.parentNode.removeChild(oTr) ;
			}
			function chooseVehicleDispatchInit() {
				OpenHtmlWindow('<%=contextPath%>/sales/storageManage/VehicleDispatch/chooseVehicleDispatchInit.do',850,500);
			}
			function resetInput(){
				document.getElementById("dealerId").value = "";
				document.getElementById("dealerCode").value = "";
			}
	</script>
	</head>
	<body onunload='javascript:destoryPrototype();' onload='checkFirst();'>
		<div class="wbox">
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />
				&nbsp;当前位置： 整车销售 &gt; 库存管理 &gt; 批发申请
			</div>
			<div class="form-panel">
			<h2>批发申请</h2>
			<div class="form-body">
			<form id="fm" name="fm" method="post">
				<input type="hidden" name="curPage" id="curPage" value="1" />
				<input type="hidden" id="dlrId" name="dlrId" value="" />
				<table class="table_query" border="0">
<!--				<tr><td  colspan="3"><font color="red">1、不同大区之间经销商批发，需要车厂审核</font></td></tr>-->
					<tr><td  colspan="3"><font color="red">1、不同大区经销商批发，需要车厂审核</font></td></tr>
					<tr><td  colspan="3"><font color="red">2、同一大区经销商批发，需要大区审核</font></td></tr>
					<tr><td  colspan="3"><font color="red">3、一、二级经销商之间批发，不需要审核</font></td></tr>
					<tr>
						<td class="table_query_3Col_input">
							<input type="button" class="u-button" onclick="chooseVehicleDispatchInit(1);" value="  添  加  "   id="queryBtn_add" />
						</td>
					</tr>
				</table>
				<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
				<div id="myGrid">
					<table style="border-bottom: 1px solid #DAE0EE" class="table_list" id="myTable">
						<tbody>
							<tr class="table_list_th">
								<th class="noSort" style="text-class: center;">
								</th>
								<th class="noSort">
									VIN
								</th>
								<!-- <th class="noSort">
									车辆业务范围
								</th> -->
								<th class="noSort">
									发动机号
								</th>
								<th class="noSort">
									物料代码
								</th>
								<th class="noSort">
									物料名称
								</th>
								<th class="noSort">
									验收日期
								</th>
								<th class="noSort">
									库存天数
								</th>
								<th class="noSort">
									操作
								</th>
							</tr>
						</tbody>
					</table>
				</div>
			</form>
		<form name="form1" id="form1">
			<input  type="hidden" name="dealerLevel" id="dealerLevel" value="${dealerLevel}"/>
			<table class="table_query" width="85%" class="center" border="0" id="roll">
				<c:if test="${dealerLevel==10851001}">
				<tr>
					<td class="right">
						<input type="radio" id="rd1" name=chngType value="1" checked="checked" onclick="checkFirst();" />
						调入经销商：
					</td>
					<td>
						<input id="dealerCode" name="dealerCode" id="dealerCode" type="text" class="middle_txt" size="15" readonly="readonly" onclick="showOrgDealer('dealerCode', 'dealerId', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', 'dealerName');"/>
						<input id="dealerName" name="dealerName" id="dealerName" type="hidden" class="middle_txt" size="15" readonly="readonly" />
						<input type="button"  class="u-button"  onclick="resetInput()" value="清空" id="button1" />
					</td>
					<td class="right">
						<input type="radio" id="rd2" name=chngType value="2" onclick="checkSeconde();" />
						下级经销商：
					</td>
					<td class="left">
						<select id="secondeDel__" name="secodeDel" onchange="chgDel();" class="u-select">
							<c:forEach items="${secondeDels }" var="secondeDels_A">
								<option value="${secondeDels_A.DEALER_ID }">
									${secondeDels_A.DEALER_NAME }-${secondeDels_A.DEALER_CODE }
								</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				</c:if>
				<c:if test="${dealerLevel==10851002}">
					<td class="right">
						调入经销商：
					</td>
					<td>
					<select id="dealerId" name="dealerId">
					<c:forEach var="listInfos" items="${listInfo}">
			  			<option value="${listInfos.DEALER_ID}">${listInfos.DEALER_SHORTNAME}</option> 
			 			</c:forEach>
					</select>
					</td>
				</c:if>
				<tr>
					<td  class="right">
						批发原因：
					</td>
					<td >
						<textarea id="transfer_reason" name="transfer_reason" class="form-control" style="width:150px;"></textarea>
					</td>
					<td  class="right">
						<c:if test="${dealerLevel==10851001}">
							<input type="hidden" id="dealerId" name="dealerId" />
						</c:if>
						<input name="button2" id="button2"  type="button" class="u-button u-submit" onclick="dispatch();" value="申请批发" />
					</td>
				</tr>
			</table>
		</form>
		</div>
		</div>
		</div>
	</body>
</html>