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
<script type="text/javascript">
	function doInit() {
		if('${disabled}'=='yes'&&'${list[0].ADDRESS_TYPE}'!=''){
			document.getElementById("SHOU_ADDRESS_TYPE").disabled=true;
		}
	}
	function add(_this) {
		var form = $("fm");
		if(validate(form)) {
			form.setAttribute("action", "<%=contextPath%>/sysmng/dealer/DealerAddressInfo/addNewDealerCsAddress.do");
			form.setAttribute("method", "post");
			form.submit();
			_this.setAttribute("disabled", "disabled");
		}
	}
	
	function validate(form) {
		var errMsg = "";
		var count = 1;
		
		if(form.LINKMAN.value.trim() == "") {
			errMsg += count++ +"、联系人姓名不能为空\n";
		}
		
// 		if(form.GENDER.value == "") {
// 			errMsg += count++ +"、请选择联系人性别\n";
// 		}
		
		if(form.MOBILE_PHONE.value != "") {
			if(!/^0?1\d{10}$/g.test(form.MOBILE_PHONE.value)) {
				errMsg += count++ +"、手机号码格式错误\n";
			}
		}
		if(form.SHOU_ADDRESS_TYPE.value == ""){
			errMsg += count++ +"、请选择地址类型\n";
		}
		if(form.ADDR.value.trim() == "") {
			errMsg += count++ +"、配件收货地址不能为空\n";
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
			form.setAttribute("action", "<%=contextPath%>/sysmng/dealer/DealerAddressInfo/updateDealerCsAddressInfo.do");
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
// 		$("GENDER").value = "${list[0].GENDER}";
		
		var len = "${fn:length(list)}";
		if(len == 1) {
			$("add_btn").style.display = "none";
			$("queryBtn").style.display = "none";
			$("update_btn").style.display = "";
			$("cancel_btn").style.display = "";
			
			$("tr_state").style.display = "";
			$("state").value = "${list[0].STATE}";
		} else {
			__extQuery__(1);
		}
		extQuery1(1);
	}
	
	function cancel() {
		window.location.href = "<%=contextPath%>/jsp/systemMng/dealer/dealerCsAddressVindicate.jsp?DEALER_ID=" 
				+ ("${param['DEALER_ID']}" != "" ? "${param['DEALER_ID']}" : "${DEALER_ID}");
	}
</script>
</head>

<body>
<form id="fm" name="fm">
<input type="hidden" value="" id="DEALER_ID" name="DEALER_ID" />
<input type="hidden" value="${list[0].ADDR_ID }" name="ADDR_ID" />
<table class="table_query" border="0">
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商代码：</td>
		<td><input
			name="dearlerCode" readonly="readonly" value="${dearlerCode }" id="dearlerCode" type="text" class="middle_txt" />
		</td>
		<td class="table_query_3Col_label_6Letter"  nowrap="nowrap">经销商名称：</td>
		<td ><input
			name="dearlerName" readonly="readonly" value="${dearlerName }"  id="dearlerName" type="text" class="middle_txt" />
		</td>
	</tr>
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">联系人姓名：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="LINKMAN" maxlength="30" value="${list[0].LINKMAN }" datatype="1,is_noquotation,30" id="LINKMAN" type="text" class="middle_txt" /></td>
<!-- 		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">联系人性别：</td> -->
<!-- 		<td class="table_query_4Col_input" nowrap="nowrap"> -->
<!-- 			<label> -->
<!-- 					<script type="text/javascript"> -->
<%-- 						genSelBoxExp("GENDER",<%=Constant.GENDER_TYPE%>,"-1",'true',"short_sel",'',"false",''); --%>
<!-- 					</script> -->
<!-- 			</label> -->
<!-- 		</td> -->
	</tr>
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">联系人手机：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"> 
			<input
				name="MOBILE_PHONE" maxlength="30" value="${list[0].MOBILE_PHONE }" datatype="1,is_noquotation,30" id="MOBILE_PHONE" type="text" class="middle_txt" /></td>
		</td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">联系人电话：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
			<input
				name="TEL" maxlength="30" value="${list[0].TEL }" datatype="1,is_noquotation,30" id="TEL" type="text" class="middle_txt" /></td>
		</td>
	</tr>
<!-- 	<tr id="tr_state" style="display: none;"> -->
<!-- 		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">状态：</td> -->
<!-- 		<td colspan="3" class="table_query_4Col_input" nowrap="nowrap"> -->
<!-- 			<label> -->
<!-- 					<script type="text/javascript"> -->
<%-- 						genSelBoxExp("STATE",<%=Constant.STATUS%>,"-1",'true',"short_sel",'',"false",''); --%>
<!-- 					</script> -->
<!-- 			</label> -->
<!-- 		</td> -->
<!-- 	</tr>	 -->
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">地址类型：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
		<input name="SHOU_ADDRESS_TYPE_HIDDEN" value="${list[0].ADDRESS_TYPE}"  id="SHOU_ADDRESS_TYPE_HIDDEN" type="hidden"/>
			<label>
					<script type="text/javascript">
						genSelBoxExp("SHOU_ADDRESS_TYPE",<%=Constant.SHOU_ADDRESS_TYPE%>,"${list[0].ADDRESS_TYPE}",'true',"short_sel","onchange='changeAddressType(this)'","false",'');
					</script>
			</label>
		</td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">地址状态：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
			<label>
					<script type="text/javascript">
						genSelBoxExp("STATUS",<%=Constant.STATUS%>,"${list[0].STATE}",false,"short_sel",'',"false",'');
					</script>
			</label>
		</td>
	</tr>
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">配件收货地址：</td>
		<td colspan="3" class="table_query_4Col_input" nowrap="nowrap">
			<input
				name="ADDR" maxlength="60" style="width:510px;" value="${list[0].ADDR }" datatype="1,is_noquotation,60" id="ADDR" type="text" class="middle_txt" /></td>
		</td>
	</tr>
	
	<tr align="center">
		<td colspan="4" class="table_query_4Col_input" style="text-align: center">
			<input name="button2" id="add_btn" type="button" class="normal_btn" onclick="add(this)" value="添 加" /> &nbsp;
			<input name="queryBtn" type="button" class="normal_btn" onclick="query()" value="查 询" id="queryBtn" /> &nbsp; 
			<span style="display:none;" id="update_btn"><input name="button2"  type="button" class="normal_btn" onclick="update()" value="修改" /> &nbsp;</span>
			<span style="display:none;" id="cancel_btn"><input name="button2" type="button" class="normal_btn" onclick="cancel()" value="返回" /> &nbsp;</span>
			<input name="button2" type="button" class="normal_btn" onclick="_hide();" value="关闭" /> &nbsp;
		</td>
	</tr>
</table>
</form>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />	
<script>
var myPage;
var url = "<%=contextPath%>/sysmng/dealer/DealerAddressInfo/queryDealeCsrAddressInfo.json?DEALER_ID=" 
		+ ("${param['DEALER_ID']}" != "" ? "${param['DEALER_ID']}" : "${DEALER_ID}");

var title= null;
var columns = [
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'ADDR_ID' ,renderer:myLink},
				{header: "配件收货地址",width:'30%',   dataIndex: 'ADDR'},
				{header: "地址类型", width:'10%', dataIndex: 'ADDRESS_TYPE',renderer:getItemValue},
// 				{header: "状态", width:'10%', dataIndex: 'STATUS',renderer:getItemValue},
				{header: "状态", width:'10%', dataIndex: 'STATE',renderer:getItemValue},
				{header: "联系人姓名", width:'20%', dataIndex: 'LINKMAN'},
// 				{header: "性别", width:'20%', dataIndex: 'GENDER',renderer:getItemValue},
				{header: "手机", width:'10%', dataIndex: 'MOBILE_PHONE'},
				{header: "电话", width:'10%', dataIndex: 'TEL'}
			  ];
			  
function myLink(value, object, record){
    return String.format("<a href=\"<%=contextPath%>/sysmng/dealer/DealerAddressInfo/queryDealerCsAddressInfoById.do?ADDR_ID="+value+"&DEALER_ID="+ record.data.DEALER_ID +"&address_type="+ record.data.ADDRESS_TYPE +"\">[修改]</a>");
}

function query() {
	url = "<%=contextPath%>/sysmng/dealer/DealerAddressInfo/queryDealeCsrAddressInfo.json?DEALER_ID=" 
		+ ("${param['DEALER_ID']}" != "" ? "${param['DEALER_ID']}" : "${DEALER_ID}")
		+ "&LINKMAN=" + $("LINKMAN").value
// 		+ "&GENDER=" + $("GENDER").value
		+ "&TEL=" + $("TEL").value
		+ "&MOBILE_PHONE=" + $("MOBILE_PHONE").value;

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
