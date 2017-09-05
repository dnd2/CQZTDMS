<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息变更申请</title>
<% String contextPath = request.getContextPath(); 
List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
	%>
<script type="text/javascript">
	doInit();
	function doInit(){
		var tab = $('partTableId');
   		var partCode=$('myPartCode').value;
		var partName = $('myPartName').value;
		var claimMelieage = $('myClaimMelieage').value;
		var claimMonth = $('myClaimMonth').value;
		var ruleListId = $('myRuleListId').value;
		//初始化限制单个配件添加
		//if(tab.rows.length==3){
			//$('button422').disabled='disabled';
		//}
		setMainPartCode(partCode,partName,claimMonth,claimMelieage,ruleListId,'1');
	}
	
	//点新增时配件工时下拉框
	function genAppTimeCombo1(obj) {MyAlert('aa1');
		var codes=document.getElementsByName("WR_LABOURCODE");//取得主工时CODE数组
		//var names=document.getElementsByName("WR_LABOURNAME");//取得主工时NAME数组
		if (codes!=null&&codes!="")
		var innerHTML = '<select  name="appTime" class="min_sel">';
		//var innerHTML= '';
		innerHTML += '<option value="">索赔工时列表</option>';
		//InsertSelect(obj,'','索赔工时列表');
		//for (var i=0;i<codes.length;i++) {
			//innerHTML += '<option value="'+codes[i].value+'">'+names[i].value+'</option>';
			//InsertSelect(obj,codes[i].value,names[i].value);
		//}
		innerHTML += '</select><input type="hidden" name="PART" value="on"/>';
		//MyAlert(obj.innerHTML);
		//MyAlert(innerHTML);
		obj.cells[10].innerHTML=innerHTML;
	}
	 // 动态生成表格
 	function addRow(tableId){
 	
 	
	    var addTable = document.getElementById(tableId);
		var rows = addTable.rows;
		var length = rows.length;
		var insertRow = addTable.insertRow(length);

		insertRow.className = "table_list_row1";
		insertRow.insertCell(0);
		insertRow.insertCell(1);
		insertRow.insertCell(2);
		insertRow.insertCell(3);
		insertRow.insertCell(4);
		insertRow.insertCell(5);
		insertRow.insertCell(6);
			addTable.rows[length].cells[0].innerHTML =  '<td><input type="text" class="phone_txt" name="PART_CODE0" datatype="0,is_null"  value="" size="10" id="PART_CODE0" readonly/><span class="tbwhite"><a href="#" onClick="javascript:selectMainPartCode(this)">选择</a></span></td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><span class="tbwhite"><input type="text" class="phone_txt" name="PART_NAME0" readonly  id="PART_NAME" name="PART_SN3"  size="10"/></span></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><input type="text"  name="claimMonth" id="claimMonth" /></td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" name="claimMelieage" id="claimMelieage"/></td>';
			//addTable.rows[length].cells[6].innerHTML =  '<td><input type="text" class="short_txt" name="REMARK" id="REMARK" size="10" maxlength="13" /></td>';
			//addTable.rows[length].cells[7].innerHTML = genSelBoxExpPay("PAY_TYPE_PART",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","true",'');
			//addTable.rows[length].cells[5].innerHTML =  '<td><input type="text" class="short_txt" name="REMARK" id="REMARK" size="10" maxlength="13" /></td>';
			//addTable.rows[length].cells[8].innerHTML = '<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'partTable\',this,\'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			//addTable.rows[length].cells[6].innerHTML = genSelBoxExpPay("PAY_TYPE_PART",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","true",'');
			addTable.rows[length].cells[4].innerHTML = '<td><input type="text" name="claimMonthNew" id="claimMonthNew"/></td>';
			//addTable.rows[length].cells[5].innerHTML =  '<td><input type="text" class="short_txt" name="REMARK" id="REMARK" size="10" maxlength="13" /></td>';
			//addTable.rows[length].cells[7].innerHTML = '<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'partTable\',this,\'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			addTable.rows[length].cells[5].innerHTML = '<td><input type="text"  name="claimMelieageNew" id="claimMelieageNew"/></td>';
			addTable.rows[length].cells[6].innerHTML = '<td><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			
			//限制只能加一条配件
			if(length==0){
				$('button422').disabled='disabled';
				return;
			}
			
			return addTable.rows[length];
		}
		//选择主上件
		function selectMainPartCode(obj){
		
			myobj = getRowObj(obj); 
			var vin = document.getElementById("VIN").value; 
			var modelId = document.getElementById("modelCode").value; 
			if (vin!=null&&vin!=''&&vin!='null') {
			OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectRulePartCodeForward.do?GROUP_ID='+modelId+'&vin='+vin,800,500);
			}else {
				MyAlert('请先输入车辆VIN后，再添加配件！');
			}
		}
		//得到行对象
		function getRowObj(obj)
		{
		   var i = 0;
		   while(obj.tagName.toLowerCase() != "tr"){
		    obj = obj.parentNode;
		    if(obj.tagName.toLowerCase() == "table")
		  return null;
		   }
		   return obj;
		}
		//主配件选择

	function setMainPartCode(partCode,partName,
	claimMonth,claimMelieage,ruleListId,flag) {
		if(flag!=''){
			var mySelfObj = $('partTableId').childNodes[0].childNodes[2];
				/*if($('PART_CODE')!=null){
					var partCodes = $$('.PART_CODE').pluck('value');
					if(partCodes.indexOf(partCode)!=-1){
							MyAlert("该配件已经存在，不可添加！");
							return false;
					}
				}
				*/
				//判断是否添加了重复的主工时
		    //myobj.cells.item(3).childNodes[0].value='';
		    var claimMonthNewValue = $('myClaimMonthNew').value;
		    var claimMelieageNewValue = $('myClaimMelieageNew').value;
		    
		    mySelfObj.cells.item(2).innerHTML='<input type="text" class="short_txt" name="CLAIM_MONTH" value="'+claimMonth+'" size="10" maxlength="11" id="CLAIM_MONTH" readonly/>';
			mySelfObj.cells.item(0).innerHTML='<input type="text" class="short_txt PART_CODE" name="PART_CODE"   value="'+partCode+'" size="10" id="PART_CODE" readonly="true"/><span class="tbwhite"><a href="#" onClick="javascript:selectMainPartCode(this)">选择</a></span>';
			
			//myobj.cells.item(2).innerHTML='<td><input type="text" name="DOWN_PART_CODE" value="'+partCode+'" size="10" readonly="true"/><span class="tbwhite"><input type="hidden" name="DOWN_PART_NAME"  value="'+partName+'" size="10" maxlength="13" readonly/><input type="hidden" name="s"  value="1" size="10" maxlength="13" readonly/><a href="#" onClick="javascript:selectDownPartCode(this)">选择</a></span></td>';
			mySelfObj.cells.item(1).innerHTML='<span class="tbwhite"><input type="text" class="short_txt" name="PART_NAME" readonly value="'+partName+'" id="PART_NAME"  size="10"/></span>';
			mySelfObj.cells.item(3).innerHTML='<input type="text" class="short_txt" name="CLAIM_MELIEAGE" value="'+claimMelieage+'" size="10" maxlength="11" id="CLAIM_MELIEAGE" readonly/><input type="hidden"  name="ruleListId" id="ruleListId" value="'+ruleListId+'"/>';
			mySelfObj.cells.item(4).innerHTML='<input type="text" class="short_txt" name="claimMonthNew" value="'+claimMonthNewValue+'" size="10" maxlength="11" id="claimMonthNew" />';
			mySelfObj.cells.item(5).innerHTML='<input type="text" class="short_txt" name="claimMelieageNew" value="'+claimMelieageNewValue+'" size="10" maxlength="11" id="claimMelieageNew" />';
			//myobj.cells.item(7).innerHTML = genSelBoxExpPay("PAY_TYPE_PART",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","true",'');
			//myobj.cells.item(8).innerHTML='<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'partTable\',this,\''+partCode+'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			//myobj.cells.item(7).innerHTML='<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'partTable\',this,\''+partCode+'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			//getGuaFlag();
		}else{
			var table = myobj.parentNode;
			var length= table.childNodes.length;
			var flag=0;
			/*if($('PART_CODE')!=null){
				var partCodes = $$('.PART_CODE').pluck('value');
				if(partCodes.indexOf(partCode)!=-1){
						MyAlert("该配件已经存在，不可添加！");
						return false;
				}
			}
			*/
			//判断是否添加了重复的主工时
			
			if (flag==0) {
			cloMainPart=1;
				
		    //myobj.cells.item(3).childNodes[0].value='';
		    myobj.cells.item(2).innerHTML='<input type="text" class="short_txt" name="CLAIM_MONTH" value="'+claimMonth+'" size="10" maxlength="11" id="CLAIM_MONTH" readonly/>';
			myobj.cells.item(0).innerHTML='<input type="text" class="short_txt" name="PART_CODE"   value="'+partCode+'" size="10" id="PART_CODE" readonly="true"/><span class="tbwhite"><a href="#" onClick="javascript:selectMainPartCode(this)">选择</a></span>';
			//myobj.cells.item(2).innerHTML='<td><input type="text" name="DOWN_PART_CODE" value="'+partCode+'" size="10" readonly="true"/><span class="tbwhite"><input type="hidden" name="DOWN_PART_NAME"  value="'+partName+'" size="10" maxlength="13" readonly/><input type="hidden" name="s"  value="1" size="10" maxlength="13" readonly/><a href="#" onClick="javascript:selectDownPartCode(this)">选择</a></span></td>';
			myobj.cells.item(1).innerHTML='<span class="tbwhite"><input type="text" class="short_txt" name="PART_NAME" readonly value="'+partName+'" id="PART_NAME"  size="10"/></span>';
			myobj.cells.item(3).innerHTML='<input type="text" class="short_txt" name="CLAIM_MELIEAGE" value="'+claimMelieage+'" size="10" maxlength="11" id="CLAIM_MELIEAGE" readonly/><input type="hidden"  name="ruleListId" id="ruleListId" value="'+ruleListId+'"/>';
			//myobj.cells.item(7).innerHTML = genSelBoxExpPay("PAY_TYPE_PART",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","true",'');
			//myobj.cells.item(8).innerHTML='<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'partTable\',this,\''+partCode+'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			//myobj.cells.item(7).innerHTML='<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'partTable\',this,\''+partCode+'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			//getGuaFlag();
			parent._hide();
		}
		}
	}
	//删除行 配件
		function delPartItem(obj,name){
		     var tr = this.getRowObj(obj);
		    //MyAlert("length="+tr.childNodes[10].childNodes.length);
		    if(tr.childNodes[5].childNodes.length==3) {
		    	MyConfirm("是否删除？",delPartItems,[tr]);
		    }else{
		   if(tr != null){
		    tr.parentNode.removeChild(tr);
		   }else{
		    throw new Error("the given object is not contained by the table");
		   }
		   }
		   //限制只能加一条配件
			if($('partTableId').rows.length==2){
				$('button422').disabled='';
				return;
			}
		}
</script>
<body onload="queryVehicle();">
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;车辆信息变更申请
</div>
<form name="fm" id="fm">
	<input type=hidden name="changeId" id="changeId" value="${part.CHANGE_ID }"/>
	<input type=hidden name="myRuleListId" id="myRuleListId" value="${part.RULE_LIST_ID }"/>
	<input type=hidden name="myPartCode" id="myPartCode" value="${part.PART_CODE }"/>
	<input type=hidden name="myPartName" id="myPartName" value="${part.PART_NAME }"/>
	<input type=hidden name="myClaimMonth" id="myClaimMonth" value="${part.WR_MONTH }"/>
	<input type=hidden name="myClaimMonthNew" id="myClaimMonthNew" value="${part.CLAIM_MONTH_NEW }"/>
	<input type=hidden name="myClaimMelieage" id="myClaimMelieage" value="${part.WR_MELIEAGE }"/>
	<input type=hidden name="myClaimMelieageNew" id="myClaimMelieageNew" value="${part.CLAIM_MELIEAGE_NEW }"/>
	<input type=hidden name="beforeVin" id="beforeVin" value="${vin }"/>
	<table class="table_edit" id="vehicleInfo">
		<th colspan="8"><img class="nav" src="../../../img/subNav.gif"/> 车辆信息</th>
		<tr>
			<td width="10%" align="right">VIN：</td>
			<td width="22%"><input name="vin" id="vin" type="text" class="middle_txt" onblur="queryVehicle();" value="${vin }"/><font color="red">*</font></td>
			<td width="10%" align="right">制单人：</td>
			<td width="22%" align="left">${part.APPLY_PERSON }</td>
			<td width="10%" align="right">制单公司：</td>
			<td width="22%" align="left">${part.COM_NAME }</td>
			<td width="50%" align="right" nowrap="nowrap">制单时间：</td>
			<td width="50%" nowrap="nowrap"><fmt:formatDate  value="${part.APPLY_DATE }" pattern="yyyy-MM-dd" /></td>
		</tr>
		<tr style="display:none;">
			<td align="right">产地：<input type="hidden" id="yieldly" name="yieldly"/></td>
			<td></td>
			<td width="10%" align="right">车系：<input type="hidden" id="seriesCode" name="seriesId"/></td>
			<td width="22%" align="left"></td>
			<td width="12%" align="right">车型：<input type="hidden" id="modelCode" name="modelId"/></td>
			<td></td>
		</tr>
		<tr style="display:none;">
			<td align="right">购车日期：<input type="hidden" id="purchasedDate" name="purchasedDate"/></td>
			<td align="left"></td>
			<td align="right">行驶里程：<input type="hidden" id="mileage" name="mileage"/></td>
			<td></td>
			<td align="right">保养次数：<input type="hidden" id="freeTimes" name="freeTimes"/></td>
			<td></td>
		</tr>
		<tr style="display:none;">
			<td width="10%" align="right">车主姓名：<input type="hidden" id="ctmId" name="ctmId"/><!-- 车主id --></td>
			<td></td>
			<td width="10%" align="right">车主电话：</td>
			<td width="22%" align="left"></td>
			<td width="12%" align="right">三包策略代码：<input type="hidden" id="gameId" name="gameId"/><!-- 策略id --></td>
			<td></td>
		</tr>
		<tr style="display:none;">
			<td width="12%" align="right" nowrap="nowrap">三包规则代码：<input type="hidden" id="gameId" name="gameId"/><!-- 三包规则代码 --></td>
			<td></td>
			<td width="10%" align="right">车主地址：<!-- 车主地址 --></td>
			<td></td>
			<td>发动机号：<input type="hidden" id="engineNo" name="engineNo"/></td>
			<td><input name="engineNo1" id="engineNo1" type="text" class="middle_txt""/><font color="red">*</font></td>
		</tr>
		<tr style="display:none;">
			<td>牌照号：<input type="hidden" id="vehicleNo" name="vehicleNo"/></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
	</table>
				<table id="partTableId" name='partTableId' border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list">

				<th colspan="12" align="left">
					<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
					维修配件
				</th>
				<tr align="center" class="table_list_row1">
					<td>
						新件代码
					</td>
					<td>
						新件名称
					</td>
					<td style="display: none">
						车辆三包期(月)
					</td>
					<td style="display: none">
						车辆三包里程
					</td>
					<td>
						更改三包期
					</td>
					<td>
						更改里程
					</td>
					<td style="display:none;">
						<input id="partBtn" type="button" class="normal_btn" value="新增"
							name="button422" onClick="javascript:addRow('partTable');" />
					</td>
				</tr>
				<tr align="center" class="table_list_row1">
					<td><input type="text" class="short_txt" name="PART_CODE0"  value="" size="10" id="PART_CODE0" readonly/><span class="tbwhite"><a href="#" onClick="javascript:selectMainPartCode(this)">选择</a></span></td>
					<td><span class="tbwhite"><input type="text" class="short_txt" name="PART_NAME0" readonly  id="PART_NAME" name="PART_SN3"  size="10"/></span></td>
					<td style="display: none"><input type="text" class="short_txt" name="claimMonth" id="claimMonth" /></td>
					<td style="display: none"><input type="text" class="short_txt" name="claimMelieage" id="claimMelieage"/></td>
					<td><input type="text" class="short_txt" name="claimMonthNew" id="claimMonthNew"/></td>
					<td><input type="text" class="short_txt" name="claimMelieageNew" id="claimMelieageNew"/></td>
					<td style="display:none;">
						<input id="partBtn" type="button" class="normal_btn" value="新增"
							name="button422" onClick="javascript:addRow('partTable');" />
					</td>
				</tr>
			</table>
			<table class="table_edit">
				<tr>
			<td nowrap="nowrap">
				申请备注：
			</td>
			<td colspan="7">
				<textarea rows="4" cols="130" id="applyRemark" name='applyRemark'>${part.APPLY_REMARK }</textarea>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap">
				审核备注：
			</td>
			<td colspan="7">
				<textarea rows="4" cols="130" id="checkRemark" name='checkRemark' readonly="readonly">${part.CHECK_REMARK }</textarea>
			</td>
		</tr>
			</table>
		</br>
		<table width=100% border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="12" align=left width="33%">&nbsp;</td>
             	<td height="12" align=center width="33%">
                	<input type="button" onClick="doCommit(1);" class="normal_btn"  style="width=8%" value="保存"/>
                	&nbsp;
                	<input type="button" onclick="doCommit(2);" class="normal_btn"  style="width=8%" value="上报"/>
                	&nbsp;
					<input type="button" onClick="javascript:history.go(-1);" class="normal_btn"  style="width=8%" value="返回"/>
    			</td>
            	<td height="12" align=center width="33%">
      			</td>
			</tr>
		</table>
	</form>
</body>
</html>
<script type="text/javascript" >


function queryVehicle() {
	var vin = document.getElementById("vin").value;
	var pattern=/^([A-Z]|[0-9]){17,17}$/;
	if(pattern.exec(vin)) {
		var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/queryVehicle.json?vin='+vin;
		makeNomalFormCall(url,showVehicle,'fm');
	} else {
		MyAlert("不是有效的VIN格式");
	}
}
function showVehicle(json) { 
	if (!json.vehicle) {
		MyAlert("没有找到对应的VIN");
		return;
	}
	//如果VIN发生修改
	if($('beforeVin').value!=$('vin').value){
		//取得flag判断车辆是否保养
		var flag = json.flag;
		if(flag=='NO'){
			MyAlert('该车未按规定保养，不需要申报变更！');
			$('vin').value=$('beforeVin').value;
			return;
		}else{
			//清除配件信息
			$('PART_CODE').value='';
			$('PART_NAME').value='';
			$('CLAIM_MONTH').value='';
			$('CLAIM_MELIEAGE').value='';
			$('claimMonthNew').value='';
			$('claimMelieageNew').value='';
		}

	}
	var vehicleInfo = document.getElementById("vehicleInfo");
	var vehicle = json.vehicle;
	var ruleListPs = json.ruleListPs;
	vehicleInfo.rows[5].cells[5].innerText = checkNull(vehicle.ENGINE_NO);//发动机号
	document.getElementById("engineNo").value = checkNull(vehicle.ENGINE_NO);
	vehicleInfo.rows[6].cells[2].innerText = checkNull(vehicle.VEHICLE_NO);//牌照号
	document.getElementById("vehicleNo").value = checkNull(vehicle.VEHICLE_NO);
	vehicleInfo.rows[2].cells[1].innerText = checkNull(vehicle.YIELDLY);//产地
	document.getElementById("yieldly").value = checkNull(vehicle.Y_ID);
	vehicleInfo.rows[2].cells[3].innerText = checkNull(vehicle.SERIES_CODE);//车系
	document.getElementById("seriesId").value = checkNull(vehicle.SERIES_ID);
	vehicleInfo.rows[2].cells[5].innerText = checkNull(vehicle.MODEL_CODE);//车型
	document.getElementById("modelId").value = checkNull(vehicle.MODEL_ID);
	vehicleInfo.rows[3].cells[1].innerText = checkNull(vehicle.PURCHASED_DATE);//购车日期
	document.getElementById("purchasedDate").value = checkNull(vehicle.PURCHASED_DATE);
	vehicleInfo.rows[3].cells[3].innerText = checkNull(vehicle.MILEAGE);//行驶里程
	document.getElementById("MILEAGE").value = checkNull(vehicle.MILEAGE);
	vehicleInfo.rows[3].cells[5].innerText = checkNull(vehicle.FREE_TIMES);//保养次数
	document.getElementById("freeTimes").value = checkNull(vehicle.FREE_TIMES);
	vehicleInfo.rows[4].cells[1].innerText = checkNull(vehicle.CTM_NAME);//车主姓名
	document.getElementById("ctmId").value = checkNull(vehicle.CTM_ID);//车主ID
	vehicleInfo.rows[4].cells[3].innerText = checkNull(vehicle.MAIN_PHONE);//车主电话
	//document.getElementById("mainPhone").value = checkNull(vehicle.MAIN_PHONE);
	vehicleInfo.rows[4].cells[5].innerText = checkNull(vehicle.GAME_CODE);//三包策略代码
	document.getElementById("gameId").value = checkNull(vehicle.GAME_ID);//三包策略ID
	vehicleInfo.rows[5].cells[1].innerText = checkNull(vehicle.RULE_CODE);//三包规则代码
	vehicleInfo.rows[5].cells[3].innerText = checkNull(vehicle.ADDRESS);//车主地址
	
	//根据车辆的保养次数算出保养次数的上下限
	var freeTimes = checkNull(vehicle.FREE_TIMES);  //车辆的保养次数
	var minFreeTimes = freeTimes - 1 < 0 ? 0 : freeTimes - 1;//最小保养次数
	var maxFreeTimes = parseInt(freeTimes) + 1;//最大保养次数
	var cFreeTimes = minFreeTimes + '-' + maxFreeTimes;
	document.getElementById("cFreeTimes").innerText = cFreeTimes;//可变保养次数 minFreeTimes到maxFreeTimes
	document.getElementById("tcFreeTimes").value = cFreeTimes;
	
	
}

function checkNull(value) {
	if (value == null || value == 'null') {
		return '';
	} else {
		return value;
	}
}

function doCusChange(){
	var selectType = document.getElementById("changeType").value;	
	if (selectType == '') {
		document.getElementById("mileage_body").style.display = "none";//行驶里程
		document.getElementById("freetimes_body").style.display = "none";//保养次数
		document.getElementById("remark_body").style.display = "none";
		document.getElementById("purchase_date_chg").style.display = "none";
		document.getElementById("ctm_info_chg").style.display = "none";
		clearApplyData();
	}
	if (selectType == 13141001) {//行驶里程变更
		document.getElementById("mileage_body").style.display = "inline";
		document.getElementById("remark_body").style.display = "inline";
		document.getElementById("freetimes_body").style.display = "none";
		document.getElementById("purchase_date_chg").style.display = "none";
		document.getElementById("ctm_info_chg").style.display = "none";
		$('add_file').style.display = 'none' ;
		$('check_2').style.display = 'block' ;
		clearApplyData();
	}
	else if (selectType == 13141002) {//保养次数变更
		document.getElementById("freetimes_body").style.display = "inline";
		document.getElementById("remark_body").style.display = "inline";
		document.getElementById("mileage_body").style.display = "none";
		document.getElementById("purchase_date_chg").style.display = "none";
		document.getElementById("ctm_info_chg").style.display = "none";
		$('add_file').style.display = 'none' ;
		$('check_2').style.display = 'block' ;
		clearApplyData();
	}
	//购车时间变更
	else if(selectType == 13141003) {
		document.getElementById("mileage_body").style.display = "none";
		document.getElementById("freetimes_body").style.display = "none";
		document.getElementById("remark_body").style.display = "inline";
		document.getElementById("purchase_date_chg").style.display = "inline";
		//document.getElementById("tdwrGames").style.display = "none";
		document.getElementById("ctm_info_chg").style.display = "none";
		$('add_file').style.display = 'block' ;
		$('check_2').style.display = 'none' ;
		$('span1').innerHTML = '(必须上传发票照片 ) ' ;
		clearApplyData();
	}
	//三包策略变更
	else if(selectType == 13141004) {
		document.getElementById("mileage_body").style.display = "none";
		document.getElementById("freetimes_body").style.display = "none";
		document.getElementById("purchase_date_chg").style.display = "none";
		document.getElementById("remark_body").style.display = "inline";
		//document.getElementById("tdwrGames").style.display = "inline";
		document.getElementById("ctm_info_chg").style.display = "none";
		$('add_file').style.display = 'none' ;
		$('check_2').style.display = 'block' ;
		clearApplyData();
	}
	//车主信息变更
	else if(selectType == 13141005) {
		document.getElementById("mileage_body").style.display = "none";
		document.getElementById("freetimes_body").style.display = "none";
		document.getElementById("purchase_date_chg").style.display = "none";
		document.getElementById("remark_body").style.display = "inline";
		//document.getElementById("tdwrGames").style.display = "none";
		document.getElementById("ctm_info_chg").style.display = "block";
		$('add_file').style.display = 'block' ;
		$('check_2').style.display = 'none' ;
		$('span1').innerHTML = '(必须上传行车执照或发票照片 ) ' ;
		clearApplyData();
	}
}
//onchange下拉框的时候 清空原来的选择
function clearApplyData() {
	document.getElementById("cmileage").value = "";//变更后数据 变更里程
	document.getElementById("cfree_times").value="";//变更后数据 保养次数
	document.getElementById("errorRoCode").value="";//变更里程选择的单据
	document.getElementById("ferrorRoCode").value="";//变更保养次数的单据
	document.getElementById("roDealerCode").innerText = '';//变更里程的经销商代码
	document.getElementById("troDealerCode").value = "";
	document.getElementById("froDealerCode").innerText = '';//变更保养次数经销商代码
	document.getElementById("ftroDealerCode").value = "";
	document.getElementById("roInMileage").innerText = '';//单据里程
	document.getElementById("troMileage").value = "";
	document.getElementById("roFreeTimes").innerText = '';//保养次数
	document.getElementById("troFreeTimes").value = "";
	document.getElementById("cInMileage").innerText = '';//可变最小里程
	document.getElementById("tcMileage").value = "";
	//这里不清空可变保养次数,可变保养次数是在选择VIN的时候带出来的
	//document.getElementById("cFreeTimes").innerText = '';//可变保养次数
	//document.getElementById("tcFreeTimes").value = "";
	document.getElementById("remark").value = '';//备注
	$('c_purchase_date').value = '' ;
	$('c_ctm_name').value = '' ;
	$('c_ctm_phone').value = '' ;
	$('c_ctm_address').value = '' ;
}

//保存
function doCommit(flag) {
	/*if (!validate()) {
		MyAlert("带*为必填项");
		return;
	}
	if (!compareEngineNo()) {
		return;
	}
	if (!validateValue()) {
		return;
	}
	var cmileage = document.getElementById("cmileage").value;
	var pattern = /^(\d){0,100}$/ ;
	if(!pattern.exec(cmileage)){
		MyAlert('里程只能输入正整数！');
		return ;
	}*/
	if($('PART_CODE').value==''||$('PART_CODE').value==null){
		MyAlert('请选择要更改的配件代码和名称！');
		return;
	}
	if($('claimMonthNew').value==''||$('claimMonthNew').value==null){
		MyAlert('请输入想要修改三包期！');
		return;
	}
	if($('claimMelieageNew').value==''||$('claimMelieageNew').value==null){
		MyAlert('请输入想要修改的三包里程！');
		return;
	}
	if('1'==flag){
		MyConfirm('确认修改保存？',myCommit,[flag]);
	}else{
		MyConfirm('确认提报修改？',myCommit,[flag]);
	}
}
function myCommit(flag){
	var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/ruleChangeApplyModify.json?COMMAND=1&flag='+flag;
	//fm.action = url;
	//fm.method = "post";
	//fm.submit();
	makeNomalFormCall(url,handleDoSave,'fm');
}
function handleDoSave(json) {
	if(json.msg!=null && json.msg!=""){
	MyAlert("操作成功");
	var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/vehicleRuleInfoChangeApplyInit.do';
	fm.action = url;
	fm.method = "post";
	fm.submit();
	}else{
	MyAlert("操作失败");
	}
}
//上报
function doSubmit() {
	if (!validate()) {
		MyAlert("带*为必填项");
		return;
	}
	if (!compareEngineNo()) {
		return;
	} 
	if (!validateValue()) {
		return;
	}
	var cmileage = document.getElementById("cmileage").value;
	var pattern = /^(\d){0,100}$/ ;
	if(!pattern.exec(cmileage)){
		MyAlert('里程只能输入正整数！');
		return ;
	}
	var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/doSave.do?addFlag=1';
	fm.action = url;
	fm.method = "post";
	fm.submit();
	//makeNomalFormCall(url,handleDoSave,'fm');
}

//清空经销商
function clrDlr(dealerId, dealerCode) {
	document.getElementById(dealerId).value="";
	document.getElementById(dealerCode).value="";
}

function openItem() {
	var vin = document.getElementById("vin").value;
	var selectType = document.getElementById("changeType").value;
	var url = "<%=contextPath%>/jsp/vehicleInfoManage/apply/showOrder.jsp?vin="+vin+"&selectType="+selectType;
	OpenHtmlWindow(url,800,500);
}

//清空选择的错误单据
function clrRecord() {
	document.getElementById("cmileage").value = "";//变更后数据 变更里程
	document.getElementById("cfree_times").value="";//变更后数据 保养次数
	document.getElementById("errorRoCode").value="";//变更里程选择的单据
	document.getElementById("ferrorRoCode").value="";//变更保养次数的单据
	document.getElementById("roDealerCode").innerText = '';//变更里程的经销商代码
	document.getElementById("troDealerCode").value = "";
	document.getElementById("froDealerCode").innerText = '';//变更保养次数经销商代码
	document.getElementById("ftroDealerCode").value = "";
	document.getElementById("roInMileage").innerText = '';//单据里程
	document.getElementById("troMileage").value = "";
	document.getElementById("roFreeTimes").innerText = '';//保养次数
	document.getElementById("troFreeTimes").value = "";
	document.getElementById("cInMileage").innerText = '';//可变最小里程
	document.getElementById("tcMileage").value = "";
	//这里不清空可变保养次数,可变保养次数是在选择VIN的时候带出来的
	//document.getElementById("cFreeTimes").innerText = '';//可变保养次数
	//document.getElementById("tcFreeTimes").value = "";
}

function validate() {
	var vin = document.getElementById("vin").value;
	if (!vin) {
		return fasle;
	}
	var engineNo1 = document.getElementById("engineNo1").value;//手动输入发动机号
	engineNo1 = (engineNo1.toUpperCase()).replace(/\s+/g, "");
	if (!engineNo1) {
		return false;
	}
	var changeType = document.getElementById("changeType").value;
	if (changeType == 13141001) {
		var errorRoCode = document.getElementById('errorRoCode').value;//错误单据号
    	if(''== errorRoCode ||null == errorRoCode) {
        	MyAlert('错误单据号不能为空');
        	return false;
    	}
		var cmileage = document.getElementById("cmileage").value;
		if (!cmileage) {
			return false;
		}
	} else if (changeType == 13141002) {
		var cfreeTimes = document.getElementById("cfree_times").value;
		if (!cfreeTimes) {
			return false;
		}
	} else if (changeType == 13141003) {
		var cpurchasedDate = document.getElementById("c_purchase_date").value;
		if (!cpurchasedDate) {
			return false;
		}
		if(!$('uploadFileId')){
			return false;
		}
	} else if (changeType == 13141005) {
		var cCtmName = document.getElementById("c_ctm_name").value;
		var cCtmPhone = $('c_ctm_phone').value ;
		var cCtmAddress = $('c_ctm_address').value ;
		if (!cCtmName || !cCtmPhone || !cCtmAddress) {
			return false;
		}
		if(!$('uploadFileId')){
			return false;
		}
	}
	/*
	var errorDealerCode = document.getElementById("errorDealerCode").value;
	if (!errorDealerCode) {
		return false;
	}
	*/
	var remark = document.getElementById("remark").value;
	if (!remark.trim()) {
		return false;
	}
	var sure = document.getElementById("sure").checked;//确认checkBox
	if(changeType!=13141003 && changeType!=13141005){
		if (!sure) {
			return false;
		}
	}
	return true;
}
//比较VIN带出来的发动机号和手动输入的发动机号是否相同
function compareEngineNo() {
	var engineNo1 = document.getElementById("engineNo1").value;//手动输入发动机号
	engineNo1 = (engineNo1.toUpperCase()).replace(/\s+/g, "");//发动机号变成大写 去掉所有空格
	var engineNo = document.getElementById("engineNo").value;//输入VIN带出来的发动机号,隐藏域里面
	if (engineNo1 != engineNo) {
		MyAlert("VIN和发动机号不符");
		return false;
	}
	return true;
}
//根据变更类型判断输入的值是否合法
function validateValue() {
	var selectType = document.getElementById("changeType").value;
	//行驶里程变更	
	if (selectType == <%=Constant.VEHICLE_CHANGE_TYPE_01%>) {
	    var cmileage = document.getElementById("cmileage").value;
		var mileage = document.getElementById('mileage').value;
		var tcMileage = document.getElementById("tcMileage").value;
		if(isNaN(cmileage)){
			MyAlert('变更后数据必须输入数字');
		 	return false;
		}
		if(parseInt(cmileage) <= parseInt(tcMileage)){
			MyAlert('行驶里程必须大于最小可变里程');
		  	return false;
		}
	}
	//保养次数变更
	else if(selectType == <%=Constant.VEHICLE_CHANGE_TYPE_02%>) {
		var cfree_times = document.getElementById("cfree_times").value;	
		if (isNaN(cfree_times)) {
			MyAlert('变更后数据必须输入数字');
			return false;
		} 
		//var roFreeTimes = document.getElementById("roFreeTimes").innerText;
		var freeTimes = document.getElementById("freeTimes").value;
		if (freeTimes == 0) {//如果保养次数为0  修改保养次数为0或者1
			if (cfree_times != 0 && cfree_times != 1) {
				MyAlert('保养次数只能填0或1');
				return false;
			}
			return true;
		} else if (cfree_times < 0) {
			MyAlert('保养次数不能小于0');
			return false;
		} 
		var minF = parseInt(freeTimes) - 1;//最小保养次数
		var maxF = parseInt(freeTimes) + 1;//最大保养次数
		if ((cfree_times == minF) || (cfree_times == maxF)) {//修改保养次数为正负1
			return true;
		} else {
			MyAlert('保养次数只能填' + (parseInt(freeTimes) - 1) + '或' + (parseInt(freeTimes) + 1));
			return false;
		}
	}
	return true;
}

function showRo(roNo, dealerCode, inMileage, freeTimes,nextInMileage, dealerName) {
	document.getElementById("errorRoCode").value = roNo;//选定的工单编号
	document.getElementById("roDealerCode").innerText = dealerName;//经销商名称
	document.getElementById("troDealerCode").value = dealerCode;//经销商代码
	document.getElementById("roInMileage").innerText = inMileage;//单据里程
	document.getElementById("troMileage").value = inMileage;
	document.getElementById("tcMileage").value = nextInMileage;//可变更最小里程
	document.getElementById("cInMileage").innerText =  nextInMileage;
	
	document.getElementById("roFreeTimes").innerText = freeTimes;//保养次数
	document.getElementById("troFreeTimes").value = freeTimes;
	/*var minFreeTimes = freeTimes - 1 < 0 ? 0 : freeTimes - 1;//最小保养次数
	var maxFreeTimes = parseInt(freeTimes) + 1;//最大保养次数
	var cFreeTimes = minFreeTimes + '-' + maxFreeTimes;
	document.getElementById("cFreeTimes").innerText = cFreeTimes;//可变保养次数 minFreeTimes到maxFreeTimes
	document.getElementById("tcFreeTimes").value = cFreeTimes;
	*/
}
//根据vin和保养次数查询保养类的工单
function queryOrder() {
	if (!validateValue()) {
		return;
	}
	//先清除原来工单的数据
	document.getElementById("ftroDealerCode").value = '';
	document.getElementById("froDealerCode").innerText = '';
	document.getElementById("ferrorRoCode").value = '';
	document.getElementById("troFreeTimes").value = '';
	document.getElementById("roFreeTimes").innerText = '';
	var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/queryOrderByFreeTimes.json';
	makeNomalFormCall(url,handleQueryOrder,'fm');
}

function handleQueryOrder(json) {
	var data = json.ps;
	document.getElementById("ftroDealerCode").value = data.DEALER_CODE;
	document.getElementById("froDealerCode").innerText = data.DEALER_NAME;
	document.getElementById("ferrorRoCode").value = data.RO_NO;
	document.getElementById("troFreeTimes").value = data.FREE_TIMES;
	document.getElementById("roFreeTimes").innerText = data.FREE_TIMES;
}

</script>
