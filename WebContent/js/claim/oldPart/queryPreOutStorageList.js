var HIDDEN_ARRAY_IDS = [ 'form1' ];

var myPage;
// 查询路径
var url = globalContextPath + "/claim/oldPart/ClaimOldPartOutStorageManager/queryPreOutStoreList.json";

var title = null;

var columns = [
		{header: "序号",align:'center',renderer:getIndex},
		{id:'action',header: "全选<input type='checkbox' id='checkAll' name='checkAll' onclick='selectAll(this,\"orderIds\")'>", width:'8%',sortable: false,dataIndex: 'id',renderer:myCheckBox},
		{header : "切换件",dataIndex : 'is_qhj',	align : 'center'},
		{header: "索赔单号", dataIndex: 'claim_no', align:'center',renderer:myLink1},
		{header: "配件代码", dataIndex: 'part_code', align:'center',renderer:myLink2},
		{header: "配件名称",dataIndex: 'part_name',align:'center',renderer:myLink3},
		{header: "供应商代码", dataIndex: 'supply_code', align:'center',renderer:myLink4},
		{header: "供应商名称",dataIndex: 'supply_name',align:'center',renderer:myLink5},
		{header: "出库数",dataIndex: 'all_amount',align:'center',renderer:myLink6},
		{header: "主因件类型",dataIndex: 'partReturn',align:'center',renderer:myLink7},
		{header: "备注",dataIndex: '',align:'center',renderer:myRemark}
	];
// 全选checkbox
function myCheckBox(value, metaDate, record) {
	return String.format("<input type='checkbox' id='orderIds"+ record.data.id
			+ "' name='orderIds' value='"+ value
			+ "' /><input type='hidden'  class='middle_txt' maxlength='30' id='supplyCode"
			+ record.data.id + "' name='supplyCode' value='"
			+ record.data.supply_code + "' />");
}
function myLink1(value, metaDate, record) {
	var width = 900;
	var height = 500;
	var screenW = window.screen.width - 30;
	var screenH = document.viewport.getHeight();
	if (screenW != null && screenW != 'undefined')
		width = screenW;
	if (screenH != null && screenH != 'undefined')
		height = screenH;
	var ID = record.data.claim_id;
	var claimNo = record.data.claim_no;
	var bill_type = record.data.bill_type;
	var hlink = '';
	if (1 == bill_type) {
		hlink = "<a href='#' onclick='OpenHtmlWindow(\""
				+ globalContextPath
				+ "/claim/application/ClaimBillStatusTrack/claimBillDetailForward.do?ID="
				+ ID + "\"," + width + "," + height + ")' >[" + claimNo
				+ "]</a>";
	} else if (2 == bill_type) {
		var fee_type = record.data.fee_type;
		ID = record.data.id;
		hlink = "<a href='#' onclick='OpenHtmlWindow("
				+ "\"" + globalContextPath
				+ "/claim/specialExpenses/SpecialExpensesManage/speciaExpensesInfo.do?id="
				+ ID + "&feeType=" + fee_type + "\"," + 800 + "," + 600
				+ ")'>" + "[" + claimNo + "]" + "</a>";
	}
	if (hlink == '') {
		hlink = "[" + claimNo + "]";
	}
	return String.format(hlink + "<input type='hidden'  id='claimNo"
			+ record.data.id + "' name='claimNo" + record.data.id + "' value='"
			+ value + "' />");
}
function myLink2(value, metaDate, record) {
	var claimNo = record.data.claim_no;
	var supCode = record.data.supply_code;
	var supName = record.data.supply_name;
	return String.format("<a href='#' onClick='partDetail(\"" + value + "\",\""
			+ claimNo + "\",\"" + supCode + "\",\"" + supName + "\");'>["
			+ value + "]</a> <input type='hidden'  id='partCode"
			+ record.data.id + "' name='partCode" + record.data.id
			+ "' value='" + value + "' />");
}
function myLink3(value, metaDate, record) {
	return String.format("" + value + "<input type='hidden'  id='partName"
			+ record.data.id + "' name='partName" + record.data.id
			+ "' value='" + value + "' />");
}
function myLink4(value, metaDate, record) {
	return String.format("" + value + "<input type='hidden'  id='supplyCode"
			+ record.data.id + "' name='supplyCode" + record.data.id
			+ "' value='" + value + "' />");
}
function myLink5(value, metaDate, record) {
	return String.format("" + value + "<input type='hidden'  id='supplyName"
			+ record.data.id + "' name='supplyName" + record.data.id
			+ "' value='" + value + "' />");
}
function myLink6(value, metaDate, record) {
	return String.format("" + value + "<input type='hidden'  id='allAmount"
			+ record.data.id + "' name='allAmount" + record.data.id
			+ "' value='" + value + "' />");
}
function myLink7(value, metaDate, record) {
	if (value == 1) {
		return String.format("实物返件<input type='hidden'  id='partReturn"
				+ record.data.id + "' name='partReturn" + record.data.id
				+ "' value='" + value + "' />");
	} else if (value == 0) {
		return String.format("无件返厂<input type='hidden'  id='partReturn"
				+ record.data.id + "' name='partReturn" + record.data.id
				+ "' value='" + value + "' />");
	} else if (value == 2) {
		return String.format("特殊费用<input type='hidden'  id='partReturn"
				+ record.data.id + "' name='partReturn" + record.data.id
				+ "' value='" + value + "' />");
	}
}
// 生成备注文本框
function myRemark(value, metaDate, record) {
	return String.format("<input type='text'  class='middle_txt' maxlength='20' id='remark"
					+ record.data.id + "' name='remark" + record.data.id + "' value='' />");
}
function partDetail(partCode, claimNo, supCode, supName) {
	OpenHtmlWindow(globalContextPath + '/claim/oldPart/ClaimOldPartOutStorageManager/queryPartClaim.do?partCode='
					+ partCode + '&claimNo=' + claimNo + '&supCode=' + supCode
					+ '&supName=' + encodeURI(supName), 800, 500);
}
// 格式化时间为YYYY-MM-DD
function formatDate(value, meta, record) {
	if (value == "" || value == null) {
		return "";
	} else {
		return value.substr(0, 10);
	}
}

function checkDate() {
	var name = $('supply_name').value;
	var code = $('supply_code').value;
	var dname = $('dealer_name').value;
	var dcode = $('dealer_code').value;
	if (name == "" && code == "" && dname == "" && dcode == "") {
		alert("供应商代码和简称，服务站代码和名称4个中至少输一个!");
		return false;
	} else {
		getNo();
		__extQuery__(1);
	}
}
function doInit() {
	loadcalendar();
}

// 选中预检查
function preChecked() {
	var str = "";
	var chk = document.getElementsByName("orderIds");
	var len = chk.length;
	var cnt = 0;
	var types = document.getElementById("OUT_CLAIM_TYPE").value;
	if (types == null || types == '') {
		alert("请选择出库类型!");
		return false;
	}
	var noflag = false;
	var noType = document.getElementsByName("noType");
	for (var i = 0; i < noType.length; i++) {
		if (noType[i].checked) {
			noflag = true;
		}
	}
	if (!noflag) {
		alert("请选择出库方式!");
		return false;
	}
	for (var i = 0; i < len; i++) {
		if (chk[i].checked) {
			str = chk[i].value + "," + str;
			cnt++;
		}
	}
	if (cnt == 0) {
		MyAlert("请选择要出库的配件！");
		return;
	}

	if (str != "") {
		str = str.substring(0, str.length - 1);
	}
	$('save_btn').disabled = "disabled";
	var url = globalContextPath + "/claim/oldPart/ClaimOldPartOutStorageManager/outOfStoreCheck.json?idStr=" + str;
	makeNomalFormCall(url, showResult, 'fm', 'createOrdBtn');
}
function showResult(json) {
	$('save_btn').disabled = false;
	if (json.msg == "") {
		$('isHs').value = json.isHs;
		MyConfirm("确认出库？", outOfStore, [ json.idStr ]);
	} else {
		alert(json.msg);
		return false;
	}

}
// 出库操作
function outOfStore(str) {
	$('save_btn').disabled = "disabled";
	var url = globalContextPath + "/claim/oldPart/ClaimOldPartOutStorageManager/outOfStore.json?idStr=" + str;
	makeNomalFormCall(url, afterCall, 'fm', 'createOrdBtn');
}
// 签收回调处理
function afterCall(json) {
	var retCode = json.updateResult;
	if (retCode != null && retCode != '') {
		if (retCode == "updateSuccess") {
			alert("出库成功!");
			getNo();
			__extQuery__(1);
			$('save_btn').disabled = false;
		} else if (json.msg != null && json.msg != "") {
			alert(json.msg);
			$('save_btn').disabled = false;
		} else {
			MyAlert("出库失败!请联系管理员!");
			$('save_btn').disabled = false;
		}
	}
}
function backTo() {
	var yieldly = $('yieldly').value;
	if (yieldly == '<%=Constant.PART_IS_CHANGHE_01 %>') {
		fm.action = globalContextPath + "/claim/oldPart/ClaimOldPartOutStorageManager/queryListPage.do";
	} else if (yieldly == '<%=Constant.PART_IS_CHANGHE_02 %>') {
		fm.action = globalContextPath + "/claim/oldPart/ClaimOldPartOutStorageManager/queryListPage2.do";
	}
	fm.method = "post";
	fm.submit();
}
var obj = document.getElementById("OUT_CLAIM_TYPE");
if (obj) {
	obj.attachEvent('onchange', changeValues);//
}
function changeValues() {
	$('save_btn').disabled = false;
	var ids = document.getElementsByName('orderIds');
	document.getElementById('checkAll').checked = false;
	for (var i = 0; i < ids.length; i++) {
		ids[i].checked = false;
	}
}

function printDetail() {
	var name = $('supply_name').value;
	var code = $('supply_code').value;
	var dname = $('dealer_name').value;
	var dcode = $('dealer_code').value;
	var partCode = $('part_code').value;
	var partName = $('part_name').value;
	var claimNo = $('claim_no').value;// encodeURI(code)
	var url = globalContextPath + "/claim/oldPart/ClaimOldPartOutStorageManager/printPartCode.do?code="
			+ code + "&dcode=" + dcode + "&partCode=" + partCode + "&claimNo="
			+ claimNo + "&name=" + encodeURI(name) + "&dname="
			+ encodeURI(dname) + "&partName=" + encodeURI(partName);
	window.open(url,"关联件打印","height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");

}
function getNo() {
	var url = globalContextPath + "/claim/oldPart/ClaimOldPartOutStorageManager/getNo.json";
	makeNomalFormCall(url, succ, 'fm', '');
}
function succ(json) {
	var list = json.relationOutNo;
	var len = list.length;

	var relationOutNo = document.getElementsByName("relationOutNo");
	relationOutNo[0].options.length = 0;
	var varItem3 = new Option("---请选择---", "-1");
	relationOutNo[0].options.add(varItem3);
	for (var i = 0; i < len; i++) {
		varItem3 = new Option(list[i].OUT_NO, list[i].OUT_NO);
		relationOutNo[0].options.add(varItem3);
	}
}
