<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商地址更改审核</title>
<script type="text/javascript">
<!--
function checkNull(obj) {
	
	var sStr = obj.value.replace(/(^\s*)|(\s*$)/g, ""); 
	
	if(!sStr.length) {
		return false ;
	} else {
		return true ;
	}
}
//-->
</script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：经销商地址更改审核</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="curPage" id="curPage" value="1" /> <input type="hidden" id="dlrId" name="dlrId" value="" />
<table class="table_query" border="0">
	<tr>
		<td width="20%" class="tblopt">
		<div class="right">选择经销商：</div>
		</td>
		<td>
			<input name="dealerCode" type="text" id="dealerCode" class="middle_txt" value="" size="20" readonly="readonly" />
            <input name="dlbtn" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','','true');" value="..." />
            <input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn" />
		</td>
		<td></td>
	</tr>
	<tr>
		<td width="20%" class="tblopt">
		<div class="right">业务范围：</div>
		</td>
		<td>
			<select name="areaId" id='areaId' class="u-select">
				<option value="">-请选择-</option>
				<c:forEach items="${areaBusList}" var="po">
					<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
				</c:forEach>
            </select>
		</td>
	</tr>
	<tr>
		<td width="20%" class="tblopt">
		<div class="right">地址名称：</div>
		</td>
		<td width="39%"><input type="text" class="middle_txt" id="address" name="address" /></td>
		<td class="table_query_3Col_input">
			<input type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" />
		</td>
	</tr>
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /> 
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<form  name="form1" id="form1">
<table class="table_query" width="85%" align="center">
	<tr>
		<td class="tblopt">
			<div class="right">审核意见：</div>
		</td>
		<td>
			<textarea rows="3" cols="50" name="desc" id="desc"></textarea>&nbsp;<font color="red">*</font>
		</td>
	</tr>
	<tr class="table_list_row2">
		<td align="center" colspan="2">
			<input type="button" name="button1" class="normal_btn" onclick="checkSubmit(1);" value="通 过" /> 
			<input type="button" name="button3" class="normal_btn" onclick="checkSubmit(2);" value="驳 回" />
		</td>
	</tr>
</table>
</form>

</div>
<script type="text/javascript">
	document.form1.style.display = "none";
	var HIDDEN_ARRAY_IDS=['form1'];

	var myPage;
	var url = "<%=contextPath%>/sales/storageManage/DealerAddressCheck/queryAddress.json?COMMAND=1";
	var title = null;
	var columns = null ;
	
	if("${dutyType}" == <%=Constant.DUTY_TYPE_COMPANY %>) {
		columns = [
		   		{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"Ids\")' />全选", width:'6%',sortable: false,dataIndex: 'ID',renderer:myCheckBox},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center',renderer:myHref},
				{header: "经销商名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "地址代码", dataIndex: 'ADD_CODE', align:'center'},
				{header: "地址名称", dataIndex: 'ADDRESS', align:'center'},
				{header: "收车单位", dataIndex: 'RECEIVE_ORG', align:'center'},
				{header: "限时类型", dataIndex: 'LIMIT_TYPE', align:'center',renderer:getItemValue},
				{header: "有效日期", dataIndex: 'START_TIME', align:'center',renderer:getLimitType},
				{header: "地址用途", dataIndex: 'ADDRESS_USE', align:'center'},
				{header: "联系人", dataIndex: 'LINK_MAN', align:'center'},
				{header: "电话", dataIndex: 'TEL', align:'center'},
				{header: "省份", dataIndex: 'PROVINCE_NAME', align:'center'},
				{header: "地级市", dataIndex: 'CITY_NAME', align:'center'},
				{header: "区县", dataIndex: 'AREA_NAME', align:'center'},
				{header: "业务范围", dataIndex: 'A_NAME', align:'center'},
				{header: "备注", dataIndex: 'REMARK', align:'center'},
				{header: "初审人", dataIndex: 'NAME', align:'center'},
				{header: "初审日期", dataIndex: 'LOW_CHECK_DATE', align:'center'},
				{header: "操作", dataIndex: 'ID', align:'center',renderer:itLink}
					      ];
	} else {
		columns = [
			   		{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"Ids\")' />全选", width:'6%',sortable: false,dataIndex: 'ID',renderer:myCheckBox},
					{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center',renderer:myHref},
					{header: "经销商名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
					{header: "地址代码", dataIndex: 'ADD_CODE', align:'center'},
					{header: "地址名称", dataIndex: 'ADDRESS', align:'center'},
					{header: "收车单位", dataIndex: 'RECEIVE_ORG', align:'center'},
					{header: "限时类型", dataIndex: 'LIMIT_TYPE', align:'center',renderer:getItemValue},
					{header: "有效日期", dataIndex: 'START_TIME', align:'center',renderer:getLimitType},
					{header: "地址用途", dataIndex: 'ADDRESS_USE', align:'center'},
					{header: "联系人", dataIndex: 'LINK_MAN', align:'center'},
					{header: "电话", dataIndex: 'TEL', align:'center'},
					{header: "省份", dataIndex: 'PROVINCE_NAME', align:'center'},
					{header: "地级市", dataIndex: 'CITY_NAME', align:'center'},
					{header: "区县", dataIndex: 'AREA_NAME', align:'center'},
					{header: "业务范围", dataIndex: 'A_NAME', align:'center'},
					{header: "备注", dataIndex: 'REMARK', align:'center'},
					{header: "操作", dataIndex: 'ID', align:'center',renderer:itLink}
						      ];
	} 
		
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='Ids' value='" + value + "' />");
	}
	
	function getLimitType(value,metaDate,record) {
		var sEndTime = record.data.END_TIME ;
		var sLimitType = record.data.LIMIT_TYPE ;
		var sReturnValue = "-" ;
		
		if(sLimitType == <%=Constant.ADDRESS_TIME_LIMIT_PERP %>) {
			sReturnValue = "-" ;
		} else if(sLimitType == <%=Constant.ADDRESS_TIME_LIMIT_TEMP %>) {
			sReturnValue = value + "至" + sEndTime ;
		}
		
		return String.format(sReturnValue);
	}

	function checkSubmit(flag){
		var Ids = document.getElementsByName("Ids");
		var addFlag = false;
		for(var i=0; i<Ids.length; i++){
			if(Ids[i].checked){
				addFlag = true;
				break;
			}
		}  
		if(!addFlag){
			MyAlert("请选择要审核信息!");
			return;
		}
		
		if("1" == flag){
			MyConfirm("是否通过?",checkSubmitAction,[flag]);
		}else{
			if(!checkNull(document.getElementById("desc"))) {
				MyAlert("请输入审核意见!");
				return;
			}
			
			MyConfirm("是否驳回?",checkSubmitAction,[flag]);
		}
	}

	function checkSubmitAction(flag){
		var Ids = document.getElementsByName("Ids");
		var idsArray=new Array();
		for(var i=0; i< Ids.length; i++){
			if(Ids[i].checked){
				idsArray.push(Ids[i].value);
			}
		}
		makeNomalFormCall('<%=contextPath%>/sales/storageManage/DealerAddressCheck/checkSubmit.json?flag='+flag+'&idsArray='+idsArray.toString(),showResult,'form1');
    }

	function showResult(json){
		if(json.returnValue == '1'){
			MyAlert("本次审核成功!");
			__extQuery__(1);
		}else{
			MyAlert("本次审核失败，请重新操作或联系管理员!");
		}
	}

	
	function myLink(value,metaDate,record){
		var data = record.data;
		var id = data.ID;
		return String.format(
			   "<a href=\"<%=contextPath%>/sales/storageManage/DealerAddressCheck/toCheckAddress.do?id="+id+"\">[审核]</a>");
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
	//查询该经销商审核通过的地址
	function myHref(value,metaDate,record){
		var dealerId = record.data.DEALER_ID;
		var dealerCode = record.data.DEALER_CODE;
		var dealerShortName = record.data.DEALER_SHORTNAME;
		var areaId = document.getElementById('areaId').value;
		var url = "<%=contextPath%>/sales/storageManage/DealerAddressCheck/queryPassAddressByDealerId.do?dealerId="+dealerId+"&dealerCode="+dealerCode+"&dealerShortName="+dealerShortName+"&areaId="+areaId;
		var link ="<a href='javascript:getDealerPassAddress(\""+url+"\")'>"+value+"</a>";
		return String.format(link);
	}
	function getDealerPassAddress(url){
		OpenHtmlWindow(url,900,500);
	}
	
	function itLink(value,metaDate,record) {
		var url = "<%=contextPath%>/sales/storageManage/DealerAddressCheck/toUpdateAddress.do?id=" + value ;
		var link = "<a href=" + url + ">[审核]</a>" ;
		return String.format(link);
	}
</script>
</body>
</html>