<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>配件销售价格变更申请</title>
</head>
<script type="text/javascript">
var myPage;
var url = "<%=contextPath%>/parts/baseManager/partSalePrice/PartSalePriceChange/queryPartSalePrice.json";
var title = null;
var columns = [
    {header: "序号", style: 'text-align:left', renderer: getIndex},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'APPLY_NO', renderer: myLink, align: 'center'},
    {header: "变更申请号", dataIndex: 'APPLY_NO', style: 'text-align:left'},
    {header: "有效期从", dataIndex: 'VALID_FROM', style: 'text-align:center'},
    {header: "有效期到", dataIndex: 'VALID_TO', style: 'text-align:center'},
    {header: "创建日期", dataIndex: 'CREATE_DATE', style: 'text-align:center'},
    {header: "创建人", dataIndex: 'NAME', style: 'text-align:left'},
    {header: "备注", dataIndex: 'REMARK', style: 'text-align:center'},
    {header: "状态", dataIndex: 'STATE', style: 'text-align:center', renderer: getItemValue}
];

function loadPartPriceExcel(chgId, flag) {
    document.getElementById("chgId").value = chgId;
    document.getElementById("flag").value = flag;
    fm.action = "<%=contextPath%>/parts/baseManager/partSalePrice/PartSalePriceChange/exportPartPriceExcel.do?";
    fm.submit();
}

function loadExcelIntoDB() {
    var filevalue = fm.uploadFile.value;
    if (filevalue == '') {
        MyAlert('导入文件不能空!');
        return false;
    }
    var fi = filevalue
            .substring(filevalue.length - 3, filevalue.length);
    if (fi != 'xls') {
        MyAlert('导入文件格式不对,请导入xls文件格式');
        return false;
    }
    MyConfirm("确认导入选中文件? 确定后请耐心等待...", conmmitFile, [])

}

function conmmitFile() {
    fm.action = "<%=contextPath%>/parts/baseManager/partSalePrice/PartSalePriceChange/loadPartPriceDataIntoDB.do";
    fm.submit();
}
function myLink(value, meta, record) {
    var text = "<a href=\"#\" onclick='lookDetail(\"" + value + "\")'>[查看]</a>";
    text = text + "<a href=\"#\" onclick='save(\"" + value + "\")'>[提交]</a>";
    text = text + "<a href=\"#\" onclick='cancelApply(\"" + value + "\")'>[作废]</a>";
    var exportUrl = "<a href=\"#\" onclick='loadPartPriceExcel(\"" + value + "\",\"" + 2 + "\")'>[明细导出]</a>";
    if (record.data.STATE == '<%=Constant.PART_PRICE_CHG_STATE_01%>' || record.data.STATE == '<%=Constant.PART_PRICE_CHG_STATE_04%>') {
        return String.format(text + exportUrl);
    } else {
        return String.format("<a href=\"#\" onclick='lookDetail(\"" + value + "\")'>[查看]</a>" + exportUrl);
    }


}

//查看
function lookDetail(value) {
    var url_u = g_webAppName + "/parts/baseManager/partSalePrice/PartSalePriceChange/lookDetailInit.do?APPLY_NO=" + value;
    OpenHtmlWindow(url_u, 1300, 550);
}
//作废
function cancelApply(value) {
	MyConfirm("确认作废配件价格变更申请？", function(){
        var saveUrl = g_webAppName + "/parts/baseManager/partSalePrice/PartSalePriceChange/cancelPartSalePrice.json?APPLY_NO=" + value;
        makeNomalFormCall(saveUrl, forback2, 'fm');
	});
}
function forback2(jsonObj) {
    var success = jsonObj.success;
    if (success.length > 0) {
        MyAlert(success);
        window.location.reload();
    } else {
        MyAlert("配件价格变更申请作废失败,请联系管理员!");
    }
}
//提交
function save(value) {
	MyConfirm("确认提交配件价格变更申请？", function(){
        var saveUrl = g_webAppName + "/parts/baseManager/partSalePrice/PartSalePriceChange/savePartSalePrice.json?APPLY_NO=" + value;
        makeNomalFormCall(saveUrl, veiwPrices, 'fm');
	});
}
function veiwPrices(jsonObj) {
    var success = jsonObj.success;
    if (success.length > 0) {
        MyAlert(success);
        __extQuery__(1);
    }
    else {
        MyAlert("配件价格变更申请提交失败,请联系管理员!");
    }
}
function addApply() {
    window.location.href = g_webAppName + "/parts/baseManager/partSalePrice/PartSalePriceChange/addApply.do";
}
//插入文本框SalePrice1
function insertInputSalePrice1(value, meta, record) {

    var output = '<input type="text" class="short_txt" onchange="checkNumberLength(this,8,2)" id="SALE_PRICE1' + '+' + record.data.PRICE_ID + '"' + 'id="SALE_PRICE1' + '+' + record.data.PRICE_ID + '"'
            + ' value="' + value + '" size ="10" style="background-color:#FF9;text-align:right;text-align:right" datatype="1,is_double,10"   decimal="2">';
    return output;
}

//插入文本框SalePrice2
function insertInputSalePrice2(value, meta, record) {

    var output = '<input type="text" class="short_txt" onchange="checkNumberLength(this,8,2)" id="SALE_PRICE2' + '+' + record.data.PRICE_ID + '"'
            + ' value="' + value + '" size ="10" style="background-color:#FF9;text-align:right" datatype="1,is_double,10"  decimal="2">';
    return output;
}
//插入文本框SalePrice3
function insertInputSalePrice3(value, meta, record) {

    var output = '<input type="text" class="short_txt" onchange="checkNumberLength(this,8,2)" id="SALE_PRICE3' + '+' + record.data.PRICE_ID + '"'
            + ' value="' + value + '" size ="10" style="background-color:#FF9;text-align:right" datatype="1,is_double,10"  decimal="2">';
    return output;
}

//插入文本框SalePrice4
function insertInputSalePrice4(value, meta, record) {

    var output = '<input type="text" class="short_txt" onchange="checkNumberLength(this,8,2)" id="SALE_PRICE4' + '+' + record.data.PRICE_ID + '"'
            + ' value="' + value + '" size ="10" style="background-color:#FF9;text-align:right" datatype="1,is_double,10"  decimal="2">';
    return output;
}
//插入文本框SalePrice5
function insertInputSalePrice5(value, meta, record) {

    var output = '<input type="text" class="short_txt" onchange="checkNumberLength(this,8,2)" id="SALE_PRICE5' + '+' + record.data.PRICE_ID + '"'
            + ' value="' + value + '" size ="10" style="background-color:#FF9;text-align:right" datatype="1,is_double,10"  decimal="2">';
    return output;
}
//插入文本框SalePrice6
function insertInputSalePrice6(value, meta, record) {

    var output = '<input type="text" class="short_txt" onchange="checkNumberLength(this,8,2)" id="SALE_PRICE6' + '+' + record.data.PRICE_ID + '"'
            + ' value="' + value + '" size ="10" style="background-color:#FF9;text-align:right" datatype="1,is_double,10"  decimal="2">';
    return output;
}
//插入文本框SalePrice7
function insertInputSalePrice7(value, meta, record) {

    var output = '<input type="text" class="short_txt" onchange="checkNumberLength(this,8,2)" id="SALE_PRICE7' + '+' + record.data.PRICE_ID + '"'
            + ' value="' + value + '" size ="10" style="background-color:#FF9;text-align:right" datatype="1,is_double,10"  decimal="2">';
    return output;
}
//插入文本框SalePrice8
function insertInputSalePrice8(value, meta, record) {

    var output = '<input type="text" class="short_txt" onchange="checkNumberLength(this,8,2)" id="SALE_PRICE8' + '+' + record.data.PRICE_ID + '"'
            + ' value="' + value + '" size ="10" style="background-color:#FF9;text-align:right" datatype="1,is_double,10"  decimal="2">';
    return output;
}
//插入文本框SalePrice9
function insertInputSalePrice9(value, meta, record) {

    var output = '<input type="text" class="short_txt" onchange="checkNumberLength(this,8,2)" id="SALE_PRICE9' + '+' + record.data.PRICE_ID + '"'
            + ' value="' + value + '" size ="10" style="background-color:#FF9;text-align:right" datatype="1,is_double,10"  decimal="2">';
    return output;
}
//插入文本框SalePrice10
function insertInputSalePrice10(value, meta, record) {

    var output = '<input type="text" class="short_txt" onchange="checkNumberLength(this,8,2)" id="SALE_PRICE10' + '+' + record.data.PRICE_ID + '"'
            + ' value="' + value + '" size ="10" style="background-color:#FF9;text-align:right" datatype="1,is_double,10"  decimal="2">';
    return output;
}
//插入文本框SalePrice11
function insertInputSalePrice11(value, meta, record) {

    var output = '<input type="text" class="short_txt" onchange="checkNumberLength(this,8,2)" id="SALE_PRICE11' + '+' + record.data.PRICE_ID + '"'
            + ' value="' + value + '" size ="10" style="background-color:#FF9;text-align:right" datatype="1,is_double,10"  decimal="2">';
    return output;
}
//插入文本框SalePrice12
function insertInputSalePrice12(value, meta, record) {

    var output = '<input type="text" class="short_txt" onchange="checkNumberLength(this,8,2)" id="SALE_PRICE12' + '+' + record.data.PRICE_ID + '"'
            + ' value="' + value + '" size ="10" style="background-color:#FF9;text-align:right" datatype="1,is_double,10"  decimal="2">';
    return output;
}
//插入文本框SalePrice13
function insertInputSalePrice13(value, meta, record) {

    var output = '<input type="text" class="short_txt"  onchange="checkNumberLength(this,8,2)" id="SALE_PRICE13' + '+' + record.data.PRICE_ID + '"'
            + ' value="' + value + '" size ="10" style="background-color:#FF9;text-align:right" datatype="1,is_double,10"  decimal="2">';
    return output;
}
//插入文本框SalePrice14
function insertInputSalePrice14(value, meta, record) {

    var output = '<input type="text" class="short_txt" onchange="checkNumberLength(this,8,2)" id="SALE_PRICE14' + '+' + record.data.PRICE_ID + '"'
            + ' value="' + value + '" size ="10" style="background-color:#FF9;text-align:right" datatype="1,is_double,10"  decimal="2">';
    return output;
}
//插入文本框SalePrice15
function insertInputSalePrice15(value, meta, record) {

    var output = '<input type="text" class="short_txt" onchange="checkNumberLength(this,8,2)" id="SALE_PRICE15' + '+' + record.data.PRICE_ID + '"'
            + ' value="' + value + '" size ="10" style="background-color:#FF9;text-align:right" datatype="1,is_double,10"  decimal="2">';
    return output;
}

//验证最大长度指定的正小数,inputObj为input对象，beforeLength为小数点前面的位数个数，afterLength为小数点后面的位数个数
function checkNumberLength(inputObj, beforeLength, afterLength) {
    if (inputObj.value.indexOf(".") >= 0) {
        //var regex = new RegExp("/^[0-9]{0," + beforeLength + "}.[0-9]{0," + afterLength + "}$/");  +\.[0-9]{2})[0-9]*
        var regex = new RegExp("/^[0-9]{0,10}.\\d{0,2}$/");
        if (regex.test(inputObj.value) == true) {
            MyAlert("请录入正确的采购金额，且小数保留精度最大为" + afterLength + "位!");
            inputObj.value = "";
            inputObj.focus();
        } else {
            var re = /([0-9]+\.[0-9]{2})[0-9]*/;
            inputObj.value = inputObj.value.replace(re, "$1");
        }
    }
    else {
        var re = "/^\d{" + beforeLength + "}$/";
        if (re.test(inputObj.value)) {
            MyAlert("只能是数字，且纯数字不能超过或等于" + beforeLength + "位!");
            inputObj.value = "";
            inputObj.focus();
        } else {

        }
    }
}
function enableAllStartBtn() {
    var inputArr = document.getElementsByTagName("input");
    for (var i = 0; i < inputArr.length; i++) {
        if (inputArr[i].type == "button") {
            inputArr[i].disabled = false;
        }
    }
}
function showUpload() {
    if ($("uploadTable").style.display == "none") {
        $("uploadTable").style.display = "block";
    } else {
        $("uploadTable").style.display = "none";
    }
}
//下载
function loadExcelTemplate() {
    document.fm.action = "<%=contextPath%>/parts/baseManager/partSalePrice/PartSalePriceChange/exportExcelTemplate.do";
    document.fm.target = "_self";
    document.fm.submit();
}
$(function(){__extQuery__(1);});
</script>

<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 技术相关信息维护 &gt; 配件价格变更申请
		</div>
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<input type="hidden" name="curPage" id="curPage" value="${curPage}" />
			<input type="hidden" name="chgId" id="chgId" value="0" />
			<input type="hidden" name="flag" id="flag" value="1" />
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">变更申请号：</td>
							<td>
								<input class="middle_txt" type="text" name="APPLY_NO" />
							</td>
							<td class="right">变更申请时间:</td>
							<td>
								<input id="checkSDate" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" jset="para" style="width:80px;" value="${old }"/>
								<input class="time_ico" value=" " type="button" />
								&nbsp;至&nbsp;
								<input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" jset="para" style="width:80px;" value="${now}" />
								<input class="time_ico" value=" " type="button" />
							</td>
							<td class="right">
								<span>状态：</span>
							</td>
							<td>
								<script type="text/javascript">
                        			genSelBox("state", <%=Constant.PART_PRICE_CHG_STATE%>, <%=Constant.PART_PRICE_CHG_STATE_01%>, true, "");
								</script>
							</td>

						</tr>
						<tr>
							<td class="center" colspan="6">
								<input class="u-button" type="button" value="查 询" onclick="__extQuery__(1);" />
								<input class="u-button" type="button" value="重 置" onclick="reset();" />
								<input class="u-button" type="button" value="新 增" onclick="addApply();" />
								<input class="u-button" type="button" value="导 出" onclick="loadPartPriceExcel('0','1');" />
								<input class="u-button" type="button" value="批量导入" onclick="showUpload();" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<table class="table_edit" id="uploadTable" style="display: none">
				<tr>
					<td>
						<input type="button" class="normal_btn" onclick="loadExcelTemplate()" value="模版下载" />
						<font color="red">文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font>
						<input type="file" name="uploadFile" style="width: 250px" id="uploadFile" value="" />
						&nbsp;
						<input type="button" class="normal_btn" onclick="loadExcelIntoDB()" value="确定" />
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</div>
	</form>
</body>
</html>
