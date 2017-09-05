<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商地址维护</title>
<style>#ADDRESS{width:505px;}</style>
<script type="text/javascript">
	function doInit() {
		genLocSel('PROVINCE_ID','CITY_ID','AREA_ID','${list[0].PROVINCE_ID}','${list[0].CITY_ID}','${list[0].AREA_ID}'); // 加载省份城市和县
	}
	
	function add(_this) {
		var form = document.getElementById("fm");
		if(validate(form)) {
			form.setAttribute("action", "<%=contextPath%>/sysmng/dealer/DealerAddressInfo/addNewDealerAddress.do");
			form.setAttribute("method", "post");
			form.submit();
			_this.setAttribute("disabled", "disabled");
		}
	}
	
	function validate(form) {
		var errMsg = "";
		var count = 1;
		
		if(form.LINK_MAN.value.trim() == "") {
			errMsg += count++ +"、联系人姓名不能为空\n";
		}
		
		if(form.SEX.value == "") {
			errMsg += count++ +"、请选择联系人性别\n";
		}
		
		if(form.MOBILE_PHONE.value != "") {
			if(!/^0?1\d{10}$/g.test(form.MOBILE_PHONE.value)) {
				errMsg += count++ +"、手机号码格式错误\n";
			}
		}
		
		if(form.ADDRESS_TYPE.value == ""){
			errMsg += count++ +"、请选择地址类型\n";
		}
		
		if(form.ADDRESS_TYPE.value == "<%=Constant.SH_ADDRESS_TYPE_04%>") {
			if(form.PROVINCE_ID.value == "" || form.CITY_ID.value == "" || form.AREA_ID.value == "") {
				errMsg += count++ +"、请选择省/市/县\n";
			}
		}
			
		if(form.ADDRESS.value.trim() == "") {
			errMsg += count++ +"、详细地址不能为空\n";
		}
		
		if(errMsg == "") {
			return true;
		} else {
			MyAlert(errMsg);
			return false;
		}
	}
	
	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	
	function update() {
		var form = document.getElementById("fm");
		
		if(validate(form)) {
			form.setAttribute("action", "<%=contextPath%>/sysmng/dealer/DealerAddressInfo/updateDealerAddressInfo.do?ADDRESS_TYPE=${list[0].ADDRESS_TYPE}");
			form.setAttribute("method", "post");
			form.submit();
		}
	}
	
	window.onload = function() {
		var inp = document.getElementById("DEALER_ID");
		if("${param['DEALER_ID']}" != "") {
			inp.value = "${param['DEALER_ID']}";
		} else {
			inp.value = "${DEALER_ID}";
		}
		
		document.getElementById("SEX").value = "${list[0].SEX}";
		document.getElementById("ADDRESS_TYPE").value = "${list[0].ADDRESS_TYPE}";
		document.getElementById("STATUS").value = "${list[0].STATUS}";
		
		var len = "${fn:length(list)}";
		if(len == 1) {
			document.getElementById("add_btn").style.display = "none";
			document.getElementById("queryBtn").style.display = "none";
			document.getElementById("update_btn").style.display = "";
			document.getElementById("cancel_btn").style.display = "";
			$("ADDRESS_TYPE").setAttribute("disabled", "disabled");
			
			if("${list[0].ADDRESS_TYPE}" == "<%=Constant.SH_ADDRESS_TYPE_04%>") {
				document.getElementById("tr_address").style.display = "";
			}
		} else {
			__extQuery__(1);
		}
		extQuery1(1);
		doInit();
	}
	
	function cancel() {
		window.location.href = "<%=contextPath%>/jsp/systemMng/dealer/dealerAddressVindicate.jsp?DEALER_ID=" 
				+ ("${param['DEALER_ID']}" != "" ? "${param['DEALER_ID']}" : "${DEALER_ID}");
	}
</script>
</head>

<body>
<form id="fm" name="fm">
<input type="hidden" value="" id="DEALER_ID" name="DEALER_ID" />
<input type="hidden" value="${list['0'].ID }" name="ID" />
<table class="table_query" border="0">
	<tr>
		<td class="table_query_3Col_label_6Letter right" nowrap="nowrap">经销商代码：</td>
		<td><input
			name="dearlerCode" readonly="readonly" value="${dearlerCode}" id="dearlerCode" type="text" class="middle_txt" />
		</td>
		<td class="table_query_3Col_label_6Letter right"  nowrap="nowrap">经销商名称：</td>
		<td ><input
			name="dearlerName" readonly="readonly" value="${dearlerName}"  id="dearlerName" type="text" class="middle_txt" />
		</td>
	</tr>
	<tr>
		<td class="table_query_3Col_label_6Letter right" nowrap="nowrap">联系人姓名：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="LINK_MAN" maxlength="30" value="${list[0].LINK_MAN }" datatype="1,is_noquotation,30" id="LINK_MAN" type="text" class="middle_txt" /></td>
		<td class="table_query_3Col_label_6Letter right" nowrap="nowrap">联系人性别：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
			<label>
					<script type="text/javascript">
						genSelBoxExp("SEX",<%=Constant.GENDER_TYPE%>,"-1",'true',"short_sel u-select",'',"false",'');
					</script>
			</label>
		</td>
	</tr>
	<tr>
		<td class="table_query_3Col_label_6Letter right" nowrap="nowrap">联系人手机：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"> 
			<input
				name="MOBILE_PHONE" maxlength="30" value="${list[0].MOBILE_PHONE }" datatype="1,is_noquotation,30" id="MOBILE_PHONE" type="text" class="middle_txt" /></td>
		</td>
		<td class="table_query_3Col_label_6Letter right" nowrap="nowrap">联系人电话：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
			<input
				name="TEL" maxlength="30" value="${list[0].TEL }" datatype="1,is_noquotation,30" id="TEL" type="text" class="middle_txt" /></td>
		</td>
	</tr>
	<tr>
		<td class="table_query_3Col_label_6Letter right" nowrap="nowrap">地址类型：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
			<label>
					<script type="text/javascript">
						genSelBoxExp("ADDRESS_TYPE",<%=Constant.SH_ADDRESS_TYPE%>,"-1",'true',"short_sel u-select","onchange='changeAddressType(this)'","false",'');
					</script>
			</label>
		</td>
		<td class="table_query_3Col_label_6Letter right" nowrap="nowrap">地址状态：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
			<label>
					<script type="text/javascript">
						genSelBoxExp("STATUS",<%=Constant.STATUS%>,"-1",'true',"short_sel u-select",'',"false",'');
					</script>
			</label>
		</td>
	</tr>
	<tr id="tr_address" style="display: none;">
		<td class="table_query_3Col_label_6Letter right" nowrap="nowrap">省/市/县：</td>
		<td colspan="3" class="table_query_4Col_input" nowrap="nowrap">
			<select class="min_sel u-select" id="PROVINCE_ID" name="PROVINCE_ID" onchange="_genCity(this,'CITY_ID')">
			</select>
			<select class="min_sel u-select" id="CITY_ID" name="CITY_ID" onchange="_genCity(this,'AREA_ID')">
			</select>
			<select class="min_sel u-select" id="AREA_ID" name="AREA_ID">
			</select>
		</td>
	</tr>
	<tr>
		<td class="table_query_3Col_label_6Letter right" nowrap="nowrap">详细地址：</td>
		<td colspan="3" class="table_query_4Col_input" nowrap="nowrap">
			<input
				name="ADDRESS" maxlength="60" value="${list[0].ADDRESS }" datatype="1,is_noquotation,60" id="ADDRESS" type="text" class="middle_txt" /></td>
		</td>
	</tr>
	
	<tr align="center">
		<td colspan="4" class="table_query_4Col_input" style="text-align: center">
			<input name="button2" id="add_btn" type="button" class="u-button" onclick="add(this)" value="添 加" /> &nbsp;
			<input name="queryBtn" type="button" class="u-button u-query" onclick="query()" value="查 询" id="queryBtn" /> &nbsp; 
			<span style="display:none;" id="update_btn"><input name="button2"  type="button" class="u-button" onclick="update()" value="修改" /> &nbsp;</span>
			<span style="display:none;" id="cancel_btn"><input name="button2" type="button" class="u-button" onclick="cancel()" value="返回" /> &nbsp;</span>
			<input name="button2" type="button" class="u-button u-cancel" onclick="_hide();" value="关闭" /> &nbsp;
		</td>
	</tr>
</table>
</form>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />	
<script>
var myPage;
var url = "<%=contextPath%>/sysmng/dealer/DealerAddressInfo/queryDealerAddressInfo.json?DEALER_ID=" 
		+ ("${param['DEALER_ID']}" != "" ? "${param['DEALER_ID']}" : "${DEALER_ID}");

var title= null;
var columns = [
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'ID' ,renderer:myLink},
				{header: "详细地址",width:'30%',   dataIndex: 'ADDRESS'},
				{header: "地址类型", width:'10%', dataIndex: 'ADDRESS_TYPE',renderer:getItemValue},
				{header: "状态", width:'10%', dataIndex: 'STATUS',renderer:getItemValue},
				{header: "联系人姓名", width:'20%', dataIndex: 'LINK_MAN'},
				{header: "性别", width:'20%', dataIndex: 'SEX',renderer:getItemValue},
				{header: "手机", width:'10%', dataIndex: 'MOBILE_PHONE'},
				{header: "电话", width:'10%', dataIndex: 'TEL'}
			  ];
			  
function myLink(value, object, record){
    return String.format("<a href=\"<%=contextPath%>/sysmng/dealer/DealerAddressInfo/queryDealerAddressInfoById.do?ID="
    		+value+"&DEALER_ID="+ document.getElementById("DEALER_ID").value +"&ADDRESS_TYPE="+ record.data.ADDRESS_TYPE +"\">[修改]</a>");
}

function changeAddressType(_this) {
	if(_this.value == <%=Constant.SH_ADDRESS_TYPE_04%>) {
		$("tr_address").style.display = "";
	} else {
		$("tr_address").style.display = "none";
	}
	
	$("PROVINCE_ID").value = "";
	$("CITY_ID").value = "";
	$("AREA_ID").value = "";
}

function query() {
	url = "<%=contextPath%>/sysmng/dealer/DealerAddressInfo/queryDealerAddressInfo.json?DEALER_ID=" 
		+ ("${param['DEALER_ID']}" != "" ? "${param['DEALER_ID']}" : "${DEALER_ID}")
		+ "&ADDRESS_TYPE=" + $("ADDRESS_TYPE").value
		+ "&STATUS=" + $("STATUS").value
		+ "&LINK_MAN=" + $("LINK_MAN").value
		+ "&SEX=" + $("SEX").value
		+ "&TEL=" + $("TEL").value
		+ "&MOBILE_PHONE=" + $("MOBILE_PHONE").value
		+ "&PROVINCE_ID=" + $("PROVINCE_ID").value
		+ "&CITY_ID=" + $("CITY_ID").value
		+ "&AREA_ID=" + $("AREA_ID").value;
	extQuery1('${curPage}');
	__extQuery__('${curPage}');
}

function $(id) {
	if(typeof id === "string") {
		return document.getElementById(id);
	}
	
	return id;
}
function extQuery1(page){

	entThisPage = page;
	showMask();
	submitForm('fm') ? sendAjax(url+(url.lastIndexOf("?") == -1?"?":"&")+"curPage="+page,callBack1,'fm') : ($("queryBtn").disabled = "",removeMask());
}

function callBack1(json) {
	var dearlerCode = json.dearlerCode;
	if(dearlerCode != null){
		document.getElementById("dearlerCode").value=dearlerCode;
	}
	var dearlerName = json.dearlerName;
	if(dearlerName != null){
		document.getElementById("dearlerName").value=dearlerName;
	}
}
</script>
</body>
</html>
