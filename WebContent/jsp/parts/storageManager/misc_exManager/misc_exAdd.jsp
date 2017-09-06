<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script type=text/javascript>
$(function(){
	$('#partDiv').css('min-width', $('#file').width()-40);
});
var myPage;
var url = "<%=contextPath%>/parts/storageManager/miscManager/Misc_exManager/showPartBase.json";
var title = null;

var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'PART_ID', align: 'center', width: '33px', renderer: seled},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: left;'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: left;'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align: left;'},
    {header: "单位", dataIndex: 'UNIT', align: 'center'},
    {header: "批次", dataIndex: 'BATCH_NO', align: 'center'},
    {header: "货位编码", dataIndex: 'LOC_CODE', align: 'center'},
    {header: "可用数量", dataIndex: 'NORMAL_QTY', align: 'center'},
    {header: "出库数量", dataIndex: 'PART_ID', align: 'center', renderer: returnText}
];

var tidx = 0;
var tidxn = "";
function seled(value, meta, record) {
	tidx++;
	tidxn = record.data.PART_ID + '_RNUM'+tidx;
    var loc = record.data.PART_ID;
	    loc += "," + record.data.LOC_ID;
	    loc += "," + record.data.LOC_CODE;
	    loc += "," + record.data.LOC_NAME;
	    loc += "," + record.data.BATCH_NO;
    var html = "<input type='checkbox' value='" + tidxn + "' name='ck' id='ck_" + tidxn + "' />";
    	html += '<input type="hidden" id="PART_OLDCODE_'+tidxn+'" value="'+record.data.PART_OLDCODE+'"/>';
    	html += '<input type="hidden" id="PART_CNAME_'+tidxn+'" value="'+record.data.PART_CNAME+'"/>';
    	html += '<input type="hidden" id="PART_CODE_'+tidxn+'" value="'+record.data.PART_CODE+'"/>';
    	html += '<input type="hidden" id="UNIT_'+tidxn+'" value="'+record.data.UNIT+'"/>';
    	html += '<input type="hidden" id="VENDER_ID_'+tidxn+'" value="'+record.data.STOCK_VENDER_ID+'"/>';
    	html += '<input type="hidden" id="BATCH_NO_'+tidxn+'" value="'+record.data.BATCH_NO+'"/>';
    	html += '<input type="hidden" id="LOC_CODE_'+tidxn+'" value="'+record.data.LOC_CODE+'"/>';
    	html += '<input type="hidden" id="LOC_'+tidxn+'" value="'+loc+'"/>';
    	html += '<input type="hidden" id="NORMAL_QTY_'+tidxn+'" value="'+record.data.NORMAL_QTY+'"/>';
    return html;
}

// 出库数量
function returnText(value, meta, record) {
    return "<input class='short_txt' type='text' value='' id='Num_" + tidxn + "' maxlength='10' onchange='dataCheck(this, \"" + tidxn + "\")'/>";
}
// 验证出库数量
function dataCheck(obj, partId) {
    var value = obj.value;
    if (isNaN(value)) {
        MyAlert("请输入数字!");
        obj.value = "";
        return;
    }
    var re = /^[1-9]+[0-9]*]*$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入正整数!");
        obj.value = "";
        return;
    }
    var normalQtyId = obj.id == "Num_"+partId ? '#NORMAL_QTY_' : '#normalQty_';
    var normalQty = $(normalQtyId+partId).val();
    if(parseInt(normalQty) < parseInt(obj.value)){
        MyAlert("出库数量不能大于可用数量!");
        obj.value = "";
        return;
    }
    if(normalQtyId == '#NORMAL_QTY_'){
	    document.getElementById("ck_" + partId).checked = true;
    }
}

function validateCell(value) {
    var flag = true;
    var tbl = document.getElementById('file');
    for (var i = 1; i <= tbl.rows.length - 2; i++) {
        if (value == document.getElementById('cell_' + i).value) {
            flag = false;
            break;
        }
    }
    return flag;
}

function selAll(obj) {
    var cks = document.getElementsByName('ck');
    for (var i = 0; i < cks.length; i++) {
        if (obj.checked) {
            cks[i].checked = true;
        } else {
            cks[i].checked = false;
        }
    }
}
function selAll2(obj) {
    var cb = document.getElementsByName('cb');
    for (var i = 0; i < cb.length; i++) {
        if (obj.checked) {
            cb[i].checked = true;
        } else {
            cb[i].checked = false;
        }
    }
}

// 添加记录
function addCells() {
	var len = $('input[name="ck"]:checked').length;
	if(len==0){
		MyAlert('请选择要添加的数据！');
		return;
	}
	var error = '';
	for(var i=0;i<len;i++){
		var partId = $('input[name="ck"]:checked').eq(i).val(); // 配件id
        var partCode = $('#PART_CODE_'+partId).val();  //件号
        var partOldcode = $('#PART_OLDCODE_'+partId).val();  //配件编码
        var partCname = $('#PART_CNAME_'+partId).val();  //配件名称
        var unit = $('#UNIT_'+partId).val();  //配件名称
		var loc = $('#LOC_'+partId).val(); // 配件id，货位信息，批次号
		var locCode = $('#LOC_CODE_'+partId).val(); // 货位编码
		var batchNo = $('#BATCH_NO_'+partId).val(); // 批次号
		var venderId = $('#VENDER_ID_'+partId).val(); // 批次号
		var normalQty = $('#NORMAL_QTY_'+partId).val(); // 可用库存
		var inQty = $('#Num_'+partId).val(); // 出库数量
		
		var leng = $('input[name="cb"]').length;
		var flag = true;
		var index = 0; 
		for(var k=0;k<leng;k++){
			//判断重复
			var partIdk = $('input[name="cb"]').eq(k).val();
			var locInfo = $('#loc_'+partIdk).val();
			if(locInfo==loc){
				flag = false;
				index = $('input[name="ck"]:checked').eq(i).parent().parent().index();
			}
		}
        
		if(flag){
			//不重复，写入保存信息块
			var rsStr = '<input type="checkbox" checked="checked" name="cb" value="'+partId+'" />';
				rsStr += '<input type="hidden" id="partCode_'+partId+'" name="partCode_'+partId+'" value="'+partCode+'">';
				rsStr += '<input type="hidden" id="partOldcode_'+partId+'" name="partOldcode_'+partId+'" value="'+partOldcode+'">';
				rsStr += '<input type="hidden" id="partCname_'+partId+'" name="partCname_'+partId+'" value="'+partCname+'">';
				rsStr += '<input type="hidden" id="unit_'+partId+'" name="unit_'+partId+'" value="'+unit+'">';
				rsStr += '<input type="hidden" id="loc_'+partId+'" name="loc_'+partId+'" value="'+loc+'">';
				rsStr += '<input type="hidden" id="venderId_'+partId+'" name="venderId_'+partId+'" value="'+venderId+'">';
				rsStr += '<input type="hidden" id="normalQty_'+partId+'" name="normalQty_'+partId+'" value="'+normalQty+'">';
				
			var str = '<tr id="delete_'+partId+'" class="delete_claszzall">';
			    str += '<td>'+rsStr+'</td>';
			    str += '<td class="my_xh"></td>';
			    str += '<td>'+partOldcode+'</td>';
			    str += '<td class="cname_'+partId+'">'+partCname+'</td>';
			    str += '<td>'+partCode+'</td>';
			    str += '<td>'+unit+'</td>';
			    str += '<td>'+locCode+'</td>';
			    str += '<td>'+batchNo+'</td>';
			    str += '<td>'+normalQty+'</td>';
			    str += '<td><input type="text" class="short_txt" value="'+inQty+'" name="inQty_'+partId+'" id="inQty_'+partId+'" maxlength="10" onchange="dataCheck(this, \''+partId+'\')"/></td>';
			    str += '<td><input type="button" class="u-button" value="删除" onclick="deleteRow(\'delete_'+partId+'\')"></td>';
			    str += '</tr>';
			$('#file').append(str);
		}else{
			//重复，写入提示信息
            MyAlert("第" + index + "行配件：" + partOldcode + " 已存在!</br>");
            break;
		}
	}

	if(error!=''){
		MyAlert(error);
	}
	
	setXhDom();//写入序号元素
	setStyleDom();//写入行样式元素
}

//删除行
function deleteRow(id){
	$('#'+id).remove();
	setXhDom();//重写序号
	setStyleDom();
}

//删除所有已添加的明细
function deleteTblAll() {
    var tbl = document.getElementById('file');
    var count = tbl.rows.length;
    for (var i = count - 1; i > 0; i--) {
        tbl.deleteRow(i);
    }
}

//写入序号元素
function setXhDom(){
	var leng = $('.my_xh').length;
	for(var i=0;i<leng;i++){
		$('.my_xh').eq(i).html(i+1);
	}
}


//写入行样式元素
function setStyleDom(){
	var leng = $('input[name="ck"]').length;
	
	for(var i=0;i<leng;i++){
		/* var trclass="";
		if((i+1)%2==0){
			trclass = 'table_list_row2';
		}else{
			trclass = 'table_list_row1';
		} */
		var ckId = $('input[name="ck"]').eq(i).val();
		$('#delete_'+ckId).removeClass('table_list_row2');
		$('#delete_'+ckId).removeClass('table_list_row1');
		// $('#delete_'+ckId).addClass(trclass);
		$('#applyQty'+ckId).attr('tabindex',(i+1));//按tab快捷到下一个输入框
		var kyQty = $('#kyQty'+ckId).val();
		if($('#applyQty'+ckId).val()==''){
			$('#applyQty'+ckId).val(kyQty);//可用=申请
		}
		
	}
}

function addPartDiv() {
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    var whValue = document.getElementById("whId").value;
    if ("增 加" == addPartViv.value) {
        if ("" == whValue) {
            MyAlert("请先选择仓库！");
            return false;
        }
    }
    if (partDiv.style.display == "block") {
        addPartViv.value = "增 加";
        partDiv.style.display = "none";
    } else {
        addPartViv.value = "收 起";
        partDiv.style.display = "block";
        __extQuery__(1);
    }
}

//仓库变化
function WHChanged() {
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    var whValue = document.getElementById("whId").value;

    deleteTblAll();

    if ("" == whValue && "收 起" == addPartViv.value) {
        addPartViv.value = "增 加";
        partDiv.style.display = "none";
    }
    if ("" != whValue && "收 起" == addPartViv.value) {
        __extQuery__(1);
    }
}

//保存确认
function miscSaveConfirm() {
    if (!validateFm()) {
        return;
    }
    MyConfirm('确定保存该单据?', miscSave, []);
}

function validateFm() {
    var msg = "";
    if (document.getElementById("textarea1").value == "") {
        msg += "请填写备注!</br>";
    }
    if (document.getElementById("whId").value == "") {
        msg += "请选择仓库!</br>";
    }
    if ('${dataMap.orgId}' == '${dataMap.OEM}' && document.getElementById("EX_TYPE").value == "") {
        msg += "请选择出库类型!</br>";
    }
    var cb = document.getElementsByName("cb");
    if (cb.length <= 0) {
        msg += "请添加配件明细!</br>";
    }
    /* if ('${dataMap.orgId}' == '${dataMap.OEM}' && document.getElementById("department").value == "") {
        msg += "请选择部门!</br>";
    } */
    var ary = new Array();
    var l = cb.length;
    for (var i = 0; i < l; i++) {
        if (cb[i].checked) {
            var partId = cb[i].value;
            ary.push(partId);
        }
    }

    //提交时,将其属性设置成DISABLED,从而达到过滤选择的目的
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            cb[i].disabled = false;
            //需要校验计划量是否填写
            if (document.getElementById("inQty_" + cb[i].value).value == "") {
                msg += "请填写第" + (i + 1) + "行的出库数量!</br>";
            }
        } else {
            cb[i].disabled = true;
        }
    }

    var s = ary.join(",") + ",";
    var pflag = false;
    var nclass = "";
    var sid = "";
    for (var i = 0; i < ary.length; i++) {
        $(".cname_" + ary[i]).parent("td").css({background: ""});
    }
    for (var i = 0; i < ary.length; i++) {
        if (s.replace(ary[i] + ",", "").indexOf(ary[i] + ",") > -1) {
            pflag = true;
            sid = "partCname_" + ary[i];
            nclass = "cname_" + ary[i];
            var partCname = document.getElementById(sid).value;
            MyAlert("配件：" + partCname + " 被重复上传!");
            break;
        }
    }
    if (pflag) {
        $("." + nclass).parent("td").css({background: "red"});
        return false;
    }

    if (msg != "") {
        MyAlert(msg);
        for (var i = 0; i < cb.length; i++) {
            cb[i].disabled = false;
        }
        return false;
    }
    return true;
}

//保存杂项出库单
function miscSave() {
    disableAllClEl();
    var url = "<%=contextPath%>/parts/storageManager/miscManager/Misc_exManager/MiscSave.json";
    sendAjax(url, getResult, 'fm');
}

function getResult(jsonObj) {
    enableAllClEl();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;

        if (null != error && error.length > 0) {
            MyAlert(error);
        }
        else if (null != success && success.length > 0) {
            MyAlert(success, function(){
	            window.location = "<%=contextPath%>/parts/storageManager/miscManager/Misc_exManager/init.do";
            });
        }
        else {
            MyAlert(exceptions.message);
        }
    }
}

//返回
function miscBack() {
    fm.action = "<%=contextPath%>/parts/storageManager/miscManager/Misc_exManager/init.do";
    fm.submit();
}

function disableAllClEl() {
    disableAllA();
    disableAllBtn();
}
function enableAllClEl() {
    enableAllBtn();
    enableAllA();
}

function disableAllA() {
    var inputArr = document.getElementsByTagName("a");
    for (var i = 0; i < inputArr.length; i++) {
        inputArr[i].disabled = true;
    }
}

function enableAllA() {
    var inputArr = document.getElementsByTagName("a");
    for (var i = 0; i < inputArr.length; i++) {
        inputArr[i].disabled = false;
    }
}
function disableAllBtn() {
    var inputArr = document.getElementsByTagName("input");
    for (var i = 0; i < inputArr.length; i++) {
        if (inputArr[i].type == "button") {
            inputArr[i].disabled = true;
        }
    }
}
function enableAllBtn() {
    var inputArr = document.getElementsByTagName("input");
    for (var i = 0; i < inputArr.length; i++) {
        if (inputArr[i].type == "button") {
            if (inputArr[i].id == "rep" && document.getElementById("isBatchSo").value ==<%=Constant.PART_BASE_FLAG_YES%>) {
                continue;
            }
            inputArr[i].disabled = false;
        }
    }
}

function showUpload() {
    var uploadDiv = document.getElementById("uploadDiv");
    if (uploadDiv.style.display == "block") {
        uploadDiv.style.display = "none";
    } else {
        uploadDiv.style.display = "block";
    }
}

function exportExcelTemplate() {
    fm.action = "<%=contextPath%>/parts/storageManager/miscManager/Misc_exManager/exportExcelTemplate.do";
    fm.submit();
}

//上传检查和确认信息
function confirmUpload() {
    if (fileVilidate()) {
        MyConfirm("确定上传选中的文件?", uploadExcel, []);
    }
}

//上传
function uploadExcel() {
    btnDisable();
    fm.action = "<%=contextPath%>/parts/storageManager/miscManager/Misc_exManager/uploadExcel.do";
    fm.submit();
    ;
}

function fileVilidate() {
    var msg = "";
    if (document.getElementById("whId").value == "") {
        msg += "请先选择仓库!</br>";
    }
    if (msg != "") {
        MyAlert(msg);
        return false;
    }
    var importFileName = $("uploadFile").value;
    if (importFileName == "") {
        MyAlert("请选择上传文件!");
        return false;
    }
    var index = importFileName.lastIndexOf(".");
    var suffix = importFileName.substr(index + 1, importFileName.length).toLowerCase();
    if (suffix != "xls" && suffix != "xlsx") {
        MyAlert("请选择Excel格式文件!");
        return false;
    }
    return true;
}

function uploadExcelssss() {
    if ($('whId').value == "") {
        MyAlert("请先选择仓库!");
        return;
    }
    var url = "<%=contextPath%>/parts/storageManager/miscManager/Misc_exManager/uploadExcel.json";
    sendAjax(url, getUploadResult, 'fm');
}
function getUploadResult(jsonObj) {
    if (jsonObj != null) {
        var obj = jsonObj.partData;
        if (!obj) {
            MyAlert(jsonObj.error);
            return;
        }
        var error = obj.error;
        var msg = "";
        if (error != "") {
            msg += '<font color="RED">' + error + '</font>';
        }
        var data = obj.dataList;
        for (var i = 0; i < data.length; i++) {
            var tbl = document.getElementById('file');
            var flag = true;
            for (var j = 0; j < tbl.rows.length; j++) {
                if (data[i].PART_ID == tbl.rows[j].cells[0].firstChild.value) {
                    msg += "第" + (j) + "行配件：" + data[i].PART_CNAME + " 已存在!</br>";
                    flag = false;
                    break;
                }
            }
            if (flag) {
                addCell(data[i].PART_ID, data[i].PART_CODE, data[i].PART_OLDCODE, data[i].PART_CNAME, data[i].UNIT == null ? "" : data[i].UNIT, data[i].IN_QTY == null ? "" : data[i].IN_QTY);
                document.getElementById("inQty_" + data[i].PART_ID).value = data[i].inQty;
            }
        }
        if ("" != msg) {
            MyAlert(msg);
        }
    }
}
function check(obj) {
    if (isNaN(obj.value)) {
        MyAlert("请录入正整数！");
        obj.value = obj.value.replace(/\D/g, '');
        obj.focus();
    }
}

</script>
</head>
<body>
	<div class="wbox">
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<input name="orderId" id="orderId" type="hidden" value="${dataMap.orderId}" />
			<input name="orderCode" id="orderCode" type="hidden" value="${dataMap.orderCode}" />
			<input name="orgId" id="orgId" type="hidden" value="${dataMap.orgId}" />
			<input name="orgCode" id="orgCode" type="hidden" value="${dataMap.orgCode}" />
			<input name="orgName" id="orgName" type="hidden" value="${dataMap.orgName}" />

			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 配件仓库管理 &gt; 杂项出库(新增)
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/img/subNav.gif" /> 杂项出库信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">杂项出库单号：</td>
							<td>${dataMap.orderCode}</td>
							<td class="right">出库日期：</td>
							<td>${dataMap.now}</td>
							<td class="right">制单人：</td>
							<td>${dataMap.userName}</td>
						</tr>
						<tr>
							<td class="right">仓库：</td>
							<td align="left">
								<select name="whId" id="whId" style='width: 150px;' onchange="WHChanged()" class="u-select">
									<c:forEach items="${wareHouseList}" var="wareHouse">
										<option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
									</c:forEach>
									<font color="RED">*</font>
							</td>
						</tr>
						<c:choose>
							<c:when test="${dataMap.orgId eq dataMap.OEM}">
								<tr>
									<td class="right">出库类型：</td>
									<td>
										<script type="text/javascript">
											genSelBoxExp("EX_TYPE",
										<%=Constant.MISC_EX_TYPE%>
											, "", "true", "", "", "false", '');
										</script>
										<font color="RED">*</font>
									</td>
									<%-- <td class="right">部门:</td>
        <td>
            <select name="department" id="department" style='width:150px;'>
                <option selected value=''>-请选择-</option>
                <c:forEach items="${departmentList}" var="departmentList">
                    <option value="${departmentList.DEPARTMENT_CODE}">${departmentList.DEPARTMENT_NAME}</option>
                </c:forEach>
            </select><font color="RED">*</font>
        </td> --%>
									<td></td>
									<td></td>
							</c:when>
						</c:choose>
						</tr>
						<tr>
							<td class="right">备注：</td>
							<td colspan="5">
								<textarea class="form-control" style="width: 80%;" name="textarea1" id="textarea1" rows="3"></textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<table id="file" class="table_list" style="border-bottom: 1px solid #DAE0EE">
				<caption><img class="nav" src="<%=contextPath%>/img/subNav.gif" />配件信息</caption>
				<tr>
					<th align="center">
						<input type="checkbox" checked name="ckAll" id="ckAll" onclick="selectAll()" />
					</th>
					<th align="center">序号</th>
					<th align="center">配件编码</th>
					<th align="center">配件名称</th>
					<th align="center">配件件号</th>
					<th align="center">单位</th>
					<th align="center">货位编码</th>
					<th align="center">批次</th>
					<th align="center">可用数量</th>
					<th align="center">
						出库数量<font color="RED">*</font>
					</th>
					<th align="center">操作</th>
				</tr>
			</table>
			<table border="0" class="table_query">
				<tr>
					<td class="center">
						<input class="u-button" type="button" value="保 存" onclick="miscSaveConfirm();">
						&nbsp;
						<input class="u-button" type="button" value="返 回" onclick="miscBack()" />
						&nbsp;
					</td>
				</tr>
			</table>
			<FIELDSET>
				<LEGEND style="MozUserSelect: none; KhtmlUserSelect: none" unselectable="on">
					<th colspan="6">
						<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" /> 配件库存查询
						<input type="button" class="u-button" name="addPartViv" id="addPartViv" value="增 加" onclick="addPartDiv()" />
					</th>
				</LEGEND>
				<div style="display: none; heigeht: 5px" id="partDiv">
					<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
						<tr>
							<td class="right">配件编码：</td>
							<td>
								<input class="middle_txt" id="partOldcode" datatype="1,is_noquotation,30" name="partOldcode" type="text" />
							</td>
							<td class="right">配件名称：</td>
							<td>
								<input class="middle_txt" id="partCname" datatype="1,is_noquotation,30" name="partCname" type="text" />
							</td>
							<td class="right">件号：</td>
							<td>
								<input class="middle_txt" id="partCode" datatype="1,is_noquotation,30" name="partCode" type="text" />
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input class="u-button" type="button" name="BtnQuery" id="queryBtn" value="查 询" onclick="__extQuery__(1)" />
								<input class="u-button" type="button" name="BtnQuery" id="queryBtn" value="添 加" onclick="addCells()" />
							</td>
						</tr>
					</table>
					<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
					<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
				</div>
			</FIELDSET>
		</form>
	</div>
</body>
</html>
