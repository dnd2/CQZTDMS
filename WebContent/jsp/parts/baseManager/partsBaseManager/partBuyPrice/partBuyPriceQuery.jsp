<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
    String error = request.getParameter("error");
%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>配件采购价格查询</title>
<script type="text/javascript">
var myPage;
var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/queryPartBuyPriceInfo.json";
var title = null;
var columns = [
	{header: "序号",  style: 'text-align: center',renderer:getIndex},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'PRICE_ID', renderer: myLink,style: 'text-align:center'},
    {header: "配件编码", dataIndex: 'PART_OLDCODE',style: 'text-align:center'},
    {header: "配件名称", dataIndex: 'PART_CNAME',style: 'text-align:center'},
    {header: "件号", dataIndex: 'PART_CODE',style: 'text-align:center'},
    {header: "供应商编码", dataIndex: 'VENDER_CODE',style: 'text-align:center'},
    {header: "供应商名称", dataIndex: 'VENDER_NAME',style: 'text-align:center'},
//     {header: "计划价(元)", dataIndex: 'PLAN_PRICE',style: 'text-align:center'},
    {header: "采购价(元)", dataIndex: 'BUY_PRICE',style: 'text-align:center', renderer: insertInput},
    {header: "最小包装量", dataIndex: 'MIN_PACKAGE',style: 'text-align:center',},
    {header: "是否暂估", dataIndex: 'IS_GUARD', renderer: getItemValueWithSelect},
//     {header: "是否默认", dataIndex: 'IS_DEFAULT',style: 'text-align:center', renderer: getItemValue},
        {header: "采购方式", width: '10%', dataIndex: 'PRODUCE_FAC', style: "text-align: center", renderer: getItemValue},
    {header: "是否有效", dataIndex: 'STATE',style: 'text-align:center', renderer: getItemValue}

];

//设置超链接
function myLink(value, meta, record) {
    var state = record.data.STATE;
    var partId = record.data.PART_ID;
    if (state ==<%=Constant.STATUS_DISABLE %>) {
        return String.format("<a href=\"#\" onclick='changeState(" + value + "," + partId + ", "+2+")' id='sel'>[有效]</a>");
    }
    return String.format("<a href=\"#\" onclick='save(" + value + "," + partId + ")'>[保存]</a>&nbsp;<a href=\"#\" onclick='changeState(" + value + "," + partId + ", "+1+")'>[失效]</a>");
}

function query() {
    btnDisable();
    url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/queryPartBuyPriceInfo.json";
    columns = [
        {header: "序号", width: '5%', renderer: getIndex},
        {id: 'action', header: "操作", sortable: false, dataIndex: 'PRICE_ID', renderer: myLink,style: 'text-align:center'},
        {header: "配件编码", width: 113, dataIndex: 'PART_OLDCODE',style: 'text-align:center'},
        {header: "配件名称", width: '10%', dataIndex: 'PART_CNAME',style: 'text-align:center'},
        {header: "件号", width: '10%', dataIndex: 'PART_CODE',style: 'text-align:center'},
        {header: "供应商编码", width: '10%', dataIndex: 'VENDER_CODE',style: 'text-align:center'},
        {header: "供应商名称", width: '10%', dataIndex: 'VENDER_NAME',style: 'text-align:center'},
//         {header: "计划价(元)", width: '10%', dataIndex: 'PLAN_PRICE',style: 'text-align:center;'},
        {header: "采购价(元)", width: '10%', dataIndex: 'BUY_PRICE',style: 'text-align:center;', renderer: insertInput},
        {header: "最小包装量", width: '10%', dataIndex: 'MIN_PACKAGE',style: 'text-align:center',},
        {header: "是否暂估", width: '10%', dataIndex: 'IS_GUARD', renderer: getItemValueWithSelect},
//         {header: "是否默认", width: '10%', dataIndex: 'IS_DEFAULT', renderer: getItemValue},
        {header: "采购方式", width: '10%', dataIndex: 'PRODUCE_FAC', style: "text-align: center", renderer: getItemValue},
        {header: "是否有效", width: '10%', dataIndex: 'STATE', renderer: getItemValue}

    ];
    __extQuery__(1);
}

function updateRecord() {
    btnDisable();
    url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/queryPartBuyPriceHisInfo.json";
    columns = [
        {header: "序号", width: '5%', renderer: getIndex},
        {header: "配件编码", width: '10%', dataIndex: 'PART_OLDCODE',style: 'text-align:center'},
        {header: "配件名称", width: '10%', dataIndex: 'PART_CNAME',style: 'text-align:center'},
        {header: "件号", width: '10%', dataIndex: 'PART_CODE',style: 'text-align:center'},
        {header: "供应商编码", width: '10%', dataIndex: 'VENDER_CODE',style: 'text-align:center'},
        {header: "供应商名称", width: '10%', dataIndex: 'VENDER_NAME',style: 'text-align:center'},
        //{header: "制造商编码", dataIndex: 'MAKER_CODE',style: 'text-align:center'},
        //{header: "制造商名称", dataIndex: 'MAKER_NAME',style: 'text-align:center'},
        //{header: "计划价(元)", width: '10%', dataIndex: 'PLAN_PRICE',style: 'text-align:center'},
        {header: "原采购价(元)", width: '10%', dataIndex: 'OLD_BUY_PRICE',style: 'text-align:center;'},
        {header: "现采购价(元)", width: '10%', dataIndex: 'BUY_PRICE',style: 'text-align:center;'},
        {header: "是否暂估", width: '10%', dataIndex: 'IS_GUARD', style: 'text-align:center',renderer: getItemValue},
        {header: "是否有效", width: '10%', dataIndex: 'STATE', style: 'text-align:center',renderer: getItemValue},
        {header: "修改日期", width: '10%', dataIndex: 'CREATE_DATE', style: 'text-align:center'},
        {header: "修改人", width: '10%', dataIndex: 'ACNT',style: 'text-align:center'}
    ];
    __extQuery__(1);
}

function queryMaker() {
    btnDisable();
    url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/queryPartMakerInfo.json";
    columns = [
        {header: "序号", width: '5%', renderer: getIndex},
        {header: "配件编码", width: '10%', dataIndex: 'PART_OLDCODE',style: 'text-align:center'},
        {header: "配件名称", width: '10%', dataIndex: 'PART_CNAME',style: 'text-align:center'},
        {header: "件号", width: '10%', dataIndex: 'PART_CODE',style: 'text-align:center'},
        //{header: "供应商编码", dataIndex: 'VENDER_CODE',style: 'text-align:center'},
        //{header: "供应商名称", dataIndex: 'VENDER_NAME',style: 'text-align:center'},
        {header: "制造商编码", width: '10%', dataIndex: 'MAKER_CODE',style: 'text-align:center'},
        {header: "制造商名称", width: '10%', dataIndex: 'MAKER_NAME',style: 'text-align:center'},
//         {header: "是否默认", width: '10%', dataIndex: 'IS_DEFAULT',style: 'text-align:center',renderer: getItemValue},
        {header: "是否有效", width: '10%', dataIndex: 'STATE',style: 'text-align:center', renderer: getItemValue}
    ];
    __extQuery__(1);
}

//插入计划价格文本框
function insertPlanInput(value, meta, record) {

    var output = '<input type="text" class="phone_txt" readonly  style=" background-color:#FF9;text-align:right"  blur="checkPrice(this)" maxlength="18" id="PLAN_PRICE' + record.data.PRICE_ID + '" name="PLAN_PRICE" value="' + value + '" size ="10" />\n';
    return output;
}

//插入采购价格文本框
function insertInput(value, meta, record) {

    var output = '<input type="text" class="phone_txt" style=" background-color:#FF9;text-align:right"  onkeyup="checkPrice(this)" maxlength="18" id="BUY_PRICE' + record.data.PRICE_ID + '" name="BUY_PRICE" value="' + value + '" size ="10" style="background-color:#FF9"/>\n';
    return output;
}
//验证最大长度指定的正小数,inputObj为input对象，beforeLength为小数点前面的位数个数，afterLength为小数点后面的位数个数
/*function checkNumberLength(inputObj, beforeLength, afterLength) {
 if (inputObj.value.indexOf(".") >= 0) {
 //var regex = new RegExp("/^[0-9]{0," + beforeLength + "}.[0-9]{0," + afterLength + "}$/");  +\.[0-9]{2})[0-9]*
 var regex = new RegExp("/^[0-9]{0,10}.\\d{0,7}$/");
 if(regex.test(inputObj.value)==true){
 MyAlert("请录入正确的采购金额，且小数保留精度最大为"+afterLength+"位!");
 inputObj.value="";
 inputObj.focus();
 }else {
 var re = /([0-9]+\.[0-9]{7})[0-9]$/;
 inputObj.value=  inputObj.value.replace(re,"$1");
 var regex = new RegExp("^[0-9]{0," + beforeLength + "}.[0-9]{0," + afterLength + "}$");
 if(regex.test(inputObj.value)==false){
 MyAlert("整数部分不能超过"+beforeLength+"位!");
 inputObj.value="";
 inputObj.focus();
 }
 }
 }
 else {
 var regex = new RegExp("^[0-9]{0," + beforeLength + "}$");
 if(regex.test(inputObj.value)==false){
 MyAlert("只能是数字，且纯数字不能超过"+beforeLength+"位!");
 inputObj.value="";
 inputObj.focus();
 }else {

 }
 }
 }*/


function checkPrice(obj) {
    if (obj.value == "") {
        MyAlert("价格不能为空!");
        return;
    }
    var patrn = /^((0\.[0-9]*[1-9][0-9]*)|([1-9][0-9]*\.[0-9]+)|([1-9][0-9]*))$/;
    if (!patrn.exec(obj.value)) {
        MyAlert("价格无效,请重新输入!");
        obj.value = "";
        return;
    } else {
        if (obj.value.indexOf(".") >= 0) {
            var patrn = /^[0-9]{0,10}.[0-9]{0,7}$/;
            if (!patrn.exec(obj.value)) {
                MyAlert("价格整数部分不能超过10位,且保留精度最大为7位!");
                obj.value = "";
                return;
            }
        } else {
            var patrn = /^[0-9]{0,10}$/;
            if (!patrn.exec(obj.value)) {
                MyAlert("价格整数部分不能超过10位!");
                obj.value = "";
                return;
            }
        }
    }
}

//生成下拉框
function getItemValueWithSelect(value, meta, record) {
    var str = genSelBoxStrExp("IS_GUARD" + record.data.PRICE_ID, <%=Constant.IS_GUARD%>, value, false, "", "", false, "");
    return str;
}
//新增
function partBuyPriceAdd() {
    window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/addPartBuyPriceInit.do';
}

//设置默认供应商最小订货量
function formod(parms){
	btnDisable();
	document.getElementById("partId").value = parms;
	fm.action ="<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/partVenderMakerInit.do";
	fm.submit();
}

//保存
function save(buyPriceId, partId) {
    var curPage = myPage.page;
    document.getElementById("curPage").value = curPage;
    document.getElementById("partId").value = partId;
    var buyPrice = document.getElementById("BUY_PRICE" + buyPriceId).value;
    if (buyPrice == null || buyPrice == "") {
        MyAlert("采购价格不能为空!");
        document.getElementById("BUY_PRICE" + buyPriceId).focus();
        return;
    }
    var isGuard = document.getElementById("IS_GUARD" + buyPriceId).value;
//     MyConfirm("确定保存?", saveRecord, [buyPriceId,buyPrice,isGuard], null, null, 2);
    MyConfirm("确定保存?", saveRecord, [buyPriceId,buyPrice,isGuard]);
}

// 保存记录
function saveRecord(buyPriceId,buyPrice,isGuard){
    btnDisable();
    var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/updatePartBuyPrice.json';
    url += '?buyPriceId=' + buyPriceId;
    url += '&buyPrice=' + buyPrice;
    url += '&isGuard=' + isGuard;
    url += '&curPage=' + myPage.page;
    makeNomalFormCall(url, handleControl, 'fm');
}

//失效/有效
function changeState(buyPriceId, partId, type) {
	document.getElementById("partId").value = partId;
    var buyPrice = document.getElementById("BUY_PRICE" + buyPriceId).value;
    if (buyPrice == null || buyPrice == "") {
    	MyAlert("采购价格不能为空!");
        document.getElementById("BUY_PRICE" + buyPriceId).focus();
        return;
    }
    var isGuard = document.getElementById("IS_GUARD" + buyPriceId).value;

    var msg = "";
    //  失效
    if(type==1){
    	msg = "确定要设置为无效?";
    }else if(type == 2){
    	msg = "确定要设置为有效?";
    }
    if(msg != ""){
// 	    MyConfirm(msg, validRecord, [buyPriceId,buyPrice,isGuard, type], null, null, 2);
	    MyConfirm(msg, validRecord, [buyPriceId,buyPrice,isGuard, type]);
    }
}
// 失效/有效
function validRecord(buyPriceId,buyPrice,isGuard, type){
	btnDisable();
	var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/';
    if(type==1){
    	url += 'celPartBuyPrice.json';
    }else if(type == 2){
    	url += 'selPartBuyPrice.json';
    }
    url += '?buyPriceId=' + buyPriceId;
    url += '&buyPrice=' + buyPrice;
    url += '&isGuard=' + isGuard;
    url += '&curPage=' + myPage.page;
	makeNomalFormCall(url, handleControl, 'fm');
}
// 修改记录后返回调用
function handleControl(jsonObj) {
	btnEnable();
    if (jsonObj != null) {
        var success = jsonObj.success;
        MyAlert(success);
        __extQuery__(jsonObj.curPage);
    }
}

//下载模板
function exportBuyPriceTemplate() {
    fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/exportBuyPriceTemplate.do";
    fm.submit();
}

//下载采购价格数据
function exportPartBuyPriceExcel() {
    fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/exportPartBuyPriceExcel.do";
    fm.target = "_self";
    fm.submit();
}

function exporMaker()
{
	fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/exportMakersExcel.do";
    fm.target = "_self";
    fm.submit();
}

//通过excel导入采购价格
function uploadExcel() {
    var fileValue = document.getElementById("uploadFile").value;

    if (fileValue == "") {
        MyAlert("请选择文件!");
        return;
    }
    var fi = fileValue.substring(fileValue.length - 3, fileValue.length);
    if (fi != 'xls') {
        MyAlert('导入文件格式不对,请导入xls文件格式');
        return false;
    }
    
    MyConfirm("确认上传选择的文件?确定后请耐心等待...",confirmResult);
}
function confirmResult(){
	disableAllStartBtn();
	fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/uploadBuyPriceExcel.do";
    fm.submit();
}

function showUpload() {
    if ($("#uploadTable")[0].style.display == "none") {
        $("#uploadTable")[0].style.display = "block";
    } else {
        $("#uploadTable")[0].style.display = "none";
    }
}

function disableAllStartBtn(){
	var inputArr = document.getElementsByTagName("input");
	for(var i=0;i<inputArr.length;i++){
		if(inputArr[i].type=="button"){
			inputArr[i].disabled=true;
		}
	}
}

$(document).ready(function(){
	__extQuery__(1);
});

</script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 配件采购价格维护</div>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input type="hidden" name="curPage" id="curPage"/>
    <input type="hidden" name="partId" id="partId"/>
    <input type="hidden" name="buyPriceId" id="buyPriceId"/>
    <div class="form-panel">
	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
    <div class="form-body">
    <table class="table_query">
        <tr>
            <td class="right">配件编码：</td>
            <td><input class="middle_txt" type="text" name="PART_OLDCODE"/></td>
            <td class="right">配件名称：</td>
            <td><input class="middle_txt" type="text" name="PART_CNAME"/></td>
            <td class="right">件号：</td>
            <td><input class="middle_txt" type="text" name="PART_CODE"/></td>
        </tr>
        <tr>
            <td class="right">供应商编码：</td>
            <td><input class="middle_txt" type="text" name="VENDER_CODE"/></td>
            <td class="right">供应商名称：</td>
            <td><input class="middle_txt" type="text" name="VENDER_NAME"/></td>
            <td class="right">是否有效：</td>
            <td>
                <script type="text/javascript">
                    genSelBox("STATE", <%=Constant.STATUS %>, "<%=Constant.STATUS_ENABLE %>", true, "", "");
                </script>
            </td>
        </tr>
        <tr>
            <td class="right">是否暂估：</td>
            <td colspan="3">
                <script type="text/javascript">
                genSelBox("IS_GUARD", <%=Constant.IS_GUARD %>, "", true, "", "");
                </script>
            </td>
        </tr>
        <tr>
            <td class="center" colspan="6">
                <input type="button" id="queryBtn" name="BtnQuery" class="u-button" value="查 询" onclick="query();"/>
                <input type="reset" class="u-button" value="重 置"/>
                <input type="button" class="u-button" value="新 增" onclick="partBuyPriceAdd();"/>
                <input name="RecQuery" id="RecQuery" class="u-button" type="button" value="变更记录" onclick="updateRecord();"/>
                <input name="expButton" id="expButton" class="u-button" type="button" value="导 出" onclick="exportPartBuyPriceExcel();"/>
                <input name="BtnUpload" id="BtnUpload" class="u-button" type="button" value="批量导入" onclick="showUpload();"/>
            </td>
        </tr>
    </table>
    </div>
    </div>
    
    <table class="table_edit" id="uploadTable" style="display: none; ">
        <tr>
            <td>
            	<font color="red">
	                <input type="button" class="normal_btn" value="模版下载" onclick="exportBuyPriceTemplate()"/>
	               	文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;
               	</font>
                <input type="file" name="uploadFile" style="width: 250px" id="uploadFile" value=""/>
                &nbsp;
                <input type="button" id="upbtn" class="normal_btn" value="确定" onclick="uploadExcel()"/>
            </td>
        </tr>
    </table>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>
</div>
</body>
</html>