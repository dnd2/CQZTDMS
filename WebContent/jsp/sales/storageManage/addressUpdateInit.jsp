<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	String contextPath = request.getContextPath();
	String isSpecialCar = String.valueOf(request.getAttribute("isSpecialCar"));
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商新增地址申请</title>
<script type="text/javascript"><!--

function doInit()
{  
	   //初始化时间控件
	genLocSel('txt1','txt2','txt3','${province}','${city}','${area}'); // 加载省份城市和县
	var status = '${status}';
	
	var receive_org = '${addressInfo.RECEIVE_ORG }';
	if(receive_org){
		document.getElementById("receiveOrg").value = receive_org;
	}
	setLimitTimeDisplay() ;
	
	chgSel();
}

function setLimitTimeDisplay() {
	var sLimitValue = document.getElementById("limit").value ;
	
	if(sLimitValue == ${conMap.perp}) {
		setStyleDisplay("limitLable", "none") ;
		setStyleDisplay("limitTime", "none") ;
	} else if(sLimitValue == ${conMap.temp}) {
		setStyleDisplay("limitLable", "inline") ;
		setStyleDisplay("limitTime", "inline") ;
	}
}

function setStyleDisplay(objId, paramValue) {
	document.getElementById(objId).style.display = paramValue ;
}

function checkDate() {
	var sLimitValue = document.getElementById("limit").value ;
	
	if(sLimitValue == ${conMap.temp}) {
		var startDate = document.getElementById("t1").value ;
		var sysDate = document.getElementById("sys_date__").value ;
		var aSysDate = sysDate.split(",") ;
		var sSysDate = aSysDate[0] + "-" + aSysDate[1] + "-" + aSysDate[2] ;
		
		if(startDate >= sSysDate) {
			return true ;
		} else {
			return false ;
		}
	} else {
		return true ;
	}
}

function retFunction() {
	document.getElementById('fm').action= "<%=request.getContextPath()%>/sales/storageManage/AddressAddApply/addressAddApplyInit.do";
	document.getElementById('fm').submit();
}

function chgSel() {
	var sAddressId = document.getElementById("addressId").value ;
	
	var url = "<%=request.getContextPath()%>/sales/storageManage/AddressAddApply/dlrSelChange.json";
		
	makeFormCall(url, showResult, "fm") ;
	
}

function showResult(json) {
	//var addressCode = json.addCode ;
	
	//document.getElementById("addCode").value = addressCode ;
	
	var aAreaId = json.areaIds.split(",") ;
	var aAreaName = json.areaNames.split(",") ;
	var aChkArea = new Array() ;
	
	if(json.checkAreas) {
		aChkArea = json.checkAreas.split(",") ;
	}
	
	if(!aAreaId || aAreaId == "") {
		MyAlert("选择经销商未维护业务范围!") ;
		
		return false ;
	} else {
		var iLen = aAreaId.length ;
		var sStr = "" ;
		
		for(var i=0; i<iLen; i++) {
			var bChkFlag = false ;
			
			if(!aChkArea || aChkArea == "") {
				
			} else {
				var iChkLen = aChkArea.length ;
				
				for(var j=0; j<iChkLen; j++) {
					if(aAreaId[i] == aChkArea[j]) {
						bChkFlag = true ;
						
						break ;
					}
				}
			}
			
			if(bChkFlag) {
				sStr += "<input type=\"checkBox\" name=\"addressAreas\" id=\"" + aAreaId[i] + "\" value=\"" + aAreaId[i] + "\" checked disabled /><label for=\"" + aAreaId[i] + "\">" + aAreaName[i] + "</label>" ;
			} else {
				sStr += "<input type=\"checkBox\" name=\"addressAreas\" id=\"" + aAreaId[i] + "\" value=\"" + aAreaId[i] + "\" /><label for=\"" + aAreaId[i] + "\">" + aAreaName[i] + "</label>" ;
			}
		}
		
		document.getElementById("areas").innerHTML = sStr ;
	}
}
//-->
</script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：采购管理 &gt; 库存管理 &gt; 经销商新增地址申请&gt; 经销商地址信息</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="curPage" id="curPage" value="1" /> <input type="hidden" id="dlrId" name="dlrId" value="" />
<table class="table_query" border="0">
<!--  
	<tr>
		<td class="tblopt" nowrap="nowrap" class="right">经销商选择：</td>
		<td colspan="7">
			<select id="dlrList" name="dlrList" onchange="chgSel();">
				<option value="">-请选择-</option>
				<c:forEach var="dlrList" items="${dlrList }">
					<c:if test="${dlrList.DEALER_ID == dealerId}">
						<option value="${dlrList.DEALER_ID }" selected>${dlrList.DEALER_NAME }-${dlrList.DEALER_CODE }</option>
					</c:if>
					<c:if test="${dlrList.DEALER_ID != dealerId}">
						<option value="${dlrList.DEALER_ID }">${dlrList.DEALER_NAME }-${dlrList.DEALER_CODE }</option>
					</c:if>
				</c:forEach>
			</select>
		</td>
	</tr>
	-->
	<tr>
		<td class="tblopt" nowrap="nowrap" >
			<div class="right">业务范围：</div>
		</td>
		<td colspan="7"><span id="areas"></span></td>
	</tr>
	<tr>
		<td class="tblopt" nowrap="nowrap" >
			<div class="right">是否跨区域发运：</div>
		</td>
		<td>
				<script type="text/javascript">
					genSelBoxExp("isCrossing",<%=Constant.IF_TYPE%>,"${addressInfo.IS_CROSSING}",true,"u-select","","false",'');
				</script>
				<font color="red">*</font>
		</td>
		<td colspan="7"></td>
	</tr>
	<tr>
		<td class="tblopt" nowrap="nowrap" >
			<div class="right">时限：</div>
		</td>
		<td>
				<script type="text/javascript">
					genSelBoxExp("limit",<%=Constant.ADDRESS_TIME_LIMIT%>,"${addressInfo.LIMIT_TYPE }",false,"u-select","onchange=setLimitTimeDisplay();","false",'');
				</script>
		</td>
		<td class="tblopt" nowrap="nowrap" id="limitLable">
			<div class="right">有效期：</div>
		</td>
		<td id="limitTime">
			<div align="left">
            	<input name="limitStartDate" id="t1" type="text" class="short_txt" value="${addressInfo.START_TIME }" datatype="0,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);" />
            		&nbsp;至&nbsp;
            	<input name="limitEndDate" id="t2" type="text" class="short_txt" value="${addressInfo.END_TIME }" datatype="0,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);" />
            </div>
		</td>
	</tr>
	<!--  
	<tr>
		<td class="tblopt" nowrap="nowrap" > 
			<div class="right">地址代码：</div>
		</td>
		<td colspan="7"><input type="text" id="addCode" name="addCode" class="middle_txt" value="${addCode }" datatype="0,is_textarea,20" maxlength="20" readonly="readonly"/></td>
	</tr>
	-->
	<tr>
	    <td class="tblopt" nowrap="nowrap" ><div class="right">省份：</div></td>
	    <td nowrap="nowrap" ><select class="u-select" id="txt1" name="province" onchange="_genCity(this,'txt2')"></select><font color="red">*</font> </td>
        <td class="tblopt" nowrap="nowrap" ><div class="right">地级市：</div></td>
	    <td nowrap="nowrap" ><select class="u-select" id="txt2" name="city"  onchange="_genCity(this,'txt3')" ></select><font color="red">*</font></td>  
	    <td class="tblopt" nowrap="nowrap" ><div class="right">区县：</div></td>
	    <td width="40%" nowrap="nowrap" ><select class="u-select" id="txt3" name="area" ></select><font color="red">*</font></td>
	</tr>
	<tr>	
		<td class="tblopt" nowrap="nowrap" >
			<div class="right">街道地址<font color="red">(请勿填写省份、地级市、区县)</font>：</div>
		</td>
		<td width="40%" colspan="7">
		<input type="text" id="address" name="address" class="long_txt" value="${address }" datatype="0,is_textarea,100" size="80" style="width:320px" />
		<input type="button" class="long_btn" onclick="queryAllAdd();" value="已维护地址" style="width:70px" />
		</td>
	</tr>
	<tr>
		<td class="tblopt" nowrap="nowrap" >
			<div class="right">收车单位：</div>
		</td>
		<td colspan="5" nowrap="nowrap" ><input type="text" id="receiveOrg" name="receiveOrg" class="long_txt" value="${addressInfo.RECEIVE_ORG }" datatype="0,is_textarea,100" size="80" style="width:320px" /></td>
	</tr>
	<tr>
		<td class="tblopt" nowrap="nowrap" >
			<div class="right">联系人：</div>
		</td>
		<td nowrap="nowrap" ><input type="text" id="linkMan" name="linkMan" class="middle_txt" value="${addressInfo.LINK_MAN }" datatype="0,is_textarea,10" /></td>
		<td class="tblopt" nowrap="nowrap" >
			<div class="right">电话：</div>
		</td>
		<td colspan="3"><input type="text" id="tel" name="tel" value="${addressInfo.TEL }" class="middle_txt" datatype="0,is_textarea,40" maxlength="40" /></td>
	</tr>
	<tr>
		<td class="tblopt" nowrap="nowrap" >
			<div class="right">备注：</div>
		</td>
		<td colspan="5" nowrap="nowrap" ><input type="text" id="remark" name="remark" class="long_txt" value="${addressInfo.REMARK }" size="80" style="width:320px" /></td>
	</tr>
	<tr>
		<td class="tblopt" nowrap="nowrap" >
			<div class="right">发运地址用途：</div>
		</td>
		<td colspan="5" nowrap="nowrap" ><input type="text" id="addressUse" name="addressUse" class="long_txt" value="${addressInfo.ADDRESS_USE }" datatype="0,is_textarea,100" size="80" style="width:320px" /></td>
	</tr>
	</table>
	<c:if test="${logFlag == 1}">
	<table width='100%' border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
	        <th colspan="4">&nbsp;审批记录</th>
		</tr>
		<tr align="center">
			<td>审核人员</td>
			<td>审批状态</td>
			<td>审批描述</td>
			<td>审批时间</td>
		</tr>
		<c:forEach items="${logList}" var="list3">
			<tr class="table_list_row2" align="center">
				<td>${list3.NAME}</td>
				<td>
					<script>document.write(getItemValue(${list3.CHECK_STATUS }));</script>
				</td>
				<td>${list3.CHECK_DESC}</td>
				<td>${list3.CREATE_DATE}</td>
			</tr>
		</c:forEach>
	</table>
	</c:if>
	<br />
	<table border="0" align="center">
	<tr>
		<td>
			<input type="button" class="normal_btn" onclick="saveOrSubmitAddress(1);" value="保 存" id="queryBtn" />
			<input type="button" class="normal_btn" onclick="saveOrSubmitAddress(2);" value="提 交" id="queryBtn1" />
			<input type="button" class="normal_btn" onclick="retFunction();" value="返 回" id="retBtn" />
			<input type="hidden" id="actFlag" name="actFlag" />
			<input type="hidden" id="addressId" name="addressId" value="${addressInfo.ID }" />
			<input type="hidden" id="status" name="status" value="${status }" />
		</td>
		<td><input type="hidden" id="areaId" name="areaId" value="${areaId }" /></td>
	</tr>
</table>
</form>
</div>
<script type="text/javascript">

	function queryAllAdd(){
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/storageManage/AddressAddApply/queryAllAdd.do',900,500);
	}

	function saveOrSubmitAddress(flag){ 
		document.getElementById("actFlag").value = flag ;
		
		var cross = document.getElementById("isCrossing").value;
		
		var txt1 = document.getElementById("txt1").value;
		var txt2 = document.getElementById("txt2").value;
		var txt3 = document.getElementById("txt3").value;
		
		//var sDlr = document.getElementById("dlrList").value ;
		
		//if(!sDlr.length) {
		//	MyAlert("请选择经销商!") ;
			
		//	return false ;
		//}
		
		var aAreas = document.getElementsByName("addressAreas") ;
		
		if(!aAreas || aAreas.length == 0) {
			MyAlert("业务范围未维护!") ;
			
			return false ;
		} else {
			var iAreaLen = aAreas.length ;
			var bAreaFlag = false ;
			
			for(var i=0; i<iAreaLen; i++) {
				if(aAreas[i].checked) {
					bAreaFlag = true ;
				}
			}
			
			if(!bAreaFlag) {
				MyAlert("请选择业务范围!") ;
				
				return false ;
			}
		}
		
		if(cross.length == 0){
			MyAlert("请选择'是否跨区域发运'!");
			return;
		}
		
		var sLimitValue = document.getElementById("limit").value ;
		
		if(sLimitValue == ${conMap.temp}) {
			var startDate = document.getElementById("t1").value ;
			
			if(!startDate.length) {
				MyAlert("请填写开始有效日期!");
				return;
			}
		}
		
		if(!checkDate()) {
			MyAlert("开始有效日期必须大于系统当前日期!");
			return;
		}
			
			if(txt1.length==0){
				MyAlert("请选择'省份'!");
				return;
			}
			if(txt2.length==0){
				MyAlert("请选择'地级市'!");
				return;
			}
			if(txt3.length==0){
				MyAlert("请选择'县'!");
				return;
			}
		if(submitForm('fm')){
			var url = "<%=request.getContextPath()%>/sales/storageManage/AddressAddApply/chkAddressSame.json" ;
			
			makeFormCall(url, execAction, "fm") ;
		}
	}
	
	function execAction(json) {
		var flagStr = json.flagStr ;
		var areaStr = json.areaStr ;
		
		if(flagStr == "error") {
			MyAlert("业务范围为:" + areaStr + "的地址,已存在!") ;
			
			return false ;
		}
		
		var flag = document.getElementById("actFlag").value ;
		if("1" == flag+""){
			MyConfirm("是否保存?",submitAction,[flag]);
		}
		if("2" == flag+""){
			MyConfirm("是否提交?",submitAction,[flag]);
		}
	}
	
	function submitAction(flag){
		document.getElementById('fm').action= "<%=request.getContextPath()%>/sales/storageManage/AddressAddApply/updateAction.do?flag="+flag;
		document.getElementById('fm').submit();
	}

	function province(province,city,area){
		genLocSel('txt1','txt2','txt3',province,city,area);
	}
</script>
</body>
</html>