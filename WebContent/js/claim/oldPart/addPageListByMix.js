var myPage;
// 查询路径
var url = globalContextPath+"/claim/oldPart/ClaimOldPartOutStorageManager/queryPreOutStoreListByMix.json";

var title = null;

var columns = [
		{header : "序号",align : 'center',renderer : getIndex},
		{id : 'action',header : "全选<input type='checkbox' id='checkAll' name='checkAll' onclick='selectAll(this)'>"
				,width : '8%',sortable : false,dataIndex : 'id',renderer : myCheckBox},
		{header : "配件代码",dataIndex : 'PART_CODE',	align : 'center',renderer : myLink_3},
		{header : "配件名称",	dataIndex : 'PART_NAME',align : 'center'},
		{header : "供应商代码",dataIndex : 'SUPPLY_CODE',	align : 'center'},
		{header : "供应商名称",dataIndex : 'SUPPLY_NAME',align : 'center'	},
		{header : "实件库存数",dataIndex : 'RETRUN_AMOUNT',align : 'center',renderer : myLink_1},
		{header : "关联次因件数量",dataIndex : 'COUNTPART',align : 'center',renderer : return_link},
		{header : "实件库存数填写",dataIndex : 'RETRUN_AMOUNT',align : 'center',renderer : myLink_1_1},
		{header : "无件库存数",dataIndex : 'NO_RETURN_AMOUNT',align : 'center',renderer : myLink_2},
		{header : "无件库存数填写",dataIndex : 'NO_RETURN_AMOUNT',align : 'center',renderer : myLink_2_2}
	];

var rtId = 'return_';
var nrtId = 'no_return_';

function return_link(value, meta, record) {
	var SUPPLY_CODE = record.data.SUPPLY_CODE;
	var PART_CODE = record.data.PART_CODE;
	var url = "<a href='#' onclick='return_show(\"" + SUPPLY_CODE + "\",\""
			+ PART_CODE + "\",\"" + value + "\");'>[" + value + "]</a>";
	return String.format(url);
}
function return_show(SUPPLY_CODE, PART_CODE, num) {
	if (num != '0') {
		var url = globalContextPath+"/claim/oldPart/ClaimOldPartOutStorageManager/return_show.do?SUPPLY_CODE="
				+ SUPPLY_CODE + "&PART_CODE=" + PART_CODE;
		OpenHtmlWindow(url, 800, 500);
	}
}

function myLink_3(value, metaDate, record) {
	var url = globalContextPath+"/OutStoreAction/linkByPartCode.do?partCode="+value;
	return String.format("<a href='" + url + "'>" + value + "</a>");
}

function checkData() {
	var name = $('supply_name').value;
	var code = $('supply_code').value;
	var dname = $('dealer_name').value;
	var dcode = $('dealer_code').value;
	if (name == "" && code == "") {
		MyAlert("提示：供应商代码和简称至少输入一个!");
		return;
	}
	sendAjax(globalContextPath+'/OutStoreAction/showOutStoreNumByCondition.json',backAjax, 'fm');
}
function backAjax(json) {
	$("retrun_amount_temp").innerHTML = json.mapNum.retrun_amount;
	$("no_retrun_amount_temp").innerHTML = json.mapNum.no_return_amount;
	setCheckedFlag("0");
	__extQuery__(1);
}

function outStore() {
	if ($('OUT_CLAIM_TYPE').value == "") {
		alert("提示：请先选择出库类型！");
		return;
	}
	var checkids = document.getElementsByName('checkid');
	for (var i = 0; i < checkids.length; i++) {
		if (checkids[i].checked) {
			var partCodeId = checkids[i].parentNode.childNodes[1].value;
			var supplyCodeId = checkids[i].parentNode.childNodes[2].value;
			var return_1 = $(rtId + partCodeId + "_" + supplyCodeId).value;
			var no_return_1 = $(nrtId + partCodeId + "_" + supplyCodeId).value;
			if (return_1 == "") {
				return_1 = "0";
			}
			if (no_return_1 == "") {
				no_return_1 = "0";
			}
			if (return_1 == "0" && no_return_1 == "0") {
				alert("提示：检查选中的该行，请至少填写一个出库数！");
				return;
			} else {
				return_temp += parseInt(return_1);
				no_return_temp += parseInt(no_return_1);
			}
		}
	}
	setCheckedFlag('1');
	cusGetClickValFun();
	var paramStr = $(hideCheckedDealerId).value;
	var return_temp = 0;
	var no_return_temp = 0;
	if (paramStr) {
		var params = paramStr.split(",");
		for (var i = 0; i < params.length; i++) {
			var param = params[i].split("_");
			var return_1 = param[2];
			var no_return_1 = param[3];
			if (return_1 == "") {
				return_1 = "0";
			}
			if (no_return_1 == "") {
				no_return_1 = "0";
			}
			if (return_1 == "0" && no_return_1 == "0") {
				alert("提示：" + param[0] + "的出库数量为0,请至少填写一个出库数!");
				return;
			} else {
				return_temp += parseInt(return_1);
				no_return_temp += parseInt(no_return_1);
			}
		}
		MyConfirm("出库实件数：" + return_temp + "出库无件数：" + no_return_temp
				+ "是否确认出库？", doChangeInit, "");
	} else {
		alert("提示:请选择至少一个再出库！");
	}
}
function doChangeInit() {
	var checkids = document.getElementsByName('checkid');
	var temp = 0;
	for (var i = 0; i < checkids.length; i++) {
		if (checkids[i].checked) {
			temp++;
		}
	}
	if (temp == 0) {
		alert("提示：请选择至少一个再出库！");
		return;
	}
	if ($('OUT_CLAIM_TYPE').value == "") {
		alert("提示：请先选择出库类型！");
		return;
	}

	var params = "";
	for (var i = 0; i < checkids.length; i++) {
		if (checkids[i].checked) {
			var partCodeId = checkids[i].parentNode.childNodes[1].value;
			var supplyCodeId = checkids[i].parentNode.childNodes[2].value;
			var return_1 = $(rtId + partCodeId + "_" + supplyCodeId).value;
			var no_return_1 = $(nrtId + partCodeId + "_" + supplyCodeId).value;
			if (return_1 == "") {
				return_1 = "0";
			}
			if (no_return_1 == "") {
				no_return_1 = "0";
			}
			if (return_1 == "0" && no_return_1 == "0") {
				alert("提示：检查选中的该行，请至少填写一个出库数！");
				return;
			} else {
				params += partCodeId + "," + supplyCodeId + "," + return_1
						+ "," + no_return_1 + ";";
			}
		}
	}
	setCheckedFlag('1');
	cusGetClickValFun();
	params = $(hideCheckedDealerId).value;
	if (params != "") {
		doChange(params);
	}
}
function selectAll() {
	var checkids = document.getElementsByName('checkid');
	if ($('checkAll').checked) {
		for (var i = 0; i < checkids.length; i++) {
			checkids[i].checked = true;
		}
	} else {
		for (var i = 0; i < checkids.length; i++) {
			checkids[i].checked = false;
		}
	}
}
// 全选checkbox
function myCheckBox(value, metaDate, record) {
	var param = getCheckBoxValue(record);
	var checked = "";
	if(param){
		checked = "checked='checked'";
	}
	return String.format("<input type='checkbox' name='checkid' id='"+rtId+ record.data.PART_CODE
					+ "_"+ record.data.SUPPLY_CODE+ "_1' "+checked+"/>"
					+ "<input type='hidden'  name='partCode' value='"+ record.data.PART_CODE
					+ "' /><input type='hidden'  name='supplyCode' value='"+ record.data.SUPPLY_CODE + "' />");
}
// 获得复选框的值
function getCheckBoxValue(record){
	var paramStr = $(hideCheckedDealerId).value;
	var val = null;
	if (paramStr) {
		var params = paramStr.split(",");
		for (var i = 0; i < params.length; i++) {
			var param = params[i];
			var return_1 = param.split("_")[2];
			var no_return_1 = param.split("_")[3];
			if (return_1 == "") {
				return_1 = "0";
			}
			if (no_return_1 == "") {
				no_return_1 = "0";
			}
			if(param.indexOf(record.data.PART_CODE+ "_"+ record.data.SUPPLY_CODE)>-1){
				val = record.data.PART_CODE+ "_"+ record.data.SUPPLY_CODE+"_"+return_1+"_"+no_return_1;
				break;
			}
		}
	}
	return val;
}
/**
 * 自定义的checkbox初始化
 */
function customCheckboxInit(){
	
}

function myLink_1_1(value, metaDate, record) {
	var str = "<input type='text' name='retrun_amount_1' id='"+rtId
			+ record.data.PART_CODE + "_" + record.data.SUPPLY_CODE + "'";
	if (value == "0") {
		str += " readonly='readonly' ";
	} else {
		str += " onblur='updateReturnNum(this)' style='font-weight: bold;color: green;' ";
	}
	var param = getCheckBoxValue(record);
	var val = "";
	if(param){
		val = param.split("_")[2];
	}
	str += " class='short_txt' maxlength='5' valueTemp3='" + value
			+ "' value='"+val+"'/>";
	return String.format(str);
}
function myLink_2_2(value, metaDate, record) {
	var str = "<input type='text' name='"+nrtId+"amount_1' id='"+nrtId
			+ record.data.PART_CODE + "_" + record.data.SUPPLY_CODE + "'";
	if (value == "0") {
		str += " readonly='readonly'";
	} else {
		str += " onblur='updateNoReturnNum(this)' style='font-weight: bold;color: green;' ";
	}
	var param = getCheckBoxValue(record);
	var val = "";
	if(param){
		val = param.split("_")[3];
	}
	str += " class='short_txt' maxlength='5' valueTemp3='" + value
			+ "' value='"+val+"'/>";
	return String.format(str);
}
function updateReturnNum(obj) {
	var val = obj.attributes["valueTemp3"].nodeValue;
	var reg = new RegExp("^[0-9]*$");
	var id = obj.id + "_1";
	if (obj.value != "") {
		if (reg.test(obj.value)) {
			if (parseInt(obj.value) > parseInt(val) || parseInt(obj.value) < 0) {
				alert("提示：如果要出库请填写一个正确的数据！");
				obj.value = '';
				document.getElementById(id).checked = false;
			} else if (parseInt(obj.value) > 0) {
				document.getElementById(id).checked = true;
			} else {
				document.getElementById(id).checked = false;
			}
		} else {
			alert("提示：如果要出库请填写一个正确的数据！");
			obj.value = '';
			document.getElementById(id).checked = false;
		}
	} else {
		document.getElementById(id).checked = false;
	}
}
function updateNoReturnNum(obj) {
	var val = obj.attributes["valueTemp3"].nodeValue;
	var reg = new RegExp("^[0-9]*$");
	if (obj.value != "") {
		if (reg.test(obj.value)) {
			if (parseInt(obj.value) > parseInt(val) || parseInt(obj.value) < 0) {
				alert("提示：如果要出库请填写一个正确的数据！");
				obj.value = '';
			}
		} else {
			alert("提示：如果要出库请填写一个正确的数据！");
			obj.value = '';
		}
	}
}
function doChange(val) {
	$('save_btn').disabled = true;
	sendAjax(globalContextPath+'/claim/oldPart/ClaimOldPartOutStorageManager/doChangeByVal.json?val='
					+ val, backSucc, 'fm');
}
function backSucc(json) {
	if (json.result) {
		var jsonArray = json.result;
		if (jsonArray) {
			var info = "提示：出库失败！\n";
			var uInfo = "提示：可以强制出库，请确认！\n";
			var array = eval(jsonArray);
			var flag = true;// 是否可以出库标识
			for (var i = 0; i < array.length; i++) {
				if (array[i].flag == "0") {
					info += "配件代码:" + array[i].partCode + " 与 供应商代码:"
							+ array[i].supplyCode + "没有可匹配的关系\n";
					flag = false;
				}
				if (array[i].flag == "3") {
					info += "配件代码:" + array[i].partCode + " 与 供应商代码:"
							+ array[i].supplyCode + "没有有效的价格\n";
					flag = false;
				}
				if (array[i].flag == "2") {
					uInfo += "配件代码:" + array[i].partCode + " 与 供应商代码:"
							+ array[i].supplyCode
							+ "不存在显示关系,但与其他供应商有关系，可以强制出库\n";
				}
			}
			if (!flag) {
				alert(info);
			} else {
				MyConfirm(uInfo + ",是否确认出库？", forceOutDo, "");

			}
		}
	}
	if (json.succ == '1') {
		alert("提示：出库成功！");
		$('save_btn').disabled = false;
		var hideCheckedNode = document.getElementById(hideCheckedDealerId);
		setCheckedFlag("0");
		__extQuery__(1);
	}
}

function forceOutDo() {
	var checkids = document.getElementsByName('checkid');
	var temp = 0;
	for (var i = 0; i < checkids.length; i++) {
		if (checkids[i].checked) {
			temp++;
		}
	}
	if (temp == 0) {
		alert("提示：请先选择至少一个再出库！");
		return;
	}
	if ($('OUT_CLAIM_TYPE').value == "") {
		alert("提示：请先选择出库类型！");
		return;
	}
	var params = "";
	for (var i = 0; i < checkids.length; i++) {
		if (checkids[i].checked) {
			var partCodeId = checkids[i].parentNode.childNodes[1].value;
			var supplyCodeId = checkids[i].parentNode.childNodes[2].value;
			var return_1 = $(rtId + partCodeId + "_" + supplyCodeId).value;
			var no_return_1 = $(nrtId + partCodeId + "_" + supplyCodeId).value;
			if (return_1 == "") {
				return_1 = "0";
			}
			if (no_return_1 == "") {
				no_return_1 = "0";
			}
			if (return_1 == "0" && no_return_1 == "0") {
				alert("提示：检查选中的该行，请至少填写一个出库数！");
				return;
			} else {
				params += partCodeId + "," + supplyCodeId + "," + return_1
						+ "," + no_return_1 + ";";
			}
		}
	}
	$('save_btn').disabled = true;
	sendAjax(globalContextPath+'/claim/oldPart/ClaimOldPartOutStorageManager/doChangeByVal.json?val='
					+ val + "&out_flag=force", forceFlag, 'fm');
}
function forceFlag(json) {
	if (json.result) {
		alert(json.result);
	}
	if (json.succ == '1') {
		alert("提示：出库成功！");
		$('save_btn').disabled = false;
		var hideCheckedNode = document.getElementById(hideCheckedDealerId);
		setCheckedFlag("0");
		__extQuery__(1);
	}
}

function myLink_1(value, metaDate, record) {
	var str = "<input type='text' name='retrun_amount'  readonly='readonly' class='short_txt' maxlength='5' value='"
			+ value + "'";
	if (value != "0") {
		str += " style='font-weight: bold;color: red;'";
	}
	str += "/>";
	return String.format(str);
}
function myLink_2(value, metaDate, record) {
	var str = "<input type='text' name='"+nrtId+"amount'  readonly='readonly' class='short_txt' maxlength='5' value='"
			+ value + "'";
	if (value != "0") {
		str += " style='font-weight: bold;color: red;'";
	}
	str += "/>";
	return String.format(str);
}
function backTo() {
	var url = globalContextPath+"/claim/oldPart/ClaimOldPartOutStorageManager/queryListPage.do";
	fm.action = url;
	fm.submit();
}

function cusGetClickValFun() {
	var flagNode = document.getElementById("checkedFlag");
	if("0"==flagNode.value){
		clearCheckedValue();
		return ;
	}
	var checkids = document.getElementsByName('checkid');
	var temp = 0;
	for (var i = 0; i < checkids.length; i++) {
		if (checkids[i].checked) {
			temp++;
		}
	}
	if (temp == 0) {
		return;
	}

	var params = "";
	var hideCheckedNode = document.getElementById(hideCheckedDealerId);
	for (var i = 0; i < checkids.length; i++) {
		if (checkids[i].checked) {
			var partCodeId = checkids[i].parentNode.childNodes[1].value;
			var supplyCodeId = checkids[i].parentNode.childNodes[2].value;
			var return_1 = $(rtId + partCodeId + "_" + supplyCodeId).value;
			var no_return_1 = $(nrtId + partCodeId + "_" + supplyCodeId).value;
			if (return_1 == "") {
				return_1 = "0";
			}
			if (no_return_1 == "") {
				no_return_1 = "0";
			}
			if (return_1 == "0" && no_return_1 == "0") {
				return;
			} else {
				var param = partCodeId + "_" + supplyCodeId + "_" + return_1
						+ "_" + no_return_1;
				if (hideCheckedNode) {
					if (hideCheckedNode.value.indexOf(param) == -1) {
						hideCheckedNode.value = clearValue(
								hideCheckedNode.value + checkSep + param,
								checkSep);
					}
				}
			}
		}
	}
}

function setCheckedFlag(_flag){
	var flagNode = document.getElementById("checkedFlag");
	flagNode.value=_flag;
}
